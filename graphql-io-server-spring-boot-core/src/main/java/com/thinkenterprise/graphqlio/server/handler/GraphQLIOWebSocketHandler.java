package com.thinkenterprise.graphqlio.server.handler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.thinkenterprise.graphqlio.server.GraphQLIOContext;
import com.thinkenterprise.graphqlio.server.converter.GraphQLIOFrameToRequestMessageConverter;
import com.thinkenterprise.graphqlio.server.converter.GraphQLIONotifierMessageToFrameConverter;
import com.thinkenterprise.graphqlio.server.converter.GraphQLIOResponseMessageToFrameConverter;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOConnection;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessageType;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOScope;
import com.thinkenterprise.graphqlio.server.evaluation.GraphQLIOEvaluation;
import com.thinkenterprise.graphqlio.server.execution.GraphQLIOExecutionStrategy;
import com.thinkenterprise.graphqlio.server.schema.GraphQLIOSchemaProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.DataItem;

public class GraphQLIOWebSocketHandler extends AbstractWebSocketHandler {

	private final Logger logger = LoggerFactory.getLogger(GraphQLIOWebSocketHandler.class);

	private Map<String, GraphQLIOConnection> webSocketConnections = new ConcurrentHashMap<String, GraphQLIOConnection>();
	private Map<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<String, WebSocketSession>();

	@Autowired
	private GraphQLIOSchemaProvider graphQLIOSchemaProvider;

	@Autowired
	private GraphQLIOFrameToRequestMessageConverter requestConverter;

	@Autowired
	private GraphQLIOResponseMessageToFrameConverter responseConverter;

	private GraphQLIONotifierMessageToFrameConverter notifyerConverter;

	@Autowired
	private GraphQLIOExecutionStrategy graphQLIOQueryExecutionStrategy;

	@Autowired
	private GraphQLIOEvaluation graphQLIOEvaluation;

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {

		List<DataItem> dataItems = CborDecoder.decode(message.getPayload().array());

		System.out.print(dataItems);

	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		logger.info("GraphQLIO Handler received graphqlio message :" + message.getPayload());
		logger.info("GraphQLIO Handler session :" + session);
		logger.info("GraphQLIO Handler session ID :" + session.getId());
		logger.info("GraphQLIO Handler this :" + this);
		logger.info("GraphQLIO Handler Thread :" + Thread.currentThread());

		// Convert Frame to Message
		GraphQLIOMessage requestMessage = requestConverter.convert(message.getPayload());

		// Get the Connection, create a Scope and push it to the context
		GraphQLIOConnection connection = webSocketConnections.get(session.getId());
		GraphQLIOScope scope = GraphQLIOScope.builder().withQuery(requestMessage.getData()).build();
		connection.addScope(scope);

		// Create Context Infromation for Execution
		GraphQLIOContext graphQLIOContext = GraphQLIOContext.builder().webSocketSession(session)
				.graphQLSchema(graphQLIOSchemaProvider.getGraphQLSchema()).requestMessage(requestMessage).scope(scope)
				.build();

		// Execute Message
		graphQLIOQueryExecutionStrategy.execute(graphQLIOContext);

		// Convert Result Message to Frame
		String frame = responseConverter.convert(graphQLIOContext.getResponseMessage());

		// Send back
		session.sendMessage(new TextMessage(frame));

		// Evaluate Subscriptions and notify clients
		List<String> sids = graphQLIOEvaluation.evaluateOutdatedSids(graphQLIOContext.getScope());
		Map<String, Set<String>> sids4cid = graphQLIOEvaluation.evaluateOutdatedsSidsPerCid(sids,
				webSocketConnections.values());
		sendNotifierMessageToClients(sids4cid, requestMessage);

	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		webSocketConnections.put(session.getId(), GraphQLIOConnection.builder().fromSession(session).build());
		webSocketSessions.put(session.getId(), session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		webSocketConnections.remove(session.getId());
		webSocketSessions.remove(session.getId());
	}

	private void sendNotifierMessageToClients(Map<String, Set<String>> sids4cid, GraphQLIOMessage requestMessage) throws Exception {
		
		Set<String> cids = sids4cid.keySet();
		
		for (String cid : cids) {
			GraphQLIOMessage message = GraphQLIOMessage.builder()
													   .fid(requestMessage.getFid())
													   .rid(requestMessage.getRid())
													   .type(GraphQLIOMessageType.GRAPQLNOTIFIER)
													   .data(notifyerConverter.createData(sids4cid.get(cid))).build();
			String frame = notifyerConverter.convert(message);
			webSocketSessions.get(cid).sendMessage(new TextMessage(frame));;

		}
	
	}

}
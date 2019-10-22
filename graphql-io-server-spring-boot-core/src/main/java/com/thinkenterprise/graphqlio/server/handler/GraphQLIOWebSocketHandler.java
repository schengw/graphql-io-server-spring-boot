package com.thinkenterprise.graphqlio.server.handler;

import java.util.List;

import com.thinkenterprise.graphqlio.server.GraphQLIOContext;
import com.thinkenterprise.graphqlio.server.converter.GraphQLIOMessage2FrameConverter;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;
import com.thinkenterprise.graphqlio.server.execution.GraphQLIOQueryExecutionStrategy;
import com.thinkenterprise.graphqlio.server.schema.GraphQLIOSchemaProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.DataItem;


public class GraphQLIOWebSocketHandler extends AbstractWebSocketHandler {
  
	private final Logger logger = LoggerFactory.getLogger(GraphQLIOWebSocketHandler.class);
	
	@Autowired
	private GraphQLIOSchemaProvider graphQLIOSchemaProvider;

	@Autowired
	private GraphQLIOMessage2FrameConverter graphQLIOSimpleMessage2FrameConverter;

	@Autowired
	private GraphQLIOQueryExecutionStrategy graphQLIOQueryExecutionStrategy; 

	
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

		// Convert Frame to Message 
		GraphQLIOMessage requestMessage = graphQLIOSimpleMessage2FrameConverter.from(message.getPayload());
		
		// Create Context Infromation for Execution 
		GraphQLIOContext graphQLIOContext = GraphQLIOContext.builder()
															.webSocketSession(session)
															.graphQLSchema(graphQLIOSchemaProvider.getGraphQLSchema())
															.requestMessage(requestMessage).build();

		// Execute Message
		graphQLIOQueryExecutionStrategy.execute(graphQLIOContext);

		// Convert Result Message to Frame 
		String frame = graphQLIOSimpleMessage2FrameConverter.to(graphQLIOContext.getResponseMessage());
		
		// Send back 
		session.sendMessage(new TextMessage(frame));
		
	}


}
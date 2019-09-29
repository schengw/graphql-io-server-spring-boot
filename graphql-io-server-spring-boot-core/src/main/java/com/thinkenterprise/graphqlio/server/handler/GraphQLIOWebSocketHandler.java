package com.thinkenterprise.graphqlio.server.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.thinkenterprise.graphqlio.server.converter.GraphQLIORequestMessageConverter;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;
import com.thinkenterprise.graphqlio.server.schema.GraphQLIOSchemaCreator;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.DataItem;
import graphql.schema.GraphQLSchema;


@Component
public class GraphQLIOWebSocketHandler extends AbstractWebSocketHandler {
  
	private final Logger logger = LoggerFactory.getLogger(GraphQLIOWebSocketHandler.class);
	
	@Autowired
	private GraphQLIOSchemaCreator graphQLIOSchemaCreator;
	private GraphQLSchema graphQLSchema;
	
    @Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
    	
    	List<DataItem> dataItems = CborDecoder.decode(message.getPayload().array());
    	System.out.print(dataItems);
    	
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {		
		
		GraphQLIOMessage graphQLIOMessage = new GraphQLIORequestMessageConverter().convert(message.getPayload());
		
		
		
		logger.info("GraphQLIO Handler received graphqlio message :" + graphQLIOMessage.toString());
		
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		// How can we test Web Sockets 
		// Test Test Builder Process 
		// Introduce the GraphQLQueryExecution Strategie 
		
		this.graphQLSchema=graphQLIOSchemaCreator.create();
		super.afterConnectionEstablished(session);
	}
	
	

}
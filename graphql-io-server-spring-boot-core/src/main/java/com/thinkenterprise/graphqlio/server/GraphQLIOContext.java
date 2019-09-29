package com.thinkenterprise.graphqlio.server;

import org.springframework.web.socket.WebSocketSession;

public class GraphQLIOContext {
	
	final private WebSocketSession webSocketSession;
	final private GraphQLIORootObject graphQLIORootObject;
	
	
	public GraphQLIOContext(WebSocketSession webSocketSession, GraphQLIORootObject graphQLIORootObject) {
		super();
		this.webSocketSession = webSocketSession;
		this.graphQLIORootObject = graphQLIORootObject;
	}


	public WebSocketSession getWebSocketSession() {
		return webSocketSession;
	}


	public GraphQLIORootObject getGraphQLIORootObject() {
		return graphQLIORootObject;
	}

	
}

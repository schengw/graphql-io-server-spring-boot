package com.thinkenterprise.graphqlio.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class GraphQLIOWebSocketHandlerConfiguration implements WebSocketConfigurer {

	@Value("${com.thinkenterprise.graphqlio.server.endpoint}")
	private String endpoint = "/api/data/graph";
	
	GraphQLIOWebSocketHandler graphQLIOWebSocketHandler;
	
	@Autowired
	public GraphQLIOWebSocketHandlerConfiguration(GraphQLIOWebSocketHandler graphQLIOWebSocketHandler) {
		this.graphQLIOWebSocketHandler=graphQLIOWebSocketHandler;
	}
	
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {	
    	//registry.addHandler(new GraphQLIOWebSocketHandler(), endpoint);   
    	registry.addHandler(graphQLIOWebSocketHandler, endpoint);   
    }
    
}
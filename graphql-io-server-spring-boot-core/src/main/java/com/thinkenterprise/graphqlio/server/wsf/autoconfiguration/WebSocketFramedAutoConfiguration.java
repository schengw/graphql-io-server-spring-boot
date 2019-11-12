package com.thinkenterprise.graphqlio.server.wsf.autoconfiguration;

import com.thinkenterprise.graphqlio.server.wsf.socket.WebSocketFramed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableConfigurationProperties(WebSocketFramedProperties.class)
@EnableWebSocket
public class WebSocketFramedAutoConfiguration implements WebSocketConfigurer {

	@Autowired
	private WebSocketFramedProperties properties;

	@Autowired
	private WebSocketFramed handler;

	
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {	 
    	registry.addHandler(this.handler, properties.getEndpoint());   
	}
	
	@Bean
	@ConditionalOnMissingBean
	public WebSocketFramed graphQLIOWebSocketHandler() {
		return new WebSocketFramed();

	}
    
}
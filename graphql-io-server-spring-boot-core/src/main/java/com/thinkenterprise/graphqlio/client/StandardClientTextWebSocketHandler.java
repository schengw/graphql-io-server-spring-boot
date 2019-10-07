package com.thinkenterprise.graphqlio.client;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class StandardClientTextWebSocketHandler extends TextWebSocketHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("message received : " + message.getPayload());
	}
 
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("connection esablished  : " + session.getId());
	}

}

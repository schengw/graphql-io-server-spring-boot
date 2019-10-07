package com.thinkenterprise.graphqlio.server.test;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class StandardClientTextWebSocketHandler extends TextWebSocketHandler {

	private Boolean dataReceived = false;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("message received : " + message.getPayload());
		dataReceived=true;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("connection esablished  : " + session.getId());
	}

	public Boolean getDataReceived() {
		return this.dataReceived;
	}

}

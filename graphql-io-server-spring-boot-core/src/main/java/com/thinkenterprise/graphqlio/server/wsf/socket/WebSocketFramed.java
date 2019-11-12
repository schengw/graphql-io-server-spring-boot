package com.thinkenterprise.graphqlio.server.wsf.socket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.thinkenterprise.graphqlio.server.wsf.codec.Codec;
import com.thinkenterprise.graphqlio.server.wsf.converter.Converter;
import com.thinkenterprise.graphqlio.server.wsf.event.InboundFrameEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class WebSocketFramed extends AbstractWebSocketHandler implements ApplicationListener<InboundFrameEvent> {


	private Map<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<String, WebSocketSession>();

	private Codec codec;
	private Converter converter;
	private ApplicationEventPublisher emitter;

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		handleTextMessage(session, new TextMessage(codec.decode(message.getPayload().array())));
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		emitter.publishEvent(new InboundFrameEvent(converter.from(message.getPayload()), session.getId()));
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// Edgar : Set the right code
		if (session.getAcceptedProtocol() == "")
			codec = null;
		webSocketSessions.put(session.getId(), session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		webSocketSessions.remove(session.getId());
	}

	@Override
	public void onApplicationEvent(InboundFrameEvent event) {
		try {
			webSocketSessions.get(event.getCid()).sendMessage(new TextMessage(converter.to(event.getFrame())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
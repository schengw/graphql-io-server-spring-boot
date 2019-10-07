package com.thinkenterprise.graphqlio.server.test;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class TestGraphQLIOServer {

    @Test
    @DisabledIf("false")
    public void queryTest() throws InterruptedException, ExecutionException, IOException {


        StandardClientTextWebSocketHandler handler = new StandardClientTextWebSocketHandler();

        WebSocketClient webSocketClient = new StandardWebSocketClient();
		WebSocketSession webSocketSession = webSocketClient.doHandshake(handler, new WebSocketHttpHeaders(), URI.create("ws://127.0.0.1:8080/api/data/graph")).get();
        webSocketSession.sendMessage(new TextMessage("[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"{ routes { id } }\"}]"));
        while(handler.getDataReceived()==false)
		webSocketSession.close();
    }

    @Test
    public void subscriptionTest() throws InterruptedException, ExecutionException, IOException {


        StandardClientTextWebSocketHandler handler = new StandardClientTextWebSocketHandler();

        WebSocketClient webSocketClient = new StandardWebSocketClient();
		WebSocketSession webSocketSession = webSocketClient.doHandshake(handler, new WebSocketHttpHeaders(), URI.create("ws://127.0.0.1:8080/api/data/graph")).get();
        webSocketSession.sendMessage(new TextMessage("[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"{ _Subscription { subscribe } routes { id } }\"}]"));
        while(handler.getDataReceived()==false)
		webSocketSession.close();
    }



}
package com.thinkenterprise.graphqlio.client;

import java.net.URI;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

public class GraphQLIOClient {

    public static void main(String[] args)  {
        try {
            
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            WebSocketSession webSocketSession = webSocketClient.doHandshake(new StandardClientTextWebSocketHandler(), new WebSocketHttpHeaders(), URI.create("ws://127.0.0.1:8080/api/data/graph")).get();
            int i = 0;
            while(i++<10) {
              webSocketSession.sendMessage(new TextMessage("[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"{ routes { id } } \"}]"));
              Thread.sleep(5000);
            }
            webSocketSession.close(); 
        } catch (Exception e) {
            e.printStackTrace(); 
        }
     
    }


}
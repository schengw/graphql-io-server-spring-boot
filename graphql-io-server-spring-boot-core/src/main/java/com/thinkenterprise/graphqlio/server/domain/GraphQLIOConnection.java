package com.thinkenterprise.graphqlio.server.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketConnection
 */
public class GraphQLIOConnection {

    private String connectionId;
    private List<GraphQLIOScope> scopes = new ArrayList<>();

    public GraphQLIOConnection(Builder builder) {
        this.connectionId=builder.connectionId;
    }
    
    public String getConnectionId() {
        return this.connectionId;
    }

    public void addScope(GraphQLIOScope scope) {
        this.scopes.add(scope);
    }

    public List<GraphQLIOScope> scopes() {
        return this.scopes;
    }

    public static Builder builder() {
		return new Builder();
	}

    public static final class Builder {

        private String connectionId;
        private WebSocketSession session;
             
		private Builder() {
        }

		public Builder fromSession(WebSocketSession session) {
            this.session=session;
			return this;
        }
        
		public GraphQLIOConnection build() {
            this.connectionId=session.getId();
            return new GraphQLIOConnection(this);
		} 

	}



}
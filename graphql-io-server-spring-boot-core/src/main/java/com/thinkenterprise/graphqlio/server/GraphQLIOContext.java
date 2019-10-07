package com.thinkenterprise.graphqlio.server;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;

import org.springframework.web.socket.WebSocketSession;

import graphql.schema.GraphQLSchema;

public class GraphQLIOContext {

	private WebSocketSession webSocketSession;
	private GraphQLSchema graphQLSchema;
	private GraphQLIOMessage requestMessage;
	private GraphQLIOMessage responseMessage;

	private GraphQLIOContext(Builder builder) {
		this.webSocketSession=builder.webSocketSession;
		this.graphQLSchema=builder.graphQLSchema;
		this.requestMessage=builder.requestMessage;
		this.responseMessage=builder.responseMessage;
	}

	public WebSocketSession getWebSocketSession() {
		return webSocketSession;
	}

	public GraphQLSchema getGraphQLSchema() {
		return this.graphQLSchema;
	}

	public GraphQLIOMessage getResponseMessage() {
		return this.responseMessage;
	}

	public void setResponseMessage(GraphQLIOMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public GraphQLIOMessage getRequestMessage() {
		return this.requestMessage;
	}

	public void setRequestMessage(GraphQLIOMessage requestMessage) {
		this.requestMessage = requestMessage;
	}

	public static Builder builder() {
		return new Builder();
	} 

	public static final class Builder {

		private WebSocketSession webSocketSession;
		private GraphQLSchema graphQLSchema;
		private GraphQLIOMessage requestMessage;
		private GraphQLIOMessage responseMessage;

		private Builder() {

		}

		public Builder webSocketSession(WebSocketSession webSocketSession) {
			this.webSocketSession = webSocketSession;
			return this;
		}

		public Builder graphQLSchema(GraphQLSchema graphQLSchema) {
			this.graphQLSchema = graphQLSchema;
			return this;
		}

		public Builder requestMessage(GraphQLIOMessage requestMessage) {
			this.requestMessage = requestMessage;
			return this;
		}

		public Builder responseMessage(GraphQLIOMessage responseMessage) {
			this.responseMessage = responseMessage;
			return this;
		}

		public GraphQLIOContext build() {
			return new GraphQLIOContext(this);
		}

	}

}

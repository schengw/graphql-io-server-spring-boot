package com.thinkenterprise.graphqlio.server.domain;

public final class GraphQLIOMessage {
	
	private String fid;
	private String rid;
	private GraphQLIOMessageType type;
	private String data;
	
	private GraphQLIOMessage(Builder builder) {
		this.fid = builder.fid;
		this.rid = builder.rid;
		this.type = builder.type;
		this.data = builder.data;
	}

	public String getFid() {
		return fid;
	}
	
	public String getRid() {
		return rid;
	}

	public GraphQLIOMessageType getType() {
		return type;
	}

	public String getData() {
		return data;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private String fid;
		private String rid;
		private GraphQLIOMessageType type;
		private String data;

		private Builder() {
		}

		public Builder fromRequestMessage(GraphQLIOMessage message) {
			this.fid=message.getFid();

			Long ridValue = Long.decode(message.getRid());
			++ridValue;
			this.rid= ridValue.toString();

			this.type=GraphQLIOMessageType.GRAPQLRESPONSE;

			return this;
		}
		public Builder fid(String fid) {
			this.fid=fid;
			return this;
		}

		public Builder rid(String rid) {
			this.rid=rid;
			return this;
		}

		public Builder data(String data) {
			this.data=data;
			return this;
		}

		public Builder type(GraphQLIOMessageType type) {
			this.type=type;
			return this;
		}

		public GraphQLIOMessage build() {
			return new GraphQLIOMessage(this);
		} 


	}
	

}

package com.thinkenterprise.graphqlio.server.wbf.domain;

public final class Frame {

	private String fid;
	private String rid;
	private FrameType type;
	private String data;

	private Frame(Builder builder) {
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

	public FrameType getType() {
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
		private FrameType type;
		private String data;

		private Builder() {
		}

		public Builder fid(String fid) {
			this.fid = fid;
			return this;
		}

		public Builder rid(String rid) {
			this.rid = rid;
			return this;
		}

		public Builder data(String data) {
			this.data = data;
			return this;
		}

		public Builder type(FrameType type) {
			this.type = type;
			return this;
		}

		public Frame build() {
			return new Frame(this);
		} 


	}
	

}

package com.thinkenterprise.graphqlio.server.domain;

public class GraphQLIOMessage {
	
	private String fid;
	private String rid;
	private GraphQLIOMessageType type;
	private String data;
	

	public GraphQLIOMessage() {
		super();
	}	
	public GraphQLIOMessage(String fid, String rid, GraphQLIOMessageType type, String data) {
		super();
		this.fid = fid;
		this.rid = rid;
		this.type = type;
		this.data = data;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public GraphQLIOMessageType getType() {
		return type;
	}
	public void setType(GraphQLIOMessageType type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	

}

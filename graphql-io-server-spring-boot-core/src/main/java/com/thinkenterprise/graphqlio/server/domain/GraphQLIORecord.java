package com.thinkenterprise.graphqlio.server.domain;

public class GraphQLIORecord {

    String srcType;
    String srcId;
    String srcAttr;
    String op;
    String arity;
    String dstType;
    String dstIds;
    String dstAttrs;

    // Introduce Setter & Getter 
    // Introduce Builder Pattern 
    // Introduce GraphQLIOOperationType 
    // Introduce GraphQLIOArityType 

    public GraphQLIORecord(Builder builder) {
    }

    public static Builder builder() {
		return new Builder();
	}

    public static final class Builder {
             
		private Builder() {
        }

	
		public GraphQLIORecord build() {
			return new GraphQLIORecord(this);
		} 

	}

}

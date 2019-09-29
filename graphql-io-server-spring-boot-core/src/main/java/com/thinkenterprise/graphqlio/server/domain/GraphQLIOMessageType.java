package com.thinkenterprise.graphqlio.server.domain;

public enum GraphQLIOMessageType {
	GRAPQLREQUEST {
		@Override
        public String toString() {
            return "GRAPHQL-REQUEST";
        }
    }
}

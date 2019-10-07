package com.thinkenterprise.graphqlio.server.execution;

import com.thinkenterprise.graphqlio.server.GraphQLIOContext;

public interface GraphQLIOQueryExecutionStrategy {
	
	void execute(GraphQLIOContext graphQLIOContext); 

}

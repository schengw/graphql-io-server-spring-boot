package com.thinkenterprise.graphqlio.server.execution;

import com.thinkenterprise.graphqlio.server.GraphQLIOContext;

public interface GraphQLIOExecutionStrategy {
	
	void execute(GraphQLIOContext graphQLIOContext); 

}

package com.thinkenterprise.graphqlio.server.execution;

import com.thinkenterprise.graphqlio.server.GraphQLIOContext;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;

public class DefaultGraphQLIOQueryExecutionStrategie implements GraphQLIOQueryExecutionStrategy{

	@Override
	public String execute(String query, GraphQLIOContext graphQLIOContext) {
	
		
		GraphQL graphQL = GraphQL.newGraphQL(null).build();
		ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(query)
																          .context(graphQLIOContext)
																          .operationName("")
																          .root(graphQLIOContext.getGraphQLIORootObject().getRootObject())
																          .variables(null)
																          .build();
		
		
		//ExecutionResult executionResult = graphQL.execute(new ExecutionInput(query, operationName, context, rootObject, transformVariables(schema, query, variables)));
		
		return null;
		
		
		
	}

}

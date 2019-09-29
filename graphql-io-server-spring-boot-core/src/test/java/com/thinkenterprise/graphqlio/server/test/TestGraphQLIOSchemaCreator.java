package com.thinkenterprise.graphqlio.server.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.thinkenterprise.graphqlio.server.schema.GraphQLIOSchemaCreator;

import graphql.schema.GraphQLSchema;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGraphQLIOSchemaCreator {

	@Autowired
	private GraphQLIOSchemaCreator graphQLIOSchemaCreator;
	
	
	@Test
	public void create() {
		
		GraphQLSchema graphQLSchema =  graphQLIOSchemaCreator.create();
		Assert.assertNotNull(graphQLSchema);
		
	}
	
	
	
	
}

package com.thinkenterprise.graphqlio.server.test;

import org.junit.Assert;
import org.junit.Test;

import com.thinkenterprise.graphqlio.server.converter.GraphQLIOMessageConverter;
import com.thinkenterprise.graphqlio.server.converter.GraphQLIORequestMessageConverter;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;

public class TestGrapQLIOMessageConverter {

	
	private GraphQLIOMessageConverter graphQLIOMessageConverter = new GraphQLIORequestMessageConverter();
	
	@Test
	public void graphQLIOMessageConverter() {
		
		String inputMessage = "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"{ hello } \"}]";
		GraphQLIOMessage graphQLIOMessage = graphQLIOMessageConverter.convert(inputMessage);
		Assert.assertTrue(graphQLIOMessage.getRid().equals("0"));
		
		
	}
	
	
}

package com.thinkenterprise.graphqlio.server.test;

import com.thinkenterprise.graphqlio.server.converter.GraphQLIOFrameToRequestMessageConverter;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGrapQLIOMessage2FrameConverter {

	@Autowired
	private GraphQLIOFrameToRequestMessageConverter graphQLIOMessageConverter;
	
	@Test
	public void graphQLIOMessageConverter() {
		
		String inputMessage = "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"{ routes { id } }\"}]";
		GraphQLIOMessage graphQLIOMessage = graphQLIOMessageConverter.convert(inputMessage);
		Assert.assertTrue(graphQLIOMessage.getRid().equals("0"));
		
		
	}
	
	
}

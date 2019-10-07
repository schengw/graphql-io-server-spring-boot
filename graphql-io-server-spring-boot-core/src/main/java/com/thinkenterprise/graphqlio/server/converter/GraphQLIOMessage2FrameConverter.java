package com.thinkenterprise.graphqlio.server.converter;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;

public interface GraphQLIOMessage2FrameConverter {
	
	GraphQLIOMessage from(String frame);
	String to(GraphQLIOMessage message);

}

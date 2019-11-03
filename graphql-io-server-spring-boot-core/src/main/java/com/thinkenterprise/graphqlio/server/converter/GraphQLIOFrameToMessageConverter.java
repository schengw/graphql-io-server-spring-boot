package com.thinkenterprise.graphqlio.server.converter;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;

public interface GraphQLIOFrameToMessageConverter {
	
	GraphQLIOMessage convert(String frame);

}

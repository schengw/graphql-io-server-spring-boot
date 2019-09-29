package com.thinkenterprise.graphqlio.server.converter;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;

public interface GraphQLIOMessageConverter {
	
	GraphQLIOMessage convert(String message);

}

package com.thinkenterprise.graphqlio.server.converter;

import org.springframework.util.StringUtils;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessageType;
import com.thinkenterprise.graphqlio.server.exception.GraphQLIOException;

public class GraphQLIORequestMessageConverter implements GraphQLIOMessageConverter{

	@Override
	public GraphQLIOMessage convert(String message) {
		
		message = StringUtils.deleteAny(message, "[");
		message = StringUtils.deleteAny(message, "]");
		
		String[] messageValues = StringUtils.tokenizeToStringArray(message, ",");
		
		if(messageValues.length!=4)
			throw new GraphQLIOException();
		
		messageValues[2] = StringUtils.deleteAny(messageValues[2], "\"");
		
		GraphQLIOMessageType graphQLIOMessageType = GraphQLIOMessageType.valueOf(GraphQLIOMessageType.GRAPQLREQUEST.name());
		
		
		if(!messageValues[2].equals(graphQLIOMessageType.toString()))
			throw new GraphQLIOException();
	
		return new GraphQLIOMessage(messageValues[0],messageValues[1],GraphQLIOMessageType.GRAPQLREQUEST,messageValues[3]);
	}

}

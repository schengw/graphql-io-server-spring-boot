package com.thinkenterprise.graphqlio.server.converter;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessageType;
import com.thinkenterprise.graphqlio.server.exception.GraphQLIOException;

public class GraphQLIOResponseMessageToFrameConverter implements GraphQLIOMessageToFrameConverter {

	@Override
	public String convert(GraphQLIOMessage message) {
 
		if(message.getType()!=GraphQLIOMessageType.GRAPQLRESPONSE)
			throw new GraphQLIOException();

		// Create Frame from Response Message
		String frame = "[" + message.getFid() + "," + message.getRid() + "," + "\""+ GraphQLIOMessageType.GRAPQLRESPONSE + "\"" + "," + message.getData() + "]";
		return frame;
	}

}

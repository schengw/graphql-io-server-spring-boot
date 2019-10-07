package com.thinkenterprise.graphqlio.server.converter;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessageType;
import com.thinkenterprise.graphqlio.server.exception.GraphQLIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class GraphQLIOSimpleMessage2FrameConverter implements GraphQLIOMessage2FrameConverter {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public GraphQLIOMessage from(String message) {

		String fid;
		String rid;
		GraphQLIOMessageType type;
		String data;

		// Delete not nedded characters 
		message = StringUtils.deleteAny(message, "[");
		message = StringUtils.deleteAny(message, "]");

		// Tokenize String 
		String[] messageValues = StringUtils.tokenizeToStringArray(message, ",");

		// Check count of values 
		if (messageValues.length != 4)
			throw new GraphQLIOException();

		// Delete excape characters for double quotas 
		// ToDo: not 2 insted 3 or?
		messageValues[2] = StringUtils.deleteAny(messageValues[2], "\"");

		// Actually we only convert the GRAPHQLREQUEST frame type  
		GraphQLIOMessageType graphQLIOMessageType = GraphQLIOMessageType
				.valueOf(GraphQLIOMessageType.GRAPQLREQUEST.name());

		// Check the right frame type 
		if (!messageValues[2].equals(graphQLIOMessageType.toString()))
			throw new GraphQLIOException();

		// Read the Query 
		Map<String, String> queryValue = new HashMap<>();
		String query;

		try {
			queryValue = objectMapper.readValue(messageValues[3], HashMap.class);
			query = queryValue.get("query");
		} catch (Exception e) {
			throw new GraphQLIOException();
		}
	
		// Set local variables more readable 
		fid=messageValues[0];
		rid=messageValues[1];
		type=graphQLIOMessageType;
		data=query;

		// Build Message  
		return  GraphQLIOMessage.builder().fid(fid).rid(rid).type(type).data(data).build();
	}

	@Override
	public String to(GraphQLIOMessage message) {

		// Currently the converter supports only one message type 
		if(message.getType()!=GraphQLIOMessageType.GRAPQLRESPONSE)
			throw new GraphQLIOException();

		// Create Frame for message Type 
		String frame = "[" + message.getFid() + "," + message.getRid() + "," + "\""+ GraphQLIOMessageType.GRAPQLRESPONSE + "\"" + "," + message.getData() + "]";
		return frame;
	}

}

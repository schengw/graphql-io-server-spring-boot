package com.thinkenterprise.graphqlio.server.converter;

import java.util.Set;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIOMessage;

/**
 * GraphQLIONotifierMessageToFrame
 */
public class GraphQLIONotifierMessageToFrameConverter implements GraphQLIOMessageToFrameConverter {

    @Override
    public String convert(GraphQLIOMessage message) {
        // ToDo : Build a Notifier Message Frame 
        // {"fid":2,"rid":0,"type":"GRAPHQL-NOTIFY","data":["5c989173-0eed-55b6-8f48-44890f621aaa"]}
        return null;
    }

	public String createData(Set<String> set) {
        // ToDo : Build Response Data with JSON - JSON-field: data, JSON-Data: Array of sids 
        // "data":["5c989173-0eed-55b6-8f48-44890f621aaa"]
		return null;
	}
    
}
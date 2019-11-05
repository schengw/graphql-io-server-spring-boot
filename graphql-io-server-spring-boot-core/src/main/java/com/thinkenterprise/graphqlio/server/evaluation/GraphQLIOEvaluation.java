package com.thinkenterprise.graphqlio.server.evaluation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIOConnection;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOScope;

/**
 * GraphQLIOEvaluation
 */
public class GraphQLIOEvaluation {


    public List<String> evaluateOutdatedSids(GraphQLIOScope scope) {
        // ToDo : Implementation of the Algorithm 
        // Description : Activity Diagramm 
        // Source : https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        //          __scopeProcess (scope)
        return null;
    }

    public Map<String, Set<String>> evaluateOutdatedsSidsPerCid(List<String> sids, Collection<GraphQLIOConnection> connections) {
        // ToDo : Implementation of the Algorithm 
        // Description : Iterate over all connections, Iterate over all scopes 
        //               If scope has the sid and the rigth status insert into the List ... 
        // Source : https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        //            __scopeOutdatedEvent (sids) {
        return null;
    }

    private boolean outdated(GraphQLIORecord oldRecord, GraphQLIORecord newRecord) {
        // ToDo : Implementation of the Algorithm 
        // Description : Edgar 
        // Source : https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        //            __scopeOutdatedEvent (sids) {
        return true;
    }

    private String recordStringify(GraphQLIORecord record) {
        // Description : Serialize/Serialize GraphQLIORecord to String according regular expression
    	//               "^(?:(.+?)#(.+?)\.(.+?)->)?(.+?)\((.+?)\)->(.+?)#\{(.*?)\}\.\{(.+?)\}$" 
        // Source : https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        //           __recordStringify {
    	
		return record.stringify();
    }

    private GraphQLIORecord recordUnstringify(String record) {
        // Description : Unserialize/Unstringfy String into GraphQLIORecord using regular expression
    	// 				 "^(?:(.+?)#(.+?)\.(.+?)->)?(.+?)\((.+?)\)->(.+?)#\{(.*?)\}\.\{(.+?)\}$" 
        // Source : https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        //            __recordUnstringify 
    	
    	return GraphQLIORecord.builder().stringified(record).build();
    }




}
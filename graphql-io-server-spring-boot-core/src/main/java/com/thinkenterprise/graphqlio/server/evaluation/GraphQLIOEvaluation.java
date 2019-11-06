package com.thinkenterprise.graphqlio.server.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord.GraphQLIOArityType;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord.GraphQLIOOperationType;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOConnection;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOScope;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOScopeState;
import com.thinkenterprise.graphqlio.server.keyvaluestore.KeyValStore;

/**
 * GraphQLIOEvaluation
 */
public class GraphQLIOEvaluation {

	@Autowired
	private KeyValStore keyval;

    public List<String> evaluateOutdatedSids(GraphQLIOScope scope) {
        // ToDo : Implementation of the Algorithm 
        // Description : Activity Diagramm 
        // Source : https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        //          __scopeProcess (scope)
    	
    	
        String scopeId = scope.getScopeId();
        String connectionId = scope.getConnectionId();
    	
    	HashMap<String, Boolean> outdatedSids = new HashMap<>();
 
        /*  filter out write records in the scope  */
        List<GraphQLIORecord> recordsWrite = 
        		scope.getRecords().stream().filter( r -> GraphQLIOOperationType.cud.contains(r.op())
        			).collect(Collectors.toList());
        
        /*  queries (scopes without writes)...  */
        if (recordsWrite.isEmpty()) {
        	
            if (scope.getScopeState() == GraphQLIOScopeState.SUBSCRIBED) {
                /*  ...with subscriptions are remembered  */
                String[] rec = this.keyval.getRecords(connectionId, scopeId);
                String[] recNew = scope.getStringifiedRecords().toArray(String[]::new);
                
                if (rec == null || rec.length == 0 || !Arrays.equals(rec, recNew)) {
                    this.keyval.putRecords(connectionId, scopeId, recNew);
//                    this.emit("debug", `scope-store-update sid=${sid} cid=${cid}`)
                }
            }
            else {
                /*  ...without subscriptions can be just destroyed
                    (and the processing short-circuited)  */
            	
//                scope.destroy();  ??? Todo: parent needs to destroy scope? 
            	this.keyval.delete(connectionId, scopeId);	/// delete scope records
            	
            }
        }
        /*  mutations (scopes with writes) might outdate queries (scopes with reads)  */
        else {

            /*  determine all stored scope records  */
           	HashMap<String, List<String>> sids = new HashMap<>();
        	Set<String> keys = this.keyval.getAllKeys();
        	keys.forEach(key -> {
        		String cid = KeyValStore.getConnectionId(key);
        		String sid = KeyValStore.getScopeId(key);
        		if (!sids.containsKey(sid)) {
        			List<String> cids = new ArrayList<>();
        			cids.add(cid);
        			sids.put(sid, cids);
        		}
        	});
        	
        	HashMap<String, Boolean> checkedRecords = new HashMap<>();   //??? key serialized String of records???? !!!!
        	
        	sids.keySet().forEach(sid -> {
        		if (sids.containsKey(sid)) {
        			
        			/// check just once
        			if (outdatedSids.containsKey(sid)  && !outdatedSids.get(sid).booleanValue())
        				return;
        			
            		sids.get(sid).forEach(cid -> {
            			
            			/// check just once
            			if (outdatedSids.containsKey(sid)  && !outdatedSids.get(sid).booleanValue())
            				return;
            			
            			boolean outdated = evaluateOutdatedSidPerCid( checkedRecords, recordsWrite, cid, sid);
            			if (outdated)
            				outdatedSids.put(sid, true);
             		});
        			
        		}
        	});
        }
        
        Set<String> set = outdatedSids.keySet();
        List<String> list = new ArrayList<>();
        list.addAll(set);
        return list;
    }
    
    
    /// may stored records for <scopeId, connectionId>
    /// if there are no more read records
    private boolean evaluateOutdatedSidPerCid( 	Map<String, Boolean> checkedRecords, /// passes HashMap in real
    									List<GraphQLIORecord> recordsWrite,
    									String connectionId, 
    									String scopeId) {
    	
        // fetch scope records value
        String[] strRecordsRead = this.keyval.getRecords(connectionId, scopeId);
        boolean recordsReadChecked = false; 
        String stringifiedRecordsRead = null;
        if (strRecordsRead != null  && strRecordsRead.length > 0 ) {
        	
        	StringBuilder sb = new StringBuilder();
        	for (String record: strRecordsRead) {
        		sb.append(record);
        	}
        	stringifiedRecordsRead = sb.toString();
        	recordsReadChecked = 
        			checkedRecords.containsKey(stringifiedRecordsRead) && 
            		checkedRecords.get(stringifiedRecordsRead).booleanValue();
        }
        
        if (!recordsReadChecked) {
        	
            if (strRecordsRead == null || strRecordsRead.length == 0) {
            	this.keyval.delete(connectionId, scopeId);
            }
            else {
            	List<GraphQLIORecord> recordsRead = new ArrayList<>();
            	for (String recordRead: strRecordsRead) {
            		recordsRead.add(GraphQLIORecord.builder().stringified(recordRead).build());
            	}
            	
                //  check whether writes outdate reads
                boolean outdated = GraphQLIOEvaluation.outdated(recordsWrite, recordsRead);

                //  remember that these scope records were already checked 
                if (stringifiedRecordsRead != null)
                	checkedRecords.put(stringifiedRecordsRead, true);

                if (outdated)
                	return true;
                
            }
        	
        }
    	    	
    	return false;
    }
    
    
    public Map<String, Set<String>> evaluateOutdatedsSidsPerCid(List<String> sids, Collection<GraphQLIOConnection> connections) {
        // ToDo : Implementation of the Algorithm 
        // Description : Iterate over all connections, Iterate over all scopes 
        //               If scope has the sid and the right status insert into the List ... 
        // Source : https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        //            __scopeOutdatedEvent (sids) {
        return null;
    }

	private static boolean outdated(List<GraphQLIORecord> newRecords, List<GraphQLIORecord> oldRecords) {

//		 iterate over all new and old records...

		for (GraphQLIORecord recNew : newRecords ) {
       	
	       	for (GraphQLIORecord recOld : oldRecords) {
	            boolean outdated = false;

               /*
                *  CASE 1: modified entity (of arbitrary direct access)
                *  old/query:    [*#{*}.*->]read(*)->Item#{1}.{id,name}
                *  new/mutation: [*#{*}.*->]update/delete(*)->Item#{1}.{name}
				*/
	            
	           if (GraphQLIOOperationType.ud.contains(recNew.op())
	                    && recOld.dstType().equals(recNew.dstType())
	                    && overlap(recOld.dstIds(),     recNew.dstIds())
	                    && overlap(recOld.dstAttrs(),   recNew.dstAttrs()))
                   outdated = true;

               /*
                *  CASE 2: modified entity list (of relationship traversal)
                *  old/query     Card#1.items->read(*)->Item#{2}.{id,name}
                *  new/mutation: [*#{*}.*->]update(*)->Card#{1}.{items}
                */

               else if (recNew.op() == GraphQLIOOperationType.UPDATE
	                    &&           recOld.srcType() != null
	                    &&           recOld.srcType().equals(recNew.dstType())
	                    && overlap( new String[] {recOld.srcId()} , recNew.dstIds())
	                    && overlap( new String[] {recOld.srcAttr()},  recNew.dstAttrs()))
               	outdated = true;

               /*
                *  CASE 3: modified entity list (of direct query)
                *  old/query     [*#{*}.*->]read(many/all)->Item#{*}.{id,name}
                *  new/mutation: [*#{*}.*->]create/update/delete(*)->Item#{*}.{name}
                */
               else if ( GraphQLIOOperationType.cud.contains(recNew.op())
                   &&    GraphQLIOArityType.multi.contains(recOld.arity())
                   &&         recOld.dstType().equals(recNew.dstType())
                   && overlap(recOld.dstAttrs(),   recNew.dstAttrs()))
                   outdated = true;

               /*  report outdate combination  */
               if (outdated) {
//                   this.emit("scope-outdated", { old: recordsOld[j], new: recordsNew[i] })
//                   let recOld = this.__recordStringify(recordsOld[j])
//                   let recNew = this.__recordStringify(recordsNew[i])
//                   this.emit("debug", `scope-outdated old=${recOld} new=${recNew}`)
                   return true;
               }
           }
       }

       /*  ...else the scope is still valid (not outdated)  */
       return false;
   }
      
   private static boolean overlap(String[] list1, String[] list2) {
       // Description : checks, if 2 lists contain overlapping strings  
       // Source : https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
       //            __scopeOutdatedEvent (sids) {
   	
       if (list1.length == 0 || list2.length == 0)
           return false;
       if (list1.length == 1 && list1[0].equals("*"))
           return true;
       if (list2.length == 1 && list2[0].equals("*"))
           return true;
               
       return Arrays.asList(list1).retainAll(new HashSet<String>(Arrays.asList(list2)));
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
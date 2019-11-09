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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
@Component
public class GraphQLIOEvaluation {

    @Autowired
    private KeyValStore keyval;

    private String recordStringify(GraphQLIORecord record) {
        // Description : Serialize/Serialize GraphQLIORecord to String according regular
        // expression
        // "^(?:(.+?)#(.+?)\.(.+?)->)?(.+?)\((.+?)\)->(.+?)#\{(.*?)\}\.\{(.+?)\}$"
        // Source :
        // https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        // __recordStringify {

        return record.stringify();
    }

    private GraphQLIORecord recordUnstringify(String record) {
        // Description : Unserialize/Unstringfy String into GraphQLIORecord using
        // regular expression
        // "^(?:(.+?)#(.+?)\.(.+?)->)?(.+?)\((.+?)\)->(.+?)#\{(.*?)\}\.\{(.+?)\}$"
        // Source :
        // https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        // __recordUnstringify

        return GraphQLIORecord.builder().stringified(record).build();
    }

    private static boolean outdated(List<GraphQLIORecord> newRecords, List<GraphQLIORecord> oldRecords) {

        // iterate over all new and old records...

        for (GraphQLIORecord recNew : newRecords) {

            for (GraphQLIORecord recOld : oldRecords) {
                boolean outdated = false;

                /*
                 * CASE 1: modified entity (of arbitrary direct access) old/query:
                 * [*#{*}.*->]read(*)->Item#{1}.{id,name} new/mutation:
                 * [*#{*}.*->]update/delete(*)->Item#{1}.{name}
                 */

                if ((recNew.op() == GraphQLIOOperationType.UPDATE  ||
                	 recNew.op() == GraphQLIOOperationType.DELETE   )   && 
                	recOld.dstType().equals(recNew.dstType()) 			&& 
                	overlap(recOld.dstIds(), recNew.dstIds()) 			&& 
                	overlap(recOld.dstAttrs(), recNew.dstAttrs()))
                    outdated = true;

                /*
                 * CASE 2: modified entity list (of relationship traversal) old/query
                 * Card#1.items->read(*)->Item#{2}.{id,name} new/mutation:
                 * [*#{*}.*->]update(*)->Card#{1}.{items}
                 */

                /// warum nicht Delete???
                else if (recNew.op() == GraphQLIOOperationType.UPDATE 				&& 
                		recOld.srcType() != null									&& 
                		recOld.srcType().equals(recNew.dstType())					&& 
                		overlap(new String[] { recOld.srcId() }, recNew.dstIds())	&& 
                		overlap(new String[] { recOld.srcAttr() }, recNew.dstAttrs()))
                    outdated = true;

                /*
                 * CASE 3: modified entity list (of direct query) old/query
                 * [*#{*}.*->]read(many/all)->Item#{*}.{id,name} new/mutation:
                 * [*#{*}.*->]create/update/delete(*)->Item#{*}.{name}
                 */
                else if ((recNew.op() == GraphQLIOOperationType.CREATE  ||
                		  recNew.op() == GraphQLIOOperationType.UPDATE  ||
                   	 	  recNew.op() == GraphQLIOOperationType.DELETE   )  && 
                		 (recOld.arity() == GraphQLIOArityType.MANY     ||
                		  recOld.arity() == GraphQLIOArityType.ALL       )	&& 
                		 recOld.dstType().equals(recNew.dstType()) 			&& 
                		 overlap(recOld.dstAttrs(), recNew.dstAttrs()))
                    outdated = true;

                /* report outdate combination */
                if (outdated) {
                    // this.emit("scope-outdated", { old: recordsOld[j], new: recordsNew[i] })
                    // let recOld = this.__recordStringify(recordsOld[j])
                    // let recNew = this.__recordStringify(recordsNew[i])
                    // this.emit("debug", `scope-outdated old=${recOld} new=${recNew}`)
                    return true;
                }
            }
        }

        /* ...else the scope is still valid (not outdated) */
        return false;
    }

    private static boolean overlap(String[] list1, String[] list2) {
        // Description : checks, if 2 lists contain overlapping strings
        // Source :
        // https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        // __scopeOutdatedEvent (sids) {

        if (list1.length == 0 || list2.length == 0)
            return false;
        if (list1.length == 1 && list1[0].equals("*"))
            return true;
        if (list2.length == 1 && list2[0].equals("*"))
            return true;

        List<String> listA = new ArrayList<>(Arrays.asList(list1));
        List<String> listB = new ArrayList<>(Arrays.asList(list2));

        Set<String> result = listA.stream()
        		  .distinct()
        		  .filter(listB::contains)
        		  .collect(Collectors.toSet());

        return result.size() > 0;
    }

    public List<String> evaluateOutdatedSids(GraphQLIOScope scope) {
        // ToDo : Implementation of the Algorithm
        // Description : Activity Diagramm
        // Source :
        // https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        // __scopeProcess (scope)

        String scopeId = scope.getScopeId();
        String connectionId = scope.getConnectionId();

        /// Contains for a HashSet is O(1) compared to O(n) for a list, 
        /// therefore we use hashset to check if a scope is outdated        
        HashMap<String, Boolean> outdatedSids = new HashMap<>();

        /* filter out write records in the scope */
        List<GraphQLIORecord> recordsWrite = scope.getRecords().stream()
                .filter(r -> (r.op() == GraphQLIOOperationType.CREATE  ||
                			  r.op() == GraphQLIOOperationType.UPDATE  ||
                			  r.op() == GraphQLIOOperationType.DELETE   ))
                			  .collect(Collectors.toList());

        
        
        
        /* Check Workflow: which component is responsible for storing scope records to KeyValueStore
         * 
         * according Ralf's implementation 
         *   this.keyval.putRecords(connectionId, scopeId, recNew);
         *   is only called if "recordWrite.isEmpty"
         *   why?
         *   
         *   if we have 2 scopes each with a read and write operation
         *   the "else case" "keyset" returns no keys as there was no putRecords call before 
         */
        
                
        /* queries (scopes without writes)... */        
        if (recordsWrite.isEmpty()) {

            if (scope.getScopeState() == GraphQLIOScopeState.SUBSCRIBED) {
                /* ...with subscriptions are remembered */
                String[] rec = this.keyval.getRecords(connectionId, scopeId);
                List<String> records = scope.getStringifiedRecords();
                String[] recNew = records.toArray(new String[records.size()]);

                if (rec == null || rec.length == 0 || !Arrays.equals(rec, recNew)) {
                    this.keyval.putRecords(connectionId, scopeId, recNew);
                    // this.emit("debug", `scope-store-update sid=${sid} cid=${cid}`)
                }
            } else {
                /*
                 * ...without subscriptions can be just destroyed (and the processing
                 * short-circuited)
                 */

                // scope.destroy(); ??? Todo: parent needs to destroy scope?
                this.keyval.delete(connectionId, scopeId); /// delete scope records

            }
        }
        /* mutations (scopes with writes) might outdate queries (scopes with reads) */
        else {
            /* determine all stored scope records */
            HashMap<String, List<String>> sids = new HashMap<>();
            Set<String> keys = this.keyval.getAllKeys();
            keys.forEach(key -> {
                // CR 11072019 We should discuss about the compound key
            	// coded analog RedisTemplate
                String cid = KeyValStore.getConnectionId(key);
                String sid = KeyValStore.getScopeId(key);

                // CR 11072019 We should discuss the Algorithm
                
                /// ToDo check relation between (sid,cid)
                if (!sids.containsKey(sid)) {
                    List<String> cids = new ArrayList<>();
                    cids.add(cid);
                    sids.put(sid, cids);
                }
            });

            // CR 11072019 We should discuss the Algorithm
            HashMap<String, Boolean> checkedRecords = new HashMap<>(); // ??? key serialized String of records???? !!!!

            sids.keySet().forEach(sid -> {
                /// check just once
                if (outdatedSids.containsKey(sid))
                    return;

                sids.get(sid).forEach(cid -> {

                    boolean outdated = evaluateOutdatedSidPerCid(checkedRecords, recordsWrite, cid, sid);
                    if (outdated)
                        outdatedSids.put(sid, true);
                });
            });
        }

        Set<String> set = outdatedSids.keySet();
        List<String> list = new ArrayList<>();
        list.addAll(set);
        return list;
    }

    // CR 11072019 What is this
    
    /// evaluate if scope is outdated for a particular connection
    private boolean evaluateOutdatedSidPerCid(Map<String, Boolean> checkedRecords, /// passes HashMap in real
            List<GraphQLIORecord> recordsWrite, String connectionId, String scopeId) {

        // fetch scope records value
        String[] strRecords = this.keyval.getRecords(connectionId, scopeId);
        boolean recordsChecked = false;
        String stringifiedRecords = null;
        /// should be discussed with Ralf if this is really necessary and saves performance
        if (strRecords != null && strRecords.length > 0) {

            StringBuilder sb = new StringBuilder();
            for (String record : strRecords) {
                sb.append(record);
            }
            stringifiedRecords = sb.toString();
            recordsChecked = checkedRecords.containsKey(stringifiedRecords);
        }

        if (!recordsChecked) {

            if (strRecords == null || strRecords.length == 0) {
                this.keyval.delete(connectionId, scopeId);
            } else {
                List<GraphQLIORecord> recordsRead = new ArrayList<>();
                for (String strRecord : strRecords) {
                	GraphQLIORecord record = GraphQLIORecord.builder().stringified(strRecord).build();
                	if ( record.op() ==  GraphQLIOOperationType.READ)
                		recordsRead.add(record);
                }

                // check whether writes outdate reads
                boolean outdated = GraphQLIOEvaluation.outdated(recordsWrite, recordsRead);

                // remember that these scope records were already checked
                if (stringifiedRecords != null)
                    checkedRecords.put(stringifiedRecords, true);

                if (outdated)
                    return true;

            }

        }

        return false;
    }

    // CR 11072019 This Algorithm is open 
    // Map<cid, <sids>>
	// method is called by GraphQLIOWebSocketHandler::handleTextMessage
    public Map<String, Set<String>> evaluateOutdatedsSidsPerCid(List<String> sids,
            Collection<GraphQLIOConnection> connections) {

    	Map<String, Set<String>> sidsPerCid = new HashMap<>();
    	
        Set<String> keys = this.keyval.getAllKeys();
        keys.forEach(key -> {
        	
            String cid = KeyValStore.getConnectionId(key);
            String sid = KeyValStore.getScopeId(key);

            if (sids.contains(sid)) {
            	if (!sidsPerCid.containsKey(cid)) {
            		Set<String> sidSet = new HashSet<>(); 
            		sidSet.add(sid);
            		sidsPerCid.put(cid,  sidSet);
            	}
            	else {
            		sidsPerCid.get(cid).add(sid);
            	}            	
            }
        	
        });
    	
        return sidsPerCid;
    }

    

}
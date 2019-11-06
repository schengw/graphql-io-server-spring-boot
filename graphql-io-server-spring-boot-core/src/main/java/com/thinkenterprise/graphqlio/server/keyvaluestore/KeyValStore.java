package com.thinkenterprise.graphqlio.server.keyvaluestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class KeyValStore {

	public KeyValStore() {		
	}
	
	private HashMap<String,String> hashMap = new  HashMap<>();
	
	/// key pattern "cid:*:sid:*
	/// value: string, e.g. serialized data structure

	private static String generateKey(String connectionId, String scopeId) {
		return "cid:" + connectionId+ ":sid:" + scopeId;
	}
	
	public static String getConnectionId(String key) {
		int indexCid =  key.indexOf("cid:") + 4;
		int indexSid = key.indexOf(":sid:") + 5;
		assert (indexCid == 4 &&  indexSid > 4);
		
		if (indexCid == 4 &&  indexSid > 4) {
			return key.substring(indexCid, indexSid);
		}
		else return null;
	}

	public static String getScopeId(String key) {
		int indexCid =  key.indexOf("cid:") + 4;
		int indexSid = key.indexOf(":sid:") + 5;
		assert (indexCid == 4 &&  indexSid >= 10);
		
		if (indexCid == 4 &&  indexSid >= 10) {
			return key.substring(indexSid);
		}
		else return null;
	}
	
	public void put(String connectionId, String scopeId, String value ) {
		hashMap.put(generateKey(connectionId, scopeId), value);
	}
	
	public void putRecords(String connectionId, String scopeId, String[] records) {
		String value = Integer.toString(records.length) + "@" + String.join(" ", records);
		hashMap.put(generateKey(connectionId, scopeId), value);		
	}

	public String get (String connectionId, String scopeId) {
        return hashMap.get(generateKey(connectionId, scopeId));		
	}

	public String[] getRecords(String connectionId, String scopeId) {
		String[] parts = hashMap.get(generateKey(connectionId, scopeId)).split("\\@");
		assert (parts.length == 2);
		if (parts.length == 2) {
			return parts[1].split(" ");
		}
		else
			return null;
	}

	public String delete (String connectionId, String scopeId) {
    	return hashMap.remove(generateKey(connectionId, scopeId));
	}

	public boolean hasKey (String connectionId, String scopeId) {
    	return hashMap.containsKey(generateKey(connectionId, scopeId));
	}

	public Set<String> getAllKeys()  {
		return hashMap.keySet();
	}	
	
	/// only fake; need real implementation when using Redis 
	public Set<String> getAllKeysForConnection(String connectionId)  {
		return hashMap.keySet();
	}
	
	/// only fake; need real implementation when using Redis 
	public Set<String> getAllKeysForScope(String scopeId)  {
		return hashMap.keySet();
	}
	
}


package com.thinkenterprise.graphqlio.server.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * GraphQLIOScope
 */
public class GraphQLIOScope {

    private String scopeId;
    private String connectionId = null;			//// default no connection `${this.uuid}:none`  ????
    private String query;
    private String variables;
    private GraphQLIOScopeState scopeState;
    private List<GraphQLIORecord> records = new ArrayList<>();

    public GraphQLIOScope(Builder builder) {
        this.scopeId=builder.scopeId;
        this.connectionId=builder.connectionId;
        this.scopeState=builder.scopeState;
        this.query=builder.query;
        this.variables=builder.variables;
        this.addRecord(GraphQLIORecord.builder().stringified(query).build());
    }

    public String getScopeId() {
        return this.scopeId;
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public GraphQLIOScopeState getScopeState() {
        return this.scopeState;
    }

    public List<GraphQLIORecord> getRecords() {
        return this.records;
    }
    
    public void addRecord(GraphQLIORecord record) {
        this.records.add(record);
    }

    public List<String> getStringifiedRecords() {
    	List<String> stringifiedRecords = new ArrayList<>(); 
    	this.records.forEach(r -> stringifiedRecords.add(r.stringify()));
    	return stringifiedRecords;
    }
    
    
    
    public static Builder builder() {
		return new Builder();
	}

    public static final class Builder {

        private String scopeId = null;
        private String connectionId = null;		//// default no connection `${this.uuid}:none`  ????
        private GraphQLIOScopeState scopeState = GraphQLIOScopeState.UNSUBSCRIBED;
        private String query;
        private String variables;
             
		private Builder() {
        }

		public Builder withQuery(String query) {
            this.query=query;
			return this;
        }
        public Builder withVariables(String variables) {
            this.variables=variables;
			return this;
		}

        public Builder withScopeId(String scopeId) {
            this.scopeId=scopeId;
			return this;
		}
        
        public Builder withConnectionId(String connectionId) {
            this.connectionId=connectionId;
			return this;
		}
        
        public Builder withState(GraphQLIOScopeState scopeState) {
        	this.scopeState = scopeState;
        	return this;
        }
		public GraphQLIOScope build() {
            // Build Process 
			
			if (this.scopeId == null) {
	            // scopeId = .... from query and variables and some other stuff 
				///           this.scopeId = this.variables + this.query;
	            this.scopeId = UUID.randomUUID().toString();
			}
			return new GraphQLIOScope(this);
		} 

	}
    
}
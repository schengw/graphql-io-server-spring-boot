package com.thinkenterprise.graphqlio.server.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * GraphQLIOScope
 */
public class GraphQLIOScope {

    private String scopeId;
    private GraphQLIOScopeState scopeState;
    private List<GraphQLIORecord> records = new ArrayList<>();

    public GraphQLIOScope(Builder builder) {
        this.scopeId=builder.scopeId;
        this.scopeState=builder.scopeState;
    }

    public String getScopeId() {
        return this.scopeId;
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

    public static Builder builder() {
		return new Builder();
	}

    public static final class Builder {

        private String scopeId;
        private GraphQLIOScopeState scopeState = GraphQLIOScopeState.UNSUBSCRIBED;
        private String query;
        private String variables;
             
		private Builder() {
        }

		public Builder withQuery(String query) {
            this.query=query;
			return this;
        }
        public Builder withVariable(String variables) {
            this.variables=variables;
			return this;
		}
        
		public GraphQLIOScope build() {
            // Build Process 
            // scopeId = .... from query and varaibles and some other stuff 
            this.scopeId = this.variables + this.query;
            this.scopeId = UUID.randomUUID().toString();
			return new GraphQLIOScope(this);
		} 

	}
    
}
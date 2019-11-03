package com.thinkenterprise.graphqlio.server.domain;

public enum GraphQLIOScopeState {
	SUBSCRIBED {
		@Override
        public String toString() {
            return "subscribed";
        }
    },
    UNSUBSCRIBED {
        @Override
        public String toString() {
            return "unsubscribed";
        }
    }
    
}

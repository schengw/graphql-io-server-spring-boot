package com.thinkenterprise.graphqlio.server.wsf.domain;

public enum FrameType {
	GRAPQLREQUEST {
		@Override
        public String toString() {
            return "GRAPHQL-REQUEST";
        }
    },
    GRAPQLRESPONSE {
        @Override
        public String toString() {
            return "GRAPHQL-RESPONSE";
        }
    },
    GRAPQLNOTIFIER {
        @Override
        public String toString() {
            return "GRAPHQL-NOTIFIER";
        }
    }
    
}

package com.thinkenterprise.graphqlio.server.wsf.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "graphqlio.server")
public class WebSocketFramedProperties {

    private String schemaLocationPattern;
    private String endpoint;
    
    public void setSchemaLocationPattern(String schemaLocationPattern) {
        this.schemaLocationPattern=schemaLocationPattern;
    }

    public String getSchemaLocationPattern() {
        return this.schemaLocationPattern;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint=endpoint;
    }


}
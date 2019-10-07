package com.thinkenterprise.graphqlio.server.schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import graphql.schema.GraphQLSchema;

@Component
public class GraphQLIOSchemaProvider implements CommandLineRunner {

    @Autowired
    private GraphQLIOSchemaCreator graphQLIOSchemaCreator;

    private GraphQLSchema graphQLSchema;

    @Override
    public void run(String... args) throws Exception {
        this.graphQLSchema = graphQLIOSchemaCreator.create();
    }

    public GraphQLSchema getGraphQLSchema() {
        return this.graphQLSchema;
    }

    
}
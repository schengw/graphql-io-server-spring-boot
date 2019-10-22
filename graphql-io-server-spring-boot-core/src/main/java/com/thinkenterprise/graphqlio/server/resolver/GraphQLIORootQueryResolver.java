package com.thinkenterprise.graphqlio.server.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import org.springframework.stereotype.Component;

@Component
public class GraphQLIORootQueryResolver implements GraphQLQueryResolver {

    public GraphQLIOSubscription _Subscription() {
        return new GraphQLIOSubscription();
    } 

    
}
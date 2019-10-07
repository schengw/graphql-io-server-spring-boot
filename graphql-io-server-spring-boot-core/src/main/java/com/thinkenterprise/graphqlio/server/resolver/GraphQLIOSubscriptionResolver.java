package com.thinkenterprise.graphqlio.server.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;

import org.springframework.stereotype.Component;

@Component
public class GraphQLIOSubscriptionResolver implements GraphQLResolver<GraphQLIOSubscription> {

    public String subscribe(GraphQLIOSubscription subscription) {
        return "UUID";
    }

}
package com.thinkenterprise.resolver;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thinkenterprise.domain.Route;
import com.thinkenterprise.domain.RouteRepository;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOScope;

import graphql.schema.DataFetchingEnvironment;


public class QueryResolver implements GraphQLQueryResolver {

	private RouteRepository routeRepository;
	
	public QueryResolver(RouteRepository routeRepository) {
		super();
		this.routeRepository=routeRepository;	
	}

	
	public List<Route> routes(DataFetchingEnvironment environment) {

		GraphQLIOScope scope = environment.getContext();
		scope.addRecord(GraphQLIORecord.builder().build());

		return routeRepository.findAll();
	} 
	
}

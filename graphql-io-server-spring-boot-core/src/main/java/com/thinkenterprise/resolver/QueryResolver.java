package com.thinkenterprise.resolver;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thinkenterprise.domain.Route;
import com.thinkenterprise.domain.RouteRepository;


public class QueryResolver implements GraphQLQueryResolver {

	private RouteRepository routeRepository;
	
	public QueryResolver(RouteRepository routeRepository) {
		super();
		this.routeRepository=routeRepository;	
	}

	
	public List<Route> routes() {
		return routeRepository.findAll();
	} 
	
}

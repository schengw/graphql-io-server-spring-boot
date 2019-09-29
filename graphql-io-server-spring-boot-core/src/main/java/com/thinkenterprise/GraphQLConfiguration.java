package com.thinkenterprise;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thinkenterprise.domain.RouteRepository;
import com.thinkenterprise.resolver.QueryResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfiguration {
	
	
	@Bean
	public GraphQLQueryResolver getQueryResolver(RouteRepository routeRepository) {
		return new QueryResolver(routeRepository);
	}

}

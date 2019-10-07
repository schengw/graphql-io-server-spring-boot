package com.thinkenterprise.graphqlio.server.schema;

import java.io.IOException;
import java.util.List;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParser;
import com.thinkenterprise.graphqlio.server.autoconfiguration.GraphQLIOProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import graphql.schema.GraphQLSchema;


public class GraphQLIOSimpleSchemaCreator implements GraphQLIOSchemaCreator {

	@Autowired(required = false)
	List<GraphQLResolver<?>> resolvers;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private GraphQLIOProperties graphqlioProperties;

	
	@Override
	public GraphQLSchema create() {
		
		// @Fixme : Some other parameter like Scalars, should be configured 
		GraphQLSchema  graphQLSchema = SchemaParser.newParser()
		               							.files(getFiles())
		               						    .resolvers(resolvers)
		               						    .build()
		               						    .makeExecutableSchema();
			          
		
		return graphQLSchema;
		
	}
	
	
	protected String[] getFiles() {
		
		Resource[] resources = null;
		String[] files = null;
		
		try {

	   		resources = applicationContext.getResources("classpath*:" + graphqlioProperties.getSchemaLocationPattern());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		files= new String[resources.length];
		
		for (int i = 0; i < resources.length; ++i ) {
			files[i]=resources[i].getFilename();
		}
		
		return files;
		
	}
	

}

package com.thinkenterprise.graphqlio.server.schema;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;


@Component
public class DefaultGraphQLIOSchemaCreator implements GraphQLIOSchemaCreator{

	@Autowired(required = false)
	List<GraphQLResolver<?>> resolvers;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Value("${graphql.tools.schemaLocationPattern:**/*.graphql}")
	private String schemaLocationPattern;

	
	@Override
	public GraphQLSchema create() {
		

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
			resources = applicationContext.getResources("classpath*:" + schemaLocationPattern);
			
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

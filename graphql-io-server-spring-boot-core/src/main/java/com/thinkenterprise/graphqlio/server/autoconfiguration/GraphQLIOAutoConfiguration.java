package com.thinkenterprise.graphqlio.server.autoconfiguration;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.thinkenterprise.graphqlio.server.converter.GraphQLIOFrameToRequestMessageConverter;
import com.thinkenterprise.graphqlio.server.converter.GraphQLIONotifierMessageToFrameConverter;
import com.thinkenterprise.graphqlio.server.converter.GraphQLIOResponseMessageToFrameConverter;
import com.thinkenterprise.graphqlio.server.execution.GraphQLIOQueryExecutionStrategy;
import com.thinkenterprise.graphqlio.server.execution.GraphQLIOSimpleQueryExecutionStrategie;
import com.thinkenterprise.graphqlio.server.handler.GraphQLIOWebSocketHandler;
import com.thinkenterprise.graphqlio.server.schema.GraphQLIOSchemaCreator;
import com.thinkenterprise.graphqlio.server.schema.GraphQLIOSimpleSchemaCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableConfigurationProperties(GraphQLIOProperties.class)
@EnableWebSocket
public class GraphQLIOAutoConfiguration implements WebSocketConfigurer {

	@Autowired
	private GraphQLIOProperties graphQLIOProperties;

	@Autowired
	private GraphQLIOWebSocketHandler graphQLIOWebSocketHandler;

	
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {	 
    	registry.addHandler(this.graphQLIOWebSocketHandler, graphQLIOProperties.getEndpoint());   
	}
	
	@Bean
	@ConditionalOnMissingBean
    public GraphQLIOFrameToRequestMessageConverter graphQLIOFrameToRequestMessageConverter() {
        return new GraphQLIOFrameToRequestMessageConverter();
	}

	@Bean
	@ConditionalOnMissingBean
    public GraphQLIOResponseMessageToFrameConverter graphQLIOResponseMessageToFrameConverter() {
        return new GraphQLIOResponseMessageToFrameConverter();
	}

	@Bean
	@ConditionalOnMissingBean
    public GraphQLIONotifierMessageToFrameConverter graphQLIONotifierMessageToFrameConverter() {
        return new GraphQLIONotifierMessageToFrameConverter();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public GraphQLIOSchemaCreator graphQLIOSchemaCreator() {
		return new GraphQLIOSimpleSchemaCreator();
	}

	@Bean
	@ConditionalOnMissingBean
	public GraphQLIOQueryExecutionStrategy graphQLIOQueryExecutionStrategy() {
		return new GraphQLIOSimpleQueryExecutionStrategie();
	}

	@Bean
	@ConditionalOnMissingBean
	public GraphQLIOWebSocketHandler graphQLIOWebSocketHandler() {
		return new GraphQLIOWebSocketHandler();

	}

	@Bean
	public ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).registerModule(new Jdk8Module());
        
        InjectableValues.Std injectableValues = new InjectableValues.Std();
        injectableValues.addValue(ObjectMapper.class, mapper);
        mapper.setInjectableValues(injectableValues);

        return mapper;
    }

	



    
}
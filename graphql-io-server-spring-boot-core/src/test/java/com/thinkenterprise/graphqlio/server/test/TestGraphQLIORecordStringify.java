package com.thinkenterprise.graphqlio.server.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord.GraphQLIOArityType;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord.GraphQLIOOperationType;

// Review 11112019: Should be a normal JUnit Test, right? 
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGraphQLIORecordStringify {
	
	final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger();
	
	
	@Test
	public void testStringify() {

		GraphQLIORecord record = GraphQLIORecord.builder()
			.op(GraphQLIOOperationType.READ)
			.arity(GraphQLIOArityType.ONE)
			.dstType("dstType")
			.dstIds(new String[] {"dstId1"})
			.dstAttrs(new String[] {"dstAttr1", "dstAttr2"})
			.build();
						
		String strRecord = record.stringify();
		log.info("strRecord =" + strRecord);
		
		Assert.assertTrue(GraphQLIORecord.matchesPredefinedPattern(strRecord));		
	}


	@Test
	public void testUnstringify() {
		String strRecord1 = "read(one)->dstType#{dstId}.{dstAttr1,dstAttr2}";

		GraphQLIORecord record1 = GraphQLIORecord.builder().stringified(strRecord1).build();

		String stringifiedRecord1 = record1.stringify();
		log.info("stringifiedRecord1 =" + stringifiedRecord1);
		
		Assert.assertTrue(GraphQLIORecord.matchesPredefinedPattern(stringifiedRecord1));				
		Assert.assertTrue(record1 != null && stringifiedRecord1 != null && strRecord1.equals(stringifiedRecord1));				
		
		
		String strRecord2 = "srcType#srcId.srcAttr->read(many)->dstType#{dstId1,dstId2}.{dstAttr1,dstAttr2}";	
		
		GraphQLIORecord record2 = GraphQLIORecord.builder().stringified(strRecord2).build();
		
		String stringifiedRecord2 = record2.stringify();
		log.info("stringifiedRecord2 =" + stringifiedRecord2);
		
		Assert.assertTrue(GraphQLIORecord.matchesPredefinedPattern(stringifiedRecord2));
		Assert.assertTrue(record2 != null && stringifiedRecord2 != null && strRecord2.equals(stringifiedRecord2));				
		
		
		String strRecord3 = "srcType#srcId.srcAttr->read(all)->dstType#{*}.{*}";	
		
		GraphQLIORecord record3 = GraphQLIORecord.builder().stringified(strRecord3).build();
		
		String stringifiedRecord3 = record3.stringify();
		log.info("stringifiedRecord3 =" + stringifiedRecord3);
		
		Assert.assertTrue(GraphQLIORecord.matchesPredefinedPattern(stringifiedRecord3));
		Assert.assertTrue(record3 != null && stringifiedRecord3 != null && strRecord3.equals(stringifiedRecord3));				
		
		
		
	}
	
	
}

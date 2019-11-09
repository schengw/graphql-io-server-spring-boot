package com.thinkenterprise.graphqlio.server.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.thinkenterprise.graphqlio.server.domain.GraphQLIORecord;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOScope;
import com.thinkenterprise.graphqlio.server.domain.GraphQLIOScopeState;
import com.thinkenterprise.graphqlio.server.evaluation.GraphQLIOEvaluation;
import com.thinkenterprise.graphqlio.server.keyvaluestore.KeyValStore;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGraphQLIOEvaluation {
	
	final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private KeyValStore keyval;
	
	@Autowired
	GraphQLIOEvaluation graphQLIOEvaluation;
	
//// test data	
	final String strRecordQuerySid1 = "read(many)->item#{id1,id2,id3,id4,id5}.{id,name,address,email}";
	final String strRecordQuerySid2 = "read(many)->item#{id4,id5,id6,id7}.{id,name}";
	final String strRecordQuerySid3 = "card#id1.items->read(one)->item#{id1}.{id,name}";

	final String strRecordMutationUpdateItemInQuerySid1 = "update(one)->item#{id3}.{email}";
	final String strRecordMutationDeleteItemInQuerySid1 = "delete(one)->item#{id2}.{*}";
	final String strRecordMutationUpdateOtherItem = "update(one)->item#{id10}.{email}";
	final String strRecordMutationUpdateOtherAttribute = "update(one)->item#{id3}.{telephone}";
	
	final String strRecordMutationUpdateCardItems = "update(one)->card#{id1}.{items}";
	
	final GraphQLIORecord recordMutationUpdateItemInQuerySid1 = 
			GraphQLIORecord.builder().stringified(strRecordMutationUpdateItemInQuerySid1).build();		
	final GraphQLIORecord recordMutationDeleteItemInQuerySid1 =
			GraphQLIORecord.builder().stringified(strRecordMutationDeleteItemInQuerySid1).build();		
	final GraphQLIORecord recordMutationUpdateOtherItem =
			GraphQLIORecord.builder().stringified(strRecordMutationUpdateOtherItem).build();		
	final GraphQLIORecord recordMutationUpdateOtherAttribute =
			GraphQLIORecord.builder().stringified(strRecordMutationUpdateOtherAttribute).build();	
	final GraphQLIORecord recordMutationUpdateCardItems =
			GraphQLIORecord.builder().stringified(strRecordMutationUpdateCardItems).build();	
	
	
	GraphQLIOScope scopeSid1Cid1 =
			GraphQLIOScope.builder().withScopeId("Sid1").withConnectionId("Cid1").withQuery(strRecordQuerySid1).withState(GraphQLIOScopeState.SUBSCRIBED).build();
	GraphQLIOScope scopeSid1Cid2 = 
			GraphQLIOScope.builder().withScopeId("Sid1").withConnectionId("Cid2").withQuery(strRecordQuerySid1).withState(GraphQLIOScopeState.SUBSCRIBED).build();
	GraphQLIOScope scopeSid2Cid1 = 
			GraphQLIOScope.builder().withScopeId("Sid2").withConnectionId("Cid1").withQuery(strRecordQuerySid2).withState(GraphQLIOScopeState.SUBSCRIBED).build();
	GraphQLIOScope scopeSid2Cid2 = 
			GraphQLIOScope.builder().withScopeId("Sid2").withConnectionId("Cid2").withQuery(strRecordQuerySid2).withState(GraphQLIOScopeState.SUBSCRIBED).build();	
	GraphQLIOScope scopeSid3Cid1 = 
			GraphQLIOScope.builder().withScopeId("Sid3").withConnectionId("Cid1").withQuery(strRecordQuerySid3).withState(GraphQLIOScopeState.SUBSCRIBED).build();	

	
	@Test
	public void testMutationUpdateItemOutdatesScopeUseCase1() {

        /*
         * CASE 1: modified entity (of arbitrary direct access) old/query:
         * [*#{*}.*->]read(*)->Item#{1}.{id,name} new/mutation:
         * [*#{*}.*->]update/delete(*)->Item#{1}.{name}
         */
		
		
		List<String> outdatedSids = null;		

		//// update mutation in scope 1 outdates "Scope" sid1 but not sid2
		scopeSid1Cid1.addRecord(recordMutationUpdateItemInQuerySid1);		
		//// update mutation in scope 2 outdates "Scope" sid1 but not side
		scopeSid2Cid1.addRecord(recordMutationUpdateItemInQuerySid1);		

        List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
        String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
		keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);
		
        List<String> records2 = scopeSid2Cid1.getStringifiedRecords();
        String[] scopeRecords2 = records2.toArray(new String[records2.size()]);
		keyval.putRecords(scopeSid2Cid1.getConnectionId(), scopeSid2Cid1.getScopeId(), scopeRecords2);
		
		outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);
		Assert.assertTrue(outdatedSids.size() == 1);
		Assert.assertTrue(outdatedSids.contains("Sid1"));	
		
		outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid2Cid1);
		Assert.assertTrue(outdatedSids.size() == 1);
		Assert.assertTrue(outdatedSids.contains("Sid1")); 	
	}

	
	@Test
	public void testMutationUpdateCardOutdatesScopeUseCase2() {

        /*
         * CASE 2: modified entity list (of relationship traversal) old/query
         * Card#1.items->read(*)->Item#{2}.{id,name} new/mutation:
         * [*#{*}.*->]update(*)->Card#{1}.{items}
         */
				
		List<String> outdatedSids = null;		
		
		
		//// card update mutation in scope 1 outdates "Scope" sid3  (read card items)
		scopeSid1Cid1.addRecord(recordMutationUpdateCardItems);

        List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
        String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
		keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);

        List<String> records3 = scopeSid3Cid1.getStringifiedRecords();
        String[] scopeRecords3 = records3.toArray(new String[records3.size()]);
		keyval.putRecords(scopeSid3Cid1.getConnectionId(), scopeSid3Cid1.getScopeId(), scopeRecords3);
		
		
		outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);
		Assert.assertTrue(outdatedSids.size() == 1);
		Assert.assertTrue(outdatedSids.contains("Sid3"));	
		
	}	
	
	@Test
	public void testMutationDeleteItemOutdatesScopeUseCase3() {
		
        /*
         * CASE 3: modified entity list (of direct query) old/query
         * [*#{*}.*->]read(many/all)->Item#{*}.{id,name} new/mutation:
         * [*#{*}.*->]create/update/delete(*)->Item#{*}.{name}
         * 
         * "Read:Many", "Delete:Attr=*
         */		
		
		List<String> outdatedSids = null;		
		
		//// delete mutation in scope 1 outdates "Scope" sid1 and sid2
		scopeSid1Cid1.addRecord(recordMutationDeleteItemInQuerySid1);
		//// delete mutation in scope 2 outdates "Scope" sid1 and sid2
		scopeSid2Cid1.addRecord(recordMutationDeleteItemInQuerySid1);		

        List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
        String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
		keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);
		
        List<String> records2 = scopeSid2Cid1.getStringifiedRecords();
        String[] scopeRecords2 = records2.toArray(new String[records2.size()]);
		keyval.putRecords(scopeSid2Cid1.getConnectionId(), scopeSid2Cid1.getScopeId(), scopeRecords2);
				
		outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);
		Assert.assertTrue(outdatedSids.size() == 2);		
		Assert.assertTrue(outdatedSids.contains("Sid1"));
		Assert.assertTrue(outdatedSids.contains("Sid2"));
		
		outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid2Cid1);
		Assert.assertTrue(outdatedSids.size() == 2);		
		Assert.assertTrue(outdatedSids.contains("Sid1"));
		Assert.assertTrue(outdatedSids.contains("Sid2"));
		
	}
	
	@Test
	public void testMutationUpdateItemOutdatesScopeUseCase3() {
		
        /*
         * CASE 3: modified entity list (of direct query) old/query
         * [*#{*}.*->]read(many/all)->Item#{*}.{id,name} new/mutation:
         * [*#{*}.*->]create/update/delete(*)->Item#{*}.{name}
         * 
         * "Read:Many", "Update (other item, but matching attribute "email" to query in Sid1) ==> outdates Sid1
         * "Read:Many", "Update (other item, and non matching attribute") ==> does not outdate queries in sid1 or sid2
         */		
		List<String> outdatedSids = null;		
		
		// updates item id10 which is not in returned list, but "read many
		scopeSid1Cid1.addRecord(recordMutationUpdateOtherItem);

		// updates item id10 which is not in returned list, but "read many
		scopeSid2Cid1.addRecord(recordMutationUpdateOtherItem);		
		
		
        List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
        String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
		keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);
		
        List<String> records2 = scopeSid2Cid1.getStringifiedRecords();
        String[] scopeRecords2 = records2.toArray(new String[records2.size()]);
		keyval.putRecords(scopeSid2Cid1.getConnectionId(), scopeSid2Cid1.getScopeId(), scopeRecords2);
		
		outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);
		Assert.assertTrue(outdatedSids.size() == 1);
		Assert.assertTrue(outdatedSids.contains("Sid1"));
		
		outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid2Cid1);
		Assert.assertTrue(outdatedSids.size() == 1);
		Assert.assertTrue(outdatedSids.contains("Sid1"));
	}
	

}

package com.versionone.sdk.legacy.integration.tests;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.versionone.Oid;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.AttributeSelection;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.exceptions.V1Exception;
import com.versionone.apiclient.filters.AndFilterTerm;
import com.versionone.apiclient.filters.FilterTerm;
import com.versionone.apiclient.filters.GroupFilterTerm;
import com.versionone.apiclient.filters.IFilterTerm;
import com.versionone.apiclient.filters.OrFilterTerm;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IMetaModel;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.OrderBy.Order;
import com.versionone.apiclient.services.QueryFind;
import com.versionone.apiclient.services.QueryResult;


public class FindAndQueryTests {
	
	private final String InitialStoryName = "Initial name";
	private final String ChangedStoryName = "Changed name";
	private final String FinalStoryName = "Final name";

	private static IMetaModel metaModel;
	private static IServices services;
	private static IAssetType storyType;

	private static IAttributeDefinition nameDef;
	private static IAttributeDefinition scopeDef;
	private static IAttributeDefinition momentDef;
	private static Collection<IAttributeDefinition> attributesToQuery;


	@BeforeClass
	public static void beforeClass() {

		metaModel = APIClientLegacyIntegrationTestSuiteIT.get_metaModel();
		services = APIClientLegacyIntegrationTestSuiteIT.get_services();
		storyType = metaModel.getAssetType("Story");

		nameDef = storyType.getAttributeDefinition("Name");
		scopeDef = storyType.getAttributeDefinition("Scope");
		momentDef = storyType.getAttributeDefinition("Moment");
		attributesToQuery = new LinkedList<IAttributeDefinition>();
		attributesToQuery.add(nameDef);
		attributesToQuery.add(scopeDef);
		attributesToQuery.add(momentDef);
	}

	// Query for single assets
	@Test
	public void testQuerySingleAsset() throws Exception {

			Oid memberId = Oid.fromToken("Member:20", metaModel);
			Query query = new Query(memberId);
			IAttributeDefinition nameAttribute = metaModel.getAttributeDefinition("Member.Username");
			query.getSelection().add(nameAttribute);
			QueryResult result = services.retrieve(query);

			Assert.assertNotNull(result.getAssets());

			Assert.assertEquals("1 asset", 1, result.getAssets().length);
	}

	// Query for multiple assets
	@Test
	public void testQueryMultipleAsset() throws Exception {
	      
		
		Query query = new Query(storyType);
	    IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
	    IAttributeDefinition estimateAttribute = storyType.getAttributeDefinition("Estimate");
	    query.getSelection().add(nameAttribute);
	    query.getSelection().add(estimateAttribute);

	    QueryResult result = services.retrieve(query);

	      Assert.assertNotNull(result.getAssets());

	      Assert.assertTrue(result.getAssets().length > 1);
	}
	
	// Filter query // Find query
	@Test
	@Ignore
	public void testFindInAQuery() throws Exception {

		Asset newStoryUrgent = createDisposableStory();// services.createNew(storyType, APIClientIntegrationTestSuiteIT.get_projectId());
		IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
		newStoryUrgent.setAttributeValue(nameAttribute, "FindAndQueryTest: Find in a Query - Urgent story");
		services.save(newStoryUrgent);

		Asset newStory = createDisposableStory();//services.createNew(storyType, APIClientIntegrationTestSuiteIT.get_projectId());
		nameAttribute = storyType.getAttributeDefinition("Name");
		newStory.setAttributeValue(nameAttribute, "FindAndQueryTest: Find in a Query - Common story");
		services.save(newStory);
//		query
		IAssetType requestType = metaModel.getAssetType("Story");
		Query query = new Query(requestType);
		IAttributeDefinition nameAttributeToQuery = requestType.getAttributeDefinition("Name");
		query.getSelection().add(nameAttributeToQuery);
		
//		find
		IAttributeDefinition nameAttributeToFindQuery = requestType.getAttributeDefinition("Name");
		AttributeSelection selection = new AttributeSelection();
		selection.add(nameAttributeToFindQuery);
		query.setFind(new QueryFind("Urgent", selection)); 
		QueryResult result = services.retrieve(query);
		Asset urgentStory = result.getAssets()[0];
		
		Assert.assertEquals("FindAndQueryTest: Find in a Query - Urgent story", urgentStory.getAttribute(nameAttribute).getValue().toString());
	}

	// Sort query
	@Test
		public void testSortStories() throws Exception {

		//add 3 stories
		Asset storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, "1MM-Story");
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);
		storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, "1AA-Story");
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);
		storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, "1HH-Story");
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);
		
		IAssetType storyType = metaModel.getAssetType("Story");
		Query query = new Query(storyType);
		IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
		query.getSelection().add(nameAttribute);
		query.getOrderBy().minorSort(nameAttribute, Order.Ascending);
		
		QueryResult result = services.retrieve(query);
		
		Asset first_story = result.getAssets()[0];
		
		Assert.assertEquals("1AA-Story", first_story.getAttribute(nameAttribute).getValue().toString());
		
	}	

	//asof
	@Test
	@Ignore
	public void testAsof() throws Exception {
		Asset storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, "Test Asof");
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);

		IAssetType storyType = metaModel.getAssetType("Story");
		Query query = new Query(storyType, true);
		IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
		IAttributeDefinition createDateAttribute = storyType.getAttributeDefinition("CreateDate");

		query.getSelection().add(nameAttribute);
		query.getSelection().add(createDateAttribute);
		AttributeSelection selection = new AttributeSelection();
		selection.add(nameAttribute);
		query.setFind(new QueryFind("Test Asof", selection)); 
		Date date = (Date) Calendar.getInstance().getTime();
		query.setAsOf(date); 
		QueryResult result = services.retrieve(query);
		
	//	Assert.assertNotNull(result);
		
		Assert.assertEquals("Test Asof", result.getAssets()[0].getAttribute(nameAttribute).getValue().toString());

	}	
	
	@Test
	public void testQueryStoryByMoment() throws Exception {
		Asset storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, InitialStoryName);
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);

		storyAsset = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery)[0];
		Object moment = storyAsset.getAttribute(momentDef).getValue();
		Assert.assertEquals(storyAsset.getAttribute(nameDef).getValue(), InitialStoryName);

		storyAsset.setAttributeValue(nameDef, ChangedStoryName);
		services.save(storyAsset);

		storyAsset = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery)[0];
		Assert.assertEquals(storyAsset.getAttribute(nameDef).getValue(), ChangedStoryName);

		FilterTerm filter = new FilterTerm(momentDef);
		filter.equal(moment);
		storyAsset = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery, filter, false)[0];
		Assert.assertEquals(storyAsset.getAttribute(nameDef).getValue(), ChangedStoryName);
	}

	//query History
	@Test
	public void testQueryStoryHistoryByMoment() throws Exception {
		Asset storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, InitialStoryName);
		storyAsset.setAttributeValue(scopeDef,APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);

		storyAsset = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery)[0];
		Object moment = storyAsset.getAttribute(momentDef).getValue();
		Assert.assertEquals(storyAsset.getAttribute(nameDef).getValue(), InitialStoryName);

		storyAsset.setAttributeValue(nameDef, ChangedStoryName);
		services.save(storyAsset);

		storyAsset = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery)[0];
		Assert.assertEquals(storyAsset.getAttribute(nameDef).getValue(), ChangedStoryName);

		FilterTerm filter = new FilterTerm(momentDef);
		filter.equal(moment);
		storyAsset = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery, filter, true)[0];
		Assert.assertEquals(storyAsset.getAttribute(nameDef).getValue(), InitialStoryName);
	}

	@Test
	public void testQueryStoryChangesWithInequalityFilter() throws Exception {
	
		Asset storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, InitialStoryName);
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);

		storyAsset = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery)[0];
		Object moment1 = storyAsset.getAttribute(momentDef).getValue();

		storyAsset.setAttributeValue(nameDef, ChangedStoryName);
		services.save(storyAsset);

		storyAsset.setAttributeValue(nameDef, FinalStoryName);
		services.save(storyAsset);
		storyAsset = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery)[0];
		Object moment3 = storyAsset.getAttribute(momentDef).getValue();

		FilterTerm filter = new FilterTerm(momentDef);
		filter.greaterOrEqual(moment1);
		Asset[] assets = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery, filter, true);
		Assert.assertEquals(3, assets.length);
		Assert.assertTrue(nameMatch(InitialStoryName, assets));
		Assert.assertTrue(nameMatch(ChangedStoryName, assets));
		Assert.assertTrue(nameMatch(FinalStoryName, assets));

		filter = new FilterTerm(momentDef);
		filter.greater(moment1);
		assets = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery, filter, true);
		Assert.assertEquals(2, assets.length);
		Assert.assertFalse(nameMatch(InitialStoryName, assets));
		Assert.assertTrue(nameMatch(ChangedStoryName, assets));
		Assert.assertTrue(nameMatch(FinalStoryName, assets));

		FilterTerm lessFilter = new FilterTerm(momentDef);
		lessFilter.less(moment3);
		FilterTerm greaterFilter = new FilterTerm(momentDef);
		greaterFilter.greater(moment1);
		GroupFilterTerm groupFilter = new AndFilterTerm(lessFilter, greaterFilter);
		assets = getAssetsByOid(storyAsset.getOid().getMomentless(), attributesToQuery, groupFilter, true);
		Assert.assertEquals(1, assets.length);
		Assert.assertFalse(nameMatch(InitialStoryName, assets));
		Assert.assertTrue(nameMatch(ChangedStoryName, assets));
		Assert.assertFalse(nameMatch(FinalStoryName, assets));
	}

//	Paging
	@Test
	public void testPageListOfAssets() throws Exception {

		//add 3 stories
		Asset storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, "Paging Test - Story1");
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);
		storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, "Paging Test - Story2");
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);
		storyAsset = createDisposableStory();
		storyAsset.setAttributeValue(nameDef, "Paging Test - Story3");
		storyAsset.setAttributeValue(scopeDef, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
		services.save(storyAsset);
		
		Query query = new Query(storyType);
        IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
        IAttributeDefinition estimateAttribute = storyType.getAttributeDefinition("Estimate");
        query.getSelection().add(nameAttribute);
        query.getSelection().add(estimateAttribute);
        QueryResult result = services.retrieve(query);
        
        Assert.assertTrue(result.getAssets().length > 2 );
       
        query.getPaging().setPageSize(2);
        query.getPaging().setStart(0);
        result = services.retrieve(query);
    
        Assert.assertTrue(result.getAssets().length == 2 );
	}
	
	
	private boolean nameMatch(String name, Asset[] assets) throws Exception {
		for (Asset asset : assets) {
			String assetName = asset.getAttribute(nameDef).getValue().toString();

			if (name.equals(assetName)) {
				return true;
			}
		}
		return false;
	}

	private Asset createDisposableStory() throws V1Exception {
		Asset story = services.createNew(storyType, APIClientLegacyIntegrationTestSuiteIT.get_projectId());
//		assetsToDispose.add(story);
		return story;
	}

	private Asset[] getAssetsByOid(Oid oid, Collection<IAttributeDefinition> attributesToQuery) throws Exception {
		return getAssetsByOid(oid, attributesToQuery, null, false);
	}

	private Asset[] getAssetsByOid(Oid oid, Collection<IAttributeDefinition> attributesToQuery, IFilterTerm filter, boolean historicalQuery)
			throws Exception {
		Query query = new Query(oid, historicalQuery);
		query.getSelection().addAll(attributesToQuery);

		if (filter != null) {
			query.setFilter(filter);
		}

		return services.retrieve(query).getAssets();
	}
}

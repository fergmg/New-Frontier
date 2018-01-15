package com.versionone.sdk.documentation.tests;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.versionone.Oid;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.AssetState;
import com.versionone.apiclient.AttributeSelection;
import com.versionone.apiclient.EnvironmentContext;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.V1APIConnector;
import com.versionone.apiclient.V1Configuration;
import com.versionone.apiclient.exceptions.APIException;
import com.versionone.apiclient.exceptions.ConnectionException;
import com.versionone.apiclient.exceptions.OidException;
import com.versionone.apiclient.filters.FilterTerm;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IMetaModel;
import com.versionone.apiclient.interfaces.IOperation;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.OrderBy;
import com.versionone.apiclient.services.QueryResult;

/**
 * This class contains the examples used in the Java SDK documentation.
 */
public class JavaSDKCodeSamples {

    private EnvironmentContext _context;

    private IMetaModel _metaModel;
    private IMetaModel _metaModelWithProxy;
    private IServices _services;
    private IServices _servicesWithProxy;
    private V1Configuration _config;

    public JavaSDKCodeSamples() throws Exception {

        _context = new EnvironmentContext();

        _metaModel = _context.getMetaModel();
        _metaModelWithProxy = _context.getMetaModelWithProxy();  //if you use a proxy server
        _services = _context.getServices();
        _servicesWithProxy = _context.getServicesWithProxy();  //if you use a proxy server
        _config = _context.getV1Configuration();
    }

    public void GetV1configuration()
    {
        V1Configuration configuration = new V1Configuration(new V1APIConnector("https://www14.v1host.com/v1sdktesting/config.v1/"));
    }

    public Asset SingleAsset() throws Exception {
        Oid memberId = Oid.fromToken("Member:20", _metaModel);
        Query query = new Query(memberId);
        QueryResult result = _services.retrieve(query);
        Asset member = result.getAssets()[0];

        System.out.println(member.getOid().getToken());
        /***** OUTPUT *****
         Member:20
         ******************/

        return member;
    }

    public Asset SingleAssetWithAttributes() throws Exception {
        Oid memberId = Oid.fromToken("Member:20", _metaModel);
        Query query = new Query(memberId);
        IAttributeDefinition nameAttribute = _metaModel.getAttributeDefinition("Member.Name");
        IAttributeDefinition emailAttribute = _metaModel.getAttributeDefinition("Member.Email");
        query.getSelection().add(nameAttribute);
        query.getSelection().add(emailAttribute);
        QueryResult result = _services.retrieve(query);
        Asset member = result.getAssets()[0];

        System.out.println(member.getOid().getToken());
        System.out.println(member.getAttribute(nameAttribute).getValue());
        System.out.println(member.getAttribute(emailAttribute).getValue());

        /***** OUTPUT *****
         Member:20
         Administrator
         admin@company.com
         ******************/

        return member;
    }

    public boolean IsEffortTrackingEnabled() throws Exception
    {
        return _config.isEffortTracking();

        /***** OUTPUT *****
         False
         ******************/
    }

    public Asset[] ListOfAssets() throws Exception {
        IAssetType storyType = _metaModel.getAssetType("Story");
        Query query = new Query(storyType);
        IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
        IAttributeDefinition estimateAttribute = storyType.getAttributeDefinition("Estimate");
        query.getSelection().add(nameAttribute);
        query.getSelection().add(estimateAttribute);
        QueryResult result = _services.retrieve(query);

        for (Asset story : result.getAssets()) {
            System.out.println(story.getOid().getToken());
            System.out.println(story.getAttribute(nameAttribute).getValue());
            System.out.println(story.getAttribute(estimateAttribute).getValue());
            System.out.println();
        }
        /***** OUTPUT *****
         Story:1083
         View Daily Call Count
         5

         Story:1554
         Multi-View Customer Calendar
         1 ...
         ******************/

        return result.getAssets();
    }

    public Asset[] FindListOfAssets() throws Exception
    {
        IAssetType requestType = _metaModel.getAssetType("Story");
        Query query = new Query(requestType);
        IAttributeDefinition nameAttribute = requestType.getAttributeDefinition("Name");

        query.getSelection().add(nameAttribute);

        AttributeSelection selection = new AttributeSelection();
        selection.add(nameAttribute);
        //query.setFind(new QueryFind("Urgent", selection)); //if you'd like find only stories marked as urgent, for example.

        QueryResult result = _services.retrieve(query);

        for (Asset request : result.getAssets())
        {
            System.out.println(request.getOid().getToken());
            System.out.println(request.getAttribute(nameAttribute).getValue());
            System.out.println();
        }
        /***** OUTPUT *****
         Request:1195
         Urgent!  Filter by owner

         Task:1244
         Urgent: improve search performance ...
         ******************/

        return result.getAssets();
    }

    public Asset[] FilterListOfAssets() throws Exception {
        IAssetType taskType = _metaModel.getAssetType("Task");
        Query query = new Query(taskType);
        IAttributeDefinition nameAttribute = taskType.getAttributeDefinition("Name");
        IAttributeDefinition todoAttribute = taskType.getAttributeDefinition("ToDo");
        query.getSelection().add(nameAttribute);
        query.getSelection().add(todoAttribute);

        FilterTerm toDoTerm = new FilterTerm(todoAttribute);
        toDoTerm.equal(0);
        query.setFilter(toDoTerm);
        QueryResult result = _services.retrieve(query);

        for (Asset task : result.getAssets()) {
            System.out.println(task.getOid().getToken());
            System.out.println(task.getAttribute(nameAttribute).getValue());
            System.out.println(task.getAttribute(todoAttribute).getValue());
            System.out.println();
        }
        /***** OUTPUT *****
         Task:1153
         Code Review
         0

         Task:1154
         Design Component
         0 ...
         ******************/

        return result.getAssets();
    }

    public Asset[] SortListOfAssets() throws Exception {
        IAssetType storyType = _metaModel.getAssetType("Story");
        Query query = new Query(storyType);
        IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
        IAttributeDefinition estimateAttribute = storyType.getAttributeDefinition("Estimate");
        query.getSelection().add(nameAttribute);
        query.getSelection().add(estimateAttribute);
        query.getOrderBy().minorSort(estimateAttribute, OrderBy.Order.Ascending);
        QueryResult result = _services.retrieve(query);

        for (Asset story : result.getAssets()) {
            System.out.println(story.getOid().getToken());
            System.out.println(story.getAttribute(nameAttribute).getValue());
            System.out.println(story.getAttribute(estimateAttribute).getValue());
            System.out.println();
        }
        /***** OUTPUT *****
         Story:1073
         Add Order Line
         1

         Story:1068
         Update Member
         2 ...
         ******************/

        return result.getAssets();
    }

    public Asset[] PageListOfAssets() throws Exception {
        IAssetType storyType = _metaModel.getAssetType("Story");
        Query query = new Query(storyType);
        IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
        IAttributeDefinition estimateAttribute = storyType.getAttributeDefinition("Estimate");
        query.getSelection().add(nameAttribute);
        query.getSelection().add(estimateAttribute);
        query.getPaging().setPageSize(3);
        query.getPaging().setStart(0);
        QueryResult result = _services.retrieve(query);

        for (Asset story : result.getAssets()) {
            System.out.println(story.getOid().getToken());
            System.out.println(story.getAttribute(nameAttribute).getValue());
            System.out.println(story.getAttribute(estimateAttribute).getValue());
            System.out.println();
        }
        /***** OUTPUT *****
         Story:1063
         Logon
         2

         Story:1064
         Add Customer Details
         2

         Story:1065
         Add Customer Header
         3
         ******************/

        return result.getAssets();
    }

    public Asset[] HistorySingleAsset() throws Exception {
        IAssetType memberType = _metaModel.getAssetType("Member");
        Query query = new Query(memberType, true);
        IAttributeDefinition idAttribute = memberType.getAttributeDefinition("ID");
        IAttributeDefinition changeDateAttribute = memberType.getAttributeDefinition("ChangeDate");
        IAttributeDefinition emailAttribute = memberType.getAttributeDefinition("Email");
        query.getSelection().add(changeDateAttribute);
        query.getSelection().add(emailAttribute);
        FilterTerm idTerm = new FilterTerm(idAttribute);
        idTerm.equal("Member:20");
        query.setFilter(idTerm);
        QueryResult result = _services.retrieve(query);
        Asset[] memberHistory = result.getAssets();

        for (Asset member : memberHistory) {
            System.out.println(member.getOid().getToken());
            System.out.println(member.getAttribute(changeDateAttribute).getValue());
            System.out.println(member.getAttribute(emailAttribute).getValue());
            System.out.println();
        }
        /***** OUTPUT *****
         Member:1000:105
         4/2/2007 1:22:03 PM
         andre.agile@company.com

         Member:1000:101
         3/29/2007 4:10:29 PM
         andre@company.net
         ******************/

        return memberHistory;
    }

    public Asset[] HistoryListOfAssets() throws Exception {
        IAssetType memberType = _metaModel.getAssetType("Member");
        Query query = new Query(memberType, true);
        IAttributeDefinition changeDateAttribute = memberType.getAttributeDefinition("ChangeDate");
        IAttributeDefinition emailAttribute = memberType.getAttributeDefinition("Email");
        query.getSelection().add(changeDateAttribute);
        query.getSelection().add(emailAttribute);
        QueryResult result = _services.retrieve(query);
        Asset[] memberHistory = result.getAssets();

        for (Asset member : memberHistory) {
            System.out.println(member.getOid().getToken());
            System.out.println(member.getAttribute(changeDateAttribute).getValue());
            System.out.println(member.getAttribute(emailAttribute).getValue());
            System.out.println();
        }
        /***** OUTPUT *****
         Member:20:0
         Thu Nov 30 19:00:00 EST 1899
         null

         Member:20:17183
         Fri Nov 09 09:46:25 EST 2012
         versionone@mailinator.com

         Member:20:17190
         Sun Nov 11 22:59:23 EST 2012
         versionone@mailinator.com

         Member:20:17191
         Sun Nov 11 22:59:47 EST 2012
         versionone@mailinator.com
         ******************/

        return memberHistory;
    }

    public void StoryAndDefectTrackingLevel() throws Exception
    {

        System.out.println(_config.getStoryTrackingLevel());
        System.out.println(_config.getDefectTrackingLevel());

        /***** OUTPUT *****
         Off
         On
         ******************/
    }
    public Asset[] HistoryAsOfTime() throws Exception {
        IAssetType storyType = _metaModel.getAssetType("Story");
        Query query = new Query(storyType, true);
        IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
        IAttributeDefinition estimateAttribute = storyType.getAttributeDefinition("Estimate");
        query.getSelection().add(nameAttribute);
        query.getSelection().add(estimateAttribute);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -7);
        query.setAsOf(c.getTime()); // query.AsOf = DateTime.Now.AddDays(-7); //7 days ago
        QueryResult result = _services.retrieve(query);

        for (Asset story : result.getAssets()) {
            System.out.println(story.getOid().getToken());
            System.out.println(story.getAttribute(nameAttribute).getValue());
            System.out.println(story.getAttribute(estimateAttribute).getValue());
            System.out.println();
        }
        /***** OUTPUT *****
         Story:1063
         Logon
         3

         Story:1064
         Add Customer Details
         1

         Story:1065
         Add Customer Header
         3
         ******************/

        return result.getAssets();
    }

    public Asset UpdateScalarAttribute() throws Exception {
        Oid storyId = Oid.fromToken("Story:1094", _metaModel);
        Query query = new Query(storyId);
        IAssetType storyType = _metaModel.getAssetType("Story");
        IAttributeDefinition nameAttribute = storyType.getAttributeDefinition("Name");
        query.getSelection().add(nameAttribute);
        QueryResult result = _services.retrieve(query);
        Asset story = result.getAssets()[0];
        String oldName = story.getAttribute(nameAttribute).getValue().toString();
        story.setAttributeValue(nameAttribute, GetNewName());
        _services.save(story);

        System.out.println(story.getOid().getToken());
        System.out.println(oldName);
        System.out.println(story.getAttribute(nameAttribute).getValue());
        /***** OUTPUT *****
         Story:1094:1446
         Logon
         New Name
         ******************/

        return story;
    }

    public Asset UpdateSingleValueRelation() throws Exception {
        Oid storyId = Oid.fromToken("Story:1094", _metaModel);
        Query query = new Query(storyId);
        IAssetType storyType = _metaModel.getAssetType("Story");
        IAttributeDefinition sourceAttribute = storyType.getAttributeDefinition("Source");
        query.getSelection().add(sourceAttribute);
        QueryResult result = _services.retrieve(query);
        Asset story = result.getAssets()[0];
        String oldSource = story.getAttribute(sourceAttribute).getValue().toString();
        story.setAttributeValue(sourceAttribute, GetNextSourceID(oldSource));
        _services.save(story);

        System.out.println(story.getOid().getToken());
        System.out.println(oldSource);
        System.out.println(story.getAttribute(sourceAttribute).getValue());
        /***** OUTPUT *****
         Story:1094:1446
         StorySource:148
         StorySource:149
         ******************/

        return story;
    }

    public Asset UpdateMultiValueRelation() throws Exception {
        Oid storyId = Oid.fromToken("Story:1124", _metaModel);
        Query query = new Query(storyId);
        IAssetType storyType = _metaModel.getAssetType("Story");
        IAttributeDefinition ownersAttribute = storyType.getAttributeDefinition("Owners");
        query.getSelection().add(ownersAttribute);
        QueryResult result = _services.retrieve(query);
        Asset story = result.getAssets()[0];
        List<Object> oldOwners = new ArrayList<Object>();
        oldOwners.addAll(Arrays.asList(story.getAttribute(ownersAttribute).getValues()));
        story.removeAttributeValue(ownersAttribute, getOwnerToRemove(oldOwners));
        story.addAttributeValue(ownersAttribute, getOwnerToAdd(oldOwners));
        _services.save(story);

        System.out.println(story.getOid().getToken());
        Iterator<Object> iter = oldOwners.iterator();
        while (iter.hasNext()) {
            Oid oid = (Oid) iter.next();
            System.out.println(oid.getToken());
        }
        for (Object o : story.getAttribute(ownersAttribute).getValues()) {
            Oid oid = (Oid) o;
            System.out.println(oid.getToken());
        }
        /***** OUTPUT *****
         Story:1094:1446
         Member:1003
         Member:1000
         ******************/

        return story;
    }

    public Asset AddNewAsset() throws Exception {
        Oid projectId = Oid.fromToken("Scope:0", _metaModel);
        IAssetType assetType = _metaModel.getAssetType("Story");
        Asset newStory = _services.createNew(assetType, projectId);
        IAttributeDefinition nameAttribute = assetType.getAttributeDefinition("Name");
        newStory.setAttributeValue(nameAttribute, "My New Story");
        _services.save(newStory);

        System.out.println(newStory.getOid().getToken());
        System.out.println(newStory.getAttribute(assetType.getAttributeDefinition("Scope")).getValue());
        System.out.println(newStory.getAttribute(nameAttribute).getValue());
        /***** OUTPUT *****
         Story:1094
         Scope:1012
         My New Story
         ******************/

        return newStory;
    }

    public Oid DeleteAsset() throws Exception {
        Asset story = AddNewAsset();
        IOperation deleteOperation = _metaModel.getOperation("Story.Delete");
        Oid deletedID = _services.executeOperation(deleteOperation, story.getOid());
        Query query = new Query(deletedID.getMomentless());
        try {
            @SuppressWarnings("unused")
            QueryResult result = _services.retrieve(query);
        } catch (ConnectionException e) {
            if (404 == e.getServerResponseCode())
                System.out.println("Story has been deleted: " + story.getOid().getMomentless());
        }

        /***** OUTPUT *****
         Story has been deleted: Story:1049
         ******************/

        return deletedID;
    }

    public Asset CloseAsset() throws Exception {
        Asset story = AddNewAsset();
        IOperation closeOperation = _metaModel.getOperation("Story.Inactivate");
        Oid closeID = _services.executeOperation(closeOperation, story.getOid());

        Query query = new Query(closeID.getMomentless());
        IAttributeDefinition assetState = _metaModel.getAttributeDefinition("Story.AssetState");
        query.getSelection().add(assetState);
        QueryResult result = _services.retrieve(query);
        Asset closeStory = result.getAssets()[0];
        AssetState state = AssetState.valueOf((Integer) closeStory.getAttribute(assetState).getValue());

        System.out.println(closeStory.getOid());
        System.out.println(state.toString());
        /***** OUTPUT *****
         Story:1050
         Closed
         ******************/

        return closeStory;
    }

    public Asset ReOpenAsset() throws Exception {
        Asset story = CloseAsset();
        IOperation activateOperation = _metaModel.getOperation("Story.Reactivate");
        Oid activeID = _services.executeOperation(activateOperation, story.getOid());

        Query query = new Query(activeID.getMomentless());
        IAttributeDefinition assetState = _metaModel.getAttributeDefinition("Story.AssetState");
        query.getSelection().add(assetState);
        QueryResult result = _services.retrieve(query);
        Asset activeStory = result.getAssets()[0];
        @SuppressWarnings("unused")
        AssetState state = AssetState.valueOf(((Integer) activeStory.getAttribute(assetState).getValue()).intValue());

        System.out.println(activeStory.getOid());
        /***** OUTPUT *****
         Story:1051
         Active
         ******************/

        return activeStory;
    }

    public Asset createStoryThroughProxy(String storyName, String projectToken) throws APIException, URISyntaxException, ConnectionException, OidException {
        IAttributeDefinition nameDef = _servicesWithProxy.getAttributeDefinition("Story.Name");
        IAttributeDefinition scopeDef = _servicesWithProxy.getAttributeDefinition("Story.Scope");

        final Asset asset = new Asset(_servicesWithProxy.getAssetType("Story"));
        asset.setAttributeValue(nameDef, storyName);
        asset.setAttributeValue(scopeDef, _servicesWithProxy.getOid(projectToken));
        _servicesWithProxy.save(asset);
        return asset;
    }


    IMetaModel getMetaModel() {
        return _metaModel;
    }

    IServices getServices() {
        return _services;
    }


    private static String GetNewName() {
        return java.util.UUID.randomUUID().toString(); // Guid.NewGuid().toString();
    }

    private String GetNextSourceID(String oldSource) {
        if (oldSource == "StorySource:148") return "StorySource:149";
        if (oldSource == "StorySource:149") return "StorySource:150";
        return "StorySource:148";
    }

    private String getOwnerToAdd(List<Object> oids) {
        for (Object o : oids.toArray()) {
            Oid oid = (Oid) o;
            if (oid.getToken() == "Member:3722") return "Member:3710";
        }
        return "Member:3722";
    }

    private String getOwnerToRemove(List<Object> oids) {
        for (Object o : oids.toArray()) {
            Oid oid = (Oid) o;
            if (oid.getToken() == "Member:3722") return "Member:3722";
        }
        return "Member:3710";
    }
}


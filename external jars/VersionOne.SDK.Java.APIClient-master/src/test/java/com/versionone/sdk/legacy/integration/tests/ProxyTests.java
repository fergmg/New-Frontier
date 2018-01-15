package com.versionone.sdk.legacy.integration.tests;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.versionone.apiclient.MetaModel;
import com.versionone.apiclient.ProxyProvider;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.Services;
import com.versionone.apiclient.V1APIConnector;
import com.versionone.apiclient.exceptions.APIException;
import com.versionone.apiclient.exceptions.ConnectionException;
import com.versionone.apiclient.exceptions.OidException;
import com.versionone.apiclient.interfaces.IAPIConnector;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IMetaModel;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.QueryResult;

@Ignore("This test requires a proxy server.")
public class ProxyTests {

	private final static String proxyAddress = "http://proxy:3128";
	private final static String proxyUserName = "user";
	private final static String proxyPassword = "password";

	private final static String V1Url = APIClientLegacyIntegrationTestSuiteIT.getInstanceUrl().getV1Url();
	private final static String V1UserName = APIClientLegacyIntegrationTestSuiteIT.getInstanceUrl().getV1UserName();
	private final static String V1Password = APIClientLegacyIntegrationTestSuiteIT.getInstanceUrl().getV1Password();
	private final static String METAV1 = APIClientLegacyIntegrationTestSuiteIT.getInstanceUrl().getMetaUrl();
	private final static String RESTV1 = APIClientLegacyIntegrationTestSuiteIT.getInstanceUrl().getDataUrl();


	@Test
	public void testGetProjectList() throws URISyntaxException, ConnectionException, APIException, OidException {
		URI proxy = new URI(proxyAddress);
		ProxyProvider proxyProvider = new ProxyProvider(proxy, proxyUserName, proxyPassword);

		//Connection with proxy
		IAPIConnector metaConnectorWithProxy = new V1APIConnector(V1Url + METAV1, proxyProvider);
        IMetaModel metaModelWithProxy = new MetaModel(metaConnectorWithProxy);
        IAPIConnector dataConnectorWithProxy = new V1APIConnector(V1Url + RESTV1, V1UserName, V1Password, proxyProvider);
        IServices servicesWithProxy = new Services(metaModelWithProxy, dataConnectorWithProxy);

        //Connection without proxy
		IAPIConnector metaConnector = new V1APIConnector(V1Url + METAV1);
        IMetaModel metaModel = new MetaModel(metaConnector);
        IAPIConnector dataConnector = new V1APIConnector(V1Url + RESTV1, V1UserName, V1Password);
        IServices services = new Services(metaModel, dataConnector);

        IAssetType projectTypeWithProxy = metaModelWithProxy.getAssetType("Scope");
        Query scopeQueryWithProxy = new Query(projectTypeWithProxy);
        QueryResult resultWithProxy = servicesWithProxy.retrieve(scopeQueryWithProxy);

        IAssetType projectType = metaModel.getAssetType("Scope");
        Query scopeQuery = new Query(projectType);
        QueryResult result = services.retrieve(scopeQuery);

        Assert.assertEquals(result.getAssets().length, resultWithProxy.getAssets().length);

	}
}

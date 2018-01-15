package com.versionone.sdk.legacy.integration.tests;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.versionone.apiclient.Connectors;
import com.versionone.apiclient.EnvironmentContext;
import com.versionone.apiclient.ModelsAndServices;
import com.versionone.apiclient.V1Configuration;
import com.versionone.apiclient.interfaces.IConnectors;
import com.versionone.apiclient.interfaces.ICredentials;
import com.versionone.apiclient.interfaces.IMetaModel;
import com.versionone.apiclient.interfaces.IModelsAndServices;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.interfaces.IUrls;

public class EnvironmentContextTests {

    private EnvironmentContext _defaultTarget;
    private EnvironmentContext _nonDefaultTarget;

    @Before
    public void setup() throws Exception {

        _defaultTarget = new EnvironmentContext();

        IUrls urls = new IUrls() {
            public String getV1Url() {
                return "http://google.com/";
            }

            public String getMetaUrl() {
                return "/blah1.1/";
            }

            public String getDataUrl() {
                return "/jimmy2.2/";
            }

            public String getProxyUrl() {
                return "http://wwww.myproxy.com/";
            }

            public String getConfigUrl() {
                return "http://www14.v1.com/config1.1/";
            }
        };

        ICredentials credentials = new ICredentials() {
            public String getV1UserName() {
                return "NA";
            }

            public String getV1Password() {
                return "NA";
            }

            public String getProxyUserName() {
                return "fred";
            }

            public String getProxyPassword() {
                return "Wx7123456Wx7";
            }
        };

        //can implement your own IConnector if needed.
        IConnectors connectors = new Connectors(urls, credentials);
        IModelsAndServices modelsAndServices = new ModelsAndServices(connectors);

        _nonDefaultTarget = new EnvironmentContext(modelsAndServices);

    }

    //retrieve meta model using environmentContext
    @Test
    public void testGetMetaModel(){
        IMetaModel model = _defaultTarget.getMetaModel();
        Assert.assertNotNull(model);
        model = _nonDefaultTarget.getMetaModel();
        Assert.assertNotNull(model);
    }

    //retrieve services using environmentContext
    @Test
    public void testGetServices(){
        IServices services = _defaultTarget.getServices();
        Assert.assertNotNull(services);
        services = _nonDefaultTarget.getServices();
        Assert.assertNotNull(services);
    }

    @Test
    public void testGetMetaModelWithProxy() throws URISyntaxException {
        IMetaModel model = _defaultTarget.getMetaModelWithProxy();
        Assert.assertNotNull(model);
        model = _nonDefaultTarget.getMetaModelWithProxy();
        Assert.assertNotNull(model);
    }

    @Test
    public void testGetServicesWithProxy() throws URISyntaxException {
        IServices services = _defaultTarget.getServicesWithProxy();
        Assert.assertNotNull(services);
        services = _nonDefaultTarget.getServicesWithProxy();
        Assert.assertNotNull(services);
    }

    @Test
    public void testGetV1Configuration() {
        V1Configuration config = _defaultTarget.getV1Configuration();
        Assert.assertNotNull(config);
        config = _nonDefaultTarget.getV1Configuration();
        Assert.assertNotNull(config);
    }

    @Test
    public void testGetV1ConfigurationWithProxy() throws URISyntaxException {
        V1Configuration config = _defaultTarget.getV1ConfigurationWithProxy();
        Assert.assertNotNull(config);
        config = _nonDefaultTarget.getV1ConfigurationWithProxy();
        Assert.assertNotNull(config);
    }

    @SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
    public void testNullModelsAndServices(){
        EnvironmentContext context = new EnvironmentContext(null);
    }

}

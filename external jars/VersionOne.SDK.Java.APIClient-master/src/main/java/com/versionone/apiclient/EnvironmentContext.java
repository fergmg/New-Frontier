package com.versionone.apiclient;

import java.net.URISyntaxException;

import com.versionone.apiclient.interfaces.IMetaModel;
import com.versionone.apiclient.interfaces.IModelsAndServices;
import com.versionone.apiclient.interfaces.IServices;

/**
 * @deprecated This class has been deprecated. Please use V1Connector instead. 
 */
@Deprecated
public final class EnvironmentContext {

    private IModelsAndServices _modelsAndServices;

    public EnvironmentContext() throws Exception {
        _modelsAndServices = new ModelsAndServices();
    }

    //inject your own IModelsAndServices implementation if necessary
    public EnvironmentContext(IModelsAndServices modelsAndServices){
        if (null == modelsAndServices) throw new IllegalArgumentException("modelsAndServices cannot be null");
        _modelsAndServices = modelsAndServices;
    }

    public IMetaModel getMetaModel(){
        return _modelsAndServices.getMetaModel();
    }

    public IMetaModel getMetaModelWithProxy() throws URISyntaxException {
        return _modelsAndServices.getMetaModelWithProxy();
    }

    public IServices getServices(){
        return _modelsAndServices.getServices();
    }

    public IServices getServicesWithProxy() throws URISyntaxException {
        return _modelsAndServices.getServicesWithProxy();
    }

    public V1Configuration getV1Configuration(){
        return _modelsAndServices.getV1Configuration();
    }

    public V1Configuration getV1ConfigurationWithProxy() throws URISyntaxException {
        return _modelsAndServices.getV1ConfigurationWithProxy();
    }

}

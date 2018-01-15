package com.versionone.apiclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.versionone.Oid;
import com.versionone.apiclient.exceptions.APIException;
import com.versionone.apiclient.exceptions.ConnectionException;
import com.versionone.apiclient.exceptions.MetaException;
import com.versionone.apiclient.exceptions.OidException;
import com.versionone.apiclient.filters.AndFilterTerm;
import com.versionone.apiclient.filters.FilterTerm;
import com.versionone.apiclient.filters.IFilterTerm;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IMetaModel;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.QueryResult;

public class RequiredFieldValidator {
    private final Map<IAssetType, List<IAttributeDefinition>> requiredFields;
    private final IMetaModel metaModel;
    private final IServices services;

    public RequiredFieldValidator(IMetaModel metaModel, IServices services) {
        requiredFields = new HashMap<IAssetType, List<IAttributeDefinition>>();
        this.metaModel = metaModel;
        this.services = services;
    }

    /**
     * Validate single Asset attribute. If attribute is not loaded, it is just considered invalid.
     *
     * @param asset 				- Asset to validate
     * @param attributeDefinition 	- Attribute definition of validated attribute
     * @return Validation result
     * @throws APIException - APIException
     * @throws OidException - OidException
     * @throws ConnectionException - ConnectionException
     */
    public boolean validate(Asset asset, IAttributeDefinition attributeDefinition) throws APIException, ConnectionException, OidException {
        getRequiredFields(asset.getAssetType());
        asset.ensureAttribute(attributeDefinition);
        Attribute attribute = asset.getAttribute(attributeDefinition);

        boolean result = attribute != null && !(isMultiValueAndUnfilled(attribute) || isSingleValueAndUnfilled(attribute));

        if (!result) {
        	result = !attribute.hasChanged() && !isAttributeUnfilledOnServer(asset, attributeDefinition);
        }
        return result;
    }

    /**
	 *	Validate all available Asset attributes.
     *	If there are required attributes that haven't been loaded, these are considered failures.
     *	In some cases, attributes could exist on server and have valid values, but it's up to user to retrieve them or ignore corresponding validation errors.
     *
     *  @param asset	- Asset to validate
     *  @return Collection of attribute definitions for attributes which values do not pass validation
     * @throws APIException - APIException
     * @throws OidException - OidException
     * @throws ConnectionException - ConnectionException
     */
    public List<IAttributeDefinition> validate(Asset asset) throws APIException, ConnectionException, OidException {
        getRequiredFields(asset.getAssetType());
        List<IAttributeDefinition> results = new ArrayList<IAttributeDefinition>();
        List<IAttributeDefinition> requiredAttributes = requiredFields.get(asset.getAssetType());

        for (IAttributeDefinition attributeDefinition : requiredAttributes) {
        	asset.ensureAttribute(attributeDefinition);
            Attribute attribute = asset.getAttribute(attributeDefinition);

            if(attribute == null || !validate(asset, attributeDefinition)) {
                results.add(attributeDefinition);
            }
        }

        return results;
    }

    /**
     * Validate collection of Assets.
     *
     * @param assets - Assets collection to check.
     * @return Validation results dictionary, each entry value key is Asset, and value is output of @seeValidate(Asset)
     * @throws APIException - APIException
     * @throws OidException - OidException
     * @throws ConnectionException - ConnectionException
     */
    public Map<Asset, List<IAttributeDefinition>> validate(Asset[] assets) throws APIException, ConnectionException, OidException {
        Map<Asset, List<IAttributeDefinition>> results = new HashMap<Asset, List<IAttributeDefinition>>();

        for (Asset asset : assets) {
            results.put(asset, validate(asset));
        }

        return results;
    }

    /**
     * Check whether the attribute corresponding to the attributeName is required in current VersionOne server setup.
     *
     * @param assetType		- Asset type.
     * @param attributeName - Attribute name.
     * @return is specified attribute required for specified asset.
     * @throws MetaException - MetaException
     * @throws OidException  - OidException
     * @throws APIException - APIException
     * @throws ConnectionException - ConnectionException
     */
    public boolean isRequired(IAssetType assetType, String attributeName) throws ConnectionException, APIException, OidException, MetaException {
        return isRequired(assetType.getAttributeDefinition(attributeName));
    }

    /**
     * Check whether the provided attribute is required in current VersionOne server setup.
     *
     * @param definition - Attribute definition.
     * @return is specified attribute definition required.
     * @throws MetaException - MetaException
     * @throws OidException - OidException
     * @throws APIException - APIException
     * @throws ConnectionException - ConnectionException
     */
    public boolean isRequired(IAttributeDefinition definition) throws ConnectionException, APIException, OidException, MetaException {
        getRequiredFields(definition.getAssetType());
        return isRequiredField(definition.getAssetType(), definition.getName());
    }

    /**
     * Get required fields for asset type.
     *
     * @param assetType - Asset type.
     * @throws OidException
     * @throws APIException
     * @throws ConnectionException
     */
    private void getRequiredFields(IAssetType assetType) throws ConnectionException, APIException, OidException {
        if (!requiredFields.containsKey(assetType)) {
            List<IAttributeDefinition> requiredFieldsForType = loadRequiredFields(assetType);
            requiredFields.put(assetType, requiredFieldsForType);
        }
    }

    /**
     * Load required fields attribute definitions for provided Asset type.
     * 
     * @param assetType - Asset type.
     * @return Collection of attribute definitions for required fields.
     * @throws OidException
     * @throws APIException
     * @throws ConnectionException
     */
    private List<IAttributeDefinition> loadRequiredFields(IAssetType assetType) throws ConnectionException, APIException, OidException {
        List<IAttributeDefinition> requiredFieldsForType = new ArrayList<IAttributeDefinition>();

        IAssetType attributeDefinitionAssetType = metaModel.getAssetType("AttributeDefinition");
        IAttributeDefinition nameAttributeDef = attributeDefinitionAssetType.getAttributeDefinition("Name");
        IAttributeDefinition assetNameAttributeDef =
            attributeDefinitionAssetType.getAttributeDefinition("Asset.AssetTypesMeAndDown.Name");

        Query query = new Query(attributeDefinitionAssetType);
        query.getSelection().add(nameAttributeDef);
        FilterTerm assetTypeTerm = new FilterTerm(assetNameAttributeDef);
        assetTypeTerm.equal(assetType.getToken());
        query.setFilter(new AndFilterTerm(new IFilterTerm[] { assetTypeTerm }));

        QueryResult result = services.retrieve(query);

        for (Asset asset : result.getAssets()) {
        	Attribute attr = asset.getAttribute(nameAttributeDef);
        	if (attr == null) {
        		continue;
        	}
            String name = (String)attr.getValue();
            if (isRequiredField(assetType, name)) {
                IAttributeDefinition definition = assetType.getAttributeDefinition(name);
                requiredFieldsForType.add(definition);
            }
        }

        return requiredFieldsForType;
    }

    private boolean isRequiredField(IAssetType assetType, String name) {
        IAttributeDefinition def = assetType.getAttributeDefinition(name);
        return def.isRequired() && !def.isReadOnly();
    }

    private boolean isSingleValueAndUnfilled(Attribute attribute) throws APIException {
        return !attribute.getDefinition().isMultiValue() &&
                ((attribute.getValue() instanceof Oid && ((Oid)attribute.getValue()).isNull()) ||
                 attribute.getValue() == null);
    }

    private boolean isMultiValueAndUnfilled(Attribute attribute) {
        return (attribute.getDefinition().isMultiValue() && attribute.getValues().length < 1);
    }

    private boolean isAttributeUnfilledOnServer(Asset asset, IAttributeDefinition attributeDefinition) {
    	if (asset.getOid() == Oid.Null) {
    		return true;
    	}
        Query query = new Query(asset.getOid());
        query.getSelection().add(attributeDefinition);
        QueryResult result = null;
    	try {
    		result = services.retrieve(query);
		} catch (Exception e) {
			//do nothing
		}

		if(result != null) {
			Attribute attr = result.getAssets()[0].getAttribute(attributeDefinition);
			try {
				return isSingleValueAndUnfilled(attr) && isMultiValueAndUnfilled(attr);
			} catch (APIException e) {
				// do nothing
			}
		}

		return true; // there is no data on the server.
    }
}

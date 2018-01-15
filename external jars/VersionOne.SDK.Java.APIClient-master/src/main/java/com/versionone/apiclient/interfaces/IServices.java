package com.versionone.apiclient.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

import com.versionone.Oid;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.V1Connector;
import com.versionone.apiclient.exceptions.APIException;
import com.versionone.apiclient.exceptions.ConnectionException;
import com.versionone.apiclient.exceptions.MetaException;
import com.versionone.apiclient.exceptions.OidException;
import com.versionone.apiclient.exceptions.V1Exception;
import com.versionone.apiclient.services.QueryResult;

/**
 * Methods implemented by a class providing VersionOne services.
 */
public interface IServices {

	/**
	 * Returns the type information for an asset specified in a token.
	 * 
	 * @param token The token to process
	 * @return IAssetType Based on the token
	 * @throws MetaException if the token is invalid
	 */
	IAssetType getAssetType(String token) throws MetaException;

	/**
	 * Returns the type information for an attribute specified in a token.
	 * 
	 * @param token The token to process
	 * @return IAttributeDefinition Based on the token
	 * @throws MetaException if the token is invalid
	 */
	IAttributeDefinition getAttributeDefinition(String token) throws MetaException;

	/**
	 * Returns the type information for an operation specified in a token.
	 * 
	 * @param token The token to process
	 * @return IOperation Based on the token
	 * @throws MetaException if the token is invalid
	 */
	IOperation getOperation(String token) throws MetaException;

	/**
	 * Returns an object identifier for the specified token.
	 * 
	 * @param token The token to process
	 * @return Object Identifier
	 * @throws OidException if the token is invalid
	 */
	Oid getOid(String token) throws OidException;

	/**
	 * Executes a query and returns the results.
	 * 
	 * @param query The query to execute.
	 * @return QueryResult of the query
	 * @throws ConnectionException - if connection to VersionOne down
	 * @throws OidException - if there is problem related to Object Identifiers
	 * @throws APIException if there is a problem executing the query
	 */
	QueryResult retrieve(Query query) throws ConnectionException, APIException, OidException;

	/**
	 * Persists changes to an asset without a comment.
	 * 
	 * @param asset The asset that changed
	 * @throws ConnectionException - if connection to VersionOne down
	 * @throws APIException - if the save fails
	 */
	void save(Asset asset) throws APIException, ConnectionException;

	/**
	 * Persists changes to as asset with a comment.
	 * 
	 * @param asset The asset that changed
	 * @param comment - comment
	 * @throws ConnectionException - if connection to VersionOne down
	 * @throws APIException - if the save fails
	 */
	void save(Asset asset, String comment) throws APIException, ConnectionException;

	/**
	 * Saves multiple assets without a comment.
	 * 
	 * @param assetList An array of assets to save
	 * @throws V1Exception - if the save fails
	 */
	void save(Asset[] assetList) throws V1Exception;

	/**
	 * Determines the OID of the currently logged in user.
	 * 
	 * @return Object Identifier for current user
	 * @throws ConnectionException - if connection to VersionOne down
	 * @throws OidException - if there is problem related to Object Identifiers
	 * @throws APIException - if there is any other error
	 */
	Oid getLoggedIn() throws APIException, ConnectionException, OidException;

	/**
	 * Creates a new asset.
	 * 
	 * @param assetType The type of asset to create
	 * @param context - context under which to create the asset
	 * @return New Asset
	 * @throws V1Exception - when the create fails
	 */
	Asset createNew(IAssetType assetType, Oid context) throws V1Exception;

	/**
	 * Executes an operation.
	 * 
	 * @param op The operation to execute
	 * @param oid - OID of object on which method is executed
	 * @return Oid result of operation
	 * @throws APIException - when the operation fails
	 */
	Oid executeOperation(IOperation op, Oid oid) throws APIException;

	/**
	 * Returns the MetaModel.
	 * 
	 * @return IMetaModel
	 */
	IMetaModel getMeta();

	/**
	 * Returns the V1Connector.
	 * 
	 * @return V1Connector
	 */
	V1Connector getV1Connector();

	/**
	 * Executes a Query API query using in JSON or YAML format.
	 * 
	 * @param query The query JSON or YAML query string
	 * @return String
	 */
	String executePassThroughQuery(String query);

	/**
	 * Returns a localization value based on an attribute definition.
	 * 
	 * @param attribute An attribute definition
	 * @return String String
	 * @throws V1Exception V1Exception
	 */
	String getLocalization(IAttributeDefinition attribute) throws V1Exception;

	/**
	 * Returns a localization value based on key.
	 * 
	 * @param key String A string value of the key
	 * @return String String
	 * @throws V1Exception V1Exception
	 */
	String getLocalization(String key) throws V1Exception;

	/**
	 * Returns a Map of localized values.
	 * 
	 * @param attributes attributes
	 * @return Map Map of strings
	 * @throws ConnectionException ConnectionException
	 */
	Map<String, String> getLocalization(ArrayList<IAttributeDefinition> attributes) throws ConnectionException;

	/**
	 * Saves an attachment to the specified asset.
	 * 
	 * @param filePath Path and name of the attachment file.
	 * @param asset Asset to save the attachment to.
	 * @param attachmentName The name of the attachment.
	 * @return Oid
	 * @throws V1Exception V1Exception
	 * @throws IOException IOException
	 */
	Oid saveAttachment(String filePath, Asset asset, String attachmentName) throws V1Exception, IOException;

	/**
	 * Returns the attachment data for the specified attachment Oid.
	 * 
	 * @param attachmentOid The Oid of the attachment to return.
	 * @return Reader
	 * @throws V1Exception V1Exception
	 */
	InputStream getAttachment(Oid attachmentOid) throws V1Exception;

	/**
	 * Saves an embedded image to the specified asset.
	 * 
	 * @param filePath Path and name of the embedded image file.
	 * @param asset Asset to save the embedded image to.
	 * @return Oid
	 * @throws V1Exception V1Exception
	 * @throws IOException IOException
	 */
	Oid saveEmbeddedImage(String filePath, Asset asset) throws V1Exception, IOException;

	/**
	 * Returns the embedded image data for the specified attachment Oid.
	 * 
	 * @param embeddedImageOid The Oid of the embedded image to return.
	 * @return Reader
	 * @throws V1Exception V1Exception
	 */
	Reader getEmbeddedImage(Oid embeddedImageOid) throws V1Exception;

}

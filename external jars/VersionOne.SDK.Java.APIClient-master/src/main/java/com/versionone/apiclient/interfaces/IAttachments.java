package com.versionone.apiclient.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

import com.versionone.apiclient.exceptions.AttachmentLengthException;
import com.versionone.apiclient.exceptions.ConnectionException;

/**
 * @deprecated This interface has been deprecated. Please use methods of the IServices interface instead. 
 */
@Deprecated
public interface IAttachments {

	/**
	 * Getting reader
	 *
	 * @param key path to the data on server
	 * @return the stream for reading data
	 * @throws ConnectionException if any connection problems occur
	 */
	InputStream getReader(String key) throws ConnectionException;;

	/**
	 * Getting writer
	 *
	 * @param key path to the data on server
	 * @param contentType Content-type of HTTP header
	 * @return the stream for writing data
	 * @throws ConnectionException if any connection problems occur
	 */
	OutputStream getWriter(String key, String contentType) throws ConnectionException;

	/**
	 * Setting Writer
	 *
	 * @param key path to the data on server
	 * @throws ConnectionException if any problem appears with connection to
	 *             server
	 * @throws AttachmentLengthException attachment too big
	 */
	void setWriter(String key) throws ConnectionException, AttachmentLengthException;

}

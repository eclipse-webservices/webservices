/*
 * Created on Nov 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.wst.wsi.internal.log;

import org.eclipse.wst.wsi.internal.document.DocumentElement;

/**
 * @author lauzond
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface MimePart extends DocumentElement {

	/**
	 * Gets the content for the mime part.
	 * 
	 * @return the mime content.
	 * @see #setContent
	 */
	public String getContent();

	/**
	 * Sets the content for the mime part.
	 * 
	 * @param mimeContent
	 *            the mime content.
	 * @see #getContent
	 */
	public void setContent(String mimeContent);

	/**
	 * Gets the headers for the mime part.
	 * 
	 * @return the mime headers.
	 * @see #setMimeHeaders
	 */
	public String getHeaders();

	/**
	 * Sets the headers for the mime part.
	 * 
	 * @param mimeHeaders
	 *            the mime headers.
	 * @see #getHeaders
	 */
	public void setHeaders(String mimeHeaders);

	/**
	 * Gets the boundary strings for the mime part. Note that the last part of a
	 * multipart/ related message will have 2 boundary strings. All other parts
	 * will have one.
	 * 
	 * @return the boundary strings.
	 * @see #setBoundaryStrings
	 */
	public String[] getBoundaryStrings();

	/**
	 * Sets the boundary strings for the mime part. Note that the last part of a
	 * multipart/ related message will have 2 boundary strings. All other parts
	 * will have one.
	 * 
	 * @param boundaryStrings
	 *            the boundary strings.
	 * @see #getBoundaryStrings
	 */
	public void setBoundaryStrings(String[] mimeHeaders);

	public String toXMLString(String namespaceName);
}

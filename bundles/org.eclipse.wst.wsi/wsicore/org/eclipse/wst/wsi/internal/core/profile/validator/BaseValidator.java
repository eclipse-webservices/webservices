/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator;

import org.eclipse.wst.wsi.internal.core.WSIException;

import org.w3c.dom.Document;

/**
 * The base interface for the profile validator.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface BaseValidator
{
  /**
   * Set all test assertions for an artifact to missingInput.
   * @throws WSIException if there is any problems while processing.
   */
  public void setAllMissingInput() throws WSIException;

  /**
   * Cleanup after processing all of the test assertions for an artifact.
   * @throws WSIException if there is any problems during cleanup.
   */
  public void cleanup() throws WSIException;

  /**
     * Parse XML document and validate with a schema document.
     * @param urlString XML document location.
     * @param baseURI a base url to assist in locating the XML document.
     * @param schema the related XML schema.
     * @return XML document.
     * @throws WSIException if there are any problems while parsing or 
     *         validating the XML document.
     */
  public Document parseXMLDocumentURL(
    String urlString,
    String baseURI,
    String schema)
    throws WSIException;
}

/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.report;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;

/**
 * An artifact reference.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface ArtifactReference extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_ARTIFACT_REFERENCE;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_REPORT, ELEM_NAME);

  /**
   * Get timestamp.
   * @return timestamp.
   * @see #setTimestamp
   */
  public String getTimestamp();

  /**
   * Set timestamp.
   * @param timestamp a timestamp.
   * @see #getTimestamp
   */
  public void setTimestamp(String timestamp);

  /**
   * Get document element.
   * @return document element.
   * @see #setDocumentElement
   */
  public DocumentElement getDocumentElement();

  /**
   * Set document element.
   * @param documentElement document element.
   * @param namespaceName   namespace prefix.
   * @see #getDocumentElement
   */
  public void setDocumentElement(
    DocumentElement documentElement,
    String namespaceName);
}

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
package org.eclipse.wst.wsi.internal.core.document;

/**
 * Defines the interface used to read the Conformance XML documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface DocumentReader
{
  /**
   * Get document location.
   * @return document location.
   * @see #setLocation
   */
  public String getLocation();

  /**
   * Set document location.
   * @param documentURI document location.
   * @see #getLocation
   */
  public void setLocation(String documentURI);
}

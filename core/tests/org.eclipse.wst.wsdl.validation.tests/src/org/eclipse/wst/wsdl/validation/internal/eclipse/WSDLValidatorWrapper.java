/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.eclipse;

import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;

/**
 * Wrapper for the Eclipse WSDL validator class to allow for testing.
 */
public class WSDLValidatorWrapper extends WSDLValidator 
{
  /**
   * Constructor.
   */
  public WSDLValidatorWrapper()
  {
	super();
  }
  
  /**
   * Get the URI resolver registered on the WSDL validator.
   * 
   * @return The URI resolver registered on the WSDL validator.
   */
  public URIResolver getURIResolver()
  {
	return uriResolver;
  }
}

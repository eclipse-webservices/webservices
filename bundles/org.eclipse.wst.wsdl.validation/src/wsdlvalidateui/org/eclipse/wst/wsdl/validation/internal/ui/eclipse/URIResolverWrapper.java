/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.ui.eclipse;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.common.uriresolver.URIResolver;
import org.eclipse.wst.common.uriresolver.URIResolverPlugin;
import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolver;

/**
 * An wrapper URI resolver that wraps the Web Standard Tools URI resolver
 * in a WSDL validator URI resolver.
 */
public class URIResolverWrapper implements IURIResolver
{
  /**
   * Constructor.
   */
  public URIResolverWrapper()
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.internal.resolver.IURIResolver#resolve(java.lang.String, java.lang.String, java.lang.String)
   */
  public String resolve(String baseLocation, String publicId, String systemId)
  {
    URIResolver resolver = URIResolverPlugin.createResolver();
    String result = resolver.resolve(baseLocation, publicId, systemId);
    try
    {
      URL tempURL = new URL(result);
      tempURL = Platform.resolve(tempURL);
      result = tempURL.toString();
    }
    catch(Exception e)
    {
      result = null;
    }
    // If the result is the same as the request return null.
    if (result != null && (result.equals(systemId)))
    {
      result = null;
    }

    return result;
  }
}
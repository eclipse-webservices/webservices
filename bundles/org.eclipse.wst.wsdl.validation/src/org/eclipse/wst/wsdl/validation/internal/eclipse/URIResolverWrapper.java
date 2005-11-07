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

package org.eclipse.wst.wsdl.validation.internal.eclipse;

import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolver;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.wsdl.validation.internal.resolver.IExtensibleURIResolver;
import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult;

/**
 * An wrapper URI resolver that wraps the Web Standard Tools URI resolver
 * in a WSDL validator URI resolver.
 */
public class URIResolverWrapper implements IExtensibleURIResolver
{
  /**
   * Constructor.
   */
  public URIResolverWrapper()
  {
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.resolver.IExtensibleURIResolver#resolve(java.lang.String, java.lang.String, java.lang.String, org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult)
   */
  public void resolve(String baseLocation, String publicId, String systemId, IURIResolutionResult result)
  {
    URIResolver resolver = URIResolverPlugin.createResolver();
    String location = null;
    if (publicId != null || systemId != null)
    {  
      location = resolver.resolve(baseLocation, publicId, systemId);
    }  
    
    if (location != null)
    {       
      result.setLogicalLocation(location);
      String physical = resolver.resolvePhysicalLocation(baseLocation, publicId, location);
      if(physical != null)
      {
        result.setPhysicalLocation(physical);
      }
      else
      {
        result.setPhysicalLocation(location);
      }
    }
  }
}
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolver;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolverDelegate;

/**
 * Read extension URI resolvers.
 * 
 *  <extension
 *     point="org.eclipse.wst.wsdl.validation.uriresolver">
 *    <uriresolver
 *       class="org.eclipse.wst.wsdl.validation.someclass"/>
 *   </extension>
 *  
 */
class URIResolverRegistryReader
{
  protected static final String PLUGIN_ID = "org.eclipse.wst.wsdl.validation";
  protected static final String EXTENSION_POINT_ID = "uriresolver";
  protected static final String ATT_CLASS = "class";
  protected String tagName;

  /**
   * Constructor.
   */
  public URIResolverRegistryReader()
  {
  }

  /**
   * Read from plugin registry and handle the configuration elements that match
   * the spedified elements.
   */
  public List readRegistry()
  {
  	List resolverList = new ArrayList();
    IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
    IExtensionPoint point = pluginRegistry.getExtensionPoint(PLUGIN_ID, EXTENSION_POINT_ID);
    if (point != null)
    {
      IConfigurationElement[] elements = point.getConfigurationElements();
      for (int i = 0; i < elements.length; i++)
      {
        IURIResolver resolver = readElement(elements[i]);
        if(resolver != null)
        {
          resolverList.add(resolver);
        }
      }
    }
    return resolverList;
  }

  /**
   * Parse and deal with the extension points.
   * 
   * @param element The extension point element.
   */
  protected IURIResolver readElement(IConfigurationElement element)
  {
    if (element.getName().equals(EXTENSION_POINT_ID))
    {
      String resolverClass = element.getAttribute(ATT_CLASS);

      if (resolverClass != null)
      {
        try
        {
          ClassLoader pluginLoader =
            element.getDeclaringExtension().getDeclaringPluginDescriptor().getPluginClassLoader();
            
          return new URIResolverDelegate(resolverClass, pluginLoader).getURIResolver();
        }
        catch (Exception e)
        {
        }
      }
    }
    return null;
  }
}

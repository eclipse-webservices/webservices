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
package org.eclipse.wst.wsdl.internal.extensibility;
import java.net.URL;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.wsdl.WSDLPlugin;


public class ExtensibilityElementFactoryRegistryReader
{
  protected static final String PLUGIN_ID = WSDLPlugin.getPlugin().getDescriptor().getUniqueIdentifier();
  protected static final String EXTENSION_POINT_ID = "extensibilityElementFactories";
  protected static final String ELEMENT_NAME = "extensibilityElementFactory";
  protected static final String ATT_NAMESPACE = "namespace";
  protected static final String ATT_CLASS = "class";
  protected ExtensibilityElementFactoryRegistry extensibilityElementFactoryRegistry;

  public ExtensibilityElementFactoryRegistryReader(ExtensibilityElementFactoryRegistry extensibilityElementFactoryRegistry)
  {
    this.extensibilityElementFactoryRegistry = extensibilityElementFactoryRegistry;
  }

  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry()
  {
    IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
    IExtensionPoint point = pluginRegistry.getExtensionPoint(PLUGIN_ID, EXTENSION_POINT_ID);
    if (point != null)
    {
      IConfigurationElement[] elements = point.getConfigurationElements();
      for (int i = 0; i < elements.length; i++)
      {
        readElement(elements[i]);
      }
    }
  }

  public String resolve(URL platformURL, String relativePath) throws Exception
  {
    URL resolvedURL = Platform.resolve(platformURL);
    return resolvedURL.toString() + relativePath;
  }

  protected void readElement(IConfigurationElement element)
  {
    if (element.getName().equals(ELEMENT_NAME))
    {
      try
      {
        IConfigurationElement childElement = (IConfigurationElement) element;
        String namespace = childElement.getAttribute(ATT_NAMESPACE);
        if (namespace != null)
        {
          ClassLoader pluginClasssLoader = element.getDeclaringExtension().getDeclaringPluginDescriptor().getPluginClassLoader();
          String className = childElement.getAttribute(ATT_CLASS);
          ExtensibilityElementFactoryDescriptor descriptor = new ExtensibilityElementFactoryDescriptor(className,namespace,pluginClasssLoader);
          extensibilityElementFactoryRegistry.put(namespace, descriptor);
        }
      }
      catch (Exception e)
      {
      }
    }
  }
}
/**********************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
  *
 * Contributors:
 *    IBM - Initial API and implementation
 **********************************************************************/

package org.eclipse.wst.wsdl.tests;


import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


/**
 * The main plug-in class to be used in the desktop.
 */
public class WSDLTestsPlugin extends Plugin
{
  //The shared instance.
  private static WSDLTestsPlugin plugin;

  //Resource bundle.
  private ResourceBundle resourceBundle;

  private static Bundle pluginBundle;

  /**
   * The constructor.
   */
  public WSDLTestsPlugin()
  {
    super();
    plugin = this;
  }

  /**
   * This method is called upon plug-in activation
   */
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    pluginBundle = context.getBundle();
  }

  /**
   * This method is called when the plug-in is stopped
   */
  public void stop(BundleContext context) throws Exception
  {
    super.stop(context);
    plugin = null;
    resourceBundle = null;
    pluginBundle = context.getBundle();
  }

  /**
   * Returns the shared instance.
   */
  public static WSDLTestsPlugin getDefault()
  {
    return plugin;
  }

  /**
   * Returns the string from the plugin's resource bundle,
   * or 'key' if not found.
   */
  public static String getResourceString(String key)
  {
    ResourceBundle bundle = WSDLTestsPlugin.getDefault().getResourceBundle();
    try
    {
      return (bundle != null) ? bundle.getString(key) : key;
    }
    catch (MissingResourceException e)
    {
      return key;
    }
  }

  /**
   * Returns the plugin's resource bundle,
   */
  public ResourceBundle getResourceBundle()
  {
    try
    {
      if (resourceBundle == null)
      {
        resourceBundle = ResourceBundle.getBundle("org.eclipse.wst.wsdl.tests.WSDLTestsPluginResources");
      }
    }
    catch (MissingResourceException x)
    {
      resourceBundle = null;
    }
    return resourceBundle;
  }

  /**
   * Get the install URL of this plug-in.
   * 
   * @return the install URL of this plug-in
   */
  public static String getInstallURL()
  {
    try
    {
      return FileLocator.resolve(pluginBundle.getEntry("/")).getFile();
    }
    catch (IOException e)
    {
      return null;
    }
  }
}

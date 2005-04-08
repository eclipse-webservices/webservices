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
package org.eclipse.wst.wsdl.validation.tests.internal;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Lawrence Mandel, IBM
 */
public class WSDLValidatorTestsPlugin extends AbstractUIPlugin
{
  //The shared instance.
  private static WSDLValidatorTestsPlugin plugin;

  //Resource bundle.
  private ResourceBundle resourceBundle;

  /**
   * The constructor.
   */
  public WSDLValidatorTestsPlugin()
  {
    super();
    plugin = this;
//    try
//    {
//      resourceBundle = ResourceBundle.getBundle("org.eclipse.wsdl.validate.tests.WSDLValidatorTestsPluginResources");
//    }
//    catch (MissingResourceException x)
//    {
//      resourceBundle = null;
//    }
  }

  /**
   * This method is called upon plug-in activation
   */
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
  }

  /**
   * This method is called when the plug-in is stopped
   */
  public void stop(BundleContext context) throws Exception
  {
    super.stop(context);
  }

  /**
   * Returns the shared instance.
   */
  public static WSDLValidatorTestsPlugin getDefault()
  {
    return plugin;
  }

  /**
   * Returns the string from the plugin's resource bundle, or 'key' if not
   * found.
   */
  public static String getResourceString(String key)
  {
    ResourceBundle bundle = WSDLValidatorTestsPlugin.getDefault().getResourceBundle();
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
    return resourceBundle;
  }

  /**
   * Return the location of this plugin (ie. where this plugin is installed.)
   * 
   * @return The location of this plugin.
   */
  public static String getPluginLocation()
  {
    try
    {
      URL location = Platform.resolve(getDefault().find(new Path("")));
      return location.toExternalForm();
    }
    catch(IOException e)
    {
    }
    return null;
  }
}
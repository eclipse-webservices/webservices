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

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class WSDLValidatorTestsPlugin extends AbstractUIPlugin
{
  //The shared instance.
  private static WSDLValidatorTestsPlugin plugin;
  private static Bundle pluginBundle = null;

  /**
   * The constructor.
   */
  public WSDLValidatorTestsPlugin()
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
  }

  /**
   * Returns the shared instance.
   */
  public static WSDLValidatorTestsPlugin getDefault()
  {
    return plugin;
  }
  
  /**
   * Get the install URL of this plugin.
   * 
   * @return the install url of this plugin
   */
  public static String getInstallURL()
  {
    try
    {
      return Platform.resolve(pluginBundle.getEntry("/")).getFile();
    }
    catch (IOException e)
    {
      return null;
    }
  }
}
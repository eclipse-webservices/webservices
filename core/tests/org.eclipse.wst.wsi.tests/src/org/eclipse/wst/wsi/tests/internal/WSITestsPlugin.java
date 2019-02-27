/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
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

package org.eclipse.wst.wsi.tests.internal;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
* The main plugin class to be used in the desktop.
*/
public class WSITestsPlugin extends Plugin
{
 //The shared instance.
 private static WSITestsPlugin plugin;
 private static Bundle pluginBundle = null;

 /**
  * The constructor.
  */
 public WSITestsPlugin()
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
 public static WSITestsPlugin getDefault()
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
     return FileLocator.resolve(pluginBundle.getEntry("/")).getFile(); //$NON-NLS-1$
   }
   catch (IOException e)
   {
     return null;
   }
 }
}

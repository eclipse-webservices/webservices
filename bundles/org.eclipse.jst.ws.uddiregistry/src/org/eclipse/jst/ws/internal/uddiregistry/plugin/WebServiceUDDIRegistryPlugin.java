/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.uddiregistry.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.eclipse.EclipseLog;

/**
* This is the plugin class for the Web Services UDDI Registry plugin.
* <p>
* The Web Services UDDI Registry plugin's is to add the
* option to create a unit test UDDI registry 
*/
public class WebServiceUDDIRegistryPlugin extends Plugin
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2002.";
  /**
  * The identifier of the descriptor of this plugin in plugin.xml.
  */
  public static final String ID = "org.eclipse.jst.ws.uddiregistry";

  /**
  * The reference to the singleton instance of this plugin.
  */
  private static WebServiceUDDIRegistryPlugin instance_;
  private Log log_;
  
  /**
  * Constructs a runtime plugin object for this plugin.
  * The "plugin" element in plugin.xml should include the attribute
  * class = "org.eclipse.jst.ws.internal.uddiregistry.plugin.WebServiceUDDIRegistryPlugin".
  * @param descriptor The descriptor of this plugin.
  */
  public WebServiceUDDIRegistryPlugin ( IPluginDescriptor descriptor )
  {
    super(descriptor);
    if (instance_ == null)
    {
      instance_ = this;
    }
    log_ = new EclipseLog();
  }

  /**
  * Returns the singleton instance of this plugin. Equivalent to calling
  * (WebServiceUDDIRegistryPlugin)Platform.getPlugin("org.eclipse.jst.ws.uddiregistry");
  * @return The WebServiceUDDIRegistryPlugin singleton.
  */
  public static WebServiceUDDIRegistryPlugin getInstance ()
  {
    return instance_;
  }

  /**
  * Called once by the platform when this plugin is first loaded.
  * @throws CoreException If this plugin fails to start.
  */
  public void startup () throws CoreException
  {
  	log_.log(Log.INFO, 5072, this, "startup", "Starting plugin org.eclipse.jst.ws.uddiregistry");
    super.startup();
  }

  /**
  * Called once by the platform when this plugin is unloaded.
  * @throws CoreException If this plugin fails to shutdown.
  */
  public void shutdown () throws CoreException
  {
  	log_.log(Log.INFO, 5073, this, "shutdown", "Shutting plugin org.eclipse.jst.ws.uddiregistry");
    super.shutdown();
  }

  /**
  * Returns an image descriptor for the named resource
  * as relative to the plugin install location.
  * @return An image descriptor, possibly null.
  */
  public static ImageDescriptor getImageDescriptor ( String name )
  {
    try
    {
      URL installURL = instance_.getDescriptor().getInstallURL();
      URL imageURL = new URL(installURL,name);
      return ImageDescriptor.createFromURL(imageURL);
    }
    catch (MalformedURLException e)
    {
      return null;
    }
  }

  /**
  * Returns the message string identified by the given key from
  * plugin.properties.
  * @return The String message.
  */
  public static String getMessage ( String key )
  {
    return instance_.getDescriptor().getResourceString(key);
  }

  /**
  * Returns the message string identified by the given key from
  * plugin.properties. Substitution sequences in the message string
  * are replaced by the given array of substitution objects (which
  * are most frequently strings). See the JDK's
  * {@link java.text.MessageFormat java.text.MessageFormat}
  * class for further details on substitution.
  * @return The String message.
  */
  public static String getMessage ( String key, Object[] args )
  {
    return MessageFormat.format(getMessage(key),args);
  }
}

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Hashtable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.ws.internal.explorer.LaunchWizardTask;
import org.eclipse.wst.ws.internal.explorer.favorites.FavoritesRegistryTypeDefault;
import org.osgi.framework.BundleContext;


/**
 * This is the plugin class for the Web Services Explorer.
 */
public class ExplorerPlugin extends AbstractUIPlugin
{
  /**
   * The identifier of the descriptor of this plugin in plugin.xml.
   */
  public static final String ID = "org.eclipse.wst.ws.explorer";
  public static final String CHARSET = "UTF-8";
  /**
   * The reference to the singleton instance of this plugin.
   */
  private static ExplorerPlugin instance_;

  /**
   * Constructs a runtime plugin object for this plugin. The "plugin" element
   * in plugin.xml should include the attribute class =
   * "org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin".
   * 
   * @param descriptor
   *            The descriptor of this plugin.
   */
  public ExplorerPlugin()
  {
    super();
    instance_ = this;
  }

  /**
   * Returns the singleton instance of this plugin. Equivalent to calling
   * (ExplorerPlugin)Platform.getPlugin("org.eclipse.wst.ws.explorer");
   * 
   * @return The ExplorerPlugin singleton.
   */
  static public ExplorerPlugin getInstance()
  {
    return instance_;
  }

  /**
   * Called once by the platform when this plugin is first loaded.
   * 
   * @throws CoreException
   *             If this plugin fails to start.
   */
  public void start(BundleContext bundle) throws Exception
  {
    super.start(bundle);
    // init the shell of LaunchWizardTask
    LaunchWizardTask.getInstance();
  }

  /**
   * Called once by the platform when this plugin is unloaded.
   * 
   * @throws CoreException
   *             If this plugin fails to shutdown.
   */
  public void stop(BundleContext bundle) throws Exception
  {
    super.stop(bundle);
  }

  /**
   * Returns the message string identified by the given key from the
   * plugin.properties file for the appropriate locale.
   * 
   * @param key
   *            The message key string prefixed by a "%" symbol. That is, the
   *            string passed in must be of the form "%KEY" where the
   *            plugin.properties file contains a line of the form: "KEY =
   *            value".
   * @return The locale-specific message.
   */
  public static String getMessage(String key)
  {
    return Platform.getResourceString(instance_.getBundle(), key); 
  }

  /**
   * Returns the message string identified by the given key from the
   * plugin.properties file for the appropriate locale. Substitution sequences
   * in the message string are replaced by the given array of substitution
   * objects (which are most frequently strings). See java.text.MessageFormat
   * for further details on substitution.
   * 
   * @param key
   *            The message key string prefixed by a "%" symbol. That is, the
   *            string passed in must be of the form "%KEY" where the
   *            plugin.properties file contains a line of the form: "KEY =
   *            value".
   * @param args
   *            The substitution values for the message as required by the
   *            message in plugin.properties and by the rules of class
   *            java.text.MessageFormat.
   * @return The locale-specific message.
   */
  public static String getMessage(String key, Object[] args)
  {
    return MessageFormat.format(getMessage(key), args);
  }

  /**
   * Returns an image descriptor for the named resource as relative to the
   * plugin install location.
   * 
   * @return An image descriptor, possibly null.
   */
  public static ImageDescriptor getImageDescriptor(String name)
  {
    try
    {
      URL installURL = instance_.getBundle().getEntry("/");
      URL imageURL = new URL(installURL, name);
      return ImageDescriptor.createFromURL(imageURL);
    }
    catch (MalformedURLException e)
    {
      return null;
    }
  }

  /**
   * See IPluginHelper.
   */
  public void setMsgLoggerConfig(Hashtable msgLoggerConfig)
  {
  }

  /**
   * See IPluginHelper.
   */
  public Hashtable getMsgLoggerConfig(Plugin plugin)
  {
    return new Hashtable();
  }

  /**
   * See IPluginHelper.
   */
  public Hashtable getMsgLoggerConfig()
  {
    return getMsgLoggerConfig(this);
  }

  public String getPluginStateLocation()
  {
    return Platform.getPluginStateLocation(this).addTrailingSeparator().toOSString();
  }

  public String getDefaultFavoritesLocation()
  {
	// TODO: getPluginInstallLocation can return null and cause trouble for the WSE's favourites mechanism. 89101 should correct this.
	return getPluginInstallLocation()+FavoritesRegistryTypeDefault.FAVORITES_DEFAULT;
  }
  
  public String getPluginInstallLocation()
  {
    try
    {
      return Platform.resolve(instance_.getBundle().getEntry("/")).getFile();
    }
    catch (Exception e)
    {
      return null;
    }
  }
}

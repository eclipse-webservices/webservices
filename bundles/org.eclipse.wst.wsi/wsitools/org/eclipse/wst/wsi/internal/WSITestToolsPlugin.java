/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal;

import java.io.IOException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The WS-I test tools plugin.
 * 
 * @author lauzond
 */

public class WSITestToolsPlugin extends AbstractUIPlugin
{
  private static AbstractUIPlugin instance;
  
  /**
   * Constructor for wsiTestToolsPlugin.
   * @param descriptor an IPluginDescriptor object.
   */
  public WSITestToolsPlugin(IPluginDescriptor descriptor)
  {
    super(descriptor);
    instance = this;

    // set the current directory
   WSITestToolsProperties.setInstallDir(getInstallURL());
  }

  /**
   * Method getInstance.
   * @return AbstractUIPlugin
   */
  public static AbstractUIPlugin getInstance()
  {
    return instance;
  }

  /**
   * Method getPlugin.
   * @return WSIToolsUtilPlugin
   */
  public static WSITestToolsPlugin getPlugin()
  {
    return (WSITestToolsPlugin) instance;
  }

  /**
   * Returns installable URL for this plugin.
   * @return installable URL for this plugin.
   */
  public String getInstallURL()
  {
    try
    {
      return Platform.resolve(getDescriptor().getInstallURL()).getFile();
    }
    catch (IOException e)
    {
      return null;
    }
  }
}

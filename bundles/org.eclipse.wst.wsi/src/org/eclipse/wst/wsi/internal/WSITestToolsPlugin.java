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

import org.eclipse.core.runtime.Plugin;

/**
 * The WS-I test tools plugin.
 * 
 * @author lauzond
 */

public class WSITestToolsPlugin extends Plugin
{
  private static Plugin instance;
  protected final String PLUGIN_PROPERTIES = "wsivalidate";
  //protected ResourceBundle resourcebundle = null;
  
  /**
   * Constructor for wsiTestToolsPlugin.
   * @param descriptor an IPluginDescriptor object.
   */
  public WSITestToolsPlugin()
  {
    super();
    instance = this;

    // set the current directory
   WSITestToolsProperties.setEclipseContext(true);
   //resourcebundle = ResourceBundle.getBundle(PLUGIN_PROPERTIES);
  }

  /**
   * Method getInstance.
   * @return AbstractUIPlugin
   */
  public static Plugin getInstance()
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
}

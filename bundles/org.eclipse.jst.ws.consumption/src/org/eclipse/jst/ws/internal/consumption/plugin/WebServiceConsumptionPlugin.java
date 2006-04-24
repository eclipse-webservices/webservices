/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060424   115690 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.plugin;

import org.eclipse.core.runtime.Plugin;

/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the bulk of the Web Services runtime.
* Only the graphical user interface portion of the runtime is
* found elsewhere - in the org.eclipse.jst.ws.ui plugin.
*/
public class WebServiceConsumptionPlugin extends Plugin
{
  /**
  * The identifier of the descriptor of this plugin in plugin.xml.
  */
  public static final String ID = "org.eclipse.jst.ws.consumption";

  /**
  * The reference to the singleton instance of this plugin.
  */
  private static WebServiceConsumptionPlugin instance_;

  /**
  * Constructs a runtime plugin object for this plugin.
  */
  public WebServiceConsumptionPlugin ()
  {
    super();
    instance_ = this;
  }

  /**
  * Returns the singleton instance of this plugin. Equivalent to calling
  * (WebServiceConsumptionPlugin)Platform.getPlugin("org.eclipse.jst.ws");
  * @return The WebServiceConsumptionPlugin singleton.
  */
  public static WebServiceConsumptionPlugin getInstance ()
  {
    return instance_;
  }

}

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

package org.eclipse.jst.ws.internal.plugin;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jst.ws.internal.context.PersistentScenarioContext;
import org.eclipse.jst.ws.internal.context.PersistentUDDIPreferenceContext;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.context.UDDIPreferenceContext;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;


/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the bulk of the Web Services runtime.
* Only the graphical user interface portion of the runtime is
* found elsewhere - in the org.eclipse.jst.ws.ui plugin.
*/
public class WebServicePlugin extends Plugin
{
  /**
  * The identifier of the descriptor of this plugin in plugin.xml.
  */
  public static final String ID = "org.eclipse.jst.ws";

  /**
  * The reference to the singleton instance of this plugin.
  */
  private static WebServicePlugin instance_;

  private PersistentResourceContext resourceContext_;
  private PersistentScenarioContext scenarioContext_;
  private PersistentUDDIPreferenceContext uddiPreferenceContext;
  
  /**
   * Constructs a runtime plugin object for this plugin.
   */
  public WebServicePlugin()
  {
    super();
    instance_ = this; 
  }
  
  /**
  * Returns the singleton instance of this plugin.
  * @return The WebServicePlugin singleton.
  */
  public static WebServicePlugin getInstance ()
  {
    return instance_;
  }

  protected void initializeDefaultPluginPreferences() 
  {
  		((PersistentUDDIPreferenceContext) getUDDIPreferenceContext()).load();
  }

  public ResourceContext getResourceContext()
  {
  		if (resourceContext_ == null) 
  			resourceContext_ = PersistentResourceContext.getInstance();
		return resourceContext_;
  }

  public ScenarioContext getScenarioContext()
  {
  		if (scenarioContext_ == null)
  		{
  		  scenarioContext_ = new PersistentScenarioContext();
  		  scenarioContext_.load();
  		}

		return scenarioContext_;
  }
  
  public UDDIPreferenceContext getUDDIPreferenceContext()
  {
    if (uddiPreferenceContext == null)
      uddiPreferenceContext = new PersistentUDDIPreferenceContext();
    return uddiPreferenceContext;
  }

}

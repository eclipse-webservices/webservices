/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.plugin;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jst.ws.internal.context.PersistentProjectTopologyContext;
import org.eclipse.jst.ws.internal.context.PersistentScenarioContext;
import org.eclipse.jst.ws.internal.context.PersistentUDDIPreferenceContext;
import org.eclipse.jst.ws.internal.context.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.context.UDDIPreferenceContext;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.command.env.eclipse.EclipseLog;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;


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
  private PersistentProjectTopologyContext projectTopologyContext_;
  private PersistentUDDIPreferenceContext uddiPreferenceContext;
  private Log log_;
  
  /**
  * Constructs a runtime plugin object for this plugin.
  * The "plugin" element in plugin.xml should include the attribute
  * class = "org.eclipse.jst.ws.internal.plugin.WebServicePlugin".
  * @param descriptor The descriptor of this plugin.
  */
  public WebServicePlugin ( IPluginDescriptor descriptor )
  {
    super(descriptor);
    if (instance_ == null)
    {
      instance_ = this;
    }
    log_ = new EclipseLog();
  }

  // New M7 code will create plugins with the default constructor.
  public WebServicePlugin()
  {
    instance_ = this; 
  }
  
  /**
  * Returns the singleton instance of this plugin. Equivalent to calling
  * (WebServicePlugin)Platform.getPlugin("org.eclipse.jst.ws");
  * @return The WebServicePlugin singleton.
  */
  public static WebServicePlugin getInstance ()
  {
    return instance_;
  }

  /**
  * Called once by the platform when this plugin is first loaded.
  * @throws CoreException If this plugin fails to start.
  */
  public void startup () throws CoreException
  {
    log_.log(Log.INFO, 5027, this, "startup", "Starting plugin org.eclipse.jst.ws");
    super.startup();
  }

  /**
  * Called once by the platform when this plugin is unloaded.
  * @throws CoreException If this plugin fails to shutdown.
  */
  public void shutdown () throws CoreException
  {
    log_.log(Log.INFO, 5028, this, "shutdown", "Shutting plugin org.eclipse.jst.ws");
    
    super.shutdown();
  }

  protected void initializeDefaultPluginPreferences() 
  {
  		((PersistentProjectTopologyContext) getProjectTopologyContext()).load();
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

  public ProjectTopologyContext getProjectTopologyContext()
  {
    if (projectTopologyContext_ == null)
      projectTopologyContext_ = new PersistentProjectTopologyContext();
    return projectTopologyContext_;
  }

  
  public UDDIPreferenceContext getUDDIPreferenceContext()
  {
    if (uddiPreferenceContext == null)
      uddiPreferenceContext = new PersistentUDDIPreferenceContext();
    return uddiPreferenceContext;
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

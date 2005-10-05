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
package org.eclipse.jst.ws.internal.axis.creation.ui.plugin;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.command.internal.env.eclipse.EclipseLog;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.osgi.framework.BundleContext;

/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the graphic user interface to the
* Web Services runtime found in org.eclipse.jst.ws.
*/
public class WebServiceAxisCreationUIPlugin extends AbstractUIPlugin
{
	
	// Copyright
	public static final String copyright = "(c) Copyright IBM Corporation 2003."; //$NON-NLS-1$

  /**
  * The identifier of the descriptor of this plugin in plugin.xml.
  */
  public static final String ID = "org.eclipse.jst.ws.axis.creation.ui";	//$NON-NLS-1$

  /**
  * The reference to the singleton instance of this plugin.
  */
  private static WebServiceAxisCreationUIPlugin instance_;
  private Log log_;

  /**
  * Constructs a runtime plugin object for this plugin.
  * The "plugin" element in plugin.xml should include the attribute
  * class = "org.eclipse.jst.ws.internal.ui.plugin.WebServicePlugin".
  * @param descriptor The descriptor of this plugin.
  */
  public WebServiceAxisCreationUIPlugin()
  {
    super();
    if (instance_ == null)
    {
      instance_ = this;
    }
    log_ = new EclipseLog();

  }

  // This method is needed to keep the logging from blowing up.
  public String toString()
  {
    return ID;  
  }
  
  /**
  * Returns the singleton instance of this plugin. Equivalent to calling
  * (WebServiceWasCreationUIPlugin)Platform.getPlugin("org.eclipse.jst.ws.was.v5.tp.ui");
  * @return The WebServiceConsumptionUIPlugin singleton.
  */
  static public WebServiceAxisCreationUIPlugin getInstance ()
  {
    return instance_;
  }

  /**
  * Called once by the platform when this plugin is first loaded.
  * @throws CoreException If this plugin fails to start.
  */
  public void start( BundleContext context ) throws CoreException
  {
  	log_.log(Log.INFO, 5068, this, "start", "Starting plugin org.eclipse.jst.ws.axis.creation.ui");
    
    try
    {
      super.start( context );
    }
    catch( Exception exc )
    {
      log_.log( Log.ERROR, 5068, this, "start", exc );  
    }
    
    setPreferences();
  }

  /**
  * Called once by the platform when this plugin is unloaded.
  * @throws CoreException If this plugin fails to shutdown.
  */
  public void stop( BundleContext context ) throws CoreException
  {
  	log_.log(Log.INFO, 5069, this, "stop", "Shutting plugin org.eclipse.jst.ws.axis.creation.ui");
    
    try
    {
      super.stop( context );
    }
    catch( Exception exc )
    {
      log_.log( Log.ERROR, 5068, this, "start", exc );        
    }
  }


  /**
   * Sets the general preferences to the values in the preferences store.
  **/
  public void setPreferences()
  {
   // setDialogsPreferences();
  }

 /**
   * @see AbstractUIPlugin#initializeDefaultPreferences
   */
  protected void initializeDefaultPreferences(IPreferenceStore preferenceStore) {

	// Set the defaults Preference
//	ActionDialogsPreferenceHelper.initializeDefaults(preferenceStore);
  }
 
}

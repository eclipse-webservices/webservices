/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd   bug     Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060329   127016 andyzhai@ca.ibm.com - Andy Zhai
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.core.plugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jst.ws.internal.axis.consumption.core.context.AxisEmitterContext;
import org.eclipse.jst.ws.internal.axis.consumption.core.context.PersistentAxisEmitterContext;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.ILog;
import org.osgi.framework.BundleContext;


/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the graphic user interface to the
* Web Services runtime found in org.eclipse.jst.ws.
*/
public class WebServiceAxisConsumptionCorePlugin extends Plugin
{

	/**
	* The identifier of the descriptor of this plugin in plugin.xml.
	*/
	public static final String ID =
		"org.eclipse.jst.ws.axis.consumption.core";

	/**
	* The reference to the singleton instance of this plugin.
	*/
	private static WebServiceAxisConsumptionCorePlugin instance_;
	private ILog log_;

	private PersistentAxisEmitterContext axisEmitterContext_;
	/**
	* Constructs a runtime plugin object for this plugin.
	* The "plugin" element in plugin.xml should include the attribute
	* class = "org.eclipse.jst.ws.axis.consumption.core".
	* @param descriptor The descriptor of this plugin.
	*/
	public WebServiceAxisConsumptionCorePlugin() {
		super();
		if (instance_ == null) {
			instance_ = this;
		}
		log_ = EnvironmentService.getEclipseLog();

	}

  // This method is needed to keep the logging from blowing up.
  public String toString()
  {
    return ID;  
  }
  
	/**
	* Returns the singleton instance of this plugin. Equivalent to calling
	* (WebServiceWasConsumptionPlugin)Platform.getPlugin("org.eclipse.jst.ws.was.v5.tp");
	* @return The WebServiceAxisConsumptionCorePlugin singleton.
	*/
	static public WebServiceAxisConsumptionCorePlugin getInstance() {
		return instance_;
	}
	
	public AxisEmitterContext getAxisEmitterContext()
	{
		if (axisEmitterContext_ == null) 
	  		axisEmitterContext_ = PersistentAxisEmitterContext.getInstance();
		return axisEmitterContext_;
	}

	/**
	* Called once by the platform when this plugin is first loaded.
	* @throws CoreException If this plugin fails to start.
	*/
	public void start( BundleContext bundle ) throws CoreException {
		log_.log(ILog.INFO, 5087, this, "start", "Starting plugin org.eclipse.jst.ws.axis.consumption.core");
    
    try
    {
		  super.start( bundle );
    }
    catch( Exception exc )
    {
      log_.log( ILog.ERROR, 5088, this, "start", exc );
    }
	}

	/**
	* Called once by the platform when this plugin is unloaded.
	* @throws CoreException If this plugin fails to shutdown.
	*/
	public void stop( BundleContext context ) throws CoreException {
		log_.log(ILog.INFO, 5089, this, "shutdown", "Shutting plugin org.eclipse.jst.ws.axis.consumption.core");
    
    try
    {
		  super.stop( context );
    }
    catch( Exception exc )
    {
      log_.log( ILog.ERROR, 5090, this, "stop", exc );      
    }
	}
}

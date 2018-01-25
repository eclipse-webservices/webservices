/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070130   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070516   183147 sandakith@wso2.com - Lahiru Sandakith Fix for the persisting DBCS paths
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.plugin;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.context.PersistentAxis2EmitterContext;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class WebServiceAxis2CorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.jst.ws.axis2.core";

	// The shared instance
	private static WebServiceAxis2CorePlugin instance_;
	

	private PersistentAxis2EmitterContext axis2EmitterContext_;
	
	/**
	 * The constructor
	 */
	public WebServiceAxis2CorePlugin() {
		super();
		if (instance_ == null) {
			instance_ = this;
		}
	}

	
	public Axis2EmitterContext getAxisEmitterContext(){
		if (axis2EmitterContext_ == null) 
	  		axis2EmitterContext_ = PersistentAxis2EmitterContext.getInstance();
		return axis2EmitterContext_;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		instance_ = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static WebServiceAxis2CorePlugin getDefault() {
		return instance_;
	}
	
	 // This method is needed to keep the logging from blowing up.
	  public String toString()
	  {
	    return PLUGIN_ID;  
	  }
	  
		/**
		* Returns the singleton instance of this plugin. Equivalent to calling
		* (WebServiceWasConsumptionPlugin)Platform.getPlugin("org.eclipse.jst.ws.was.v5.tp");
		* @return The WebServiceAxisConsumptionCorePlugin singleton.
		*/
		static public WebServiceAxis2CorePlugin getInstance() {
			return instance_;
		}

}

/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060403 128827   kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.plugin;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.ws.internal.preferences.PersistentMergeContext;
import org.eclipse.wst.ws.internal.preferences.PersistentWSDLValidationContext;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIAPContext;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;
import org.eclipse.wst.ws.internal.preferences.PersistentWSISSBPContext;
import org.eclipse.wst.ws.internal.preferences.PersistentWaitForWSDLValidationContext;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class WSPlugin extends Plugin {

	// The shared instance.
	private static WSPlugin plugin;
	
	// Resource bundle.
	private ResourceBundle resourceBundle;
	
	private PersistentWSISSBPContext wsiSSBPContext_;
	private PersistentWSIAPContext wsiAPContext_;
	private PersistentWSDLValidationContext wsdlValidationContext_;
	private PersistentWaitForWSDLValidationContext waitForWsdlValidationContext_;
	private PersistentMergeContext mergeContext_;

	public static final String ID = "org.eclipse.wst.ws";
	
	/**
	 * The constructor.
	 */
	public WSPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the singleton instance of this plugin. Equivalent to calling
	 * (WSPlugin)Platform.getPlugin("org.eclipse.wst.ws");
	 * @return The WSPlugin singleton.
	 */
	 static public WSPlugin getInstance() {
	   return plugin;
	 }
	 
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static WSPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = WSPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found. Replaces substitution variables in the message by objects in 'args'.
	 */
	public static String getResourceString(String key, Object[] args) {
		return MessageFormat.format(getResourceString(key), args);
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle
						.getBundle("org.eclipse.wst.ws.WSPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
	/**
	 * Get WSI Context
	 * @deprecated use getWSISSBPContext or getWSIAPContext instead
	 * 
	 */ 
 public PersistentWSIContext getWSIContext() 
 	{ // defaulting to get WSI Simple SOAP Binding Profile context
 	  return getWSISSBPContext();
 	}
 
 /**
	 * Get WSI Simple SOAP Binding Profile Context
	 * 
	 */ 
 public PersistentWSISSBPContext getWSISSBPContext() 
	{
	  if (wsiSSBPContext_ == null)
	  	{
	  		wsiSSBPContext_ = new PersistentWSISSBPContext();
	  		wsiSSBPContext_.load();
	  	}
	  return wsiSSBPContext_;
	}
 
 /**
	 * Get WSI Attachment Profile Context
	 * 
	 */ 
 public PersistentWSIAPContext getWSIAPContext() 
	{
	  if (wsiAPContext_ == null)
	  	{
	  		wsiAPContext_ = new PersistentWSIAPContext();
	  		wsiAPContext_.load();
	  	}
	  return wsiAPContext_;
	}

	 /**
	 * Get WSDL Valiation Context
	 * 
	 */ 

	public PersistentWSDLValidationContext getWSDLValidationContext() {
		if (wsdlValidationContext_ == null) {
			wsdlValidationContext_ = new PersistentWSDLValidationContext();
			wsdlValidationContext_.load();
		}
		return wsdlValidationContext_;
	}
	
	/**
	 * Get Wait for WSDL Valiation Context
	 * 
	 */ 

	public PersistentWaitForWSDLValidationContext getWaitForWSDLValidationContext() {
		if (waitForWsdlValidationContext_ == null) {
			waitForWsdlValidationContext_ = new PersistentWaitForWSDLValidationContext();
			waitForWsdlValidationContext_.load();
		}
		return waitForWsdlValidationContext_;
	}
	
	/**
	 * Get Skeleton Merge Context
	 * 
	 */ 
 public PersistentMergeContext getMergeContext() 
	{
	  if (mergeContext_ == null)
	  	{
		  mergeContext_ = new PersistentMergeContext();
		  mergeContext_.load();
	  	}
	  return mergeContext_;
	}
}

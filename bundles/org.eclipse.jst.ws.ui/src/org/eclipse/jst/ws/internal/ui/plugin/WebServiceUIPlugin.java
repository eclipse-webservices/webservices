/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jst.ws.internal.ui.preferences.PersistentActionDialogsContext;
import org.eclipse.jst.ws.internal.ui.wsi.preferences.PersistentWSIAPContext;
import org.eclipse.jst.ws.internal.ui.wsi.preferences.PersistentWSIContext;
import org.eclipse.jst.ws.internal.ui.wsi.preferences.PersistentWSISSBPContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.eclipse.EclipseLog;



/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the graphic user interface to the
* Web Services runtime found in org.eclipse.jst.ws.
*/
public class WebServiceUIPlugin extends AbstractUIPlugin
{
	// Copyright
	public static final String copyright =
		"(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * The identifier of the descriptor of this plugin in plugin.xml.
  */
  public static final String ID = "org.eclipse.jst.ws.ui";

  /**
  * The reference to the singleton instance of this plugin.
  */
  private static WebServiceUIPlugin instance_;

  private PersistentWSISSBPContext wsiSSBPContext_;
  private PersistentWSIAPContext wsiAPContext_;
  private PersistentActionDialogsContext actionDialogsContext_;
  private Log log_;
  
  /**
  * Constructs a runtime plugin object for this plugin.
  * The "plugin" element in plugin.xml should include the attribute
  * class = "org.eclipse.jst.ws.internal.ui.plugin.WebServiceUIPlugin".
  * @param descriptor The descriptor of this plugin.
  */
	public WebServiceUIPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		if (instance_ == null) {
			instance_ = this;
		}
		log_ = new EclipseLog();
  }

  /**
  * Returns the singleton instance of this plugin. Equivalent to calling
  * (WebServiceUIPlugin)Platform.getPlugin("org.eclipse.jst.ws.ui");
  * @return The WebServiceUIPlugin singleton.
  */
	static public WebServiceUIPlugin getInstance() {
    return instance_;
  }

  /**
  * Called once by the platform when this plugin is first loaded.
  * @throws CoreException If this plugin fails to start.
  */
	public void startup() throws CoreException {
		log_.log(Log.INFO, 5040, this, "startup", "Starting plugin org.eclipse.jst.ws.ui");
    super.startup();
  }

  /**
  * Called once by the platform when this plugin is unloaded.
  * @throws CoreException If this plugin fails to shutdown.
  */
	public void shutdown() throws CoreException {
		log_.log(Log.INFO, 5041, this, "shutdown", "Shutting plugin org.eclipse.jst.ws.ui");
    super.shutdown();
  }

	/**
	 * Get WSI Context
	 * @deprecated use getWSISSBPContext or getWSIAPContext instead
	 * 
	 */ 
  public PersistentWSIContext getWSIContext() 
  	{ // defaulting to get WSI Simple SOAP Basic Profile context
  	  return getWSISSBPContext();
  	}
  
  /**
	 * Get WSI Simple SOAP Basic Profile Context
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

  public PersistentActionDialogsContext getActionDialogsContext() 
  	{
  	  if (actionDialogsContext_ == null)
  	  	{
  	  		actionDialogsContext_ = new PersistentActionDialogsContext();
  	  		actionDialogsContext_.load();
  	  	}
  	  return actionDialogsContext_;
  	}
		
  /**
  * Returns the message string identified by the given key from
  * the plugin.properties file for the appropriate locale.
  * @param key The message key string prefixed by a "%" symbol.
  * That is, the string passed in must be of the form "%KEY"
  * where the plugin.properties file contains a line of the
  * form: "KEY = value".
  * @return The locale-specific message.
  */
	public static String getMessage(String key) 
	{
	    MessageUtils msgUtils = new MessageUtils( "org.eclipse.jst.ws.ui.plugin", instance_ );
	    
	    if( key.startsWith("%"))
	    {
	      key = key.substring( 1, key.length() );
	    }
	    
	    return msgUtils.getMessage(key);
    }

  /**
  * Returns the message string identified by the given key from
  * the plugin.properties file for the appropriate locale.
  * Substitution sequences in the message string
  * are replaced by the given array of substitution objects (which
  * are most frequently strings). See java.text.MessageFormat for
  * further details on substitution.
  * @param key The message key string prefixed by a "%" symbol.
  * That is, the string passed in must be of the form "%KEY"
  * where the plugin.properties file contains a line of the
  * form: "KEY = value".
  * @param args The substitution values for the message
  * as required by the message in plugin.properties and
  * by the rules of class java.text.MessageFormat.
  * @return The locale-specific message.
  */
	public static String getMessage(String key, Object[] args) {
		return MessageFormat.format(getMessage(key), args);
  }

  /**
  * Returns an image descriptor for the named resource
  * as relative to the plugin install location.
  * @return An image descriptor, possibly null.
  */
	public static ImageDescriptor getImageDescriptor(String name) {
		try {
			URL installURL = instance_.getDescriptor().getInstallURL();
			URL imageURL = new URL(installURL, name);
			return ImageDescriptor.createFromURL(imageURL);
		} catch (MalformedURLException e) {
      return null;
    }
  }

  public String getPluginStateLocation() {
    return Platform.getPluginStateLocation(this).toOSString();
  }

  public String getPluginInstallLocation() {
    try {
      return Platform.resolve(getDescriptor().getInstallURL()).getFile();
		} catch (Exception e) {
			return null;
		}
	}

}

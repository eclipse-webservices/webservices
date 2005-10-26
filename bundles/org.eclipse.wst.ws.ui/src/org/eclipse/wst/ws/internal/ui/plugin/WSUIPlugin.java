package org.eclipse.wst.ws.internal.ui.plugin;

/*******************************************************************************
* Copyright (c) 2005 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSIAPContext;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSIContext;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSISSBPContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;


/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the graphic user interface to the
* Web Services runtime found in org.eclipse.jst.ws.
*/
public class WSUIPlugin extends AbstractUIPlugin
{
	// Copyright
	public static final String copyright =
		"(c) Copyright IBM Corporation 2000, 2002.";

 /**
 * The identifier of the descriptor of this plugin in plugin.xml.
 */
 public static final String ID = "org.eclipse.wst.ws.ui";

 /**
 * The reference to the singleton instance of this plugin.
 */
 private static WSUIPlugin instance_;

 private PersistentWSISSBPContext wsiSSBPContext_;
 private PersistentWSIAPContext wsiAPContext_;
 
 /**
  * Constructs a runtime plugin object for this plugin.
  */
 public WSUIPlugin() {
	super();
	instance_ = this;
 }

 /**
 * Returns the singleton instance of this plugin. Equivalent to calling
 * (WSUIPlugin)Platform.getPlugin("org.eclipse.wst.ws.ui");
 * @return The WSUIPlugin singleton.
 */
 static public WSUIPlugin getInstance() {
   return instance_;
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
			URL installURL = instance_.getBundle().getEntry("/");
			URL imageURL = new URL(installURL, name);
			return ImageDescriptor.createFromURL(imageURL);
		} catch (MalformedURLException e) {
     return null;
   }
 }

}

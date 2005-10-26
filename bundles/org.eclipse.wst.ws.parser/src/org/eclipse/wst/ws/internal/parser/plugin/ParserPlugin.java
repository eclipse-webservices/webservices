/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.wst.ws.internal.parser.plugin;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;

/**
 * The main plugin class to be used in the desktop.
 */
public class ParserPlugin extends Plugin {
	//The shared instance.
	private static ParserPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public ParserPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle   = ResourceBundle.getBundle("org.eclipse.wst.ws.parser.ParserPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static ParserPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ParserPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
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
	  public static String getMessage ( String key )
	  {
	    MessageUtils msgUtils = new MessageUtils( "org.eclipse.wst.ws.parser.plugin", plugin );
	    
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
	  public static String getMessage ( String key, Object[] args )
	  {
	    return MessageFormat.format(getMessage(key),args);
	  }
}

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

package org.eclipse.jst.ws.internal.axis.consumption.ui.plugin;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.eclipse.EclipseLog;


/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the graphic user interface to the
* Web Services runtime found in org.eclipse.jst.ws.
*/
public class WebServiceAxisConsumptionUIPlugin extends Plugin
{

	/**
	* The identifier of the descriptor of this plugin in plugin.xml.
	*/
	public static final String ID =
		"org.eclipse.jst.ws.axis.consumption.ui";

	/**
	* The reference to the singleton instance of this plugin.
	*/
	private static WebServiceAxisConsumptionUIPlugin instance_;
	private Log log_;

	/**
	* Constructs a runtime plugin object for this plugin.
	* The "plugin" element in plugin.xml should include the attribute
	* class = "org.eclipse.jst.ws.internal.ui.plugin.WebServicePlugin".
	* @param descriptor The descriptor of this plugin.
	*/
	public WebServiceAxisConsumptionUIPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		if (instance_ == null) {
			instance_ = this;
		}
		log_ = new EclipseLog();

	}

	/**
	* Returns the singleton instance of this plugin. Equivalent to calling
	* (WebServiceWasConsumptionPlugin)Platform.getPlugin("org.eclipse.jst.ws.was.v5.tp");
	* @return The WebServiceWasConsumptionPlugin singleton.
	*/
	static public WebServiceAxisConsumptionUIPlugin getInstance() {
		return instance_;
	}

	/**
	* Called once by the platform when this plugin is first loaded.
	* @throws CoreException If this plugin fails to start.
	*/
	public void startup() throws CoreException {
		log_.log(Log.INFO, 5066, this, "startup", "Starting plugin org.eclipse.jst.ws.axis.consumption.ui");
		super.startup();
	}

	/**
	* Called once by the platform when this plugin is unloaded.
	* @throws CoreException If this plugin fails to shutdown.
	*/
	public void shutdown() throws CoreException {
		log_.log(Log.INFO, 5067, this, "shutdown", "Shutting plugin org.eclipse.jst.ws.axis.consumption.ui");
		super.shutdown();
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
	public static String getMessage(String key) {
		return instance_.getDescriptor().getResourceString(key);
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

	public String getPluginStateLocation() {
		return Platform
			.getPluginStateLocation(this)
			.addTrailingSeparator()
			.toOSString();
	}

	public String getPluginInstallLocation() {
		try {
			return Platform.resolve(getDescriptor().getInstallURL()).getFile();
		} catch (Exception e) {
			return null;
		}
	}
}

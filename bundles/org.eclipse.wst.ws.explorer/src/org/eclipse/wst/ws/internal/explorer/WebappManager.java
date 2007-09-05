/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class WebappManager {

	private static String host;
	private static int port = -1;
	private static final int AUTO_SELECT_JETTY_PORT = 0;
	
	public static void start(String webappName) throws Exception {
		Dictionary d = new Hashtable();
		
		configurePort();
		d.put("http.port", new Integer(getPortParameter())); //$NON-NLS-1$

		// set the base URL
		d.put("context.path", "/wse"); //$NON-NLS-1$ //$NON-NLS-2$
		d.put("other.info", "org.eclipse.wst.ws.explorer"); //$NON-NLS-1$ //$NON-NLS-2$
		
		// suppress Jetty INFO/DEBUG messages to stderr
		Logger.getLogger("org.mortbay").setLevel(Level.WARNING); //$NON-NLS-1$	

		JettyConfigurator.startServer(webappName, d);
		checkBundle();
		
	}
	
	/*
	 * Ensures that the bundle with the specified name and the highest available
	 * version is started and reads the port number
	 */
	private static void checkBundle() throws InvalidSyntaxException, BundleException {
		Bundle bundle = Platform.getBundle("org.eclipse.equinox.http.registry"); //$NON-NLS-1$if (bundle != null) {
		if (bundle.getState() == Bundle.RESOLVED) {
			bundle.start(Bundle.START_TRANSIENT);
		}
		if (port == -1) {
			// Jetty selected a port number for us
			ServiceReference[] reference = bundle.getBundleContext().getServiceReferences("org.osgi.service.http.HttpService", "(other.info=org.eclipse.wst.ws.explorer)"); //$NON-NLS-1$ //$NON-NLS-2$
			Object assignedPort = reference[0].getProperty("http.port"); //$NON-NLS-1$
			port = Integer.parseInt((String)assignedPort);
		}
	}

	public static void stop(String webappName) throws CoreException {
		try {
			JettyConfigurator.stopServer(webappName);
		}
		catch (Exception e) {
			//HelpBasePlugin.logError("An error occured while stopping the help server", e); //$NON-NLS-1$
		}
	}

	public static int getPort() {
		return port;
	}

	private static void configurePort() {
		if (port == -1) {
			String portCommandLineOverride = ExplorerPlugin.getBundleContext().getProperty("server_port");
			if (portCommandLineOverride != null && portCommandLineOverride.trim().length() > 0) {
				try {
					port = Integer.parseInt(portCommandLineOverride);
				}
				catch (NumberFormatException e) {
					String msg = "WSE server port specified in VM arguments is invalid (" + portCommandLineOverride + ")"; //$NON-NLS-1$ //$NON-NLS-2$
					//HelpBasePlugin.logError(msg, e);
				}
			}
		}
	}
	
	/*
	 * Get the port number which will be passed to Jetty
	 */
	private static int getPortParameter() {
		if (port == -1) { 
			return AUTO_SELECT_JETTY_PORT;
		}
		return port;
	}

	public static String getHost() {
		if (host == null) {
			String hostCommandLineOverride = ExplorerPlugin.getBundleContext().getProperty("server_host"); //$NON-NLS-1$
			if (hostCommandLineOverride != null && hostCommandLineOverride.trim().length() > 0) {
				host = hostCommandLineOverride;
			}
			else {
				host = "127.0.0.1"; //$NON-NLS-1$
			}
		}
		return host;
	}
	
	private WebappManager() {
	}
	
}

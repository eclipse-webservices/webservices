/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060721   151409 makandre@ca.ibm.com - Andrew Mak, WSE does not open in external browser on RH
 * 20060802   150428 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20061219   168620 makandre@ca.ibm.com - Andrew Mak, WSE does not open in external browser on Linux
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;

public class WSExplorer {
	private static WSExplorer instance;

	private static int launchOptionsKey_ = 0;

	private IWebBrowser internalBrowser_ = null;
	
	public WSExplorer() {
	}

	public static WSExplorer getInstance() {
		if (instance == null) {
			instance = new WSExplorer();
		}
		return instance;
	}

	public String getContextName() {
		return "wsexplorer";
	}

	public String getParentPluginID() {
		return ExplorerPlugin.ID;
	}

	public String getWARLocation() {
		return "wsexplorer.war";
	}

	public String getWebAppLocation() {
		return "wsexplorer";
	}

	public String getWelcomeURL() {
		return "wsexplorer.jsp";
	}

	public String getLaunchOptionRegistryURL() {
		return "launch_options_registry.jsp";
	}

	public String getBaseURL() {
		StringBuffer sb = new StringBuffer();
		sb.append("http://localhost:");
		sb.append(CatalinaRunnable.getCatalinaRunnable().getTomcatPort());
		sb.append("/");
		sb.append(getContextName());
		sb.append("/");
		return sb.toString();
	}

	public String getMetadataDirectory() {
		// <workspace>/.metadata/.plugins/org.eclipse.wst.ws.explorer/ (note
		// the trailing separator).
		return ExplorerPlugin.getInstance().getPluginStateLocation();
	}

	public IStatus launch(IWorkbench wb, IStructuredSelection sel,
		LaunchOption[] options, boolean forceLaunchOutsideIDE) {
		// launchOptionKey
		int launchOptionKey = getLaunchOptionsKey();
		// Web Services Explorer URL
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseURL());
		sb.append(getWelcomeURL());
		sb.append("?");
		if (options != null && options.length > 0) {
			String encodedID = null;
			try {
				encodedID = URLEncoder.encode(ExplorerPlugin.ID,
						ExplorerPlugin.CHARSET);
			} catch (UnsupportedEncodingException e) {
				return new Status(
						IStatus.ERROR,
						ExplorerPlugin.ID,
						0,
						ExplorerPlugin
								.getMessage("%MSG_BROKEN_VM_DOES_NOT_SUPPORT_UTF-8"),
						e);
			}
			sb.append(encodedID);
			sb.append("=");
			sb.append(launchOptionKey);
		}
		// launch Web Services Explorer
		try {
			IWorkbenchBrowserSupport browserSupport = ExplorerPlugin.getInstance().getWorkbench().getBrowserSupport();
			IWebBrowser browser = null;
			
			if (forceLaunchOutsideIDE) {
				browser = browserSupport.getExternalBrowser();
				
				// external browser support uses swt Program.findProgram() to locate an appropriate browser for HTML files
				// certain versions of swt Program class need to be run from the UI thread, otherwise findProgram() does not
				// work properly (this applies to Linux only).  The code below is to workaround this problem.
				
				// Display.getCurrent() will be null if this is not the UI thread
				if (Display.getCurrent() == null) {
					
					// create a runnable to open the browser, run it in the UI thread
					OpenBrowserRunnable runnable = new OpenBrowserRunnable(browser, new URL(sb.toString()));
					Display.getDefault().syncExec(runnable);
					
					if (runnable.getException() != null)
						throw runnable.getException();
				}
				else
					browser.openURL(new URL(sb.toString()));
			}
			else {
				// browserId
				StringBuffer browserId = new StringBuffer();
				browserId.append(ExplorerPlugin.ID);
				browserId.append(getContextName());
				
				if (internalBrowser_==null)
					internalBrowser_ = browserSupport.createBrowser(browserId.toString());
				browser = internalBrowser_;
				browser.openURL(new URL(sb.toString()));
			}
		} catch (Exception e) {
			return new Status(IStatus.ERROR, ExplorerPlugin.ID, 0,
					ExplorerPlugin.getMessage("%MSG_ERROR_LAUNCH_WSEXPLORER"),
					e);
		}
		// register launch options
		if (options != null && options.length > 0) {
			try {
				StringBuffer launchOptionsRegURL = new StringBuffer();
				launchOptionsRegURL.append(getBaseURL());
				launchOptionsRegURL.append(getLaunchOptionRegistryURL());
				URL url = new URL(launchOptionsRegURL.toString());
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				StringBuffer params = new StringBuffer();
				String encodedID = null;
				try {
					encodedID = URLEncoder.encode(ExplorerPlugin.ID,
							ExplorerPlugin.CHARSET);
				} catch (UnsupportedEncodingException e) {
					return new Status(
							IStatus.ERROR,
							ExplorerPlugin.ID,
							0,
							ExplorerPlugin
									.getMessage("%MSG_BROKEN_VM_DOES_NOT_SUPPORT_UTF-8"),
							e);
				}
				params.append(encodedID);
				params.append("=");
				params.append(launchOptionKey);
				params.append("&");
				for (int i = 0; i < options.length; i++) {
					if (options[i] != null) {
						params.append(options[i].getKey());
						params.append("=");
						String option = null;
						try {
							option = URLEncoder.encode(options[i].getOption(),
									ExplorerPlugin.CHARSET);
						} catch (UnsupportedEncodingException e) {
							return new Status(
									IStatus.ERROR,
									ExplorerPlugin.ID,
									0,
									ExplorerPlugin
											.getMessage("%MSG_BROKEN_VM_DOES_NOT_SUPPORT_UTF-8"),
									e);
						}
						params.append(option);
						params.append("&");
					}
				}
				out.print(params.toString());
				out.close();
				out = null;
				InputStream is = connection.getInputStream();
				is.close();
				is = null;
			} catch (Exception e) {
				return new Status(IStatus.WARNING, ExplorerPlugin.ID, 0,
						ExplorerPlugin
								.getMessage("%MSG_ERROR_LOAD_LAUNCH_OPTIONS"),
						e);
			}
		}
		return new Status(IStatus.OK, ExplorerPlugin.ID, 0, "", null);
	}

	private static int getLaunchOptionsKey() {
		return launchOptionsKey_++;
	}

	public IStatus launch(IWorkbench wb, IStructuredSelection sel,
			String[] inquiryURL, String[] publishURL,
			boolean forceLaunchOutsideIDE) {
		int inquiryURLOptionLength = (inquiryURL != null) ? inquiryURL.length
				: 0;
		int publishURLOptionLength = (publishURL != null) ? publishURL.length
				: 0;
		LaunchOption[] options = new LaunchOption[inquiryURLOptionLength
				+ publishURLOptionLength];
		int index = 0;
		if (inquiryURL != null) {
			for (int i = 0; i < inquiryURL.length; i++) {
				options[index] = new LaunchOption(LaunchOptions.INQUIRY_URL,
						inquiryURL[i]);
				index++;
			}
		}
		if (publishURL != null) {
			for (int i = 0; i < publishURL.length; i++) {
				options[index] = new LaunchOption(LaunchOptions.PUBLISH_URL,
						publishURL[i]);
				index++;
			}
		}
		return launch(wb, sel, options, forceLaunchOutsideIDE);
	}
}

/**
 * Helper class for opening a browser and storing the
 * Exception thrown if it was unsuccessful. 
 */
class OpenBrowserRunnable implements Runnable {
	
	private IWebBrowser browser;
	private URL url;
	private Exception exception = null;
	
	public OpenBrowserRunnable(IWebBrowser browser, URL url) {
		this.browser = browser;
		this.url = url;
	}
	
	public void run() {
		try {
			browser.openURL(url);			
		}
		catch (Exception e) {
			exception = e;
		}
	}
	
	public Exception getException() {
		return exception;
	}
}

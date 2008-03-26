/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070501   184505 kathy@ca.ibm.com - Kathy Chan
 * 20080326   224148 makandre@ca.ibm.com - Andrew Mak, Web service scenarios broke in latest builds with Equinox p2
 *******************************************************************************/
package org.eclipse.wst.ws.internal.common;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

public class BundleUtils
{
	static public URL getURLFromBundle( String bundleId, String path ) throws MalformedURLException
	{
		Bundle      bundle     = Platform.getBundle( bundleId );
		URL         installURL = bundle.getEntry("/");
		URL         fileURL    = new URL(installURL, path );

		return fileURL;
	}

	/**
	 * @param bundleId
	 * @return Returns the path for the Jarred plugin.  Returns null if path not found.
	 */
	static public IPath getJarredPluginPath(String bundleId) {
		IPath result = null;
		Bundle bundle = Platform.getBundle(bundleId);
		if (bundle != null) {
			try {
				File file = FileLocator.getBundleFile(bundle);
				result = new Path(file.getCanonicalPath());
			}
			catch (IOException e) {
				// ignore, return null
			}
		}
		return result;
	}

	/**
	 * @param bundleId
	 * @return The versoin of the plugin with that bundle ID
	 */
	static public Version getVersion(String bundleId) {
		Version result = null;
		Bundle bundle = Platform.getBundle(bundleId);
		String versionString = (String) bundle.getHeaders().get("Bundle-Version");
		result = new Version(versionString);
		return result;
	}

}

/*******************************************************************************
 * Copyright (c) 2003, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070501   184505 kathy@ca.ibm.com - Kathy Chan
 * 20080326   224148 makandre@ca.ibm.com - Andrew Mak, Web service scenarios broke in latest builds with Equinox p2
 * 20120418   364026 lippert@acm.org - Martin Lippert, saaj.jar deployment fails when multiple javax.xml.soap bundles are installed
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
		Bundle      bundle     = Platform.getBundle(bundleId);
		URL         installURL = bundle.getEntry("/");
		URL         fileURL    = new URL(installURL, path );

		return fileURL;
	}

	static public URL getURLFromBundle( String bundleId, Version bundleVersion, Version upperVersion, String path ) throws MalformedURLException
	{
		Bundle      bundle     = getBundleWithinVersionRange(bundleId, bundleVersion, upperVersion);
		URL         installURL = bundle.getEntry("/");
		URL         fileURL    = new URL(installURL, path );

		return fileURL;
	}

	private static Bundle getBundleWithinVersionRange(String bundleId,
			Version bundleVersion, Version upperVersion) {
		Bundle[] bundles = Platform.getBundles(bundleId, bundleVersion.toString());
		for(Bundle bundle : bundles) {
			Version aVersion = bundle.getVersion();
			if (aVersion.compareTo(bundleVersion) >= 0 && aVersion.compareTo(upperVersion) < 0) {
				return bundle;
			}
		}
		
		return null;
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

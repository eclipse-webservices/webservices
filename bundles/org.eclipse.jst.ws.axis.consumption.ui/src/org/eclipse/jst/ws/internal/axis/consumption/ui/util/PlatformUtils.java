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
package org.eclipse.jst.ws.internal.axis.consumption.ui.util;


import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;

public class PlatformUtils {

	public static final String PLATFORM_ROOT = "platform:/resource";
	public static final String WIN_FILE_PROTOCOL = "file:/";
	public static final String LNX_FILE_PROTOCOL = "file://";
	public static final String PLATFORM = "platform";
	public static final String RESOURCE = "/resource";

	private PlatformUtils() {
	}

	/**
	* Returns the string representation of platform URL given an IPath
	* @param IPath path
	* @return String platformURL
	*/
	public static String getPlatformURL(IPath path) {
		String platformURL;
		platformURL = URI.createPlatformResourceURI(path.toString()).toString();
		// old method: file system representation
		// 	platformURL = ResourcesPlugin.getWorkspace().getRoot().getFile(path).getLocation().toString();
		return platformURL;
	}

	/**
	* Returns the URL representation of the Platform URL given an IPath
	* The PlatformResourceManager underdstands this format and therefore
	* this is suited for command URL's
	* @param IPath path
	* @return URL Platform URL
	*/
	public static URL getCommandPlatformURL(IPath path) {
		URL url = null;
		try {
			url = new URL(PLATFORM, null, RESOURCE + path.toString());
		} catch (MalformedURLException e) {
			return url;
		}
		return url;
	}

	/**
	* Returns the string representation of path given platform URL string
	* @param String platform URL string
	* @return String path string
	*/
	public static String getPathFromPlatform(String platformStr) {
		String pathStr = platformStr;

		String rootLocation;
		rootLocation = PLATFORM_ROOT;
		// old method: file system representation
		//	rootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		if (platformStr.startsWith(rootLocation))
			pathStr = platformStr.substring(rootLocation.length());
		return pathStr;
	}

	/**
	* Returns the string representation of platform URL given the path string
	* @param String path string
	* @return String platform URL string
	*/
	public static String getPlatformFromPath(String pathStr) {
		String platformStr = pathStr;

		String rootLocation;
		rootLocation = PLATFORM_ROOT;
		// old method: file system representation
		//	rootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		platformStr = rootLocation + pathStr;
		return platformStr;
	}

	/**
	* Returns the string representation of the local file system file given platform URL string
	* @param String platform URL string
	* @return String file string
	*/
	public static String getFileFromPlatform(String platformStr) {
		String fileStr = platformStr;
		String pathStr = getPathFromPlatform(platformStr);

		fileStr =
			ResourcesPlugin
				.getWorkspace()
				.getRoot()
				.getFile(new Path(pathStr))
				.getLocation()
				.toString();

		// old method: file system representation
		//	Do nothing

		return fileStr;
	}

	/**
	* Returns the file protocol representation of the local file system file given platform URL string
	* @param String platform URL string
	* @return String the file protocol uri
	*/
	public static String getFileURLFromPlatform(String platformStr) {
		String fileStr = platformStr;
		String pathStr = getPathFromPlatform(platformStr);
		try {
			fileStr =
				ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.getFile(new Path(pathStr))
					.getLocation()
					.toFile()
					.toURL()
					.toString();
		} catch (MalformedURLException murle) {
			fileStr = getFileURLFromPath(new Path(pathStr));
		}
		return fileStr;
	}

	/**
	* Returns the file protocol representation of a local file
	* @param IPath the Path object representing the file
	* @return String the file protocol uri
	*/
	public static String getFileURLFromPath(IPath path) {

		String file = null;
		if (path != null) {
			if (!path.isAbsolute()) {
				file =
					ResourcesPlugin
						.getWorkspace()
						.getRoot()
						.getFile(path)
						.getLocation()
						.toString();
				if (file.charAt(0) == IPath.SEPARATOR) {
					file = LNX_FILE_PROTOCOL + file;
				} else {
					file = WIN_FILE_PROTOCOL + file;
				}
			} else {
				file = path.toString();
			}
			if (file != null && file.charAt(0) == IPath.SEPARATOR) {
				file = LNX_FILE_PROTOCOL + file;
			} else {
				file = WIN_FILE_PROTOCOL + file;
			}
		}
		return file == null ? null : file;

	}
}

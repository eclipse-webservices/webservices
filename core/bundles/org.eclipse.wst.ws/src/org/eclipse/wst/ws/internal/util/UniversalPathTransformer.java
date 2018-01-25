/*******************************************************************************
 * Copyright (c) 2006, 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060825   135570 makandre@ca.ibm.com - Andrew Mak, Service implementation URL not displayed properly on first page
 * 20070125   171071 makandre@ca.ibm.com - Andrew Mak, Create public utility method for copying WSDL files 
 * 20070509   182274 kathy@ca.ibm.com - Kathy Chan
 * 20080411   226767 makandre@ca.ibm.com - Andrew Mak, UniversalPathTransformer does not handle paths w/ spaces that are encoded
 * 20090128   262639 ericdp@ca.ibm.com - Eric D. Peters, WSDLCopier gives NPE in projects with spaces
 *******************************************************************************/
package org.eclipse.wst.ws.internal.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.uriresolver.internal.util.URIEncoder;

/**
 * <p>A utility class for transforming a path to and from the following formats:</p>
 * <ul>
 * <li>An absolute eclipse path (i.e. starts with /ProjectName/...)</li>
 * <li>An absolute filesystem URI path (i.e. file:/C:/MyWorkspace/ProjectName/...)</li>
 * <li>A platform URI path (i.e. platform:/resource/ProjectName/...)</li> 
 * </ul> 
 * <p>In all cases, if the transformer could not convert the path, it is returned unchanged.</p>
 */
public class UniversalPathTransformer {
	
	/**
	 * Prefix of platform URIs.
	 */
	public static final String PLATFORM_PREFIX = "platform:/resource/";
	
	/**
	 * Prefix of filesystem URIs.
	 */
	public static final String LOCATION_PREFIX = "file:/";

	private static String PROTOCOL_MARKER = ":";
	private static char PATH_SEPARATOR    = '/';
 	
	/**
	 * Determines if a URI string starts with the given prefix.  This method is case-insensitive. 
	 * 
	 * @param uri The URI string to check.
	 * @param prefix The prefix.
	 * @return true iff prefix is a strict prefix of the URI string (i.e. prefix != uri), false
	 * is returned otherwise.
	 */
	private static boolean isPrefix(String uri, String prefix) {
		if (prefix.length() >= uri.length())
			return false;
		
		return uri.substring(0, prefix.length()).equalsIgnoreCase(prefix);
	}

	/**
	 * Transform the given path to an eclipse path.  If the given path is a filesystem URI, it must map to
	 * a real resource in the workspace.
	 * 
	 * @param str The path to transform.
	 * @return Returns an eclipse path equivalent of the given path.
	 */
	public static String toPath(String str) {
		
		if (str == null || str.length() == 0)
			return str;
		
		if (isPrefix(str, PLATFORM_PREFIX)) {
			str = str.substring(PLATFORM_PREFIX.length() - 1);
		}
		else if (isPrefix(str, LOCATION_PREFIX)) {
			IFile file = toFile(str);
			if (file != null)
				str = file.getFullPath().makeAbsolute().toString();
		}
		
		return str;
	}

	/**
	 * Transform the given path to a platform URI path.  If the given path is a filesystem URI, it must map to
	 * a real resource in the workspace.
	 * 
	 * @param str The path to transform.
	 * @return Returns a platform URI path equivalent of the given path.
	 */
	public static String toPlatformPath(String str) {

		if (str == null || str.length() == 0)
			return str;		
		
		if (isPrefix(str, LOCATION_PREFIX)) {
			IFile file = toFile(str);
			if (file != null)
				str = PLATFORM_PREFIX + file.getFullPath().makeRelative();
		}			
		else if (str.indexOf(PROTOCOL_MARKER) == -1) {
			if (str.charAt(0) == PATH_SEPARATOR)
				str = PLATFORM_PREFIX + str.substring(1);
			else
				str = PLATFORM_PREFIX + str;
		}
		
		return str;
	}
	
	/**
	 * Transform the given path to a filesystem URI path.  The path must be a valid
	 * eclipse resource.
	 * 
	 * @param str The path to transform.
	 * @return Returns a filesystem URI path equivalent of the given path.
	 */
	public static String toLocation(String str) {
		
		if (str == null || str.length() == 0)
			return str;
		
		String s = str;
		
		if (isPrefix(s, PLATFORM_PREFIX))
			s = toPath(s);
		
		if (s.indexOf(PROTOCOL_MARKER) == -1) {			
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(s));
			if (file != null)
				str = file.getLocationURI().toString();
		}
				
		return str;
	}	
	
	/**
	 * Transform the given URI to an IFile.  Returns null if the URI is not in the workspace.
	 * 
	 * @param str The URI to transform.
	 * @return Returns IFile
	 */
	public static IFile toFile(String str) {
		
		IFile file = null;
		
		// local filesystem path
		if (isPrefix(str, LOCATION_PREFIX)) {
			str = str.substring(LOCATION_PREFIX.length()).replaceAll("%20", " ");
			file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(str));
		}
		else {	
			// platform path
			if (str.startsWith(PLATFORM_PREFIX))
				str = str.substring(PLATFORM_PREFIX.length());
		
			if (str.indexOf(PROTOCOL_MARKER) == -1) {			
				file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(str));
			}
		}
								
		return file;
	}	
	
	/**
	 * Transform the given URIs to an IFiles.  
	 * IFile set to null if the URI is not in the workspace.
	 * 
	 * @param str The URIs to transform.
	 * @return Returns IFile[]
	 */
	public static IFile[] toFiles(String[] uris) {
		
		IFile[] files = null;
		if (uris != null) {
			files = new IFile[uris.length];		
			for (int i = 0; i < uris.length; i++) {
				String uri = uris[i];
				files[i] = UniversalPathTransformer.toFile(uri);
			}
		}
		return files;
	}

	/**
	 * Transform the given encoded URI to a file: protocol URI.  The URI must be a valid
	 * eclipse resource.
	 * 
	 * @param str The URI to transform.
	 * @return Returns a file: protocol URI equivalent of the given URI.
	 */
	public static String uriToLocation(String str) {
		
		if (str == null || str.length() == 0)
			return str;
		
		String s = str;
		if (isPrefix(s, PLATFORM_PREFIX))
			s = UniversalPathTransformer.toPath(s);
		
		if (s.indexOf(PROTOCOL_MARKER) == -1) {		
			try {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(URLDecoder.decode(s, "UTF-8")));
			if (file != null) 
				str = uriCreate(URLDecoder.decode(file.getLocationURI().toString(), "UTF-8")).toString();
			} catch (Exception e) {
					//nothing to do here, just return same string
			}
		}
				
		return str;
	}

	private static java.net.URI uriCreate(String uri) {
		if (uri != null)
			uri = uri.replace('\\', '/');
		try {
			try {
				return java.net.URI.create(uri);
			}
			catch (IllegalArgumentException e) {				
				return java.net.URI.create(URIEncoder.encode(uri, "UTF-8"));
			}
		}
		catch (UnsupportedEncodingException e) {
			return java.net.URI.create("");
		}	
	}	
}

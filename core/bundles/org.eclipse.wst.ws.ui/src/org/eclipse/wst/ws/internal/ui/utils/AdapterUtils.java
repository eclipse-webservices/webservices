/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
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
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 * 20070716   191357 kathy@ca.ibm.com - Kathy Chan
 * 20080220   219537 makandre@ca.ibm.com - Andrew Mak
 * 20080421   227824 makandre@ca.ibm.com - Andrew Mak, AdapterUtils adapt to IFile before String
 * 20090310   242440 yenlu@ca.ibm.com - Yen Lu
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui.utils;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.internal.converter.IIFile2UriConverter;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;

/**
 * The AdapterUtils class provides utility methods to get objects from the Platform's adapter extension.
 *
 */
public class AdapterUtils {	
	
	/**
	 * Determine if the given object has an IFile or String adapter.
	 * 
	 * @param object The object.
	 * @return True the object is adaptable to IFile or String, false otherwise.
	 */
	public static boolean hasAdapter(Object object) {
		if (object == null)
			return false;
		boolean hasAdapter = Platform.getAdapterManager().hasAdapter(object, "org.eclipse.core.resource.IFile");
		if (!hasAdapter)
			hasAdapter = Platform.getAdapterManager().hasAdapter(object, "java.lang.String");
		return hasAdapter;
	}
	
	/**
	 * @param object Object to adapt
	 * @return The adapted object representing the file or String if an adapter is found. 
	 *         Returns null if an adapter is not found.
	 */
	public static Object getAdaptedObject (Object object) { 
		if (object == null) 
			return null;
		Object adaptedObject = Platform.getAdapterManager().loadAdapter(object, "java.lang.String");
		if (adaptedObject == null) {
			adaptedObject = Platform.getAdapterManager().loadAdapter(object, "org.eclipse.core.resources.IFile");
		}
		return adaptedObject;
	}
	
	/**
	   * @param object Look up an adapter mapping the object to IFile or String.
	   * @return The WSDL uri returned by the adapter or null if no adapter is found.
	   */
	  public static String getAdaptedWSDL (Object object) {
		  String wsdlURI = null;
		  Object adaptedObject = AdapterUtils.getAdaptedObject(object);
		  if ( adaptedObject != null) {
			  if (adaptedObject instanceof IFile)
			  {
				  IFile file = (IFile)adaptedObject;
				  boolean allowBaseConversionOnFailure = true;
				  IIFile2UriConverter converter = WSPlugin.getInstance().getIFile2UriConverter();
				  if (converter != null)
				  {
					  wsdlURI = converter.convert(file);
					  allowBaseConversionOnFailure = converter.allowBaseConversionOnFailure();
				  }
				  if (wsdlURI == null && allowBaseConversionOnFailure)
				  {
				    URI uri = file.getLocationURI();
				    if (uri != null) {
					  wsdlURI = uri.toString();
				    }
				  }
			  } else if (adaptedObject instanceof String) {
				  wsdlURI = (String) adaptedObject;
			  }
		  }
		  return wsdlURI;
	  }
	
}

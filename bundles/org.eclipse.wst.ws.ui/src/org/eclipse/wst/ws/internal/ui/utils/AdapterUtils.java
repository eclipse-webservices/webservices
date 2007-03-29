/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui.utils;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;

public class AdapterUtils {	
	
	/**
	 * @param object Object to adapte
	 * @return The adapted object representing the file or String if an adapter is found. 
	 *         Returns null if an adapter is not found.
	 */
	public static Object getAdaptedObject (Object object) { 
		Object adaptedObject = null;
		adaptedObject = Platform.getAdapterManager().loadAdapter(object, "org.eclipse.core.resources.IFile");
		if (adaptedObject == null) {
			adaptedObject = Platform.getAdapterManager().loadAdapter(object, "java.lang.String");
		}
		return adaptedObject;
	}
	
	/**
	   * @param object Look up an adapter mapping the object to IFile or String.
	   * @return The WSDL string returned by the adapter or null if no adapter is found.
	   */
	  public static String getAdaptedWSDL (Object object) {
		  String wsdlURL = null;
		  Object adaptedObject = AdapterUtils.getAdaptedObject(object);
		  if ( adaptedObject != null) {
			  if (adaptedObject instanceof IFile)
			  {
				  File wsdlFile = ((IFile)adaptedObject).getLocation().toFile();
				  try
				  {
					  wsdlURL = wsdlFile.toURL().toString();
				  }
				  catch (MalformedURLException murle)
				  {
					  wsdlURL = wsdlFile.toString();
				  }
			  } else if (adaptedObject instanceof String) {
				  wsdlURL = (String) adaptedObject;
			  }
		  }
		  return wsdlURL;
	  }
	
}

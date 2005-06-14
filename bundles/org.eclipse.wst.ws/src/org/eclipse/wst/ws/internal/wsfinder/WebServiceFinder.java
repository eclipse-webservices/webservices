/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsfinder;

import java.util.Iterator;
import java.util.Vector;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author joan
 *
 */

public class WebServiceFinder {
	
	private static WebServiceFinder instance = null;
	
	private static final String CLASS_ATTRIBUTE= "class"; //$NON-NLS-1$
		
	public WebServiceFinder()	
	{
		super();
	}
	
	public static WebServiceFinder instance()
	{
		if (instance == null)
		{
			instance = new WebServiceFinder();
		}
		return instance;
	}
		
	public Iterator getWebServices()
	{
		Vector webServices = new Vector();

		WebServiceLocatorRegistry wslr = WebServiceLocatorRegistry.getInstance();

		IConfigurationElement[] regElements = wslr.getConfigElements();
		for (int i = 0; i < regElements.length; i++) {
			try{
			    Object obj = regElements[i].createExecutableExtension(CLASS_ATTRIBUTE);
			    
			    if (obj instanceof IWebServiceLocator)
			    {
			    	IWebServiceLocator wsl = (IWebServiceLocator)obj;
			    	List wsList = wsl.getWebServices();
			    	webServices.addAll(wsList);
			     }				
			} 
			catch (CoreException ex){
				
			}		
		} 
		return webServices.iterator();	
	} 
	
}

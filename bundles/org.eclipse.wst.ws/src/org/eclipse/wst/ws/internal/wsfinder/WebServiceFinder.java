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
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author joan
 *
 * WebServiceFinder is a singleton which finds all extenders and returns a comprehensive list of WebServiceInfo objects.
 * Finds web services by calling locators extending {@link org.eclipse.wst.ws.internal.wsfinder.AbstractWebServiceLocator} 
 * which have been registered with {@link org.eclipse.wst.ws.internal.wsfinder.WebServiceLocatorRegistry}.
 */

public class WebServiceFinder {

	private static WebServiceFinder instance = null;

	private static final String ELEMENT_LOCATOR = "webServiceLocator"; //$NON-NLS-1$
	private static final String ELEMENT_CATEGORY = "webServiceLocatorCategory"; //$NON-NLS-1$
	private static final String ATTRIBUTE_ID = "id"; //$NON-NLS-1$
	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$
	private static final String ATTRIBUTE_CATEGORY = "category"; //$NON-NLS-1$
//	private static final String ATTRIBUTE_LABEL = "label"; //$NON-NLS-1$
//	private static final String ATTRIBUTE_DESCRIPTION = "description"; //$NON-NLS-1$
//	private static final String ATTRIBUTE_ICON = "icon"; //$NON-NLS-1$

	/*
	 * Public construction is not allowed.
	 * Use WebServiceFinder.instance().
	 */
	private WebServiceFinder()	
	{
		super();
	}

	/**
	 * Returns the singleton of <code>WebServiceFinder</code>.
	 * @return The singleton of <code>WebServiceFinder</code>.
	 */
	public static WebServiceFinder instance()
	{
		if (instance == null)
		{
			instance = new WebServiceFinder();
		}
		return instance;
	}
		
	/**
	 * Returns an iterator of WebServiceInfo objects which represent web services found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point.  Currently returns all web 
	 * services found for all registered locators.  Locators must extend {@link AbstractWebServiceLocator}.
	 * 
	 * Callers can use the getter methods on the WebServiceInfo object to retrieve information on the 
	 * web services found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceInfo
	 * objects returned.  This is left to the locator implementations.
	 *  
	 * @return iterator of WebServiceInfo objects
	 */
	public Iterator getWebServices()
	{
		return getWebServices(null);
	}

	/**
	 * Returns an iterator of WebServiceInfo objects which represent web services found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * 
	 * Callers can use the getter methods on the WebServiceInfo object to retrieve information on the 
	 * web services found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceInfo
	 * objects returned.  This is left to the locator implementations.
	 *  
	 * @return iterator of WebServiceInfo objects
	 * @param category The category of locators to use
	 */
	public Iterator getWebServices(String category)
	{
		LinkedList webServices = new LinkedList();
		WebServiceLocatorRegistry wslr = WebServiceLocatorRegistry.getInstance();
		IConfigurationElement[] regElements = wslr.getConfigElements();
		for (int i = 0; i < regElements.length; i++)
		{
			if (ELEMENT_LOCATOR.equals(regElements[i].getName()))
			{
				try
				{
					if (category == null || category.equals(regElements[i].getAttributeAsIs(ATTRIBUTE_CATEGORY)))
					{
						Object obj = regElements[i].createExecutableExtension(ATTRIBUTE_CLASS);
						if (obj instanceof IWebServiceLocator)
						{
							IWebServiceLocator wsl = (IWebServiceLocator)obj;
							List wsList = wsl.getWebServices();
							webServices.addAll(wsList);
						}
					}
				} 
				catch (CoreException ex){
					// Quietly bypass any IWebServiceLocators that failed to be loaded.
				}
			}
		} 
		return webServices.iterator();	
	} 

	/**
	 * Returns an array of registered Web Service Category IDs.
	 *  
	 * @return An array, never null but possibly empty,
	 * of registered Web Service Category IDs.
	 */
	public String[] getCategoryIDs()
	{
		LinkedList categories = new LinkedList();
		WebServiceLocatorRegistry wslr = WebServiceLocatorRegistry.getInstance();
		IConfigurationElement[] regElements = wslr.getConfigElements();
		for (int i = 0; i < regElements.length; i++)
		{
			if (ELEMENT_CATEGORY.equals(regElements[i].getName()))
			{
				String id = regElements[i].getAttributeAsIs(ATTRIBUTE_ID);
				if (id != null)
				{
					categories.add(id);
				}
			}
		}
		return (String[])categories.toArray(new String[0]);
	}
	
	//TODO: Need another method to return full Category objects,
	//as in "public WebServiceLocatorCategory getCategories()"
	//where WebServiceLocatorCategory is a new bean class with properties
	//mirroring the attributes of a webServiceLocatorCategory extension.
}

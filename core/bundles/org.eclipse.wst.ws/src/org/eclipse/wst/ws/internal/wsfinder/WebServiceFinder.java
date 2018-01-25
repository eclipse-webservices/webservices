/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 * 20060620   147862 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsfinder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;

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
	private WebServiceFinder ()	
	{
		super();
	}

	/**
	 * Returns the singleton of <code>WebServiceFinder</code>.
	 * @return The singleton of <code>WebServiceFinder</code>.
	 */
	public static WebServiceFinder instance ()
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
	 * Callers can use the getter methods on the WebServiceInfo object to retrieve information on the 
	 * web services found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceInfo objects, never null.
	 */
	public Iterator getWebServices ( IProgressMonitor monitor )
	{
		return getWebServicesByCategoryId(null,monitor);
	}
	
	/**
	 * Returns an iterator of WebServiceInfo objects which represent web services found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point.  Currently returns all web 
	 * services found in the context of the given projects for all registered locators.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceInfo object to retrieve information on the 
	 * web services found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceInfo objects, never null.
	 */
	public Iterator getWebServices ( IProject[] projects, IProgressMonitor monitor )
	{
		return getWebServicesByCategoryId(null,projects,monitor);
	}
	
	/**
	 * Returns an iterator of WebServiceInfo objects which represent web services found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceInfo object to retrieve information on the 
	 * web services found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param category The category of locators to use.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceInfo objects, never null.
	 */
	public Iterator getWebServicesByCategory ( WebServiceCategory category, IProgressMonitor monitor  )
	{
		return getWebServicesByCategoryId(category == null ? null : category.getId(),monitor);
	}

	/**
	 * Returns an iterator of WebServiceInfo objects which represent web services found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * The search is constrained to the given set of one or more projects.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceInfo object to retrieve information on the 
	 * web services found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param category The category of locators to use.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceInfo objects, never null.
	 */
	public Iterator getWebServicesByCategory ( WebServiceCategory category, IProject[] projects, IProgressMonitor monitor  )
	{
		return getWebServicesByCategoryId(category == null ? null : category.getId(),projects,monitor);
	}

	/**
	 * Returns an iterator of WebServiceInfo objects which represent web services found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceInfo object to retrieve information on the 
	 * web services found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param categoryId The category of locators to use.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceInfo objects, never null.
	 */
	public Iterator getWebServicesByCategoryId ( String categoryId, IProgressMonitor monitor )
	{
		return getWebServicesByCategoryId(categoryId,null,monitor);	
	} 

	/**
	 * Returns an iterator of WebServiceInfo objects which represent web services found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * The search is constrained to the given set of one or more projects.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceInfo object to retrieve information on the 
	 * web services found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param categoryId The category of locators to use.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceInfo objects, never null.
	 */
	public Iterator getWebServicesByCategoryId ( String categoryId, IProject[] projects, IProgressMonitor monitor )
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
					if (categoryId == null || categoryId.equals(regElements[i].getAttributeAsIs(ATTRIBUTE_CATEGORY)))
					{
						Object obj = regElements[i].createExecutableExtension(ATTRIBUTE_CLASS);
						if (obj instanceof IWebServiceLocator)
						{
							IWebServiceLocator wsl = (IWebServiceLocator)obj;
							List wsList = projects == null ? wsl.getWebServices(monitor) : wsl.getWebServices(projects,monitor);
							if (wsList != null)
							{
								webServices.addAll(wsList);
							}
						}
					}
				} 
				catch (CoreException ex)
				{
					// Quietly bypass any IWebServiceLocators that failed to be loaded.
				}
			}
		} 
		return webServices.iterator();	
	} 

	/**
	 * Returns an iterator of WebServiceClientInfo objects which represent web service clients found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point.  Currently returns all web 
	 * service clients found for all registered locators.  Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceClientInfo object to retrieve information on the 
	 * web service clients found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceClientInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceClientInfo objects, never null.
	 */
	public Iterator getWebServiceClients ( IProgressMonitor monitor )
	{
		return getWebServiceClientsByCategoryId(null,monitor);
	}
	
	/**
	 * Returns an iterator of WebServiceClientInfo objects which represent web service clients found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point.  Currently returns all web 
	 * service clients found for all registered locators in the context of the given projects.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceClientInfo object to retrieve information on the 
	 * web service clients found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceClientInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceClientInfo objects, never null.
	 */
	public Iterator getWebServiceClients ( IProject[] projects, IProgressMonitor monitor )
	{
		return getWebServiceClientsByCategoryId(null,projects,monitor);
	}
	
	/**
	 * Returns an iterator of WebServiceClientInfo objects which represent web service clients found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * Locators must extend {@link AbstractWebServiceLocator}. 
	 * Callers can use the getter methods on the WebServiceClientInfo object to retrieve information on the 
	 * web service clients found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceClientInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param category The category of locators to use.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceClientInfo objects, never null.
	 */
	public Iterator getWebServiceClientsByCategory ( WebServiceCategory category, IProgressMonitor monitor )
	{
		return getWebServiceClientsByCategoryId(category == null ? null : category.getId(),monitor);
	}

	/**
	 * Returns an iterator of WebServiceClientInfo objects which represent web service clients found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * The search is constrained to the given set of one or more projects.
	 * Locators must extend {@link AbstractWebServiceLocator}. 
	 * Callers can use the getter methods on the WebServiceClientInfo object to retrieve information on the 
	 * web service clients found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceClientInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param category The category of locators to use.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceClientInfo objects, never null.
	 */
	public Iterator getWebServiceClientsByCategory ( WebServiceCategory category, IProject[] projects, IProgressMonitor monitor )
	{
		return getWebServiceClientsByCategoryId(category == null ? null : category.getId(),projects,monitor);
	}

	/**
	 * Returns an iterator of WebServiceClientInfo objects which represent web service clients found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceClientInfo object to retrieve information on the 
	 * web service clients found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceClientInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param categoryId The category of locators to use.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceClientInfo objects, never null.
	 */
	public Iterator getWebServiceClientsByCategoryId ( String categoryId, IProgressMonitor monitor )
	{
		return getWebServiceClientsByCategoryId(categoryId,null,monitor);
	} 

	/**
	 * Returns an iterator of WebServiceClientInfo objects which represent web service clients found by locators that
	 * have registered using the org.eclipse.wst.ws.locator extension point under the given category.
	 * The search is constrained to the given set of one or more projects.
	 * Locators must extend {@link AbstractWebServiceLocator}.
	 * Callers can use the getter methods on the WebServiceClientInfo object to retrieve information on the 
	 * web service clients found.  The WebServiceFinder cannot guarantee the level of detail contained in WebServiceClientInfo
	 * objects returned.  This is left to the locator implementations.
	 * @param categoryId The category of locators to use.
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return An iterator of WebServiceClientInfo objects, never null.
	 */
	public Iterator getWebServiceClientsByCategoryId ( String categoryId, IProject[] projects, IProgressMonitor monitor )
	{
		LinkedList webServiceClients = new LinkedList();
		WebServiceLocatorRegistry wslr = WebServiceLocatorRegistry.getInstance();
		IConfigurationElement[] regElements = wslr.getConfigElements();
		for (int i = 0; i < regElements.length; i++)
		{
			if (ELEMENT_LOCATOR.equals(regElements[i].getName()))
			{
				try
				{
					if (categoryId == null || categoryId.equals(regElements[i].getAttributeAsIs(ATTRIBUTE_CATEGORY)))
					{
						Object obj = regElements[i].createExecutableExtension(ATTRIBUTE_CLASS);
						if (obj instanceof IWebServiceLocator)
						{
							IWebServiceLocator wsl = (IWebServiceLocator)obj;
							List wsList = projects == null ? wsl.getWebServiceClients(monitor) : wsl.getWebServiceClients(projects,monitor);
							if (wsList != null)
							{
								webServiceClients.addAll(wsList);
							}
						}
					}
				} 
				catch (CoreException ex)
				{
					// Quietly bypass any IWebServiceLocators that failed to be loaded.
				}
			}
		} 
		return webServiceClients.iterator();	
	} 

	/**
	 * Returns an array of registered Web Service Category IDs.
	 * @return An array, never null but possibly empty,
	 * of registered Web Service Category descriptors.
	 */
	public WebServiceCategory[] getWebServiceCategories ()
	{
		LinkedList categories = new LinkedList();
		WebServiceLocatorRegistry wslr = WebServiceLocatorRegistry.getInstance();
		IConfigurationElement[] regElements = wslr.getConfigElements();
		for (int i = 0; i < regElements.length; i++)
		{
			if (ELEMENT_CATEGORY.equals(regElements[i].getName()))
			{
				categories.add(new WebServiceCategory(regElements[i]));
			}
		}
		return (WebServiceCategory[])categories.toArray(new WebServiceCategory[0]);

	}	

	/**
	 * Returns an array of registered Web Service Category IDs.
	 * @return An array, never null but possibly empty,
	 * of registered Web Service Category IDs.
	 */
	public String[] getWebServiceCategoryIds ()
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
	
	/**
	 * Returns the WebServiceCategory with the given ID,
	 * or null if there is none.
	 * @param categoryId The ID of the category to find.
	 * @return The matching category descriptor, or null if there is none.
	 */
	public WebServiceCategory getWebServiceCategoryById ( String categoryId )
	{
		WebServiceCategory category = null;
		if (categoryId != null)
		{
			WebServiceLocatorRegistry wslr = WebServiceLocatorRegistry.getInstance();
			IConfigurationElement[] regElements = wslr.getConfigElements();
			for (int i = 0; i < regElements.length; i++)
			{
				if (ELEMENT_CATEGORY.equals(regElements[i].getName()))
				{
					String id = regElements[i].getAttributeAsIs(ATTRIBUTE_ID);
					if (categoryId.equals(id))
					{
						return new WebServiceCategory(regElements[i]);
					}
				}
			}
		}
		return category;
	}
}

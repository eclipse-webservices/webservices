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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.internal.ext.WebServiceExtension;
import org.eclipse.wst.ws.internal.ext.WebServiceExtensionRegistryImpl;

/**
 * @author joan
 *
 * Retrieves locators which extend {@link org.eclipse.wst.ws.internal.wsfinder.AbstractWebServiceLocator}.
 * Locators must create an extension for the org.eclipse.wst.ws.locator extension point in order to be retrieved by
 * the registry.
 */


public class WebServiceLocatorRegistry extends WebServiceExtensionRegistryImpl {

	 // Copyright
	  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
	  /*
	   * This is a singleton becasue it was decided that the memory foot print 
	   * is not as expensive as the time taken retrieving the data
	   * 
	   */
	  
	  
	  private static WebServiceLocatorRegistry wslr;
	  
	  public static WebServiceLocatorRegistry getInstance()
	  {
	  	if(wslr == null) wslr = new WebServiceLocatorRegistry();
	    return wslr;
	  }
	  
	  private WebServiceLocatorRegistry()
	  {
	    super();
	  }
	  
	  /**
	  * Children registries will have different extension types 
	  * @return WebserviceExtension holds a config elem
	  * for that extension capable of creating an executable file
	  */
	  public WebServiceExtension createWebServiceExtension(IConfigurationElement configElement)
	  {
	    return new WebServiceLocatorExtension(configElement);
	  }

	  /**
	  * Contacts platform registry and gets all locators which have registered 
	  *  using the org.eclipse.wst.ws.locator extension point.
	  * @return IConfigurationElement[] an array of elements which contain locators 
	  * extending AbstractWebServiceLocator.
	  */
	  public IConfigurationElement[] getConfigElements()
	  {
	    IExtensionRegistry reg = Platform.getExtensionRegistry();
	    IConfigurationElement[] config = reg.getConfigurationElementsFor(
	                                     "org.eclipse.wst.ws",
	                                     "locator");
	    return config;    
	  }

}

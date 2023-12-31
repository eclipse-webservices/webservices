/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
 * 20060821   153833 makandre@ca.ibm.com - Andrew Mak, Allow the Web Service Test extension point to specify the supported client runtime
 * 20070314   154543 makandre@ca.ibm.com - Andrew Mak, WebServiceTestRegistry is tracking extensions using label attribute instead of ID
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ext.test;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.internal.ext.WebServiceExtension;
import org.eclipse.jst.ws.internal.ext.WebServiceExtensionRegistryImpl;

public class WebServiceTestRegistry extends WebServiceExtensionRegistryImpl
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  /*
   * This is a singleton becasue it was decided that the memory foot print 
   * is not as expensive as the time taken retrieving the data
   * 
   */
  
  
  private static WebServiceTestRegistry wstr;
  
  public static WebServiceTestRegistry getInstance()
  {
  	if(wstr == null) wstr = new WebServiceTestRegistry();
    return wstr;
  }
  
  private WebServiceTestRegistry()
  {
    super();
  }
  
  /**
   * Helper method to "join" an existing value and a new value together by
   * a space in between.  Handles special case when either value is null. 
   *  
   * @param existingValue The existing value
   * @param newValue The new value
   * @return If eiter the existing or new value is null, simply returns the
   * new value as is (which could be null).  Otherwise returns the value joined
   * by a space.
   */
  private String genNewValue(String existingValue, String newValue) {
	  
	  if (existingValue == null || newValue == null)
		  return newValue;
	  	  	 
	  return existingValue + " " + newValue;
  }
    
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.ext.WebServiceExtensionRegistryImpl#loadExtensions()
   */
  protected void loadExtensions ()
  {
	// keep track of the support runtimes for each test facility ID
    Hashtable supportedClientRuntimes = new Hashtable();
	  
    IConfigurationElement[] config = getConfigElements();
    
    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement elem = config[idx];
      String label = elem.getAttribute( "label" );
      String id =   elem.getAttribute( "id" );      
      
      // label is found, hence we have a master extension element
      if (label != null) {
    	  WebServiceExtension webServiceExtension = createWebServiceExtension(elem);	
    	  nameExtensionTable_.put(id,webServiceExtension);
    	  label_.add(label);
    	  id_.add(id);
      }
      
      // slave extension element, which only has the id and supportedClientRuntimes attributes
      else {
    	  String existingValue = (String) supportedClientRuntimes.get(id);
    	  String newValue = genNewValue(existingValue, elem.getAttribute("supportedClientRuntimes"));
    	  
    	  if (newValue != null)
    		  supportedClientRuntimes.put(id, newValue);
      }
    }
    
    // merge any supportedClientRuntimes attributes from the slave elements back to the master elements
    
    Iterator iter = nameExtensionTable_.values().iterator();
    
    while (iter.hasNext()) {
    	WebServiceTestExtension webServiceTestExtension = (WebServiceTestExtension) iter.next();
    
    	// skip if the extension does not allow restriction on the supported client runtimes
    	if (webServiceTestExtension.allowClientRuntimesRestriction()) {
    		String id = webServiceTestExtension.getId();
    		webServiceTestExtension.addRuntimes((String) supportedClientRuntimes.get(id));
    	}
    }    
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.ext.WebServiceExtensionRegistryImpl#getWebServiceExtensionNames()
   */
  public String[] getWebServiceExtensionNames() {
	return (String[])label_.toArray( new String[0] );
  } 

  /**
   * Return the ID corresponding to the label.
   * @param label The label.
   * @return The ID corresponding to the label, or if the label parameter is not a label
   * (must be already an ID), it is returned.
   */
  public String labelToId(String label) {
    int labelIndex = label_.indexOf(label);
    if (labelIndex == -1)
      return label;
    return (String) id_.elementAt(labelIndex);
  } 

  /**
   * Returns the WebServiceExtension with the given ID.
   * @param id The ID.
   * @return The WebServiceExtension object.
   */
  public WebServiceExtension getWebServiceExtensionsById(String id) {
    return (WebServiceExtension) nameExtensionTable_.get(id);
  } 

  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.ext.WebServiceExtensionRegistryImpl#getWebServiceExtensionsByName(java.lang.String)
   */
  public WebServiceExtension getWebServiceExtensionsByName(String name) {
    return getWebServiceExtensionsById(labelToId(name));
  }

  /**
  * Children registries will have different extension types 
  * @return WebserviceExtension holds a config elem
  * for that extension capable of creating an executable file
  */
  public WebServiceExtension createWebServiceExtension(IConfigurationElement configElement)
  {
    return new WebServiceTestExtension(configElement);
  }

  /**
  * Children must implement how they get the IConfigurationElement[] 
  * @return IConfigurationElement[] an array of elements particular to that
  * extension
  */
  public IConfigurationElement[] getConfigElements()
  {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.consumption.ui",
                                     "tester");
    return config;    
  }


    
  
}

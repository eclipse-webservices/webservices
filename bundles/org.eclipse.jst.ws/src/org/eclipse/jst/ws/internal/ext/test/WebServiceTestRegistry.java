/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ext.test;

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
                                     "webServiceTest");
    return config;    
  }


    
  
}

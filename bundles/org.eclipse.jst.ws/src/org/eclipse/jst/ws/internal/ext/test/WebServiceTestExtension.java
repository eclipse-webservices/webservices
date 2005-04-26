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
import org.eclipse.jst.ws.internal.ext.WebServiceExtensionImpl;



/**
* This represents an extension in the plugin registry 
* It job is to act as a proxy to the iconfigelement
* In particular it holds a client test extension element 
*/
public class WebServiceTestExtension extends WebServiceExtensionImpl
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  public WebServiceTestExtension(IConfigurationElement configElement)
  {
    super(configElement);
  }
  
  /**
  * Will this testClient generate code
  * @return boolean true if codegen needed
  */
  public boolean isCodeGenNeeded()
  {
    return Boolean.valueOf(getConfigElement().getAttribute( "codegen" )).booleanValue();
  }
  
  /**
  * Use the default Folder Provided by us
  * @return boolean true if no special folder requires
  */
  public boolean useDefaultCodeGenFolder()
  {
    return Boolean.valueOf(getConfigElement().getAttribute( "defaultcodegenfolder" )).booleanValue();
  }

  /**
  * Use Special Folder for generated code
  * @return String special folder name
  */
  public String getCodeGenFolder()
  {
    return getConfigElement().getAttribute( "codegenfolder" );
  }

  /**
  * Do we need to launch something
  * @return Boolean if true launch codegen
  */
  public boolean isLaunchRequired()
  {
    return Boolean.valueOf(getConfigElement().getAttribute( "launchRequired" )).booleanValue();
  }

  /**
  * Do we need the methods for the proxy
  * @return boolean true if proxy methods needed
  */
  public boolean areMethodsNeeded()
  {
    return Boolean.valueOf(getConfigElement().getAttribute( "methodsneeded" )).booleanValue();
  }

  /**
  * Does this test machine need a server
  * @return boolean true if a server is required
  */
  public boolean isServerNeeded()
  {
    return Boolean.valueOf(getConfigElement().getAttribute( "serverneeded" )).booleanValue();
  }

  /**
  * Use the Default Server chosen by the wizard
  * @return boolean true if the default server is wanted
  */
  public boolean useDefaultServer()
  {
    return Boolean.valueOf(getConfigElement().getAttribute( "defaultserver" )).booleanValue();
  }

  /**
  * Use the Default Server chosen by the wizard
  * @return boolean true if the default server is wanted
  */
  public String useServer()
  {
    return getConfigElement().getAttribute( "useserver" );
  }

  public boolean testWSDL()
  {
  	String wsdl = getConfigElement().getAttribute( "testWSDL" );
    if(wsdl.equals("yes") || wsdl.equals("true"))
	  return true;
	 
	return false;  
  }
  	
  
}



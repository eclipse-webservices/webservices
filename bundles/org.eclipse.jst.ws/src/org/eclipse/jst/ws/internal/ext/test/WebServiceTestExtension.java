/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060816   104870 kathy@ca.ibm.com - Kathy Chan
 * 20060821   153833 makandre@ca.ibm.com - Andrew Mak, Allow the Web Service Test extension point to specify the supported client runtime
 * 20070314   154543 makandre@ca.ibm.com - Andrew Mak, WebServiceTestRegistry is tracking extensions using label attribute instead of ID
 * 20080325   184761 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ext.test;

import java.util.ArrayList;
import java.util.List;

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

  private List supportedRuntimes_ = new ArrayList();
  
  public WebServiceTestExtension(IConfigurationElement configElement)
  {
    super(configElement);
       
    String runtimesList = getConfigElement().getAttribute("supportedClientRuntimes");  
   
    addRuntimes(runtimesList);
  }
  
  /**
   * Adds a list of space delimited runtime IDs.
   * 
   * @param runtimesList The list of runtime IDs.
   */
  public void addRuntimes(String runtimesList) {
	  
	  if (runtimesList == null)
		  return;
	  
	  String[] runtimes = runtimesList.split("\\s+");
	    
	  for (int i = 0; i < runtimes.length; i++) {	    	
		  if (runtimes[i].length() > 0)
			  supportedRuntimes_.add(runtimes[i]);   
	  }
  }
  
  /**
  * Will this testClient generate code
  * @return boolean true if codegen needed
  */
  public boolean isCodeGenNeeded()
  {
    return Boolean.valueOf(getConfigElement().getAttribute( "codeGenerated" )).booleanValue();
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

  public boolean isDefaultJAXRPC()
  {
	  return Boolean.valueOf(getConfigElement().getAttribute( "isdefaultjaxrpc" )).booleanValue();
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
    return Boolean.valueOf(getConfigElement().getAttribute( "serverRequired" )).booleanValue();
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
  
  public boolean testJavaProxy()
  {
  	String javaProxy = getConfigElement().getAttribute( "testJavaProxy" );
    if(javaProxy == null) return false;
  	if(javaProxy.equals("yes") || javaProxy.equals("true"))
	  return true;
	 
	return false;  
  }
  	
  /**
   * Returns the id attribute of this WebServiceTestExtension
   * 
   * @return The value of the id attribute.
   */
  public String getId() {
	return getConfigElement().getAttribute( "id" ); 
  }
   
  /**
   * Returns the label attribute of this WebServiceTestExtension
   * 
   * @return The value of the label attribute.
   */
  public String getLabel() {
    return getConfigElement().getAttribute( "label" );
  }
  
  /**
   * Does this extension allow extenders to restrict the runtimes that are supported?
   *  
   * @return The value of the allowRunimesRestriction attribute, false if not present.
   */
  public boolean allowClientRuntimesRestriction() {
	  return Boolean.valueOf(getConfigElement().getAttribute( "allowClientRuntimesRestriction" )).booleanValue();
  }  
       
  /**
   * Determines if this WebServiceTestExtension supports the give client runtime ID.
   * 
   * @return true if the client runtime ID is supported, false otherwise.
   */
  public boolean supportsRuntime(String runtimeId) {
	  
	 if (!allowClientRuntimesRestriction() || supportedRuntimes_.isEmpty())
		 return true;
	  
	 return supportedRuntimes_.contains(runtimeId);
  }
}



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

package org.eclipse.jst.ws.internal.ext;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.Log;





/**
* This represents an extension in the plugin registry 
* It job is to act as a proxy to the iconfigelement
*/
public class WebServiceExtensionImpl implements WebServiceExtension
{

	private Log	log_;
	
  /*
  * The ConfigElement that holds extension info
  */
  protected IConfigurationElement configElement_;

  /*
  * The extension executable 
  */
  protected WebServiceExecutable webServiceExecutable_;
  

  public WebServiceExtensionImpl(IConfigurationElement configElement)
  {
    configElement_ = configElement;
    log_ = EnvironmentService.getEclipseLog();
  }

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * If the extension has code that needs to be executed or a fragment added
  * @return WebServiceExecutable this interface holds executable code and a 
  * fragment
  */
  public Object getWebServiceExecutableExtension()
  {
    if(webServiceExecutable_ != null) return webServiceExecutable_;

    try{
      return configElement_.createExecutableExtension("class");
    }catch (CoreException e){
      log_.log(Log.ERROR, 5029, this, "getWebServiceExecutableExtension",e);
    }

    return null;
  }

  /**
  * This is the config element that holds the extension info
  * @param IConfigurationElement Extension element
  */
  public void setConfigElement(IConfigurationElement configElement)
  {
    configElement_ = configElement;
  }	

  /**
  * This is the config element that holds the extension info
  * @return IConfigurationElement Extension element
  */
  public IConfigurationElement getConfigElement()
  {
    return configElement_;
  }
  
  /**
  * The name of the extension being used
  * @return String name of this extension
  */
  public String getName()
  {
    return getConfigElement().getAttribute( "name" );
  }


}



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

package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUICore;

/**
* This is the interface for objects that represent a kind of
* Web Service artifact. The primary purpose of a WebServiceType
* object is to manufacture the wizard pages that support the type.
*/
public class WebServiceServer implements IWebServiceServer
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  public IConfigurationElement element_;
  public String serverId_;
  public String factoryId_;
  public String serverLabel_;

  public WebServiceServer(IConfigurationElement elem)
  {
    super();
    this.element_ = elem; 
  }
  /**
  * Returns the id of this Web Service type.
  * @return id of this Web Service type.
  */
  public String getId ()
  {
    if (serverId_==null)
      serverId_ = element_.getAttribute("id");
    return serverId_;
  }
  /**
  * Returns a short, locale specific name of this Web Service type.
  * @return A short, locale specific name of this Web Service type.
  */
  public String getFactoryId ()
  {
    if (factoryId_==null)
      factoryId_ = element_.getAttribute("factoryId");
    return factoryId_;
  }

  /**
  * Returns a short, locale specific name of this Web Service type.
  * @return A short, locale specific name of this Web Service type.
  */
  public String getLabel ()
  {
    if (serverLabel_ == null)
    {
      IServerType serverType = ServerCore.findServerType(getFactoryId());    
      serverLabel_ = ServerUICore.getLabelProvider().getText(serverType);
    }
    return serverLabel_;    
  }  

  /**
   *  Returns a True if this server has been set as default
   *  @return a boolean
   */
  public boolean getIsDefault()
  {

    return Boolean.valueOf(element_.getAttribute("isDefault")).booleanValue();
  }
  
  /**
   * Returns runtime target Id
   * @return String
   */
  public String getRuntimeTypeId()
  {
    return element_.getAttribute("runtimeTypeIds");
  }
  
  /**
   * Returns the configuration element associated with
   * this type.
   * 
   * @return org.eclipse.core.runtime.IConfigurationElement
   */
  public IConfigurationElement getConfigurationElement()
  {
    return this.element_; 
  } 

  }

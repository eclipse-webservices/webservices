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

package org.eclipse.jst.ws.internal.wsrt;

import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceTypeImpl;

public class WebServiceRuntimeExtensionRegistry
{

  private static WebServiceRuntimeExtensionRegistry instance_;

  HashMap webServiceTypes_;

  Hashtable webServiceImpls_;

  Hashtable webServiceClientImpls_;

  Hashtable serviceTypes_;

  Hashtable clientTypes_;

  Hashtable webServiceRuntimes_;

  /**
   * Returns a singleton instance of this class.
   * 
   * @return A singleton WebServiceRuntimeExtensionRegistry object.
   */
  public static WebServiceRuntimeExtensionRegistry getInstance()
  {
    if (instance_ == null)
    {
      instance_ = new WebServiceRuntimeExtensionRegistry();
      instance_.load();
    }
    return instance_;
  }

  private void load()
  {
    // Can't get rid of webServiceTypes in M4
    webServiceTypes_ = new HashMap();
    webServiceImpls_ = new Hashtable();
    webServiceClientImpls_ = new Hashtable();
    serviceTypes_ = new Hashtable();
    clientTypes_ = new Hashtable();
    webServiceRuntimes_ = new Hashtable();

    IExtensionRegistry reg = Platform.getExtensionRegistry();
  
    //Load WebServiceTypes
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.consumption.ui",
                                     "webServiceServerRuntimeType");
    
    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement elem = config[idx];

        if (elem.getName().equals("webServiceType"))
        {
          WebServiceTypeImpl wst = new WebServiceTypeImpl(elem);
          webServiceTypes_.put(elem.getAttribute("id"), wst);
        }        
    }

    //Load WebServiceImpls
    IConfigurationElement[] wsImplExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "wsImpl");
    
    for(int idx=0; idx<wsImplExts.length; idx++) 
    {
      IConfigurationElement elem = wsImplExts[idx];

        if (elem.getName().equals("webServiceImpl"))
        {
          WebServiceImpl wsimpl = new WebServiceImpl(elem);
          webServiceImpls_.put(elem.getAttribute("id"), wsimpl);
        }        
    }
    
    //Load WebServiceClientImpls
    IConfigurationElement[] wsClientImplExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "wsClientImpl");
    
    for(int idx=0; idx<wsClientImplExts.length; idx++) 
    {
      IConfigurationElement elem = wsClientImplExts[idx];

        if (elem.getName().equals("webServiceClientImpl"))
        {
          WebServiceClientImpl wsClientImpl = new WebServiceClientImpl(elem);
          webServiceClientImpls_.put(elem.getAttribute("id"), wsClientImpl);
        }        
    }
    
    //Load ServiceTypes
    IConfigurationElement[] serviceTypeExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "serviceType");
    
    for(int idx=0; idx<serviceTypeExts.length; idx++) 
    {
      IConfigurationElement elem = serviceTypeExts[idx];

        if (elem.getName().equals("serviceType"))
        {
          ServiceType st = new ServiceType(elem,webServiceImpls_,webServiceTypes_);
          serviceTypes_.put(elem.getAttribute("id"), st);
        }        
    }
    
    //Load ClientTypes
    IConfigurationElement[] clientTypeExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "clientType");
    
    for(int idx=0; idx<clientTypeExts.length; idx++) 
    {
      IConfigurationElement elem = clientTypeExts[idx];

        if (elem.getName().equals("clientType"))
        {
          ClientType ct = new ClientType(elem,webServiceClientImpls_);
          clientTypes_.put(elem.getAttribute("id"), ct);
        }        
    }
    
    //Load WebSerivceRuntimes
    IConfigurationElement[] wsrtExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "webServiceRuntime");
    
    for(int idx=0; idx<wsrtExts.length; idx++) 
    {
      IConfigurationElement elem = wsrtExts[idx];

        if (elem.getName().equals("webServiceRuntime"))
        {
          WebServiceRuntimeInfo wsrtInfo = new WebServiceRuntimeInfo(elem, serviceTypes_, clientTypes_);
          webServiceRuntimes_.put(elem.getAttribute("id"), wsrtInfo);
        }        
    }
  }  
    
}

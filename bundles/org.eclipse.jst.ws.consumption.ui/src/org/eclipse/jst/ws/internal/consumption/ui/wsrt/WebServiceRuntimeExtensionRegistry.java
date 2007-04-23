/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class WebServiceRuntimeExtensionRegistry
{

  private static WebServiceRuntimeExtensionRegistry instance_;

  HashMap webServiceTypes_;
  
  ArrayList webServiceTypesList_;
  
  ArrayList webServiceClientTypesList_;

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
    webServiceTypesList_ = new ArrayList();
    webServiceClientTypesList_ = new ArrayList();
    webServiceImpls_ = new Hashtable();
    webServiceClientImpls_ = new Hashtable();
    serviceTypes_ = new Hashtable();
    clientTypes_ = new Hashtable();
    webServiceRuntimes_ = new Hashtable();

    IExtensionRegistry reg = Platform.getExtensionRegistry();
    
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
          ServiceType st = new ServiceType(elem,webServiceImpls_);
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
          
          //Update the type lists
          updateWebServiceTypeList(wsrtInfo);
          updateWebServiceClientTypeList(wsrtInfo);
          
        }        
    }
  }  
    
  private void updateWebServiceTypeList(WebServiceRuntimeInfo wsrtInfo)
  {
    ServiceType[] sts = wsrtInfo.getServiceTypes();
    if (sts != null)
    {
      for (int j = 0; j < sts.length; j++)
      {
        String implId = sts[j].getWebServiceImpl().getId();
        String[] bus = sts[j].getBottomUpModuleTypesInclude();
        String[] tds = sts[j].getTopDownModuleTypesInclude();
        if (bus != null)
        {
          StringBuffer entrybuff = new StringBuffer();
          entrybuff.append(String.valueOf(WebServiceScenario.BOTTOMUP));
          entrybuff.append("/");
          entrybuff.append(implId);
          String entry = entrybuff.toString();
          if (!webServiceTypesList_.contains(entry))
          {
            webServiceTypesList_.add(entry);
          }
        }
        if (tds != null)
        {
          StringBuffer entrybuff = new StringBuffer();
          entrybuff.append(String.valueOf(WebServiceScenario.TOPDOWN));
          entrybuff.append("/");
          entrybuff.append(implId);
          String entry = entrybuff.toString();
          if (!webServiceTypesList_.contains(entry))
          {
            webServiceTypesList_.add(entry);
          }
        }
      }
    }
  }
  
  private void updateWebServiceClientTypeList(WebServiceRuntimeInfo wsrtInfo)
  {
    ClientType[] cts = wsrtInfo.getClientTypes();
    if (cts != null)
    {
      for (int j = 0; j < cts.length; j++)
      {
        String implId = cts[j].getWebServiceClientImpl().getId();
        String[] mods = cts[j].getModuleTypesInclude();
        if (mods != null)
        {
          StringBuffer entrybuff = new StringBuffer();
          entrybuff.append(String.valueOf(WebServiceScenario.CLIENT));
          entrybuff.append("/");
          entrybuff.append(implId);
          String entry = entrybuff.toString();
          if (!webServiceClientTypesList_.contains(entry))
          {
            webServiceClientTypesList_.add(entry);
          }
        }
      }
    }
  }
  
}

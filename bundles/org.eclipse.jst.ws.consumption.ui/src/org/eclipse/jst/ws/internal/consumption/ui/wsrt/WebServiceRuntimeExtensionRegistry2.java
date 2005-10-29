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

package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class WebServiceRuntimeExtensionRegistry2
{

  private static WebServiceRuntimeExtensionRegistry2 instance_;


  Hashtable webServiceImpls_;

  Hashtable webServiceClientImpls_;

  Hashtable runtimes_;
  
  Hashtable serviceRuntimes_;
  
  Hashtable clientRuntimes_;
  
  ArrayList webServiceTypesList_;
  
  ArrayList webServiceClientTypesList_;  
  
  
  /**
   * Returns a singleton instance of this class.
   * 
   * @return A singleton WebServiceRuntimeExtensionRegistry2 object.
   */
  public static WebServiceRuntimeExtensionRegistry2 getInstance()
  {
    if (instance_ == null)
    {
      instance_ = new WebServiceRuntimeExtensionRegistry2();
      instance_.load();
    }
    return instance_;
  }

  private void load()
  {
    webServiceImpls_ = new Hashtable();
    webServiceClientImpls_ = new Hashtable();
    runtimes_ = new Hashtable();
    serviceRuntimes_ = new Hashtable();
    clientRuntimes_ = new Hashtable();
    webServiceTypesList_ = new ArrayList();
    webServiceClientTypesList_ = new ArrayList();
    
    
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    
    //Load WebServiceImpls
    IConfigurationElement[] wsImplExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "wsImpl");
    
    for(int idx=0; idx<wsImplExts.length; idx++) 
    {
      IConfigurationElement elem = wsImplExts[idx];
        System.out.println("element name = "+elem.getName());
        if (elem.getName().equals("webServiceImpl"))
        {
          WebServiceImpl wsimpl = new WebServiceImpl(elem);
          System.out.println(elem.getAttribute("id"));
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
    
    //Load runtimes
    IConfigurationElement[] runtimeExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "runtimes");
    
    for(int idx=0; idx<runtimeExts.length; idx++) 
    {
      IConfigurationElement elem = runtimeExts[idx];

        if (elem.getName().equals("runtime"))
        {
          RuntimeDescriptor rd = new RuntimeDescriptor(elem);
          runtimes_.put(elem.getAttribute("id"), rd);
        }        
    }
    
    //Load serviceRuntimes
    IConfigurationElement[] serviceRuntimeExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "serviceRuntimes");
    
    for(int idx=0; idx<serviceRuntimeExts.length; idx++) 
    {
      IConfigurationElement elem = serviceRuntimeExts[idx];

        if (elem.getName().equals("serviceRuntime"))
        {
          ServiceRuntimeDescriptor rd = new ServiceRuntimeDescriptor(elem, webServiceImpls_, runtimes_);
          serviceRuntimes_.put(elem.getAttribute("id"), rd);          
          updateWebServiceTypeList(rd);
        }        
    }
    
    //Load serviceRuntimes
    IConfigurationElement[] clientRuntimeExts = reg.getConfigurationElementsFor(
        "org.eclipse.jst.ws.consumption.ui", "clientRuntimes");
    
    for(int idx=0; idx<clientRuntimeExts.length; idx++) 
    {
      IConfigurationElement elem = clientRuntimeExts[idx];

      if (elem.getName().equals("clientRuntime"))
      {
        ClientRuntimeDescriptor rd = new ClientRuntimeDescriptor(elem, webServiceClientImpls_, runtimes_);
        clientRuntimes_.put(elem.getAttribute("id"), rd);
        updateWebServiceClientTypeList(rd);
          
      }        
    }
  }  
    
  private void updateWebServiceTypeList(ServiceRuntimeDescriptor descriptor)
  {
    String serviceImplId = descriptor.getServiceImplementationType().getId();
    boolean bottomUp = descriptor.getBottomUp();
    boolean topDown = descriptor.getTopDown();
    if (bottomUp)
    {
      StringBuffer entrybuff = new StringBuffer();
      entrybuff.append(String.valueOf(WebServiceScenario.BOTTOMUP));
      entrybuff.append("/");
      entrybuff.append(serviceImplId);
      String entry = entrybuff.toString();
      if (!webServiceTypesList_.contains(entry))
      {
        webServiceTypesList_.add(entry);
      }      
    }
    
    if (topDown)
    {
      StringBuffer entrybuff = new StringBuffer();
      entrybuff.append(String.valueOf(WebServiceScenario.TOPDOWN));
      entrybuff.append("/");
      entrybuff.append(serviceImplId);
      String entry = entrybuff.toString();
      if (!webServiceTypesList_.contains(entry))
      {
        webServiceTypesList_.add(entry);
      }      
    }
  }
  
  private void updateWebServiceClientTypeList(ClientRuntimeDescriptor descriptor)
  {
    String clientImplId = descriptor.getClientImplementationType().getId();
    StringBuffer entrybuff = new StringBuffer();
    entrybuff.append(String.valueOf(WebServiceScenario.CLIENT));
    entrybuff.append("/");
    entrybuff.append(clientImplId);
    String entry = entrybuff.toString();
    if (!webServiceClientTypesList_.contains(entry))
    {
      webServiceClientTypesList_.add(entry);
    }    
  }
  
}

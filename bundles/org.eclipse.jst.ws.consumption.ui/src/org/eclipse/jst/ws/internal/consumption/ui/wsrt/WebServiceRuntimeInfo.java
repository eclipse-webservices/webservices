/*
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

import java.util.Hashtable;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceRuntime;


public class WebServiceRuntimeInfo {

  private IConfigurationElement elem_;
  private Hashtable allServiceTypes_;
  private Hashtable allClientTypes_;
  
  private String id;
  private String label;
	private ServiceType[] serviceTypes;
	private ClientType[] clientTypes;
	private java.lang.String[] servletLevels;
	private java.lang.String[] j2eeLevels;
	private java.lang.String[] runtimeIds;
	private java.lang.String[] serverFactoryIds;
	private String className;
	private IWebServiceRuntime webServiceRuntime_;
  
  public WebServiceRuntimeInfo(IConfigurationElement elem_, Hashtable allServiceTypes_, Hashtable allClientTypes_)
  {
    super();
    this.elem_ = elem_;
    this.allServiceTypes_ = allServiceTypes_;
    this.allClientTypes_ = allClientTypes_;
  }

  public String getClassName()
  {
    if (className==null)
    {
      className=elem_.getAttribute("class");
    }
    return className;
  }
  

  public ClientType[] getClientTypes()
  {
    if (clientTypes==null)
    {
      String attr = elem_.getAttribute("clientTypes");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        clientTypes = new ClientType[size];
        for (int i = 0; i < clientTypes.length; i++)
        {
          String ctid = st.nextToken();          
          clientTypes[i] = (ClientType)allClientTypes_.get(ctid);
        }
      }
    }
    return clientTypes;
  }
  

  public String getId()
  {
    if (id==null)
    {
      id = elem_.getAttribute("id");
    }
    return id;
  }
  

  public String getLabel()
  {
    if (label==null)
    {
      label = elem_.getAttribute("label");
    }
    return label;
  }   

  
  public java.lang.String[] getJ2eeLevels()
  {
    if (j2eeLevels == null)
    {
      String attr = elem_.getAttribute("j2eeLevels");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        j2eeLevels = new String[size];
        for (int i = 0; i < j2eeLevels.length; i++)
          j2eeLevels[i] = st.nextToken();
      }
    }      
    
    return j2eeLevels;
  }
  

  public java.lang.String[] getRuntimeIds()
  {
    if (runtimeIds == null)
    {
      String attr = elem_.getAttribute("runtimes");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        runtimeIds = new String[size];
        for (int i = 0; i < runtimeIds.length; i++)
          runtimeIds[i] = st.nextToken();
      }
    }      
    
    return runtimeIds;
  }
  

  public java.lang.String[] getServerFactoryIds()
  {
    if (serverFactoryIds == null)
    {
      String attr = elem_.getAttribute("servers");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        serverFactoryIds = new String[size];
        for (int i = 0; i < serverFactoryIds.length; i++)
          serverFactoryIds[i] = st.nextToken();
      }      
    }
    return serverFactoryIds;
  }
  

  public ServiceType[] getServiceTypes()
  {
    if (serviceTypes==null)
    {
      String attr = elem_.getAttribute("serviceTypes");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        serviceTypes = new ServiceType[size];
        for (int i = 0; i < serviceTypes.length; i++)
        {
          String ctid = st.nextToken();          
          serviceTypes[i] = (ServiceType)allServiceTypes_.get(ctid);
        }
      }
    }
    return serviceTypes;
  }
  

  public java.lang.String[] getServletLevels()
  {
    if (servletLevels==null)
    {
      String attr = elem_.getAttribute("servletLevels");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        servletLevels = new String[size];
        for (int i = 0; i < servletLevels.length; i++)
          servletLevels[i] = st.nextToken();
      }      
    }
    return servletLevels;
  }
  
  public IWebServiceRuntime getWebServiceRuntime()
  {
	if (webServiceRuntime_ == null)
	{
		try
		{
			webServiceRuntime_ = (IWebServiceRuntime)elem_.createExecutableExtension("class");
		}
		catch(CoreException ce)
		{
			
		}
	}
	
	return webServiceRuntime_;
  }
  
}

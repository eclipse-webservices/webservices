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
package org.eclipse.jst.ws.internal.wsrt;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.IWebServiceType;

public class ServiceType {

  private IConfigurationElement elem_;
  private Hashtable webServiceImpls_;
  private HashMap webServiceTypes_;
	private String id;
	private WebServiceImpl webServiceImpl;
	private String[] bottomUpModuleTypesInclude;
	private String[] topDownModuleTypesInclude;
  private String[] bottomUpModuleTypesExclude;
  private String[] topDownModuleTypesExclude;
  private IWebServiceType webServiceType;
  

  
  public ServiceType(IConfigurationElement elem_, Hashtable webServiceImpls_, HashMap webServiceTypes_)
  {
    super();
    this.elem_ = elem_;
    this.webServiceImpls_ = webServiceImpls_;
    this.webServiceTypes_ = webServiceTypes_;
  }

  public String[] getBottomUpModuleTypesExclude()
  {
    if (bottomUpModuleTypesExclude==null)
    {
      String attr = elem_.getAttribute("buModuleTypesExclude");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        bottomUpModuleTypesExclude = new String[size];
        for (int i = 0; i < bottomUpModuleTypesExclude.length; i++)
          bottomUpModuleTypesExclude[i] = st.nextToken();
      }
    }
    return bottomUpModuleTypesExclude;
  }
  
  public void setBottomUpModuleTypesExclude(String[] bottomUpModuleTypesExclude)
  {
    this.bottomUpModuleTypesExclude = bottomUpModuleTypesExclude;
  }
  
  public String[] getBottomUpModuleTypesInclude()
  {
    if (bottomUpModuleTypesInclude==null)
    {
      String attr = elem_.getAttribute("buModuleTypesInclude");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        bottomUpModuleTypesInclude = new String[size];
        for (int i = 0; i < bottomUpModuleTypesInclude.length; i++)
          bottomUpModuleTypesInclude[i] = st.nextToken();
      }
    }
    return bottomUpModuleTypesInclude;
  }
  
  public void setBottomUpModuleTypesInclude(String[] bottomUpModuleTypesInclude)
  {
    this.bottomUpModuleTypesInclude = bottomUpModuleTypesInclude;
  }
  
  public String getId()
  {
    if (id==null)
    {
      id = elem_.getAttribute("id");
    }
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String[] getTopDownModuleTypesExclude()
  {
    if (topDownModuleTypesExclude==null)
    {
      String attr = elem_.getAttribute("tdModuleTypesExclude");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        topDownModuleTypesExclude = new String[size];
        for (int i = 0; i < topDownModuleTypesExclude.length; i++)
          topDownModuleTypesExclude[i] = st.nextToken();
      }
    }
    return topDownModuleTypesExclude;
  }
  
  public void setTopDownModuleTypesExclude(String[] topDownModuleTypesExclude)
  {
    this.topDownModuleTypesExclude = topDownModuleTypesExclude;
  }
  
  public String[] getTopDownModuleTypesInclude()
  {
    if (topDownModuleTypesInclude==null)
    {
      String attr = elem_.getAttribute("tdModuleTypesInclude");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        topDownModuleTypesInclude = new String[size];
        for (int i = 0; i < topDownModuleTypesInclude.length; i++)
          topDownModuleTypesInclude[i] = st.nextToken();
      }
    }
    return topDownModuleTypesInclude;
  }
  
  public void setTopDownModuleTypesInclude(String[] topDownModuleTypesInclude)
  {
    this.topDownModuleTypesInclude = topDownModuleTypesInclude;
  }
  
  public IWebServiceType getWebSerivceType()
  {
    if (webServiceType==null)
    {
      String wsid = elem_.getAttribute("webServiceTypeId");
      webServiceType = (IWebServiceType)webServiceTypes_.get(wsid);
    }
    return webServiceType;
  }
  
  public void setWebServiceType(IWebServiceType webServiceType)
  {
    this.webServiceType = webServiceType;
  }
  
  public WebServiceImpl getWebServiceImpl()
  {
    if (webServiceImpl == null)
    {
      String wsimplId = elem_.getAttribute("implId");
      webServiceImpl = (WebServiceImpl)webServiceImpls_.get(wsimplId);
    }
    return webServiceImpl;
  }
  
  public void setWebServiceImpl1(WebServiceImpl webServiceImpl)
  {
    this.webServiceImpl = webServiceImpl;
  }
  
	
  
	
	
	
}

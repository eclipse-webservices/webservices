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

import java.util.Hashtable;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;

public class ClientType {

  private IConfigurationElement elem_;
  private Hashtable webServiceClientImpls_;
    
	private String id;
	private WebServiceClientImpl webServiceClientImpl;
	private String[] moduleTypesInclude;
  private String[] moduleTypesExclude;
  private String webServiceClientTypeId;
	
  
  
	public ClientType(IConfigurationElement elem_, Hashtable webServiceClientImpls_)
  {
    super();
    this.elem_ = elem_;
    this.webServiceClientImpls_ = webServiceClientImpls_;
  }



  public String getId()
  {
    if (id==null)
    {
      id = elem_.getAttribute("id");
    }
    return id;
  }
  



  public String[] getModuleTypesExclude()
  {
    if (moduleTypesExclude==null)
    {
      String attr = elem_.getAttribute("moduleTypesExclude");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        moduleTypesExclude = new String[size];
        for (int i = 0; i < moduleTypesExclude.length; i++)
          moduleTypesExclude[i] = st.nextToken();
      }
    }
    return moduleTypesExclude;
  }
  



  public String[] getModuleTypesInclude()
  {
    if (moduleTypesInclude==null)
    {
      String attr = elem_.getAttribute("moduleTypesInclude");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        moduleTypesInclude = new String[size];
        for (int i = 0; i < moduleTypesInclude.length; i++)
          moduleTypesInclude[i] = st.nextToken();
      }
    }
    return moduleTypesInclude;
  }
  



  public WebServiceClientImpl getWebServiceClientImpl()
  {
    if (webServiceClientImpl==null)
    {
      String wscimplId = elem_.getAttribute("implId");
      webServiceClientImpl = (WebServiceClientImpl)webServiceClientImpls_.get(wscimplId);
    }
    return webServiceClientImpl;
  }
  



  public String getWebServiceClientTypeId()
  {
    if (webServiceClientTypeId==null)
    {
      webServiceClientTypeId = elem_.getAttribute("webServiceClientTypeId");
    }
    return webServiceClientTypeId;
  }
  


	
	
	
}

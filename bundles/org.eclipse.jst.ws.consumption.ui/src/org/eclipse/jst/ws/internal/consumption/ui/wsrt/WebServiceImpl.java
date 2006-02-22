/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060216   127138 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import org.eclipse.core.runtime.IConfigurationElement;
import com.ibm.icu.util.StringTokenizer;

public class WebServiceImpl {
	
  private IConfigurationElement elem_;
	private String id;
	private String label;
	private String[] resourceTypeMetadata;
	private String[] extensionMetadata;
  private String objectSelectionWidget;
  
  public WebServiceImpl(IConfigurationElement elem_)
  {
    super();
    this.elem_ = elem_;
  }

  public String[] getExtensionMetadata()
  {
    if (extensionMetadata==null)
    {
      String attr = elem_.getAttribute("extensionMetadata");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        extensionMetadata = new String[size];
        for (int i = 0; i < extensionMetadata.length; i++)
          extensionMetadata[i] = st.nextToken();
      }      
    }
    return extensionMetadata;
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
  

  public String[] getResourceTypeMetadata()
  {
    if (resourceTypeMetadata==null)
    {
      String attr = elem_.getAttribute("resourceTypeMetadata");
      if (attr != null && attr.length()>0)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        resourceTypeMetadata = new String[size];
        for (int i = 0; i < resourceTypeMetadata.length; i++)
          resourceTypeMetadata[i] = st.nextToken();
      }      
    }
    return resourceTypeMetadata;
  }

  public String getObjectSelectionWidget()
  {
    if (objectSelectionWidget==null)
    {
      objectSelectionWidget = elem_.getAttribute("objectSelectionWidget");
    }
    return objectSelectionWidget;
  }
  
	
  
  


}

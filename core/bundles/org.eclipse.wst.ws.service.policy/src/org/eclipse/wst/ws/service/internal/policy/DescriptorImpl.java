/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071030   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy;

import org.eclipse.wst.ws.service.policy.IDescriptor;

public class DescriptorImpl implements IDescriptor
{
  private String  shortName;
  private String  longName;
  private String  description;
  private String  iconPath;
  private String  iconBundleId;
  private String  contextHelpId;
  private boolean hasChanged = false;
    
  public boolean hasChanged()
  {
    return hasChanged;
  }
  
  public void resetHasChanged()
  {
    hasChanged = false;  
  }
  
  public String getShortName()
  {
    return shortName;
  }
  
  public void setShortName(String name)
  {
    this.shortName = name;
    
    if( longName == null ) longName = shortName;
    
    hasChanged = true;
  }
  
  public String getLongName()
  {
    return longName;
  }

  public void setLongName(String longName)
  {
    this.longName = longName;
    
    if( shortName == null ) shortName = longName;
    
    hasChanged = true;
  }

  public String getDescription()
  {
    return description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
    hasChanged = true;
  }
  
  public String getIconPath()
  {
    return iconPath;
  }
  
  public void setIconPath(String iconPath)
  {
    this.iconPath = iconPath;
    hasChanged = true;
  }
  
  public String getIconBundleId()
  {
    return iconBundleId;
  }
  
  public void setIconBundleId(String iconBundleId)
  {
    this.iconBundleId = iconBundleId;
    hasChanged = true;
  }
  
  public String getContextHelpId()
  {
    return contextHelpId;
  }

  public void setContextHelpId(String contextHelpId)
  {
    this.contextHelpId = contextHelpId;
    hasChanged = true;
  } 
}

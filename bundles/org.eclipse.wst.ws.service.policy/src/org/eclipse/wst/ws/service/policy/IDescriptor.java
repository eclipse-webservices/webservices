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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

public interface IDescriptor
{
  public String getShortName();
  
  public void setShortName(String name);
  
  public String getLongName();

  public void setLongName(String longName);

  public String getDescription();
  
  public void setDescription(String description);
  
  public String getIconPath();
  
  public void setIconPath(String iconPath);
  
  public String getIconBundleId();
  
  public void setIconBundleId(String iconBundleId);
  
  public String getContextHelpId();

  public void setContextHelpId(String contextHelpId);
}

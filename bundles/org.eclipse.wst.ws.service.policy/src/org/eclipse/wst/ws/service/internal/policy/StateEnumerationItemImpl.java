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
package org.eclipse.wst.ws.service.internal.policy;

import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;

public class StateEnumerationItemImpl implements IStateEnumerationItem
{
  private String  id;
  private String  shortName;
  private String  longName;
  private boolean isDefault = false;
  
  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getShortName()
  {
    return shortName;
  }
  
  public void setShortName(String shortName)
  {
    this.shortName = shortName;
  }
  
  public String getLongName()
  {
    return longName;
  }
  
  public void setLongName(String longName)
  {
    this.longName = longName;
  }

  public boolean isDefault()
  {
    return isDefault;
  }

  public void setDefault(boolean isDefault)
  {
    this.isDefault = isDefault;
  }
}

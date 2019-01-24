/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.engine.data;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class ActionDescriptor
{
  private String id;
  private Hashtable properties;
  private int attempts;
  private String statusId;
  private List statusList;
  
  public ActionDescriptor()
  {
    attempts = 1;
  }

  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }

  public Hashtable getProperties()
  {
    return properties;
  }
  
  public void setProperties(Hashtable properties)
  {
    this.properties = properties;
  }
  
  public int getAttempts()
  {
  	return attempts;
  }
  
  public void setAttempts(int attempts)
  {
  	this.attempts = attempts;
  }
  
  public String getStatusId()
  {
    return statusId;
  }
  
  public void setStatusId(String statusId)
  {
    this.statusId = statusId;
  }
  
  public void addStatus(Object status)
  {
    if (status != null)
    {
      if (statusList == null)
        statusList = new Vector();
      statusList.add(status);
    }
  }
  
  public boolean removeStatus(Object status)
  {
    if (status != null && statusList != null)
      return statusList.remove(status);
    else
      return false;
  }
  
  public List getStatus()
  {
    return statusList;
  }
}

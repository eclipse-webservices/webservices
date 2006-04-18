/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
/**
 */
package org.eclipse.jst.ws.internal.data;

public class TypeRuntimeServer
{
  private String typeId_     = "";
  private String runtimeId_  = "";
  private String serverId_   = "";
  private String serverInstanceId_;
  
  /**
   * @return Returns the runtimeId_.
   */
  public String getRuntimeId()
  {
    return runtimeId_;
  }
  /**
   * @param runtimeId_ The runtimeId_ to set.
   */
  public void setRuntimeId(String runtimeId_)
  {
    this.runtimeId_ = runtimeId_;
  }
  /**
   * @return Returns the serverId_.
   */
  public String getServerId()
  {
    return serverId_;
  }
  /**
   * @param serverId_ The serverId_ to set.
   */
  public void setServerId(String serverId_)
  {
    this.serverId_ = serverId_;
  }
  /**
   * @return Returns the serverInstanceId_.
   */
  public String getServerInstanceId()
  {
    return serverInstanceId_;
  }
  /**
   * @param serverInstanceId_ The serverInstanceId_ to set.
   */
  public void setServerInstanceId(String serverInstanceId_)
  {
    this.serverInstanceId_ = serverInstanceId_;
  }
  /**
   * @return Returns the typeId_.
   */
  public String getTypeId()
  {
    return typeId_;
  }
  /**
   * @param typeId_ The typeId_ to set.
   */
  public void setTypeId(String typeId_)
  {
    this.typeId_ = typeId_;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "type(" + typeId_ + ") runtime(" + runtimeId_ + ") factory(" + serverId_ + ") servInstId(" + serverInstanceId_ + ")";
  }
  
  public boolean equals(Object object)
  {
	if (object != null && object instanceof TypeRuntimeServer)
	{
		TypeRuntimeServer trs = (TypeRuntimeServer)object;
		String trsTypeId = trs.getTypeId();
		String trsRuntimeId = trs.getRuntimeId();
		String trsServerId = trs.getServerId();
		String trsServerInstanceId = trs.getServerInstanceId();
		
		boolean typeIdsMatch = ((typeId_ == null) && (trsTypeId == null)) || ((typeId_ != null) && (typeId_.equals(trsTypeId)));
		if (!typeIdsMatch) return false;
		boolean runtimeIdsMatch = ((runtimeId_ == null) && (trsRuntimeId == null)) || ((runtimeId_ != null) && (runtimeId_.equals(trsRuntimeId)));
		if (!runtimeIdsMatch) return false;
		boolean serverIdsMatch = ((serverId_ == null) && (trsServerId == null)) || ((serverId_ != null) && (serverId_.equals(trsServerId)));
		if (!serverIdsMatch) return false;
		boolean serverInstanceIdsMatch = ((serverInstanceId_ == null) && (trsServerInstanceId == null)) || ((serverInstanceId_ != null) && (serverInstanceId_.equals(trsServerInstanceId)));
		if (!serverInstanceIdsMatch) return false;
		
		return true;		
	}
	
	return false;
  }
}

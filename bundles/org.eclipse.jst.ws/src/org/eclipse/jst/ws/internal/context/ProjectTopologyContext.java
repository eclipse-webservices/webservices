/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.context;

public interface ProjectTopologyContext 
{
  /**
    * This constant String is used to lookup the preferred order of the different client types
  **/
  public static final String PREFERENCE_CLIENT_TYPES = "clientTypes";
  
  /**
    * This constant String is used to lookup the two EAR option
  **/
  public static final String PREFERENCE_USE_TWO_EARS = "useTwoEARs";
  
  public void setClientTypes(String[] ids);
  public String[] getClientTypes();

  public void setUseTwoEARs(boolean use);
  public boolean isUseTwoEARs();

  public ProjectTopologyContext copy();
}

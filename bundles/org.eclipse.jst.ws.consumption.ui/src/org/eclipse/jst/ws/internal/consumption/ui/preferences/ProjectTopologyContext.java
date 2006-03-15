/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.preferences;

public interface ProjectTopologyContext 
{
  /**
    * This constant String is used to lookup the preferred order of the different client types
  **/
  public static final String PREFERENCE_CLIENT_TYPES = "clientTypes";
  public static final String PREFERENCE_SERVICE_TYPES = "serviceTypes";
  
  /**
    * This constant String is used to lookup the two EAR option
  **/
  public static final String PREFERENCE_USE_TWO_EARS = "useTwoEARs";
  
  public void setServiceTypes(String[] ids);
  public String[] getServiceTypes();
  public String[] getDefaultServiceTypes();
  
  public void setClientTypes(String[] ids);
  public String[] getClientTypes();
  public String[] getDefaultClientTypes();

  public void setUseTwoEARs(boolean use);
  public boolean isUseTwoEARs();

  public ProjectTopologyContext copy();
}

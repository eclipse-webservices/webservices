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


public class TransientProjectTopologyContext implements ProjectTopologyContext
{
 private String[] serviceTypes;
 private String[] defaultServiceTypes;
 
 private String[] clientTypes;
 private String[] defaultClientTypes;
 
 private boolean twoEARs;
 
 public TransientProjectTopologyContext () {}

 public void setServiceTypes(String[] serviceTypes)
 {
   this.serviceTypes = serviceTypes;
 }
 public String[] getServiceTypes()
 {
   return serviceTypes;
 }
 
 public void setClientTypes(String[] clientTypes)
 {
   this.clientTypes = clientTypes;
 }
 public String[] getClientTypes()
 {
   return clientTypes;
 }

 public void setUseTwoEARs(boolean use)
 {
   this.twoEARs = use;
 }
 public boolean isUseTwoEARs()
 {
   return twoEARs;
 }

 public ProjectTopologyContext copy() {
 	TransientProjectTopologyContext context = new TransientProjectTopologyContext();
    context.setServiceTypes(getServiceTypes());
    context.setDefaultServiceTypes(getDefaultServiceTypes());
	context.setClientTypes(getClientTypes());
    context.setDefaultClientTypes(getDefaultClientTypes());
	context.setUseTwoEARs(isUseTwoEARs());
	return context;
 }

 
public void setDefaultServiceTypes(String[] defaultServiceTypes)
{
  this.defaultServiceTypes = defaultServiceTypes;
}

public String[] getDefaultServiceTypes()
{
  return defaultServiceTypes;

}


public void setDefaultClientTypes(String[] defaultClientTypes)
{
  this.defaultClientTypes = defaultClientTypes;
}

public String[] getDefaultClientTypes()
{
  return defaultClientTypes;

}
}

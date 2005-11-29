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

package org.eclipse.jst.ws.internal.consumption.ui.preferences;


public class TransientProjectTopologyContext implements ProjectTopologyContext
{
 private String[] clientTypes;
 private boolean twoEARs;
 
 public TransientProjectTopologyContext () {}

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
	context.setClientTypes(getClientTypes());
	context.setUseTwoEARs(isUseTwoEARs());
	return context;
 }
}

/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080221 146023 gilberta@ca.ibm.com - Gilbert Andrews 
 * 20080425   221232 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080616   237298 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080619   237797 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/
/**
 */
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.Vector;

public class LabelsAndIds
{
  private Vector labels_ = new Vector();
  private Vector ids_ = new Vector();
  
  
  public void add(String id, String label){
	  ids_.add(id);
	  labels_.add(label);
  }
  
  public int size(){
	  return labels_.size();
  }
  
  public String getId(int index){
	  if (index < 0 || index + 1 > ids_.size()) return null;
	  return (String)ids_.get(index);
  }
  
  public void clear(){
	  labels_.clear();
	  ids_.clear();
  }
  
  /**
   * @return Returns the ids_.
   */
  public String[] getIds()
  {
	  String[] stringArray = new String[ids_.size()]; 
	  ids_.copyInto(stringArray);
	  return stringArray;
  }
  
  public String[] getLabels()
  {
	  String[] stringArray = new String[ids_.size()]; 
	  labels_.copyInto(stringArray);
	  return stringArray;
  }
  
}

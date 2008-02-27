/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080221 146023 gilberta@ca.ibm.com - Gilbert Andrews 
 *******************************************************************************/
/**
 */
package org.eclipse.wst.command.internal.env.ui.common;

import java.util.Vector;

public class LabelsAndIds
{
  private Vector labels_ = new Vector();
  private Vector ids_ = new Vector();
  
  
  public void add(String id, String label){
	  ids_.add(id);
	  labels_.add(label);
  }
  
  public String getId(int index){
	  return (String)ids_.get(index);
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

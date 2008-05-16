/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

/**
 * 
 * This interface is used to describe a state enumeration item that was
 * defined by an enumeration in plugin.xml meta data.
 *
 */
public interface IStateEnumerationItem
{  
  /**
   * 
   * Returns the ID of this state enumeration item.
   * 
   * @return returns the ID of this state enumeration item.
   */
  public String getId();

  /**
   * Returns the short name of this state enumeration item.
   * 
   * @return returns the short name of this state enumeration item.
   */
  public String getShortName();
    
  /**
   * 
   * Returns the long name of this state enumeration item.
   * 
   * @return returns the long name of this state enumeration item.
   */
  public String getLongName();

  /**
   * 
   * Returns whether this is the default item or not.
   * 
   * @return returns true if this enumeration item is the default for this
   * enumeration.
   */
  public boolean isDefault();
}

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
 * 20080325   222095 pmoogk@ca.ibm.com - Peter Moogk
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

/**
 * 
 * This interface is used to access the state when an enumeration is used
 * to persist state.  
 *
 */
public interface IPolicyStateEnum
{
  public final static String TRUE_ENUM  = "org.eclipse.wst.true";
  public final static String FALSE_ENUM = "org.eclipse.wst.false";
  
  /**
   * 
   * Returns the enumeration id associated with this service policy.
   * 
   * @return returns the enumeration id associated with this service policy.
   */
  public String getEnumId();
  
  /**
   * Returns the default enum item for this enumeration.
   * 
   * @return returns the default enum item for this enumeration.
   */
  public String getDefaultId();
  
  /**
   * Sets the current enum item.  This enum item id needs to be and enum item
   * id associated with this enumeration.
   * @param itemId
   */
  public void setCurrentItem( String itemId );
  
  /**
   * 
   * Returns the current enum item.
   * 
   * @return returns the current enum item.
   */
  public IStateEnumerationItem getCurrentItem(); 
}

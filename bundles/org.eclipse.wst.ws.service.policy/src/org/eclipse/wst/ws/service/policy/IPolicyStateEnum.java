/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
  /**
   * 
   * @return returns the enumeration id associated with this service policy.
   */
  public String getEnumId();
  
  /**
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
   * @return returns the current enum item.
   */
  public IStateEnumerationItem getCurrentItem(); 
}

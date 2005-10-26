/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * This class combines a string with a boolean value.  This is
 * useful for storing the state data for a boolean selection list.
 */
package org.eclipse.wst.command.internal.env.core.selection;

public class BooleanSelection
{
  private String  value_;
  private boolean selected_;
  
  public BooleanSelection( String value, boolean selected )
  {
    value_    = value;
    selected_ = selected;
  }
  
  /**
   * 
   * @return Get the string value.
   */
  public String getValue()
  {
    return value_;
  }
  
  /**
   * 
   * @return Get the boolean value for this string.
   */
  public boolean isSelected()
  {
    return selected_;
  }
}

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
 */
package org.eclipse.wst.command.env.core.selection;

public class BooleanSelection
{
  private String  value_;
  private boolean selected_;
  
  public BooleanSelection( String value, boolean selected )
  {
    value_    = value;
    selected_ = selected;
  }
  
  public String getValue()
  {
    return value_;
  }
  
  public boolean isSelected()
  {
    return selected_;
  }
}

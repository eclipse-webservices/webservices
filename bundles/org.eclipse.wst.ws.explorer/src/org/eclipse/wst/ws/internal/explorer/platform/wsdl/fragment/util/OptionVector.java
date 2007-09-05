/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util;

import java.util.Vector;

public class OptionVector
{
  private Vector options_;
  
  public OptionVector()
  {
    options_ = new Vector();
  }
  
  public void addOption(String displayValue,String value)
  {
    options_.addElement(new Option(displayValue,value));
  }
  
  public String getDisplayValue(int position)
  {
    Option option = (Option)options_.elementAt(position);
    return option.getDisplayValue();
  }
  
  public String getValue(int position)
  {
    Option option = (Option)options_.elementAt(position);
    return option.getValue();
  }
  
  public int size()
  {
    return options_.size();
  }
  
  private final class Option
  {
    private String displayValue_;
    private String value_;
    public Option(String displayValue,String value)
    {
      displayValue_ = displayValue;
      value_ = value;
    }
    
    public final String getDisplayValue()
    {
      return displayValue_;
    }
    
    public final String getValue()
    {
      return value_;
    }
  }
}

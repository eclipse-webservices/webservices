/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.Enumeration;
import java.util.Hashtable;

public class FormToolProperties implements FormToolPropertiesInterface
{
  private Hashtable propertyTable_;
  private Hashtable errantValues_;

  public FormToolProperties()
  {
    propertyTable_ = new Hashtable();
    errantValues_ = new Hashtable();
  }

  public final Object getProperty(Object key)
  {
    return propertyTable_.get(key);
  }

  public final void setProperty(Object key,Object value)
  {
    propertyTable_.put(key,value);
  }

  public final void removeProperty(Object key)
  {
    propertyTable_.remove(key);
  }

  public final void clearPropertyTable()
  {
    propertyTable_.clear();
  }

  public final void updatePropertyTable(Hashtable newPropertyTable)
  {
    Enumeration e = newPropertyTable.keys();
    while (e.hasMoreElements())
    {
      String key = (String)e.nextElement();
      propertyTable_.put(key,newPropertyTable.get(key));
    }
  }

  public final void setPropertyTable(Hashtable newPropertyTable)
  {
    clearPropertyTable();
    updatePropertyTable(newPropertyTable);
  }

  public final void flagError(Object inputKey)
  {
    if (isInputValid(inputKey))
      errantValues_.put(inputKey,Boolean.TRUE);
  }

  public final void flagRowError(Object inputKey,int rowNumber)
  {
    flagRowError(inputKey,String.valueOf(rowNumber));
  }
  
  public final void flagRowError(Object inputKey,Object rowId)
  {
    Object errorObject = errantValues_.get(inputKey);
    Hashtable rowHash;
    if (errorObject instanceof Hashtable)
      rowHash = (Hashtable)errorObject;
    else
      rowHash = new Hashtable();
    rowHash.put(rowId,Boolean.TRUE);
    errantValues_.put(inputKey,rowHash);
  }
  
  public final void clearErrors()
  {
    errantValues_.clear();
  }

  public final boolean isInputValid(Object inputKey)
  {
    return (errantValues_.get(inputKey) == null);
  }

  public final boolean isRowInputValid(Object inputKey,int rowNumber)
  {
    return isRowInputValid(inputKey,String.valueOf(rowNumber));
  }
  
  public final boolean isRowInputValid(Object inputKey,Object rowId)
  {
    Hashtable rowHash = (Hashtable)errantValues_.get(inputKey);
    if (rowHash == null)
      return true;
    return (rowHash.get(rowId) == null);
  }
}

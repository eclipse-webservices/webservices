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

import java.util.*;

public abstract interface FormToolPropertiesInterface
{
  // Getter and Setter for a property.
  public abstract Object getProperty(Object key);
  public abstract void setProperty(Object key,Object value);
  public abstract void removeProperty(Object key);

  // Property table operators.
  public abstract void clearPropertyTable();
  public abstract void updatePropertyTable(Hashtable propertyTable);
  public abstract void setPropertyTable(Hashtable propertyTable);

  // Error handling.
  public abstract void flagError(Object inputKey);
  public abstract void flagRowError(Object inputKey,int rowNumber);
  public abstract void flagRowError(Object inputKey,Object rowId);
  public abstract void clearErrors();
  public abstract boolean isInputValid(Object inputKey);
  public abstract boolean isRowInputValid(Object inputKey,int rowNumber);
  public abstract boolean isRowInputValid(Object inputKey,Object rowId);
}

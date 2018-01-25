/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ToolTypes;

public abstract class FormTool extends Tool implements FormToolPropertiesInterface
{
  protected FormToolProperties formToolProperties_;

  public FormTool(ToolManager toolManager,String enabledImageLink,String highlightedImageLink,String alt)
  {
    super(toolManager,enabledImageLink,highlightedImageLink,alt,ToolTypes.FORM);
    formToolProperties_ = new FormToolProperties();
    initDefaultProperties();
  }

  protected void initDefaultProperties()
  {
  }

  public final Object getProperty(Object key)
  {
    return formToolProperties_.getProperty(key);
  }

  public final void setProperty(Object key,Object value)
  {
    formToolProperties_.setProperty(key,value);
  }

  public final void removeProperty(Object key)
  {
    formToolProperties_.removeProperty(key);
  }

  public final void clearPropertyTable()
  {
    formToolProperties_.clearPropertyTable();
  }

  public final void updatePropertyTable(Hashtable newPropertyTable)
  {
    formToolProperties_.updatePropertyTable(newPropertyTable);
  }

  public final void setPropertyTable(Hashtable newPropertyTable)
  {
    formToolProperties_.setPropertyTable(newPropertyTable);
  }

  public final void flagError(Object inputKey)
  {
    formToolProperties_.flagError(inputKey);
  }

  public final void flagRowError(Object inputKey,int rowNumber)
  {
    formToolProperties_.flagRowError(inputKey,rowNumber);
  }
  
  public final void flagRowError(Object inputKey,Object rowId)
  {
    formToolProperties_.flagRowError(inputKey,rowId);
  }

  public final void clearErrors()
  {
    formToolProperties_.clearErrors();
  }

  public final boolean isInputValid(Object inputKey)
  {
    return formToolProperties_.isInputValid(inputKey);
  }

  public final boolean isRowInputValid(Object inputKey,int rowNumber)
  {
    return formToolProperties_.isRowInputValid(inputKey,String.valueOf(rowNumber));
  }
  
  // The row number may also be represented by a unique object (e.g. UUID)
  public final boolean isRowInputValid(Object inputKey,Object rowId)
  {
    return formToolProperties_.isRowInputValid(inputKey,rowId);
  }

  public final String getActionLink()
  {
    return null;
  }
}

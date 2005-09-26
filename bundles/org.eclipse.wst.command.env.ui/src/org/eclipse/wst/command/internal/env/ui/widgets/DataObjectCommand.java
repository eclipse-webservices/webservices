/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;


/**
 * This Command can be use to get the resulting data object for
 * this wizard.
 *
 */
public class DataObjectCommand extends EnvironmentalOperation
{
  private Object dataObject_;
  
  public void setDataObject( Object object )
  {
    dataObject_ = object;
  }
  
  public Object getDataObject()
  {
    return dataObject_;
  }
}

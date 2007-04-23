/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.engine.data;

public class TransactionDescriptor
{
  private ActionDescriptor[] actionDescriptors;

  public ActionDescriptor[] getActionDescriptors()
  {
    return actionDescriptors;
  }
  
  public void setActionDescriptors(ActionDescriptor[] actionDescriptors)
  {
    this.actionDescriptors = actionDescriptors;
  }
}

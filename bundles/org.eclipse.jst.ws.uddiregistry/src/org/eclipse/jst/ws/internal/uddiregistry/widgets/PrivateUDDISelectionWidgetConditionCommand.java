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
package org.eclipse.jst.ws.internal.uddiregistry.widgets;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Condition;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


public class PrivateUDDISelectionWidgetConditionCommand extends SimpleCommand implements Condition
{
  private boolean condition;

  public PrivateUDDISelectionWidgetConditionCommand()
  {
    condition = true;
  }
  
  public Status execute(Environment env)
  {
    condition = !condition;
    return new SimpleStatus("");
  }
  
  public Status undo(Environment env)
  {
    condition = !condition;
    return new SimpleStatus("");
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.Command#isUndoable()
   */
  public boolean isUndoable()
  {
    return true;
  }

  public boolean evaluate()
  {
    return condition;
  }
}
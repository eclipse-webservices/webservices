/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsil;

import org.eclipse.jst.ws.internal.consumption.wsil.AddWSDLToWSILCommand;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;



public class AddWSDLToWSILWrapperCommand extends SimpleCommand
{
  private AddWSDLToWSILCommand command;
  private Arguments args;

  public AddWSDLToWSILWrapperCommand()
  {
    super("org.eclipse.jst.ws.internal.consumption.ui.wsil.AddWSDLToWSILWrapperCommand", "org.eclipse.jst.ws.internal.consumption.ui.wsil.AddWSDLToWSILWrapperCommand");
  }

  public Status execute(Environment env)
  {
    if (command == null)
      command = new AddWSDLToWSILCommand();
    if (!args.isEmpty())
    {
      command.setArguments(args.getStringArguments());
      command.setWWWAuthenticationHandler(new DialogWWWAuthentication(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()));
      return command.execute(env);
    }
    return new SimpleStatus("");
  }
  /**
   * @param args The args to set.
   */
  public void setArgs(Arguments args)
  {
    this.args = args;
  }

  /**
   * @param command The command to set.
   */
  public void setCommand(AddWSDLToWSILCommand command)
  {
    this.command = command;
  }

}

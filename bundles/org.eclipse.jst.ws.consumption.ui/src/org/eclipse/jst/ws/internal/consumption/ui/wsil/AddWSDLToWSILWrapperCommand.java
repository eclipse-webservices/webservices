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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsil.AddWSDLToWSILCommand;



public class AddWSDLToWSILWrapperCommand extends AbstractDataModelOperation
{
  private AddWSDLToWSILCommand command;
  private Arguments args;

  public AddWSDLToWSILWrapperCommand()
  {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    
    if (command == null)
      command = new AddWSDLToWSILCommand();
    if (!args.isEmpty())
    {
      command.setArguments(args.getStringArguments());
      command.setWWWAuthenticationHandler(new DialogWWWAuthentication(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()));
      command.setEnvironment( env );
      return command.execute( null, null);
    }
    return Status.OK_STATUS;
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

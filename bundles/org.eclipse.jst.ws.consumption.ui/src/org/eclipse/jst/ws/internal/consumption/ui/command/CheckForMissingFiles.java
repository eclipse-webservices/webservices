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
package org.eclipse.jst.ws.internal.consumption.ui.command;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.common.dependencychecker.CheckMissingFiles;

public class CheckForMissingFiles extends SimpleCommand
{

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.Command#execute(org.eclipse.wst.command.env.core.common.Environment)
   */
  public Status execute(Environment environment) 
  {
    Status  status       = new SimpleStatus( "" );
    String  pluginId     = "org.eclipse.jst.ws.consumption.ui";
    boolean missingFiles = CheckMissingFiles.hasMissingFiles( pluginId );
    
    if( missingFiles )
    {
      MessageUtils msgUtils = new MessageUtils(pluginId + ".plugin", this);  
      status = new SimpleStatus( "", msgUtils.getMessage( "MSG_MISSING_THIRD_PARTY_FILES" ), Status.ERROR );
      environment.getStatusHandler().reportError( status );
    }
    
    return status;
  }
}

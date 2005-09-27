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

package org.eclipse.jst.ws.internal.consumption.common;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;

/**
 *  This command is intended to clean up data after either the
 *  client or service wizards.  Currently is just resets the
 *  parser factory. 
 *
 */
public class ScenarioCleanupCommand extends EnvironmentalOperation
{

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  WSDLParserFactory.killParser();
	
	  return Status.OK_STATUS;
  }

  
  public IStatus undo( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  // Even if the user cancels out of the wizard we want to
	  // kill the parser.
	  WSDLParserFactory.killParser();
    
    return Status.OK_STATUS;
  }
}

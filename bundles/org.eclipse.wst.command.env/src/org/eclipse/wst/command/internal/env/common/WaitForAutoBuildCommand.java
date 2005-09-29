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
package org.eclipse.wst.command.internal.env.common;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;


public class WaitForAutoBuildCommand extends EnvironmentalOperation
{
  public IStatus execute( IProgressMonitor montitor, IAdaptable adaptable ) 
  {
  	IStatus status = Status.OK_STATUS;
  	
	try 
	{
	  Platform.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);
	} 
	catch( InterruptedException exc ) 
	{
	  // UISynchronizer.syncExec seems to interrupt the UI tread when the autobuilder is done.  Not sure, why.
	  // I'm assuming here that the autobuilder has actually completed its stuff.	
	}
		
	return status;
  }
}

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
package org.eclipse.wst.command.env.common;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


public class WaitForAutoBuildCommand extends SimpleCommand
{
  public Status execute(Environment environment) 
  {
  	SimpleStatus status = new SimpleStatus( "" );
  	
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

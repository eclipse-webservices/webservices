/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.ws.internal.WstWSPluginMessages;


public class WSIComplianceUtils
{

/**
 * @param project
 * @param context
 * @return
 */
public static int getWSISeverity (IProject project, PersistentWSIContext context)
{
	if (context.projectStopNonWSICompliances(project))
		return Status.ERROR;
	else if (context.projectWarnNonWSICompliances(project))
		return Status.WARNING;
	else
		return Status.INFO;
}

/**
 * @param monitor IStatusHandler
 * @param status Status[]
 * @param project IProject
 * @param context PersistentWSIContext
 * @return boolean true if to continue
 */
public static boolean checkWSICompliance ( IStatusHandler monitor, Status[] status, IProject project, PersistentWSIContext context)
{
  	if (context.projectStopNonWSICompliances(project))
  		{
      
  			// emit an error message and return false
  			IStatus status_ = StatusUtils.multiStatus( context.getError(), status );
			monitor.reportError(status_);
  			return false;
  		}
  	else if (context.projectWarnNonWSICompliances(project))
  		{
  			// give a warning message with the options to stop, ignore this one, or ignore all coming messages
  			IStatus status_ = StatusUtils.multiStatus( context.getWarning(), status);

  			Choice ignoreChoice = new Choice('I', WstWSPluginMessages.IGNORE_LABEL, WstWSPluginMessages.IGNORE_DESCRIPTION);
  			Choice ignoreAllChoice = new Choice('A', WstWSPluginMessages.IGNORE_ALL_LABEL, WstWSPluginMessages.IGNORE_ALL_DESCRIPTION);
  			Choice cancelChoice = new Choice('C', WstWSPluginMessages.CANCEL_LABEL, WstWSPluginMessages.CANCEL_DESCRIPTION);
  			Choice result = monitor.report(status_, new Choice[]{ignoreChoice, ignoreAllChoice, cancelChoice});
  			
  			// if the user closes the message box or selects ignore continue
  			if (result == null || (result.getLabel().equals(ignoreChoice.getLabel())))
        			return true;
  			// if the user selects ignore all, change the preference
	        else  if (result.getLabel().equals(ignoreAllChoice.getLabel()))
	        	{
	        		context.updateProjectWSICompliances(project, PersistentWSIContext.IGNORE_NON_WSI);
	        		return true;
	        	}
			// if the user selects to cancel , do not continue with the command
        	else if (result.getLabel().equals(cancelChoice.getLabel()))
						return false;
  		}
  return true;
  }

}

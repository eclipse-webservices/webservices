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

package org.eclipse.jst.ws.internal.axis.creation.ui.task;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CheckAxisDeploymentDescriptorsTask;

public class TDCheckAxisDeploymentDescriptors extends CheckAxisDeploymentDescriptorsTask 
{
  public TDCheckAxisDeploymentDescriptors() 
  {
  }
  
  /*
	 * The execute method of this command do nothing.  It is merely an anchor point for UI page
	 * TODO Remove this class and use another command as an anchor point for the UI
	 */
  public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable) {
		return Status.OK_STATUS;
	}
}

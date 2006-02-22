/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class AntDefaultingOperation extends AbstractDataModelOperation{

	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// TODO Auto-generated method stub
		return Status.OK_STATUS;
	}
	
	public boolean getServiceIdsFixed()
	{
		return true;
	}

	public boolean getClientIdsFixed()
	{
		return true;
	}
	
	public boolean getStartService()
	{
		return false;	
	}
	
	public boolean getInstallService()
	{
		return false;
	}
    
    public boolean getDeployService()
    {
      return true;
    }
    
    public boolean getDeployClient()
    {
      return true;
    }
}

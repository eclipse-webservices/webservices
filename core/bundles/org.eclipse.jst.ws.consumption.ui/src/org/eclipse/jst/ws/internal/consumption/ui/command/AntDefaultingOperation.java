/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20061011   159283 makandre@ca.ibm.com - Andrew Mak, project not associated to EAR when using ant on command-line
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.internal.emf.resource.RendererFactory;

public class AntDefaultingOperation extends AbstractDataModelOperation{

	private boolean rendererValidation;
	
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// TODO Auto-generated method stub

		rendererValidation = RendererFactory.getDefaultRendererFactory().isValidating();		
		RendererFactory.getDefaultRendererFactory().setValidating(false);
		
		return Status.OK_STATUS;
	}
	
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		RendererFactory.getDefaultRendererFactory().setValidating(rendererValidation);
		
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
    
    public boolean getRendererValidation()
    {
    	return rendererValidation;
    }
}

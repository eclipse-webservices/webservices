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

public class AntRestoringCommand extends AbstractDataModelOperation{

	private boolean rendererValidation;
	
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		RendererFactory.getDefaultRendererFactory().setValidating(rendererValidation);

		return Status.OK_STATUS;
	}
	
    public void setRendererValidation(boolean rendererValidation)
    {
    	this.rendererValidation = rendererValidation;
    }
}

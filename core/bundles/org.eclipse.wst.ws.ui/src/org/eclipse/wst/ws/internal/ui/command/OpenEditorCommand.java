/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
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
 * 20060810   135395 makandre@ca.ibm.com - Andrew Mak, Enable WTP Web service framework opening Java editor
 * 20061025   162288 makandre@ca.ibm.com - Andrew Mak, workspace paths with spaces break Java Editor Launch
 * 20080411   226767 makandre@ca.ibm.com - Andrew Mak, UniversalPathTransformer does not handle paths w/ spaces that are encoded
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;
import org.eclipse.wst.ws.internal.util.UniversalPathTransformer;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

/**
 * A command which, when given an array of implementation URLs, will
 * open them within Eclipse using the editor registered with the
 * file extensions.
 */
public class OpenEditorCommand extends AbstractDataModelOperation {

	private IWebService webService;
	private IContext	context;
 
	/**
	 * Setter for the IWebService
	 * 
	 * @param webService The IWebService
	 */
	public void setWebService(IWebService webService) {
		this.webService = webService;  
	}
	  
	/**
	 * Setter for the IContext
	 * 
	 * @param context The IContext
	 */
	public void setContext (IContext context) {
		this.context = context;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
	
		if (context.getScenario().getValue() != WebServiceScenario.TOPDOWN)
			return Status.OK_STATUS;
		
		String[] implURLs = webService.getWebServiceInfo().getImplURLs();
			
		// nothing to open
		if (implURLs == null)
			return Status.OK_STATUS;
		
		IFile file;
		
		for (int i = 0; i < implURLs.length; i++) {
			try {    						
				String implURL = implURLs[i];
				file = UniversalPathTransformer.toFile(implURL);
						
				if (file == null || !file.exists())
					continue;
				
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				
				if (page == null)
					continue;
								
				IDE.openEditor(page, file, true);
			}
			catch (Exception e) {
				//TODO: for WTP 2.0, return proper status (with NLS)
				WSUIPlugin.getInstance().getLog().log(
						new Status(IStatus.ERROR, WSUIPlugin.ID, 0, e.getMessage(), e));
			}			
		}
		
		return Status.OK_STATUS;
	}	
}

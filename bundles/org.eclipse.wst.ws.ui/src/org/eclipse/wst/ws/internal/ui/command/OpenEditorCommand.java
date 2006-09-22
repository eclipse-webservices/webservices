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
 * 20060810   135395 makandre@ca.ibm.com - Andrew Mak, Enable WTP Web service framework opening Java editor
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui.command;

import java.net.URI;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

/**
 * A command which, when given an array of implementation URLs, will
 * open them within Eclipse using the editor registered with the
 * file extensions.
 */
public class OpenEditorCommand extends AbstractDataModelOperation {

	private final static String PLATFORM_RESOURCE = "platform:/resource/";
	
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
		
		IFile file = null;
		
		for (int i = 0; i < implURLs.length; i++) {
			try {    						
				URI uri    = new URI(implURLs[i]); 				
				IPath path = URIUtil.toPath(uri);
				
				// local filesystem path
				if (path != null)
					file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
				
				// platform path
				else if (implURLs[i].startsWith(PLATFORM_RESOURCE)) {
					path = new Path(uri.getPath()).removeFirstSegments(1);					
					file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				}
						
				if (file == null)
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

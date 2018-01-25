/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.integration.navigator.actions;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.jaxws.dom.integration.internal.plugin.DomIntegrationMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

public class OpenWSResourceAction extends SelectionListenerAction implements IActionDelegate2 
{
	protected Object srcObject;

	private IJavaProject project;
	
	private IServiceEndpointInterface sei;
	private DomUtil util = DomUtil.INSTANCE;

	protected OpenWSResourceAction() {
		this(DomIntegrationMessages.OpenWSResourceAction_Name);
	}

	protected OpenWSResourceAction(String text) {
		super(text);
	}

	@Override
	public boolean isEnabled() {
		boolean isEnabled = super.isEnabled()
		&& this.project!=null;
		
		if(srcObject instanceof IWebService)
		{
			return isEnabled;
		}
		
		if(srcObject instanceof IServiceEndpointInterface)
		{
			return isEnabled;
		}
		
		if(srcObject instanceof IWebMethod)
		{
			return isEnabled;
		}
		
		if(srcObject instanceof IWebParam)
		{
			return isEnabled;
		}		
		
		return false;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		run();
	}

	/**
	 * The user has invoked this action
	 */
	public void run() {
		if (!isEnabled())
			return;

		ICompilationUnit cu;
		IWebMethod webMethod = null;
		IWebParam webParam = null;
		
		if(srcObject instanceof IWebMethod)
		{
			webMethod = (IWebMethod)srcObject;
			srcObject = sei;
		}
		if(srcObject instanceof IWebParam)
		{
			webParam = (IWebParam)srcObject;
			webMethod = (IWebMethod)webParam.eContainer();
			srcObject = sei;
		}
		
		try {

			cu = project.findType(((IJavaWebServiceElement) srcObject).getImplementation()).getCompilationUnit();

			IWorkbenchPage page = getWorkbench().getActiveWorkbenchWindow().getActivePage();

			IEditorDescriptor desc = getWorkbench().getEditorRegistry().getDefaultEditor(cu.getResource().getName());
		
			cu = getCUToOpen(cu, webMethod);		
			if (cu == null)
			{
				logger().logError("Unable to locate containing CU to open"); //$NON-NLS-1$
				return;
			}
		
			IFile file = getWorkspace().getRoot().findFilesForLocation(cu.getResource().getLocation())[0]; 
			IEditorPart editor = page.openEditor(new FileEditorInput(file), desc.getId());
			
			IMarker marker = file.createMarker(IMarker.TEXT);
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put(IMarker.CHAR_START, getMarkerPosition(cu, webMethod, false));
			map.put(IMarker.CHAR_END, getMarkerPosition(cu, webMethod, true));
			
			marker.setAttributes(map);
			
			IDE.gotoMarker(editor, marker);
		} catch (PartInitException pie) {
			logger().logError("Unable to open part editor", pie); //$NON-NLS-1$
		} catch(CoreException ce) {
			logger().logError(ce.getMessage(), ce); 
		}
	}

	private int getMarkerPosition(ICompilationUnit cu, IWebMethod webMethod, boolean endPos) throws JavaModelException
	{
		final IType[] types = cu.getTypes();
		for (int ii = 0; ii < types.length; ii++)
		{
			if (webMethod == null)
			{
				return getPosition(types[ii], endPos);
			}

			final IMethod method = util.findMethod(types[ii], webMethod);
			if (method==null) {
				continue;
			}
			
			return getPosition(method, endPos);
		}
				
		return 0;
	}
	
	private int getPosition(IMember member, boolean endPos) throws JavaModelException 
	{
		return member.getNameRange().getOffset() + ((endPos) ? member.getNameRange().getLength() : 0);
	}
	
	private ICompilationUnit getCUToOpen(ICompilationUnit cu, IWebMethod webMethod)
	{
		if (webMethod == null) 
		{
			return cu;
		}
		
		try
		{
			final IType[] types = cu.getTypes();			
			for(int ii=0; ii<types.length; ii++)
			{
				IMethod method = util.findMethod(types[ii], webMethod);
				if (method != null)
				{
					return types[ii].getCompilationUnit();
				}
			}
			
			for(int ii=0; ii<types.length; ii++)
			{
				IType[] superIntfTypes = types[ii].newSupertypeHierarchy(null).getSuperInterfaces(types[ii]);
				
				for(int jj=0; jj<superIntfTypes.length; jj++)
				{
					ICompilationUnit cuToOpen = getCUToOpen(superIntfTypes[jj].getCompilationUnit(), webMethod);
					
					if(cuToOpen!=null)
					{
						return cuToOpen;
					}
				}
				
				IType superClassType = types[ii].newSupertypeHierarchy(null).getSuperclass(types[ii]);
				
				ICompilationUnit cuToOpen = getCUToOpen(superClassType.getCompilationUnit(), webMethod);
					
				if(cuToOpen!=null)
				{
					return cuToOpen;
				}
			}
		}
		catch(JavaModelException jme)
		{
			logger().logError(jme.getMessage(), jme);
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		updateSelection((IStructuredSelection) selection);
	}

	public void selectionChanged(IStructuredSelection selection, IJavaProject project, IServiceEndpointInterface sei) {
		super.selectionChanged(selection);

		this.project = project;
		this.sei = sei;
	}

	/**
	 * The structured selection has changed in the workbench. Subclasses should
	 * override this method to react to the change. Returns true if the action
	 * should be enabled for this selection, and false otherwise.
	 * 
	 * When this method is overridden, the super method must always be invoked.
	 * If the super method returns false, this method must also return false.
	 * 
	 * @param sel
	 *            the new structured selection
	 */
	public boolean updateSelection(IStructuredSelection s) {
		if (!super.updateSelection(s))
			return false;

		srcObject = s.getFirstElement();

		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate2#dispose()
	 */
	public void dispose() {
		// Dispose
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate2#init(org.eclipse.jface.action.IAction)
	 */
	public void init(IAction action) {
		// init
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate2#runWithEvent(org.eclipse.jface.action.IAction,
	 *      org.eclipse.swt.widgets.Event)
	 */
	public void runWithEvent(IAction action, Event event) {
		runWithEvent(event);

	}
	
	private ILogger logger() {
		return new Logger();
	}

	private IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	private IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
}

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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jst.javaee.ejb.MessageDrivenBean;
import org.eclipse.jst.javaee.ejb.SessionBean;
import org.eclipse.jst.jee.ui.internal.navigator.ejb.GroupEJBProvider;
import org.eclipse.jst.jee.ui.internal.navigator.ejb.GroupEjbSession;
import org.eclipse.jst.ws.jaxws.dom.integration.internal.plugin.DomIntegrationMessages;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ISEIChildList;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMRuntimeManager;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.Jee5WsDomRuntimeExtension;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;
import org.eclipse.jst.ws.jaxws.utils.resources.ProjectManagementUtils;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.actions.SelectionListenerAction;

public class NavigateToImplementationAction extends SelectionListenerAction
		implements IActionDelegate2 {
	protected Object srcObject;

	private IJavaProject project;

	private StructuredViewer viewer;

	protected NavigateToImplementationAction(StructuredViewer viewer) {
		this(DomIntegrationMessages.OpenWSResourceAction_Name);

		this.viewer = viewer;
	}

	protected NavigateToImplementationAction(String text) {
		super(text);
	}

	@Override
	public String getText()
	{
		if(srcObject instanceof SessionBean)
		{
			return DomIntegrationMessages.NavigateToImplementationAction_ShowInWebServicesAreaAction;
		}
		else
		{
			return DomIntegrationMessages.NavigateToImplementationAction_ShowInEJBAreaAction;
		}
	}
	
	@Override
	public boolean isEnabled() {
		boolean isEnabled = super.isEnabled() 
		&& this.viewer instanceof TreeViewer
		&& this.project !=null
		&& ProjectManagementUtils.isEjb3Project(this.project.getProject().getName());
		
		if(srcObject instanceof IWebService)
		{
			return isEnabled;
		}
		
		if(srcObject instanceof SessionBean)
		{
			SessionBean sessionBean = ((SessionBean)srcObject);
			IWebService webService = getWebService(sessionBean);
			if (webService == null)
			{
				return false;
			}
			return isEnabled;
		}
		
		if(srcObject instanceof IServiceEndpointInterface
				&& ((IServiceEndpointInterface)srcObject).isImplicit())
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
		// TODO Auto-generated method stub
		run();

	}

	/**
	 * The user has invoked this action
	 */
	public void run() {
		if (!isEnabled())
			return;

		ICompilationUnit cu = null;

		if(srcObject instanceof SessionBean)
		{
			SessionBean sessionBean = ((SessionBean)srcObject);
			String ejbClass = sessionBean.getEjbClass();
			IWebService webService = getWebService(sessionBean);
			if (webService == null)
			{
				return;
			}

			IServiceEndpointInterface sEI = webService.getServiceEndpoint();
			boolean isImplicit = sEI.isImplicit();
			String sEIClass;
			if (!isImplicit){
				String impl = sEI.getImplementation();
				sEIClass = impl;
			} else {
				sEIClass = ejbClass;
			}
            try {
				cu = project.findType(sEIClass).getCompilationUnit();
				IResource resource = cu.getResource();
				moveToSEINode(resource);
			} catch (JavaModelException jme) {
				logger().logError("Unexpected exception occurred", jme); //$NON-NLS-1$
				return;
			}
            
		}
		else
		{
			try {
				cu = project.findType(
						((IJavaWebServiceElement) srcObject)
								.getImplementation()).getCompilationUnit();
			} catch (JavaModelException jme) {
				logger().logError("Unexpected exception occurred", jme); //$NON-NLS-1$
				return;
			}
			
			moveToEjbNode(cu.getResource());
		}
	}

	/**
	 * Returns IWebService if sessionBean has been exposed as web service.
	 * @param sessionBean a session bean.
	 * @return IWebService if sessionBean has been exposed as web service. If the session bean has not been
	 * exposed as web service - returns null.
	 * @throws NullPointerException if sessionBean is null.
	 */
	protected IWebService getWebService(final SessionBean sessionBean) 
	{
		final IWsDOMRuntimeExtension domRuntime = getDomRuntime();
		if (domRuntime == null) {
		      return null;
		}

		try {
			final IDOM dom = domRuntime.getDOM();
			if (dom == null) {
				return null;
			}

			final IWebServiceProject webServiceProject = DomUtil.INSTANCE.findProjectByName(dom, this.project.getProject().getName());		
			return DomUtil.INSTANCE.findWsByImplName(webServiceProject, sessionBean.getEjbClass());
		} 
		catch (WsDOMLoadCanceledException e) { // $JL-EXC$
			return null;
		}
	}

	protected IWsDOMRuntimeExtension getDomRuntime() {
		return WsDOMRuntimeManager.instance().getDOMRuntime(Jee5WsDomRuntimeExtension.ID);
	}

	private void moveToSEINode(IResource resource)
	{
		TreeViewer treeViewer = (TreeViewer)this.viewer;
		ISelection sel = null;

		Object[] expandedObjects = treeViewer.getExpandedElements();

		Object[] projectElements = ((ITreeContentProvider) treeViewer
				.getContentProvider()).getChildren(this.project.getProject());
		IWebServiceProject wsProject = null;

		for (int ii = 0; ii < projectElements.length; ii++) {
			if (projectElements[ii] instanceof IWebServiceProject) {
				wsProject = (IWebServiceProject) projectElements[ii];
				treeViewer.expandToLevel(wsProject, 2);
				break;
			}
		}

		if (wsProject == null)
			return;

		for(IServiceEndpointInterface sEI : wsProject.getServiceEndpointInterfaces())
		{
			try {
				ICompilationUnit cu = project.findType(
						((IJavaWebServiceElement) sEI)
								.getImplementation()).getCompilationUnit();
				
				if(cu.getResource().equals(resource))
				{
					sel = new StructuredSelection(sEI);
					TreePath treePath = getTreePathForSEI(treeViewer,
							wsProject);
					Object[] elementsToExpand = new Object[treePath
							.getSegmentCount()];

					for (int jj = 0; jj < elementsToExpand.length; jj++) {
						if (notAlreadyExpanded(treePath.getSegment(jj),
								expandedObjects)) {
							elementsToExpand[jj] = treePath.getSegment(jj);
						}
					}

					for (int jj = 0; jj < elementsToExpand.length; jj++) {
						if (elementsToExpand[jj] != null) {
							Object[] tmp = expandedObjects;
							expandedObjects = new Object[expandedObjects.length + 1];

							for (int kk = 0; kk < tmp.length; kk++) {
								expandedObjects[kk] = tmp[kk];
							}

							expandedObjects[expandedObjects.length - 1] = elementsToExpand[jj];
						}
					}

					break;
				}
			} catch (JavaModelException jme) {
				logger().logError("Unexpected exception occurred", jme); //$NON-NLS-1$
				return;
			}
			
		}
		 setExpandedElementsAndSelection(treeViewer, expandedObjects, sel);
	}
	
	private void moveToEjbNode(IResource resource) {
		TreeViewer treeViewer = (TreeViewer)this.viewer;
		ISelection sel = null;

		Object[] expandedObjects = treeViewer.getExpandedElements();

		Object[] projectElements = ((ITreeContentProvider) treeViewer
				.getContentProvider()).getChildren(this.project.getProject());
		GroupEJBProvider ejbProvider = null;
		GroupEjbSession ejbSession = null;

		for (int ii = 0; ii < projectElements.length; ii++) {
			if (projectElements[ii] instanceof GroupEJBProvider) {
				ejbProvider = (GroupEJBProvider) projectElements[ii];
				treeViewer.expandToLevel(ejbProvider, 1);
				break;
			}
		}

		if (ejbProvider == null)
			return;

		for (Object ejbProviderChild : ejbProvider.getChildren()) {
			if (ejbProviderChild instanceof GroupEjbSession) {
				ejbSession = (GroupEjbSession) ejbProviderChild;
				treeViewer.expandToLevel(ejbSession, 2);
			}
		}

		if (ejbSession == null) {
			return;
		}

		Object[] ejbs = treeViewer.getExpandedElements();

		for (int ii = 0; ii < ejbs.length; ii++) {
			if ((ejbs[ii] instanceof SessionBean)||(ejbs[ii] instanceof MessageDrivenBean)) {
			
				IJavaProject javaProject = findJavaProject(ejbs[ii]);
				IType t = null;
				try {
					if((ejbs[ii] instanceof SessionBean))
						t = javaProject.findType(((SessionBean)ejbs[ii]).getEjbClass());
					else
						t = javaProject.findType(((MessageDrivenBean)ejbs[ii]).getEjbClass());
				} catch (JavaModelException e) {
					return;
				}
				IResource r = t.getResource();
				
				if (r.equals(resource)) {
					sel = new StructuredSelection(ejbs[ii]);
					TreePath treePath = getTreePathForObject(treeViewer,
							ejbs[ii]);
					expandElements(expandedObjects, treePath);

					break;
				}
			}
		}

		 setExpandedElementsAndSelection(treeViewer, expandedObjects, sel);
	}
	
	protected IJavaProject findJavaProject(Object ejb)
	{
		IProject proj = ProjectUtilities.getProject(ejb);
		return JavaCore.create(proj);
	}

	private void setExpandedElementsAndSelection(TreeViewer treeViewer, Object[] expandedObjects, ISelection sel)
	{
		treeViewer.setExpandedElements(expandedObjects);

		if (sel != null) {
			treeViewer.setSelection(sel, true);
		}
	}
	
	private void expandElements(Object[] expandedObjects, TreePath treePath)
	{
		
		Object[] elementsToExpand = new Object[treePath.getSegmentCount()];

		for (int jj = 0; jj < elementsToExpand.length; jj++) 
		{
			if (notAlreadyExpanded(treePath.getSegment(jj), expandedObjects)) 
			{
				elementsToExpand[jj] = treePath.getSegment(jj);
			}
		}

		for (int jj = 0; jj < elementsToExpand.length; jj++) {
		    if (elementsToExpand[jj] != null) {
		    	Object[] tmp = expandedObjects;
		        expandedObjects = new Object[expandedObjects.length + 1];

				for (int kk = 0; kk < tmp.length; kk++) {
					expandedObjects[kk] = tmp[kk];
				}

				expandedObjects[expandedObjects.length - 1] = elementsToExpand[jj];
		    }
		}
	}


	private boolean notAlreadyExpanded(Object obj, Object[] expObjs) {
		for (int ii = 0; ii < expObjs.length; ii++) {
			if (expObjs[ii].equals(obj)) {
				return false;
			}
		}

		return true;
	}

	private TreePath getTreePathForSEI(TreeViewer treeViewer, IWebServiceProject wsProject)
	{
		TreePath[] treePaths = treeViewer.getExpandedTreePaths();

		for (int ii = 0; ii < treePaths.length; ii++) {
			if (treePaths[ii].getLastSegment() instanceof ISEIChildList
					&& contains(treePaths[ii], wsProject)) {
				return treePaths[ii];
			}
		}
		return null;
	}
	
	private boolean contains(TreePath treePath, IWebServiceProject wsProject)
	{
		for(int ii=0; ii<treePath.getSegmentCount(); ii++)
		{
			if(treePath.getSegment(ii).equals(wsProject))
			{
				return true;
			}
		}
		
		return false;
	}
	private TreePath getTreePathForObject(TreeViewer treeViewer, Object obj) {
		TreePath[] treePaths = treeViewer.getExpandedTreePaths();

		for (int ii = 0; ii < treePaths.length; ii++) {
			if (treePaths[ii].getLastSegment().equals(obj)) {
				return treePaths[ii];
			}
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
		// TODO HACK!
		updateSelection((IStructuredSelection) selection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IStructuredSelection selection,
			IJavaProject project) {
		super.selectionChanged(selection);

		this.project = project;
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
	
	private ILogger logger() {
		return new Logger();
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
}

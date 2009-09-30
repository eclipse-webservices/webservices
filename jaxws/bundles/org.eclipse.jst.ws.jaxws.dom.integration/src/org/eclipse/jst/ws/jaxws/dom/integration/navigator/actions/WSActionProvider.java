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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;


public class WSActionProvider extends CommonActionProvider 
{
	private OpenWSResourceAction openAction;

	private NavigateToImplementationAction navigateAction;
	
	@Override
	public void fillActionBars(IActionBars actionBars) 
	{
		if(openAction.isEnabled()) 
			actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, openAction);
		
		if(navigateAction.isEnabled())
			actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), navigateAction);	
	}

	@Override
	public void fillContextMenu(IMenuManager menu) 
	{
		if (getContext()==null || getContext().getSelection().isEmpty())
			return;
		
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();
		openAction.selectionChanged(selection);
		navigateAction.selectionChanged(selection);
		navigateAction.setText(navigateAction.getText());
		
		if (openAction.isEnabled())
			menu.insertAfter(ICommonMenuConstants.GROUP_OPEN, openAction);
		
		if(navigateAction.isEnabled())
			menu.insertAfter(ICommonMenuConstants.GROUP_OPEN_WITH, navigateAction);
	}

	@Override
	public void init(ICommonActionExtensionSite site) {
		super.init(site);
		
		openAction = new OpenWSResourceAction();
		navigateAction = initNavigateAction(site);
	}
	
	protected NavigateToImplementationAction initNavigateAction(ICommonActionExtensionSite site)
	{
		return new NavigateToImplementationAction(site.getStructuredViewer());
	}

	@Override
	public void setContext(ActionContext context) {
		if (context != null && context.getSelection() instanceof IStructuredSelection) 
		{
			IStructuredSelection selection = (IStructuredSelection) context.getSelection();
			
			if(selection instanceof ITreeSelection)
			{
				TreePath[] paths = ((ITreeSelection)selection).getPathsFor(selection.getFirstElement());
				IProject project = null;
				
				for(int ii=0; ii<paths.length; ii++)
				{
					TreePath path = paths[ii];
					IServiceEndpointInterface sei = null;
					
					for(int jj=0; jj<path.getSegmentCount(); jj++)
					{
						if(path.getSegment(jj) instanceof IServiceEndpointInterface)
						{
							sei = (IServiceEndpointInterface)path.getSegment(jj);
							break;
						}
					}
					
					for(int jj=0; jj<path.getSegmentCount(); jj++)
					{
						if(path.getSegment(jj) instanceof IProject)
						{
							project = (IProject)path.getSegment(jj);
							openAction.selectionChanged(selection, getJavaProject(project), sei);
							navigateAction.selectionChanged(selection, getJavaProject(project));
							super.setContext(context);
							return;
						}
					}
				}
			}
			
			openAction.selectionChanged(selection);
			navigateAction.selectionChanged(selection);
		}
		
		super.setContext(context);
	}
	
	protected IJavaProject getJavaProject(IProject project)
	{
		return JavaCore.create(project);
	}
}

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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.ICompilationUnitFinder;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IProjectSelector;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WorkspaceCUFinder;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource.ServiceModelData;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

public class OnEventModelSynchronizer extends AbstractModelSynchronizer implements IElementChangedListener
{

	@Override
	public IDOM getDomBeingLoaded()
	{
		return resource().getDOM();
	}

	public OnEventModelSynchronizer(JaxWsWorkspaceResource resource, ServiceModelData serviceData)
	{
		super(resource, serviceData);
	}

	public void elementChanged(final ElementChangedEvent event)
	{
		try
		{
			if (logger().isDebug())
			{
				logger().logDebug("ElementChangedEvent has been delivered. Event details: " + event.toString());//$NON-NLS-1$
			}
			
			// no processing if DOM load has been canceled
			if (resource().isLoadCnaceled()) {
				return;
			}
			
			resource().disableSaving();
			
			// handles ElementChangedEvent.POST_RECONCILE - the case when the editor sends reconcile event
			if (event.getType() == ElementChangedEvent.POST_RECONCILE)
			{
				handleReconcileEvent(event);
			}

			// handles ElementChangedEvent.POST_CHANGE - the case when the compilation unit has been changed
			if (event.getDelta().getElement() != resource().javaModel())
			{
				return;
			}
			
			assert (event.getDelta().getFlags() & (IJavaElementDelta.F_CHILDREN | IJavaElementDelta.F_CONTENT)) != 0;
			for (IJavaElementDelta child : event.getDelta().getAffectedChildren())
			{
				if (child.getElement() instanceof IJavaProject)
				{
					handleChangedProject(child);
				}
			}
		} 
		catch (CoreException ce) {
			logger().logError("Unable to synchronize Web Service model upon java element delta "+ event.getDelta(), ce); //$NON-NLS-1$
		} 
		catch (WsDOMLoadCanceledException e) {
			logger().logError("Unable to synchronize Web Service model upon java element delta " + event.getDelta(), e); //$NON-NLS-1$
		} 
		finally {
			resource().enableSaving();
		}
	}
	
	protected void handleReconcileEvent(final ElementChangedEvent event) throws JavaModelException
	{
		if(!(event.getDelta().getElement() instanceof ICompilationUnit )) {
			return;
		}
		
		int requiredFlags = IJavaElementDelta.F_CONTENT | IJavaElementDelta.F_CHILDREN;
		
		if ((event.getDelta().getFlags() & requiredFlags) == 0) {
			return;
		}

		final ICompilationUnit cu = (ICompilationUnit)event.getDelta().getElement();
		if (!resource().approveProject(cu.getJavaProject())) {
			return;
		}

		if (isJavaResource(cu)) {
			handleChangedContentCompilationUnit(cu);
		}
	}
	
	private boolean isJavaResource(ICompilationUnit cu) throws JavaModelException 
	{
		IJavaElement javaElement = cu;
		while ((javaElement = javaElement.getParent()) != null) 
		{
			if (!javaElement.exists()) {
				return false;
			}
		}
		
		return true;
	}
	
	void handleChangedProject(IJavaElementDelta projectDelta) throws CoreException, WsDOMLoadCanceledException
	{
		assert (projectDelta.getElement() instanceof IJavaProject);
	
		if ((((projectDelta.getFlags() & IJavaElementDelta.F_CLOSED) != 0) || projectDelta.getKind() == IJavaElementDelta.REMOVED)
			&& null != util().findProjectByName(getDomBeingLoaded(), projectDelta.getElement().getElementName()))
		{
			//if the project has been closed then it's facet's could not be read. Thus the approve method
			//will always return false. That's why we check if the project has already been in the 
			handleClosedProject(projectDelta);
		}
		if (!resource().approveProject((IJavaProject)projectDelta.getElement()))
		{
			return;
		}
		if (((projectDelta.getFlags() & IJavaElementDelta.F_OPENED) != 0) || projectDelta.getKind() == IJavaElementDelta.ADDED)
		{
			handleOpenedProject(projectDelta);
		}
	
		for (IJavaElementDelta childDelta : projectDelta.getAffectedChildren())
		{
			if (!(childDelta.getElement() instanceof IPackageFragmentRoot))
			{
				continue;
			}
			if ((childDelta.getFlags() & IJavaElementDelta.F_ADDED_TO_CLASSPATH) != 0)
			{
				handleClosedProject(projectDelta);
				handleOpenedProject(projectDelta);
			}
			if ((childDelta.getFlags() & IJavaElementDelta.F_REMOVED_FROM_CLASSPATH) != 0)
			{
				handleClosedProject(projectDelta);
				handleOpenedProject(projectDelta);
			}
			if ((childDelta.getFlags() & IJavaElementDelta.F_CHILDREN) != 0)
			{
				recursevilyHandleRemovedPackages(childDelta);
				recursevilyHandleAddedPackages(childDelta);
				recursevilyHandleCompilationUnits(childDelta);
			}
		}
	}
	
	protected void recursevilyHandleAddedPackages(IJavaElementDelta rootDelta) throws JavaModelException
	{
		final IWebServiceProject wsPrj = findProjectByDelta(rootDelta);
		if (wsPrj == null) {
			return;
		}
		
		for (IJavaElementDelta childDelta : rootDelta.getAffectedChildren())
		{
			if (childDelta.getElement() instanceof IPackageFragment && childDelta.getKind() == IJavaElementDelta.ADDED)
			{
				for (ICompilationUnit cu : ((IPackageFragment)childDelta.getElement()).getCompilationUnits())
				{
					processCompilationUnit(wsPrj, cu);
				}
				recursevilyHandleAddedPackages(childDelta);
			}
		}
	}

	protected IWebServiceProject findProjectByDelta(IJavaElementDelta rootDelta) {
		return util().findProjectByName(getDomBeingLoaded(), rootDelta.getElement().getJavaProject().getElementName());
	}
	
	private void recursevilyHandleRemovedPackages(IJavaElementDelta rootDelta) throws JavaModelException
	{
		
		for (IJavaElementDelta childDelta : rootDelta.getAffectedChildren())
		{
			if (childDelta.getElement() instanceof IPackageFragment)
			{
				if (childDelta.getKind() == IJavaElementDelta.REMOVED)
				{
					final IWebServiceProject wsPrj = findProjectByDelta(rootDelta);
					if (wsPrj != null) {
						removeModelElements(wsPrj, childDelta.getElement().getElementName(), true);
					}
				} else
				{
					recursevilyHandleRemovedPackages(childDelta);
				}
			}
		}
	}

	private void handleOpenedProject(IJavaElementDelta projectDelta) throws CoreException, WsDOMLoadCanceledException
	{
		assert projectDelta.getElement() instanceof IJavaProject;
		final IJavaProject changedProject = (IJavaProject) projectDelta.getElement();
		newProjectCUFinder(changedProject).find(null, new LoaderCompilationUnitHandler());
	}

	private void handleClosedProject(IJavaElementDelta projectDelta) throws CoreException
	{
		assert projectDelta.getElement() instanceof IJavaProject;
		final IWebServiceProject wsPrj = util().findProjectByName(getDomBeingLoaded(), projectDelta.getElement().getElementName());
		if (wsPrj==null) {
			return;
		}
		
		for (IServiceEndpointInterface sei : wsPrj.getServiceEndpointInterfaces())
		{
			sei.getImplementingWebServices().clear();
		}
		for (IWebService ws : wsPrj.getWebServices())
		{
			serviceData().unmap(ws);
		}
		getDomBeingLoaded().getWebServiceProjects().remove(wsPrj);
	}

	private ICompilationUnitFinder newProjectCUFinder(final IJavaProject prj)
	{
		return new WorkspaceCUFinder(resource().javaModel(), new IProjectSelector[] { new IProjectSelector()
		{
			public boolean approve(IJavaProject toApprove)
			{
				return prj.getElementName().equals(toApprove.getElementName());
			}
		} });
	}

	// handles compilation units - skips working copies
	private void recursevilyHandleCompilationUnits(IJavaElementDelta delta)
	{
		if (delta.getElement() instanceof ICompilationUnit)
		{
			final ICompilationUnit cu = (ICompilationUnit) delta.getElement();
			if (util().findProjectByName(getDomBeingLoaded(), cu.getJavaProject().getElementName())==null)
			{
				return;
			}
			
			try
			{
				if (delta.getKind() == IJavaElementDelta.ADDED && (delta.getFlags() & IJavaElementDelta.F_MOVED_FROM) == 0)
				{
					handleAddedCompilationUnit(cu);
				}
				if (delta.getKind() == IJavaElementDelta.REMOVED && (delta.getFlags() & IJavaElementDelta.F_MOVED_TO) == 0)
				{
					handleRemovedCompilationUnit(cu);
				}
				if (delta.getKind() == IJavaElementDelta.REMOVED && (delta.getFlags() & IJavaElementDelta.F_MOVED_TO) != 0)
				{
					handleMovedToCompilationUnit(cu, (ICompilationUnit) delta.getMovedToElement());
				}
				if (delta.getKind() == IJavaElementDelta.CHANGED && 
					((delta.getFlags() & IJavaElementDelta.F_PRIMARY_RESOURCE) != 0) ||
					((delta.getFlags() & IJavaElementDelta.F_PRIMARY_WORKING_COPY) != 0))
				{
					handleChangedContentCompilationUnit(cu);
				}
			} catch (JavaModelException jme)
			{
				logger().logError("Unable to parse the content of compilation unit" + delta.getElement(), jme); //$NON-NLS-1$
			}
		} else
		{
			for (IJavaElementDelta childDelta : delta.getAffectedChildren())
			{
				recursevilyHandleCompilationUnits(childDelta);
			}
		}
	}

	private void handleChangedContentCompilationUnit(ICompilationUnit cu) throws JavaModelException
	{
		final IType type = cu.findPrimaryType();	
		final IAnnotation<IType> annotation = type==null ? null : resource().newAnnotationInspector(type).inspectType(WS_ANNOTATION);
		if (type == null || annotation == null)
		{
			updateSubclassingWSElements(cu);
			handleRemovedCompilationUnit(cu);
		} else
		{
			final IWebServiceProject wsProject = util().findProjectByName(getDomBeingLoaded(), cu.getJavaProject().getElementName());
			assert wsProject != null;
			processCompilationUnit(wsProject, cu);
		}
	}

	private void updateSubclassingWSElements(ICompilationUnit cu) throws JavaModelException
	{
		for (IWebServiceProject wsPrj : getDomBeingLoaded().getWebServiceProjects())
		{
			final IJavaProject javaPrj = javaModel().getJavaProject(wsPrj.getName());
			for (IServiceEndpointInterface sei : wsPrj.getServiceEndpointInterfaces())
			{
				if(serviceData().getHierarchy(sei.getImplementation())==null) {
					continue;
				}
				if (serviceData().getHierarchy(sei.getImplementation()).contains(cu))
				{
					processCompilationUnit(wsPrj, javaPrj.findType(sei.getImplementation()).getCompilationUnit());
				}
			}
			for (IWebService ws : wsPrj.getWebServices())
			{
				if(serviceData().getHierarchy(ws.getImplementation())==null) {
					continue;
				}
				if (serviceData().getHierarchy(ws.getImplementation()).contains(cu))
				{
					processCompilationUnit(wsPrj, javaPrj.findType(ws.getImplementation()).getCompilationUnit());
				}
			}
		}
	}

	private void handleAddedCompilationUnit(ICompilationUnit addedCU) throws JavaModelException
	{
		final IWebServiceProject wsPrj = util().findProjectByName(getDomBeingLoaded(), addedCU.getJavaProject().getElementName());
		this.processCompilationUnit(wsPrj, addedCU);
	}

	void handleMovedToCompilationUnit(ICompilationUnit fromCU, ICompilationUnit toCU) throws JavaModelException
	{
		final IJavaWebServiceElement wsElem = guessContainedWSElement(fromCU);
		if (wsElem == null)
		{
			return;
		}

		final boolean fqNameChanged = !toCU.getElementName().equals(fromCU.getElementName())
										|| !toCU.getParent().getElementName().equals(fromCU.getParent().getElementName());
		if (fqNameChanged)
		{
			handleRemovedCompilationUnit(fromCU);
			handleAddedCompilationUnit(toCU);
		} else
		// then assume that the project only has changed.
		{
			final IWebServiceProject toPrj = util().findProjectByName(getDomBeingLoaded(), toCU.getJavaProject().getElementName());
			if (wsElem instanceof IWebService)
			{
				toPrj.getWebServices().add((IWebService) wsElem);
			} else
			{
				toPrj.getServiceEndpointInterfaces().add((IServiceEndpointInterface) wsElem);
			}
		}
	}

	private void handleRemovedCompilationUnit(ICompilationUnit remCU) throws JavaModelException
	{
		final IWebServiceProject wsPrj = util().findProjectByName(getDomBeingLoaded(), remCU.getJavaProject().getElementName());
		removeModelElements(wsPrj, guessPrimaryTypeName(remCU), false);
	}

	private void removeModelElements(IWebServiceProject wsPrj, String implBaseName, boolean isForPackage) throws JavaModelException
	{
		if (wsPrj == null) {
			return;
		}
		removeWsModelElements(wsPrj, implBaseName, isForPackage);
		removeSeiModelElements(wsPrj, implBaseName, isForPackage, false);
	}
	
	private IJavaWebServiceElement guessContainedWSElement(ICompilationUnit cu)
	{
		final IWebServiceProject wsPrj = util().findProjectByName(getDomBeingLoaded(), cu.getJavaProject().getElementName());
		return util().findJavaWebServiceElemByImplName(wsPrj, guessPrimaryTypeName(cu));
	}

	protected String guessPrimaryTypeName(ICompilationUnit cu)
	{
		final String parentName = cu.getParent().getElementName();
		final StringBuilder assumedPrimaryTypeName = new StringBuilder();
		if (parentName.length() > 0) {
			assumedPrimaryTypeName .append(parentName).append('.');
		}
		return assumedPrimaryTypeName.append(cu.getElementName()).
			delete(assumedPrimaryTypeName.lastIndexOf(".java"), assumedPrimaryTypeName.length()).toString();//$NON-NLS-1$
	}
}

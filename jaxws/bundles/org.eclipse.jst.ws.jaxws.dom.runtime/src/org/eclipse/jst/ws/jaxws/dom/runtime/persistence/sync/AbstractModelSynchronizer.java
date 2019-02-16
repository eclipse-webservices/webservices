/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.ICompilationUnitHandler;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource.ServiceModelData;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;

public abstract class AbstractModelSynchronizer implements IModelElementSynchronizer
{
	private final SeiSynchronizer seiLoader;

	private final WsSynchronizer wsLoader;

	private final JaxWsWorkspaceResource resource;

	private IDOM domBeingLoaded;

	private final ServiceModelData serviceData;

	private final DomUtil util;

	public AbstractModelSynchronizer(JaxWsWorkspaceResource resource, ServiceModelData serviceData)
	{
		this.resource = resource;
		this.util = new DomUtil();
		this.serviceData = serviceData;
		this.seiLoader = new SeiSynchronizer(this);
		this.wsLoader = new WsSynchronizer(this);
	}

	public DomFactory domFactory()
	{
		return DomFactory.eINSTANCE;
	}

	public IDOM getDomBeingLoaded()
	{
		if (domBeingLoaded == null)
		{
			domBeingLoaded = domFactory().createIDOM();
		}
		return domBeingLoaded;
	}

	public IJavaModel javaModel()
	{
		return resource.javaModel();
	}

	public ILogger logger()
	{
		return resource.logger();
	}

	public JaxWsWorkspaceResource resource()
	{
		return resource;
	}

	public ServiceModelData serviceData()
	{
		return serviceData;
	}

	public DomUtil util()
	{
		return util;
	}

	protected void processCompilationUnit(final IWebServiceProject wsProject, final ICompilationUnit cu) throws JavaModelException
	{
		try {
			resource.disableSaving();
			
			if (cu.findPrimaryType() == null || !(cu.findPrimaryType().isInterface() || cu.findPrimaryType().isClass()))
			{
				if (logger().isDebug())
				{
					logger().logDebug("no primary type in compilation unit" + cu.getPath().toOSString());//$NON-NLS-1$
				}
				return;
			} 
						
			final IAnnotationInspector inspector = resource().newAnnotationInspector(cu.findPrimaryType());
			final IAnnotation<IType> wsAnnotation = inspector.inspectType(WS_ANNOTATION);
			if (wsAnnotation == null)
			{
				if (logger().isDebug())
				{
					logger().logDebug("no " + WS_ANNOTATION + " annotation found on type " + cu.findPrimaryType().getFullyQualifiedName());//$NON-NLS-1$//$NON-NLS-2$
				}
				return;
			}
			
			recordHierarchy(cu.findPrimaryType());
			
			if (wsAnnotation.getAppliedElement().isInterface())
			{
				// handle class changed to interface
				removeWsModelElements(wsProject, wsAnnotation.getAppliedElement().getFullyQualifiedName(), false);
				
				seiLoader.synchronizeInterface(wsProject, wsAnnotation, inspector);
			} else if (wsAnnotation.getAppliedElement().isClass())
			{
				// handle interface changed to class
				removeSeiModelElements(wsProject, wsAnnotation.getAppliedElement().getFullyQualifiedName(), false, true);
				
				// if the annotation has changed and endpointInterface has been added the existing
				// SEI in the project should be removed
				final IWebService ws = wsLoader.synchronizeWebService(wsProject, wsAnnotation, inspector);
				if (ws!=null && (ws.getServiceEndpoint()==null || !ws.getServiceEndpoint().isImplicit())) {
					removeSeiModelElements(wsProject, ws.getImplementation(), false, false);
				}
			} else
			{
				if (logger().isDebug())
				{
					logger().logDebug("annotation " + WS_ANNOTATION + " is put on type "//$NON-NLS-1$//$NON-NLS-2$
									+ wsAnnotation.getAppliedElement().getFullyQualifiedName()
									+ "which is neither interface, nor a class. Ignorring!");//$NON-NLS-1$
				}
			}			
		} finally {
			resource.enableSaving();
		}
	}
	
	/**
	 * Removes web services from the model.
	 * @param wsProject
	 * @param implBaseName
	 */
	protected void removeWsModelElements(final IWebServiceProject wsProject, final String implBaseName, final boolean isForPackage) 
	{
		final Iterator<IWebService> wsIter = wsProject.getWebServices().iterator();
		while (wsIter.hasNext())
		{
			final IWebService ws = wsIter.next();
			if (ws.getImplementation().equals(implBaseName) || 
				(isForPackage && ws.getImplementation().startsWith(implBaseName)))
			{
				serviceData().clearHierarchy(ws.getImplementation());
				wsIter.remove();
				if (ws.getServiceEndpoint() != null)
				{
					ws.getServiceEndpoint().getImplementingWebServices().remove(ws);
				}
				serviceData().unmap(ws);
			}
		}
	}
	
	/**
	 * Removes SEI's with <code>implBaseName</code> implementation from the model.
	 * @param wsProject
	 * @param implBaseName
	 * @param onlyExplicit if <code>true</code> implicit interfaces are not removed from model, otherwise both are removed.
	 */
	protected void removeSeiModelElements(final IWebServiceProject wsProject, final String implBaseName, final boolean isForPackage, final boolean onlyExplicit) 
	{
		final Iterator<IServiceEndpointInterface> seiIter = wsProject.getServiceEndpointInterfaces().iterator();
		while (seiIter.hasNext())
		{
			final IServiceEndpointInterface sei = seiIter.next();
			if (sei.getImplementation().equals(implBaseName) ||
				(isForPackage && sei.getImplementation().startsWith(implBaseName)))
			{
				if ((onlyExplicit && sei.isImplicit())) {
					continue;
				}
				serviceData().clearHierarchy(sei.getImplementation());
				sei.getImplementingWebServices().clear();
				seiIter.remove();
			}
		}
	}	

	void recordHierarchy(IType type) throws JavaModelException
	{
		final Collection<ICompilationUnit> compilationUnits = new LinkedList<ICompilationUnit>();
		
		for (IType superType : type.newSupertypeHierarchy(null).getAllSupertypes(type)) {
			compilationUnits.add(superType.getCompilationUnit());
		}
			
		serviceData().recordHierarchy(type.getFullyQualifiedName(), compilationUnits);
	}


	protected IWebServiceProject createProject(final IJavaProject jprj)
	{
		final IWebServiceProject thisProject = domFactory().createIWebServiceProject();
		util().setFeatureValue(thisProject, DomPackage.IWEB_SERVICE_PROJECT__NAME, jprj.getElementName());

		util().addToCollectionFeature(getDomBeingLoaded(), DomPackage.IDOM__WEB_SERVICE_PROJECTS, thisProject);
		return thisProject;
	}

	protected class LoaderCompilationUnitHandler implements ICompilationUnitHandler
	{
		public void started()
		{
		}

		public void finished()
		{
			resource().getContents().add(getDomBeingLoaded());
		}

		protected IWebServiceProject wsProject;

		public void handle(final IJavaProject jprj)
		{
			wsProject = createProject(jprj);
		}

		public void handle(final ICompilationUnit cu)
		{
			try
			{
				processCompilationUnit(wsProject, cu);
			} catch (JavaModelException jme)
			{
				logger().logError("Unable to parse the content of compilation unit" + cu.getPath().toOSString(), jme); //$NON-NLS-1$
			}
		}
	}
}

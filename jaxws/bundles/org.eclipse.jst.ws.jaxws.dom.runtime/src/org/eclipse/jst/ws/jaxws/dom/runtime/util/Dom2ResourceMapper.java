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
package org.eclipse.jst.ws.jaxws.dom.runtime.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;


/**
 * Class that takes care for mapping DOM objects to Eclipse resources. 
 * 
 * @author Georgi Vachkov
 */
public class Dom2ResourceMapper
{
	/** the singleton instance */
	public static final Dom2ResourceMapper INSTANCE = new Dom2ResourceMapper();
	
	private DomSwitch<String> implClassFinderSwitch;
	
	/**
	 * Singleton Constructor 
	 */
	private Dom2ResourceMapper() {	
		this.implClassFinderSwitch = createImplFinderSwitch();
	}
	
	/**
	 * Finds underlying {@link IResource} for this DOM object
	 * @param eObject the DOM object
	 * @return found resource or <code>null</code>
	 * @throws JavaModelException
	 */
	public IResource findResource(final EObject eObject) throws JavaModelException
	{
		ContractChecker.nullCheckParam(eObject);
		switch (eObject.eClass().getClassifierID()) 
		{
		case DomPackage.IWEB_SERVICE:
		case DomPackage.ISERVICE_ENDPOINT_INTERFACE: 
		case DomPackage.IWEB_METHOD: 
		case DomPackage.IWEB_PARAM:	
			final IType type = findType(eObject); 
			return type==null ? null : type.getResource();
		case DomPackage.IWEB_SERVICE_PROJECT:
			return findProject((IWebServiceProject)eObject);
		}

		return null;
	}
	
	/**
	 * Finds IProject instance for the web service project represented by <code>wsProject</code>
	 * @param wsProject the project DOM object
	 * @return found resource or <code>null</code>
	 */
	public IProject findProject(final IWebServiceProject wsProject)
	{
		if (wsProject == null) {
			return null;
		}

		return ResourcesPlugin.getWorkspace().getRoot().getProject(wsProject.getName());
	}
	
	/**
	 * Finds underlying {@link IType} instance for this <code>eObject</code> 
	 * @param eObject the DOM object
	 * @return the IType containing this <code>eObject</code> or <code>null</code>
	 * @throws JavaModelException
	 */
	public IType findType(final EObject eObject) throws JavaModelException 
	{
		ContractChecker.nullCheckParam(eObject);
		
		final IProject project = findProject(DomUtil.INSTANCE.findWsProject(eObject));
		final IJavaProject javaProject = JavaCore.create(project);
		final String fqName = implClassFinderSwitch.doSwitch(eObject);
		if (javaProject==null || fqName == null) {
			return null;
		}
		
		return javaProject.findType(fqName);
	}

	private DomSwitch<String> createImplFinderSwitch()
	{
		return new DomSwitch<String>()
		{
			@Override
			public String caseIWebService(IWebService ws) {
				return ws.getImplementation();
			}
			
			@Override
			public String caseIServiceEndpointInterface(IServiceEndpointInterface sei) {
				return sei == null ? null : sei.getImplementation();
			}
			
			@Override
			public String caseIWebMethod(IWebMethod wm) {
				return wm == null ? null : caseIServiceEndpointInterface((IServiceEndpointInterface) wm.eContainer());
			}	
			
			@Override
			public String caseIWebParam(IWebParam wp) {
				return caseIWebMethod((IWebMethod)wp.eContainer());
			}
		};
	}
}

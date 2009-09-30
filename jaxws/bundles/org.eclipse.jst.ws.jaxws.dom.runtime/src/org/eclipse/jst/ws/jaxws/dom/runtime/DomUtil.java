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
package org.eclipse.jst.ws.jaxws.dom.runtime;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.DomSwitch;


public class DomUtil
{
	public static final DomUtil INSTANCE = new DomUtil();
	
	private final DomSwitch<String> implSwitch;
	
	public DomUtil() {
		implSwitch = createImplFinderSwitch();
	}
	
	public void setFeatureValue(final EObject object, final int featureId, final Object value)
	{
		final EStructuralFeature feature = object.eClass().getEStructuralFeature(featureId);
		object.eSet(feature, value);
	}

	public Object getFeatureValue(final EObject object, final int featureId)
	{
		final EStructuralFeature feature = object.eClass().getEStructuralFeature(featureId);
		return object.eGet(feature);
	}

	public void addToCollectionFeature(final EObject object, final int featureId, final Object value)
	{
		@SuppressWarnings("unchecked")
		final Collection<Object> oldValue = (Collection<Object>) getFeatureValue(object, featureId);
		oldValue.add(value);
	}

	public IWebServiceProject findProjectByName(IDOM dom, String name)
	{
		for (IWebServiceProject wsProject : dom.getWebServiceProjects())
		{
			if (wsProject.getName().equals(name))
			{
				return wsProject;
			}
		}
		return null;
	}

	public IWebService findWsByImplName(IWebServiceProject wsProject, String name)
	{
		for (IWebService ws : wsProject.getWebServices())
		{
			if (ws.getImplementation().equals(name))
			{
				return ws;
			}
		}
		return null;
	}

	public IServiceEndpointInterface findSeiByImplName(IWebServiceProject wsProject, String name)
	{
		for (IServiceEndpointInterface sei : wsProject.getServiceEndpointInterfaces())
		{
			if (sei.getImplementation().equals(name))
			{
				return sei;
			}
		}
		return null;
	}
	
	
	public IWebMethod findWebMethodByImpl(IServiceEndpointInterface sei, String impl)
	{
		for (IWebMethod wm : sei.getWebMethods())
		{
			if (wm.getImplementation().equals(impl))
			{
				return wm;
			}
		}
		return null;
	}
	
	public IMethod findMethod(final IType seiType, final IWebMethod webMethod) throws JavaModelException
	{
		for (IMethod method : seiType.getMethods()) 
		{
			if (webMethod.getImplementation().equals(calcImplementation(method))) {
				return method;
			}
		}
		
		return null;
	}	

	public IJavaWebServiceElement findJavaWebServiceElemByImplName(IWebServiceProject wsProject, String name)
	{
		final IWebService ws = findWsByImplName(wsProject, name);
		return ws == null ? findSeiByImplName(wsProject, name) : ws;
	}
	
	/**
	 * Calculates the method signature to be used as implementation in {@link IWebMethod}
	 * @param method
	 * @return calculated string
	 * @throws JavaModelException
	 */
	public String calcImplementation(final IMethod method) throws JavaModelException
	{		
		return method.getElementName() + method.getSignature();
	}
	
	/**
	 * Extract {@link IWebServiceProject} out of DOM object by recursively
	 * iterating parent objects.
	 * @param eObject
	 * @return the found project or <code>null</code> in case not found
	 */
	public IWebServiceProject findWsProject(EObject eObject)
	{
		EObject parent = eObject;
		while (parent!=null) 
		{
			if (parent instanceof IWebServiceProject) {
				return (IWebServiceProject)parent;
			}
			
			parent = parent.eContainer();
		}
		
		return null;
	}		
	
	/**
	 * Defines if this SEI is part of outside-in web service
	 * @param sei
	 * @return true if there is a web service with wsdlLocation set to something different than <code>null</code>
	 */
	public boolean isOutsideInWebService(final IServiceEndpointInterface sei)
	{
		if (sei == null) {
			return false;
		}
		
		for (IWebService webService : sei.getImplementingWebServices()) {
			if (webService.getWsdlLocation()!=null) {
				return true;
			}
		}
		
		return false;
	}	
	
	/**
	 * Calculates unique implementation for DOM object in the scope of the resource in which
	 * they exist. Useful when you need to uniquely identify DOM object. 
	 * 
	 * @param eObject
	 * @return calculated implementation
	 */
	public String calcUniqueImpl(final EObject eObject)
	{
		if (eObject.eClass().getClassifierID() == DomPackage.IWEB_PARAM) 
		{
			final IWebParam wp = (IWebParam) eObject;
			return ((IWebMethod)wp.eContainer()).getImplementation() + "[" + wp.getImplementation() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return extractImplementation(eObject);
	}
	
	/**
	 * Extracts the implementation property for DOM object without casting :-D.
	 *
	 * @param eObject
	 * @return the implementation 
	 */
	public String extractImplementation(final EObject eObject) {
		return implSwitch.doSwitch(eObject);
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
				return sei.getImplementation();
			}
			
			@Override
			public String caseIWebMethod(IWebMethod wm) {
				return wm.getImplementation();
			}	
			
			@Override
			public String caseIWebParam(IWebParam wp) {
				return wp.getImplementation();
			}
			
			@Override
			public String caseIWebServiceProject(IWebServiceProject wsProject) {
				return wsProject.getName();
			}
		};
	}
}

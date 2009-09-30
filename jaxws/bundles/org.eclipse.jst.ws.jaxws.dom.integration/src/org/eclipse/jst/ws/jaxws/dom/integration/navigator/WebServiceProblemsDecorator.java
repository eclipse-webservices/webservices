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
package org.eclipse.jst.ws.jaxws.dom.integration.navigator;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jdt.ui.ProblemsLabelDecorator;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.utils.dom.validation.DomValidationConstants;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

public class WebServiceProblemsDecorator extends ProblemsLabelDecorator
{
	/** Enum to represent different marker types */
	public enum Severity {OK, ERROR, WARNING};
	
	private DomUtil domUtil = DomUtil.INSTANCE;
	
	@Override
	protected int computeAdornmentFlags(Object obj) 
	{
		final Severity severity = defineSeverity(obj);		
		if (severity == Severity.ERROR) {
			return JavaElementImageDescriptor.ERROR;
		} 
		
		if (severity == Severity.WARNING) {
			return JavaElementImageDescriptor.WARNING;
		}
		
		return 0;
	}	

	/**
	 * Checks the object's resource for markers and defines the severity of the
	 * problem if any. 
	 * @param obj
	 * @return the severity calculated.
	 */
	protected Severity defineSeverity(final Object obj) 
	{
		try 
		{
			// process additional DOM objects needed for UI - IWebServiceChildList and ISEIChildList
			if (obj instanceof IWebServiceChildList) {
				return defineSeverityForEList(((IWebServiceChildList)obj).getWSChildList());
			}
			
			if (obj instanceof ISEIChildList) {
				return defineSeverityForEList(((ISEIChildList)obj).getSEIChildList());
			}
			
			// do not process non DOM objects
			if( !(obj instanceof EObject) ) {
				return Severity.OK;
			}
			
			return defineSeverity((EObject) obj);
		} 
		catch (CoreException e) {
			(new Logger()).logError(e.getMessage(), e);
		}

		return Severity.OK;
	}
	
	protected Severity defineSeverityForEList(final List<? extends EObject> list) throws CoreException
	{
		Severity severity = Severity.OK;		
		for (EObject eObject : list) 
		{
			Severity tempSeverity = defineSeverity(eObject);
			if (tempSeverity == Severity.ERROR) {
				return Severity.ERROR;
			}
			
			if (tempSeverity==Severity.WARNING) {
				severity = Severity.WARNING;
			}
		}
		
		return severity;
	}
	
	protected Severity defineSeverity(final EObject eObject) throws CoreException 
	{ 		
		final IResource resource = Dom2ResourceMapper.INSTANCE.findResource(eObject);
		if (resource!=null) {
			return defineSeverity(eObject, resource);
		}
		
		return Severity.OK;
	}
		
	protected Severity defineSeverity(final EObject eObject, final IResource resource) throws CoreException
	{
		if (!resource.isAccessible()) {
			return Severity.OK;
		}
		
		final int depth = (resource.getType()==IResource.PROJECT) ? IResource.DEPTH_INFINITE : IResource.DEPTH_ZERO;
		final IMarker[] markers = resource.findMarkers(DomValidationConstants.MARKER_ID, false, depth);
		Severity severity = Severity.OK;
		for (IMarker marker : markers) 
		{
			if (!isRelevantFor(eObject, marker)) {
				continue;
			}
			
			int tempSeverity = (Integer)marker.getAttribute(IMarker.SEVERITY);
			if (tempSeverity==IMarker.SEVERITY_ERROR) {
				return Severity.ERROR;
			} 

			if(tempSeverity==IMarker.SEVERITY_WARNING) {
				severity = Severity.WARNING;
			}
		}
		
		if (severity == Severity.OK && (eObject instanceof IWebService)) 
		{
			// for bean with explicit interface we need to check the interface as well 
			// because it resides in another resource
			final IServiceEndpointInterface sei = ((IWebService)eObject).getServiceEndpoint();
			if (sei != null && !sei.isImplicit()) {
				severity = defineSeverity(sei);
			}
		}
		
		return severity;
	}

	/**
	 * Defines if <code>marker</code> is relevant for <code>eObject</code>.
	 * Only for web method and web parameter we need to define if the marker is relevant
	 * because they are not unique in the scope of the resource. For example if there is a
	 * marker set on the resource containing SEI, the SEI should be marked in any case 
	 * while the method only in case when the error is specifically for this method or 
	 * method parameter. 
	 * @param eObject the object to be validated
	 * @param marker the resource marker 
	 * @return <code>true</code> in case the marker is relevant for <code>eObject</code> 
	 * @throws CoreException
	 */
	protected boolean isRelevantFor(final EObject eObject, final IMarker marker) throws CoreException
	{
		switch (eObject.eClass().getClassifierID())
		{
		case DomPackage.IWEB_METHOD:
			return isRelevantForMethod((IWebMethod)eObject, marker);
		case DomPackage.IWEB_PARAM:
			return isRelevant(domUtil.calcUniqueImpl(eObject), marker);
		}
		
		return true;
	}
	
	/**
	 * Defines if <code>marker</code> is relevant for <code>webMethod</code>.
	 * Checks if the marker is specifically for this method or some of it's parameters.
	 * @param webMethod the method to be checked
	 * @param marker the marker to be checked
	 * @return <code>true</code> in case marker is relevant
	 * @throws CoreException
	 */	
	protected boolean isRelevantForMethod(final IWebMethod webMethod, final IMarker marker) throws CoreException 
	{
		if (isRelevant(domUtil.calcUniqueImpl(webMethod), marker)) {
			return true;
		}
		
		for (IWebParam webParam : webMethod.getParameters()) 
		{
			if (isRelevant(domUtil.calcUniqueImpl(webParam), marker)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if marker's {@link DomValidationConstants#IMPLEMENTATION} attribute has same
	 * value as <code>implementation</code>.
	 * @param implementation the implementation signature
	 * @param marker the marker to be checked
	 * @return <code>true</code> in case marker is for this implementation
	 * @throws CoreException
	 */
	protected boolean isRelevant(final String implementation, final IMarker marker) throws CoreException
	{
		final Object found = marker.getAttribute(DomValidationConstants.IMPLEMENTATION);
		if (found != null && found.equals(implementation)) {
			return true;
		}
		
		return false;
	}
}

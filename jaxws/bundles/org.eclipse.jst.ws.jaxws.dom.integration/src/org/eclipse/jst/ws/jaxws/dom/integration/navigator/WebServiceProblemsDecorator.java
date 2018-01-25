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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.apt.core.internal.AptPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jdt.ui.ProblemsLabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class WebServiceProblemsDecorator extends ProblemsLabelDecorator
{
	private static final String THIS_DECORATOR_ID = "org.eclipse.jst.ws.jaxws.dom.integration.navigator.WebServiceDecorator"; 
	
	/**
	 * The APT marker IDs
	 */
	private static final String APT_MARKER_ID = AptPlugin.APT_COMPILATION_PROBLEM_MARKER;
	private static final String APT_NONRECONCILE_MARKER_ID = AptPlugin.APT_NONRECONCILE_COMPILATION_PROBLEM_MARKER;
	
	/** Enum to represent different marker types */
	public enum Severity {OK, ERROR, WARNING};
	
	private DomUtil domUtil = DomUtil.INSTANCE;
	private final Dom2ResourceMapper dom2ResourceMapper = Dom2ResourceMapper.INSTANCE;
	
	public WebServiceProblemsDecorator()
	{
		addListener(new ILabelProviderListener()
		{
			public void labelProviderChanged(LabelProviderChangedEvent event)
			{
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						PlatformUI.getWorkbench().getDecoratorManager().update(THIS_DECORATOR_ID);
					}
				});
			}
		});
	}
	
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
		final Collection<IMarker> markers = findAptMarkers(resource, depth);
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
			return isRelevantForParameter((IWebParam)eObject, marker);
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
		final IType type = dom2ResourceMapper.findType(webMethod);
		final IMethod javaMethod = domUtil.findMethod(type, webMethod);
		final IAnnotationInspector annotationInspector = AnnotationFactory.createAnnotationInspector(type);
		boolean relevant = isMarkerRelevantFor(javaMethod, marker, annotationInspector);
		if (!relevant)
		{
			for (String methodParameterName : javaMethod.getParameterNames())
			{
				final ITypeParameter param = javaMethod.getTypeParameter(methodParameterName);
				relevant = relevant || isMarkerRelevantFor(param, marker, annotationInspector);
			}
		}

		return relevant;
	}

	protected boolean isRelevantForParameter(final IWebParam webParam, final IMarker marker) throws CoreException
	{
		final IType type = dom2ResourceMapper.findType(webParam);
		final ITypeParameter javaParameter = findJavaParameter(type, webParam);
		return isMarkerRelevantFor(javaParameter, marker, AnnotationFactory.createAnnotationInspector(type));
	}

	/**
	 * Finds the {@link ITypeParameter} instance which corresponds to the web parameter specified
	 */
	private ITypeParameter findJavaParameter(final IType type, final IWebParam webParam) throws JavaModelException
	{
		final IMethod javaMethod = domUtil.findMethod(type, (IWebMethod) webParam.eContainer());
		for (String paramName : javaMethod.getParameterNames())
		{
			final ITypeParameter javaParam = javaMethod.getTypeParameter(paramName);
			if (webParam.getImplementation().equals(javaParam.getElementName()))
			{
				return javaParam;
			}
		}

		throw new IllegalStateException("Parameter " + webParam.getImplementation() + " not found");
	}

	private boolean isMarkerRelevantFor(final IMethod javaMethod, final IMarker marker, final IAnnotationInspector inspector) throws CoreException
	{
		for (IAnnotation<IMethod> ann : inspector.inspectMethod(javaMethod))
		{
			if (isMarkerRelevantToAnnotation(marker, ann))
			{
				return true;
			}
		}

		return false;
	}

	private boolean isMarkerRelevantFor(final ITypeParameter parameter, final IMarker marker, final IAnnotationInspector inspector) throws CoreException
	{
		for (IAnnotation<ITypeParameter> ann : inspector.inspectParam(parameter))
		{
			if (isMarkerRelevantToAnnotation(marker, ann))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Check whether the marker is relevant for the annotation specified<br>
	 * The implementation would read all the annotations relevant for the java element and check whether the marker start/end position is between annotation start/end
	 */
	private boolean isMarkerRelevantToAnnotation(final IMarker marker, final IAnnotation<? extends IJavaElement> ann) throws CoreException
	{
		final int annStartPos = ann.getLocator().getStartPosition();
		final int annEndPos = annStartPos + ann.getLocator().getLength() - 1;
		return (annStartPos <= (Integer) marker.getAttribute(IMarker.CHAR_START)) && (annEndPos >= (Integer) marker.getAttribute(IMarker.CHAR_END));
	}

	/**
	 * Finds all APT nonreconcile markers associated with the resource specified
	 */
	private Collection<IMarker> findAptMarkers(final IResource resource, final int depth) throws CoreException
	{
		final Collection<IMarker> result = new ArrayList<IMarker>();
		// Search for both APT marker types. One of each IDs is used on Linux and Windows
		for(final IMarker m : resource.findMarkers(APT_MARKER_ID, false, depth))
		{
			result.add(m);
		}
		
		for(final IMarker m : resource.findMarkers(APT_NONRECONCILE_MARKER_ID, false, depth))
		{
			result.add(m);
		}
		
		return result;
	}
}

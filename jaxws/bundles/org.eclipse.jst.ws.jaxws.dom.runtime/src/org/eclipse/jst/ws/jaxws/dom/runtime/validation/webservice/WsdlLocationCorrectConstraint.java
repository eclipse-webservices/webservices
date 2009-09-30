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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.webservice;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WSDL_LOCATION_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintSeverity;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.AnnotationAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Checks if the WSDL location contains valid value.
 * 
 * @author Georgi Vachkov
 */
public class WsdlLocationCorrectConstraint  extends AbstractValidationConstraint
{
	public WsdlLocationCorrectConstraint() {
		super(new WarningConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(IValidationContext ctx) throws JavaModelException 
	{	final IWebService ws = (IWebService)ctx.getTarget();
		final IAnnotation<? extends IJavaElement> annotation = findAnnotation(ws, WS_ANNOTATION);
		if (annotation==null || annotation.getPropertyValue(WSDL_LOCATION_ATTRIBUTE)==null) {
			return createOkStatus(ws);
		}
		
		if (checkWsdlLocationInProject(ws)) {
			return createOkStatus(ws);
		}
		
		return checkWsdlUrl(ws, annotation.getPropertyValue(WSDL_LOCATION_ATTRIBUTE));
	}
	
	protected IStatus checkWsdlUrl(final IWebService ws, final String wsdlUrl) throws JavaModelException
	{
		try {
			new URL(wsdlUrl);
		}
		catch (MalformedURLException e) { // $JL-EXC$
			String msg = ValidationMessages.bind(ValidationMessages.WsValidation_WsdlDoesNotExists,
					e.getLocalizedMessage());
			return createStatus(ws, msg, WS_ANNOTATION, WSDL_LOCATION_ATTRIBUTE);
		} 
		
		return createOkStatus(ws);
	}
	
	protected boolean checkWsdlLocationInProject(final IWebService webService) throws JavaModelException 
	{
		final IAnnotationAdapter adapter = (IAnnotationAdapter)AnnotationAdapterFactory.INSTANCE.adapt(webService, IAnnotationAdapter.class);
		final IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation(WS_ANNOTATION);
		if (annotation==null) {
			return true;
		}
		
		final String wsdlLocation = annotation.getPropertyValue(WSDL_LOCATION_ATTRIBUTE);
		if (wsdlLocation == null) {
			return true;
		}
		
		if (wsdlLocation.trim().length()==0) {
			return false;
		}
		
		return findFile(webService, wsdlLocation);
	}
	
	protected boolean findFile(final IWebService webService, final String file) throws JavaModelException
	{
		final IProject project = Dom2ResourceMapper.INSTANCE.findProject((IWebServiceProject)webService.eContainer());
		IResource resource = project.findMember(file);
		if (resource!=null)	{
			return true;
		}
		
		final IJavaProject javaProject = JavaCore.create(project);
		for (IPackageFragmentRoot root : javaProject.getPackageFragmentRoots()) 
		{
			if (root.getResource()==null) {
				continue;
			}
			
			if (root.getResource().getType()==IResource.FOLDER) {
				if (((IFolder) root.getResource()).findMember(file) != null) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected static class WarningConstraintDescriptor extends WsConstraintDescriptor
	{
		@Override
		public ConstraintSeverity getSeverity() {
			return ConstraintSeverity.WARNING;
		}
	}
}

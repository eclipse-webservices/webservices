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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintSeverity;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.AnnotationAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.annotation.impl.AbstractLocatorsTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IConstraintStatusExtended;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IProblemLocation;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;

public class AbstractValidationConstraintTest extends AbstractLocatorsTest 
{
	private MyAbstractValidationConstraint constraint;
	private Mock<IResource> resource;
	
	@Override
	public void setUp()
	{
		super.setUp();
		resource = mock(IResource.class);		
		Mock<IConstraintDescriptor> descriptor = mock(IConstraintDescriptor.class);
		descriptor.stubs().method("getPluginId").will(returnValue("plugin.id"));
		descriptor.stubs().method("getSeverity").will(returnValue(ConstraintSeverity.ERROR));
		descriptor.stubs().method("getStatusCode").will(returnValue(-1));
		
		constraint = new MyAbstractValidationConstraint(descriptor.proxy());
	}
	
	public void testCreateOkStatus() throws JavaModelException 
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();		
		IConstraintStatusExtended cs = constraint.createOkStatus(ws);
		assertNotNull("Constraint status is null", cs);
		assertTrue(cs.isOK());
		assertEquals(0, cs.getResultLocus().size());
		assertEquals(0, cs.getProblemLocations().size());
	}

	public void testCreateErrorStatus() throws JavaModelException 
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();		
		IConstraintStatusExtended cs = constraint.createStatus(ws, "error message", null, null);
		assertNotNull(cs);
		assertFalse(cs.isOK()); 
		IProblemLocation pl = cs.getProblemLocations().iterator().next();
		assertEquals(resource.proxy(), pl.getResource());
		assertNotNull(pl.getLocator());
		assertEquals(0, pl.getLocator().getStartPosition());
		assertEquals(0, pl.getLocator().getLength());
	}

	public void testGetLocatorNoAnnotations() throws JavaModelException 
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();		
		IProblemLocation pl = constraint.getLocator(ws, null, null);
		assertEquals(resource.proxy(), pl.getResource());
	}
	
	public void testGetLocatorForAnnotation() throws JavaModelException
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();
		Mock<IJavaElement> javaElement = mock(IJavaElement.class);
		javaElement.stubs().method("getResource").will(returnValue(resource.proxy()));
		annotation.stubs().method("getAppliedElement").will(returnValue(javaElement.proxy()));
		
		final IAnnotationAdapter adapter = (IAnnotationAdapter)AnnotationAdapterFactory.INSTANCE.adapt(ws, IAnnotationAdapter.class);
		adapter.addAnnotation(WSAnnotationFeatures.WS_ANNOTATION, annotation.proxy());
		IProblemLocation pl = constraint.getLocator(ws, WSAnnotationFeatures.WS_ANNOTATION, null);
		assertEquals(resource.proxy(), pl.getResource());
		assertEquals(annotation.proxy().getLocator(), pl.getLocator());
	}
	
	public void testGetLocatorForAttribute() throws JavaModelException
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();	
		Mock<IJavaElement> javaElement = mock(IJavaElement.class);
		javaElement.stubs().method("getResource").will(returnValue(resource.proxy()));
		annotation.stubs().method("getAppliedElement").will(returnValue(javaElement.proxy()));
		
		final IAnnotationAdapter adapter = (IAnnotationAdapter)AnnotationAdapterFactory.INSTANCE.adapt(ws, IAnnotationAdapter.class);
		adapter.addAnnotation(WSAnnotationFeatures.WS_ANNOTATION, annotation.proxy());
		IProblemLocation pl = constraint.getLocator(ws, WSAnnotationFeatures.WS_ANNOTATION, "param1");
		assertEquals(resource.proxy(), pl.getResource());
		assertEquals(param1.getValue().getLocator(), pl.getLocator());
	}

	protected class MyAbstractValidationConstraint extends AbstractValidationConstraint
	{
		public MyAbstractValidationConstraint(IConstraintDescriptor descriptor) {
			super(descriptor);
		}

		public IStatus validate(IValidationContext ctx) {
			return null;
		}
		
		@Override
		public IConstraintStatusExtended createOkStatus(EObject object) throws JavaModelException {
			return super.createOkStatus(object);
		}
		
		@Override
		protected IConstraintStatusExtended createStatus(EObject object, String errorMessage, String annFQName, String attributeName) throws JavaModelException {
			return super.createStatus(object, errorMessage, annFQName, attributeName);
		}
		
		@Override
		protected IResource findResource(EObject eObject) throws JavaModelException {			
			return resource.proxy();
		}
		
		public IProblemLocation getLocator(EObject eObject, String annFQName, String attributeName) throws JavaModelException {
			return super.getLocator(eObject, annFQName, attributeName);
		}
		
		@Override
		protected ILocator getLocatorForImplementation(final EObject eObject) throws JavaModelException 
		{
			return new ILocator() {
				public int getLength() { return 0; }
				public int getStartPosition() { return 0; }
				public int getLineNumber() { return 0; }
			};
		}

		@Override
		protected IStatus doValidate(IValidationContext ctx) throws JavaModelException {
			return null;
		}
	}
}

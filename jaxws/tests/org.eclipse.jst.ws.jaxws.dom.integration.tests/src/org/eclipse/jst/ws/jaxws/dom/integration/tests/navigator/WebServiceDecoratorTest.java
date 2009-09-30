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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator;

import junit.framework.TestCase;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.CustomDomItemProviderAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ISEIChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.IWebServiceChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.WebServiceProblemsDecorator;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.WebServiceProblemsDecorator.Severity;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.utils.dom.validation.DomValidationConstants;

/**
 * Tests for {@link WebServiceDecorator} class
 * 
 * @author Georgi Vachkov
 */
public class WebServiceDecoratorTest extends TestCase 
{
	private TestWebServiceDecorator decorator = new TestWebServiceDecorator();;
	private TestProject testProject;
	private IType seiType;
	private IType wsType;
	
	private DomUtil domUtil = DomUtil.INSTANCE;
	private IWebService ws;
	private IServiceEndpointInterface sei;
	private IWebServiceProject wsProject;
	
	@Override
	public void setUp() throws CoreException
	{
		testProject = new TestProject();
		testProject.createSourceFolder("src");
		IPackageFragment pack = testProject.createPackage("test");
		seiType = testProject.createType(pack, "Sei.java", "public interface Sei {}");
		
		sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		sei.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, seiType.getFullyQualifiedName());

		wsType = testProject.createType(pack, "Ws.java", "public class Ws {}");
		ws = DomFactory.eINSTANCE.createIWebService();
		ws.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, wsType.getFullyQualifiedName());
		
		wsProject = DomFactory.eINSTANCE.createIWebServiceProject();
		wsProject.eSet(DomPackage.Literals.IWEB_SERVICE_PROJECT__NAME, testProject.getProject().getName());
		wsProject.getServiceEndpointInterfaces().add(sei);
		wsProject.getWebServices().add(ws);
	}

	public void testDefineSeverityForObject() throws CoreException 
	{
		assertEquals(Severity.OK, decorator.defineSeverity(new Integer(1)));
	}
	
	public void testDefineSeverityForProject() throws CoreException 
	{
		assertEquals(Severity.OK, decorator.defineSeverity(wsProject));
		
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);		
		assertEquals(Severity.ERROR, decorator.defineSeverity(wsProject));

		seiType.getResource().deleteMarkers(DomValidationConstants.MARKER_ID, true, IResource.DEPTH_ZERO);
		assertEquals(Severity.OK, decorator.defineSeverity(wsProject));

		marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		assertEquals(Severity.ERROR, decorator.defineSeverity(wsProject));
	}
	
	public void testDefineSeverityForISEIChildList() throws CoreException 
	{
		Adapter adapter = CustomDomItemProviderAdapterFactory.INSTANCE.adapt(wsProject, ISEIChildList.class);
		assertEquals(Severity.OK, decorator.defineSeverity(adapter));	
		
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		
		assertEquals(Severity.ERROR, decorator.defineSeverity(adapter));			
		
		adapter = CustomDomItemProviderAdapterFactory.INSTANCE.adapt(wsProject, IWebServiceChildList.class);
		assertEquals(Severity.OK, decorator.defineSeverity(adapter));		
	}
	
	public void testDefineSeverityForIWebServiceChildList() throws CoreException 
	{
		Adapter adapter = CustomDomItemProviderAdapterFactory.INSTANCE.adapt(wsProject, IWebServiceChildList.class);
		assertEquals(Severity.OK, decorator.defineSeverity(adapter));	
		
		IMarker marker = wsType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		
		assertEquals(Severity.ERROR, decorator.defineSeverity(adapter));			
		
		adapter = CustomDomItemProviderAdapterFactory.INSTANCE.adapt(wsProject, ISEIChildList.class);
		assertEquals(Severity.OK, decorator.defineSeverity(adapter));			
	}
	
	public void testDefineSeverityIWebService() throws CoreException
	{
		assertEquals(Severity.OK, decorator.defineSeverity(ws));
		
		IMarker marker = wsType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		
		assertEquals(Severity.ERROR, decorator.defineSeverity(ws));
	}
	
	public void testDefineSeverityIWebServiceWrongSei() throws CoreException
	{
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);

		assertEquals(Severity.OK, decorator.defineSeverity(ws));

		sei.getImplementingWebServices().add(ws);
		assertEquals(Severity.ERROR, decorator.defineSeverity(ws));
	}
	
	public void testDefineSeveritySei() throws CoreException
	{
		assertEquals(Severity.OK, decorator.defineSeverity(sei));
		
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		assertEquals(Severity.ERROR, decorator.defineSeverity(sei));
		
	}
	
	public void testDefineSeveritySeiWrongMethod() throws CoreException
	{
		assertEquals(Severity.OK, decorator.defineSeverity(sei));
		
		IWebMethod webMethod = createMethod(true);
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);		
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, domUtil.calcUniqueImpl(webMethod));
		
		assertEquals(Severity.ERROR, decorator.defineSeverity(sei));
	}
	
	public void testDefineSeveritySeiWrongParam() throws CoreException
	{
		assertEquals(Severity.OK, decorator.defineSeverity(sei));
		
		IWebMethod webMethod = createMethod(true);
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);		
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, domUtil.calcUniqueImpl(webMethod.getParameters().get(0)));
		
		assertEquals(Severity.ERROR, decorator.defineSeverity(sei));
	}
	
	public void testDefineSeverityEObjectIResource() throws CoreException 
	{		
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);		
		assertEquals(Severity.ERROR, decorator.defineSeverity(sei, seiType.getResource()));

		assertEquals(Severity.ERROR, decorator.defineSeverity(wsProject, testProject.getProject()));

		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		assertEquals(Severity.WARNING, decorator.defineSeverity(sei, seiType.getResource()));

		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
		assertEquals(Severity.OK, decorator.defineSeverity(sei, seiType.getResource()));
	}

	public void testIsRelevantForMethod() throws CoreException 
	{
		final IWebMethod webMethod = createMethod(false);
		
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, domUtil.calcUniqueImpl(webMethod));
		assertTrue(decorator.isRelevantForMethod(webMethod, marker));
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, "other");
		assertFalse(decorator.isRelevantForMethod(webMethod, marker));
	}

	public void testIsRelevant() throws CoreException 
	{
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		assertFalse(decorator.isRelevant("impl", marker));
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, "impl");
		assertTrue(decorator.isRelevant("impl", marker));
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, "impl");
		assertFalse(decorator.isRelevant("newImpl", marker));		
	}
	
	public void testIsRelevantFor() throws CoreException 
	{
		IMarker marker = seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		assertTrue(decorator.isRelevantFor(sei, marker));
		IWebMethod webMethod = createMethod(false);
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, domUtil.calcUniqueImpl(webMethod));
		assertTrue(decorator.isRelevantFor(webMethod, marker));
		webMethod = createMethod(true);
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, domUtil.calcUniqueImpl(webMethod.getParameters().get(0)));
		assertTrue(decorator.isRelevantFor(webMethod, marker));
		assertTrue(decorator.isRelevantFor(webMethod.getParameters().get(0), marker));
		marker.setAttribute(DomValidationConstants.IMPLEMENTATION, "impl");
		assertFalse(decorator.isRelevantFor(webMethod, marker));
		assertFalse(decorator.isRelevantFor(webMethod.getParameters().get(0), marker));		
	}
	
	private IWebMethod createMethod(boolean withParam) 
	{
		final IWebMethod webMethod = DomFactory.eINSTANCE.createIWebMethod();
		webMethod.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "method(I)");
		
		if (withParam) 
		{
			final IWebParam webParam = DomFactory.eINSTANCE.createIWebParam();
			webParam.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "a");
			webMethod.getParameters().add(webParam);
		}
		
		return webMethod;
	}

	protected static class TestWebServiceDecorator extends WebServiceProblemsDecorator
	{
		@Override
		protected boolean isRelevant(final String implementation, final IMarker marker) throws CoreException {
			return super.isRelevant(implementation, marker);
		}
		
		@Override
		protected boolean isRelevantFor(final EObject eObject, final IMarker marker) throws CoreException {
			return super.isRelevantFor(eObject, marker);
		}
		
		@Override
		protected boolean isRelevantForMethod(final IWebMethod webMethod, final IMarker marker) throws CoreException {
			return super.isRelevantForMethod(webMethod, marker);
		}
		
		@Override
		protected Severity defineSeverity(final EObject eObject, final IResource resource) throws CoreException {
			return super.defineSeverity(eObject, resource);
		}
		
		@Override
		protected Severity defineSeverity(final Object obj) {
			return super.defineSeverity(obj);
		}
	}
}

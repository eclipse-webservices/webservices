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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.PORT_NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.SERVICE_NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WSDL_LOCATION_ATTRIBUTE;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer.WsSerializerAdapter;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

/**
 * Tests for {@link WsSerializerAdapter} class. 
 * 
 * @author Georgi Vachkov
 */
public class WsSerializerAdapterTest extends SerializerAdapterTestFixture
{
	public void testCreateIAnnotationIWebServiceNpe() throws JavaModelException
	{
		try {
			new MyWsSerializerAdapter(resource).createIAnnotation((IWebService)null, project.getJavaProject().getJavaModel());
			fail("NullPointerException not thrown");
		} catch (NullPointerException _) {}
		
		try {
			new MyWsSerializerAdapter(resource).createIAnnotation(ws, null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException _) {}		
	}	
	
	public void testCreateIAnnotationIWebServiceExplicit() throws JavaModelException 
	{
		final IAnnotation<? extends IJavaElement> annotation = new MyWsSerializerAdapter(resource).createIAnnotation(ws, project.getJavaProject().getJavaModel());
		assertEquals(5, annotation.getParamValuePairs().size());
		checkIWebServiceIAnnotation(annotation);
		assertNotNull(annotation.getPropertyValue(WSAnnotationFeatures.ENDPOINT_INTERFACE_ATTRIBUTE));
		assertEquals(implBeanType.getFullyQualifiedName(), ((IType)annotation.getAppliedElement()).getFullyQualifiedName());
	}
	
	public void testCreateIAnnotationIWebServiceImplicit() throws JavaModelException 
	{
		final IAnnotation<? extends IJavaElement> annotation = new MyWsSerializerAdapter(resource).createIAnnotation(wsImplicit, project.getJavaProject().getJavaModel());
		assertEquals(5, annotation.getParamValuePairs().size());
		checkIWebServiceIAnnotation(annotation);
		
		assertEquals("ImplicitName", annotation.getPropertyValue(WSAnnotationFeatures.NAME_ATTRIBUTE));		
		assertEquals(implBeanImplicit.getFullyQualifiedName(), ((IType)annotation.getAppliedElement()).getFullyQualifiedName());
	}
	
	public void testCreateIAnnotationIWebServiceDefault() throws JavaModelException
	{
		resource.disableSaving();
		wsImplicit.getServiceEndpoint().setName("ImplicitImplBean");
		wsImplicit.setName("ImplicitImplBeanService");
		wsImplicit.setPortName("ImplicitImplBeanPort");
		wsImplicit.setTargetNamespace("http://test/");
		wsImplicit.setWsdlLocation(null);
		final IAnnotation<? extends IJavaElement> annotation =new MyWsSerializerAdapter(resource).createIAnnotation(wsImplicit, project.getJavaProject().getJavaModel());
		assertEquals(0, annotation.getParamValuePairs().size());
	}
	
	public void testSaveAnnotation() throws JavaModelException
	{
		resource.getSerializerFactory().adapt(ws, IAnnotationSerializer.class);
		ws.setName("ChangedService");
		
		IAnnotationInspector inspector = AnnotationFactory.createAnnotationInspector(implBeanType);
		IAnnotation<IType> found = inspector.inspectType(WS_ANNOTATION);
		assertNotNull(found);
		assertEquals("ChangedService", found.getPropertyValue(SERVICE_NAME_ATTRIBUTE));
		assertEquals(PORT_NAME, found.getPropertyValue(PORT_NAME_ATTRIBUTE));
		assertEquals(TARGET_NAMESPACE, found.getPropertyValue(TARGET_NAMESPACE_ATTRIBUTE));
		assertEquals(WSDL_LOCATION, found.getPropertyValue(WSDL_LOCATION_ATTRIBUTE));
	}
	
	public static void checkIWebServiceIAnnotation(IAnnotation<? extends IJavaElement> annotation)
	{
		boolean [] found = {false, false, false, false};
		String [] params = {"serviceName", "portName", "targetNamespace", "wsdlLocation" };
		for (IParamValuePair paramValuePair : annotation.getParamValuePairs()) 
		{
			String param = paramValuePair.getParam();
			if (param.equals(WSAnnotationFeatures.SERVICE_NAME_ATTRIBUTE)) {
				assertEquals(SERVICE_NAME, paramValuePair.getValue().toString());
				found[0] = true;
			}
			if (param.equals(WSAnnotationFeatures.PORT_NAME_ATTRIBUTE)) {
				assertEquals(PORT_NAME, paramValuePair.getValue().toString());
				found[1] = true;
			}
			if (param.equals(WSAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE)) {
				assertEquals(TARGET_NAMESPACE, paramValuePair.getValue().toString());
				found[2] = true;
			}
			if (param.equals(WSAnnotationFeatures.WSDL_LOCATION_ATTRIBUTE)) {
				assertEquals(WSDL_LOCATION, paramValuePair.getValue().toString());
				found[3] = true;
			}			
		}
		
		for (int i=0; i < found.length; i++) 
		{
			assertTrue("Annotation param " + params[i] + " not found", found[i]);
		}
	}
	
	protected class MyWsSerializerAdapter extends WsSerializerAdapter
	{
		public MyWsSerializerAdapter(JaxWsWorkspaceResource resource) {
			super(resource);
		}

		@Override
		public IAnnotation<? extends IJavaElement> createIAnnotation(final IWebService ws, final IJavaModel javaModel) throws JavaModelException
		{
			return super.createIAnnotation(ws, javaModel);
		}
	}
}

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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.WP_ANNOTATION;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer.ParameterSerializerAdapter;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

public class ParameterSerializerAdapterTest extends SerializerAdapterTestFixture
{
	private MyParameterSerializerAdapter adapter;
	private IMethod method; 
	private IWebMethod wsMethod;
	private IWebParam webParam; 
	private IAnnotationInspector inspector;
	
	public void setUp() throws Exception
	{
		super.setUp();
		resource.startSynchronizing();
		adapter = new MyParameterSerializerAdapter(resource);
		method = seiType.createMethod("public void method(int param1);", null, false, null);
		wsMethod = domUtil.findWebMethodByImpl(sei, domUtil.calcImplementation(method));
		webParam = wsMethod.getParameters().get(0);
		adapter.setTarget(webParam);
		resource.disableSaving();
		inspector = AnnotationFactory.createAnnotationInspector(seiType);
	}
	
	public void testGetAnnotationKind() throws Exception
	{
		// IN case
		webParam.setKind(WebParamKind.IN);
		IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(0, annotation.getParamValuePairs().size());
		
		// INOUT case
		webParam.setKind(WebParamKind.INOUT);
		annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(1, annotation.getParamValuePairs().size());
		IParamValuePair paramValue = annotation.getParamValuePairs().iterator().next();
		assertEquals("mode", paramValue.getParam());
		assertTrue(WPAnnotationFeatures.WEB_PARAM_MODE_INOUT.endsWith(paramValue.getValue().toString()));
		
		// OUT case
		webParam.setKind(WebParamKind.OUT);
		annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(1, annotation.getParamValuePairs().size());
		paramValue = annotation.getParamValuePairs().iterator().next();
		assertEquals("mode", paramValue.getParam());
		assertTrue(WPAnnotationFeatures.WEB_PARAM_MODE_OUT.endsWith(paramValue.getValue().toString()));
	}
	
	public void testGetAnnotationHeader() throws Exception
	{
		// not in header case
		webParam.setHeader(false);
		IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(0, annotation.getParamValuePairs().size());
		
		// in header case
		webParam.setHeader(true);
		annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(1, annotation.getParamValuePairs().size());
		IParamValuePair paramValue = annotation.getParamValuePairs().iterator().next();
		assertEquals("header", paramValue.getParam());
		assertEquals("true", paramValue.getValue().toString());	
	}
	
	public void testGetAnnotationName() throws Exception
	{
		webParam.setName("arg0");
		IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(0, annotation.getParamValuePairs().size());
		
		// in header case
		webParam.setName("changed");
		annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(1, annotation.getParamValuePairs().size());
		IParamValuePair paramValue = annotation.getParamValuePairs().iterator().next();
		assertEquals("name", paramValue.getParam());
		assertEquals("changed", paramValue.getValue().toString());		
	}

	public void testGetAnnotationPartName() throws Exception
	{
		wsMethod.setSoapBindingStyle(SOAPBindingStyle.RPC);
		webParam.setPartName("arg0");
		IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation();
		assertEquals(WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(0, annotation.getParamValuePairs().size());
		
		// in header case
		webParam.setPartName("changed");
		annotation = adapter.getAnnotation();
		assertEquals(WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(1, annotation.getParamValuePairs().size());
		IParamValuePair paramValue = annotation.getParamValuePairs().iterator().next();
		assertEquals("partName", paramValue.getParam());
		assertEquals("changed", paramValue.getValue().toString());		
	}	
	
	public void testChangeNameChangesPartName() throws Exception
	{
		resource.enableSaving();
		webParam.setName("changed");
		
		IAnnotation<ITypeParameter> annotation = inspector.inspectParam(method.getTypeParameter(webParam.getImplementation()), WP_ANNOTATION);
		assertNotNull(annotation);
		assertEquals("changed", annotation.getPropertyValue("name"));
		assertEquals(null, annotation.getPropertyValue("partName"));
	}
	
	public void testChangeNameDoesNotChangePartNameIfPartNameInAnnotation() throws Exception
	{
		method = seiType.createMethod("public void method1(@javax.jws.WebParam(name=\"param1\", partName=\"param1Part\") int param1);", null, false, null);
		wsMethod = testUtil.findWebMethodByName(sei, "method1");
		webParam = testUtil.findParam(wsMethod, "param1");
		wsMethod.setSoapBindingStyle(SOAPBindingStyle.RPC);
		resource.enableSaving();
		webParam.setName("test");
		IAnnotation<ITypeParameter> annotation = inspector.inspectParam(method.getTypeParameter(webParam.getImplementation()), WP_ANNOTATION);
		assertNotNull(annotation);
		assertEquals("param1Part", annotation.getPropertyValue("partName"));
	}

	public void testGetAnnotationTargetNS() throws Exception
	{
		wsMethod.setSoapBindingStyle(SOAPBindingStyle.RPC);
		webParam.setTargetNamespace("http://com.sap/test");
		IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(0, annotation.getParamValuePairs().size());
		
		// changed target namespace case
		webParam.setTargetNamespace("http://changed");
		annotation = adapter.getAnnotation();
		assertEquals(WPAnnotationFeatures.WP_ANNOTATION, annotation.getAnnotationName());
		assertEquals(1, annotation.getParamValuePairs().size());
		IParamValuePair paramValue = annotation.getParamValuePairs().iterator().next();
		assertEquals("targetNamespace", paramValue.getParam());
		assertEquals("http://changed", paramValue.getValue().toString());		
	}	
	
	public void testSaveAnnotation() throws Exception
	{
		wsMethod.setSoapBindingStyle(SOAPBindingStyle.DOCUMENT);
		webParam.setName("changed");
		webParam.setKind(WebParamKind.INOUT);
		webParam.setPartName("different");
		webParam.setHeader(true);
		resource.enableSaving();
		webParam.setTargetNamespace("http://changed/terget/ns");
		
		IAnnotation<ITypeParameter> annotation = inspector.inspectParam(method.getTypeParameter(webParam.getImplementation()), WP_ANNOTATION);
		assertNotNull(annotation);
		assertEquals(4, annotation.getParamValuePairs().size());
		assertEquals("changed", annotation.getPropertyValue("name"));
		assertTrue(WPAnnotationFeatures.WEB_PARAM_MODE_INOUT.endsWith(annotation.getPropertyValue("mode")));
		assertEquals("http://changed/terget/ns", annotation.getPropertyValue("targetNamespace"));

		wsMethod.setSoapBindingStyle(SOAPBindingStyle.RPC);
		webParam.setPartName("different");
		// create new inspector cause source has been changed
		inspector = AnnotationFactory.createAnnotationInspector(seiType);
		annotation = inspector.inspectParam(method.getTypeParameter(webParam.getImplementation()), WP_ANNOTATION);
		assertEquals(5, annotation.getParamValuePairs().size());
		assertEquals("different", annotation.getPropertyValue("partName"));
	}
	
	public void testEmptyAnnotationRemoved() throws JavaModelException
	{
		method = seiType.createMethod("public void method1(@javax.jws.WebParam(name=\"param1\") int param1);", null, false, null);
		wsMethod = testUtil.findWebMethodByName(sei, "method1");
		webParam = testUtil.findParam(wsMethod, "param1");
		resource.enableSaving();
		webParam.setName("arg0");
		IAnnotation<ITypeParameter>annotation = inspector.inspectParam(method.getTypeParameter(webParam.getImplementation()), WP_ANNOTATION);
		assertNull("Empty annotation has not been removed", annotation);
	}

	protected class MyParameterSerializerAdapter extends ParameterSerializerAdapter
	{
		public MyParameterSerializerAdapter(JaxWsWorkspaceResource resource) {
			super(resource);
		}

		public IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException {
			return super.getAnnotation();
		}
	}
}

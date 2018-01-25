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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer.MethodSerializerAdapter;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures.WM_ANNOTATION;

public class MethodSerializerAdapterTest extends SerializerAdapterTestFixture 
{
	private IWebMethod webMethod1;
	private IMethod method1;
	
	public void setUp() throws Exception
	{
		super.setUp();
	
		resource.startSynchronizing();
		method1 = seiType.createMethod("public void test1();", null, true, null);
		webMethod1 = sei.getWebMethods().get(0);
	}

	public void testGetAnnotationDefaultValues() throws Exception
	{
		MyMethodSerializerAdapter adapter = new MyMethodSerializerAdapter(resource, webMethod1);
		IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation();
		assertNotNull(annotation);
		assertEquals(0, annotation.getParamValuePairs().size());
		assertNull(annotation.getPropertyValue(WMAnnotationFeatures.WM_NAME_ATTRIBUTE));
		assertNull(annotation.getPropertyValue(WMAnnotationFeatures.WM_EXCLUDED_ATTRIBUTE));
	}
	
	public void testGetAnnotationNonDefaultValues() throws Exception
	{		
		resource.disableSaving();
		webMethod1.setName("changed");
		webMethod1.setExcluded(true);
		MyMethodSerializerAdapter adapter = new MyMethodSerializerAdapter(resource, webMethod1);
		IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation();
		assertNotNull(annotation);
		assertEquals(2, annotation.getParamValuePairs().size());
		assertEquals("changed", annotation.getPropertyValue(WMAnnotationFeatures.WM_NAME_ATTRIBUTE));
		assertEquals("true", annotation.getPropertyValue(WMAnnotationFeatures.WM_EXCLUDED_ATTRIBUTE));
	}

	public void testSaveAnnotation() throws Exception
	{
		webMethod1.setName("changedOperation");
		final IAnnotationInspector inspector = AnnotationFactory.createAnnotationInspector(seiType);
		final IAnnotation<IMethod> annotation = inspector.inspectMethod(method1, WM_ANNOTATION);
		assertNotNull(annotation);
		assertEquals("changedOperation", annotation.getPropertyValue(WMAnnotationFeatures.WM_NAME_ATTRIBUTE));		
	}
	
	public void testSaveAnnotationEmptyName() throws Exception
	{
		webMethod1.setName("method1");
		webMethod1.setName(null);
		final IAnnotationInspector inspector = AnnotationFactory.createAnnotationInspector(seiType);
		final IAnnotation<IMethod> annotation = inspector.inspectMethod(method1, WM_ANNOTATION);
		assertNotNull(annotation);
		assertEquals("method1", annotation.getPropertyValue(WMAnnotationFeatures.WM_NAME_ATTRIBUTE));			
	}
	
	protected class MyMethodSerializerAdapter extends MethodSerializerAdapter
	{
		public MyMethodSerializerAdapter(JaxWsWorkspaceResource resource, IWebMethod webMethod) {
			super(resource);
			setTarget(webMethod);
		}
		
		public IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException 
		{
			return super.getAnnotation();
		}
	}
}

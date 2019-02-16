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
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer.SeiSerializerAdapter;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;

/**
 * Tests for {@link SeiSerializerAdapter} class;
 * 
 * @author Georgi Vachkov
 */
public class SeiSerializerAdapterTest extends SerializerAdapterTestFixture 
{
	public void testCreateIAnnotationIServiceEndpointInterface() throws JavaModelException
	{
		try {
			new MySeiSerializerAdapter(resource).createIAnnotation((IServiceEndpointInterface)null, project.getJavaProject().getJavaModel());
			fail("NullPointerException not thrown");
		} catch (NullPointerException _) {}
		
		try {
			new MySeiSerializerAdapter(resource).createIAnnotation(sei, null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException _) {}
	}

	public void testCreateIAnnotationExplicitSei() throws JavaModelException 
	{
		final IAnnotation<? extends IJavaElement> annotation =  new MySeiSerializerAdapter(resource).createIAnnotation(sei, project.getJavaProject().getJavaModel());
		assertEquals(2, annotation.getParamValuePairs().size());
		assertEquals(TARGET_NAMESPACE, annotation.getPropertyValue(TARGET_NAMESPACE_ATTRIBUTE));
		assertEquals(PORT_TYPE_NAME, annotation.getPropertyValue(NAME_ATTRIBUTE));
		assertEquals(seiType.getFullyQualifiedName(), ((IType)annotation.getAppliedElement()).getFullyQualifiedName());
	}
	
	public void testCreateIAnnotationImplicitSei() throws JavaModelException 
	{
		final IAnnotation<? extends IJavaElement> annotation = new MySeiSerializerAdapter(resource).createIAnnotation(wsImplicit.getServiceEndpoint(), project.getJavaProject().getJavaModel());
		assertEquals(5, annotation.getParamValuePairs().size());
		WsSerializerAdapterTest.checkIWebServiceIAnnotation(annotation);
		assertEquals("ImplicitName", annotation.getPropertyValue(WSAnnotationFeatures.NAME_ATTRIBUTE));
		assertEquals(implBeanImplicit.getFullyQualifiedName(), ((IType)annotation.getAppliedElement()).getFullyQualifiedName());
	}	
	
	public void testCreateIAnnotationDefaultValues() throws JavaModelException
	{
		sei.setName("SEI");
		sei.setTargetNamespace("http://test/");
		final IAnnotation<? extends IJavaElement> annotation = new MySeiSerializerAdapter(resource).createIAnnotation(sei, project.getJavaProject().getJavaModel());
		assertEquals(0, annotation.getParamValuePairs().size());
	}
	
	public void testSaveAnnotation() throws JavaModelException
	{
		resource.getSerializerFactory().adapt(sei, IAnnotationSerializer.class);
		sei.setName("ChangedSeiName");
		
		IAnnotationInspector inspector = AnnotationFactory.createAnnotationInspector(seiType);
		IAnnotation<IType> found = inspector.inspectType(WS_ANNOTATION);
		assertNotNull(found);
		assertEquals("ChangedSeiName", found.getPropertyValue(NAME_ATTRIBUTE));
		assertEquals(TARGET_NAMESPACE, found.getPropertyValue(TARGET_NAMESPACE_ATTRIBUTE));
	}
	
	protected class MySeiSerializerAdapter extends SeiSerializerAdapter
	{
		public MySeiSerializerAdapter(JaxWsWorkspaceResource resource) {
			super(resource);
		}
		
		@Override
		protected IAnnotation<? extends IJavaElement> createIAnnotation(final IServiceEndpointInterface sei, final IJavaModel javaModel) throws JavaModelException 
		{
			return super.createIAnnotation(sei, javaModel);
		}
	}
}

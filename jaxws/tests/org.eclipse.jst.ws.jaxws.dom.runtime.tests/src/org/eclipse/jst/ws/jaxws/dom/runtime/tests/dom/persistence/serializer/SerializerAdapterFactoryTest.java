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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer.SerializerAdapterFactory;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;

/**
 * Tests for {@link WsSerializerAdapterFactory} class.
 * 
 * @author Georgi Vachkov
 */
public class SerializerAdapterFactoryTest extends MockObjectTestCase 
{
	private JaxWsWorkspaceResource resource;
	private SerializerAdapterFactory factory;
	
	@Override
	public void setUp()
	{
		Mock<IJavaModel> javaModelMock = mock(IJavaModel.class);
		resource = new JaxWsWorkspaceResource(javaModelMock.proxy());
		factory = resource.getSerializerFactory();
	}

	public void testIsFactoryForType()	
	{		
		assertTrue(factory.isFactoryForType(IAnnotationSerializer.class));
		assertFalse(factory.isFactoryForType(Notification.class));
	}

	public void testAdaptWebService()
	{
		IWebService webService = DomFactory.eINSTANCE.createIWebService();
		Adapter adapter1 = factory.adapt(webService, IAnnotationSerializer.class);
		assertEquals(1, webService.eAdapters().size());
		
		Adapter adapter2 = factory.adapt(webService, IAnnotationSerializer.class);
		assertEquals(1, webService.eAdapters().size());
		
		assertTrue(adapter1==adapter2);
	}
	
	public void testAdaptSei()
	{
		IServiceEndpointInterface sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		Adapter adapter1 = factory.adapt(sei, IAnnotationSerializer.class);
		assertEquals(1, sei.eAdapters().size());
		
		Adapter adapter2 = factory.adapt(sei, IAnnotationSerializer.class);
		assertEquals(1, sei.eAdapters().size());
		
		assertTrue(adapter1==adapter2);
	}	
}

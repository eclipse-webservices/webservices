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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.annotation;

import junit.framework.TestCase;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.AnnotationAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;

public class AnnotationAdapterFactoryTest extends TestCase 
{
	public void testIsFactoryForTypeObject() 
	{
		assertTrue(AnnotationAdapterFactory.INSTANCE.isFactoryForType(IAnnotationAdapter.class));
	}

	public void testCreateAdapter() 
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();
		AnnotationAdapterFactory.INSTANCE.adapt(ws, IAnnotationAdapter.class);
		Adapter adapter = ws.eAdapters().get(0);
		assertNotNull(adapter);
		assertTrue(adapter instanceof IAnnotationAdapter);
		
		AnnotationAdapterFactory.INSTANCE.adapt(ws, IAnnotationAdapter.class);
		assertEquals(1, ws.eAdapters().size());
	}
}

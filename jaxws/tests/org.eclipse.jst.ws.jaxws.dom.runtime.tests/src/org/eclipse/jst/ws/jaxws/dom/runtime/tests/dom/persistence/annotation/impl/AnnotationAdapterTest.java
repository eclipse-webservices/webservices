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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.annotation.impl;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.impl.AnnotationAdapter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.SBAnnotationFeatures.SB_ANNOTATION;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;


public class AnnotationAdapterTest extends AbstractLocatorsTest 
{

	private AnnotationAdapter adapter;
	
	@Override
	public void setUp()
	{
		super.setUp();
		adapter = new AnnotationAdapter();
	}

	public void testAddGetAnnotationLocations() 
	{
		adapter.addAnnotation(WS_ANNOTATION, annotation.proxy());
		annotation.stubs().method("getLocator").will(returnValue(createLocator(100, 110)));
		adapter.addAnnotation(SB_ANNOTATION, annotation.proxy());
		IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation(WS_ANNOTATION);
		assertNotNull("Annotation adapter not added", annotation);
		assertEquals(100, annotation.getLocator().getStartPosition());
		
		annotation = adapter.getAnnotation(SB_ANNOTATION);
		assertNotNull("Annotation adapter not added", annotation);
		assertEquals(100, annotation.getLocator().getStartPosition());
				
		adapter.addAnnotation(WS_ANNOTATION, null);
		annotation= adapter.getAnnotation(WS_ANNOTATION);
		assertNull("Annotation adapter not cleared", annotation);		
	}

	public void testIsAdapterForType() 
	{
		assertTrue("Adapter reports that it is not for ILocatorAdapter", adapter.isAdapterForType(IAnnotationAdapter.class));
	}

}

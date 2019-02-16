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
package org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.ClassValueImpl;

public class ClassValueImplTest extends ClassLoadingTest
{
	private static final String SRC = "src";

	private static final String PCK = "org.eclipse.demo";

	private IType endpoint;

	public void setUp() throws CoreException, IOException, AnnotationGeneratorException
	{
		createJavaProject(SRC, PCK);
		endpoint = createClass("Endpoint.src", "Endpoint");
		AnnotationFactory.removeAnnotationsFromJavaElement(endpoint);
	}

	public void testIntegers() throws Exception
	{
		setUp();
		
		ClassValueImpl cv = new ClassValueImpl(endpoint.getFullyQualifiedName());
				
		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("param", cv));
		
		IAnnotation<IType> ann = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation", pv, endpoint);
		AnnotationWriter.getInstance().setAppliedElement(ann, endpoint);
		Collection<IAnnotation<IType>> annotattions = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		
		assertNotNull(annotattions);
		assertTrue(annotattions.size() == 1);
		IAnnotation<IType> readAnnotation = annotattions.iterator().next();
		readAnnotation.equals(ann);
		assertEquals(ann.getPropertyValue("param").toString(), endpoint.getFullyQualifiedName());
		AnnotationWriter.getInstance().remove(ann);
		annotattions = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		
		assertNotNull(annotattions);
		assertTrue(annotattions.size() == 0);
	}
	
	public void testEqualsObject()
	{
		ClassValueImpl cv1 = new ClassValueImpl(endpoint.getFullyQualifiedName());
		ClassValueImpl cv2 = new ClassValueImpl(null);
		ClassValueImpl cv3 = new ClassValueImpl(endpoint.getFullyQualifiedName());

		assertFalse(cv1.equals(null));
		assertFalse(cv1.equals(true));
		assertFalse(cv1.equals(cv2));

		assertTrue(cv1.equals(cv3));
		assertTrue(cv1.equals(cv1));
	}

}

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
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.ArrayValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.BooleanValueImpl;

public class BooleanValueImplTest extends ClassLoadingTest
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


	public void testBooleanValueImpl()
	{
		try
		{
			new ArrayValueImpl(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException e)
		{
			assertTrue(true);
		}
	}

	public void testBooleans() throws Exception
	{
		setUp();
		
		BooleanValueImpl bv = new BooleanValueImpl(true);
				
		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("param", bv));
		
		IAnnotation<IType> ann = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation", pv, endpoint);
		AnnotationWriter.getInstance().setAppliedElement(ann, endpoint);
		Collection<IAnnotation<IType>> annotattions = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		
		assertNotNull(annotattions);
		assertTrue(annotattions.size() == 1);
		IAnnotation<IType> readAnnotation = annotattions.iterator().next();
		readAnnotation.equals(ann);
		AnnotationWriter.getInstance().remove(ann);
		annotattions = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		
		assertNotNull(annotattions);
		assertTrue(annotattions.size() == 0);
	}
	
	public void testEqualsObject()
	{
		BooleanValueImpl bv1 = new BooleanValueImpl(true);
		BooleanValueImpl bv2 = new BooleanValueImpl(false);
		BooleanValueImpl bv3 = new BooleanValueImpl(true);

		assertFalse(bv1.equals(null));
		assertFalse(bv1.equals(123));
		assertFalse(bv1.equals(bv2));

		assertTrue(bv1.equals(bv3));
		assertTrue(bv1.equals(bv1));
	}

}

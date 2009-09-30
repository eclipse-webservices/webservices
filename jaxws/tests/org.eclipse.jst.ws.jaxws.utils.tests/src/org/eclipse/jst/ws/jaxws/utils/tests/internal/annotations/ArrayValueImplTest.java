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
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.ArrayValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.StringValueImpl;

public class ArrayValueImplTest extends ClassLoadingTest
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

	public void testHashCode()
	{
		Set<IValue> values1 = new HashSet<IValue>();
		values1.clear();
		values1.add(new StringValueImpl("val1"));
		ArrayValueImpl av1 = new ArrayValueImpl(values1);
		
		Set<IValue> values2 = new HashSet<IValue>();
		values2.clear();
		values2.add(new StringValueImpl("val2"));
		ArrayValueImpl av2 = new ArrayValueImpl(values2);

		assertTrue(av1.hashCode() != av2.hashCode());
	}

	public void testArrayValueImpl()
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

	public void testEqualsObject()
	{
		Set<IValue> values1 = new HashSet<IValue>();
		values1.clear();
		values1.add(new StringValueImpl("val1"));
		ArrayValueImpl av1 = new ArrayValueImpl(values1);

		Set<IValue> values2 = new HashSet<IValue>();
		values2.clear();
		values2.add(new StringValueImpl("val2"));
		ArrayValueImpl av2 = new ArrayValueImpl(values2);

		Set<IValue> values3 = new HashSet<IValue>();
		values3.clear();
		values3.add(new StringValueImpl("val1"));
		ArrayValueImpl av3 = new ArrayValueImpl(values3);

		assertFalse(av1.equals(null));
		assertFalse(av1.equals(123));
		assertFalse(av1.equals(av2));

		assertTrue(av1.equals(av3));
		assertTrue(av1.equals(av1));
	}
	
	public void testArrays() throws Exception
	{
		setUp();
		
		Set<IValue> values = new HashSet<IValue>();
		values.clear();
		values.add(new StringValueImpl("val1"));
		values.add(new StringValueImpl("val2"));
		ArrayValueImpl av = new ArrayValueImpl(values);
				
		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("param", av));
		
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
}

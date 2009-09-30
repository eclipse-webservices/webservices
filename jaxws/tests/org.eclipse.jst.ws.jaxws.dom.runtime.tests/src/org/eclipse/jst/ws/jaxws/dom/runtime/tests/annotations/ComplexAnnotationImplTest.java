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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

public class ComplexAnnotationImplTest extends MockObjectTestCase
{	
	private Mock<IType> typeElement = mock(IType.class);
	
	public void testHashCode()
	{
		IValue value = AnnotationFactory.createStringValue("value");
		IParamValuePair pvp = AnnotationFactory.createParamValuePairValue("param", value);
		Set<IParamValuePair> paramValues = new HashSet<IParamValuePair>();
		paramValues.add(pvp);
		IAnnotation<IType> annotation1 = AnnotationFactory.createAnnotation("annotationName", paramValues, typeElement.proxy());
		IAnnotation<IType> annotation2 = AnnotationFactory.createAnnotation("annotationName2", paramValues,typeElement.proxy());

		assertTrue(annotation1.hashCode() != annotation2.hashCode());
	}

	public void testEqualsObject()
	{
		IValue value = AnnotationFactory.createStringValue("value");
		IParamValuePair pvp = AnnotationFactory.createParamValuePairValue("param", value);
		Set<IParamValuePair> paramValues = new HashSet<IParamValuePair>();
		paramValues.add(pvp);
		IAnnotation<IType> annotation1 = AnnotationFactory.createAnnotation("annotationName", paramValues, typeElement.proxy());
		IAnnotation<IType> annotation2 = AnnotationFactory.createAnnotation("annotationName2", paramValues, typeElement.proxy());
		IAnnotation<IType> annotation3 = AnnotationFactory.createAnnotation("annotationName", paramValues, typeElement.proxy());

		assertFalse(annotation1.equals(annotation2));
		assertFalse(annotation1.equals(null));
		assertFalse(annotation1.equals(123));
		assertTrue(annotation1.equals(annotation3));
	}
}

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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

public class AnnotationUtilsTest extends ClassLoadingTest
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

	public void testRemove() throws Exception
	{
		setUp();
		
		IValue sv = AnnotationFactory.createStringValue("My String Value");

		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("Param", sv));
		
		IAnnotation<IField> ann = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation", pv, endpoint.getField("field1"));
		AnnotationWriter.getInstance().setAppliedElement(ann, endpoint.getField("field1"));
		assertEquals(ann.getAppliedElement(), endpoint.getField("field1"));
		
		AnnotationWriter.getInstance().remove(ann);
	}
}

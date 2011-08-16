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
package org.eclipse.jst.ws.jaxws.utils.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.jaxws.utils.resources.StringInputStreamAdapterTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.FileUtilsUnitTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.JaxWsUtilsTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.AnnotationFactoryTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.AnnotationInspectorImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.AnnotationPropertyContainerTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.AnnotationUtilsTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.AnnotationsBaseImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.ArrayValueImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.BooleanValueImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.ClassValueImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.IntegerValueImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.ParamValuePairImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.QualifiedNameValueImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations.StringValueImplTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.clazz.ASTUtilsTest;
import org.eclipse.jst.ws.jaxws.utils.tests.internal.operation.OperationInJobRunnerTest;


public class AllTestsSuite
{
	/**
	 * Returns a test suite containing the tests to be run by Java Developer Test Dispatcher
	 * 
	 * @return - a suite of junit pde test
	 */
	public static Test suite()
	{
		final TestSuite suite = new TestSuite();

		suite.addTestSuite(AnnotationFactoryTest.class);
		suite.addTestSuite(ArrayValueImplTest.class);
		suite.addTestSuite(AnnotationsBaseImplTest.class);
		suite.addTestSuite(AnnotationInspectorImplTest.class);
		suite.addTestSuite(AnnotationUtilsTest.class);
		suite.addTestSuite(BooleanValueImplTest.class);
		suite.addTestSuite(IntegerValueImplTest.class);
		suite.addTestSuite(ParamValuePairImplTest.class);
		suite.addTestSuite(QualifiedNameValueImplTest.class);
		suite.addTestSuite(StringValueImplTest.class);
		suite.addTestSuite(ClassValueImplTest.class);
		suite.addTestSuite(AnnotationPropertyContainerTest.class);
		suite.addTestSuite(ASTUtilsTest.class);
		suite.addTestSuite(FileUtilsUnitTest.class);
		suite.addTestSuite(JaxWsUtilsTest.class);
		suite.addTestSuite(StringInputStreamAdapterTest.class);
		suite.addTestSuite(OperationInJobRunnerTest.class);
		
		return suite;
	}
}

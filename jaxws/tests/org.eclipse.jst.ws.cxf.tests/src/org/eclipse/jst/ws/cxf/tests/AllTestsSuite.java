/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.cxf.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.jaxb.core.tests.JAXBCoreTestSuite;
import org.eclipse.jst.ws.jaxws.core.annotation.validation.tests.JAXWSAnnotationValidationTestSuite;
import org.eclipse.jst.ws.jaxws.core.tests.JAXWSCoreTestSuite;

/**
 * This class specifies all the bundles of this component that provide a test
 * suite to run during automated testing.
 */
public class AllTestsSuite extends TestSuite {

	public static Test suite() {
		return new AllTestsSuite();
	}

	public AllTestsSuite() {
		super("All JAXWS and JAXB Test Suites");
		addTest(JAXWSCoreTestSuite.suite());
		addTest(JAXWSAnnotationValidationTestSuite.suite());
		addTest(JAXBCoreTestSuite.suite());

		//Adding JAX-WS DOM Tools test suites
		//addTest(org.eclipse.jst.ws.jaxws.dom.integration.tests.dom.AllTestsSuite.suite());
		addTest(org.eclipse.jst.ws.jaxws.dom.runtime.tests.AllTestsSuite.suite());
		//addTest(org.eclipse.jst.ws.jaxws.dom.ui.tests.AllTestsSuite.suite());
		//addTest(org.eclipse.jst.ws.jaxws.utils.tests.AllTestsSuite.suite());
	}

	/**
	 * This is just need to run in a development environment workbench.
	 */
	public void testAll() {
		// this method needs to exist, but doesn't really do anything
		// other than to signal to create an instance of this class.
		// The rest it automatic from the tests added in constructor.

	}
}

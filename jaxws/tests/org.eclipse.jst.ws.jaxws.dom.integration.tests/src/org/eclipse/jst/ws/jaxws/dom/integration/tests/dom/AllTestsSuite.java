/*******************************************************************************
 * Copyright (c) 2009, 2013 by SAP AG, Walldorf. 
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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.dom;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.DOMAdapterFactoryContentProviderTest;
import org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.DOMAdapterFactoryLabelProviderTest;
import org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.DOMPropertyViewAdapterFactoryTest;
import org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.WebServiceDecoratorTest;
import org.eclipse.jst.ws.jaxws.dom.integration.tests.util.CommonNavigatorFinderTest;
import org.eclipse.jst.ws.jaxws.dom.integration.tests.util.LoadingWsProjectNodesCollectorTest;

public class AllTestsSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTestSuite(DOMAdapterFactoryContentProviderTest.class);
		suite.addTestSuite(DOMAdapterFactoryLabelProviderTest.class);
		suite.addTestSuite(DOMPropertyViewAdapterFactoryTest.class);
		suite.addTestSuite(WebServiceDecoratorTest.class);
		suite.addTestSuite(LoadingWsProjectNodesCollectorTest.class);
		suite.addTestSuite(CommonNavigatorFinderTest.class);
		
		return suite;
	}
}
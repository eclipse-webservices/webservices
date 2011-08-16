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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.dom;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.DOMAdapterFactoryContentProviderTest;
import org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.DOMAdapterFactoryLabelProviderTest;
import org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.DOMPropertyViewAdapterFactoryTest;
import org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.WebServiceDecoratorTest;
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
		
		return suite;
	}
}
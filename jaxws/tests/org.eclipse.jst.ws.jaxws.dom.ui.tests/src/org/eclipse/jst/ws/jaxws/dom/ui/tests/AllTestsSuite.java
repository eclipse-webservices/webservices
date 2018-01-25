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
package org.eclipse.jst.ws.jaxws.dom.ui.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.jaxws.dom.ui.tests.internal.AllDomItemProviderTest;
import org.eclipse.jst.ws.jaxws.dom.ui.tests.internal.DOMItemPropertyProviderTest;

public class AllTestsSuite
{
	public static Test suite()
	{
		final TestSuite suite = new TestSuite();

		suite.addTestSuite(AllDomItemProviderTest.class);
		suite.addTestSuite(DOMItemPropertyProviderTest.class);
		
		return suite;
	}
}

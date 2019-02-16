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
package org.eclipse.jst.ws.jaxws.testutils.jmock.testcases;

import junit.framework.TestCase;

public abstract class BeforeAfterTestCase extends TestCase implements IBeforeAfterEnabled {
	
	private final BeforeAfterEnabler bfe = new BeforeAfterEnabler(this);
	
	public void runBareInternal() throws Throwable {
		super.runBare();
	}

	@Override
	public void runBare() throws Throwable {
		bfe.runBare();
	}
}

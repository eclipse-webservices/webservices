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
package org.eclipse.jst.ws.jaxws.testutils.jmock.testcases;

public interface IBeforeAfterEnabled {

	public void beforeTestCase() throws Exception;
	
	public void afterTestCase() throws Exception;
	
	public void runBareInternal() throws Throwable;
	
	
}

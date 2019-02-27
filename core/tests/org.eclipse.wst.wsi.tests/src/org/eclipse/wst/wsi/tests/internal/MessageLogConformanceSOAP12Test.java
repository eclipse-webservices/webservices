/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsi.tests.internal;


/**
 * JUnit tests that test message logs against the WS-I Attachments profile.
 */
public class MessageLogConformanceSOAP12Test extends CoreMessageLogConformanceTest {

	public static final String DEFAULT_LOG_FILENAME = "log.wsimsg";
	
    public MessageLogConformanceSOAP12Test(String name) {
        super(name);
    }

    public void test_SOAP() { runTestWithWSDL("SOAP12", "SimpleLog", new String[] {"Hello", "http://test/", "HelloService", "port"}); } 

   
}

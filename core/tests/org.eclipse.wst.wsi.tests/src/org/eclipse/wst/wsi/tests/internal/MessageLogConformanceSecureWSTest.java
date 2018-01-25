/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsi.tests.internal;


/**
 * JUnit tests that test message logs against the WS-I Attachments profile.
 */
public class MessageLogConformanceSecureWSTest extends CoreMessageLogConformanceTest {

	public static final String DEFAULT_LOG_FILENAME = "log.wsimsg";
	
    public MessageLogConformanceSecureWSTest(String name) {
        super(name);
    }

    public void test_SecureWS_NoSecuriy() { runTestWithWSDL("SecureWS", "NoSecurity", new String[] {"WebService", "http://main", "WebServiceService", "port"}); } 

    public void test_SecureWS_Signature() { runTestWithWSDL("SecureWS", "Signature", new String[] {"WebService", "http://main", "WebServiceService", "port"}); } 
    
    public void test_SecureWS_Encryption() { runTestWithWSDL("SecureWS", "Encryption", new String[] {"WebService", "http://main", "WebServiceService", "port"}); }
       
    public void test_SecureWS_SignatureAndEncryption() { runTestWithWSDL("SecureWS", "SignatureAndEncryption", new String[] {"WebService", "http://main", "WebServiceService", "port"}); } 
    



}

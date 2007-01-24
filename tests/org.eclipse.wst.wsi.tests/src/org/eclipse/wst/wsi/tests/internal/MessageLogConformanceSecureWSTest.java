package org.eclipse.wst.wsi.tests.internal;

import org.eclipse.wst.wsi.internal.analyzer.MessageAnalyzer;



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

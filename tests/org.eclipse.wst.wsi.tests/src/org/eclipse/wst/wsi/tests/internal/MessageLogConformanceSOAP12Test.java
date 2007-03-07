package org.eclipse.wst.wsi.tests.internal;

import org.eclipse.wst.wsi.internal.analyzer.MessageAnalyzer;



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

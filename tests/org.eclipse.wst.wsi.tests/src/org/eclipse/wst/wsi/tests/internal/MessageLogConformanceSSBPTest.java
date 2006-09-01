package org.eclipse.wst.wsi.tests.internal;

/**
 * JUnit tests that test message logs against the WS-I Simple Soap Binding
 * profile.
 */
public class MessageLogConformanceSSBPTest extends
        CoreMessageLogConformanceTest {

    public MessageLogConformanceSSBPTest(String name) {
        super(name);
    }
    
    public void test_1309_1() { runTest("bp", "1309-1", TAD_ID_SSBP); } 
    public void test_1309_2() { runTest("bp", "1309-2", TAD_ID_SSBP); } 
}

package org.eclipse.wst.wsi.tests.internal;

/**
 * JUnit tests that test message logs against the WS-I Attachments profile.
 */
public class MessageLogConformanceAPTest extends CoreMessageLogConformanceTest {

    public MessageLogConformanceAPTest(String name) {
        super(name);
    }

    public void test_1309_1() { runTest("bp", "1309-1", TAD_ID_AP); } 
    public void test_1309_2() { runTest("bp", "1309-2", TAD_ID_AP); } 
    public void test_1600_1() { runTest("bp", "1600-1", TAD_ID_AP); }
}

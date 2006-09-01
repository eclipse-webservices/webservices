package org.eclipse.wst.wsi.tests.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.wst.wsi.internal.WSITestToolsProperties;
import org.eclipse.wst.wsi.internal.report.AssertionError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

public class CoreConformanceTest extends TestCase {

    public static final String TEST_CASE_FILE = "testcase.xml";
    public static final String TAD_ID_AP = "AP10";
    public static final String TAD_ID_SSBP = "SSBP10";

    protected String pluginURI = null;
    protected String tadID = TAD_ID_AP;

    public CoreConformanceTest(String name) {
        super(name);
        pluginURI = WSITestsPlugin.getInstallURL();
    }

    protected void setup()
    {
    }

    /**
     * Retrieve the expected assertion failures.
     * @param filename - the location of the testcase.xml file.
     * @return the expected assertion failures.
     */
    protected List getExpectedAssertionFailures(String filename) {
        List assertionFailures = new ArrayList();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder();
            Document doc = builder.parse(filename);
            NodeList list = doc.getElementsByTagName("assertion");
            if (list != null)
                for (int i=0; i<list.getLength(); i++) {
                    Element element = (Element)list.item(i);
                    String tad = element.getAttribute("tadID");
                    if ((tad != null) && tad.equals(this.tadID))
                        assertionFailures.add(element.getAttribute("id"));
                }
        } catch (Exception e) {
            assertionFailures = null;
        }
        return assertionFailures;
    }

    /**
     * Compare the actual errors with the expected errors.
     * @param errors the actual assertion failures
     * @param expectedErrors the expected assertion failures
     */
    protected void analyzeResults(List errors, List expectedErrors) {
        List actualErrors = new ArrayList();
        if ((errors != null) && (expectedErrors != null)) {
            Iterator i = errors.iterator();
            while (i.hasNext()) {
                AssertionError e = (AssertionError)i.next();
                String assertionId = e.getAssertionID();
                actualErrors.add(assertionId);

                if (!expectedErrors.contains(assertionId)) {
                    fail("Unexpected assertion failure: " + assertionId); 
                }
            }

            i = expectedErrors.iterator();
            while (i.hasNext()) {
                String assertionId = (String)i.next();

                if (!actualErrors.contains(assertionId)) {
                    fail("Expected assertion failure: " + assertionId); 
                }
            }
            assertEquals(expectedErrors.size(), errors.size());
        }
    }

    /**
     * Return the location of the given tad.
     * @param tid the tad id.
     * @return the location of the tad.
     */
    protected String getTADURI(String tid)
    {
        if (tid.equals(TAD_ID_AP))
            return WSITestToolsProperties.AP_ASSERTION_FILE;
        else
            return WSITestToolsProperties.SSBP_ASSERTION_FILE;
    }
}

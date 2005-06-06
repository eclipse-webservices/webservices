package org.eclipse.wst.wsdl.tests.performance;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.wsdl.WSDLException;
import junit.framework.Assert;
import org.eclipse.test.performance.Dimension;
import org.eclipse.test.performance.PerformanceTestCase;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLReaderImpl;

public class ReadOAGISWSDLTestcase extends PerformanceTestCase
{
  private List defs = new ArrayList();

  public void testReadWSDL() throws MalformedURLException, WSDLException
  {
    String oagis80Dir = System.getProperty("oagis80Dir");
    Assert.assertNotNull(oagis80Dir);
    if (!oagis80Dir.endsWith("/") && !oagis80Dir.endsWith("\\"))
      oagis80Dir = oagis80Dir + "/";
    File dir = new File(oagis80Dir + "OAGIS8.0/ws/wsdl");
    if (dir.exists() && dir.isDirectory())
    {
      File[] wsdls = dir.listFiles
      (
        new FileFilter()
        {
          public boolean accept(File pathname)
          {
            return pathname.getName().endsWith(".wsdl");
          }
        }
      );
      tagAsSummary("Read OAGIS WSDL", new Dimension[] {Dimension.ELAPSED_PROCESS, Dimension.WORKING_SET});
      startMeasuring();
      for (int i = 0; i < wsdls.length; i++)
        readWSDL(wsdls[i].toURL().toString());
      stopMeasuring();
      commitMeasurements();
      assertPerformance();
    }
    else
      fail(dir.toString());
  }

  private void readWSDL(String location) throws WSDLException
  {
    WSDLReaderImpl reader = new WSDLReaderImpl();
    defs.add(reader.readWSDL(location));
  }
}
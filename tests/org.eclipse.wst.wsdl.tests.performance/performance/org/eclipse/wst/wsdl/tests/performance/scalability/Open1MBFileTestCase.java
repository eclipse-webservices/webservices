package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.wst.common.tests.performance.internal.scalability.OpenEditorTestCase;

public class Open1MBFileTestCase extends OpenEditorTestCase
{
  protected String getEditorId()
  {
    return "org.eclipse.wst.wsdl.ui.internal.WSDLEditor";
  }

  protected String getBundleId()
  {
    return "org.eclipse.wst.wsdl.tests.performance";
  }

  protected String getFilePath()
  {
    return "data/1MB.wsdl";
  }

  public void testOpen1MBFile()
  {
    try
    {
      super.execute();
    }
    catch (Throwable t)
    {
      fail(t.getMessage());
    }
  }
}
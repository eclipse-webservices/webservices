package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.wst.common.tests.performance.internal.scalability.OpenEditorTestCase;

public class Open1KBFileTestCase extends OpenEditorTestCase
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
    return "data/1KB.wsdl";
  }

  public void testOpen1KBFile()
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
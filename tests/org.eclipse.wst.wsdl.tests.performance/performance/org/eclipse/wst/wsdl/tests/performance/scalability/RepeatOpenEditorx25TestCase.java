package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.wst.common.tests.performance.internal.scalability.RepeatOpenEditorTestCase;

public class RepeatOpenEditorx25TestCase extends RepeatOpenEditorTestCase
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
    return "data/100KB.wsdl";
  }

  protected int getRepeatCount()
  {
    return 25;
  }

  public void testOpenx25()
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
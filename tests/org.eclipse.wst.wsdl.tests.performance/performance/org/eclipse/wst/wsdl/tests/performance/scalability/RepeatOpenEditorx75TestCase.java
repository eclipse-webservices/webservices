package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.wst.common.tests.performance.internal.scalability.RepeatOpenEditorTestCase;

public class RepeatOpenEditorx75TestCase extends RepeatOpenEditorTestCase
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
    return 75;
  }

  public void testOpenx75()
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
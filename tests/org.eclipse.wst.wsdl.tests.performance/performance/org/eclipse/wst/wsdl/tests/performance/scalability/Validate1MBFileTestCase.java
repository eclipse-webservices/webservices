package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.wst.common.tests.performance.internal.scalability.RunValidatorTestCase;

public class Validate1MBFileTestCase extends RunValidatorTestCase
{
  protected String getValidatorId()
  {
    return "org.eclipse.wst.wsdl.ui.internal.validation.Validator";
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
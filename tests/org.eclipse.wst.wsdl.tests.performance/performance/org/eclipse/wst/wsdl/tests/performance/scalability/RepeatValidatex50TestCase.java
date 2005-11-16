package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.wst.common.tests.performance.internal.scalability.RepeatRunValidatorTestCase;

public class RepeatValidatex50TestCase extends RepeatRunValidatorTestCase
{
  protected int getRepeatCount()
  {
    return 50;
  }

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
    return "data/100KB.wsdl";
  }

  public void testValidatex50()
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
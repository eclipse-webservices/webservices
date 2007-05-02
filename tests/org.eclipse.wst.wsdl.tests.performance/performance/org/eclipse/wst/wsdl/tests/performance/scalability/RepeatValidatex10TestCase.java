/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance.scalability;

public class RepeatValidatex10TestCase extends RepeatRunWSDLValidatorTestCase
{
  protected int getRepeatCount()
  {
    return 10;
  }

  public void testValidatex10()
  {
    try
    {
      super.execute();
    }
    catch (Exception t)
    {
      fail(t.getMessage());
    }
  }
}

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

public class RepeatOpenEditorx75TestCase extends RepeatOpenWSDLEditorTestCase
{
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
    catch (Exception t)
    {
      fail(t.getMessage());
    }
  }
}

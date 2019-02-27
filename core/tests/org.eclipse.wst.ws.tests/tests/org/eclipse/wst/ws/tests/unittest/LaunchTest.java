/*******************************************************************************
 * Copyright (c) 2005, 2019 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.unittest;

import org.eclipse.core.runtime.IPlatformRunnable;

/**
 * Minimal app to run as Eclipse "application"
 */
public class LaunchTest implements IPlatformRunnable
{
  /**
   * @see org.eclipse.core.runtime.IPlatformRunnable#run(Object)
   */
  public Object run(Object args) throws Exception
  {

    Object result = new AllWSJUnitTests().runMain(args);

    if (result != null)
    {
      if (result instanceof Throwable)
      {
        ((Throwable) result).printStackTrace();
      }
      else
      {
        System.out.println("tests didn't return 'ok'");
      }
      return result;
    }
    else
      return IPlatformRunnable.EXIT_OK;
  }
}

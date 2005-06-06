package org.eclipse.jst.ws.tests.unittest;

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

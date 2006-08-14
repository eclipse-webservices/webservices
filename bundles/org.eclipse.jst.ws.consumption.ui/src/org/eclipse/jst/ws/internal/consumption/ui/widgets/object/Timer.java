/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060803   152486 makandre@ca.ibm.com - Andrew Mak, Typing WSDL in Service definition field is very slow
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.util.Calendar;
import org.eclipse.swt.widgets.Display;

public class Timer extends Thread
{
  private final long ONE_SECOND = 1000;
  private long refreshTime;
  private boolean isRunning = true;
  private Display display;
  private Runnable runnable;

  private Timer(Display display, Runnable runnable)
  {
    this.display = display;
    this.runnable = runnable;
  }

  public synchronized static Timer newInstance(Timer timer, Display display, Runnable runnable)
  {
    if (!Timer.isRunning(timer))
      return new Timer(display, runnable);
    
    return timer;
  }
  
  public synchronized static boolean isRunning(Timer timer)
  {
    return timer != null && timer.isRunning;
  }

  public synchronized void startTimer()
  {
    refreshTime = getCurrentTime() + ONE_SECOND;
    if (!isAlive())
      this.start();
  }

  private long getCurrentTime()
  {
    return Calendar.getInstance().getTime().getTime();
  }

  public void run()
  {
    long currTime = getCurrentTime();
    while (currTime < refreshTime)
    {
      try
      {
        sleep(refreshTime - currTime);
      }
      catch (InterruptedException ie)
      {
      }
      currTime = getCurrentTime();
    }
    isRunning = false;
    display.syncExec(runnable);
  }
}
package org.eclipse.jst.ws.tests.performance.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;


public class EclipsePerformanceLog implements Log {

  private boolean runState = false;
  Performance perf = null;      
  PerformanceMeter performanceMeter= null;  
  
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isEnabled(String option) {
    // TODO Auto-generated method stub
    return false;
  }
  
  public void log(int severity, int messageNum, Object caller, String method, Throwable throwable) {
    // TODO Auto-generated method stub

  }

  public void log(int severity, String option, int messageNum, Object caller, String method, Throwable throwable) {
    // TODO Auto-generated method stub

  }

  public void log(int severity, int messageNum, Object caller, String method, IStatus status) {
    // TODO Auto-generated method stub

  }

  public void log(int severity, String option, int messageNum, Object caller, String method, IStatus status) {
    // TODO Auto-generated method stub

  }

  public void log(int severity, int messageNum, Object caller, String method, Object object) {
    // TODO Auto-generated method stub

  }

  public void log(int severity, String option, int messageNum, Object caller, String method, Object object) {
    
    if (method.equals("runCommand")) {

      try {
        if (!runState) {
          // begin performance recording
          perf = Performance.getDefault();      
          performanceMeter = perf.createPerformanceMeter(((String)object).toString());          
          performanceMeter.start();
          runState = true;
        }
        else {
          // end performance recording
          performanceMeter.stop();
          performanceMeter.commit();
          perf.assertPerformance(performanceMeter);
          performanceMeter.dispose();
          runState = false;
        }
      }
      catch(Exception e) {
        // handle exception
      }        
    }
  }

}

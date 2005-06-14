/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.util;

import java.util.Vector;

import org.eclipse.wst.command.internal.provisional.env.core.common.Choice;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusException;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler;

public class AccumulateStatusHandler implements StatusHandler {
	  Vector statusList_;
	  
	  public AccumulateStatusHandler()
	  {
	    resetStatus();    
	  }
	  
	  /**
	   *  Resets the status so that it starts out empty again. 
	   *
	   */
	  public void resetStatus()
	  {
	    statusList_ = new Vector();      
	  }
	  
	  public Status getStatus()
	  {
	    Status worstStatus = new SimpleStatus( "", "", Status.OK );
	    
	    // Find the worst error status code
	    for( int index = 0; index < statusList_.size(); index++ )
	    {
	      Status status = (Status)statusList_.elementAt( index );
	      
	      if( status.getSeverity() > worstStatus.getSeverity() )
	      {
	        worstStatus = status;
	      }
	    }
	    
	    return new SimpleStatus( worstStatus.getId(), 
	                             worstStatus.getMessage(),
	                             (Status[])statusList_.toArray( new Status[0] ) );
	  }
	  
	  /**
	   * @see org.eclipse.env.common.StatusHandler#report(org.eclipse.env.common.Status, org.eclipse.env.common.Choice[])
	   */
	  public Choice report(Status status, Choice[] choices) 
	  {
	  	Choice result = null;
	  	
	  	// Always take the first choice if available.
	    if( choices != null && choices.length > 0 )
	    {
	    	result = choices[0];
	    }
	    
	    statusList_.add( status );
	    
	    return result;
	  }

	  /**
	   * @see org.eclipse.env.common.StatusHandler#report(org.eclipse.env.common.Status)
	   */
	  public void report(Status status) throws StatusException
	  {
	    statusList_.add( status );
	  }
	  
	  /*
	   * Report a warning.
	   */
	  private boolean reportWarning(Status status)
	  {
	    statusList_.add( status );
	    
	    return true;
	  }
	 
	  /**
	   * @see com.ibm.env.common.StatusHandler#reportError(com.ibm.env.common.Status)
	   */
	  public void reportError(Status status)
	  {
	    statusList_.add( status );
	  }
	  
	  /**
	   * @see com.ibm.env.common.StatusHandler#reportInfo(com.ibm.env.common.Status)
	   */
	  public void reportInfo(Status status)
	  {
	    statusList_.add( status );
	  }

}

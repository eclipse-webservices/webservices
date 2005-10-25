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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.environment.IStatusHandler;

public class AccumulateStatusHandler implements IStatusHandler {
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
	  
	  public IStatus getStatus()
	  {
	    IStatus worstStatus = Status.OK_STATUS;
	    
	    // Find the worst error status code
	    for( int index = 0; index < statusList_.size(); index++ )
	    {
	      IStatus status = (IStatus)statusList_.elementAt( index );
	      
	      if( status.getSeverity() > worstStatus.getSeverity() )
	      {
	        worstStatus = status;
	      }
	    }
	    
	    return StatusUtils.multiStatus( worstStatus.getMessage(),
                                     (Status[])statusList_.toArray( new Status[0] ) );
	  }
	  
	  /**
	   * @see org.eclipse.env.common.IStatusHandler#report(org.eclipse.env.common.Status, org.eclipse.env.common.Choice[])
	   */
	  public Choice report(IStatus status, Choice[] choices) 
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
	   * @see org.eclipse.env.common.IStatusHandler#report(org.eclipse.env.common.Status)
	   */
	  public void report(IStatus status) throws StatusException
	  {
	    statusList_.add( status );
	  }
	  	 
	  /**
	   * @see com.ibm.env.common.IStatusHandler#reportError(com.ibm.env.common.Status)
	   */
	  public void reportError(IStatus status)
	  {
	    statusList_.add( status );
	  }
	  
	  /**
	   * @see com.ibm.env.common.IStatusHandler#reportInfo(com.ibm.env.common.Status)
	   */
	  public void reportInfo(IStatus status)
	  {
	    statusList_.add( status );
	  }

}

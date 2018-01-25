/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070510   180567 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.eclipse;

import java.util.ArrayList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.StatusException;

public class AccumulateStatusHandler extends BaseStatusHandler{

	ArrayList statusList_ = null;
	ArrayList errorList_ = null;
	ArrayList warningList_ = null;
	
	/**
	 * Constructor
	 */
	public AccumulateStatusHandler(){
		resetStatus();
	}
	
	/**
	 * Constructor for reloading status values and using the utility methods
	 */
	public AccumulateStatusHandler(IStatus[] status){
		resetStatus();
		for (int i=0;i<status.length;i++)
			statusList_.add(status[i]);
	}
	
	public IStatus getStatus() {
	    IStatus worstStatus = Status.OK_STATUS;
	    
	    // Find the worst error status code
	    for( int index = 0; index < statusList_.size(); index++ )
	    {
	      IStatus status = (IStatus)statusList_.get( index );
	      
	      if( status.getSeverity() > worstStatus.getSeverity() )
	      {
	        worstStatus = status;
	      }
	    }
	    
	    return worstStatus;
	}

	public void resetStatus() {
	    statusList_ = new ArrayList();
	    errorList_ = null;
	    warningList_ = null;
	}

	public void report(IStatus status) throws StatusException {
		statusList_.add(status);
	}

	public Choice report(IStatus status, Choice[] choices) {
	  	Choice result = null;
	  	
	  	// Always take the first choice if available.
	    if( choices != null && choices.length > 0 )
	    {
	    	result = choices[0];
	    }
	    
	    statusList_.add( status );
	    
	    return result;
	}

	public void reportError(IStatus status) {
		statusList_.add(status);
	}

	public void reportInfo(IStatus status) {
		statusList_.add(status);
	}
	
	/**
	 * For use by assertion methods
	 * @return IStatus[]
	 */
	public IStatus[] getAllReports(){
		if (statusList_==null || statusList_.isEmpty()){
			return new IStatus[]{Status.OK_STATUS};
		}
		return (IStatus[])statusList_.toArray(new IStatus[0]);
	}
	
	/**
	 * Get error statuses only
	 * @return IStatus[]
	 */
	public IStatus[] getErrorReports(){
		if (errorList_==null)
			sortReports();
	    return (IStatus[])errorList_.toArray(new IStatus[0]);
	}
	
	/**
	 * Get warning statuses only
	 * @return IStatus[]
	 */
	public IStatus[] getWarningReports(){
		if (warningList_==null)
			sortReports();
		return (IStatus[])warningList_.toArray(new IStatus[0]);
	}
	
	/**
	 * Sort all status reports
	 */
	private void sortReports(){
	    errorList_ = new ArrayList();
	    warningList_ = new ArrayList();
	    
	    // Sort according to error status code
	    for( int index = 0; index < statusList_.size(); index++ )
	    {
	      IStatus status = (IStatus)statusList_.get(index);
	      
	      if( status.getSeverity()== IStatus.ERROR){
	        errorList_.add(status);
	      }
	      else if (status.getSeverity()==IStatus.WARNING){
	        warningList_.add(status);
	      }
	    }
	    

	}

}

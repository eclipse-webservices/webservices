/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ant;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentEngine;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * Manages the execution of commands in the root fragment passed to the constructor.
 * 
 * @author joan
 *
 */	

public class AntOperationManager extends CommandFragmentEngine
{	
	  private AntEnvironment environment_;
	  
	  protected IStatus initBeforeExecute( AbstractDataModelOperation operation )
    {      
      environment_.getLog().log(ILog.INFO, "ws_ant", 5098, this, "initBeforeExecute", "Initializing data for: " + operation.getClass().getName());
      IStatus initStatus = Status.OK_STATUS;
      try
      {
    	  initStatus = environment_.initOperationData( operation );  
      }
      catch (Exception e)
      {
    	 throw new IllegalArgumentException(e.getMessage());  
      }        
      return initStatus;
    }

    /**
	   * Creates a CommandFragmentEngine.
	   * 
	   * @param startFragment the root fragment where traversal will begin.
	   * @param dataManager the data manager containing all of the data mappings.
	   * @param environment the environment.
	   */
	  public AntOperationManager( CommandFragment startFragment, DataFlowManager dataManager, AntEnvironment environment )
	  {
      super( startFragment, dataManager, environment );
      
	  	environment_ = environment;
	  } 
}




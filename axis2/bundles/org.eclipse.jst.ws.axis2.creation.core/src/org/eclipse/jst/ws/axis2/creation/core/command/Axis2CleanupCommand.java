/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070314   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse 
 * 										the Axis2 runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class Axis2CleanupCommand extends AbstractDataModelOperation 
{
  
  public Axis2CleanupCommand(){
  }
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
  {
    // Cleanup the reflection over head by increasing the priority to the garbage thread
    System.gc();
    return Status.OK_STATUS;      	
  }
}

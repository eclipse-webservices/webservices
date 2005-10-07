/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors;

import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.Environment;


/**
* Objects of this class represent a visitor.
* */
public class JavaMofParameterVisitor implements Visitor 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  private MessageUtils msgUtils_;
  private Environment env_;

  /*
  * Constructor
  **/
  public JavaMofParameterVisitor(Environment env)
  {
	String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);  	
  	env_ = env;
  }
  
  /*
  * Run through all the parameters in this method
  * @param JavaClass javaclass that holds the parameters
  * @param VisitorAction Action to be performed on each method
  **/
  public IStatus run ( Object imethod, VisitorAction vAction)
  {
  	IStatus status = Status.OK_STATUS;
  	Choice OKChoice = new Choice('O', msgUtils_.getMessage("LABEL_OK"), msgUtils_.getMessage("DESCRIPTION_OK"));
  	Choice CancelChoice = new Choice('C', msgUtils_.getMessage("LABEL_CANCEL"), msgUtils_.getMessage("DESCRIPTION_CANCEL"));  	
    Method method = (Method)imethod;
   
    JavaHelpers javaReturnParameter = method.getReturnType();
    //pgm This visitor used to take a JavaParameter type and now it is being called
    //    with a JavaHelpers type.  Gil, please ensure that this is Ok.
    status = vAction.visit(javaReturnParameter);
    //
    int severity = status.getSeverity(); 
    if (severity==Status.ERROR)
    	return status;
    
    if (severity==Status.WARNING)
    {
      Choice result = env_.getStatusHandler().report(status, new Choice[]{OKChoice, CancelChoice});
      if (result.getLabel().equals(CancelChoice.getLabel()))
      {
      	 //return an error status since the user canceled
      	  return StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED") );
      }
      	
    }
    //           

    //now the inputs
    List parameters = method.getParameters(); 
    
    for (int index = 0; index < parameters.size(); index++) 
    {
      JavaParameter param=(JavaParameter)parameters.get(index);
      status = vAction.visit(param);
      //
      severity = status.getSeverity(); 
      if (severity==Status.ERROR)
      	return status;
      
      if (severity==Status.WARNING)
      {
        Choice result = env_.getStatusHandler().report(status, new Choice[]{OKChoice, CancelChoice});
        if (result.getLabel().equals(CancelChoice.getLabel()))
        {
        	 //return an error status since the user canceled
        	  return StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED") );
        }
        	
      }
      //      
    }
    
    return status;
    
  }
  
        
  
}

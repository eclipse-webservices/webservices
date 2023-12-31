/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitoractions;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofTypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.ParameterElement;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.datamodel.Element;



/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a parameterElement using the 
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk to its type
* */
public class JavaMofParameterVisitorAction extends JavaMofBeanVisitorAction 
{


  /*
  *Constructor
  **/
  public JavaMofParameterVisitorAction(Element parentElement,String project, IEnvironment env)
  {
    super(parentElement,project, env);
  }
   
  /**
  * Create a parameter element from the method 
  * @param JavaParameter the mof element to be used to create the Parameter
  **/
  public IStatus visit (Object ijavaParameter)
  {
  	IStatus status = Status.OK_STATUS;
  	Choice OKChoice = new Choice('O',ConsumptionMessages.LABEL_OK, ConsumptionMessages.DESCRIPTION_OK);
  	Choice CancelChoice = new Choice('C', ConsumptionMessages.LABEL_CANCEL, ConsumptionMessages.DESCRIPTION_CANCEL);
  	
    ParameterElement parameterElement = (ParameterElement)BeanModelElementsFactory.getBeanModelElement(ijavaParameter,fParentElement);
    JavaMofTypeVisitorAction typeVisitorAction = new JavaMofTypeVisitorAction(parameterElement,clientProject, env_);
    //typeVisitorAction.setStatusMonitor(getStatusMonitor());
    typeVisitorAction.setReturnParam(parameterElement.isReturn());
    JavaMofTypeVisitor typeVisitor = new JavaMofTypeVisitor(env_);
    typeVisitor.setClientProject(getProject());
    status = typeVisitor.run(ijavaParameter,typeVisitorAction);
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
      	  return StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_SAMPLE_CREATION_CANCELED );
      }
      	
    }
    //           
    return status;
  }
}

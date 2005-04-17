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

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitoractions;

import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofTypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.ParameterElement;
import org.eclipse.wst.command.internal.provisional.env.core.common.Choice;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.datamodel.Element;



/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a parameterElement using the 
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk to its type
* */
public class JavaMofParameterVisitorAction extends JavaMofBeanVisitorAction 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  private MessageUtils msgUtils_;
    
  /*
  *Constructor
  **/
  public JavaMofParameterVisitorAction(Element parentElement,String project, Environment env)
  {
    super(parentElement,project, env);
	String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);    
  }
   
  /**
  * Create a parameter element from the method 
  * @param JavaParameter the mof element to be used to create the Parameter
  **/
  public Status visit (Object ijavaParameter)
  {
  	Status status = new SimpleStatus("");
  	Choice OKChoice = new Choice('O', msgUtils_.getMessage("LABEL_OK"), msgUtils_.getMessage("DESCRIPTION_OK"));
  	Choice CancelChoice = new Choice('C', msgUtils_.getMessage("LABEL_CANCEL"), msgUtils_.getMessage("DESCRIPTION_CANCEL"));
  	
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
      	  return new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED"), Status.ERROR);
      }
      	
    }
    //           
    return status;
  }
}

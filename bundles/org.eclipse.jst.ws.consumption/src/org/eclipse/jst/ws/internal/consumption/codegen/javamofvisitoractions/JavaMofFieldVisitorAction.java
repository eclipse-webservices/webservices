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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.Field;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofTypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.FieldElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.ws.internal.datamodel.Element;


/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a AttributeElement using the
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk a complex type
* */
public class JavaMofFieldVisitorAction extends JavaMofBeanVisitorAction
{

  /*
  *Constructor
  **/
  public JavaMofFieldVisitorAction(Element parentElement, String project, IEnvironment env)
  {
    super(parentElement,project, env);
  }


  public IStatus visit (Object field_)
  {
  	IStatus status = Status.OK_STATUS;
    Field field = (Field)field_;
     
    try{
       //if the type of this attribute is unsupported dont make an Attribute
      if(!(getReturnParam() && TypeFactory.isRecognizedReturnType((JavaHelpers)field.getJavaClass())) 
      	&& (TypeFactory.isUnSupportedType((JavaHelpers)field.getJavaClass()))){
      	  status = StatusUtils.warningStatus( ConsumptionMessages.MSG_WARN_JTS_UNSUPPORTED_TYPE + ((JavaHelpers)field.getJavaClass()).getJavaName() );
	      //getStatusMonitor().reportStatus( new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
	      //ConsumptionMessages.MSG_WARN_JTS_UNSUPPORTED_TYPE + ((JavaHelpers)field.getJavaClass()).getJavaName(),null));
	      return status;
	  }	


      FieldElement fieldElement = (FieldElement)BeanModelElementsFactory.getBeanModelElement(field,fParentElement);

      if(fieldElement != null){
        //lets check and see if the getter takes input params
        //if so throw an error
        //now the inputs
        
        JavaMofTypeVisitorAction typeVisitorAction = new JavaMofTypeVisitorAction(fieldElement,clientProject, env_);
        typeVisitorAction.setStatusMonitor(getStatusMonitor());
        typeVisitorAction.setBeansCreated(getBeansCreated());
        typeVisitorAction.setReturnParam(getReturnParam()); 
        JavaMofTypeVisitor typeVisitor = new JavaMofTypeVisitor(env_);
        typeVisitor.setClientProject(getProject());
        status = typeVisitor.run(field,typeVisitorAction);
       
      }
      return status;
    
    }catch(Exception e)
	{
    	env_.getLog().log(ILog.WARNING, 5055, this, "visit", e);
    	status = StatusUtils.warningStatus(	ConsumptionMessages.MSG_ERROR_JTS_JSP_GEN, e);
    	try {
    		env_.getStatusHandler().report(status);
    	} 
      catch (StatusException e1) 
      {
    		status = StatusUtils.errorStatus(ConsumptionMessages.MSG_ERROR_JTS_JSP_GEN, e1 );
    	}
    	return status;
	}

  }
 
}

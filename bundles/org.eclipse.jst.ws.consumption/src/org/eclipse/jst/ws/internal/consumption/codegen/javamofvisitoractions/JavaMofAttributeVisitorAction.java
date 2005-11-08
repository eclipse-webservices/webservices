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
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofTypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.AttributeElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.jst.ws.internal.consumption.sampleapp.common.SamplePropertyDescriptor;
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
public class JavaMofAttributeVisitorAction extends JavaMofBeanVisitorAction
{

  /*
  *Constructor
  **/
  public JavaMofAttributeVisitorAction(Element parentElement, String project, IEnvironment env)
  {
    super(parentElement,project, env);
  }

  public IStatus visit (Object propertyDecorator)
  {
  	IStatus status = Status.OK_STATUS;
  	  	
    //PropertyDecorator pd = (PropertyDecorator)propertyDecorator;
    SamplePropertyDescriptor pd = (SamplePropertyDescriptor)propertyDecorator;  
    
    
    try{
       //if the type of this attribute is unsupported dont make an Attribute
      if(!(getReturnParam() && TypeFactory.isRecognizedReturnType((JavaHelpers)pd.getPropertyType())) 
      	&& (TypeFactory.isUnSupportedType((JavaHelpers)pd.getPropertyType()))){
      	  status = StatusUtils.warningStatus( ConsumptionMessages.MSG_WARN_JTS_UNSUPPORTED_TYPE + ((JavaHelpers)pd.getPropertyType()).getJavaName() );
	      //getStatusMonitor().reportStatus( new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
	      //ConsumptionMessages.MSG_WARN_JTS_UNSUPPORTED_TYPE + ((JavaHelpers)pd.getPropertyType()).getJavaName(),null));
	      return status;
	  }	

      Method setMethod = pd.getWriteMethod();
      Method getMethod = pd.getReadMethod();
      if(setMethod != null){
      	if(setMethod.isStatic()){
          setMethod=null; 
      	}
      }
      if(getMethod != null){
      	if(getMethod.isStatic()){
          getMethod=null;
      	}
      }
      if(pd.isfStatic()){
        return status;
      }
      
      AttributeElement attributeElement = (AttributeElement)BeanModelElementsFactory.getBeanModelElement(propertyDecorator,fParentElement);

      if(attributeElement != null){
        
        //lets check and see if the getter takes input params
        //if so throw an error
        //now the inputs
        if(getMethod != null){
	      JavaParameter javaParameter[] = getMethod.listParametersWithoutReturn();
	        if(javaParameter.length > 0){
	          if(getReturnParam()) return status;
	          status = StatusUtils.warningStatus( ConsumptionMessages.MSG_WARN_JTS_UNSUPPORTED_INDEXED_PROPERTIES + getMethod.getName() );
	          //getStatusMonitor().reportStatus (new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
	          //		ConsumptionMessages.MSG_WARN_JTS_UNSUPPORTED_INDEXED_PROPERTIES + getMethod.getName(),null));
	          return status;
	        }
        }
        if(setMethod != null) attributeElement.setSetterMethod(setMethod.getMethodElementSignature());
        if(getMethod != null) attributeElement.setGetterMethod(getMethod.getMethodElementSignature());

        JavaMofTypeVisitorAction typeVisitorAction = new JavaMofTypeVisitorAction(attributeElement,clientProject, env_);
        typeVisitorAction.setStatusMonitor(getStatusMonitor());
        typeVisitorAction.setBeansCreated(getBeansCreated());
        typeVisitorAction.setReturnParam(getReturnParam()); 
        JavaMofTypeVisitor typeVisitor = new JavaMofTypeVisitor(env_);
        typeVisitor.setClientProject(getProject());
        status = typeVisitor.run(pd,typeVisitorAction);
       
      }
    
      return status;
    }catch(Exception e)
    {
    	env_.getLog().log(ILog.WARNING, 5054, this, "visit", e);
    	status = StatusUtils.warningStatus(	ConsumptionMessages.MSG_ERROR_JTS_JSP_GEN, e);
    	try {
			env_.getStatusHandler().report(status);
		} catch (StatusException e1) {
			status = StatusUtils.errorStatus(ConsumptionMessages.MSG_ERROR_JTS_JSP_GEN);
		}
    	return status;
    }
    //env.Log.write(this,"visit",ILog.OK,e);}

  }
 
}

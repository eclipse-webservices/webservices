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

import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofTypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.AttributeElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.jst.ws.internal.consumption.sampleapp.common.SamplePropertyDescriptor;
import org.eclipse.jst.ws.internal.datamodel.Element;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.common.StatusException;


/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a AttributeElement using the
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk a complex type
* */
public class JavaMofAttributeVisitorAction extends JavaMofBeanVisitorAction
{
	
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  private MessageUtils msgUtils_;

  /*
  *Constructor
  **/
  public JavaMofAttributeVisitorAction(Element parentElement, String project, Environment env)
  {
    super(parentElement,project, env);
	String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
  }

  /**
  * Create a type element from the JavaHelper
  * @param JavaParameter the mof element to be used to create the attribute
  **/


  //bean info code


  public Status visit (Object propertyDecorator)
  {
  	Status status = new SimpleStatus("");
  	  	
    //PropertyDecorator pd = (PropertyDecorator)propertyDecorator;
    SamplePropertyDescriptor pd = (SamplePropertyDescriptor)propertyDecorator;  
    
    
    try{
       //if the type of this attribute is unsupported dont make an Attribute
      if(!(getReturnParam() && TypeFactory.isRecognizedReturnType((JavaHelpers)pd.getPropertyType())) 
      	&& (TypeFactory.isUnSupportedType((JavaHelpers)pd.getPropertyType()))){
      	  status = new SimpleStatus("", msgUtils_.getMessage("MSG_WARN_JTS_UNSUPPORTED_TYPE") + ((JavaHelpers)pd.getPropertyType()).getJavaName(), Status.WARNING );
	      //getStatusMonitor().reportStatus( new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
	      //WebServiceConsumptionPlugin.getMessage( "%MSG_WARN_JTS_UNSUPPORTED_TYPE") + ((JavaHelpers)pd.getPropertyType()).getJavaName(),null));
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
	          status = new SimpleStatus("", msgUtils_.getMessage("MSG_WARN_JTS_UNSUPPORTED_INDEXED_PROPERTIES") + getMethod.getName(), Status.WARNING );
	          //getStatusMonitor().reportStatus (new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
	          //		WebServiceConsumptionPlugin.getMessage( "%MSG_WARN_JTS_UNSUPPORTED_INDEXED_PROPERTIES" ) + getMethod.getName(),null));
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
    	env_.getLog().log(Log.WARNING, 5054, this, "visit", e);
    	status = new SimpleStatus("JavaMofAttributeVisitorAction", //$NON-NLS-1$
    			msgUtils_.getMessage("MSG_ERROR_JTS_JSP_GEN"), Status.WARNING, e);
    	try {
			env_.getStatusHandler().report(status);
		} catch (StatusException e1) {
			status = new SimpleStatus("JavaMofAttributeVisitorAction", //$NON-NLS-1$
	    			msgUtils_.getMessage("MSG_ERROR_JTS_JSP_GEN"), Status.ERROR);
		}
    	return status;
    }
    //env.Log.write(this,"visit",Log.OK,e);}

  }
 
}

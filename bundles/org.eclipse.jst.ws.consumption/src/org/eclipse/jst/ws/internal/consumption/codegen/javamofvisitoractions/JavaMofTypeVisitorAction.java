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

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofBeanVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.jst.ws.internal.datamodel.Element;
import org.eclipse.wst.command.env.core.common.Choice;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;




/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a TypeElement using the
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk a complex type
* */
public class JavaMofTypeVisitorAction extends JavaMofBeanVisitorAction
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  private MessageUtils msgUtils_;

  /*
  *Constructor
  **/
  public JavaMofTypeVisitorAction(Element parentElement,String project, Environment env)
  {
    super(parentElement,project, env);
	String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);    
  }

  /**
  * Create a type element from the JavaHelper
  * @param JavaHelper the mof element to be used to create the Type
  **/
  public Status visit (Object typeNavigator)
  {
  	 Status status = new SimpleStatus("");
   	 Choice OKChoice = new Choice('O', msgUtils_.getMessage("LABEL_OK"), msgUtils_.getMessage("DESCRIPTION_OK"));
  	 Choice CancelChoice = new Choice('C', msgUtils_.getMessage("LABEL_CANCEL"), msgUtils_.getMessage("DESCRIPTION_CANCEL"));  	 

     //if bean
	 JavaClass javaClass = null;
	 if(typeNavigator instanceof JavaClass){
	    javaClass = (JavaClass)typeNavigator;
	 }


     /* If we are dealing with a return parameter then we need to treat this differently then if it is an input 
     *  parameter.
     *  We need to first see if it is a special case for return types if not we treat it the same as everything
     *  else.
     */
     if(getReturnParam() && TypeFactory.isRecognizedReturnType((JavaHelpers)typeNavigator)){
       TypeElement typeElement = (TypeElement)BeanModelElementsFactory.getBeanModelElement(typeNavigator,fParentElement);
     }	
     else{
	    if(TypeFactory.isUnSupportedType((JavaHelpers)typeNavigator)){
	      status = new SimpleStatus("", msgUtils_.getMessage("MSG_WARN_JTS_UNSUPPORTED_TYPE") + ((JavaHelpers)typeNavigator).getJavaName(), Status.WARNING );	    	
	      //getStatusMonitor().reportStatus( new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
              //WebServiceConsumptionPlugin.getMessage( "%MSG_WARN_JTS_UNSUPPORTED_TYPE" ) + ((JavaHelpers)typeNavigator).getJavaName(),null));
	      return status;
	    }	
	    
	    //if javaclass is null then we have a simple type 
	    if((javaClass == null) || TypeFactory.recognizedBean(javaClass.getJavaName())){
	      TypeElement typeElement = (TypeElement)BeanModelElementsFactory.getBeanModelElement(typeNavigator,fParentElement);
	    }
	    else{
	      JavaMofBeanVisitorAction beanVisitorAction = new JavaMofBeanVisitorAction(fParentElement,clientProject, env_);
	      beanVisitorAction.setStatusMonitor(getStatusMonitor());
	      beanVisitorAction.setBeansCreated(getBeansCreated());
          beanVisitorAction.setReturnParam(getReturnParam());
		  JavaMofBeanVisitor beanVisitor = new JavaMofBeanVisitor();
	      status = beanVisitor.run(javaClass,beanVisitorAction);
	    }
     }
     
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

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070410   180952 makandre@ca.ibm.com - Andrew Mak, Sample JSP generator chokes on interfaces and abstract classes
 * 20080505   182167 makandre@ca.ibm.com - Andrew Mak, Warning not issued when non-instantiable class is bypassed in sampe JSPs
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitoractions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaVisibilityKind;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofBeanVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.datamodel.Element;




/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a TypeElement using the
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk a complex type
* */
public class JavaMofTypeVisitorAction extends JavaMofBeanVisitorAction
{

  /*
  *Constructor
  **/
  public JavaMofTypeVisitorAction(Element parentElement,String project, IEnvironment env)
  {
    super(parentElement,project, env);
  }

  /**
  * Create a type element from the JavaHelper
  * @param JavaHelper the mof element to be used to create the Type
  **/
  public IStatus visit (Object typeNavigator)
  {
  	 IStatus status = Status.OK_STATUS;
   	 Choice OKChoice = new Choice('O', ConsumptionMessages.LABEL_OK, ConsumptionMessages.DESCRIPTION_OK);
  	 Choice CancelChoice = new Choice('C', ConsumptionMessages.LABEL_CANCEL, ConsumptionMessages.DESCRIPTION_CANCEL);  	 

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
     if(getReturnParam() && TypeFactory.isRecognizedReturnType((JavaHelpers)typeNavigator))
     {
       BeanModelElementsFactory.getBeanModelElement(typeNavigator,fParentElement);
     }	
     else{
	    if(TypeFactory.isUnSupportedType((JavaHelpers)typeNavigator)){
	      status = StatusUtils.warningStatus( ConsumptionMessages.MSG_WARN_JTS_UNSUPPORTED_TYPE + ((JavaHelpers)typeNavigator).getJavaName() );	    	
	      //getStatusMonitor().reportStatus( new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
              //ConsumptionMessages.MSG_WARN_JTS_UNSUPPORTED_TYPE + ((JavaHelpers)typeNavigator).getJavaName(),null));
	      return status;
	    }	
	    
	    //if javaclass is null then we have a simple type 
	    if((javaClass == null) || TypeFactory.recognizedBean(javaClass.getJavaName()))
      {
	      BeanModelElementsFactory.getBeanModelElement(typeNavigator,fParentElement);
	    }
	    else if (javaClass != null && !getReturnParam() &&
				// the following cases cannot be instantiated for input
				(javaClass.isInterface() || javaClass.isAbstract() || !hasDefaultConstructor(javaClass))) {
	    	
	    	Element element = BeanModelElementsFactory.getBeanModelElement(typeNavigator,fParentElement);
	    	element.setPropertyAsObject(TypeElement.NON_INSTANTIABLE, Boolean.TRUE);
    		status = StatusUtils.warningStatus(
    				ConsumptionMessages.bind(ConsumptionMessages.MSG_WARN_JTS_NON_INSTANTIABLE_TYPE, element.getName()));
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
       	  return StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_SAMPLE_CREATION_CANCELED );
       }
       status = Status.OK_STATUS; // user says it's OK
     }
     //
     return status;
   }

  /**
   * Determine if the given javaClass has a default constructor.
   * @param javaClass The java class
   * @return true if javaClass has a default constructor, false otherwise
   */
  private boolean hasDefaultConstructor(JavaClass javaClass) {
	   
	  List methods = javaClass.getMethods();
	  Iterator iter = methods.iterator();
	   
	  boolean foundConstructor = false;
	   
	  while (iter.hasNext()) {
		  Method method = (Method) iter.next();
		   
		  if (method.getName().equals(javaClass.getName())) {
			  if (method.listParametersWithoutReturn().length == 0)
				  return method.getJavaVisibility().getValue() == JavaVisibilityKind.PUBLIC;
			  foundConstructor = true;
		  }
	  }
	   
	  // if no constructor is found at this point, a default one is implicitly provided
	  return !foundConstructor;
  }
}

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

import java.util.Iterator;
import java.util.Vector;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofParameterVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.MethodElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.ws.internal.datamodel.Element;


/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a BeanElement using the 
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk the methods in the JavaClass
* */
public class JavaMofMethodVisitorAction extends JavaMofBeanVisitorAction 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private MessageUtils msgUtils_;
  public String fUnsupportedParameterName;

 /*
  * Methods omitted from the proxy
  */
  protected Vector fMethodsOmitted;

  /*
  * Methods processed from proxy
  */
  protected Vector fMethodsProcessed;

     
  /*
  *Constructor
  **/
  public JavaMofMethodVisitorAction(Element parentElement,String project, Environment env)
  {
    super(parentElement,project, env);
    String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    fMethodsOmitted = new Vector();
    fMethodsProcessed = new Vector();

  }
   
  /**
  * Create a method element from the method 
  * @param Method the class to be used to create the method
  **/
  public IStatus visit (Object imethod)
  {
  	IStatus status = Status.OK_STATUS;
    Method method = (Method)imethod;
           
    if (methodCheck(method)){
      //this is to check immediate input and return parameters
      //if there is an unsupported type in these we can react immediately
      //we add it to the omitted methods an go to the next one
      if(!parameterCheck(method)){
      	status = StatusUtils.warningStatus( msgUtils_.getMessage("MSG_WARN_JTS_UNSUPPORTED_PARAMETERS_ARRAYS") + fUnsupportedParameterName );
        //getStatusMonitor().reportStatus (new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
        //		WebServiceConsumptionPlugin.getMessage( "%MSG_WARN_JTS_UNSUPPORTED_PARAMETERS_ARRAYS" ) + fUnsupportedParameterName,null));
        fMethodsOmitted.addElement(method.getMethodElementSignature());
        return status;
      }
      if(!nullConstructor(method)){
      	status = StatusUtils.warningStatus( msgUtils_.getMessage("MSG_WARN_JTS_UNSUPPORTED_PARAMETERS_INPUTS") + fUnsupportedParameterName );
        //getStatusMonitor().reportStatus (new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
       	//	WebServiceConsumptionPlugin.getMessage( "%MSG_WARN_JTS_UNSUPPORTED_PARAMETERS_INPUTS" ) + fUnsupportedParameterName,null));
        fMethodsOmitted.addElement(method.getMethodElementSignature());
        return status;
      
      }

      //carry on all parameters supported
      MethodElement methodElement = (MethodElement)BeanModelElementsFactory.getBeanModelElement(method,fParentElement);
      JavaMofParameterVisitorAction parameterVisitorAction = new JavaMofParameterVisitorAction(methodElement,clientProject,env_);
      parameterVisitorAction.setStatusMonitor(getStatusMonitor());
      JavaMofParameterVisitor parameterVisitor = new JavaMofParameterVisitor(env_);
      status = parameterVisitor.run(method,parameterVisitorAction);   
      //something may have gone wrong with an internal type
      if (status.getSeverity()!=Status.OK){
        //This method has to be omitted
        fMethodsOmitted.addElement(method.getMethodElementSignature());
        methodElement.setMethodOmmission(true);  
      }	
      else {
        fMethodsProcessed.addElement(method.getMethodElementSignature());
      }	
    }
    
    return status;
  }
 
  /*
  * Tells wether any methods were processed 
  * @return boolean true if any methods were processed 
  */
  public boolean wereMethodsProcessed()
  {
    if(fMethodsProcessed.isEmpty()) return false;
    return true;
  }

  /*
  * Tells wether there were methods omitted because of unsupported types
  * @return boolean true if methods were omitted
  */
  public boolean wereMethodsOmitted()
  {
    if(fMethodsOmitted.isEmpty()) return false;
    return true;
  }
  
  /**
  * There may be methods that we dont support
  * and dont want in the model
  * @param boolean true if all parameters are fine
  **/
  public boolean methodCheck(Method method)
  {
    boolean ok = true;
    // if we have a constructor we return false
    if (method.isConstructor()) return false;

       
    return ok;
  }

  /**
  * There may be parameters of this method that have types
  * not yet supported return false if we find any
  * @param boolean true if all parameters are fine
  **/
  public boolean parameterCheck(Method method)
  {

    boolean ok = true;
    
    //now the inputs
    JavaParameter javaParameter[] = method.listParametersWithoutReturn(); 
    for (int i = 0;i<javaParameter.length;i++) {
      JavaParameter param=javaParameter[i];
      JavaHelpers javaHelper1 = param.getJavaType();
      if(TypeFactory.isUnSupportedType(javaHelper1)) {
        fUnsupportedParameterName = param.getName(); 
    	return false;
      }    
    }
  return ok;
  }

 
  /**
  * There may be parameters of this method that have types
  * not yet supported return false if we find any
  * @param boolean true if all parameters are fine
  **/
  public boolean nullConstructor(Method method)
  {
    
    boolean ok = true;
    //now the inputs
    JavaParameter javaParameter[] = method.listParametersWithoutReturn(); 
    for (int i = 0;i<javaParameter.length;i++) {
      JavaParameter param=javaParameter[i];
      JavaHelpers javaHelper1 = param.getJavaType();
      if(javaHelper1 instanceof JavaClass){
        JavaClass javaClass = (JavaClass)javaHelper1;
        if(TypeFactory.recognizedBean(javaClass.getJavaName()))return true;
          Iterator m=javaClass.getMethods().iterator();
          while (m.hasNext()) {
            Method method2=(Method)m.next();
            if (javaClass.getName().equals(method2.getName())){
              //now the inputs
              JavaParameter javaParam[] = method2.listParametersWithoutReturn();
              if (javaParam.length > 0){
                //then we have no default constructor
                fUnsupportedParameterName = param.getName(); 
    	        ok = false;
              }    
              else return true;
            } 
          }
        }
      }
  return ok;
  }

 








}

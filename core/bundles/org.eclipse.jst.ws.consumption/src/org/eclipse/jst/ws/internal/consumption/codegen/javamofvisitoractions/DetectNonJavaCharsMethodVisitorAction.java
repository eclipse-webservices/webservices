/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitoractions;

import java.util.Vector;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofParameterVisitor;


/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a BeanElement using the 
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk the methods in the JavaClass
* */
public class DetectNonJavaCharsMethodVisitorAction extends VisitorActionImpl 
{
  private static char UNDERSCORE = '_';
  private boolean serviceBean = false;

   public DetectNonJavaCharsMethodVisitorAction ( Vector messages, Vector beans)
  	{
  		super (messages, beans);
  		if ( beans.size() == 1)
  			serviceBean = true;
  	}

  /**
  * Create a method element from the method 
  * @param Method the class to be used to create the method
  **/
  public IStatus visit (Object imethod)
  {
    
    Method method = (Method)imethod;
    String  className = method.getContainingJavaClass().getName();
    String name = method.getName();

	if ( serviceBean && !method.isConstructor() )
		checkUpperCase(name, className);
	
	// for service bean or any referenced bean property accessors
	 if ( serviceBean || name.startsWith("get") || name.startsWith("set") || name.startsWith("is")) 
	 {
	    checkUnderScore(name, className);
	    checkNumericDigits(name, className);
	    checkBooleanProperties(method, className);
		  	
	 	DetectNonJavaCharsParameterVisitorAction parameterVisitorAction = new DetectNonJavaCharsParameterVisitorAction(getMessages(), getBeansVisited());
	 	// rsk Passing in null for the environment. DetectNonJavaCharsParameterVisitorAction always returns an OK status.
    	JavaMofParameterVisitor parameterVisitor = new JavaMofParameterVisitor(null);
    	parameterVisitor.run(method, parameterVisitorAction);   
	  }
	 
	 return Status.OK_STATUS;
	}

  private void checkUpperCase(String name, String className)
  {
  	 // in a service bean check if any method starts with upper case letter
	 if ( Character.isUpperCase(name.charAt(0)))
    		addMessage(ConsumptionMessages.MSG_WARN_JAVA_METHOD_START_WITH_UPPER_CASE, new String[]{name, className});  

  }

  private void checkUnderScore(String name, String className)
  {
     //check if the name has an underscore, then next letter should be upper case
     int underScoreIndex = name.indexOf(UNDERSCORE);
	 String tempName = name;
	 while(underScoreIndex !=-1) {
		tempName = tempName.substring(underScoreIndex+1);
		if ( Character.isLowerCase(tempName.charAt(0))) {
    		addMessage(ConsumptionMessages.MSG_WARN_METHOD_NAME_INVALID, new String[]{name, className});  
     		break;
		}
		else
			underScoreIndex = tempName.indexOf(UNDERSCORE);
	   }
  }

  private void checkNumericDigits(String name, String className)
  {
     //check if the name has a numeric, then next letter should be upper case
     for ( int i = 0; i < name.length(); i++)
     	{
     		if ( Character.isDigit(name.charAt(i)))
     			{
     				if (Character.isLowerCase(name.charAt(i+1))) {
			    		addMessage(ConsumptionMessages.MSG_WARN_METHOD_NAME_INVALID, new String[]{name, className});  
			     		break;
 					}
		}
     }
  }
  private void checkBooleanProperties(Method method, String className)
  	{
  		if (!serviceBean && method.getName().startsWith("get") && 
  			(method.getReturnType().getName().equals("boolean") ||
  			 method.getReturnType().getName().equals("java.lang.Boolean")))
  			addMessage(ConsumptionMessages.MSG_WARN_BOOLEAN_PROPERTY_ACCESSORS, new String[]{method.getName(), className});  
  	}

}

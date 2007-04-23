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
import org.eclipse.jem.java.ArrayType;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofBeanVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;



/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a BeanElement using the 
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk the methods in the JavaClass
* */
public class DetectNonJavaCharsParameterVisitorAction extends VisitorActionImpl 
{

  public DetectNonJavaCharsParameterVisitorAction ( Vector messages, Vector beans)
  	{
  		super (messages, beans);
  	}

  /**
  * Create a method element from the method 
  * @param Method the class to be used to create the method
  **/
  public IStatus visit (Object ijavaParameter)
  {
    
    JavaParameter javaParameter = (JavaParameter) ijavaParameter;
    JavaClass javaClass = null;
    if ( javaParameter.getJavaType() instanceof JavaClass) 
    	{
    	javaClass = (JavaClass) javaParameter.getJavaType();
    	if (javaClass.isArray())
    	  {
    	    JavaHelpers componentType = ((ArrayType)javaClass).getComponentTypeAsHelper();
    	    if (componentType instanceof JavaClass)
    	      {
    	        javaClass = (JavaClass)componentType;
    	      }
    	    else
    	      {
    	        javaClass = null; //The array contains primitive types, there is no need for further checking.
    	      }
    	  }
    	}
   	if (toBeVisited(javaClass))    {
			DetectNonJavaCharsBeanVisitorAction beanVisitorAction = new DetectNonJavaCharsBeanVisitorAction(getMessages(), getBeansVisited());
			JavaMofBeanVisitor beanVisitor = new JavaMofBeanVisitor();
			beanVisitor.run(javaClass,beanVisitorAction);
  	  	}
   	
   	return Status.OK_STATUS;
  }
  

  private boolean toBeVisited (JavaClass javaClass)
  {
	//check for recognized types 
	if( javaClass == null	||
	   javaClass.isPrimitive() ||
	   javaClass.getJavaName().startsWith("javax") ||
       TypeFactory.recognizedBean(javaClass.getJavaName()))
	    	return false;
	else
		  	return true;
	}
}

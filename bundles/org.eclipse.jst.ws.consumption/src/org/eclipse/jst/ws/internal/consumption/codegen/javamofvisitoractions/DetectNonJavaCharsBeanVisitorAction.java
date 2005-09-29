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

import java.util.Vector;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofMethodVisitor;




/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a BeanElement using the
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk the methods in the JavaClass
* */
public class DetectNonJavaCharsBeanVisitorAction extends VisitorActionImpl
{
  private static char UNDERSCORE = '_';

  public DetectNonJavaCharsBeanVisitorAction ( Vector messages, Vector beans)
  	{
  		super (messages, beans);
  	}

  /**
  * The visit will create the bean
  * Walk the methods
  * @param JavaClass the class to be used to create the bean model
  **/
  public IStatus visit (Object javaclass)
  {
    JavaClass javaClass = (JavaClass)javaclass;
    String packageName = javaClass.getJavaPackage().getPackageName();
    String beanName = javaClass.getName();

    if (!isBeanVisited(javaClass)) {
      	addVisitedBean(javaClass);
		// check the bean name
		checkBeanName(beanName);
		checkPackageName(packageName, beanName);
	  	DetectNonJavaCharsMethodVisitorAction methodVisitorAction = new DetectNonJavaCharsMethodVisitorAction(getMessages(), getBeansVisited());
      	JavaMofMethodVisitor methodVisitor = new JavaMofMethodVisitor();
    	methodVisitor.run(javaClass,methodVisitorAction);
     }
    
    return Status.OK_STATUS;
   }

   private void checkBeanName(String beanName)
   	{
	 
	 // check if the bean starts with a lower case
	 if ( Character.isLowerCase(beanName.charAt(0)))
    	addMessage("WARN_BEAN_NAME_STARTS_WITH_LOWER_CASE", new String[]{beanName});  

	
     //check if the name has an underscore, then next letter should be upper case
     int underScoreIndex = beanName.indexOf(UNDERSCORE);
	 String tempName = beanName;
	 while(underScoreIndex !=-1) {
		tempName = tempName.substring(underScoreIndex+1);
		if ( Character.isLowerCase(tempName.charAt(0))) {
      		addMessage("MSG_WARN_METHOD_NAME_INVALID", new String[] { beanName });  
      		break;
			}
		else
			underScoreIndex = tempName.indexOf(UNDERSCORE);
		}
	}

	private void checkPackageName(String packageName, String beanName)
		{
			if (!packageName.equals(packageName.toLowerCase()))
				addMessage("MSG_WARN_PACKAGE_NAME_HAS_UPPER_CASE", new String[] { packageName, beanName });
		}
 }

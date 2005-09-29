/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.internal.plugin.JavaEMFNature;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.Method;
import org.eclipse.jem.java.impl.JavaClassImpl;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;


public class JavaToWSDLMethodCommand extends EnvironmentalOperation {

	private static String JAVA_EXTENSION = ".java"; //$NON-NLS-1$
	private static String CLASS_EXTENSION = ".class"; //$NON-NLS-1$

	private Hashtable fMethodNames;
	private String fClassName;
	private String fbeanBaseName;
	private JavaWSDLParameter javaWSDLParam_;
	private IProject serviceProject_;
	private MessageUtils msgUtils_;

	/**
	* Default CTOR
	*/
	public JavaToWSDLMethodCommand() {
		String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	}
	/**
	* Default CTOR
	*/
	public JavaToWSDLMethodCommand(
		JavaWSDLParameter javaParameter,
		IProject serviceProject) {
		String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		javaWSDLParam_ = javaParameter;
		serviceProject_ = serviceProject;

	}

	/**
	* JavaToToWSDLMethod execute
	*/
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		Environment environment = getEnvironment();

		fbeanBaseName = javaWSDLParam_.getBeanName();
		environment.getLog().log(Log.INFO, 5070, this, "execute", "beanBaseName = "+fbeanBaseName);
		IStatus status;
		try {
			//Get the qualified bean name; my.package.MyClass
			fMethodNames = new Hashtable();
			Hashtable oldMethodsNames = javaWSDLParam_.getMethods();
			String qName = fbeanBaseName;

			if (qName.toLowerCase().endsWith(JAVA_EXTENSION)
				|| qName.toLowerCase().endsWith(CLASS_EXTENSION)) {
				qName = qName.substring(0, qName.lastIndexOf('.'));
			}

			JavaEMFNature jMOF =
				(JavaEMFNature) JavaEMFNature.createRuntime(
					serviceProject_);
			JavaClass javaClass =
				(JavaClass) JavaClassImpl.reflect(qName, jMOF.getResourceSet());
			if (!javaClass.isExistingType()) {
				environment.getLog().log(Log.ERROR, 5022, this, "execute", msgUtils_.getMessage(
						"MSG_ERROR_JAVA_MOF_REFLECT_FAILED",
						new String[] { qName }));
				
				status = StatusUtils.errorStatus(
						msgUtils_.getMessage(
					"MSG_ERROR_JAVA_MOF_REFLECT_FAILED",
					new String[] { qName }));
				environment.getStatusHandler().reportError(status);
				return status;
			}

			// Get the qualified name
			fClassName = javaClass.getQualifiedName();
			String beanName;
			if (fClassName.lastIndexOf('.') != -1) {
				beanName =
					fClassName.substring(
						fClassName.lastIndexOf('.') + 1,
						fClassName.length());
			} else
				beanName = fClassName;

			// Walk the java class and get the method names
			gatherMethods(javaClass, beanName, oldMethodsNames);
			/*
			Iterator m = javaClass.getPublicMethodsExtended().iterator();
			while (m.hasNext()) {
				Method method = (Method) m.next();  
				if ( ! method.isConstructor()){ 
				if (!beanName.equals(method.getName())
					&& !(isDuplicateMethodName(method
						.getMethodElementSignature()))
					&& !(method
						.getContainingJavaClass()
						.getJavaName()
						.equalsIgnoreCase("javax.ejb.EJBObject"))	//$NON-NLS-1$
					&& !(method
						.getContainingJavaClass()
						.getJavaName()
						.equalsIgnoreCase("javax.ejb.EJBObject[]"))	//$NON-NLS-1$
					&& !(method
						.getContainingJavaClass()
						.getJavaName()
						.equalsIgnoreCase("java.lang.Object"))	//$NON-NLS-1$
					&& !(method
						.getContainingJavaClass()
						.getJavaName()
						.equalsIgnoreCase("java.lang.Object[]"))) {	//$NON-NLS-1$
					// add the method name to our list of method names
					String methodName =method.getMethodElementSignature();
		          Boolean isSelected = new Boolean(true);
        		  if (oldMethodsNames != null && oldMethodsNames.containsKey(methodName))
          			isSelected = (Boolean)oldMethodsNames.get(methodName);
		          fMethodNames.put(methodName, isSelected);
				} 
				}
			}
			*/
			javaWSDLParam_.setMethods(fMethodNames);
			
			return Status.OK_STATUS;

		} catch (Exception e) {
			environment.getLog().log(Log.ERROR, 5023, this, "execute", msgUtils_.getMessage("MSG_ERROR_READ_BEAN"));
		
			status = StatusUtils.errorStatus(
					msgUtils_.getMessage(
				"MSG_ERROR_JAVA_TO_METHOD"),
				 e);
			environment.getStatusHandler().reportError(status);
			return status;
		}
	}

  private void gatherMethods(JavaClass javaClass, String beanName, Hashtable oldMethodsNames)
  {
    String javaName = javaClass.getJavaName();
    if (!javaClass.isInterface()
        && !javaName.equalsIgnoreCase("javax.ejb.EJBObject[]")
        && !javaName.equalsIgnoreCase("javax.ejb.EJBObject")
        && !javaName.equalsIgnoreCase("java.lang.Object[]")
        && !javaName.equalsIgnoreCase("java.lang.Object"))
    {
      List publicMethods = javaClass.getPublicMethods();
      for (Iterator it = publicMethods.iterator(); it.hasNext();)
      {
        Method method = (Method)it.next();
        if (!method.isConstructor()
            && !beanName.equals(method.getName())
            && !(isDuplicateMethodName(method.getMethodElementSignature())))
        {
          // add the method name to our list of method names
          String methodName = method.getMethodElementSignature();
          Boolean isSelected = new Boolean(true);
          if (oldMethodsNames != null && oldMethodsNames.containsKey(methodName))
            isSelected = (Boolean)oldMethodsNames.get(methodName);
          fMethodNames.put(methodName, isSelected);
        }
      }
      gatherMethods(javaClass.getSupertype(), beanName, oldMethodsNames);
    }
  }
	  
	//Returns true if the provided method name already exists in the 
	//fMethodNames vector. This method is used to detect overloaded 
	//methods in Beans and EJBs, which are currently unsupported.
	private boolean isDuplicateMethodName(String methodName) {
		return (fMethodNames == null ? false: fMethodNames.containsKey(methodName));
	}
	
	/**
	 * Returns the javaWSDLParam.
	 * @return JavaWSDLParameter
	 */
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}

	/**
	 * Sets the javaWSDLParam.
	 * @param javaWSDLParam The javaWSDLParam to set
	 */
	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		this.javaWSDLParam_ = javaWSDLParam;
	}
	
	public void setServiceProject(IProject serviceProject)
	{
	  serviceProject_ = serviceProject;
	}
}

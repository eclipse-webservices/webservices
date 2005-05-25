/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.JavaMOFUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.common.JavaResourceFilter;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


public class ValidateObjectSelectionCommand extends SimpleCommand
{
  private String JAVA_EXTENSION = ".java"; //$NON-NLS-1$
  private String CLASS_EXTENSION = ".class"; //$NON-NLS-1$
  private MessageUtils msgUtils_;
  private IStructuredSelection objectSelection;
  private String serviceProjectName;

  
  /**
   * Validates that the selected Java bean can be loaded from the service project. If it can't, an error is displayed.
   */
  public ValidateObjectSelectionCommand()
  {
    super();
    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
  }
  public Status execute(Environment env)
  {
    //Make sure a bean is selected
    String javaBeanName = null;
    if (objectSelection != null && !objectSelection.isEmpty())
    {
      Object object = objectSelection.getFirstElement();
      if (object instanceof String)
      {
        javaBeanName = ((String)object);
      }
      else
      {
      	//The ObjectSelectionWidget never appeared and so the IStucturedSelection from 
      	//ObjectSelectionWidgetOutputCommand is the initial selection. 
      	//Get the Java bean name from the selection.
      	try
		{
      	  System.out.println(object.getClass().toString());
      	  javaBeanName = getJavaBeanFromObjectSelection(objectSelection);
		} catch (CoreException ce)
		{
		  Status errorStatus = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_CANNOT_NO_JAVA_BEAN"),Status.ERROR);
		  env.getStatusHandler().reportError(errorStatus);
		  return errorStatus;			
		}
      }
    }    
    
    if (javaBeanName==null || javaBeanName.length()==0)
    {
      Status errorStatus = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_CANNOT_NO_JAVA_BEAN"),Status.ERROR);
      env.getStatusHandler().reportError(errorStatus);
      return errorStatus;
    }
    
    //Make sure a project is selected
    IProject serviceProject = (IProject)((new StringToIProjectTransformer()).transform(serviceProjectName));
    if (serviceProject==null)
    {
      Status errorStatus = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_NO_PROJECT"),Status.ERROR);
      env.getStatusHandler().reportError(errorStatus);
      return errorStatus;      
    }
    
    //Make sure the selected bean can be loaded from the selected project
	if (javaBeanName.toLowerCase().endsWith(JAVA_EXTENSION)
		|| javaBeanName.toLowerCase().endsWith(CLASS_EXTENSION)) {
		javaBeanName = javaBeanName.substring(0, javaBeanName.lastIndexOf('.'));
	}
	
      JavaClass javaClass = JavaMOFUtils.getJavaClass(javaBeanName, serviceProject);
	  if (!javaClass.isExistingType()) 
	  {		
		Status errorStatus = new SimpleStatus("JavaToWSDLMethodCommand", msgUtils_.getMessage("MSG_ERROR_CANNOT_LOAD_JAVA_BEAN", new String[] { javaBeanName, serviceProjectName }),Status.ERROR);
		env.getStatusHandler().reportError(errorStatus);
		return errorStatus;
	  }

    
    return new SimpleStatus("");
  }
  
  /**
   * @param serviceProjectName The serviceProjectName to set.
   */
  public void setServiceProjectName(String serviceProjectName)
  {
    this.serviceProjectName = serviceProjectName;
  }
  
  /**
   * @param objectSelection The objectSelection to set.
   */
  public void setObjectSelection(IStructuredSelection objectSelection)
  {
    this.objectSelection = objectSelection;
  }

  private String getJavaBeanFromObjectSelection(IStructuredSelection objectSelection) throws CoreException
  {
    String beanClass = "";
    JavaResourceFilter filter = new JavaResourceFilter(); 
    //IStructuredSelection selection = initialSelection_;
    //
    if (objectSelection != null)
    {
      Object obj = objectSelection.getFirstElement();
      if (obj != null)
      {
        // get the IResource represented by the selection
        IResource res = null;
        
        res = ResourceUtils.getResourceFromSelection(obj);
        
        if (filter.accepts(res))
        {
          // get the package-qualified class name without file extensions
          String beanPackage = ResourceUtils.getJavaResourcePackageName(res.getFullPath());
          if (beanPackage==null)
            beanPackage = "";
          else
            beanPackage = beanPackage + ".";

          beanClass = beanPackage + res.getName();

          if (beanClass.toLowerCase().endsWith(".java") || beanClass.toLowerCase().endsWith(".class")) {
            beanClass = beanClass.substring(0,beanClass.lastIndexOf('.'));
          }
        }
      }
    }
    
    //
    return beanClass;
  }    
}

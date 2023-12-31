/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.common.JavaMOFUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.JavaResourceFilter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class ValidateObjectSelectionCommand extends AbstractDataModelOperation
{
  private String JAVA_EXTENSION = ".java"; //$NON-NLS-1$
  private String CLASS_EXTENSION = ".class"; //$NON-NLS-1$
  private IStructuredSelection objectSelection;
  private String serviceProjectName;

  
  /**
   * Validates that the selected Java bean can be loaded from the service project. If it can't, an error is displayed.
   */
  public ValidateObjectSelectionCommand()
  {
    super();
  }
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();
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
		  IStatus errorStatus = StatusUtils.errorStatus(AxisCreationUIMessages.MSG_ERROR_CANNOT_NO_JAVA_BEAN);
		  environment.getStatusHandler().reportError(errorStatus);
		  return errorStatus;			
		}
      }
    }    
    
    if (javaBeanName==null || javaBeanName.length()==0)
    {
      IStatus errorStatus = StatusUtils.errorStatus(AxisCreationUIMessages.MSG_ERROR_CANNOT_NO_JAVA_BEAN);
      environment.getStatusHandler().reportError(errorStatus);
      return errorStatus;
    }
    
    //Make sure a project is selected
    IProject serviceProject = ProjectUtilities.getProject(serviceProjectName);
    if (serviceProject==null)
    {
      IStatus errorStatus = StatusUtils.errorStatus(AxisCreationUIMessages.MSG_ERROR_NO_PROJECT);
      environment.getStatusHandler().reportError(errorStatus);
      return errorStatus;      
    }
    
    //Make sure the selected bean can be loaded from the selected project
	if (javaBeanName.toLowerCase().endsWith(JAVA_EXTENSION)
		|| javaBeanName.toLowerCase().endsWith(CLASS_EXTENSION)) {
		javaBeanName = javaBeanName.substring(0, javaBeanName.lastIndexOf('.'));
	}
	
    try
    {
	  JavaClass javaClass = JavaMOFUtils.getJavaClass(javaBeanName, serviceProject); 
	  if (!javaClass.isExistingType()) 
	  {		
		IStatus errorStatus = StatusUtils.errorStatus( NLS.bind(AxisCreationUIMessages.MSG_ERROR_CANNOT_LOAD_JAVA_BEAN, new String[] { javaBeanName, serviceProjectName }));
		environment.getStatusHandler().reportError(errorStatus);
		return errorStatus;
	  }
    } catch (CoreException ce)
    {
	  IStatus errorStatus = StatusUtils.errorStatus( NLS.bind(AxisCreationUIMessages.MSG_ERROR_CANNOT_LOAD_JAVA_BEAN, new String[] { javaBeanName, serviceProjectName }));
	  environment.getStatusHandler().reportError(errorStatus);
      return errorStatus;      
    }
    
    return Status.OK_STATUS;
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

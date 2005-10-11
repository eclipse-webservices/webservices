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

package org.eclipse.jst.ws.internal.consumption.command.common;


//core stuff
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jem.internal.plugin.JavaEMFNature;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


/**
* This class is to be used to Build the data model
* first we get the java class and then build the model
*/
public class JavaMofReflectionCommand extends AbstractDataModelOperation
{

  public static String LABEL = "JavaMofReflectionCommand"; 
  public static String DESCRIPTION = "reflection for a given class"; 
  public static String OK_MESSAGE =  "The model has been built "; 
  private static String JAVA_EXTENSION = ".java";
  private static String CLASS_EXTENSION = ".class";
  
  private String clientProject;
  private ResourceSet resourceSet;
  private JavaHelpers javaClass;
  private String qname;
  private String proxyBean;
 
  /**
  * Constructs a new JavaMofReflectionCommand with the given label and description
  * 
  */
  public JavaMofReflectionCommand()
  {
  }
  
  // setters for this command
  
    
  /**
  * The end result of this whole process is to get the Java Class
  */
  public JavaHelpers getJavaClass()
  {
    return javaClass;
  }

  
  private void processQName()
  {
      qname = proxyBean;
	  if (qname.toLowerCase().endsWith(JAVA_EXTENSION)) {
		  qname = qname.substring(0,(qname.length() -5));
	  }
	  if (qname.toLowerCase().endsWith(CLASS_EXTENSION)) {
		  qname = qname.substring(0,(qname.length() -6));
	  }
   }
      
  /**
  * Get the java model from the resource then 
  * build the model from the mof
  */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
 	  //just make sure the project and qname are there
  	//they are essential for this operation
  	IStatus status = Status.OK_STATUS;
  	IProject clientIProject = (IProject)ResourceUtils.findResource(clientProject);
  	processQName();
  	if(clientProject == null || qname == null)
  	  return StatusUtils.warningStatus( WebServiceConsumptionPlugin.getMessage("%MSG_WARN_UNABLE_TO_FIND_PROXY") );
  	
    JavaEMFNature nature = (JavaEMFNature)JavaEMFNature.getRuntime(clientIProject);
    if(nature == null){
      try{
        nature = (JavaEMFNature)JavaEMFNature.createRuntime(clientIProject);  	
      }catch(CoreException exc){}
    }
    resourceSet = nature.getResourceSet();
    javaClass = JavaRefFactory.eINSTANCE.reflectType(qname,resourceSet);
    
    return status;
  }
  
  public void setProxyBean(String proxyBean)
  {
  	this.proxyBean = proxyBean;
  }

  public void setClientProject(String clientProject)
  {
  	this.clientProject = clientProject;
  }
  
 }

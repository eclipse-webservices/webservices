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

package org.eclipse.jst.ws.internal.consumption.sampleapp.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitoractions.JavaMofBeanVisitorAction;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofBeanVisitor;
import org.eclipse.jst.ws.internal.consumption.command.common.JavaMofReflectionCommand;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Choice;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.selection.BooleanSelection;
import org.eclipse.wst.ws.internal.datamodel.Element;
import org.eclipse.wst.ws.internal.datamodel.Model;

/**
* This is the base class for commands that need to report progress
* and status during and after their execution. This class extends
* {@link org.eclipse.emf.common.command.AbstractCommand AbstractCommand}

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

* with methods to {@link #setProgressMonitor set}
* and {@link #getProgressMonitor get} a progress monitor,
* and to get a status object {@link #getReadyStatus before}
* or {@link #getResultStatus after} execution.
* <p>
* Note that the responsibility of providing an
* {@link org.eclipse.core.runtime.IProgressMonitor IProgressMonitor}
* rests with frameworks that construct or run ProgressCommand objects,
* whereas the responsibility of providing an
* {@link org.eclipse.core.runtime.IStatus IStatus}
* object lies with the subclasses of ProgressCommand.
* Subclasses must follow the rules described for
* {@link org.eclipse.emf.common.command.AbstractCommand AbstractCommand}.
*/
public class JavaToModelCommand extends SimpleCommand
{
  private MessageUtils msgUtils;
  private IResource resource;
  private String clientProject;
  private BooleanSelection[] methods;
  private String proxyBean;
  private JavaClass javaClass;
  private Model model;
  private Element parentElement;
  private IProject project;
  
  public static String LABEL = "JavaToModelCommand";
  public static String DESCRIPTION = "this creates a model from a resource";
 
  

  public JavaToModelCommand ()
  {
	String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils = new MessageUtils(pluginId + ".plugin", this);  	
  	setDescription(DESCRIPTION);
  	setName(LABEL);
  }

  
  private Status createJavaReflection(Environment env)
  {
  	Status status = new SimpleStatus("");
    JavaMofReflectionCommand javaMofReflectionCommand = new JavaMofReflectionCommand();
    javaMofReflectionCommand.setClientProject(clientProject);
    javaMofReflectionCommand.setProxyBean(proxyBean);
    //javaMofReflectionCommand.setStatusMonitor(getStatusMonitor());
    status = javaMofReflectionCommand.execute(env);
    javaClass = (JavaClass)javaMofReflectionCommand.getJavaClass();
    return status;
  }

  /**
  * The Model that was created from this javamof
  * @return Model The data model that was created
  **/
  public Model getDataModel()
  {
    return model;
  }

  /**
  * Build the datamodel from the mof
  */
  public Status buildModelFromMof (Environment env) throws CoreException
  {
  	
  	Choice OKChoice = new Choice('O', msgUtils.getMessage("LABEL_OK"), msgUtils.getMessage("DESCRIPTION_OK"));
  	Choice CancelChoice = new Choice('C', msgUtils.getMessage("LABEL_CANCEL"), msgUtils.getMessage("DESCRIPTION_CANCEL"));
  	
    // we could have one of three cases:
    //1. The model is null meaning we want and the parent element is null, meaning we want to
    //   create a brand new model and make this bean its root
    //2. The model is null but the parentElement is not, meaning we want to add the Bean to the
    //   given parent element
    //3. The model is not null however the parentElement is, meaning we want to add this Bean to
    //   This model but dont attach it to anything
      Status status = new SimpleStatus("");

      if(model == null && parentElement == null){
         JavaMofBeanVisitorAction beanVisitorAction = new JavaMofBeanVisitorAction(clientProject,methods, env);
         //beanVisitorAction.setStatusMonitor(getStatusMonitor());
	     JavaMofBeanVisitor beanVisitor = new JavaMofBeanVisitor();
         status = beanVisitor.run(javaClass,beanVisitorAction);
         //
         int severity = status.getSeverity(); 
         if (severity==Status.ERROR)
         	return status;
         
         if (severity==Status.WARNING)
         {
           Choice result = env.getStatusHandler().report(status, new Choice[]{OKChoice, CancelChoice});
           if (result.getLabel().equals(CancelChoice.getLabel()))
           {
           	 //return an error status since the user canceled
           	  return new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED"), Status.ERROR);
           }
           	
         }
         //
         model = beanVisitorAction.getModel();

      }
      else if (model == null && parentElement != null){
         JavaMofBeanVisitorAction beanVisitorAction = new JavaMofBeanVisitorAction(parentElement,clientProject, env);
         //beanVisitorAction.setStatusMonitor(getStatusMonitor());
	     JavaMofBeanVisitor beanVisitor = new JavaMofBeanVisitor();
         status = beanVisitor.run(javaClass,beanVisitorAction);
         //
         int severity = status.getSeverity(); 
         if (severity==Status.ERROR)
         	return status;
         
         if (severity==Status.WARNING)
         {
           Choice result = env.getStatusHandler().report(status, new Choice[]{OKChoice, CancelChoice});
           if (result.getLabel().equals(CancelChoice.getLabel()))
           {
           	 //return an error status since the user canceled
           	  return new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED"), Status.ERROR);
           }
           	
         }
         //         
         model = beanVisitorAction.getModel();
      }
      else {
         JavaMofBeanVisitorAction beanVisitorAction = new JavaMofBeanVisitorAction(model,clientProject, env);
         //beanVisitorAction.setStatusMonitor(getStatusMonitor());
	     JavaMofBeanVisitor beanVisitor = new JavaMofBeanVisitor();
         status = beanVisitor.run(javaClass,beanVisitorAction);
         //
         int severity = status.getSeverity(); 
         if (severity==Status.ERROR)
         	return status;
         
         if (severity==Status.WARNING)
         {
           Choice result = env.getStatusHandler().report(status, new Choice[]{OKChoice, CancelChoice});
           if (result.getLabel().equals(CancelChoice.getLabel()))
           {
           	 //return an error status since the user canceled
           	  return new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED"), Status.ERROR);
           }
           	
         }
         //         
         model = beanVisitorAction.getModel();
      }

      return status;
  }

  /**
  * Get the java model from the resource then
  * build the model from the mof
  */
  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus("");
    if(clientProject == null) return status;
    IProject project = (IProject)ResourceUtils.findResource(clientProject);
    
  	status = createJavaReflection(env);
    if (status.getSeverity()==Status.ERROR) return status;
    try{
      status = buildModelFromMof(env);
      return status;
    }catch(CoreException exc){
      IStatus embeddedStatus = exc.getStatus();
      status = EnvironmentUtils.convertIStatusToStatus(embeddedStatus);
      return status;
    }

  }
    
  public void setMethods(BooleanSelection[] methods)
  {
  	this.methods =  methods;
  }
  
  public void setClientProject(String clientProject)
  {
  	this.clientProject = clientProject;
  }
  
  public void setParentElement(Element parentElement)
  {
  	this.parentElement = parentElement;
  }
     
  public void setProxyBean(String proxyBean)
  {
  	this.proxyBean = proxyBean;
  }

 }


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
package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;



public class CreateJavaProjectCommand extends EnvironmentalOperation
{
  private String projectName_;

  /**
   * Default CTOR
   */
  public CreateJavaProjectCommand()
  {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName_);
    
    if (project != null && !project.exists())
    {
      try
      {
        project.create(ResourcesPlugin.getWorkspace().newProjectDescription(project.getName()), monitor);
        project.open(monitor);
        IProjectDescription desc = project.getDescription();
        desc.setNatureIds(new String[] {JavaCore.NATURE_ID});
        ICommand cmd = desc.newCommand();
        cmd.setBuilderName(JavaCore.BUILDER_ID);
        desc.setBuildSpec(new ICommand[] {cmd});
        project.setDescription(desc, monitor);
        IJavaProject javaProject = JavaCore.create(project);
        Path projectRoot = new Path(Path.ROOT.append(new Path(project.getName())).toString());
        javaProject.setRawClasspath(new IClasspathEntry[] {JavaCore.newSourceEntry(projectRoot), JavaCore.newContainerEntry(new Path(JavaRuntime.JRE_CONTAINER))}, monitor);
        javaProject.setOutputLocation(projectRoot, monitor);
      }
      catch (CoreException ce)
      {
        Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, WebServiceConsumptionPlugin.getMessage("%MSG_ERROR_CANNOT_CREATE_JAVA_PROJECT", new String[] {projectName_}), Status.ERROR, ce);
        env.getStatusHandler().reportError(status);
        return status;
      }
    }
    return new SimpleStatus("");
  }
  
  public void setProjectName(String projectName)
  {
    projectName_ = projectName;
  }
}

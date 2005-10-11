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
package org.eclipse.jst.ws.internal.consumption.ui.command;

import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class OpenJavaEditorCommand extends AbstractDataModelOperation
{
  private List classNames;
  private IProject project;

  public OpenJavaEditorCommand()
  {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    OpenJavaEditorJob job = new OpenJavaEditorJob(classNames, project);
    job.setPriority(Job.LONG);
    job.schedule();
    return Status.OK_STATUS;
  }

  public void setClassNames(List classNames)
  {
    this.classNames = classNames;
  }

  public void setProject(IProject project)
  {
    this.project = project;
  }
  
  private class OpenJavaEditorJob extends UIJob
  {
    private List classNames;
    private IProject project;

    public OpenJavaEditorJob(List classNames, IProject project)
    {
      super("org.eclipse.jst.ws.internal.consumption.ui.command.OpenJavaEditorJob");
      this.classNames = classNames;
      this.project = project;
    }
    
    public IStatus runInUIThread(IProgressMonitor monitor)
    {
      if (project != null && classNames != null)
      {
        IJavaProject javaProject = JavaCore.create(project);
        for (Iterator it = classNames.iterator(); it.hasNext();)
        {
          String className = (String)it.next();
          try
          {
            IType type = javaProject.findType(className);
            JavaUI.openInEditor(type);
          }
          catch (Throwable t)
          {
            return new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.Status.ERROR, WebServiceConsumptionUIPlugin.ID, 0, WebServiceConsumptionUIPlugin.getMessage("MSG_ERROR_UNABLE_TO_OPEN_JAVA_EDITOR", new String[]{className, project.getName()}), t);
          }
        }
        return new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.Status.OK, WebServiceConsumptionUIPlugin.ID, 0, "", null);
      }
      else
        return new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.Status.ERROR, WebServiceConsumptionUIPlugin.ID, 0, WebServiceConsumptionUIPlugin.getMessage("MSG_ERROR_UNABLE_TO_OPEN_JAVA_EDITOR", new String[]{classNames != null ? classNames.toString() : "", project != null ? project.getName() : ""}), null);
    }
  }
}

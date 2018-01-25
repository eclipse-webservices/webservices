/*******************************************************************************
 * Copyright (c) 2004,2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060404 134913   sengpl@ca.ibm.com - Seng Phung-Lu       
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
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class OpenJavaEditorCommand extends AbstractDataModelOperation
{
  private List classNames;
  private IProject project;
  private boolean isEnabled_ = true;  // is true by default

  public OpenJavaEditorCommand()
  {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    if (isEnabled_) {
      OpenJavaEditorJob job = new OpenJavaEditorJob(classNames, project);
      job.setPriority(Job.LONG);
      job.schedule();
    }
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
  
  public void setIsEnabled(boolean isEnabled){
    this.isEnabled_ = isEnabled;
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
            return new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.Status.ERROR, WebServiceConsumptionUIPlugin.ID, 0, NLS.bind(ConsumptionUIMessages.MSG_ERROR_UNABLE_TO_OPEN_JAVA_EDITOR, new String[]{className, project.getName()}), t);
          }
        }
        return Status.OK_STATUS;
      }
      else if (project!=null || classNames.isEmpty()){
        // do nothing ; nothing to open
        return Status.OK_STATUS;
      }
      else
        return new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.Status.ERROR, WebServiceConsumptionUIPlugin.ID, 0, NLS.bind(ConsumptionUIMessages.MSG_ERROR_UNABLE_TO_OPEN_JAVA_EDITOR, new String[]{classNames != null ? classNames.toString() : "", project != null ? project.getName() : ""}), null);
    }
  }
}

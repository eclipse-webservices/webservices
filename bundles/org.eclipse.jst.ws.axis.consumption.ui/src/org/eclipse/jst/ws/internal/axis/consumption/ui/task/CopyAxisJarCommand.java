/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060509   125094 sengpl@ca.ibm.com - Seng Phung-Lu, Use WorkspaceModifyOperation
 * 20060515   115225 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.task;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.plugin.WebServiceAxisConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.common.BundleUtils;


public class CopyAxisJarCommand extends AbstractDataModelOperation {

  public static String AXIS_RUNTIME_PLUGIN_ID = "org.apache.axis"; //$NON-NLS-1$
  public static String[] JARLIST = new String[] {
	  "axis.jar",
	  "commons-discovery-0.2.jar",
	  "commons-logging-1.0.4.jar",
	  "jaxrpc.jar",
	  "saaj.jar",
	  "wsdl4j-1.5.1.jar"
  };
  public static String PATH_TO_JARS_IN_PLUGIN = "lib/";

  private IProject project;
  private Boolean projectRestartRequired_ = Boolean.FALSE;
  
  /**
   * Default CTOR;
   */
  public CopyAxisJarCommand( ) {
  }

  /**
   * Execute the command
   */
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		
		IEnvironment env = getEnvironment();
		IStatus status = Status.OK_STATUS;
	    ProgressUtils.report(monitor, AxisConsumptionUIMessages.PROGRESS_INFO_COPY_AXIS_CFG);
	    
	    if (J2EEUtils.isWebComponent(project))
	    {
	    	copyAxisJarsToProject(project, status, env, monitor);	
	    }
	    else
	    {
	    	//Check if it's a plain old Java project
	 		 if (J2EEUtils.isJavaComponent(project))
	 		 {
	 			status = addAxisJarsToBuildPath(project, env, monitor);
	 			if (status.getSeverity()==Status.ERROR)
	 			{
	 				env.getStatusHandler().reportError(status);
	 				return status;
	 			}
	 		 }
	 		 else
	 		 {
	 		   status = StatusUtils.errorStatus( AxisConsumptionUIMessages.MSG_WARN_NO_JAVA_NATURE);	
	 		   env.getStatusHandler().reportError(status);
	 		   return status;
	 		 }

	    }
	    
	    return status;

	}

  private void copyAxisJarsToProject(IProject project, IStatus status, IEnvironment env, IProgressMonitor monitor) {

	IPath webModulePath = J2EEUtils.getWebContentPath( project );
    if (webModulePath == null) {
      status = StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_PROJECT_NOT_FOUND);
      env.getStatusHandler().reportError(status);
      return;
    }
	
	for (int i=0; i<JARLIST.length; ) {
		copyIFile("lib/"+JARLIST[i], webModulePath, "WEB-INF/lib/"+JARLIST[i++], status, env, monitor); 
	    if (status.getSeverity() == Status.ERROR)
	      return;
	}
    return;
  }

  /**
   *  
   */
  private void copyIFile(String source, IPath targetPath, String targetFile, IStatus status, IEnvironment env, IProgressMonitor monitor) {
    IPath target = targetPath.append(new Path(targetFile));
    ProgressUtils.report(monitor,ConsumptionMessages.PROGRESS_INFO_COPYING_FILE);

    try {
      ResourceContext context = new TransientResourceContext();
      context.setOverwriteFilesEnabled(true);
      context.setCreateFoldersEnabled(true);
      context.setCheckoutFilesEnabled(true);
      URL sourceURL = BundleUtils.getURLFromBundle( AXIS_RUNTIME_PLUGIN_ID, source );
      IFile resource = ResourceUtils.getWorkspaceRoot().getFile(target);
      if (!resource.exists()) {
        IFile file = FileResourceUtils.createFile(context, target, sourceURL.openStream(), monitor, 
            env.getStatusHandler());
        if (projectRestartRequired_.booleanValue() == false && file.exists()) {
          projectRestartRequired_ = Boolean.TRUE;
        }

      }
    }
    catch (Exception e) {
      status = StatusUtils.errorStatus( AxisConsumptionUIMessages.MSG_ERROR_FILECOPY, e);
      env.getStatusHandler().reportError(status);

    }
  }

  public IStatus addAxisJarsToBuildPath(IProject p, IEnvironment env, IProgressMonitor monitor)
  {
	  String[] jarNames = new String[JARLIST.length];
	  for (int i=0; i<JARLIST.length; i++)
	  {
		  StringBuffer sb = new StringBuffer();
		  sb.append(PATH_TO_JARS_IN_PLUGIN);
		  sb.append(JARLIST[i]);
		  String jarName = sb.toString();
		  jarNames[i] = jarName;
	  }
	  
	  IStatus status = addJar(p, AXIS_RUNTIME_PLUGIN_ID, jarNames, env, monitor);
	  if (status.getSeverity()==Status.ERROR)
	  {			  
		  return status;
	  }
	  return Status.OK_STATUS;
  }

  
  private IStatus addJar(IProject webProject, String pluginId, String[] jarNames, IEnvironment env, IProgressMonitor monitor)
  {

    IStatus status = Status.OK_STATUS;
    //
    // Get the current classpath.
    //
    IJavaProject javaProject_ = null;
    IClasspathEntry[] oldClasspath = null;
    javaProject_ = JavaCore.create(webProject);
    try
    {
      oldClasspath = javaProject_.getRawClasspath();
    } catch (JavaModelException jme)
    {
      status = StatusUtils.errorStatus( AxisConsumptionUIMessages.MSG_ERROR_BAD_BUILDPATH, jme);
      // env.getStatusHandler().reportError(status);
      return status;
    }

    ArrayList newJarNamesList = new ArrayList();

    for (int k = 0; k < jarNames.length; k++)
    {
      boolean found = false;
      for (int i = 0; i < oldClasspath.length; i++)
      {
        found = oldClasspath[i].getPath().toString().toLowerCase().endsWith(jarNames[k].toLowerCase());
        if (found)
        {
          break;
        }
      }

      if (!found)
      {
        newJarNamesList.add(jarNames[k]);
      }
    }

    if (newJarNamesList.size() > 0)
    {
      String[] newJarNames = (String[]) newJarNamesList.toArray(new String[] {});

      IClasspathEntry[] newClasspath = new IClasspathEntry[oldClasspath.length + newJarNames.length];
      int i = 0;
      while (i < oldClasspath.length)
      {
        newClasspath[i] = oldClasspath[i];
        i++;
      }

      try
      {
        int m = 0;
        while (i < newClasspath.length)
        {
          newClasspath[i] = JavaCore.newLibraryEntry(getTheJarPath(pluginId, newJarNames[m]), null, null);
          m++;
          i++;
        }
      } catch (CoreException e)
      {
        status = StatusUtils.errorStatus( AxisConsumptionUIMessages.MSG_ERROR_BAD_BUILDPATH, e);
        return status;
      }

      //
      // Then update the project classpath.
      //
      try
      {
        javaProject_.setRawClasspath(newClasspath, monitor);
      } catch (JavaModelException e)
      {
        status = StatusUtils.errorStatus(AxisConsumptionUIMessages.MSG_ERROR_BAD_BUILDPATH, e);
        return status;
      }
    }

    return status;

  }

		//
		// Returns the local native pathname of the jar.
		//
		private IPath getTheJarPath(String pluginId, String theJar)
			throws CoreException {
			try {
				if (pluginId != null) {
					URL localURL =	Platform.asLocalURL(BundleUtils.getURLFromBundle( pluginId, theJar ) );
					return new Path(localURL.getFile());
				} else {
					return new Path(theJar);
				}
			} catch (MalformedURLException e) {
				throw new CoreException(
					new org.eclipse.core.runtime.Status(
						IStatus.WARNING,
						WebServiceAxisConsumptionUIPlugin.ID,
						0,
						AxisConsumptionUIMessages.MSG_ERROR_BAD_BUILDPATH,
						e));
			} catch (IOException e) {
				throw new CoreException(
					new org.eclipse.core.runtime.Status(
						IStatus.WARNING,
						WebServiceAxisConsumptionUIPlugin.ID,
						0,
						AxisConsumptionUIMessages.MSG_ERROR_BAD_BUILDPATH,
						e));
			}
		}  
  
  public void setProject(IProject project) {
    this.project = project;
  }

  public boolean getProjectRestartRequired() {
    return projectRestartRequired_.booleanValue();
  }
  
}

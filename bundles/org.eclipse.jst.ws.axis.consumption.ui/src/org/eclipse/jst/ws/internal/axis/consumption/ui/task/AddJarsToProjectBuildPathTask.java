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
package org.eclipse.jst.ws.internal.axis.consumption.ui.task;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.plugin.WebServiceAxisConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseProgressMonitor;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.ProgressMonitor;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusException;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;

public class AddJarsToProjectBuildPathTask extends EnvironmentalOperation {

	private IJavaProject javaProject_;
	private IClasspathEntry[] oldClasspath_;
	private IClasspathEntry[] newClasspath_;
	private JavaWSDLParameter javaWSDLParam_;
	private boolean isJ2EE_13 = true;
	private MessageUtils msgUtils_;
	
	private IProject project;
	private String module_;

	public AddJarsToProjectBuildPathTask()
	  {
			String pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
			msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
			module_ = J2EEUtils.getFirstWebModuleName(project);
		}
	
	public AddJarsToProjectBuildPathTask(String module)
  {
		String pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
		msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
		module_ = module;
	}

	/**
	* Execute AddJarsToProjectBuildPathTask
	*/
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		Environment env = getEnvironment();
//		try {
//			if (project.hasNature(IWebNatureConstants.J2EE_NATURE_ID)) {
//				J2EEWebNatureRuntime webNatureRuntime =
//					(J2EEWebNatureRuntime) project.getNature(
//						IWebNatureConstants.J2EE_NATURE_ID);
//				isJ2EE_13 = webNatureRuntime.isJ2EE1_3();
//			}
//		} catch (Exception e) {
//		}
//    Status status = new SimpleStatus("");
//		if (!isJ2EE_13)
//		{
//		//	AddJar(project, null, "XERCES_API_JAR", status, environment);
//		}
//		return status;
		
		Status status = new SimpleStatus("");
//		 TODO: workaround for 90515 
		//
		   // Get the current classpath.
		   //
		   javaProject_ = null;
		   oldClasspath_ = null;
		   try
		   {    
			 javaProject_ = JavaCore.create(project);    
			 if (javaProject_ != null)
			 {
			   oldClasspath_ = javaProject_.getRawClasspath();
			 }
			 else
			 {
			   status = new SimpleStatus("", msgUtils_.getMessage("MSG_WARN_NO_JAVA_NATURE"), Status.ERROR);	
			   env.getStatusHandler().reportError(status);
			   return status;
			 }
		   }
		   catch (JavaModelException e)
		   {
			   status = new SimpleStatus("", msgUtils_.getMessage("MSG_WARN_NO_JAVA_NATURE"), Status.ERROR);
			   env.getStatusHandler().reportError(status);
			   return status;	   	
		   }


			try {
				
				ArrayList aList = new ArrayList();
				String classpathEntry = null;
				IVirtualComponent component = ComponentCore.createComponent(project, module_);
				if (component != null) {
					
					IVirtualFolder webInfLib = component.getRootFolder().getFolder(new Path(
							"/WEB-INF/lib"));
					if (webInfLib != null) {
						IVirtualResource[] resources = webInfLib.members();
						IResource aResource = null;
						
						for (int i = 0; i < resources.length; i++) {
							aResource = resources[i].getUnderlyingResource();
							classpathEntry = aResource.getLocation().toString();
							if (!FoundClasspathEntry(classpathEntry)) {
								aList.add(classpathEntry);
							}
						}
					}
				}
				
				int newListSize = aList.size();
				
				if (newListSize == 0) {
					return status;
				}
				
				// Update classpath
				newClasspath_ = new IClasspathEntry[oldClasspath_.length + newListSize];
				Iterator iter = aList.iterator();
		
				int j=0;
				while (iter.hasNext()) {
					newClasspath_[j] = JavaCore.newLibraryEntry(new Path((String)iter.next()), null, null);
					j++;
				}
				for (int i=0; i<oldClasspath_.length; i++) {
					newClasspath_[j+i] = oldClasspath_[i];
				}
 
			   	 ProgressMonitor localMonitor = env.getProgressMonitor();
			   	 IProgressMonitor eclipseMonitor = null;
			   	 if (localMonitor instanceof EclipseProgressMonitor)
			   	 {
			   	 	eclipseMonitor = ((EclipseProgressMonitor)localMonitor).getMonitor();
			   	 }
				 javaProject_.setRawClasspath(newClasspath_,eclipseMonitor);
			}
			   catch (JavaModelException e)
			   {
				 status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_BAD_BUILDPATH"), Status.WARNING, e);
				 try
				 {
				 	env.getStatusHandler().report(status);	
				 }
				 catch(StatusException se)
				 {
				 	status = new SimpleStatus("", "User aborted", Status.ERROR);
				 }
			   }
			 catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			 end of workaround
			return status;
		}  
		   

		private boolean FoundClasspathEntry(String classpathEntry)  {
			   

			   boolean found = false;
			   for (int i=0; i<oldClasspath_.length && !found; i++)
			   {
				 if (oldClasspath_[i].getPath().toString().toLowerCase().equals(classpathEntry.toLowerCase())) {
					 found=true;
				 }
			   }
		   	 return found;
		}

	private void AddJar(IProject webProject, String pluginId, String jarName, Status status, Environment env)  {
	   //
	   // Get the current classpath.
	   //
	   javaProject_ = null;
	   oldClasspath_ = null;
	   try
	   {    
		 javaProject_ = JavaCore.create(webProject);    
		 if (javaProject_ != null)
		 {
		   oldClasspath_ = javaProject_.getRawClasspath();
		 }
		 else
		 {
		   status = new SimpleStatus("", msgUtils_.getMessage("MSG_WARN_NO_JAVA_NATURE"), Status.ERROR);	
		   env.getStatusHandler().reportError(status);
		   return;
		 }
	   }
	   catch (JavaModelException e)
	   {
		   status = new SimpleStatus("", msgUtils_.getMessage("MSG_WARN_NO_JAVA_NATURE"), Status.ERROR);
		   env.getStatusHandler().reportError(status);
		   return;	   	
	   }

	   boolean found = false;
	   for (int i=0; i<oldClasspath_.length; i++)
	   {
		 found = found || oldClasspath_[i].getPath().toString().toLowerCase().endsWith(jarName.toLowerCase());
	   }

	   if (found) 
	   {
		 return;
	   }

	   newClasspath_ = new IClasspathEntry[oldClasspath_.length + 1];
	   int i=0;
	   while (i<oldClasspath_.length)
	   {
		 newClasspath_[i] = oldClasspath_[i];
		 i++;
	   }

	   try
	   {
		 if(pluginId!=null)
		 {
		   newClasspath_[i++] = JavaCore.newLibraryEntry(getTheJarPath(pluginId,jarName), null, null);
		 }
		 else
		 {
		   newClasspath_[i++] = JavaCore.newVariableEntry(getTheJarPath(pluginId,jarName), null, null);
		 }
	   }
	   catch (CoreException e)
	   {
		 status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_BAD_BUILDPATH"), Status.WARNING, e);
		 try
		 {
		 	env.getStatusHandler().report(status);
		 }
		 catch(StatusException se)
		 {
		 	status = new SimpleStatus("","User aborted",Status.ERROR);
		 }
		 return;
	   }

	   //
	   // Then update the project classpath.
	   //
	   try
	   {
	   	 ProgressMonitor localMonitor = env.getProgressMonitor();
	   	 IProgressMonitor eclipseMonitor = null;
	   	 if (localMonitor instanceof EclipseProgressMonitor)
	   	 {
	   	 	eclipseMonitor = ((EclipseProgressMonitor)localMonitor).getMonitor();
	   	 }
		 javaProject_.setRawClasspath(newClasspath_,eclipseMonitor);
	   }
	   catch (JavaModelException e)
	   {
		 status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_BAD_BUILDPATH"), Status.WARNING, e);
		 try
		 {
		 	env.getStatusHandler().report(status);	
		 }
		 catch(StatusException se)
		 {
		 	status = new SimpleStatus("", "User aborted", Status.ERROR);
		 }
		 return;
	   }
	 }

	//
	// Returns the local native pathname of the jar.
	//
	private IPath getTheJarPath(String pluginId, String theJar)
		throws CoreException {
		try {
			if (pluginId != null) {
				IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
				IPluginDescriptor pluginDescriptor =
					pluginRegistry.getPluginDescriptor(pluginId);
				URL localURL =
					Platform.asLocalURL(
						new URL(pluginDescriptor.getInstallURL(), theJar));
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
					msgUtils_.getMessage("MSG_BAD_AXIS_JAR_URL"),
					e));
		} catch (IOException e) {
			throw new CoreException(
				new org.eclipse.core.runtime.Status(
					IStatus.WARNING,
					WebServiceAxisConsumptionUIPlugin.ID,
					0,
					msgUtils_.getMessage("MSG_BAD_AXIS_JAR_URL"),
					e));
		}
	}

	public void setProject(IProject project)
	{
	  this.project = project;
	}
}

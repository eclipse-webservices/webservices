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
/*
 * Command to add axis.jar to the build path of a project as an external variable
 */
package org.eclipse.jst.ws.internal.axis.consumption.ui.command;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


/**
 * @author rsinha
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AddAxisJARToBuildPathCommand extends SimpleCommand
{
  private String pluginId_ = "org.eclipse.jst.ws.axis.consumption.ui";
  private MessageUtils msgUtils;
  private String jarPluginId = "org.apache.axis";
  private String jarPluginRelPath = "lib";
  private String jarName = "axis.jar";
  private IProject project;
  
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.Command#execute(org.eclipse.wst.command.internal.provisional.env.core.common.Environment)
   */
  public Status execute(Environment environment)
  {
    msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    Status status = new SimpleStatus("");

    //Add JARs to the classpath
    IClasspathEntry[] oldClasspath;
    IClasspathEntry[] newClasspath;
    IJavaProject javaProject;
    
    //Get the old classpath
    try
    {    
      javaProject = JavaCore.create(project);    
      if (javaProject == null)
      {
        Status wStatus = new SimpleStatus("", msgUtils.getMessage("MSG_WARNING_NOT_COMPILE"), Status.WARNING);
        return wStatus;
      }

      oldClasspath = javaProject.getRawClasspath();
    }
    catch (JavaModelException e)
    {
      Status wStatus = new SimpleStatus("", msgUtils.getMessage("MSG_WARNING_NOT_COMPILE"), Status.WARNING);
      return wStatus;
    }
    
    //Check if the axis.jar is already on the build path
    boolean found = false;
	for (int i=0; i<oldClasspath.length; i++)
	{
	  found = found || oldClasspath[i].getPath().toString().toLowerCase().endsWith(jarName.toLowerCase());
    }

	if (found) 
	{
	  return status;
	}    
    
	//Add axis.jar to the build path
	newClasspath = new IClasspathEntry[oldClasspath.length + 1];
    int i = 0;
    for (i=0 ; i < oldClasspath.length; i++)
    {
      newClasspath[i] = oldClasspath[i];
    }
    
    IClasspathEntry newEntry = getClasspathEntry();
    if (newEntry==null)
    {
      Status wStatus = new SimpleStatus("", msgUtils.getMessage("MSG_WARNING_NOT_COMPILE"), Status.WARNING);
      return wStatus;
    }
    newClasspath[i] = newEntry;
    
    try
    {
      javaProject.setRawClasspath(newClasspath,null);
    }
    catch (JavaModelException e)
    {
      Status wStatus = new SimpleStatus("", msgUtils.getMessage("MSG_WARNING_NOT_COMPILE"), Status.WARNING);
      return wStatus;
    }
    
    
    return status;
    
  }
  
  private IClasspathEntry getClasspathEntry()
  {
    try
    {
      IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
      IPluginDescriptor pluginDescriptor = pluginRegistry.getPluginDescriptor(jarPluginId);

      StringBuffer theJar = new StringBuffer();
      theJar.append(jarPluginRelPath);

      if (jarPluginRelPath.length()>0)
        theJar.append(IPath.SEPARATOR);

      theJar.append(jarName);
      
      URL localURL = Platform.asLocalURL(new URL(pluginDescriptor.getInstallURL(),theJar.toString()));
      Path jarPath =  new Path(localURL.getFile());
      IClasspathEntry cpEntry = JavaCore.newLibraryEntry(jarPath, null, null);
      return cpEntry;
    }
    catch (MalformedURLException e)
    {
      return null;
    }
    catch (IOException e)
    {
      return null;
    }

  }
 
  
  /**
   * @param project The project to set.
   */
  public void setProject(IProject project)
  {
    this.project = project;
  }
}

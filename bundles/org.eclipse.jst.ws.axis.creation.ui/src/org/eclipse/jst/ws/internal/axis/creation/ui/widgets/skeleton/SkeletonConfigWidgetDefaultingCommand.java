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
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.j2ee.internal.web.operations.J2EEWebNatureRuntime;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;

public class SkeletonConfigWidgetDefaultingCommand extends SimpleCommand
{
  private String wsdlURI;
  private IProject serverProject;
  private JavaWSDLParameter javaWSDLParam;

  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }
  
  public void setServerProject(String serverProject)
  {
    this.serverProject = ResourcesPlugin.getWorkspace().getRoot().getProject(serverProject);
  }

  public String getEndpointURI()
  {
    return null;
  }
  
  public String getOutputWSDLFolder()
  {
//      J2EEWebNatureRuntime nature = (J2EEWebNatureRuntime)serverProject.getNature(IWebNatureConstants.J2EE_NATURE_ID);
//      StringBuffer sb = new StringBuffer(nature.getModuleServerRoot().getFullPath().addTrailingSeparator().toString());
//      sb.append("wsdl");
	  IPath wsdlPath = J2EEUtils.getFirstWebContentPath(serverProject).append("wsdl");
      return wsdlPath.toString();
  }
  
  public String getOutputWSDLFile()
  {
    int index = wsdlURI.lastIndexOf('/');
    if (index == -1)
      index = wsdlURI.lastIndexOf('\\');
    return wsdlURI.substring(index+1, wsdlURI.length());
  }
  
  public String getOutputJavaFolder()
  {
    try
    {
      IClasspathEntry[] cp = JavaCore.create(serverProject).getRawClasspath();
      for (int i = 0; i < cp.length; i++)
        if (cp[i].getEntryKind() == IClasspathEntry.CPE_SOURCE)
          return ResourcesPlugin.getWorkspace().getRoot().getLocation().removeTrailingSeparator().append(cp[i].getPath()).toString();
    }
    catch (JavaModelException jme)
    {
    }
    return null;
  }

  public boolean getShowMapping()
  {
    return false;
  }
  
  public boolean isShowMapping()
  {
    return getShowMapping();
  }
  
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
  {
  	this.javaWSDLParam = javaWSDLParam;
  }
  
  public JavaWSDLParameter getJavaWSDLParam()
  {
    javaWSDLParam.setOutput(getOutputWSDLFolder());
    javaWSDLParam.setJavaOutput(getOutputJavaFolder());
    return javaWSDLParam;
  }
}
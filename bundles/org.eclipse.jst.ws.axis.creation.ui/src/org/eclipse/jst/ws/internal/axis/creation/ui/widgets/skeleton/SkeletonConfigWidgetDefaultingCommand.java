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

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

public class SkeletonConfigWidgetDefaultingCommand extends SimpleCommand
{
  private String wsdlURI;
  private IProject serverProject;
  private JavaWSDLParameter javaWSDLParam;
  private String moduleName_;

  public SkeletonConfigWidgetDefaultingCommand( String moduleName )
  {
    moduleName_ = moduleName;
  }
  
  public Status execute(Environment env) 
  {
    String root = getRootURL();
    javaWSDLParam.setOutput( root + getOutputWSDLFolder());
    javaWSDLParam.setJavaOutput(root + getOutputJavaFolder());  
    return new SimpleStatus("");
    
  }
  
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
	  IPath wsdlPath = J2EEUtils.getWebContentPath(serverProject, moduleName_ ).append("wsdl");
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
    if (serverProject!=null){
      return ResourceUtils.getJavaSourceLocation(serverProject, moduleName_).toString();
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
    return javaWSDLParam;
  }
  
  private String getRootURL()
  {
    String rootURL = ResourcesPlugin.getWorkspace().getRoot().getLocation().removeTrailingSeparator().toString(); 
    File   file    = new File( rootURL );
    
    try
    {
      rootURL = file.toURL().toString();
      
      char lastChar = rootURL.charAt(rootURL.length()-1);
      
      if (lastChar == '/' || lastChar == '\\')
      {
        rootURL = rootURL.substring(0, rootURL.length()-1);
      }
    }
    catch (MalformedURLException murle)
    {
    }
    
    return rootURL;
  }
}
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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class SkeletonConfigWidgetDefaultingCommand extends AbstractDataModelOperation
{
  private String wsdlURI;
  private IProject serverProject;
  private JavaWSDLParameter javaWSDLParam;
  private String serviceServerTypeID_;

  public SkeletonConfigWidgetDefaultingCommand( )
  {
  }
  
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();
		IStatus status = Status.OK_STATUS;
		
    String root = getRootURL();
    String outputDir =	ResourceUtils.findResource(J2EEUtils.getWebInfPath( serverProject )).getLocation().toString();
    javaWSDLParam.setOutput( outputDir );
    javaWSDLParam.setJavaOutput(root + getOutputJavaFolder()); 
	
	String projectURL = ServerUtils.getEncodedWebComponentURL(serverProject, serviceServerTypeID_);
	
	if (projectURL == null) {
	    status = StatusUtils.errorStatus(NLS.bind(AxisCreationUIMessages.MSG_ERROR_PROJECT_URL, new String[] { serverProject.toString()}));
	    environment.getStatusHandler().reportError(status);
	    return status;		  
	} else {
		javaWSDLParam.setProjectURL(projectURL);
	}
	
    return Status.OK_STATUS;
    
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
	  IPath wsdlPath = J2EEUtils.getWebContentPath(serverProject ).append("wsdl");
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
      return ResourceUtils.getJavaSourceLocation(serverProject).toString();
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

public String getServiceServerTypeID() {
	return serviceServerTypeID_;
}

public void setServiceServerTypeID(String serviceServerTypeID) {
	this.serviceServerTypeID_ = serviceServerTypeID;
}
}
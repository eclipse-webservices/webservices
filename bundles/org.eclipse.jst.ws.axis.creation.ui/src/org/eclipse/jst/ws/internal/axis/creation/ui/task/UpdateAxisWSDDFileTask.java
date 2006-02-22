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
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.FileUtil;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.axis.creation.ui.plugin.WebServiceAxisCreationUIPlugin;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.common.BundleUtils;

public class UpdateAxisWSDDFileTask extends AbstractDataModelOperation {
	
	private final String DEPLOY_XSL = "deploy.xsl";	//$NON-NLS-1$
	private final String DEPLOY_BAK = "deploy.wsdd.bak";		//$NON-NLS-1$
	private final String CLASSNAME_PARAM = "newClassName";		//$NON-NLS-1$
	private JavaWSDLParameter javaWSDLParam_;
	private IProject serviceProject_;
    private String serviceServerTypeID_;

	public UpdateAxisWSDDFileTask() 
	{
	}
	
	/**
	* Execute UpdateAxisWSDDFileTask
	*/
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{    
		IEnvironment environment = getEnvironment();
	    IStatus status = Status.OK_STATUS;		
		if (javaWSDLParam_ == null) {
		  status = StatusUtils.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
		  environment.getStatusHandler().reportError(status);
		  return status;		  
		}

		IProject project = serviceProject_;
        
        String projectURL = null;
        if (serviceServerTypeID_ != null && serviceServerTypeID_.length()>0)
        {
		  projectURL = ServerUtils.getEncodedWebComponentURL(project, serviceServerTypeID_);
        }
        else
        {
          projectURL = "http://tempuri.org/";          
        }
        
		if (projectURL == null) {
		    status = StatusUtils.errorStatus(NLS.bind(AxisCreationUIMessages.MSG_ERROR_PROJECT_URL,new String[] {project.toString()}));
		    environment.getStatusHandler().reportError(status);
		    return status;		  
		} else {
			javaWSDLParam_.setProjectURL(projectURL);
		}
		String outputLocation = javaWSDLParam_.getJavaOutput();
		if (outputLocation == null) {
			return status;
		}

		String webContentPath =	J2EEUtils.getWebContentContainer( project ).getLocation().toString();
		try {

			if (javaWSDLParam_.getDeploymentFiles() != null
				&& javaWSDLParam_.getDeploymentFiles().length > 0) {

				String wsdd_deploy = javaWSDLParam_.getDeploymentFiles()[0];
				IPath deployPath = new Path(wsdd_deploy);
				String deployBackup =
					deployPath
						.removeLastSegments(1)
						.append(DEPLOY_BAK)
						.toString();
				FileUtil.createTargetFile(wsdd_deploy, deployBackup, true);
				String deployXSL = getPluginFilePath(DEPLOY_XSL).toString();

				TransformerFactory tFactory = TransformerFactory.newInstance();

				// Use the TransformerFactory to instantiate a Transformer that will work with  
				// the stylesheet you specify. This method call also processes the stylesheet
				// into a compiled Templates object.
				Transformer transformer =
					tFactory.newTransformer(new StreamSource(deployXSL));
				transformer.setParameter(CLASSNAME_PARAM, javaWSDLParam_.getBeanName());

				// Use the Transformer to apply the associated Templates object to an XML document
				// (foo.xml) and write the output to a file (foo.out).
        FileOutputStream wsddStream = new FileOutputStream( wsdd_deploy );
        
        try
        {
				  transformer.transform( new StreamSource(deployBackup), new StreamResult(wsddStream) );
        }
        finally
        {
          wsddStream.close();
        }
        
        IPath eclipseDeployPath = findPathFromFilePath( deployPath );
        
        if( eclipseDeployPath != null )
        {
          IResource deployFile = ResourceUtils.findResource( eclipseDeployPath );
          
          if( deployFile != null )
          {
            deployFile.refreshLocal( IResource.DEPTH_ZERO, monitor );
          }
        }
			}

		} catch (Exception e) {
		    String[] errorMsgStrings = new String[]{project.toString(), outputLocation.toString(), webContentPath.toString()}; 
		    status = StatusUtils.errorStatus(NLS.bind(AxisCreationUIMessages.MSG_ERROR_UPDATE_AXIS_WSDD, errorMsgStrings), e);
		    environment.getStatusHandler().reportError(status);
		    return status;		  		  
		}
		
		return status;
	}

  // This method attempts to convert a path that is relative to a file system to a path
  // that is relative to the workspace.
  private IPath findPathFromFilePath( IPath filePath )
  {
    IPath result = null;
    
    IPath installLocation = Platform.getLocation();
    
    if( installLocation.matchingFirstSegments( filePath ) == installLocation.segmentCount() )
    {
      filePath = filePath.removeFirstSegments( installLocation.segmentCount() );
      result = filePath.setDevice( null );
    }
    
    return result;
  }
  
	private IPath getPluginFilePath(String pluginfileName)
		throws CoreException {
		try {

			URL localURL =
				Platform.asLocalURL(
					BundleUtils.getURLFromBundle( WebServiceAxisCreationUIPlugin.ID, pluginfileName));
			return new Path(localURL.getFile());
		} catch (MalformedURLException e) {
			throw new CoreException(
				new org.eclipse.core.runtime.Status(
					IStatus.WARNING,
					WebServiceAxisCreationUIPlugin.ID,
					0,
					AxisCreationUIMessages.MSG_PLUGIN_FILE_URL,
					e));
		} catch (IOException e) {
			throw new CoreException(
				new org.eclipse.core.runtime.Status(
					IStatus.WARNING,
					WebServiceAxisCreationUIPlugin.ID,
					0,
					AxisCreationUIMessages.MSG_PLUGIN_FILE_URL,
					e));
		}
	}


	/**
	* Sets the javaWSDLParam.
	* @param javaWSDLParam The javaWSDLParam to set
	*/
	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		javaWSDLParam_ = javaWSDLParam;
	}
	
	public JavaWSDLParameter getJavaWSDLParam()
	{
		return javaWSDLParam_;
	}	

	public void setServiceProject(IProject serviceProject)
	{
	  serviceProject_ = serviceProject;
	}
	
	// rm 
	/*
	public void setModel(Model model)
	{
	  model_ = model;
	}
	*/
    public void setServiceServerTypeID(String id)
    {
      serviceServerTypeID_ = id;
    }    
}

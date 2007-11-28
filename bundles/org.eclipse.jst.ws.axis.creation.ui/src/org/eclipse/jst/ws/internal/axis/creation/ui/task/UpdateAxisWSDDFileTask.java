/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
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
 * 20060330   124667 kathy@ca.ibm.com - Kathy Chan
 * 20060517   134104 kathy@ca.ibm.com - Kathy Chan
 * 20071030   128419 kelvinhc@ca.ibm.com - Kelvin H. Cheung
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.FileUtil;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.axis.creation.ui.plugin.WebServiceAxisCreationUIPlugin;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class UpdateAxisWSDDFileTask extends AbstractDataModelOperation {
	
	private final String DEPLOY_XSL = "deploy.xsl";	//$NON-NLS-1$
	private final String DEPLOY_BAK = "deploy.wsdd.bak";		//$NON-NLS-1$
	private final String CLASSNAME_PARAM = "newClassName";		//$NON-NLS-1$
	private JavaWSDLParameter javaWSDLParam_;
	private IProject serviceProject_;
    
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

				TransformerFactory tFactory = TransformerFactory.newInstance();

				// Use the TransformerFactory to instantiate a Transformer that will work with  
				// the stylesheet you specify. This method call also processes the stylesheet
				// into a compiled Templates object.
				Transformer transformer =
					tFactory.newTransformer(new StreamSource(getPluginFileAsInputStream(DEPLOY_XSL)));
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


        IResource deployFile = ResourceUtils.findResourceAtLocation(wsdd_deploy, project);
        if( deployFile != null )
        {
        	deployFile.refreshLocal( IResource.DEPTH_ZERO, monitor );
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
	
	private InputStream getPluginFileAsInputStream(String pluginfileName) throws CoreException {
		try {
			InputStream is = WebServiceAxisCreationUIPlugin.getInstance().getBundle().getEntry(pluginfileName).openStream();
		
			return is;
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
	  
}

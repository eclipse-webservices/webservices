/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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


import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.ClasspathUtils;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.FileUtil;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.PlatformUtils;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.WSDLUtils;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsil.Utils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

public class DefaultsForServerJavaWSDLCommand extends AbstractDataModelOperation {

	private JavaWSDLParameter javaWSDLParam_ = null;
	private IProject serviceProject_;
	private String javaBeanName_; // this needs to be set by the extension with initial selection
	private String WSDLServiceURL_;
	private String WSDLServicePathname_;
	private WebServicesParser WSParser_;
    private String serviceServerTypeID_;
	
	private final String WSDL_FOLDER = "wsdl"; //$NON-NLS-1$
	public final String SERVICE_EXT = "/services/"; //$NON-NLS-1$
	private final String WSDL_EXT = "wsdl"; //$NON-NLS-1$
	public final byte MODE_BEAN = (byte) 0;
	public final String SERVICE_NAME_EXT = "Service"; //$NON-NLS-1$

	public DefaultsForServerJavaWSDLCommand( ) 
	{
	}
	

	/**
	* Execute DefaultsForJavaToWSDLTask
	*/
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();

		IStatus status;
		if (javaWSDLParam_ == null) {
			status = StatusUtils.errorStatus( AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
			environment.getStatusHandler().reportError(status);
			return status;
		}
		
		if (javaBeanName_ == null) {  // either set by extension point or bean class page
			javaBeanName_ = javaWSDLParam_.getBeanName();
			if (javaBeanName_ == null) {
				//rm javaBeanName_ = isdElement.getJavaBeanName();
				javaWSDLParam_.setBeanName(javaBeanName_);
			}
		}
		// rm WSParser_ =	(WebServicesParser) wse_.getWSParser();
		
		javaWSDLParam_.setServerSide(JavaWSDLParameter.SERVER_SIDE_BEAN);
		javaWSDLParam_.setSkeletonDeploy(false);

		javaWSDLParam_.setBeanName(javaBeanName_);
		String classpath = ClasspathUtils.getInstance().getClasspathString(serviceProject_);
		javaWSDLParam_.setClasspath(classpath);

		String simpleBeanName = javaBeanName_;
		if (javaBeanName_ != null) {
			int index = javaBeanName_.lastIndexOf('.');
			if (index != -1) {
				simpleBeanName = javaBeanName_.substring(index + 1);
			}
		}
		String namespace = WSDLUtils.makeNamespace(javaWSDLParam_.getBeanName());
		javaWSDLParam_.setNamespace(namespace);

		javaWSDLParam_.setPortTypeName(simpleBeanName);
		javaWSDLParam_.setServiceName(simpleBeanName + SERVICE_NAME_EXT);

		IPath moduleServerRoot = null;

		IPath modulePath = serviceProject_.getFullPath();
		IPath webinfPath = serviceProject_.getFullPath();
		try {
			if (J2EEUtils.isWebComponent(serviceProject_)){
				moduleServerRoot = ResourceUtils.getJavaSourceLocation(serviceProject_);
				modulePath = J2EEUtils.getWebContentPath(serviceProject_);
				webinfPath = J2EEUtils.getWebInfPath( serviceProject_ );				
			}
		} catch (Exception e) {
			status =  StatusUtils.errorStatus( AxisConsumptionUIMessages.MSG_ERROR_DEFAULT_BEAN, e );
			environment.getStatusHandler().reportError(status);
			return status;
		}

		IPath wsdlPath =
			modulePath.append(WSDL_FOLDER).append(simpleBeanName).addFileExtension(WSDL_EXT);

		try{
			IFolder folder = ResourceUtils.getWorkspaceRoot().getFolder(modulePath.append(WSDL_FOLDER));
			FileUtil.createFolder(folder, true, true);
		
		}
		catch(CoreException e){
			status = StatusUtils.errorStatus( AxisConsumptionUIMessages.MSG_ERROR_WRITE_WSDL, e );
			environment.getStatusHandler().reportError(status);
			return status;
		}
		
		String wsdlLocation = ResourceUtils.getWorkspaceRoot().getFile(wsdlPath).getLocation().toString();

		javaWSDLParam_.setOutputWsdlLocation(wsdlLocation);
		javaWSDLParam_.setInputWsdlLocation(wsdlLocation);
		WSDLServicePathname_ = wsdlPath.toString();

		if (wsdlPath != null) {
			String wsdlURL = PlatformUtils.getFileURLFromPath(new Path(wsdlLocation));
			WSDLServiceURL_ = wsdlURL;
			// set parser 
			if (wsdlURL == null || wsdlURL.length() <= 0) {
				IResource res =
					ResourceUtils.findResource(WSDLServicePathname_);
				if (res != null)
					wsdlURL = (new Utils()).toFileSystemURI(res);
			}
			if (wsdlURL != null && wsdlURL.length() > 0) {
				if (WSParser_ == null) {
					WSParser_ = new WebServicesParserExt();
				}
			}
		}

		javaWSDLParam_.setStyle(JavaWSDLParameter.STYLE_WRAPPED);
		javaWSDLParam_.setUse(JavaWSDLParameter.USE_LITERAL);

        String projectURL = null;
        if (serviceServerTypeID_ != null && serviceServerTypeID_.length()>0)
        {
		  projectURL = ServerUtils.getEncodedWebComponentURL(serviceProject_, serviceServerTypeID_);          
        }
        else
        {
          projectURL = "http://tempuri.org/";          
        }
        
		if (projectURL == null) {
			status = StatusUtils.errorStatus(NLS.bind(AxisCreationUIMessages.MSG_ERROR_PROJECT_URL, new String[] {serviceProject_.getName()}));
			environment.getStatusHandler().reportError(status);
			return status;
		}
		
		String serviceURL = projectURL + SERVICE_EXT + simpleBeanName;
		javaWSDLParam_.setUrlLocation(serviceURL);
		
		javaWSDLParam_.setMetaInfOnly(true);

		String javaOutput =	ResourceUtils.findResource(moduleServerRoot).getLocation().toString();
	
		String serviceName = javaWSDLParam_.getServiceName();
		IPath outputPath =	ResourceUtils.findResource(webinfPath).getLocation();
		String output = serviceProject_.getFullPath().toString();
		if (outputPath!=null)
		 output = outputPath.append(serviceName).toString();

		javaWSDLParam_.setJavaOutput(javaOutput);
		javaWSDLParam_.setOutput(output);
		
		return Status.OK_STATUS;
	}

	/**
	 * Returns the javaWSDLParam.
	 * @return JavaWSDLParameter
	 */
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}

	/**
	 * Sets the javaWSDLParam.
	 * @param javaWSDLParam The javaWSDLParam to set
	 */
	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		this.javaWSDLParam_ = javaWSDLParam;
	}

	public void setServiceProject(IProject serviceProject) {
		this.serviceProject_ = serviceProject;
	}
	
	public void setJavaBeanName(String javaBeanName) {
		this.javaBeanName_ = javaBeanName;
	}
	
	public String getWSDLServiceURL() {
		return WSDLServiceURL_;
	}
	
	public String getWSDLServicePathname() {
		return WSDLServicePathname_;
	}
	
	public void setParser(WebServicesParser wsParser) {
		this.WSParser_ = wsParser;
	}
	
	public WebServicesParser getParser() {
		return WSParser_;
	}

  public void setObjectSelection(IStructuredSelection objectSelection)
  {
    if (objectSelection != null && !objectSelection.isEmpty())
    {
      Object object = objectSelection.getFirstElement();
      if (object instanceof String)
        setJavaBeanName((String)object);
    }
  }
  
  public void setServiceServerTypeID(String id)
  {
    serviceServerTypeID_ = id;
  }
}

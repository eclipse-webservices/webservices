/*******************************************************************************
 * Copyright (c) 2006, 2012 IBM Corporation and others.
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
 * 20060517   142327 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20061004   159356 kathy@ca.ibm.com - Kathy Chan, Get correct module root URL based on server chosen
 * 20100420   307152 kchong@ca.ibm.com - Keith Chong, Web Service deployment fails without web.xml
 * 20120409   376345 yenlu@ca.ibm.com, kchong@ca.ibm.com - Stability improvements to web services commands/operations
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.plugin.WebServiceAxisCreationUIPlugin;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.BackupSkelImplCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.Skeleton2WSDLCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

public class TDCodeGenOperation extends AbstractDataModelOperation {

	// CopyAxisJarCommand
	private CopyAxisJarCommand copyAxisJarCommand = null;
	
	// BackupSkelImplCommand
	private BackupSkelImplCommand backupSkelImplCommand = null;
	private WebServicesParser webServicesParser;
	private JavaWSDLParameter javaWSDLParam;
	private WebServiceInfo webServiceInfo;
	
	// WSDL2JavaCommand
	private WSDL2JavaCommand wsdl2JavaCommand = null;
	private String wsdlURI;
	private String httpBasicAuthUsername;
	private String httpBasicAuthPassword;
	
	// Skeleton2WSDLCommand
	private Skeleton2WSDLCommand skeleton2WSDLCommand = null;
	private IProject serverProject;
	private String serviceServerTypeID;
	private IServer serviceExistingServer;
	  
	// UpdateWebXMLCommand
	private UpdateWEBXMLCommand updateWebXMLCommand = null;

	// RefreshProjectCommand
	private RefreshProjectCommand refreshProjectCommand = null;
	
	// CreateDeploymentDescriptorCommand
	private CreateDeploymentDescriptorCommand createDDCommand = null;
	
	/**
	 * This command runs the commands passed by the constructor in a WorkspaceModifyOperation.
	 * The commands are listed above, with only some of their data registry parameters since some appear 
	 * to overlap the commands.
	 * @param backupSkelImpl
	 * @param wsdl2Java
	 * @param skeleton2WSDL
	 * @param updateWebXML
	 */
	public TDCodeGenOperation() { 
		copyAxisJarCommand = new CopyAxisJarCommand();
		backupSkelImplCommand = new BackupSkelImplCommand();
		wsdl2JavaCommand = new WSDL2JavaCommand();
		skeleton2WSDLCommand = new Skeleton2WSDLCommand();
		updateWebXMLCommand = new UpdateWEBXMLCommand();
		refreshProjectCommand = new RefreshProjectCommand();
		createDDCommand = new CreateDeploymentDescriptorCommand();
	}
	
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) {
		IEnvironment env = getEnvironment();
		TopDownWSModifyOperation tdOperation = new TopDownWSModifyOperation(info, env);
		try {
			tdOperation.run(monitor);
			
			//serverProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,monitor);
			BuildProjectCommand buildCommand = new BuildProjectCommand();
			buildCommand.setProject(serverProject);
			buildCommand.setEnvironment(env);
			buildCommand.setForceBuild(true);
			buildCommand.execute(monitor, info);			
			
			GenSkeletonAndUpdateDDOperation genSkeletonAndUpdateDDOperation = new GenSkeletonAndUpdateDDOperation(info, env);
			genSkeletonAndUpdateDDOperation.run(monitor);
		}
		catch(Exception e){
			IStatus status = new Status(IStatus.ERROR, WebServiceAxisCreationUIPlugin.ID, e.getMessage(), e);
			return status;
		}
		return Status.OK_STATUS;
	}
	
	private class GenSkeletonAndUpdateDDOperation extends WorkspaceModifyOperation
	{
		private IAdaptable info = null;
		private IEnvironment env = null;
		
		protected GenSkeletonAndUpdateDDOperation(IAdaptable adaptable, IEnvironment environment)
		{
			//super(serverProject);
			info = adaptable;
			env = environment;
		}

		@Override
		protected void execute(IProgressMonitor monitor) throws CoreException
		{
			// Skeleton2WSDLCommand
			skeleton2WSDLCommand.setEnvironment(env);
			skeleton2WSDLCommand.setWebServicesParser(webServicesParser);
			skeleton2WSDLCommand.setJavaWSDLParam(javaWSDLParam);
			skeleton2WSDLCommand.setServerProject(serverProject);
			skeleton2WSDLCommand.setServiceServerTypeID(serviceServerTypeID);
			skeleton2WSDLCommand.setServiceExistingServer(serviceExistingServer);
			IStatus status = skeleton2WSDLCommand.execute(monitor, info);
			if (status.getSeverity() == Status.ERROR) {
				throw new CoreException(status);
			}
			wsdlURI = skeleton2WSDLCommand.getWsdlURI();

			// create the deployment descriptor if it doesn't exist
			createDDCommand.setEnvironment(env);
			createDDCommand.setServerProject(serverProject);
			status = createDDCommand.execute(monitor, info);
			if (status.getSeverity() == Status.ERROR) {
				throw new CoreException(status);
			}

			// UpdateWebXMLCommand
			updateWebXMLCommand.setEnvironment(env);
			updateWebXMLCommand.setServerProject(serverProject);
			status = updateWebXMLCommand.execute(monitor, info);
			if (status.getSeverity() == Status.ERROR) {
				throw new CoreException(status);
			}			
			
		}
	}
	
	private class TopDownWSModifyOperation extends WorkspaceModifyOperation {

		private IAdaptable info = null;
		private IEnvironment env = null;
		
		protected TopDownWSModifyOperation(IAdaptable adaptable, IEnvironment environment){
			//super(serverProject);
			info = adaptable;
			env = environment;
		}
		
		protected void execute(IProgressMonitor monitor) throws CoreException {

			IStatus status = null;
			
		    // CopyAxisJarCommand
		    copyAxisJarCommand.setEnvironment(env);
		    copyAxisJarCommand.setProject(serverProject);
		    status = copyAxisJarCommand.execute(monitor, info);
		    if (status.getSeverity() == Status.ERROR){
		    	throw new CoreException(status);
		    }
			
			// backupSkelImplCommand
			backupSkelImplCommand.setEnvironment(env);
			backupSkelImplCommand.setWebServicesParser(webServicesParser);
			backupSkelImplCommand.setJavaWSDLParam(javaWSDLParam);
			backupSkelImplCommand.setWebServiceInfo(webServiceInfo);
			status = backupSkelImplCommand.execute(monitor, info);
			if (status.getSeverity() == Status.ERROR) {
				throw new CoreException(status);
			}

			// wsdl2JavaCommand
			wsdl2JavaCommand.setEnvironment(env);
			wsdl2JavaCommand.setWsdlURI(wsdlURI);
			wsdl2JavaCommand.setHttpBasicAuthUsername(httpBasicAuthUsername);
			wsdl2JavaCommand.setHttpBasicAuthPassword(httpBasicAuthPassword);
			wsdl2JavaCommand.setJavaWSDLParam(javaWSDLParam);
			status = wsdl2JavaCommand.execute(monitor, info);
			if (status.getSeverity() == Status.ERROR) {
				throw new CoreException(status);
			}
			javaWSDLParam = wsdl2JavaCommand.getJavaWSDLParam();
			
			// RefreshProjectCommand
			refreshProjectCommand.setEnvironment(env);
			refreshProjectCommand.setProject(serverProject);
			status = refreshProjectCommand.execute(monitor, info);
			if (status.getSeverity() == Status.ERROR) {
				throw new CoreException(status);
			}
			
		}
	
	}



	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam;
	}

	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		this.javaWSDLParam = javaWSDLParam;
	}

	public void setWebServiceInfo(WebServiceInfo webServiceInfo) {
		this.webServiceInfo = webServiceInfo;
	}

	public void setWebServicesParser(WebServicesParser webServicesParser) {
		this.webServicesParser = webServicesParser;
	}
	
	/**
	 * @param httpBasicAuthPassword
	 *            The httpBasicAuthPassword to set.
	 */
	public void setHttpBasicAuthPassword(String httpBasicAuthPassword) {
		this.httpBasicAuthPassword = httpBasicAuthPassword;
	}

	/**
	 * @param httpBasicAuthUsername
	 *            The httpBasicAuthUsername to set.
	 */
	public void setHttpBasicAuthUsername(String httpBasicAuthUsername) {
		this.httpBasicAuthUsername = httpBasicAuthUsername;
	}

	public void setWsdlURI(String wsdlURI) {
		this.wsdlURI = wsdlURI;
	}

	public String getWsdlURI() {
		return wsdlURI;
	}
	
	/**
	 * @param serverProject The serverProject to set.
	 */
	public void setServerProject(IProject serverProject) {
	  this.serverProject = serverProject;
	}
		  
	public void setServiceServerTypeID(String id) {
	  this.serviceServerTypeID = id;
	}
	
	public void setServiceExistingServer(IServer server) {
		  this.serviceExistingServer = server;
		}

}

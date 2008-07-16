/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060509   125094 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060515   115225 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060728   145426 kathy@ca.ibm.com - Kathy Chan
 * 20080603   235367 makandre@ca.ibm.com - Andrew Mak, "IWAB0014E Unexpected exception occurred" from ant task for web service client generation
 * 20080626   229867 makandre@ca.ibm.com - Andrew Mak, Missing method in generated proxy class
 * 20080709   240225 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.task;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

public class ClientCodeGenOperation extends AbstractDataModelOperation {
	
	private CopyAxisJarCommand copyAxisJarCommand = null;
	
	private WSDL2JavaCommand wsdl2JavaCommand = null;
	private JavaWSDLParameter javaWSDLParam;
	private String wsdlURI;
	
	private IProject project;
	
	private Stub2BeanCommand stub2BeanCommand = null;
	private WebServicesParser webServicesParser;
	private String outputFolder;
	private String proxyBean;
	private String proxyEndpoint;
	
	private RefreshProjectCommand refreshProjectCommand = null;

	public ClientCodeGenOperation(){
		copyAxisJarCommand = new CopyAxisJarCommand();
		wsdl2JavaCommand = new WSDL2JavaCommand();
		stub2BeanCommand = new Stub2BeanCommand();
		refreshProjectCommand = new RefreshProjectCommand();
	}
	
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) {

		IEnvironment env = getEnvironment();
		ClientWSModifyOperation buOperation = new ClientWSModifyOperation(info, env);
		try {
			buOperation.execute(monitor);
		}
		catch(CoreException ce){
			IStatus status = ce.getStatus();
			return status;
		}
		return Status.OK_STATUS;

	}

	
	private class ClientWSModifyOperation extends WorkspaceModifyOperation {
		
		  private IAdaptable info = null;
		  private IEnvironment env = null;

		  
		  protected ClientWSModifyOperation(IAdaptable adaptable, IEnvironment environment){
			  info = adaptable;
			  env = environment;
		  }
		  
		  protected void execute(IProgressMonitor monitor) throws CoreException{
		  
			    IStatus status = null;

			    // CopyAxisJarCommand
			    copyAxisJarCommand.setEnvironment(env);
			    copyAxisJarCommand.setProject(project);
			    status = copyAxisJarCommand.execute(monitor, info);
			    if (status.getSeverity() == Status.ERROR){
			    	throw new CoreException(status);
			    }
				
				// WSDL2JavaCommand
				wsdl2JavaCommand.setEnvironment(env);
				wsdl2JavaCommand.setJavaWSDLParam(javaWSDLParam);
				wsdl2JavaCommand.setWsdlURI(wsdlURI);
				status = wsdl2JavaCommand.execute(monitor, info);
				if (status.getSeverity() == Status.ERROR) {
					throw new CoreException(status);
				}
								
				// Stub2BeanCommand
				stub2BeanCommand.setEnvironment(env);
				stub2BeanCommand.setWebServicesParser(webServicesParser);
				stub2BeanCommand.setOutputFolder(outputFolder);
				stub2BeanCommand.setJavaWSDLParam(javaWSDLParam);
				stub2BeanCommand.setClientProject(project);
				status = stub2BeanCommand.execute(monitor, info);
				if (status.getSeverity() == Status.ERROR) {
					throw new CoreException(status);
				}
				proxyBean = stub2BeanCommand.getProxyBean();
				proxyEndpoint = stub2BeanCommand.getProxyEndpoint();
			  
				// RefreshProjectCommand
				refreshProjectCommand.setEnvironment(env);
				refreshProjectCommand.setProject(project);
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

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public void setWebServicesParser(WebServicesParser webServicesParser) {
		this.webServicesParser = webServicesParser;
	}

	public void setWsdlURI(String wsdlURI) {
		this.wsdlURI = wsdlURI;
	}
		
	public String getProxyBean() {
		return proxyBean;
	}
	
	public String getProxyEndpoint() {
		return proxyEndpoint;
	}
	
}

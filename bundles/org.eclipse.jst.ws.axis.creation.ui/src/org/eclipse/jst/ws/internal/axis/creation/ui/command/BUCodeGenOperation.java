/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 *      
 * 20060509   125094 sengpl@ca.ibm.com - Seng Phung-Lu, Use WorkspaceModifyOperation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.Java2WSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.UpdateAxisWSDDFileTask;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class BUCodeGenOperation extends AbstractDataModelOperation {

	// Java2WSDLCommand
	private Java2WSDLCommand java2WSDLCommand = null;
	private JavaWSDLParameter javaWSDLParam;  
	private String wsdlURI; 
	
	// wsdl2JavaCommand
	private WSDL2JavaCommand wsdl2JavaCommand = null;
		
	// UpdateAxisWSDDFileTask
	private UpdateAxisWSDDFileTask updateAxisWsddCommand = null;
	private IProject serviceProject;
	
	// UpdateWEBXMLCommand
	private UpdateWEBXMLCommand updateWebXMLCommand = null;
	
	/**
	 * This command runs the commands in the constructor in a WorkspaceModifyOperation.
	 * The commands are listed above, along with their relevant data registry parameters 
	 * @param java2WSDL
	 * @param wsdl2Java
	 * @param updateAxisWsdd
	 * @param updateWebXML
	 */
	public BUCodeGenOperation(){
		java2WSDLCommand = new Java2WSDLCommand();
		wsdl2JavaCommand = new WSDL2JavaCommand();
		updateAxisWsddCommand = new UpdateAxisWSDDFileTask();
		updateWebXMLCommand = new UpdateWEBXMLCommand();
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info) {

		IEnvironment env = getEnvironment();
		BottomUpWSModifyOperation buOperation = new BottomUpWSModifyOperation(info, env);
		try {
			buOperation.execute(monitor);
		}
		catch(CoreException ce){
			IStatus status = ce.getStatus();
			return status;
		}
		return Status.OK_STATUS;

	}

	private class BottomUpWSModifyOperation extends WorkspaceModifyOperation {
		
		  private IAdaptable info = null;
		  private IEnvironment env = null;

		  
		  protected BottomUpWSModifyOperation(IAdaptable adaptable, IEnvironment environment){
			  info = adaptable;
			  env = environment;
		  }
		  
		  protected void execute(IProgressMonitor monitor) throws CoreException{
		  
			    IStatus status = null;
			  
			  	// Java2WSDLCommand
				java2WSDLCommand.setEnvironment(env);
				java2WSDLCommand.setJavaWSDLParam(javaWSDLParam);
				status = java2WSDLCommand.execute(monitor, info);
				if (status.getSeverity() == Status.ERROR) {
					throw new CoreException(status);
				}
				wsdlURI = java2WSDLCommand.getWsdlURI();
				
				// WSDL2JavaCommand
				wsdl2JavaCommand.setEnvironment(env);
				wsdl2JavaCommand.setJavaWSDLParam(javaWSDLParam);
				status = wsdl2JavaCommand.execute(monitor, info);
				if (status.getSeverity() == Status.ERROR) {
					throw new CoreException(status);
				}
				javaWSDLParam = wsdl2JavaCommand.getJavaWSDLParam();
				
				// UpdateAxisWsddCommand
				updateAxisWsddCommand.setEnvironment(env);
				updateAxisWsddCommand.setJavaWSDLParam(javaWSDLParam);
				updateAxisWsddCommand.setServiceProject(serviceProject);
				status = updateAxisWsddCommand.execute(monitor, info);
				if (status.getSeverity() == Status.ERROR) {
					throw new CoreException(status);
				}
				javaWSDLParam = updateAxisWsddCommand.getJavaWSDLParam();
				
				// UpdateWebXMLCommand
				updateWebXMLCommand.setEnvironment(env);
				updateWebXMLCommand.setServerProject(serviceProject);
				status = updateWebXMLCommand.execute(monitor, info);
				if (status.getSeverity() == Status.ERROR) {
					throw new CoreException(status);
				}
			  
		  }
		
	}

	public String getWsdlURI() {
		return wsdlURI;
	}
	
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam;
	}

	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		this.javaWSDLParam = javaWSDLParam;
	}

	public void setServiceProject(IProject serviceProject) {
		this.serviceProject = serviceProject;
	}
	
}

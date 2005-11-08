/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.core.command;


import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.axis.tools.ant.wsdl.Java2WsdlAntTask;
import org.apache.axis.tools.ant.wsdl.NamespaceMapping;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * Commands are executable, undoable, redoable objects.
 * Every Command has a name and a description.
 */

public class Java2WSDLCommand extends AbstractDataModelOperation
{

	private JavaWSDLParameter javaWSDLParam_;

	public Java2WSDLCommand() {
	}	
	/**
	 * Constructor for Java2WSDLCommand.
	 */
	public Java2WSDLCommand(JavaWSDLParameter javaWSDLParam) {
		super();
		this.javaWSDLParam_ = javaWSDLParam;
	}

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();
		IStatus status;
		if (javaWSDLParam_ == null) {
			status = StatusUtils.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
			environment.getStatusHandler().reportError(status);
			return status;
		}

		if (javaWSDLParam_.getBeanName() == null) {
			status = StatusUtils.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
			environment.getStatusHandler().reportError(status);
			return status;
		}

		ProgressUtils.report(monitor, NLS.bind(AxisConsumptionCoreMessages.MSG_GENERATE_WSDL, javaWSDLParam_.getBeanName() ));

		return executeAntTask(environment);
	}

	protected IStatus executeAntTask(IEnvironment environment) {

		final class Emitter extends Java2WsdlAntTask {
			public Emitter() {
				super.setProject(new Project());
				super.getProject().init();
				super.setTaskType("axis"); //$NON-NLS-1$
				super.setTaskName("axis-java2wsdl"); //$NON-NLS-1$
				super.setOwningTarget(new Target());
			}
		}

		Emitter emitter = new Emitter();
		emitter.createClasspath().setPath(javaWSDLParam_.getClasspath());
		environment.getLog().log(ILog.INFO, 5008, this, "executeAntTask", "Class Path = "+ javaWSDLParam_.getClasspath());
		
		emitter.setPortTypeName(javaWSDLParam_.getPortTypeName());
		environment.getLog().log(ILog.INFO, 5009, this, "executeAntTask", "Port Type Name = "+ javaWSDLParam_.getPortTypeName());
		
		emitter.setServiceElementName(javaWSDLParam_.getServiceName());
		environment.getLog().log(ILog.INFO, 5010, this, "executeAntTask", "Service Name = "+ javaWSDLParam_.getServiceName());
		
		emitter.setLocation(javaWSDLParam_.getUrlLocation());
		environment.getLog().log(ILog.INFO, 5011, this, "executeAntTask", "URL Location = "+ javaWSDLParam_.getUrlLocation());
		
		emitter.setMethods(javaWSDLParam_.getMethodString());
		environment.getLog().log(ILog.INFO, 5012, this, "executeAntTask", "Methods = "+ javaWSDLParam_.getMethodString());
		
		emitter.setStyle(javaWSDLParam_.getStyle());
		environment.getLog().log(ILog.INFO, 5013, this, "executeAntTask", "Style = "+ javaWSDLParam_.getStyle());
		
		emitter.setUse(javaWSDLParam_.getUse());
		environment.getLog().log(ILog.INFO, 5014, this, "executeAntTask", "Use = "+ javaWSDLParam_.getUse());
		
		emitter.setOutput(new File(javaWSDLParam_.getOutputWsdlLocation()));
		environment.getLog().log(ILog.INFO, 5015, this, "executeAntTask", "WSDL Location = "+ javaWSDLParam_.getOutputWsdlLocation());
		
		emitter.setNamespace(javaWSDLParam_.getNamespace());
		environment.getLog().log(ILog.INFO, 5016, this, "executeAntTask", "Name Space = "+ javaWSDLParam_.getNamespace());
		
		emitter.setClassName(javaWSDLParam_.getBeanName());
		environment.getLog().log(ILog.INFO, 5017, this, "executeAntTask", "Bean name = "+ javaWSDLParam_.getBeanName());
		
		emitter.setImplClass(javaWSDLParam_.getBeanName());
		
		HashMap mappings = javaWSDLParam_.getMappings();
		if(mappings != null){
			Iterator keys = mappings.keySet().iterator();
			while(keys.hasNext()){
				String pakage = (String)keys.next();
				String namespace = (String)mappings.get(pakage);
				NamespaceMapping map = new NamespaceMapping();
				map.setPackage(pakage);
				map.setNamespace(namespace);
				emitter.addMapping(map);		
			}
		}
		
	
		try {
			emitter.execute();
		} catch (BuildException e) {
			environment.getLog().log(ILog.ERROR, 5018, this, "executeAntTask", e);
			IStatus status = StatusUtils.errorStatus(
			AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_GENERATE + " " //$NON-NLS-1$
			+e.getCause().toString());
			environment.getStatusHandler().reportError(status);
			return status;
		}
		return Status.OK_STATUS;

	}
	
	public Status undo(IEnvironment environment) {
		return null;
	}

	public Status redo(IEnvironment environment) {
		return null;
	}

	/**
	 * Returns the javaWSDLParam.
	 * @return JavaWSDLParameter
	 */
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}

	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParm) {
		this.javaWSDLParam_ = javaWSDLParm;
	}
	
	public String getWsdlURI()
	{
	  File file = new File(getJavaWSDLParam().getOutputWsdlLocation()); 
	    String url = "";
		  try {
	    	url = file.toURL().toString();
	    }
		  catch(MalformedURLException mue){}
		return url;
	}
}

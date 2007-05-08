/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070205   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070508   175030 sandakith@wso2.com - Lahiru Sandakith, WSDL not passed to Axis2 client fix
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.consumption.core.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.data.DataModel;
import org.eclipse.jst.ws.axis2.consumption.core.messages.Axis2ConsumptionUIMessages;
import org.eclipse.jst.ws.axis2.consumption.core.utils.DefaultCodegenUtil;
import org.eclipse.jst.ws.axis2.core.context.ServiceContext;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;

public class Axis2ClientDefaultingCommand extends AbstractDataModelOperation {
	
	DataModel model;
	private IWebServiceClient ws;
	IStatus status;
	
	public Axis2ClientDefaultingCommand(DataModel model, IWebServiceClient ws){
		this.model=model;
		this.ws=ws;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		//Default Settings for the Codegeneration
		String deployedWSDLURL = FacetContainerUtils.getDeployedWSDLURL(
	  								model.getWebProjectName().split("Client")[0],
	  								ServiceContext.getInstance().getServiceName());
		String wsFrameworkWSDLURL = ws.getWebServiceClientInfo().getWsdlURL();
 
		model.setWsdlURI(
				((wsFrameworkWSDLURL!=null) || (deployedWSDLURL!=wsFrameworkWSDLURL))?
						wsFrameworkWSDLURL:
							deployedWSDLURL
		);
		
		// WSE with Client alone. 
		ws.getWebServiceClientInfo().setWsdlURL(deployedWSDLURL);
		
		model.setDatabindingType(Axis2ConsumptionUIMessages.DATA_BINDING_ADB);
		model.setASync(false);
		model.setSync(false);
		
		DefaultCodegenUtil defaultCodegenUtil = new DefaultCodegenUtil(model);
		defaultCodegenUtil.populateModelParamsFromWSDL();
		
		status = Status.OK_STATUS;
		return status;
	}
	
	public DataModel getWebServiceDataModel(){
		return model;
	}

}

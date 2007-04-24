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
 * 20070206   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.utils.DefaultCodegenUtil;
import org.eclipse.jst.ws.axis2.core.context.BUServiceContext;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.jst.ws.axis2.creation.core.utils.CommonUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class Axis2DefaultingCommand extends AbstractDataModelOperation 
{
	private DataModel model;
	private IWebService ws;
	private int scenario;
	IStatus status;

	public Axis2DefaultingCommand( DataModel model ,IWebService ws, int scenario)
	{
		this.model = model;  
		this.ws=ws;
		this.scenario=scenario;
	}

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ){
		//Check wether the current dynamic Web Project is properly build to invoke web service
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(Axis2CoreUtils.tempRuntimeStatusFileLocation()));
			if (properties.containsKey(Axis2CoreUIMessages.PROPERTY_KEY_STATUS)){
				 status = Status.OK_STATUS;
				 model.setServerStatus(true);
			}else{
				status = StatusUtils.errorStatus(Axis2CoreUIMessages.ERROR_SERVER_IS_NOT_SET);
			}
		} catch (FileNotFoundException e) {
			status = StatusUtils.errorStatus(Axis2CoreUIMessages.ERROR_INVALID_FILE_READ_WRITEL+
												Axis2CoreUIMessages.ERROR_SERVER_IS_NOT_SET);
		} catch (IOException e) {
			status = StatusUtils.errorStatus(Axis2CoreUIMessages.ERROR_INVALID_FILE_READ_WRITEL+
												Axis2CoreUIMessages.ERROR_SERVER_IS_NOT_SET);
		}
		
		if (scenario == WebServiceScenario.TOPDOWN) {
			model.setWsdlURI(ws.getWebServiceInfo().getWsdlURL());
			model.setDatabindingType(Axis2CreationUIMessages.DATA_BINDING_ADB);
			DefaultCodegenUtil defaultCodegenUtil = new DefaultCodegenUtil(model);
			defaultCodegenUtil.populateModelParamsFromWSDL();
			model.setServicesXML(true);
			model.setServerXMLCheck(true);
		}else if (scenario == WebServiceScenario.BOTTOMUP) {
			model.setServiceClass(ws.getWebServiceInfo().getImplURL());
			//set the service name inside BUServiceContext for used by client if invoke together
			BUServiceContext.getInstance().setServiceName(
					CommonUtils.classNameFromQualifiedName(ws.getWebServiceInfo().getImplURL())
					);
			
			model.setGenerateServicesXML(true);
		}else{
			//never come here
		}
		return status;      	
	}

	public DataModel getWebServiceDataModel()
	{
		return model;
	}
}

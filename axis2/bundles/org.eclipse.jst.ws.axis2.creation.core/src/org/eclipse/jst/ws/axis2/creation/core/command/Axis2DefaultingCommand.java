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
 * 20070508   175030 sandakith@wso2.com - Lahiru Sandakith, WSDL not passed to Axis2 client fix
 * 20070612   192047 sandakith@wso2.com - Lahiru Sandakith, 192047
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.utils.DefaultCodegenUtil;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.context.ServiceContext;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
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
			if (properties.containsKey(Axis2Constants.PROPERTY_KEY_STATUS)){
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
			model.setDatabindingType(Axis2Constants.DATA_BINDING_ADB);
			DefaultCodegenUtil defaultCodegenUtil = new DefaultCodegenUtil(model);
			defaultCodegenUtil.populateModelParamsFromWSDL();
			model.setServicesXML(true);
			model.setServerXMLCheck(true);
			ServiceContext.getInstance().setServiceName(model.getServiceName());
		}else if (scenario == WebServiceScenario.BOTTOMUP) {
			model.setServiceClass(ws.getWebServiceInfo().getImplURL());
			//set the service name inside BUServiceContext for used by client if invoke together
			ServiceContext.getInstance().setServiceName(
					CommonUtils.classNameFromQualifiedName(ws.getWebServiceInfo().getImplURL())
					);
			
			model.setGenerateServicesXML(true);
		}else{
			//never come here
		}
		
		// Fix for the Bugzilla Bug 175030
		// Axis2: WSDL representing Web service not passed to Axis2 client
		// After setting the initial wsdlURL return from the framework to the data model,
		// replace it with the deployed wsdlURL
		String deployedWSDLURL = FacetContainerUtils.getDeployedWSDLURL(
				ResourcesPlugin.getWorkspace().getRoot().getProject(model.getWebProjectName()),
				ws.getWebServiceInfo().getServerFactoryId(),
				ws.getWebServiceInfo().getServerInstanceId(),
				ServiceContext.getInstance().getServiceName());
		
		ws.getWebServiceInfo().setWsdlURL(deployedWSDLURL);
		
		return status;      	
	}

	public DataModel getWebServiceDataModel()
	{
		return model;
	}
}

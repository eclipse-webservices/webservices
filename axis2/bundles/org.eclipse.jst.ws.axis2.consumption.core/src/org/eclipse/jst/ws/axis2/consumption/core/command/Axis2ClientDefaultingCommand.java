/*******************************************************************************
 * Copyright (c) 2007, 2008 WSO2 Inc., IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070205   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070508   175030 sandakith@wso2.com - Lahiru Sandakith, WSDL not passed to Axis2 client fix
 * 20070612   192047 sandakith@wso2.com - Lahiru Sandakith, 192047
 * 20070612   192047 kathy@ca.ibm.com   - Kathy Chan
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20080622   241170 samindaw@wso2.com - Saminda Wijeratne, Axis2 preferences respected when click finish in 1st page
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.consumption.core.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.data.DataModel;
import org.eclipse.jst.ws.axis2.consumption.core.utils.DefaultCodegenUtil;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
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

		String wsdlURL = ws.getWebServiceClientInfo().getWsdlURL();
		model.setWsdlURI(wsdlURL);
		
		DefaultCodegenUtil defaultCodegenUtil = new DefaultCodegenUtil(model);
		defaultCodegenUtil.populateModelParamsFromWSDL();
		
		status = Status.OK_STATUS;
		return status;
	}
	
	public DataModel getWebServiceDataModel(){
		return model;
	}

}

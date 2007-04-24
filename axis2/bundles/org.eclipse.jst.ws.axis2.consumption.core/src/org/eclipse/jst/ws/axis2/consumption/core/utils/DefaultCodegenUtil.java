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
package org.eclipse.jst.ws.axis2.consumption.core.utils;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.data.Model;
import org.eclipse.jst.ws.axis2.consumption.core.messages.Axis2ConsumptionUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.ClassLoadingUtil;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;

public class DefaultCodegenUtil {
	
	private WSDLPropertyReader reader;
	private Model model;
	private List serviceQNameList = null;
	IStatus status;
	
	public DefaultCodegenUtil(org.eclipse.jst.ws.axis2.consumption.core.data.DataModel model){
		this.model=model;
	}
	
	public DefaultCodegenUtil(org.eclipse.jst.ws.axis2.creation.core.data.DataModel model){
		this.model=model;
	}

	/**
	 * populate the model for the default scenario from the wsdl url
	 */
	public void populateModelParamsFromWSDL() {
		status = Status.OK_STATUS;
		if (reader == null)
			reader = new WSDLPropertyReader();
		try {
			String lname = model.getWsdlURI();
			if (!"".equals(lname.trim())) {

				reader.readWSDL(model.getWebProjectName(), lname);

				this.serviceQNameList = reader.getServiceList();
				if (!serviceQNameList.isEmpty()) {
					// add the local part of the
					Object serviceQnameInstance = serviceQNameList.get(0);
					Class QNameClass = ClassLoadingUtil.loadClassFromAntClassLoader("javax.xml.namespace.QName");
					Method GetLocalPartMethod  = QNameClass.getMethod("getLocalPart", null);
					Object resultLocalPart = GetLocalPartMethod.invoke(serviceQnameInstance, null);
					model.setServiceName(resultLocalPart.toString());
					// load the ports
					 loadPortNames();
				} 
				populatePackageName();
				//populate the namespacess
				//loadNamespaces(reader.getDefinitionNamespaceMap());
			}
		} catch (Exception e) {
			status = StatusUtils.errorStatus(NLS.bind(
					Axis2ConsumptionUIMessages.ERROR_INVALID_WSDL_FILE_READ_WRITEL,
					new String[]{e.getLocalizedMessage()}), e);
		}
	}
	
	private void loadPortNames() {
			java.util.List ports = reader.getPortNameList(serviceQNameList.get(0));
					// add the local part of the
					model.setPortName(ports.get(0).toString());
	}
	
	private void populatePackageName() {
		model.setPackageText(reader.packageFromTargetNamespace());
	}

}

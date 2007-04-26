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
 * 20070213   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse 
 * 										the Axis2 runtime to the framework for 168762
 * 20070420   168762 sandakith@wso2.com - Lahiru Sandakith, Include the preference 
 * 										persist model parameters
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.plugin.data;

import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;

public class ServerModel {
	
	//Axis2 Runtime
	private static String axis2ServerPath;
	private static boolean axis2ServerPathRepresentsWar;
	
	//Axis2 Preferences
	//Service Codegen Options
	private static String serviceDatabinding = Axis2CoreUIMessages.ADB;
	private static boolean serviceTestcase = false;
	private static boolean serviceInterfaceSkeleton = false;
	private static boolean serviceGenerateAll = false;
	
	//Client Codegen Options
	private static boolean sync = false;
	private static boolean async = false;
	private static String cleintDatabinding = Axis2CoreUIMessages.ADB;
	private static boolean clientTestcase = false;
	private static boolean clientGenerateAll = false;
	
	//Service Archive Options
	private static String aarExtention = Axis2CoreUIMessages.AAR;

	public static String getAxis2ServerPath() {
		return axis2ServerPath;
	}

	public static void setAxis2ServerPath(String axis2ServerPathParam) {
		axis2ServerPath = axis2ServerPathParam;
	}

	public static boolean isAxis2ServerPathRepresentsWar() {
		return axis2ServerPathRepresentsWar;
}
	public static void setAxis2ServerPathRepresentsWar(
			boolean axis2ServerPathRepresentsWar) {
		ServerModel.axis2ServerPathRepresentsWar = axis2ServerPathRepresentsWar;
	}

	public static String getAarExtention() {
		return aarExtention;
	}

	public static void setAarExtention(String aarExtention) {
		ServerModel.aarExtention = aarExtention;
	}

	public static boolean isAsync() {
		return async;
	}

	public static void setAsync(boolean async) {
		ServerModel.async = async;
	}

	public static String getCleintDatabinding() {
		return cleintDatabinding;
	}

	public static void setCleintDatabinding(String cleintDatabinding) {
		ServerModel.cleintDatabinding = cleintDatabinding;
	}

	public static boolean isClientGenerateAll() {
		return clientGenerateAll;
	}

	public static void setClientGenerateAll(boolean clientGenerateAll) {
		ServerModel.clientGenerateAll = clientGenerateAll;
	}

	public static boolean isClientTestcase() {
		return clientTestcase;
	}

	public static void setClientTestcase(boolean clientTestcase) {
		ServerModel.clientTestcase = clientTestcase;
	}

	public static String getServiceDatabinding() {
		return serviceDatabinding;
	}

	public static void setServiceDatabinding(String serviceDatabinding) {
		ServerModel.serviceDatabinding = serviceDatabinding;
	}

	public static boolean isServiceGenerateAll() {
		return serviceGenerateAll;
	}

	public static void setServiceGenerateAll(boolean serviceGenerateAll) {
		ServerModel.serviceGenerateAll = serviceGenerateAll;
	}

	public static boolean isServiceInterfaceSkeleton() {
		return serviceInterfaceSkeleton;
	}

	public static void setServiceInterfaceSkeleton(boolean serviceInterfaceSkeleton) {
		ServerModel.serviceInterfaceSkeleton = serviceInterfaceSkeleton;
	}

	public static boolean isServiceTestcase() {
		return serviceTestcase;
	}

	public static void setServiceTestcase(boolean serviceTestcase) {
		ServerModel.serviceTestcase = serviceTestcase;
	}

	public static boolean isSync() {
		return sync;
	}

	public static void setSync(boolean sync) {
		ServerModel.sync = sync;
	}

}

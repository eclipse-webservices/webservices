/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071107 203826 kathy@ca.ibm.com - Kathy Chan
 * 20071130 203826 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsrt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.server.core.IRuntime;

/*
 * The IWebServiceRuntimeChecker interface allow the extender to have a way to check the 
 * the Web service runtime's compatibility with server type, project, project type and EAR.
 * This class can be provided as the runtimeChecker attribute in the 
 * "org.eclipse.jst.ws.consumption.ui.clientRuntimes" and
 * "org.eclipse.jst.ws.consumption.ui.serviceRuntimes" extension points.
 */

public interface IWebServiceRuntimeChecker {

	/**
	 * @param serverTypeId server type ID
	 * @param serverInstanaceId server instance ID
	 * @param projectName project name 
	 * @param projectTypeId project template ID, set to "" if the project specified by projectName already exist 
	 * @param earProjectName EAR project name
	 * @return IStatus
	 */
	public IStatus checkRuntimeCompatibility(String serverTypeId, String serverInstanceId, 
			String projectName, String projectTypeId, String earProjectName);
	
}

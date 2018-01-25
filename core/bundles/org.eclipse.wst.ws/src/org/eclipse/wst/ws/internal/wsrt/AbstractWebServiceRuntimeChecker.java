/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
 * 20080326   198439 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsrt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class AbstractWebServiceRuntimeChecker implements
		IWebServiceRuntimeChecker {

	public IStatus checkRuntimeCompatibility(String serverTypeId, String serverInstanceId,
			String projectName, String projectTypeId, String earProjectName) {
		return Status.OK_STATUS;
	}
	
	public IStatus checkServiceClientCompatibility(boolean serviceNeedEAR,
			String serviceEARName, String serviceProjectName,
			boolean clientNeedEAR, String clientEARName,
			String clientProjectName) {
		return Status.OK_STATUS;
	}
	
}

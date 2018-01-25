/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080326   198439 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.wsrt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebServiceRuntimeChecker;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntimeChecker;

public class AxisWebServiceRuntimeChecker extends
		AbstractWebServiceRuntimeChecker implements IWebServiceRuntimeChecker {
	
	public IStatus checkServiceClientCompatibility(boolean serviceNeedEAR,
			String serviceEARName, String serviceProjectName,
			boolean clientNeedEAR, String clientEARName,
			String clientProjectName) {

		if (serviceProjectName != null && serviceProjectName.equalsIgnoreCase(clientProjectName)) {
			return StatusUtils
			.errorStatus(ConsumptionUIMessages.MSG_SAME_CLIENT_AND_SERVICE_PROJECTS);
		}
		if (serviceNeedEAR && clientNeedEAR) {
			if (serviceEARName != null && serviceEARName.equals(clientEARName)) {
				return StatusUtils.warningStatus(NLS.bind(
						ConsumptionUIMessages.MSG_SAME_CLIENT_AND_SERVICE_EARS,
						new String[] { "EAR" }));
			}
		}
		return Status.OK_STATUS;
	}

}

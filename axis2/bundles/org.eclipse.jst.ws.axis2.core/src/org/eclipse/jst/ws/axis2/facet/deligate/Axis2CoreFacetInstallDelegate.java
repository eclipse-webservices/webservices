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
 * 20070213  168766 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  facet to the framework for 168766
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 * 20070606   177421 sandakith@wso2.com - fix web.xml wiped out when Axis2 facet
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.deligate;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.RuntimePropertyUtils;
import org.eclipse.jst.ws.axis2.facet.commands.Axis2WebservicesServerCommand;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class Axis2CoreFacetInstallDelegate implements IDelegate {

	private IStatus status;

	public void execute(IProject project, IProjectFacetVersion arg1, Object arg2,
			IProgressMonitor monitor) throws CoreException {
		monitor.beginTask(Axis2CoreUIMessages.PROGRESS_INSTALL_AXIS2_RUNTIME, 2 );

		Axis2WebservicesServerCommand command = new Axis2WebservicesServerCommand(project); 
		status = command.executeOverride(monitor);
		if (status.getCode() == Status.OK_STATUS.getCode() ){
			RuntimePropertyUtils.writeServerStausToPropertiesFile(
					Axis2Constants.SERVER_STATUS_PASS);
		}else{
			RuntimePropertyUtils.writeServerStausToPropertiesFile(
					Axis2Constants.SERVER_STATUS_FAIL);
			throw new CoreException(status);
		}
		monitor.worked( 1 );
		monitor.done();
	}

}

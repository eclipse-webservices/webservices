/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100420   307152 kchong@ca.ibm.com - Keith Chong, Web Service deployment fails without web.xml
 * 
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.project.JavaEEProjectUtilities;
import org.eclipse.jst.jee.project.facet.IAppClientCreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.IConnectorCreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.ICreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.IEJBCreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.IEarCreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.IWebCreateDeploymentFilesDataModelProperties;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class CreateDeploymentDescriptorCommand extends AbstractDataModelOperation {

	private IProject project;
	
	public CreateDeploymentDescriptorCommand() {
		super();
	}

	public CreateDeploymentDescriptorCommand(IDataModel model) {
		super(model);
	}

    public void setServerProject(IProject serverProject)
	{
	    this.project = serverProject;
	}

	protected boolean hasDeploymentDescriptor(IProject project) {
		boolean ret = true;
		IPath ddFilePath = null;
		if (JavaEEProjectUtilities.isEARProject(project)) {
			ddFilePath = new Path(J2EEConstants.APPLICATION_DD_URI);
		} else if (JavaEEProjectUtilities.isEJBProject(project)) {
			ddFilePath = new Path(J2EEConstants.EJBJAR_DD_URI);
		} else if (JavaEEProjectUtilities.isDynamicWebProject(project)) {
			ddFilePath = new Path(J2EEConstants.WEBAPP_DD_URI);
		} else if (JavaEEProjectUtilities.isApplicationClientProject(project)) {
			ddFilePath = new Path(J2EEConstants.APP_CLIENT_DD_URI);
		} else if (JavaEEProjectUtilities.isJCAProject(project)) {
			ddFilePath = new Path(J2EEConstants.RAR_DD_URI);
		}
		IVirtualComponent component = ComponentCore.createComponent(project);
		if (component.getRootFolder() != null
				&& component.getRootFolder().getUnderlyingFolder() != null) {
			IFile ddXmlFile = component.getRootFolder().getUnderlyingFolder()
					.getFile(ddFilePath);
			ret = ddXmlFile.exists();
		}
		return ret;
	}

	protected IDataModel getDataModel(IProject project) {
		Class dataModelClass = null;
		if (JavaEEProjectUtilities.isEARProject(project)) {
			dataModelClass = IEarCreateDeploymentFilesDataModelProperties.class;
		} else if (JavaEEProjectUtilities.isEJBProject(project)) {
			dataModelClass = IEJBCreateDeploymentFilesDataModelProperties.class;
		} else if (JavaEEProjectUtilities.isDynamicWebProject(project)) {
			dataModelClass = IWebCreateDeploymentFilesDataModelProperties.class;
		} else if (JavaEEProjectUtilities.isApplicationClientProject(project)) {
			dataModelClass = IAppClientCreateDeploymentFilesDataModelProperties.class;
		} else if (JavaEEProjectUtilities.isJCAProject(project)) {
			dataModelClass = IConnectorCreateDeploymentFilesDataModelProperties.class;
		}
		IDataModel dataModel = DataModelFactory.createDataModel(dataModelClass);
		dataModel.setProperty(
				ICreateDeploymentFilesDataModelProperties.TARGET_PROJECT,
				project);
		return dataModel;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info) {

		IEnvironment environment = getEnvironment();
		IStatus status = Status.OK_STATUS;
		if (project != null && !hasDeploymentDescriptor(project)) {
			try {
				getDataModel(project).getDefaultOperation().execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				status = StatusUtils.errorStatus(e.getMessage(), e);
			}
		}
		if (status.getSeverity() == Status.ERROR) {
			environment.getStatusHandler().reportError(status);
			return status;
		}
		return status;
	}
}

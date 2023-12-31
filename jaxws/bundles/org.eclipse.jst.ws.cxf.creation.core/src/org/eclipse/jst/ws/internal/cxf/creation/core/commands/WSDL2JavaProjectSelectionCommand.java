/*******************************************************************************
 * Copyright (c) 2008, 2009 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCoreMessages;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * This is stop gap workaround for bugs <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=243286">#243286</a>
 * and <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=243290">#243290</a>
 *
 *
 */
public class WSDL2JavaProjectSelectionCommand extends AbstractDataModelOperation {
    private WSDL2JavaDataModel model;
    private IProject initialProject;
    private IProject serverProject;
    private IProject currentProject;

    public WSDL2JavaProjectSelectionCommand(WSDL2JavaDataModel model) {
        this.model = model;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;

        if (currentProject == null && initialProject == null && serverProject != null) {
            status = Status.OK_STATUS;
            model.setProjectName(serverProject.getName());
            return status;
        }

        if (currentProject == null && initialProject != null && !initialProject.equals(serverProject)) {
            status = new Status(IStatus.WARNING, CXFCorePlugin.PLUGIN_ID, CXFCreationCoreMessages.bind(
                    CXFCreationCoreMessages.WSDL2JAVA_PROJECT_SELECTION_ERROR, new Object[]{
                            serverProject.getName(), initialProject.getName()}));
        } else if (initialProject == null && currentProject != null && !currentProject.equals(serverProject)) {
            status = new Status(IStatus.WARNING, CXFCorePlugin.PLUGIN_ID, CXFCreationCoreMessages.bind(
                    CXFCreationCoreMessages.WSDL2JAVA_PROJECT_SELECTION_ERROR, new Object[]{
                            serverProject.getName(), currentProject.getName()}));
        } else if (initialProject != null && currentProject != null && !currentProject.equals(serverProject)) {
            status = new Status(IStatus.WARNING, CXFCorePlugin.PLUGIN_ID, CXFCreationCoreMessages.bind(
                    CXFCreationCoreMessages.WSDL2JAVA_PROJECT_SELECTION_ERROR, new Object[]{
                            serverProject.getName(), currentProject.getName()}));
        } else {
            if (serverProject != null && serverProject.getProject() != null) {
                model.setProjectName(serverProject.getProject().getName());
                status = Status.OK_STATUS;
            }
        }

        if (!status.isOK()) {
            try {
                getEnvironment().getStatusHandler().report(status);
            } catch (StatusException e) {
                return new Status(IStatus.ERROR, CXFCorePlugin.PLUGIN_ID, 0, "", null);
            }
        }
        return status;
    }

    /*
     * The value to test against. Make sure the "Service Project"
     * and the project that contains the wsdl file match.
     */
    public void setServerProject(IProject serverProject) {
        this.serverProject = serverProject;
    }

    /*
     *
     * The initial project will be null when there's nothing selected in the project explorer
     * This forces the user to make a selection thus setting the current project
     * below.
     *
     * If there was an initial selection and the user changes from one top down service
     * type to another then we had to check.
     */
    public void setInitialProject(IProject project) {
        this.initialProject = project;
    }

    /*
     * If there was an initial selection in the project explorer the above value is set and
     * this is set to null.
     *
     * This will change however if the user modifies the text field or browses for a
     * wsdl file. Upon selecting a file in a valid location, this value gets set.
     * No problem in that situation.
     */
    public void setProject(IProject project) {
        this.currentProject = project;
    }

}

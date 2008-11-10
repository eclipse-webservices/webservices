/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.consumption.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jst.ws.internal.cxf.consumption.core.CXFConsumptionCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.internal.cxf.core.resources.JavaResourceChangeListener;
import org.eclipse.jst.ws.internal.cxf.core.utils.CommandLineUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.FileUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.JDTUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.LaunchUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * Executes the <code>org.apache.cxf.tools.wsdlto.WSDLToJava</code> command with the arguments
 * necessary to generate a client.
 * 
 * @author sclarke
 */
public class WSDL2JavaClientCommand extends AbstractDataModelOperation {
    public static final String CXF_TOOL_CLASS_NAME = "org.apache.cxf.tools.wsdlto.WSDLToJava"; //$NON-NLS-1$

    private WSDL2JavaDataModel model;

    private JavaResourceChangeListener javaResourceChangeListener;

    /**
     * Constructs a <code>WSDL2JavaClientCommand</code> object.
     * @param model the <code>WSDL2JavaDataModel</code> used to pass information
     * between commands.
     */
    public WSDL2JavaClientCommand(WSDL2JavaDataModel model) {
        this.model = model;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        javaResourceChangeListener = new JavaResourceChangeListener(model.getJavaSourceFolder());
        ResourcesPlugin.getWorkspace().addResourceChangeListener(javaResourceChangeListener,
                IResourceChangeEvent.POST_CHANGE);

        String[] progArgs = CommandLineUtils.getWSDL2JavaGenerateClientArguments(model);

        try {
            String projectName = model.getProjectName();

            IJavaProject javaProject = JDTUtils.getJavaProject(projectName);
            LaunchUtils.launch(javaProject, WSDL2JavaClientCommand.CXF_TOOL_CLASS_NAME, progArgs);
            FileUtils.copyW2JFilesFromTmp(this.model);
        } catch (CoreException ce) {
            status = ce.getStatus();
            CXFConsumptionCorePlugin.log(status);
        }

        ResourcesPlugin.getWorkspace().removeResourceChangeListener(javaResourceChangeListener);
        return status;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        List<IResource> changedResources = new ArrayList<IResource>();
        changedResources.addAll(javaResourceChangeListener.getChangedResources());
        if (changedResources.size() > 0) {
            for (IResource resource : changedResources) {
                try {
                    resource.delete(true, monitor);
                } catch (CoreException ce) {
                    status = ce.getStatus();
                    CXFConsumptionCorePlugin.log(status);
                }
            }
        }
        return status;
    }
}

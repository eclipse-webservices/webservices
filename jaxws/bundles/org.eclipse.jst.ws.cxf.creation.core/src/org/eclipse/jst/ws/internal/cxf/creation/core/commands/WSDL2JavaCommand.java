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
package org.eclipse.jst.ws.internal.cxf.creation.core.commands;

import java.io.IOException;
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
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.internal.cxf.core.resources.JavaResourceChangeListener;
import org.eclipse.jst.ws.internal.cxf.core.resources.WebContentChangeListener;
import org.eclipse.jst.ws.internal.cxf.core.utils.CommandLineUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.FileUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.LaunchUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.SpringUtils;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCorePlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

/**
 * Provides a wrapper around the <code>org.apache.cxf.tools.wsdlto.WSDLToJava</code> command.
 * 
 */
@SuppressWarnings("restriction")
public class WSDL2JavaCommand extends AbstractDataModelOperation {
    public static final String CXF_TOOL_CLASS_NAME = "org.apache.cxf.tools.wsdlto.WSDLToJava"; //$NON-NLS-1$
    
    private WSDL2JavaDataModel model;
    private IWebService ws;

    private JavaResourceChangeListener javaResourceChangeListener;
    private WebContentChangeListener webContentChangeListener;

    public WSDL2JavaCommand(WSDL2JavaDataModel model, IWebService ws) {
        this.model = model;
        this.ws = ws;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        javaResourceChangeListener = new JavaResourceChangeListener(model.getJavaSourceFolder());
        webContentChangeListener = new WebContentChangeListener(model.getProjectName());

        ResourcesPlugin.getWorkspace().addResourceChangeListener(javaResourceChangeListener,
                IResourceChangeEvent.POST_CHANGE);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(webContentChangeListener,
                IResourceChangeEvent.POST_CHANGE);

        String projectName = model.getProjectName();
        
        //TODO revisit
        ws.getWebServiceInfo().setImplURLs(new String[] {projectName + "/Impl.java"}); //$NON-NLS-1$

        String[] progArgs = CommandLineUtils.getWSDL2JavaProgramArguments(model);

        try {
            IJavaProject javaProject = JDTUtils.getJavaProject(projectName);
            LaunchUtils.launch(javaProject, CXF_TOOL_CLASS_NAME, progArgs);
            FileUtils.copyW2JFilesFromTmp(model);

            if (model.isGenerateImplementation()) {
                SpringUtils.createConfigurationFromWSDL(model);
            }

        } catch (CoreException ce) {
            status = ce.getStatus();
            CXFCreationCorePlugin.log(status);
        } catch (IOException ioe) {
            status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ioe.getLocalizedMessage());
            CXFCreationCorePlugin.log(status);  
        } finally {
            ResourcesPlugin.getWorkspace().removeResourceChangeListener(javaResourceChangeListener);
            ResourcesPlugin.getWorkspace().removeResourceChangeListener(webContentChangeListener);
            FileUtils.refreshProject(projectName, monitor);
        }

        return status;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        List<IResource> changedResources = new ArrayList<IResource>();
        changedResources.addAll(javaResourceChangeListener.getChangedResources());
        changedResources.addAll(webContentChangeListener.getChangedResources());
        if (changedResources.size() > 0) {
            for (IResource resource : changedResources) {
                try {
                    resource.delete(true, monitor);
                } catch (CoreException ce) {
                    status = ce.getStatus();
                    CXFCreationCorePlugin.log(status);
                }
            }
        }
        return status;
    }
    
    public CXFDataModel getCXFDataModel() {
        return model;
    }

}

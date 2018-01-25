/*******************************************************************************
 * Copyright (c) 2008, 2010 IONA Technologies PLC, Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IONA Technologies PLC, modify web.xml code initially part of org.eclipse.jst.ws.internal.cxf.facet.CXFFacetInstallDelegate
 *    Shane Clarke - added web.xml file creation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.core.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.core.Description;
import org.eclipse.jst.javaee.core.DisplayName;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.core.ParamValue;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.ServletMapping;
import org.eclipse.jst.javaee.web.SessionConfig;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.jst.jee.project.facet.ICreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.IWebCreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.resources.WebContentChangeListener;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCorePlugin;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class CreateDeploymentDescriptorCommand extends AbstractDataModelOperation {

    private String projectName;

    private WebContentChangeListener webContentChangeListener;

    public CreateDeploymentDescriptorCommand(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        webContentChangeListener = new WebContentChangeListener(projectName);

        ResourcesPlugin.getWorkspace().addResourceChangeListener(webContentChangeListener,
                IResourceChangeEvent.POST_CHANGE);

        final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        IPath webXmlFilePath = new Path("WEB-INF/web.xml"); //$NON-NLS-1$
        IVirtualComponent virtualComponent = ComponentCore.createComponent(project);
        if(virtualComponent.getRootFolder() != null
                && virtualComponent.getRootFolder().getUnderlyingFolder() != null) {
            IFile webXmlFile = virtualComponent.getRootFolder().getUnderlyingFolder().getFile(webXmlFilePath);
            if (!webXmlFile.exists()) {
                IDataModel dataModel = DataModelFactory.createDataModel(IWebCreateDeploymentFilesDataModelProperties.class);
                dataModel.setProperty(ICreateDeploymentFilesDataModelProperties.TARGET_PROJECT, project);
                dataModel.getDefaultOperation().execute( new NullProgressMonitor(), null);
            }
        }

        // Add CXF Servlet, Servlet Mapping and Session Config to web.xml
        final IModelProvider provider = ModelProviderManager.getModelProvider(project);
        provider.modify(new Runnable() {
            public void run() {
                Object modelProvider = provider.getModelObject();
                boolean useSpringAppContext = CXFCorePlugin.getDefault().getJava2WSContext().isUseSpringApplicationContext();
                // jst.web 2.5
                if (modelProvider instanceof org.eclipse.jst.javaee.web.WebApp) {
                    org.eclipse.jst.javaee.web.WebApp javaeeWebApp =
                        (org.eclipse.jst.javaee.web.WebApp) modelProvider;
                    addCXFJSTWEB25Servlet(project, javaeeWebApp);
                    if (useSpringAppContext) {
                        addSpringApplicationContextWeb25(project, javaeeWebApp);
                    }
                }
            }
        }, null);

        ResourcesPlugin.getWorkspace().removeResourceChangeListener(webContentChangeListener);

        return Status.OK_STATUS;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        List<IResource> changedResources = new ArrayList<IResource>();
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

    @SuppressWarnings("rawtypes")
    private void addSpringApplicationContextWeb25(IProject webProject,
            org.eclipse.jst.javaee.web.WebApp webapp) {
        List contextParams = webapp.getContextParams();
        for (int i = 0; i < contextParams.size(); i++) {
            ParamValue contextParam = (ParamValue) contextParams.get(i);
            if (contextParam.getParamName().equals("contextConfigLocation")) { //$NON-NLS-1$
                return;
            }
        }

        List listeners = webapp.getListeners();
        for (int i = 0; i < listeners.size(); i++) {
            org.eclipse.jst.javaee.core.Listener contextLoaderListener =
                (org.eclipse.jst.javaee.core.Listener) listeners.get(i);
            if (contextLoaderListener.getListenerClass().equals(
            "org.springframework.web.context.ContextLoaderListener")) { //$NON-NLS-1$
                return;
            }
        }

        JavaeeFactory javaeeFactory = JavaeeFactory.eINSTANCE;

        ParamValue configLocationParam = javaeeFactory.createParamValue();
        configLocationParam.setParamName("contextConfigLocation"); //$NON-NLS-1$
        configLocationParam.setParamValue("WEB-INF/cxf-beans.xml"); //$NON-NLS-1$

        webapp.getContextParams().add(configLocationParam);

        org.eclipse.jst.javaee.core.Listener contextLoaderListener = javaeeFactory.createListener();
        contextLoaderListener.setListenerClass("org.springframework.web.context.ContextLoaderListener"); //$NON-NLS-1$

        webapp.getListeners().add(contextLoaderListener);
    }

    @SuppressWarnings("rawtypes")
    private void addCXFJSTWEB25Servlet(IProject webProject, org.eclipse.jst.javaee.web.WebApp webapp) {
        List servlets = webapp.getServlets();
        for (int i = 0; i < servlets.size(); i++) {
            Servlet servlet = (Servlet) servlets.get(i);
            if (servlet.getServletName().equals("cxf")) { //$NON-NLS-1$
                return;
            }
        }

        // CXF Servlet
        WebFactory factory = WebFactory.eINSTANCE;
        Servlet cxfServlet = factory.createServlet();

        cxfServlet.setServletName("cxf"); //$NON-NLS-1$

        DisplayName cxfServletDisplayName = JavaeeFactory.eINSTANCE.createDisplayName();
        cxfServletDisplayName.setValue("cxf"); //$NON-NLS-1$
        cxfServlet.getDisplayNames().add(cxfServletDisplayName);

        Description cxfServletDescription = JavaeeFactory.eINSTANCE.createDescription();
        cxfServletDescription.setValue("Apache CXF Endpoint"); //$NON-NLS-1$
        cxfServlet.getDescriptions().add(cxfServletDescription);

        cxfServlet.setServletClass("org.apache.cxf.transport.servlet.CXFServlet"); //$NON-NLS-1$

        cxfServlet.setLoadOnStartup(Integer.valueOf(1));

        webapp.getServlets().add(cxfServlet);

        ServletMapping cxfServletMapping = factory.createServletMapping();
        cxfServletMapping.setServletName("cxf"); //$NON-NLS-1$
        UrlPatternType url = JavaeeFactory.eINSTANCE.createUrlPatternType();
        url.setValue("/services/*"); //$NON-NLS-1$
        cxfServletMapping.getUrlPatterns().add(url);
        webapp.getServletMappings().add(cxfServletMapping);

        SessionConfig sessionConfig = factory.createSessionConfig();
        sessionConfig.setSessionTimeout(new BigInteger("60")); //$NON-NLS-1$
        webapp.getSessionConfigs().add(sessionConfig);
    }
}

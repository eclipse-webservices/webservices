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
package org.eclipse.jst.ws.internal.cxf.facet;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.apt.core.util.AptConfig;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
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
import org.eclipse.jst.ws.internal.cxf.core.CXFCoreMessages;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Adds the CXF classpath container to the project.
 * <p>
 * Also sets up the web projects application deployment descriptor (web.xml file)
 * to use cxf-servlet or the Spring Application context (WEB-INF/beans.xml) for
 * endpoint configuration. Depends on a setting in the CXF preferences.
 * 
 */
public class CXFFacetInstallDelegate implements IDelegate {

    public void execute(final IProject project, IProjectFacetVersion fv, Object config,
            IProgressMonitor monitor) throws CoreException {

        CXFDataModel model = (CXFDataModel) config;

        //Set project default
        CXFCorePlugin.getDefault().setCXFRuntimeVersion(project, model.getDefaultRuntimeVersion());

        if (CXFCorePlugin.getDefault().getJava2WSContext().getDefaultRuntimeLocation().equals("")) { //$NON-NLS-1$
            throw new CoreException(new Status(Status.ERROR, CXFCorePlugin.PLUGIN_ID,
                    CXFCoreMessages.CXF_FACET_INSTALL_DELEGATE_RUNTIME_LOCATION_NOT_SET));
        }

        IPath cxfLibPath = new Path(model.getDefaultRuntimeLocation());

        if (!cxfLibPath.hasTrailingSeparator()) {
            cxfLibPath = cxfLibPath.addTrailingSeparator();
        }
        cxfLibPath = cxfLibPath.append("lib"); //$NON-NLS-1$

        IClasspathAttribute jstComponentDependency =
            JavaCore.newClasspathAttribute("org.eclipse.jst.component.dependency", "/WEB-INF/lib"); //$NON-NLS-1$
        IClasspathEntry cxfClasspathContainer =
            JavaCore.newContainerEntry(new Path(CXFCorePlugin.CXF_CLASSPATH_CONTAINER_ID),
                    new IAccessRule[0],
                    CXFCorePlugin.getDefault().getJava2WSContext().isExportCXFClasspathContainer()
                    ? new IClasspathAttribute[]{jstComponentDependency} : new IClasspathAttribute[]{},
                            true);

        JDTUtils.addToClasspath(JavaCore.create(project), cxfClasspathContainer);

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

        if (CXFCorePlugin.getDefault().getJava2WSContext().isAnnotationProcessingEnabled()) {
            AptConfig.setEnabled(JavaCore.create(project), true);
        }
    }

    @SuppressWarnings("unchecked")
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
        configLocationParam.setParamValue("WEB-INF/beans.xml"); //$NON-NLS-1$

        webapp.getContextParams().add(configLocationParam);

        org.eclipse.jst.javaee.core.Listener contextLoaderListener = javaeeFactory.createListener();
        contextLoaderListener.setListenerClass("org.springframework.web.context.ContextLoaderListener"); //$NON-NLS-1$

        webapp.getListeners().add(contextLoaderListener);
    }

    @SuppressWarnings("unchecked")
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

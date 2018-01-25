/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 * Bug #274293 - sudhan@progress.com
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.core.commands;

import java.io.IOException;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap12.SOAP12Address;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.utils.FileUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.SpringUtils;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCorePlugin;
import org.eclipse.jst.ws.jaxws.core.utils.WSDLUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

@SuppressWarnings("restriction")
public class CXFDeployCommand extends AbstractDataModelOperation {
    private static final String CXF_SERVLET = "org.apache.cxf.transport.servlet.CXFServlet"; //$NON-NLS-1$
    private IProject project;
    private IWebService webService;
    private CXFDataModel model;

    public CXFDeployCommand(String projectName, IWebService webService) {
        this.project = FileUtils.getProject(projectName);
        this.webService = webService;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;

        try {
            WebServiceInfo webServiceInfo = webService.getWebServiceInfo();

            String serverInstanceId = webServiceInfo.getServerInstanceId();
            String serverFactoryId = webServiceInfo.getServerFactoryId();
            if (serverInstanceId != null) {
                IServer server = ServerCore.findServer(serverInstanceId);
                String webCobComponentURL = ServerUtils.getEncodedWebComponentURL(project, serverFactoryId,
                        server);

                String urlPattern = ""; //$NON-NLS-1$

                IModelProvider provider = ModelProviderManager.getModelProvider(project);
                Object modelProvider = provider.getModelObject();
                // jst.web 2.5
                if (modelProvider instanceof org.eclipse.jst.javaee.web.WebApp) {
                    org.eclipse.jst.javaee.web.WebApp javaeeWebApp =
                        (org.eclipse.jst.javaee.web.WebApp) modelProvider;

                    urlPattern = getURLPattern(javaeeWebApp);
                }

                // jst.web 2.4
                if (modelProvider instanceof org.eclipse.jst.j2ee.webapplication.WebApp) {
                    org.eclipse.jst.j2ee.webapplication.WebApp webApp =
                        (org.eclipse.jst.j2ee.webapplication.WebApp) modelProvider;

                    urlPattern = getURLPattern(webApp);
                }

                urlPattern = urlPattern.substring(0, urlPattern.lastIndexOf("/")); //$NON-NLS-1$
                String jaxwsEndpointAddress = SpringUtils.getEndpointAddress(project,
                        model.getConfigId());

                String wsdlAddress = webCobComponentURL + urlPattern + jaxwsEndpointAddress;
                String wsdlURL = wsdlAddress + "?wsdl"; //$NON-NLS-1$

                webService.getWebServiceInfo().setWsdlURL(wsdlURL);

                Definition definition = model.getWsdlDefinition();

                ExtensibilityElement extensibilityElement = WSDLUtils.getEndpointAddress(definition);
                if (extensibilityElement != null) {
                    if (extensibilityElement instanceof SOAPAddress) {
                        ((SOAPAddress) extensibilityElement).setLocationURI(wsdlAddress);
                    }
                    if (extensibilityElement instanceof SOAP12Address) {
                        ((SOAP12Address) extensibilityElement).setLocationURI(wsdlAddress);
                    }

                    WSDLUtils.writeWSDL(model.getWsdlURL(), definition);
                }
            }
        } catch (IOException ioe) {
            status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ioe.getLocalizedMessage());
            CXFCreationCorePlugin.log(status);
        } catch (CoreException ce) {
            status = ce.getStatus();
            CXFCreationCorePlugin.log(status);
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    private String getURLPattern(org.eclipse.jst.javaee.web.WebApp javaeeWebApp) {
        List<org.eclipse.jst.javaee.web.Servlet> servlets = javaeeWebApp.getServlets();
        for (org.eclipse.jst.javaee.web.Servlet servlet : servlets) {
            if (servlet.getServletClass().equals(CXF_SERVLET)) {
                List<org.eclipse.jst.javaee.web.ServletMapping> servletMappings =
                    javaeeWebApp.getServletMappings();
                for (org.eclipse.jst.javaee.web.ServletMapping servletMapping : servletMappings) {
                    if (servletMapping.getServletName().equals(servlet.getServletName())) {
                        List<org.eclipse.jst.javaee.core.UrlPatternType> urlPatterns =
                            servletMapping.getUrlPatterns();
                        if (urlPatterns.size() > 0) {
                            String value = (urlPatterns.get(0)).getValue();
                            return value;
                        }
                    }
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")
    private String getURLPattern(org.eclipse.jst.j2ee.webapplication.WebApp webApp) {
        List<org.eclipse.jst.j2ee.webapplication.Servlet> servlets = webApp.getServlets();
        for (org.eclipse.jst.j2ee.webapplication.Servlet servlet : servlets) {
            if (servlet.getServletClass().getJavaName().equals(CXF_SERVLET)) {
                List<org.eclipse.jst.j2ee.webapplication.ServletMapping> servletMappings =
                    webApp.getServletMappings();
                for (org.eclipse.jst.j2ee.webapplication.ServletMapping servletMapping : servletMappings) {
                    if (servletMapping.getServlet().getServletName().equals(servlet.getServletName())) {
                        return servletMapping.getUrlPattern();
                    }
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    public void setCXFDataModel(CXFDataModel model) {
        this.model = model;
    }

    public String getClientComponentType() {
        return "template.cxf.core"; //$NON-NLS-1$
    }
}

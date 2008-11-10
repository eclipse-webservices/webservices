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
package org.eclipse.jst.ws.internal.cxf.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.xml.sax.InputSource;

/**
 * @author sclarke
 */
public final class WSDLUtils {
	public static final String WSDL_FILE_EXTENSION = ".wsdl"; //$NON-NLS-1$
	public static final IPath WSDL_FOLDER_PATH = new Path("wsdl/"); //$NON-NLS-1$
    private static final String WSDL_FILE_NAME_PATTERN = "[a-zA-Z0-9_\\-]+";//$NON-NLS-1$

    private WSDLUtils() {
    }
    
    public static String getWSDLFileNameFromURL(URL wsdlURL) {
        IPath wsdlPath = new Path(wsdlURL.toExternalForm());
        return wsdlPath.lastSegment();
    }

    public static Definition readWSDL(URL wsdlURL) {
        try {
        	InputSource inputSource = new InputSource(wsdlURL.openStream());
            WSDLFactory wsdlFactory = WSDLFactory.newInstance();
            WSDLReader wsdlReader = wsdlFactory.newWSDLReader();
            Definition definition = wsdlReader.readWSDL(wsdlURL.getPath(), inputSource);
            return definition;
        } catch (WSDLException wsdle) {
            CXFCorePlugin.log(wsdle);
        } catch (IOException ioe) {
            CXFCorePlugin.log(ioe);
        }
        return null;
    }
    
    public static void writeWSDL(CXFDataModel model) throws IOException, CoreException {
        URL wsdlURL = model.getWsdlURL();
        Definition definition = model.getWsdlDefinition();
        OutputStream wsdlOutputStream = null;
        try {
            File wsdlFile = new File(wsdlURL.toURI());
            wsdlOutputStream = new FileOutputStream(wsdlFile);
            WSDLFactory wsdlFactory = WSDLFactory.newInstance();
            WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
            wsdlWriter.writeWSDL(definition, wsdlOutputStream);
        } catch (WSDLException wsdle) {
            CXFCorePlugin.log(wsdle);
        } catch (URISyntaxException urise) {
            CXFCorePlugin.log(urise);
        } finally {
            if (wsdlOutputStream != null) {
                wsdlOutputStream.close();
            }
            WSDLUtils.getWSDLFolder(model.getProjectName()).getFile(model.getWsdlFileName()).refreshLocal(
                    IResource.DEPTH_INFINITE, new NullProgressMonitor());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void loadSpringConfigInformationFromWSDL(CXFDataModel model) {
        IFile wsdlFile = WSDLUtils.getWSDLFolder(model.getProjectName()).getFile(model.getWsdlFileName());
        if (wsdlFile.exists()) {
            try {
                model.setWsdlURL(wsdlFile.getLocationURI().toURL());
                Definition definition = WSDLUtils.readWSDL(model.getWsdlURL());
                Map servicesMap = definition.getServices();
                Set<Map.Entry> servicesSet = servicesMap.entrySet();
                for (Map.Entry serviceEntry : servicesSet) {
                    Service service = (Service) serviceEntry.getValue();
                    model.setServiceName(service.getQName().getLocalPart());
                    Map portsMap = service.getPorts();
                    Set<Map.Entry> portsSet = portsMap.entrySet();
                    for (Map.Entry portEntry : portsSet) {
                        Port port = (Port) portEntry.getValue();
                        model.setEndpointName(port.getName());
                     }
                }
                model.setWsdlDefinition(definition);
            } catch (MalformedURLException murle) {
                CXFCorePlugin.log(murle);
            }
        }
    }
    
    public static boolean isValidWSDLFileName(String wsdlFileName) {
        boolean isValid = true;
        if (wsdlFileName != null && wsdlFileName.trim().length() > 0 
                && wsdlFileName.indexOf(WSDL_FILE_EXTENSION) != -1 
                && wsdlFileName.substring(0, wsdlFileName.indexOf(WSDL_FILE_EXTENSION)).trim().length() > 0) {
            wsdlFileName = wsdlFileName.substring(0, wsdlFileName.indexOf(WSDL_FILE_EXTENSION)).trim();
            if (wsdlFileName.matches(WSDL_FILE_NAME_PATTERN)) {
                isValid = true;     
            } else {
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return isValid;
    }

    
    public static  IFolder getWSDLFolder(String projectName) {
        return WSDLUtils.getWSDLFolder(FileUtils.getProject(projectName));
    }
    
    public static IFolder getWSDLFolder(IProject project) {
        IPath wsdlFolderPath = FileUtils.getWebContentPath(project).append(WSDLUtils.WSDL_FOLDER_PATH);
        IFolder wsdlFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(wsdlFolderPath);
        if (!wsdlFolder.exists()) {
            try {
                wsdlFolder.create(true, true, new NullProgressMonitor());
            } catch (CoreException ce) {
                CXFCorePlugin.log(ce.getStatus());
            }
        }
        return wsdlFolder;
    }

    @SuppressWarnings("unchecked")
    public static SOAPAddress getEndpointAddress(Definition definition) {
        if (definition != null) {
            Map servicesMap = definition.getServices();
            Set<Map.Entry> servicesSet = servicesMap.entrySet();
            for (Map.Entry serviceEntry : servicesSet) {
                Service service = (Service) serviceEntry.getValue();
                Map portsMap = service.getPorts();
                Set<Map.Entry> portsSet = portsMap.entrySet();
                for (Map.Entry portEntry : portsSet) {
                    Port port = (Port) portEntry.getValue();
                    List extensibilityElements = port.getExtensibilityElements();
                    for (Object object : extensibilityElements) {
                        if (object instanceof SOAPAddress) {
                            SOAPAddress address = (SOAPAddress) object;
                            return address;      
                        }
                    }
                 }
            }
        }
        return null;
    }
    
    public static String getPackageNameFromNamespace(String namespace) {
        String packageName = ""; //$NON-NLS-1$
        try {
            List<String> packageNameElements = new ArrayList<String>();

            URL namespaceURL = new URL(namespace);

            // Remove www if there
            String authority = namespaceURL.getAuthority();
            if (authority.indexOf("www") != -1) { //$NON-NLS-1$
                authority = authority.substring(authority.indexOf(".") + 1, authority.length()); //$NON-NLS-1$
            }
            // Flip it
            List<String> authorityElements = Arrays.asList(authority.split("\\.")); //$NON-NLS-1$
            Collections.reverse(authorityElements);
            packageNameElements.addAll(authorityElements);

            String path = namespaceURL.getPath();
            List<String> pathElements = Arrays.asList(path.split("[/\\\\]")); //$NON-NLS-1$
            packageNameElements.addAll(pathElements);

            Iterator<String> packageIterator = packageNameElements.iterator();
            while (packageIterator.hasNext()) {
                String element = packageIterator.next();
                if (element.trim().length() > 0) {
                    packageName += element;
                    if (packageIterator.hasNext()) {
                        packageName += "."; //$NON-NLS-1$
                    }
                }
            }
        } catch (MalformedURLException murle) {
            CXFCorePlugin.log(murle);
        }
        return packageName.toLowerCase(Locale.getDefault());
    }
}

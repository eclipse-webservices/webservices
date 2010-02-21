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
package org.eclipse.jst.ws.jaxws.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.xml.sax.InputSource;

/**
 * WSDL Utility class.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under
 * development and expected to change significantly before reaching stability. It is being made available at
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public final class WSDLUtils {
    private static final String WSDL_FILE_NAME_PATTERN = "[a-zA-Z0-9_\\-]+.wsdl";//$NON-NLS-1$
    private static final String WSDL_QUERY = "?wsdl"; //$NON-NLS-1$

    private static final IPath WSDL_FOLDER_PATH = new Path("wsdl/"); //$NON-NLS-1$
    private static final int TIMEOUT = 30000;

    public static final String WSDL_FILE_EXTENSION = ".wsdl"; //$NON-NLS-1$

    private WSDLUtils() {
    }

    /**
     * Returns a <code>javax.wsdl.Definition</code> by reading the WSDL document at the given URL or null if none can be found
     * or if the connection times out.
     * @param wsdlURL the url of the wsdl document to read.
     * @return the definition described in the wsdl document pointed to by the given URL.
     * @throws IOException if an I/O exception occurs.
     */
    public static Definition readWSDL(URL wsdlURL) throws IOException {
        URLConnection urlConnection = wsdlURL.openConnection();
        urlConnection.setConnectTimeout(TIMEOUT);
        urlConnection.setReadTimeout(TIMEOUT);
        InputStream inputStream = null;
        try {
            inputStream = urlConnection.getInputStream();
            InputSource inputSource = new InputSource(inputStream);
            WSDLFactory wsdlFactory = WSDLFactory.newInstance();
            WSDLReader wsdlReader = wsdlFactory.newWSDLReader();
            Definition definition = wsdlReader.readWSDL(wsdlURL.getPath(), inputSource);
            return definition;
        } catch (WSDLException wsdle) {
            JAXWSCorePlugin.log(wsdle);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return null;
    }

    /**
     * Writes the given <code>javax.wsdl.Definition</code> to the wsdl document at the given URL.
     * @param wsdlURL the url of the wsdl document to write to.
     * @param definition the WSDL definition to be written.
     * @throws IOException  if an I/O exception occurs.
     * @throws CoreException if an exception occurs refreshing the file in the workspace if it exists.
     */
    public static void writeWSDL(URL wsdlURL, Definition definition) throws IOException, CoreException {
        URI wsdlURI = null;
        OutputStream wsdlOutputStream = null;
        try {
            wsdlURI = wsdlURL.toURI();
            File wsdlFile = new File(wsdlURI);
            wsdlOutputStream = new FileOutputStream(wsdlFile);
            WSDLFactory wsdlFactory = WSDLFactory.newInstance();
            WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
            wsdlWriter.writeWSDL(definition, wsdlOutputStream);
        } catch (WSDLException wsdle) {
            JAXWSCorePlugin.log(wsdle);
        } catch (URISyntaxException urise) {
            JAXWSCorePlugin.log(urise);
        } finally {
            if (wsdlOutputStream != null) {
                wsdlOutputStream.close();
                IFile file = ResourcesPlugin.getWorkspace().getRoot()
                .getFileForLocation(URIUtil.toPath(wsdlURI));
                if (file != null && file.exists()) {
                    file.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
                }
            }
        }
    }

    /**
     * Returns <code>true</code> if the given file name contains Alphanumeric characters, underscore '_',
     * dashes '-' and ends with the '.wsdl' extension. Otherwise returns <code>false</code>.
     * @param wsdlFileName the wsdl file name
     * @return <code>true</code> if valid, code>false</code> otherwise.
     */
    public static boolean isValidWSDLFileName(String wsdlFileName) {
        return wsdlFileName != null && wsdlFileName.matches(WSDL_FILE_NAME_PATTERN);
    }

    private static IProject getProject(String projectName) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    }

    /**
     * Returns the Web Content folder of the given project. The returned resource may not exist.
     * @param project the name of the web project
     * @return the web content folder
     */
    public static IFolder getWebContentFolder(IProject project) {
        return ResourcesPlugin.getWorkspace().getRoot().getFolder(
                WSDLUtils.getWebContentPath(project));
    }

    /**
     * Returns the WSDL folder of the project with the given name. The WSDL folder path is the projects web content folder
     * path appended with the 'WSDL' directory. The returned resource may not exist.
     * @param projectName the name of the web project
     * @return the wsdl folder
     */
    public static IFolder getWSDLFolder(String projectName) {
        return WSDLUtils.getWSDLFolder(WSDLUtils.getProject(projectName));
    }

    /**
     * Returns the WSDL folder of the given project. The WSDL folder path is the projects web content folder
     * path appended with the 'WSDL' directory. The returned resource may not exist.
     * @param projectName the name of the web project
     * @return the wsdl folder
     */
    public static IFolder getWSDLFolder(IProject project) {
        IPath wsdlFolderPath = WSDLUtils.getWebContentPath(project).append(WSDLUtils.WSDL_FOLDER_PATH);
        IFolder wsdlFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(wsdlFolderPath);
        if (!wsdlFolder.exists()) {
            try {
                wsdlFolder.create(true, true, new NullProgressMonitor());
            } catch (CoreException ce) {
                JAXWSCorePlugin.log(ce.getStatus());
            }
        }
        return wsdlFolder;
    }

    private static IPath getWebContentPath(IProject project) {
        return J2EEUtils.getWebContentPath(project).addTrailingSeparator();
    }

    /**
     * Returns the first <code>SOAPAddress</code> or <code>SOAP12Address</code> found in the given
     * <code>Definition</code> or null if none is found.
     * @param definition the given definition.
     * @return return one of:
     * <li>SOAPAddress<li>SOAP12Address<li>null if it can not find a soap address
     */
    @SuppressWarnings("unchecked")
    public static ExtensibilityElement getEndpointAddress(Definition definition) {
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
                        if (object instanceof SOAPAddress || object instanceof SOAP12Address) {
                            return (ExtensibilityElement) object;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the location URI from the first <code>SOAPAddress</code> or <code>SOAP12Address</code> found
     * in the given <code>Definition</code> or null if none is found. The returned location URI is appended with
     * the '?wsdl' query if the query was not present in the <code>SOAPAddress</code> or <code>SOAP12Address</code> location URI.
     * @param definition the given defintion.
     * @return the location URI or the first <code>SOAPAddress</code> or <code>SOAP12Address</code> found in the given definition.
     * @throws MalformedURLException if an error occurs testing the location URI for a query part.
     */
    public static String getWSDLLocation(Definition definition) throws MalformedURLException {
        ExtensibilityElement extensibilityElement = WSDLUtils.getEndpointAddress(definition);
        if (extensibilityElement != null) {
            String locationURI = getLocationURI(extensibilityElement);
            if (locationURI.length() > 0) {
                URL endpointURL = new URL(locationURI);
                if (endpointURL.getQuery() == null) {
                    locationURI += WSDL_QUERY;
                }
                return locationURI;
            }
        }
        return null;
    }

    private static String getLocationURI(ExtensibilityElement extensibilityElement) {
        if (extensibilityElement instanceof SOAPAddress) {
            return ((SOAPAddress) extensibilityElement).getLocationURI();
        }
        if (extensibilityElement instanceof SOAP12Address) {
            return ((SOAP12Address) extensibilityElement).getLocationURI();
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * Constructs a dot separated package name from a given namespace.
     * <p>E.g., the namespace “http://ws.example.com/” would return the Java package name “com.example.ws”.</p>
     * <p>N.B. This method does not preserve 'www' in the returned package name if it exists in the given namespace.</p>
     * @param namespace the given name.
     * @return a package name.
     */
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
            JAXWSCorePlugin.log(murle);
        }
        return packageName.toLowerCase();
    }
}

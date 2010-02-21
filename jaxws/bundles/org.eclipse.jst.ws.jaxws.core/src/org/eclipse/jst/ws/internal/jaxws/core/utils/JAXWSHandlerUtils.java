/*******************************************************************************
 * Copyright (c) 2010 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.sse.ui.internal.FormatProcessorsExtensionReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public final class JAXWSHandlerUtils {
    public static final String HANDLER_CHAINS = "handler-chains"; //$NON-NLS-1$
    public static final String HANDLER_CHAIN = "handler-chain"; //$NON-NLS-1$
    public static final String HANDLER = "handler"; //$NON-NLS-1$
    public static final String HANDLER_NAME = "handler-name"; //$NON-NLS-1$
    public static final String HANDLER_CLASS = "handler-class"; //$NON-NLS-1$

    public static final Namespace JAVAEE_NS = Namespace.getNamespace("http://java.sun.com/xml/ns/javaee"); //$NON-NLS-1$

    private JAXWSHandlerUtils() {
    }

    public static void createHandlerChainFile(IPath path, String handlerName, String handlerClass) {
        IFile handlerChainFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        if (!handlerChainFile.exists()) {
            try {
                IProgressMonitor progressMonitor = new NullProgressMonitor();
                handlerChainFile.create(new ByteArrayInputStream(new byte[] {}), true, progressMonitor);

                Element handlerChainsElement = new Element(HANDLER_CHAINS);
                handlerChainsElement.setNamespace(JAVAEE_NS);

                Element handlerChainElement = new Element(HANDLER_CHAIN, JAVAEE_NS);
                Element handlerElement = new Element(HANDLER, JAVAEE_NS);

                Element handlerNameElement = new Element(HANDLER_NAME, JAVAEE_NS);
                handlerNameElement.setText(handlerName);

                Element handlerClassElement = new Element(HANDLER_CLASS, JAVAEE_NS);
                handlerClassElement.setText(handlerClass);

                handlerElement.addContent(handlerNameElement);
                handlerElement.addContent(handlerClassElement);

                handlerChainElement.addContent(handlerElement);

                handlerChainsElement.addContent(handlerChainElement);

                Document document = new Document(handlerChainsElement);
                OutputStream outputStream = new FileOutputStream(handlerChainFile.getLocation().toFile());
                XMLOutputter outputter = new XMLOutputter();
                outputter.output(document, outputStream);
                handlerChainFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
                formatXMLFile(handlerChainFile);
            } catch (CoreException ce) {
                JAXWSCorePlugin.log(ce);
            } catch (FileNotFoundException fnfe) {
                JAXWSCorePlugin.log(fnfe);
            } catch (IOException ioe) {
                JAXWSCorePlugin.log(ioe);
            }
        }
    }

    public static void createHandlerChainFile(IPath path) {
        IFile handlerChainFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        if (!handlerChainFile.exists()) {
            try {
                IProgressMonitor progressMonitor = new NullProgressMonitor();
                handlerChainFile.create(new ByteArrayInputStream(new byte[] {}), true, progressMonitor);

                Element handlerChainsElement = new Element(HANDLER_CHAINS);
                handlerChainsElement.setNamespace(JAVAEE_NS);

                Document document = new Document(handlerChainsElement);
                OutputStream outputStream = new FileOutputStream(handlerChainFile.getLocation().toFile());
                XMLOutputter outputter = new XMLOutputter();
                outputter.output(document, outputStream);
                handlerChainFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
                formatXMLFile(handlerChainFile);
            } catch (CoreException ce) {
                JAXWSCorePlugin.log(ce);
            } catch (FileNotFoundException fnfe) {
                JAXWSCorePlugin.log(fnfe);
            } catch (IOException ioe) {
                JAXWSCorePlugin.log(ioe);
            }
        }
    }

    public static void addHandlerToHandlerChain(IPath path, String handlerName, String handlerClass) throws CoreException {
        IFile handlerChainFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        if (handlerChainFile.exists() && handlerChainFile.isAccessible()) {
            try {
                InputStream handlerInputStream = new FileInputStream(handlerChainFile.getLocation().toFile());
                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(handlerInputStream);
                Element root = document.getRootElement();

                Element handlerChainElement = root.getChild(HANDLER_CHAIN, JAVAEE_NS);

                if (handlerChainElement == null) {
                    handlerChainElement = new Element(HANDLER_CHAIN, JAVAEE_NS);
                    root.addContent(handlerChainElement);
                }

                Element handlerElement = new Element(HANDLER, JAVAEE_NS);

                Element handlerNameElement = new Element(HANDLER_NAME, JAVAEE_NS);
                handlerNameElement.setText(handlerName);

                Element handlerClassElement = new Element(HANDLER_CLASS, JAVAEE_NS);
                handlerClassElement.setText(handlerClass);

                handlerElement.addContent(handlerNameElement);
                handlerElement.addContent(handlerClassElement);

                handlerChainElement.addContent(handlerElement);

                OutputStream outputStream = new FileOutputStream(handlerChainFile.getLocation().toFile());
                XMLOutputter outputter = new XMLOutputter();
                outputter.output(document, outputStream);
            } catch (FileNotFoundException fnfe) {
                JAXWSCorePlugin.log(fnfe);
            } catch (IOException ioe) {
                JAXWSCorePlugin.log(ioe);
            } catch (JDOMException jdome) {
                JAXWSCorePlugin.log(jdome);
            } finally {
                //TODO Close streams
                handlerChainFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
                formatXMLFile(handlerChainFile);
            }
        }
    }

    public static void writeDocumentToFile(IPath filePath, Document document) throws CoreException {
        IFile handlerChainFile = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
        if (handlerChainFile.exists()) {
            try {
                if (isHandlerChainFile(handlerChainFile)) {
                    OutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(handlerChainFile.getLocation().toFile());
                        XMLOutputter outputter = new XMLOutputter();
                        outputter.output(document, outputStream);
                    } finally {
                        handlerChainFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
                        formatXMLFile(handlerChainFile);
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                }
            } catch (IOException ioe) {
                JAXWSCorePlugin.log(ioe);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean isHandlerDefined(IFile handlerChainFile, String handlerName, String handlerClass) throws IOException {
        if (isHandlerChainFile(handlerChainFile)) {
            SAXBuilder builder = new SAXBuilder();
            FileInputStream handlerChainInputSteam = new FileInputStream(handlerChainFile.getLocation()
                    .toFile());
            try {
                Document doc = builder.build(handlerChainInputSteam);
                Element root = doc.getRootElement();
                List<Element> handlerChains = root.getChildren(HANDLER_CHAIN, JAVAEE_NS);
                for (Element handlerChain : handlerChains) {
                    Element handler = handlerChain.getChild(HANDLER, JAVAEE_NS);
                    if (handler != null) {
                        Element handlerNameElement = handler.getChild(HANDLER_NAME, JAVAEE_NS);
                        Element handlerClassElement = handler.getChild(HANDLER_CLASS, JAVAEE_NS);

                        if (handlerNameElement != null && handlerNameElement.getText().equals(handlerName)
                                && handlerClassElement != null && handlerClassElement.getText().equals(handlerClass)) {
                            return true;
                        }
                    }
                }
            } catch (JDOMException jdome) {
                JAXWSCorePlugin.log(jdome);
            } finally {
                handlerChainInputSteam.close();
            }
        }
        return false;
    }

    public static boolean isHandlerChainFile(IFile handlerChainFile) throws IOException {
        if (handlerChainFile.exists()) {
            FileInputStream handlerChainInputStream = new FileInputStream(handlerChainFile.getLocation().toFile());
            if (handlerChainInputStream.available() > 0) {
                SAXBuilder builder = new SAXBuilder();
                try {
                    Document doc = builder.build(handlerChainInputStream);
                    Element root = doc.getRootElement();
                    if (root.getName().equals(HANDLER_CHAINS) && root.getNamespace().equals(JAVAEE_NS)) {
                        return true;
                    }
                } catch (JDOMException jdome) {
                    JAXWSCorePlugin.log(jdome);
                } finally {
                    handlerChainInputStream.close();
                }
            }
        }
        return false;
    }

    public static void formatXMLFile(IFile file) {
        if (file != null) {
            try {
                IContentDescription contentDescription = file.getContentDescription();
                if (contentDescription == null) {
                    return;
                }
                IContentType contentType = contentDescription.getContentType();
                IStructuredFormatProcessor formatProcessor = FormatProcessorsExtensionReader.getInstance()
                .getFormatProcessor(contentType.getId());
                if (formatProcessor != null) {
                    formatProcessor.formatFile(file);
                }
            } catch (CoreException ce) {
                JAXWSCorePlugin.log(ce.getStatus());
            } catch (IOException ioe) {
                JAXWSCorePlugin.log(ioe);
            }
        }
    }

}

/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.HandlerChain;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.sse.ui.internal.FormatProcessorsExtensionReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class NewJAXWSHandlerWizard extends Wizard implements INewWizard {
    private IWorkbench workbench;
    private IStructuredSelection selection;

    private NewJAXWSHandlerWizardPage handlerWizardPage;

    private String HANDLER_CHAINS = "handler-chains"; //$NON-NLS-1$
    private String HANDLER_CHAIN = "handler-chain"; //$NON-NLS-1$
    private String HANDLER = "handler"; //$NON-NLS-1$
    private String HANDLER_NAME = "handler-name"; //$NON-NLS-1$
    private String HANDLER_CLASS = "handler-class"; //$NON-NLS-1$

    private Namespace JAVAEE_NS = Namespace.getNamespace("http://java.sun.com/xml/ns/javaee"); //$NON-NLS-1$

    @Override
    public void addPages() {
        if (handlerWizardPage == null) {
            handlerWizardPage= new NewJAXWSHandlerWizardPage();
            handlerWizardPage.init(getSelection());
        }
        addPage(handlerWizardPage);
    }

    public NewJAXWSHandlerWizard() {
        setWindowTitle(JAXWSUIMessages.JAXWS_HANDLER_WIZARD_TITLE);
        //TODO replace with dedicated handler wizban
        setDefaultPageImageDescriptor(JAXWSUIPlugin.getImageDescriptor("$nl$/icons/wizban/new_wiz.png"));  //$NON-NLS-1$
    }

    @Override
    public boolean performFinish() {
        try {

            ISchedulingRule rule = null;
            Job job = Job.getJobManager().currentJob();
            if (job != null) {
                rule = job.getRule();
            } else {
                rule = ResourcesPlugin.getWorkspace().getRoot();
            }
            WorkspaceModifyOperation workspaceModifyOperation = new WorkspaceModifyOperation(rule) {

                @Override
                protected void execute(IProgressMonitor monitor) throws CoreException,
                InvocationTargetException, InterruptedException {
                    handlerWizardPage.createType(monitor);
                    IType type = handlerWizardPage.getCreatedType();
                    if (handlerWizardPage.isConfigureHandlerChain()) {
                        if (handlerWizardPage.isCreateHandlerChain()) {
                            String newHandlerChainName = handlerWizardPage.getNewHandlerChainPath();
                            createHandlerChainFile(new Path(newHandlerChainName), type.getElementName(),
                                    type.getFullyQualifiedName());
                        } else if (handlerWizardPage.isEditHandlerChain()) {
                            String existingHandlerChainName = handlerWizardPage.getExistingHandlerChainPath();
                            addHandlerToHandlerChain(new Path(existingHandlerChainName), type.getElementName(),
                                    type.getFullyQualifiedName());
                        }
                        if (handlerWizardPage.isAssociateHandlerChain()) {
                            String fullyQualifiedName = handlerWizardPage.getSelectedWebServicePath();
                            IType webServiceType = handlerWizardPage.getJavaProject().findType(fullyQualifiedName);
                            IPath handlerChainPath = new Path(handlerWizardPage.getHandlerChainPath());
                            IFile handlerChainFile = ResourcesPlugin.getWorkspace().getRoot().getFile(handlerChainPath);

                            URI relativeURI = URI.createPlatformResourceURI(handlerChainFile.getLocation().toOSString(),
                                    false).deresolve(
                                            URI.createPlatformResourceURI(webServiceType.getResource().getLocation().toOSString(),
                                                    false));

                            ICompilationUnit compilationUnit = webServiceType.getCompilationUnit();
                            ASTParser parser = ASTParser.newParser(AST.JLS3);
                            parser.setSource(compilationUnit);
                            CompilationUnit cu = (CompilationUnit)parser.createAST(null);

                            Annotation annotation = AnnotationUtils.getAnnotation(webServiceType, HandlerChain.class);
                            if (annotation != null && annotation instanceof NormalAnnotation) {
                                NormalAnnotation handlerChainAnnotation = (NormalAnnotation) annotation;
                                MemberValuePair filePair = AnnotationUtils.getMemberValuePair(handlerChainAnnotation,
                                "file");  //$NON-NLS-1$
                                if (filePair != null) {
                                    StringLiteral file = AnnotationsCore.createStringLiteral(cu.getAST(),
                                            relativeURI.toFileString());
                                    AnnotationUtils.updateMemberValuePair(filePair, file);
                                }
                            } else {
                                List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
                                MemberValuePair filePair = AnnotationsCore.createStringMemberValuePair(cu.getAST(),
                                        "file", relativeURI.toFileString());  //$NON-NLS-1$
                                memberValuePairs.add(filePair);
                                Annotation handlerChainAnnotation = AnnotationsCore.createNormalAnnotation(cu.getAST(),
                                        HandlerChain.class.getSimpleName(), memberValuePairs);
                                AnnotationUtils.addAnnotation(webServiceType, handlerChainAnnotation);
                                AnnotationUtils.addImport(webServiceType, HandlerChain.class.getCanonicalName());
                            }
                        }
                    }
                }

            };
            PlatformUI.getWorkbench().getProgressService().runInUI(getContainer(), workspaceModifyOperation, rule);
        } catch (InvocationTargetException ite) {
            JAXWSUIPlugin.log(ite);
            return false;
        } catch  (InterruptedException ie) {
            JAXWSUIPlugin.log(ie);
            return false;
        }

        IResource resource= handlerWizardPage.getModifiedResource();
        if (resource != null) {
            BasicNewResourceWizard.selectAndReveal(resource, workbench.getActiveWorkbenchWindow());
            openResource((IFile) resource);
        }

        return true;
    }


    protected void openResource(final IFile resource) {
        final IWorkbenchPage activePage= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (activePage != null) {
            final Display display= getShell().getDisplay();
            if (display != null) {
                display.asyncExec(new Runnable() {
                    public void run() {
                        try {
                            IDE.openEditor(activePage, resource, true);
                        } catch (PartInitException e) {
                            JAXWSUIPlugin.log(e);
                        }
                    }
                });
            }
        }
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
    }

    private IStructuredSelection getSelection() {
        return selection;
    }

    private void createHandlerChainFile(IPath path, String handlerName, String handlerClass) {
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
                JAXWSUIPlugin.log(ce);
            } catch (FileNotFoundException fnfe) {
                JAXWSUIPlugin.log(fnfe);
            } catch (IOException ioe) {
                JAXWSUIPlugin.log(ioe);
            }
        }
    }

    private void addHandlerToHandlerChain(IPath path, String handlerName, String handlerClass) {
        IFile handlerChainFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        if (handlerChainFile.exists()) {
            try {
                if (isHandlerChainFile(handlerChainFile)) {
                    SAXBuilder builder = new SAXBuilder();
                    FileInputStream handlerChainInputSteam = new FileInputStream(handlerChainFile.getLocation()
                            .toFile());
                    try {
                        Document document = builder.build(handlerChainInputSteam);
                        Element root = document.getRootElement();

                        Element handlerChainElement = root.getChild(HANDLER_CHAIN, JAVAEE_NS);

                        if (handlerChainElement != null) {
                            Element handlerElement = new Element(HANDLER, JAVAEE_NS);

                            Element handlerNameElement = new Element(HANDLER_NAME, JAVAEE_NS);
                            handlerNameElement.setText(handlerName);

                            Element handlerClassElement = new Element(HANDLER_CLASS, JAVAEE_NS);
                            handlerClassElement.setText(handlerClass);

                            handlerElement.addContent(handlerNameElement);
                            handlerElement.addContent(handlerClassElement);

                            if (!isHandlerDefined(handlerChainFile, handlerName, handlerClass)) {
                                handlerChainElement.addContent(handlerElement);

                                OutputStream outputStream = new FileOutputStream(handlerChainFile.getLocation().toFile());
                                XMLOutputter outputter = new XMLOutputter();
                                outputter.output(document, outputStream);
                                handlerChainFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
                                formatXMLFile(handlerChainFile);
                            }
                        }
                    } catch (JDOMException jdome) {
                        JAXWSUIPlugin.log(jdome);
                    } catch (CoreException ce) {
                        JAXWSUIPlugin.log(ce);
                    } finally {
                        handlerChainInputSteam.close();
                    }

                }
            } catch (IOException ioe) {
                JAXWSUIPlugin.log(ioe);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isHandlerDefined(IFile handlerChainFile, String handlerName, String handlerClass) throws IOException {
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
                JAXWSUIPlugin.log(jdome);
            } finally {
                handlerChainInputSteam.close();
            }
        }
        return false;
    }

    protected boolean isHandlerChainFile(IFile handlerChainFile) throws IOException {
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
                JAXWSUIPlugin.log(jdome);
            } finally {
                handlerChainInputStream.close();
            }
        }
        return false;
    }

    private static void formatXMLFile(IFile file) {
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
                JAXWSUIPlugin.log(ce.getStatus());
            } catch (IOException ioe) {
                JAXWSUIPlugin.log(ioe);
            }
        }
    }
}
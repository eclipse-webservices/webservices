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
import org.eclipse.core.runtime.Path;
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
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSHandlerUtils;
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

public class NewJAXWSHandlerWizard extends Wizard implements INewWizard {
    private IWorkbench workbench;
    private IStructuredSelection selection;

    private NewJAXWSHandlerWizardPage handlerWizardPage;
    private OrderHandlerChainPage handlerOrderPage;

    @Override
    public void addPages() {
        if (handlerWizardPage == null) {
            handlerWizardPage= new NewJAXWSHandlerWizardPage();
            handlerWizardPage.init(getSelection());
        }

        if (handlerOrderPage == null) {
            handlerOrderPage = new OrderHandlerChainPage();
        }
        addPage(handlerWizardPage);
        addPage(handlerOrderPage);
    }

    public NewJAXWSHandlerWizard() {
        setWindowTitle(JAXWSUIMessages.JAXWS_HANDLER_WIZARD_TITLE);
        //TODO replace with dedicated handler wizban
        setDefaultPageImageDescriptor(JAXWSUIPlugin.getImageDescriptor("$nl$/icons/wizban/new_wiz.png"));  //$NON-NLS-1$
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        if (page == handlerWizardPage && handlerWizardPage.isConfigureHandlerChain() && handlerWizardPage.isEditHandlerChain()) {
            handlerOrderPage.setJavaProject(handlerWizardPage.getJavaProject());
            IPath handlerChainPath = new Path(handlerWizardPage.getExistingHandlerChainPath());
            handlerOrderPage.setInput(handlerChainPath);
            handlerOrderPage.addHandler(handlerWizardPage.getTypeName(), handlerWizardPage.getPackageText(), handlerWizardPage.getSelectedHandlerType());
            return handlerOrderPage;
        }
        return null;
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
                            IPath handlerChainPath = new Path(handlerWizardPage.getNewHandlerChainPath());
                            JAXWSHandlerUtils.createHandlerChainFile(handlerChainPath);
                            JAXWSHandlerUtils.addHandlerToHandlerChain(handlerChainPath, type.getElementName(), type.getFullyQualifiedName());
                        } else if (handlerWizardPage.isEditHandlerChain()) {
                            IPath handlerChainPath = new Path(handlerWizardPage.getExistingHandlerChainPath());
                            JAXWSHandlerUtils.writeDocumentToFile(handlerChainPath, handlerOrderPage.getDocument());
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
}
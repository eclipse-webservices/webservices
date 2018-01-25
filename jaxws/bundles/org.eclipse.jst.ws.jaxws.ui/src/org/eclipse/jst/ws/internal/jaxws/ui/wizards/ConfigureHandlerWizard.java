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
package org.eclipse.jst.ws.internal.jaxws.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.HandlerChain;

import org.eclipse.core.resources.IContainer;
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
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSHandlerUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class ConfigureHandlerWizard extends Wizard {
    private AddHandlerChainPage addHandlerChainPage;
    private OrderHandlerChainPage orderHandlerChainPage;

    private boolean fileCreated;
    private boolean addAnnotation;

    private IPath handlerChainPath;
    private IType type;

    public ConfigureHandlerWizard(IType type) {
        this.type = type;
        setWindowTitle(JAXWSUIMessages.JAXWS_CONFIGURE_HANDLER_WIZARD_TITLE);
        //TODO replace with dedicated handler wizban
        setDefaultPageImageDescriptor(JAXWSUIPlugin.getImageDescriptor("$nl$/icons/wizban/new_wiz.png"));  //$NON-NLS-1$
        handlerChainPath = getHandlerChainPath(type);
    }

    @Override
    public void addPages() {
        if (addHandlerChainPage == null) {
            addHandlerChainPage = new AddHandlerChainPage(type);
        }
        if (orderHandlerChainPage == null) {
            orderHandlerChainPage = new OrderHandlerChainPage(handlerChainPath, type.getJavaProject());
        }
        if (handlerChainPath != null && handlerChainPath.isEmpty()) {
            addPage(addHandlerChainPage);
        }
        addPage(orderHandlerChainPage);
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        if (page == addHandlerChainPage && addHandlerChainPage.isPageComplete()) {
            if (addHandlerChainPage.isCreateHandlerChain()) {
                handlerChainPath = new Path(addHandlerChainPage.getNewHandlerChainPath());
                orderHandlerChainPage.setHandlerChainPath(handlerChainPath);
                addAnnotation = true;
                fileCreated = true;
                addHandlerChainPage.setFileCreated(fileCreated);
            }
            if (addHandlerChainPage.isEditHandlerChain()) {
                handlerChainPath = new Path(addHandlerChainPage.getExistingHandlerChainPath());
                orderHandlerChainPage.setHandlerChainPath(handlerChainPath);
                addAnnotation = true;
                fileCreated = false;
                addHandlerChainPage.setFileCreated(fileCreated);
            }
        }
        return super.getNextPage(page);
    }

    private IPath getHandlerChainPath(IType type) {
        if (type != null) {
            Annotation handlerChain = AnnotationUtils.getAnnotation(type, HandlerChain.class);
            if (handlerChain != null) {
                String file = AnnotationUtils.getStringValue(handlerChain, "file"); //$NON-NLS-1$
                if (file != null) {
                    if (!file.startsWith("../")) { //$NON-NLS-1$
                        return type.getPackageFragment().getPath().append(new Path(file));
                    } else {
                        return findHandlerChainPath(file);
                    }
                }
            }
        }
        return Path.EMPTY;
    }

    private IPath findHandlerChainPath(String file) {
        IContainer container = type.getResource().getParent();
        if (container != null) {
            while(file.startsWith("../")) { //$NON-NLS-1$
                file = file.substring(3);
                if (container != null) {
                    container = container.getParent();
                }
            }
            if (container != null) {
                IResource handlerChainFile = container.findMember(file);
                if (handlerChainFile != null) {
                    return handlerChainFile.getFullPath();
                }
            }
        }
        return Path.EMPTY;
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
                protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
                    if (addAnnotation) {
                        IFile handlerChainFile = ResourcesPlugin.getWorkspace().getRoot().getFile(handlerChainPath);

                        URI relativeURI = URI.createPlatformResourceURI(handlerChainFile.getLocation().toOSString(),
                                false).deresolve(
                                        URI.createPlatformResourceURI(type.getResource().getLocation().toOSString(),
                                                false));

                        ICompilationUnit compilationUnit = type.getCompilationUnit();
                        ASTParser parser = ASTParser.newParser(AST.JLS3);
                        parser.setSource(compilationUnit);
                        CompilationUnit cu = (CompilationUnit)parser.createAST(null);
                        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
                        MemberValuePair filePair = AnnotationsCore.createStringMemberValuePair(cu.getAST(),
                                "file", relativeURI.toFileString());  //$NON-NLS-1$
                        memberValuePairs.add(filePair);
                        Annotation handlerChainAnnotation = AnnotationsCore.createNormalAnnotation(cu.getAST(),
                                HandlerChain.class.getSimpleName(), memberValuePairs);
                        AnnotationUtils.addAnnotation(type, handlerChainAnnotation);
                        AnnotationUtils.addImport(type, HandlerChain.class.getCanonicalName());
                    }
                    JAXWSHandlerUtils.writeDocumentToFile(handlerChainPath, orderHandlerChainPage.getDocument());
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
        return true;
    }

    public void deleteFile(IPath filePath) {
        IFile handlerFile = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
        if (handlerFile.exists()) {
            try {
                handlerFile.delete(true, null);
            } catch (CoreException ce) {
                JAXWSUIPlugin.log(ce.getStatus());
            }
        }
    }
    
    @Override
    public boolean performCancel() {
        if (fileCreated) {
            IPath filePath = type.getPackageFragment().getPath().append(new Path("handler-chain.xml")); //$NON-NLS-1$
            deleteFile(filePath);
        }
        return super.performCancel();
    }

}

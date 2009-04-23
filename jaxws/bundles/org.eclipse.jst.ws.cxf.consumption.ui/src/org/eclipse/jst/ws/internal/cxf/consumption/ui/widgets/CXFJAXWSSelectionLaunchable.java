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
package org.eclipse.jst.ws.internal.cxf.consumption.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.AbstractObjectSelectionLaunchable;
import org.eclipse.jst.ws.internal.cxf.consumption.ui.CXFConsumptionUIMessages;
import org.eclipse.jst.ws.internal.cxf.consumption.ui.CXFConsumptionUIPlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class CXFJAXWSSelectionLaunchable extends AbstractObjectSelectionLaunchable {
    private IStatus validationStatus = Status.OK_STATUS;
    
    private IProject project;
    private String serverComponentName;
    private String className = "";
    
    @Override
    public void setInitialSelection(IStructuredSelection initialSelection) {
        if (initialSelection != null && !initialSelection.isEmpty()) {
            Object firstElement = initialSelection.getFirstElement();
            className = firstElement.toString();
        }
    }

    @Override
    public int launch(Shell shell) {
        IStatus status = Status.OK_STATUS;
        ElementTreeSelectionDialog selectionDialog = new ElementTreeSelectionDialog(shell,
                new JavaElementLabelProvider(), new StandardJavaElementContentProvider());
        selectionDialog.setTitle(CXFConsumptionUIMessages.CXFJAXWSSELECTIONLAUNCHABLE_SELECTION_DIALOG_TITLE);
        selectionDialog
                .setMessage(CXFConsumptionUIMessages.CXFJAXWSSELECTIONLAUNCHABLE_SELECTION_DIALOG_MESSAGE);
        selectionDialog.setAllowMultiple(false);
        selectionDialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
        selectionDialog.addFilter(new JavaViewerFilter());

        selectionDialog.setValidator(new JavaSelectionStatusValidator());

        int returnCode = selectionDialog.open();
        if (returnCode == Window.OK) {
            ICompilationUnit selectedCompilationUnit = (ICompilationUnit) selectionDialog.getFirstResult();
            IType type = selectedCompilationUnit.findPrimaryType();
            className = type.getFullyQualifiedName();
            try {
                IResource typeResource = type.getUnderlyingResource();
                if (typeResource != null) {
                    this.project = typeResource.getProject();
                    IVirtualComponent comp = ResourceUtils.getComponentOf(typeResource);
                    if (comp != null) {
                        serverComponentName = comp.getName();
                    }
                } else {
                    project = null;
                    serverComponentName = null;
                }
                return status.getSeverity();
            } catch (JavaModelException jme) {
                project = null;
                serverComponentName = null;
                status = jme.getStatus();
                CXFConsumptionUIPlugin.log(status);
                return status.getSeverity();
            }
        }

        return IStatus.CANCEL;
    }

    @Override
    public IStructuredSelection getObjectSelection() {
        return new StructuredSelection(className);
    }

    public String getComponentName() {
        return serverComponentName;
    }

    public IProject getProject() {
            ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getSelectionService();
            ISelection selection = selectionService.getSelection();
            
            if (selection != null && !selection.isEmpty() && selection instanceof TreeSelection) {
                TreeSelection treeSelection = (TreeSelection) selection;
                Object firstElement = treeSelection.getFirstElement();
                if (firstElement instanceof ICompilationUnit) {
                    ICompilationUnit compilationUnit = (ICompilationUnit) firstElement;
                    project = compilationUnit.getResource().getProject();
                }
            }
        return project;
    }

    @Override
    public IStatus validateSelection(IStructuredSelection objectSelection) {
        return validationStatus;
    }

    @Override
    public String getObjectSelectionDisplayableString() {
        return className;
    }

    @Override
    public boolean validate(String stringToValidate) {
        className = stringToValidate;

        IProject project = getProject();
        if (project != null) {
            validationStatus = JDTUtils.validateJavaTypeName(project.getName(), className);
        } else {
            validationStatus = JDTUtils.validateJavaTypeName(className);
        }

//        IProject project = getProject();
//        if (project != null) {
//            IType type = JDTUtils.getType(project, className);
//            if (type == null || !type.exists()) {
//                validationStatus = new Status(IStatus.ERROR, CXFConsumptionUIPlugin.PLUGIN_ID, "");
//            } else {
//                validationStatus = JDTUtils.validateJavaTypeName(project.getName(), className);
//            }
//            return validationStatus.isOK();
//        }
        return validationStatus.isOK();
    }
    
    private static class JavaViewerFilter extends ViewerFilter {
        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            try {
                if (element instanceof IJavaProject) {
                    return true;
                }
                if (element instanceof IPackageFragmentRoot) {
                    IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot) element;
                    return packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE;
                }
                if (element instanceof IPackageFragment) {
                    IPackageFragment packageFragment = (IPackageFragment) element;
                    return packageFragment.hasChildren();
                }
                if (element instanceof ICompilationUnit) {
                    ICompilationUnit compilationUnit = (ICompilationUnit) element;
                    IType type = compilationUnit.findPrimaryType();
                    return type.isClass() || type.isInterface();

                }
            } catch (JavaModelException jme) {
                CXFConsumptionUIPlugin.log(jme.getStatus());
            }
            return false;
        }
    }
    
    private static class JavaSelectionStatusValidator implements ISelectionStatusValidator {
        public IStatus validate(Object[] selection) {
            if (selection.length == 1) {
                if (selection[0] instanceof ICompilationUnit) {
                    return new Status(IStatus.OK, CXFConsumptionUIPlugin.PLUGIN_ID, ""); //$NON-NLS-1$
                }
            }
            return new Status(IStatus.ERROR, CXFConsumptionUIPlugin.PLUGIN_ID, ""); //$NON-NLS-1$
        }
    }
}

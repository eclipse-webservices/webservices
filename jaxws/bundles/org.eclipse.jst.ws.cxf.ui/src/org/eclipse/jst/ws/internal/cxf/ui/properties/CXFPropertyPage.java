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
package org.eclipse.jst.ws.internal.cxf.ui.properties;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.cxf.core.CXFClasspathContainer;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.dialogs.PropertyPage;

public class CXFPropertyPage extends PropertyPage {

    private IJavaProject javaProject;
    private IProject project;
    private ComboViewer installationsComboViewer;
    private Link link;
    private CXFInstall defaultInstall;
    private CXFContext context;

    public CXFPropertyPage() {
        context = CXFCorePlugin.getDefault().getJava2WSContext();
    }

    @Override
    protected Control createContents(Composite parent) {
        IAdaptable element = getElement();

        if (!(element instanceof IJavaProject)) {
            return null;
        }

        javaProject = (IJavaProject) element;
        this.project = javaProject.getProject();

        Composite composite = new Composite(parent, SWT.NONE);

        GridLayout gridLayout = new GridLayout(2, false);
        composite.setLayout(gridLayout);

        link = new Link(composite, SWT.NONE);
        link.setText(CXFUIMessages.CXF_CONFIGURE_INSTALLED_RUNTIMES_LABEL);
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int selectedIndex = installationsComboViewer.getCombo().getSelectionIndex();
                int result = PreferencesUtil.createPreferenceDialogOn(getShell(),
                        "org.eclipse.jst.ws.cxf.ui.CXFRuntimePreferencesPage", //$NON-NLS-1$
                        new String[] {"org.eclipse.jst.ws.cxf.ui.CXFRuntimePreferencesPage"}, null).open(); //$NON-NLS-1$
                if (result == Window.OK) {
                    installationsComboViewer.refresh();
                    installationsComboViewer.getCombo().select(selectedIndex);
                }
            }
        });

        GridData gridData = new GridData(SWT.END, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        link.setLayoutData(gridData);

        Label label = new Label(composite, SWT.NONE);
        label.setText(CXFUIMessages.CXF_PROPERTY_PAGE_RUNTIME_LABEL);

        installationsComboViewer = new ComboViewer(composite, SWT.BORDER | SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        installationsComboViewer.getCombo().setLayoutData(gridData);

        installationsComboViewer.setContentProvider(new IStructuredContentProvider() {

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            public void dispose() {
            }

            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof Collection<?>) {
                    return ((Collection<?>) inputElement).toArray();
                }
                return new Object[] {};
            }
        });

        installationsComboViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                if (element instanceof CXFInstall) {
                    CXFInstall cxfInstall = (CXFInstall) element;
                    return cxfInstall.getType() + " " + cxfInstall.getVersion();
                }
                return ""; //$NON-NLS-1$
            }

        });

        Collection<CXFInstall> installations = context.getInstallations().values();

        installationsComboViewer.setInput(installations);

        String intalledVersion = CXFCorePlugin.getDefault().getCXFRuntimeVersion(project);
        defaultInstall = context.getInstallations().get(intalledVersion);
        installationsComboViewer.setSelection(new StructuredSelection(defaultInstall), true);
        return composite;
    }

    @Override
    public boolean performOk() {
        CXFInstall selectedInstall = getSelectedInstall();
        //if (isUpdateRequired(project, selectedInstall)) {
        //if (!defaultInstall.getVersion().equals(selectedInstall.getVersion())) {
        if (selectedInstall != null) {
            CXFCorePlugin.getDefault().setCXFRuntimeVersion(project, selectedInstall.getVersion());

            ClasspathContainerInitializer classpathContainerInitializer = JavaCore.getClasspathContainerInitializer(
                    CXFCorePlugin.CXF_CLASSPATH_CONTAINER_ID);
            if (classpathContainerInitializer != null) {
                IPath containerPath = new Path(CXFCorePlugin.CXF_CLASSPATH_CONTAINER_ID);
                CXFClasspathContainer cxfClasspathContainer = new CXFClasspathContainer(containerPath, javaProject);
                try {
                    classpathContainerInitializer.requestClasspathContainerUpdate(containerPath, javaProject,
                            cxfClasspathContainer);
                } catch (CoreException ce) {
                    CXFCorePlugin.log(ce.getStatus());
                }
            }
        }
        return true;
    }

    private CXFInstall getSelectedInstall() {
        StructuredSelection structuredSelection = (StructuredSelection) installationsComboViewer.getSelection();
        CXFInstall selectedInstall = (CXFInstall) structuredSelection.getFirstElement();
        return selectedInstall;
    }

    @Override
    protected void performDefaults() {
        CXFInstall defaultInstall = context.getInstallations().get(context.getDefaultRuntimeVersion());
        installationsComboViewer.setSelection(new StructuredSelection(defaultInstall), true);
        super.performDefaults();
    }
}
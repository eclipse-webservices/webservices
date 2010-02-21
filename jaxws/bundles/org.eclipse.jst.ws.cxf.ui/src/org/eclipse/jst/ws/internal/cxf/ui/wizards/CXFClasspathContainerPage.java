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
package org.eclipse.jst.ws.internal.cxf.ui.wizards;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class CXFClasspathContainerPage extends WizardPage implements IClasspathContainerPage, IClasspathContainerPageExtension {

    private IProject project;
    private ComboViewer installationsComboViewer;
    private Link link;
    private CXFContext context;
    private CXFInstall defaultInstall;
    private String intalledVersion;

    public CXFClasspathContainerPage() {
        super("cxf.classpath.container.page");
        setTitle(CXFUIMessages.CXF_INSTALL_WIZARD_PAGE_TITLE);
        setDescription(CXFUIMessages.CXF_INSTALL_WIZARD_PAGE_DESCRIPTION);
        setImageDescriptor(CXFUIPlugin.imageDescriptorFromPlugin(CXFUIPlugin.PLUGIN_ID, "icons/wizban/library_wiz.png"));
        context = CXFCorePlugin.getDefault().getJava2WSContext();
    }

    public boolean finish() {
        return true;
    }

    public IClasspathEntry getSelection() {
        CXFInstall selectedInstall = getSelectedInstall();
        if (selectedInstall != null && !selectedInstall.getVersion().equals(intalledVersion)) {
            CXFCorePlugin.getDefault().setCXFRuntimeVersion(project, selectedInstall.getVersion());

            IClasspathAttribute jstComponentDependency =
                JavaCore.newClasspathAttribute("org.eclipse.jst.component.dependency", "/WEB-INF/lib"); //$NON-NLS-1$
            IPath path = new Path(CXFCorePlugin.CXF_CLASSPATH_CONTAINER_ID);
            path = path.append(selectedInstall.getType());
            path = path.append(selectedInstall.getVersion());
            return JavaCore.newContainerEntry(path, new IAccessRule[0],
                    CXFCorePlugin.getDefault().getJava2WSContext().isExportCXFClasspathContainer()
                    ? new IClasspathAttribute[]{jstComponentDependency} : new IClasspathAttribute[]{},
                            true);
        }
        return null;
    }

    public void setSelection(IClasspathEntry containerEntry) {
        if (project != null) {
            intalledVersion = CXFCorePlugin.getDefault().getCXFRuntimeVersion(project);
            defaultInstall = context.getInstallations().get(intalledVersion);
            if (defaultInstall != null && installationsComboViewer != null) {
                installationsComboViewer.setSelection(new StructuredSelection(defaultInstall), true);
            }
        }
    }

    public void createControl(Composite parent) {
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

        Dialog.applyDialogFont(composite);
        setControl(composite);
        setSelection(null);
    }

    public void initialize(IJavaProject javaProject, IClasspathEntry[] currentEntries) {
        if (javaProject != null) {
            this.project = javaProject.getProject();
        }
    }

    private CXFInstall getSelectedInstall() {
        StructuredSelection structuredSelection = (StructuredSelection) installationsComboViewer.getSelection();
        if (!structuredSelection.isEmpty()) {
            CXFInstall selectedInstall = (CXFInstall) structuredSelection.getFirstElement();
            return selectedInstall;
        }
        return null;
    }

}

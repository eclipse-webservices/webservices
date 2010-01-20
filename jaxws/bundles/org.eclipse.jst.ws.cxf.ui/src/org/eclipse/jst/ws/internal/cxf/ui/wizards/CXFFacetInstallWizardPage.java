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

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;

public class CXFFacetInstallWizardPage extends AbstractFacetWizardPage {

    private CXFDataModel dataModel;
    private CXFContext cxfContext;

    private ComboViewer installationsComboViewer;

    public CXFFacetInstallWizardPage() {
        super("cxf.core.facet.install.page"); //$NON-NLS-1$
        setTitle(CXFUIMessages.CXF_FACET_INSTALL_WIZARD_PAGE_TITLE);
        setDescription(CXFUIMessages.CXF_FACET_INSTALL_WIZARD_PAGE_DESCRIPTION);
        cxfContext = CXFCorePlugin.getDefault().getJava2WSContext();
    }

    public void setConfig(Object config) {
        dataModel = (CXFDataModel) config;
    }

    @Override
    public void transferStateToConfig() {
        StructuredSelection structuredSelection = (StructuredSelection) installationsComboViewer.getSelection();
        CXFInstall selectedInstall = (CXFInstall) structuredSelection.getFirstElement();
        dataModel.setDefaultRuntimeVersion(selectedInstall.getVersion());
        dataModel.setDefaultRuntimeLocation(selectedInstall.getLocation());
        dataModel.setDefaultRuntimeType(selectedInstall.getType());
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);

        GridLayout gridLayout = new GridLayout(2, false);
        composite.setLayout(gridLayout);

        Link link = new Link(composite, SWT.RIGHT);
        link.setText(CXFUIMessages.CXF_CONFIGURE_INSTALLED_RUNTIMES_LABEL);
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int selectedIndex = installationsComboViewer.getCombo().getSelectionIndex();
                int result = PreferencesUtil.createPreferenceDialogOn(getShell(),
                        "org.eclipse.jst.ws.cxf.ui.CXFRuntimePreferencesPage", //$NON-NLS-1$
                        new String[] { "org.eclipse.jst.ws.cxf.ui.CXFRuntimePreferencesPage" }, null).open(); //$NON-NLS-1$
                if (result == Window.OK) {
                    installationsComboViewer.refresh();
                    setDefault();
                }
            }
        });

        GridData gridData = new GridData(SWT.END, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        link.setLayoutData(gridData);

        Label label = new Label(composite, SWT.NONE);
        label.setText("CXF runtime:");

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
                return "";
            }

        });

        Collection<CXFInstall> installations = cxfContext.getInstallations().values();
        installationsComboViewer.setInput(installations);
        setDefault();
        setControl(composite);
    }

    private void setDefault() {
        String intalledVersion = CXFCorePlugin.getDefault().getJava2WSContext().getDefaultRuntimeVersion();
        CXFInstall defaultInstall = cxfContext.getInstallations().get(intalledVersion);
        if (defaultInstall != null) {
            installationsComboViewer.setSelection(new StructuredSelection(defaultInstall), true);
        }
    }
}

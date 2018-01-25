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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.jst.ws.internal.jaxws.ui.dialogs.HandlerChainSelectionDialog;
import org.eclipse.jst.ws.internal.jaxws.ui.dialogs.NewHandlerChainDialog;
import org.eclipse.jst.ws.internal.jaxws.ui.filters.NewHandlerChainViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class AddHandlerChainPage extends WizardPage {

    private IStatus ok_status = new Status(IStatus.OK, JAXWSUIPlugin.PLUGIN_ID, "");  //$NON-NLS-1$
    private IStatus addNewHandlerChainStatus = Status.OK_STATUS;
    private IStatus editHandlerChainStatus = Status.OK_STATUS;

    private IType type;

    private Button createHandlerChainButton;
    private Text newHandlerChainText;
    private Button browseNewHandlerChainButton;

    private Button editHandlerChainButton;
    private Text exisitingHandlerChainText;
    private Button browseExistingHandlerChainButton;

    private boolean fileCreated;

    protected AddHandlerChainPage(IType type) {
        super("add.handlerchain.wizard.page"); //$NON-NLS-1$
        this.type = type;
        setTitle(JAXWSUIMessages.JAXWS_ADD_HANDLER_WIZARD_PAGE_TITLE);
        setDescription(JAXWSUIMessages.JAXWS_ADD_HANDLER_WIZARD_PAGE_DESCRIPTION);
    }

    public void createControl(Composite parent) {
        initializeDialogUnits(parent);

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setFont(parent.getFont());

        int nColumns = 4;

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns= nColumns;
        composite.setLayout(gridLayout);

        createHandlerChainButton = new Button(composite, SWT.RADIO);
        createHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE);
        GridData gridData =  new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 4;
        createHandlerChainButton.setLayoutData(gridData);
        createHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                enableNewHandlerChainFileWidgets(button.getSelection());
                enableEditHandlerChainFileWidgets(!button.getSelection());
                updateConfigureHandlerStatus();
            }
        });

        Label createNewHandlerLabel = new Label(composite, SWT.NONE);
        createNewHandlerLabel.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_FILE_PATH1);

        newHandlerChainText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        newHandlerChainText.setFont(composite.getFont());
        gridData =  new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.horizontalSpan = 2;
        newHandlerChainText.setLayoutData(gridData);

        newHandlerChainText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                updateConfigureHandlerStatus();
            }
        });

        browseNewHandlerChainButton = new Button(composite, SWT.PUSH);
        browseNewHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_BROWSE1);
        gridData =  new GridData(SWT.END, SWT.FILL, false, false);
        browseNewHandlerChainButton.setLayoutData(gridData);
        browseNewHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                NewHandlerChainDialog dialog = new NewHandlerChainDialog(getShell(),
                        new JavaElementLabelProvider(), new StandardJavaElementContentProvider());
                dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
                dialog.addFilter(new NewHandlerChainViewerFilter(type.getJavaProject(), true, true));
                if (dialog.open() == Window.OK) {
                    newHandlerChainText.setText(dialog.getFilePath());
                    updateConfigureHandlerStatus();
                }
            }
        });

        editHandlerChainButton = new Button(composite, SWT.RADIO);
        editHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CHOOSE);
        gridData =  new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 4;
        editHandlerChainButton.setLayoutData(gridData);

        Label addToHandlerLabel = new Label(composite, SWT.NONE);
        addToHandlerLabel.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_FILE_PATH3);

        editHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateConfigureHandlerStatus();
            }

        });

        exisitingHandlerChainText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        exisitingHandlerChainText.setFont(composite.getFont());
        gridData =  new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.horizontalSpan = 2;
        exisitingHandlerChainText.setLayoutData(gridData);
        exisitingHandlerChainText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                updateConfigureHandlerStatus();
            }

        });

        browseExistingHandlerChainButton = new Button(composite, SWT.PUSH);
        browseExistingHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_BROWSE2);
        gridData =  new GridData(SWT.FILL, SWT.FILL, false, false);
        browseExistingHandlerChainButton.setLayoutData(gridData);

        browseExistingHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                HandlerChainSelectionDialog dialog = new HandlerChainSelectionDialog(getShell(),
                        new JavaElementLabelProvider(), new StandardJavaElementContentProvider());

                dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
                dialog.addFilter(new NewHandlerChainViewerFilter(type.getJavaProject(), false, true));

                if (dialog.open() == Window.OK) {
                    Object[] result = dialog.getResult();
                    IResource resource = (IResource) result[0];
                    exisitingHandlerChainText.setText(resource.getFullPath().toOSString());
                    updateConfigureHandlerStatus();
                }
            }
        });


        setInitialSelection();
        setControl(composite);
    }

    private void setInitialSelection() {
        IPath handlerChainPath = new Path(type.getResource().getParent().getFullPath().addTrailingSeparator()
                + "handler-chain.xml");
        IResource handlerChain = ResourcesPlugin.getWorkspace().getRoot().getFile(handlerChainPath);
        if (handlerChain.exists()) {
            editHandlerChainButton.setSelection(true);
            exisitingHandlerChainText.setText(handlerChainPath.toString());
            enableNewHandlerChainFileWidgets(false);
            enableEditHandlerChainFileWidgets(true);
        } else {
            createHandlerChainButton.setSelection(true);
            newHandlerChainText.setText(handlerChainPath.toString());
            enableNewHandlerChainFileWidgets(true);
            enableEditHandlerChainFileWidgets(false);
        }
        updateConfigureHandlerStatus();
    }

    public void setFileCreated(boolean fileCreated) {
        this.fileCreated = fileCreated;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && fileCreated) {
            ((ConfigureHandlerWizard) getWizard()).deleteFile(new Path(getNewHandlerChainPath()));
        }
    }

    private void enableEditHandlerChainFileWidgets(boolean enable) {
        exisitingHandlerChainText.setEnabled(enable);
        browseExistingHandlerChainButton.setEnabled(enable);
    }

    private void enableNewHandlerChainFileWidgets(boolean enable) {
        newHandlerChainText.setEnabled(enable);
        browseNewHandlerChainButton.setEnabled(enable);
    }

    public boolean isCreateHandlerChain() {
        return createHandlerChainButton.getSelection();
    }

    public boolean isEditHandlerChain() {
        return editHandlerChainButton.getSelection();
    }

    public String getNewHandlerChainPath() {
        return newHandlerChainText.getText();
    }

    public String getExistingHandlerChainPath() {
        return exisitingHandlerChainText.getText();
    }

    private void updateConfigureHandlerStatus() {
        try {
            validateNewHandlerChainField();
            validateExistingHandlerChainField();
            if (addNewHandlerChainStatus.getSeverity() != IStatus.OK || editHandlerChainStatus.getSeverity() != IStatus.OK) {
                setErrorMessage(findMostSevere().getMessage());
                setPageComplete(false);
            } else {
                setErrorMessage(null);
                setPageComplete(true);
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
    }

    private IStatus findMostSevere() {
        if (addNewHandlerChainStatus.getSeverity() > editHandlerChainStatus.getSeverity()) {
            return addNewHandlerChainStatus;
        }
        if (editHandlerChainStatus.getSeverity() > addNewHandlerChainStatus.getSeverity()) {
            return editHandlerChainStatus;
        }
        if (addNewHandlerChainStatus.getSeverity() == editHandlerChainStatus.getSeverity()) {
            return addNewHandlerChainStatus;
        }

        return addNewHandlerChainStatus;
    }

    private IStatus validateNewHandlerChainField() throws JavaModelException {
        if (isCreateHandlerChain()) {
            editHandlerChainStatus = ok_status;
            String newHandlerChainPath = getNewHandlerChainPath().trim();
            if (newHandlerChainPath.length() == 0) {
                return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_NEW_HANDLER_PATH_EMTPY);
            }

            if (!newHandlerChainPath.endsWith(".xml")) {  //$NON-NLS-1$
                return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_INVALID_NAME);
            }

            IPath path = new Path(newHandlerChainPath);
            if (path.segmentCount() >= 2) {
                String[] segmenets = path.segments();
                if (!segmenets[0].equals(type.getJavaProject().getElementName())) {
                    return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                            JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_PROJECT,
                                    type.getJavaProject().getElementName()));
                }
                boolean inSourceFolder = false;
                IPackageFragmentRoot[] packageFragmentRoots = type.getJavaProject().getPackageFragmentRoots();
                for (IPackageFragmentRoot packageFragmentRoot : packageFragmentRoots) {
                    if (segmenets[1].equals(packageFragmentRoot.getElementName())) {
                        inSourceFolder = true;
                        break;
                    }
                }
                if (!inSourceFolder) {
                    return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                            JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_PROJECT,
                                    type.getJavaProject().getElementName()));
                }
                if (path.lastSegment() != null && path.lastSegment().equals(".xml")) { //$NON-NLS-1$
                    return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                            JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_EMPTY_FILE_NAME);
                }
            } else {
                return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_PROJECT,
                                type.getJavaProject().getElementName()));
            }

            IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
            if (res != null && res.exists()) {
                return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_EXISTS,
                                res.getName()));
            }

            return addNewHandlerChainStatus = ok_status;
        } else {
            return addNewHandlerChainStatus = ok_status;
        }
    }

    private IStatus validateExistingHandlerChainField() {
        if (isEditHandlerChain()) {
            addNewHandlerChainStatus = ok_status;
            String existingHandlerChainPath = getExistingHandlerChainPath().trim();
            if (existingHandlerChainPath.length() == 0) {
                return editHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_EXISTING_HANDLER_PATH_EMTPY);
            }

            if (!existingHandlerChainPath.endsWith(".xml")) {  //$NON-NLS-1$
                return editHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_INVALID_NAME);
            }

            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IPath path = new Path(existingHandlerChainPath);

            if (path.segmentCount() >= 2) {
                if (path.lastSegment() != null && path.lastSegment().equals(".xml")) { //$NON-NLS-1$
                    return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                            JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_EMPTY_FILE_NAME);
                }
            }

            if (path.segmentCount() >= 2 && path.segment(0).equals(type.getJavaProject().getElementName())
                    && workspace.getRoot().getFile(path).exists()) {
                return editHandlerChainStatus = ok_status;
            } else {
                return editHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_EDIT_FILE_NOT_FOUND,
                                existingHandlerChainPath));
            }
        } else {
            return editHandlerChainStatus = ok_status;
        }
    }

}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jws.WebService;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

public class NewJAXWSHandlerWizardPage extends NewTypeWizardPage {
    private List<String> logicalHandlerList = new ArrayList<String>();
    private List<String> soapHandlerList = new ArrayList<String>();

    private IStatus ok_status = new Status(IStatus.OK, JAXWSUIPlugin.PLUGIN_ID, "");  //$NON-NLS-1$

    private IStatus superClassImplementsStatus = Status.OK_STATUS;
    private IStatus addNewHandlerChainStatus = Status.OK_STATUS;
    private IStatus editHandlerChainStatus = Status.OK_STATUS;
    private IStatus associateWebServiceStatus = Status.OK_STATUS;

    private Combo handlerTypeCombo;
    private Button configureHandlerChainButton;
    private Button createHandlerChainButton;
    private Text newHandlerChainText;
    private Button browseNewHandlerChainButton;

    private Button editHandlerChainButton;
    private Text exisitingHandlerChainText;
    private Button browseExistingHandlerChainButton;

    private Button associateHandlerChainButton;
    private Text webServiceText;
    private Button browseWebServiceButton;

    public NewJAXWSHandlerWizardPage() {
        super(true, "create.new.handler.chain.page"); //$NON-NLS-1$
        setTitle(JAXWSUIMessages.JAXWS_HANDLER_WIZARD_PAGE_TITLE);
        setDescription(JAXWSUIMessages.JAXWS_HANDLER_WIZARD_PAGE_DESCRIPTION);
        logicalHandlerList.add("javax.xml.ws.handler.LogicalHandler<javax.xml.ws.handler.LogicalMessageContext>");  //$NON-NLS-1$
        soapHandlerList.add("javax.xml.ws.handler.soap.SOAPHandler<javax.xml.ws.handler.soap.SOAPMessageContext>");  //$NON-NLS-1$
    }

    public void init(IStructuredSelection selection) {
        IJavaElement javaElement = getInitialJavaElement(selection);
        initContainerPage(javaElement);
        initTypePage(javaElement);
        doStatusUpdate();
    }

    public void createControl(Composite parent) {
        initializeDialogUnits(parent);

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setFont(parent.getFont());

        int nColumns = 4;

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns= nColumns;
        composite.setLayout(gridLayout);

        createContainerControls(composite, nColumns);
        createPackageControls(composite, nColumns);

        createSeparator(composite, nColumns);

        createTypeNameControls(composite, nColumns);
        createModifierControls(composite, nColumns);

        createSuperClassControls(composite, nColumns);

        Label handlerTypeLabel = new Label(composite, SWT.NONE);
        handlerTypeLabel.setText(JAXWSUIMessages.JAXWS_HANDLER_TYPE);

        handlerTypeCombo = new Combo(composite, SWT.READ_ONLY | SWT.BORDER);

        GridData gridData =  new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.horizontalSpan = 2;
        handlerTypeCombo.setLayoutData(gridData);
        handlerTypeCombo.add(JAXWSUIMessages.JAXWS_LOGICAL_HANDLER);
        handlerTypeCombo.add(JAXWSUIMessages.JAXWS_SOAP_HANDLER);
        handlerTypeCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Combo combo = (Combo) e.widget;
                if (combo.getSelectionIndex() == 0) {
                    setSuperInterfaces(logicalHandlerList, true);
                } else if (combo.getSelectionIndex() == 1) {
                    setSuperInterfaces(soapHandlerList, true);
                }
            }
        });

        Group configurationGroup = new Group(composite, SWT.SHADOW_NONE);
        configurationGroup.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        configurationGroup.setLayout(gridLayout);

        gridData =  new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.horizontalSpan = 4;
        configurationGroup.setLayoutData(gridData);

        configureHandlerChainButton = new Button(configurationGroup, SWT.CHECK);
        configureHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_ADD);
        configureHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                if (button.getSelection()) {
                    createHandlerChainButton.setEnabled(true);
                    editHandlerChainButton.setEnabled(true);
                    associateHandlerChainButton.setEnabled(true);
                    enableNewHandlerChainFileWidgets(createHandlerChainButton.getSelection());
                    enableEditHandlerChainFileWidgets(editHandlerChainButton.getSelection());
                    enableWebServiceWidgets(associateHandlerChainButton.getSelection());
                } else {
                    createHandlerChainButton.setEnabled(false);
                    editHandlerChainButton.setEnabled(false);
                    associateHandlerChainButton.setEnabled(false);
                    enableNewHandlerChainFileWidgets(false);
                    enableEditHandlerChainFileWidgets(false);
                    enableWebServiceWidgets(false);
                }
                updateConfigureHandlerStatus();
            }
        });

        gridData =  new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.horizontalSpan = 4;
        configureHandlerChainButton.setLayoutData(gridData);

        Group innerGroup = new Group(configurationGroup, SWT.SHADOW_NONE);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        innerGroup.setLayout(gridLayout);

        gridData =  new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 4;
        innerGroup.setLayoutData(gridData);

        createHandlerChainButton = new Button(innerGroup, SWT.RADIO);
        createHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE);
        gridData =  new GridData(SWT.FILL, SWT.FILL, true, false);
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

        Label createNewHandlerLabel = new Label(innerGroup, SWT.NONE);
        createNewHandlerLabel.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_FILE_PATH1);

        newHandlerChainText = new Text(innerGroup, SWT.SINGLE | SWT.BORDER);
        newHandlerChainText.setFont(innerGroup.getFont());
        gridData =  new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.horizontalSpan = 2;
        newHandlerChainText.setLayoutData(gridData);

        newHandlerChainText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                updateConfigureHandlerStatus();
            }

        });

        browseNewHandlerChainButton = new Button(innerGroup, SWT.PUSH);
        browseNewHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_BROWSE1);
        gridData =  new GridData(SWT.END, SWT.FILL, false, false);
        browseNewHandlerChainButton.setLayoutData(gridData);
        browseNewHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                NewHandlerChainDialog dialog = new NewHandlerChainDialog(getShell(),
                        new JavaElementLabelProvider(), new StandardJavaElementContentProvider());
                dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
                dialog.addFilter(new NewHandlerChainViewerFilter(getJavaProject(), true, true));
                if (dialog.open() == Window.OK) {
                    newHandlerChainText.setText(dialog.getFilePath());
                    updateConfigureHandlerStatus();
                }
            }
        });

        editHandlerChainButton = new Button(innerGroup, SWT.RADIO);
        editHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_EDIT);
        gridData =  new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 4;
        editHandlerChainButton.setLayoutData(gridData);

        Label addToHandlerLabel = new Label(innerGroup, SWT.NONE);
        addToHandlerLabel.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_FILE_PATH2);

        editHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateConfigureHandlerStatus();
            }

        });

        exisitingHandlerChainText = new Text(innerGroup, SWT.SINGLE | SWT.BORDER);
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

        browseExistingHandlerChainButton = new Button(innerGroup, SWT.PUSH);
        browseExistingHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_BROWSE2);
        gridData =  new GridData(SWT.FILL, SWT.FILL, false, false);
        browseExistingHandlerChainButton.setLayoutData(gridData);

        browseExistingHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                HandlerChainSelectionDialog dialog = new HandlerChainSelectionDialog(getShell(),
                        new JavaElementLabelProvider(), new StandardJavaElementContentProvider());

                dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
                dialog.addFilter(new NewHandlerChainViewerFilter(getJavaProject(), false, true));

                if (dialog.open() == Window.OK) {
                    Object[] result = dialog.getResult();
                    IResource resource = (IResource) result[0];
                    exisitingHandlerChainText.setText(resource.getFullPath().toOSString());
                    updateConfigureHandlerStatus();
                }
            }
        });

        createSeparator(innerGroup, nColumns);

        associateHandlerChainButton = new Button(innerGroup, SWT.CHECK);
        gridData =  new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 4;
        associateHandlerChainButton.setLayoutData(gridData);
        associateHandlerChainButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_ASSOCIATE);
        associateHandlerChainButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                enableWebServiceWidgets(button.getSelection());
                updateConfigureHandlerStatus();
            }

        });

        Label webServiceLabel = new Label(innerGroup, SWT.NONE);
        webServiceLabel.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_WEB_SERVICE);

        webServiceText = new Text(innerGroup,  SWT.SINGLE | SWT.BORDER);
        webServiceText.setFont(composite.getFont());
        gridData =  new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.horizontalSpan = 2;
        webServiceText.setLayoutData(gridData);
        webServiceText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                updateConfigureHandlerStatus();
            }

        });

        browseWebServiceButton = new Button(innerGroup, SWT.PUSH);
        browseWebServiceButton.setText(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_BROWSE3);
        gridData =  new GridData(SWT.FILL, SWT.FILL, false, false);
        browseWebServiceButton.setLayoutData(gridData);

        browseWebServiceButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(),
                        new JavaElementLabelProvider(), new StandardJavaElementContentProvider());
                dialog.setTitle(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_WS_DIALOG_TITLE);
                dialog.setMessage(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_WS_DIALOG_DESCRIPTION);
                dialog.setAllowMultiple(false);
                dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));

                dialog.addFilter(new NewHandlerChainViewerFilter(getJavaProject(), true, false));
                dialog.setValidator(new ISelectionStatusValidator() {

                    public IStatus validate(Object[] selection) {
                        if (selection.length == 1) {
                            if (selection[0] instanceof ICompilationUnit) {
                                ICompilationUnit compilationUnit = (ICompilationUnit) selection[0];
                                if (AnnotationUtils.getAnnotation(compilationUnit.findPrimaryType(), WebService.class)
                                        != null) {
                                    return ok_status;
                                } else {
                                    return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                                            JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_WS_DIALOG_INVALID);
                                }
                            }
                        }
                        return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID, ""); //$NON-NLS-1$
                    }
                });

                if (dialog.open() == Window.OK) {
                    ICompilationUnit selectedCompilationUnit = (ICompilationUnit) dialog.getFirstResult();
                    webServiceText.setText(selectedCompilationUnit.findPrimaryType().getFullyQualifiedName());
                    updateConfigureHandlerStatus();
                }

            }
        });

        composite.pack();
        setControl(composite);
        Dialog.applyDialogFont(composite);

        createHandlerChainButton.setSelection(true);
        createHandlerChainButton.setEnabled(false);
        editHandlerChainButton.setEnabled(false);
        associateHandlerChainButton.setEnabled(false);
        enableNewHandlerChainFileWidgets(false);
        enableEditHandlerChainFileWidgets(false);
        enableWebServiceWidgets(false);
    }

    public boolean isConfigureHandlerChain() {
        return configureHandlerChainButton.getSelection();
    }

    public boolean isCreateHandlerChain() {
        return createHandlerChainButton.getSelection();
    }

    public boolean isEditHandlerChain() {
        return editHandlerChainButton.getSelection();
    }

    public String getSelectedHandlerType() {
        return handlerTypeCombo.getText();
    }

    public String getNewHandlerChainPath() {
        return newHandlerChainText.getText();
    }

    public String getExistingHandlerChainPath() {
        return exisitingHandlerChainText.getText();
    }

    public String getHandlerChainPath() {
        if (isCreateHandlerChain()) {
            return getNewHandlerChainPath();
        } else if (isEditHandlerChain()) {
            return getExistingHandlerChainPath();
        }
        return "";
    }

    public boolean isAssociateHandlerChain() {
        return associateHandlerChainButton.getSelection();
    }

    public String getSelectedWebServicePath() {
        return webServiceText.getText();
    }

    private void enableEditHandlerChainFileWidgets(boolean enable) {
        exisitingHandlerChainText.setEnabled(enable);
        browseExistingHandlerChainButton.setEnabled(enable);
    }

    private void enableNewHandlerChainFileWidgets(boolean enable) {
        newHandlerChainText.setEnabled(enable);
        browseNewHandlerChainButton.setEnabled(enable);
    }

    private void enableWebServiceWidgets(boolean enable) {
        webServiceText.setEnabled(enable);
        browseWebServiceButton.setEnabled(enable);
    }

    private void doStatusUpdate() {
        IStatus[] status = new IStatus[] {
                fContainerStatus,
                fPackageStatus,
                fTypeNameStatus,
                fSuperClassStatus,
                fSuperInterfacesStatus,
                superClassImplementsStatus,
                addNewHandlerChainStatus,
                editHandlerChainStatus,
                associateWebServiceStatus
        };

        updateStatus(status);
    }

    private void updateConfigureHandlerStatus() {
        try {
            validateNewHandlerChainField();
            validateExistingHandlerChainField();
            validateWebServiceField();
            doStatusUpdate();
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
    }

    private IStatus validateNewHandlerChainField() throws JavaModelException {
        if (isConfigureHandlerChain() && isCreateHandlerChain()) {
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
                if (!segmenets[0].equals(getJavaProject().getElementName())) {
                    return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                            JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_PROJECT,
                                    getJavaProject().getElementName()));
                }
                boolean inSourceFolder = false;
                IPackageFragmentRoot[] packageFragmentRoots = getJavaProject().getPackageFragmentRoots();
                for (IPackageFragmentRoot packageFragmentRoot : packageFragmentRoots) {
                    if (segmenets[1].equals(packageFragmentRoot.getElementName())) {
                        inSourceFolder = true;
                        break;
                    }
                }
                if (!inSourceFolder) {
                    return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                            JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_PROJECT,
                                    getJavaProject().getElementName()));
                }
                if (path.lastSegment() != null && path.lastSegment().equals(".xml")) { //$NON-NLS-1$
                    return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                            JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_EMPTY_FILE_NAME);
                }
            } else {
                return addNewHandlerChainStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.bind(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_CREATE_DIALOG_FILE_PROJECT,
                                getJavaProject().getElementName()));
            }

            IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
            if (res != null) {
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
        if (isConfigureHandlerChain() && isEditHandlerChain()) {
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
            if (path.segmentCount() > 1 && path.segment(0).equals(getJavaProject().getElementName())
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

    private IStatus validateWebServiceField() throws JavaModelException {
        if (isConfigureHandlerChain() && isAssociateHandlerChain()) {
            String webServicePath = getSelectedWebServicePath().trim();
            if (webServicePath.length() == 0) {
                return associateWebServiceStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_WS_PATH_EMTPY);
            } else if (getJavaProject().findType(webServicePath) == null){
                return associateWebServiceStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                        JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_WS_NOT_FOUND);
            } else {
                return associateWebServiceStatus = ok_status;
            }
        }  else {
            return associateWebServiceStatus = ok_status;
        }
    }

    @Override
    protected void handleFieldChanged(String fieldName) {
        super.handleFieldChanged(fieldName);
        if (fieldName.equals(SUPER) || fieldName.equals(INTERFACES)) {
            try {
                if (getSuperInterfaces().size() == 0 && fSuperClassStatus.isOK()) {
                    IType type = getJavaProject().findType(getSuperClass());
                    if (type != null) {
                        List<String> interfaceNames = new ArrayList<String>();
                        List<String> superInterfaceNames = Arrays.asList(type.getSuperInterfaceNames());
                        for (String interfaceName : superInterfaceNames) {
                            interfaceNames.add(Signature.getTypeErasure(interfaceName));
                        }

                        if (interfaceNames.contains("javax.xml.ws.handler.soap.SOAPHandler") ||  //$NON-NLS-1$
                                interfaceNames.contains("javax.xml.ws.handler.LogicalHandler")) {  //$NON-NLS-1$
                            superClassImplementsStatus = ok_status;
                        } else {
                            superClassImplementsStatus = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                                    JAXWSUIMessages.JAXWS_HANDLER_EXTEND_IMPLEMENT_HANDLER);
                        }
                    }
                } else {
                    superClassImplementsStatus = Status.OK_STATUS;
                }
            } catch (JavaModelException jme) {
                JAXWSUIPlugin.log(jme);
            }
        }

        doStatusUpdate();
    }

    @Override
    protected IStatus superInterfacesChanged() {
        IStatus status = Status.OK_STATUS;

        IPackageFragmentRoot root = getPackageFragmentRoot();

        if (root != null) {
            String interfaceName = "javax.xml.ws.handler.LogicalHandler";  //$NON-NLS-1$
            try {
                IType type = root.getJavaProject().findType(interfaceName);
                if (type == null) {
                    status = new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                            JAXWSUIMessages.JAXWS_HANDLER_LIBRARY_CLASSPATH);
                    return status;
                }
            } catch (JavaModelException jme) {
                JAXWSUIPlugin.log(jme.getStatus());
            }
        }
        return status;
    }

    @Override
    protected void createTypeMembers(IType newType, final ImportsManager imports, IProgressMonitor monitor)
    throws CoreException {
        createInheritedMethods(newType, false, true, imports, new SubProgressMonitor(monitor, 1));
        if (monitor != null) {
            monitor.done();
        }
    }

}
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
package org.eclipse.jst.ws.internal.cxf.creation.ui.widgets;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIPlugin;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.Java2WSWidgetFactory;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

@SuppressWarnings("restriction")
public class Java2WSClassConfigWidget extends Java2WSTypeConfigWidget {
    private IStatus JAVA_TYPE_NAME_STATUS = Status.OK_STATUS;
    private IStatus JAVA_TYPE_EXISTS_STATUS = Status.OK_STATUS;
    private IStatus SEI_SELECTION_STATUS = Status.OK_STATUS;

    private static final String NAME = "name";

    private int NUMBER_OF_PUBLIC_METHODS;
    private int NUMBER_OF_CHECKED_METHODS;
    private IMethod[] publicMethods;

    private Button useSEIButton;
    private Button selectSEIButton;
    private Button browseSEIButton;
    private Button extractSEIButton;

    private Combo selectSEICombo;
    private Text seiInterfaceNameText;

    private CheckboxTableViewer seiMembersToExtractTableViewer;
    private Table seiMembersToExtractTable;
    private Button selectAllButton;
    private Button deselectAllButton;

    private Java2WSDataModel model;
    private IType startingPointType;

    public Java2WSClassConfigWidget() {
    }

    public void setJava2WSDataModel(Java2WSDataModel model) {
        this.model = model;
    }

    public void setJavaStartingPointType(IType startingPointType) {
        this.startingPointType = startingPointType;
    }

    @Override
    public void internalize() {
        boolean useSEI = model.isUseServiceEndpointInterface();
        useSEIButton.setSelection(useSEI);
        selectSEIButton.setSelection(useSEI);
        selectSEIButton.setEnabled(useSEI);
        if (!useSEI) {
            selectSEICombo.deselectAll();
        }
        enableSelectSEIControls(useSEI);
        extractSEIButton.setSelection(false);
        extractSEIButton.setEnabled(useSEI);
        enableExtractSEIControls(useSEI);
        seiInterfaceNameText.setText("");
        seiMembersToExtractTableViewer.setAllChecked(false);
        NUMBER_OF_CHECKED_METHODS = 0;
        validateSEISelection();
    }

    @Override
    public WidgetDataEvents addControls(Composite parent, final Listener statusListener) {
        final Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        composite.setLayout(gridLayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        composite.setLayoutData(gridData);

        useSEIButton = Java2WSWidgetFactory.createUseSEIButton(composite);
        useSEIButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                boolean useSEI = useSEIButton.getSelection();
                model.setUseServiceEndpointInterface(useSEI);
                selectSEIButton.setEnabled(useSEI);
                extractSEIButton.setEnabled(useSEI);
                updateSEISelectionStatus();
                statusListener.handleEvent(null);
                if (!useSEI) {
                    model.setServiceEndpointInterfaceName("");

                    selectSEIButton.setSelection(false);
                    enableSelectSEIControls(false);

                    extractSEIButton.setSelection(false);
                    enableExtractSEIControls(false);
                }
            }

        });
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
        gridData.horizontalSpan = 3;
        useSEIButton.setLayoutData(gridData);

        Label paddingLabel = Java2WSWidgetFactory.createPaddingLabel(composite);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 3;
        paddingLabel.setLayoutData(gridData);

        selectSEIButton = Java2WSWidgetFactory.createSelectSEIButton(composite);
        selectSEIButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                enableSelectSEIControls(selectSEIButton.getSelection());
                enableExtractSEIControls(!selectSEIButton.getSelection());
                if (selectSEIButton.getSelection()) {
                    updateSEISelectionStatus();
                    statusListener.handleEvent(null);
                }
            }
        });

        selectSEIButton.setSelection(false);
        selectSEIButton.setEnabled(false);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        selectSEIButton.setLayoutData(gridData);

        selectSEICombo = Java2WSWidgetFactory.createSelectSEICombo(composite, model, startingPointType);
        selectSEICombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateSEISelectionStatus();
                statusListener.handleEvent(null);
            }
        });

        selectSEICombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                updateSEISelectionStatus();
                statusListener.handleEvent(null);
            }
        });

        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.horizontalSpan = 1;
        selectSEICombo.setLayoutData(gridData);

        browseSEIButton = Java2WSWidgetFactory.createBrowseButton(composite);

        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.horizontalSpan = 1;
        browseSEIButton.setLayoutData(gridData);

        browseSEIButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ElementTreeSelectionDialog selectionDialog = Java2WSWidgetFactory.createElementTreeSelectionDialog(
                        composite.getShell(), CXFCreationUIMessages.JAVA2WS_SELECT_SEI_DIALOG_TITLE,
                        CXFCreationUIMessages.JAVA2WS_SELECT_SEI_DIALOG_DESCRIPTION,
                        JDTUtils.getJavaProject(model.getProjectName()), IJavaSearchConstants.INTERFACE);

                int returnCode = selectionDialog.open();
                if (returnCode == Window.OK) {
                    ICompilationUnit selectedCompilationUnit = (ICompilationUnit) selectionDialog.getFirstResult();
                    String selectedInterface = selectedCompilationUnit.findPrimaryType().getFullyQualifiedName();
                    List<String> seis = Arrays.asList(selectSEICombo.getItems());
                    if (!seis.contains(selectedInterface)) {
                        selectSEICombo.add(selectedInterface);
                    }
                    selectSEICombo.setText(selectedInterface);
                }
            }
        });

        paddingLabel = Java2WSWidgetFactory.createPaddingLabel(composite);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 3;
        paddingLabel.setLayoutData(gridData);

        extractSEIButton = Java2WSWidgetFactory.createExtractSEIButton(composite);
        extractSEIButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                boolean extract = extractSEIButton.getSelection();
                model.setExtractInterface(extract);
                enableExtractSEIControls(extract);
                enableSelectSEIControls(!extract);
                if (extract) {
                    updateExtractSEIInfo();
                }
                updateSEISelectionStatus();
                statusListener.handleEvent(null);
            }
        });
        extractSEIButton.setSelection(model.isExtractInterface());
        extractSEIButton.setEnabled(false);

        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        extractSEIButton.setLayoutData(gridData);

        seiInterfaceNameText = Java2WSWidgetFactory.createSEIInterfaceNameText(composite);
        seiInterfaceNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                if (extractSEIButton.getSelection()) {
                    updateExtractSEIInfo();
                    updateSEISelectionStatus();
                    statusListener.handleEvent(null);
                }
            }
        });

        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.horizontalSpan = 1;
        seiInterfaceNameText.setLayoutData(gridData);

        Java2WSWidgetFactory.createPaddingLabel(composite);
        Java2WSWidgetFactory.createPaddingLabel(composite);

        Java2WSWidgetFactory.createMemebersToExtractLabel(composite);

        Java2WSWidgetFactory.createPaddingLabel(composite);
        Java2WSWidgetFactory.createPaddingLabel(composite);

        seiMembersToExtractTableViewer = Java2WSWidgetFactory
        .createSEIMembersToExtractTableViewer(composite);
        seiMembersToExtractTable = seiMembersToExtractTableViewer.getTable();
        seiMembersToExtractTableViewer.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                Object[] checkedMethods = seiMembersToExtractTableViewer.getCheckedElements();

                Map<IMethod, Map<String, Boolean>> methodMap = new HashMap<IMethod, Map<String, Boolean>>();
                for (int i = 0; i < checkedMethods.length; i++) {
                    methodMap.put((IMethod)checkedMethods[i], model.getAnnotationMap());
                }
                model.setMethodMap(methodMap);

                NUMBER_OF_CHECKED_METHODS = checkedMethods.length;

                if (NUMBER_OF_CHECKED_METHODS < NUMBER_OF_PUBLIC_METHODS) {
                    selectAllButton.setEnabled(true);
                } else if (NUMBER_OF_CHECKED_METHODS == NUMBER_OF_PUBLIC_METHODS) {
                    selectAllButton.setEnabled(false);
                }

                if (NUMBER_OF_CHECKED_METHODS > 0) {
                    deselectAllButton.setEnabled(true);
                } else {
                    deselectAllButton.setEnabled(false);
                }
                updateSEISelectionStatus();
                statusListener.handleEvent(null);
            }
        });
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.verticalSpan = 3;
        gridData.heightHint = 100;
        seiMembersToExtractTable.setLayoutData(gridData);

        selectAllButton = Java2WSWidgetFactory.createSelectAllButton(composite);
        selectAllButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                seiMembersToExtractTableViewer.setAllChecked(true);
                selectAllButton.setEnabled(false);
                deselectAllButton.setEnabled(true);

                TableItem[] checkedMethods = seiMembersToExtractTableViewer.getTable().getItems();
                for (int i = 0; i < checkedMethods.length; i++) {
                    TableItem tableItem = checkedMethods[i];
                    SelectionEvent selectionEvent = event;
                    selectionEvent.detail = SWT.CHECK;
                    selectionEvent.item = tableItem;
                    seiMembersToExtractTableViewer.handleSelect(selectionEvent);
                }
                updateSEISelectionStatus();
                statusListener.handleEvent(null);
            }

        });
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        selectAllButton.setLayoutData(gridData);

        Java2WSWidgetFactory.createPaddingLabel(composite);

        deselectAllButton = Java2WSWidgetFactory.createDeselectAllButton(composite);
        deselectAllButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                seiMembersToExtractTableViewer.setAllChecked(false);
                selectAllButton.setEnabled(true);
                deselectAllButton.setEnabled(false);

                TableItem[] checkedMethods = seiMembersToExtractTableViewer.getTable().getItems();
                for (int i = 0; i < checkedMethods.length; i++) {
                    TableItem tableItem = checkedMethods[i];
                    SelectionEvent selectionEvent = event;
                    selectionEvent.detail = SWT.CHECK;
                    selectionEvent.item = tableItem;
                    seiMembersToExtractTableViewer.handleSelect(selectionEvent);
                }

                updateSEISelectionStatus();
                statusListener.handleEvent(null);
            }
        });
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        deselectAllButton.setLayoutData(gridData);

        publicMethods = JDTUtils.getPublicMethods(startingPointType);
        NUMBER_OF_PUBLIC_METHODS = publicMethods.length;
        seiMembersToExtractTableViewer.setInput(publicMethods);

        enableSelectSEIControls(false);
        enableExtractSEIControls(false);
        selectAllButton.setEnabled(false);
        deselectAllButton.setEnabled(false);

        if (model.isUseServiceEndpointInterface()) {
            if (selectSEICombo.indexOf(model.getServiceEndpointInterfaceName()) == -1) {
                selectSEICombo.add(model.getServiceEndpointInterfaceName());
            }
            selectSEICombo.setText(model.getServiceEndpointInterfaceName());
            model.setFullyQualifiedJavaInterfaceName(model.getServiceEndpointInterfaceName());
        }

        return this;
    }

    private void updateExtractSEIInfo() {
        String interfaceName = seiInterfaceNameText.getText().trim();
        if (interfaceName.length() > 0) {
            String packageName = startingPointType.getPackageFragment().getElementName();
            if (packageName.length() > 0) {
                interfaceName = packageName + "." + interfaceName;
            }
            model.setServiceEndpointInterfaceName(interfaceName);
            model.setFullyQualifiedJavaInterfaceName(interfaceName);

            Object[] checkedMethods = seiMembersToExtractTableViewer.getCheckedElements();

            Map<IMethod, Map<String, Boolean>> methodMap = new HashMap<IMethod, Map<String, Boolean>>();
            for (int i = 0; i < checkedMethods.length; i++) {
                methodMap.put((IMethod)checkedMethods[i], model.getAnnotationMap());
            }
            model.setMethodMap(methodMap);
        }
    }
    
    private void validateSEISelection() {
        if (!useSEIButton.getSelection()) {
            SEI_SELECTION_STATUS = Status.OK_STATUS;
            return;
        }
        IType seiType = JDTUtils.findType(model.getProjectName(), selectSEICombo.getText());
        if (seiType != null) {
            try {
                IMethod[] seiMethods = seiType.getMethods();
                for (IMethod seiMethod : seiMethods) {
                    if (!isMethodImplemented(startingPointType, seiMethod)) {
                        SEI_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                                CXFCreationUIMessages.bind(CXFCreationUIMessages.WEBSERVICE_ENPOINTINTERFACE_MUST_IMPLEMENT,
                                        getImplementsMessage(seiType, seiMethod)));
                        break;
                    }
                }
                if (SEI_SELECTION_STATUS.isOK()) {
                    SEI_SELECTION_STATUS = validateSEIAddition();
                }
                model.setServiceEndpointInterfaceName(selectSEICombo.getText());
                model.setFullyQualifiedJavaInterfaceName(selectSEICombo.getText());
            } catch (JavaModelException jme) {
                CXFCreationUIPlugin.log(jme.getStatus());
            }
        } else {
            SEI_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                    CXFCreationUIMessages.bind(CXFCreationUIMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_FOUND,
                            selectSEICombo.getText()));
        }
    }

    private String getImplementsMessage(IType seiType, IMethod seiMethod) {
        StringBuilder message = new StringBuilder(seiType.getElementName());
        message.append("."); //$NON-NLS-1$
        message.append(seiMethod.getElementName());
        message.append("("); //$NON-NLS-1$
        String[] parameterTypes = seiMethod.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            String parameterType = Signature.toString(parameterTypes[i]);
            message.append(parameterType);
            if (i < parameterTypes.length - 1) {
                message.append(", "); //$NON-NLS-1$
            }
        }
        message.append(")"); //$NON-NLS-1$
        return message.toString();
    }

    private IStatus validateSEIAddition() {
        IStatus status = Status.OK_STATUS;
        try {
            IAnnotation webService = AnnotationUtils.getAnnotation(WebService.class, startingPointType);
            if (webService != null) {
                Object name = AnnotationUtils.getAnnotationValue(webService, NAME);
                if (name != null) {
                    status = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                            CXFCreationUIMessages.JAVA2WS_SELECT_SEI_WEBSERVICE_NAME_ATTRIBUTE_PRESENT);
                }
            }
        } catch (JavaModelException jme) {
            CXFCreationUIPlugin.log(jme.getStatus());
        }
        return status;
    }

    public void enableSelectSEIControls(boolean enable) {
        selectSEICombo.setEnabled(enable);
        browseSEIButton.setEnabled(enable);
    }

    public void enableExtractSEIControls(boolean enable) {
        seiInterfaceNameText.setEnabled(enable);
        seiMembersToExtractTable.setEnabled(enable);
        if (enable && NUMBER_OF_CHECKED_METHODS < NUMBER_OF_PUBLIC_METHODS) {
            selectAllButton.setEnabled(true);
        }

        if (enable && NUMBER_OF_CHECKED_METHODS > 0) {
            deselectAllButton.setEnabled(true);
        }

        if (!enable) {
            selectAllButton.setEnabled(false);
            deselectAllButton.setEnabled(false);
        }
    }

    @Override
    public IStatus getStatus() {
        return findMostSevere();
    }

    private void updateSEISelectionStatus() {
        if (useSEIButton.getSelection()) {
            if (!selectSEIButton.getSelection() && !extractSEIButton.getSelection()) {
                SEI_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                        CXFCreationUIMessages.JAVA2WS_SELECT_SEI_OPTION);
            } else if (selectSEIButton.getSelection()) {
                SEI_SELECTION_STATUS = Status.OK_STATUS;
                JAVA_TYPE_NAME_STATUS = Status.OK_STATUS;
                JAVA_TYPE_EXISTS_STATUS = Status.OK_STATUS;
                if (selectSEICombo.getText().length() > 0) {
                    validateSEISelection();
                } else {
                    SEI_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                            CXFCreationUIMessages.JAVA2WS_SELECT_SEI_MESSAGE);
                }
            } else if (extractSEIButton.getSelection()) {
                if (seiInterfaceNameText.getText().trim().length() > 0) {
                    String interfaceName = seiInterfaceNameText.getText().trim(); 
                    JAVA_TYPE_NAME_STATUS = JDTUtils.validateJavaTypeName(model.getProjectName(), interfaceName);
                    String compilationUnitName = interfaceName + ".java"; //$NON-NLS-1$
                    JAVA_TYPE_EXISTS_STATUS = checkTypeExists(startingPointType, compilationUnitName);
                    
                    if (NUMBER_OF_PUBLIC_METHODS > 0 && NUMBER_OF_CHECKED_METHODS == 0) {
                        SEI_SELECTION_STATUS = new Status(IStatus.INFO, CXFCreationUIPlugin.PLUGIN_ID,
                                CXFCreationUIMessages.bind(
                                        CXFCreationUIMessages.JAVA2WS_SELECT_SEI_EXTRACTED_METHODS,
                                        startingPointType.getElementName()));
                    } else {
                        SEI_SELECTION_STATUS = Status.OK_STATUS;
                    }
                } else {
                    SEI_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                            CXFCreationUIMessages.JAVA2WS_ENTER_SEI_NAME);
                }
            }
        } else {
            SEI_SELECTION_STATUS = Status.OK_STATUS;
            JAVA_TYPE_NAME_STATUS = Status.OK_STATUS;
            JAVA_TYPE_EXISTS_STATUS = Status.OK_STATUS;
        }
    }

    private IStatus findMostSevere() {
        if (JAVA_TYPE_NAME_STATUS.matches(IStatus.ERROR)) {
            return JAVA_TYPE_NAME_STATUS;
        }
        if (JAVA_TYPE_EXISTS_STATUS.matches(IStatus.ERROR)) {
            return JAVA_TYPE_EXISTS_STATUS;
        }
        if (SEI_SELECTION_STATUS.matches(IStatus.ERROR)) {
            return SEI_SELECTION_STATUS;
        }

        if (SEI_SELECTION_STATUS.getSeverity() >= JAVA_TYPE_NAME_STATUS.getSeverity()) {
            return SEI_SELECTION_STATUS;
        }

        if (JAVA_TYPE_EXISTS_STATUS.getSeverity() >= JAVA_TYPE_NAME_STATUS.getSeverity()) {
            return JAVA_TYPE_EXISTS_STATUS;
        }

        return JAVA_TYPE_NAME_STATUS;
    }

    public IStatus checkTypeExists(IType type, String compilationUnitName) {
        compilationUnitName = compilationUnitName.trim();

        IPackageFragment packageFragment = type.getPackageFragment();
        ICompilationUnit compilationUnit = packageFragment.getCompilationUnit(compilationUnitName);
        IResource resource = compilationUnit.getResource();

        if (resource.exists()) {
            return new Status(IStatus.ERROR, JAXWSCorePlugin.PLUGIN_ID, JAXWSCoreMessages
                    .bind(JAXWSCoreMessages.TYPE_WITH_NAME_ALREADY_EXISTS, new Object[] {
                            compilationUnitName, packageFragment.getElementName() }));
        }
        URI location = resource.getLocationURI();
        if (location != null) {
            try {
                IFileStore fileStore = EFS.getStore(location);
                if (fileStore.fetchInfo().exists()) {
                    return new Status(IStatus.ERROR, JAXWSCorePlugin.PLUGIN_ID,
                            JAXWSCoreMessages.TYPE_NAME_DIFFERENT_CASE_EXISTS);
                }
            } catch (CoreException ce) {
                JAXWSCorePlugin.log(ce.getStatus());
            }
        }
        return Status.OK_STATUS;
    }
}

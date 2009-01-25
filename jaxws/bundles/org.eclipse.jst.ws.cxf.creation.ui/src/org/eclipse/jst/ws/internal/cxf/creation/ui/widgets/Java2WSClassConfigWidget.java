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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.utils.JDTUtils;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIPlugin;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.Java2WSWidgetFactory;
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
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class Java2WSClassConfigWidget extends SimpleWidgetDataContributor {
    private IStatus JAVA_TYPE_NAME_STATUS = Status.OK_STATUS;
    private IStatus JAVA_TYPE_EXISTS_STATUS = Status.OK_STATUS;
    private IStatus SEI_SELECTION_STATUS = Status.OK_STATUS;

    private int NUMBER_OF_PUBLIC_METHODS;
    private int NUMBER_OF_CHECKED_METHODS;
    private IMethod[] publicMethods;

    private Button useSEIButton;
    private Button selectSEIButton;
    private Button extractSEIButton;

    private Combo selectSEICombo;
    private Text seiInterfaceNameText;
    private Table seiMembersToExtractTable;
    private Button selectAllButton;
    private Button deselectAllButton;

    private Java2WSDataModel model;
    private IType startingPointType;

    public Java2WSClassConfigWidget(Java2WSDataModel model, IType startingPointType) {
        this.model = model;
        this.startingPointType = startingPointType;
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

        Label infoLabel = Java2WSWidgetFactory.createInformationLabel(composite, startingPointType);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.horizontalSpan = 3;
        gridData.widthHint = 100;
        infoLabel.setLayoutData(gridData);

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
                if (selectSEIButton.getSelection() && selectSEICombo.getSelectionIndex() != -1) {
                    model.setFullyQualifiedJavaInterfaceName(selectSEICombo.getText());
                }
                updateSEISelectionStatus();
                statusListener.handleEvent(null);
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
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        selectSEICombo.setLayoutData(gridData);

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
                String interfaceName = seiInterfaceNameText.getText();
                model.setServiceEndpointInterfaceName(interfaceName);

                String compilationUnitName = interfaceName + ".java"; //$NON-NLS-1$
                JAVA_TYPE_NAME_STATUS = JDTUtils.validateJavaTypeName(model.getProjectName(), interfaceName);
                JAVA_TYPE_EXISTS_STATUS = JDTUtils.checkTypeExists(startingPointType, compilationUnitName);
                updateSEISelectionStatus();
                statusListener.handleEvent(null);
            }
        });

        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        seiInterfaceNameText.setLayoutData(gridData);

        Java2WSWidgetFactory.createPaddingLabel(composite);

        Java2WSWidgetFactory.createMemebersToExtractLabel(composite);

        Java2WSWidgetFactory.createPaddingLabel(composite);
        Java2WSWidgetFactory.createPaddingLabel(composite);

        final CheckboxTableViewer seiMembersToExtractTableViewer = Java2WSWidgetFactory
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

        return this;
    }

    public void enableSelectSEIControls(boolean enable) {
        selectSEICombo.setEnabled(enable);
    }

    public void enableExtractSEIControls(boolean enable) {
        seiInterfaceNameText.setEnabled(enable);
        seiMembersToExtractTable.setEnabled(enable);
        if (enable && (NUMBER_OF_CHECKED_METHODS < NUMBER_OF_PUBLIC_METHODS)) {
            selectAllButton.setEnabled(true);
        }

        if (enable && (NUMBER_OF_CHECKED_METHODS > 0)) {
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
            }

            if (selectSEIButton.getSelection() && selectSEICombo.getText().length() == 0) {
                SEI_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                        CXFCreationUIMessages.JAVA2WS_SELECT_SEI_FROM_TYPE_HIERARCHY);
            } else if (selectSEIButton.getSelection() && selectSEICombo.getText().length() > 0) {
                SEI_SELECTION_STATUS = Status.OK_STATUS;
            }

            if (extractSEIButton.getSelection() && seiInterfaceNameText.getText().trim().length() == 0) {
                SEI_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                        CXFCreationUIMessages.JAVA2WS_ENTER_SEI_NAME);
            } else if (extractSEIButton.getSelection() && NUMBER_OF_PUBLIC_METHODS > 0
                    && NUMBER_OF_CHECKED_METHODS == 0) {
                SEI_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                        CXFCreationUIMessages.bind(
                                CXFCreationUIMessages.JAVA2WS_SELECT_SEI_EXTRACTED_METHODS,
                                startingPointType.getElementName()));
            } else if (extractSEIButton.getSelection() && seiInterfaceNameText.getText().trim().length() > 0
                    && NUMBER_OF_PUBLIC_METHODS > 0 && NUMBER_OF_CHECKED_METHODS > 0) {
                SEI_SELECTION_STATUS = Status.OK_STATUS;
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

        return JAVA_TYPE_NAME_STATUS;    }

}

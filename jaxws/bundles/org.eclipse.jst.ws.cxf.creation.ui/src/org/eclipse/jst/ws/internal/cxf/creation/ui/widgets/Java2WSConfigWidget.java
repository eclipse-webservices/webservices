/*******************************************************************************
 * Copyright (c) 2008, 2010 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIPlugin;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.Java2WSWidgetFactory;
import org.eclipse.jst.ws.jaxws.core.utils.WSDLUtils;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

@SuppressWarnings("restriction")
public class Java2WSConfigWidget extends SimpleWidgetDataContributor {
    private IStatus WSDL_FILENAME_OK_STATUS = Status.OK_STATUS;
    private IStatus WSDL_FILENAME_ERROR_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
            CXFCreationUIMessages.JAVA2WS_ENTER_VALID_WSDL_NAME);

    private Button generateClientButton;
    private Button generateServerButton;
    private Button generateWraperFaultBeansButton;
    private Button generateWSDLButton;
    private Combo soapBindingCombo;
    private Button createXSDImports;
    private Text wsdlFileText;
    private Java2WSDataModel model;

    public Java2WSConfigWidget() {
    }

    public void setJava2WSDataModel(Java2WSDataModel model) {
        this.model = model;
    }

    @Override
    public void internalize() {
        if (model.getDefaultRuntimeVersion().compareTo(CXFCorePlugin.CXF_VERSION_2_1) >= 0) {
            generateClientButton.setSelection(model.isGenerateClient());
            generateServerButton.setSelection(model.isGenerateServer());
            generateWraperFaultBeansButton.setSelection(model.isGenerateWrapperFaultBeans());
            generateWSDLButton.setSelection(model.isGenerateWSDL());
            enableWSDLGroup(model.isGenerateWSDL());
        }
        if (model.isSoap12Binding()) {
            soapBindingCombo.setText("SOAP 1.2"); //$NON-NLS-1$
        } else {
            soapBindingCombo.setText("SOAP 1.1"); //$NON-NLS-1$
        }
        createXSDImports.setSelection(model.isGenerateXSDImports());
    }

    @Override
    public WidgetDataEvents addControls(Composite parent, final Listener statusListener) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        composite.setLayout(gridLayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        composite.setLayoutData(gridData);

        Group java2wsGroup = null;
        if (model.getDefaultRuntimeVersion().compareTo(CXFCorePlugin.CXF_VERSION_2_1) >= 0) {
            // Frontend
            //            Java2WSWidgetFactory.createFrontendLabel(composite);
            //
            //            final Combo frontendCombo = Java2WSWidgetFactory.createFrontendCombo(composite, model);
            //            gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
            //            frontendCombo.setLayoutData(gridData);

            // Java2WS Group
            java2wsGroup = new Group(composite, SWT.SHADOW_IN);
            java2wsGroup.setText(CXFCreationUIMessages.JAVA2WS_GROUP_LABEL);
            GridLayout java2wslayout = new GridLayout();

            java2wslayout.numColumns = 3;

            java2wslayout.marginHeight = 10;
            java2wsGroup.setLayout(java2wslayout);
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.horizontalSpan = 2;
            java2wsGroup.setLayoutData(gridData);

            // Gen Client
            generateClientButton = Java2WSWidgetFactory.createGenerateClientButton(java2wsGroup, model);

            // Gen Server
            generateServerButton = Java2WSWidgetFactory.createGenerateServerButton(java2wsGroup, model);

            // Gen Wrapper and Fault Bean
            generateWraperFaultBeansButton = Java2WSWidgetFactory.createGenerateWrapperFaultBeanButton(
                    java2wsGroup, model);

            // Gen WSDL
            generateWSDLButton = Java2WSWidgetFactory.createGenerateWSDLButton(java2wsGroup, model);
            generateWSDLButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    enableWSDLGroup(generateWSDLButton.getSelection());
                }
            });
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.horizontalSpan = 3;
            generateWSDLButton.setLayoutData(gridData);
        }

        Group wsdlGroup = new Group(java2wsGroup == null ? composite : java2wsGroup, SWT.SHADOW_ETCHED_IN);
        GridLayout wsdlGroupLayout = new GridLayout(2, false);
        wsdlGroup.setLayout(wsdlGroupLayout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        wsdlGroup.setLayoutData(gridData);

        Java2WSWidgetFactory.createWSDLFileNameLabel(wsdlGroup);

        wsdlFileText = Java2WSWidgetFactory.createWSDLFileNameText(wsdlGroup, model);
        wsdlFileText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                statusListener.handleEvent(null);
            }
        });

        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        wsdlFileText.setLayoutData(gridData);

        Java2WSWidgetFactory.createSOAPBindingLabel(wsdlGroup);

        soapBindingCombo = Java2WSWidgetFactory.createSOAPBingCombo(wsdlGroup, model);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        soapBindingCombo.setLayoutData(gridData);

        createXSDImports = Java2WSWidgetFactory.createXSDImportsButton(wsdlGroup, model);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        createXSDImports.setLayoutData(gridData);

        enableWSDLGroup(model.isGenerateWSDL());

        return this;
    }

    private void enableWSDLGroup(boolean enable) {
        wsdlFileText.setEnabled(enable);
        soapBindingCombo.setEnabled(enable);
        createXSDImports.setEnabled(enable);
    }

    @Override
    public IStatus getStatus() {
        return getWSDLFileNameStatus();
    }

    private IStatus getWSDLFileNameStatus() {
        IStatus status = WSDL_FILENAME_OK_STATUS;
        String wsdlFileName = wsdlFileText.getText();
        if (!WSDLUtils.isValidWSDLFileName(wsdlFileName)) {
            status = WSDL_FILENAME_ERROR_STATUS;
        }
        return status;
    }
}

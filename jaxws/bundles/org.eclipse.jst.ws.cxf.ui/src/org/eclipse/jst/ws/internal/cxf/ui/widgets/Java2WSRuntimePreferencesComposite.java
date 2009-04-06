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
package org.eclipse.jst.ws.internal.cxf.ui.widgets;

import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.context.Java2WSPersistentContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TabFolder;

/**
 * @author sclarke
 */
public class Java2WSRuntimePreferencesComposite extends Composite {
    private Java2WSPersistentContext context = CXFCorePlugin.getDefault().getJava2WSContext();
    
    private Combo soapBindingCombo;
    private Button createXSDImportsButton;

    private Button generateClientButton;
    private Button generateServerButton;
    private Button generateWrapperFaultBeanButton;
    private Button generateWSDLButton;
    
    private TabFolder tabFolder;
    
    public Java2WSRuntimePreferencesComposite(Composite parent, int style, TabFolder tabFolder) {
        super(parent, style);
        this.tabFolder = tabFolder;
    }

    public void addControls() {
        GridLayout preflayout = new GridLayout();

        preflayout.numColumns = 2;
        preflayout.marginHeight = 10;
        this.setLayout(preflayout);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        this.setLayoutData(gridData);

        // Java2WS Group
        Group java2wsGroup = new Group(this, SWT.SHADOW_IN);
        java2wsGroup.setText(CXFUIMessages.JAVA2WS_GROUP_LABEL);
        GridLayout java2wslayout = new GridLayout();

        java2wslayout.numColumns = 3;

        java2wslayout.marginHeight = 10;
        java2wsGroup.setLayout(java2wslayout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        java2wsGroup.setLayoutData(gridData);

        // Frontend
        Java2WSWidgetFactory.createFrontendLabel(java2wsGroup);
        Combo frontendCombo = Java2WSWidgetFactory.createFrontendCombo(java2wsGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        frontendCombo.setLayoutData(gridData);

        // Databinding
        Java2WSWidgetFactory.createDatabindingLabel(java2wsGroup);
        Combo databindingCombo = Java2WSWidgetFactory.createDatabindingCombo(java2wsGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        databindingCombo.setLayoutData(gridData);

        // Gen Client
        generateClientButton = Java2WSWidgetFactory.createGenerateClientButton(java2wsGroup, context);

        // Gen Server
        generateServerButton = Java2WSWidgetFactory.createGenerateServerButton(java2wsGroup, context);

        // Gen Wrapper and Fault Bean
        generateWrapperFaultBeanButton = Java2WSWidgetFactory.createGenerateWrapperFaultBeanButton(
                java2wsGroup, context);

        // Gen WSDL
        generateWSDLButton = Java2WSWidgetFactory.createGenerateWSDLButton(java2wsGroup, context);
        generateWSDLButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                enableWSDLGroup(generateWSDLButton.getSelection());
            }
        });

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        generateWSDLButton.setLayoutData(gridData);

        Group wsdlGroup = new Group(java2wsGroup, SWT.SHADOW_ETCHED_IN);
        GridLayout wsdlGroupLayout = new GridLayout(2, false);
        wsdlGroup.setLayout(wsdlGroupLayout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        wsdlGroup.setLayoutData(gridData);

        Java2WSWidgetFactory.createSOAPBindingLabel(wsdlGroup);

        soapBindingCombo = Java2WSWidgetFactory.createSOAPBingCombo(wsdlGroup, context);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        soapBindingCombo.setLayoutData(gridData);

        createXSDImportsButton = Java2WSWidgetFactory.createXSDImportsButton(wsdlGroup, context);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        createXSDImportsButton.setLayoutData(gridData);
        
        Link link = new Link(this, SWT.NONE);
        link.setText(CXFUIMessages.ANNOTATIONS_PREFERENCES_LINK);
        link.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                tabFolder.setSelection(3); 
            }
        });
        enableWSDLGroup(context.isGenerateWSDL());
    }

    protected void enableWSDLGroup(boolean enable) {
        soapBindingCombo.setEnabled(enable);
        createXSDImportsButton.setEnabled(enable);
    }
    
    public void setDefaults() {
        if (CXFModelUtils.getDefaultBooleanValue(CXFPackage.JAVA2_WS_CONTEXT,
                CXFPackage.JAVA2_WS_CONTEXT__SOAP12_BINDING)) {
            soapBindingCombo.setText("SOAP 1.2"); //$NON-NLS-1$
        } else {
            soapBindingCombo.setText("SOAP 1.1"); //$NON-NLS-1$
        }
        
        createXSDImportsButton.setSelection(CXFModelUtils.getDefaultBooleanValue(CXFPackage.JAVA2_WS_CONTEXT,
                CXFPackage.JAVA2_WS_CONTEXT__GENERATE_XSD_IMPORTS));

        generateClientButton.setSelection(CXFModelUtils.getDefaultBooleanValue(CXFPackage.CXF_CONTEXT,
                CXFPackage.CXF_CONTEXT__GENERATE_CLIENT));
        
        generateServerButton.setSelection(CXFModelUtils.getDefaultBooleanValue(CXFPackage.CXF_CONTEXT,
                CXFPackage.CXF_CONTEXT__GENERATE_SERVER));
        
        generateWrapperFaultBeanButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WRAPPER_FAULT_BEANS));
        
        generateWSDLButton.setSelection(CXFModelUtils.getDefaultBooleanValue(CXFPackage.JAVA2_WS_CONTEXT,
                CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WSDL));
    }
    
    public void refresh() {
        if (context.isSoap12Binding()) {
            soapBindingCombo.setText("SOAP 1.2"); //$NON-NLS-1$    
        } else {
            soapBindingCombo.setText("SOAP 1.1"); //$NON-NLS-1$            
        }

        createXSDImportsButton.setSelection(context.isGenerateXSDImports());
        generateClientButton.setSelection(context.isGenerateClient());
        generateServerButton.setSelection(context.isGenerateServer());
        generateWrapperFaultBeanButton.setSelection(context.isGenerateWrapperFaultBeans());
        generateWSDLButton.setSelection(context.isGenerateWSDL());
    }
    
    public void storeValues() {
        if (soapBindingCombo.getText().equals("SOAP 1.2")) {
            context.setSoap12Binding(true);
        } else {
            context.setSoap12Binding(false);
        }

        context.setGenerateXSDImports(createXSDImportsButton.getSelection());
        context.setGenerateClient(generateClientButton.getSelection());
        context.setGenerateServer(generateServerButton.getSelection());
        context.setGenerateWrapperFaultBeans(generateWrapperFaultBeanButton.getSelection());
        context.setGenerateWSDL(generateWSDLButton.getSelection());
    }
}

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
public class Java2WSDLRuntimePreferencesComposite extends Composite {
    private Java2WSPersistentContext context = CXFCorePlugin.getDefault().getJava2WSContext();

    private Combo soapBindingCombo;
    private Button createXSDImportsButton;    
    private TabFolder tabFolder;
    
    public Java2WSDLRuntimePreferencesComposite(Composite parent, int style, TabFolder tabFolder) {
        super(parent, style);
        this.tabFolder = tabFolder;
    }

    public void addControls() {
        GridLayout preflayout = new GridLayout(1, true);
        this.setLayout(preflayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        this.setLayoutData(gridData);

        Group java2wsdlGroup = new Group(this, SWT.SHADOW_IN);
        java2wsdlGroup.setText(CXFUIMessages.JAVA2WSDL_GROUP_LABEL);
        GridLayout java2wslayout = new GridLayout();
        java2wslayout.numColumns = 2;
        java2wsdlGroup.setLayout(java2wslayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        java2wsdlGroup.setLayoutData(gridData);

        Java2WSWidgetFactory.createSOAPBindingLabel(java2wsdlGroup);

        soapBindingCombo = Java2WSWidgetFactory.createSOAPBingCombo(java2wsdlGroup, context);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        soapBindingCombo.setLayoutData(gridData);

        createXSDImportsButton = Java2WSWidgetFactory.createXSDImportsButton(java2wsdlGroup, context);
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
    }
    
    public void refresh() {
        if (context.isSoap12Binding()) {
            soapBindingCombo.setText("SOAP 1.2"); //$NON-NLS-1$    
        } else {
            soapBindingCombo.setText("SOAP 1.1"); //$NON-NLS-1$            
        }
        createXSDImportsButton.setSelection(context.isGenerateXSDImports());
    }
    
    public void storeValues() {
        if (soapBindingCombo.getText().equals("SOAP 1.2")) {
            context.setSoap12Binding(true);
        } else {
            context.setSoap12Binding(false);
        }

        context.setGenerateXSDImports(createXSDImportsButton.getSelection());
    }
}

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
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * @author sclarke
 */
public class CXF21WSDL2JavaPreferencesComposite extends CXF20WSDL2JavaPreferencesComposite {
    WSDL2JavaContext context = CXFCorePlugin.getDefault().getWSDL2JavaContext();

    protected Button useDefaultValuesButton;
    protected Button noAddressBindingButton;

    public CXF21WSDL2JavaPreferencesComposite(Composite parent, int style) {
        super(parent, style);
        addPaintListener(this);
    }

    @Override
    public void addControls() {
        GridLayout preflayout = new GridLayout(1, true);
        this.setLayout(preflayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        this.setLayoutData(gridData);

        Group wsdl2javaGroup = new Group(this, SWT.SHADOW_IN);
        wsdl2javaGroup.setText(CXFUIMessages.WSDL2JAVA_GROUP_LABEL);
        GridLayout wsdl2javalayout = new GridLayout(2, false);
        wsdl2javaGroup.setLayout(wsdl2javalayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        wsdl2javaGroup.setLayoutData(gridData);

        // Frontend
        // WSDL2JavaWidgetFactory.createFrontendLabel(wsdl2javaGroup);
        // Combo frontendCombo =
        // WSDL2JavaWidgetFactory.createFrontendCombo(wsdl2javaGroup, context);
        // gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        // frontendCombo.setLayoutData(gridData);

        // Databinding
        // WSDL2JavaWidgetFactory.createDatabindingLabel(wsdl2javaGroup);
        // Combo databindingCombo =
        // WSDL2JavaWidgetFactory.createDatabindingCombo(wsdl2javaGroup,
        // context);
        // gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        // databindingCombo.setLayoutData(gridData);

        // WSDL Version
        // WSDL2JavaWidgetFactory. createWSDLVersionLabel(wsdl2javaGroup);
        // Combo wsdlVersionCombo =
        // WSDL2JavaWidgetFactory.createWSDLVersionCombo(wsdl2javaGroup,
        // context);
        // gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        // wsdlVersionCombo.setLayoutData(gridData);

        generateServerButton = WSDL2JavaWidgetFactory.createGenerateServerButton(wsdl2javaGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        generateServerButton.setLayoutData(gridData);

        generateImplementationButton = WSDL2JavaWidgetFactory.createGenerateImplementationButton(
                wsdl2javaGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        generateImplementationButton.setLayoutData(gridData);

        useDefaultValuesButton = WSDL2JavaWidgetFactory.createDefaultValuesButton(wsdl2javaGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        useDefaultValuesButton.setLayoutData(gridData);

        processSOAPHeadersButton = WSDL2JavaWidgetFactory.createProcessSOAPHeadersButton(wsdl2javaGroup,
                context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        processSOAPHeadersButton.setLayoutData(gridData);

        namespacePackageMappingButton = WSDL2JavaWidgetFactory.createNamespacePackageMappingButton(
                wsdl2javaGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        namespacePackageMappingButton.setLayoutData(gridData);

        excludesNamespaceMappingButton = WSDL2JavaWidgetFactory.createExcludesNamespaceMappingButton(
                wsdl2javaGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        excludesNamespaceMappingButton.setLayoutData(gridData);

        noAddressBindingButton = WSDL2JavaWidgetFactory.createNoAddressBindingButton(wsdl2javaGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        noAddressBindingButton.setLayoutData(gridData);
            
        Group xjcArgGroup = new Group(this, SWT.SHADOW_IN);
        xjcArgGroup.setText(CXFUIMessages.WSDL2JAVA_XJC_ARG_GROUP_TITLE);
        GridLayout xjcArgLayout = new GridLayout(1, true);
        xjcArgGroup.setLayout(xjcArgLayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        xjcArgGroup.setLayoutData(gridData);

        xjcArgsTable = WSDL2JavaWidgetFactory.createXJCArgTable(xjcArgGroup, context);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 3;
        gridData.verticalSpan = 6;
        xjcArgsTable.setLayoutData(gridData);

        xjcDefaultValuesTableItem = WSDL2JavaWidgetFactory.createXJCDefaultValuesTableItem(xjcArgsTable,
                context);
        xjcToStringTableItem = WSDL2JavaWidgetFactory.createXJCToStringTableItem(xjcArgsTable, context);
        xjcToStringMultiLineTableItem = WSDL2JavaWidgetFactory.createXJCToStringMultiLineTableItem(
                xjcArgsTable, context);
        xjcToStringSimpleTableItem = WSDL2JavaWidgetFactory.createXJCToStringSimpleTableItem(xjcArgsTable,
                context);
        xjcLocatorTableItem = WSDL2JavaWidgetFactory.createXJCLocatorTableItem(xjcArgsTable, context);
        xjcSyncMethodsTableItem = WSDL2JavaWidgetFactory.createXJCSyncMethodsTableItem(xjcArgsTable, context);
        xjcMarkGeneratedTableItem = WSDL2JavaWidgetFactory.createXJCMarkGeneratedTableItem(xjcArgsTable,
                context);
    }
    
    @Override
    public void paintControl(PaintEvent event) {
        super.paintControl(event);
        if (useDefaultValuesButton != null && noAddressBindingButton != null) {
            useDefaultValuesButton.setSelection(context.isUseDefaultValues());
            noAddressBindingButton.setSelection(context.isNoAddressBinding());
        }
    }
    
    @Override
    public void setDefaults() {
        useDefaultValuesButton.setSelection(true);
        noAddressBindingButton.setSelection(false);
        
        generateServerButton.setSelection(false);
        generateImplementationButton.setSelection(true);
        processSOAPHeadersButton.setSelection(false);
        namespacePackageMappingButton.setSelection(true);
        excludesNamespaceMappingButton.setSelection(true);

        xjcDefaultValuesTableItem.setChecked(false);
        xjcToStringTableItem.setChecked(false);
        xjcToStringMultiLineTableItem.setChecked(false);
        xjcToStringSimpleTableItem.setChecked(false);
        xjcLocatorTableItem.setChecked(false);
        xjcSyncMethodsTableItem.setChecked(false);
        xjcMarkGeneratedTableItem.setChecked(false);
    }

}

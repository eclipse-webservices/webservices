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
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author sclarke
 */
public class CXF20WSDL2JavaPreferencesComposite extends Composite {
    WSDL2JavaContext context = CXFCorePlugin.getDefault().getWSDL2JavaContext();

    private Button generateServerButton;
    private Button generateImplementationButton;
    private Button processSOAPHeadersButton;
    private Button namespacePackageMappingButton;
    private Button excludesNamespaceMappingButton;

    private Button useDefaultValuesButton;
    private Button autoNameResolutionButton;

    private Table xjcArgsTable;

    private TableItem xjcDefaultValuesTableItem;
    private TableItem xjcToStringTableItem;
    private TableItem xjcToStringMultiLineTableItem;
    private TableItem xjcToStringSimpleTableItem;
    private TableItem xjcLocatorTableItem;
    private TableItem xjcSyncMethodsTableItem;
    private TableItem xjcMarkGeneratedTableItem;

    public CXF20WSDL2JavaPreferencesComposite(Composite parent, int style) {
        super(parent, style);
    }

    public void addControls() {
        GridLayout preflayout = new GridLayout(1, true);
        this.setLayout(preflayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        this.setLayoutData(gridData);

        Group wsdl2javaGroup = new Group(this, SWT.SHADOW_IN);
        wsdl2javaGroup.setText(CXFUIMessages.WSDL2JAVA_GROUP_LABEL);
        GridLayout wsdl2javalayout = new GridLayout(1, true);
        wsdl2javaGroup.setLayout(wsdl2javalayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        wsdl2javaGroup.setLayoutData(gridData);

        generateServerButton = WSDL2JavaWidgetFactory.createGenerateServerButton(wsdl2javaGroup, context);
        generateImplementationButton = WSDL2JavaWidgetFactory.createGenerateImplementationButton(
                wsdl2javaGroup, context);
        
        useDefaultValuesButton = WSDL2JavaWidgetFactory.createDefaultValuesButton(wsdl2javaGroup, context);

        processSOAPHeadersButton = WSDL2JavaWidgetFactory.createProcessSOAPHeadersButton(wsdl2javaGroup,
                context);

        namespacePackageMappingButton = WSDL2JavaWidgetFactory.createNamespacePackageMappingButton(
                wsdl2javaGroup, context);

        excludesNamespaceMappingButton = WSDL2JavaWidgetFactory.createExcludesNamespaceMappingButton(
                wsdl2javaGroup, context);
        
        if (CXFModelUtils.isAutoNameResolutionPermitted()) {
            autoNameResolutionButton = WSDL2JavaWidgetFactory.createAutoNameResolutionButton(wsdl2javaGroup,
                    context);
        }

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

    public void setDefaults() {
        useDefaultValuesButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__USE_DEFAULT_VALUES));

        generateServerButton.setSelection(CXFModelUtils.getDefaultBooleanValue(CXFPackage.CXF_CONTEXT,
                CXFPackage.CXF_CONTEXT__GENERATE_SERVER));
        generateImplementationButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__GENERATE_IMPLEMENTATION));
        processSOAPHeadersButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__PROCESS_SOAP_HEADERS));
        namespacePackageMappingButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT,
                CXFPackage.WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING));
        excludesNamespaceMappingButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT,
                CXFPackage.WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING));

        if (canUpdateAutoNameResolution()) {
            autoNameResolutionButton.setSelection(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__AUTO_NAME_RESOLUTION));
        }

        xjcDefaultValuesTableItem.setChecked(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__XJC_USE_DEFAULT_VALUES));
        xjcToStringTableItem.setChecked(CXFModelUtils.getDefaultBooleanValue(CXFPackage.WSDL2_JAVA_CONTEXT,
                CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING));
        xjcToStringMultiLineTableItem.setChecked(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING_MULTI_LINE));
        xjcToStringSimpleTableItem.setChecked(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING_SIMPLE));
        xjcLocatorTableItem.setChecked(CXFModelUtils.getDefaultBooleanValue(CXFPackage.WSDL2_JAVA_CONTEXT,
                CXFPackage.WSDL2_JAVA_CONTEXT__XJC_LOCATOR));
        xjcSyncMethodsTableItem.setChecked(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__XJC_SYNC_METHODS));
        xjcMarkGeneratedTableItem.setChecked(CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.WSDL2_JAVA_CONTEXT, CXFPackage.WSDL2_JAVA_CONTEXT__XJC_MARK_GENERATED));
    }
    
    public void refresh() {
        useDefaultValuesButton.setSelection(context.isUseDefaultValues());
        generateServerButton.setSelection(context.isGenerateServer());
        generateImplementationButton.setSelection(context.isGenerateImplementation());
        processSOAPHeadersButton.setSelection(context.isProcessSOAPHeaders());
        namespacePackageMappingButton.setSelection(context.isLoadDefaultNamespacePackageNameMapping());
        excludesNamespaceMappingButton.setSelection(context.isLoadDefaultExcludesNamepsaceMapping());
        
        if (canUpdateAutoNameResolution()) {
            autoNameResolutionButton.setSelection(context.isAutoNameResolution());
        }
        
        xjcDefaultValuesTableItem.setChecked(context.isXjcUseDefaultValues());
        xjcToStringTableItem.setChecked(context.isXjcToString());
        xjcToStringMultiLineTableItem.setChecked(context.isXjcToStringMultiLine());
        xjcToStringSimpleTableItem.setChecked(context.isXjcToStringSimple());
        xjcLocatorTableItem.setChecked(context.isXjcLocator());
        xjcSyncMethodsTableItem.setChecked(context.isXjcSyncMethods());
        xjcMarkGeneratedTableItem.setChecked(context.isXjcMarkGenerated());
    }
    
    public void storeValues() {
        context.setUseDefaultValues(useDefaultValuesButton.getSelection());
        context.setGenerateServer(generateServerButton.getSelection());
        context.setGenerateImplementation(generateImplementationButton.getSelection());
        context.setProcessSOAPHeaders(processSOAPHeadersButton.getSelection());
        context.setLoadDefaultNamespacePackageNameMapping(namespacePackageMappingButton.getSelection());
        context.setLoadDefaultExcludesNamepsaceMapping(excludesNamespaceMappingButton.getSelection());
        
        if (canUpdateAutoNameResolution()) {
            context.setAutoNameResolution(autoNameResolutionButton.getSelection());
        }
        
        context.setXjcUseDefaultValues(xjcDefaultValuesTableItem.getChecked());
        context.setXjcToString(xjcToStringTableItem.getChecked());
        context.setXjcToStringMultiLine(xjcToStringMultiLineTableItem.getChecked());
        context.setXjcToStringSimple(xjcToStringSimpleTableItem.getChecked());
        context.setXjcLocator(xjcLocatorTableItem.getChecked());
        context.setXjcSyncMethods(xjcSyncMethodsTableItem.getChecked());
        context.setXjcMarkGenerated(xjcMarkGeneratedTableItem.getChecked());
    }
    
    private boolean canUpdateAutoNameResolution() {
        return autoNameResolutionButton != null && CXFModelUtils.isAutoNameResolutionPermitted();
    }

}

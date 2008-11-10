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
import org.eclipse.swt.events.PaintListener;
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
public class CXF20WSDL2JavaPreferencesComposite extends Composite implements PaintListener {
    WSDL2JavaContext context = CXFCorePlugin.getDefault().getWSDL2JavaContext();

    protected Button generateServerButton;
    protected Button generateImplementationButton;
    protected Button processSOAPHeadersButton;
    protected Button namespacePackageMappingButton;
    protected Button excludesNamespaceMappingButton;

    protected Table xjcArgsTable;

    protected TableItem xjcDefaultValuesTableItem;
    protected TableItem xjcToStringTableItem;
    protected TableItem xjcToStringMultiLineTableItem;
    protected TableItem xjcToStringSimpleTableItem;
    protected TableItem xjcLocatorTableItem;
    protected TableItem xjcSyncMethodsTableItem;
    protected TableItem xjcMarkGeneratedTableItem;

    public CXF20WSDL2JavaPreferencesComposite(Composite parent, int style) {
        super(parent, style);
        addPaintListener(this);
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

        processSOAPHeadersButton = WSDL2JavaWidgetFactory.createProcessSOAPHeadersButton(wsdl2javaGroup,
                context);

        namespacePackageMappingButton = WSDL2JavaWidgetFactory.createNamespacePackageMappingButton(
                wsdl2javaGroup, context);

        excludesNamespaceMappingButton = WSDL2JavaWidgetFactory.createExcludesNamespaceMappingButton(
                wsdl2javaGroup, context);

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

    public void paintControl(PaintEvent event) {
        if ((generateServerButton == null) || (generateImplementationButton == null)
                || (processSOAPHeadersButton == null) || (namespacePackageMappingButton == null)
                || (excludesNamespaceMappingButton == null) || (xjcDefaultValuesTableItem == null)
                || (xjcToStringTableItem == null) || (xjcToStringMultiLineTableItem == null)
                || (xjcToStringSimpleTableItem == null) || (xjcLocatorTableItem == null)
                || (xjcSyncMethodsTableItem == null) || (xjcMarkGeneratedTableItem == null)) {
            return;
        }
        generateServerButton.setSelection(context.isGenerateServer());
        generateImplementationButton.setSelection(context.isGenerateImplementation());
        processSOAPHeadersButton.setSelection(context.isProcessSOAPHeaders());
        namespacePackageMappingButton.setSelection(context.isLoadDefaultNamespacePackageNameMapping());
        excludesNamespaceMappingButton.setSelection(context.isLoadDefaultExcludesNamepsaceMapping());

        xjcDefaultValuesTableItem.setChecked(context.isXjcUseDefaultValues());
        xjcToStringTableItem.setChecked(context.isXjcToString());
        xjcToStringMultiLineTableItem.setChecked(context.isXjcToStringMultiLine());
        xjcToStringSimpleTableItem.setChecked(context.isXjcToStringSimple());
        xjcLocatorTableItem.setChecked(context.isXjcLocator());
        xjcSyncMethodsTableItem.setChecked(context.isXjcSyncMethods());
        xjcMarkGeneratedTableItem.setChecked(context.isXjcMarkGenerated());
    }
    
    public void setDefaults() {
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

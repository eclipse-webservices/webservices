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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.internal.cxf.ui.viewers.PackageNameEditingSupport;
import org.eclipse.jst.ws.internal.cxf.ui.viewers.PackageNameTableContentProvider;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.WSDL2JavaWidgetFactory;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

@SuppressWarnings("restriction")
public class WSDL2JavaConfigWidget extends SimpleWidgetDataContributor {
    private IStatus status = Status.OK_STATUS;
    private Composite parent;
    private Listener statusListener;

    private WSDL2JavaDataModel model;
    
    private Combo outputDirCombo;
    private Text packageNameText;
    private Button namespaceMappingButton;
    private TableViewer packageNameTableViewer;
    private Combo serviceNameCombo;
    private List bindingFilesList;
    
    private Composite namespaceCompositeHolder;
    private Composite namespaceComposite;
    private int namespaceCompositeHeight = -1;

    public WSDL2JavaConfigWidget() {
    }
    
    public void setWSDL2JavaDataModel(WSDL2JavaDataModel model) {
        this.model = model;
    }
    
    @Override
    public void internalize() {
        WSDL2JavaWidgetFactory.populateOutputDirectoryCombo(outputDirCombo, model.getProjectName());
        packageNameText.setText(model.getIncludedNamespaces().get(model.getTargetNamespace()));
        namespaceMappingButton.setSelection(false);
        expandNamespaceComposite(false);
        if (packageNameTableViewer != null && packageNameTableViewer.getContentProvider() != null) {
            packageNameTableViewer.setInput(model.getWsdlDefinition());
        }
        WSDL2JavaWidgetFactory.populateServiceNameCombo(serviceNameCombo, model);

        bindingFilesList.removeAll();
    }

    @Override
    public WidgetDataEvents addControls(final Composite parent, final Listener statusListener) {
        this.parent = parent;
        this.statusListener = statusListener;

        final Composite mainComposite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        mainComposite.setLayout(gridLayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        mainComposite.setLayoutData(gridData);

        // Output Dir
        WSDL2JavaWidgetFactory.createOutputDirectoryLabel(mainComposite);

        outputDirCombo = WSDL2JavaWidgetFactory.createOutputDirectoryCombo(mainComposite, model);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        outputDirCombo.setLayoutData(gridData);

        // Package Name:
        WSDL2JavaWidgetFactory.createPackageNameLabel(mainComposite);

        packageNameText = WSDL2JavaWidgetFactory.createPackageNameText(mainComposite, model);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        packageNameText.setLayoutData(gridData);

        packageNameText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                String packageName = packageNameText.getText();
                status = JDTUtils.validatePackageName(model.getProjectName(), packageName);
                statusListener.handleEvent(null);
            }
        });

        namespaceMappingButton = WSDL2JavaWidgetFactory
                .createNamespacePackageMappingButton(mainComposite);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 3;
        namespaceMappingButton.setLayoutData(gridData);
        namespaceMappingButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                expandNamespaceComposite(namespaceMappingButton.getSelection());
            }
        });

        namespaceCompositeHolder = new Composite(mainComposite, SWT.NONE);
        gridLayout = new GridLayout(1, true);
        namespaceCompositeHolder.setLayout(gridLayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.horizontalSpan = 3;
        namespaceCompositeHolder.setLayoutData(gridData);

        // Service Name:
        WSDL2JavaWidgetFactory.createServiceNameLabel(mainComposite);

        serviceNameCombo = WSDL2JavaWidgetFactory.createServiceNameCombo(mainComposite, model);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        serviceNameCombo.setLayoutData(gridData);

        Label bindingFilesLabel = WSDL2JavaWidgetFactory.createBindingFilesLabel(mainComposite);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.verticalSpan = 3;
        bindingFilesLabel.setLayoutData(gridData);

        bindingFilesList = WSDL2JavaWidgetFactory.createBindingFilesList(mainComposite);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.verticalSpan = 3;
        bindingFilesList.setLayoutData(gridData);

        Button addBindingFileButton = WSDL2JavaWidgetFactory.createAddBindingFileButton(mainComposite, model,
                bindingFilesList);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        addBindingFileButton.setLayoutData(gridData);

        Button removeBindingFileButton = WSDL2JavaWidgetFactory.createRemoveBindingFileButton(mainComposite,
                model, bindingFilesList);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        removeBindingFileButton.setLayoutData(gridData);

        WSDL2JavaWidgetFactory.createPaddingLabel(mainComposite);

        /*
        WSDL2JavaWidgetFactory.createXMLCatalogLabel(mainComposite);

        Text xmlCatalogText = WSDL2JavaWidgetFactory.createXMLCatalogText(mainComposite, model);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        xmlCatalogText.setLayoutData(gridData);

        WSDL2JavaWidgetFactory.createXMLCatalogBrowseButton(mainComposite);
         */
        return this;
    }

    private void expandNamespaceComposite(boolean expand) {
        if (expand) {
            if (namespaceComposite == null) {
                GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
                gridData.horizontalSpan = 3;
                gridData.verticalSpan = 7;
                namespaceCompositeHolder.setLayoutData(gridData);
                namespaceCompositeHolder.pack();

                namespaceComposite = getNamespaceMappingComposite(namespaceCompositeHolder);
                if (namespaceCompositeHeight == -1) {
                    Point groupSize = namespaceComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
                    namespaceCompositeHeight = groupSize.y;
                }
                Shell shell = parent.getShell();
                Point shellSize = shell.getSize();
                shell.setSize(shellSize.x, shellSize.y + namespaceCompositeHeight);

                namespaceCompositeHolder.layout();
            }
        } else {
            if (namespaceComposite != null) {
                GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
                gridData.horizontalSpan = 3;
                namespaceCompositeHolder.setLayoutData(gridData);
                namespaceCompositeHolder.pack();
                namespaceComposite.dispose();
                namespaceComposite = null;
                namespaceCompositeHolder.layout();
                Shell shell = parent.getShell();
                Point shellSize = shell.getSize();
                shell.setSize(shellSize.x, shellSize.y - namespaceCompositeHeight);
            }
        }
    }
    
    private Composite getNamespaceMappingComposite(Composite parent) {
        namespaceComposite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, true);
        namespaceComposite.setLayout(gridLayout);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        namespaceComposite.setLayoutData(gridData);

        Table packageNameTable = createNamespaceMappingTable(namespaceComposite);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 3;
        gridData.verticalSpan = 6;
        packageNameTable.setLayoutData(gridData);

        return namespaceComposite;
    }

    private Table createNamespaceMappingTable(Composite parent) {
        packageNameTableViewer = new TableViewer(parent, SWT.CHECK | SWT.MULTI | SWT.BORDER
                | SWT.FULL_SELECTION);

        Table packageNameTable = packageNameTableViewer.getTable();
        packageNameTable.setLinesVisible(true);
        packageNameTable.setHeaderVisible(true);

        TableLayout tableLayout = new TableLayout();
        packageNameTable.setLayout(tableLayout);

        WSDL2JavaWidgetFactory.createWSDLNamespaceViewerColumn(packageNameTableViewer);

        ColumnWeightData columnWeightData = new ColumnWeightData(100, 100, true);
        tableLayout.addColumnData(columnWeightData);

        TableViewerColumn packageNameViewerColumn = WSDL2JavaWidgetFactory.createPackageNameColumn(
                packageNameTableViewer, model);
        
        TextCellEditor textCellEditor = new TextCellEditor(packageNameTableViewer.getTable());
        textCellEditor.addListener( new ICellEditorListener() {
		
			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
				statusListener.handleEvent(null);				
			}
		
			public void cancelEditor() {
			}
		
			public void applyEditorValue() {
			}
		});
        
        textCellEditor.setValidator(new ICellEditorValidator() {
		
			public String isValid(Object packageName) {
	            status = JDTUtils.validatePackageName(model.getProjectName(), packageName.toString());
	            if (status.getSeverity() == IStatus.OK) {
	                return null;
	            }
	            return status.getMessage();
			}
		});

        packageNameViewerColumn.setEditingSupport(new PackageNameEditingSupport(packageNameTableViewer,
        		textCellEditor, model));

        columnWeightData = new ColumnWeightData(100, 100, true);
        tableLayout.addColumnData(columnWeightData);

        packageNameTableViewer.setContentProvider(new PackageNameTableContentProvider());
        packageNameTableViewer.setInput(model.getWsdlDefinition());

        TableItem[] tableItems = packageNameTable.getItems();
        for (int i = 0; i < tableItems.length; i++) {
            TableItem tableItem = tableItems[i];
            if (model.getIncludedNamespaces().containsKey(tableItem.getText(0))) {
                tableItem.setChecked(true);
            }
        }
        return packageNameTable;
    }

    @Override
    public IStatus getStatus() {
        return status;
    }
}

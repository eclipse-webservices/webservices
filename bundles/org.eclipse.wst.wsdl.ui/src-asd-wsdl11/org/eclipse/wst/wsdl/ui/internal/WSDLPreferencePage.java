/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;

public class WSDLPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  private Composite interfacePreferencesComposite;
  private BooleanFieldEditor generateSeparateIntfFieldEditor;

  public WSDLPreferencePage()
  {
    super(FieldEditorPreferencePage.FLAT);
    setPreferenceStore(WSDLEditorPlugin.getInstance().getPreferenceStore());
  }

  public void init(IWorkbench workbench)
  {
  }

  protected void createFieldEditors()
  {
    Composite parent = getFieldEditorParent();
	GridLayout parentLayout = new GridLayout();
	parentLayout.marginWidth = 0;
	parent.setLayout(parentLayout);
	
//    WorkbenchHelp.setHelp(getControl(), some context id here); 

	Group group = new Group(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.numColumns = 2;
    group.setLayout(layout);
    group.setText(Messages._UI_PREF_PAGE_CREATING_FILES);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    group.setLayoutData(data);
    
    Composite composite = new Composite(group, SWT.NULL);
    GridData data1 = new GridData();
    data1.verticalAlignment = GridData.FILL;
    data1.horizontalAlignment = GridData.FILL;
    data1.grabExcessHorizontalSpace = true;
    composite.setLayoutData(data1);
	
    GridLayout compositeLayout = new GridLayout();
    compositeLayout.marginWidth = 5;   // Default value
    compositeLayout.numColumns = 2;
    composite.setLayout(compositeLayout);
    
    String prefixLabel = Messages._UI_PREF_PAGE_DEFAULT_PREFIX;
    StringFieldEditor prefix = new StringFieldEditor(WSDLEditorPlugin.DEFAULT_TARGET_NAMESPACE_PREFIX_PREFERENCE_ID, prefixLabel, composite);
    addField(prefix);
    
	String namespaceLabel = Messages._UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE;
    StringFieldEditor targetNamespace = new StringFieldEditor(namespaceLabel, namespaceLabel, composite);
    addField(targetNamespace);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(targetNamespace.getTextControl(composite), ASDEditorCSHelpIds.WSDL_PREF_DEFAULT_TNS);
	
    Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
    GridData gridData= new GridData(GridData.FILL_HORIZONTAL);
    gridData.horizontalSpan = 2;
    gridData.verticalIndent = 5;
    separator.setLayoutData(gridData);
    
    String generateSeparateIntfLabel = Messages._UI_PREF_GENERATE_SEPARATE_INTERFACE; 
    Composite oneColumnComposite = new Composite(composite, SWT.None);
    oneColumnComposite.setLayout(new GridLayout());
    GridData intfGridData = new GridData(GridData.FILL_BOTH);
    intfGridData.horizontalSpan = 2;
    intfGridData.horizontalIndent = 0;
    intfGridData.verticalIndent = 0;    
    oneColumnComposite.setLayoutData(intfGridData);
    generateSeparateIntfFieldEditor = new BooleanFieldEditor(WSDLEditorPlugin.GENERATE_SEPARATE_INTERFACE_PREFERENCE_ID, generateSeparateIntfLabel, oneColumnComposite);
    addField(generateSeparateIntfFieldEditor);
    
    interfacePreferencesComposite = new Composite(composite, SWT.None);
    layout = new GridLayout();
    layout.numColumns = 2;
    interfacePreferencesComposite.setLayout(layout);
    GridData compositeGridData = new GridData(GridData.FILL_BOTH);
    compositeGridData.horizontalSpan = 2;
    compositeGridData.horizontalIndent = 15;
    compositeGridData.verticalIndent = 0;    
    interfacePreferencesComposite.setLayoutData(compositeGridData);
    
    String interfacePrefixLabel = Messages._UI_PREF_PAGE_INTERFACE_DEFAULT_PREFIX;
    StringFieldEditor interfacePrefix = new StringFieldEditor(WSDLEditorPlugin.INTERFACE_PREFIX_PREFERENCE_ID, interfacePrefixLabel, interfacePreferencesComposite);
    addField(interfacePrefix);
    
    String interfaceNamespaceLabel = Messages._UI_PREF_PAGE_INTERFACE_DEFAULT_TARGET_NAMESPACE;
    StringFieldEditor interfaceNamespace = new StringFieldEditor(WSDLEditorPlugin.INTERFACE_DEFAULT_TARGET_NAMESPACE_PREFERENCE_ID, interfaceNamespaceLabel, interfacePreferencesComposite);
    addField(interfaceNamespace);
    
    String portTypeFilenameSuffixLabel = Messages._UI_PREF_PAGE_INTERFACE_FILE_SUFFIX;
    StringFieldEditor interfaceSuffix = new StringFieldEditor(WSDLEditorPlugin.INTERFACE_FILE_SUFFIX_PREFERENCE_ID, portTypeFilenameSuffixLabel, interfacePreferencesComposite);
    addField(interfaceSuffix);
    
	String generateLabel = Messages._UI_PREF_PAGE_AUTO_REGENERATE_BINDING;
	BooleanFieldEditor generateBindingOnSave = new BooleanFieldEditor(generateLabel, generateLabel, parent);
	addField(generateBindingOnSave);
    
	String showGenerateDialogLabel = Messages._UI_PREF_PAGE_PROMPT_REGEN_BINDING_ON_SAVE;
	BooleanFieldEditor showGenerateDialog = new BooleanFieldEditor(showGenerateDialogLabel, showGenerateDialogLabel, parent);
	addField(showGenerateDialog);
	
	String unusedImportLabel = Messages._UI_PREF_PAGE_ENABLE_AUTO_IMPORT_CLEANUP;
	BooleanFieldEditor removeUnusedImports = new BooleanFieldEditor(unusedImportLabel, unusedImportLabel, parent);
	addField(removeUnusedImports);
	
    String openImportDialogLabel = Messages._UI_PREF_PAGE_ENABLE_AUTO_OPEN_IMPORT_DIALOG;
    BooleanFieldEditor openImportDialog = new BooleanFieldEditor(openImportDialogLabel, openImportDialogLabel, parent);
    addField(openImportDialog);
	
    boolean enabled = WSDLEditorPlugin.getInstance().getPreferenceStore().getBoolean(WSDLEditorPlugin.GENERATE_SEPARATE_INTERFACE_PREFERENCE_ID);
    setChildrenEnabled(interfacePreferencesComposite, enabled);
	applyDialogFont(parent);
    
  }

  public void propertyChange(PropertyChangeEvent event) {
	  super.propertyChange(event);
	  boolean enabled = generateSeparateIntfFieldEditor.getBooleanValue();
	  setChildrenEnabled(interfacePreferencesComposite, enabled);
  }

  private void setChildrenEnabled(Composite composite, boolean enabled) {
	  Control[] children = composite.getChildren();
	  for (int i = 0; i < children.length; i++)
		  children[i].setEnabled(enabled);
  }

  protected void performDefaults() {
	  super.performDefaults();
	  boolean enabled = generateSeparateIntfFieldEditor.getBooleanValue();
	  setChildrenEnabled(interfacePreferencesComposite, enabled);
  }
}

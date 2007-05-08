/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;

public class WSDLPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

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
    group.setText(Messages._UI_PREF_PAGE_CREATING_FILES); //$NON-NLS-1$

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
	
	String namespaceLabel = Messages._UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE;  //$NON-NLS-1$
    StringFieldEditor targetNamespace = new StringFieldEditor(namespaceLabel, namespaceLabel, composite);
    GridLayout compositeLayout = (GridLayout)composite.getLayout();
    compositeLayout.marginWidth = 5;   // Default value
    compositeLayout.numColumns = 2;
    addField(targetNamespace);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(targetNamespace.getTextControl(composite), ASDEditorCSHelpIds.WSDL_PREF_DEFAULT_TNS);
	
	String generateLabel = Messages._UI_PREF_PAGE_AUTO_REGENERATE_BINDING; //$NON-NLS-1$
	BooleanFieldEditor generateBindingOnSave = new BooleanFieldEditor(generateLabel, generateLabel, parent);
	addField(generateBindingOnSave);
    
	String showGenerateDialogLabel = Messages._UI_PREF_PAGE_PROMPT_REGEN_BINDING_ON_SAVE; //$NON-NLS-1$
	BooleanFieldEditor showGenerateDialog = new BooleanFieldEditor(showGenerateDialogLabel, showGenerateDialogLabel, parent);
	addField(showGenerateDialog);

    /* Do we need this preference below?  If so, change WSDLEditorPlugin.java and use this preference in:
       HttpContentGenerator.java
       SoapContentGenerator.java
       SoapPortOptionsPage.java
    */
//    HttpPortOptionsPage.java    StringFieldEditor location = new StringFieldEditor("Defualt Location:", "Default Location:", editorGroup1); // Externalize
//    addField(location); 
	
	applyDialogFont(parent);
    
  }
}

/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
//import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

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
	parentLayout.verticalSpacing = 15;
	parent.setLayout(parentLayout);
//    WorkbenchHelp.setHelp(getControl(), some context id here); 

	Group editorGroup1 = new Group(parent, SWT.NONE);
	editorGroup1.setText(WSDLEditorPlugin.getWSDLString("_UI_EDITOR_NAME"));

	GridData grid = new GridData();
	grid.widthHint = 400;
	editorGroup1.setLayoutData(grid);
	
	GridLayout layout = new GridLayout();
	layout.numColumns = 2;
	editorGroup1.setLayout(layout);	

    StringFieldEditor targetNamespace = new StringFieldEditor(WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"), WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"), editorGroup1); // Externalize
    addField(targetNamespace);

    /* Do we need this preference below?  If so, change WSDLEditorPlugin.java and use this preference in:
       HttpContentGenerator.java
       SoapContentGenerator.java
       SoapPortOptionsPage.java
    */
//    HttpPortOptionsPage.java    StringFieldEditor location = new StringFieldEditor("Defualt Location:", "Default Location:", editorGroup1); // Externalize
//    addField(location);    
    
	GridLayout tempLayout = (GridLayout) editorGroup1.getLayout();
	tempLayout.marginHeight = 7;
	tempLayout.marginWidth = 7;
    
    targetNamespace.setFocus();
  }
}
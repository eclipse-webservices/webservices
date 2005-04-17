/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.jst.ws.internal.ui.plugin.WebServiceUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceDefaults;



public class ResourceManagmentPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  /*CONTEXT_ID PPRM0001 for the Resource Managment Preference Page*/
  private String INFOPOP_PPRM_PAGE = WebServiceUIPlugin.ID + ".PPRM0001";
  //
  private Button overWriteFiles;
  /*CONTEXT_ID PPRM0002 for the file overwrite check box on the Resource Management Preference Page*/
  private String INFOPOP_PPRM_CHECKBOX_OVERWRITE_FILES = WebServiceUIPlugin.ID + ".PPRM0002";
  //
  private Button createFolders;
  /*CONTEXT_ID PPRM0003 for the create folders check box on the Resource Management Preference Page*/
  private String INFOPOP_PPRM_CHECKBOX_CREATE_FOLDERS = WebServiceUIPlugin.ID + ".PPRM0003";
  //
  private Button checkoutFiles;
  /*CONTEXT_ID PPRM0004 for the checkout files check box on the Resource Management Preference Page*/
  private String INFOPOP_PPRM_CHECKBOX_CHECK_OUT = WebServiceUIPlugin.ID + ".PPRM0004";

 /**
   * Creates preference page controls on demand.
   *   @param parent  the parent for the preference page
   */
  protected Control createContents(Composite superparent)
  {
  	IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
  	
    Composite   parent = new Composite( superparent, SWT.NONE );	
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    parent.setLayout( layout );
    parent.setToolTipText(getMessage("%TOOLTIP_PPRM_PAGE"));
    helpSystem.setHelp(parent, INFOPOP_PPRM_PAGE);

    overWriteFiles = createCheckBox(parent, WebServiceUIPlugin.getMessage("%BUTTON_OVERWRITE_FILES" ));
    overWriteFiles.setToolTipText(getMessage("%TOOLTIP_PPRM_CHECKBOX_OVERWRITE_FILES"));
    helpSystem.setHelp(overWriteFiles, INFOPOP_PPRM_CHECKBOX_OVERWRITE_FILES);

    createFolders  = createCheckBox(parent, WebServiceUIPlugin.getMessage("%BUTTON_CREATE_FOLDERS" ));
    createFolders.setToolTipText(getMessage("%TOOLTIP_PPRM_CHECKBOX_CREATE_FOLDERS"));
    helpSystem.setHelp(createFolders, INFOPOP_PPRM_CHECKBOX_CREATE_FOLDERS);

    checkoutFiles = createCheckBox(parent, WebServiceUIPlugin.getMessage("%BUTTON_CHECKOUT_FILES" ));    
    checkoutFiles.setToolTipText(getMessage("%TOOLTIP_PPRM_CHECKBOX_CHECK_OUT"));
    helpSystem.setHelp(checkoutFiles, INFOPOP_PPRM_CHECKBOX_CHECK_OUT);

    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);    

    return parent;
  }
 
  private String getMessage(String key) 
  {
  		return WebServiceUIPlugin.getMessage(key);
  }

  private Button createCheckBox( Composite parent, String text )
  {
    Button button = new Button( parent, SWT.CHECK );
    button.setText( text );
    return button;
  }

  /**
   * Does anything necessary because the default button has been pressed.
   */
  protected void performDefaults()
  {
    super.performDefaults();
    initializeDefaults();
  }

  /**
   * Do anything necessary because the OK button has been pressed.
   *  @return whether it is okay to close the preference page
   */
  public boolean performOk()
  {
    storeValues();
    return true;
  }

  protected void performApply()
  {
    performOk();
  }

  /**
   * @see IWorkbenchPreferencePage
   */
  public void init(IWorkbench workbench)  { }

  /**
   * Initializes states of the controls using default values
   * in the preference store.
   */
  private void initializeDefaults()
  {
    overWriteFiles.setSelection( ResourceDefaults.getOverwriteFilesDefault());
    createFolders.setSelection( ResourceDefaults.getCreateFoldersDefault());
    checkoutFiles.setSelection( ResourceDefaults.getCheckoutFilesDefault());
  }

  /**
   * Initializes states of the controls from the preference store.
   */
  private void initializeValues()
  {
    // get the persistent context from the plugin
    ResourceContext context = WebServicePlugin.getInstance().getResourceContext();
    overWriteFiles.setSelection( context.isOverwriteFilesEnabled());
    createFolders.setSelection( context.isCreateFoldersEnabled());
    checkoutFiles.setSelection( context.isCheckoutFilesEnabled());    
   }

  /**
   * Stores the values of the controls back to the preference store.
   */
  private void storeValues()
  {
    // get the persistent context from the plugin
    ResourceContext context = WebServicePlugin.getInstance().getResourceContext();
    context.setOverwriteFilesEnabled( overWriteFiles.getSelection() );
    context.setCreateFoldersEnabled( createFolders.getSelection() );
    context.setCheckoutFilesEnabled( checkoutFiles.getSelection() );    
  }
}


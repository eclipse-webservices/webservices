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

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.internal.ui.plugin.WebServiceUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.command.internal.env.ui.EnvironmentUIMessages;

;

public class ActionDialogsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, Listener
{
  /*CONTEXT_ID PPAD0001 for the Action Dialogs Preference Page*/
  private String INFOPOP_PPAD_PAGE = WebServiceUIPlugin.ID + ".PPAD0001";
  //
  private Button showAll;
  /*CONTEXT_ID PPAD0002 for the  show all check box on the Action Dialogs Preference Page*/
  private String INFOPOP_PPAD_BUTTON_SHOW_ALL = WebServiceUIPlugin.ID + ".PPAD0002";
  //
  private Button hideAll;
  /*CONTEXT_ID PPAD0003 for the  hide all check box on the Action Dialogs Preference Page*/
  private String INFOPOP_PPAD_BUTTON_HIDE_ALL = WebServiceUIPlugin.ID + ".PPAD0003";

  private Hashtable checkBoxes_;

 /**
   * Creates preference page controls on demand.
   *   @param parent  the parent for the preference page
   */
  protected Control createContents(Composite superparent)
  {
    checkBoxes_ = new Hashtable();
    addOptionalDialogsCheckBoxes (superparent);
	new Label(superparent, SWT.HORIZONTAL);
	
	IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
	
    Composite   parent = new Composite( superparent, SWT.NONE );	
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    parent.setLayout( layout );
    parent.setToolTipText(EnvironmentUIMessages.TOOLTIP_PPAD_PAGE);
    helpSystem.setHelp(parent,INFOPOP_PPAD_PAGE);
	
    showAll = new Button(parent, SWT.NONE);
    showAll.setText(EnvironmentUIMessages.BUTTON_SHOW_ALL_DIALOGS);
    showAll.addListener(SWT.Selection, this);
    showAll.setToolTipText(EnvironmentUIMessages.TOOLTIP_PPAD_BUTTON_SHOW_ALL);
    helpSystem.setHelp(showAll,INFOPOP_PPAD_BUTTON_SHOW_ALL);
   
    hideAll = new Button(parent, SWT.NONE);
    hideAll.setText(EnvironmentUIMessages.BUTTON_HIDE_ALL_DIALOGS);
    hideAll.addListener ( SWT.Selection, this);
    hideAll.setToolTipText(EnvironmentUIMessages.TOOLTIP_PPAD_BUTTON_HIDE_ALL);
    helpSystem.setHelp(hideAll,INFOPOP_PPAD_BUTTON_HIDE_ALL);

    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);
    return parent;
  }

  private String getMessage(String key) 
  {
  		return WebServiceUIPlugin.getMessage(key);
  }

 public void handleEvent (Event event) 
 {
    if (showAll == event.widget)
		handleShowAllEvent();
    
    else if ( hideAll == event.widget)
    	handleHideAllEvent();
 }

 private void handleShowAllEvent ()
 {
    Enumeration e = checkBoxes_.elements();
 	for (int i=0; e.hasMoreElements(); i++)
    	{
    		Button dialog = (Button) e.nextElement();
    		dialog.setSelection( true );
    	}                      
 }

 private void handleHideAllEvent ()
 {
 	Enumeration e = checkBoxes_.elements();
 	for (int i=0; e.hasMoreElements(); i++)
    	{
    		Button dialog = (Button) e.nextElement();
    		dialog.setSelection( false );
    	}
 }
 
 private void addOptionalDialogsCheckBoxes ( Composite parent)
  {
    PersistentActionDialogsContext context = WebServiceUIPlugin.getInstance().getActionDialogsContext();
    ActionDialogPreferenceType[] dialogs = context.getDialogs();
    for (int i = 0; i < dialogs.length; i++) {
    	Button checkBox = createCheckBox(parent , dialogs[i].getName());
    	checkBox.setToolTipText(getMessage(dialogs[i].getTooltip()));
    	PlatformUI.getWorkbench().getHelpSystem().setHelp(checkBox,dialogs[i].getInfopop());
    	checkBoxes_.put(dialogs[i].getId(), checkBox);
    }
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
   * The proxy information is stored in the preference store.
   */
  public IPreferenceStore getPreferenceStore()
  {
    return WebServiceUIPlugin.getInstance().getPreferenceStore();
  }

  /**
   * Initializes states of the controls using default values
   * in the preference store.
   */
  private void initializeDefaults()
  {
    Enumeration e = checkBoxes_.elements();
    for (int i=0; e.hasMoreElements(); i++)
    {
      Button dialog = (Button) e.nextElement();
      dialog.setSelection(true);
    }
  }

  /**
   * Initializes states of the controls from the preferences.
   */
  private void initializeValues()
  {
    PersistentActionDialogsContext context = WebServiceUIPlugin.getInstance().getActionDialogsContext();
    Enumeration e = checkBoxes_.keys();
    for (int i=0; e.hasMoreElements(); i++)
    {
      String id = (String) e.nextElement();
      Button button = (Button) checkBoxes_.get(id);
      button.setSelection(context.isActionDialogEnabled(id));
    }
  }

  /**
   * Stores the values of the controls back to the preference store.
   */
  private void storeValues()
  {
    PersistentActionDialogsContext context = WebServiceUIPlugin.getInstance().getActionDialogsContext();

    Enumeration e = checkBoxes_.keys();
    for (int i=0; e.hasMoreElements(); i++)
    {
      String id = (String) e.nextElement();
      context.setActionDialogEnabled(id, ((Button)checkBoxes_.get(id)).getSelection());
    }
  }
}


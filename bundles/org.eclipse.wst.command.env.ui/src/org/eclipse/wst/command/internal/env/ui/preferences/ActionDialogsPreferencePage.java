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
package org.eclipse.wst.command.internal.env.ui.preferences;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.preference.PreferencePage;
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
import org.eclipse.wst.command.internal.env.context.PersistentActionDialogsContext;
import org.eclipse.wst.command.internal.env.preferences.ActionDialogPreferenceType;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;


/**
 * This class can be used to create a popup actions preference page for a 
 * particular category.  For example:
 *
 * <pre>
 *  &lt;extension
 *        point="org.eclipse.ui.preferencePages"&gt;
 *     &lt;page
 *           name="%PREFERENCE_CATEGORY_DIALOGS"
 *           category="org.eclipse.jst.ws.ui.preferences.name"
 *           class="org.eclipse.wst.command.internal.env.preferences.ActionDialogsPreferencePage"
 *           id="org.eclipse.jst.wss.popup.category"&gt;
 *     &lt;/page&gt;
 * * </pre>
 * This entry specifies that all popup actions that are associated with the
 * org.eclipse.jst.wss.popup.category will be displayed on this
 * preference page.
 */
public class ActionDialogsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, Listener, IExecutableExtension
{
  /*CONTEXT_ID PPAD0001 for the Action Dialogs Preference Page*/
  private String INFOPOP_PPAD_PAGE = "org.eclipse.wst.command.env.ui.PPAD0001";
  //
  private Button showAll;
  /*CONTEXT_ID PPAD0002 for the  show all check box on the Action Dialogs Preference Page*/
  private String INFOPOP_PPAD_BUTTON_SHOW_ALL = "org.eclipse.wst.command.env.ui.PPAD0002";
  //
  private Button hideAll;
  /*CONTEXT_ID PPAD0003 for the  hide all check box on the Action Dialogs Preference Page*/
  private String INFOPOP_PPAD_BUTTON_HIDE_ALL = "org.eclipse.wst.command.env.ui.PPAD0003";

  private Hashtable checkBoxes_;
  private String    categoryId_;
  
  public void setInitializationData( IConfigurationElement config,
                                     String                propertyName,
                                     Object                data )
    throws CoreException
  {
    categoryId_   = config.getAttribute( "id" );
  }

  /**
   * Creates preference page controls on demand.
   *   @param parent  the parent for the preference page
   */
  protected Control createContents(Composite superparent)
  {
    MessageUtils msgUtils = new MessageUtils( "org.eclipse.wst.command.env.ui.environmentui", this );
    
    checkBoxes_ = new Hashtable();
    addOptionalDialogsCheckBoxes (superparent);
	new Label(superparent, SWT.HORIZONTAL);
	
    Composite   parent = new Composite( superparent, SWT.NONE );	
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    parent.setLayout( layout );
    parent.setToolTipText(msgUtils.getMessage("TOOLTIP_PPAD_PAGE"));
    PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, INFOPOP_PPAD_PAGE );
	
    showAll = new Button(parent, SWT.NONE);
    showAll.setText(msgUtils.getMessage("BUTTON_SHOW_ALL_DIALOGS"));
    showAll.addListener(SWT.Selection, this);
    showAll.setToolTipText(msgUtils.getMessage("TOOLTIP_PPAD_BUTTON_SHOW_ALL"));
    PlatformUI.getWorkbench().getHelpSystem().setHelp(showAll, INFOPOP_PPAD_BUTTON_SHOW_ALL );
   
    hideAll = new Button(parent, SWT.NONE);
    hideAll.setText(msgUtils.getMessage("BUTTON_HIDE_ALL_DIALOGS"));
    hideAll.addListener ( SWT.Selection, this);
    hideAll.setToolTipText(msgUtils.getMessage("TOOLTIP_PPAD_BUTTON_HIDE_ALL"));
    PlatformUI.getWorkbench().getHelpSystem().setHelp(hideAll, INFOPOP_PPAD_BUTTON_HIDE_ALL );

    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);
    return parent;
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
    		dialog.setSelection( false );
    	}                      
 }

 private void handleHideAllEvent ()
 {
 	Enumeration e = checkBoxes_.elements();
 	for (int i=0; e.hasMoreElements(); i++)
    	{
    		Button dialog = (Button) e.nextElement();
    		dialog.setSelection( true );
    	}
 }
 
 private void addOptionalDialogsCheckBoxes ( Composite parent)
  {
    PersistentActionDialogsContext context = PersistentActionDialogsContext.getInstance();
    ActionDialogPreferenceType[] dialogs = context.getDialogs();
    
    for (int i = 0; i < dialogs.length; i++) 
    {
      ActionDialogPreferenceType dialog   = dialogs[i];
      String                     category = dialog.getCategory();
      
      if( dialog.getShowCheckbox() &&  category != null && category.equals( categoryId_) )
      {
    	Button checkBox = createCheckBox(parent, dialog.getName());
    	checkBox.setToolTipText( dialog.getTooltip() );
    	PlatformUI.getWorkbench().getHelpSystem().setHelp(checkBox, dialog.getInfopop() );
    	checkBoxes_.put(dialog.getId(), checkBox);
      }
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
    PersistentActionDialogsContext context = PersistentActionDialogsContext.getInstance();
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
    PersistentActionDialogsContext context = PersistentActionDialogsContext.getInstance();

    Enumeration e = checkBoxes_.keys();
    for (int i=0; e.hasMoreElements(); i++)
    {
      String id = (String) e.nextElement();
      context.setActionDialogEnabled(id, ((Button)checkBoxes_.get(id)).getSelection());
    }
  }
}


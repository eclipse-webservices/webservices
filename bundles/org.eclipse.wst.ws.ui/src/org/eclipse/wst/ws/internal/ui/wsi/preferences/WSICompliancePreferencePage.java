/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060310   131352 pmoogk@ca.ibm.com - Peter Moogk
 * 20060728   151632 kathy@ca.ibm.com - Kathy Chan
 * 20071025          ericdp@ca.ibm.com - Eric Peters
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui.wsi.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSDLValidationContext;
import org.eclipse.wst.ws.internal.ui.WstWSUIPluginMessages;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;



public class WSICompliancePreferencePage extends PreferencePage implements IWorkbenchPreferencePage, SelectionListener, Listener

{
	  
  /*CONTEXT_ID PWSI0001 for the WS-I Preference Page*/
  private String INFOPOP_PWSI_PAGE = WSUIPlugin.ID + ".PWSI0000";
  
  private Text wsdlValidationLabel_;
  /*CONTEXT_ID PWSI0009 for the No Wizard WSDL validation button on the Profile Compliance and Validation page*/
  private Button validateNoWsdlButton_;
  /*CONTEXT_ID PWSI00010 for the Wizard WSDL validation for remote files only button on the Profile Compliance and Validation page*/
  private Button validateRemoteWsdlButton_;
  /*CONTEXT_ID PWSI00011 for the Wizard WSDL validation for all files button on the Profile Compliance and Validation page*/
  private Button validateAllWsdlButton_;
  

  /*
   * CONTEXT_ID PWRS0009 for the no wsdl validation radio button of the profile validation preference page
   */
  private String INFOPOP_PWSI_RADIO_WSDLVAL_NONE = WSUIPlugin.ID + ".PWSI0009";
  /*
   * CONTEXT_ID PWRS0010 for the wsdl validation on remote document radio button of the profile validation preference page
   */
  private String INFOPOP_PWSI_RADIO_WSDLVAL_REMOTE = WSUIPlugin.ID + ".PWSI00010";
  /*
   * CONTEXT_ID PWRS0011 for the wsdl validation on all document radio button of the profile validation preference page
   */
  private String INFOPOP_PWSI_RADIO_WSDLVAL_ALL = WSUIPlugin.ID + ".PWSI0011";
  /*
   * CONTEXT_ID PWRS0012 for the wsdl validation label of the profile validation preference page
   */
  private String INFOPOP_PWSI_WSDLVAL_LABEL = WSUIPlugin.ID + ".PWSI0012";

 /**
   * Creates preference page controls on demand.
   *   @param parentComposite  the parent for the preference page
   */
  protected Control createContents(Composite superparent)
  {
   
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
	
    Composite   parent = new Composite( superparent, SWT.NONE );	
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    parent.setLayout( layout );
    parent.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
    parent.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_PAGE);
    helpSystem.setHelp(parent,INFOPOP_PWSI_PAGE);

    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    gl.marginHeight = 0;
    gl.marginWidth = 0;

    
    wsdlValidationLabel_ = new Text(parent, SWT.READ_ONLY);
    wsdlValidationLabel_.setText(WstWSUIPluginMessages.LABEL_WSDLVAL);
    wsdlValidationLabel_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_WSDLVAL_LABEL);
    helpSystem.setHelp(wsdlValidationLabel_, INFOPOP_PWSI_WSDLVAL_LABEL);
    
    validateNoWsdlButton_ = new Button(parent, SWT.RADIO);
    validateNoWsdlButton_.setText(WstWSUIPluginMessages.LABEL_WSDLVAL_NONE);
    validateNoWsdlButton_.addListener(SWT.Selection, this);
    validateNoWsdlButton_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_RADIO_WSDLVAL_NONE);
    helpSystem.setHelp(validateNoWsdlButton_, INFOPOP_PWSI_RADIO_WSDLVAL_NONE);
    
    validateRemoteWsdlButton_ = new Button(parent, SWT.RADIO);
    validateRemoteWsdlButton_.setText(WstWSUIPluginMessages.LABEL_WSDLVAL_REMOTE);
    validateRemoteWsdlButton_.addListener(SWT.Selection, this);
    validateRemoteWsdlButton_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_RADIO_WSDLVAL_REMOTE);
    helpSystem.setHelp(validateRemoteWsdlButton_, INFOPOP_PWSI_RADIO_WSDLVAL_REMOTE);
    
    validateAllWsdlButton_ = new Button(parent, SWT.RADIO);
    validateAllWsdlButton_.setText(WstWSUIPluginMessages.LABEL_WSDLVAL_ALL);
    validateAllWsdlButton_.addListener(SWT.Selection, this);
    validateAllWsdlButton_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_RADIO_WSDLVAL_ALL);
    helpSystem.setHelp(validateAllWsdlButton_, INFOPOP_PWSI_RADIO_WSDLVAL_ALL);
    
    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);    
    return parent;
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
    return true;
  }

  protected void performApply()
  {
    performOk();
  }

  /**
   * @see IWorkbenchPreferencePage
   */
  public void init(IWorkbench workbench)   { }

  /**
   * Initializes states of the controls using default values
   * in the preference store.
   */
  private void initializeDefaults()
  {

    PersistentWSDLValidationContext wsdlValidationContext = WSPlugin.getInstance().getWSDLValidationContext();
    String validationSelection = wsdlValidationContext.getDefault();
    wsdlValidationContext.updateWSDLValidation(validationSelection);
    validateNoWsdlButton_.setSelection(false);
    validateRemoteWsdlButton_.setSelection(false);
    validateAllWsdlButton_.setSelection(false);
    processWSDLValidationSelection(validationSelection);
    
    WSPlugin.getInstance().getWaitForWSDLValidationContext().setWaitForWSDLValidation(WSPlugin.getInstance().getWaitForWSDLValidationContext().getDefault());
    
  }

  /**
   * Initializes states of the controls from the preference store.
   */
  private void initializeValues()
  {
    String validationSelection = WSPlugin.getInstance().getWSDLValidationContext().getPersistentWSDLValidation();
    processWSDLValidationSelection(validationSelection);
    
  }

  private void processWSDLValidationSelection(String validationSelection) {

		if (PersistentWSDLValidationContext.VALIDATE_NO_WSDL.equals(validationSelection)) {
			validateNoWsdlButton_.setSelection(true);
		} else if (PersistentWSDLValidationContext.VALIDATE_REMOTE_WSDL.equals(validationSelection)) {
			validateRemoteWsdlButton_.setSelection(true);
		} else if (PersistentWSDLValidationContext.VALIDATE_ALL_WSDL.equals(validationSelection)) {
			validateAllWsdlButton_.setSelection(true);
		}

	}

  public void widgetSelected(SelectionEvent e)
  {
  }

  public void widgetDefaultSelected(SelectionEvent e) {
	
  }
  
  /**
   * Called when an event occurs on the page. Handles the event and revalidates the page.
   * 
   * @param event
   *          The event that occured.
   */
  public void handleEvent(Event event) {
	  
	  String wsdlValdationSelection = null;
	  if (validateNoWsdlButton_ == event.widget) {
		  wsdlValdationSelection = PersistentWSDLValidationContext.VALIDATE_NO_WSDL;
	  }
	  else if (validateRemoteWsdlButton_ == event.widget) {
		  wsdlValdationSelection = PersistentWSDLValidationContext.VALIDATE_REMOTE_WSDL;
	  }
	  else if (validateAllWsdlButton_ == event.widget) {
		  wsdlValdationSelection = PersistentWSDLValidationContext.VALIDATE_ALL_WSDL;
	  }
	  WSPlugin.getInstance().getWSDLValidationContext().updateWSDLValidation(wsdlValdationSelection);
	  
  }


}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui.wsi.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSDLValidationContext;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;
import org.eclipse.wst.ws.internal.ui.WstWSUIPluginMessages;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;



public class WSICompliancePreferencePage extends PreferencePage implements IWorkbenchPreferencePage, SelectionListener, Listener

{
	  
  /*CONTEXT_ID PWSI0001 for the WS-I Preference Page*/
  private String INFOPOP_PWSI_PAGE = WSUIPlugin.ID + ".PWSI0000";
  //

  private Label wsi_ssbp_Label_;
  private Combo wsi_ssbp_Types_;
  
  /*CONTEXT_ID PWSI0004 for the WS-I SSBP type combo box on the Profile Compliance and Validation page*/
  private String INFOPOP_PWSI_SSBP_COMBO_TYPE = WSUIPlugin.ID + ".PWSI0004";
  /*CONTEXT_ID PWSI0008 for the WS-I AP type combo box on the Profile Compliance and Validation page*/
  private String INFOPOP_PWSI_AP_COMBO_TYPE = WSUIPlugin.ID + ".PWSI0008";
      
  private Label wsi_ap_Label_;
  private Combo wsi_ap_Types_;
  
  private int savedSSBPSetting_ = -1;
  
  private Group validationSelectionGroup_;
  
  private Label wsdlValidationLabel_;
  /*CONTEXT_ID PWSI0009 for the No Wizard WSDL validation button on the Profile Compliance and Validation page*/
  private Button validateNoWsdlButton_;
  /*CONTEXT_ID PWSI00010 for the Wizard WSDL validation for remote files only button on the Profile Compliance and Validation page*/
  private Button validateRemoteWsdlButton_;
  /*CONTEXT_ID PWSI00011 for the Wizard WSDL validation for all files button on the Profile Compliance and Validation page*/
  private Button validateAllWsdlButton_;
  private Label waitForWsdlValidationLabel_;
  /*CONTEXT_ID PWSI00012 for the Wait for pending WSDL validation to complete checkbox on the Profile Compliance and Validation page*/
  private Button waitForWSDLValidationCheckbox_;
  

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
  /*
   * CONTEXT_ID PWRS0013 for the wait for wsdl validation checkbox of the profile validation preference page
   */
  private String INFOPOP_PWSI_BUTTON_WAIT_FOR_WSDLVAL = WSUIPlugin.ID + ".PWSI0013";

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
    parent.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    parent.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_PAGE);
    helpSystem.setHelp(parent,INFOPOP_PWSI_PAGE);

    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    gl.marginHeight = 0;
    gl.marginWidth = 0;

    Composite wsi_Composite = new Composite (parent, SWT.NONE);
    wsi_Composite.setLayout(gl);
    wsi_Composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    
    wsi_ap_Label_ = new Label(wsi_Composite, SWT.NONE);
    wsi_ap_Label_.setText(WstWSUIPluginMessages.LABEL_WSI_AP);
    wsi_ap_Label_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_AP_LABEL);
    wsi_ap_Types_ = new Combo(wsi_Composite, SWT.DROP_DOWN | SWT.READ_ONLY);
    wsi_ap_Types_.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    wsi_ap_Types_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_AP_COMBO);
    helpSystem.setHelp(wsi_ap_Types_,INFOPOP_PWSI_AP_COMBO_TYPE);
    
    wsi_ap_Types_.add(WstWSUIPluginMessages.STOP_NON_WSI);
    wsi_ap_Types_.add(WstWSUIPluginMessages.WARN_NON_WSI);
    wsi_ap_Types_.add(WstWSUIPluginMessages.IGNORE_NON_WSI);
    
    wsi_ap_Types_.addSelectionListener(this);
    
    wsi_ssbp_Label_ = new Label(wsi_Composite, SWT.NONE);
    wsi_ssbp_Label_.setText(WstWSUIPluginMessages.LABEL_WSI_SSBP);
    wsi_ssbp_Label_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_SSBP_LABEL);
    wsi_ssbp_Types_ = new Combo(wsi_Composite, SWT.DROP_DOWN | SWT.READ_ONLY);
    wsi_ssbp_Types_.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    wsi_ssbp_Types_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_SSBP_COMBO);
    helpSystem.setHelp(wsi_ssbp_Types_,INFOPOP_PWSI_SSBP_COMBO_TYPE);
    
    wsi_ssbp_Types_.add(WstWSUIPluginMessages.STOP_NON_WSI);
    wsi_ssbp_Types_.add(WstWSUIPluginMessages.WARN_NON_WSI);
    wsi_ssbp_Types_.add(WstWSUIPluginMessages.IGNORE_NON_WSI);
  
    // WSDL validation preferences
    validationSelectionGroup_ = new Group(wsi_Composite, SWT.NONE);
    gl = new GridLayout();
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    validationSelectionGroup_.setLayout(gl);
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    validationSelectionGroup_.setLayoutData(gd);
    
    wsdlValidationLabel_ = new Label(validationSelectionGroup_, SWT.NONE);
    wsdlValidationLabel_.setText(WstWSUIPluginMessages.LABEL_WSDLVAL);
    wsdlValidationLabel_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_WSDLVAL_LABEL);
    helpSystem.setHelp(wsdlValidationLabel_, INFOPOP_PWSI_WSDLVAL_LABEL);
    
    validateNoWsdlButton_ = new Button(validationSelectionGroup_, SWT.RADIO);
    validateNoWsdlButton_.setText(WstWSUIPluginMessages.LABEL_WSDLVAL_NONE);
    validateNoWsdlButton_.addListener(SWT.Selection, this);
    validateNoWsdlButton_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_RADIO_WSDLVAL_NONE);
    helpSystem.setHelp(validateNoWsdlButton_, INFOPOP_PWSI_RADIO_WSDLVAL_NONE);
    
    validateRemoteWsdlButton_ = new Button(validationSelectionGroup_, SWT.RADIO);
    validateRemoteWsdlButton_.setText(WstWSUIPluginMessages.LABEL_WSDLVAL_REMOTE);
    validateRemoteWsdlButton_.addListener(SWT.Selection, this);
    validateRemoteWsdlButton_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_RADIO_WSDLVAL_REMOTE);
    helpSystem.setHelp(validateRemoteWsdlButton_, INFOPOP_PWSI_RADIO_WSDLVAL_REMOTE);
    
    validateAllWsdlButton_ = new Button(validationSelectionGroup_, SWT.RADIO);
    validateAllWsdlButton_.setText(WstWSUIPluginMessages.LABEL_WSDLVAL_ALL);
    validateAllWsdlButton_.addListener(SWT.Selection, this);
    validateAllWsdlButton_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_RADIO_WSDLVAL_ALL);
    helpSystem.setHelp(validateAllWsdlButton_, INFOPOP_PWSI_RADIO_WSDLVAL_ALL);
    
    new Label(validationSelectionGroup_, SWT.NONE);;
    
    waitForWsdlValidationLabel_ = new Label(validationSelectionGroup_, SWT.NONE);
    waitForWsdlValidationLabel_.setText(WstWSUIPluginMessages.LABEL_WAIT_FOR_WSDLVAL);
    waitForWsdlValidationLabel_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_LABEL_WAIT_FOR_WSDLVAL);
    
    waitForWSDLValidationCheckbox_ = new Button(validationSelectionGroup_, SWT.CHECK);
    waitForWSDLValidationCheckbox_.setText(WstWSUIPluginMessages.BUTTON_WAIT_FOR_WSDLVAL);            
    waitForWSDLValidationCheckbox_.addListener(SWT.Selection, this);
    waitForWSDLValidationCheckbox_.setToolTipText(WstWSUIPluginMessages.TOOLTIP_PWSI_BUTTON_WAIT_FOR_WSDLVAL);
    helpSystem.setHelp(waitForWSDLValidationCheckbox_, INFOPOP_PWSI_BUTTON_WAIT_FOR_WSDLVAL);
    
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
  public void init(IWorkbench workbench)   { }

  /**
   * Initializes states of the controls using default values
   * in the preference store.
   */
  private void initializeDefaults()
  {
    // force WSI compliance by default
    
    wsi_ssbp_Types_.select(wsi_ssbp_Types_.indexOf(WstWSUIPluginMessages.IGNORE_NON_WSI));
    int apSelection = wsi_ap_Types_.indexOf(WstWSUIPluginMessages.IGNORE_NON_WSI);
    wsi_ap_Types_.select(apSelection);
    savedSSBPSetting_ = -1;  // do not restore saved SSBP setting
    processAPSelection(apSelection);

    PersistentWSDLValidationContext wsdlValidationContext = WSPlugin.getInstance().getWSDLValidationContext();
    String validationSelection = wsdlValidationContext.getDefault();
    wsdlValidationContext.updateWSDLValidation(validationSelection);
    validateNoWsdlButton_.setSelection(false);
    validateRemoteWsdlButton_.setSelection(false);
    validateAllWsdlButton_.setSelection(false);
    processWSDLValidationSelection(validationSelection);
    
    waitForWSDLValidationCheckbox_.setSelection(WSPlugin.getInstance().getWaitForWSDLValidationContext().getDefault());
    WSPlugin.getInstance().getWaitForWSDLValidationContext().setWaitForWSDLValidation(WSPlugin.getInstance().getWaitForWSDLValidationContext().getDefault());
    
  }

  /**
   * Initializes states of the controls from the preference store.
   */
  private void initializeValues()
  {
    
	String WSIText = getWSISelection(WSPlugin.getInstance().getWSISSBPContext());
    wsi_ssbp_Types_.select(wsi_ssbp_Types_.indexOf(WSIText));
    
    int apSelection = wsi_ap_Types_.indexOf(getWSISelection(WSPlugin.getInstance().getWSIAPContext()));
    wsi_ap_Types_.select(apSelection);
    savedSSBPSetting_ = -1;  // do not restore saved SSBP setting
    processAPSelection(apSelection);
    
    String validationSelection = WSPlugin.getInstance().getWSDLValidationContext().getPersistentWSDLValidation();
    processWSDLValidationSelection(validationSelection);
    
    waitForWSDLValidationCheckbox_.setSelection(WSPlugin.getInstance().getWaitForWSDLValidationContext().getPersistentWaitForWSDLValidation());
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

private String getWSISelection(PersistentWSIContext context)
  {
    
    String WSIvalue = context.getPersistentWSICompliance();
    String WSIText = WstWSUIPluginMessages.WARN_NON_WSI;
    if (PersistentWSIContext.STOP_NON_WSI.equals(WSIvalue)) {
		WSIText = WstWSUIPluginMessages.STOP_NON_WSI;
	} else if (PersistentWSIContext.IGNORE_NON_WSI.equals(WSIvalue)) {
		WSIText = WstWSUIPluginMessages.IGNORE_NON_WSI;
	}
	return WSIText;
  }
  /**
   * Stores the values of the controls back to the preference store.
   */
  private void storeValues()
  {
  	updateWSIContext(wsi_ssbp_Types_.getSelectionIndex(), WSPlugin.getInstance().getWSISSBPContext());
  	updateWSIContext(wsi_ap_Types_.getSelectionIndex(), WSPlugin.getInstance().getWSIAPContext());
  }
  
  private void updateWSIContext(int selectionIndex, PersistentWSIContext context)
  {
    // get the persistent context from the plugin
    
    String value=null;
    switch (selectionIndex) {
    	case 0:
    		value = PersistentWSIContext.STOP_NON_WSI;
    		break;
    	case 1:
    		value = PersistentWSIContext.WARN_NON_WSI;
    		break;
    	case 2:
    		value = PersistentWSIContext.IGNORE_NON_WSI;		
    		break;
    }
    context.updateWSICompliances(value);
  }
  
  public void widgetSelected(SelectionEvent e)
  {
  	
  	processAPSelection( wsi_ap_Types_.getSelectionIndex() );
  	
  }
  
  public void processAPSelection(int selection) {
  	if (selection == 2) { // reset SSBP to default if AP is ignore
  		wsi_ssbp_Types_.setEnabled(true);
  		if (savedSSBPSetting_ != -1)  {
  			// restore saved SSBP setting, if any
  			wsi_ssbp_Types_.select(savedSSBPSetting_);
  			savedSSBPSetting_ = -1;
  		}
  	} else { // set SSBP to follow AP setting if STOP or WARN chosen
  		if (savedSSBPSetting_ == -1)  {  // SSBP setting not saved
  			savedSSBPSetting_ = wsi_ssbp_Types_.getSelectionIndex();
  		}
  		wsi_ssbp_Types_.select(selection);
  		wsi_ssbp_Types_.setEnabled(false);
  		
  	}
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
	  
	  if (waitForWSDLValidationCheckbox_ == event.widget) {		   		  
		  WSPlugin.getInstance().getWaitForWSDLValidationContext().setWaitForWSDLValidation(waitForWSDLValidationCheckbox_.getSelection());
	  } else {
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


}

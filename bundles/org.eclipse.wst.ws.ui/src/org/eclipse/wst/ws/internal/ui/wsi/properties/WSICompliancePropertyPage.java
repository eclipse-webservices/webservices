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
package org.eclipse.wst.ws.internal.ui.wsi.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSIContext;


public class WSICompliancePropertyPage extends PropertyPage implements SelectionListener
{
   private MessageUtils msgUtils_;
	  
  /*CONTEXT_ID PWSI0001 for the WS-I Preference Page*/
  private String INFOPOP_PWSI_PAGE = WSUIPlugin.ID + ".PWSI0000";
  //

  private Label wsi_ssbp_Label_;
  private Combo wsi_ssbp_Types_;
  
  /*CONTEXT_ID PWSI0001 for the stop WS-I SSBP Non compliance Preference Page*/
  private String INFOPOP_PWSI_SSBP_COMBO_STOP_NON_WSI = WSUIPlugin.ID + ".PWSI0001";
  /*CONTEXT_ID PWSI0002 for the warn WS-I SSBP Non compliance Preference Page*/
  private String INFOPOP_PWSI_SSBP_COMBO_WARN_NON_WSI = WSUIPlugin.ID + ".PWSI0002";
  /*CONTEXT_ID PWSI0003 for the ignore WS-I SSBP Non compliance Preference Page*/
  private String INFOPOP_PWSI_SSBP_COMBO_IGNORE_NON_WSI = WSUIPlugin.ID + ".PWSI0003";
  /*CONTEXT_ID PWSI0004 for the WS-I SSBP type combo box on the WS-I AP Non compliance Preference Page*/
  private String INFOPOP_PWSI_SSBP_COMBO_TYPE = WSUIPlugin.ID + ".PWSI0004";
  
  private Label wsi_ap_Label_;
  private Combo wsi_ap_Types_;
  
  /*CONTEXT_ID PWSI0005 for the stop WS-I AP Non compliance Preference Page*/
  private String INFOPOP_PWSI_AP_COMBO_STOP_NON_WSI = WSUIPlugin.ID + ".PWSI0005";
  /*CONTEXT_ID PWSI0006 for the warn WS-I AP Non compliance Preference Page*/
  private String INFOPOP_PWSI_AP_COMBO_WARN_NON_WSI = WSUIPlugin.ID + ".PWSI0006";
  /*CONTEXT_ID PWSI0007 for the ignore WS-I AP Non compliance Preference Page*/
  private String INFOPOP_PWSI_AP_COMBO_IGNORE_NON_WSI = WSUIPlugin.ID + ".PWSI0007";
  /*CONTEXT_ID PWSI0008 for the WS-I AP type combo box on the WS-I AP Non compliance Preference Page*/
  private String INFOPOP_PWSI_AP_COMBO_TYPE = WSUIPlugin.ID + ".PWSI0008";
  
  /*CONTEXT_ID PWSI0009 for the WS-I follow preference radio button the WS-I SSBP compliance project property Page*/
  private String INFOPOPP_PWSI_RADIO_FOLLOW_WSI_SSBP_PREFERENCE = WSUIPlugin.ID + ".PWSI0009";
  /*CONTEXT_ID PWSI0010 for the WS-I follow preference radio button the WS-I AP compliance project property Page*/
  private String INFOPOPP_PWSI_RADIO_FOLLOW_WSI_AP_PREFERENCE = WSUIPlugin.ID + ".PWSI0010";

  private int savedSSBPSetting_ = -1;
  
 /**
   * Creates preference page controls on demand.
   *   @param parent  the parent for the preference page
   */
  protected Control createContents(Composite superparent)
  {
  	String       pluginId = "org.eclipse.wst.ws.ui";
    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    
    Composite   parent = new Composite( superparent, SWT.NONE );	
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    parent.setLayout( layout );
    parent.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    parent.setToolTipText(msgUtils_.getMessage("TOOLTIP_PWSI_PAGE"));
    helpSystem.setHelp(parent, INFOPOP_PWSI_PAGE);

    GridLayout gl = new GridLayout();
    gl.numColumns = 2;
    gl.marginHeight = 0;
    gl.marginWidth = 0;

    Composite wsi_Composite = new Composite (parent, SWT.NONE);
    wsi_Composite.setLayout(gl);
    wsi_Composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        
    wsi_ap_Label_ = new Label(wsi_Composite, SWT.NONE);
    wsi_ap_Label_.setText(msgUtils_.getMessage("LABEL_WSI_AP"));
    wsi_ap_Label_.setToolTipText(msgUtils_.getMessage("TOOLTIP_PWSI_AP_LABEL"));
    wsi_ap_Types_ = new Combo(wsi_Composite, SWT.DROP_DOWN | SWT.READ_ONLY);
    wsi_ap_Types_.setToolTipText(msgUtils_.getMessage("TOOLTIP_PWSI_AP_COMBO"));
    wsi_ap_Types_.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    helpSystem.setHelp(wsi_ap_Types_,INFOPOP_PWSI_AP_COMBO_TYPE);
    
    wsi_ap_Types_.add(msgUtils_.getMessage("STOP_NON_WSI"));
    wsi_ap_Types_.add(msgUtils_.getMessage("WARN_NON_WSI"));
    wsi_ap_Types_.add(msgUtils_.getMessage("IGNORE_NON_WSI"));
    wsi_ap_Types_.add(msgUtils_.getMessage("FOLLOW_WSI_PREFERENCE"));

    wsi_ap_Types_.addSelectionListener(this);
    
    wsi_ssbp_Label_ = new Label(wsi_Composite, SWT.NONE);
    wsi_ssbp_Label_.setText(msgUtils_.getMessage("LABEL_WSI_SSBP"));
    wsi_ssbp_Label_.setToolTipText(msgUtils_.getMessage("TOOLTIP_PWSI_SSBP_LABEL"));
    wsi_ssbp_Types_ = new Combo(wsi_Composite, SWT.DROP_DOWN | SWT.READ_ONLY);
    wsi_ssbp_Types_.setToolTipText(msgUtils_.getMessage("TOOLTIP_PWSI_SSBP_COMBO"));
    wsi_ssbp_Types_.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    helpSystem.setHelp(wsi_ssbp_Types_,INFOPOP_PWSI_SSBP_COMBO_TYPE);
    
    wsi_ssbp_Types_.add(msgUtils_.getMessage("STOP_NON_WSI"));
    wsi_ssbp_Types_.add(msgUtils_.getMessage("WARN_NON_WSI"));
    wsi_ssbp_Types_.add(msgUtils_.getMessage("IGNORE_NON_WSI"));
    wsi_ssbp_Types_.add(msgUtils_.getMessage("FOLLOW_WSI_PREFERENCE"));

    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);    
    return parent;
  }
 
  private String getMessage(String key) 
  {
  		return WSUIPlugin.getMessage(key);
  }

  private Button createRadioButton( Composite parent, String text )
  {
    Button button = new Button( parent, SWT.RADIO );
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
   * @see IWorkbenchPropertyPage
   */
  public void init(IWorkbench workbench)   { 
  }

  /**
   * Initializes states of the controls using default values
   * in the preference store.
   */
  private void initializeDefaults()
  {
    wsi_ssbp_Types_.select(wsi_ssbp_Types_.indexOf(msgUtils_.getMessage("FOLLOW_WSI_PREFERENCE")));
    int apSelection = wsi_ap_Types_.indexOf(msgUtils_.getMessage("FOLLOW_WSI_PREFERENCE"));
    wsi_ap_Types_.select(apSelection);
    savedSSBPSetting_ = -1;  // do not restore saved SSBP setting
    processAPSelection(apSelection);
  }

  /**
   * Initializes states of the controls from the preference store.
   */
  private void initializeValues() {
  	String WSIText = getWSISelection(WSUIPlugin.getInstance().getWSISSBPContext());
    wsi_ssbp_Types_.select(wsi_ssbp_Types_.indexOf(WSIText));
    
    int apSelection = wsi_ap_Types_.indexOf(getWSISelection(WSUIPlugin.getInstance().getWSIAPContext()));
    wsi_ap_Types_.select(apSelection);
    savedSSBPSetting_ = -1;  // do not restore saved SSBP setting
    processAPSelection(apSelection);
	}

  private String getWSISelection(PersistentWSIContext context)
  {
    
  	IProject project = (IProject) getElement();
  	String WSIvalue = context.getProjectWSICompliance(project);
	String WSIText = msgUtils_.getMessage("FOLLOW_WSI_PREFERENCE");
	if (PersistentWSIContext.STOP_NON_WSI.equals(WSIvalue)) {
		WSIText = msgUtils_.getMessage("STOP_NON_WSI");
	} else if (PersistentWSIContext.WARN_NON_WSI.equals(WSIvalue)) {
		WSIText = msgUtils_.getMessage("WARN_NON_WSI");
	} else if (PersistentWSIContext.IGNORE_NON_WSI.equals(WSIvalue)) {
		WSIText = msgUtils_.getMessage("IGNORE_NON_WSI");
	}
	return WSIText;
  }
  
  /**
   * Stores the values of the controls back to the preference store.
   */
  private void storeValues()
  {
  	updateWSIContext(wsi_ssbp_Types_.getSelectionIndex(), WSUIPlugin.getInstance().getWSISSBPContext());
  	updateWSIContext(wsi_ap_Types_.getSelectionIndex(), WSUIPlugin.getInstance().getWSIAPContext());
  }
  
  private void updateWSIContext(int selectionIndex, PersistentWSIContext context)
  {    
  	String value = PersistentWSIContext.FOLLOW_WSI_PREFERENCE;
    IProject project = (IProject) getElement();
    
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
    	case 3:
    		value = PersistentWSIContext.FOLLOW_WSI_PREFERENCE;		
    		break;
    }
    context.updateProjectWSICompliances(project, value);
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
}


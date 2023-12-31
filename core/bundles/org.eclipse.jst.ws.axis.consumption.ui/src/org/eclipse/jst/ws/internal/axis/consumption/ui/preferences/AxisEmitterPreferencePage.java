/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * yyyymmdd   bug     Email and other contact information
 * 20060329   127016 andyzhai@ca.ibm.com - Andy Zhai    
 * 20080428   224726 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.internal.axis.consumption.core.context.AxisEmitterContext;
import org.eclipse.jst.ws.internal.axis.consumption.core.context.AxisEmitterDefaults;
import org.eclipse.jst.ws.internal.axis.consumption.core.plugin.WebServiceAxisConsumptionCorePlugin;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.plugin.WebServiceAxisConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;



public class AxisEmitterPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  /*CONTEXT_ID PPAE0001 for the Axis Emitter Preference Page*/
  private String INFOPOP_PPAE_PAGE = WebServiceAxisConsumptionUIPlugin.ID + ".PPAE0001";
  //
  private Button allWanted;
  /*CONTEXT_ID PPAE0002 for the all wanted check box on the Axis Emitter Preference Page*/
  private String INFOPOP_PPAE_CHECKBOX_ALL_WANTED = "PPAE0002";
  //
  private Button helperWanted;
  /*CONTEXT_ID PPAE0003 for helper wanted check box on the Axis Emitter Preference Page*/
  private String INFOPOP_PPAE_CHECKBOX_HELPER_WANTED = "PPAE0003";
  //
  private Button wrapArrays;
  /*CONTEXT_ID PPAE0004 for the wrap arrays check box on the Axis Emitter Preference Page*/
  private String INFOPOP_PPAE_CHECKBOX_WRAP_ARRAYS = "PPAE0004";

  private Combo deployScopeTypes;
  //*CONTEXT_ID PPAE0005 for the deploy scope type combo box on the Axis Emitter page*/
  private String INFOPOP_PPAE_COMBO_DEPLOY_SCOPE = "PPAE0005";
  
  private StackLayout timeOutSubCompositeStackLayout;
  
  private Text timeOutField;
  int timeOut;
  String wsdl2JavaTimeoutProperty = System.getProperty("AxisWsdl2JavaTimeout");
  
  private Text timeOutMsgText;
  /*CONTEXT_ID PPAE0006 for the time out field on the Axis Emitter Preference page*/
  private String INFOPOP_PPAE_FIELD_TIME_OUT = "PPAE0006";

  private Button useInheritedMethods;
  /*CONTEXT_ID PPAE0007 for the use inherited methods check box on the Axis Emitter Preference page*/
  private String INFOPOP_PPAE_CHECKBOX_USE_INHERITED_METHODS = "PPAE0007";

  private Button validateAgainstJAXRPC;
  /*CONTEXT_ID PPAE0010 for the "validate against JAXRPC..." check box on the Axis Emitter Preference page*/
  private String INFOPOP_PPAE_CHECKBOX_VALIDATE_AGAINST_JAXRPC = "PPAE0010";
  
  /*CONTEXT_ID PPAE0008 for the wsdl2java group on the Axis Emitter Preference page*/
  private String INFOPOP_PPAE_GROUP_WSDL2JAVA = "PPAE0008";
  /*CONTEXT_ID PPAE0009 for the java2wsdl group on the Axis Emitter Preference page*/
  private String INFOPOP_PPAE_GROUP_JAVA2WSDL = "PPAE0009";
/**
   * Creates preference page controls on demand.
   *   @param parent  the parent for the preference page
   */
  protected Control createContents(Composite superparent)
  {
	UIUtils utils = new UIUtils( WebServiceAxisConsumptionUIPlugin.ID );
  	IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
  	
    Composite   parent = new Composite( superparent, SWT.NONE );	
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    parent.setLayout( layout );
    parent.setToolTipText(AxisConsumptionUIMessages.TOOLTIP_PPAE_PAGE);
    helpSystem.setHelp(parent, INFOPOP_PPAE_PAGE);
    Group wsdl2JavaGroup = utils.createGroup(parent, AxisConsumptionUIMessages.GROUP_WSDL2JAVA_NAME, AxisConsumptionUIMessages.TOOLTIP_PPAE_GROUP_WSDL2JAVA, INFOPOP_PPAE_GROUP_WSDL2JAVA, 2, 10,10);
    allWanted = createCheckBox(wsdl2JavaGroup, AxisConsumptionUIMessages.BUTTON_ALL_WANTED,AxisConsumptionUIMessages.TOOLTIP_PPAE_CHECKBOX_ALL_WANTED,INFOPOP_PPAE_CHECKBOX_ALL_WANTED);

    helperWanted  = createCheckBox(wsdl2JavaGroup, AxisConsumptionUIMessages.BUTTON_HELPER_WANTED,AxisConsumptionUIMessages.TOOLTIP_PPAE_CHECKBOX_HELPER_WANTED,INFOPOP_PPAE_CHECKBOX_HELPER_WANTED);

    wrapArrays = createCheckBox(wsdl2JavaGroup, AxisConsumptionUIMessages.BUTTON_WRAP_ARRAYS,AxisConsumptionUIMessages.TOOLTIP_PPAE_CHECKBOX_WRAP_ARRAYS,INFOPOP_PPAE_CHECKBOX_WRAP_ARRAYS);    
    
    deployScopeTypes = utils.createCombo(wsdl2JavaGroup, AxisConsumptionUIMessages.LABEL_DEPLOY_SCOPE, AxisConsumptionUIMessages.TOOLTIP_PPAE_COMBO_DEPLOY_SCOPE, INFOPOP_PPAE_COMBO_DEPLOY_SCOPE, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
    /* 
     * Ensure the order is the same as it in the fields for class
     * org.eclipse.jst.ws.internal.axis.consumption.core.context.AxisEmitterContext
     */
    deployScopeTypes.setItems(new String []{AxisConsumptionUIMessages.DEPLOY_SCOPE_APPLICATION, AxisConsumptionUIMessages.DEPLOY_SCOPE_REQUEST,AxisConsumptionUIMessages.DEPLOY_SCOPE_SESSION});
    
    Label timeoutLabel = new Label(wsdl2JavaGroup, SWT.WRAP);
    timeoutLabel.setText(AxisConsumptionUIMessages.LABEL_TIME_OUT);
    timeoutLabel.setToolTipText(AxisConsumptionUIMessages.TOOLTIP_PPAE_FIELD_TIME_OUT);
    
    Composite timeOutSubComposite = utils.createComposite(wsdl2JavaGroup, 1);
    timeOutSubCompositeStackLayout = new StackLayout();
    timeOutSubComposite.setLayout(timeOutSubCompositeStackLayout);
    
    // only one of this is displayed at one time, they share infopop and tooltip text
    timeOutField = createTextField(timeOutSubComposite, null ,AxisConsumptionUIMessages.TOOLTIP_PPAE_FIELD_TIME_OUT,INFOPOP_PPAE_FIELD_TIME_OUT);
    timeOutMsgText = createTextField(timeOutSubComposite, null ,AxisConsumptionUIMessages.TOOLTIP_PPAE_FIELD_TIME_OUT,INFOPOP_PPAE_FIELD_TIME_OUT);
    
    timeOutMsgText.setEditable(false);
    
    Group java2WsdlGroup = utils.createGroup(parent, AxisConsumptionUIMessages.GROUP_JAVA2WSDL_NAME, AxisConsumptionUIMessages.TOOLTIP_PPAE_GROUP_JAVA2WSDL, INFOPOP_PPAE_GROUP_JAVA2WSDL, 2, 10,10);
    useInheritedMethods = createCheckBox(java2WsdlGroup, AxisConsumptionUIMessages.BUTTON_USE_INHERITED_METHODS,AxisConsumptionUIMessages.TOOLTIP_PPAE_CHECKBOX_USE_INHERITED_METHODS,INFOPOP_PPAE_CHECKBOX_USE_INHERITED_METHODS);
    validateAgainstJAXRPC = createCheckBox(java2WsdlGroup, AxisConsumptionUIMessages.BUTTON_VALIDATE_AGAINST_JAXRPC,AxisConsumptionUIMessages.TOOLTIP_PPAE_CHECKBOX_VALIDATE_AGAINST_JAXRPC,INFOPOP_PPAE_CHECKBOX_VALIDATE_AGAINST_JAXRPC);

    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);    

    return parent;
  }
  
  /**
   * Creates checkbox with horizontalSpan = 2 in its grid data in order to
   * match the grid layout for combo 
   */
  private Button createCheckBox( Composite parent, String labelName, String tooltip, String infopop )
  {
    Button button = new Button( parent, SWT.CHECK );
    button.setText(labelName);
    button.setToolTipText( tooltip );
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( button, WebServiceAxisConsumptionUIPlugin.ID + "." + infopop );
	GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
	gd.horizontalSpan= 2;
	button.setLayoutData(gd);	
    return button;
  }
     
  private Text createTextField(Composite parent,String labelName, String tooltip, String infopop) 
  { tooltip = tooltip == null ? labelName : tooltip;
	if( labelName != null )
    {
      Label label = new Label( parent, SWT.WRAP);
      label.setText( labelName );
      label.setToolTipText( tooltip );
    }
     Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
     GridData data = new GridData();
     data.verticalAlignment = GridData.FILL;
     data.horizontalAlignment = GridData.FILL;
     data.grabExcessHorizontalSpace = true;
     text.setLayoutData(data);
     text.setToolTipText(tooltip);
     if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( text, WebServiceAxisConsumptionUIPlugin.ID + "." + infopop );
     
     text.addModifyListener( new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			numberFieldChanged((Text) e.widget);
		}
	 }
     );
     return text;
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
    if(validateNumber(timeOutField.getText()).isOK())
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
    allWanted.setSelection( AxisEmitterDefaults.getAllWantedDefault());
    helperWanted.setSelection( AxisEmitterDefaults.getHelperWantedDefault());
    wrapArrays.setSelection( AxisEmitterDefaults.getWrapArraysDefault());
    deployScopeTypes.select(AxisEmitterDefaults.getDeployScopeDefault());
    if (wsdl2JavaTimeoutProperty != null) timeOutField.setText(""+ getTimeOutValueWithProperty());
    else timeOutField.setText("" + AxisEmitterDefaults.getTimeOutDefault());	
    useInheritedMethods.setSelection(AxisEmitterDefaults.getUseInheritedMethodsDefault());
    validateAgainstJAXRPC.setSelection(AxisEmitterDefaults.getValidateAgainstJAXRPC());
  }

  /**
   * Initializes states of the controls from the preference store.
   */
  private void initializeValues()
  {
    // get the persistent context from the plugin
    AxisEmitterContext context = WebServiceAxisConsumptionCorePlugin.getInstance().getAxisEmitterContext();
    allWanted.setSelection( context.isAllWantedEnabled());
    helperWanted.setSelection( context.isHelperWantedEnabled());
    wrapArrays.setSelection( context.isWrapArraysEnabled());
    deployScopeTypes.select(context.getDeployScopeType());
    useInheritedMethods.setSelection( context.isUseInheritedMethodsEnabled());
    validateAgainstJAXRPC.setSelection( context.isValidateAgainstJAXRPCEnabled() );
    if (wsdl2JavaTimeoutProperty != null) 
    {	timeOut=getTimeOutValueWithProperty();
    	//timeOutField.setEnabled(false);
    	timeOutSubCompositeStackLayout.topControl = timeOutMsgText;
        timeOutMsgText.setText(AxisConsumptionUIMessages.MSG_USE_JVM_ARGUMENT_FOR_TIME_OUT);		
    }
	else
	{
	    timeOut = context.getTimeOut();
	    timeOutSubCompositeStackLayout.topControl = timeOutField;
	}
    timeOutField.setText(""+ timeOut);
   }

  /**
   * Stores the values of the controls back to the preference store.
   */
  private void storeValues()
  {
    // get the persistent context from the plugin
    AxisEmitterContext context = WebServiceAxisConsumptionCorePlugin.getInstance().getAxisEmitterContext();
    context.setAllWantedEnabled( allWanted.getSelection() );
    context.setHelperWantedEnabled( helperWanted.getSelection() );
    context.setWrapArraysEnabled( wrapArrays.getSelection() );
    context.selectDeployScopeType(deployScopeTypes.getSelectionIndex());
    timeOut = Integer.parseInt(timeOutField.getText().trim());
    context.setTimeOut(timeOut);
    context.setUseInheritedMethodsEnabled( useInheritedMethods.getSelection() );
    context.setValidateAgainstJAXRPCEnabled( validateAgainstJAXRPC.getSelection() );
  }
  
  private void numberFieldChanged(Text textControl) {
		IStatus status = validateNumber(textControl.getText());
		setValid(!status.matches(IStatus.ERROR));
		applyToStatusLine(this,status);	
 }
  
  private int getTimeOutValueWithProperty() 
  {
	  if (wsdl2JavaTimeoutProperty != null)
	  {	try {
		  long timeOutProperty = Long.parseLong(wsdl2JavaTimeoutProperty);
		  if (timeOutProperty < 0) return -1; // timeout = -1 equals never time out; treating all negative number as -1
		  else return (int)Math.ceil(timeOutProperty/1000.0);
	    } catch (NumberFormatException e){
	      System.err.print("AxisWSDL2JavaTimeout is not a valid number");
	      return AxisEmitterDefaults.getTimeOutDefault();
	    }
	  }
	  else return AxisEmitterDefaults.getTimeOutDefault();
  }
  
  private IStatus validateNumber(String numberString)
  {
	    IStatus status;
		try 
		{
			int number = Integer.parseInt(numberString);
			if (number <= 0 && number != -1)
			{
				status = new Status(IStatus.ERROR, WebServiceAxisConsumptionUIPlugin.ID, IStatus.ERROR,
						AxisConsumptionUIMessages.MSG_ERROR_INVALID_TIME_OUT, null);
			}
			else
			{	timeOut = number;
				status = Status.OK_STATUS;
				//we set param:message="" here, Later in 
				//applyToStatusLine(), we do page.setMessage(null,type) 
				status = new Status(IStatus.OK, WebServiceAxisConsumptionUIPlugin.ID, IStatus.OK, "",null);
			}
		} 
		catch 
		(NumberFormatException e) 
		{
			status = new Status(IStatus.ERROR, WebServiceAxisConsumptionUIPlugin.ID, IStatus.ERROR,
					AxisConsumptionUIMessages.MSG_ERROR_INVALID_TIME_OUT, null);
		}
		return status; 
  	}
  
	private void applyToStatusLine(DialogPage page, IStatus status) {
		String message= status.getMessage();
		switch (status.getSeverity()) {
			case IStatus.OK:
				page.setMessage(null, IMessageProvider.NONE);
				page.setErrorMessage(null);
				break;
			case IStatus.WARNING:
				page.setMessage(message, IMessageProvider.WARNING);
				page.setErrorMessage(null);
				break;				
			case IStatus.INFO:
				page.setMessage(message, IMessageProvider.INFORMATION);
				page.setErrorMessage(null);
				break;			
			default:
				if (message.length() == 0) {
					message= null;
				}
				page.setMessage(null);
				page.setErrorMessage(message);
				break;		
		}
	}
}

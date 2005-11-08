/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on May 12, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.jst.ws.internal.ui.dialog.DialogUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



public class AddHandlerDialog extends Dialog implements Listener {

  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";

  private boolean validateOn_;
  private boolean isClient_;
  private String className;
  private String name;
  private String selectedPortName;
  
  private Text classNameText;
  private Text nameText;
  private Combo portNameCombo;
  
  // ----TOOLTIPS Section----
  /* CONTEXT_ID AHDL0001 for the Handler Config Page */
  private final String INFOPOP_TEXT_HANDLER_NAME  = "AHDL0001"; //$NON-NLS-1$
  
  /* CONTEXT_ID AHDL0002 for the Handler Config Page */
  private final String INFOPOP_TEXT_HANDLER_CLASS  = "AHDL0002"; //$NON-NLS-1$
  
  /* CONTEXT_ID AHDL0001 for the Handler Config Page */
  private final String INFOPOP_TEXT_HANDLER_PORT  = "AHDL0003"; //$NON-NLS-1$
    
  private Button beanClassBrowseButton_;
  /*CONTEXT_ID PBCL0003 for the Bean Class Browse button of the Bean Selection Page*/
  private String INFOPOP_PBCL_BUTTON_BEAN_CLASS_BROWSE = "PBCL0003";
  
  public AddHandlerDialog( Shell shell, boolean isClientHandler)
  {
    super(shell);
    isClient_ = isClientHandler;
  }

  protected Control createContents(Composite parent) {
    Composite comp = (Composite) super.createContents(parent);
    parent.getShell().pack();
    return comp;
  }

  protected Control createDialogArea(Composite parent) {
    validateOn_ = false;
    UIUtils uiUtils = new UIUtils(pluginId_);
    
    Shell thisShell = parent.getShell();
    if (thisShell == null) {
      thisShell = createShell();
    }
    thisShell.setText(ConsumptionUIMessages.DIALOG_TITLE_WS_ADD_HANDLER);
    
    Composite composite = (Composite) super.createDialogArea(parent);    
    
    Composite sourceLocationComp = uiUtils.createComposite(composite, 3);
    
    classNameText = uiUtils.createText(sourceLocationComp, ConsumptionUIMessages.LABEL_TEXT_HANDLER_CLASS, ConsumptionUIMessages.TOOLTIP_TEXT_HANDLER_CLASS, INFOPOP_TEXT_HANDLER_CLASS, SWT.SINGLE | SWT.BORDER);
    classNameText.addListener(SWT.Modify,this);
    
    beanClassBrowseButton_ = uiUtils.createPushButton( sourceLocationComp, ConsumptionUIMessages.BUTTON_BROWSE_CLASSES,
    		ConsumptionUIMessages.TOOLTIP_PBCL_BUTTON_BEAN_CLASS_BROWSE,
                                                       INFOPOP_PBCL_BUTTON_BEAN_CLASS_BROWSE );
    beanClassBrowseButton_.addSelectionListener( new SelectionAdapter()
        {
          public void widgetSelected( SelectionEvent evt )
          {
            handleBrowseClasses();  
          }
        } );
    
    nameText = uiUtils.createText(sourceLocationComp, ConsumptionUIMessages.LABEL_TEXT_HANDLER_NAME, ConsumptionUIMessages.TOOLTIP_TEXT_HANDLER_NAME, INFOPOP_TEXT_HANDLER_NAME, SWT.SINGLE | SWT.BORDER );
    nameText.addListener(SWT.Modify, this);
    
    // dummy label for column 3.
    new Label( sourceLocationComp, SWT.NONE );
    
    if (!isClient_){
      portNameCombo = uiUtils.createCombo(sourceLocationComp, ConsumptionUIMessages.LABEL_TEXT_HANDLER_PORT, ConsumptionUIMessages.TOOLTIP_TEXT_HANDLER_PORT, INFOPOP_TEXT_HANDLER_PORT, SWT.SINGLE | SWT.BORDER );
      portNameCombo.addListener(SWT.Modify, this);
      
      // dummy label for column 3.
      new Label( sourceLocationComp, SWT.NONE );
    }
    validateOn_ = true;
   return composite;
  }
  
  private void handleBrowseClasses()
  {
    Shell shell = getShell();
    IType itype = DialogUtils.browseClassesAsIType(shell, ResourcesPlugin.getWorkspace().getRoot().getProjects(), new ProgressMonitorDialog(shell));
    
    if( itype != null )
    {
      classNameText.setText(itype.getFullyQualifiedName());      
    }
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
   */
  public void handleEvent(Event event) {
    if (!validateOn_)
      return;
    if (classNameText == event.widget) {
      handleClassNameTextEvent();
      validateTextFieldEntries();
      return;
    }
    if (nameText == event.widget) {
      handleNameTextEvent();
      validateTextFieldEntries();      
      return;
    }
    if (portNameCombo == event.widget) {
      handlePortNameTextEvent();
      validateTextFieldEntries();      
      return;
    }    
  }

  private void handleClassNameTextEvent(){
    className = classNameText.getText();
    if (className.lastIndexOf(".java") != -1){
      nameText.setText(className.substring(0, className.lastIndexOf(".java")));
    }
    else {
      nameText.setText(className);
    }
    
    if (className.lastIndexOf(".") != -1){
      nameText.setText(className.substring(className.lastIndexOf(".")+1, className.length()));
    }
    name = nameText.getText();
  }
  
  private void handleNameTextEvent() {
   name = nameText.getText(); 

  }
  
  private void handlePortNameTextEvent(){
    selectedPortName = portNameCombo.getText();
  }
  
  private void disableOKButton() {
    if (getButton(0) != null)
      getButton(0).setEnabled(false);
  }

  private void enableOKButton() {
    if (getButton(0) != null)
      getButton(0).setEnabled(true);
  }
  
  private void validateTextFieldEntries(){
  	if (classNameText.getText().equals("")){
  		disableOKButton();
  		return;
  	}
  	if (nameText.getText().equals("")){
  		disableOKButton();
  		return;
  	}
  	if (!isClient_){
  		if (portNameCombo.getText()!=null && portNameCombo.getText().equals("")){
  			disableOKButton();
  			return;
  		}
  		else if (portNameCombo.getText()==null) {
  			disableOKButton();
  			return;
  		}
  	}
  	
  	enableOKButton();
  	
	
  }
  
  /**
   * @return Returns the className.
   */
  public String getClassName() {
    if (className.lastIndexOf(".java") != -1){
      return className.substring(0, className.lastIndexOf(".java"));
    }
    else {
      return className;
    }
  }
  /**
   * @param className The className to set.
   */
  public void setClassName(String className) {
    this.className = className;
  }
  /**
   * @return Returns the name.
   */
  public String getName() {
    return name;
  }
  /**
   * @param name The name to set.
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * @return Returns the portName.
   */
  public String getPortName() {
    return selectedPortName;
  }
  /**
   * @param portName The portName to set.
   */
  public void setPortNames(String[] portNames) {
    portNameCombo.setItems(portNames);
    portNameCombo.select(0);
  }
}

/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
 * 20060418   136335 joan@ca.ibm.com - Joan Haggarty
 * 20060418   136759 joan@ca.ibm.com - Joan Haggarty
 * 20061106   142500 david.schneider@unisys.com - David Schneider
 * 20060829   180833 ericdp@ca.ibm.com - Eric D. Peters,  New Web Service wizard service settings dialog has unlabelled enabled text fields
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class SimpleDialog extends Dialog implements DialogDataEvents
{
  private   WidgetContributor widget_;
  private   WidgetDataEvents  dataEvents_;
  private   Listener          statusListener_;
  protected WizardPageManager pageManager_;  
  private Text messageText_;
  private boolean ok_;
  	
  public SimpleDialog( Shell shell, PageInfo pageInfo)
  {
	super(shell);
	widget_  = pageInfo.getWidgetFactory().create();
  	statusListener_ = new StatusListener();
  } 	 
  
  Composite parent_;
  public Control createDialogArea( Composite parent )
  {	
	  parent_ = parent;
	Composite control = (Composite) super.createDialogArea(parent);
	dataEvents_ = widget_.addControls( control, statusListener_ );
	
	//Text area to display validation messages
	messageText_ = new Text(parent, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
	messageText_.setBackground(parent.getBackground());
    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    griddata.horizontalIndent = 10;
    messageText_.setLayoutData( griddata );    
    parent_.setFocus();
    
    //Call extenders override of the callSetters method to intialize any data in widget
    callSetters();   
    Dialog.applyDialogFont(parent);
    return control;
  }  
  
  //Override of Dialog.createContents so we can affect button enablement 
  protected Control createContents(Composite parent) {
	Control c = super.createContents(parent);
	//enable controls for initial entry to dialog
	validatePageToStatus();
	return c;
  }

  // Enable or disable ok button based on state
  private void enableOk(boolean state)
  {
	  Button okButton = getButton(IDialogConstants.OK_ID);
	  if (okButton != null)
	    okButton.setEnabled(state);
  }
  
  //Implement this method to call all setters on the dialog widget after the controls are created
  protected abstract void callSetters();
  
  public WidgetDataEvents getDataEvents()
  {
    return dataEvents_;
  }
  
  protected WidgetContributor getWidget()
  {
	  return widget_;
  }
  
  // sets message in message area above OK and Cancel buttons
  private void setMessage(String message)
  {  
	  if (message == null)
		  messageText_.setText("");	      
	  else		  
	      messageText_.setText(message);
      // force a resize of the parent composite and wrap of the text field if necessary
	  parent_.layout();  
  }
  	  
  
  
  public void validatePageToStatus()
  {
    IStatus status  = widget_.getStatus();    
        
    if( status != null )
    {
      String message = status.getMessage();
      int severity = status.getSeverity();
      
      // for error condition, disable ok and print message
      if( severity == IStatus.ERROR )
      { 
        if( message.length() == 0 )
        {
          setMessage( null);
        }
        else
        {
          setMessage(message);
        }
        ok_ = false;
      }
      // display warning and info messages but still enable OK
      else if (severity == IStatus.WARNING | severity == IStatus.INFO)
      {
        setMessage(message);
        ok_ = true;        
      }     
      // no message to display
      else  
      {
        setMessage( null);
        ok_ = true;
      }
    }
    // null status case
    else
    {
      setMessage(null);
      ok_ = true;
    }
    
    enableOk(ok_);    
  }
  
  private class StatusListener implements Listener
  {
	public void handleEvent( Event evt ) 
	{
      validatePageToStatus();
	}
  }
}  


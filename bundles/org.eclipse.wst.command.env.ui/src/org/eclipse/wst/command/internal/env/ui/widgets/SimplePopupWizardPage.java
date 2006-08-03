/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060731   120378 makandre@ca.ibm.com - Andrew Mak, Fields not visible in Large font settings
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.context.PersistentActionDialogsContext;
import org.eclipse.wst.command.internal.env.ui.EnvironmentUIMessages;



public class SimplePopupWizardPage extends SimpleWizardPage
{  
  private String id_;
  
  public SimplePopupWizardPage( PageInfo pageInfo, WizardPageManager pageManager, String id )
  {
  	super( pageInfo, pageManager );
  	
  	id_ = id;
  } 	  
  	
  protected Composite internalCreateControl( Composite parent ) 
  {  	
	Composite composite = super.internalCreateControl(parent);
	  
  	final PersistentActionDialogsContext context   = PersistentActionDialogsContext.getInstance();
  	
  	// If the current page is null then this must be the first page.
  	if( pageManager_.getCurrentPage() == null && context.showCheckbox(id_) )
  	{
      final  Button button  = new Button( composite, SWT.CHECK );
      String label   = EnvironmentUIMessages.CHECKBOX_DO_NOT_SHOW_DIALOG_AGAIN;
      
      /*CONTEXT_ID TWP0001 for the show/hide check box*/
      String infopop =	"org.eclipse.wst.command.env.ui.TWP0001";
      String tooltip = EnvironmentUIMessages.TOOLTIP_DO_NOT_SHOW_DIALOG_AGAIN;
      
      button.setText( label );
      button.setToolTipText( tooltip );
      button.addSelectionListener( new SelectionAdapter()
                                   {
                                     public void widgetSelected( SelectionEvent evt )
                                     {
                                       context.setActionDialogEnabled( id_, button.getSelection() );  
                                     }
                                   });
      button.setSelection( false );
      
      PlatformUI.getWorkbench().getHelpSystem().setHelp( button, infopop );  	
  	}
  	
  	return composite;
  }  
}  

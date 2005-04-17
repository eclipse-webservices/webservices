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
package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.context.PersistentActionDialogsContext;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;



public class SimplePopupWizardPage extends SimpleWizardPage
{  
  private String id_;
  
  public SimplePopupWizardPage( PageInfo pageInfo, WizardPageManager pageManager, String id )
  {
  	super( pageInfo, pageManager );
  	
  	id_ = id;
  } 	  
  	
  public void createControl( Composite parent ) 
  {
  	      Composite                      composite = new Composite( parent, SWT.NONE );
  	      MessageUtils                   msgUtils  = new MessageUtils("org.eclipse.wst.command.env.ui.environmentui", this );
  	final PersistentActionDialogsContext context   = PersistentActionDialogsContext.getInstance();
  	
    GridLayout gridlayout   = new GridLayout();
    gridlayout.marginHeight = 0;
    gridlayout.marginWidth  = 0;
    
    composite.setLayout( gridlayout );
    GridData griddata = new GridData(GridData.FILL_BOTH );
    composite.setLayoutData( griddata );
      	
  	super.createControl( composite );
  	
  	// If the current page is null then this must be the first page.
  	if( pageManager_.getCurrentPage() == null && context.showCheckbox(id_) )
  	{
      final  Button button  = new Button( composite, SWT.CHECK );
      String label   = msgUtils.getMessage( "CHECKBOX_DO_NOT_SHOW_DIALOG_AGAIN" );
      
      /*CONTEXT_ID TWP0001 for the show/hide check box*/
      String infopop =	"org.eclipse.wst.command.env.ui.TWP0001";
      String tooltip = msgUtils.getMessage( "TOOLTIP_DO_NOT_SHOW_DIALOG_AGAIN" );
      
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
  	
  	setControl( composite );
  }  
}  

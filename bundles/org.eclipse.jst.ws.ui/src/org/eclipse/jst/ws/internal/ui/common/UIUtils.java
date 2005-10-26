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
package org.eclipse.jst.ws.internal.ui.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;


public class UIUtils 
{
  String       infoPopid_;
  MessageUtils msgUtils_;
  
  public UIUtils( MessageUtils msgUtils, String infoPopid )
  {
    msgUtils_  = msgUtils;
    infoPopid_ = infoPopid;
  }
  
  public Button createRadioButton( Composite parent, String labelName, String tooltip, String infopop )
  {
    return createButton( SWT.RADIO, parent, labelName, tooltip, infopop );
  }
  
  public Button createCheckbox( Composite parent, String labelName, String tooltip, String infopop )
  {
    return createButton( SWT.CHECK, parent, labelName, tooltip, infopop );
  }
  
  public Button createPushButton( Composite parent, String labelName, String tooltip, String infopop )
  {
    return createButton( SWT.NONE, parent, labelName, tooltip, infopop );
  }
  
  public Button createButton( int kind, Composite parent, String labelName, String tooltip, String infopop )
  {
    Button button = new Button( parent, kind );
    
    tooltip = tooltip == null ? labelName : tooltip;
    button.setText( msgUtils_.getMessage( labelName ) );
    button.setToolTipText( msgUtils_.getMessage( tooltip ) );
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( button, infoPopid_ + "." + infopop );
    
    return button;
  }
  
  public Combo createCombo( Composite parent, String labelName, String tooltip, String infopop, int style )
  {    
    tooltip = tooltip == null ? labelName : tooltip;
    
    if( labelName != null )
    {
      Label label = new Label( parent, SWT.WRAP);
      label.setText( msgUtils_.getMessage( labelName ) );
      label.setToolTipText( msgUtils_.getMessage( tooltip ) );
    }
    
    Combo combo = new Combo( parent, style );
    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
     
    combo.setLayoutData( griddata );
    combo.setToolTipText( msgUtils_.getMessage(tooltip));
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( combo, infoPopid_ + "." + infopop );
    
    return combo;      
  }
  
  public Text createText( Composite parent, String labelName, String tooltip, String infopop, int style )
  {    
    tooltip = tooltip == null ? labelName : tooltip;
    
    if( labelName != null )
    {
      Label label = new Label( parent, SWT.WRAP);
      label.setText( msgUtils_.getMessage( labelName ) );
      label.setToolTipText( msgUtils_.getMessage( tooltip ) );
    }
    
    Text text = new Text( parent, style );
    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    
    text.setLayoutData( griddata );
    text.setToolTipText( msgUtils_.getMessage(tooltip));
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( text, infoPopid_ + "." + infopop );
    
    return text;      
  }
  
  public Composite createComposite( Composite parent, int columns )
  {
    return createComposite( parent, columns, -1, -1 );
  }
  
  public Composite createComposite( Composite parent, int columns, int marginHeight, int marginWidth )
  {
    Composite composite = new Composite( parent, SWT.NONE );
    
    GridLayout gridlayout   = new GridLayout();
    gridlayout.numColumns   = columns;
  
    if( marginHeight >= 0 ) gridlayout.marginHeight = marginHeight;
    if( marginWidth >= 0 )  gridlayout.marginWidth  = marginWidth;
    
    composite.setLayout( gridlayout );
    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    composite.setLayoutData( griddata );
    
    return composite;
    
  }
  public Group createGroup( Composite parent, String groupName, String tooltip, String infopop )
  {
  	return createGroup( parent, groupName, tooltip, infopop, 1, -1, -1 );
  }
  
  public Group createGroup( Composite parent, String groupName, String tooltip, String infopop, int columns, int marginHeight, int marginWidth )
  {
    Group      newGroup    = new Group( parent, SWT.NONE );
    GridData   griddata    = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    GridLayout gridlayout  = new GridLayout();
    
    gridlayout.numColumns  = columns;
  
    if( marginHeight >= 0 ) gridlayout.marginHeight = marginHeight;
    if( marginWidth >= 0 )  gridlayout.marginWidth  = marginWidth;
        
    tooltip = tooltip == null ? groupName : tooltip;
    newGroup.setLayout( gridlayout );
    newGroup.setText( msgUtils_.getMessage( groupName ) );
    newGroup.setLayoutData( griddata );
    newGroup.setToolTipText( msgUtils_.getMessage( tooltip ));
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp(newGroup, infoPopid_ + "." + infopop);
    
    return newGroup;
  }
  
  public Tree createTree( Composite parent, String tooltip, String infopop, int style )
  {
    
    tooltip = tooltip == null ? "" : tooltip;
    
    Tree tree = new Tree( parent, style );
     
    tree.setLayoutData( createFillAll() );
    tree.setToolTipText( msgUtils_.getMessage(tooltip));
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( tree, infoPopid_ + "." + infopop );
    
    return tree;      
    
  }
  
  public Table createTable( Composite parent, String tooltip, String infopop, int style )
  {
    
    tooltip = tooltip == null ? "" : tooltip;
    
    Table table = new Table( parent, style );
     
    //table.setLayoutData( createFillAll() );
    table.setToolTipText( msgUtils_.getMessage(tooltip));
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( table, infoPopid_ + "." + infopop );
    
    return table;      
    
  }  
  
  public Label createHorizontalSeparator( Composite parent, int spacing )
  {
    Composite composite = createComposite( parent, 1, spacing, -1 );
     
	Label separator = new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL );
	
	GridData griddata  = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
	separator.setLayoutData( griddata );
   
	return separator;
  }
  
  public GridData createFillAll()
  {
	GridData data = new GridData( GridData.HORIZONTAL_ALIGN_FILL |
			                      GridData.GRAB_HORIZONTAL |
			                      GridData.VERTICAL_ALIGN_FILL |
			                      GridData.GRAB_VERTICAL );
    return data;
  }
}

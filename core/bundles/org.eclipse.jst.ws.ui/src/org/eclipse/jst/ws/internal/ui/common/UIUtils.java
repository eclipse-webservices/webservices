/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
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
 * 20060817   140017 makandre@ca.ibm.com - Andrew Mak, longer project or server/runtime strings do not resize wizard
 * 20060829   155441 makandre@ca.ibm.com - Andrew Mak, web service wizard hangs during resize
 * 20090302   242462 ericdp@ca.ibm.com - Eric D. Peters, Save Web services wizard settings
 * 20090722   278136 kchong@ca.ibm.com - Keith Chong, Use SWT.LEFT or LEAD instead of SWT.WRAP style for Label
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jst.ws.internal.ui.common.ComboWithHistory;


public class UIUtils 
{
	/**
	 * A default padding value for horizontalResize().
	 */
	public final static int DEFAULT_PADDING = 35;
	
  String       infoPopid_;
  
  public UIUtils( String infoPopid )
  {
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
    button.setText( labelName );
    button.setToolTipText( tooltip );
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( button, infoPopid_ + "." + infopop );
    
    return button;
  }
  
  public Combo createCombo( Composite parent, String labelName, String tooltip, String infopop, int style )
  {    
	  return createCombo(parent, labelName, tooltip, infopop, style, null);
  }
  public Combo createCombo( Composite parent, String labelName, String tooltip, String infopop, int style, IDialogSettings settings ) {
	    tooltip = tooltip == null ? labelName : tooltip;
	    Combo combo;
	    if( labelName != null )
	    {
	      Label label = new Label( parent, SWT.WRAP);
	      label.setText( labelName );
	      label.setToolTipText( tooltip );
	    }
	    if (settings == null)
	    	combo = new Combo( parent, style );
	    else
	    	combo = new ComboWithHistory(parent, style, settings);
	    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	     
	    combo.setLayoutData( griddata );
	    combo.setToolTipText( tooltip );
	    
	    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( combo, infoPopid_ + "." + infopop );
	    
	    return combo;      

  }
  public ComboWithHistory createComboWithHistory(Composite parent, String labelName, String tooltip, String infopop, int style, IDialogSettings settings ) {
	  return (ComboWithHistory)createCombo(parent, labelName, tooltip, infopop, style, settings);
  }
  public Text createText( Composite parent, String labelName, String tooltip, String infopop, int style )
  {    
    tooltip = tooltip == null ? labelName : tooltip;
    
    if( labelName != null )
    {
      Label label = new Label( parent, SWT.LEAD);
      label.setText(  labelName  );
      label.setToolTipText(  tooltip );
    }
    
    Text text = new Text( parent, style );
    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    
    text.setLayoutData( griddata );
    text.setToolTipText( tooltip);
    
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
    newGroup.setText(  groupName  );
    newGroup.setLayoutData( griddata );
    newGroup.setToolTipText(  tooltip );
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp(newGroup, infoPopid_ + "." + infopop);
    
    return newGroup;
  }
  
  public Tree createTree( Composite parent, String tooltip, String infopop, int style )
  {
    
    tooltip = tooltip == null ? "" : tooltip;
    
    Tree tree = new Tree( parent, style );
     
    tree.setLayoutData( createFillAll() );
    tree.setToolTipText( tooltip);
    
    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( tree, infoPopid_ + "." + infopop );
    
    return tree;      
    
  }
  
  public Table createTable( Composite parent, String tooltip, String infopop, int style )
  {
    
    tooltip = tooltip == null ? "" : tooltip;
    
    Table table = new Table( parent, style );
     
    //table.setLayoutData( createFillAll() );
    table.setToolTipText( tooltip);
    
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
  
  public void createInfoPop(Control ctrl, String infopop)
  {
	  if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( ctrl, infoPopid_ + "." + infopop );
  }
  
  /**
   * Resizes the width of the target composite so that it is as wide as the
   * reference composite plus a padding value.  
   * 
   * @param target The composite to resize.
   * @param reference The reference composite
   * @param padding The padding value
   */
  public void horizontalResize(Composite target, Composite reference, int padding) {
		
		Point originalSize  = target.getSize();
		Point referenceSize = reference.getSize();
		
		padding = padding >= 0 ? padding : DEFAULT_PADDING;
		
		if (referenceSize.x + padding > originalSize.x)
			target.setSize(referenceSize.x + padding, originalSize.y);
  }  
}

/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.viewers.widgets;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.flatui.FlatPageSection;
import org.eclipse.wst.wsdl.ui.internal.util.flatui.WidgetFactory;
import org.eclipse.wst.wsdl.ui.internal.util.ui.FlatViewUtility;

public class ComponentsSection extends FlatPageSection
{
  protected CCombo namesCombo;
  protected Button newButton;
  protected Button importButton;
  protected String title;
    
  public ComponentsSection(Composite parent, FlatViewUtility flatViewUtility, String title)
  {                            
    this(parent, title);

    WidgetFactory factory = new WidgetFactory();
    factory.setClientAreaColor(flatViewUtility.getBackgroundColor());

    Control section = createControl(this, factory);
    setContent(section);

    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.verticalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = true;
    setLayoutData(gridData);        
  }


  public ComponentsSection(Composite parent, String title)
  {
    super(parent);
    setCollapsable(true);
    setHeaderText(title);
    setAddSeparator(true);                                                   
  }         

  public CCombo getNamesCombo()
  {
    return namesCombo;
  }
  
  public Button getNewMessageButton()
  {
     return newButton;
  }

  public Button getImportMessageButton()
  {
     return importButton;
  }

  /*
   * @see FlatPageSection#createClient(Composite, WidgetFactory)
   */
  public Composite createClient(Composite parent, WidgetFactory factory)
  {
    FlatViewUtility flatViewUtility = new FlatViewUtility(true);

    Composite client = flatViewUtility.createComposite(parent, 1);

    namesCombo = flatViewUtility.createCComboBox(client);
                  
    Composite buttonComposite = flatViewUtility.createComposite(client, 3, true);
    GridLayout gridLayout = (GridLayout)buttonComposite.getLayout();
    gridLayout.marginWidth = 3;
    gridLayout.marginHeight = 3;
    gridLayout.makeColumnsEqualWidth = true;
    
    Label filler = flatViewUtility.createHorizontalFiller(buttonComposite, 1);
    GridData gd = (GridData)filler.getLayoutData();
    gd.grabExcessHorizontalSpace = true;
    newButton = flatViewUtility.createPushButton(buttonComposite, WSDLEditorPlugin.getWSDLString("_UI_BUTTON_NEW")); //$NON-NLS-1$
    importButton = flatViewUtility.createPushButton(buttonComposite, WSDLEditorPlugin.getWSDLString("_UI_BUTTON_IMPORT")); //$NON-NLS-1$

    return client;
  }  
}

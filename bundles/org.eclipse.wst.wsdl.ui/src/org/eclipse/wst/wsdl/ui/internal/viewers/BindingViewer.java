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
package org.eclipse.wst.wsdl.ui.internal.viewers;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.viewers.widgets.ComponentNameComboHelper;
import org.eclipse.wst.wsdl.ui.internal.viewers.widgets.InvokeDialogButton;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;

public class BindingViewer extends NamedComponentViewer 
{                      
  protected CCombo componentNameCombo;      
  protected ComponentNameComboHelper componentNameComboHelper;
  InvokeDialogButton button;

  public BindingViewer(Composite parent, IEditorPart editorPart)
  {
    super(parent, editorPart);    
  } 

  protected String getHeadingText()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_LABEL_BINDING");  //$NON-NLS-1$
  }

  protected Composite populatePrimaryDetailsSection(Composite parent)
  {
    Composite composite = super.populatePrimaryDetailsSection(parent);
                   
    Label portTypeLabel = flatViewUtility.createLabel(composite, 0, WSDLEditorPlugin.getWSDLString("_UI_LABEL_PORTTYPE")); //$NON-NLS-1$

    componentNameCombo = flatViewUtility.createCComboBox(composite);
    componentNameCombo.addListener(SWT.Modify, this); 
                                                                  

    componentNameComboHelper = new ComponentNameComboHelper(componentNameCombo)
    {
      protected List getComponentNameList(ComponentReferenceUtil util)
      {
        return util.getPortTypeNames();
      }

      protected String getAttributeName()
      {
        return WSDLConstants.TYPE_ATTRIBUTE;
      }
    };

    button = new InvokeDialogButton(composite, getInput());
    button.setEditor(editorPart);

    return composite;
  }  

  protected void update()
  {
    super.update();                                                     
    componentNameComboHelper.update(input);
    button.setInput(input);
  }

  protected void handleEventHelper(Element element, Event event)
  {                        
    super.handleEventHelper(element, event);
    componentNameComboHelper.handleEventHelper(element, event);
  }
}
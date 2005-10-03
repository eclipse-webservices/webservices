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

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.viewers.widgets.InvokeDialogButton;
import org.w3c.dom.Element;

public class PartViewer extends NamedComponentViewer
{   
  protected CCombo componentNameCombo;
  protected CCombo referenceKindCombo; 
  protected Label componentNameLabel;
  InvokeDialogButton button;
//  Button importButton;
  

  public PartViewer(Composite parent, IEditorPart editorPart)
  {
    super(parent, editorPart);    
  } 

  protected String getHeadingText()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_LABEL_PART");  //$NON-NLS-1$
  } 

  public boolean isObjectExtensible()
  {
    return false;
  }
    
  protected Composite populatePrimaryDetailsSection(Composite parent)
  {
    Composite composite = super.populatePrimaryDetailsSection(parent);
     
    flatViewUtility.createLabel(composite, WSDLEditorPlugin.getWSDLString("_UI_LABEL_REFERENCE_KIND")); //$NON-NLS-1$
    referenceKindCombo = flatViewUtility.createCComboBox(composite);
    referenceKindCombo.add("element");
    referenceKindCombo.add("type");
    referenceKindCombo.setText("type");
    referenceKindCombo.addListener(SWT.Modify, this);

    flatViewUtility.createLabel(composite, ""); 
    
    componentNameLabel = flatViewUtility.createLabel(composite, WSDLEditorPlugin.getWSDLString("_UI_LABEL_ELEMENT"));   //$NON-NLS-1$

    componentNameCombo = flatViewUtility.createCComboBox(composite);
    componentNameCombo.addListener(SWT.Modify, this);
    componentNameCombo.addSelectionListener(this);

    button = new InvokeDialogButton(composite, getInput());
    
    return composite;
  }  
          
  protected void update()
  {                    
    try
    {
      Part part = (Part)input;
      boolean isType = ComponentReferenceUtil.isType(part);
      String value = ComponentReferenceUtil.getPartComponentReference(part);

      // update the combo-box content
      //
      componentNameCombo.removeAll(); 
      List compList = ComponentReferenceUtil.getComponentNameList(part, isType);
      if (compList != null)
      {
        for (Iterator iterator =  compList.iterator(); iterator.hasNext();)
        {
          componentNameCombo.add((String)iterator.next());
        }
      } 
      
      if (isType)
      {                                                 
        referenceKindCombo.setText("type");
        componentNameLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_TYPE"));  //$NON-NLS-1$
        componentNameCombo.setText(value != null ? value : "");
        button.setReferenceKind("type");
      }
      else
      {
        referenceKindCombo.setText("element");
        componentNameLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_ELEMENT"));  //$NON-NLS-1$
        componentNameCombo.setText(value != null ? value : "");
        button.setReferenceKind("element");
      }                                                        
      button.setInput(input);
      button.setEditor(editorPart);
      super.update();  
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }       
      
     
  protected void handleEventHelper(Element element, Event event)
  {  
    super.handleEventHelper(element, event);
    if (event.type == SWT.Modify)
    {  
      if (event.widget == referenceKindCombo)
      {                        
        // TODO... I have no idea why we need to use a delayed runnable to get 
        // updating working properly .... this is something we need to revisit
        DelayedRenameRunnable runnable = new DelayedRenameRunnable((Part)input);
        Display.getCurrent().asyncExec(runnable);  
      } 
    }
  }      

  protected class DelayedRenameRunnable implements Runnable
  {                       
    Part part;

    DelayedRenameRunnable(Part part)
    {
      this.part = part;
    }

    public void run()                  
    {                                                               
      boolean isType = referenceKindCombo.getText().equals("type");
      ComponentReferenceUtil.setComponentReference(part, isType, null);    
    }
  }  

  public void doWidgetSelected(SelectionEvent e)
  {
    if (e.widget == componentNameCombo)
    {                                                      
      Part part = (Part)input;
      boolean isType = referenceKindCombo.getText().equals("type");
      ComponentReferenceUtil.setComponentReference(part, isType, componentNameCombo.getText());          
    }
  } 
}
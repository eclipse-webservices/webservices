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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.viewers.widgets.AttributesTable;
import org.w3c.dom.Node;

public class ExtensibilityElementViewer extends BaseViewer implements ModelAdapterListener
{             
  protected Composite control;   
  protected IEditorPart editorPart;   
  protected AttributesTable attributesTable;
  boolean isForTabbedProperties = false;
  
  public ExtensibilityElementViewer(Composite parent, IEditorPart editorPart)
  {
    super(getStatusLineManager(editorPart));    
    this.editorPart = editorPart;                  

    createControl(parent);   
  }
  
  public ExtensibilityElementViewer(Composite parent, IEditorPart editorPart, boolean isForTabbedProperties)
  {
    super(getStatusLineManager(editorPart));    
    this.editorPart = editorPart;                  
    this.isForTabbedProperties = isForTabbedProperties;
    if (isForTabbedProperties)
    {
      createControlForTabbedPropertySheet(parent);
    }
    else
    {
      createControl(parent);
    }
  } 

  protected String getHeadingText()
  { 
    String result = "";                            
    if (input != null)
    {
      Node node = ((WSDLElement)input).getElement();
      result = node.getNodeName();
      if (result == null || result.length() == 0)
      {
        result = WSDLEditorPlugin.getWSDLString("_UI_LABEL_UNKNOWN_EXTENSIBILITY_ELEMENT"); //$NON-NLS-1$ 
      }
    }
    return result;
  }         
  
  // TODO... move this method down to BaseViewer!
  public void doHandleEvent(Event event)
  {
  }

  public void createControlForTabbedPropertySheet(Composite parent)
  { 
    attributesTable = new AttributesTable(editorPart, parent);
  }  

  public void createControl(Composite parent)
  { 
    control = flatViewUtility.createComposite(parent, 1, true);   
	  control.setBackground (new Color(null,255,0,0));
	  GridData gd= new GridData();
	  gd.horizontalAlignment= GridData.FILL;
	  gd.grabExcessHorizontalSpace= true;
	  gd.verticalAlignment= GridData.FILL;
	  gd.grabExcessVerticalSpace= true;
	  
    control.setLayoutData(gd);


    GridLayout layout = (GridLayout)control.getLayout();
    layout.verticalSpacing = 0;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
                                      
    flatViewUtility.createFlatPageHeader(control, "");

    Composite c = flatViewUtility.createComposite(control, 1, true);
    GridData cGD= new GridData();
    cGD.horizontalAlignment= GridData.FILL;
    cGD.grabExcessHorizontalSpace= true;
    cGD.verticalAlignment= GridData.FILL;
    cGD.grabExcessVerticalSpace= true;
    
    c.setLayoutData(cGD);                                 
    attributesTable = new AttributesTable(editorPart, c);

    GridData attributesTableGD= new GridData();
    attributesTableGD.horizontalAlignment= GridData.FILL;
    attributesTableGD.grabExcessHorizontalSpace= true;
    attributesTableGD.verticalAlignment= GridData.FILL;
    attributesTableGD.grabExcessVerticalSpace= true;

    attributesTable.getControl().setLayoutData(attributesTableGD);
  }  

  public Control getControl()
  {
    return control;
  } 
   

  public void doSetInput(Object input)
  {
    if (!isForTabbedProperties)
    {
      flatViewUtility.updateFlatPageHeaderTitle(getHeadingText());
    }
    setListenerEnabled(false);                          
               
    attributesTable.setInput(((WSDLElement)input).getElement());
 
    setListenerEnabled(true);

    if (oldInput != null)
    {
      WSDLModelAdapterFactory.removeModelAdapterListener(oldInput, this);
    }
    WSDLModelAdapterFactory.addModelAdapterListener(input, this);

    oldInput = input;
  }

  public void propertyChanged(Object object, String property)
  {
    if (isListenerEnabled())
    {
      setListenerEnabled(false);
      update();
      setListenerEnabled(true);
    }
  }  

  protected void update()
  {                          
    Runnable runnable = new Runnable()
    { 
      public void run()
      {           
        if (!attributesTable.getControl().isDisposed())
        {
          attributesTable.refresh();
        }
      }
    };               
    Display.getCurrent().asyncExec(runnable);
  }
}
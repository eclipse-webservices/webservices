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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.SmartRenameAction;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;

public abstract class NamedComponentViewer extends BaseViewer implements ModelAdapterListener
{                                    
  protected Composite control;
  protected Text nameField;  
  protected IEditorPart editorPart;

  public NamedComponentViewer(Composite parent, IEditorPart editorPart)
  {
    super(getStatusLineManager(editorPart));    
    this.editorPart = editorPart;                  

    createControl(parent);
  } 

  protected abstract String getHeadingText();  

  protected Composite populatePrimaryDetailsSection(Composite parent)
  {
  	Composite group = flatViewUtility.createComposite(parent, 2, true);
 	
  	group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    Composite nameFieldComposite = flatViewUtility.createComposite(group, 2, true);
	((GridLayout)nameFieldComposite.getLayout()).marginWidth = 0;
    ((GridLayout)nameFieldComposite.getLayout()).marginHeight = 0; 	
	 
    GridLayout layout = new GridLayout();
    layout.makeColumnsEqualWidth  = false;
    layout.numColumns = 3;
    layout.marginHeight = 4;
    layout.marginWidth = 0;

    nameFieldComposite.setLayout(layout);      
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 1;
    gd.verticalAlignment = GridData.BEGINNING;
    nameFieldComposite.setLayoutData(gd);


    flatViewUtility.createLabel(nameFieldComposite, 0, WSDLEditorPlugin.getWSDLString("_UI_LABEL_NAME")); //$NON-NLS-1$
    nameField = flatViewUtility.createTextField(nameFieldComposite);  
    nameField.addListener(SWT.Modify, this);  
    
    // place holder
    Control c = flatViewUtility.createLabel(nameFieldComposite, 0, "");
    c.setBackground(new Color(null, 0, 255, 100));
    gd = new GridData();
    gd.grabExcessHorizontalSpace = false;
    gd.heightHint = 0;
    gd.widthHint = 0;
    c.setLayoutData(gd);
    	
    return nameFieldComposite;
  } 

  public void createControl(Composite parent)
  { 
	SashForm sashForm = new SashForm(parent, SWT.BORDER);
	sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
	control = sashForm;
	sashForm.setOrientation(SWT.HORIZONTAL);
	
    Composite composite = flatViewUtility.createComposite(sashForm, 1, true);        
	composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    GridLayout layout = (GridLayout)composite.getLayout();
    layout.verticalSpacing = 0;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
                       
    flatViewUtility.createFlatPageHeader(composite, getHeadingText());
       
    populatePrimaryDetailsSection(composite);
    
	TabbedViewer tabbedViewer = new TabbedViewer((WSDLEditor)editorPart);

	Control c = tabbedViewer.createControl(sashForm);
	c.setLayoutData(new GridData(GridData.FILL_BOTH));  	     	
  }
              
  public void doSetInput(Object input)
  {    
    setListenerEnabled(false);                          
    try
    {          
//      Node node = WSDLEditorUtil.getInstance().getNodeForObject(input);
      update();
    }
    finally
    {
      setListenerEnabled(true);
    }

    if (oldInput != null)
    {
      WSDLModelAdapterFactory.removeModelAdapterListener(oldInput, this);
    }

    WSDLModelAdapterFactory.addModelAdapterListener(input, this);
    oldInput = input;
  }

  protected void update()
  {
    Element element = ((WSDLElement)input).getElement();                          
    flatViewUtility.updateFlatPageHeaderTitle(getHeadingText());

    nameField.setText("");
    String nameValue = element.getAttribute(WSDLConstants.NAME_ATTRIBUTE);
    nameField.setText(nameValue != null ? nameValue : "");  
  }

  public Control getControl()
  {
    return control;
  }

  /*
   * @see BaseWindow#doHandleEvent(Event)
   */
  public void doHandleEvent(Event event)
  {
    setListenerEnabled(false);
    Element element = WSDLEditorUtil.getInstance().getElementForObject(input);
    handleEventHelper(element, event);   
    setListenerEnabled(true);
  }             

  protected void handleEventHelper(Element element, Event event)
  {                          
    if (event.type == SWT.Modify)
    {  
      if (event.widget == nameField)
      { 
        new SmartRenameAction(input, nameField.getText()).run();
      }
    }
  }

  public void propertyChanged(Object object, String property)
  {
    if (isListenerEnabled())
    {
      setListenerEnabled(false);
      if (!control.isDisposed())
      {      
        update();
      }  
      setListenerEnabled(true);
    }
  }
}
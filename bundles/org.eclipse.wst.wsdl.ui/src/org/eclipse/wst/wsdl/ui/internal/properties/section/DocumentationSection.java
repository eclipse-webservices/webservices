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
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class DocumentationSection extends AbstractSection {

	Text docText;

	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
	  super.createControls(parent, factory);
		composite =	getWidgetFactory().createFlatFormComposite(parent);
		
		docText = getWidgetFactory().createText(composite, "", SWT.MULTI | SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL); //$NON-NLS-1$
    docText.addListener(SWT.Modify, this);  
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		docText.setLayoutData(data);
	}

	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh()
	{
    super.refresh();
    if (docText.isFocusControl())
    {
      return;
    }
    setListenerEnabled(false);
	  docText.setText("");
	  if (getElement() != null)
	  {
	    Element docNode = getElement().getDocumentationElement();
	    if (docNode != null)
	    {
        Node textNode = docNode.getFirstChild();
        if (textNode != null)
        {
          String docValue = textNode.getNodeValue();
          if (docValue != null)
          {
            docText.setText(docValue);
          }
        }
	    }
	  }
	  setListenerEnabled(true);
	}
	
	public void doHandleEvent(Event event)
	{
	  if (event.widget == docText)
	  {
	    String value = docText.getText();
	    if (getElement() != null)
	    {
	      Element docNode = getElement().getDocumentationElement();
	      if (docNode != null)
	      {
          Node textNode = docNode.getFirstChild();
          if (textNode != null)
          {
            textNode.setNodeValue(value);
          }
          else
          {
            if (value.length() > 0)
            {
              Document document = docNode.getOwnerDocument();
              org.w3c.dom.Text newTextNode = document.createTextNode(value);
              docNode.appendChild(newTextNode);
            }
          }
	      }
	      else
	      {
	        Element element = getElement().getElement();
          AddElementAction action = new AddElementAction(element, element.getPrefix(), "documentation", element.getFirstChild()); //$NON-NLS-1$
          action.run();
          Element newDocumentation = action.getNewElement();
          
          Document document = newDocumentation.getOwnerDocument();
          org.w3c.dom.Text newTextNode = document.createTextNode(value);
          newDocumentation.appendChild(newTextNode);
          getElement().setDocumentationElement(newDocumentation);
	      }
	    }
	  }
	}
}
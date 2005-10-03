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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementAction;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.xsd.XSDComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TabbedViewer implements ISelectionChangedListener
{                                  
  protected WSDLEditor editor;
  protected CTabFolder tabFolder;
  protected Text documentationField;
  protected DocumentationListener documentationListener;
  protected boolean listenerEnabled;

  public TabbedViewer(WSDLEditor editor)
  {
    this.editor = editor;                            
  }     

  public Control createControl(Composite parent)
  { 
    tabFolder = new CTabFolder(parent, 0);

    CTabItem tab = new CTabItem(tabFolder, SWT.NONE);
    tab.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_DOCUMENTION")); //$NON-NLS-1$
    documentationField = new Text(tabFolder, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
    tab.setControl(documentationField);
    tabFolder.setSelection(tab);
    
    documentationListener = new DocumentationListener();
    listenerEnabled = true;
    documentationField.addModifyListener(documentationListener);
    
    editor.getSelectionManager().addSelectionChangedListener(this);

    return tabFolder;
  }  

  public void setInput(Object object)
  {
    listenerEnabled = false;
    documentationListener.setInput(object);
    documentationField.setEnabled(true);
    documentationField.setText("");
    
    if (object instanceof XSDComponent || !(object instanceof WSDLElement))
    {
      documentationField.setEnabled(false);
      listenerEnabled = true;
      return;
    }

    Element element = WSDLEditorUtil.getInstance().getElementForObject(object);
    if (element != null)
    {
      if (element.getLocalName().equals("documentation"))
      {
        documentationField.setEnabled(false);
        listenerEnabled = true;
        return;
      }

      Node docNode = getChildNode(element, element.getPrefix(), "documentation");
      if (docNode != null)
      {
        Node textNode = docNode.getFirstChild();
        if (textNode != null)
        {                            
          String textNodeValue = textNode.getNodeValue();
          documentationField.setText(textNodeValue != null ? textNodeValue : "");          
        }
      }
    }
    listenerEnabled = true;
// There appears to be a defect in the model...it doesn't return
// the documentationElement even though it exists
//    if (object instanceof WSDLElement)
//    {
//      WSDLElement elem = (WSDLElement)object;
//      System.out.println("elem = " + elem);
//
//      if (elem != null)
//      {
//        Element doc = elem.getDocumentationElement();
//        System.out.println("docNode = " + doc);
//        if (doc != null)
//        {
//          Node textNode = doc.getFirstChild();
//          if (textNode != null)
//          {
//            documentationField.setText(textNode.getNodeValue());
//          }
//        }
//      }
//    }
  }
   
  public void selectionChanged(SelectionChangedEvent event)  
  {                                 
    ISelection selection = event.getSelection();
    if (selection instanceof IStructuredSelection)
    {
      Object o = ((IStructuredSelection)selection).getFirstElement();
      if (o != null)
      {
        setInput(o); 
      }
    }
  }   

  public Node getChildNode(Element parent, String prefix, String childName)
  {
    NodeList list = null;
    if (parent != null)
    {
      list = parent.getChildNodes();
    }
    String targetName;
    if (prefix != null && prefix.length() > 0)
    {
      targetName = prefix + ":" + childName;
    }
    else
    {
      targetName = childName;
    }  
   
    if (list != null)
    {
      // Performance issue perhaps?
      for (int i = 0; i < list.getLength(); i++)
      {
        if (list.item(i) instanceof Element)
        {
          if (list.item(i).getNodeName().equals(targetName))
          {
            return list.item(i);
          }
        }
      }
    }
    return null;
  }



  class DocumentationListener implements ModifyListener
  {
    Object object;
    
    public void setInput(Object object)
    {
      this.object = object;
    }

    /**
     * @see org.eclipse.swt.events.ModifyListener#modifyText(ModifyEvent)
     */
    public void modifyText(ModifyEvent e)
    {
      if (listenerEnabled)
      {
        Element element = WSDLEditorUtil.getInstance().getElementForObject(object);
        if (element != null)
        {
          Node docNode = getChildNode(element, element.getPrefix(), "documentation");
          if (docNode != null)
          {
            Node textNode = docNode.getFirstChild();
            if (textNode != null)
            {
              textNode.setNodeValue(documentationField.getText());
            }
            else
            {
              if (documentationField.getText() != null && documentationField.getText().length() > 0)
              {
                Document document = docNode.getOwnerDocument();
                org.w3c.dom.Text newTextNode = document.createTextNode(documentationField.getText());
                docNode.appendChild(newTextNode);
              }
            }
          }
          else
          {
            AddElementAction action = new AddElementAction(element, element.getPrefix(), "documentation", element.getFirstChild());
            action.run();
            Element newDocumentation = action.getNewElement();
            
            Document document = newDocumentation.getOwnerDocument();
            org.w3c.dom.Text newTextNode = document.createTextNode(documentationField.getText());
            newDocumentation.appendChild(newTextNode);
          }
        }
      }
    }
  }
}
                      
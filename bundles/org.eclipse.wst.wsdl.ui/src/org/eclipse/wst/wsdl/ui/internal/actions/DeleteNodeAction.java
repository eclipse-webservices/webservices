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
package org.eclipse.wst.wsdl.ui.internal.actions; 
                      
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;


public class DeleteNodeAction extends BaseNodeAction
{ 
  protected List list;

  public DeleteNodeAction(Node node)
  {
    setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE"));  //$NON-NLS-1$
    list = new Vector();
    list.add(node);
  }    
  
  public DeleteNodeAction(List nodeList)
  {
    setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE"));  //$NON-NLS-1$
    list = new Vector();
    Iterator it = nodeList.iterator();
    while (it.hasNext()) {
    	list.add(it.next());
    }
  }

  public void run()
  {
    beginRecording();

    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      Node node = (Node)i.next();
      if (node.getNodeType() == Node.ATTRIBUTE_NODE)
      {
        Attr attr = (Attr)node;
        attr.getOwnerElement().removeAttributeNode(attr);
      }
      else
      {                
        Node parent = node.getParentNode();
        if (parent != null)
        {                                         
          Node previousSibling = node.getPreviousSibling();
          if (previousSibling != null && isWhitespaceTextNode(previousSibling))
          {
            parent.removeChild(previousSibling);
          }
          parent.removeChild(node);
        }
      }
    }         

    endRecording();
  }   

  protected boolean isWhitespaceTextNode(Node node) 
  {
	  return (node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getNodeValue().trim().length() == 0);
  }    

  public Node getNode()
  {
    return list.size() > 0 ? (Node)list.get(0) : null;
  } 

  public String getUndoDescription()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE"); //$NON-NLS-1$
  }
}
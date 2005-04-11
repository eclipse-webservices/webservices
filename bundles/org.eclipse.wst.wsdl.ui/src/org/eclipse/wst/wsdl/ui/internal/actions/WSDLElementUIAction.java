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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.ui.IEditorPart;


import org.w3c.dom.Node;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.commands.WSDLElementCommand;
import org.eclipse.wst.xml.core.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;

public abstract class WSDLElementUIAction extends Action
{
  protected WSDLElementCommand modelAction;
  private String undoDescription;
  private IEditorPart editorPart;
  
  public WSDLElementUIAction
  	(WSDLElementCommand action,
  	 String undoDescription,
  	 String label,
  	 ImageDescriptor image)
  {
    super(label,image);
    modelAction = action;
    this.undoDescription = undoDescription;
  }

  public void setEditorPart(IEditorPart editorPart)
  {
    this.editorPart = editorPart;
  }
  
  private String getUndoDescription()
  {
    return undoDescription;
  }

  private Node getOwnerNode()
  {
    if (getOwner() != null)
      return getOwner().getElement();
    else
      return null;
  }

  abstract protected WSDLElement getOwner();
  
  abstract protected boolean showDialog();
  
  public void run()
  {
    boolean ok = showDialog();
    if (ok)
    {
	    preRun();
	    modelAction.run();
	    format();
	    selectObject();
	    postRun();
    }
  }
  
  protected void preRun()
  {
  }
  
  protected void postRun()
  {
  }

  private void beginRecording()
  {    
    Node node = getOwnerNode();  
    if (node instanceof IDOMNode)
    {
      ((IDOMNode)node).getModel().beginRecording(this, getUndoDescription());  
    }
  }

  private void endRecording()
  {                 
    Node node = getOwnerNode(); 
    if (node instanceof IDOMNode)
    {
      ((IDOMNode)node).getModel().endRecording(this);  
    }
  }
  
  protected WSDLElement getWSDLElement()
  {
    return modelAction.getWSDLElement();
  }
  
  private void format()
  {
    Node parentNode = getOwnerNode();
    if (parentNode instanceof IDOMNode) 
    {
		  // format selected node                                                    
      FormatProcessorXML formatProcessorXML = new FormatProcessorXML();
      formatProcessorXML.formatNode((IDOMNode)parentNode);      
    }
  }
  
  private void selectObject()
  {               
    if (editorPart != null && getOwner() != null)
    {
      Object object = getWSDLElement();
      if (object != null)
      {
        ISelectionProvider selectionProvider = (ISelectionProvider)editorPart.getAdapter(ISelectionProvider.class);
        if (selectionProvider != null)
        {
          selectionProvider.setSelection(new StructuredSelection(object));
        }
      }  
    }   
  }
}

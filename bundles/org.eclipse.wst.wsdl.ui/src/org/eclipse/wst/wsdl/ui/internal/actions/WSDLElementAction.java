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
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.widgets.NewComponentDialog;
import org.eclipse.wst.xml.core.document.XMLNode;
import org.eclipse.wst.xml.core.format.FormatProcessorXML;

import org.eclipse.jface.dialogs.IDialogConstants;

public abstract class WSDLElementAction extends Action
{
  protected WSDLElementCommand modelCommand;
  private String undoDescription;
  private IEditorPart editorPart;
  
  public WSDLElementAction
  	(WSDLElementCommand command,
  	 String undoDescription,
  	 String label,
  	 ImageDescriptor image)
  {
    super(label,image);
    modelCommand = command;
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

  abstract protected WSDLElement getOwner();
  
  abstract protected boolean showDialog();
  
  public void run()
  {
    boolean ok = showDialog();
    if (ok)
    {
	    preRun();
	    modelCommand.run();
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
    Node node = null;
    if (getOwner() != null)
      node = getOwner().getElement(); 
    
    if (node instanceof XMLNode)
    {
      ((XMLNode)node).getModel().beginRecording(this, getUndoDescription());  
    }
  }

  private void endRecording()
  {                 
    Node node = null;
    if (getOwner() != null)
      node = getOwner().getElement(); 
    
    if (node instanceof XMLNode)
    {
      ((XMLNode)node).getModel().endRecording(this);  
    }
  }
  
  protected WSDLElement getWSDLElement()
  {
    return modelCommand.getWSDLElement();
  }
  
  private void format()
  {
    Node parentNode = null;
    if (getOwner() != null)
      parentNode = getOwner().getElement(); 
    
    if (parentNode instanceof XMLNode) 
    {
		  // format selected node                                                    
      FormatProcessorXML formatProcessorXML = new FormatProcessorXML();
      formatProcessorXML.formatNode((XMLNode)parentNode);      
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
  
  protected String showDialogHelper(String title, String defaultName)
  {   
    String result = defaultName;                                                                                             
    NewComponentDialog dialog = new NewComponentDialog(WSDLEditorPlugin.getShell(), title, defaultName);
    int rc = dialog.createAndOpen();
    if (rc == IDialogConstants.OK_ID)
    {
      result = dialog.getName();  
    }
    else
    {
      result = null;
    }               
    return result;
  }
}

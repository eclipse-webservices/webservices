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
package org.eclipse.wst.wsdl.ui.internal.xsd.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLGraphViewer;
import org.eclipse.xsd.XSDSchema;

import org.eclipse.wst.xsd.ui.internal.gef.util.editparts.AbstractComponentViewerRootEditPart;

public class BackAction extends Action
{
  ISelectionProvider selectionProvider;
  XSDSchema xsdSchema;
  Definition definition;
  WSDLGraphViewer graphViewer;
  
  /**
   * 
   */
  public BackAction()
  {
    super();
  }

  /**
   * @param text
   */
  public BackAction(String text)
  {
    super(text);
  }

  /**
   * @param text
   * @param image
   */
  public BackAction(String text, ImageDescriptor image)
  {
    super(text, image);
  }

  /**
   * @param text
   * @param style
   */
  public BackAction(String text, int style)
  {
    super(text, style);
  }

  public void setSelectionProvider(ISelectionProvider selectionProvider)
  {
    this.selectionProvider = selectionProvider;
  }
  
  AbstractComponentViewerRootEditPart editPart;
  public void setRootEditPart(AbstractComponentViewerRootEditPart editPart)
  {
    this.editPart = editPart;
  }

  public void setDefinition(Definition definition)
  {
    this.definition = definition;
  }
  
  public void setXSDSchema(XSDSchema xsdSchema)
  {
    this.xsdSchema = xsdSchema;
  }
  
  public void setGraphViewer(WSDLGraphViewer graphViewer)
  {
    this.graphViewer = graphViewer;
  }
  
  /*
   * @see IAction#run()
   */
  public void run()
  {
    if (xsdSchema != null)
    {
      StructuredSelection selection = new StructuredSelection(xsdSchema);
      selectionProvider.setSelection(selection);
      // editPart.setInput(xsdSchema);
      graphViewer.setInput(xsdSchema);
    }
    else if (definition != null)
    {
      StructuredSelection selection = new StructuredSelection(definition);
      selectionProvider.setSelection(selection);
      // editPart.setInput(definition);
      graphViewer.setInput(definition);
    }
  }

}

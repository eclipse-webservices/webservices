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

import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.util.InvokeSetDialog;
import org.w3c.dom.Node;



public class AddNewComponentAction extends AddElementAction
{
  Object input;
  protected IEditorPart editorPart;
  protected String kind;

  /**
   * Constructor for AddNewComponentAction.
   * @param text
   * @param imageDescriptorKey
   * @param parentNode
   * @param nodeName
   */
  public AddNewComponentAction(
    String text,
    String imageDescriptorKey,
    Node parentNode,
    String nodeName,
    Object input)
  {
    super(text, imageDescriptorKey, parentNode, nodeName);
    this.input = input;
  }
  /**
   * Constructor for AddNewComponentAction.
   * @param text
   * @param imageDescriptorKey
   * @param parentNode
   * @param prefix
   * @param localName
   */
  public AddNewComponentAction(
    String text,
    String imageDescriptorKey,
    Node parentNode,
    String prefix,
    String localName,
    Object input)
  {
    super(text, imageDescriptorKey, parentNode, prefix, localName);
    this.input = input;
  }

  public AddNewComponentAction(
    String text,
    Node parentNode,
    String prefix,
    String localName,
    Object input)
  {
    super(text, parentNode, prefix, localName);
    this.input = input;
  }

  public void setEditor(IEditorPart editorPart)
  {
    this.editorPart = editorPart; 
  }

  public void setReferenceKind(String kind)
  {
    this.kind = kind;
  }

  protected void performAddElement()
  {
    InvokeSetDialog dialog = new InvokeSetDialog();
    if (input instanceof Part)
    {
      dialog.setReferenceKind(kind);
    }
    dialog.run(input, editorPart);
  }
}

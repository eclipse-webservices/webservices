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

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.dialogs.InvokeSetDialog;
import org.w3c.dom.Node;



public class SetExistingComponentAction extends AddElementAction
{
  Object input;
  protected IEditorPart editorPart;
  protected String kind;

  public SetExistingComponentAction(
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
    
    // We shouldn't know about the editor in this class
    WSDLEditor editor = (WSDLEditor) editorPart;
    editor.getSelectionManager().setSelection(new StructuredSelection(input));
  }
}

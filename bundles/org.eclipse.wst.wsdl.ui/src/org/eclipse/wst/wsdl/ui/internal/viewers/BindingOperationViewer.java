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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class BindingOperationViewer extends NamedComponentViewer 
{                    
  public BindingOperationViewer(Composite parent, IEditorPart editorPart)
  {
    super(parent, editorPart);    
  } 

  protected String getHeadingText()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_LABEL_BINDING_OPERATION"); //$NON-NLS-1$
  }
}
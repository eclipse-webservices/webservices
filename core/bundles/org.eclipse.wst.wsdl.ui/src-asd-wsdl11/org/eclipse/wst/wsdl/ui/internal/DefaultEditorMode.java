/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.ASDEditPartFactory;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ASDContentOutlineProvider;
import org.eclipse.wst.xsd.ui.internal.adt.editor.EditorMode;

public class DefaultEditorMode extends EditorMode
{
  public final static String ID =  DefaultEditorMode.class.getName();
  
  public String getDisplayName()
  {
    return Messages._UI_LABEL_DEFAULT;
  }

  public EditPartFactory getEditPartFactory()
  {
    return new ASDEditPartFactory();
  }

  public String getId()
  {
    return ID;
  }

  public IContentProvider getOutlineProvider()
  {
    return new ASDContentOutlineProvider();
  } 
   
  public Object getAdapter(Class adapter)
  {
    return super.getAdapter(adapter);
  }
}

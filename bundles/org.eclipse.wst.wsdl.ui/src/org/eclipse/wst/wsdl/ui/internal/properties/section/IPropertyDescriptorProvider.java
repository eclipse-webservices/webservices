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

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.w3c.dom.Element;

public interface IPropertyDescriptorProvider 
{
  //public void init(IEditorPart editor);  
  public IPropertyDescriptor getPropertyDescriptor(IEditorPart editor, Element ownerElement, String attributeNamespace, String attributeName);
}

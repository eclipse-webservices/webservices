/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.visitor;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;

public abstract class BaseRenamer extends WSDLVisitor
{
  protected String newName;
  protected WSDLElement globalComponent;
  
  public BaseRenamer(WSDLElement globalComponent, String newName)
  {
    super(globalComponent.getEnclosingDefinition());
    this.globalComponent = globalComponent;
    this.newName = newName;
  }
  
  public String getNewQName()
  {
    String qName = null;
    if (newName != null)
    {
      Definition definition = globalComponent.getEnclosingDefinition();
      qName = definition.getPrefix(definition.getTargetNamespace());
      qName += ":" + newName; //$NON-NLS-1$
    }
    else
    {
      qName = newName;
    }
    
    return qName;
  }
}

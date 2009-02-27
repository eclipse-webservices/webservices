/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.filter;


import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public abstract class AbstractExtensibilityElementFilter implements ExtensiblityElementFilter
{

  public AbstractExtensibilityElementFilter()
  {
  }

  protected boolean isWSDLBindingOperation(Node node)
  {
    if (! (node instanceof Element))
    {
      return false;
    }
    Node parentNode = node.getParentNode();
    return parentNode != null && WSDLConstants.OPERATION_ELEMENT_TAG.equals(node.getLocalName())
      && WSDLConstants.BINDING_ELEMENT_TAG.equals(parentNode.getLocalName());
  }
}

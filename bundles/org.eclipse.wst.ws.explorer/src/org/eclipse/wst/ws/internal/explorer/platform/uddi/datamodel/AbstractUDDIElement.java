/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel;

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;

public abstract class AbstractUDDIElement extends TreeElement
{
  public AbstractUDDIElement(String name, Model model)
  {
    super(name, model);
  }

  public final TreeElement getParentElement()
  {
    return getParentElement(this);
  }

  public final TreeElement getParentElement(TreeElement child)
  {
    Enumeration e = child.getElements(ModelConstants.REL_OWNER);
    if (e.hasMoreElements())
      return (TreeElement)e.nextElement();
    else
      return null;
  }

  public final RegistryElement getRegistryElement()
  {
    if (this instanceof RegistryElement)
      return (RegistryElement)this;
    else
    {
      TreeElement childElement = this;
      TreeElement parentElement = getParentElement(childElement);
      while (parentElement != null && parentElement != childElement)
      {
        if (parentElement instanceof RegistryElement)
          return (RegistryElement)parentElement;
        childElement = parentElement;
        parentElement = getParentElement(childElement);
      }
      return null;
    }
  }
}

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

package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class EJBSelectionTransformer implements Transformer
{

  public Object transform(Object value)
  {
    if (value instanceof IStructuredSelection)
    {
      Object sel = ((IStructuredSelection)value).getFirstElement();
      if (sel instanceof EnterpriseBean)
      {
        return new StructuredSelection(((EnterpriseBean)sel).getName());
      }
    }
    return value;
  }
  

}

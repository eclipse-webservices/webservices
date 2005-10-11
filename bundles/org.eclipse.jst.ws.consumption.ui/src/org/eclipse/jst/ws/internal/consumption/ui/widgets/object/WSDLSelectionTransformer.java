/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.net.MalformedURLException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsclient.internal.impl.ServiceRefImpl;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.wst.command.internal.provisional.env.core.data.Transformer;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

public class WSDLSelectionTransformer implements Transformer
{
  public Object transform(Object value)
  {
    if (value instanceof IStructuredSelection)
    {
      Object sel = ((IStructuredSelection)value).getFirstElement();
      if (sel instanceof IResource)
      {
        try
        {
          return new StructuredSelection(((IResource)sel).getLocation().toFile().toURL().toString());
        }
        catch (MalformedURLException murle)
        {
        }
      }
      else if (sel instanceof ServiceImpl)
      {
        return new StructuredSelection(J2EEActionAdapterFactory.getWSDLURI((ServiceImpl)sel));
      }
      else if (sel instanceof ServiceRefImpl)
      {
        return new StructuredSelection(J2EEActionAdapterFactory.getWSDLURI((ServiceRefImpl)sel));
      }
      else if (sel instanceof WSDLResourceImpl)
      {
        return new StructuredSelection(J2EEActionAdapterFactory.getWSDLURI((WSDLResourceImpl)sel));
      }
    }
    return value;
  }
}
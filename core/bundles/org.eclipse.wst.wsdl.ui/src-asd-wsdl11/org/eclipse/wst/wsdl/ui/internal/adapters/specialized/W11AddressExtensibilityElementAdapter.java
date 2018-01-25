/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.specialized;

import org.eclipse.wst.wsdl.binding.http.HTTPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;

public class W11AddressExtensibilityElementAdapter extends W11ExtensibilityElementAdapter
{
  // TODO... make this class abstract and provide concrete implementations in
  // SOAP and HTTP packages. The WSDLAdapterFactory needs to know to delegate to extension
  // adapters to support the addition of more extension languages
  //
  public String getLocationURI()
  {
    String address = ""; //$NON-NLS-1$
    if (target instanceof SOAPAddress)
    {
      address = ((SOAPAddress) target).getLocationURI();
    }
    else if (target instanceof HTTPAddress)
    {
      address = ((HTTPAddress) target).getLocationURI();
    }
    return address;
  }

  public void setLocationURI(String address)
  {
    if (target instanceof SOAPAddress)
    {
      ((SOAPAddress) target).setLocationURI(address);
    }
    else if (target instanceof HTTPAddress)
    {
      ((HTTPAddress) target).setLocationURI(address);
    }
  }
}

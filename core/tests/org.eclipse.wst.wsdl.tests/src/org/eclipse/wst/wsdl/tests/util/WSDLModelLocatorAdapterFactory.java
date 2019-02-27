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
package org.eclipse.wst.wsdl.tests.util;


import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.wsdl.internal.util.WSDLModelLocator;


public class WSDLModelLocatorAdapterFactory extends AdapterFactoryImpl
{
  protected CustomWSDLModelLocator customWSDLModelLocator = new CustomWSDLModelLocator();

  class CustomWSDLModelLocator extends AdapterImpl implements WSDLModelLocator
  {
    public String resolveURI(String baseLocation, String namespace, String location)
    {
      return URIResolverPlugin.createResolver().resolve(baseLocation, namespace, location);
    }

    public boolean isAdatperForType(Object type)
    {
      return type == WSDLModelLocator.class;
    }
  }

  public boolean isFactoryForType(Object type)
  {
    return type == WSDLModelLocator.class;
  }

  public Adapter adaptNew(Notifier target, Object type)
  {
    return customWSDLModelLocator;
  }
}

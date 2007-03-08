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
package org.eclipse.wst.wsdl.tests.util;


import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;


public class XSDSchemaLocationResolverImpl extends AdapterImpl implements XSDSchemaLocationResolver
{
  public String resolveSchemaLocation(XSDSchema xsdSchema, String namespaceURI, String schemaLocationURI)
  {
    String baseLocation = xsdSchema.getSchemaLocation();
    return URIResolverPlugin.createResolver().resolve(baseLocation, namespaceURI, schemaLocationURI);
  }

  public boolean isAdatperForType(Object type)
  {
    return type == XSDSchemaLocator.class;
  }
}

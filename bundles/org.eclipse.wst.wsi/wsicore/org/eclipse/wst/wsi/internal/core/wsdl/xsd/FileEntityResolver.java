/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.wsdl.xsd;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * Entity resolve to resolve file entities.
 * 
 * @author Lawrence Mandel, IBM
 */
public class FileEntityResolver implements XMLEntityResolver
{

  /**
   * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
   */
  public XMLInputSource resolveEntity(XMLResourceIdentifier resource) throws XNIException, IOException
  {
    String publicId = resource.getPublicId();
    String systemId = resource.getExpandedSystemId();
    String namespace = resource.getNamespace();
    URL url = null;
    if(systemId != null)
    {
      url = new URL(systemId);
    }
    else if(publicId != null)
    {
      url = new URL(publicId);
    }
    else if(namespace != null)
    {
      url = new URL(namespace);
    }
    if(url != null)
    {
      InputStream is = url.openStream();
      return new XMLInputSource(publicId, resource.getExpandedSystemId(), resource.getExpandedSystemId(), is, null);
    }
    return null;
  }

}

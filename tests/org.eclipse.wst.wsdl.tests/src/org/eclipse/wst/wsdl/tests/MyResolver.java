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
package org.eclipse.wst.wsdl.tests;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class MyResolver implements EntityResolver
{
  public InputSource resolveEntity(String publicId, String systemId)
  {
    if (systemId.equals("http://www.myhost.com/today"))
    {
      // return a special input source
      //MyReader reader = new MyReader();
      //return new InputSource(reader);
      return new InputSource(systemId); // TBD
    }
    else
    {
      // use the default behaviour
      return null;
    }
  }
}

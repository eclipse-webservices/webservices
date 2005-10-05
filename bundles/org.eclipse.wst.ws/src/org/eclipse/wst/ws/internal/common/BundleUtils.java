/***************************************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.ws.internal.common;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class BundleUtils
{
  static public URL getURLFromBundle( String bundleId, String path ) throws MalformedURLException
  {
    Bundle      bundle     = Platform.getBundle( bundleId );
    URL         installURL = bundle.getEntry("/");
    URL         fileURL    = new URL(installURL, path );
    
    return fileURL;
  }
}

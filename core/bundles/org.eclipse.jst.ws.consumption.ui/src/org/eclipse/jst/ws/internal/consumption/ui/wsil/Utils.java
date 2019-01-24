/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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

package org.eclipse.jst.ws.internal.consumption.ui.wsil;

import java.net.MalformedURLException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.common.ResourceUtils;


public class Utils
{
  public Utils()
  {
  }

  public String toFileSystemURI(String relativePlatformURI)
  {
    if (relativePlatformURI != null)
      return toFileSystemURI(ResourceUtils.findResource(relativePlatformURI));
    else
      return null;
  }

  public String toFileSystemURI(IResource res)
  {
    if (res != null)
    {
      IPath path = res.getLocation();
      if (path != null)
      {
        try
        {
          return path.toFile().toURL().toString();
        }
        catch (MalformedURLException murle)
        {
        }
      }
    }
    return null;
  }
}

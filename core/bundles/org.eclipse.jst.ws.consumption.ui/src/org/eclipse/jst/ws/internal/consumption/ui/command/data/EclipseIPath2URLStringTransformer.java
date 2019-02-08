/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.jst.ws.internal.consumption.ui.command.data;

import java.net.MalformedURLException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.command.internal.env.core.data.Transformer;


public class EclipseIPath2URLStringTransformer implements Transformer
{
  public Object transform(Object value)
  {
    String s = value.toString();
    if (s.indexOf(":") < 0)
    {
      IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(value.toString());
      if (res.exists())
      {
        try
        {
          s = res.getLocation().toFile().toURL().toString();
        }
        catch (MalformedURLException murle)
        {
        }
      }
    }
    return s;
  }
}

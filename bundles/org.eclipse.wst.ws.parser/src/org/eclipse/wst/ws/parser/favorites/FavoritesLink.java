/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.parser.favorites;

import org.apache.wsil.Link;

public class FavoritesLink
{
  protected Link link_;

  public FavoritesLink()
  {
    link_ = null;
  }

  public Link getLink()
  {
    return link_;
  }

  public void setLink(Link link)
  {
    link_ = link;
  }
}

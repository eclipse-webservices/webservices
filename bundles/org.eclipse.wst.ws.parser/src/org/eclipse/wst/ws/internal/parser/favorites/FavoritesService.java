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

package org.eclipse.wst.ws.internal.parser.favorites;

import org.apache.wsil.Service;

public class FavoritesService
{
  protected Service service_;

  public FavoritesService()
  {
    service_ = null;
  }

  public Service getService()
  {
    return service_;
  }

  public void setService(Service service)
  {
    service_ = service;
  }
}

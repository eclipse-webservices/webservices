/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.parser.favorites;

public class FavoritesWSDL extends FavoritesService implements IFavoritesWSDL
{
  public FavoritesWSDL()
  {
    super();
  }

  public String getName()
  {
    return getWsdlUrl();
  }

  public String getWsdlUrl()
  {
    return (service_.getDescriptions())[0].getLocation();
  }

  public void setName(String name)
  {
  }

  public void setWsdlUrl(String wsdlURL)
  {
  }
}

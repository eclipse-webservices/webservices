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

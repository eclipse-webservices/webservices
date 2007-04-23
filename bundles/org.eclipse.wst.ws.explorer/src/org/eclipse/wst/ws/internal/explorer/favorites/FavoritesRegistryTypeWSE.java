/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.favorites;

import org.apache.wsil.WSILDocument;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesRegistryTypeAbstract;


public class FavoritesRegistryTypeWSE extends FavoritesRegistryTypeAbstract
{
  private static WSILDocument wsilDoc_;
  public static final String FAVORITES_WSEXPLORER = "favorites.wsil";
  private String defaultFavorites_ = null;
  private String stateLocation_ = null;
  
  public FavoritesRegistryTypeWSE()
  {
    super();
    wsilDoc_ = null;
  }
  
  public FavoritesRegistryTypeWSE(String defaultFavorites, String stateLocation) {
	super();
	wsilDoc_ = null;
	defaultFavorites_ = defaultFavorites;
	stateLocation_ = stateLocation;
  }

  public String getReadLocation()
  {
    StringBuffer readLocation = new StringBuffer();
    readLocation.append(stateLocation_ == null ? ExplorerPlugin.getInstance().getPluginStateLocation() : stateLocation_);
    readLocation.append(FAVORITES_WSEXPLORER);
    return readLocation.toString();
  }

  public String getWriteLocation()
  {
    return getReadLocation();
  }

  protected WSILDocument getWSILDocument()
  {
    if (wsilDoc_ == null)
    {
      wsilDoc_ = loadWSILDocument(getReadLocation(), false);
      if (wsilDoc_ == null)
      {
        restoreFavoritesDefault();
        try
        {
          save();
        }
        catch (Throwable t)
        {
        }
      }
    }
    return wsilDoc_;
  }

  public void restoreFavoritesDefault()
  {
    wsilDoc_ = (new FavoritesRegistryTypeDefault(defaultFavorites_)).getWSILDocument();
  }
}

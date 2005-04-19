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

package org.eclipse.wst.ws.internal.explorer.favorites;

import java.io.IOException;

import org.apache.wsil.WSILDocument;
import org.apache.wsil.WSILException;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesRegistryTypeAbstract;
import org.eclipse.wst.ws.internal.parser.favorites.IFavoritesUDDIRegistry;

public class FavoritesRegistryTypeDefault extends FavoritesRegistryTypeAbstract {
	private static WSILDocument wsilDoc_;

	public static final String FAVORITES_DEFAULT = "properties/favorites_default.wsil";
	
	private String defaultFavorites_ = null;

	public FavoritesRegistryTypeDefault() {
		super();
		wsilDoc_ = null;
	}

	public FavoritesRegistryTypeDefault(String defaultFavorites) {
		super();
		wsilDoc_ = null;
		defaultFavorites_ = defaultFavorites;
	}

	public String getReadLocation() {
		StringBuffer readLocation = new StringBuffer();
		if (defaultFavorites_ == null)
		{
			readLocation.append(ExplorerPlugin.getInstance().getPluginInstallLocation());
			readLocation.append(FAVORITES_DEFAULT);
		}
		else
		{
			readLocation.append(defaultFavorites_);
		}
		return readLocation.toString();
	}

	public String getWriteLocation() {
		return null;
	}

	protected WSILDocument getWSILDocument() {
		if (wsilDoc_ == null) {
			wsilDoc_ = loadWSILDocument(getReadLocation(), true);
			IFavoritesUDDIRegistry[] registries = getFavoritesUDDIRegistries();
			/* TODO: Public registry strings shouldn't be in properties - Ain't extensible.
			for (int i = 0; i < registries.length; i++) {
				registries[i].setName(WSPlugin.getResourceString(registries[i].getName()));
			}
			*/
		}
		return wsilDoc_;
	}

	public WSILDocument getFavoritesDefault() {
		return getWSILDocument();
	}

	public void save() throws WSILException, IOException {
	}
}

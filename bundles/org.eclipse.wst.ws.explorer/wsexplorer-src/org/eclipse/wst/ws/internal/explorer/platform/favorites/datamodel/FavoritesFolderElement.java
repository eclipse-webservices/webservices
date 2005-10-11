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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel;

import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

/**
* The data model element that represents 
* a WSIL document
*/
public abstract class FavoritesFolderElement extends TreeElement
{
    protected NodeManager nodeManager_;
    
    public FavoritesFolderElement(String name, Model model, NodeManager nodeManager) {
        super(name, model);
        nodeManager_ = nodeManager;
    }

    protected FavoritesMainElement getFavoritesMainElement() {
        return (FavoritesMainElement)nodeManager_.getRootNode().getTreeElement();
    }

    public abstract void init(FavoritesMainElement favMainElement);

    public abstract boolean removeFavoriteByNodeID(int nodeID,String pluginMetadataDirectory);

    public abstract boolean removeAllFavorites(String pluginMetadataDirectory);

    public abstract boolean addFavorite(Hashtable table);

    public abstract boolean favoriteExists(Hashtable table);

    public abstract Enumeration getAllFavorites();

}

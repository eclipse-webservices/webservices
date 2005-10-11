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
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;

/**
* The data model element that represents 
* a WSIL document
*/
public class FavoritesElement extends TreeElement {

  public FavoritesElement(String name, Model model) {
    super(name, model);
  }

  public FavoritesFolderElement getParentFolderElement() {
    Enumeration e = getElements(ModelConstants.REL_OWNER);
    if (!e.hasMoreElements())
      return null;
    else
      return (FavoritesFolderElement)e.nextElement();
  }
}

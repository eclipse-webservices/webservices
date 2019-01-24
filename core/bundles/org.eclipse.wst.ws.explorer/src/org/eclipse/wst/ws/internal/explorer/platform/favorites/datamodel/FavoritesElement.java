/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

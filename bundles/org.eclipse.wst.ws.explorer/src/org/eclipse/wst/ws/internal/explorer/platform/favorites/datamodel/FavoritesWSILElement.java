/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel;

import org.apache.wsil.Link;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesWSIL;

public class FavoritesWSILElement extends FavoritesElement {

  private FavoritesWSIL wsil_;

  public FavoritesWSILElement(String name, Model model, Link link) {
    super(name, model);
    wsil_ = new FavoritesWSIL();
    wsil_.setLink(link);
  }

  public Link getLink() {
    return wsil_.getLink();
  }

  public String getWsilUrl() {
    return wsil_.getWsilUrl();
  }

  public String toString() {
    return getWsilUrl();
  }
}

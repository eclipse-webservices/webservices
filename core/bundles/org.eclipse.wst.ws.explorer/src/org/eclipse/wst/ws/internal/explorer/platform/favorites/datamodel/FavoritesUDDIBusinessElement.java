/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
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

import org.apache.wsil.Link;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesUDDIBusiness;

public class FavoritesUDDIBusinessElement extends FavoritesElement {

  private FavoritesUDDIBusiness uddiBusiness_;

  public FavoritesUDDIBusinessElement(String name, Model model, Link link) {
    super(name, model);
    uddiBusiness_ = new FavoritesUDDIBusiness();
    uddiBusiness_.setLink(link);
  }

  public Link getLink() {
    return uddiBusiness_.getLink();
  }

  public String getName() {
    return uddiBusiness_.getName();
  }

  public String getInquiryURL() {
    return uddiBusiness_.getInquiryURL();
  }

  public String getBusinessKey() {
    return uddiBusiness_.getBusinessKey();
  }

  public String toString() {
    return getName();
  }
}

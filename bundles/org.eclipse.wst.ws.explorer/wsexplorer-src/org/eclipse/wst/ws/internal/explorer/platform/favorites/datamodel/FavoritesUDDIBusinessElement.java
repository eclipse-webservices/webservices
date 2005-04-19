/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel;

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesUDDIBusiness;
import org.apache.wsil.*;

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

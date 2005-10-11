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

import org.apache.wsil.Link;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesUDDIRegistry;
import org.eclipse.wst.ws.internal.parser.favorites.IFavoritesUDDIRegistry;

public class FavoritesUDDIRegistryElement extends FavoritesElement {

  private FavoritesUDDIRegistry uddiReg_;

  public FavoritesUDDIRegistryElement(String name, Model model, Link link) {
    super(name, model);
    uddiReg_ = new FavoritesUDDIRegistry();
    uddiReg_.setLink(link);
  }

  public Link getLink() {
    return uddiReg_.getLink();
  }

  public String getName() {
    return uddiReg_.getName();
  }

  public String getInquiryURL() {
    return uddiReg_.getInquiryURL();
  }

  public String getPublishURL() {
    return uddiReg_.getPublishURL();
  }

  public String getRegistrationURL() {
    return uddiReg_.getRegistrationURL();
  }

  public String toString() {
    return getName();
  }
  
  public IFavoritesUDDIRegistry getIFavoritesUDDIRegistryInterface()
  {
    return uddiReg_;
  }
}
  

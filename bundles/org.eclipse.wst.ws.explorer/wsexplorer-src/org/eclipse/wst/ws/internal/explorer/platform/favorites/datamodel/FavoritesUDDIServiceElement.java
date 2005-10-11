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

import org.apache.wsil.Service;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesUDDIService;

public class FavoritesUDDIServiceElement extends FavoritesElement {

  private FavoritesUDDIService uddiService_;

  public FavoritesUDDIServiceElement(String name, Model model, Service service) {
    super(name, model);
    uddiService_ = new FavoritesUDDIService();
    uddiService_.setService(service);
  }

  public Service getService() {
    return uddiService_.getService();
  }

  public String getName() {
    return uddiService_.getName();
  }

  public String getInquiryURL() {
    return uddiService_.getInquiryURL();
  }

  public String getServiceKey() {
    return uddiService_.getServiceKey();
  }

  public String toString() {
    return getName();
  }
}

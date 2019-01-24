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

import org.apache.wsil.Service;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesUDDIServiceInterface;

public class FavoritesUDDIServiceInterfaceElement extends FavoritesElement {

  private FavoritesUDDIServiceInterface uddiSerInt_;

  public FavoritesUDDIServiceInterfaceElement(String name, Model model, Service service) {
    super(name, model);
    uddiSerInt_ = new FavoritesUDDIServiceInterface();
    uddiSerInt_.setService(service);
  }

  public Service getService() {
    return uddiSerInt_.getService();
  }

  public String getName() {
    return uddiSerInt_.getName();
  }

  public String getInquiryURL() {
    return uddiSerInt_.getInquiryURL();
  }

  public String getServiceInterfaceKey() {
    return uddiSerInt_.getServiceInterfaceKey();
  }

  public String toString() {
    return getName();
  }
}

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
import org.eclipse.wst.ws.internal.parser.favorites.FavoritesWSDL;
import org.apache.wsil.*;

public class FavoritesWSDLServiceElement extends FavoritesElement {

  private FavoritesWSDL wsdl_;

  public FavoritesWSDLServiceElement(String name, Model model, Service service) {
    super(name, model);
    wsdl_ = new FavoritesWSDL();
    wsdl_.setService(service);
  }

  public Service getService() {
    return wsdl_.getService();
  }

  public String getWsdlUrl() {
    return wsdl_.getWsdlUrl();
  }

  public String toString() {
    return getWsdlUrl();
  }
}

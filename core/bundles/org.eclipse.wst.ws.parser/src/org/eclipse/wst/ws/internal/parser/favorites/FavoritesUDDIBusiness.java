/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.parser.favorites;

import org.apache.wsil.extension.uddi.BusinessDescription;

public class FavoritesUDDIBusiness extends FavoritesLink implements IFavoritesUDDIBusiness
{
  public FavoritesUDDIBusiness()
  {
    super();
  }

  public String getName()
  {
    return (link_.getAbstracts())[0].getText();
  }

  public String getInquiryURL()
  {
    return ((BusinessDescription)link_.getExtensionElement()).getLocation();
  }

  public String getBusinessKey()
  {
    return ((BusinessDescription)link_.getExtensionElement()).getBusinessKey().getText();
  }

  public void setName(String name)
  {
  }

  public void setInquiryURL(String inquiryURL)
  {
  }

  public void setBusinessKey(String key)
  {
  }
}

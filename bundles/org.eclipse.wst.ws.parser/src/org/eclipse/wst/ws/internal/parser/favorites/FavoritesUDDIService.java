/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.parser.favorites;

import org.apache.wsil.extension.uddi.ServiceDescription;

public class FavoritesUDDIService extends FavoritesService implements IFavoritesUDDIService
{
  public FavoritesUDDIService()
  {
    super();
  }

  public String getName()
  {
    return (service_.getServiceNames())[0].getText();
  }

  public String getInquiryURL()
  {
    ServiceDescription sd = (ServiceDescription)(service_.getDescriptions())[0].getExtensionElement();
    return sd.getLocation();
  }

  public String getServiceKey()
  {
    ServiceDescription sd = (ServiceDescription)(service_.getDescriptions())[0].getExtensionElement();
    return sd.getServiceKey().getText();
  }

  public void setName(String name)
  {
  }

  public void setInquiryURL(String inquiryURL)
  {
  }

  public void setServiceKey(String key)
  {
  }
}

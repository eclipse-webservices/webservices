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

import org.apache.wsil.Abstract;
import org.eclipse.wst.ws.internal.parser.plugin.ParserPlugin;

public class FavoritesUDDIRegistry extends FavoritesLink implements IFavoritesUDDIRegistry
{
  public FavoritesUDDIRegistry()
  {
    super();
  }

  public String getName()
  {
    String name = (link_.getAbstracts())[0].getText();
    if (name.startsWith("%"))
    {
      String translatedName = ParserPlugin.getMessage(name);
      if (translatedName != null)
        name = translatedName;
    }
    return name;
  }

  public String getInquiryURL()
  {
    return (link_.getAbstracts())[1].getText();
  }

  public String getPublishURL()
  {
    Abstract[] abstracts = link_.getAbstracts();
    if (abstracts.length > 2)
    {
      String publishURL = abstracts[2].getText();
      if (publishURL != null && publishURL.length() > 0)
        return publishURL;
    }
    return null;
  }

  public String getRegistrationURL()
  {
    Abstract[] abstracts = link_.getAbstracts();
    if (abstracts.length > 3)
    {
      String registrationURL = abstracts[3].getText();
      if (registrationURL != null && registrationURL.length() > 0)
        return registrationURL;
    }
    return null;
  }

  public void setName(String name)
  {
    (link_.getAbstracts())[0].setText(name);
  }

  public void setInquiryURL(String inquiryURL)
  {
    (link_.getAbstracts())[1].setText(inquiryURL);
  }

  public void setPublishURL(String publishURL)
  {
    Abstract[] abstracts = link_.getAbstracts();
    if (abstracts.length > 2)
      abstracts[2].setText(publishURL);
  }

  public void setRegistrationURL(String registrationURL)
  {
    Abstract[] abstracts = link_.getAbstracts();
    if (abstracts.length > 3)
      abstracts[3].setText(registrationURL);
  }
}

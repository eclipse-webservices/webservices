/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.parser.favorites;

public interface IFavoritesUDDIBusiness
{
  public String getName();
  public String getInquiryURL();
  public String getBusinessKey();

  public void setName(String name);
  public void setInquiryURL(String inquiryURL);
  public void setBusinessKey(String key);
}

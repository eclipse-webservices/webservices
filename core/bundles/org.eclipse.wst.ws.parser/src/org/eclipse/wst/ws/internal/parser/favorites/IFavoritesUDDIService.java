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

public interface IFavoritesUDDIService
{
  public String getName();
  public String getInquiryURL();
  public String getServiceKey();

  public void setName(String name);
  public void setInquiryURL(String inquiryURL);
  public void setServiceKey(String key);
}

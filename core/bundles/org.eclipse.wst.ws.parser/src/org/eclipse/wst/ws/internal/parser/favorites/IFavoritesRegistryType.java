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

public interface IFavoritesRegistryType
{
  public IFavoritesUDDIRegistry[] getFavoritesUDDIRegistries();
  public IFavoritesUDDIBusiness[] getFavoritesUDDIBusinesses();
  public IFavoritesUDDIService[] getFavoritesUDDIServices();
  public IFavoritesUDDIServiceInterface[] getFavoritesUDDIServiceInterfaces();
  public IFavoritesWSDL[] getFavoritesWSDLs();
  public IFavoritesWSIL[] getFavoritesWSILs();

  public void addFavoritesUDDIRegistry(IFavoritesUDDIRegistry registry);
  public void addFavoritesUDDIBusiness(IFavoritesUDDIBusiness business);
  public void addFavoritesUDDIService(IFavoritesUDDIService service);
  public void addFavoritesUDDIServiceInterface(IFavoritesUDDIServiceInterface serviceInterface);
  public void addFavoritesWSDL(IFavoritesWSDL wsdl);
  public void addFavoritesWSIL(IFavoritesWSIL wsil);

  public void removeFavoritesUDDIRegistry(IFavoritesUDDIRegistry registry);
  public void removeFavoritesUDDIBusiness(IFavoritesUDDIBusiness business);
  public void removeFavoritesUDDIService(IFavoritesUDDIService service);
  public void removeFavoritesUDDIServiceInterface(IFavoritesUDDIServiceInterface serviceInterface);
  public void removeFavoritesWSDL(IFavoritesWSDL wsdl);
  public void removeFavoritesWSIL(IFavoritesWSIL wsil);  
}

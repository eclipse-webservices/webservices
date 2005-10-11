/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.eclipse.wst.ws.internal.explorer.LaunchOptions;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public class LaunchOptionsManager
{
  private static LaunchOptionsManager instance_;

  private Hashtable optionsTable_;

  private LaunchOptionsManager()
  {
    optionsTable_ = new Hashtable();
  }

  public static LaunchOptionsManager getInstance()
  {
    if (instance_ == null)
      instance_ = new LaunchOptionsManager();
    return instance_;
  }

  public synchronized void manage(String key, Object value, ServletContext application)
  {
    if (value instanceof String)
      manageSession(key, (String)value, application);
    else if (value instanceof Hashtable)
      manageOptions(key, (Hashtable)value, application);
  }

  private void manageSession(String key, String sessionId, ServletContext application)
  {
    Object obj = optionsTable_.remove(key);
    if (obj != null && (obj instanceof Hashtable))
      loadOptions(sessionId, (Hashtable)obj, application);
    else
      optionsTable_.put(key, sessionId);
  }

  private void manageOptions(String key, Hashtable options, ServletContext application)
  {
    Object obj = optionsTable_.remove(key);
    if (obj != null && (obj instanceof String))
      loadOptions((String)obj, options, application);
    else
      optionsTable_.put(key, options);
  }

  private void loadOptions(String sessionId, Hashtable options, ServletContext application)
  {
    HttpSession httpSession = (HttpSession)application.getAttribute(sessionId);
    if (httpSession != null)
    {
      Controller controller = (Controller)httpSession.getAttribute("controller");
	  String[] stateLocations = (String[])options.get(LaunchOptions.STATE_LOCATION);
	  String[] defaultFavoritesLocations = (String[])options.get(LaunchOptions.DEFAULT_FAVORITES_LOCATION);
      String[] inquiryURLs = (String[])options.get(LaunchOptions.INQUIRY_URL);
      String[] publishURLs = (String[])options.get(LaunchOptions.PUBLISH_URL);
      String[] serviceNames = (String[])options.get(LaunchOptions.SERVICE_NAME);
      String[] serviceKeys = (String[])options.get(LaunchOptions.SERVICE_KEY);
      String[] wsdlURLs = (String[])options.get(LaunchOptions.WSDL_URL);
      String[] endpoints = (String[])options.get(LaunchOptions.WEB_SERVICE_ENDPOINT);
      String[] serviceQNameStrings = (String[])options.get(LaunchOptions.SERVICE_QNAME_STRING);
      String[] bindingNameStrings = (String[])options.get(LaunchOptions.BINDING_NAME_STRING);
	  String stateLocation = (stateLocations == null || stateLocations.length == 0) ? null : stateLocations[0];
	  controller.setStateLocation(stateLocation);
	  String defaultFavoritesLocation = (defaultFavoritesLocations == null || defaultFavoritesLocations.length == 0) ? null : defaultFavoritesLocations[0];
	  controller.setDefaultFavoritesLocation(defaultFavoritesLocation);
      UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
      uddiPerspective.preloadUDDIRegistries(inquiryURLs, publishURLs);
      uddiPerspective.preloadServices(inquiryURLs,serviceNames,serviceKeys);
      WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
      wsdlPerspective.preloadWSDL(wsdlURLs);
      wsdlPerspective.preloadEndpoints(wsdlURLs, endpoints);
      wsdlPerspective.preselectServiceOrBinding(wsdlURLs,serviceQNameStrings,bindingNameStrings);
    }
  }
}
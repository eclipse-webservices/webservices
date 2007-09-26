/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import javax.servlet.ServletContext;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.engine.ActionEngine;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;

public class Controller extends MessageProvider
{
  private String sessionId_;
  private String stateLocation_;
  private String defaultFavoritesLocation_;
  private String contextPath_;
  private int historyDirection_;
  private History history_;
  private UDDIPerspective uddiPerspective_;
  private WSILPerspective wsilPerspective_;
  private WSDLPerspective wsdlPerspective_;
  private FavoritesPerspective favoritesPerspective_;
  private Perspective currentPerspective_;
  private boolean isPerspectiveContentBlank_;
  private ActionEngine actionEngine;
  
  // WSDL Browser
  private int wsdlType_;

  public Controller()
  {
    super("explorer");
    contextPath_ = null;
	stateLocation_ = null;
    history_ = new History();
    uddiPerspective_ = new UDDIPerspective(this);
    wsilPerspective_ = new WSILPerspective(this);
    wsdlPerspective_ = new WSDLPerspective(this);
    favoritesPerspective_ = new FavoritesPerspective(this);
    // Set the default perspective to UDDI.
    currentPerspective_ = uddiPerspective_;
    historyDirection_ = ActionInputs.JUMP_BACK;
    wsdlType_ = ActionInputs.WSDL_TYPE_SERVICE_INTERFACE;
    isPerspectiveContentBlank_ = true;
    actionEngine = new ActionEngine(this);
  }
  
  public final void setStateLocation(String stateLocation)
  {
	stateLocation_ = stateLocation;
  }

  public final void setDefaultFavoritesLocation(String defaultFavoritesLocation)
  {
	  defaultFavoritesLocation_ = defaultFavoritesLocation;
  }
  
  // This method should be called immediately after the Controller bean is instantiated.
  private final void setContextPath(String contextPath)
  {
    if (contextPath_ == null)
      contextPath_ = contextPath;
  }
  
  public final String getPathWithContext(String relativePath)
  {
    if (relativePath == null)
      return relativePath;
    StringBuffer path = new StringBuffer(relativePath);
    if (contextPath_ != null)
      path.insert(0,'/').insert(0,contextPath_);
    return path.toString();
  }

  public final String getSessionId()
  {
    return sessionId_;
  }
  
  public final String getContextPath()
  {
    return contextPath_;
  }
  
  public final String getServletEngineStateLocation() {
	return stateLocation_;
  }
  
  public final String getDefaultFavoritesLocation () {
	return defaultFavoritesLocation_;
  }
  
  public final void init(String sessionId,ServletContext application,String contextPath)
  {
    sessionId_ = sessionId;
    setContextPath(contextPath);
    uddiPerspective_.initPerspective(application);
    wsilPerspective_.initPerspective(application);
    wsdlPerspective_.initPerspective(application);
    favoritesPerspective_.initPerspective(application);
  }

  // Setter for the current perspective.
  public final void setCurrentPerspective(int perspective)
  {
    switch (perspective)
    {
      case ActionInputs.PERSPECTIVE_UDDI:
        currentPerspective_ = uddiPerspective_;
        break;
      case ActionInputs.PERSPECTIVE_WSIL:
        currentPerspective_ = wsilPerspective_;
        break;
      case ActionInputs.PERSPECTIVE_WSDL:
        currentPerspective_ = wsdlPerspective_;
        break;
      case ActionInputs.PERSPECTIVE_FAVORITES:
        currentPerspective_ = favoritesPerspective_;
        break;
    }
  }

  // Getter for the current perspective.
  public final Perspective getCurrentPerspective()
  {
    return currentPerspective_;
  }

  // Getter for the UDDI perspective.
  public final UDDIPerspective getUDDIPerspective()
  {
    return uddiPerspective_;
  }

  // Getter for the WSIL perspective.
  public final WSILPerspective getWSILPerspective()
  {
    return wsilPerspective_;
  }
  
  // Getter for the WSDL perspective.
  public final WSDLPerspective getWSDLPerspective()
  {
    return wsdlPerspective_;
  }

  // Getter for the Favorites perspective.
  public final FavoritesPerspective getFavoritesPerspective() {
    return favoritesPerspective_;
  }
  
  // Getter for the action engine
  public final ActionEngine getActionEngine() {
    return actionEngine;
  }

  public final boolean addToHistory(int perspectiveId,String url)
  {
    if (url != null)
      return history_.addBreadCrumb(new BreadCrumb(perspectiveId,url));
    return false;
  }

  // Wrapper for moving forward in the history list and obtaining the breadcrumb.
  public final BreadCrumb forward()
  {
    historyDirection_ = ActionInputs.JUMP_FORWARD;
    return history_.forward();
  }

  // Wrapper for moving back in the history list and obtaining the breadcrumb.
  public final BreadCrumb back()
  {
    historyDirection_ = ActionInputs.JUMP_BACK;
    return history_.back();
  }

  public final int getHistoryDirection()
  {
    return historyDirection_;
  }

  public final void processStaleBreadCrumb()
  {
    history_.removeCurrentBreadCrumb();
  }
  
  // WSDL Browser
  public final void setWSDLType(int wsdlType)
  {
    wsdlType_ = wsdlType;
  }

  public final int getWSDLType()
  {
    return wsdlType_;
  }
  
  public final boolean isPerspectiveContentBlank()
  {
    return isPerspectiveContentBlank_;
  }
  
  public final void enablePerspectiveContentBlank(boolean isPerspectiveContentBlank)
  {
    isPerspectiveContentBlank_ = isPerspectiveContentBlank;
  }
  
  public final void dumpHistory()
  {
    history_.dump();
  }
}

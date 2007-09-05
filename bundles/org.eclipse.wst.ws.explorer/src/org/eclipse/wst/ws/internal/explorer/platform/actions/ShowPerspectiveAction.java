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

package org.eclipse.wst.ws.internal.explorer.platform.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public class ShowPerspectiveAction extends LinkAction
{
  public ShowPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  // actions/show_perspective.jsp?perspective=0<&isHistory=1> - UDDI (ActionInputs.PERSPECTIVE_UDDI)
  // actions/show_perspective.jsp?perspective=1<&isHistory=1> - WSIL (ActionInputs.PERSPECTIVE_WSIL)
  // actions/show_perspective.jsp?perspective=2<&isHistory=1> - WSDL (ActionInputs.PERSPECTIVE_WSDL)
  // actions/show_perspective.jsp?perspective=3<&isHistory=1> - Favorites (ActionInputs.PERSPECTIVE_FAVORITES)
  public final static String getActionLink(int newPerspective,boolean forHistory)
  {
    StringBuffer actionLink = new StringBuffer("actions/ShowPerspectiveActionJSP.jsp?");
    actionLink.append(ActionInputs.PERSPECTIVE).append('=').append(newPerspective);
    if (forHistory)
      actionLink.append('&').append(ActionInputs.ISHISTORY).append("=1");
    return actionLink.toString();
  }
  
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    return true;
  }
  
  protected final boolean processLinkParameters(HttpServletRequest request)
  {
    boolean result = false;
    String perspectiveString = request.getParameter(ActionInputs.PERSPECTIVE);
    // Perform data validation.
    try
    {
      Integer.parseInt(perspectiveString);
      propertyTable_.put(ActionInputs.PERSPECTIVE,perspectiveString);
      MultipartFormDataParser parser = new MultipartFormDataParser();
      parser.parseRequest(request,HTMLUtils.UTF8_ENCODING);
      result = processParsedResults(parser);
      removedProperties_.removeAllElements();
    }
    catch (NumberFormatException e)
    {
      // Validation failed! - This should not happen.
    }
    catch (MultipartFormDataException e)
    {
    }
    return result;
  }

  public boolean run()
  {
    int perspective = Integer.parseInt((String)propertyTable_.get(ActionInputs.PERSPECTIVE));
    controller_.setCurrentPerspective(perspective);
    addToHistory(perspective,getActionLink(perspective,true));
    return true;
  }
}

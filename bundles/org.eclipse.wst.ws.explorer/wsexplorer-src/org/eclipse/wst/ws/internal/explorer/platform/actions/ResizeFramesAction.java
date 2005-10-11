/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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

public abstract class ResizeFramesAction extends Action
{
  protected Controller controller_;
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String frameName = parser.getParameter(ActionInputs.FRAME_NAME);
    if (frameName != null)
      propertyTable_.put(ActionInputs.FRAME_NAME,frameName);
    else
      return false;
    return processOthers(parser);
  }
  
  protected abstract boolean processOthers(MultipartFormDataParser parser) throws MultipartFormDataException;
  
  public ResizeFramesAction(Controller controller)
  {
    controller_ = controller;
  }
  
  public final boolean populatePropertyTable(HttpServletRequest request)
  {
    try
    {
      MultipartFormDataParser parser = new MultipartFormDataParser();
      parser.parseRequest(request,HTMLUtils.UTF8_ENCODING);
      boolean result = processParsedResults(parser);
      return result;
    }
    catch (MultipartFormDataException e)
    {
    }
    return false;
  }
}

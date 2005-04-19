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
package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.LinkAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.MassNodeIdTransformer;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;

import javax.servlet.http.*;

public abstract class MultipleLinkAction extends LinkAction
{
  public MultipleLinkAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processLinkParameters(HttpServletRequest request)
  {
    String isMultipleLinkAction = request.getParameter(FavoritesActionInputs.MULTIPLE_LINK_ACTION);
    if (isMultipleLinkAction != null)
    {
      propertyTable_.put(FavoritesActionInputs.MULTIPLE_LINK_ACTION, isMultipleLinkAction);
      return processMultipleLinkActionParameters(request);
    }
    else
    {
      propertyTable_.remove(FavoritesActionInputs.MULTIPLE_LINK_ACTION);
      String nodeIDString = request.getParameter(ActionInputs.NODEID);
      String toolIDString = request.getParameter(ActionInputs.TOOLID);
      String viewIDString = request.getParameter(ActionInputs.VIEWID);
      String viewToolIDString = request.getParameter(ActionInputs.VIEWTOOLID);
      if (nodeIDString != null)
        propertyTable_.put(ActionInputs.NODEID, nodeIDString);
      if (toolIDString != null)
        propertyTable_.put(ActionInputs.TOOLID, toolIDString);
      if (viewIDString != null)
        propertyTable_.put(ActionInputs.VIEWID, viewIDString);
      if (viewToolIDString != null)
        propertyTable_.put(ActionInputs.VIEWTOOLID, viewToolIDString);
      return true;
    }
  }

  protected boolean processMultipleLinkActionParameters(HttpServletRequest request)
  {
    String[] massActionNodeIDs = request.getParameterValues(FavoritesActionInputs.MASS_ACTION_NODE_ID);
    propertyTable_.put(FavoritesActionInputs.MASS_ACTION_NODE_ID, massActionNodeIDs);
    if (massActionNodeIDs.length == 0)
      return false;
    else
      return true;
  }

  public ITransformer[] getTransformers()
  {
    return new ITransformer[] {new MassNodeIdTransformer(controller_, FavoritesActionInputs.MASS_ACTION_NODE_ID)};
  }

  protected boolean isMultipleLinkAction()
  {
    return propertyTable_.containsKey(FavoritesActionInputs.MULTIPLE_LINK_ACTION);
  }

  protected String[] getMassActionNodeIds()
  {
    Object object = propertyTable_.get(FavoritesActionInputs.MASS_ACTION_NODE_ID);
    if (object == null)
      return new String[0];
    else if (object.getClass().isArray())
      return (String[])object;
    else
      return new String[] {(String)object};
  }

  public boolean run()
  {
    return (isMultipleLinkAction()) ? executeMultipleLinkAction() : executeSingleLinkAction();
  }

  protected boolean executeMultipleLinkAction()
  {
    boolean result = false;
    String[] massActionNodeIDs = getMassActionNodeIds();
    if (massActionNodeIDs != null)
    {
      for (int i = 0; i < massActionNodeIDs.length; i++)
      {
        propertyTable_.put(ActionInputs.NODEID, massActionNodeIDs[i]);
        if (executeSingleLinkAction())
          result = true;
      }
      propertyTable_.remove(ActionInputs.NODEID);
    }
    return result;
  }

  protected abstract boolean executeSingleLinkAction();
}
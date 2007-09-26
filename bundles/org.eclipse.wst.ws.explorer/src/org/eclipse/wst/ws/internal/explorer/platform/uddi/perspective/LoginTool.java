/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   136449 brunssen@us.ibm.com - Vince Brunssen  
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import java.util.Enumeration;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectPropertiesToolAction;

public class LoginTool extends FormTool
{
  private Controller controller_;
  private String formLink = "uddi/forms/LoginUddiForm.jsp";
  
  public LoginTool(ToolManager toolManager, String alt)
  {
    super(toolManager,"uddi/images/open_registry_enabled.gif","uddi/images/open_registry_highlighted.gif",alt);
  }

  protected final void initDefaultProperties()
  {
    controller_ = toolManager_.getNode().getNodeManager().getController();
  }

  public String getSelectToolActionHref(boolean forHistory)
  {
    Node node = toolManager_.getNode();
    return SelectPropertiesToolAction.getActionLink(node.getNodeId(),toolId_,node.getViewId(),node.getViewToolId(),forHistory);
  }

  public String getFormLink()
  {
    return formLink;
  }
  
  public void setToLogoutLink() 
  {
  	formLink = "uddi/actions/LogoutAdvancedActionJSP.jsp";
  }
  
  public void setToRegistryLink() 
  {
  	formLink = "";
  }
  
  public final Enumeration getFavoriteRegistryElements()
  {
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager favoritesNodeManager = favPerspective.getNodeManager();
    TreeElement favoritesMainElement = favoritesNodeManager.getRootNode().getTreeElement();
    TreeElement favoriteRegistriesElement = (TreeElement)(favoritesMainElement.getElements(FavoritesModelConstants.REL_UDDI_REGISTRY_FOLDER_NODE).nextElement());
    return favoriteRegistriesElement.getElements(FavoritesModelConstants.REL_UDDI_REGISTRY_NODE);
  }
}

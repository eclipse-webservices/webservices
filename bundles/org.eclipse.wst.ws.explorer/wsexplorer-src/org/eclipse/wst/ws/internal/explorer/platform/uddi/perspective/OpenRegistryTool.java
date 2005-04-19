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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;

import java.util.*;

public class OpenRegistryTool extends FormTool
{
  private Controller controller_;
  
  public OpenRegistryTool(ToolManager toolManager,String alt)
  {
    super(toolManager,"uddi/images/open_registry_enabled.gif","uddi/images/open_registry_highlighted.gif",alt);
  }

  protected final void initDefaultProperties()
  {
    controller_ = toolManager_.getNode().getNodeManager().getController();
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    setProperty(UDDIActionInputs.REGISTRY_NAME,uddiPerspective.getMessage("DEFAULT_REGISTRY_NAME"));
    setProperty(UDDIActionInputs.INQUIRY_URL,uddiPerspective.getIBMPublicUDDITestRegistry().getInquiryURL());
  }

  public String getSelectToolActionHref(boolean forHistory)
  {
    Node node = toolManager_.getNode();
    return SelectPropertiesToolAction.getActionLink(node.getNodeId(),toolId_,node.getViewId(),node.getViewToolId(),forHistory);
  }

  public String getFormLink()
  {
    return "uddi/forms/OpenRegistryForm.jsp";
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

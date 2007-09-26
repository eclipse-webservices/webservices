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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.BusinessGetServicesAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;

public class BusinessNode extends UDDINavigatorNode
{
  private DetailsTool detailsTool_;
  private BusPublishServiceTool publishServiceTool_;
  private ManagePublisherAssertionsTool managePublisherAssertionsTool_;
  private ManageReferencedServicesTool manageReferencedServicesTool_;
  private UnpublishTool unpublishTool_;

  public BusinessNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"uddi/images/business_highlighted.gif");
  }

  protected final void initTools()
  {
    Controller controller = nodeManager_.getController();
    UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
    detailsTool_ = new BusinessDetailsTool(toolManager_,uddiPerspective.getMessage("ALT_BUSINESS_DETAILS"));
    new GetServicesTool(toolManager_,uddiPerspective.getMessage("ALT_GET_SERVICES"),BusinessGetServicesAction.getActionLink(nodeId_));    
    publishServiceTool_ = new BusPublishServiceTool(toolManager_,uddiPerspective.getMessage("ALT_PUBLISH_SERVICE"));
    manageReferencedServicesTool_ = new ManageReferencedServicesTool(toolManager_,uddiPerspective.getMessage("ALT_MANAGE_REFERENCED_SERVICES"));
    managePublisherAssertionsTool_ = new ManagePublisherAssertionsTool(toolManager_,uddiPerspective.getMessage("ALT_MANAGE_PUBLISHER_ASSERTIONS"));
    new AddToFavoritesTool(toolManager_, uddiPerspective.getMessage("ALT_ADD_TO_FAVORITES"));
    unpublishTool_ = new UnpublishTool(toolManager_,"uddi/images/unpublish_bus_enabled.gif","uddi/images/unpublish_bus_highlighted.gif",uddiPerspective.getMessage("ALT_UNPUBLISH_BUSINESS"));
  }

  public void addAuthenticationProperties(RegistryElement regElement)
  {
    detailsTool_.addAuthenticationProperties(regElement);
    publishServiceTool_.addAuthenticationProperties(regElement);
    manageReferencedServicesTool_.addAuthenticationProperties(regElement);
    managePublisherAssertionsTool_.addAuthenticationProperties(regElement);
    unpublishTool_.addAuthenticationProperties(regElement);
  }
}

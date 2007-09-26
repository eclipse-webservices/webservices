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

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.ServiceGetBusinessAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.ServiceGetServiceInterfacesAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;

public class ServiceNode extends UDDINavigatorNode
{
  private DetailsTool detailsTool_;
  private UnpublishTool unpublishTool_;
  public ServiceNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"uddi/images/service_highlighted.gif");
  }

  protected final void initTools()
  {
    Controller controller = nodeManager_.getController();
    UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
    detailsTool_ = new ServiceDetailsTool(toolManager_,uddiPerspective.getMessage("ALT_SERVICE_DETAILS"));
    new GetBusinessesTool(toolManager_,"uddi/images/business_enabled.gif","uddi/images/business_highlighted.gif",uddiPerspective.getMessage("ALT_GET_BUSINESS"),ServiceGetBusinessAction.getActionLink(nodeId_));
    new GetServiceInterfacesTool(toolManager_,uddiPerspective.getMessage("ALT_GET_SERVICE_INTERFACES"),ServiceGetServiceInterfacesAction.getActionLink(nodeId_));
    new UDDIImportWSDLToWorkbenchTool(toolManager_,controller.getMessage("ALT_IMPORT_WSDL_TO_WORKBENCH"));
    new UDDIImportWSDLToFileSystemTool(toolManager_,controller.getMessage("ALT_WSDL_IMPORT_TO_FS"));
    new UDDILaunchWebServiceWizardTool(toolManager_,controller.getMessage("ALT_LAUNCH_WEB_SERVICE_WIZARD"));
    new UDDIAddToWSDLPerspectiveTool(toolManager_,uddiPerspective.getMessage("ALT_ADD_TO_WSDL_PERSPECTIVE"));
    new AddToFavoritesTool(toolManager_,uddiPerspective.getMessage("ALT_ADD_TO_FAVORITES"));
    unpublishTool_ = new UnpublishTool(toolManager_,"uddi/images/unpublish_service_enabled.gif","uddi/images/unpublish_service_highlighted.gif",uddiPerspective.getMessage("ALT_UNPUBLISH_SERVICE"));
  }
  
  public void addAuthenticationProperties(RegistryElement regElement)
  {
    detailsTool_.addAuthenticationProperties(regElement);
    unpublishTool_.addAuthenticationProperties(regElement);
  }

  public String getWSDLURLFromDetailsTool()
  {
    ListElement wsdlURLListElement = (ListElement)detailsTool_.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL);
    if (wsdlURLListElement != null)
      return (String)wsdlURLListElement.getObject();
    else
      return "temp.wsdl";
  }
}

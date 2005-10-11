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

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.ServiceInterfaceGetBusinessesAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.ServiceInterfaceGetServicesAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;

public class ServiceInterfaceNode extends UDDINavigatorNode
{
  private DetailsTool detailsTool_;
  private UnpublishTool unpublishTool_;
  public ServiceInterfaceNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"uddi/images/service_interface_highlighted.gif");
  }

  protected final void initTools()
  {
    Controller controller = nodeManager_.getController();
    UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
    detailsTool_ = new ServiceInterfaceDetailsTool(toolManager_,uddiPerspective.getMessage("ALT_SERVICE_INTERFACE_DETAILS"));
    new GetServicesTool(toolManager_,uddiPerspective.getMessage("ALT_GET_SERVICES"),ServiceInterfaceGetServicesAction.getActionLink(nodeId_));
    new GetBusinessesTool(toolManager_,"uddi/images/businesses_closed_enabled.gif","uddi/images/businesses_closed_highlighted.gif",uddiPerspective.getMessage("ALT_GET_BUSINESSES"),ServiceInterfaceGetBusinessesAction.getActionLink(nodeId_));
    new UDDIImportWSDLToWorkbenchTool(toolManager_,controller.getMessage("ALT_IMPORT_WSDL_TO_WORKBENCH"));
    new UDDIImportWSDLToFileSystemTool(toolManager_,controller.getMessage("ALT_WSDL_IMPORT_TO_FS"));
    new UDDILaunchWebServiceWizardTool(toolManager_,controller.getMessage("ALT_LAUNCH_WEB_SERVICE_WIZARD"));
    new AddToFavoritesTool(toolManager_, uddiPerspective.getMessage("ALT_ADD_TO_FAVORITES"));
    unpublishTool_ = new UnpublishTool(toolManager_,"uddi/images/unpublish_service_interface_enabled.gif","uddi/images/unpublish_service_interface_highlighted.gif",uddiPerspective.getMessage("ALT_UNPUBLISH_SERVICE_INTERFACE"));
  }
  
  public void addAuthenticationProperties(RegistryElement regElement)
  {
    detailsTool_.addAuthenticationProperties(regElement);
    unpublishTool_.addAuthenticationProperties(regElement);
  }
  
  public String getWSDLURLFromDetailsTool()
  {
    ListElement wsdlURLListElement = (ListElement)detailsTool_.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL);
    if (wsdlURLListElement != null)
      return (String)wsdlURLListElement.getObject();
    else
      return "temp.wsdl";
  }
}

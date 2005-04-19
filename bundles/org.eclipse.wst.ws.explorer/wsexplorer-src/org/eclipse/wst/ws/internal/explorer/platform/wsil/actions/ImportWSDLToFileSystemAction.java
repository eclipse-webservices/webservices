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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*;

import java.io.OutputStream;
import javax.wsdl.Definition;
import org.uddi4j.client.UDDIProxy;

public class ImportWSDLToFileSystemAction extends ImportToFileSystemAction
{
  private Definition def_;
  private String wsdlFileName_;
  private NodeManager nodeManager_;

  public ImportWSDLToFileSystemAction(Controller controller)
  {
    super(controller);
    nodeManager_ = controller.getWSILPerspective().getNodeManager();
  }

  public static final String getActionLink(int nodeId, int toolId, int viewId, int viewToolId)
  {
    return ImportToFileSystemAction.getActionLink(nodeId, toolId, viewId, viewToolId, "wsil/actions/ImportWSDLToFileSystemActionJSP.jsp");
  }

  public boolean write(OutputStream os)
  {
    if (def_ != null)
    {
      return writeWSDLDefinition(os, def_);
    }
    else
    {
      return false;
    }
  }

  public String getDefaultFileName()
  {
    return wsdlFileName_;
  }

  private boolean getWSDLFromURL()
  {
    int nodeID = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    int viewID = Integer.parseInt((String)propertyTable_.get(ActionInputs.VIEWID));
    Node selectedNode = nodeManager_.getNode(nodeID);
    WsilElement wsilElement = (WsilElement)selectedNode.getTreeElement();
    ListManager wsilServices = wsilElement.getAllWSDLServices();
    ListElement le = wsilServices.getElementWithViewId(viewID);
    WsilWsdlServiceElement service = (WsilWsdlServiceElement)le.getObject();
    String wsdlURL = service.getWSDLServiceURL();
    wsdlFileName_ = WSDLFileNameHelper.getWSDLFileName(wsdlURL);
    try
    {
      def_ = (new Uddi4jHelper()).getWSDLDefinition(wsdlURL);
    }
    catch (Exception e)
    {
      WSILPerspective wsilPerspective = controller_.getWSILPerspective();
      wsilPerspective.getMessageQueue().addMessage(e.getMessage());
      return false;
    }
    return true;
  }

  private boolean getWSDLFromUDDIRegistry()
  {
    int nodeID = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    int viewID = Integer.parseInt((String)propertyTable_.get(ActionInputs.VIEWID));
    Node selectedNode = nodeManager_.getNode(nodeID);
    WsilElement wsilElement = (WsilElement)selectedNode.getTreeElement();
    ListManager wsilServices = wsilElement.getAllUDDIServices();
    ListElement le = wsilServices.getElementWithViewId(viewID);
    WsilUddiServiceElement service = (WsilUddiServiceElement)le.getObject();
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    wsdlFileName_ = service.getName() + ".wsdl";
    Uddi4jHelper uddi4jHelper = new Uddi4jHelper();
    try
    {
      UDDIProxy proxy = null;
      String inquiryURL = service.getUDDIServiceInquiryAPI();
      if (inquiryURL != null)
      {
        proxy = new UDDIProxy();
        proxy.setInquiryURL(inquiryURL);
      }
      def_ = uddi4jHelper.getWSDLDefinition(uddi4jHelper.getWSDL(service.getServiceDefinition(), proxy));
    }
    catch (Exception e)
    {
      // the wsdl representing this service is unreachable through the UDDI
      // registry. We will try using the discovery URL instead.
      try
      {
        def_ = uddi4jHelper.getWSDLDefinition(service.getUDDIServiceDiscoveryURL());
        return true;
      }
      catch (Exception e2)
      {
      }
      // if the discoveryURL also fails, return an error
      wsilPerspective.getMessageQueue().addMessage(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean run()
  {
    int nodeID = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    Node selectedNode = nodeManager_.getNode(nodeID);
    Tool selectedTool = selectedNode.getToolManager().getSelectedTool();
    if (selectedTool instanceof ListWSDLServicesTool)
      return getWSDLFromURL();
    else if (selectedTool instanceof ListUDDIServicesTool)
      return getWSDLFromUDDIRegistry();
    else
      return false;
  }

  public final String getStatusContentVar()
  {
    return controller_.getWSILPerspective().getStatusContentVar();
  }

  public final String getStatusContentPage()
  {
    return controller_.getWSILPerspective().getStatusContentPage();
  }
}
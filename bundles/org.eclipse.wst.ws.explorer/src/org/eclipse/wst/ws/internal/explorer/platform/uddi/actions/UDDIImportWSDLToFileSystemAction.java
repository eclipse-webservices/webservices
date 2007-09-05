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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.io.OutputStream;
import javax.wsdl.Definition;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.actions.WSDLFileNameHelper;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;

public class UDDIImportWSDLToFileSystemAction extends ImportToFileSystemAction
{
  private NodeManager nodeManager_;
  private Definition def_;
  private String defaultWSDLFileName_;

  public UDDIImportWSDLToFileSystemAction(Controller controller)
  {
    super(controller);
    nodeManager_ = controller.getUDDIPerspective().getNavigatorManager();
    def_ = null;
    defaultWSDLFileName_ = "temp.wsdl";
  }

  public static final String getActionLink(int nodeId,int toolId,int viewId,int viewToolId)
  {
    return ImportToFileSystemAction.getActionLink(nodeId,toolId,viewId,viewToolId,"uddi/actions/UDDIImportWSDLToFileSystemActionJSP.jsp");
  }

  public final boolean write(OutputStream os)
  {
    return writeWSDLDefinition(os,def_);
  }

  public final String getDefaultFileName()
  {
    return defaultWSDLFileName_;
  }

  public final boolean run()
  {
    MessageQueue messageQueue = controller_.getUDDIPerspective().getMessageQueue();
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    Node node = nodeManager_.getNode(nodeId);
    Uddi4jHelper uddi4jHelper = new Uddi4jHelper();
    String wsdlUrl = null;
    if (node != null)
    {
      TreeElement element = node.getTreeElement();
      if (element instanceof ServiceElement)
      {
        ServiceElement serviceElement = (ServiceElement)element;
        BusinessService bs = serviceElement.getBusinessService();
        wsdlUrl = uddi4jHelper.getWSDL(bs, serviceElement.getRegistryElement().getProxy());
      }
      else if (element instanceof ServiceInterfaceElement)
      {
        TModel tModel = ((ServiceInterfaceElement)element).getTModel();
        wsdlUrl = uddi4jHelper.getWSDL(tModel);
      }
    }
    if (wsdlUrl != null)
    {
      try
      {
        def_ = uddi4jHelper.getWSDLDefinition(wsdlUrl);
        if (def_ != null)
        {
          String wsdlPathname;
          int index = wsdlUrl.indexOf('/');
          if (index != -1)
            wsdlPathname = wsdlUrl.substring(index+1, wsdlUrl.length());
          else
            wsdlPathname = wsdlUrl;
          defaultWSDLFileName_ = WSDLFileNameHelper.getWSDLFileName(wsdlPathname);
        }
        return true;
      }
      catch (Throwable t)
      {
        messageQueue.addMessage(controller_.getMessage("MSG_ERROR_RETRIEVING_WSDL_DOC"));
      }
    }
    return false;
  }

  public final String getStatusContentVar()
  {
    return controller_.getUDDIPerspective().getStatusContentVar();
  }

  public final String getStatusContentPage()
  {
    return controller_.getUDDIPerspective().getStatusContentPage();
  }
}

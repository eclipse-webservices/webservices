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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import org.eclipse.wst.ws.internal.explorer.platform.actions.LinkAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.OpenWSDLAction;
import org.uddi4j.datatype.service.BusinessService;

public class UDDIAddToWSDLPerspectiveAction extends LinkAction
{
  public UDDIAddToWSDLPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  public static final String getActionLink(int nodeId,int toolId,int viewId,int viewToolId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/UDDIAddToWSDLPerspectiveActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID);
    actionLink.append('=');
    actionLink.append(nodeId);
    actionLink.append('&');
    actionLink.append(ActionInputs.TOOLID);
    actionLink.append('=');
    actionLink.append(toolId);
    actionLink.append('&');
    actionLink.append(ActionInputs.VIEWID);
    actionLink.append('=');
    actionLink.append(viewId);
    actionLink.append('&');
    actionLink.append(ActionInputs.VIEWTOOLID);
    actionLink.append('=');
    actionLink.append(viewToolId);
    return actionLink.toString();
  }

  protected boolean processLinkParameters(HttpServletRequest request)
  {
    String nodeIdString = request.getParameter(ActionInputs.NODEID);
    // Perform data validation.
    try
    {
      Integer.parseInt(nodeIdString);
    }
    catch (NumberFormatException e)
    {
      // Validation failed!
      return false;
    }
    propertyTable_.put(ActionInputs.NODEID,nodeIdString);
    return true;
  }

  public boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    NodeManager nodeManager = uddiPerspective.getNavigatorManager();
    Node node = nodeManager.getNode(nodeId);
    TreeElement element = node.getTreeElement();
    if (element instanceof ServiceElement)
    {
      ServiceElement serviceElement = (ServiceElement)element;
      BusinessService bs = serviceElement.getBusinessService();
      Uddi4jHelper uddi4jHelper = new Uddi4jHelper();
      String wsdlURL = uddi4jHelper.getWSDL(bs, serviceElement.getRegistryElement().getProxy());
      if (wsdlURL == null)
      {
        try
        {
          Definition def = uddi4jHelper.getWSDLDefinition(wsdlURL);
          File tempFile = File.createTempFile("temp",".wsdl");
          UDDIImportWSDLToFileSystemAction action = new UDDIImportWSDLToFileSystemAction(controller_);
          action.writeWSDLDefinition(new FileOutputStream(tempFile),def);
          wsdlURL = tempFile.toURL().toString();
        }
        catch (WSDLException e)
        {
          wsdlURL = null;
        }
        catch (MalformedURLException e)
        {
          wsdlURL = null;
        }
        catch (IOException e)
        {
          wsdlURL = null;
        }
      }
        
      if (wsdlURL != null)
      {
        OpenWSDLAction openWSDLAction = new OpenWSDLAction(controller_);
        Hashtable propertyTable = openWSDLAction.getPropertyTable();
        propertyTable.put(ActionInputs.QUERY_INPUT_WSDL_URL,wsdlURL);
        boolean actionResult = openWSDLAction.run();
        if (actionResult) {
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_WSDL_ADDED_TO_WSDL_PERSPECTIVE",wsdlURL));
          return true;
        }
      }
    }
    messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_WSDL_NOT_ADDED_TO_WSDL_PERSPECTIVE"));
    return false;
  }
}

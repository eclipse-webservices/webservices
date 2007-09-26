/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ViewSelectionTransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListUDDIBusinessTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListUDDIServicesTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListWSDLServicesTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListWSILLinksTool;

public class WSILViewSelectionTransformer extends ViewSelectionTransformer
{
  public static final byte NONE = 0;
  public static final byte WSDL_SERVICE = 1;
  public static final byte UDDI_SERVICE = 2;
  public static final byte UDDI_BUSINESS = 3;
  public static final byte WSIL_LINK = 4;
  public static final byte FROM_TOOLID = 5;
  private byte type;

  public WSILViewSelectionTransformer(Controller controller)
  {
    this(controller, "", ActionInputs.VIEWID, FROM_TOOLID);
  }

  public WSILViewSelectionTransformer(Controller controller, String listManagerKey, String viewKey, byte type)
  {
    super(controller, listManagerKey, viewKey);
    this.type = type;
  }

  public Hashtable normalize(Hashtable properties)
  {
    if (type == FROM_TOOLID)
    {
      Node currNode = controller.getCurrentPerspective().getNodeManager().getSelectedNode();
      if (currNode != null)
      {
        try
        {
          int toolId = Integer.parseInt((String) properties.get(ActionInputs.TOOLID));
          ToolManager toolManager = currNode.getToolManager();
          Tool tool = toolManager.getTool(toolId);
          if (tool instanceof ListWSDLServicesTool)
            listManagerKey = WsilModelConstants.LIST_MANAGER_WSDL_SERVICES;
          else if (tool instanceof ListUDDIServicesTool)
            listManagerKey = WsilModelConstants.LIST_MANAGER_UDDI_SERVICES;
          else if (tool instanceof ListUDDIBusinessTool)
            listManagerKey = WsilModelConstants.LIST_MANAGER_UDDI_LINKS;
          else if (tool instanceof ListWSILLinksTool)
            listManagerKey = WsilModelConstants.LIST_MANAGER_WSIL_LINKS;
        }
        catch (NumberFormatException nfe)
        {
        }
      }
    }
    return super.normalize(properties);
  }

  public Hashtable deNormalize(Hashtable properties)
  {
    Node currNode = controller.getCurrentPerspective().getNodeManager().getSelectedNode();
    if (currNode != null)
    {
      TreeElement currElement = currNode.getTreeElement();
      if (currElement instanceof WsilElement)
      {
        WsilElement wsilElement = (WsilElement) currElement;
        switch (type)
        {
          case WSDL_SERVICE :
            wsilElement.getAllWSDLServices();
            break;
          case UDDI_SERVICE :
            wsilElement.getAllUDDIServices();
            break;
          case UDDI_BUSINESS :
            wsilElement.getAllUDDILinks();
            break;
          case WSIL_LINK :
            wsilElement.getAllWSILLinks();
            break;
          case FROM_TOOLID :
            try
            {
              int toolId = Integer.parseInt((String) properties.get(ActionInputs.TOOLID));
              ToolManager toolManager = currNode.getToolManager();
              Tool tool = toolManager.getTool(toolId);
              if (tool instanceof ListWSDLServicesTool)
              {
                listManagerKey = WsilModelConstants.LIST_MANAGER_WSDL_SERVICES;
                wsilElement.getAllWSDLServices();
              }
              else if (tool instanceof ListUDDIServicesTool)
              {
                listManagerKey = WsilModelConstants.LIST_MANAGER_UDDI_SERVICES;
                wsilElement.getAllUDDIServices();
              }
              else if (tool instanceof ListUDDIBusinessTool)
              {
                listManagerKey = WsilModelConstants.LIST_MANAGER_UDDI_LINKS;
                wsilElement.getAllUDDILinks();
              }
              else if (tool instanceof ListWSILLinksTool)
              {
                listManagerKey = WsilModelConstants.LIST_MANAGER_WSIL_LINKS;
                wsilElement.getAllWSILLinks();
              }
            }
            catch (NumberFormatException nfe)
            {
            }
            break;
          default :
            break;
        }
      }
    }
    return super.deNormalize(properties);
  }
}

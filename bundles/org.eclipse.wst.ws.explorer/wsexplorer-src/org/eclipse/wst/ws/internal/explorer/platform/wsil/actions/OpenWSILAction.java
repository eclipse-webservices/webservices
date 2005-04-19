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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.FormAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServiceEntity;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

import org.apache.wsil.WSILDocument;

import java.util.Enumeration;
import java.util.List;
import java.util.Iterator;

public class OpenWSILAction extends FormAction
{

  public OpenWSILAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String wsilURL = parser.getParameter(WsilActionInputs.WSIL_URL);
    String inspectionType = parser.getParameter(WsilActionInputs.WSIL_INSPECTION_TYPE);
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    MessageQueue messageQueue = wsilPerspective.getMessageQueue();
    FormTool formTool = getSelectedFormTool();
    propertyTable_.put(WsilActionInputs.WSIL_URL, wsilURL);
    if (inspectionType != null && inspectionType.length() > 0)
      propertyTable_.put(WsilActionInputs.WSIL_INSPECTION_TYPE, inspectionType);
    formTool.updatePropertyTable(propertyTable_);
    return true;
  }

  public FormTool getSelectedFormTool()
  {
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    return (FormTool)wsilPerspective.getNodeManager().getSelectedNode().getToolManager().getSelectedTool();
  }

  public boolean run()
  {
    String wsilURL = (String)propertyTable_.get(WsilActionInputs.WSIL_URL);
    int inspectionType = Integer.parseInt((String)propertyTable_.get(WsilActionInputs.WSIL_INSPECTION_TYPE));
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    MessageQueue msgQueue = wsilPerspective.getMessageQueue();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node rootNode = nodeManager.getRootNode();
    TreeElement root = rootNode.getTreeElement();
    WsilElement wsilElement = getOpenedWSIL(root, wsilURL);
    if (wsilElement == null)
    {
      WebServicesParserExt parser = new WebServicesParserExt(wsilURL);
      try
      {
        parser.parse(WebServicesParser.PARSE_NONE);
        WebServiceEntity wsEntity = parser.getWebServiceEntityByURI(wsilURL);
        int type = wsEntity.getType();
        if (type == WebServiceEntity.TYPE_HTML)
          return openHTML(wsEntity.getChildren(), inspectionType);
        else if (type == WebServiceEntity.TYPE_WSIL)
          return openWSIL(wsilURL, parser.getWSILDocument(wsilURL), inspectionType);
        else
          msgQueue.addMessage(wsilPerspective.getMessage("MSG_ERROR_INVALID_WSIL_URL", wsilURL));
      }
      catch (Throwable t)
      {
        msgQueue.addMessage(t.getMessage());
      }
      return false;
    }
    else
    {
      preselectWSILNode(nodeManager, rootNode.getChildNode(wsilElement).getNodeId(), inspectionType);
      msgQueue.addMessage(wsilPerspective.getMessage("MSG_ERROR_WSIL_ALREADY_OPENED", wsilURL));
      return true;
    }
  }

  private boolean openHTML(List wsils, int inspectionType)
  {
    int nodeId = -1;
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    MessageQueue msgQueue = wsilPerspective.getMessageQueue();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node rootNode = nodeManager.getRootNode();
    TreeElement root = rootNode.getTreeElement();
    for (Iterator it = wsils.iterator(); it.hasNext();)
    {
      WebServiceEntity wsilEntity = (WebServiceEntity)it.next();
      String wsilURL = wsilEntity.getURI();
      WsilElement wsilElement = getOpenedWSIL(root, wsilURL);
      if (wsilElement == null)
      {
        try
        {
          WSILDocument wsilDoc = WSILDocument.newInstance();
          wsilDoc.read(wsilURL);
          wsilElement = new WsilElement(wsilURL, root.getModel(), wsilDoc, wsilURL);
          root.connect(wsilElement, WsilModelConstants.REL_WSIL, ModelConstants.REL_OWNER);
          nodeId = rootNode.getChildNode(wsilElement).getNodeId();
          msgQueue.addMessage(wsilPerspective.getMessage("MSG_INFO_OPEN_WSIL_SUCCESSFUL", wsilURL));
        }
        catch (Throwable t)
        {
          msgQueue.addMessage(t.getMessage());
        }
      }
      else
      {
        msgQueue.addMessage(wsilPerspective.getMessage("MSG_ERROR_WSIL_ALREADY_OPENED", wsilURL));
        nodeId = rootNode.getChildNode(wsilElement).getNodeId();
      }
    }
    if (nodeId != -1)
    {
      preselectWSILNode(nodeManager, nodeId, inspectionType);
      return true;
    }
    else
      return false;
  }

  private boolean openWSIL(String wsilURL, WSILDocument wsilDoc, int inspectionType)
  {
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    MessageQueue msgQueue = wsilPerspective.getMessageQueue();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node rootNode = nodeManager.getRootNode();
    TreeElement root = rootNode.getTreeElement();
    WsilElement wsilElement = new WsilElement(wsilURL, root.getModel(), wsilDoc, wsilURL);
    root.connect(wsilElement, WsilModelConstants.REL_WSIL, ModelConstants.REL_OWNER);
    int nodeId = rootNode.getChildNode(wsilElement).getNodeId();
    msgQueue.addMessage(wsilPerspective.getMessage("MSG_INFO_OPEN_WSIL_SUCCESSFUL", wsilURL));
    preselectWSILNode(nodeManager, nodeId, inspectionType);
    return true;
  }

  private void preselectWSILNode(NodeManager nodeManager, int nodeId, int inspectionType)
  {
    nodeManager.setSelectedNodeId(nodeId);
    Node selectedNode = nodeManager.getNode(nodeId);
    selectedNode.setViewId(ActionInputs.VIEWTOOLID_DEFAULT);
    ToolManager toolManager = selectedNode.getToolManager();
    toolManager.setSelectedToolId(inspectionType);
    addToHistory(ActionInputs.PERSPECTIVE_WSIL, SelectWSILToolAction.getActionLink(nodeId, inspectionType, ActionInputs.VIEWID_DEFAULT, ActionInputs.VIEWTOOLID_DEFAULT, true));        
  }

  private WsilElement getOpenedWSIL(TreeElement root, String wsilURL)
  {
    Enumeration e = root.getElements(WsilModelConstants.REL_WSIL);
    while (e.hasMoreElements())
    {
      WsilElement wsilElement = (WsilElement)e.nextElement();
      if (wsilElement.getWsilUrl().equals(wsilURL))
        return wsilElement;
    }
    return null;
  }
}

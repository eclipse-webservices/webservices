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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.wsdl.Service;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.eclipse.wst.ws.internal.datamodel.BasicModel;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Perspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.OpenWSDLAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.SwitchPerspectiveFromWSDLAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.UpdateWSDLBindingAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.BindingTypes;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLBindingElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLServiceElement;
import org.eclipse.xsd.impl.XSDPackageImpl;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

public class WSDLPerspective extends Perspective
{
  public static final int STATUS_CONTENT_DEFAULT = 0;
  public static final int STATUS_CONTENT_RESULT_FORM = 1;
  public static final int STATUS_CONTENT_RESULT_SOURCE = 2;

  private BasicModel model_;
  private NodeManager nodeManager_;
  private String perspectiveContentFramesetCols_;
  private String savedPerspectiveContentFramesetCols_;
  private String actionsContainerFramesetRows_;
  private String savedActionsContainerFramesetRows_;
  private int statusContentType_;

  private SOAPMessageQueue soapRequestQueue_;
  private SOAPMessageQueue soapResponseQueue_;
  private Node operationNode_;

  static
  {
    // Initialize the XSD subsystem.
    (new ResourceFactoryRegistryImpl()).getExtensionToFactoryMap().put("xsd",new XSDResourceFactoryImpl());
    // port to org.eclipse.xsd
    // Init.init();
    EcorePackageImpl.init();
    XSDPackageImpl.init();
  }

  public WSDLPerspective(Controller controller)
  {
    super("wsdl",controller);
  }

  public final void initPerspective(ServletContext application)
  {
    model_ = new BasicModel("wsdlModel");
    TreeElement treeElement = new TreeElement(getMessage("NODE_NAME_WSDL_MAIN"), model_);
    model_.setRootElement(treeElement);
    nodeManager_ = new NodeManager(controller_);
    WSDLMainNode wsdlMainNode = new WSDLMainNode(treeElement, nodeManager_);
    nodeManager_.setRootNode(wsdlMainNode);

    // Starting frameset sizes.
    if (!DirUtils.isRTL())
      perspectiveContentFramesetCols_ = "30%,*";
    else
      perspectiveContentFramesetCols_ = "*,30%";
    savedPerspectiveContentFramesetCols_ = perspectiveContentFramesetCols_;
    actionsContainerFramesetRows_ = "75%,*";
    savedActionsContainerFramesetRows_ = actionsContainerFramesetRows_;

    // Message status pane
    statusContentType_ = STATUS_CONTENT_DEFAULT;
    soapRequestQueue_ = new SOAPMessageQueue();
    soapResponseQueue_ = new SOAPMessageQueue();
    operationNode_ = null;
  }

  public final void preloadWSDL(String[] wsdlURLs) {
    if (wsdlURLs != null) {
      for (int i = 0; i < wsdlURLs.length; i++) {
        OpenWSDLAction openWSDLAction = new OpenWSDLAction(controller_);
        Hashtable propertyTable = openWSDLAction.getPropertyTable();
        String decodedWSDLURL = URLUtils.decode(wsdlURLs[i]);
        propertyTable.put(ActionInputs.QUERY_INPUT_WSDL_URL, decodedWSDLURL);
        openWSDLAction.run();
      }
      if (wsdlURLs.length > 0)
        controller_.setCurrentPerspective(ActionInputs.PERSPECTIVE_WSDL);
    }
  }
  
  public final void preloadEndpoints(String[] wsdlURLs, String[] endpoints)
  {
    if (wsdlURLs != null && wsdlURLs.length > 0 && endpoints != null && endpoints.length > 0)
    {
      String wsdlURL = wsdlURLs[0];
      Vector wsdlNodes = nodeManager_.getRootNode().getChildNodes();
      for (Iterator wsdlNodesIterator = wsdlNodes.iterator(); wsdlNodesIterator.hasNext();)
      {
        Node wsdlNode = (Node)wsdlNodesIterator.next();
        if (wsdlNode.getNodeName().equals(wsdlURL))
        {
          Vector serviceNodes = wsdlNode.getChildNodes();
          for (Iterator serviceNodesIterator = serviceNodes.iterator(); serviceNodesIterator.hasNext();)
          {
            Node serviceNode = (Node)serviceNodesIterator.next();
            Vector bindingNodes = serviceNode.getChildNodes();
            String[] nodeIds = new String[bindingNodes.size()];
            for (int i = 0; i < nodeIds.length; i++)
              nodeIds[i] = String.valueOf(((Node)bindingNodes.get(i)).getNodeId());
            UpdateWSDLBindingAction action = new UpdateWSDLBindingAction(controller_);
            Hashtable propertyTable = new Hashtable();
            propertyTable.put(ActionInputs.NODEID, nodeIds);
            propertyTable.put(WSDLActionInputs.END_POINT, endpoints);
            action.setPropertyTable(propertyTable);
            action.execute(false);
          }
          return;
        }
      }
    }
  }
  
  public final void preselectServiceOrBinding(String[] wsdlURLs,String[] serviceQNameStrings,String[] bindingNameStrings)
  {
    if (wsdlURLs != null && wsdlURLs.length > 0)
    {
      String wsdlURL = wsdlURLs[0];
      if (serviceQNameStrings != null && serviceQNameStrings.length > 0)
        preselectService(wsdlURL,serviceQNameStrings[0]);
      else
      {
        if (bindingNameStrings != null && bindingNameStrings.length > 0)
          preselectBinding(wsdlURL,bindingNameStrings[0]);
      }
    }
  }
  
  private final void preselectService(String wsdlURL,String serviceQNameString)
  {
    Vector wsdlNodes = nodeManager_.getRootNode().getChildNodes();
    for (int i=0;i<wsdlNodes.size();i++)
    {
      Node wsdlNode = (Node)wsdlNodes.elementAt(i);
      if (wsdlNode.getNodeName().equals(wsdlURL))
      {
        Vector serviceNodes = wsdlNode.getChildNodes();
        for (int j=0;j<serviceNodes.size();j++)
        {
          Node serviceNode = (Node)serviceNodes.elementAt(j);
          WSDLServiceElement serviceElement = (WSDLServiceElement)serviceNode.getTreeElement();
          Service service = serviceElement.getService();
          if (service.getQName().toString().equals(serviceQNameString))
          {
            nodeManager_.setSelectedNodeId(serviceNode.getNodeId());
            nodeManager_.makeSelectedNodeVisible();
            controller_.setCurrentPerspective(ActionInputs.PERSPECTIVE_WSDL);
            return;
          }
        }
      }
    }
  }
  
  private final void preselectBinding(String wsdlURL,String bindingNameString)
  {
    Vector wsdlNodes = nodeManager_.getRootNode().getChildNodes();
    for (int i=0;i<wsdlNodes.size();i++)
    {
      Node wsdlNode = (Node)wsdlNodes.elementAt(i);
      if (wsdlNode.getNodeName().equals(wsdlURL))
      {
        Vector serviceNodes = wsdlNode.getChildNodes();
        for (int j=0;j<serviceNodes.size();j++)
        {
          Node serviceNode = (Node)serviceNodes.elementAt(j);
          Vector bindingNodes = serviceNode.getChildNodes();
          for (int k=0;k<bindingNodes.size();k++)
          {
            Node bindingNode = (Node)bindingNodes.elementAt(k);
            WSDLBindingElement bindingElement = (WSDLBindingElement)bindingNode.getTreeElement();
            if (bindingElement.getBinding().getQName().toString().equals(bindingNameString))
            {
              nodeManager_.setSelectedNodeId(bindingNode.getNodeId());
              nodeManager_.makeSelectedNodeVisible();
              controller_.setCurrentPerspective(ActionInputs.PERSPECTIVE_WSDL);
              return;
            }
          }
        }          
      }
    }    
  }

  public NodeManager getNodeManager()
  {
    return nodeManager_;
  }

  public String getPerspectiveContentPage()
  {
    return "wsdl/wsdl_perspective_content.jsp";
  }

  public int getPerspectiveId()
  {
    return ActionInputs.PERSPECTIVE_WSDL;
  }

  public String getPanesFile()
  {
    return "wsdl/scripts/wsdlpanes.jsp";
  }

  public String getFramesetsFile()
  {
    return "wsdl/scripts/wsdlframesets.jsp";
  }

  public String getProcessFramesetsForm()
  {
    return "wsdl/forms/ProcessWSDLFramesetsForm.jsp";
  }

  public String getTreeContentVar()
  {
    return "wsdlNavigatorContent";
  }

  public String getTreeContentPage()
  {
    return "wsdl/wsdl_navigator_content.jsp";
  }

  public String getPropertiesContainerVar()
  {
    return "wsdlPropertiesContainer";
  }

  public String getPropertiesContainerPage()
  {
    return "wsdl/wsdl_properties_container.jsp";
  }

  public String getStatusContentVar()
  {
    return "wsdlStatusContent";
  }

  public String getStatusContentPage()
  {
    return "wsdl/wsdl_status_content.jsp";
  }

  public final String getPerspectiveContentFramesetCols()
  {
    return perspectiveContentFramesetCols_;
  }

  public final void setPerspectiveContentFramesetCols(String cols)
  {
    perspectiveContentFramesetCols_ = cols;
  }

  public final void setSavedPerspectiveContentFramesetCols(String cols)
  {
    savedPerspectiveContentFramesetCols_ = cols;
  }

  public final String getSavedPerspectiveContentFramesetCols()
  {
    return savedPerspectiveContentFramesetCols_;
  }

  public final String getActionsContainerFramesetRows()
  {
    return actionsContainerFramesetRows_;
  }

  public final void setActionsContainerFramesetRows(String rows)
  {
    actionsContainerFramesetRows_ = rows;
  }

  public final void setSavedActionsContainerFramesetRows(String rows)
  {
    savedActionsContainerFramesetRows_ = rows;
  }

  public final String getSavedActionsContainerFramesetRows()
  {
    return savedActionsContainerFramesetRows_;
  }

  public final String getSwitchPerspectiveFormActionLink(int targetPerspectiveId,boolean forHistory)
  {
    return SwitchPerspectiveFromWSDLAction.getFormActionLink(targetPerspectiveId,forHistory);
  }

  public final int getStatusContentType()
  {
    return statusContentType_;
  }

  public final void setStatusContentType(int statusContentType)
  {
    statusContentType_ = statusContentType;
  }

  public final SOAPMessageQueue getSOAPRequestQueue()
  {
    return soapRequestQueue_;
  }

  public final SOAPMessageQueue getSOAPResponseQueue()
  {
    return soapResponseQueue_;
  }

  public final Node getOperationNode()
  {
    return operationNode_;
  }

  public final void setOperationNode(Node operationNode)
  {
    operationNode_ = operationNode;
  }

  public final String getSOAPEnvelopeXMLLink(int soapEnvelopeType)
  {
    StringBuffer link = new StringBuffer("wsdl/soap_envelope_xml.jsp?");
    link.append(WSDLActionInputs.SOAP_ENVELOPE_TYPE).append('=').append(soapEnvelopeType);
    return link.toString();
  }

  public final String getBindingTypeString(int bindingType)
  {
    switch (bindingType)
    {
      case BindingTypes.SOAP:
        return getMessage("FORM_LABEL_BINDING_TYPE_SOAP");
      case BindingTypes.HTTP_GET:
        return getMessage("FORM_LABEL_BINDING_TYPE_HTTP_GET");
      case BindingTypes.HTTP_POST:
        return getMessage("FORM_LABEL_BINDING_TYPE_HTTP_POST");
      default:
        return getMessage("FORM_LABEL_BINDING_TYPE_UNSUPPORTED");
    }
  }
}

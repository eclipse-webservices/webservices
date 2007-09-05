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

import java.util.Vector;
import org.eclipse.wst.ws.internal.datamodel.ElementAdapter;
import org.eclipse.wst.ws.internal.datamodel.RelAddEvent;
import org.eclipse.wst.ws.internal.datamodel.RelRemoveEvent;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.FindAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;

public class QueryNode extends UDDINavigatorNode
{
  private FormTool regFindTool_;
  private FindAction findAction_;
  public QueryNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"uddi/images/query.gif");
    element.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
        String rel = event.getOutBoundRelName();
        if (rel.equals(UDDIModelConstants.REL_QUERY_RESULTS))
        {
          TreeElement treeElement = (TreeElement)event.getParentElement();
          createChildNode(treeElement);
        }
      }

      public void relRemoved(RelRemoveEvent event)
      {
        TreeElement treeElement = null;
        if (event.getInBoundRelName().equals(UDDIModelConstants.REL_QUERY_RESULTS))
          treeElement = (TreeElement)event.getInboundElement();
        else if (event.getOutBoundRelName().equals(UDDIModelConstants.REL_QUERY_RESULTS))
          treeElement = (TreeElement)event.getOutBoundElement();

        if (treeElement != null)
          removeChildNode(treeElement);
      }
    });
    createChildren();
    setVisibilityOfChildren(false);
    findAction_ = null;
  }

  private final void createChildren()
  {
    Vector initialResults = (Vector)element_.getPropertyAsObject(UDDIModelConstants.INITIAL_RESULTS);
    for (int i=0;i<initialResults.size();i++)
    {
      Object object = initialResults.elementAt(i);
      TreeElement childElement = null;
      if (object instanceof BusinessEntity)
        childElement = new BusinessElement((BusinessEntity)object,element_.getModel());
      else if (object instanceof BusinessService)
        childElement = new ServiceElement((BusinessService)object,element_.getModel());
      else if (object instanceof TModel)
        childElement = new ServiceInterfaceElement((TModel)object,element_.getModel());
      if (childElement != null)
        element_.connect(childElement,UDDIModelConstants.REL_QUERY_RESULTS,ModelConstants.REL_OWNER);
    }
  }

  private final void createChildNode(TreeElement element)
  {
    Node childNode = null;
    if (element instanceof BusinessElement)
      childNode = new BusinessNode(element,nodeManager_,nodeDepth_+1);
    else if (element instanceof ServiceElement)
      childNode = new ServiceNode(element,nodeManager_,nodeDepth_+1);
    else if (element instanceof ServiceInterfaceElement)
      childNode = new ServiceInterfaceNode(element,nodeManager_,nodeDepth_+1);
    if (childNode != null)
      addChild(childNode);
  }

  protected final void initTools()
  {
    Controller controller = nodeManager_.getController();
    UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
    new ResultsTool(toolManager_,controller.getMessage("ALT_RESULTS"));
    regFindTool_ = new RegFindTool(toolManager_,uddiPerspective.getMessage("ALT_FIND"));
  }

  public final void setFindToolProperties(FindAction findAction)
  {
    findAction_ = findAction;
    regFindTool_.updatePropertyTable(findAction.getPropertyTable());
  }

  public void addAuthenticationProperties(RegistryElement regElement)
  {
    ((RegFindTool)regFindTool_).addAuthenticationProperties(regElement);
    Vector childNodes = getChildNodes();
    for (int i=0;i<childNodes.size();i++)
    {
      UDDINavigatorNode navigatorNode = (UDDINavigatorNode)childNodes.elementAt(i);
      navigatorNode.addAuthenticationProperties(regElement);
    }
  }

  public final FindAction getFindAction()
  {
    return findAction_;
  }
}

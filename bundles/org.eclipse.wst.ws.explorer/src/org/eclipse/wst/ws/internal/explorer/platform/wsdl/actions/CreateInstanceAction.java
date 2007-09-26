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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.util.Iterator;
import javax.wsdl.Part;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDMapFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.InvokeWSDLOperationTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public class CreateInstanceAction extends WSDLPropertiesFormAction
{
  public CreateInstanceAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    super.processParsedResults(parser);
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    NodeManager nodeManager = wsdlPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    InvokeWSDLOperationTool invokeWSDLOperationTool = (InvokeWSDLOperationTool)(selectedNode.getCurrentToolManager().getSelectedTool());
    WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
    Iterator it = operElement.getOrderedBodyParts().iterator();
    boolean resultsValid = true;
    while (it.hasNext())
    {
      Part part = (Part)it.next();
      IXSDFragment frag = operElement.getFragment(part);
      if (!frag.processParameterValues(parser))
        resultsValid = false;
    }
    String fragmentID = parser.getParameter(FragmentConstants.FRAGMENT_ID);
    if (fragmentID != null && fragmentID.length() > 0)
      propertyTable_.put(FragmentConstants.FRAGMENT_ID, fragmentID);
    else
    {
      propertyTable_.remove(FragmentConstants.FRAGMENT_ID);
      resultsValid = false;
    }
    String nameAnchorID = parser.getParameter(FragmentConstants.NAME_ANCHOR_ID);
    invokeWSDLOperationTool.setFragmentNameAnchorID(nameAnchorID);
    return resultsValid;
  }

  public FormTool getSelectedFormTool()
  {
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    return (FormTool)wsdlPerspective.getNodeManager().getSelectedNode().getToolManager().getSelectedTool();
  }

  public boolean run()
  {
    String fragmentID = (String)propertyTable_.get(FragmentConstants.FRAGMENT_ID);
    if (fragmentID != null && fragmentID.length() > 0)
    {
      WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
      NodeManager nodeManager = wsdlPerspective.getNodeManager();
      Node selectedNode = nodeManager.getSelectedNode();
      WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
      IXSDFragment frag = operElement.getFragmentByID(fragmentID);
      if ((frag instanceof IXSDMapFragment) && ((IXSDMapFragment)frag).createInstance() != null)
        return true;
      else
        return false;
    }
    else
      return false;
  }
}

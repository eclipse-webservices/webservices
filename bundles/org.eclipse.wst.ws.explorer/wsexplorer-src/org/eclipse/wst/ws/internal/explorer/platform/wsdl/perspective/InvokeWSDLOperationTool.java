/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*;

public class InvokeWSDLOperationTool extends FormTool {

  private String fragmentViewID_;
  private String fragmentNameAnchorID_;
  private String endPoint_;

  public InvokeWSDLOperationTool(ToolManager toolManager, String alt) {
    super(toolManager, "wsdl/images/invoke_wsdl_operation_enabled.gif", "wsdl/images/invoke_wsdl_operation_highlighted.gif", alt);
    fragmentViewID_ = FragmentConstants.FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM;
    fragmentNameAnchorID_ = null;
    endPoint_ = null;
  }

  protected void initDefaultProperties() {
  }

  public String getSelectToolActionHref(boolean forHistory) {
    Node selectedNode = toolManager_.getNode();
    return SelectWSDLPropertiesToolAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(), forHistory);
  }

  public String getFormLink() {
    StringBuffer formLink = new StringBuffer();
    formLink.append("wsdl/forms/InvokeWSDLOperationForm.jsp");
    String nameAnchorID = getFragmentNameAnchorID();
    if (nameAnchorID != null && nameAnchorID.length() > 0) {
      formLink.append("#");
      formLink.append(nameAnchorID);
      setFragmentNameAnchorID(null);
    }
    return formLink.toString();
  }

  public String getFormActionLink(int operationType,String fragmentViewID)
  {
    switch (operationType)
    {
      case WSDLOperationElement.OPERATION_TYPE_SOAP:
        if (fragmentViewID.equals(FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE))
          return "wsdl/actions/InvokeWSDLSOAPOperationSourceActionJSP.jsp";
        else
          return "wsdl/actions/InvokeWSDLSOAPOperationFormActionJSP.jsp";
      case WSDLOperationElement.OPERATION_TYPE_HTTP_GET:
        return "wsdl/actions/InvokeWSDLHttpGetOperationFormActionJSP.jsp";
      case WSDLOperationElement.OPERATION_TYPE_HTTP_POST:
      default:
        return "wsdl/actions/InvokeWSDLHttpPostOperationFormActionJSP.jsp";
    }
  }

  public String getFragmentViewID() {
    return fragmentViewID_;
  }

  public void setFragmentViewID(String fragmentViewID) {
    fragmentViewID_ = fragmentViewID;
  }

  public String getFragmentNameAnchorID() {
    return fragmentNameAnchorID_;
  }

  public void setFragmentNameAnchorID(String fragmentNameAnchorID) {
    fragmentNameAnchorID_ = fragmentNameAnchorID;
  }

  public String getEndPoint()
  {
    return endPoint_;
  }

  public void setEndPoint(String endPoint)
  {
    endPoint_ = endPoint;
  }
}

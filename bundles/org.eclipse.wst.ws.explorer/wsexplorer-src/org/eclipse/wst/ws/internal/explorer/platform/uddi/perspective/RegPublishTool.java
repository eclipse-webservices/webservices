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

import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectFindToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;

public class RegPublishTool extends FormTool implements MultipleFormToolPropertiesInterface
{
  ToolManager invisibleToolManager_;
  private RegFindTool regFindTool_;
  public RegPublishTool(ToolManager toolManager,String alt)
  {
    super(toolManager,"uddi/images/publish_enabled.gif","uddi/images/publish_highlighted.gif",alt);
    invisibleToolManager_ = new ToolManager(toolManager.getNode());
    regFindTool_ = new RegFindTool(invisibleToolManager_,"");
  }

  protected final void initDefaultProperties()
  {
    toolManager_.getNode().getNodeManager().getController().getUDDIPerspective();

    setProperty(UDDIActionInputs.SUBQUERY_KEY,"");

    setProperty(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES));
    String simpleStyleString = String.valueOf(UDDIActionInputs.QUERY_STYLE_SIMPLE);
    setProperty(UDDIActionInputs.QUERY_STYLE_BUSINESSES,simpleStyleString);
    setProperty(UDDIActionInputs.QUERY_STYLE_SERVICES,simpleStyleString);
    setProperty(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES,simpleStyleString);

    // RegPublishBusinessSimpleAction inputs
    setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME,"");
    setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_DESCRIPTION,"");

    // RegPublishServiceSimpleAction inputs
    setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_WSDL_URL,"");
    setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME,"");
    setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION,"");

    // RegPublishServiceAdvancedAction inputs
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL,"");

    // RegPublishServiceInterfaceSimpleAction inputs
    setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_WSDL_URL,"");
    setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME,"");
    setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION,"");

    // RegPublishServiceInterfaceAdvancedAction inputs
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL,"");
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME,"");
  }

  public final void addAuthenticationProperties(RegistryElement regElement)
  {
    String publishURL = regElement.getPublishURL();
    String userId = regElement.getUserId();
    String password = regElement.getCred();

    if (publishURL == null)
      publishURL = "";
    if (userId == null)
      userId = "";
    if (password == null)
      password = "";

    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userId);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
    regFindTool_.addAuthenticationProperties(regElement);
  }

  public final FormToolPropertiesInterface getFormToolProperties(Object subQueryKeyObject)
  {
    // Empty/Non-existent subquerykey implies publish form. All else implies query form.
    String subQueryKey = (String)subQueryKeyObject;
    if (subQueryKey == null || subQueryKey.length() < 1)
      return this;
    else
      return regFindTool_.getFormToolProperties(subQueryKeyObject);
  }

  public String getSelectToolActionHref(boolean forHistory)
  {
    Node node = toolManager_.getNode();
    String subQueryKey = (String)getProperty(UDDIActionInputs.SUBQUERY_KEY);
    return SelectFindToolAction.getActionLink(node.getNodeId(),toolId_,node.getViewId(),node.getViewToolId(),subQueryKey,forHistory);
  }

  public String getFormLink()
  {
    Object subQueryKeyObject = getProperty(UDDIActionInputs.SUBQUERY_KEY);
    String subQueryKey = null;
    if (subQueryKeyObject != null)
    {
      getProperty(UDDIActionInputs.SUBQUERIES_PROPERTIES);
      FormToolPropertiesInterface subQueryProperties = getFormToolProperties(subQueryKeyObject);
      Object subQueryGetObject = subQueryProperties.getProperty(UDDIActionInputs.SUBQUERY_GET);
      if (subQueryGetObject != null)
      {
        boolean isSubQueryGet = ((Boolean)subQueryGetObject).booleanValue();
        if (isSubQueryGet)
          return "uddi/forms/GetForm.jsp";
      }
      subQueryKey = (String)subQueryKeyObject;
    }
      
    if (subQueryKey == null || subQueryKey.length() < 1)
      return "uddi/forms/RegPublishForm.jsp";
    else
      return regFindTool_.getFormLink();
  }
}

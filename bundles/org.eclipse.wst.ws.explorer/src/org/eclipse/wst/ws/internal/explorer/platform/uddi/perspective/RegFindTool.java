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

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolProperties;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectFindToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;

public class RegFindTool extends FormTool implements MultipleFormToolPropertiesInterface
{
  public RegFindTool(ToolManager toolManager,String alt)
  {
    super(toolManager,"uddi/images/find_enabled.gif","uddi/images/find_highlighted.gif",alt);
  }

  protected final void initDefaultProperties()
  {
    initDefaultProperties(this);
    setProperty(UDDIActionInputs.SUBQUERY_KEY,"");
    setProperty(UDDIActionInputs.SUBQUERIES_PROPERTIES,new Hashtable());
    UDDIPerspective uddiPerspective = toolManager_.getNode().getNodeManager().getController().getUDDIPerspective();
    setProperty(UDDIActionInputs.QUERY_NAME,uddiPerspective.getMessage("DEFAULT_QUERY_NAME"));
  }

  private final void initDefaultProperties(FormToolPropertiesInterface formToolPI)
  {
    toolManager_.getNode().getNodeManager().getController().getUDDIPerspective();

    formToolPI.setProperty(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES));
    String simpleStyleString = String.valueOf(UDDIActionInputs.QUERY_STYLE_SIMPLE);
    formToolPI.setProperty(UDDIActionInputs.QUERY_STYLE_BUSINESSES,simpleStyleString);
    formToolPI.setProperty(UDDIActionInputs.QUERY_STYLE_SERVICES,simpleStyleString);
    formToolPI.setProperty(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES,simpleStyleString);

    String maxSearchSet = String.valueOf(UDDIActionInputs.QUERY_MAX_SEARCH_SET);
    String maxResults = String.valueOf(UDDIActionInputs.QUERY_MAX_RESULTS);

    // RegFindBusinessesSimpleAction inputs
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME,"");

    // RegFindBusinessesAdvancedAction inputs
    // The authentication inputs must be added later as this method is called before we can get a valid handle
    // to the RegistryElement in the case of a query node.
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_SEARCH_SET,maxSearchSet);
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_RESULTS,maxResults);

    // RegFindServicesSimpleAction inputs
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME,"");

    // RegFindServicesAdvancedAction inputs
    // The authentication inputs must be added later as this method is called before we can get a valid handle
    // to the RegistryElement in the case of a query node.
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_SEARCH_SET,maxSearchSet);
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_RESULTS,maxResults);

    // RegFindServiceInterfacesSimpleAction inputs
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME,"");

    // RegFindServiceInterfacesAdvancedAction inputs
    // The authentication inputs must be added later as this method is called before we can get a valid handle
    // to the RegistryElement in the case of a query node.
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME,"");
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_SEARCH_SET,maxSearchSet);
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_RESULTS,maxResults);
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
  }

  public final FormToolPropertiesInterface getFormToolProperties(Object subQueryKeyObject)
  {
    String subQueryKey = (String)subQueryKeyObject;
    if (subQueryKey == null || subQueryKey.length() < 1)
      return this;
    Hashtable subQueriesProperties = (Hashtable)getProperty(UDDIActionInputs.SUBQUERIES_PROPERTIES);
    FormToolPropertiesInterface subQueryProperties = (FormToolPropertiesInterface)subQueriesProperties.get(subQueryKey);
    if (subQueryProperties == null)
    {
      subQueryProperties = new FormToolProperties();
      initDefaultProperties(subQueryProperties);
      subQueriesProperties.put(subQueryKey,subQueryProperties);
    }
    return subQueryProperties;
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
    }
    return "uddi/forms/RegFindForm.jsp";
  }
}

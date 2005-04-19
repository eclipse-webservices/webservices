<%
/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs,
                                                        org.eclipse.wst.ws.internal.explorer.platform.engine.ActionEngine,
                                                        org.eclipse.wst.ws.internal.explorer.platform.engine.ActionDataParser,
                                                        org.eclipse.wst.ws.internal.explorer.platform.engine.data.ScenarioDescriptor,
                                                        org.eclipse.wst.ws.internal.explorer.platform.engine.data.TransactionDescriptor,
                                                        org.eclipse.wst.ws.internal.explorer.platform.engine.data.ActionDescriptor,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser,
                                                        java.util.List,
                                                        java.util.Iterator" %>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
String mode = request.getParameter(ActionInputs.ACTION_ENGINE_MODE);
String scenario = null;
try
{
  MultipartFormDataParser parser = new MultipartFormDataParser();
  parser.parseRequest(request, HTMLUtils.UTF8_ENCODING);
  scenario = parser.getParameter(ActionInputs.ACTION_ENGINE_SCENARIO);
}
catch (Throwable t)
{
}
ActionEngine actionEngine = controller.getActionEngine();
if (mode != null)
{
  actionEngine.setMode(ActionEngine.MODE_DISABLED);
  try
  {
    actionEngine.setMode(Byte.parseByte(mode));
  }
  catch (NumberFormatException nfe)
  {
  }
}
if (scenario != null && scenario.length() > 0)
{
  ScenarioDescriptor scenarioDescriptor = null;
  try
  {
    ActionDataParser parser = new ActionDataParser();
    scenarioDescriptor = parser.parseScenario(XMLUtils.stringToElement(scenario));
    actionEngine.executeScenario(scenarioDescriptor);
  }
  catch (Throwable t)
  {
  }
  if (scenarioDescriptor != null)
  {
    TransactionDescriptor[] transactionDescriptors = scenarioDescriptor.getTransactionDescriptors();
    for (int i = 0; i < transactionDescriptors.length; i++)
    {
      ActionDescriptor[] actionDescriptors = transactionDescriptors[i].getActionDescriptors();
      for (int j = 0; j < actionDescriptors.length; j++)
      {
        %>
        <!--
        <%=actionDescriptors[j].getId()%>
        <%=actionDescriptors[j].getStatusId()%>
        <%
        List status = actionDescriptors[j].getStatus();
        if (status != null)
        {
          for (Iterator it = status.iterator(); it.hasNext();)
          {
            %>
            <%=it.next().toString()%>
            <%
          }
        }
        %>
        -->
        <%
      }
    }
  }
}
%>

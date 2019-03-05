<%
/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.engine.data.ScenarioDescriptor,
                                                        org.eclipse.wst.ws.internal.explorer.platform.engine.ActionDataParser,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils,
                                                        java.io.PrintWriter,
                                                        org.w3c.dom.Element"%>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
response.setContentType("application/octet-stream");
response.setHeader("Content-Disposition","attachment;filename=scenario.xml");
ScenarioDescriptor scenarioDescriptor = controller.getActionEngine().getScenario();
ActionDataParser parser = new ActionDataParser();
Element scenario = parser.toElement(scenarioDescriptor);
if (scenario != null)
{
  try
  {
    String scenarioString = XMLUtils.serialize(scenario, false);
    if (scenarioString != null)
    {
      PrintWriter pw = new PrintWriter(response.getOutputStream());
      pw.println(scenarioString);
      pw.close();
    }
  }
  catch (Throwable t)
  {
  }
}
%>

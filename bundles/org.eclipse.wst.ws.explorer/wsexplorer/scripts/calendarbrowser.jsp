<%
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
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   String sessionId = session.getId();
%>
<script language="javascript">
  var calendarBrowser;
  var calendarBrowserClosed = true;
  var calendarTargetRow = -1;
  var calendarTarget;

  function openCalendarBrowser(type,formContainerId,elementName,index)
  {
    var form = document.getElementById(formContainerId).getElementsByTagName("form").item(0);
    var elements = form.elements[elementName];
    if (index == 0 && elements.length == undefined)
<%
   // Only one element.
%>
      calendarTarget = elements;
    else
    {
      calendarTargetRow = index;
      calendarTarget = elements.item(index);
    }
    var link;
    switch (type)
    {
      case <%=ActionInputs.CALENDAR_TYPE_DATE%>:
        link = "<%=response.encodeURL(controller.getPathWithContext(OpenCalendarBrowserAction.getActionLinkForDate(sessionId)))%>";
        break;
      case <%=ActionInputs.CALENDAR_TYPE_DATETIME%>:
        link = "<%=response.encodeURL(controller.getPathWithContext(OpenCalendarBrowserAction.getActionLinkForDateTime(sessionId)))%>";
        break;
      case <%=ActionInputs.CALENDAR_TYPE_GYEARMONTH%>:
        link = "<%=response.encodeURL(controller.getPathWithContext(OpenCalendarBrowserAction.getActionLinkForGYearMonth(sessionId)))%>";
        break;
      case <%=ActionInputs.CALENDAR_TYPE_GDAY%>:
        link = "<%=response.encodeURL(controller.getPathWithContext(OpenCalendarBrowserAction.getActionLinkForGDay(sessionId)))%>";
        break;
      case <%=ActionInputs.CALENDAR_TYPE_GMONTHDAY%>:
        link = "<%=response.encodeURL(controller.getPathWithContext(OpenCalendarBrowserAction.getActionLinkForGMonthDay(sessionId)))%>";
        break;
    }
    calendarBrowser = window.open(link,"calendarBrowser","height=350,width=264,status=yes,scrollbars=yes,resizable=yes");
    if (calendarBrowser.focus)
      calendarBrowser.focus();
  }

  function closeCalendarBrowser()
  {
    if (!calendarBrowserClosed)
      calendarBrowser.close();
  }
</script>

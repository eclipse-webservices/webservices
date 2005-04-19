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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils,
                                                        java.util.Enumeration,
                                                        java.net.*,
                                                        java.io.*"%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <script language="javascript">
    function initWindowName(name)
    {
      window.name = name;
    }
  </script>
  <%
  String sessionId = session.getId();
  %>
  <jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session">
    <%
    // Add the session to the application. This allows us to resurrect the session even if the browser chooses not to participate.
    application.setAttribute(sessionId,session);

    if (session.getMaxInactiveInterval() > 0)
      session.setMaxInactiveInterval(-1);

    // LaunchOptionManager (below) handles most options,
    // but need to get state and install locations earlier,
    // specifically before controller.init().
    Enumeration paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements())
    {
      String paramName = (String)paramNames.nextElement();
      String[] paramValues = request.getParameterValues(paramName);
      if (paramValues != null && paramValues.length > 0)
      {
        String decodedParamName = URLUtils.decode(paramName);
        if (decodedParamName.equals(LaunchOptions.DEFAULT_FAVORITES_LOCATION))
        {
          controller.setDefaultFavoritesLocation(paramValues[0]);
        }
        else if (decodedParamName.equals(LaunchOptions.STATE_LOCATION))
        {
          controller.setStateLocation(paramValues[0]);
        }
      }
    }

    // controller.init()
    controller.init(sessionId,application,request.getContextPath());
    %>
  </jsp:useBean>
  <%
  // preload from LaunchOptionManager
  String key = request.getParameter(URLUtils.encode(WSExplorerContext.ID));
  if (key != null && key.length() > 0)
  {
    LaunchOptionsManager manager = LaunchOptionsManager.getInstance();
    manager.manage(key, sessionId, application);
  }
  %>
  <jsp:include page="/actionengine.jsp" flush="true"/>
  <title><%=controller.getMessage("TITLE_WSEXPLORER")%></title>
</head>
<%
// reset perspective content to blank
controller.enablePerspectiveContentBlank(true);
%>
<frameset rows="0,35,*" border=0 onload="javascript:initWindowName('<%=FrameNames.WINDOW_NAME_WSEXPLORER_JSP%>')">
  <frame name="<%=FrameNames.PERSPECTIVE_WORKAREA%>" title="<%=controller.getMessage("FRAME_TITLE_PERSPECTIVE_WORKAREA")%>" frameborder=0 noresize>
  <frame name="<%=FrameNames.PERSPECTIVE_TOOLBAR%>" title="<%=controller.getMessage("FRAME_TITLE_PERSPECTIVE_TOOLBAR")%>" src="<%=response.encodeURL(controller.getPathWithContext("perspective_toolbar.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=0 noresize>
  <frame name="<%=FrameNames.PERSPECTIVE_CONTENT%>" title="<%=controller.getMessage("FRAME_TITLE_PERSPECTIVE_CONTENT")%>" src="<%=response.encodeURL(controller.getPathWithContext("perspective_content.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=0>
</frameset>
</html>

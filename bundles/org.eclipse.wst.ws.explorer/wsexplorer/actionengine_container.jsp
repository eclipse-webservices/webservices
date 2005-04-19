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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.engine.ActionEngine,
                                                        java.net.*" %>
<%
String sessionId = request.getParameter(ActionInputs.SESSIONID);
HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
Controller controller = (Controller)currentSession.getAttribute("controller");
ActionEngine actionEngine = controller.getActionEngine();
String modeString = request.getParameter(ActionInputs.ACTION_ENGINE_MODE);
if (modeString != null && modeString.length() > 0)
{
  try
  {
    byte mode = Byte.parseByte(modeString);
    actionEngine.setMode(mode);
  }
  catch (Throwable t)
  {
  }
}
String playFileId = "playFileId";
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=controller.getMessage("TITLE_ACTION_RECORDER")%></title>
  <script language="javascript">
    function setMode(mode)
    {
      <%
      StringBuffer formAction = new StringBuffer(response.encodeURL(controller.getPathWithContext("actionengine_container.jsp?")));
      formAction.append(ActionInputs.SESSIONID);
      formAction.append("=");
      formAction.append(sessionId);
      formAction.append("&");
      formAction.append(ActionInputs.ACTION_ENGINE_MODE);
      formAction.append("=");
      %>
      var form = document.forms[0];
      form.action = '<%=formAction.toString()%>' + mode;
      form.target = "_self";
      form.submit();
    }

    function play()
    {
      var playFile = document.getElementById("<%=playFileId%>");
      if (playFile.value != null && playFile.value.length > 0)
      {
        var form = document.forms[0];
        form.action = '<%=response.encodeURL(controller.getPathWithContext("wsexplorer.jsp"))%>';
        form.target = "<%=FrameNames.WINDOW_NAME_WSEXPLORER_JSP%>";
        form.enctype = "multipart/form-data";
        // for backward compatibility
        form.encoding = "multipart/form-data";
        form.submit();
        setMode('<%=ActionEngine.MODE_PLAY%>');
      }
      else
        alert('<%=controller.getMessage("MSG_ERROR_INVALID_PLAY_FILE")%>');
    }

    function save()
    {
      var form = document.forms[0];
      form.action = '<%=response.encodeURL(controller.getPathWithContext("actions/SaveActionEngineScenario.jsp"))%>';
      form.target = "_self";
      form.submit();
    }
  </script>
</head>
<body>
  <form method="post">
    <%
    String labelModePlay = controller.getMessage("FORM_LABEL_MODE_PLAY");
    String labelModeStop = controller.getMessage("FORM_LABEL_MODE_STOP");
    String labelModeRecord = controller.getMessage("FORM_LABEL_MODE_RECORD");
    String labelSave = controller.getMessage("FORM_LABEL_SAVE");
    byte mode = actionEngine.getMode();
    String imgPlay = (mode == ActionEngine.MODE_PLAY) ? "images/elcl16/actionengine_play.gif" : "images/dlcl16/actionengine_play.gif";
    String imgStop = (mode == ActionEngine.MODE_STOP) ? "images/elcl16/actionengine_stop.gif" : "images/dlcl16/actionengine_stop.gif";
    String imgRecord = (mode == ActionEngine.MODE_RECORD) ? "images/elcl16/actionengine_record.gif" : "images/dlcl16/actionengine_record.gif";
    String imgSave = "images/dlcl16/actionengine_save.gif";
    %>
    <table border=0 cellpadding=3 cellspacing=3>
      <tr>
        <td valign="middle" align="left" class="labels" nowrap>
          <a href="javascript:play()">
            <img class="normal" border=0 alt="<%=labelModePlay%>" title="<%=labelModePlay%>" src="<%=response.encodeURL(controller.getPathWithContext(imgPlay))%>">
            <%=controller.getMessage("FORM_LABEL_MODE_PLAY")%>
          </a>
        </td>
        <td valign="middle" align="left" class="labels" nowrap>
          <a href="javascript:setMode('<%=String.valueOf(ActionEngine.MODE_STOP)%>')">
            <img class="normal" border=0 alt="<%=labelModeStop%>" title="<%=labelModeStop%>" src="<%=response.encodeURL(controller.getPathWithContext(imgStop))%>">
            <%=controller.getMessage("FORM_LABEL_MODE_STOP")%>
          </a>
        </td>
        <td valign="middle" align="left" class="labels" nowrap>
          <a href="javascript:setMode('<%=String.valueOf(ActionEngine.MODE_RECORD)%>')">
            <img class="normal" border=0 alt="<%=labelModeRecord%>" title="<%=labelModeRecord%>" src="<%=response.encodeURL(controller.getPathWithContext(imgRecord))%>">
            <%=controller.getMessage("FORM_LABEL_MODE_RECORD")%>
          </a>
        </td>
        <td valign="middle" align="left" class="labels" nowrap>
          <a href="javascript:save()">
            <img class="normal" border=0 alt="<%=labelSave%>" title="<%=labelSave%>" src="<%=response.encodeURL(controller.getPathWithContext(imgSave))%>">
            <%=controller.getMessage("FORM_LABEL_SAVE")%>
          </a>
        </td>
      </tr>
    </table>
    <table border=0 cellpadding=3 cellspacing=3>
      <tr>
        <td valign="center" align="left" class="labels" nowrap><%=controller.getMessage("FORM_LABEL_PLAY_FILE")%></td>
        <td valign="center" align="left" class="labels">
          <input type="file" id="<%=playFileId%>" name="<%=ActionInputs.ACTION_ENGINE_SCENARIO%>">
        </td>
      </tr>
    </table>
  </form>
</body>
</html>

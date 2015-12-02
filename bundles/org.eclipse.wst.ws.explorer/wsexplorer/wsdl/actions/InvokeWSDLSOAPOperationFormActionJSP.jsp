<%
/*******************************************************************************
 * Copyright (c) 2001, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8"
	import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport.HTTPTransport,
                                                        org.eclipse.wst.ws.internal.explorer.transport.HTTPTransportException,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLFrameNames, 
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils,
                                                        sun.misc.BASE64Decoder,
                                                        javax.servlet.http.HttpServletResponse"%>



<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
// Prepare the action.
InvokeWSDLSOAPOperationFormAction action = new InvokeWSDLSOAPOperationFormAction(controller);
String httpBasicAuthData = request.getHeader(HTTPTransport.HTTP_HEADER_AUTH);
if (httpBasicAuthData != null && httpBasicAuthData.length() > 0)
{
  int basicIndex = httpBasicAuthData.indexOf(HTTPTransport.BASIC);
  if (basicIndex != -1)
  {
    BASE64Decoder decoder = new BASE64Decoder();
    httpBasicAuthData = new String(decoder.decodeBuffer(httpBasicAuthData.substring(basicIndex + HTTPTransport.BASIC.length() + 1)));
    int colonIndex = httpBasicAuthData.indexOf(HTTPTransport.COLON);
    if (colonIndex != -1)
    {
      action.addProperty(WSDLActionInputs.HTTP_BASIC_AUTH_USERNAME, httpBasicAuthData.substring(0, colonIndex));
      action.addProperty(WSDLActionInputs.HTTP_BASIC_AUTH_PASSWORD, httpBasicAuthData.substring(colonIndex + 1, httpBasicAuthData.length()));
    }
  }
}

// Load the parameters for the action from the servlet request.
boolean inputsValid = action.populatePropertyTable(request);
if (!inputsValid)
{
%>
  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
    <head>
		<script language="javascript">
		  var perspectiveWorkArea = top.frames["<%=FrameNames.PERSPECTIVE_WORKAREA%>"];
		  var perspectiveToolbar = top.frames["<%=FrameNames.PERSPECTIVE_TOOLBAR%>"];
		  var perspectiveContent = top.frames["<%=FrameNames.PERSPECTIVE_CONTENT%>"];
		
		  function getPerspectiveContentFrameset()
		  {
		    return perspectiveContent.document.getElementsByTagName("frameset").item(0);
		  }
		
		  function toggleDoubleClickColumnTitle()
		  {
		    var doubleClickColumn = document.getElementById("doubleclickcolumn");
		    if (doubleClickColumn == null)
		      return;
		<%String jsAltRestore = HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_RESTORE"));%>
		    if (doubleClickColumn.title == "<%=jsAltRestore%>")
		      doubleClickColumn.title = "<%=HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_MAXIMIZE"))%>";
		    else
		      doubleClickColumn.title = "<%=jsAltRestore%>";
		  }

		  var wsdlNavigatorContainer = perspectiveContent.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTAINER%>"];
		  var wsdlNavigatorToolbar = wsdlNavigatorContainer.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_TOOLBAR%>"];
		  var wsdlNavigatorContent = wsdlNavigatorContainer.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTENT%>"];
		  var wsdlActionsContainer = perspectiveContent.frames["<%=WSDLFrameNames.WSDL_ACTIONS_CONTAINER%>"];
		  var wsdlPropertiesContainer = wsdlActionsContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_CONTAINER%>"];
		  var wsdlPropertiesToolbar = wsdlPropertiesContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_TOOLBAR%>"];
		  var wsdlPropertiesContent = wsdlPropertiesContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_CONTENT%>"];
		  var wsdlStatusContainer = wsdlActionsContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_CONTAINER%>"];
		  var wsdlStatusToolbar = wsdlStatusContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_TOOLBAR%>"];
		  var wsdlStatusContent = wsdlStatusContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_CONTENT%>"];

		</script>
    
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>"></script>
    </head>
    <body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
      <script language="javascript">
        wsdlPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_content.jsp"))%>";
        wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_status_content.jsp"))%>";
      </script>
    </body>
  </html>
<%
}
else
{
  try
  {
    // Run the action and obtain the return code (fail/success).
    boolean actionResult = action.execute();
%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
	<html lang="<%=response.getLocale().getLanguage()%>">
      <head>
		<script language="javascript">
		  var perspectiveWorkArea = top.frames["<%=FrameNames.PERSPECTIVE_WORKAREA%>"];
		  var perspectiveToolbar = top.frames["<%=FrameNames.PERSPECTIVE_TOOLBAR%>"];
		  var perspectiveContent = top.frames["<%=FrameNames.PERSPECTIVE_CONTENT%>"];
		
		  function getPerspectiveContentFrameset()
		  {
		    return perspectiveContent.document.getElementsByTagName("frameset").item(0);
		  }
		
		  function toggleDoubleClickColumnTitle()
		  {
		    var doubleClickColumn = document.getElementById("doubleclickcolumn");
		    if (doubleClickColumn == null)
		      return;
		<%String jsAltRestore = HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_RESTORE"));%>
		    if (doubleClickColumn.title == "<%=jsAltRestore%>")
		      doubleClickColumn.title = "<%=HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_MAXIMIZE"))%>";
		    else
		      doubleClickColumn.title = "<%=jsAltRestore%>";
		  }

		  var wsdlNavigatorContainer = perspectiveContent.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTAINER%>"];
		  var wsdlNavigatorToolbar = wsdlNavigatorContainer.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_TOOLBAR%>"];
		  var wsdlNavigatorContent = wsdlNavigatorContainer.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTENT%>"];
		  var wsdlActionsContainer = perspectiveContent.frames["<%=WSDLFrameNames.WSDL_ACTIONS_CONTAINER%>"];
		  var wsdlPropertiesContainer = wsdlActionsContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_CONTAINER%>"];
		  var wsdlPropertiesToolbar = wsdlPropertiesContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_TOOLBAR%>"];
		  var wsdlPropertiesContent = wsdlPropertiesContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_CONTENT%>"];
		  var wsdlStatusContainer = wsdlActionsContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_CONTAINER%>"];
		  var wsdlStatusToolbar = wsdlStatusContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_TOOLBAR%>"];
		  var wsdlStatusContent = wsdlStatusContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_CONTENT%>"];

		</script>
      
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>"></script>
      </head>
      <body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
        <script language="javascript">
          wsdlPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_container.jsp"))%>";
          <%
          if (actionResult)
          {
          %>
            wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_result_content.jsp"))%>";
          <%
          }
          else
          {
          %>
            wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_status_content.jsp"))%>";
          <%
          }
          %>
        </script>
      </body>
    </html>
<%
  }
  catch (HTTPTransportException httpe)
  {
    int code = httpe.getStatusCode();
    if (code == HttpServletResponse.SC_UNAUTHORIZED)
    {
      response.setStatus(code);
      response.setHeader(HTTPTransport.HTTP_HEADER_CONTENT_LENGTH, String.valueOf(0));
      String wwwAuthData = httpe.getHeader(HTTPTransport.HTTP_HEADER_WWW_AUTH.toLowerCase());
      if (wwwAuthData == null)
        wwwAuthData = httpe.getHeader(HTTPTransport.HTTP_HEADER_WWW_AUTH);
      if (wwwAuthData == null)
        wwwAuthData = HTTPTransport.BASIC;
      response.setHeader(HTTPTransport.HTTP_HEADER_WWW_AUTH, wwwAuthData);
    }
    else
    {
      WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
      MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
      messageQueue.addMessage(controller.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage(String.valueOf(code));
      messageQueue.addMessage(httpe.getMessage());
      %>
        
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
          <head>
		<script language="javascript">
		  var perspectiveWorkArea = top.frames["<%=FrameNames.PERSPECTIVE_WORKAREA%>"];
		  var perspectiveToolbar = top.frames["<%=FrameNames.PERSPECTIVE_TOOLBAR%>"];
		  var perspectiveContent = top.frames["<%=FrameNames.PERSPECTIVE_CONTENT%>"];
		
		  function getPerspectiveContentFrameset()
		  {
		    return perspectiveContent.document.getElementsByTagName("frameset").item(0);
		  }
		
		  function toggleDoubleClickColumnTitle()
		  {
		    var doubleClickColumn = document.getElementById("doubleclickcolumn");
		    if (doubleClickColumn == null)
		      return;
		<%String jsAltRestore = HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_RESTORE"));%>
		    if (doubleClickColumn.title == "<%=jsAltRestore%>")
		      doubleClickColumn.title = "<%=HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_MAXIMIZE"))%>";
		    else
		      doubleClickColumn.title = "<%=jsAltRestore%>";
		  }

		  var wsdlNavigatorContainer = perspectiveContent.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTAINER%>"];
		  var wsdlNavigatorToolbar = wsdlNavigatorContainer.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_TOOLBAR%>"];
		  var wsdlNavigatorContent = wsdlNavigatorContainer.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTENT%>"];
		  var wsdlActionsContainer = perspectiveContent.frames["<%=WSDLFrameNames.WSDL_ACTIONS_CONTAINER%>"];
		  var wsdlPropertiesContainer = wsdlActionsContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_CONTAINER%>"];
		  var wsdlPropertiesToolbar = wsdlPropertiesContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_TOOLBAR%>"];
		  var wsdlPropertiesContent = wsdlPropertiesContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_CONTENT%>"];
		  var wsdlStatusContainer = wsdlActionsContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_CONTAINER%>"];
		  var wsdlStatusToolbar = wsdlStatusContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_TOOLBAR%>"];
		  var wsdlStatusContent = wsdlStatusContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_CONTENT%>"];

		</script>
          
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>"></script>
          </head>
          <body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
            <script language="javascript">
              wsdlPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_content.jsp"))%>";
              wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_status_content.jsp"))%>";
            </script>
          </body>
        </html>
      <%
    }
  }
}
%>

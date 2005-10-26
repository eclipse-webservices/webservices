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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.core.resources.*,
                                                        org.eclipse.core.runtime.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.wst.ws.internal.wsrt.WebServiceInfo,
                                                        org.eclipse.wst.ws.internal.wsfinder.WebServiceFinder,
                                                        javax.wsdl.extensions.soap.*,
                                                        javax.wsdl.extensions.http.*,
                                                        javax.wsdl.extensions.*,
                                                        javax.wsdl.*,
                                                        java.util.*,
                                                        java.net.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
   Controller controller = (Controller)currentSession.getAttribute("controller");
   int wsdlType = controller.getWSDLType();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title><%=controller.getMessage("FRAME_TITLE_WSDL_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/resumeproxyloadpage.js"))%>">
</script>  
<script language="javascript">
  var sectionIds = ["workbench","favorites"];
  function fillWebProjects()
  {
    var x = 0;
<%
   {
     IWorkspaceRoot iWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
     IProject[] projects = iWorkspaceRoot.getProjects();
     for (int i=0;i<projects.length;i++)
     {
       if (!projects[i].isOpen())
         continue;
       
%>
    document.forms[0].<%=ActionInputs.PROJECT%>.options[x++] = new Option("<%=HTMLUtils.JSMangle(projects[i].getName())%>","<%=HTMLUtils.JSMangle(projects[i].getName())%>");
<%
       
     }
   }
%>
    if (document.forms[0].<%=ActionInputs.PROJECT%>.options.length > 0)
    {
      document.forms[0].<%=ActionInputs.PROJECT%>.options[0].selected = true;
      fillWSDLFiles(document.forms[0].<%=ActionInputs.PROJECT%>.options[0].value);
    }
  }

<%! private Vector wsdlURLs_ = new Vector(); %>

  function fillWSDLFiles(selectedWebProjectName)
  {
    var currentNumberOfOptions = document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options.length;
    for (var i=0;i<currentNumberOfOptions;i++)
      document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options[0] = null;
<%
   {

     IWorkspaceRoot iWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
     IProject[] projects = iWorkspaceRoot.getProjects();
       
     for (int i=0;i<projects.length;i++)
     {
       if (!projects[i].isOpen())
         continue;
       {
%>
    if (selectedWebProjectName == "<%=HTMLUtils.JSMangle(projects[i].getName())%>")
    {
      var x=0;
<%
         String platformProtocol = "platform:/resource";
         wsdlURLs_.removeAllElements();
         
         Iterator ws = WebServiceFinder.instance().getWebServices();  

         while (ws.hasNext())
         {
            WebServiceInfo wsInfo = (WebServiceInfo)ws.next();
            String url = wsInfo.getWsdlURL();
            
           // filter wsdl URLs with same project name as selected project
           // only look at workspace URLs returned from the web service finder
          if (url.startsWith(platformProtocol))
          {
             //strip off platformProtocol to look at the workspace path
             Path wsdlPath = new Path(url.substring(platformProtocol.length()+1));
             String pathProjName = wsdlPath.segment(0);                          

             if (pathProjName.equals(projects[i].getName()))
             {
               wsdlURLs_.add(url);
             }
          }
         }
         if (wsdlType == ActionInputs.WSDL_TYPE_SERVICE_INTERFACE)
         {
         
         }

         for (Iterator it = wsdlURLs_.iterator(); it.hasNext();)
         {
             String wsdl = HTMLUtils.JSMangle(it.next().toString());
             %>
              document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options[x++] = new Option("<%=wsdl%>", "<%=wsdl%>"); 
             <%
         }
%>
    }
<%
       }
     }
   }
%>
  }
  
  function fillFavoriteWSDLFiles()
  {
    var x = 0;
<%
   FavoritesPerspective favoritesPerspective = controller.getFavoritesPerspective();
   NodeManager favoritesNodeManager = favoritesPerspective.getNodeManager();
   TreeElement favoritesMainElement = favoritesNodeManager.getRootNode().getTreeElement();
   TreeElement favoriteWSDLServicesElement = (TreeElement)(favoritesMainElement.getElements(FavoritesModelConstants.REL_WSDL_SERVICE_FOLDER_NODE).nextElement());
   Enumeration favoriteWSDLServiceElements = favoriteWSDLServicesElement.getElements(FavoritesModelConstants.REL_WSDL_SERVICE_NODE);
   while (favoriteWSDLServiceElements.hasMoreElements())
   {
     FavoritesWSDLServiceElement favoriteWSDLServiceElement = (FavoritesWSDLServiceElement)favoriteWSDLServiceElements.nextElement();
     String url = HTMLUtils.JSMangle((favoriteWSDLServiceElement.getService().getDescriptions())[0].getLocation());
%>
    document.forms[0].<%=ActionInputs.QUERY_INPUT_FAVORITE_WSDL_URL%>.options[x++] = new Option("<%=url%>","<%=url%>");
<%     
   }
%>  
  }
  
  function toggleForm(formIndex)
  {
    for (var i=0;i<sectionIds.length;i++)
    {
      if (i == formIndex)
        document.getElementById(sectionIds[i]).style.display = "";
      else
        document.getElementById(sectionIds[i]).style.display = "none";
    }  
  }
  
  function setDefaults()
  {
    fillWebProjects();
    fillFavoriteWSDLFiles();
    document.getElementById(sectionIds[0]).style.display = "";
    var loadScreenTable = document.getElementById("loadScreen");
    if (loadScreenTable.rows.length > 0)
      loadScreenTable.deleteRow(0);
    document.getElementById("mainScreen").style.display = "";      
  }
</script>
</head>

<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
  <div id="contentborder">
    <table id="loadScreen">
      <tr>
        <td>
          <%=controller.getMessage("MSG_LOAD_IN_PROGRESS")%>
        </td>
      </tr>
    </table>
    <div id="mainScreen" style="display:none;">
      <form style="margin-top:0">
<%
   if (wsdlType == ActionInputs.WSDL_TYPE_SERVICE)
   {
%>
        <table>
          <tr>
            <td> <%=controller.getMessage("FORM_LABEL_WSDL_SOURCE")%> </td>
          </tr>
          <tr>
            <td>               
              <input type="radio" name="selectFrom" onClick="toggleForm(0)" checked><%=controller.getMessage("FORM_LABEL_WSDL_SOURCE_WEBPROJECTS")%>
              <input type="radio" name="selectFrom" onClick="toggleForm(1)"><%=controller.getMessage("FORM_LABEL_WSDL_SOURCE_FAVORITES")%>
            </td>
          </tr>
        </table>
<%              
   }
%>              
        <div id="workbench" style="display:none;">
          <table width="95%" border=0 cellpadding=3 cellspacing=0>
            <tr>
              <td height=30 valign="bottom" class="labels"><%=controller.getMessage("FORM_LABEL_WEB_PROJECT")%></td>
            </tr>
            <tr>
              <td nowrap>
                <select name="<%=ActionInputs.PROJECT%>" onChange="fillWSDLFiles(this.value)" class="selectlist">
                </select>
                <input type="button" value="<%=controller.getMessage("FORM_BUTTON_REFRESH")%>" onClick="document.location.reload()" class="button">
              </td>
            </tr>
            <tr>
              <td height=10 valign="bottom" class="labels"><%=controller.getMessage("FORM_LABEL_WSDL_URL")%></td>
            </tr>
            <tr>
              <td>
                <select name="<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>" class="selectlist">
                </select>
              </td>
            </tr>
          </table>
          <table border=0 cellpadding=2 cellspacing=0>
            <tr>
              <td height=30 valign="bottom" nowrap align="left">
                <input type="button" value="<%=controller.getMessage("FORM_BUTTON_GO")%>" onClick="top.opener.targetWSDLURLElement.value=this.form.<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.value;top.close()" class="button">
              </td>
              <td height=30 valign="bottom" nowrap align="left">
                <input type="button" value="<%=controller.getMessage("FORM_BUTTON_CANCEL")%>" onClick="top.close()" class="button">
              </td>
              <td nowrap width="90%">&nbsp;</td>
            </tr>
          </table>
        </div>
        <div id="favorites" style="display:none;">
          <table width="95%" border=0 cellpadding=3 cellspacing=0>
            <tr>
              <td height=30 valign="bottom" class="labels"><%=controller.getMessage("FORM_LABEL_WSDL_URL")%></td>
            </tr>
            <tr>
              <td>
                <select name="<%=ActionInputs.QUERY_INPUT_FAVORITE_WSDL_URL%>" class="selectlist">
              </td>
          </table>
          <table border=0 cellpadding=2 cellspacing=0>
            <tr>
              <td height=30 valign="bottom" nowrap align="left">
                <input type="button" value="<%=controller.getMessage("FORM_BUTTON_GO")%>" onClick="top.opener.targetWSDLURLElement.value=this.form.<%=ActionInputs.QUERY_INPUT_FAVORITE_WSDL_URL%>.value;top.close()" class="button">
              </td>
              <td height=30 valign="bottom" nowrap align="left">
                <input type="button" value="<%=controller.getMessage("FORM_BUTTON_CANCEL")%>" onClick="top.close()" class="button">
              </td>
              <td nowrap width="90%">&nbsp;</td>
            </tr>
          </table>          
        </div>
      </form>
    </div>
  </div>
<script language="javascript">
  setDefaults();
  resumeProxyLoadPage();
</script>  
</body>
</html>

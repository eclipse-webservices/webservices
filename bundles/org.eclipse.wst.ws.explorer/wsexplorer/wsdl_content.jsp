<%
/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 * 20060524   142499 jeffliu@ca.ibm.com - Jeffrey Liu
 * 20070109   169553 makandre@ca.ibm.com - Andrew Mak
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
                                                        org.eclipse.wst.ws.internal.wsfinder.WebServiceCategory,
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
   
   Vector wsInfoCache = new Vector();
   int workspaceStart = -1;
   int workspaceEnd   = -1;
   
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title><%=controller.getMessage("FRAME_TITLE_WSDL_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/resumeproxyloadpage.js"))%>">
</script>  
<script language="javascript">
  
  
  function fillCategories()
  {
    var y = 0;
    var x = 0;
     document.forms[0].<%=ActionInputs.CATEGORY%>.options[x++] = new Option("<%=controller.getMessage("FORM_LABEL_WSDL_All")%>","<%=controller.getMessage("FORM_LABEL_WSDL_All")%>");
     document.forms[0].<%=ActionInputs.CATEGORY%>.options[x++] = new Option("<%=controller.getMessage("FORM_LABEL_WSDL_SOURCE_FAVORITES")%>","<%=controller.getMessage("FORM_LABEL_WSDL_SOURCE_FAVORITES")%>");
<%
   {
     WebServiceCategory[] categories = WebServiceFinder.instance().getWebServiceCategories();
     for (int i=0;i<categories.length;i++)
     {
       String label = HTMLUtils.JSMangle(categories[i].getLabel());
       if(categories[i].getId().equals("org.eclipse.wst.ws.internal.wsfinder.category.workspace")){
%>
    var y = x;
<%    
       }    
%>    
    document.forms[0].<%=ActionInputs.CATEGORY%>.options[x++] = new Option("<%=label%>","<%=label%>");
<%       
     }
   } 
%>
    if (document.forms[0].<%=ActionInputs.CATEGORY%>.options.length > 0)
    {
      document.forms[0].<%=ActionInputs.CATEGORY%>.options[y].selected = true;
      fillWSDLFilesByCategory(document.forms[0].<%=ActionInputs.CATEGORY%>.options[y].value);
    }
  
  }    
  
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
       String name = HTMLUtils.JSMangle(projects[i].getName());
%>
    document.forms[0].<%=ActionInputs.PROJECT%>.options[x++] = new Option("<%=name%>","<%=name%>");
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

   function fillWSDLFilesByCategory(webServiceFinderLabel)
   {
    var currentNumberOfOptions = document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options.length;
    for (var i=0;i<currentNumberOfOptions;i++)
      document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options[0] = null;
    if (webServiceFinderLabel == "<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_WSDL_All"))%>"){
      document.getElementById("projects").style.display = "none"; 
      fillAllWSDLFiles();
    }
    if (webServiceFinderLabel == "<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LABEL_WSDL_SOURCE_FAVORITES"))%>"){
      document.getElementById("projects").style.display = "none"; 
      fillFavoriteWSDLFiles();
    }
    
<%
   {
         
     WebServiceFinder finder = WebServiceFinder.instance();
     WebServiceCategory[] categories = finder.getWebServiceCategories();
     for (int i=0;i<categories.length;i++)
     {
       WebServiceCategory category = categories[i];
%>
    if (webServiceFinderLabel == "<%=HTMLUtils.JSMangle(category.getLabel())%>")
    {
    var x = 0;
        
<%
       Iterator it = finder.getWebServicesByCategory(category,null);
       if(category.getId().equals("org.eclipse.wst.ws.internal.wsfinder.category.workspace")){

    	   workspaceStart = wsInfoCache.size();
    	   workspaceEnd   = workspaceStart;
    	   
    	   while (it.hasNext()) {
        	   wsInfoCache.add(it.next());
        	   workspaceEnd++;
    	   }
%>
      document.getElementById("projects").style.display = ""; 
      fillWebProjects();
<%
       }
       else{
%>
      document.getElementById("projects").style.display = "none";
      
<%  
         while(it.hasNext())
         {
           WebServiceInfo wsi = (WebServiceInfo)it.next();
           String wsdl = HTMLUtils.JSMangle(wsi.getWsdlURL());
%>
      document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options[x++] = new Option("<%=wsdl%>", "<%=wsdl%>"); 
<%
           wsInfoCache.add(wsi);
         }
       }
%>
    }
<%
     }
   }
%>
  }
  
  function fillAllWSDLFiles()
  {
    var x = 0;
    var currentNumberOfOptions = document.forms[0].webProjectWSDLURL.options.length;
    for (var i=0;i<currentNumberOfOptions;i++)
      document.forms[0].webProjectWSDLURL.options[0] = null;
<%
     TreeSet urls = new TreeSet();
     Iterator wsIterator = wsInfoCache.iterator();
     while (wsIterator.hasNext())
     {
       WebServiceInfo wsInfo = (WebServiceInfo)wsIterator.next();
       String wsdl = HTMLUtils.JSMangle(wsInfo.getWsdlURL());
       urls.add(wsdl);
     }
     
     FavoritesPerspective favoritesPerspective = controller.getFavoritesPerspective();
     NodeManager favoritesNodeManager = favoritesPerspective.getNodeManager();
     TreeElement favoritesMainElement = favoritesNodeManager.getRootNode().getTreeElement();
     TreeElement favoriteWSDLServicesElement = (TreeElement)(favoritesMainElement.getElements(FavoritesModelConstants.REL_WSDL_SERVICE_FOLDER_NODE).nextElement());
     Enumeration favoriteWSDLServiceElements = favoriteWSDLServicesElement.getElements(FavoritesModelConstants.REL_WSDL_SERVICE_NODE);
     while (favoriteWSDLServiceElements.hasMoreElements())
     {
       FavoritesWSDLServiceElement favoriteWSDLServiceElement = (FavoritesWSDLServiceElement)favoriteWSDLServiceElements.nextElement();
       String wsdl = HTMLUtils.JSMangle((favoriteWSDLServiceElement.getService().getDescriptions())[0].getLocation());
       urls.add(wsdl);
     }
     Iterator iterator = urls.iterator();
     while(iterator.hasNext()){
       String wsdl = HTMLUtils.JSMangle(iterator.next().toString());
%>
     document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options[x++] = new Option("<%=wsdl%>", "<%=wsdl%>"); 
<%

     }
   
%>      
  }
  
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
         String httpProtocol = "http://";
         String httpsProtocol = "https://";
         wsdlURLs_.removeAllElements();

		 Iterator ws = wsInfoCache.subList(workspaceStart, workspaceEnd).iterator();

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
          // also add any http or https URLs returned
          else if (url.startsWith(httpProtocol) || url.startsWith(httpsProtocol))
          {
               wsdlURLs_.add(url);
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
    var currentNumberOfOptions = document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options.length;
    for (var i=0;i<currentNumberOfOptions;i++)
      document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options[0] = null;
    var x = 0;
<%
   Enumeration favoriteWSDLServiceElements2 = favoriteWSDLServicesElement.getElements(FavoritesModelConstants.REL_WSDL_SERVICE_NODE);
   while (favoriteWSDLServiceElements2.hasMoreElements())
   {
     FavoritesWSDLServiceElement favoriteWSDLServiceElement = (FavoritesWSDLServiceElement)favoriteWSDLServiceElements2.nextElement();
     String url = HTMLUtils.JSMangle((favoriteWSDLServiceElement.getService().getDescriptions())[0].getLocation());
%>
    document.forms[0].<%=ActionInputs.QUERY_INPUT_WEBPROJECT_WSDL_URL%>.options[x++] = new Option("<%=url%>","<%=url%>");
<%     
   }
%>  
  
  }
  
  
  
  function setDefaults()
  {
    fillCategories();
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

        <table>
          <tr>
            <td> <%=controller.getMessage("FORM_LABEL_WSDL_SOURCE")%> </td>
          </tr>
        </table>
        <div id="workbench" >
          <table width="95%" border=0 cellpadding=3 cellspacing=0>
            <tr>
              <td height=30 valign="bottom" class="labels"><%=controller.getMessage("FORM_LABEL_WSDL_CATEGORY")%></td>
            </tr>
            <tr>
              <td nowrap>
                <select name="<%=ActionInputs.CATEGORY%>" onChange="fillWSDLFilesByCategory(this.value)" class="selectlist">
                </select>
                <input type="button" value="<%=controller.getMessage("FORM_BUTTON_REFRESH")%>" onClick="document.location.reload()" class="button">
              </td>
            </tr>
          </table>    
          <div id="projects" style="display:none;">          
            <table width="95%" border=0 cellpadding=3 cellspacing=0>              
              <tr>
                <td height=30 valign="bottom" class="labels"><%=controller.getMessage("FORM_LABEL_WSDL_SOURCE_WEBPROJECTS")%></td>
              </tr>
              <tr>
                <td nowrap>
                  <select name="project" onChange="fillWSDLFiles(this.value)" class="selectlist">
                  </select>
                  <input type="button" value="<%=controller.getMessage("FORM_BUTTON_REFRESH")%>" onClick="document.location.reload()" class="button">
                </td>
              </tr>
            </table>
          </div>
          <table>
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
   
      </form>
    </div>
  </div>
<script language="javascript">
  setDefaults();
  resumeProxyLoadPage();
</script>  
</body>
</html>

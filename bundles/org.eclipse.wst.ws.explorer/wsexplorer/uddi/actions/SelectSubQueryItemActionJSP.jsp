<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   // Prepare the action.
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   SelectSubQueryItemAction action = new SelectSubQueryItemAction(controller);

   // Load the parameters for the action from the servlet request.
   action.populatePropertyTable(request);

   // Run the action and obtain the return code (fail/success).
   boolean actionResult = action.execute(false);
   
   if (!actionResult)
   {
     Hashtable propertyTable = action.getPropertyTable();
     String subQueryKey = (String)propertyTable.get(UDDIActionInputs.SUBQUERY_KEY);
     String subQueryListKey = (String)propertyTable.get(UDDIActionInputs.SUBQUERY_LIST_KEY);
     int subQueryListItemId = Integer.parseInt((String)propertyTable.get(UDDIActionInputs.SUBQUERY_LIST_ITEMID));
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; UTF-8">
<jsp:include page="/scripts/panes.jsp" flush="true"/>  
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<script language="javascript">
  if (confirm("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_QUESTION_ITEM_VALIDATION_FAILED",action.getItemName()))%>"))
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(RemoveSubQueryItemAction.getActionLink(subQueryKey,subQueryListKey,subQueryListItemId)))%>";
</script>
</body>
</html>
<%
   }
   else
   {
     // Determine if the action was added to the history list.
     boolean isAddedToHistory = action.isAddedToHistory();
%>
<%@ include file="/actions/SelectNodeToolAction.inc" %>
<%
   }
%>

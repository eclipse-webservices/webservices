<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.ClearWSILAction"%>

<jsp:include page="/wsil/scripts/wsilPanes.jsp" flush="true"/>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
    // Prepare the action.
    ClearWSILAction action = new ClearWSILAction(controller);

     // Load the parameters for the action from the servlet request.
     action.populatePropertyTable(request);

     // Run the action and obtain the return code (fail/success).
     boolean actionResult = action.execute();
%>
<%@ include file="/actions/ClearNodeAction.inc" %>

<%
/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%
   String subQueryKey = action.getSubQueryKey();
   StringBuffer newSubQueryKey = new StringBuffer();
   if (subQueryKey != null && subQueryKey.length() > 0)
     newSubQueryKey.append(action.getSubQueryKey()).append(':');
   newSubQueryKey.append(action.getNewSubQuery());
   FormTool formTool = (FormTool)action.getSelectedFormTool();
   FormToolPropertiesInterface parentFormToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey);
   FormToolPropertiesInterface subQueryFormToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(newSubQueryKey.toString());
   String newSubQueryItemString = action.getNewSubQueryItem();
   subQueryFormToolPI.setProperty(UDDIActionInputs.QUERY_ITEM,newSubQueryItemString);
   subQueryFormToolPI.setProperty(UDDIActionInputs.SUBQUERY_GET,new Boolean(action.isSubQueryGet()));
   
   // Always initialize the query name so the subquery makes sense.
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   String parentQueryName = (String)parentFormToolPI.getProperty(UDDIActionInputs.QUERY_NAME);
   if (parentQueryName == null)
     parentQueryName = uddiPerspective.getMessage("DEFAULT_QUERY_NAME");
   int newSubQueryItem = Integer.parseInt(newSubQueryItemString);
   String newSubQueryItemTypeString = null;
   switch (newSubQueryItem)
   {
     case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
       newSubQueryItemTypeString = uddiPerspective.getMessage("FORM_OPTION_BUSINESSES");
       break;
     case UDDIActionInputs.QUERY_ITEM_SERVICES:
       newSubQueryItemTypeString = uddiPerspective.getMessage("FORM_OPTION_SERVICES");
       break;
     case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
     default:
       newSubQueryItemTypeString = uddiPerspective.getMessage("FORM_OPTION_SERVICE_INTERFACES");
   }
   String[] messageParameters = {parentQueryName,newSubQueryItemTypeString};
   subQueryFormToolPI.setProperty(UDDIActionInputs.QUERY_NAME,uddiPerspective.getMessage("DEFAULT_SUBQUERY_NAME",messageParameters));
   
   Node node = action.getSelectedNavigatorNode();
   int nodeId = node.getNodeId();
   int toolId = node.getToolManager().getSelectedToolId();
   int viewId = node.getViewId();
   int viewToolId = node.getViewToolId();
%>
<script language="javascript">
  perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(SelectFindToolAction.getActionLink(nodeId,toolId,viewId,viewToolId,newSubQueryKey.toString(),false)))%>";
</script>

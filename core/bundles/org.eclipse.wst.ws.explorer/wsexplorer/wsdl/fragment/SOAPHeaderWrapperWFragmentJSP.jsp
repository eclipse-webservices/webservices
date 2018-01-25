<%
/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.SOAPHeaderWrapperFragment,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
SOAPHeaderWrapperFragment wrapperFragment = (SOAPHeaderWrapperFragment)operElement.getFragmentByID(fragID.toString());
IXSDFragment delegationFragment = wrapperFragment.getXSDDelegationFragment();
fragID.delete(0, fragID.length());
fragID.append(delegationFragment.getID());

String mustUnderstandID = wrapperFragment.getMustUnderstandID();
String actorID = wrapperFragment.getActorID();

String fieldsetClass =  BrowserDetect.isMicrosoftInternetExplorer(request) ? "" : "headerfieldset";
%>
<table cellpadding="0" cellspacing="0" class="fixfragtable">
  <tr>
    <td>
	  <fieldset class="<%=fieldsetClass%>">
		<jsp:include page="<%=delegationFragment.getWriteFragment()%>" flush="true"/>	      	  
		<table cellpadding="3" cellspacing="0" class="fixfragtable">
		  <tr>
		    <td height="3"><!-- spacer --></td>
		  </tr>
		  <tr>
		    <td>
		      <% if (wrapperFragment.isMustUnderstand()) { %>    	
		        <input type="checkbox" id="<%=mustUnderstandID%>" name="<%=mustUnderstandID%>" checked/>
		      <% } else { %>
		        <input type="checkbox" id="<%=mustUnderstandID%>" name="<%=mustUnderstandID%>"/>
		      <% } %>      	
		    </td>
		    <td class="label">
		      <label for="<%=mustUnderstandID%>"><%=wsdlPerspective.getMessage("FORM_LABEL_MUSTUNDERSTAND")%></label>
		    </td>
		    <td>|</td>
		    <td class="label">
		      <label for="<%=actorID%>"><%=wsdlPerspective.getMessage("FORM_LABEL_ACTOR")%></label>
		    </td>
		    <td>
		      <div style="width: 5px">
      		    <% if (!wrapperFragment.validateActor()) { %>
      		      <%=HTMLUtils.redAsterisk()%>
                <% } %>
              </div>
    		</td>    	
		    <td width="100%">    	
		      <input type="text" id="<%=actorID%>" name="<%=actorID%>" value="<%=wrapperFragment.getActor()%>" class="tabletextenter"/>
		    </td>
		  </tr>
		  <tr>
		    <td height="3"><!-- spacer --></td>
		  </tr>    
		</table>
	  </fieldset>
	</td>
  </tr>
  <tr>
	<td height="5"><!-- spacer --></td>
  </tr>
</table>

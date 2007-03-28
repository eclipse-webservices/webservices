<%
/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.Node,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.wst.wsdl.binding.soap.SOAPHeader,
                                                        org.w3c.dom.*,
                                                        javax.wsdl.*,
                                                        javax.xml.parsers.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%!
private Element soapResponse_ = null;
private boolean hasSOAPHeaders = false;

private void checkSOAPHeaders() {
	
	NodeList nl = soapResponse_.getElementsByTagNameNS(FragmentConstants.URI_SOAP_ENV, FragmentConstants.QNAME_LOCAL_NAME_HEADER);    
	if (nl.getLength() == 0)
		return;
	
	Element soapHeader = (Element) nl.item(0);
	NodeList nodes = soapHeader.getChildNodes();
	
	for (int i = 0; i < nodes.getLength(); i++) {
	    if (nodes.item(i) instanceof Element) {
	    	hasSOAPHeaders = true;
	    	return;
	    }
	}
}

private Element[] parseSOAPResponse(SOAPMessageQueue soapMessageQueue, WSDLOperationElement operElement)
{
  String messages = soapMessageQueue.getMessagesFromList();
  try
  {
    soapResponse_ = XMLUtils.stringToElement(messages, true);
    checkSOAPHeaders();
    NodeList nl = soapResponse_.getElementsByTagNameNS(FragmentConstants.URI_SOAP_ENV, FragmentConstants.QNAME_LOCAL_NAME_BODY);
    if (nl.getLength() > 0)
    {
      Element soapBody = (Element)nl.item(0);
      NodeList soapFault = soapBody.getElementsByTagNameNS(FragmentConstants.URI_SOAP_ENV, FragmentConstants.QNAME_LOCAL_NAME_FAULT);
      if (soapFault.getLength() > 0)
        return new Element[0];
      NodeList instanceList;
      if (operElement.isDocumentStyle())
        instanceList = soapBody.getChildNodes();
      else
      {
        NodeList rpcWrapper = soapBody.getElementsByTagNameNS("*", operElement.getOperation().getOutput().getMessage().getQName().getLocalPart());

        /*
        * HACK - Some of the web services out on the internet do not
        * set their RPC wrapper properly.  It should be set to the output
        * message name of the selected operation.  The hack is to
        * assume the first element inside the body element is the
        * RPC wrapper.
        */
        if (rpcWrapper.getLength() <= 0)
          rpcWrapper = soapBody.getElementsByTagNameNS("*", "*");

        if (rpcWrapper.getLength() > 0)
          instanceList = rpcWrapper.item(0).getChildNodes();
        else
          return null;
      }
      return fixSOAPResponse(instanceList, operElement);
    }
  }
  catch (Throwable t) {
    t.printStackTrace();
  }
  return null;
}

/*
* HACK - The root element tag name of the instance document
* is ambiguous.  It lands on a very grey area between the SOAP
* spec and the WSDL spec.  The two specs do not explicitly define
* that the root element tag name must match the name of the
* WSDL part.  The hack is to treat elements with different tag names
* as instances of the WSDL part.
*/
private Element[] fixSOAPResponse(NodeList instanceList, WSDLOperationElement operElement)
{
  Vector instanceVector = new Vector();
  for (int i = 0; i < instanceList.getLength(); i++)
  {
    Object object = instanceList.item(i);
    if (object != null && (object instanceof Element))
      instanceVector.add(object);
  }
  Element[] instanceDocuments = new Element[instanceVector.size()];
  Operation oper = operElement.getOperation();
  Map partsMap = oper.getOutput().getMessage().getParts();
  if (partsMap.size() == 1)
  {
    Iterator it = partsMap.values().iterator();
    IXSDFragment frag = operElement.getFragment((Part)it.next(), false);
    for (int i = 0; i < instanceVector.size(); i++)
    {
      Element element = (Element)instanceVector.get(i);
      if (!element.getTagName().equals(frag.getName()))
      {
        Document doc = element.getOwnerDocument();
        NodeList children = element.getChildNodes();
        NamedNodeMap attributes = element.getAttributes();
        element = doc.createElement(frag.getName());
        for (int j = 0; j < children.getLength(); j++)
        {
          if (children.item(j) != null)
          {
            element.appendChild(children.item(j));
            // When you append a node from one element to another,
            // the original element will lose its reference to this node,
            // therefore, the size of the node list will decrease by 1.
            j--;
          }
        }
        for (int j = 0; j < attributes.getLength(); j++)
        {
          Object attr = attributes.item(j);
          if (attr != null && (attr instanceof Attr))
          {
            Attr attribute = (Attr)attr;
            element.setAttribute(attribute.getName(), attribute.getValue());
          }
        }
      }
      instanceDocuments[i] = element;
    }
  }
  else
    instanceVector.copyInto(instanceDocuments);
  return instanceDocuments;
}
%>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
wsdlPerspective.setStatusContentType(WSDLPerspective.STATUS_CONTENT_RESULT_FORM);
Node operNode = wsdlPerspective.getOperationNode();
nodeID.delete(0, nodeID.length());
nodeID.append(operNode.getNodeId());
WSDLOperationElement operElement = (WSDLOperationElement)operNode.getTreeElement();
Operation oper = operElement.getOperation();
if (oper.getOutput() == null)
{
%>
  <table width="95%" border=0 cellpadding=6 cellspacing=0>
    <tr>
      <td height=20 valign="bottom" align="left" class="labels">
        <%=wsdlPerspective.getMessage("FORM_LABEL_NOTHING_TO_DISPLAY_IN_FORM_VIEW")%>
      </td>
    </tr>
  </table>
<%
}
else
{
  boolean cached = ((Boolean)operElement.getPropertyAsObject(WSDLActionInputs.SOAP_RESPONSE_CACHED)).booleanValue();
  Element[] instanceDocuments = null;
  if (!cached)
  {
    SOAPMessageQueue soapMessageQueue = wsdlPerspective.getSOAPResponseQueue();
    instanceDocuments = parseSOAPResponse(soapMessageQueue, operElement);
  }
  if (!cached && !hasSOAPHeaders && instanceDocuments == null)
  {
  %>
    <table width="95%" border=0 cellpadding=6 cellspacing=0>
      <tr>
        <td height=20 valign="bottom" align="left" class="labels">
          <%=wsdlPerspective.getMessage("FORM_LABEL_SOAP_RESPONSE_FAILED_VALIDATION_IN_FORM_VIEW")%>
        </td>
      </tr>
    </table>
  <%
  }
  else if (!cached && !hasSOAPHeaders && instanceDocuments.length <= 0)
  {
  %>
    <table width="95%" border=0 cellpadding=6 cellspacing=0>
      <tr>
        <td height=20 valign="bottom" align="left" class="labels">
          <%=wsdlPerspective.getMessage("FORM_LABEL_NOTHING_TO_DISPLAY_IN_FORM_VIEW")%>
        </td>
      </tr>
    </table>
  <%
  }
  else
  {
	String headerDivId = "Header";
	String headerImgId = "xHeader";
	String bodyDivId = "Body";
	String bodyImgId = "xBody";	  
    %>
    <table border=0 cellpadding=6 cellspacing=0>
	  <tr>
	    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=headerDivId%>','<%=headerImgId%>')"><img name="<%=headerImgId%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a></td>
	    <td height=20 valign="bottom" align="left" nowrap class="labels"><strong><%=wsdlPerspective.getMessage("FORM_LABEL_HEADER")%></strong></td>
	  </tr>
	</table>
	
	<table width="95%" border=0 cellpadding=0 cellspacing=0>
	  <tr>
	    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" height=2 width="100%"></td>
	  </tr>
	</table>
	
	<div id="<%=headerDivId%>" class="fragarea">
	<%
	if (cached || hasSOAPHeaders) {
		hasSOAPHeaders = false;
		Iterator it = operElement.getSOAPHeaders(false).iterator();
		while (it.hasNext()) {
			SOAPHeader soapHeader = (SOAPHeader) it.next();			
			String ns = soapHeader.getEPart().getElementDeclaration().getTargetNamespace();			
			IXSDFragment frag = operElement.getHeaderFragment(soapHeader, false);
			
			if (!cached) {
				NodeList nl = soapResponse_.getElementsByTagNameNS(ns, frag.getName());
				if (nl.getLength() == 0)
					continue;
				
				Element element = (Element) nl.item(0);				
				if (!frag.setParameterValuesFromInstanceDocuments(new Element[] { element }))
					continue;
			}
			else if (!frag.validateAllParameterValues())
				continue;
			
			hasSOAPHeaders = true;				
			fragID.delete(0, fragID.length());
			fragID.append(frag.getID());
			%>
			<jsp:include page="<%=frag.getReadFragment()%>" flush="true"/>
			<%  
		}		
	}
	if (!hasSOAPHeaders) {
		%>
		<table width="95%" border=0 cellpadding=6 cellspacing=0>
	      <tr>
	        <td height=20 valign="bottom" align="left" class="labels">
	          <%=wsdlPerspective.getMessage("FORM_LABEL_NOTHING_TO_DISPLAY_IN_FORM_VIEW")%>
	        </td>
	      </tr>
	    </table>
		<%
	}
	%>	
	</div>	
	
	<table border=0 cellpadding=6 cellspacing=0>
	  <tr>
	    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=bodyDivId%>','<%=bodyImgId%>')"><img name="<%=bodyImgId%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a></td>
	    <td height=20 valign="bottom" align="left" nowrap class="labels"><strong><%=wsdlPerspective.getMessage("FORM_LABEL_BODY")%></strong></td>
	  </tr>
	</table> 
	
	<table width="95%" border=0 cellpadding=0 cellspacing=0>
	  <tr>
	    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" height=2 width="100%"></td>
	  </tr>
	</table>
	
	<div id="<%=bodyDivId%>" class="fragarea">
	<%	
	if (cached || (instanceDocuments != null && instanceDocuments.length > 0)) {
			
	    Map partsMap = oper.getOutput().getMessage().getParts();
	    Iterator it = partsMap.values().iterator();
	    Hashtable uriReferences = null;
	    while (it.hasNext())
	    {
	      IXSDFragment fragment = operElement.getFragment((Part)it.next(), false);
	      if (!cached)
	      {
	        if (!operElement.isUseLiteral() && (fragment instanceof ISOAPEncodingWrapperFragment))
	        {
	          if (uriReferences == null)
	            uriReferences = SOAPEncodingWrapperFragment.parseURIReferences(soapResponse_, true);
	          ((ISOAPEncodingWrapperFragment)fragment).setURIReferences(uriReferences);
	        }
	        fragment.setParameterValuesFromInstanceDocuments(instanceDocuments);
	      }
	      fragID.delete(0, fragID.length());
	      fragID.append(fragment.getID());
	      %>
	      <jsp:include page="<%=fragment.getReadFragment()%>" flush="true"/>
	      <%
	    }
	    operElement.setPropertyAsObject(WSDLActionInputs.SOAP_RESPONSE_CACHED, new Boolean(true));
	}
	else {
		%>
		<table width="95%" border=0 cellpadding=6 cellspacing=0>
	      <tr>
	        <td height=20 valign="bottom" align="left" class="labels">
	          <%=wsdlPerspective.getMessage("FORM_LABEL_NOTHING_TO_DISPLAY_IN_FORM_VIEW")%>
	        </td>
	      </tr>
	    </table>
		<%
	} 
    %>
    </div>
    <%
  }
}
%>

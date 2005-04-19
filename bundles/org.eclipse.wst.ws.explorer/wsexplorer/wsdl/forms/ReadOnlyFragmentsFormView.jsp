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
                                                        org.w3c.dom.*,
                                                        javax.wsdl.*,
                                                        javax.xml.parsers.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%!
private Element soapResponse_ = null;

private Element[] parseSOAPResponse(SOAPMessageQueue soapMessageQueue, WSDLOperationElement operElement)
{
  String messages = soapMessageQueue.getMessagesFromList();
  try
  {
    soapResponse_ = XMLUtils.stringToElement(messages, true);
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
  if (!cached && instanceDocuments == null)
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
  else if (!cached && instanceDocuments.length <= 0)
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
}
%>

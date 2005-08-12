<%@ page contentType="text/xml; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.xsd.*,
                                                        org.w3c.dom.Element,
                                                        org.xml.sax.*,
                                                        org.xml.sax.helpers.*,
                                                        javax.xml.rpc.NamespaceConstants,
                                                        javax.xml.parsers.*,
                                                        java.io.*,
                                                        java.util.*" %>
<%
String fragID = request.getParameter(WSDLActionInputs.FRAGMENT_ID);
String nodeID = request.getParameter(ActionInputs.NODEID);
String sessionId = request.getParameter(ActionInputs.SESSIONID);
HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
Controller controller = (Controller)currentSession.getAttribute("controller");
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node operNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID));
WSDLOperationElement operElement = (WSDLOperationElement)operNode.getTreeElement();
IXSDFragment frag = operElement.getFragmentByID(fragID);
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
Hashtable soapEnvelopeNamespaceTable = new Hashtable();
// TODO: Replace "SOAP-ENV" by NamespaceConstants.NSPREFIX_SOAP_ENVELOPE (="soapenv")
soapEnvelopeNamespaceTable.put(NamespaceConstants.NSURI_SOAP_ENVELOPE,"SOAP-ENV");
soapEnvelopeNamespaceTable.put(NamespaceConstants.NSURI_SCHEMA_XSI,NamespaceConstants.NSPREFIX_SCHEMA_XSI);
soapEnvelopeNamespaceTable.put(NamespaceConstants.NSURI_SCHEMA_XSD,NamespaceConstants.NSPREFIX_SCHEMA_XSD);
Element[] instanceDocuments = new Element[0];
StringBuffer sb = null;
try
{
  instanceDocuments = frag.genInstanceDocumentsFromParameterValues(!operElement.isUseLiteral(), soapEnvelopeNamespaceTable, XMLUtils.createNewDocument(null));
  if (instanceDocuments.length == 1)
  {
    byte[] b = XMLUtils.serialize(instanceDocuments[0], true).getBytes(HTMLUtils.UTF8_ENCODING);
    ByteArrayInputStream bais = new ByteArrayInputStream(b);
    SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    saxFactory.setValidating(true);
    SAXParser saxParser = saxFactory.newSAXParser();
    saxParser.parse(bais,
      new DefaultHandler()
      {
        public void error(SAXParseException e) throws SAXException
        {
          throw e;
        }
    
        public void fatalError(SAXParseException e) throws SAXException
        {
          throw e;
        }
    
        public void warning(SAXParseException e) throws SAXException
        {
          throw e;
        }
      });
    bais.close();
    sb = new StringBuffer(new String(b));
  }
}
catch (Throwable t)
{
}
if (sb == null)
{
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=frag.getName()%></title>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<%
  sb = new StringBuffer();
  response.setContentType("text/html; charset=UTF-8");
  for (int i = 0; i < instanceDocuments.length; i++) {
    if (instanceDocuments[i] == null)
      continue;
    sb.append(HTMLUtils.charactersToHTMLEntities(XMLUtils.serialize(instanceDocuments[i], true)));
  }
%>
<%=sb.toString()%>
</body>
</html>
<%
}
else
{
%>
<%=sb.toString()%>
<%
}
%>

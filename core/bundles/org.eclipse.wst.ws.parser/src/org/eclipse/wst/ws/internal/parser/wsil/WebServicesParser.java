/*******************************************************************************
 * Copyright (c) 2001, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060504   119296 pmoogk@ca.ibm.com - Peter Moogk
 * 20060517   142324 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060711   150301 pmoogk@ca.ibm.com - Peter Moogk
 * 20060818   154393 pmoogk@ca.ibm.com - Peter Moogk
 * 20060906   156420 pmoogk@ca.ibm.com - Peter Moogk
 * 20121004   385354 kchong@ca.ibm.com - Keith Chong
 *******************************************************************************/

package org.eclipse.wst.ws.internal.parser.wsil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.wsil.Abstract;
import org.apache.wsil.Description;
import org.apache.wsil.Inspection;
import org.apache.wsil.Link;
import org.apache.wsil.Service;
import org.apache.wsil.WSILConstants;
import org.apache.wsil.WSILDocument;
import org.apache.wsil.WSILException;
import org.apache.wsil.extension.ExtensionElement;
import org.apache.wsil.extension.uddi.ServiceDescription;
import org.apache.wsil.extension.uddi.UDDIConstants;
import org.apache.wsil.extension.wsdl.WSDLConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.ws.internal.parser.disco.DISCOContractReference;
import org.eclipse.wst.ws.internal.parser.disco.DISCOParser;
import org.eclipse.wst.ws.internal.parser.disco.DISCOReference;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WebServicesParser
{
  public static final int PARSE_NONE = 0;
  public static final int PARSE_WSIL = 1<<0;
  public static final int PARSE_WSDL = 1<<1;
  public static final int PARSE_LINKS = 1<<2;
  public static final int PARSE_DISCO = 1<<3;

  private String uri_;
  private Hashtable uriToEntityTable_;

  private String httpBasicAuthUsername_;
  private String httpBasicAuthPassword_;

  public WebServicesParser()
  {
    this(null);
  }

  public WebServicesParser(String uri)
  {
    uri_ = uri;
    uriToEntityTable_ = new Hashtable();
    httpBasicAuthUsername_ = null;
    httpBasicAuthPassword_ = null;
  }

  public String getURI()
  {
    return uri_;
  }

  public void setURI(String uri)
  {
    uri_ = uri;
  }

  public WebServiceEntity getWebServiceEntityByURI(String uri)
  {
    if (uri != null)
      return (WebServiceEntity)uriToEntityTable_.get(uri);
    else
      return null;
  }

  /**
   * 
   * @param wsilURI
   * @return
   * @deprecated replaced with getWSILDocumentVerbose(String, String)
   */
  public WSILDocument getWSILDocument(String wsilURI )
  {
    try
    {
      return getWSILDocumentVerbose(wsilURI, "UTF-8" );
    }
    catch (Exception t)
    {
    }
    return null;
  }
  
  /**
   * 
   * @param wsilURI
   * @return
   * @throws MalformedURLException
   * @throws IOException
   * @throws WWWAuthenticationException
   * @throws WSILException
   * @deprecated replaced with getWSILDocumentVerbose(String, String)
   */
  public WSILDocument getWSILDocumentVerbose(String wsilURI ) throws MalformedURLException, IOException, WWWAuthenticationException, WSILException
  {
    return getWSILDocumentVerbose( wsilURI, "UTF-8" );
  }
  
  public WSILDocument getWSILDocumentVerbose(String wsilURI, String byteEncoding ) throws MalformedURLException, IOException, WWWAuthenticationException, WSILException
  {
    WebServiceEntity wsEntity = getWebServiceEntityByURI(wsilURI);
    
    if (wsEntity == null)
    {
      wsEntity = new WebServiceEntity();
      wsEntity.setURI(wsilURI);
      uriToEntityTable_.put(wsilURI, wsEntity);
    }
    
    WSILDocument wsilDocument = (WSILDocument)wsEntity.getModel();
    
    if (wsilDocument == null)
    {
      // For some reason WSDL4J is not reading the content type properly for platform resources.  Therefore, we will get
      // the stream directly from Eclipse if we find a platform protocol specified in the URL.
      if( wsilURI.startsWith( "platform:/resource" ) )
      {
        String    path     = wsilURI.substring(18);
        IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember( path );
        
        if( resource instanceof IFile )
        {
          try
          {
            IFile             file   = (IFile)resource;
            InputStream       stream = file.getContents();
            InputStreamReader reader = new InputStreamReader( stream, file.getCharset() );
          
            wsilDocument = WSILDocument.newInstance();
            wsilDocument.read( reader );
          }
          catch( CoreException exc ){}
        }
      }
      else
      {
        // TODO the following 3 lines of code should probably be removed, since
        // the WSDL4J parser does not need a byte stream.
        byte[] b = getInputStreamAsByteArray(wsilURI);
        wsEntity.setBytes(b);
        setHTTPSettings(wsEntity);
      
        // This parser only checks the header of HTML for a particular encoding.
        // It doesn't check the encoding for general XML documents like WSDL and WSIL.
        // This causing this parser to alway use UTF-8 as the encoding.  Therefore,
        // since we can not trust the encoding specified we will not use it.  Instead,
        // we will just let the WSIL parser figure out what encoding to use.
      
        //InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(b), byteEncoding );
        wsilDocument = WSILDocument.newInstance();
        wsilDocument.read(wsilURI);
      }
      
      wsEntity.setType(WebServiceEntity.TYPE_WSIL);
      wsEntity.setModel(wsilDocument);
    }
    return wsilDocument;
  }

  public Definition getWSDLDefinition(String wsdlURI)
  {
    try
    {
      return getWSDLDefinitionVerbose(wsdlURI);
    }
    catch (Exception t)
    {
    }
    return null;
  }

  public Definition getWSDLDefinitionVerbose(String wsdlURI) throws MalformedURLException, IOException, WWWAuthenticationException, WSDLException
  {
    WebServiceEntity wsEntity = getWebServiceEntityByURI(wsdlURI);
    if (wsEntity == null)
    {
      wsEntity = new WebServiceEntity();
      wsEntity.setURI(wsdlURI);
      uriToEntityTable_.put(wsdlURI, wsEntity);
    }
    Definition definition = (Definition)wsEntity.getModel();
    if (definition == null)
    {
      byte[] b = getInputStreamAsByteArray(wsdlURI);
      wsEntity.setBytes(b);
      setHTTPSettings(wsEntity);
      ByteArrayInputStream bais = new ByteArrayInputStream(b);
      WSDLFactory factory = WSDLPlugin.INSTANCE.createWSDL4JFactory();
      WSDLReader wsdlReader = factory.newWSDLReader();
      definition = wsdlReader.readWSDL(wsdlURI.replace('\\', '/'), new InputSource(bais));
      wsEntity.setType(WebServiceEntity.TYPE_WSDL);
      wsEntity.setModel(definition);
    }
    return definition;
  }

  public void parse() throws MalformedURLException, IOException, ParserConfigurationException, SAXException, WWWAuthenticationException
  {
    parseURL(PARSE_WSIL | PARSE_WSDL | PARSE_LINKS | PARSE_DISCO);
  }

  public void parse(int parseOption) throws MalformedURLException, IOException, ParserConfigurationException, SAXException, WWWAuthenticationException
  {
    parseURL(parseOption);
  }

  private void parseURL(int parseOption) throws MalformedURLException, IOException, ParserConfigurationException, SAXException, WWWAuthenticationException
  {
    WebServiceEntity wsEntity = new WebServiceEntity();
    
    // This variable makes this object a little more thread safe than it was
    // before, although this object is not completely thread safe.  The scenario
    // that we are trying to avoid is where one call to this method gets blocked
    // at the getInputStreamAsByteArray call.  Then a second call to the method
    // is made that changes the uri_ global variable.  When the first call
    // completes it would use uri_ value from the second invocation instead
    // of the value from the first.  Storing the uri_ into this variable helps 
    // avoid this bad scenario.
    String theUri = uri_;
    
    wsEntity.setURI(theUri);
    byte[] b = getInputStreamAsByteArray(theUri);
    wsEntity.setBytes(b);
    setHTTPSettings(wsEntity);
    uriToEntityTable_.put(theUri, wsEntity);
    // parse uri_ as a HTML document
    HTMLHeadHandler headHandler = new HTMLHeadHandler(theUri);
    byte[] head = headHandler.harvestHeadTags(b);
    String byteEncoding = headHandler.getByteEncoding();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setNamespaceAware(false);
    factory.setValidating(false);
    SAXParser parser = factory.newSAXParser();
    try
    {
      ByteArrayInputStream bais = new ByteArrayInputStream(head);
      InputStreamReader isr = new InputStreamReader(bais, byteEncoding);
      InputSource is = new InputSource(isr);
      parser.parse(is, headHandler);
    }
    catch (Exception t)
    {
    }
    String[] wsilURIs = headHandler.getWsils();
    String[] discoURIs = headHandler.getDiscos();
    // true if uri_ is a HTML document
    if (wsilURIs.length > 0 || discoURIs.length > 0)
    {
      wsEntity.setType(WebServiceEntity.TYPE_HTML);
      for (int i = 0; i < wsilURIs.length; i++)
      {
        String absoluteURI = convertToAbsoluteURI(theUri, wsilURIs[i]);
        WebServiceEntity wsilEntity = new WebServiceEntity();
        wsilEntity.setType(WebServiceEntity.TYPE_WSIL);
        wsilEntity.setURI(absoluteURI);
        associate(wsEntity, wsilEntity);
        uriToEntityTable_.put(absoluteURI, wsilEntity);
        if ((parseOption | PARSE_WSIL) == parseOption)
        {
          try
          {
            parseWSIL(absoluteURI, parseOption, byteEncoding );
          }
          catch (Exception t)
          {
          }
        }
      }
      for (int i=0;i<discoURIs.length;i++)
      {
        WebServiceEntity discoEntity = new WebServiceEntity();
        discoEntity.setType(WebServiceEntity.TYPE_DISCO);
        discoEntity.setURI(discoURIs[i]);
        associate(wsEntity,discoEntity);
        uriToEntityTable_.put(discoURIs[i],discoEntity);
        if ((parseOption | PARSE_DISCO) == parseOption)
        {
          try
          {
            parseDISCO(discoURIs[i],parseOption);
          }
          catch (Exception t)
          {
          }
        }
      }
    }
    // false if uri_ is not a HTML document
    // then parse uri_ as a WSIL document
    else
    {
      try
      {
        parseWSIL(theUri, parseOption, byteEncoding );
        // no exception thrown if uri_ is a WSIL document
        wsEntity.setType(WebServiceEntity.TYPE_WSIL);
      }
      catch (Exception t)
      {
        // exception thrown if uri_ is not a WSIL document
        // then parse uri_ as a DISCO document.
        try
        {
          parseDISCO(theUri, parseOption);
          // no exception thrown if uri_ is a DISCO document
          wsEntity.setType(WebServiceEntity.TYPE_DISCO);
        }
        catch (Exception t2)
        {
          // exception thrown if uri_ is not a DISCO document
          // then parse uri_ as a WSDL document
          try
          {
            parseWSDL(theUri);
            // no exception thrown if uri_ is a WSDL document
            wsEntity.setType(WebServiceEntity.TYPE_WSDL);
          }
          catch (Exception t3)
          {
            // exception thrown if uri_ is not a WSDL document
            // then do nothing
          }
        }
      }
    }
  }

  private void parseWSIL(String wsilURI, int parseOption, String byteEncoding ) throws WSILException, MalformedURLException, IOException, WSDLException, WWWAuthenticationException
  {
    WebServiceEntity wsilEntity = getWebServiceEntityByURI(wsilURI);
    WSILDocument wsilDoc = (WSILDocument)wsilEntity.getModel();
    if (wsilDoc != null)
    {
      // Prevent infinite loops from occurring when a WSIL cycles occur.
      return;
    }
    wsilDoc = getWSILDocumentVerbose(wsilURI, byteEncoding );
    Inspection inspection = wsilDoc.getInspection();
    Service[] services = inspection.getServices();
    for (int i = 0; i < services.length; i++)
    {
      Description[] descs = services[i].getDescriptions();
      // Set the documentation to the <service> element's first abstract.
      String documentation = null;
      Abstract[] abstracts = services[i].getAbstracts();
      if (abstracts != null && abstracts.length > 0)
        documentation = abstracts[0].getText();
      for (int j = 0; j < descs.length; j++)
      {
      	String referencedNS = descs[j].getReferencedNamespace();
      	// If a <description> element contains an abstract, use it to override the service documentation.      	
      	abstracts = descs[j].getAbstracts();
      	if (abstracts != null && abstracts.length > 0)
      	  documentation = abstracts[0].getText();
        if (WSDLConstants.NS_URI_WSDL.equals(referencedNS))
        {
          String location = descs[j].getLocation();
          if (location != null && location.length() > 0)
          {
            String absoluteURI = convertToAbsoluteURI(wsilURI, location);
            WebServiceEntity wsdlEntity = new WebServiceEntity();
            wsdlEntity.setType(WebServiceEntity.TYPE_WSDL);
            wsdlEntity.setURI(absoluteURI);
            wsdlEntity.setDocumentation(documentation);
            associate(wsilEntity, wsdlEntity);
            uriToEntityTable_.put(absoluteURI, wsdlEntity);
            if ((parseOption | PARSE_WSDL) == parseOption)
              parseWSDL(absoluteURI);
          }
        }
        else if (UDDIConstants.NS_URI_UDDI_V2.equals(referencedNS))
        {
          ExtensionElement ee = descs[j].getExtensionElement();
          if (ee instanceof ServiceDescription)
          {
            ServiceDescription sd = (ServiceDescription)ee;
            String inquiryURL = sd.getLocation();
            String serviceKey = sd.getServiceKey().getText();
            WebServiceEntity uddiServiceEntity = new WebServiceEntity();
            uddiServiceEntity.setType(WebServiceEntity.TYPE_UDDI_SERVICE);
            String uddiServiceKeyURI = UDDIURIHelper.getServiceKeyURI(serviceKey,inquiryURL);
            uddiServiceEntity.setURI(uddiServiceKeyURI);
            uddiServiceEntity.setDocumentation(documentation);
            associate(wsilEntity,uddiServiceEntity);
            uriToEntityTable_.put(uddiServiceKeyURI,uddiServiceEntity);
            // TODO: parse WSDL if necessary...
          }
        }
      }
    }
    Link[] links = inspection.getLinks();    
    for (int i = 0; i < links.length; i++)
    {
      if (WSILConstants.NS_URI_WSIL.equals(links[i].getReferencedNamespace()))
      {
        String documentation = null;
        Abstract[] abstracts = links[i].getAbstracts();
        if (abstracts != null && abstracts.length > 0)
          documentation = abstracts[0].getText();
        String linkLocation = links[i].getLocation();
        String absoluteURI = convertToAbsoluteURI(wsilURI, linkLocation);
        // Prevent cycles.
        WebServiceEntity wsilLinkEntity = getWebServiceEntityByURI(absoluteURI);
        if (wsilLinkEntity == null)
        {
          wsilLinkEntity = new WebServiceEntity();
          wsilLinkEntity.setType(WebServiceEntity.TYPE_WSIL);
          wsilLinkEntity.setURI(absoluteURI);
          wsilLinkEntity.setDocumentation(documentation);
          uriToEntityTable_.put(absoluteURI, wsilLinkEntity);
          if ((parseOption | PARSE_LINKS) == parseOption)
            parseWSIL(absoluteURI, parseOption, byteEncoding );
        }
        associate(wsilEntity,wsilLinkEntity);
      }
    }
  }
  
  private void parseDISCO(String discoURI, int parseOption) throws MalformedURLException, WWWAuthenticationException, Exception
  {
    WebServiceEntity discoEntity = getWebServiceEntityByURI(discoURI);
    byte[] b = getInputStreamAsByteArray(discoURI);
    discoEntity.setBytes(b);
    setHTTPSettings(discoEntity);
    ByteArrayInputStream bais = new ByteArrayInputStream(b);
    DISCOParser parser = new DISCOParser();
    DISCOReference[] references = parser.parse(discoURI,new InputSource(bais));
    if (references != null && references.length > 0)
    {
      for (int i=0;i<references.length;i++)
      {
        if (references[i] instanceof DISCOContractReference)
        {
          // contractRef
          DISCOContractReference reference = (DISCOContractReference)references[i];
          String ref = reference.getRef();
          String docRef = reference.getDocRef();
          WebServiceEntity wsdlEntity = new WebServiceEntity();
          wsdlEntity.setType(WebServiceEntity.TYPE_WSDL);
          wsdlEntity.setURI(ref);
          wsdlEntity.setDocumentation(docRef);
          associate(discoEntity,wsdlEntity);
          uriToEntityTable_.put(ref,wsdlEntity);
          if ((parseOption | PARSE_WSDL) == parseOption)
            parseWSDL(ref);
        }
        else
        {
          // discoveryRef
          String ref = references[i].getRef();
          // Prevent cycles.
          WebServiceEntity discoLinkEntity = getWebServiceEntityByURI(ref);
          if (discoLinkEntity == null)
          {
            discoLinkEntity = new WebServiceEntity();
            discoLinkEntity.setType(WebServiceEntity.TYPE_DISCO);
            discoLinkEntity.setURI(ref);
            uriToEntityTable_.put(ref,discoLinkEntity);
            if ((parseOption | PARSE_LINKS) == parseOption)
              parseDISCO(ref,parseOption);
          }
          associate(discoEntity,discoLinkEntity);
        }
      }
    }
  }

  private Definition parseWSDL(String wsdlURI) throws WSDLException, MalformedURLException, IOException, WWWAuthenticationException
  {
    return getWSDLDefinitionVerbose(wsdlURI);
  }

  private byte[] getInputStreamAsByteArray(String uriString) throws MalformedURLException, IOException, WWWAuthenticationException
  {
    // Try to get a cached copy of the byte[]
    WebServiceEntity wsEntity = getWebServiceEntityByURI(uriString);
    if (wsEntity != null)
    {
      byte[] bytes = wsEntity.getBytes();
      if (bytes != null)
        return bytes;
    }
    // Get the byte[] by opening a stream to the URI
    URL url = createURL(uriString);
    URLConnection conn = url.openConnection();
    // proxy server setting
    String proxyUserName = System.getProperty("http.proxyUserName");
    String proxyPassword = System.getProperty("http.proxyPassword");
    if (proxyUserName != null && proxyPassword != null)
    {
      StringBuffer userNamePassword = new StringBuffer(proxyUserName);
      userNamePassword.append(':').append(proxyPassword);
      Base64 encoder = new Base64();
      String encoding = new String(encoder.encode(userNamePassword.toString().getBytes()));
      userNamePassword.setLength(0);
      userNamePassword.append("Basic ").append(encoding);
      conn.setRequestProperty("Proxy-authorization", userNamePassword.toString());
    }
    // HTTP basic authentication setting
    if (httpBasicAuthUsername_ != null && httpBasicAuthPassword_ != null)
    {
      StringBuffer sb = new StringBuffer(httpBasicAuthUsername_);
      sb.append(':').append(httpBasicAuthPassword_);
      Base64 encoder = new Base64();
      String encoding = new String(encoder.encode(sb.toString().getBytes()));
      sb.setLength(0);
      sb.append("Basic ").append(encoding);
      conn.setRequestProperty("Authorization", sb.toString());
    }
    InputStream is = null;
    try
    {
      is = conn.getInputStream();
      String wwwAuthMsg = conn.getHeaderField("WWW-Authenticate");
      if (wwwAuthMsg != null)
        throw new WWWAuthenticationException(new IOException(), wwwAuthMsg, uriString);
    }
    catch (IOException ioe)
    {
      String wwwAuthMsg = conn.getHeaderField("WWW-Authenticate");
      if (wwwAuthMsg != null)
        throw new WWWAuthenticationException(ioe, wwwAuthMsg, uriString);
      else
        throw ioe;
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] b = new byte[1024];
    int byteRead = is.read(b);
    while (byteRead != -1)
    {
      baos.write(b, 0, byteRead);
      byteRead = is.read(b);
    }
    is.close();
    return baos.toByteArray();
  }

  protected URL createURL(String url) throws MalformedURLException
  {
    return new URL(url);
  }

  private void associate(WebServiceEntity parent, WebServiceEntity child)
  {
    parent.addChild(child);
    child.setParent(parent);
  }

  private String convertToAbsoluteURI(String baseURI,String refURI)
  {
    if (refURI != null && refURI.indexOf(":") < 0)
    {
      StringBuffer absoluteURI = new StringBuffer(baseURI.substring(0,Math.max(baseURI.lastIndexOf('\\'),baseURI.lastIndexOf('/')+1)));
      absoluteURI.append(refURI);
      return absoluteURI.toString();
    }
    return refURI;
  }

  private void setHTTPSettings(WebServiceEntity entity)
  {
    if (httpBasicAuthUsername_ != null && httpBasicAuthPassword_ != null)
    {
      entity.setHTTPUsername(httpBasicAuthUsername_);
      entity.setHTTPPassword(httpBasicAuthPassword_);
    }
  }

  public String getHTTPBasicAuthUsername()
  {
    return httpBasicAuthUsername_;
  }

  public void setHTTPBasicAuthUsername(String username)
  {
    httpBasicAuthUsername_ = username;
  }

  public String getHTTPBasicAuthPassword()
  {
    return httpBasicAuthPassword_;
  }

  public void setHTTPBasicAuthPassword(String password)
  {
    httpBasicAuthPassword_ = password;
  }
}

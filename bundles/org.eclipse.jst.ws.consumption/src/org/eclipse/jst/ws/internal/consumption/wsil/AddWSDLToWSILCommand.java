/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.wsil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.wsdl.Definition;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.wsil.Abstract;
import org.apache.wsil.Description;
import org.apache.wsil.Inspection;
import org.apache.wsil.QName;
import org.apache.wsil.Service;
import org.apache.wsil.WSILDocument;
import org.apache.wsil.WSILException;
import org.apache.wsil.extension.ExtensionBuilder;
import org.apache.wsil.extension.wsdl.ImplementedBinding;
import org.apache.wsil.extension.wsdl.Reference;
import org.apache.wsil.extension.wsdl.ReferencedService;
import org.apache.wsil.extension.wsdl.WSDLConstants;
import org.apache.wsil.xml.XMLWriter;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.uri.URIException;
import org.eclipse.wst.command.env.core.uri.URIFactory;
import org.eclipse.wst.ws.parser.wsil.IllegalArgumentsException;
import org.eclipse.wst.ws.parser.wsil.WWWAuthenticationException;
import org.eclipse.wst.ws.parser.wsil.WWWAuthenticationHandler;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class AddWSDLToWSILCommand extends SimpleCommand
{
  public static final String ARG_WSIL = "-wsil";
  public static final String ARG_WSDL = "-wsdl";
  public static final String ARG_RESOLVE_WSDL = "-resolvewsdl";
  public static final String ARG_HTTP_BASIC_AUTH_USERNAME = "-httpusername";
  public static final String ARG_HTTP_BASIC_AUTH_PASSWORD = "-httppassword";
  private final String id = "org.eclipse.wst.ws.parser.wsil.AddWSDLToWSILCommand";
  private ResourceBundle resBundle_;
  private WWWAuthenticationHandler wwwAuthHandler_;
  private String[] args_;
  private String wsil_;
  private ArrayList wsdls_;
  private boolean resolveWSDL_;
  private String httpUsername_;
  private String httpPassword_;

  public AddWSDLToWSILCommand()
  {
    super("org.eclipse.wst.ws.parser.wsil.AddWSDLToWSILCommand", "org.eclipse.wst.ws.parser.wsil.AddWSDLToWSILCommand");
    resBundle_ = ResourceBundle.getBundle("org.eclipse.wst.ws.parser.wsil.wsil");
    wwwAuthHandler_ = null;
    args_ = new String[0];
    clearParsedArgs();
  }

  private void clearParsedArgs()
  {
    wsil_ = null;
    if (wsdls_ == null)
      wsdls_ = new ArrayList();
    else
      wsdls_.clear();
    resolveWSDL_ = false;
    httpUsername_ = null;
    httpPassword_ = null;
  }

  /**
   * Executes the Command.
   * 
   * @param environment
   *            The environment. Must not be null.
   * @return A <code>Status</code> object indicating the degree to which the
   *         <code>execute</code> method was successful. A valud of <code>null</code>,
   *         or a Status with a severity of less than <code>Status.ERROR</code>
   *         signifies success.
   */
  public Status execute(Environment environment)
  {
    URIFactory uriFactory = environment.getURIFactory();
    // Parse arguments
    try
    {
      parseArguments();
    }
    catch (IllegalArgumentsException iae)
    {
      return new SimpleStatus(id, resBundle_.getString("MSG_ERROR_ILLEGAL_ARGUMENTS"), Status.ERROR, iae);
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      return new SimpleStatus(id, resBundle_.getString("MSG_ERROR_INVALID_ARGUMENTS"), Status.ERROR, e);
    }
    // Create new WSIL document
    WSILDocument wsilDocument = null;
    URL wsilURL = null;
    try
    {
      wsilDocument = WSILDocument.newInstance();
      wsilURL = new URL(wsil_);
    }
    catch (MalformedURLException murle)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_INVALID_WSIL_URI"), Status.ERROR, murle);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    catch (WSILException wsile)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNEXPECTED_EXCEPTION"), Status.ERROR, wsile);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    // Read WSIL
    try
    {
      InputStream is = uriFactory.newURI(wsilURL).getInputStream();
      if (is != null)
      {
        wsilDocument.read(new InputStreamReader(is));
        is.close();
      }
    }
    catch (URIException urie)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNEXPECTED_EXCEPTION"), Status.ERROR, urie);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    catch (IOException ioe)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNEXPECTED_EXCEPTION"), Status.ERROR, ioe);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    catch (WSILException wsile)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNEXPECTED_EXCEPTION"), Status.ERROR, wsile);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    // internalExecute
    String httpUsername = null;
    String httpPassword = null;
    if (wsdls_.size() == 1 && httpUsername_ != null && httpPassword_ != null)
    {
      httpUsername = httpUsername_;
      httpPassword = httpPassword_;
    }
    for (Iterator it = wsdls_.iterator(); it.hasNext();)
    {
      String wsdl = (String) it.next();
      Status status = null;
      try
      {
        status = internalExecute(environment, wsilDocument, platform2File(uriFactory, wsil_), wsdl, httpUsername, httpPassword);
      }
      catch (WWWAuthenticationException wwwae)
      {
        if (wwwAuthHandler_ != null && httpUsername == null && httpPassword == null)
        {
          wwwAuthHandler_.handleWWWAuthentication(wwwae);
          String handlerUsername = wwwAuthHandler_.getUsername();
          String handlerPassword = wwwAuthHandler_.getPassword();
          if (handlerUsername != null && handlerPassword != null)
          {
            try
            {
              status = internalExecute(environment, wsilDocument, platform2File(uriFactory, wsil_), wsdl, handlerUsername, handlerPassword);
            }
            catch (WWWAuthenticationException wwwae2)
            {
              Status wwwaeStatus = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNRESOLVABLE_WSDL"), Status.ERROR, wwwae2.getIOException());
              environment.getStatusHandler().reportError(wwwaeStatus);
              return wwwaeStatus;
            }
          }
        }
        if (status == null)
        {
          Status s = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNRESOLVABLE_WSDL"), Status.ERROR, wwwae.getIOException());
          environment.getStatusHandler().reportError(s);
          return s;
        }
      }
      if (status.getSeverity() != Status.OK)
        return status;
    }
    // Write WSIL
    try
    {
      XMLWriter xmlWriter = new XMLWriter();
      OutputStream os = uriFactory.newURI(wsilURL).getOutputStream();
      if (os != null)
        xmlWriter.writeDocument(wsilDocument, os);
      else
        throw new IOException();
      os.close();
    }
    catch (URIException urie)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_WRITE_WSIL"), Status.ERROR, urie);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    catch (IOException ioe)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_WRITE_WSIL"), Status.ERROR, ioe);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    catch (WSILException wsile)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_WRITE_WSIL"), Status.ERROR, wsile);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    return new SimpleStatus(id, "", Status.OK, null);
  }

  private Status internalExecute(Environment environment, WSILDocument wsilDocument, String wsil, String wsdl, String httpUsername, String httpPassword) throws WWWAuthenticationException
  {
    Definition definition = null;
    ArrayList wsdlService = new ArrayList();
    ArrayList wsdlBinding = new ArrayList();
    if (resolveWSDL_)
    {
      // Parse WSDL
      try
      {
        definition = parseWSDL(wsdl, wsdlService, wsdlBinding, httpUsername, httpPassword);
      }
      catch (MalformedURLException murle)
      {
        Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_INVALID_WSDL_URI"), Status.ERROR, murle);
        environment.getStatusHandler().reportError(status);
        return status;
      }
      catch (IOException ioe)
      {
        Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNRESOLVABLE_WSDL"), Status.ERROR, ioe);
        environment.getStatusHandler().reportError(status);
        return status;
      }
      catch (ParserConfigurationException pce)
      {
        Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNEXPECTED_EXCEPTION"), Status.ERROR, pce);
        environment.getStatusHandler().reportError(status);
        return status;
      }
      catch (SAXException saxe)
      {
        Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_MALFORMED_WSDL"), Status.ERROR, saxe);
        environment.getStatusHandler().reportError(status);
        return status;
      }
    }
    // Add WSDL to WSIL
    try
    {
      Inspection inspection = wsilDocument.getInspection();
      Service service = wsilDocument.createService();
      if (definition != null)
      {
        Element documentation = definition.getDocumentationElement();
        Abstract serviceAbstract = wsilDocument.createAbstract();
        serviceAbstract.setText(Element2String(documentation));
        service.addAbstract(serviceAbstract);
      }
      Description desc = wsilDocument.createDescription();
      desc.setReferencedNamespace(WSDLConstants.NS_URI_WSDL);
      desc.setLocation(getRelativeURI(wsil, wsdl));
      ExtensionBuilder extBuilder = wsilDocument.getExtensionRegistry().getBuilder(WSDLConstants.NS_URI_WSIL_WSDL);
      if (definition == null || endpointPresent(definition))
      {
        for (Iterator it = wsdlService.iterator(); it.hasNext();)
        {
          Reference reference = (Reference) extBuilder.createElement(new QName(WSDLConstants.NS_URI_WSIL_WSDL, WSDLConstants.ELEM_REFERENCE));
          reference.setEndpointPresent(new Boolean(true));
          ReferencedService refService = (ReferencedService) extBuilder.createElement(new QName(WSDLConstants.NS_URI_WSIL_WSDL, WSDLConstants.ELEM_REF_SERVICE));
          refService.setReferencedServiceName((QName) it.next());
          reference.setReferencedService(refService);
          desc.setExtensionElement(reference);
        }
      }
      else
      {
        Reference reference = (Reference) extBuilder.createElement(new QName(WSDLConstants.NS_URI_WSIL_WSDL, WSDLConstants.ELEM_REFERENCE));
        reference.setEndpointPresent(new Boolean(false));
        for (Iterator it = wsdlBinding.iterator(); it.hasNext();)
        {
          ImplementedBinding binding = (ImplementedBinding) extBuilder.createElement(new QName(WSDLConstants.NS_URI_WSIL_WSDL, WSDLConstants.ELEM_IMPL_BINDING));
          binding.setBindingName((QName) it.next());
          reference.addImplementedBinding(binding);
        }
      }
      service.addDescription(desc);
      inspection.addService(service);
    }
    catch (WSILException wsile)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNEXPECTED_EXCEPTION"), Status.ERROR, wsile);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    catch (Throwable t)
    {
      Status status = new SimpleStatus(id, resBundle_.getString("MSG_ERROR_UNEXPECTED_EXCEPTION"), Status.ERROR, t);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    return new SimpleStatus(id, "", Status.OK, null);
  }

  /**
   * Returns <code>true</code> if, and only if, the Command's <code>undo</code>
   * method is supported.
   * 
   * @return True if the Command supports being undone.
   */
  public boolean isUndoable()
  {
    return false;
  }

  /**
   * Undoes the Command.
   * 
   * @param environment
   *            The environment. Must not be null.
   * @return A <code>Status</code> object indicating the degree to which the
   *         <code>undo</code> method was successful. A valud of <code>null</code>,
   *         or a Status with a severity of less than <code>Status.ERROR</code>
   *         signifies success.
   */
  public Status undo(Environment environment)
  {
    return new SimpleStatus(id, "", Status.OK, null);
  }

  /**
   * Re-executes the Command.
   * 
   * @param environment
   *            The environment. Must not be null.
   * @return A <code>Status</code> object indicating the degree to which the
   *         <code>redo</code> method was successful. A value of <code>null</code>,
   *         or a Status with a severity of less then <code>Status.ERROR</code>
   *         signifies success.
   */
  public Status redo(Environment environment)
  {
    return execute(environment);
  }

  private Definition parseWSDL(String wsdl, ArrayList wsdlService, ArrayList wsdlBinding, String httpUsername, String httpPassword) throws MalformedURLException, IOException, ParserConfigurationException, SAXException, WWWAuthenticationException
  {
    WebServicesParser wsParser = new WebServicesParser(wsdl);
    if (httpUsername != null && httpPassword != null)
    {
      wsParser.setHTTPBasicAuthUsername(httpUsername);
      wsParser.setHTTPBasicAuthPassword(httpPassword);
    }
    wsParser.parse(WebServicesParser.PARSE_NONE);
    Definition definition = wsParser.getWSDLDefinition(wsdl);
    if (definition != null)
    {
      Map services = definition.getServices();
      for (Iterator it = services.keySet().iterator(); it.hasNext();)
        wsdlService.add(parseQName((javax.xml.namespace.QName) it.next()));
      Map bindings = definition.getBindings();
      for (Iterator it = bindings.keySet().iterator(); it.hasNext();)
        wsdlBinding.add(parseQName((javax.xml.namespace.QName) it.next()));
    }
    return definition;
  }

  private boolean endpointPresent(Definition definition)
  {
    return (definition.getServices().size() > 0);
  }

  public String[] getArguments()
  {
    return args_;
  }

  public void setArguments(String[] args)
  {
    args_ = args;
  }

  public WWWAuthenticationHandler getWWWAuthenticationHandler()
  {
    return wwwAuthHandler_;
  }

  public void setWWWAuthenticationHandler(WWWAuthenticationHandler wwwAuthHandler)
  {
    wwwAuthHandler_ = wwwAuthHandler;
  }

  private void parseArguments() throws IllegalArgumentsException
  {
    clearParsedArgs();
    for (int i = 0; i < args_.length; i++)
    {
      if (ARG_WSIL.equals(args_[i]))
        wsil_ = args_[++i];
      else if (ARG_WSDL.equals(args_[i]))
        wsdls_.add(args_[++i]);
      else if (ARG_RESOLVE_WSDL.equals(args_[i]))
        resolveWSDL_ = true;
      else if (ARG_HTTP_BASIC_AUTH_USERNAME.equals(args_[i]))
        httpUsername_ = args_[++i];
      else if (ARG_HTTP_BASIC_AUTH_PASSWORD.equals(args_[i]))
        httpPassword_ = args_[++i];
      else
        throw new IllegalArgumentsException(args_[i]);
    }
  }

  private QName parseQName(String qnameString)
  {
    int colonIndex = qnameString.indexOf(':');
    String ns = (colonIndex != -1) ? qnameString.substring(0, colonIndex) : "";
    String localpart = (colonIndex != -1) ? qnameString.substring(colonIndex + 1, qnameString.length()) : qnameString;
    return new QName(ns, localpart);
  }

  private QName parseQName(javax.xml.namespace.QName qname)
  {
    return new QName(qname.getNamespaceURI(), qname.getLocalPart());
  }

  private String Element2String(Element e) throws TransformerConfigurationException, TransformerException, IOException
  {
    byte[] b = new byte[0];
    if (e != null)
    {
      DOMSource domSource = new DOMSource(e);
      Transformer serializer = TransformerFactory.newInstance().newTransformer();
      serializer.setOutputProperty(OutputKeys.INDENT, "yes");
      serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      serializer.transform(domSource, new StreamResult(baos));
      baos.close();
      b = baos.toByteArray();
    }
    return new String(b);
  }

  private String platform2File(URIFactory uriFactory, String uri)
  {
    try
    {
      return uriFactory.newURI(uri).asFile().toURL().toString();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
    return uri;
  }

  private final char FWD_SLASH = '/';
  private final char BWD_SLASH = '\\';
  private final String parentDir = "../";

  private String getRelativeURI(String baseURI, String targetURI)
  {
    try
    {
      String baseURICopy = baseURI.replace(BWD_SLASH, FWD_SLASH);
      String targetURICopy = targetURI.replace(BWD_SLASH, FWD_SLASH);
      int matchingIndex = -1;
      int index = targetURICopy.indexOf(FWD_SLASH);
      while (index != -1)
      {
        if (baseURICopy.startsWith(targetURICopy.substring(0, index + 1)))
        {
          matchingIndex = index;
          index = targetURICopy.indexOf(FWD_SLASH, index + 1);
        }
        else
          break;
      }
      if (matchingIndex != -1)
      {
        int slashCount = -1;
        int fromIndex = matchingIndex;
        while (fromIndex != -1)
        {
          slashCount++;
          fromIndex = baseURICopy.indexOf(FWD_SLASH, fromIndex + 1);
        }
        StringBuffer relativeURI = new StringBuffer();
        for (int i = 0; i < slashCount; i++)
          relativeURI.append(parentDir);
        relativeURI.append(targetURI.substring(matchingIndex + 1, targetURI.length()));
        return relativeURI.toString();
      }
    }
    catch (Throwable t)
    {
    }
    return targetURI;
  }
}
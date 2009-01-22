/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20061213   165723 kathy@ca.ibm.com - Kathy Chan
 * 20061213   165725 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.command.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.uri.URIException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.discovery.NetUtils;
import org.eclipse.wst.ws.internal.parser.wsil.WWWAuthenticationException;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.eclipse.xsd.util.XSDParser;
import org.w3c.dom.Element;

public class CopyWSDLCommand extends AbstractDataModelOperation
{
  private String wsdlURI;
  private WebServicesParser webServicesParser;
  private String destinationURI;
  private Definition def;
  private Vector ignoreList;

  public CopyWSDLCommand()
  {
  }

  /**
   * Execute the command
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    CopyWSDLRunnable copyWSDLRunnable = new CopyWSDLRunnable(env);
    try
    {
      ResourceUtils.getWorkspace().run(copyWSDLRunnable, null);
      return Status.OK_STATUS;
    }
    catch (CoreException ce)
    {
        IStatus status = ce.getStatus();
		env.getStatusHandler().reportError(status);
      return status;
    }
  }

   
  private class CopyWSDLRunnable implements IWorkspaceRunnable {
  	
  	private IEnvironment environment = null;
  	
  	protected CopyWSDLRunnable(IEnvironment env){
  		environment = env;
  	}
  	public void run(IProgressMonitor pm) throws CoreException {
  		ignoreList = new Vector();
  		if (def == null)
  			def = webServicesParser.getWSDLDefinition(wsdlURI);
  		try {
  			copyWSDL(environment, wsdlURI, getBaseURI(destinationURI), getLocalname(destinationURI), def);
  		} catch (Throwable e) {
  			throw new CoreException(StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_COPY_WSDL, new String[]{wsdlURI, destinationURI}), e));
  		}
  	}
  }
  private void copyWSDL(IEnvironment env, String uri, String destURI, String destLocalname) throws WSDLException, IOException, WWWAuthenticationException, TransformerException, TransformerConfigurationException, URIException
  {
  	Definition definition;
	
	try {
		definition = webServicesParser.getWSDLDefinitionVerbose(uri);
		copyWSDL(env, uri, destURI, destLocalname, definition);	
	} catch (WSDLException e) {
		copyXMLSchema(env, uri, destURI);
	}
  }

  private void copyWSDL(IEnvironment env, String uri, String destURI, String destLocalname, Definition definition) throws WSDLException, IOException, WWWAuthenticationException, TransformerException, TransformerConfigurationException, URIException
  {
	uri = normalize(uri);
	if (!needToCopy(uri)) {
	   	return;
	}
	destURI = normalize(destURI);
    String baseURI = getBaseURI(uri);
    if (destLocalname == null || destLocalname.length() <= 0)
      destLocalname = getLocalname(uri);
    
    // copy WSDL
    StringBuffer destinationFileURI = new StringBuffer(addTrailingSeparator(destURI));
    destinationFileURI.append(destLocalname);
    WSDLFactory wsdlFactory = new WSDLFactoryImpl();
    WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
    OutputStream os = env.getURIFactory().newURI(destinationFileURI.toString()).getOutputStream();
    wsdlWriter.writeWSDL(definition, os);
    os.close();
    // copy <wsdl:import>s
    Map imports = definition.getImports();
    for (Iterator it = imports.values().iterator(); it.hasNext();)
    {
      List list = (List)it.next();
      for (Iterator listIt = list.iterator(); listIt.hasNext();)
      {
        Import wsdlImport = (Import)listIt.next();
        String wsdlImportURI = wsdlImport.getLocationURI();
        if (isRelative(wsdlImportURI))
        {
          String importBaseURI = (new StringBuffer(baseURI)).append(wsdlImportURI).toString();
          String wsdlImportBaseURI = getBaseURI(wsdlImportURI);
          StringBuffer importDestURI = new StringBuffer(destURI);
          if (wsdlImportBaseURI != null)
            importDestURI.append(wsdlImportBaseURI);
          copyWSDL(env, importBaseURI, importDestURI.toString(), getLocalname(wsdlImportURI));
        }
      }
    }
    Types types = definition.getTypes();
    if (types != null)
    {
      List schemas = types.getExtensibilityElements();
      for (Iterator it = schemas.iterator(); it.hasNext();)
      {
        ExtensibilityElement extElement = (ExtensibilityElement)it.next();
        if (extElement instanceof UnknownExtensibilityElement)
        {
          UnknownExtensibilityElement schemaElement = (UnknownExtensibilityElement)extElement;
          XSDSchema xsdSchema = null;
          try
          {
            xsdSchema = XSDSchemaImpl.createSchema(schemaElement.getElement());
          }
          catch (Throwable t)
          {
            // ignore any extensibility elements that cannot be parsed into a
            // XSDSchema instance
          }
          copyXMLSchema(env, xsdSchema, baseURI, destURI);
        }
        else if (extElement instanceof XSDSchemaExtensibilityElementImpl)
        {
          XSDSchemaExtensibilityElementImpl schemaElement = (XSDSchemaExtensibilityElementImpl)extElement;
          XSDSchema xsdSchema = null;
          try
          {
            xsdSchema = XSDSchemaImpl.createSchema(schemaElement.getElement());
          }
          catch (Throwable t)
          {
            // ignore any extensibility elements that cannot be parsed into a
            // XSDSchema instance
          }
          copyXMLSchema(env, xsdSchema, baseURI, destURI);
        }
        if (extElement instanceof Schema)
        {
          Schema schemaElement = (Schema)extElement;
          XSDSchema xsdSchema = null;
          try
          {
            xsdSchema = XSDSchemaImpl.createSchema(schemaElement.getElement());
          }
          catch (Throwable t)
          {
            // ignore any extensibility elements that cannot be parsed into a
            // XSDSchema instance
          }
          copyXMLSchema(env, xsdSchema, baseURI, destURI);
        }
      }
      
    }
  }

  private String getBaseURI(String uri)
  {
    int index = uri.lastIndexOf('/');
    if (index == -1)
      index = uri.lastIndexOf('\\');
    if (index != -1)
      return uri.substring(0, index + 1);
    else
      return null;
  }

  private String getLocalname(String uri)
  {
    int index = uri.lastIndexOf('/');
    if (index == -1)
      index = uri.lastIndexOf('\\');
    if (index != -1)
      return uri.substring(index + 1);
    else
      return uri;
  }

  private String addTrailingSeparator(String s)
  {
    if (!(s.endsWith("/") || s.endsWith("\\")))
    {
      StringBuffer sb = new StringBuffer(s);
      sb.append('/');
      return sb.toString();
    }
    else
      return s;
  }

  private boolean isRelative(String uri)
  {
    return (uri.indexOf(':') == -1);
  }

  private void copyXMLSchema(IEnvironment env, String uri, String destURI) throws TransformerException, TransformerConfigurationException, IOException, URIException
  {
	  uri = normalize(uri);
	  if (!needToCopy(uri)) {
	    	return;
	    }
	  destURI = normalize(destURI);
    // load as a cached schema
    XSDSchema xsdSchema = XSDSchemaImpl.getSchemaForSchema(uri);
    // if schema is not cached, parse it
    if (xsdSchema == null)
    {
      XSDParser p = new XSDParser();
      InputStream is = NetUtils.getURLInputStream(uri);
      if (is != null)
      {
        p.parse(is);
        xsdSchema = p.getSchema();
      }
    }
    if (xsdSchema != null)
    {
      // copy schema
      Element e = xsdSchema.getElement();
      DOMSource domSource = new DOMSource(e);
      Transformer serializer = TransformerFactory.newInstance().newTransformer();
      serializer.setOutputProperty(OutputKeys.INDENT, "yes");
      serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      OutputStream os = env.getURIFactory().newURI((new StringBuffer(destURI)).append(getLocalname(uri)).toString()).getOutputStream();
      serializer.transform(domSource, new StreamResult(os));
      os.close();
      // copy <xsd:import>s and <xsd:include>s
      copyXMLSchema(env, xsdSchema, getBaseURI(uri), destURI);
    }
  }

  private void copyXMLSchema(IEnvironment env, XSDSchema xsdSchema, String baseURI, String destURI) throws TransformerException, TransformerConfigurationException, IOException, URIException
  {
    if (xsdSchema != null)
    {
      // copy <xsd:import>s and <xsd:include>s
      EList xsdSchemaContents = xsdSchema.getContents();
      for (Iterator it = xsdSchemaContents.iterator(); it.hasNext();)
      {
        Object content = it.next();
        if (content instanceof XSDSchemaDirective)
        {
          XSDSchemaDirective xsdSchemaDirective = (XSDSchemaDirective)content;
          String xsdSchemaDirectiveLocation = xsdSchemaDirective.getSchemaLocation();
          if (xsdSchemaDirectiveLocation != null && isRelative(xsdSchemaDirectiveLocation))
          {
            String directiveURI = (new StringBuffer(baseURI)).append(xsdSchemaDirectiveLocation).toString();
            String directiveBaseURI = getBaseURI(xsdSchemaDirectiveLocation);
            StringBuffer directiveDestURI = new StringBuffer(destURI);
            if (directiveBaseURI != null)
              directiveDestURI.append(directiveBaseURI);
            copyXMLSchema(env, directiveURI, directiveDestURI.toString());
          }
        }
      }
    }
  }

  private boolean needToCopy (String normalizedURI) {	
	  if (ignoreList.contains(normalizedURI))
		  return false;
	  ignoreList.add(normalizedURI);
	  return true;
  }
  
//normalize the uri to remove relative addressing "/.."
  private String normalize(String uri )
  {
  	boolean normalized = false;
  	while(!normalized){
  	  int dir = uri.indexOf("/..");
  	  if(dir == -1)
  	  	normalized = true;
  	  else{
  	  	String first = uri.substring(0,dir);
  	    String second = uri.substring(dir + 3);
  	    int newIndex = first.lastIndexOf("/");
  	    if(newIndex == -1){
  	    	normalized = true;
  	    } else {
  	    	first = first.substring(0,newIndex);
  	    	uri = first + second;
  	    }
  	  }
  	}
  	return uri;
  
  }
	  
  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }

  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }

  public void setDestinationURI(String destinationURI)
  {
    this.destinationURI = destinationURI;
  }

  public void setDefinition(Definition def)
  {
    this.def = def;
  }
}

/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070112   165721 makandre@ca.ibm.com - Andrew Mak, WSDL import cannot use relative import with to parent directories
 * 20070308   176649 makandre@ca.ibm.com - Andrew Mak, CopyWSDLTreeCommand does not handle "\" correctly in an absolute wsdl URL
 * 20070326   179337 makandre@ca.ibm.com - Andrew Mak, Regen web service skeleton re-copies wsdl and xsd onto themselves
 * 20070531   189734 makandre@ca.ibm.com - Andrew Mak, IResource.refreshLocal needed if we want to take advantage of 179337
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.UniversalPathTransformer;
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

public class CopyWSDLTreeCommand extends AbstractDataModelOperation
{
  private String wsdlURI;
  private WebServicesParser webServicesParser;
  private String destinationURI;
  private Definition def;
  private Vector ignoreList;

  private IPath pathPrefix;  
  private String wsdlRelPath;
  private Vector xmlObjectInfos;
  
  public CopyWSDLTreeCommand()
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
  	private UniversalPathTransformer transformer = null;
  	
  	protected CopyWSDLRunnable(IEnvironment env){
  		environment = env;
  		transformer = new UniversalPathTransformer();
  	}
  	
	/*
	 * Compares the 2 uris and see if they point to the same file. 
	 * We need to convert both uris to filesystem uris in order to compare.
	 */	
	private boolean isSameLocation(String uri1, String uri2) {

		// if either uri is null, we cannot make any meaningful comparison
		if (uri1 == null || uri2 == null)
			return false;
		
		uri1 = transformer.toLocation(uri1);
		uri2 = transformer.toLocation(uri2);
		
		return uri1.equals(uri2);
	}
	
  	public void run(IProgressMonitor pm) throws CoreException {
  		ignoreList = new Vector();
  		xmlObjectInfos = new Vector();
  		if (def == null)
  			def = webServicesParser.getWSDLDefinition(wsdlURI);
  		try {
  			String baseDestinationURI = getBaseURI(destinationURI);
  			
  			// copy will record what files needs to be copy, but does not write the files yet
  			copyWSDL(environment, wsdlURI, baseDestinationURI, getLocalname(destinationURI), def);
  			
  			// begin write
  			
  			Iterator iter = xmlObjectInfos.iterator();  			  			
  			
  			while (iter.hasNext()) {
  				XMLObjectInfo info = (XMLObjectInfo) iter.next();  				
  				IPath relPath = info.path.removeFirstSegments(pathPrefix.segmentCount());
  				  				  				
  				if (info.content instanceof Definition) {
  					Definition definition = (Definition) info.content;
  					
  					// if this is the starting wsdl, check if it's at the root directory
  					if (definition == def)
  						wsdlRelPath = relPath.toString();

  					String destURI = writeXMLObj(environment, baseDestinationURI, relPath.toString(), definition);
  					
  					// for the starting wsdl, if the the source path and destination path are the same,
  					// we do not need to copy the rest of the files onto themselves (bug 179337)
  					if (definition == def && wsdlURI != null &&
  						isSameLocation(normalize(wsdlURI), normalize(destURI)))
  						return;
  				}
  				else
  					writeXMLObj(environment, baseDestinationURI, relPath.toString(), (XSDSchema) info.content);
  			}
  			
  		} catch (Throwable e) {
  			throw new CoreException(StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_COPY_WSDL, new String[]{wsdlURI, destinationURI}), e));
  		}
  	}
  }
  /**
   * Helper class holding information about the files we need to write
   */
  private class XMLObjectInfo {
  	
  	public IPath  path;  	
  	public Object content;
  	  	
  	/**
  	 * Constructor.
  	 * 
  	 * @param uri The URI of the source file
  	 * @param content A representation of the source file
  	 */
  	public XMLObjectInfo(String uri, Object content) {
  		
  		int colon = uri.lastIndexOf(":"); 
  		
  		// don't need protocol or device info
  		if (colon != -1)
  			uri = uri.substring(colon + 1, uri.length());
  		
  		this.path    = new Path(uri);  		
  		this.content = content;
  	}
  }
  
  /**
   * Update the path prefix, which is the shortest parent path
   * common to all files we need to write.
   * 
   * @param info Contains info on file to write.
   */
  private void updatePathPrefix(XMLObjectInfo info) {
  	if (pathPrefix == null)
        pathPrefix = info.path.removeLastSegments(1);
    else {
    	int matching = pathPrefix.matchingFirstSegments(info.path);
    	
    	if (matching < pathPrefix.segmentCount())
    		pathPrefix = pathPrefix.uptoSegment(matching);
    }
  }    
  
  // write a wsdl file, returns the uri of the file written.
  private String writeXMLObj(IEnvironment env, String destURI, String destLocalname, Definition definition) 
	throws WSDLException, IOException, URIException {
	
	StringBuffer destinationFileURI = new StringBuffer(addTrailingSeparator(destURI));
    destinationFileURI.append(destLocalname);   
  
    WSDLFactory wsdlFactory = new WSDLFactoryImpl();
    WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
    OutputStream os = env.getURIFactory().newURI(destinationFileURI.toString()).getOutputStream();
    wsdlWriter.writeWSDL(definition, os);
    os.close();
    
    return destinationFileURI.toString();
  }
  
  // write an xsd file, returns the uri of the file written.
  private String writeXMLObj(IEnvironment env, String destURI, String destLocalname, XSDSchema xsdSchema) 
	throws TransformerConfigurationException, URIException, TransformerException, IOException {
		
	StringBuffer destinationFileURI = new StringBuffer(addTrailingSeparator(destURI));
	destinationFileURI.append(destLocalname);
		
	Element e = xsdSchema.getElement();
	DOMSource domSource = new DOMSource(e);
	Transformer serializer = TransformerFactory.newInstance().newTransformer();
	serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	OutputStream os = env.getURIFactory().newURI(destinationFileURI.toString()).getOutputStream();
	serializer.transform(domSource, new StreamResult(os));
	os.close();
	
	return destinationFileURI.toString();
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
    
    XMLObjectInfo info = new XMLObjectInfo(baseURI + destLocalname, definition);
    updatePathPrefix(info);
    xmlObjectInfos.add(info);

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
        
      XMLObjectInfo info = new XMLObjectInfo(uri, xsdSchema);
      updatePathPrefix(info);
      xmlObjectInfos.add(info);
      
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
    if (wsdlURI != null)
        wsdlURI = wsdlURI.replace('\\', '/');
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
  
  public String getWSDLRelPath() {
    return wsdlRelPath;
  }
}

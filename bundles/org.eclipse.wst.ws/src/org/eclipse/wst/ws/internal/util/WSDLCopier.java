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
 * 20070125   171071 makandre@ca.ibm.com - Andrew Mak, Create public utility method for copying WSDL files
 * 20070409   181635 makandre@ca.ibm.com - Andrew Mak, WSDLCopier utility should create target folder
 * 20071205   211262 ericdp@ca.ibm.com - Eric Peters, CopyWSDLTreeCommand fails to copy ?wsdl
 *******************************************************************************/

package org.eclipse.wst.ws.internal.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.uri.IURIScheme;
import org.eclipse.wst.common.environment.uri.SimpleURIFactory;
import org.eclipse.wst.common.environment.uri.URIException;
import org.eclipse.wst.common.uriresolver.internal.util.URIEncoder;
import org.eclipse.wst.common.uriresolver.internal.util.URIHelper;
import org.eclipse.wst.ws.internal.WstWSPluginMessages;
import org.eclipse.wst.ws.internal.parser.discovery.NetUtils;
import org.eclipse.wst.ws.internal.parser.wsil.WWWAuthenticationException;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.eclipse.xsd.util.XSDParser;
import org.w3c.dom.Element;

/**
 * <p>This is a utility class that can be used to copy a WSDL file and its relative imports
 * (both wsdl:import and xsd:import) to a folder in the filesystem.  The code will analyze
 * the directory structure and does the following:
 * <ol>
 *    <li>find all files that needs to be copied and tag them as such.</li>
 *    <li>find the common parent directory of all the files that are tagged; this is the 
 *    root of the directory structure.</li>
 * </ol>
 * During the copy, the root of the directory structure is be mapped to the 
 * specified target folder and all tagged files will be duplicated under it.</p>
 * 
 * <p>If the WSDL source and the target location are the same, this utility only guarantees
 * that the starting WSDL file is "re-copied" onto itself (since the WSDL definition could 
 * have been altered).  No guarantee is made for the other files that the WSDL file imports.</p>
 * 
 * <p>Note that WSDLCopier does not do a direct file copy.  The source files are read into
 * a model and then written out to the target folder, hence the output file might not be
 * syntactically identical to the source file but they will be structurally identical.</p>
 * 
 * <p>Since this utility implements IWorkspaceRunnable, it can be run by the workspace as
 * an atomic workspace operation.</p>
 */
public class WSDLCopier implements IWorkspaceRunnable {

	private static final String XSD = ".xsd";
	
	/**
	 * Helper class holding information about the files we need to copy
	 */
	private class XMLObjectInfo {
	  	
		public IPath  path;
	  	public Object content;
	  	  	
	  	/**
	  	 * Constructor.
	  	 * 
	  	 * @param path The path of the source file.
	  	 * @param content A representation of the source file (either wsdl or xsd)
	  	 */
	  	public XMLObjectInfo(IPath path, Object content) {
	  		this.path    = path.setDevice(null); // we want a device independent path  		
	  		this.content = content;
	  	}
	}	  
	
	private WebServicesParser parser	= null;					// parser to parse the wsdl and xsd files
	
	private String sourceURI			= "";					// uri of the starting wsdl
	private Definition definition		= null;					// representation of the starting wsdl
	
	private String targetFolderURI		= null;					// uri of the folder to copy to
	private String targetFilename		= null;					// optional new filename for the starting wsdl
	
	private IPath pathPrefix			= null;  				// the shortest common path of all files we need to copy
	private IPath wsdlRelPath			= null;					// relative path of the starting wsdl to the target folder
		
	private Map xmlObjectInfos			= new LinkedHashMap();	// table to store info about the files we need to copy
	
	private SimpleURIFactory uriFactory = null;
	
	/**
	 * Default constructor.  A new WebServicesParser is constructed for use.
	 */
	public WSDLCopier() {
		this(null);
	}
	
	/**
	 * Constructor.  The given WebServicesParser will be used.
	 * 
	 * @param parser A WebServicesParser for WSDLCopier to use.  If null, a new one will be constructed.
	 */
	public WSDLCopier(WebServicesParser parser) {
		if (parser == null)
			this.parser = new WebServicesParser();
		else
			this.parser = parser;
		
		IURIScheme eclipseScheme = EnvironmentService.getEclipseScheme();
	    IURIScheme fileScheme    = EnvironmentService.getFileScheme();
	    uriFactory = new SimpleURIFactory();
	    uriFactory.registerScheme("platform", eclipseScheme);
	    uriFactory.registerScheme("file", fileScheme);
	}

	/**
	 * Sets the URI of the starting wsdl document.  The URI must be an absolute URI
	 * with a valid protocol.  For local files, both file:/ and platform:/resource 
	 * protocol are acceptable. 
	 * 
	 * @param uri The URI of the starting wsdl document.
	 */
	public void setSourceURI(String uri) {
		setSourceURI(uri, null);
	}
	
	/**
	 * Same as setSourceURI(String uri) version, except that an already parsed
	 * representation of the wsdl document can be passed to this method to avoid
	 * having to parse it a second time.
	 * 
	 * @param uri The URI of the starting wsdl document.
	 * @param definition A parsed representation of the starting wsdl document.
	 */
	public void setSourceURI(String uri, Definition definition) {
		if (uri != null)
			uri = uri.replace('\\', '/');
		sourceURI = uri;
		this.definition = definition;
	}
	
	/**
	 * Specify the target folder URI for WSDLCopier to copy the wsdl to.  The URI must be
	 * an absolute URI with a valid protocol.  Both file:/ and platform:/resource protocol
	 * are acceptable.  The entire directory structure with all the files that the wsdl 
	 * imports will be duplicated under this folder.  If the target folder does not exist, 
	 * it will be created. 
	 * 
	 * @param uri The target folder URI where the wsdl structure is copied to.
	 */
	public void setTargetFolderURI(String uri) {
		if (uri != null)
			uri = uri.replace('\\', '/');
		targetFolderURI = uri;
	}
	
	/**
	 * Optionally set a new filename for the starting wsdl document to use after it is
	 * copied.  If not specified, the original name of the wsdl is used.
	 * 
	 * @param filename A filename.
	 */
	public void setTargetFilename(String filename) {
		targetFilename = filename;
	} 
	
	/*
	 * Compare the path prefix with the path in the info object,
	 * modify the path prefix accordingly.
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
	
	/*
	 * Determine if the given URI is a relative URI. 
	 */
	private boolean isRelative(String uri) {
		return uri.indexOf(':') == -1;
	}

	/*
	 * Analyze the wsdl document at the given URI, and traverse any relative files that
	 * it imports. Can optionally pass in a parsed Definition if one's available so
	 * we don't have to parse the wsdl again (otherwise just pass in null). 
	 */
	private void analyzeWSDL(URI uri, Definition definition) throws 
		MalformedURLException, IOException, WSDLException, WWWAuthenticationException {
		
		uri = uri.normalize();
		
		// already seen this wsdl, skip
		if (xmlObjectInfos.containsKey(uri.toString()))
			return;
				
		// need to parse the wsdl ourselves
		if (definition == null)
			definition = parser.getWSDLDefinitionVerbose(uri.toString());
		
		// save a reference to the starting wsdl
		if (this.definition == null)
			this.definition = definition;
		
		IPath path = new Path(uri.getPath());
		
		// a target filename was given, so we need to modify the path with the new name
		if (definition == this.definition && targetFilename != null)
			path = path.removeLastSegments(1).append(targetFilename);
		 		
	    XMLObjectInfo info = new XMLObjectInfo(path, definition);	    
	    xmlObjectInfos.put(uri.toString(), info);
	    updatePathPrefix(info);
		
	    // now look at wsdl imports
	    
	    for (Iterator it = definition.getImports().values().iterator(); it.hasNext();) {

	    	List list = (List) it.next();
	      
	    	for (Iterator listIt = list.iterator(); listIt.hasNext();) {
	    		
		        Import wsdlImport = (Import) listIt.next();
		        String wsdlImportLocation = wsdlImport.getLocationURI();

		        // analyze any relative imports we find
		        if (wsdlImportLocation != null && isRelative(wsdlImportLocation)) {

		        	// bad form, importing xsd with wsdl:import, but need to handle
		        	if (wsdlImportLocation.endsWith(XSD))
		        		analyzeXSD(uri.resolve(wsdlImportLocation));
		        	else	
		        		analyzeWSDL(uri.resolve(wsdlImportLocation), null);
		        }
	    	}
	    }
	    
	    // now look at xsd imports

	    Types types = definition.getTypes();
	    
	    // there's no wsdl:types, we're done
	    if (types == null)
	    	return;
	        	
    	for (Iterator it = types.getExtensibilityElements().iterator(); it.hasNext();) {			
			
    		ExtensibilityElement extElement = (ExtensibilityElement) it.next();    		
    		Element element;
    		
    		// we'll try to parse any UnknownExtensibilityElements and
    		// XSDSchemaExtensibilityElements into an XSD schema    		
    		
    		if (extElement instanceof UnknownExtensibilityElement)
    			element = ((UnknownExtensibilityElement) extElement).getElement();
   			else if (extElement instanceof XSDSchemaExtensibilityElement)
   				element = ((XSDSchemaExtensibilityElement) extElement).getElement();
   			else
   				continue;
    			    			
			try {
				XSDSchema xsdSchema = XSDSchemaImpl.createSchema(element);
				
				// analyze the inlined schema at the current uri
				analyzeXSD(uri, xsdSchema);
			}
			catch (Exception t) {
				// ignore any extensibility elements that cannot be parsed into a
				// XSDSchema instance
			}
        }
	}
	
	/*
	 * Analyze the schema at the given URI, traverse its imports and includes.
	 * The schema information is not stored in XMLObjectInfos because it's
	 * either not neccessary (schema inlined in a wsdl) or it has already
	 * been stored.
	 */
	private void analyzeXSD(URI uri, XSDSchema schema) {
		
		if (schema == null)
			return;
		
		// look at the imports and includes
	    
		for (Iterator it = schema.getContents().iterator(); it.hasNext();) {
	        
			Object content = it.next();
	        
			if (!(content instanceof XSDSchemaDirective))
				continue;
					        
			XSDSchemaDirective xsdSchemaDirective = (XSDSchemaDirective) content;
			String xsdSchemaDirectiveLocation = xsdSchemaDirective.getSchemaLocation();
	          
			// analyze any relative imports and includes we find
			if (xsdSchemaDirectiveLocation != null && isRelative(xsdSchemaDirectiveLocation))
				analyzeXSD(uri.resolve(xsdSchemaDirectiveLocation));
		}
	}
	
	/*
	 * Analyze the schema at the given URI, the schema is parsed and stored in
	 * XMLObjectInfos.
	 */
	private void analyzeXSD(URI uri) {
		
		uri = uri.normalize();
		
		// already seen this xsd, skip it
		if (xmlObjectInfos.containsKey(uri.toString()))
			return;
		
		XSDSchema xsdSchema = XSDSchemaImpl.getSchemaForSchema(uri.toString());

		// if schema is not cached, parse it
	    if (xsdSchema == null) {
	    	XSDParser p = new XSDParser(null);
	    	InputStream is = NetUtils.getURLInputStream(uri.toString());
	      
	    	if (is != null) {
	    		p.parse(is);
	    		xsdSchema = p.getSchema();
	    	}
	    }
	    
	    if (xsdSchema != null) {
	        
	    	XMLObjectInfo info = new XMLObjectInfo(new Path(uri.getPath()), xsdSchema);	    	
	    	xmlObjectInfos.put(uri.toString(), info);
	    	updatePathPrefix(info);
	      
	    	// analyze its imports and includes
	    	analyzeXSD(uri, xsdSchema);
	    }
	}
		
	/*
	 * Appends a path to a URI and returns the new combined URI.
	 */
	private String appendPathToURI(String uri, IPath path) {
		if (uri.endsWith("/"))
			return uri + path.makeRelative();
		else
			return uri + path.makeAbsolute();
	}
	
	/*
	 * Writes the wsdl definition to the file at the given path, relative to the target folder.
	 */
	private String writeXMLObj(IPath path, Definition definition, IProgressMonitor monitor) throws
		WSDLException, URIException, IOException, CoreException {				
			  
	    WSDLFactory wsdlFactory = new WSDLFactoryImpl();	    
	    WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
	    
	    String targetURI = appendPathToURI(targetFolderURI, path);
	    
	    OutputStream os = uriFactory.newURI(targetURI).getOutputStream();	    
	    wsdlWriter.writeWSDL(definition, os);
	    os.close();
	    
	    return targetURI;
	}
  
	/*
	 * Writes the xsd schema to the file at the given path, relative to the target folder.
	 */
	private String writeXMLObj(IPath path, XSDSchema xsdSchema, IProgressMonitor monitor) throws
		TransformerConfigurationException, TransformerException, URIException, IOException, CoreException {			
		
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		Element e = xsdSchema.getElement();
		DOMSource domSource = new DOMSource(e);		
		
		String targetURI = appendPathToURI(targetFolderURI, path);
		
		OutputStream os = uriFactory.newURI(targetURI).getOutputStream();
		serializer.transform(domSource, new StreamResult(os));
		os.close();
		
		return targetURI;
	}  

	/*
	 * Compares the 2 uris and see if they point to the same file. 
	 * We need to convert both uris to filesystem uris in order to compare.
	 */	
	private boolean isSameLocation(String uri1, String uri2) {

		// if either uri is null, we cannot make any meaningful comparison
		if (uri1 == null || uri2 == null)
			return false;
		
		uri1 = UniversalPathTransformer.toLocation(uri1);
		uri2 = UniversalPathTransformer.toLocation(uri2);
		
		return uri1.equals(uri2);
	}
	
	/**
	 * Executes the copying action.
	 * 
	 * @param monitor An optional progress monitor.
	 * @throws CoreException Thrown if the copying is unsuccessful.  Possible causes include:
	 * target folder URI was not specified; source URI has incorrect format; problem parsing wsdl 
	 * or xsd documents; problem writing to filesystem.
	 */
	public void run(IProgressMonitor monitor) throws CoreException {
		
		xmlObjectInfos.clear();

		// target folder must be set
		if (targetFolderURI == null)
			throw new CoreException(StatusUtils.errorStatus(WstWSPluginMessages.MSG_ERROR_TARGET_FOLDER_NOT_SPECIFIED));		
				
  		try {
  			URI uri;
  			if (URIHelper.isProtocolFile(sourceURI) || URIHelper.isPlatformResourceProtocol(sourceURI)) 
  				uri = new URI(URIEncoder.encode(sourceURI, "UTF-8"));  			
  			else 
  				uri = new URI(sourceURI);
  			
  			analyzeWSDL(uri, definition);  			 
  			
  			// begin writing out files
  			
  			Iterator iter = xmlObjectInfos.values().iterator(); 			  			
  			
  			while (iter.hasNext()) {
  				XMLObjectInfo info = (XMLObjectInfo) iter.next();  				
  				IPath relPath = info.path.removeFirstSegments(pathPrefix.segmentCount());
  				
  				if (info.content instanceof Definition) {
  					Definition definition = (Definition) info.content;
  					
  					// if this is the starting wsdl, remember its path relative to the target folder
  					if (definition == this.definition)
  						wsdlRelPath = relPath;

  					String targetURI = writeXMLObj(relPath, definition, monitor);
  					
  					if (definition == this.definition && isSameLocation(uri.toString(), targetURI))
  						return;
  				}
  				else
  					writeXMLObj(relPath, (XSDSchema) info.content, monitor);  					
  			}  			
  		} catch (Exception t) {
  			throw new CoreException(StatusUtils.errorStatus(
  					NLS.bind(WstWSPluginMessages.MSG_ERROR_COPY_WSDL, new String[] {sourceURI, targetFolderURI}), t));
  		}
	}
	
	/**
	 * Returns the path of the starting wsdl document, relative to the target folder after
	 * copying completes.
	 * 
	 * @return The relative path of the starting wsdl document.
	 */
	public IPath getRelativePath() {
		return wsdlRelPath;
	}
}

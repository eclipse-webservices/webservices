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
 * 20060717   146707 mahutch@ca.ibm.com - Mark Hutchinson
 * 20070124   167487 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.common.uriresolver.internal.util.URIEncoder;
import org.eclipse.wst.ws.internal.common.HTTPUtility;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;
import org.eclipse.wst.ws.internal.parser.discovery.NetUtils;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl;
import org.eclipse.xsd.XSDDiagnostic;
import org.eclipse.xsd.XSDDiagnosticSeverity;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.eclipse.xsd.util.XSDParser;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

public class WSDLElement extends WSDLCommonElement
{

  private String wsdlUrl_;
  private Definition definition_;
  private Vector schemaList_;
  private Vector schemaURI_;

  private static Vector w3SchemaQNameList_;
  private static Vector constantSchemaList_;
  
  //A ResourceSet for all schemas in this WSDL
  private ResourceSet resourceSet;

  static
  {
    // w3 schema URI list:
    // http://www.w3.org/2001/XMLSchema
    // http://www.w3.org/2000/10/XMLSchema
    // http://www.w3.org/1999/XMLSchema
    w3SchemaQNameList_ = new Vector();
    w3SchemaQNameList_.addElement(new QName(FragmentConstants.NS_URI_CURRENT_SCHEMA_XSD,"schema"));
    w3SchemaQNameList_.addElement(new QName(FragmentConstants.NS_URI_2000_SCHEMA_XSD,"schema"));
    w3SchemaQNameList_.addElement(new QName(FragmentConstants.NS_URI_1999_SCHEMA_XSD,"schema"));

    // Constant schema URI list:
    // http://www.w3.org/2001/XMLSchema
    // http://www.w3.org/2000/10/XMLSchema
    // http://www.w3.org/1999/XMLSchema
    // http://schemas.xmlsoap.org/soap/encoding/
    // http://schemas.xmlsoap.org/wsdl/
    constantSchemaList_ = new Vector();
    constantSchemaList_.addElement(XSDSchemaImpl.getSchemaForSchema(FragmentConstants.NS_URI_CURRENT_SCHEMA_XSD));
    constantSchemaList_.addElement(XSDSchemaImpl.getSchemaForSchema(FragmentConstants.NS_URI_2000_SCHEMA_XSD));
    constantSchemaList_.addElement(XSDSchemaImpl.getSchemaForSchema(FragmentConstants.NS_URI_1999_SCHEMA_XSD));
    constantSchemaList_.addElement(XSDSchemaImpl.getSchemaForSchema(FragmentConstants.NS_URI_SOAP_ENC));
    constantSchemaList_.addElement(XSDSchemaImpl.getSchemaForSchema(FragmentConstants.URI_WSDL));
  }

  public WSDLElement(String name, Model model, String wsdlUrl)
  {
    super(name, model);
    setWsdlUrl(wsdlUrl);
    definition_ = null;
    schemaList_ = new Vector();
	schemaURI_ = new Vector();
  }

  public void setWsdlUrl(String wsdlUrl) {
	  HTTPUtility http = new HTTPUtility();
	  wsdlUrl_ = http.handleRedirect(wsdlUrl);
  }

  public String getWsdlUrl() {
    return wsdlUrl_;
  }

  public void setSchemaList(Vector schemaList)
  {
    schemaList_ = schemaList;
  }

  public Vector getSchemaList()
  {
    return schemaList_;
  }
  
  private final Definition loadWSDL(String wsdlURL) throws WSDLException
  {
    WebServicesParser parser = new WebServicesParserExt();
    try
    {
      return parser.getWSDLDefinitionVerbose(wsdlURL);
    }
    catch (WSDLException wsdle)
    {
      throw wsdle;
    }
    catch (Throwable t)
    {
      throw new WSDLException(WSDLException.OTHER_ERROR , t.getMessage(), t);
    }
  }

  public Vector loadWSDL() throws WSDLException
  {
	  
	//	Register the appropriate resource factory to handle all file extentions.
	resourceSet = new ResourceSetImpl();
	resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION,new XSDResourceFactoryImpl());    		
	//Register the package to ensure it is available during loading.
	resourceSet.getPackageRegistry().put(XSDPackage.eNS_URI,XSDPackage.eINSTANCE);	
	  
	  
    Vector errorMessages = new Vector();
    definition_ = loadWSDL(wsdlUrl_);
    
    
    if (definition_ != null)
    {
      setDocumentation(definition_.getDocumentationElement());
      gatherSchemas(definition_, wsdlUrl_);
      // Validate the schemas.
      for (int i=0;i<schemaList_.size();i++)
      {
        XSDSchema xsdSchema = (XSDSchema)schemaList_.elementAt(i);
        xsdSchema.validate();
        EList errors = xsdSchema.getAllDiagnostics();
        if (!errors.isEmpty())
        {
          for (ListIterator li = errors.listIterator();li.hasNext();)
          {
            XSDDiagnostic xd = (XSDDiagnostic)li.next();
            String msg = xd.getMessage();
            // do not report low severity diagnostics or erroneous array reference errors.
            if (xd.getSeverity().getValue() == XSDDiagnosticSeverity.FATAL_LITERAL.getValue() || (msg != null && msg.length() > 0 && msg.toLowerCase().indexOf("#array") != -1))
              li.remove();
            else
            {
              if (msg != null && msg.length() > 0)
                errorMessages.addElement(xd.getMessage());
            }
          }
        }
      }

      for (int i=0;i<constantSchemaList_.size();i++)
        schemaList_.addElement(constantSchemaList_.elementAt(i));
      
	}
    return errorMessages;
  }

  private final void gatherSchemas(Definition definition, String definitionURL)
  {
    Types types = definition.getTypes();
    if (types != null)
    {
      List extTypes = types.getExtensibilityElements();
      if (extTypes != null)
      {
        for (int i=0;i<extTypes.size();i++)
        {
          XSDSchema xsdSchema = null;
          Object obj = extTypes.get(i);
          if (obj instanceof UnknownExtensibilityElement)
          {
            UnknownExtensibilityElement schemaElement = (UnknownExtensibilityElement)obj;
            if (isW3SchemaElementType(schemaElement.getElementType()))
            {
              xsdSchema = XSDSchemaImpl.createSchema(schemaElement.getElement());
			  if(!checkSchemaURI(definitionURL)){
				  schemaList_.addElement(xsdSchema);
				  gatherSchemaDirective(xsdSchema, definitionURL);
              }
          	}
          } 	
          else if (obj instanceof XSDSchemaExtensibilityElementImpl)
          {
            XSDSchemaExtensibilityElementImpl schemaElement = (XSDSchemaExtensibilityElementImpl)obj;
            xsdSchema = XSDSchemaImpl.createSchema(schemaElement.getElement());
			if(!checkSchemaURI(definitionURL)){
				schemaList_.addElement(xsdSchema);
				gatherSchemaDirective(xsdSchema, definitionURL);
			}
		  }
          
          if (xsdSchema != null)
          {
        	  //We need to add the schema to a Resource in a Resource set for proper validation
        	  org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createURI(definitionURL);    		
        	  Resource resource = resourceSet.createResource(uri);
        	  //Add the Schema to the resource
        	  resource.getContents().add(xsdSchema);
          }
        }
      }
    }
    Map imports = definition.getImports();
    if (imports != null)
      gatherImportedSchemas(definition,imports);
  }

  private final void gatherSchemaDirective(XSDSchema xsdSchema, String xsdSchemaURL)
  {
    if (xsdSchema != null)
    {
      EList xsdSchemaContents = xsdSchema.getContents();
      for (Iterator it = xsdSchemaContents.iterator(); it.hasNext();)
      {
        Object content = it.next();
        if (content instanceof XSDSchemaDirective)
        {
          XSDSchemaDirective xsdSchemaDirective = (XSDSchemaDirective)content;
          StringBuffer xsdSchemaDirectiveURL = new StringBuffer();
          String xsdSchemaDirectiveLocation = xsdSchemaDirective.getSchemaLocation();
          if (xsdSchemaDirectiveLocation != null && xsdSchemaDirectiveLocation.indexOf(':') == -1 && xsdSchemaURL != null && xsdSchemaURL.indexOf(':') != -1)
          {
            // relative URL
            int index = xsdSchemaURL.lastIndexOf('/');
            if (index != -1)
              xsdSchemaDirectiveURL.append(xsdSchemaURL.substring(0, index+1));
            else
            {
              xsdSchemaDirectiveURL.append(xsdSchemaURL);
              xsdSchemaDirectiveURL.append('/');
            }
          }
          xsdSchemaDirectiveURL.append(xsdSchemaDirectiveLocation);
          // resolve schema directive
          XSDSchema resolvedSchema = xsdSchemaDirective.getResolvedSchema();
          if (resolvedSchema == null && xsdSchemaDirectiveURL.length() > 0)
            resolvedSchema = getSchema(xsdSchemaDirectiveURL.toString());
          if (resolvedSchema != null)
          {
			if(!checkSchemaURI(xsdSchemaDirectiveURL.toString())){
				schemaList_.addElement(resolvedSchema);
				gatherSchemaDirective(resolvedSchema, xsdSchemaDirectiveURL.toString());
      		}
      	  }
        }
	  }
    }
  }

  private boolean checkSchemaURI(String schemaURI){
	  	boolean found = false;
	  	 	
		
		schemaURI = normalize(schemaURI); 
		if(schemaURI.equals(normalize(wsdlUrl_)))return false;
		Enumeration e = schemaURI_.elements();
		while(e.hasMoreElements()){
			String uri = (String)e.nextElement();	
			if(schemaURI.equals(uri)){ 
				found = true;
				break;
			}	
		}
		
	    if (!found){
	    	schemaURI_.addElement(schemaURI);
	    }
	    return found;
	  
	  }
	  
  private String normalize(String uri )
  {
	  try {
		  String encodedURI = URIEncoder.encode(uri,"UTF-8");
		  URI normalizedURI = new URI(encodedURI);
		  normalizedURI = normalizedURI.normalize();
		  return normalizedURI.toString();
	  } catch (URISyntaxException e) {
		  return uri;
	  } catch (UnsupportedEncodingException e) {
		  return uri;
	  }
  }
  
  private final boolean isW3SchemaElementType(QName qname)
  {
    for (int i=0;i<w3SchemaQNameList_.size();i++)
    {
      QName w3SchemaQName = (QName)w3SchemaQNameList_.elementAt(i);
      if (w3SchemaQName.equals(qname))
        return true;
    }
    return false;
  }

  private final void gatherImportedSchemas(Definition definition,Map imports)
  {
    for (Iterator iterator = imports.keySet().iterator();iterator.hasNext();)
    {
      List importList = (List)imports.get(iterator.next());
      for (int i=0;i<importList.size();i++)
      {
        Import imp = (Import)importList.get(i);
        StringBuffer locURI = new StringBuffer(imp.getLocationURI());
        if (!Validator.validateURL(locURI.toString()))
        {
          String base = definition.getDocumentBaseURI();
          locURI.insert(0,base.substring(0,base.lastIndexOf('/')+1));
        }
        try
        {
          Definition importDef = loadWSDL(locURI.toString());
          gatherSchemas(importDef, locURI.toString());
        }
        catch (WSDLException e)
        {
          // May be an XSD file.
          gatherSchema(locURI.toString());
        }
      }
    }
  }

  private final void gatherSchema(String locURI)
  {
    XSDSchema xsdSchema = getSchema(locURI);
    if (xsdSchema != null)
    {
      schemaList_.addElement(xsdSchema);
      gatherSchemaDirective(xsdSchema, locURI);
    }
  }

  private final XSDSchema getSchema(String locURI)
  {
    XSDSchema xsdSchema = XSDSchemaImpl.getSchemaForSchema(locURI);
    if (xsdSchema == null)
    {
      XSDParser p = new XSDParser();
      InputStream is = NetUtils.getURLInputStream(locURI);
      if (is != null)
      {
        p.parse(is);
        xsdSchema = p.getSchema();
      }
    }
    return xsdSchema;
  }

  public void setDefinition(Definition definition)
  {
    definition_ = definition;
  }

  public Definition getDefinition()
  {
    return definition_;
  }

  public void buildModel() {
    if (definition_ != null) {
      Map services = new HashMap(definition_.getServices());
      WSDLServiceElement[] wsdlServiceElements = new WSDLServiceElement[getNumberOfElements(WSDLModelConstants.REL_WSDL_SERVICE)];
      Enumeration e = getElements(WSDLModelConstants.REL_WSDL_SERVICE);
      for (int i = 0; i < wsdlServiceElements.length; i++) {
        wsdlServiceElements[i] = (WSDLServiceElement)e.nextElement();
      }
      for (int j = 0; j < wsdlServiceElements.length; j++) {
        QName qname = wsdlServiceElements[j].getService().getQName();
        Service service = (Service)services.get(qname);
        if (service != null) {
          services.remove(qname);
          wsdlServiceElements[j].setService(service);
          wsdlServiceElements[j].buildModel();
        }
        else
          disconnect(wsdlServiceElements[j], WSDLModelConstants.REL_WSDL_SERVICE);
      }
      for (Iterator it = services.values().iterator();it.hasNext();) {
        Service service = (Service)it.next();
        WSDLServiceElement wsdlServiceElement = new WSDLServiceElement(service.getQName().getLocalPart(), getModel(), service);
        connect(wsdlServiceElement,WSDLModelConstants.REL_WSDL_SERVICE,ModelConstants.REL_OWNER);
        wsdlServiceElement.buildModel();
      }
    }
  }
}

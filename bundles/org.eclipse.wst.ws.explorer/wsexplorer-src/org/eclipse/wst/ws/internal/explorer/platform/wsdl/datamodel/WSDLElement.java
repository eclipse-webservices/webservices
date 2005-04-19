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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel;

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*;
import org.eclipse.wst.ws.internal.parser.discovery.*;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl;

import org.eclipse.xsd.util.*;
import org.eclipse.xsd.impl.*;
import org.eclipse.xsd.*;
import org.eclipse.emf.common.util.EList;

import javax.xml.namespace.QName;
import javax.wsdl.extensions.*;
import javax.wsdl.*;
import java.util.*;
import java.io.*;

public class WSDLElement extends WSDLCommonElement
{

  private String wsdlUrl_;
  private Definition definition_;
  private Vector schemaList_;

  private static Vector w3SchemaQNameList_;
  private static Vector constantSchemaList_;

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
    wsdlUrl_ = wsdlUrl;
    definition_ = null;
    schemaList_ = new Vector();
  }

  public void setWsdlUrl(String wsdlUrl) {
    wsdlUrl_ = wsdlUrl;
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
          Object obj = extTypes.get(i);
          if (obj instanceof UnknownExtensibilityElement)
          {
            UnknownExtensibilityElement schemaElement = (UnknownExtensibilityElement)obj;
            if (isW3SchemaElementType(schemaElement.getElementType()))
            {
              XSDSchema xsdSchema = XSDSchemaImpl.createSchema(schemaElement.getElement());
              schemaList_.addElement(xsdSchema);
              gatherSchemaDirective(xsdSchema, definitionURL);
            }
          }
          else if (obj instanceof XSDSchemaExtensibilityElementImpl)
          {
            XSDSchemaExtensibilityElementImpl schemaElement = (XSDSchemaExtensibilityElementImpl)obj;
            XSDSchema xsdSchema = XSDSchemaImpl.createSchema(schemaElement.getElement());
            schemaList_.addElement(xsdSchema);
            gatherSchemaDirective(xsdSchema, definitionURL);
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
            schemaList_.addElement(resolvedSchema);
            gatherSchemaDirective(resolvedSchema, xsdSchemaDirectiveURL.toString());
          }
        }
      }
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

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
package org.eclipse.wst.ws.internal.explorer.platform.uddi.util;

import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import java.util.Iterator;
import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.Port;
import javax.wsdl.Binding;
import javax.wsdl.PortType;
import javax.wsdl.Message;
import javax.wsdl.Import;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.http.HTTPAddress;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.binding.AccessPoint;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.binding.BindingTemplates;
import org.uddi4j.datatype.binding.InstanceDetails;
import org.uddi4j.datatype.binding.TModelInstanceDetails;
import org.uddi4j.datatype.binding.TModelInstanceInfo;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.datatype.OverviewDoc;
import org.uddi4j.datatype.OverviewURL;
import org.uddi4j.response.TModelDetail;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;

import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;

public class Uddi4jHelper
{
  public final KeyedReference getWSDLKeyedReference()
  {
    KeyedReference keyRef = new KeyedReference("types", "wsdlSpec");
    keyRef.setTModelKey("UUID:C1ACF26D-9672-4404-9D70-39B756E62AB4");
    return keyRef;
  }

  public final TModel newTModel(String wsdlUrl) throws WSDLException
  {
    return newTModel(wsdlUrl, getWSDLDefinition(wsdlUrl));
  }

  public final TModel newTModel(String wsdlUrl, Definition def) throws WSDLException
  {
    TModel tModel = new TModel();
    tModel.setTModelKey("");
    tModel.setName(def.getTargetNamespace());
    tModel.setDefaultDescriptionString("");
    OverviewDoc overviewDoc = new OverviewDoc();
    overviewDoc.setOverviewURL(wsdlUrl);
    tModel.setOverviewDoc(overviewDoc);
    CategoryBag catBag = new CategoryBag();
    Vector keyRefVector = new Vector();
    keyRefVector.add(getWSDLKeyedReference());
    catBag.setKeyedReferenceVector(keyRefVector);
    tModel.setCategoryBag(catBag);
    return tModel;
  }

  public final BusinessService newBusinessService(String wsdlUrl, Hashtable tModelsTable) throws WSDLException
  {
    return newBusinessService(wsdlUrl, getWSDLDefinition(wsdlUrl), tModelsTable);
  }

  public final BusinessService newBusinessService(String wsdlUrl, Definition def, Hashtable tModelsTable) throws WSDLException
  {
    BusinessService bs = new BusinessService();
    bs.setServiceKey("");
    BindingTemplates bindingTemplates = new BindingTemplates();
    Vector bindingTemplateVector = new Vector();
    Service[] services = (Service[])def.getServices().values().toArray(new Service[0]);
    if (services.length > 0)
    {
      Port[] ports = (Port[])services[0].getPorts().values().toArray(new Port[0]);
      for (int i = 0; i < ports.length; i++)
      {
        TModelInstanceDetails tModelDetails = new TModelInstanceDetails();
        Vector tModelInfoVector = new Vector();
        TModel tModel = (TModel)tModelsTable.get(ports[i].getBinding().getPortType().getQName().getNamespaceURI());
        tModelInfoVector.add(createTModelInstanceInfo(wsdlUrl, ports[i], tModel.getTModelKey()));
        tModelDetails.setTModelInstanceInfoVector(tModelInfoVector);
        BindingTemplate bindingTemplate = new BindingTemplate();
        bindingTemplate.setDefaultDescriptionString(createDescription(ports[i]));
        bindingTemplate.setAccessPoint(createAccessPoint(ports[i]));
        bindingTemplate.setTModelInstanceDetails(tModelDetails);
        bindingTemplate.setBindingKey("");
        bindingTemplateVector.add(bindingTemplate);
      }
    }
    bindingTemplates.setBindingTemplateVector(bindingTemplateVector);
    bs.setBindingTemplates(bindingTemplates);
    return bs;
  }
  
  public final BusinessService newBusinessService(String wsdlUrl, Definition def, TModel[] tModels) throws WSDLException
  {
    BusinessService bs = new BusinessService();
    bs.setServiceKey("");
    BindingTemplates bindingTemplates = new BindingTemplates();
    Vector bindingTemplateVector = new Vector();
    Service[] services = (Service[])def.getServices().values().toArray(new Service[0]);
    if (services.length > 0)
    {
      Port[] ports = (Port[])services[0].getPorts().values().toArray(new Port[0]);
      for (int i = 0; i < ports.length; i++)
      {
        for (int j = 0; j < tModels.length; j++)
        {
          TModelInstanceDetails tModelDetails = new TModelInstanceDetails();
          Vector tModelInfoVector = new Vector();
          tModelInfoVector.add(createTModelInstanceInfo(wsdlUrl, ports[i], tModels[j].getTModelKey()));
          tModelDetails.setTModelInstanceInfoVector(tModelInfoVector);
          BindingTemplate bindingTemplate = new BindingTemplate();
          bindingTemplate.setDefaultDescriptionString(createDescription(ports[i]));
          bindingTemplate.setAccessPoint(createAccessPoint(ports[i]));
          bindingTemplate.setTModelInstanceDetails(tModelDetails);
          bindingTemplate.setBindingKey("");
          bindingTemplateVector.add(bindingTemplate);
        }
      }
    }
    bindingTemplates.setBindingTemplateVector(bindingTemplateVector);
    bs.setBindingTemplates(bindingTemplates);
    return bs;
  }

  public Definition getWSDLDefinition(String wsdlUrl) throws WSDLException
  {
    try
    {
      WebServicesParserExt parser = new WebServicesParserExt();
      String proxySet = System.getProperty("http.proxySet");
      if (proxySet != null && proxySet.equals("true"))
      {
        parser.setHTTPBasicAuthUsername(System.getProperty("http.proxyUserName"));
        parser.setHTTPBasicAuthPassword(System.getProperty("http.proxyPassword"));
      }
      return parser.getWSDLDefinitionVerbose(wsdlUrl);
    }
    catch (Exception e)
    {
      throw new WSDLException(WSDLException.PARSER_ERROR, e.getMessage(), e);
    }
  }

  private final String createDescription(Port port)
  {
    String desc = null;
    Element e = port.getDocumentationElement();
    if (e != null)
    {
      Node node = e.getFirstChild();
      StringBuffer sb = new StringBuffer();
      while (node != null)
      {
        switch (node.getNodeType())
        {
          case Node.TEXT_NODE:
          case Node.CDATA_SECTION_NODE:
            sb.append(((CharacterData)node).getData());
          default:
            node = node.getNextSibling();
            break;
        }
      }
      desc = sb.toString();
    }
    return desc;
  }
  
  private final TModelInstanceInfo createTModelInstanceInfo(String wsdlUrl, Port port, String tModelKey)
  {
    InstanceDetails details = new InstanceDetails();
    OverviewDoc overviewDoc = new OverviewDoc();
    TModelInstanceInfo tModelInfo = new TModelInstanceInfo();
    StringBuffer overviewURLString = new StringBuffer(wsdlUrl);
    String portName = port.getName();
    if (portName != null)
    {  
      overviewURLString.append("#");
      overviewURLString.append(portName);
    }
    overviewDoc.setOverviewURL(new OverviewURL(overviewURLString.toString()));
    details.setOverviewDoc(overviewDoc);
    tModelInfo.setInstanceDetails(details);
    if (tModelKey != null)
      tModelInfo.setTModelKey(tModelKey);
    return tModelInfo;
  }
  
  private final AccessPoint createAccessPoint(Port port)
  {
    AccessPoint accessPoint = null;
    List list = port.getExtensibilityElements();
    for (int j = 0; j < list.size(); j++)
     {
      ExtensibilityElement extElement = (ExtensibilityElement)list.get(j);
      if (extElement instanceof SOAPAddress)
       {  
        String locationURI = ((SOAPAddress)extElement).getLocationURI();
        accessPoint = new AccessPoint(locationURI, getURLType(locationURI));
        break;
      }
      else if (extElement instanceof HTTPAddress)
       {
        String locationURI = ((HTTPAddress)extElement).getLocationURI();
        accessPoint = new AccessPoint(locationURI, getURLType(locationURI));
        break;
      }
    }
    return accessPoint;
  }
  
  private final String URL_TYPE_OTHER = "other";
  private final String URL_TYPES = "http https mail ftp fax phone other";
  
  private final String getURLType(String url)
  {
    int i = url.indexOf(":");
    String type;
    if (i == -1)
      type = URL_TYPE_OTHER;
    else
    {
      type = url.substring(0, i);
      if (type.equals("") || URL_TYPES.indexOf(type) == -1)
        type = URL_TYPE_OTHER;
    }
    return type;
  }
  
  public boolean isMonolithicWSDL(Definition def)
  {
    return hasMessage(def) && hasPortType(def) && hasBinding(def) && hasService(def);
  }
  
  public boolean isServiceImplement(Definition def)
  {
    return hasImport(def) && hasService(def);
  }
  
  public boolean isServiceInterface(Definition def)
  {
    return hasMessage(def) && hasPortType(def) && hasBinding(def);
  }
  
  public boolean isServiceInterfaceWithBindingsOnly(Definition def)
  {
    return !hasService(def) && hasBinding(def);
  }

  private boolean hasMessage(Definition def)
  {
    for(Iterator it = def.getMessages().values().iterator(); it.hasNext();)
      if (!((Message)it.next()).isUndefined())
        return true;
    return false;
  }
  
  private boolean hasPortType(Definition def)
  {
    for(Iterator it = def.getPortTypes().values().iterator(); it.hasNext();)
      if (!((PortType)it.next()).isUndefined())
        return true;
    return false;
  }
  
  private boolean hasBinding(Definition def)
  {
    for(Iterator it = def.getBindings().values().iterator(); it.hasNext();)
      if (!((Binding)it.next()).isUndefined())
        return true;
    return false;
  }
  
  private boolean hasService(Definition def)
  {
    return !def.getServices().isEmpty();
  }

  private boolean hasImport(Definition def)
  {
    return !def.getImports().isEmpty();
  }
  
  public String[] getImports(Definition def, String wsdlUrl)
  {
    Import[] imports = (Import [])def.getImports().values().toArray(new Import[0]);
    String[] importStrings = new String[imports.length];
    String s;
    if (wsdlUrl != null && wsdlUrl.indexOf('/') != -1)
      s = wsdlUrl.substring(0, wsdlUrl.lastIndexOf('/')+1);
    else
      s = "";
    for (int i = 0; i < importStrings.length; i++)
    {
      StringBuffer sb = new StringBuffer();
      String locationURI = imports[i].getLocationURI();
      if (locationURI.indexOf(':') != -1)
        sb.append(locationURI);
      else
        sb.append(s).append(locationURI);
      importStrings[i] = sb.toString();
    }
    return importStrings;
  }

  public String getWSDL(BusinessService bs, UDDIProxy proxy)
  {
    Vector bindingTemplateVector = bs.getBindingTemplates().getBindingTemplateVector();
    if (bindingTemplateVector.size() > 0)
    {
      BindingTemplate bt = (BindingTemplate)bindingTemplateVector.get(0);
      TModelInstanceDetails tModelDetails = bt.getTModelInstanceDetails();
      if (tModelDetails != null)
      {
        Vector tModelInfoVector = tModelDetails.getTModelInstanceInfoVector();
        if (tModelInfoVector.size() > 0)
        {
          TModelInstanceInfo tModelInfo = (TModelInstanceInfo)tModelInfoVector.get(0);
          InstanceDetails details = tModelInfo.getInstanceDetails();
          if (details != null)
          {
            OverviewDoc overviewDoc = details.getOverviewDoc();
            if (overviewDoc != null)
              return parse(overviewDoc.getOverviewURLString());
          }
          else if (proxy != null)
          {
          	String tModelKey = tModelInfo.getTModelKey();
          	if (tModelKey != null)
          	{
          	  try
			  {
          	    TModelDetail t = proxy.get_tModelDetail(tModelKey);
          	    if (t != null)
          	    {
                  Vector tModelVector = t.getTModelVector();
                  if (tModelVector != null && tModelVector.size() > 0)
                    return getWSDL((TModel)tModelVector.get(0));
          	    }
              }
          	  catch (UDDIException uddie)
			  {
          	  }
          	  catch (TransportException te)
			  {
          	  }
          	}
          }
        }
      }
    }
    return null;
  }
  
  public String getWSDL(TModel tModel)
  {
    OverviewDoc overviewDoc = tModel.getOverviewDoc();
    if (overviewDoc != null)
      return parse(overviewDoc.getOverviewURLString());
    else
      return null;
  }
  
  private String parse(String s)
  {
    if (s != null && s.length() > 0)
    {
      int index = s.indexOf('#');
      if (index != -1)
        return s.substring(0, index);
    }
    return s;
  }
}
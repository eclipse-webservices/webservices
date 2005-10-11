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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import org.apache.wsil.Abstract;
import org.apache.wsil.Description;
import org.apache.wsil.Inspection;
import org.apache.wsil.Link;
import org.apache.wsil.Service;
import org.apache.wsil.WSILDocument;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListManager;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilModelConstants;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;

/**
 * The data model element that represents a WSIL document
 */
public class WsilElement extends TreeElement
{
  private WSILDocument wsilDoc_;
  private String thisWsilUrl_;
  /** ****************** Namespace ********** */
  private static final String NAMESPACE_WSDL = "http://schemas.xmlsoap.org/wsdl/";
  private static final String NAMESPACE_UDDI_V1 = "urn:uddi-org:api";
  private static final String NAMESPACE_UDDI_V2 = "urn:uddi-org:api_v2";
  //private static final String NAMESPACE_UDDI_BINDING = "http://schemas.xmlsoap.org/ws/2001/10/inspection/uddi/";
  private static final String NAMESPACE_WSIL_INSPECTION = "http://schemas.xmlsoap.org/ws/2001/10/inspection/";
  /** ****************** Rels **************** */
  // Relation for a WSIL doucment and its link nodes
  public static final String REL_WSIL_LINK = "wsil link nodes";

  public WsilElement(String name, Model model, WSILDocument wsilDocument, String wsilURL)
  {
    super(name, model);
    wsilDoc_ = wsilDocument;
    thisWsilUrl_ = wsilURL;
    ListManager wsdlServices_ = null;
    ListManager uddiServices_ = null;
    ListManager wsilLinks_ = null;
    ListManager uddiLinks_ = null;
    setPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSDL_SERVICES, wsdlServices_);
    setPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_SERVICES, uddiServices_);
    setPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_LINKS, wsilLinks_);
    setPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSIL_LINKS, uddiLinks_);
  }

  /*
   * Refresh the WSIL document
   */
  public boolean refresh()
  {
    try
    {
      WSILDocument newWSILDoc = WSILDocument.newInstance();
      newWSILDoc.read(getWsilUrl());
      wsilDoc_ = newWSILDoc;
      ListManager wsdlServices_ = null;
      ListManager uddiServices_ = null;
      ListManager wsilLinks_ = null;
      ListManager uddiLinks_ = null;
      setPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSDL_SERVICES, wsdlServices_);
      setPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_SERVICES, uddiServices_);
      setPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_LINKS, wsilLinks_);
      setPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSIL_LINKS, uddiLinks_);
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }

  /*
   * Returns the WSIL's URL
   */
  public String getWsilUrl()
  {
    return thisWsilUrl_;
  }

  /*
   * Returns the WSIL document
   */
  public WSILDocument getWSILDocument()
  {
    return wsilDoc_;
  }

  /*
   * Returns all abstract languages
   */
  public Vector getWSILAbstractLangs()
  {
    Vector v = new Vector();
    Abstract[] abstracts = wsilDoc_.getInspection().getAbstracts();
    for (int i = 0; i < abstracts.length; i++)
    {
      v.add(abstracts[i].getLang());
    }
    return v;
  }

  /*
   * Returns all abstracts
   */
  public Vector getWSILAbstracts()
  {
    Vector v = new Vector();
    Abstract[] abstracts = wsilDoc_.getInspection().getAbstracts();
    for (int i = 0; i < abstracts.length; i++)
    {
      v.add(abstracts[i].getText());
    }
    return v;
  }

  public ListManager getAllWSDLServices()
  {
    ListManager wsdlServices_ = (ListManager) getPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSDL_SERVICES);
    if (wsdlServices_ == null)
    {
      initAllWSDLServices();
      wsdlServices_ = (ListManager) getPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSDL_SERVICES);
    }
    return wsdlServices_;
  }

  public ListManager getAllUDDIServices()
  {
    ListManager uddiServices_ = (ListManager) getPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_SERVICES);
    if (uddiServices_ == null)
    {
      initAllUDDIServices();
      uddiServices_ = (ListManager) getPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_SERVICES);
    }
    return uddiServices_;
  }

  private void initAllWSDLServices()
  {
    ListManager wsdlServices_ = new ListManager();
    Inspection inspection = wsilDoc_.getInspection();
    Service[] serviceList = inspection.getServices();
    for (int i = 0; i < serviceList.length; i++)
    {
      // skip any service without a wsdl namespace
      // and invalid descriptions
      Description[] descList = serviceList[i].getDescriptions();
      if (descList.length < 1)
        continue;
      String namespace = descList[0].getReferencedNamespace();
      if (namespace == null)
        continue;
      else if (namespace.equals(NAMESPACE_WSDL))
      {
        WsilWsdlServiceElement wsdlServiceElement = new WsilWsdlServiceElement(namespace, getModel(), serviceList[i]);
        wsdlServiceElement.setBaseWsilURL(getWsilUrl());
        if (wsdlServiceElement.validateWSDLService())
        {
          wsdlServiceElement.setName(wsdlServiceElement.getWSDLServiceURL());
          wsdlServices_.add(new ListElement(wsdlServiceElement));
        }
      }
    }
    setPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSDL_SERVICES, wsdlServices_);
  }

  private void initAllUDDIServices()
  {
    ListManager uddiServices_ = new ListManager();
    Inspection inspection = wsilDoc_.getInspection();
    Service[] serviceList = inspection.getServices();
    Vector uddiServiceElements = new Vector();
    for (int i = 0; i < serviceList.length; i++)
    {
      // skip any service without a wsdl namespace
      // and invalid descriptions
      Description[] descList = serviceList[i].getDescriptions();
      if (descList.length < 1)
        continue;
      String namespace = descList[0].getReferencedNamespace();
      if (namespace == null)
        continue;
      else if ((namespace.equals(NAMESPACE_UDDI_V1) || namespace.equals(NAMESPACE_UDDI_V2)))
      {
        WsilUddiServiceElement uddiServiceElement = new WsilUddiServiceElement(namespace, getModel(), serviceList[i]);
        uddiServiceElement.setBaseWsilURL(getWsilUrl());
        if (uddiServiceElement.validateUDDIService())
        {
          uddiServiceElement.setName(uddiServiceElement.getName());
          uddiServiceElements.add(uddiServiceElement);
        }
      }
    }
    refreshServiceDefinitionsFromRegistry(uddiServiceElements);
    for (Iterator it = uddiServiceElements.iterator(); it.hasNext();)
    {
      uddiServices_.add(new ListElement(it.next()));
    }
    setPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_SERVICES, uddiServices_);
  }

  public void refreshServiceDefinitionsFromRegistry(Vector uddiServiceElements)
  {
    HashMap inquiryAPIToServices = new HashMap();
    for (int i = 0; i < uddiServiceElements.size(); i++)
    {
      WsilUddiServiceElement uddiServiceElement = (WsilUddiServiceElement) uddiServiceElements.get(i);
      String inquiryAPI = uddiServiceElement.getUDDIServiceInquiryAPI();
      if (Validator.validateURL(inquiryAPI))
      {
        Vector services = (Vector) inquiryAPIToServices.get(inquiryAPI);
        if (services == null)
        {
          services = new Vector();
          inquiryAPIToServices.put(inquiryAPI, services);
        }
        services.add(uddiServiceElement);
      }
      else
        uddiServiceElement.setServiceDefinition(null);
    }
    for (Iterator it = inquiryAPIToServices.keySet().iterator(); it.hasNext();)
    {
      String inquiryAPI = (String) it.next();
      Vector services = (Vector) inquiryAPIToServices.get(inquiryAPI);
      UDDIProxy proxy;
      try
      {
        Properties props = new Properties();
        props.put(ActionInputs.TRANSPORT_CLASS_NAME, ActionInputs.TRASPORT_CLASS);
        proxy = new UDDIProxy(props);
        proxy.setInquiryURL(new URL(inquiryAPI));
      }
      catch (Throwable t)
      {
        for (int j = 0; j < services.size(); j++)
        {
          WsilUddiServiceElement uddiServiceElement = (WsilUddiServiceElement) services.get(j);
          uddiServiceElement.setServiceDefinition(null);
        }
        continue;
      }
      Vector serviceKeys = new Vector();
      for (int j = 0; j < services.size(); j++)
      {
        serviceKeys.add(((WsilUddiServiceElement) services.get(j)).getUDDIServiceKey());
      }
      Vector businessServices;
      try
      {
        businessServices = proxy.get_serviceDetail(serviceKeys).getBusinessServiceVector();
      }
      catch (Exception ex)
      {
        businessServices = new Vector();
        if (serviceKeys.size() > 1)
        {
          for (int k = 0; k < serviceKeys.size(); k++)
          {
            try
            {
              businessServices.add((BusinessService) proxy.get_serviceDetail((String) serviceKeys.get(k)).getBusinessServiceVector().get(0));
            }
            catch (Exception exception)
            {
              businessServices.add(null);
            }
          }
        }
        else
          businessServices.add(null);
      }
      for (int k = 0; k < services.size(); k++)
      {
        BusinessService bs = (BusinessService) businessServices.get(k);
        WsilUddiServiceElement uddiServiceElement = (WsilUddiServiceElement) services.get(k);
        uddiServiceElement.setServiceDefinition(bs);
      }
    }
  }

  public ListManager getAllWSILLinks()
  {
    ListManager wsilLinks_ = (ListManager) getPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSIL_LINKS);
    if (wsilLinks_ == null)
    {
      initAllWSILLinks();
      wsilLinks_ = (ListManager) getPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSIL_LINKS);
    }
    return wsilLinks_;
  }

  public ListManager getAllUDDILinks()
  {
    ListManager uddiLinks_ = (ListManager) getPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_LINKS);
    if (uddiLinks_ == null)
    {
      initAllUDDILinks();
      uddiLinks_ = (ListManager) getPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_LINKS);
    }
    return uddiLinks_;
  }

  private void initAllUDDILinks()
  {
    ListManager uddiLinks_ = new ListManager();
    Inspection inspection = wsilDoc_.getInspection();
    Link[] linkList = inspection.getLinks();
    Vector uddiBusinessElements = new Vector();
    for (int i = 0; i < linkList.length; i++)
    {
      // process any links with UDDI namespace
      String namespace = linkList[i].getReferencedNamespace();
      if (namespace == null)
        continue;
      else if ((namespace.equals(NAMESPACE_UDDI_V1) || namespace.equals(NAMESPACE_UDDI_V2)))
      {
        WsilUddiBusinessElement uddiBusinessElement = new WsilUddiBusinessElement(namespace, getModel(), linkList[i]);
        uddiBusinessElement.setBaseWsilURL(getWsilUrl());
        if (uddiBusinessElement.validateUDDILink())
        {
          uddiBusinessElement.setName(uddiBusinessElement.getName());
          uddiBusinessElements.add(uddiBusinessElement);
        }
      }
    }
    refreshServiceProvidersFromRegistry(uddiBusinessElements);
    for (Iterator it = uddiBusinessElements.iterator(); it.hasNext();)
    {
      uddiLinks_.add(new ListElement(it.next()));
    }
    setPropertyAsObject(WsilModelConstants.LIST_MANAGER_UDDI_LINKS, uddiLinks_);
  }

  public void refreshServiceProvidersFromRegistry(Vector uddiBusinessElements)
  {
    HashMap inquiryAPIToBusinesses = new HashMap();
    for (int i = 0; i < uddiBusinessElements.size(); i++)
    {
      WsilUddiBusinessElement uddiBusinessElement = (WsilUddiBusinessElement) uddiBusinessElements.get(i);
      String inquiryAPI = uddiBusinessElement.getUDDILinkInquiryAPI();
      if (Validator.validateURL(inquiryAPI))
      {
        Vector businesses = (Vector) inquiryAPIToBusinesses.get(inquiryAPI);
        if (businesses == null)
        {
          businesses = new Vector();
          inquiryAPIToBusinesses.put(inquiryAPI, businesses);
        }
        businesses.add(uddiBusinessElement);
      }
      else
        uddiBusinessElement.setServiceProvider(null);
    }
    for (Iterator it = inquiryAPIToBusinesses.keySet().iterator(); it.hasNext();)
    {
      String inquiryAPI = (String) it.next();
      Vector businesses = (Vector) inquiryAPIToBusinesses.get(inquiryAPI);
      UDDIProxy proxy;
      try
      {
        Properties props = new Properties();
        props.put(ActionInputs.TRANSPORT_CLASS_NAME, ActionInputs.TRASPORT_CLASS);
        proxy = new UDDIProxy(props);
        proxy.setInquiryURL(new URL(inquiryAPI));
      }
      catch (Exception e)
      {
        for (int j = 0; j < businesses.size(); j++)
        {
          WsilUddiBusinessElement uddiBusinessElement = (WsilUddiBusinessElement) businesses.get(j);
          uddiBusinessElement.setServiceProvider(null);
        }
        continue;
      }
      Vector businessKeys = new Vector();
      for (int j = 0; j < businesses.size(); j++)
      {
        businessKeys.add(((WsilUddiBusinessElement) businesses.get(j)).getUDDILinkBusinessKey());
      }
      Vector businessEntities;
      try
      {
        businessEntities = proxy.get_businessDetail(businessKeys).getBusinessEntityVector();
      }
      catch (Exception ex)
      {
        businessEntities = new Vector();
        if (businessKeys.size() > 1)
        {
          for (int k = 0; k < businessKeys.size(); k++)
          {
            try
            {
              businessEntities.add((BusinessEntity) proxy.get_businessDetail((String) businessKeys.get(k)).getBusinessEntityVector().get(0));
            }
            catch (Exception exception)
            {
              businessEntities.add(null);
            }
          }
        }
        else
          businessEntities.add(null);
      }
      for (int k = 0; k < businesses.size(); k++)
      {
        BusinessEntity be = (BusinessEntity) businessEntities.get(k);
        WsilUddiBusinessElement uddiBusinessElement = (WsilUddiBusinessElement) businesses.get(k);
        uddiBusinessElement.setServiceProvider(be);
      }
    }
  }

  private void initAllWSILLinks()
  {
    ListManager wsilLinks_ = new ListManager();
    Inspection inspection = wsilDoc_.getInspection();
    Link[] linkList = inspection.getLinks();
    for (int i = 0; i < linkList.length; i++)
    {
      // process any links with UDDI namespace
      String namespace = linkList[i].getReferencedNamespace();
      if (namespace == null)
        continue;
      else if (namespace.equals(NAMESPACE_WSIL_INSPECTION))
      {
        WsilWsilLinkElement wsilLinkElement = new WsilWsilLinkElement(namespace, getModel(), linkList[i]);
        wsilLinkElement.setBaseWsilURL(getWsilUrl());
        if (wsilLinkElement.validateWSILLink())
        {
          wsilLinkElement.setName(wsilLinkElement.getWSILLinkLocation());
          wsilLinks_.add(new ListElement(wsilLinkElement));
        }
      }
    }
    setPropertyAsObject(WsilModelConstants.LIST_MANAGER_WSIL_LINKS, wsilLinks_);
  }
}

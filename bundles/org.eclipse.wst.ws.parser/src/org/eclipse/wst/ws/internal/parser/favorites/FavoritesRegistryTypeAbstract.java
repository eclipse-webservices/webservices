/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.parser.favorites;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.apache.wsil.Abstract;
import org.apache.wsil.Description;
import org.apache.wsil.Inspection;
import org.apache.wsil.Link;
import org.apache.wsil.Service;
import org.apache.wsil.ServiceName;
import org.apache.wsil.WSILDocument;
import org.apache.wsil.WSILException;
import org.apache.wsil.extension.uddi.BusinessDescription;
import org.apache.wsil.extension.uddi.ServiceDescription;
import org.apache.wsil.impl.AbstractImpl;
import org.apache.wsil.impl.DescriptionImpl;
import org.apache.wsil.impl.LinkImpl;
import org.apache.wsil.impl.ServiceImpl;
import org.apache.wsil.impl.ServiceNameImpl;
import org.apache.wsil.impl.extension.uddi.BusinessDescriptionImpl;
import org.apache.wsil.impl.extension.uddi.ServiceDescriptionImpl;
import org.uddi4j.util.BusinessKey;
import org.uddi4j.util.ServiceKey;

public abstract class FavoritesRegistryTypeAbstract implements IFavoritesRegistryType
{
  public FavoritesRegistryTypeAbstract()
  {
  }

  public abstract String getReadLocation();
  public abstract String getWriteLocation();
  protected abstract WSILDocument getWSILDocument();

  public void init()
  {
    getWSILDocument();
  }

  protected WSILDocument loadWSILDocument(String path, boolean force)
  {
    try
    {
      WSILDocument wsilDoc = WSILDocument.newInstance();
      wsilDoc.read(new FileReader(new File(path)));
      return wsilDoc;
    }
    catch (Exception t)
    {
      if (force)
      {
        try
        {
          return WSILDocument.newInstance();
        }
        catch (Exception t2)
        {
          return null;
        }
      }
      else
        return null;
    }
  }

  public String getFavoritesVersion()
  {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    Abstract[] abstracts = inspection.getAbstracts();
    if (abstracts.length > 0)
      return abstracts[0].getText();
    else
      return null;
  }

  public void setFavoritesVersion(String version)
  {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    Abstract abst = new AbstractImpl();
    abst.setText(version);
    inspection.removeAbstracts();
    inspection.addAbstract(abst);
  }

  public synchronized void save() throws WSILException, IOException
  {
    WSILDocument wsilDoc = getWSILDocument();
    if (wsilDoc != null)
      wsilDoc.write(getWriteLocation());
  }

  public IFavoritesUDDIRegistry[] getFavoritesUDDIRegistries()
  {
    Link[] links = loadUDDIRegistries();
    FavoritesUDDIRegistry[] registries = new FavoritesUDDIRegistry[links.length];
    for (int i = 0; i < links.length; i++)
    {
      registries[i] = new FavoritesUDDIRegistry();
      registries[i].setLink(links[i]);
    }
    return registries;
  }

  public IFavoritesUDDIBusiness[] getFavoritesUDDIBusinesses()
  {
    Link[] links = loadUDDIBusinesses();
    FavoritesUDDIBusiness[] businesses = new FavoritesUDDIBusiness[links.length];
    for (int i = 0; i < links.length; i++)
    {
      businesses[i] = new FavoritesUDDIBusiness();
      businesses[i].setLink(links[i]);
    }
    return businesses;
  }

  public IFavoritesUDDIService[] getFavoritesUDDIServices()
  {
    Service[] services = loadUDDIServices();
    FavoritesUDDIService[] uddiServices = new FavoritesUDDIService[services.length];
    for (int i = 0; i < services.length; i++)
    {
      uddiServices[i] = new FavoritesUDDIService();
      uddiServices[i].setService(services[i]);
    }
    return uddiServices;
  }

  public IFavoritesUDDIServiceInterface[] getFavoritesUDDIServiceInterfaces()
  {
    Service[] services = loadUDDIServiceInterfaces();
    FavoritesUDDIServiceInterface[] serInts = new FavoritesUDDIServiceInterface[services.length];
    for (int i = 0; i < services.length; i++)
    {
      serInts[i] = new FavoritesUDDIServiceInterface();
      serInts[i].setService(services[i]);
    }
    return serInts;
  }

  public IFavoritesWSDL[] getFavoritesWSDLs()
  {
    Service[] services = loadWSDLServices();
    FavoritesWSDL[] wsdls = new FavoritesWSDL[services.length];
    for (int i = 0; i < services.length; i++)
    {
      wsdls[i] = new FavoritesWSDL();
      wsdls[i].setService(services[i]);
    }
    return wsdls;
  }

  public IFavoritesWSIL[] getFavoritesWSILs()
  {
    Link[] links = loadWSILs();
    FavoritesWSIL[] wsils = new FavoritesWSIL[links.length];
    for (int i = 0; i < links.length; i++)
    {
      wsils[i] = new FavoritesWSIL();
      wsils[i].setLink(links[i]);
    }
    return wsils;
  }

  public Link[] loadUDDIRegistries() {
    return loadLinksByNamespace(FavoritesConstants.NAMESPACE_UDDI_V1);
  }
    
  public Link[] loadUDDIBusinesses() {
    return loadLinksByNamespace(FavoritesConstants.NAMESPACE_UDDI_V2);
  }

  public Service[] loadUDDIServices() {
    return loadServicesByNamespace(FavoritesConstants.NAMESPACE_UDDI_V2);
  }

  public Service[] loadUDDIServiceInterfaces() {
    return loadServicesByNamespace(FavoritesConstants.NAMESPACE_UDDI_V1);
  }

  public Service[] loadWSDLServices() {
    return loadServicesByNamespace(FavoritesConstants.NAMESPACE_WSDL);
  }

  public Link[] loadWSILs() {
    return loadLinksByNamespace(FavoritesConstants.NAMESPACE_WSIL_INSPECTION);
  }

  private Service[] loadServicesByNamespace(String namespace) {
    Vector serviceVector = new Vector();
    WSILDocument wsilDoc = getWSILDocument();
    if (wsilDoc != null)
    {
      Inspection inspection = wsilDoc.getInspection();
      Service[] services = inspection.getServices();
      for (int i = 0; i < services.length; i++)
      {
         Description[] desc = services[i].getDescriptions();
         if (desc[0].getReferencedNamespace().equals(namespace))
           serviceVector.add(services[i]);
      }
    }
    Service[] services = new Service[serviceVector.size()];
    serviceVector.copyInto(services);
    return services;
  }

  private Link[] loadLinksByNamespace(String namespace) {
    Vector linkVector = new Vector();
    WSILDocument wsilDoc = getWSILDocument();
    if (wsilDoc != null)
    {
      Inspection inspection = wsilDoc.getInspection();
      Link[] links = inspection.getLinks();
      for (int i = 0; i < links.length; i++)
      {
        if (links[i].getReferencedNamespace().equals(namespace))
          linkVector.add(links[i]);
      }
    }
    Link[] links = new Link[linkVector.size()];
    linkVector.copyInto(links);
    return links;
  }

  public void addFavoritesUDDIRegistry(IFavoritesUDDIRegistry registry)
  {
    addUDDIRegistry(registry.getName(), registry.getInquiryURL(), registry.getPublishURL(), registry.getRegistrationURL());
  }

  public void addFavoritesUDDIBusiness(IFavoritesUDDIBusiness business)
  {
    addUDDIBusiness(business.getName(), business.getInquiryURL(), business.getBusinessKey());
  }

  public void addFavoritesUDDIService(IFavoritesUDDIService service)
  {
    addUDDIService(service.getName(), service.getInquiryURL(), service.getServiceKey());
  }

  public void addFavoritesUDDIServiceInterface(IFavoritesUDDIServiceInterface serviceInterface)
  {
    addUDDIServiceInterface(serviceInterface.getName(), serviceInterface.getInquiryURL(), serviceInterface.getServiceInterfaceKey());
  }

  public void addFavoritesWSDL(IFavoritesWSDL wsdl)
  {
    addWSDLService(wsdl.getWsdlUrl());
  }

  public void addFavoritesWSIL(IFavoritesWSIL wsil)
  {
    addWSILLink(wsil.getWsilUrl());
  }

  public Link addUDDIRegistry(String registryName, String inquiryAPI, String publishAPI, String registrationURL) {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    Link link = new LinkImpl();
    // registry name
    Abstract abst = new AbstractImpl();
    abst.setText(registryName);
    link.addAbstract(abst);
    // inquiry URL
    Abstract abst2 = new AbstractImpl();
    abst2.setText(inquiryAPI);
    link.addAbstract(abst2);
    // publish URL
    Abstract abst3 = new AbstractImpl();
    if (publishAPI != null)
      abst3.setText(publishAPI);
    else
      abst3.setText("");
    link.addAbstract(abst3);
    // registration URL
    Abstract abst4 = new AbstractImpl();
    if (registrationURL != null)
      abst4.setText(registrationURL);
    else
      abst4.setText("");
    link.addAbstract(abst4);
    // add namespace
    link.setReferencedNamespace(FavoritesConstants.NAMESPACE_UDDI_V1);
    inspection.addLink(link);
    return link;
  }

  public Link addUDDIBusiness(String businessName, String inquiryAPI, String businessKey) {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    Link link = new LinkImpl();
    Abstract abst = new AbstractImpl();
    abst.setText(businessName);
    link.addAbstract(abst);
    link.setReferencedNamespace(FavoritesConstants.NAMESPACE_UDDI_V2);
    BusinessDescription bd = new BusinessDescriptionImpl();
    bd.setLocation(inquiryAPI);
    BusinessKey key = new BusinessKey(businessKey);
    bd.setBusinessKey(key);
    link.setExtensionElement(bd);
    inspection.addLink(link);
    return link;
  }

  public Service addUDDIService(String serviceName, String inquiryAPI, String serviceKey) {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    Service service = new ServiceImpl();
    ServiceName name = new ServiceNameImpl();
    name.setText(serviceName);
    service.addServiceName(name);
    Description desc = new DescriptionImpl();
    desc.setReferencedNamespace(FavoritesConstants.NAMESPACE_UDDI_V2);
    ServiceDescription sd = new ServiceDescriptionImpl();
    sd.setLocation(inquiryAPI);
    ServiceKey key = new ServiceKey(serviceKey);
    sd.setServiceKey(key);
    desc.setExtensionElement(sd);
    service.addDescription(desc);
    inspection.addService(service);
    return service;
  }

  public Service addUDDIServiceInterface(String serIntName, String inquiryAPI, String serIntKey) {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    Service service = new ServiceImpl();
    ServiceName name = new ServiceNameImpl();
    name.setText(serIntName);
    service.addServiceName(name);
    Description desc = new DescriptionImpl();
    desc.setReferencedNamespace(FavoritesConstants.NAMESPACE_UDDI_V1);
    ServiceDescription sd = new ServiceDescriptionImpl();
    sd.setLocation(inquiryAPI);
    ServiceKey key = new ServiceKey(serIntKey);
    sd.setServiceKey(key);
    desc.setExtensionElement(sd);
    service.addDescription(desc);
    inspection.addService(service);
    return service;
  }

  public Service addWSDLService(String url) {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    Service service = new ServiceImpl();
    Description desc = new DescriptionImpl();
    desc.setLocation(url);
    desc.setReferencedNamespace(FavoritesConstants.NAMESPACE_WSDL);
    service.addDescription(desc);
    inspection.addService(service);
    return service;
  }

  public Link addWSILLink(String url) {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    Link link = new LinkImpl();
    link.setLocation(url);
    link.setReferencedNamespace(FavoritesConstants.NAMESPACE_WSIL_INSPECTION);
    inspection.addLink(link);
    return link;
  }

  public void removeFavoritesUDDIRegistry(IFavoritesUDDIRegistry registry)
  {
    if (registry instanceof FavoritesUDDIRegistry)
      removeLink(((FavoritesUDDIRegistry)registry).getLink());
  }

  public void removeFavoritesUDDIBusiness(IFavoritesUDDIBusiness business)
  {
    if (business instanceof FavoritesUDDIBusiness)
      removeLink(((FavoritesUDDIBusiness)business).getLink());
  }

  public void removeFavoritesUDDIService(IFavoritesUDDIService service)
  {
    if (service instanceof FavoritesUDDIService)
      removeService(((FavoritesUDDIService)service).getService());
  }

  public void removeFavoritesUDDIServiceInterface(IFavoritesUDDIServiceInterface serviceInterface)
  {
    if (serviceInterface instanceof FavoritesUDDIServiceInterface)
      removeService(((FavoritesUDDIServiceInterface)serviceInterface).getService());
  }

  public void removeFavoritesWSDL(IFavoritesWSDL wsdl)
  {
    if (wsdl instanceof FavoritesWSDL)
      removeService(((FavoritesWSDL)wsdl).getService());
  }

  public void removeFavoritesWSIL(IFavoritesWSIL wsil)
  {
    if (wsil instanceof FavoritesWSIL)
      removeLink(((FavoritesWSIL)wsil).getLink());
  }

  public void removeService(Service service)
  {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    inspection.removeService(service);
  }

  public void removeLink(Link link)
  {
    WSILDocument wsilDoc = getWSILDocument();
    Inspection inspection = wsilDoc.getInspection();
    inspection.removeLink(link);
  }
}

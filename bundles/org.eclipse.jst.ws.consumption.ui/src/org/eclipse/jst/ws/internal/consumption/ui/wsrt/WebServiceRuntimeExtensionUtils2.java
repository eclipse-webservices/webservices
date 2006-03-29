/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner     
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060324   116750 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.ws.internal.consumption.common.FacetMatcher;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.TypeSelectionFilter2;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUICore;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class WebServiceRuntimeExtensionUtils2
{
  private static WebServiceRuntimeExtensionRegistry2 registry = WebServiceRuntimeExtensionRegistry2.getInstance();
  
  public static RuntimeDescriptor getRuntimeById(String id)
  {
    Object result = registry.runtimes_.get(id);
    if (result!=null)
    {
      return (RuntimeDescriptor)result;
    }
    return null;        
  }
  
  
  public static String[] getAllServiceProjectTypes()
  {
    ArrayList finalTemplateIdList = new ArrayList();
    Iterator iter = registry.serviceRuntimes_.values().iterator();

    //Loop through all the serviceRuntimes
    while (iter.hasNext())   
    {
      ServiceRuntimeDescriptor desc = (ServiceRuntimeDescriptor)iter.next();
      
      //Get the templates for this service runtime
      Set templates = FacetMatchCache.getInstance().getTemplatesForServiceRuntime(desc.getId());
      
      //Add the template ids to the list if they have not already been added
      Iterator itr = templates.iterator();
      while (itr.hasNext())
      {
        IFacetedProjectTemplate template = (IFacetedProjectTemplate)itr.next();
        if (!finalTemplateIdList.contains(template.getId()))
        {
          finalTemplateIdList.add(template.getId());
        }
      }            
    }
    
    return (String[])finalTemplateIdList.toArray(new String[]{});
  }
  
  public static String[] getAllClientProjectTypes()
  {
    ArrayList finalTemplateIdList = new ArrayList();
    Iterator iter = registry.clientRuntimes_.values().iterator();

    //Loop through all the clientRuntimes
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      
      //Get the templates for this client runtime
      Set templates = FacetMatchCache.getInstance().getTemplatesForClientRuntime(desc.getId());
      
      //Add the template ids to the list if they have not already been added
      Iterator itr = templates.iterator();
      while (itr.hasNext())
      {
        IFacetedProjectTemplate template = (IFacetedProjectTemplate)itr.next();
        if (!finalTemplateIdList.contains(template.getId()))
        {
          finalTemplateIdList.add(template.getId());
        }
      }            
    }
    
    return (String[])finalTemplateIdList.toArray(new String[]{});
  }
  
  public static String getRuntimeLabelById(String runtimeId) 
  {
    RuntimeDescriptor desc = getRuntimeById(runtimeId);
    if (desc == null)
      return null;
    
    return desc.getLabel();
  }    
  
  public static RuntimeDescriptor getRuntimeByLabel(String label)
  {
    Iterator iter = registry.runtimes_.values().iterator();
    while (iter.hasNext())
    {
      RuntimeDescriptor descriptor = (RuntimeDescriptor)iter.next();
      if (descriptor!=null) {
        if (label.equals(descriptor.getLabel()))
          return descriptor;
      }
    }
    return null;      
  }      

  public static String getServerLabelById(String serverFactoryId)
  {
    IServerType serverType = ServerCore.findServerType(serverFactoryId);
    if (serverType == null)
      return null;
    
    String serverLabel = ServerUICore.getLabelProvider().getText(serverType);   
    return serverLabel;
  }
  
  public static String getServerInstanceLabelFromInstanceId( String instanceId )
  {
    IServer server = ServerCore.findServer( instanceId );    
    return server.getName();
  }  

  private static String[] getServerFactoryIdsByFacetRuntimes(Set facetRuntimes)
  {
    
    ArrayList supportedServerFactoryIds = new ArrayList();
    String[] serverTypeIds = getAllServerFactoryIdsWithRuntimes();
    Iterator itr = facetRuntimes.iterator();
    while(itr.hasNext())
    {
      IRuntime fRuntime = (IRuntime)itr.next();
      org.eclipse.wst.server.core.IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
      for (int i=0; i<serverTypeIds.length; i++)
      {
        IServerType serverType = ServerCore.findServerType(serverTypeIds[i]);
        String runtimeTypeId = serverType.getRuntimeType().getId();
        if (runtimeTypeId.equals(sRuntime.getRuntimeType().getId()))
        {
          supportedServerFactoryIds.add(serverTypeIds[i]);          
        }        
      }
    }
    
    return (String[])supportedServerFactoryIds.toArray(new String[0]);
    
    //Temporarily return all factory ids with runtimes
    //return getAllServerFactoryIdsWithRuntimes();    
  }
  
  //Service-side utilities
  public static WebServiceImpl getWebServiceImplById(String id)
  {
    Object result = registry.webServiceImpls_.get(id);
    if (result!=null)
    {
      return (WebServiceImpl)result;
    }
    return null;    
  }
  
  public static ServiceRuntimeDescriptor getServiceRuntimeDescriptorById(String id)
  {
    Object result = registry.serviceRuntimes_.get(id);
    if (result!=null)
    {
      return (ServiceRuntimeDescriptor)result;
    }
    return null;        
  }  
  
  public static IWebServiceRuntime getServiceRuntime( String serviceRuntimeId )
  {
    ServiceRuntimeDescriptor descriptor = getServiceRuntimeDescriptorById(serviceRuntimeId);
    IWebServiceRuntime    webserviceRuntime     = null;
    if (descriptor != null)
    {
        webserviceRuntime = descriptor.getWebServiceRuntime();
    }
    
    return webserviceRuntime;
  }
  
  public static String getServiceRuntimeId(TypeRuntimeServer trs, String projectName, String templateId)
  {
    boolean serverSelected = (trs.getServerId() != null) && (trs.getServerId().length() > 0); 
    //Find the first service runtime that supports the implementation type, runtime, server, and project
    String[] descs = getServiceRuntimesByServiceType(trs.getTypeId());
    for (int i=0; i<descs.length; i++)
    {
      ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(descs[i]);      
      if (desc.getRuntime().getId().equals(trs.getRuntimeId()))
      {
        if (serverSelected)
        {
          boolean supportsServer = doesServiceRuntimeSupportServer(desc.getId(), trs.getServerId());
          if (!supportsServer)
          {
            continue;
          }
        }
        
        IProject project = ProjectUtilities.getProject(projectName);
        if (project.exists())
        {
          if (doesServiceRuntimeSupportProject(desc.getId(), projectName))
          {
            return desc.getId();
          }
        }
        else
        {
          //Check if template is supported
          if (doesServiceRuntimeSupportTemplate(desc.getId(), templateId))
          {
            return desc.getId();
          }
        }
      }
    }
    
    return "";
    
  }  
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */    
  public static int getScenarioFromTypeId(String typeId)
  {
    return Integer.parseInt(typeId.substring(0,typeId.indexOf("/")));
  }
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */    
  public static String getWebServiceImplIdFromTypeId(String typeId)
  {
    return typeId.substring(typeId.indexOf("/")+1);
  }    
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   * 
   * @returns String[] containing the ids of all runtimes that
   * support this type.
   */
  public static String[] getRuntimesByServiceType(String typeId) 
  {
    int scenario = getScenarioFromTypeId(typeId);
    String implId = getWebServiceImplIdFromTypeId(typeId);    
    ArrayList ids = new ArrayList();
    Iterator iter = registry.serviceRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ServiceRuntimeDescriptor desc = (ServiceRuntimeDescriptor)iter.next();
      //Check if this serviceRuntime supports the implementation type
      if (desc.getServiceImplementationType().getId().equals(implId))
      {
        switch (scenario)
        {
          case WebServiceScenario.BOTTOMUP:
            if (desc.getBottomUp())
            {
              String runtimeId = desc.getRuntime().getId(); 
              if (!ids.contains(runtimeId))
              {
                ids.add(runtimeId);
              }
            }
            break;
          case WebServiceScenario.TOPDOWN:
            if (desc.getTopDown())
            {
              String runtimeId = desc.getRuntime().getId(); 
              if (!ids.contains(runtimeId))
              {
                ids.add(runtimeId);
              }
            }
            break;
          default:          
        }
      }
    }
    
    return (String[])ids.toArray(new String[]{});
  }
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   * 
   * @returns String[] containing the ids of all service runtimes that
   * support this type.
   */
  public static String[] getServiceRuntimesByServiceType(String typeId) 
  {
    int scenario = getScenarioFromTypeId(typeId);
    String implId = getWebServiceImplIdFromTypeId(typeId);    
    ArrayList ids = new ArrayList();
    Iterator iter = registry.serviceRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ServiceRuntimeDescriptor desc = (ServiceRuntimeDescriptor)iter.next();
      //Check if this serviceRuntime supports the implementation type
      if (desc.getServiceImplementationType().getId().equals(implId))
      {
        switch (scenario)
        {
          case WebServiceScenario.BOTTOMUP:
            if (desc.getBottomUp())
            {
              String serviceRuntimeId = desc.getId(); 
              ids.add(serviceRuntimeId);
            }
            break;
          case WebServiceScenario.TOPDOWN:
            if (desc.getTopDown())
            {
              String serviceRuntimeId = desc.getId(); 
              ids.add(serviceRuntimeId);
            }
            break;
          default:          
        }
      }
    }
    
    return (String[])ids.toArray(new String[]{});
  }  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   * @param runtimeId id of a Web service runtime (RuntimeDescriptor)
   */  
  public static boolean isRuntimeSupportedForServiceType(String typeId, String runtimeId)
  {
    String[] serviceRuntimeIds = getServiceRuntimesByServiceType(typeId);
    if (serviceRuntimeIds!=null)
    {
      for (int i=0; i < serviceRuntimeIds.length; i++)
      {
        ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(serviceRuntimeIds[i]);
        if (desc.getRuntime().getId().equals(runtimeId))
        {
          return true;
        }
      }
    }
    
    return false;
  }  
  
  public static String[] getServerFactoryIdsByServiceType(String typeId)
  {
    ArrayList serverFactoryIds = new ArrayList();
    String[] srts = getServiceRuntimesByServiceType(typeId);
    if (srts != null)
    {
      for (int i = 0; i < srts.length; i++)
      {
        //Get the runtimes that work for the facets required for this service runtime        
        String[] fIds = getServerFactoryIdsByServiceRuntime(srts[i]);
        for (int j=0; j<fIds.length; j++)
        {
          if (!serverFactoryIds.contains(fIds[j]))
          {
            serverFactoryIds.add(fIds[j]);            
          }
        }
      }
    }
    
    return (String[])serverFactoryIds.toArray(new String[]{});
    
    //Temporarily return all server type ids.
    //return getAllServerFactoryIdsWithRuntimes();
  }
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */
  public static boolean isServerSupportedForChosenServiceType(String typeId, String serverFactoryId)
  {
    String[] fIds = getServerFactoryIdsByServiceType(typeId);
    if (fIds == null)
    {
      return false;
    }

    for (int i=0;i<fIds.length;i++)
    {
      if (serverFactoryId.equals(fIds[i]))
      {
        return true;
      }      
    }
    
    return false;
  }
  
  public static String[] getServerFactoryIdsByServiceRuntime(String serviceRuntimeId)
  {       
    ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(serviceRuntimeId);
    Set facetRuntimes = getRuntimes(desc.getRequiredFacetVersions());
            
    //Temporarily return all server types
    //return getAllServerFactoryIdsWithRuntimes();
    return getServerFactoryIdsByFacetRuntimes(facetRuntimes);
  } 
  

  

  public static boolean doesServiceRuntimeSupportServer(String serviceRuntimeId, String serverFactoryId)
  {
    String[] serverIds = getServerFactoryIdsByServiceRuntime(serviceRuntimeId);
    for (int i=0; i<serverIds.length; i++)
    {
      if (serverIds[i].equals(serverFactoryId))
      {
        return true;
      }
    }
    
    return false;
  }
    
  
  public static LabelsAndIds getServiceTypeLabels()
  {   
    LabelsAndIds labelIds = new LabelsAndIds();
    Iterator     iterator = registry.webServiceTypesList_.iterator();
    int          size     = registry.webServiceTypesList_.size();
    String[]     labels   = new String[size];
    String[]     ids      = new String[size];
    int          index    = 0;
    
    labelIds.setLabels_( labels );
    labelIds.setIds_( ids );
    
    while( iterator.hasNext() ) 
    {
      String wst = (String)iterator.next();
      int scenario = getScenarioFromTypeId(wst);
      String implId = getWebServiceImplIdFromTypeId(wst);
      WebServiceImpl wsimpl = getWebServiceImplById(implId);
      String impllabel = wsimpl.getLabel();
      ids[index]    = wst;
      String scenLabel = "";
      switch(scenario)
      {
      case WebServiceScenario.BOTTOMUP:
        scenLabel = NLS.bind(ConsumptionUIMessages.BOTTOMUP_LABEL, new String[0]);
        break;
      case WebServiceScenario.TOPDOWN:
        scenLabel = NLS.bind(ConsumptionUIMessages.TOPDOWN_LABEL, new String[0]);
        break; 
      default:
      }
      labels[index] = NLS.bind(ConsumptionUIMessages.COMBINED_TYPE_AND_RUNTIME_LABEL, new String[]{ scenLabel, impllabel });
      index++;
    }    
    
    return labelIds;
  }
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */
  public static String getDefaultRuntimeValueFor(String typeId)
  {
    String[] srIds = getServiceRuntimesByServiceType(typeId);
    if (srIds == null)
    {
      return null;
    }
    
    ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(srIds[0]);
    return desc.getRuntime().getId();
  }      
  
  public static String getDefaultServerValueFor(String typeId)
  {
    String[] fIds = getServerFactoryIdsByServiceType(typeId);
    if (fIds==null || fIds.length==0)
      return null;
    
    return fIds[0];
  }    
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   * @param runtimeId is the id of a RuntimeDescriptor
   * @param serverFactoryId server factory id
   */    
  public static boolean isServerRuntimeTypeSupported(String serverFactoryId, String runtimeId, String typeId)  
  {
    //Ensure there is at least one service runtime that supports the given type
    String[] serviceRuntimes = getServiceRuntimesByServiceType(typeId);
    if (serviceRuntimes!=null && serviceRuntimes.length>0)
    {
      //Ensure that at least one of these server runtimes supports the given server
      for (int i=0; i<serviceRuntimes.length; i++)
      {
        ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(serviceRuntimes[i]);
        if (desc.getRuntime().getId().equals(runtimeId))
        {
          //Matches the type and the runtime. Check if it matches the server
          Set runtimes = getRuntimes(desc.getRequiredFacetVersions());
          String[] fIds = getServerFactoryIdsByFacetRuntimes(runtimes);
          for (int j=0; j<fIds.length; j++)
          {
            if (fIds[j].equals(serverFactoryId))
            {
              return true;
            }
          }          
        }        
      }
    }
    
    //didn't find a match. return false.
    return false;
  }  
  
  public static String[] getWebServiceTypeBySelection(IStructuredSelection selection)
  {
    TypeSelectionFilter2 tsf = new TypeSelectionFilter2();
    String[] wst = tsf.getWebServiceTypeByInitialSelection(selection, registry.webServiceTypesList_);
    return wst == null ? null : wst;
  }    
  
  /*
   * @param runtimeId : id of a RuntimeDescriptor
   * @param factoryId : id of a server type
   */
  public static boolean doesRuntimeSupportServerForServiceSide(String runtimeId, String factoryId)
  {
    //Get all the ServiceRuntimeDescriptors that point to this runtimeId
    Iterator iter = registry.serviceRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ServiceRuntimeDescriptor desc = (ServiceRuntimeDescriptor)iter.next();
      //check if this service runtime points to runtimeId
      String thisRuntimeId = desc.getRuntime().getId();
      if (thisRuntimeId.equals(runtimeId))
      {
        Set facetRuntimes = getRuntimes(desc.getRequiredFacetVersions());
        String[] fIds = getServerFactoryIdsByFacetRuntimes(facetRuntimes);
        for (int j=0; j<fIds.length; j++)
        {
          if (fIds[j].equals(factoryId))
          {
            return true;
          }
        }              
      }            
    }
    
    //No service runtime matched. Return false.
    return false;
  }
  
  /*
   * @param runtimeId: id of a RuntimeDescriptor
   * @return: server factory id
   */
  public static String getFirstSupportedServerForServiceSide(String runtimeId)
  {
    //Get all the ServiceRuntimeDescriptors that point to this runtimeId
    Iterator iter = registry.serviceRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ServiceRuntimeDescriptor desc = (ServiceRuntimeDescriptor)iter.next();
      //check if this service runtime points to runtimeId
      String thisRuntimeId = desc.getRuntime().getId();
      if (thisRuntimeId.equals(runtimeId))
      {
        Set facetRuntimes = getRuntimes(desc.getRequiredFacetVersions());
        String[] factoryIds = getServerFactoryIdsByFacetRuntimes(facetRuntimes);
        if (factoryIds!=null && factoryIds.length >0)
        {
          return factoryIds[0];
        }
      }            
    }
    
    //didn't get a single suitable server type id, return null.
    return null;
  }  
  
  public static String[] getProjectsForServiceTypeAndRuntime(String typeId, String runtimeId)
  {
    IProject[] projects = FacetUtils.getAllProjects();
    ArrayList validProjects = new ArrayList();
    
    for (int i=0; i<projects.length;i++)
    {
      if (doesServiceTypeAndRuntimeSupportProject(typeId, runtimeId, projects[i].getName()))
      {
        validProjects.add(projects[i].getName());        
      }      
    }
    
    return (String[])validProjects.toArray(new String[0]);
    
  }
  
  public static boolean doesServiceTypeAndRuntimeSupportProject(String typeId, String runtimeId, String projectName)
  {
    String[] descs = getServiceRuntimesByServiceType(typeId);
    for (int j = 0; j < descs.length; j++)
    {
      ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(descs[j]);
      if (desc.getRuntime().getId().equals(runtimeId))
      {
        if (doesServiceRuntimeSupportProject(descs[j], projectName))
        {
          return true;
        }
      }
    }
    
    return false;    
    
  }  
  public static boolean doesServiceRuntimeSupportProject(String serviceRuntimeId, String projectName)
  {
    FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(false, serviceRuntimeId, projectName);
    if (fm != null)
    {
      return fm.isMatch();
    }
    else
    {
      return false;
    }    
  }  
  
  /*
   * Returns a list of valid faceted project template ids
   * @param typeId id of the form "0/implId" on the service side.
   * @param runtimeId id of a RuntimeDescriptor
   * 
   * @return String[] array of IFacetedProjectTemplate ids
   */
  public static String[] getServiceProjectTemplates(String typeId, String runtimeId)
  {
    String[] srIds = getServiceRuntimesByServiceType(typeId);
    if (srIds == null)
    {
      return null;
    }

    ArrayList templateIdList = new ArrayList();
    for (int i = 0; i < srIds.length; i++)
    {
      ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(srIds[i]);
      String thisRuntimeId = desc.getRuntime().getId();
      if (thisRuntimeId.equals(runtimeId))
      {
        //Get the templates for this client runtime
        Set templates = FacetMatchCache.getInstance().getTemplatesForServiceRuntime(desc.getId());
        
        //Add the template ids to the list if they have not already been added
        Iterator itr = templates.iterator();
        while (itr.hasNext())
        {
          IFacetedProjectTemplate template = (IFacetedProjectTemplate)itr.next();
          if (!templateIdList.contains(template.getId()))
          {
            templateIdList.add(template.getId());
          }
        }
      }
      
    }  
    
    return (String[])templateIdList.toArray(new String[]{});    
  }

  public static boolean doesServiceTypeAndRuntimeSupportTemplate(String typeId, String runtimeId, String templateId)
  {
    String[] srIds = getServiceRuntimesByServiceType(typeId);
    if (srIds == null)
    {
      return false;
    }

    for (int i = 0; i < srIds.length; i++)
    {
      ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(srIds[i]);
      String thisRuntimeId = desc.getRuntime().getId();
      if (thisRuntimeId.equals(runtimeId))
      {
        //Get the templates for this service runtime
        Set templates = FacetMatchCache.getInstance().getTemplatesForServiceRuntime(srIds[i]);
        
        //Check if any of the template ids match the given one.
        Iterator itr = templates.iterator();
        while (itr.hasNext())
        {
          IFacetedProjectTemplate template = (IFacetedProjectTemplate)itr.next();
          if (template.getId().equals(templateId))
          {
            return true;
          }

        }
      }
      
    }  
    
    return false;    
  }
  
  public static boolean doesServiceRuntimeSupportTemplate(String serviceRuntimeId, String templateId)
  {
    //Get the templates for this service runtime
    Set templates = FacetMatchCache.getInstance().getTemplatesForServiceRuntime(serviceRuntimeId);
    IFacetedProjectTemplate checkingTemplate = ProjectFacetsManager.getTemplate(templateId);
    return templates.contains(checkingTemplate);
  }  
  
  //Client-side utilities
  public static WebServiceClientImpl getWebServiceClientImplById(String id)
  {
    Object result = registry.webServiceClientImpls_.get(id);
    if (result!=null)
    {
      return (WebServiceClientImpl)result;
    }
    return null;    
  }
  
  public static ClientRuntimeDescriptor getClientRuntimeDescriptorById(String id)
  {
    Object result = registry.clientRuntimes_.get(id);
    if (result!=null)
    {
      return (ClientRuntimeDescriptor)result;
    }
    return null;        
  }    
  
  public static IWebServiceRuntime getClientRuntime( String clientRuntimeId )
  {
    ClientRuntimeDescriptor descriptor = getClientRuntimeDescriptorById(clientRuntimeId);
    IWebServiceRuntime    webserviceRuntime     = null;
    if (descriptor != null)
    {
        webserviceRuntime = descriptor.getWebServiceRuntime();
    }
    
    return webserviceRuntime;
  }
  
  public static String getClientRuntimeId(TypeRuntimeServer trs, String projectName, String templateId)
  {
    boolean serverSelected = (trs.getServerId()!=null) && (trs.getServerId().length()>0);
    //Find the first client runtime that supports the implementation type, runtime, server, and project
    String[] descs = getClientRuntimesByType(trs.getTypeId());
    for (int i=0; i<descs.length; i++)
    {
      ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(descs[i]);      
      if (desc.getRuntime().getId().equals(trs.getRuntimeId()))
      {
        if (serverSelected)
        {
          boolean supportsServer = doesClientRuntimeSupportServer(desc.getId(), trs.getServerId());
          if (!supportsServer)
          {
            continue;
          }
        }
        
        IProject project = ProjectUtilities.getProject(projectName);
        if (project.exists())
        {
          if (doesClientRuntimeSupportProject(desc.getId(), projectName))
          {
            return desc.getId();
          }
        }
        else
        {
          //Check if template is supported
          if (doesClientRuntimeSupportTemplate(desc.getId(), templateId))
          {
            return desc.getId();
          }
        }
      }
    }
    
    return "";
    
  }
  
  /*
   * @return String[] array of ids of RuntimeDescriptors
   */
  public static String[] getAllRuntimesForClientSide() 
  {
    ArrayList runtimeIds = new ArrayList();
    Iterator iter = registry.clientRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      String thisRuntimeId = desc.getRuntime().getId(); 
      if (!runtimeIds.contains(thisRuntimeId))
      runtimeIds.add(thisRuntimeId);
    }      
    return (String[])runtimeIds.toArray(new String[]{});
  }
  
  /*
   * @param clientImpld The id of a client implementation type
   * 
   * @returns String[] containing the ids of all clientRuntimes that
   * support this client implementation type
   */
  public static String[] getClientRuntimesByType(String clientImplId) 
  {
    ArrayList ids = new ArrayList();
    Iterator iter = registry.clientRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      //Check if this serviceRuntime supports the implementation type
      if (desc.getClientImplementationType().getId().equals(clientImplId))
      {
        ids.add(desc.getId());
      }
    }
    
    return (String[])ids.toArray(new String[]{});
  }  
  
  /*
   * @param clientImplId id of a WebServiceClientImpl
   * @return String[] array of RuntimeDescriptor ids
   */
  public static String[] getRuntimesByClientType(String clientImplId) 
  {
    ArrayList runtimeIds = new ArrayList();
    Iterator iter = registry.clientRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      //Check if this serviceRuntime supports the implementation type
      if (desc.getClientImplementationType().getId().equals(clientImplId))
      {
        if (!runtimeIds.contains(desc.getRuntime().getId()))
        {
          runtimeIds.add(desc.getRuntime().getId());
        }
      }
    }
    
    return (String[])runtimeIds.toArray(new String[]{});
  }
  
  /*
   * @param clientImplId The id of a client implementation type
   * @param runtimeId id of a runtime (RuntimeDescriptor)
   */  
  public static boolean isRuntimeSupportedForClientType(String clientImplId, String runtimeId)
  {
    String[] clientRuntimeIds = getClientRuntimesByType(clientImplId);
    if (clientRuntimeIds!=null)
    {
      for (int i=0; i < clientRuntimeIds.length; i++)
      {
        ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(clientRuntimeIds[i]);
        if (desc.getRuntime().getId().equals(runtimeId))
        {
          return true;
        }
      }
    }
    
    return false;
  }
  
  public static String[] getAllClientServerFactoryIds() 
  {    
    //Iterator iter = registry.clientRuntimes_.values().iterator();
    //while (iter.hasNext())   
    //{
      //TODO iterate over all the server types and see if their runtime types have an
      //id that matches the runtime type of any of the runtimes.            
      // ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      // Set runtimes = getRuntimes(desc.getRequiredFacetVersions());
      // IServerType[] allServerTypes = ServerCore.getServerTypes();
    //}
    //return (String[])serverFactoryIds.toArray(new String[]{});
    //Temporarily return all server types
    return getAllServerFactoryIdsWithRuntimes();    
  }
  
  public static String[] getServerFactoryIdsByClientType(String clientImplId)
  {
    ArrayList serverFactoryIds = new ArrayList();
    String[] crts = getClientRuntimesByType(clientImplId);
    if (crts != null)
    {
      for (int i = 0; i < crts.length; i++)
      {
        //Get the runtimes that work for the facets required for this service runtime        
        String[] fIds = getServerFactoryIdsByClientRuntime(crts[i]);
        for (int j=0; j<fIds.length; j++)
        {
          if (!serverFactoryIds.contains(fIds[j]))
          {
            serverFactoryIds.add(fIds[j]);            
          }
        }
      }        
    }
    
    return (String[])serverFactoryIds.toArray(new String[]{});
  }  
  
  /*
   * 
   */
  public static boolean isServerSupportedForChosenClientType(String clientImplId, String serverFactoryId)
  {
    String[] fIds = getServerFactoryIdsByClientType(clientImplId);
    if (fIds == null)
    {
      return false;
    }

    for (int i=0;i<fIds.length;i++)
    {
      if (serverFactoryId.equals(fIds[i]))
      {
        return true;
      }      
    }
    
    return false;
  }  
  
  /*
   * @prarm clientRuntimeId : id of a ClientRuntimeDescriptor
   * 
   */
  public static String[] getServerFactoryIdsByClientRuntime(String clientRuntimeId)
  {       
    ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(clientRuntimeId);
    Set facetRuntimes = getRuntimes(desc.getRequiredFacetVersions());

    String[] fIds = getServerFactoryIdsByFacetRuntimes(facetRuntimes);
    return fIds;    
  }    

  /*
   * @param clientRuntimeId id of a ClientRuntimeDescriptor
   */
  public static boolean doesClientRuntimeSupportServer(String clientRuntimeId, String serverFactoryId)
  {
    String[] serverIds = getServerFactoryIdsByClientRuntime(clientRuntimeId);
    for (int i=0; i<serverIds.length; i++)
    {
      if (serverIds[i].equals(serverFactoryId))
      {
        return true;
      }
    }
    
    return false;
  }
  
  /*
   * @param runtimeId : id of a RuntimeDescriptor
   * @param factoryId : id of a server type
   */
  public static boolean doesRuntimeSupportServerForClientSide(String runtimeId, String factoryId)
  {
    //Get all the ClientRuntimeDescriptors that point to this runtimeId
    Iterator iter = registry.clientRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      //check if this client runtime points to runtimeId
      String thisRuntimeId = desc.getRuntime().getId();
      if (thisRuntimeId.equals(runtimeId))
      {
        Set facetRuntimes = getRuntimes(desc.getRequiredFacetVersions());
        String[] fIds = getServerFactoryIdsByFacetRuntimes(facetRuntimes);
        for (int i=0; i<fIds.length; i++)
        {
          if (fIds[i].equals(factoryId))
          {
            return true;
          }
        }
      }            
    }
    
    //No match.
    return false;
  }    
  
  /*
   * @param runtimeId: id of a RuntimeDescriptor
   * @return: server factory id
   */
  public static String getFirstSupportedServerForClientSide(String runtimeId)
  {
    //Get all the ClientRuntimeDescriptors that point to this runtimeId
    Iterator iter = registry.clientRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      //check if this service runtime points to runtimeId
      String thisRuntimeId = desc.getRuntime().getId();
      if (thisRuntimeId.equals(runtimeId))
      {
        Set facetRuntimes = getRuntimes(desc.getRequiredFacetVersions());
        String[] factoryIds = getServerFactoryIdsByFacetRuntimes(facetRuntimes);
        if (factoryIds!=null && factoryIds.length >0)
        {
          return factoryIds[0];
        }
      }            
    }
    
    //No suitable servers found. Return null.
    return null;

  }
  
  /*
   * @param clientImplId is the id of a WebServiceClientImpl
   * @param runtimeId is the id of a RuntimeDescriptor
   * @param serverFactoryId server factory id
   */    
  public static boolean isServerClientRuntimeTypeSupported(String serverFactoryId, String runtimeId, String clientImplId)  
  {
    //Ensure there is at least one client runtime that supports the given type
    String[] clientRuntimes = getClientRuntimesByType(clientImplId);
    if (clientRuntimes!=null && clientRuntimes.length>0)
    {
      //Ensure that at least one of these server runtimes supports the given server
      for (int i=0; i<clientRuntimes.length; i++)
      {
        ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(clientRuntimes[i]);
        if (desc.getRuntime().getId().equals(runtimeId))
        {
          //Matched type and runtime. Check the server.
          String[] factoryIds = getServerFactoryIdsByClientRuntime(desc.getId());
          for (int j=0; j<factoryIds.length; j++)
          {
            if (factoryIds[j].equals(serverFactoryId))
            {
              return true;
            }
          }
        }
      }
    }
    
    //No match found. return false.
    return false;
  }    
  
  /*
   * Returns a set of templates supported by th given client runtime
   * @returns Set (type: IFacetedProjectTemplate)
   */
  /*
  private static Set getTemplatesForClientRuntime(String clientRuntimeId)
  {
    Set templates = (Set)registry.templatesByClientRuntimeId_.get(clientRuntimeId);
    if (templates != null)
    {
      //Return the cached set of templates.
      return templates;
    }
    else
    {
      //Calculate the templates, cache them for later use, and return them.
      ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(clientRuntimeId);
      //Set validTemplates = FacetUtils.getTemplates(desc.getRequiredFacetVersions());
      Set validTemplates = FacetMatchCache.getInstance().getTemplates(desc.getRequiredFacetVersions());
      registry.templatesByClientRuntimeId_.put(clientRuntimeId, validTemplates);
      return validTemplates;
    }
  }
  */
  /*
   * Returns a list of valid faceted project template ids
   * @param clientImplId id of a WebServiceClientImpl
   * @param runtimeId id of a RuntimeDescriptor
   * 
   * @return String[] array of IFacetedProjectTemplate ids
   */
  public static String[] getClientProjectTemplates(String clientImplId, String runtimeId)
  {
    String[] crIds = getClientRuntimesByType(clientImplId);
    if (crIds == null)
    {
      return null;
    }

    ArrayList templateIdList = new ArrayList();
    for (int i = 0; i < crIds.length; i++)
    {
      ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(crIds[i]);
      String thisRuntimeId = desc.getRuntime().getId();
      if (thisRuntimeId.equals(runtimeId))
      {
        //Get the templates for this client runtime
        Set templates = FacetMatchCache.getInstance().getTemplatesForClientRuntime(desc.getId());
        
        //Add the template ids to the list if they have not already been added
        Iterator itr = templates.iterator();
        while (itr.hasNext())
        {
          IFacetedProjectTemplate template = (IFacetedProjectTemplate)itr.next();
          if (!templateIdList.contains(template.getId()))
          {
            templateIdList.add(template.getId());
          }
        }
      }
      
    }  
    
    return (String[])templateIdList.toArray(new String[]{});    
  }  
  
  public static boolean doesClientTypeAndRuntimeSupportTemplate(String clientImplId, String runtimeId, String templateId)
  {
    String[] crIds = getClientRuntimesByType(clientImplId);
    if (crIds == null)
    {
      return false;
    }

    for (int i = 0; i < crIds.length; i++)
    {
      ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(crIds[i]);
      String thisRuntimeId = desc.getRuntime().getId();
      if (thisRuntimeId.equals(runtimeId))
      {
        //Get the templates for this client runtime
        Set templates = FacetMatchCache.getInstance().getTemplatesForClientRuntime(crIds[i]);
        
        //Check if the template ids contains the template we're checking for
        Iterator itr = templates.iterator();
        while (itr.hasNext())
        {
          IFacetedProjectTemplate template = (IFacetedProjectTemplate)itr.next();
          if (template.getId().equals(templateId))
          {
            return true;
          }
        }
      }
      
    }  
    
    return false;    
  }  
  

  public static boolean doesClientRuntimeSupportTemplate(String clientRuntimeId, String templateId)
  {
    //ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(clientRuntimeId);

    //Get the templates for this client runtime
    Set templates = FacetMatchCache.getInstance().getTemplatesForClientRuntime(clientRuntimeId);
    IFacetedProjectTemplate checkingTemplate = ProjectFacetsManager.getTemplate(templateId);
    return templates.contains(checkingTemplate);
  }
  
    
  public static LabelsAndIds getClientTypeLabels()
  {
    
    LabelsAndIds labelIds = new LabelsAndIds();
    String[] idsArray = new String[0];
    String[] labelsArray = new String[0];
    labelIds.setIds_(idsArray);
    labelIds.setLabels_(labelsArray);
  
    ArrayList ids = new ArrayList();
    ArrayList labels = new ArrayList();
  
    Iterator itr = registry.clientRuntimes_.values().iterator();
    while(itr.hasNext())
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)itr.next();
      WebServiceClientImpl thisClientImpl = desc.getClientImplementationType();
      if (!ids.contains(thisClientImpl.getId()))
      {
        ids.add(thisClientImpl.getId());
        labels.add(thisClientImpl.getLabel());
      }
    }

    if (ids.size() > 0)
    {
      idsArray = (String[]) ids.toArray(new String[0]);
      labelsArray = (String[]) labels.toArray(new String[0]);
      labelIds.setIds_(idsArray);
      labelIds.setLabels_(labelsArray);
    }

    return labelIds;

  }  
  
  public static String[] getAllServerFactoryIdsWithRuntimes()
  {
    ArrayList fids = new ArrayList();
    IServerType[] sts = ServerCore.getServerTypes();
    org.eclipse.wst.server.core.IRuntime[] rts = ServerCore.getRuntimes();
    
    for (int i=0; i<sts.length; i++)
    {
      IServerType st = sts[i];
      for (int j=0; j<rts.length; j++)
      {
        org.eclipse.wst.server.core.IRuntime rt = rts[j];
        // If the server type has the same runtime type as this runtime, add it to the list
        String serverTypeRuntimeTypeId = st.getRuntimeType().getId();
        String runtimeRuntimeTypeId = rt.getRuntimeType().getId();
        if (serverTypeRuntimeTypeId.equals(runtimeRuntimeTypeId))
        {
          if (!fids.contains(st.getId()))
          {
            fids.add(st.getId());
          }
        }
      }
    }
   
    return (String[])fids.toArray(new String[0]);
  }
  
  public static String[] getProjectsForClientTypeAndRuntime(String typeId, String runtimeId)
  {
    //String[] descs = getClientRuntimesByType(typeId);
    IProject[] projects = FacetUtils.getAllProjects();
    ArrayList validProjects = new ArrayList();
    
    for (int i=0; i<projects.length;i++)
    {
      if (doesClientTypeAndRuntimeSupportProject(typeId, runtimeId, projects[i].getName()))
      {
        validProjects.add(projects[i].getName());        
      }      
    }
    
    return (String[])validProjects.toArray(new String[0]);
    
  }  
  
  public static boolean doesClientTypeAndRuntimeSupportProject(String typeId, String runtimeId, String projectName)
  {
    String[] descs = getClientRuntimesByType(typeId);
    for (int j = 0; j < descs.length; j++)
    {
      ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(descs[j]);
      if (desc.getRuntime().getId().equals(runtimeId))
      {
        if (doesClientRuntimeSupportProject(descs[j], projectName))
        {
          return true;
        }
      }
    }
    
    return false;
    
    
  }  
  
  
  public static boolean doesClientRuntimeSupportProject(String clientRuntimeId, String projectName)
  {
    FacetMatcher fm = FacetMatchCache.getInstance().getMatchForProject(true, clientRuntimeId, projectName);
    if (fm != null)
    {
      return fm.isMatch();
    }
    else
    {
      return false;
    }
  }
  
  //Utilities used by the ServerRuntimePreferencePage
  
  private static SelectionListChoices serverToRuntimeToJ2EE_;
  private static Hashtable serverFactoryIdByLabel_;
  private static Hashtable runtimeIdByLabel_;
  
  public static SelectionListChoices getServerToRuntimeToJ2EE()
  {
    if (serverToRuntimeToJ2EE_!=null)
    {
      return serverToRuntimeToJ2EE_;
    }
    
    //String[] servers = getStringArrayIntersection(getAllServerFactoryIds(), WebServiceClientTypeRegistry.getInstance().getAllClientServerFactoryIds());
    String[] servers = getAllServerFactoryIds();
    SelectionList serversList = new SelectionList(servers, 0);
    Vector choices = new Vector();
    for (int i=0; i<servers.length; i++)
    {
      choices.add(getRuntimeChoices(servers[i]));
    }
    serverToRuntimeToJ2EE_ = new SelectionListChoices(serversList, choices);
    return serverToRuntimeToJ2EE_;
    
  }

  private static SelectionListChoices getRuntimeChoices(String serverFactoryId)
  {
    //Return all the runtimes for now.
    Set runtimes = registry.runtimes_.keySet();
    Iterator itr = registry.runtimes_.keySet().iterator();
    String[] runtimeIds = new String[runtimes.size()];
    //TODO String[] runtimeIds = getRuntimeIDsByServerFactoryID(serverFactoryId);    
    int i = 0;
    while (itr.hasNext())
    {
      String runtimeId = (String)itr.next();
      runtimeIds[i] = runtimeId;
      i++;
     
    }

    SelectionList runtimesList = new SelectionList(runtimeIds, 0);
    Vector choices = new Vector();
    for (int j=0; j<runtimeIds.length; j++)
    {
      choices.add(getJ2EEChoices(runtimeIds[j]));
    }
    return new SelectionListChoices(runtimesList, choices);    
  }  
  
  private static SelectionListChoices getJ2EEChoices(String runtimeId)
  {
    //J2EE levels will be removed from the Server Runtime preference page.
    //Return some hard coded values for now.
    String[] j2eeVersions = new String[]{"13", "14"};
    SelectionList j2eeVersionsList = new SelectionList(j2eeVersions, 0);
    return new SelectionListChoices(j2eeVersionsList, null);        
  }  
  
  private static String[] getAllServerFactoryIds()
  {
    //Return all server type ids for now.
    //TODO Only the servers that are appropriate for the Web service runtimes should be displayed.
    ArrayList ids = new ArrayList();
    if (serverFactoryIdByLabel_ == null)
    {
      serverFactoryIdByLabel_ = new Hashtable();
      IServerType[] serverTypes = ServerCore.getServerTypes();
      for (int i=0; i<serverTypes.length; i++)
      {
        String id = serverTypes[i].getId();
        String label = getServerLabelById(id);
        serverFactoryIdByLabel_.put(label, id);
        ids.add(id);
      }    
    }
    else
    {
      Iterator fids =  serverFactoryIdByLabel_.values().iterator();
      while (fids.hasNext())
      {
        String fid = (String)fids.next();
        ids.add(fid);
      }           
    }
    
    if (ids.size() > 0)
    {
      String[] serverFactoryIds = (String[])ids.toArray(new String[0]);
      return serverFactoryIds;
    }
    
    return null;
  }  
  
  //TODO this needs to be implemented once facet runtime to server runtime bridge is available.
  /*
  private static String[] getRuntimeIDsByServerFactoryID(String serverFactoryID) 
  {
    ArrayList ids = new ArrayList();
    Iterator iter = registry.webServiceRuntimes_.values().iterator();
    while (iter.hasNext())
    {
      WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo)iter.next();
      if (doesRuntimeSupportServer(wsr.getId(), serverFactoryID))
      {
        ids.add(wsr.getId());
      }
      
    }
    
    if (ids.size() > 0)
    {
      String[] runtimeIds = (String[])ids.toArray(new String[0]);
      return runtimeIds;
    }
    
    return null;    
    
    
  }
  */
  
  public static String getServerFactoryId(String label)
  {
    if (label==null || label.length()==0)
      return null;
    
    if (serverFactoryIdByLabel_ == null)
    {
      getAllServerFactoryIds();
    }
    
    if (serverFactoryIdByLabel_.containsKey(label))
    {
      return (String)serverFactoryIdByLabel_.get(label);  
    }
    else
    {
      return null;
    }       
  }  
  
  public static String getRuntimeId(String label)
  {
    
    if (label==null || label.length()==0)
      return null;
    
    if (runtimeIdByLabel_ == null)
    {
      runtimeIdByLabel_ = new Hashtable();
      Iterator iter = registry.runtimes_.values().iterator();
      while (iter.hasNext())
      {
        RuntimeDescriptor desc = (RuntimeDescriptor)iter.next();
        runtimeIdByLabel_.put(desc.getLabel(), desc.getId());        
      }      
    }
    
    return (String)runtimeIdByLabel_.get(label);    
  }    
  
    
  private static Set getRuntimes(RequiredFacetVersion[] requiredFacetVersions)
  {
    return FacetUtils.getRuntimes(requiredFacetVersions);
  }
}

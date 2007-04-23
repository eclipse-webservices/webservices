/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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
 * 20060427   126780 rsinha@ca.ibm.com - Rupam Kuehner
 * 20070119   159458 mahutch@ca.ibm.com - Mark Hutchinson
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
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUICore;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

/**
 * This class contains numerous utility methods that
 * process the information provided through extension points
 * which are read in WebServiceRuntimeExtensionRegistry2:
 * <ul>
 * <li> org.eclipse.jst.ws.consumption.ui.wsImpl </li>
 * <li> org.eclipse.jst.ws.consumption.ui.wsClientImpl </li>
 * <li> org.eclipse.jst.ws.consumption.ui.runtimes </li>
 * <li> org.eclipse.jst.ws.consumption.ui.serviceRuntimes </li>
 * <li> org.eclipse.jst.ws.consumption.ui.clientRuntimes </li>
 * </ul>
 * 
 * to provide answers to common questions regarding which servers,
 * projects, and project types a particular Web service runtime
 * supports.
 * <br/><br/>
 * Teminology used in the javadoc in this class:
 * <ul>
 * <li><b>Web service scenario</b>: One of WebServiceScenario.BOTTOM_UP or WebServiceScenario.TOP_DOWN.</li> 
 * <li><b>Web service implementation type</b>: extension to org.eclipse.jst.ws.consumption.ui.wsImpl.
 * The Java representation of this is org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceImpl.</li>
 * <li><b>Web service type</b>: Forward slash separated concatenation of the String representation
 * of a Web service scenario and a Web service implementation type id.
 * For example, "0/org.eclipse.jst.ws.wsImpl.java", represents the bottom up Java bean Web service type.</li>
 * <li><b>Web service client implementation type</b>: extension to org.eclipse.jst.ws.consumption.ui.wsClientImpl.
 * The Java representation of this is org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceClientImpl.</li>
 * <li><b>Web service runtime</b>: extension to org.eclipse.jst.ws.consumption.ui.runtimes. 
 * The Java representation of this is org.eclipse.jst.ws.internal.consumption.ui.wsrt.RuntimeDescriptor</li> 
 * <li><b>serviceRuntime</b>: extension to org.eclipse.jst.ws.consumption.ui.serviceRuntimes.
 * The Java representation of this is org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceRuntimeDescriptor</li>
 * <li><b>clientRuntime</b>: extension to org.eclipse.jst.ws.consumption.ui.clientRuntimes.
 * The Java representation of this is org.eclipse.jst.ws.internal.consumption.ui.wsrt.ClientRuntimeDescriptor</li>
 * <li><b>server type</b>: This is a server tools artifact. You see a list of these when creating a new server
 * in the tool.</li>
 * <li><b>server runtime</b>: This is a server tools artifact. You see a list of these if you go to Preferences, 
 * Server > Installed Runtimes in the tool. The Java representation of this is org.eclipse.wst.server.core.IRuntime</li>
 * <li><b>facet runtime</b>: The facet equivalent of a server runtime. The Java representation of this is 
 * org.eclipse.wst.common.project.facet.core.runtime.IRuntime. org.eclipse.jst.server.core.FacetUtil
 * provides methods to translate from a server runtime to a facet runtime and vice-versa.</li>
 * </ul>
 */
public class WebServiceRuntimeExtensionUtils2
{
  private static WebServiceRuntimeExtensionRegistry2 registry = WebServiceRuntimeExtensionRegistry2.getInstance();
  
  /**
   * Returns the RuntimeDescriptor representing the Web service runtime
   * with an id attribute equal to the provided id
   * @param id
   * @return RuntimeDescriptor representing the Web service runtime
   * with an id attribute equal to the provided id. Returns null
   * if such a RuntimeDescriptor cannot be found.
   */  
  public static RuntimeDescriptor getRuntimeById(String id)
  {
    Object result = registry.runtimes_.get(id);
    if (result!=null)
    {
      return (RuntimeDescriptor)result;
    }
    return null;        
  }
  
  /**
   * Returns the union of all project types supported by all serviceRuntimes. Used by the
   * ProjectTopology preference page. 
   * @return String[] array of template ids. The array may have 0 elements.
   */
  public static String[] getAllServiceProjectTypes()
  {
    ArrayList finalTemplateIdList = new ArrayList();
    Iterator iter = registry.serviceRuntimes_.values().iterator();

    //Loop through all the serviceRuntimes
    while (iter.hasNext())   
    {
      ServiceRuntimeDescriptor desc = (ServiceRuntimeDescriptor)iter.next();
      
      //Get the templates for this serviceRuntime
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
  
  /**
   * Returns the union of all project types supported by all clientRuntimes. Used by the
   * ProjectTopology preference page. 
   * @return String[] array of template ids. The array may have 0 elements.
   */
  public static String[] getAllClientProjectTypes()
  {
    ArrayList finalTemplateIdList = new ArrayList();
    Iterator iter = registry.clientRuntimes_.values().iterator();

    //Loop through all the clientRuntimes
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      
      //Get the templates for this clientRuntime
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
  
  /**
   * Returns the label of a Web service runtime given its id
   * @param runtimeId id of a Web service runtime
   * @return String the label of the Web service runtime with an id equal to runtimeId.
   * Returns null if such a Web service runtime cannot be found.
   */
  public static String getRuntimeLabelById(String runtimeId) 
  {
    RuntimeDescriptor desc = getRuntimeById(runtimeId);
    if (desc == null)
      return null;
    
    return desc.getLabel();
  }    
  
  /**
   * Returns the RuntimeDescriptor corresponding to the Web service runtime with the provided label
   * @param label label of a Web service runtime
   * @return RuntimeDescriptor corresponding to the Web service runtime with the provided label
   * Returns null if such a Web service runtime cannot be found.
   */
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

  /**
   * Returns the label of an server type given its id
   * @param serverFactoryId id of a server type
   * @return String label of the server type with an id equal to serverFactoryId.
   * Returns null if such a server type cannot be found.
   */
  public static String getServerLabelById(String serverFactoryId)
  {
    IServerType serverType = ServerCore.findServerType(serverFactoryId);
    if (serverType == null)
      return null;
    
    String serverLabel = ServerUICore.getLabelProvider().getText(serverType);   
    return serverLabel;
  }
  
  /**
   * Returns the label of an IServer given its id
   * @param instanceId id of an IServer
   * @return String label of the IServer with an id equal to instanceId.
   */  
  public static String getServerInstanceLabelFromInstanceId( String instanceId )
  {
    IServer server = ServerCore.findServer( instanceId );    
    return server.getName();
  }  

  /**
   * Returns the union of all server type ids corresponding to the facet runtimes
   * in the provided set.
   * @param facetRuntimes a set containing elements of type {@link IRuntime}.
   * @return an array of IServerType ids. The array may have 0 elements.
   */
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
        IRuntimeType runtimeType = serverType.getRuntimeType();
        if (runtimeType != null)
        {
	        String runtimeTypeId = runtimeType.getId();
	        if (sRuntime != null && sRuntime.getRuntimeType() != null && runtimeTypeId.equals(sRuntime.getRuntimeType().getId()))
	        {
	          supportedServerFactoryIds.add(serverTypeIds[i]);          
	        }      
        }
      }
    }
    
    return (String[])supportedServerFactoryIds.toArray(new String[0]);
    
    //Temporarily return all factory ids with runtimes
    //return getAllServerFactoryIdsWithRuntimes();    
  }
  
  //Service-side utilities
  /**
   * Returns a WebServiceImpl given the id of a Web service implementation type.
   * @param id 
   * @return WebServiceImpl with the given id
   * Returns null if such a WebServiceImpl cannot be found.
   */
  public static WebServiceImpl getWebServiceImplById(String id)
  {
    Object result = registry.webServiceImpls_.get(id);
    if (result!=null)
    {
      return (WebServiceImpl)result;
    }
    return null;    
  }
  
  /**
   * Returns a {@link ServiceRuntimeDescriptor} given the id of a servicRuntime.
   * @param id of a serviceRuntime
   * @return ServiceRuntimeDescriptor with the given id
   * Returns null if such a ServiceRuntimeDescriptor cannot be found.
   */
  public static ServiceRuntimeDescriptor getServiceRuntimeDescriptorById(String id)
  {
    Object result = registry.serviceRuntimes_.get(id);
    if (result!=null)
    {
      return (ServiceRuntimeDescriptor)result;
    }
    return null;        
  }  
  
  /**
   * Returns the {@link IWebServiceRuntime} of the given serviceRuntime. Extenders provide the fully 
   * qualified name of a concrete IWebServiceRuntime in the class attribute of a serviceRuntimes extension.
   * @param serviceRuntimeId id of a serviceRuntime
   * @return IWebServiceRuntime
   */
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
  
  /**
   * Returns the id of a serviceRuntime that supports the provided Web service type, Web service runtime,
   * server type/server instance (if present), project or project type.
   * @param trs an instance of {@link TypeRuntimeServer} containing a Web service type id, Web service runtime id,
   * server type id and server instance id. The Web service type id and Web service runtime id must be non-null 
   * and non-empty. The server type id and server instance id may be null or empty.
   * @param projectName the name of an IProject thay may or mat not exist in the workspace. Must be non-null and 
   * non-empty.
   * @param templateId the id of an {@link IFacetedProjectTemplate}. Must be non-null and non-empty if
   * an IProject with the name projectName does not exist in the workspace. 
   * @return String id of a serviceRuntime that supports the provided Web service type, Web service runtime,
   * server type/server instance (if present), project or project type. Returns an empty String if no 
   * such serviceRuntime could be found.
   */
  public static String getServiceRuntimeId(TypeRuntimeServer trs, String projectName, String templateId)
  {
    boolean serverSelected = (trs.getServerId() != null) && (trs.getServerId().length() > 0); 
    //Find the first serviceRuntime that supports the implementation type, runtime, server, and project
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
  
  /**
   * Returns the Web service scenario from the Web service type.
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * @return int scenario (e.g. WebServiceScenario.BOTTOM_UP or WebServiceScenario.TOP_DOWN)
   */        
  public static int getScenarioFromTypeId(String typeId)
  {
    return Integer.parseInt(typeId.substring(0,typeId.indexOf("/")));
  }
  
  /**
   * Returns the Web service implemenation type id fron the Web service type id.
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * @return String the WebServiceImpl id.
   */        
  public static String getWebServiceImplIdFromTypeId(String typeId)
  {
    return typeId.substring(typeId.indexOf("/")+1);
  }    
  
  /**
   * Returns the ids of all Web service runtimes that support the given
   * Web service type.
   * 
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * 
   * @return String[] array containing the ids of all Web service runtimes that
   * support the given Web service type. The array may have 0 elements.
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
  
  /**
   * Returns the ids of all serviceRuntimes that support the given
   * Web service type.
   * 
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * 
   * @returns String[] array containing the ids of all serviceRuntimes that
   * support the given Web service type. The array may have 0 elements.
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
  
  /**
   * Returns whether or not the given Web service runtime supports the given 
   * Web service type.
   * 
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * 
   * @param runtimeId id of a Web service runtime
   * 
   * @return boolean <code>true</code> if the given Web service runtime supports the given 
   * Web service type. <code>false</code> otherwise.
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
  
  /**
   * Returns the ids of all server types that support the given
   * Web service type.
   * 
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * 
   * @returns String[] array containing the ids of all server types that
   * support the given Web service type. The array may have 0 elements. 
   */
  public static String[] getServerFactoryIdsByServiceType(String typeId)
  {
    ArrayList serverFactoryIds = new ArrayList();
    String[] srts = getServiceRuntimesByServiceType(typeId);
    if (srts != null)
    {
      for (int i = 0; i < srts.length; i++)
      {
        //Get the runtimes that work for the facets required for this serviceRuntime        
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
  
  /**
   * Returns whether or not the given server type supports the given 
   * Web service type.
   * 
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * 
   * @param serverFactoryId id of a server type
   * 
   * @return boolean <code>true</code> if the given server type supports the given 
   * Web service type. <code>false</code> otherwise.
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
  
  /**
   * Returns the ids of all server types that support the given
   * serviceRuntime.
   * 
   * @param serviceRuntimeId id of a serviceRuntime
   * 
   * @returns String[] array containing the ids of all server types that
   * support the given serviceRuntime. The array may have 0 elements. 
   */   
  public static String[] getServerFactoryIdsByServiceRuntime(String serviceRuntimeId)
  {       
    ServiceRuntimeDescriptor desc = getServiceRuntimeDescriptorById(serviceRuntimeId);
    Set facetRuntimes = getRuntimes(desc.getRequiredFacetVersions());
            
    //Temporarily return all server types
    //return getAllServerFactoryIdsWithRuntimes();
    return getServerFactoryIdsByFacetRuntimes(facetRuntimes);
  } 
  

  
  /**
   * Returns whether or not the given server type supports the given 
   * serviceRuntime.
   * 
   * @param serviceRuntimeId id of a serviceRuntime
   * @param serverFactoryId id of a server type
   * 
   * @return boolean <code>true</code> if the given server type supports the given 
   * serviceRuntime. <code>false</code> otherwise.
   */  
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
    
  /**
   * Returns the labels and ids of all Web service types. Used to populate the Web service type combo box on
   * page 1 of the Web service wizard.
   * @return {@link LabelsAndIds}
   */
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
  
  /**
   * Returns the id of a Web service runtime that supports the given Web service type.
   *  
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * 
   * @return String id of a Web service runtime that supports the given Web service type. 
   * Returns null if such a Web service runtime cannot be found.
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

  /**
   * Returns the id of a server type that supports the given Web service type.
   *  
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * 
   * @return String id of a id of a server type that supports the given Web service type. 
   * Returns null if such a server type cannot be found.
   */    
  public static String getDefaultServerValueFor(String typeId)
  {
    String[] fIds = getServerFactoryIdsByServiceType(typeId);
    if (fIds==null || fIds.length==0)
      return null;
    
    return fIds[0];
  }    
  
  /**
   * Returns whether or not the given combination of server type, Web service runtime, and Web service type is
   * supported. Used for validation.
   * 
   * @param serverFactoryId id of a server type
   * @param runtimeId id of a Web service runtime
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * 
   * @return boolean <code>true</code> if the given combination of server type, Web service runtime, and Web service type is
   * supported. <code>false</code> otherwise.
   */
  public static boolean isServerRuntimeTypeSupported(String serverFactoryId, String runtimeId, String typeId)  
  {
    //Ensure there is at least one serviceRuntime that supports the given type
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
  
  /**
   * Returns an array of Web service type ids that are suitable for the provided selection.
   * Used to default the Web service type combo box on page 1 of the Web service wizard
   * based on the user's initial selection.
   * @param selection
   * @return String[] an array of Web service type ids that are suitable for the provided selection.
   * Returns null if the selection is empty or no suitable Web service types are found.
   */
  public static String[] getWebServiceTypeBySelection(IStructuredSelection selection)
  {
    TypeSelectionFilter2 tsf = new TypeSelectionFilter2();
    String[] wst = tsf.getWebServiceTypeByInitialSelection(selection, registry.webServiceTypesList_);
    return wst == null ? null : wst;
  }    
  
  /**
   * Returns whether or not the given server type supports the given Web service runtime
   * 
   * @param runtimeId : id of a Web service runtime
   * @param factoryId : id of a server type
   * @return boolean <code>true</code> if the given server type supports the given 
   * Web service runtime for the service side. <code>false</code> otherwise.
   */
  public static boolean doesRuntimeSupportServerForServiceSide(String runtimeId, String factoryId)
  {
    //Get all the ServiceRuntimeDescriptors that point to this runtimeId
    Iterator iter = registry.serviceRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ServiceRuntimeDescriptor desc = (ServiceRuntimeDescriptor)iter.next();
      //check if this serviceRuntime points to runtimeId
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
    
    //No serviceRuntime matched. Return false.
    return false;
  }
  
  /**
   * Returns the id of a server type that supports the given Web service runtime
   * on the service side.
   * 
   * @param runtimeId id of a Web service runtime
   * @return String the id of a server type that supports the given Web service runtime.
   * Returns null if such a server type cannot be found. 
   */
  public static String getFirstSupportedServerForServiceSide(String runtimeId)
  {
    //Get all the ServiceRuntimeDescriptors that point to this runtimeId
    Iterator iter = registry.serviceRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ServiceRuntimeDescriptor desc = (ServiceRuntimeDescriptor)iter.next();
      //check if this serviceRuntime points to runtimeId
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
  
  /**
   * Returns the names of all projects in the workspace which support the given Web service type
   * and Web service runtime.
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * @param runtimeId id of a Web service runtime
   * @return String[] array of project names. The array may have 0 elements.
   */
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
  
  /**
   * Returns whether or not the given project supports the given Web service type and Web service runtime.
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * @param runtimeId id of a Web service runtime
   * @param projectName name of an IProject in the workspace
   * @return boolean <code>true</code> if the project supports the given Web service type and 
   * Web service runtime. Returns <code>false</code>
   * <ul> 
   * <li>if the project does not support the given Web service type and Web service runtime or</li>
   * <li>if the project does not exist or</li>
   * <li>if projectName is null or empty</li>
   * </ul>
   */
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
  
  /**
   * Returns whether or not the given project supports the given serviceRuntime.
   * @param serviceRuntimeId id of a serviceRuntime
   * @param projectName name of an IProject in the workspace
   * @return boolean <code>true</code> if the project supports the given
   * serviceRuntime. Returns <code>false</code>
   * <ul> 
   * <li>if the project does not support the given serviceRuntime or</li>
   * <li>if the project does not exist or</li>
   * <li>if projectName is null or empty</li>
   * </ul>
   */  
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
  

  /**
   * Returns an array of {@link IFacetedProjectTemplate} ids that support the given Web service type
   * and Web service runtime.
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * @param runtimeId id of a Web service runtime
   * @return String[] array of {@link IFacetedProjectTemplate} ids that support the given Web service type
   * and Web service runtime. The array may have 0 elements. Returns null if no serviceRuntimes supporting
   * the given Web service type could be found.
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
        //Get the templates for this serviceRuntime
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

  /**
   * Returns whether or not the given {@link IFacetedProjectTemplate} supports the given Web service type
   * and Web service runtime.
   * @param typeId must be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the Web service implementation type id.
   * @param runtimeId id of a Web service runtime
   * @param templateId id of a {@link IFacetedProjectTemplate}
   * @return boolean <code>true</code> if the given {@link IFacetedProjectTemplate} supports the given 
   * Web service type and Web service runtime. Returns <code>false</code> otherwise. 
   */
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
        //Get the templates for this serviceRuntime
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
  
  /**
   * Returns whether or not the given {@link IFacetedProjectTemplate} supports the given serviceRuntime.
   * @param serviceRuntimeId id of a serviceRuntime
   * @param templateId id of a {@link IFacetedProjectTemplate}
   * @return boolean <code>true</code> if the given {@link IFacetedProjectTemplate} supports the given 
   * serviceRuntime. Returns <code>false</code> otherwise.
   */
  public static boolean doesServiceRuntimeSupportTemplate(String serviceRuntimeId, String templateId)
  {
    //Get the templates for this serviceRuntime
    Set templates = FacetMatchCache.getInstance().getTemplatesForServiceRuntime(serviceRuntimeId);
    IFacetedProjectTemplate checkingTemplate = ProjectFacetsManager.getTemplate(templateId);
    return templates.contains(checkingTemplate);
  }  
  
  //Client-side utilities
  /**
   * Returns a WebServiceClientImpl given the id of a Web service client implementation type.
   * @param id 
   * @return {@link WebServiceClientImpl} with the given id
   * Returns null if such a WebServiceClientImpl cannot be found.
   */  
  public static WebServiceClientImpl getWebServiceClientImplById(String id)
  {
    Object result = registry.webServiceClientImpls_.get(id);
    if (result!=null)
    {
      return (WebServiceClientImpl)result;
    }
    return null;    
  }
  
  /**
   * Returns a {@link ClientRuntimeDescriptor} given the id of a clientRuntime.
   * @param id of a clientRuntime
   * @return ClientRuntimeDescriptor with the given id
   * Returns null if such a ClientRuntimeDescriptor cannot be found.
   */  
  public static ClientRuntimeDescriptor getClientRuntimeDescriptorById(String id)
  {
    Object result = registry.clientRuntimes_.get(id);
    if (result!=null)
    {
      return (ClientRuntimeDescriptor)result;
    }
    return null;        
  }    
  
  /**
   * Returns the {@link IWebServiceRuntime} of the given clientRuntime. Extenders provide the fully 
   * qualified name of a concrete IWebServiceRuntime in the class attribute of a clientRuntimes extension.
   * @param clientRuntimeId id of a clientRuntime
   * @return IWebServiceRuntime
   */  
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
  
  /**
   * Returns the id of a clientRuntime that supports the provided Web service client implementation type, Web service runtime,
   * server type/server instance (if present), project or project type.
   * @param trs an instance of {@link TypeRuntimeServer} containing a Web service client implementation type id, Web service runtime id,
   * server type id and server instance id. The Web service client implementation type id and Web service runtime id must be non-null 
   * and non-empty. The server type id and server instance id may be null or empty.
   * @param projectName the name of an IProject thay may or mat not exist in the workspace. Must be non-null and 
   * non-empty.
   * @param templateId the id of an {@link IFacetedProjectTemplate}. Must be non-null and non-empty if
   * an IProject with the name projectName does not exist in the workspace. 
   * @return String id of a clientRuntime that supports the provided Web service client implementation type, Web service runtime,
   * server type/server instance (if present), project or project type. Returns an empty String if no 
   * such clientRuntime could be found.
   */  
  public static String getClientRuntimeId(TypeRuntimeServer trs, String projectName, String templateId)
  {
    boolean serverSelected = (trs.getServerId()!=null) && (trs.getServerId().length()>0);
    //Find the first clientRuntime that supports the implementation type, runtime, server, and project
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
  
  /**
   * Returns all Web service runtime ids for the client side.
   * @return String[] array of Web service runtime ids for the client side.
   * The array may have 0 elements. 
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
  
  /**
   * Returns the ids of all clientRuntimes that support the given
   * Web service client implementation type.
   * @param clientImplId id of a Web service client implementation type
   * @returns String[] array containing the ids of all clientRuntimes that
   * support the given Web service client implementation type. The array may have 0 elements.
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
  
  /**
   * Returns the ids of all Web service runtimes that support the given
   * Web service client implementation type.
   * @param clientImplId id of a Web service client implementation type
   * @return String[] array containing the ids of all Web service runtimes that
   * support the given Web service client implementation type. The array may have 0 elements.
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
  
  /**
   * Returns whether or not the given Web service runtime supports the given 
   * Web service client implementation type.
   * @param clientImplId id of a Web service client implementation type
   * @param runtimeId id of a Web service runtime
   * @return boolean <code>true</code> if the given Web service runtime supports the given 
   * Web service client implementation type. <code>false</code> otherwise.
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
  
  /**
   * Returns all server type ids with configured server runtimes.
   * @return String[] array of server type ids. The array may have 0 elements.
   */
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
  
  /**
   * Returns the ids of all server types that support the given
   * Web service client implementation type.
   *  
   * @param clientImplId id of a Web service client implementation type
   * @return String[] array containing the ids of all server types that
   * support the given Web service client implementation type. The array may have 0 elements.
   */
  public static String[] getServerFactoryIdsByClientType(String clientImplId)
  {
    ArrayList serverFactoryIds = new ArrayList();
    String[] crts = getClientRuntimesByType(clientImplId);
    if (crts != null)
    {
      for (int i = 0; i < crts.length; i++)
      {
        //Get the runtimes that work for the facets required for this serviceRuntime        
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
  
  /**
   * Returns whether or not the given server type supports the given 
   * Web service client implementation type.
   * @param clientImplId id of a Web service client implementation type
   * @param serverFactoryId id of a server type
   * @return <code>true</code> if the given server type supports the given 
   * Web service client implementation type. <code>false</code> otherwise.
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
  
  /**
   * Returns the ids of all server types that support the given
   * clientRuntime.
   * 
   * @param clientRuntimeId id of a clientRuntime
   * @return String[] array containing the ids of all server types that
   * support the given clientRuntime. The array may have 0 elements.
   */
  public static String[] getServerFactoryIdsByClientRuntime(String clientRuntimeId)
  {       
    ClientRuntimeDescriptor desc = getClientRuntimeDescriptorById(clientRuntimeId);
    Set facetRuntimes = getRuntimes(desc.getRequiredFacetVersions());

    String[] fIds = getServerFactoryIdsByFacetRuntimes(facetRuntimes);
    return fIds;    
  }    

  /**
   * Returns whether or not the given server type supports the given 
   * clientRuntime.
   * 
   * @param clientRuntimeId id of a clientRuntime
   * @param serverFactoryId id of a server type
   * @return boolean <code>true</code> if the given server type supports the given 
   * clientRuntime. <code>false</code> otherwise.
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
  
  /**
   * Returns whether or not the given server type supports the given Web service runtime
   * 
   * @param runtimeId id of a Web service runtime
   * @param factoryId id of a server type
   * @return boolean <code>true</code> if the given server type supports the given 
   * Web service runtime for the client side. <code>false</code> otherwise.
   */
  public static boolean doesRuntimeSupportServerForClientSide(String runtimeId, String factoryId)
  {
    //Get all the ClientRuntimeDescriptors that point to this runtimeId
    Iterator iter = registry.clientRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      //check if this clientRuntime points to runtimeId
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
  
  /**
   * Returns the id of a server type that supports the given Web service runtime
   * on the client side.
   * 
   * @param runtimeId id of a Web service runtime
   * @return String the id of a server type that supports the given Web service runtime
   * on the client side. Returns null if such a server type cannot be found.
   */
  public static String getFirstSupportedServerForClientSide(String runtimeId)
  {
    //Get all the ClientRuntimeDescriptors that point to this runtimeId
    Iterator iter = registry.clientRuntimes_.values().iterator();
    while (iter.hasNext())   
    {
      ClientRuntimeDescriptor desc = (ClientRuntimeDescriptor)iter.next();
      //check if this clientRuntime points to runtimeId
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
  

  /**
   * Returns whether or not the given combination of server type, Web service runtime, and 
   * Web service client implementation type is supported. Used for validation.
   * 
   * @param serverFactoryId id of a server type
   * @param runtimeId id of a Web service runtime
   * @param clientImplId id of a Web service client implementation type
   * @return boolean <code>true</code> if the given combination of server type, Web service runtime, 
   * and Web service client implementation type is supported. <code>false</code> otherwise.
   */
  public static boolean isServerClientRuntimeTypeSupported(String serverFactoryId, String runtimeId, String clientImplId)  
  {
    //Ensure there is at least one clientRuntime that supports the given type
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
  
  /**
   * Returns an array of {@link IFacetedProjectTemplate} ids that support the given Web service client implementation type
   * and Web service runtime.
   * 
   * @param clientImplId id of a Web service client implementation type
   * @param runtimeId id of a Web service runtime
   * @return String[] array of {@link IFacetedProjectTemplate} ids that support the given Web service client implementation type
   * and Web service runtime. The array may have 0 elements. Returns null if no clientRuntimes supporting
   * the given Web service client implementation type could be found.
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
        //Get the templates for this clientRuntime
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
  
  /**
   * Returns whether or not the given {@link IFacetedProjectTemplate} supports the given 
   * Web service client implementation type and Web service runtime.
   * 
   * @param clientImplId id of a Web service client implementation type
   * @param runtimeId id of a Web service runtime
   * @param templateId id of a {@link IFacetedProjectTemplate}
   * @return boolean <code>true</code> if the given {@link IFacetedProjectTemplate} supports the given 
   * Web service client implementation type and Web service runtime. Returns <code>false</code> otherwise. 
   */
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
        //Get the templates for this clientRuntime
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
  

  /**
   * Returns whether or not the given {@link IFacetedProjectTemplate} 
   * supports the given clientRuntime
   *  
   * @param clientRuntimeId id of a clientRuntime
   * @param templateId id of a {@link IFacetedProjectTemplate}
   * @return boolean <code>true</code> if the given {@link IFacetedProjectTemplate} supports the given 
   * clientRuntime. Returns <code>false</code> otherwise.
   */
  public static boolean doesClientRuntimeSupportTemplate(String clientRuntimeId, String templateId)
  {
    //Get the templates for this clientRuntime
    Set templates = FacetMatchCache.getInstance().getTemplatesForClientRuntime(clientRuntimeId);
    IFacetedProjectTemplate checkingTemplate = ProjectFacetsManager.getTemplate(templateId);
    return templates.contains(checkingTemplate);
  }
  
  /**
   * Returns the labels and ids of all Web service client implementation types. 
   * Used to populate the Web service client type combo box on
   * page 1 of the Web service wizard and Web service client wizard.
   * @return {@link LabelsAndIds}
   */  
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
  
  /**
   * Returns all server type ids for which there are configured server runtimes
   * present in the workspace.
   * 
   * @return String[] array of server type ids for which there are configured server runtimes
   * present in the workspace. The array may have 0 elements.
   */
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
    	if (st == null || st.getRuntimeType() == null) break;    	  
        org.eclipse.wst.server.core.IRuntime rt = rts[j];
        // If the server type has the same runtime type as this runtime, add it to the list
        String serverTypeRuntimeTypeId = st.getRuntimeType().getId();
        
        if (rt != null)
        {
        	IRuntimeType rtType = rt.getRuntimeType();
        	if (rtType != null)
        	{
		        String runtimeRuntimeTypeId = rtType.getId();
		        if (serverTypeRuntimeTypeId.equals(runtimeRuntimeTypeId))
		        {
		          if (!fids.contains(st.getId()))
		          {
		            fids.add(st.getId());
		          }
		        }
        	}
        }
      }
    }
   
    return (String[])fids.toArray(new String[0]);
  }
  
  /**
   * Returns the names of all projects in the workspace which support the given Web service client 
   * implementation type and Web service runtime.
   * 
   * @param typeId id of a Web service client implementation type
   * @param runtimeId id of a Web service runtime
   * @return String[] array of project names. The array may have 0 elements.
   */
  public static String[] getProjectsForClientTypeAndRuntime(String typeId, String runtimeId)
  {
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
  
  /**
   * Returns whether or not the given project supports the given Web service client implementation type 
   * and Web service runtime.
   * 
   * @param typeId id of a Web service client implementation type
   * @param runtimeId id of a Web service runtime
   * @param projectName name of an IProject in the workspace
   * @return boolean <code>true</code> if the project supports the given Web service type and 
   * Web service runtime. Returns <code>false</code>
   * <ul> 
   * <li>if the project does not support the given Web service client implementation type and 
   * Web service runtime or</li>
   * <li>if the project does not exist or</li>
   * <li>if projectName is null or empty</li>
   * </ul>
   */
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
  
  
  /**
   * Returns whether or not the given project supports the given clientRuntime.
   * @param clientRuntimeId id of a clientRuntime
   * @param projectName name of an IProject in the workspace
   * @return boolean <code>true</code> if the project supports the given
   * clientRuntime. Returns <code>false</code>
   * <ul> 
   * <li>if the project does not support the given clientRuntime or</li>
   * <li>if the project does not exist or</li>
   * <li>if projectName is null or empty</li>
   * </ul>
   */
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
  
  /**
   * Returns the {@link SelectionListChoices} data structure representing the 
   * server type > Web service runtime > J2EE cascading lists .
   * {@link SelectionListChoices} is a data structure  which is like a cascading
   * set of lists useful for populating a group of combo boxes in which the 
   * selection in a given combo box determines the list of items used to populate
   * the combo box below it. The Server Runtime preference page contains two combo
   * boxes: one for server types and, below it, one for Web service runtimes.
   * The server type combo box is meant to contain the list of all available server types
   * that support at least one Web service runtime on either the client or service side. 
   * The Web service runtime combo box is meant to contain the subset of Web service runtimes
   * supported by the server type selected in the server type combo box.
   * The SelectionListChoices returned by this method has three lists: server types, Web service runtimes,
   * and J2EE levels. The first two lists correspond to the combo boxes on the preference
   * page and are used to populate them. The J2EE levels list is a historical remnant from
   * when J2EE levels used to be displayed on the preference page. It remains in this 
   * data structure with some hard-coded values but is not rendered to the user. 
   * 
   * @return SelectionListChoices representing the server type > Web service runtime > J2EE cascading lists
   */
  public static SelectionListChoices getServerToRuntimeToJ2EE()
  {
    if (serverToRuntimeToJ2EE_!=null)
    {
      return serverToRuntimeToJ2EE_;
    }
    
    //TODO (see bug 116025): Instead of all server type ids, we should be calculating the set of server
    //types that support at least one Web service runtime on either the client or service side.
    //Getting all server types for now.
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
	//TODO (see bug 116025): Instead of all Web service runtimes, we should be 
	//calculating the subset of Web service runtimes supported by the server type
	//with id equal to serverFactoryId. Getting all the Web service runtimes for now.
    Set runtimes = registry.runtimes_.keySet();
    Iterator itr = registry.runtimes_.keySet().iterator();
    String[] runtimeIds = new String[runtimes.size()];    
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
    //J2EE levels have been removed from the Server Runtime preference page.
    //Return some hard coded values. These will not be rendered on the preference page.
    String[] j2eeVersions = new String[]{"13", "14"};
    SelectionList j2eeVersionsList = new SelectionList(j2eeVersions, 0);
    return new SelectionListChoices(j2eeVersionsList, null);        
  }  
  
  private static String[] getAllServerFactoryIds()
  {
    //TODO (see bug 116025): Instead of all server type ids, we should be calculating the set of server
	//types that support at least one Web service runtime on either the client or service side.
	//Return all server type ids for now.
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
    
  /**
   * Returns a server type id given the server type's label
   * @param label server type label
   * @return server type id or null if no server type with the given 
   * label is found.
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
  
  /**
   * Returns a Web service runtime's id given its label 
   * @param label Web service runtime label
   * @return Web service runtime id or null if no
   * Web service runtime with the given label is found.
   */
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
  
    
  /**
   * Returns a set of facet runtimes that support the given
   * required facet versions.
   * @param requiredFacetVersions an array containing elements of type {@link RequiredFacetVersion}
   * @return Set set of facet runtimes that support the given required facet versions.
   * (element type: {@link IRuntime}) 
   */
  private static Set getRuntimes(RequiredFacetVersion[] requiredFacetVersions)
  {
    return FacetUtils.getRuntimes(requiredFacetVersions);
  }
}

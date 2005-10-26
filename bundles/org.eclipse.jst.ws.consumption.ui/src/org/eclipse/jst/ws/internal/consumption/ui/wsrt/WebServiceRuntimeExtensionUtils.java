package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.TypeSelectionFilter;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUICore;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class WebServiceRuntimeExtensionUtils
{
  private static WebServiceRuntimeExtensionRegistry registry = WebServiceRuntimeExtensionRegistry.getInstance();
  private static SelectionListChoices serverToRuntimeToJ2EE_;
  private static Hashtable serverFactoryIdByLabel_;
  private static Hashtable runtimeIdByLabel_;
  
  public static IWebServiceRuntime getWebServiceRuntime( String runtimeId )
  {
	WebServiceRuntimeInfo wsrtInfo       		= getWebServiceRuntimeById(runtimeId);
	IWebServiceRuntime    webserviceRuntime     = null;
	if (wsrtInfo != null)
	{
		webserviceRuntime = wsrtInfo.getWebServiceRuntime();
	}
	
	return webserviceRuntime;
  }  
  
  public static WebServiceImpl getWebServiceImplById(String id)
  {
    Object result = registry.webServiceImpls_.get(id);
    if (result!=null)
    {
      return (WebServiceImpl)result;
    }
    return null;    
  }
  
  public static int getScenarioFromTypeId(String typeId)
  {
    return Integer.parseInt(typeId.substring(0,typeId.indexOf("/")));
  }
  
  public static String getImplIdFromTypeId(String typeId)
  {
    return typeId.substring(typeId.indexOf("/")+1);
  }
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */  
  public static ServiceType getServiceType(String runtimeId, String typeId)
  {
    WebServiceRuntimeInfo wsrt = getWebServiceRuntimeById(runtimeId);
    if (wsrt!=null)
    {
      int scenario = getScenarioFromTypeId(typeId);
      String implId = getImplIdFromTypeId(typeId);
      //Return the first service type that supports the impl and scenario
      ServiceType[] sts = wsrt.getServiceTypes();
      if (sts != null)
      {
        for (int i = 0; i < sts.length; i++)
        {
          String thisImplId = sts[i].getWebServiceImpl().getId();
          if (implId.equals(thisImplId))
          {
            // Check if scenario is supported
            if (sts[i].supportsScenario(scenario))
            {
              return sts[i];
            }
          }
        }
      }
    }
    
    return null;
  }
  
  public static WebServiceRuntimeInfo getWebServiceRuntimeById(String id)
  {
    Object result = registry.webServiceRuntimes_.get(id);
    if (result!=null)
    {
      return (WebServiceRuntimeInfo)result;
    }
    return null;
  }  
  
  public static WebServiceRuntimeInfo getWebServiceRuntimeByLabel(String label)
  {
    Iterator iter = registry.webServiceRuntimes_.values().iterator();
    while (iter.hasNext())
    {
      WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo)iter.next();
      if (wsr!=null) {
        if (label.equals(wsr.getLabel()))
          return wsr;
      }
    }
    return null;      
  }  

  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */
  public static String[] getRuntimesByType(String typeId) 
  {
    int scenario = getScenarioFromTypeId(typeId);
    String implId = getImplIdFromTypeId(typeId);    
    ArrayList ids = new ArrayList();
    Iterator iter = registry.webServiceRuntimes_.values().iterator();
    while (iter.hasNext())
    {
      WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo)iter.next();
      ServiceType[] sts = wsr.getServiceTypes();
      if (sts != null)
      {
        for (int i = 0; i < sts.length; i++)
        {
          String thisImplId = sts[i].getWebServiceImpl().getId();
          if (implId.equals(thisImplId))
          {
            // Check if scenario is supported
            if (sts[i].supportsScenario(scenario))
            {
              ids.add(wsr.getId());
              break;
            }
          }
        }
      }
    }
    
    if (ids.size() > 0)
    {
      String[] runtimeIds = (String[])ids.toArray(new String[0]);
      return runtimeIds;
    }
    
    return null;
  }
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */  
  public static boolean isRuntimeSupportedForType(String typeId, String runtimeId)
  {
    String[] runtimeIds = getRuntimesByType(typeId);
    if (runtimeIds!=null)
    {
      for (int i=0; i < runtimeIds.length; i++)
      {
        if (runtimeIds[i].equals(runtimeId))
        {
          return true;
        }
      }
    }
    
    return false;
  }
  
  public static String[] getServerFactoryIdsByType(String typeId) 
  {
    String[] wsrts = getRuntimesByType(typeId);
    if (wsrts == null )
    {
      return null;
    }
      
    ArrayList ids = new ArrayList();
    for (int i=0; i<wsrts.length ;i++)
    {
      String[] fIds = getWebServiceRuntimeById(wsrts[i]).getServerFactoryIds();
      if (fIds != null)
      {
        for (int j=0; j<fIds.length; j++)
        {
          if (!ids.contains(fIds[j]))
          {
            ids.add(fIds[j]);
          }
        }
      }
    }
    
    if (ids.size() > 0)
    {
      String[] serverFactoryIds = (String[])ids.toArray(new String[0]);
      return serverFactoryIds;
    }
    
    return null;    
  }
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */
  public static boolean isServerSupportedForChosenType(String typeId, String serverFactoryId)
  {
    String[] fIds = getServerFactoryIdsByType(typeId);
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
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */    
  public static boolean isServerRuntimeTypeSupported(String serverFactoryId, String runtimeId, String typeId)  
  {
    String[] rIds = getRuntimesByType(typeId);
    if (rIds == null)
    {
      return false;
    }
    
    for (int i = 0; i < rIds.length; i++)
    {
      if (runtimeId.equals(rIds[i]))
      {
        String[] fIds = getWebServiceRuntimeById(rIds[i]).getServerFactoryIds();
        if (fIds != null)
        {
          for (int j = 0; j < fIds.length; j++)
          {
            if (serverFactoryId.equals(fIds[j]))
            {
              // Found it!
              return true;
            }
          }
        }
      }
    }  
    
    return false;
  }
  
  public static boolean doesRuntimeSupportJ2EELevel(String j2eeVersionId, String runtimeId)
  {
    WebServiceRuntimeInfo wsrt = getWebServiceRuntimeById(runtimeId);
    if (wsrt==null)
      return false;
    
    String[] j2eeVersions = wsrt.getJ2eeLevels();
    if (j2eeVersions==null || j2eeVersions.length==0) 
      return true;
    
    for(int i=0; i<j2eeVersions.length; i++)
    {
      if (j2eeVersions[i].equals(j2eeVersionId))
      {
        return true;
      }
    }
    return false;        
  }
  
  public static boolean doesRuntimeSupportServerTarget(String serverTargetId, String runtimeId)
  {
    WebServiceRuntimeInfo wsrt = getWebServiceRuntimeById(runtimeId);
    if (wsrt==null)
      return false;
    
    String[] fIds = wsrt.getServerFactoryIds();
    for(int i=0;i<fIds.length;i++)
    {
      IServerType serverType = ServerCore.findServerType(fIds[i]);
      if (serverType == null)
        continue;
      
      String thisServerTargetId = serverType.getRuntimeType().getId(); 
      if (thisServerTargetId.equals(serverTargetId))
        return true;
    }
    
    return false;
  }

  /**
   * Returns a list of valid projects for the Web service type with an id of typeId.
   * In the case where the Web service type extension does not specify which project
   * natures are included, a array of all Web and EJB projects in the workspace will
   * be returned.
   * @param typeId
   * @return IProject[] an array of valid projects
   */
  public static IProject[] getAllProjects()
  {
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    ArrayList validProjects = new ArrayList();
    for (int i = 0; i < projects.length; i++)
    {
      if (!projects[i].getName().equals("Servers") && !projects[i].getName().startsWith("."))
      {
        validProjects.add(projects[i]);
      }
    }
    
    return (IProject[])validProjects.toArray(new IProject[0]); 
    // rsktodo
    /*
    IWebServiceType wst = getWebServiceTypeById(typeId);
    if (wst != null)
    {
      IConfigurationElement elem = wst.getConfigurationElement();
      String includedNatures = elem.getAttribute("includeNatures");
      String excludedNatures = elem.getAttribute("excludeNatures");
      if (includedNatures!=null && includedNatures.length()>0)
      {
        for (int i = 0; i < projects.length; i++)
        {
          if (include(projects[i], includedNatures) && exclude(projects[i], excludedNatures))
            validProjects.add(projects[i]);
        }
        return (IProject[])validProjects.toArray(new IProject[0]);
      }
    }
    
    //If wst was null or the extension didn't specify which project natures are
    // to be included, revert to the old behaviour
    for (int j = 0; j < projects.length; j++)
    {
      if (ResourceUtils.isWebProject(projects[j]) || ResourceUtils.isEJBProject(projects[j]))
        validProjects.add(projects[j]);
    }    
    return (IProject[])validProjects.toArray(new IProject[0]);
    */
    // rsktodo
  }
  
  public static LabelsAndIds getServiceTypeLabels()
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId + ".plugin", registry );
    
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
      String implId = getImplIdFromTypeId(wst);
      WebServiceImpl wsimpl = WebServiceRuntimeExtensionUtils.getWebServiceImplById(implId);
      String impllabel = wsimpl.getLabel();
      ids[index]    = wst;
      String scenLabel = "";
      switch(scenario)
      {
      case WebServiceScenario.BOTTOMUP:
        scenLabel = msgUtils.getMessage(WebServiceScenario.BOTTOMUP_LABEL);
        break;
      case WebServiceScenario.TOPDOWN:
        scenLabel = msgUtils.getMessage(WebServiceScenario.TOPDOWN_LABEL);
        break; 
      default:
      }
      labels[index] = msgUtils.getMessage( "COMBINED_TYPE_AND_RUNTIME_LABEL", new String[]{ scenLabel, impllabel } );
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
    String[] rIds = getRuntimesByType(typeId);
    if (rIds == null)
    {
      return null;
    }
    
    return rIds[0];
  }    
  
  
  public static String getDefaultServerValueFor(String typeId)
  {
    String[] fIds = getServerFactoryIdsByType(typeId);
    if (fIds==null)
      return null;
    
    return fIds[0];
  }  
  
  public static String[] getWebServiceTypeBySelection(IStructuredSelection selection)
  {
    TypeSelectionFilter tsf = new TypeSelectionFilter();
    String[] wst = tsf.getWebServiceTypeByInitialSelection(selection, registry.webServiceTypesList_);
    return wst == null ? null : wst;
  }    
  /**
   * Returns true if an EJB project is needed to host a Web service
   * of the type identified by typeId. If multiple natureIds are specified in the 
   * "includeNatures" attribute, only the first one is checked. If no natureIds are specified, 
   * false is returned.
   * 
   * @deprecated  
   * 	Should be refactored in future 
   */
//  public static boolean requiresEJBProject(String typeId)
//  {
//    IWebServiceType wst = getWebServiceTypeById(typeId);
//    if (wst != null)
//    {
//      IConfigurationElement elem = wst.getConfigurationElement();
//      String includedNatures = elem.getAttribute("includeNatures");
//      if (includedNatures==null || includedNatures.length()==0)
//      {
//        return false;
//      }
//      else
//      {
//        StringTokenizer st = new StringTokenizer(includedNatures);
//        if(st.hasMoreTokens())
//        {
//          String firstIncludedNature = st.nextToken();
//          if (firstIncludedNature.equals(IEJBNatureConstants.NATURE_ID))
//            return true;
//        }
//      }
//    }
//    return false;
//  }  
  
  /*
   * @param typeId will be a String of the format "0/implId"
   * where the digit before the "/" represents the scenario
   * (e.g. WebServiceScenario.BOTTOM_UP) and the implId is the id
   * of the WebServiceImpl
   */
  public static boolean requiresEJBProject(String runtimeId, String typeId)
  {
    ServiceType st = getServiceType(runtimeId, typeId);
    if (st!=null)
    {
      String[] includedNatures = st.getModuleTypesInclude(getScenarioFromTypeId(typeId));
      if (includedNatures!=null && includedNatures.length>0)
      {
        if (includedNatures[0].equals(IModuleConstants.JST_EJB_MODULE))
        {
          return true;
        }
      }
    }
    
    return false;
  }  
  
  public static boolean requiresEJBModuleFor(String serverFactoryId, String runtimeId, String typeId)
  {
	  // rsktodo
	  return false;
  }  
  
  //Ported from the former WebServiceClientRegistry
  
  public static String getRuntimeLabelById(String runtimeId) 
  {
    WebServiceRuntimeInfo wsrt = getWebServiceRuntimeById(runtimeId);
    if (wsrt == null)
      return null;
    
    return wsrt.getLabel();
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
  
  public static boolean doesRuntimeSupportServer(String runtimeId, String factoryId)
  {
    WebServiceRuntimeInfo wsrt = getWebServiceRuntimeById(runtimeId);
    if (wsrt != null)
    {
      String[] fids = wsrt.getServerFactoryIds();
      if (fids!=null)
      {
        for (int i=0; i<fids.length; i++)
        {
          if (fids[i].equals(factoryId))
          {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public static String getFirstSupportedServer(String runtimeId)
  {
    WebServiceRuntimeInfo wsrt = getWebServiceRuntimeById(runtimeId);
    
    if (wsrt != null)
    {
      String[] fids = wsrt.getServerFactoryIds();
      if (fids!=null && fids.length>0)
      {
        return fids[0];
      }
    }
    
    return null;    
  }


  public static String[] getRuntimesByClientType(String clientImplId) 
  {
    ArrayList ids = new ArrayList();
    Iterator iter = registry.webServiceRuntimes_.values().iterator();
    while (iter.hasNext())
    {
      WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo)iter.next();
      ClientType[] cts = wsr.getClientTypes();
      if (cts != null)
      {
        for (int i = 0; i < cts.length; i++)
        {
          String wsClientImplId = cts[i].getWebServiceClientImpl().getId();
          if (wsClientImplId.equals(clientImplId))
          {
            ids.add(wsr.getId());
            break;
          }
        }
      }
    }
    
    if (ids.size() > 0)
    {
      String[] runtimeIds = (String[])ids.toArray(new String[0]);
      return runtimeIds;
    }
    
    return null;    
  }
  

  
  public static boolean webServiceClientRuntimeTypeExists(String serverFactoryId, String runtimeId, String clientTypeId) 
  {
    String[] rIds = getRuntimesByClientType(clientTypeId);
    if (rIds == null)
    {
      return false;
    }
    
    for (int i = 0; i < rIds.length; i++)
    {
      if (runtimeId.equals(rIds[i]))
      {
        String[] fIds = getWebServiceRuntimeById(rIds[i]).getServerFactoryIds();
        if (fIds != null)
        {
          for (int j = 0; j < fIds.length; j++)
          {
            if (serverFactoryId.equals(fIds[j]))
            {
              // Found it!
              return true;
            }
          }
        }
      }
    }  
    
    return false;
  }
  
  public static String[] getAllClientRuntimes() 
  {
    ArrayList ids = new ArrayList();
    Iterator iter = registry.webServiceRuntimes_.values().iterator();
    while (iter.hasNext())
    {
      WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo)iter.next();
      ClientType[] cts = wsr.getClientTypes();
      if (cts!=null && cts.length>0)
      {
        //this runtime supports the client scenario
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
  

  public static String[] getAllClientServerFactoryIds() 
  {    
    String[] rIds = getAllClientRuntimes();
    if (rIds==null)
      return null;
    
    ArrayList ids = new ArrayList();
    for (int i=0; i<rIds.length ;i++)
    {
      String[] fIds = getWebServiceRuntimeById(rIds[i]).getServerFactoryIds();
      if (fIds != null)
      {
        for (int j=0; j<fIds.length; j++)
        {
          if (!ids.contains(fIds[j]))
          {
            ids.add(fIds[j]);
          }
        }
      }
    }
    
    if (ids.size() > 0)
    {
      String[] serverFactoryIds = (String[])ids.toArray(new String[0]);
      return serverFactoryIds;
    }
    
    return null;
  }
  

  
  public static String[] getClientProjectTypes(String clientImplId, String runtimeId)
  {
    String[] rIds = getRuntimesByClientType(clientImplId);
    if (rIds == null)
    {
      return null;
    }
    
    for (int i = 0; i < rIds.length; i++)
    {
      if (runtimeId.equals(rIds[i]))
      {
        ClientType[] cts = getWebServiceRuntimeById(rIds[i]).getClientTypes();
        if (cts != null)
        {
          for (int j = 0; j < cts.length; j++)
          {
            String thisClientImplId = cts[j].getWebServiceClientImpl().getId();
            if (clientImplId.equals(thisClientImplId))
            {
              // Found the one!
              String[] projectTypes = cts[j].getModuleTypesInclude();
              return projectTypes;
            }
          }
        }
      }
    }  
    
    return null;    
  }
  
  public static boolean doesRuntimeSupportComponentType(String clientImplId, String runtimeId, String componentTypeId)
  {
    String[] compTypeIds = getClientProjectTypes(clientImplId, runtimeId);
    if (compTypeIds!=null)
    {
      for (int i=0; i<compTypeIds.length; i++)
      {
       if (compTypeIds[i].equals(componentTypeId))
       {
         return true;
       }
      }
    }
    
    return false;
  }
  
  public static LabelsAndIds getClientTypeLabels()
  {
    
    LabelsAndIds labelIds = new LabelsAndIds();
    String[] idsArray = new String[0];
    String[] labelsArray = new String[0];
    labelIds.setIds_(idsArray);
    labelIds.setLabels_(labelsArray);
  
    String[] rIds = getAllClientRuntimes();
    if (rIds == null)
    {
      return labelIds;
    }
  
    ArrayList ids = new ArrayList();
    ArrayList labels = new ArrayList();
  
    for (int i=0; i < rIds.length; i++)
    {
      WebServiceRuntimeInfo wsrt = getWebServiceRuntimeById(rIds[i]);
      ClientType[] cts = wsrt.getClientTypes();
      if (cts != null)
      {
        for (int j = 0; j < cts.length; j++)
        {
          ClientType ct = cts[j];
          // String id = ct.getWebServiceClientTypeId();
          String id = ct.getWebServiceClientImpl().getId();
          if (!ids.contains(id))
          {
            ids.add(id);
            labels.add(ct.getWebServiceClientImpl().getLabel());
          }
        }
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
  
  //Utilities used by the ServerRuntimePreferencePage
  
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
    
    String[] runtimes = getRuntimeIDsByServerFactoryID(serverFactoryId);
    SelectionList runtimesList = new SelectionList(runtimes, 0);
    Vector choices = new Vector();
    for (int i=0; i<runtimes.length; i++)
    {
      choices.add(getJ2EEChoices(runtimes[i]));
    }
    return new SelectionListChoices(runtimesList, choices);    
  }  
  
  private static SelectionListChoices getJ2EEChoices(String runtimeId)
  {
    //String[] serviceJ2EEVersions = getWebServiceRuntimeById(runtimeId).getJ2EEVersions();
    //String[] clientJ2EEVersions = WebServiceClientTypeRegistry.getInstance().getWebServiceRuntimeById(runtimeId).getJ2EEVersions();
    //String[] j2eeVersions = getStringArrayIntersection(serviceJ2EEVersions, clientJ2EEVersions);
    WebServiceRuntimeInfo wsr = getWebServiceRuntimeById(runtimeId);
    if (wsr==null)
    {
      return null;
    }
    
    String[] j2eeVersions = wsr.getJ2eeLevels();
    SelectionList j2eeVersionsList = new SelectionList(j2eeVersions, 0);
    return new SelectionListChoices(j2eeVersionsList, null);
  }  
  
  private static String[] getAllServerFactoryIds()
  {
    ArrayList ids = new ArrayList();
    if (serverFactoryIdByLabel_ == null)
    {
      serverFactoryIdByLabel_ = new Hashtable();
      Iterator iter = registry.webServiceRuntimes_.values().iterator();
      while (iter.hasNext())
      {
        WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo) iter.next();
        String[] sfids = wsr.getServerFactoryIds();
        for (int i = 0; i < sfids.length; i++)
        {
          if (!ids.contains(sfids[i]))
          {
            String label = getServerLabelById(sfids[i]);
            if (label != null)
            {
              ids.add(sfids[i]);
              serverFactoryIdByLabel_.put(label, sfids[i]);
            }
          }
        }
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
      Iterator iter = registry.webServiceRuntimes_.values().iterator();
      while (iter.hasNext())
      {
        WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo)iter.next();
        runtimeIdByLabel_.put(wsr.getLabel(), wsr.getId());        
      }      
    }
    
    return (String)runtimeIdByLabel_.get(label);    
  }    
}

package org.eclipse.jst.ws.internal.wsrt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.project.IEJBNatureConstants;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.IWebServiceType;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.TypeSelectionFilter;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceTypeImpl;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUICore;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceRuntime;

public class WebServiceRuntimeExtensionUtils
{
  private static WebServiceRuntimeExtensionRegistry registry = WebServiceRuntimeExtensionRegistry.getInstance();
  
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
  
  public static IWebServiceType getWebServiceTypeById(String id)
  {
    Object result = registry.webServiceTypes_.get(id);
	
    if (result!=null)
    {
      return (WebServiceTypeImpl)result;
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
  
  public static String[] getRuntimesByType(String typeId) 
  {
    ArrayList ids = new ArrayList();
    Iterator iter = registry.webServiceRuntimes_.values().iterator();
    while (iter.hasNext())
    {
      WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo)iter.next();
      ServiceType[] sts = wsr.getServiceTypes();
      for (int i=0; i<sts.length; i++)
      {
        IWebServiceType wstype = sts[i].getWebSerivceType();
        if (typeId.equals(wstype.getId()))
        {
          ids.add(wsr.getId());
          break;
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
  
  public boolean isRuntimeSupportedForType(String typeId, String runtimeId)
  {
    String[] rIds = getRuntimesByType(typeId);
    if (rIds == null)
    {
      return false;
    }

    for (int i=0;i<rIds.length;i++)
    {
      if (runtimeId.equals(rIds[i]))
      {
        return true;
      }      
    }
    
    return false;
  }  
  
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
  
  public boolean doesRuntimeSupportServerTarget(String serverTargetId, String runtimeId)
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
  public static IProject[] getProjectsByWebServiceType(String typeId)
  {
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    ArrayList validProjects = new ArrayList();
    for (int i = 0; i < projects.length; i++)
    {
			if (!projects[i].getName().equals("Servers"))
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
  
  private static boolean include(IProject project, String include)
  {
    StringTokenizer st = new StringTokenizer(include);
    while(st.hasMoreTokens())
    {
      try
      {
        if (project.hasNature(st.nextToken()))
          return true;
      }
      catch (CoreException ce)
      {
      }
    }
    return false;
  }

  private static boolean exclude(IProject project, String exclude)
  {
    StringTokenizer st = new StringTokenizer(exclude);
    while(st.hasMoreTokens())
    {
      try
      {
        if (project.hasNature(st.nextToken()))
          return false;
      }
      catch (CoreException ce)
      {
      }
    }
    return true;
  }  

  
  public static LabelsAndIds getServiceTypeLabels()
  {
    LabelsAndIds labelIds = new LabelsAndIds();
    Iterator     iterator = registry.webServiceTypes_.values().iterator();
    int          size     = registry.webServiceTypes_.size();
    String[]     labels   = new String[size];
    String[]     ids      = new String[size];
    int          index    = 0;
    
    labelIds.setLabels_( labels );
    labelIds.setIds_( ids );
    
    while( iterator.hasNext() ) 
    {
      WebServiceTypeImpl type = (WebServiceTypeImpl)iterator.next();
      
      ids[index]    = type.getId();
      labels[index] = type.getLabel();
      index++;
    }    
    
    return labelIds;
  }
  
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
    String[] wst = tsf.getWebServiceTypeByInitialSelection(selection, registry.webServiceTypes_);
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
  public static boolean requiresEJBProject(String typeId)
  {
    IWebServiceType wst = getWebServiceTypeById(typeId);
    if (wst != null)
    {
      IConfigurationElement elem = wst.getConfigurationElement();
      String includedNatures = elem.getAttribute("includeNatures");
      if (includedNatures==null || includedNatures.length()==0)
      {
        return false;
      }
      else
      {
        StringTokenizer st = new StringTokenizer(includedNatures);
        if(st.hasMoreTokens())
        {
          String firstIncludedNature = st.nextToken();
          if (firstIncludedNature.equals(IEJBNatureConstants.NATURE_ID))
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
  
  
  public static String[] getRuntimesByClientType(String clientTypeId) 
  {
    ArrayList ids = new ArrayList();
    Iterator iter = registry.webServiceRuntimes_.values().iterator();
    while (iter.hasNext())
    {
      WebServiceRuntimeInfo wsr = (WebServiceRuntimeInfo)iter.next();
      ClientType[] cts = wsr.getClientTypes();
      for (int i=0; i<cts.length; i++)
      {
        String wsctypeId = cts[i].getWebServiceClientTypeId();
        if (wsctypeId.equals(clientTypeId))
        {
          ids.add(wsr.getId());
          break;
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
  
  public static String[] getClientProjectTypes(String clientTypeId, String runtimeId)
  {
    String[] rIds = getRuntimesByClientType(clientTypeId);
    if (rIds == null)
    {
      return null;
    }
    
    for (int i = 0; i < rIds.length; i++)
    {
      if (runtimeId.equals(rIds[i]))
      {
        ClientType[] cts = getWebServiceRuntimeById(rIds[i]).getClientTypes();
        for (int j=0; j<cts.length; j++)
        {
          String thisClientTypeId = cts[j].getWebServiceClientTypeId();
          if (clientTypeId.equals(thisClientTypeId))
          {
            //Found the one!
            String[] projectTypes = cts[j].getModuleTypesInclude();
            return projectTypes;
          }            
        }
      }
    }  
    
    return null;    
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
			for (int j = 0; j < cts.length; j++)
			{
				ClientType ct = cts[j];
				String id = ct.getWebServiceClientTypeId();
				if (!ids.contains(id))
				{
					ids.add(id);
					labels.add(ct.getWebServiceClientImpl().getLabel());
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
  
}

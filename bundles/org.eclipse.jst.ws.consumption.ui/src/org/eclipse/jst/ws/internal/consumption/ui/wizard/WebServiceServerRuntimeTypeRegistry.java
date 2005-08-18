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
package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.fragments.ServicePreAssemblyFragment;
import org.eclipse.jst.ws.internal.consumption.fragments.ServicePreDeployFragment;
import org.eclipse.jst.ws.internal.consumption.fragments.ServicePreDevelopFragment;
import org.eclipse.jst.ws.internal.consumption.fragments.ServicePreInstallFragment;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactoryFactory;
import org.eclipse.wst.command.internal.env.core.registry.CommandRegistry;
import org.eclipse.wst.command.internal.env.eclipse.EclipseLog;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBindingList;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;

public class WebServiceServerRuntimeTypeRegistry implements CommandRegistry
{

  private static WebServiceServerRuntimeTypeRegistry instance_;

  private IConfigurationElement[] indexedConfigElems_;

  private HashMap webServiceServers;
  private HashMap webServiceRuntimes;
  private HashMap webServiceTypes;
  private HashMap webServiceServerRuntimeTypes;
 
  private Hashtable runtimesByType_;
  private Hashtable runtimeLabelById_;
  private Hashtable runtimeIdByLabel_;
  private Hashtable serversByType_;
  private Hashtable serverLabelByFactoryId_;
  private Hashtable serverFactoryIdByLabel_;

  private Hashtable configElemsById_;
  
  private DataMappingRegistry dataMappingRegistry_;
  private WidgetRegistry      widgetRegistry_;
  private CanFinishRegistry   canFinishRegistry_;
  private HashMap             factoryCache_;
  
  private SelectionListChoices serverToRuntimeToJ2EE_;
  private Log					log_;
  
  public WebServiceServerRuntimeTypeRegistry() {
    log_ = new EclipseLog();
  }
  //
  // Loads WebServiceServerRuntimeType objects into this registry.
  // This is done by querying the plugin registry for all extensions
  // hanging on the WebServiceServerRuntimeType extension point. Extensions
  // must implement the org.eclipse.jst.ws.ui.wizard.WebServiceServerRuntimeType
  // interface.
  //
  private void loadTypes ()
  {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.consumption.ui",
                                     "webServiceServerRuntimeType");

    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement elem = config[idx];
      try 
      {
        if (elem.getName().equals("webServiceServer"))
        { 
          WebServiceServer wss = new WebServiceServer(elem);
          webServiceServers.put(elem.getAttribute("id"), wss);
          String serverLabel = wss.getLabel();
          if (serverLabel != null && serverLabel.length()>0)
          {
            if (!serverLabelByFactoryId_.containsKey(wss.getFactoryId()));
              serverLabelByFactoryId_.put(wss.getFactoryId(), serverLabel);
            if (!serverFactoryIdByLabel_.containsKey(serverLabel));
              serverFactoryIdByLabel_.put(serverLabel, wss.getFactoryId());
          }
          
        }
        else if (elem.getName().equals("webServiceRuntime"))
        {
          WebServiceRuntime rt = new WebServiceRuntime(elem);
          webServiceRuntimes.put(elem.getAttribute("id"), rt);
          if(!runtimeLabelById_.containsKey(rt.getId()))
          	runtimeLabelById_.put(rt.getId(), rt.getLabel());
          if(!runtimeIdByLabel_.containsKey(rt.getLabel()))
          	runtimeIdByLabel_.put(rt.getLabel(), rt.getId());
          
        }
        else if (elem.getName().equals("webServiceType"))
        {
          WebServiceTypeImpl wst = new WebServiceTypeImpl(elem);
          webServiceTypes.put(elem.getAttribute("id"), wst);
        }
        else if (elem.getName().equals("webServiceServerRuntimeType"))
        {
          configElemsById_.put(elem.getAttribute("id"), elem);

          String typeId = elem.getAttribute("type");
          String serverId = elem.getAttribute("server");
          String runtimeId = elem.getAttribute("runtime");
          WebServiceServerRuntimeType_ wssrt = new WebServiceServerRuntimeType_(serverId, runtimeId, typeId, elem);
          webServiceServerRuntimeTypes.put(elem.getAttribute("id"), wssrt);

          setRuntimesByType(typeId,runtimeId);
          setServersByType(typeId, serverId);
        
        }

      } 
      catch (Exception e)
      {
        log_.log(Log.ERROR, 5058, this, "loadTypes", e);
      }
      
    }
  }


  /**
  * Sets the Web service runtimes and type from each extension 
  * in a Hashtable.  The type is the key, and value is a Vector containing 
  * each runtime supported in that Web service type.
  * @param runtime name
  * @param type name
  */
  private void setRuntimesByType(String typeId, String runtime)
  {
    if(runtimesByType_.get(typeId)!=null)
    {
      Vector v = (Vector)runtimesByType_.get(typeId);
      if (!v.contains(runtime.trim())) {
        v.add(runtime.trim());
      }
    }
    else
    {
      Vector vect = new Vector();
      vect.add(runtime.trim());
      runtimesByType_.put(typeId,vect);
    }
  }

  public WebServiceRuntime getRuntimeById( String id )
  {
    return (WebServiceRuntime)webServiceRuntimes.get( id ); 
  }
  
  /**
  * Gets the Web sevice runtimes vector given a type
  * @param Web service type
  * @return array of runtime labels
  */
  public String[] getRuntimesByType(String webServiceType) 
  {
    String[] runtimes;
    if (runtimesByType_.containsKey(webServiceType))
    {
      Vector vect =(Vector)runtimesByType_.get(webServiceType);
      runtimes = new String[vect.size()];
      for (int i=0; i<vect.size(); i++)
      {
        String runtimeId = (String)vect.elementAt(i);
        WebServiceRuntime wsr = (WebServiceRuntime)webServiceRuntimes.get(runtimeId);
        if (wsr!=null) {
          runtimes[i] = (String)wsr.getId();
        }
      }
      return runtimes;

    }
    else
    {
      log_.log(Log.ERROR, 5059, this, "getRuntimesByType", "runtimes not found for Web service type "+webServiceType);
      return null;
    }

  }
  
  public boolean isRuntimeSupportedForType(String webServiceTypeId, String runtimeId)
  {
  	String[] runtimes = getRuntimesByType(webServiceTypeId);
	for(int i=0;i<runtimes.length;i++)
	{
  		if (runtimes[i].equals(runtimeId))
  		  return true;
	}
  	return false;
  }

  public boolean doesRuntimeSupportJ2EELevel(String j2eeVersionId, String runtimeId)
  {
    IWebServiceRuntime wsr = (IWebServiceRuntime)webServiceRuntimes.get(runtimeId);
    String[] j2eeVersions = wsr.getJ2EEVersions();
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
    Iterator it = webServiceServerRuntimeTypes.values().iterator();
    while (it.hasNext()) 
    {
      WebServiceServerRuntimeType_ wssrt = (WebServiceServerRuntimeType_)it.next();
      if (wssrt!=null) {
        if (wssrt.runtimeId_.equals(runtimeId))
        {
          IWebServiceServer wsserver = getWebServiceServerById(wssrt.serverId_);
          String thisFactoryId = wsserver.getFactoryId();
          IServerType serverType = ServerCore.findServerType(thisFactoryId);
          if (serverType == null)
            continue;
          
          String thisServerTargetId = serverType.getRuntimeType().getId(); 
          if (thisServerTargetId.equals(serverTargetId))
            return true;
        }
      }
    } 
    return false;  
  }
  
  /**
  * Gets the server factoryIDs of all web service server runtime type
  * with an runtime ID equals to the input
  * @param runtime ID
  * @return array of factory IDs
  */
  public String[] getServerFactoryIDByRuntimeID(String runtimeID) {
    Vector v = new Vector();
    Iterator it = webServiceServerRuntimeTypes.values().iterator();
    while (it.hasNext()) {
      WebServiceServerRuntimeType_ wssrt = (WebServiceServerRuntimeType_)it.next();
      if (wssrt!=null) {
        if (wssrt.runtimeId_.equals(runtimeID))
          v.add(getWebServiceServerById(wssrt.serverId_).getFactoryId());

      }
    }
    String[] serverFactoryIDs = new String[v.size()];
    for (int i = 0; i < v.size(); i++) {
      serverFactoryIDs[i] = (String)v.get(i);
    }
    return serverFactoryIDs;
  }

  /**
  * Gets the runtime IDs of all web service server runtime type
  * with an server factory ID equals to the input
  * @param server factory ID
  * @return array of runtime IDs
  */
  public String[] getRuntimeIDsByServerFactoryID(String serverFactoryID) {
    Vector v = new Vector();
    Iterator it = webServiceServerRuntimeTypes.values().iterator();
    while (it.hasNext()) {
      WebServiceServerRuntimeType_ wssrt = (WebServiceServerRuntimeType_)it.next();
      if (wssrt!=null) {
      	IWebServiceServer wss = getWebServiceServerById(wssrt.serverId_);
      	String wssfId = wss.getFactoryId();
      	if (wssfId.equals(serverFactoryID))
      	{
      		if (!v.contains(wssrt.runtimeId_))
      		{
      			v.add(wssrt.runtimeId_);	
      		}      		
      	}
        /*if (getWebServiceServerById(wssrt.serverId_).getFactoryId().equals(serverFactoryID))
          v.add(getWebServiceRuntimeById(wssrt.runtimeId_).getId());*/
      }
    }
    String[] runtimeIDs = new String[v.size()];
    for (int i = 0; i < v.size(); i++) {
      runtimeIDs[i] = (String)v.get(i);
    }
    return runtimeIDs;
  }
  
  /**
  * Sets the Web service servers and type from each extension 
  * in a Hashtable.  The type is the key, and value is a Vector containing 
  * each runtime supported in that Web service type.
  * @param server name
  * @param type name
  */
  private void setServersByType(String typeId, String server)
  {
    if(serversByType_.get(typeId)!=null)
    {
      Vector v = (Vector)serversByType_.get(typeId);
      if (!v.contains(server.trim())) 
      {
        v.add(server.trim());      
      }
    }
    else
    {
      Vector vect = new Vector();
      vect.add(server.trim());
      serversByType_.put(typeId, vect);
    }

  }

  /**
  * Gets the Web service servers vector given a type
  * @param Web service type
  * @return array of server factory Ids
  */
  public String[] getServerFactoryIdsByType(String webServiceType) 
  {
    String[] servers; 
    if (serversByType_.containsKey(webServiceType))
    {
      Vector vect = (Vector)serversByType_.get(webServiceType);
      servers = new String[vect.size()];
      for (int i=0; i<vect.size(); i++)
      {
        String serverId = (String)vect.elementAt(i);
        WebServiceServer wss = (WebServiceServer)webServiceServers.get(serverId);
        if (wss!=null) {
          servers[i] = (String)wss.getFactoryId(); //Note: returning server factory Ids
        }
      }
      return servers;
    }
    else
    {     
      log_.log(Log.ERROR, 5060, this, "getServerByType", "Web service server not found for Web service type "+webServiceType);
      return null;
    }

  }

  public IWebServiceRuntime getWebServiceRuntimeByLabel(String runtimeLabel)
  {
    Iterator iter = webServiceRuntimes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceRuntime wsr = (WebServiceRuntime)iter.next();
      if (wsr!=null) {
        if (runtimeLabel.equals(wsr.getLabel()))
          return wsr;
      }
    }
    return null;      
  }
  
  public IWebServiceRuntime getWebServiceRuntimeById(String runtimeId)
  {
    return (IWebServiceRuntime)webServiceRuntimes.get(runtimeId); 
  }

  public IWebServiceServer getWebServiceServerByFactoryId(String serverFactoryId)
  {
    if (serverFactoryId==null)
      return null;

    Iterator iter = webServiceServers.values().iterator();
    while (iter.hasNext())
    {
      WebServiceServer wss = (WebServiceServer)iter.next();
      if (wss!=null && wss.getFactoryId()!=null) {
        if (serverFactoryId.equals(wss.getFactoryId())) {
          return wss;
        }
      }
    }
    return null;    
    
  }

  public IWebServiceServer getWebServiceServerByLabel(String serverLabel)
  {
    if (serverLabel==null) 
      return null;
    
    Iterator iter = webServiceServers.values().iterator();
    while (iter.hasNext())
    {
      WebServiceServer wss = (WebServiceServer)iter.next();
      if (wss!=null) {
        if (serverLabel.equals(wss.getLabel()))
          return wss;
      }
    }
    return null;    
    
  }
  
  public IWebServiceServer getWebServiceServerById(String serverId)
  {
    return (IWebServiceServer)webServiceServers.get(serverId);
  }

  public IWebServiceType getWebServiceTypeByLabel(String typeLabel)
  {
    Iterator iter = webServiceTypes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceTypeImpl wst = (WebServiceTypeImpl)iter.next();
      if (wst!=null) {
        if (typeLabel.equals(wst.getLabel()))
          return wst;
      }
    }
    return null;
  }

  public IWebServiceType getWebServiceTypeById(String typeId)
  {
    if (webServiceTypes.get(typeId)!= null)
      return (WebServiceTypeImpl)webServiceTypes.get(typeId);
    return null;
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
//  public boolean requiresEJBProject(String typeId)
//  {
//    IWebServiceType wst = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceTypeById(typeId);
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
  
  /**
   * Returns a list of valid projects for the Web service type with an id of typeId.
   * In the case where the Web service type extension does not specify which project
   * natures are included, a array of all Web and EJB projects in the workspace will
   * be returned.
   * @param typeId
   * @return IProject[] an array of valid projects
   */
  public IProject[] getProjectsByWebServiceType(String typeId)
  {
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    ArrayList validProjects = new ArrayList();
//    IWebServiceType wst = WebServiceServerRuntimeTypeRegistry.getInstance().getWebServiceTypeById(typeId);
//    if (wst != null)
//    {

		//TODO: This section needs to be refactored to filter ArtifactEdits
		// and uphold project topology described in the extension
		
//      IConfigurationElement elem = wst.getConfigurationElement();
//      String includedNatures = elem.getAttribute("includeNatures");
//      String excludedNatures = elem.getAttribute("excludeNatures");
//      if (includedNatures!=null && includedNatures.length()>0)
//      {
//        for (int i = 0; i < projects.length; i++)
//        {
//          if (include(projects[i], includedNatures) && exclude(projects[i], excludedNatures))
//            validProjects.add(projects[i]);
//        }
//        return (IProject[])validProjects.toArray(new IProject[0]);
//      }
//    }
    
    //If wst was null or the extension didn't specify which project natures are
    // to be included, revert to the old behaviour
    for (int j = 0; j < projects.length; j++)
    {
      if (ResourceUtils.isWebProject(projects[j]) || ResourceUtils.isEJBProject(projects[j]))
        validProjects.add(projects[j]);
    }    
    return (IProject[])validProjects.toArray(new IProject[0]);
  }
  
  private boolean include(IProject project, String include)
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

  private boolean exclude(IProject project, String exclude)
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
  
  //
  // Loads WebServiceServerRuntimeType objects into this registry.
  // See method getInstance().
  //
  private void load ()
  {
    runtimesByType_ = new Hashtable();
    serversByType_ = new Hashtable();
    configElemsById_ = new Hashtable();
    runtimeIdByLabel_ = new Hashtable();
    runtimeLabelById_ = new Hashtable();
    serverFactoryIdByLabel_ = new Hashtable();
    serverLabelByFactoryId_ = new Hashtable();

    webServiceServers = new HashMap();
    webServiceRuntimes = new HashMap();
    webServiceTypes = new HashMap();
    
    webServiceServerRuntimeTypes = new HashMap();
    
    loadTypes();
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton WebServiceServerRuntimeTypeRegistry object.
  */
  public static WebServiceServerRuntimeTypeRegistry getInstance ()
  {
    if (instance_ == null)
    {
      instance_ = new WebServiceServerRuntimeTypeRegistry();
      instance_.load();
    }
    return instance_;
  }

  /**
  * Returns the Web Service type names of all registered <code>WebServiceServerRuntimeType</code>
  * objects.
  * @return The Web Service type names of all registered <code>WebServiceServerRuntimeType</code>
  * objects.
  */
  public String[] getWebServiceTypeNames()
  {
    
    String[] types = new String[webServiceTypes.size()];

    int i=0;
    Iterator iter = webServiceTypes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceTypeImpl wst = (WebServiceTypeImpl)iter.next();
      if (wst!=null) {
        types[i] = wst.getLabel();
        i++;
      }
    }
    return types;
  }
  
  public LabelsAndIds getServiceTypeLabels()
  {
    LabelsAndIds labelIds = new LabelsAndIds();
    Iterator     iterator = webServiceTypes.values().iterator();
    int          size     = webServiceTypes.size();
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
  
  /**
  * Returns the IConfigurationElement given the server, runtime, and type attributes
  * The benefit of getting the IConfigurationElement is that the corresponding class
  * file and plugin are not loaded immediately.
  * @param server label
  * @param runtime label
  * @param type label
  * @return IConfiguration element
  */
  public IConfigurationElement getConfigurationElementFor(String serverId, String runtimeId, String typeId)
  {
    if (serverId==null || runtimeId==null || typeId==null)
        return null;
    Iterator iter = webServiceServerRuntimeTypes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceServerRuntimeType_ wssrt = (WebServiceServerRuntimeType_)iter.next();
      if (wssrt!=null ) {
        if (wssrt.getConfigurationElementFor(serverId, runtimeId, typeId)!=null)
          return wssrt.getConfigurationElementFor(serverId, runtimeId,typeId);
      }
    }
    return null;    
  }
  
  public IConfigurationElement getConfigElementFor(String factoryId, String runtimeId, String typeId)
  {
    Iterator iter = webServiceServerRuntimeTypes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceServerRuntimeType_ wssrt = (WebServiceServerRuntimeType_)iter.next();
        if (wssrt.hasConfigElementFor(factoryId, runtimeId, typeId)) {
          return wssrt.element_;
      }
    }
    return null;    
  }

  public boolean requiresWebModuleFor(String server, String runtime, String type)
  {
    IConfigurationElement elem = getConfigurationElementFor(server, runtime, type);
    if (elem==null)
    {
      return false;  // report error here instead
    }
    return Boolean.valueOf(elem.getAttribute("requireWebModule")).booleanValue();
  }

  public boolean requiresEJBModuleFor(String server, String runtime, String type)
  {
    IConfigurationElement elem = getConfigurationElementFor(server, runtime, type);    
    if (elem==null)
    {
      return false;
    }
    return Boolean.valueOf(elem.getAttribute("requireEJBModule")).booleanValue();
  }

  /**
  * Returns the id of the <code>WebServiceServerRuntimeType</code> corresponding to the Server,
  * Runtime, and Web Service Type.
  */
  public boolean webServiceServerRuntimeTypeExists(String serverID, String runtimeID, String typeID) 
  {
    return (getConfigurationElementFor(serverID, runtimeID, typeID) != null);
  }

  /**
  * Returns the id of the <code>WebServiceServerRuntimeType</code> corresponding to the Server,
  * Runtime, and Web Service Type.
  */
  public String getWebServiceServerRuntimeTypeName(String server, String runtime, String type) 
  {
    IConfigurationElement elem = (IConfigurationElement)getConfigurationElementFor(server, runtime, type);
    return (elem==null ? null : elem.getAttribute("id"));
  }

  /**
  * Returns the <code>WebServiceServerRuntimeType</code> corresponding to the Server, 
  * Runtime, and Web Service type. This will return the <code>WebServiceServerRuntimeType</code>
  * object and set the properties found in the manifest file. i.e. isWebModuleRequired, isEJBModuleRequired
  * @param server label
  * @param runtime label
  * @param Web service type
  * @return WebServiceServerRuntimeType object
  */
  public WebServiceServerRuntimeType getWebServiceServerRuntimeType(String server, String runtime, String type)
  {

    WebServiceServerRuntimeType wssrt = null;
    String serverId = getWebServiceServerByFactoryId(server).getId();
    IConfigurationElement elem = getConfigurationElementFor(server, runtime, type);
   
    if( elem == null )
    {
      // We couldn't find the server and runtime specified so we will
      // find the default server and runtime.
      
      server  = getDefaultServerValueFor(type);
      runtime = getDefaultRuntimeValueFor(type);

      elem = getConfigurationElementFor(server, runtime, type);
      
    }
   
    return wssrt;

  }


  /**
  * Returns the <code>WebServiceServerRuntimeType</code> given the type name.
  * @param type name
  * @retrun WebServiceServerRuntimeType object
  */
  public WebServiceServerRuntimeType getWebServiceServerRuntimeTypeById(String id)
  {
    WebServiceServerRuntimeType wssrt = null;
    if (configElemsById_.containsKey(id))
    {
      try {
        IConfigurationElement elem = (IConfigurationElement)configElemsById_.get(id);
        Object webServiceType = elem.createExecutableExtension("class");
        if (webServiceType instanceof WebServiceServerRuntimeType) 
        {
          wssrt = (WebServiceServerRuntimeType)webServiceType;
          wssrt.setServerLabel(elem.getAttribute("server"));
          wssrt.setRuntimeLabel(elem.getAttribute("runtime"));
          wssrt.setWebModuleRequired( Boolean.valueOf(elem.getAttribute("requireWebModule")).booleanValue() );
          wssrt.setEJBModuleRequired( Boolean.valueOf(elem.getAttribute("requireEJBModule")).booleanValue() );
        }
        else
        {
          String implementedInterface = "org.eclipse.jst.ws.ui.wizard.WebServiceServerRuntimeType ";
          String errMsg = "Extensions of the WebServiceServerRuntimeTypes extension point must implement the ";
          errMsg = errMsg + implementedInterface + "interface.";
          log_.log(Log.ERROR, 5061, this, "getWebServiceServerRuntimeTypeById", errMsg);
        }
      }
      catch (CoreException ce)
      {
        log_.log(Log.ERROR, 5062, this, "getWebServiceServerRuntimeTypeById", ce);
      }
    }

    return wssrt;
  }

  public String[] getWebServiceTypeBySelection(IStructuredSelection selection)
  {

    TypeSelectionFilter tsf = new TypeSelectionFilter();
    String[] wst = tsf.getWebServiceTypeByInitialSelection(selection, webServiceTypes);
    return wst == null ? null : wst;

  }

  public IWebServiceType getWebServiceTypeByTypeLabel(String webServiceTypeLabel)
  {

    Iterator iter = webServiceTypes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceTypeImpl wst = (WebServiceTypeImpl)iter.next();
      if (wst!=null) {
        if (wst.getLabel().equals(webServiceTypeLabel)) {
          return wst; 
        }
      }
    }
    return null;
  }

  public boolean isInitialSelectionValidForChosenType(IStructuredSelection selection, String typeName)
  {
    String[] supportedTypes = getWebServiceTypeBySelection(selection);
    if (supportedTypes!=null)
    {
      for (int i=0; i<supportedTypes.length; i++)
      {
        if (supportedTypes[i].equals(typeName))
          return true;
      }
    }
    return false;

  }

  public boolean isServerSupportedForChosenType(String typeId, String serverID)
  {
    if (serversByType_.containsKey(typeId))
    {
      Vector vect = (Vector)serversByType_.get(typeId);
      for (int i=0; i<vect.size(); i++)
      {
        WebServiceServer wss = (WebServiceServer)webServiceServers.get((String)vect.elementAt(i));
        if (serverID!=null) {
          if (serverID.equalsIgnoreCase(wss.getFactoryId()))
            return true;
        }
      }
    }
    return false;
  }
  
  public boolean isServerRuntimeTypeSupported(String server, String runtime, String type)  {

      WebServiceServerRuntimeType wssrt = null;
      IConfigurationElement elem = getConfigurationElementFor(server, runtime, type);
      return elem == null ? false : true; 
          
  }
  
  public String getDefaultServerValueFor(String typeId)
  {
    String[] servers; 
    if (serversByType_.containsKey(typeId))
    {
      Vector vect = (Vector)serversByType_.get(typeId);
      servers = new String[vect.size()];
      for (int i=0; i<vect.size(); i++)
      {
        String serverId = (String)vect.elementAt(i);
        WebServiceServer wss = (WebServiceServer)webServiceServers.get(serverId);
        servers[i] = (String)wss.getId();
      }
      return servers[0];
    }
    else
    {
      log_.log(Log.ERROR, 5063, this, "getServerByType", "Unable to find default server for "+typeId);
      return null;
    }

  }

  public String getDefaultRuntimeValueFor(String typeId)
  {
    String[] runtimeName = getRuntimesByType(typeId);
    for (int i=0; i<runtimeName.length; i++)
    {
        WebServiceRuntime wsr = (WebServiceRuntime)webServiceRuntimes.get(runtimeName[i]);
        if (wsr.getIsDefault())
        	return runtimeName[i];
    }   
    return runtimeName[0];
  }
  
  public String getServerIdForFactoryId(String factoryId) {

    Iterator iter = webServiceServers.values().iterator();
    while (iter.hasNext()) {
      WebServiceServer wss = (WebServiceServer)iter.next();
      if (wss.getFactoryId().equals(factoryId)) {
        return wss.getId();
      }
    }
    return null;

  }
 
  public final class WebServiceServerRuntimeType_
  {
    
   public String typeId_;
   public String serverId_;
   public String runtimeId_;
   public IConfigurationElement element_;
   
   public WebServiceServerRuntimeType_(String serverId, String runtimeId,String typeId, IConfigurationElement elem)
   {
    this.typeId_ = typeId;
    this.serverId_ = serverId;
    this.runtimeId_ = runtimeId;
    this.element_ = elem;
   }
    
   public IConfigurationElement getConfigurationElementFor(String serverID, String runtimeID, String typeID)
   {
    if (this.typeId_.equals(typeID) && this.serverId_.equals(serverID) && this.runtimeId_.equals(runtimeID))
      return this.element_;
    return null;
   }
   
   public boolean hasConfigElementFor(String factoryID, String runtimeID, String typeID) 
   {
     String serverID = getServerIdForFactoryId( factoryID ); 
       
     if (typeId_.equalsIgnoreCase(typeID) &&
         serverId_.equalsIgnoreCase(serverID) &&
         runtimeId_.equalsIgnoreCase(runtimeID))
     {
        return true;
     }
     
     return false;
    }
    
    
  }

  public CommandFragmentFactoryFactory getFactoryFactory( String[] ids )
  {
    String                hashKey = ids[2] + "#" + ids[1] + "#" + ids[0];
    CommandWidgetBinding  binding = (CommandWidgetBinding)factoryCache_.get( hashKey );
    
    if( binding == null ) 
    {
      IConfigurationElement element = getConfigElementFor( ids[2], ids[1], ids[0] ); 
    
      if( element == null ) return null;
    
      binding = createBinding( element );
      
      factoryCache_.put( hashKey, binding );
      
      binding.registerDataMappings( dataMappingRegistry_ );
      
      if( widgetRegistry_ != null )
      {
        binding.registerWidgetMappings( widgetRegistry_ );
        binding.registerCanFinish( canFinishRegistry_ );
      }
    }
         
    return binding;    
  }
  
  private CommandWidgetBinding createBinding(IConfigurationElement element ) 
  {
    Vector bindings = new Vector(4);
    Vector fragments = new Vector(4);
    
    addBinding( element, "developBinding", bindings );
    addBinding( element, "assemblyBinding", bindings );
    addBinding( element, "deployBinding", bindings );
    addBinding( element, "installBinding", bindings );
    
    fragments.add( new ServicePreDevelopFragment() );
    fragments.add( new ServicePreAssemblyFragment() );
    fragments.add( new ServicePreDeployFragment() );
    fragments.add( new ServicePreInstallFragment() );
     
    return new CommandWidgetBindingList( bindings, fragments );
  }
  
  private void addBinding( IConfigurationElement element, String bindingName, Vector list )
  {
    Object binding = null;
    
    if( element.getAttribute( bindingName ) != null )
    {
      try
      {
        binding = element.createExecutableExtension( bindingName ); 
      }
      catch( CoreException exc )
      {
      }
    }
    
    list.add( binding );
  }
  
  public void setDataMappingRegistry( DataMappingRegistry registry )
  {
    dataMappingRegistry_ = registry;
    
    // We need to rebuild the factory cache each time we start the server
    // wizard.
    factoryCache_        = new HashMap();
  }
  
  public void setWidgetRegistry( WidgetRegistry registry )
  {
    widgetRegistry_ = registry;;  
  }
  
  public void setCanFinishRegistry( CanFinishRegistry registry )
  {
    canFinishRegistry_ = registry;
  }

  public String[] getAllServerFactoryIds()
  {
  	Object[] servers = webServiceServers.values().toArray();
  	String[] factoryIds = new String[servers.length];
  	for (int i=0; i<servers.length; i++)
  	{
  		WebServiceServer wss = (WebServiceServer)servers[i];
  		factoryIds[i] = wss.getFactoryId();
  	}
  	return factoryIds;
  }
  
  public String getServerFactoryIdFromServerId(String serverId)
  {
  	Object[] servers = webServiceServers.values().toArray();
  	for (int i=0; i<servers.length; i++)
  	{
  		WebServiceServer wss = (WebServiceServer)servers[i];
  		if (serverId == wss.getId())
  		{
  			return wss.getFactoryId();
  		}
  			
  	}
  	return null;
  }
  
  /*
   * Returns a cascading list of valid choices for server, runtime, and J2EE levels. 
   * This is an intersection of what is available for service and client. 
   */
  public SelectionListChoices getServerToRuntimeToJ2EE()
  {
  	if (serverToRuntimeToJ2EE_!=null)
  	{
  		return serverToRuntimeToJ2EE_;
  	}
  	
  	String[] servers = getStringArrayIntersection(getAllServerFactoryIds(), WebServiceClientTypeRegistry.getInstance().getAllClientServerFactoryIds());
    SelectionList serversList = new SelectionList(servers, 0);
    Vector choices = new Vector();
    for (int i=0; i<servers.length; i++)
    {
    	choices.add(getRuntimeChoices(servers[i]));
    }
    serverToRuntimeToJ2EE_ = new SelectionListChoices(serversList, choices);
    return serverToRuntimeToJ2EE_;
  	
  }
  
  private SelectionListChoices getRuntimeChoices(String serverFactoryId)
  {
  	String[] serviceRuntimes = getRuntimeIDsByServerFactoryID(serverFactoryId);
  	String[] clientRuntimes = WebServiceClientTypeRegistry.getInstance().getRuntimeIdsByServerFactoryId(serverFactoryId);
  	String[] runtimes = getStringArrayIntersection(serviceRuntimes, clientRuntimes);
  	SelectionList runtimesList = new SelectionList(runtimes, 0);
  	Vector choices = new Vector();
    for (int i=0; i<runtimes.length; i++)
    {
    	choices.add(getJ2EEChoices(runtimes[i]));
    }
    return new SelectionListChoices(runtimesList, choices);    
  }
  
  private SelectionListChoices getJ2EEChoices(String runtimeId)
  {
  	String[] serviceJ2EEVersions = getWebServiceRuntimeById(runtimeId).getJ2EEVersions();
  	String[] clientJ2EEVersions = WebServiceClientTypeRegistry.getInstance().getWebServiceRuntimeById(runtimeId).getJ2EEVersions();
  	String[] j2eeVersions = getStringArrayIntersection(serviceJ2EEVersions, clientJ2EEVersions);
  	SelectionList j2eeVersionsList = new SelectionList(j2eeVersions, 0);
  	return new SelectionListChoices(j2eeVersionsList, null);
  }
  
  public String getServerLabel(String factoryId)
  {
  	if (factoryId==null || factoryId.length()==0)
  		return null;
  	
  	if (serverLabelByFactoryId_.containsKey(factoryId))
  	{
  	  return (String)serverLabelByFactoryId_.get(factoryId);
  	}
  	else
  	{
  	  return null;
  	}
  }
  
  public String getServerFactoryId(String label)
  {
  	if (label==null || label.length()==0)
  		return null;
  	
  	if (serverFactoryIdByLabel_.containsKey(label))
  	{
  	  return (String)serverFactoryIdByLabel_.get(label);  
  	}
  	else
  	{
  	  return null;
  	}  	  	
  }
  
  public String getRuntimeLabel(String id)
  {
  	if (id==null || id.length()==0)
  		return null;
  	
  	return (String) runtimeLabelById_.get(id);
  }
  
  public String getRuntimeId(String label)
  {
  	if (label==null || label.length()==0)
  		return null;
  	
  	return (String)runtimeIdByLabel_.get(label);  	
  }  
  
  private String[] getStringArrayIntersection(String[] a1, String[] a2)
  {
  	Vector vf = new Vector();
  	if (a1==null || a2==null)
  	{
  		return convertToStringArray(vf.toArray());
  	}  	
  	
  	for (int i=0; i<a1.length; i++)
  	{
  		for (int j=0; j<a2.length; j++)
  		{
  			if (a1[i].equals(a2[j]))
  			{
  				vf.add(a1[i]);
  				break;
  			}
  		}
  	}
  	
  	return convertToStringArray(vf.toArray());
  }
  
  private String[] convertToStringArray(Object[] a)
  {
  	if (a==null) return new String[0];
  	
  	int length = a.length;
  	String[] sa = new String[length];
  	for (int i=0; i<length; i++)
  	{
  		Object obj = a[i];
  		if (obj instanceof String)
  		{
  			sa[i] = (String)obj;
  		}
  	}
  	return sa;
  }
}





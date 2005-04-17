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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.internal.consumption.fragments.ClientPreAssemblyFragment;
import org.eclipse.jst.ws.internal.consumption.fragments.ClientPreDeployFragment;
import org.eclipse.jst.ws.internal.consumption.fragments.ClientPreDevelopFragment;
import org.eclipse.jst.ws.internal.consumption.fragments.ClientPreInstallFragment;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.core.fragment.CommandFragmentFactoryFactory;
import org.eclipse.wst.command.env.core.registry.CommandRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBindingList;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;

public class WebServiceClientTypeRegistry implements CommandRegistry
{
  private static WebServiceClientTypeRegistry instance_;

  private Vector  configElems_;
  private HashSet typeNames_;  // no duplicate client type names
  private HashMap types_;
  private HashMap clientTypeAndIds_;
  private HashMap clientIdToLabel_;

  private HashMap webServiceServers;
  private HashMap webServiceRuntimes;
  private HashMap webServiceClientTypes;
  
  private DataMappingRegistry dataMappingRegistry_;
  private WidgetRegistry      widgetRegistry_;
  private CanFinishRegistry   canFinishRegistry_;
  private HashMap             factoryCache_;
    
  private WebServiceClientTypeRegistry()
  {
  }
  
  //
  // Loads WebServiceClientType objects into this registry.
  // This is done by querying the plugin registry for all extensions
  // hanging on the webServiceClientType extension point. Extensions
  // must implement the org.eclipse.jst.ws.ui.wizard.WebServiceClientType
  // interface.
  //
  private void loadTypes ()
  {    
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.consumption.ui",
                                     "webServiceClientType");

    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement elem = config[idx];

      if (elem.getName().equals("webServiceServer")) { 
        WebServiceServer wss = new WebServiceServer(elem);
        webServiceServers.put(elem.getAttribute("id"), wss);
      }
      else if (elem.getName().equals("webServiceRuntime")) {
        WebServiceRuntime rt = new WebServiceRuntime(elem);
        webServiceRuntimes.put(elem.getAttribute("id"), rt);
      }
      else if (elem.getName().equals("webServiceClientType"))
      {

        String name = elem.getAttribute( "name" );                
        typeNames_.add( name );  
        
        String typeId = elem.getAttribute("clientType");
        if (typeId==null) {
          typeId = name;
        }

        String id = elem.getAttribute("id");       
        clientTypeAndIds_.put(name, typeId);
        String serverId = elem.getAttribute("server");
        String runtimeId = elem.getAttribute("runtime");
        WebServiceServerRuntimeClientType wst = new WebServiceServerRuntimeClientType(serverId, runtimeId, typeId, name, elem);
        webServiceClientTypes.put(id, wst);
        types_.put(id, null );  
        clientIdToLabel_.put( typeId, name );
      }
      configElems_.add( elem );
      
    }
  }

 
  /**
  * Loads WebServiceClientType objects into this registry.
  * See method getInstance().
  */
  private void load ()
  {
    typeNames_ = new HashSet();
    configElems_ = new Vector();
    types_       = new HashMap();
    clientTypeAndIds_ = new HashMap();
    webServiceClientTypes = new HashMap();
    webServiceRuntimes = new HashMap();
    webServiceServers = new HashMap();
    clientIdToLabel_  = new HashMap();
    loadTypes();
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton WebServiceClientTypeRegistry object.
  */
  public static WebServiceClientTypeRegistry getInstance()
  {
    if (instance_ == null)
    {
      instance_ = new WebServiceClientTypeRegistry();
      instance_.load();
    }
    return instance_;
  }
  
  public WebServiceRuntime getRuntimeById( String id )
  {
    return (WebServiceRuntime)webServiceRuntimes.get( id ); 
  }

  public String[] getRuntimesByType(String typeId)
  {
    IConfigurationElement[] elems = getConfigurationElement(typeId);
    Vector runtimesVector = new Vector();
    String[] runtimeIds = new String[elems.length];
    for (int i=0; i<elems.length; i++)
    {
      IConfigurationElement elem = (IConfigurationElement)elems[i];
      runtimeIds[i] = elem.getAttribute("runtime");
    }
    return runtimeIds;    
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
    //Vector serverFactoryIds = new Vector();
    Iterator it = webServiceClientTypes.values().iterator();
    while (it.hasNext()) {
      WebServiceServerRuntimeClientType wssrt = (WebServiceServerRuntimeClientType)it.next();
      if (wssrt.runtimeId_!=null) {
        if ((wssrt.runtimeId_).equalsIgnoreCase(runtimeId)) {
          WebServiceServer wss = (WebServiceServer)webServiceServers.get(wssrt.serverId_);
          String thisFactoryId = wss.getFactoryId();
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
  * Returns the names of all registered <code>WebServiceClientType</code>
  * objects.
  * @return The names of all registered <code>WebServiceClientType</code>
  * objects.
  */
  public String[] getWebServiceClientTypeNames ()
  {
    return (String[])typeNames_.toArray( new String[0] );
  }

  public LabelsAndIds getClientTypeLabels()
  {
    LabelsAndIds labelIds = new LabelsAndIds();
    Iterator     iterator = clientIdToLabel_.entrySet().iterator();
    int          size     = clientIdToLabel_.size();
    String[]     labels   = new String[size];
    String[]     ids      = new String[size];
    int          index    = 0;
    
    labelIds.setLabels_( labels );
    labelIds.setIds_( ids );
    
    while( iterator.hasNext() ) 
    {
      Map.Entry entry = (Map.Entry)iterator.next();
      
      ids[index]    = (String)entry.getKey();
      labels[index] = (String)entry.getValue();
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
    Iterator iter = webServiceClientTypes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceServerRuntimeClientType wssrt = (WebServiceServerRuntimeClientType)iter.next();
        if (wssrt.hasConfigurationElementFor(serverId, runtimeId, typeId)) {
          return wssrt.element_;
      }
    }
    return null;    
  }
  
  public IConfigurationElement getConfigElementFor(String factoryId, String runtimeId, String typeId)
  {
    Iterator iter = webServiceClientTypes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceServerRuntimeClientType wssrt = (WebServiceServerRuntimeClientType)iter.next();
        if (wssrt.hasConfigElementFor(factoryId, runtimeId, typeId)) {
          return wssrt.element_;
      }
    }
    return null;    
  }

  public String getClientExtensionIdFor(String serverId, String runtimeId, String typeId) {
    IConfigurationElement element = getConfigurationElementFor(serverId, runtimeId, typeId);
    return element.getAttribute("id");
  }

  public IConfigurationElement getConfigurationElementById(String id) {
    
    WebServiceServerRuntimeClientType wsrt = (WebServiceServerRuntimeClientType)webServiceClientTypes.get(id);
    return (IConfigurationElement)wsrt.element_;    

  }
  
  public IConfigurationElement[] getConfigurationElement(String clientTypeId)
  {
    Vector v = new Vector();
    for (Iterator it = configElems_.iterator(); it.hasNext();)
    {
      IConfigurationElement element = (IConfigurationElement)it.next();
      String clientType = element.getAttribute("clientType");
      if (clientType != null && clientType.equals(clientTypeId))
        v.add(element);
    }
    return (IConfigurationElement[])v.toArray(new IConfigurationElement[0]);
  }

  public IConfigurationElement[] getConfigurationElement(String clientTypeId, String runtimeId)
  {
    Vector v = new Vector();
    for (Iterator it = configElems_.iterator(); it.hasNext();)
    {
      IConfigurationElement element = (IConfigurationElement)it.next();
      String clientType = element.getAttribute("clientType");
      String runtime = element.getAttribute("runtime");
      if (clientType != null && runtime != null && clientType.equals(clientTypeId) && runtime.equals(runtimeId))
        v.add(element);
    }
    return (IConfigurationElement[])v.toArray(new IConfigurationElement[0]);
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

  public String getServerFactoryIdByServerLabel(String label) {
    Iterator iter = webServiceServers.values().iterator();
    while (iter.hasNext()) {
      WebServiceServer wss = (WebServiceServer)iter.next();
      if (wss.getLabel().equals(label)) {
        return wss.getFactoryId();
      }
    }
    return null;
  }

  public WebServiceRuntime getWebServiceRuntimeByName(String runtimeName) {
    Iterator iter = webServiceRuntimes.values().iterator();
    while (iter.hasNext()) {
      WebServiceRuntime wsr = (WebServiceRuntime)iter.next();
      if (wsr.getLabel().equals(runtimeName)) {
        return wsr;
      }
    }
    return null;
  }

  public WebServiceServer getWebServiceServerByFactoryId(String factoryId) {
    Iterator iter = webServiceServers.values().iterator();
    while (iter.hasNext()) {
      WebServiceServer wss = (WebServiceServer)iter.next();
      if (wss.getFactoryId().equals(factoryId)) {
        return wss;
      }
    }
    return null;
  }
  
  public String getServerInstanceLabelFromInstanceId( String instanceId )
  {
    IServer server = ServerCore.findServer( instanceId );
    
    return server.getName();
  }
  
  public String[] getAllClientServerFactoryIds() {
    String[] serverFactoryIds = new String[webServiceServers.size()];
    int i=0;
    Iterator iter = webServiceServers.values().iterator();
    while (iter.hasNext()) {
      WebServiceServer wss = (WebServiceServer)iter.next();
      if (wss.getFactoryId()!=null) {
        serverFactoryIds[i]=wss.getFactoryId();
        i++;
      }
    }
    return serverFactoryIds;
  }

  public String[] getAllClientRuntimes() {
    String[] runtimes = new String[webServiceRuntimes.size()];
    int i=0;
    Iterator iter = webServiceRuntimes.values().iterator();
    while (iter.hasNext()) {
      WebServiceRuntime wsr = (WebServiceRuntime)iter.next();
      if (wsr.getId() != null) {
        runtimes[i]=wsr.getId();
        i++;
      }
    }
    return runtimes;
  }

  public String getRuntimeLabelById(String runtimeId) {
    WebServiceRuntime wsRuntime = (WebServiceRuntime)webServiceRuntimes.get(runtimeId);
    if (wsRuntime!=null) {
      return wsRuntime.getLabel();
    }
    return null;
  }

  public String getClientTypeIdByName(String name) {
    return (String)clientTypeAndIds_.get(name);
  }
  
  public IWebServiceRuntime getWebServiceRuntimeById(String runtimeId)
  {
    return (IWebServiceRuntime)webServiceRuntimes.get(runtimeId); 
  }


  /**
  * Gets the runtime IDs of all web service server runtime type
  * with an server factory ID equals to the input
  * @param server factory ID
  * @return array of runtime IDs
  */
  public String[] getRuntimeIdsByServerFactoryId(String serverFactoryID) {
    Vector runtimeIds = new Vector();
    Iterator it = webServiceClientTypes.values().iterator();
    while (it.hasNext()) {
      WebServiceServerRuntimeClientType wssrt = (WebServiceServerRuntimeClientType)it.next();
      if (wssrt.serverId_!=null) {
      	String serverId = wssrt.serverId_;
      	WebServiceServer wss = (WebServiceServer)webServiceServers.get(serverId);
      	String wssfId = wss.getFactoryId();
      	if (wssfId.equals(serverFactoryID))
      		runtimeIds.add(wssrt.runtimeId_);
      	/*
        String wssFactoryId = ((WebServiceServer)webServiceServers.get(wssrt.serverId_)).getFactoryId();
        if (wssFactoryId.equalsIgnoreCase(serverFactoryID)) {
          runtimeIds.add(wssrt.runtimeId_);
        }
        */
      }
    }
    return (String[])runtimeIds.toArray(new String[0]);
  }

  public String[] getServerFactoryIdsByClientType(String clientTypeName) {
    
    Vector factoryIds = new Vector();
    String clientTypeId = getClientTypeIdByName(clientTypeName);
    Iterator iter = webServiceClientTypes.values().iterator();
    while (iter.hasNext())
    {
      WebServiceServerRuntimeClientType wssrt = (WebServiceServerRuntimeClientType)iter.next();
      if ((wssrt.typeId_).equalsIgnoreCase(clientTypeId)) {
        WebServiceServer wss = (WebServiceServer)webServiceServers.get(wssrt.serverId_);
        factoryIds.add(wss.getFactoryId());
      }
  
    }
    return (String[])factoryIds.toArray(new String[0]);   

  }

  /**
  * Gets the server factoryIds of all web service server runtime type
  * with an runtime Id equals to the input
  * @param runtime Id
  * @return array of factory Ids
  */
  public String[] getServerFactoryIdsByRuntimeId(String runtimeID) {
    Vector serverFactoryIds = new Vector();
    Iterator it = webServiceClientTypes.values().iterator();
    while (it.hasNext()) {
      WebServiceServerRuntimeClientType wssrt = (WebServiceServerRuntimeClientType)it.next();
      if (wssrt.runtimeId_!=null) {
        if ((wssrt.runtimeId_).equalsIgnoreCase(runtimeID)) {
          WebServiceServer wss = (WebServiceServer)webServiceServers.get(wssrt.serverId_);
          serverFactoryIds.add(wss.getFactoryId());
        }
      }
    }
    return (String[])serverFactoryIds.toArray(new String[0]);
  }

  public boolean webServiceClientRuntimeTypeExists(String serverId, String runtimeId, String typeId) {
    if (getConfigurationElementFor(serverId, runtimeId, typeId)==null)
      return false;
    return true;

  }

  public String[] getServerFactoryIdsByType(String clientTypeId)
  {
    String clientTypeName = (String)clientIdToLabel_.get(clientTypeId);
    return getServerFactoryIdsByClientType(clientTypeName);
  }
  
  public boolean isServerSupportedForChosenType(String clientTypeId, String serverFactoryID)
  {
    String[] supportedServerFactoryIds = getServerFactoryIdsByType(clientTypeId);
    if (supportedServerFactoryIds!=null && supportedServerFactoryIds.length>0)
    {
	    for (int i=0; i<supportedServerFactoryIds.length; i++)
	    {
	      if (supportedServerFactoryIds[i].equals(serverFactoryID))
	        return true;
	    }
    }
    return false;
  }  

  /**
  *  WebServiceServerRuntimeClientType is the cached metadata including the IConfiguration
  */
  public final class WebServiceServerRuntimeClientType
  {
    
   private String typeId_ = null;
   private String serverId_ = null;
   private String runtimeId_ = null;
   private IConfigurationElement element_;
   private boolean requiresServerRuntime_ = false;
   private String label_;
   
   protected WebServiceServerRuntimeClientType(String serverId, String runtimeId,String typeId, String label, IConfigurationElement elem)
   {
    typeId_ = new String(typeId.trim());
    if (serverId!=null)
      serverId_ = new String(serverId.trim());
    if (runtimeId!=null)
      runtimeId_ = new String(runtimeId.trim());
    element_ = elem;
    if (serverId_!=null && runtimeId_!=null) {
      requiresServerRuntime_ = true;
    }

    label_ = label;
   }

   public boolean hasConfigurationElementFor(String serverID, String runtimeID, String typeID) {
    if (typeId_.equalsIgnoreCase(typeID) && !requiresServerRuntime_)
      return true;

    if (requiresServerRuntime_) {
      if (typeId_.equalsIgnoreCase(typeID) &&
         serverId_.equalsIgnoreCase(serverID) &&
         runtimeId_.equalsIgnoreCase(runtimeID))
        return true;
    }
    return false;
   }
   
   public boolean hasConfigElementFor(String factoryID, String runtimeID, String typeID) {
     if (typeId_.equalsIgnoreCase(typeID) && !requiresServerRuntime_)
       return true;

     if (requiresServerRuntime_) 
     {
       String serverID = getServerIdForFactoryId( factoryID ); 
       
       if (typeId_.equalsIgnoreCase(typeID) &&
          serverId_.equalsIgnoreCase(serverID) &&
          runtimeId_.equalsIgnoreCase(runtimeID))
         return true;
     }
     return false;
    }
   
   public IConfigurationElement getClientTypeConfigurationElementFor(String serverID, String runtimeID, String typeID)
   {
    if (this.typeId_.equalsIgnoreCase(typeID) && serverID==null && runtimeID==null)
      return this.element_;
    
    if (this.typeId_.equalsIgnoreCase(typeID) && 
       this.serverId_.equalsIgnoreCase(serverID) && 
       this.runtimeId_.equalsIgnoreCase(runtimeID))
      return this.element_;
    return null;
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
    
    fragments.add( new ClientPreDevelopFragment() );
    fragments.add( new ClientPreAssemblyFragment() );
    fragments.add( new ClientPreDeployFragment() );
    fragments.add( new ClientPreInstallFragment() );
     
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
    
    // We need to rebuild the factory cache each time we start the client
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
  
  
}

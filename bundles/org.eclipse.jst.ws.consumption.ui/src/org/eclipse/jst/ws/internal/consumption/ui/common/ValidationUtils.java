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
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jst.j2ee.internal.webservices.WebServiceEditModel;
import org.eclipse.jst.j2ee.internal.webservices.WebServicesManager;
import org.eclipse.jst.j2ee.webservice.internal.WebServiceConstants;
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.WSDLPort;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServices;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.ClientProjectTypeRegistry;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.common.componentcore.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

/**
 *
 */
public class ValidationUtils
{
  MessageUtils msgUtils;

  /**
   * 
   */
  public ValidationUtils()
  {
    String pluginId = WebServiceConsumptionUIPlugin.ID; //"org.eclipse.jst.ws.consumption.ui";
    msgUtils = new MessageUtils( pluginId + ".plugin", this );
  }
  
  public Status validateProjectTargetAndJ2EE(String projectName, String earName, String serverFactoryId, String j2eeLevel)
  {
    IProject p = FileResourceUtils.getWorkspaceRoot().getProject(projectName);
	IProject earP = null;
	if (earName!=null && !earName.equalsIgnoreCase("")) {
    	earP = FileResourceUtils.getWorkspaceRoot().getProject(earName);
	}
    Status targetStatus = doesProjectTargetMatchServerType(p, serverFactoryId);
    if (earP!=null && targetStatus.getSeverity()==Status.OK)
    {
      //check the EAR      
      Status earTargetStatus = doesProjectTargetMatchServerType(earP, serverFactoryId);
      if(earTargetStatus.getSeverity()==Status.ERROR)
      {
        return earTargetStatus;
      }            
    }
    else
    {
      return targetStatus;
    }
    

    //Validate service side J2EE level    
    Status j2eeStatus = doesProjectMatchJ2EELevel(p, j2eeLevel);
    if(earP!=null && j2eeStatus.getSeverity()==Status.OK)
    {
      Status earJ2EEStatus = doesProjectMatchJ2EELevel(earP, j2eeLevel);
      if(earJ2EEStatus.getSeverity()==Status.ERROR)
      {
        return earJ2EEStatus;
      }
    }
    else
    {
      return j2eeStatus;
    }
    
    return new SimpleStatus("");
  }
  
  private Status doesProjectTargetMatchServerType(IProject p, String serverFactoryId)
  {
    if (p!=null && p.exists())
    {
		System.out.println("P.getName = "+p.getName());
      IRuntime projectTarget = ServerSelectionUtils.getRuntimeTarget(p.getName());
      if (projectTarget!=null)
      {
        String projectTargetId = projectTarget.getRuntimeType().getId();
        String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverFactoryId);
        if (serverTargetId!=null && serverTargetId.length()>0)
        {
          if(!projectTargetId.equals(serverTargetId))
          { 
            return new SimpleStatus("",msgUtils.getMessage("MSG_SERVER_TARGET_MISMATCH",new String[]{p.getName()}),Status.ERROR);
          }
        }
      }
    }
    return new SimpleStatus("");        
  }

  private Status doesProjectMatchJ2EELevel(IProject p, String j2eeLevel)
  {
	StructureEdit mc = null;
    try {
		if (p!=null && p.exists())
		{
		  mc = StructureEdit.getStructureEditForRead(p);
		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
		  
			
		  int projectJ2EELevel = J2EEUtils.getJ2EEVersion(p);
		  if (projectJ2EELevel!=-1)
		  {
		    String projectJ2EELevelString = String.valueOf(projectJ2EELevel);
		    if (j2eeLevel!=null && j2eeLevel.length()>0)
		    {
		      if (!projectJ2EELevelString.equals(j2eeLevel))
		      {
		        return new SimpleStatus("",msgUtils.getMessage("MSG_J2EE_MISMATCH",new String[]{p.getName()}), Status.ERROR);
		      }
		    }
		  }
		}
	} finally {
		if (mc != null)
			mc.dispose();
	}
    return new SimpleStatus("");        
  }
  
  public Status validateProjectType(String projectName, SelectionListChoices runtime2ClientTypes)
  {
    Status status = new SimpleStatus("");
    IProject p = (IProject)((new StringToIProjectTransformer()).transform(projectName));
    if (p==null || !p.exists())
    {
      //Project does not exist which means a new project of the correct type will be creates
      //We're done. All is good.
      return status;
    }
      
    //If the project exists, we should see it in the project list for the selected client
    //project type.
    String[] projectNames = runtime2ClientTypes.getChoice().getChoice().getList().getList();
    for (int i=0; i<projectNames.length; i++)
    {
      if (projectName.equals(projectNames[i]))
      {
        //Found the project. All is good.
        return status;
      }
    }
    
    //Didn't find the project. Return an error.
    //Get the label for the client type id
    String clientTypeLabel = getClientTypeLabel(runtime2ClientTypes.getChoice().getList().getSelection());
    String message = msgUtils.getMessage("MSG_WRONG_CLIENT_PROJECT_TYPE",new String[]{projectName, clientTypeLabel});
    Status eStatus = new SimpleStatus("",message,Status.ERROR);
    return eStatus;
    
  }
  
  private String getClientTypeLabel( String type )
  {
    ClientProjectTypeRegistry registry         = ClientProjectTypeRegistry.getInstance();
    String                    clientTypeLabel  = null;
    
    clientTypeLabel = registry.getElementById(type).getAttribute("label");
    
    return clientTypeLabel;
  }  
  
  public boolean isProjectServiceProject(IProject p, String wsdlURL, WebServicesParser parser)
  {
    if (p==null || wsdlURL==null || wsdlURL.length()==0 || parser==null)
      return false;
    
    IResource wsXML = getWebServcesXML(p);
    if (wsXML==null)
      return false;
    
    
      //Make a list of all the wsdl-port's in webservices.xml
    WebServicesManager wsm = new WebServicesManager();
    WebServiceEditModel wsEditModel = wsm.getWSEditModel(p);
    WebServices ws = wsEditModel.getWebServices();
    Iterator wsDescs = ws.getWebServiceDescriptions().iterator();
    ArrayList wsdlPortList = new ArrayList();
    while(wsDescs.hasNext())
    {
      WebServiceDescription wsDesc = (WebServiceDescription)wsDescs.next();
      Iterator pcs = wsDesc.getPortComponents().iterator();
      while(pcs.hasNext())
      {
        PortComponent pc = (PortComponent)pcs.next();
        WSDLPort wsdlPort = pc.getWsdlPort();
        wsdlPortList.add(new QualifiedName(wsdlPort.getNamespaceURI(), wsdlPort.getLocalPart()));
      }
    }
    
    ArrayList portList = getPortNamesFromWsdl(wsdlURL, parser);

    //If any of the QualifiedNames in portList equals any of the QualifiedNames
    //in wsdlPortList, this is the service project. Return true.
    Object[] ports = portList.toArray();
    Object[] wsdlPorts = wsdlPortList.toArray();
    for (int i = 0; i < ports.length; i++)
    {
      QualifiedName portName = (QualifiedName) ports[i];
      for (int j = 0; j < wsdlPorts.length; j++)
      {
        QualifiedName wsdlPortName = (QualifiedName) wsdlPorts[j];
        if (portName.equals(wsdlPortName))
        {
          return true;
        }
      }
    }

    return false;
  }
  
  /*
   * @deprecated
   * 
   */
  private IResource getWebServcesXML(IProject p)
  {
	  // 
//    J2EENature nature = (J2EENature) J2EENature.getRegisteredRuntime(p);
//    if (nature == null)
//      return null;

//    IResource moduleRoot = nature.getModuleServerRoot();
//	IResource moduleRoot = ResourceUtils.getWebModuleServerRoot(p);
	IResource moduleRoot = J2EEUtils.getFirstWebContentContainer(p);  
    if (!(moduleRoot instanceof IContainer))
      return null;

    IResource webServicesXML=null;
//    if (nature instanceof J2EEWebNatureRuntime)
	if (ResourceUtils.isWebProject(p))
    {
      StringBuffer wsPath = new StringBuffer();
      wsPath.append("WEB-INF/");
      wsPath.append(WebServiceConstants.WEBSERVICE_DD_SHORT_NAME);
      webServicesXML = ((IContainer)moduleRoot).findMember(wsPath.toString()); 
    }
    else //Must be an Application Client Module or an EJB Module
    {
      StringBuffer wsPath = new StringBuffer();
      wsPath.append("META-INF/");
      wsPath.append(WebServiceConstants.WEBSERVICE_DD_SHORT_NAME);
      webServicesXML = ((IContainer)moduleRoot).findMember(wsPath.toString());     
    }
    
    return webServicesXML;
  }
  
  private ArrayList getPortNamesFromWsdl(String wsdlURL, WebServicesParser parser)
  {
  	ArrayList portNameList = new ArrayList();
  	Definition def = parser.getWSDLDefinition(wsdlURL);
    Map services = def.getServices();
    Service service = null;
    for (Iterator it = services.values().iterator(); it.hasNext();)
    {
      service = (Service)it.next();
      String namespace = service.getQName().getNamespaceURI();
      Map ports = service.getPorts();
      for (Iterator it2 = ports.values().iterator(); it2.hasNext();)
      {
      	Port port = (Port)it2.next();
      	portNameList.add(new QualifiedName(namespace, port.getName()));
      }
    }        
  	
  	return portNameList;
  	
  }
  
  private class QualifiedName
  {
    String namespaceURI;

    String localPart;

    /**
     * @param namespaceURI
     * @param localPart
     */
    public QualifiedName(String namespaceURI, String localPart)
    {
      super();
      this.namespaceURI = namespaceURI;
      this.localPart = localPart;
    }

    /**
     * @return Returns the localPart.
     */
    public String getLocalPart()
    {
      return localPart;
    }

    /**
     * @param localPart
     *          The localPart to set.
     */
    public void setLocalPart(String localPart)
    {
      this.localPart = localPart;
    }

    /**
     * @return Returns the namespaceURI.
     */
    public String getNamespaceURI()
    {
      return namespaceURI;
    }

    /**
     * @param namespaceURI
     *          The namespaceURI to set.
     */
    public void setNamespaceURI(String namespaceURI)
    {
      this.namespaceURI = namespaceURI;
    }

    public boolean equals(QualifiedName qn)
    {
      return (qn.getNamespaceURI().equals(namespaceURI) && qn.getLocalPart()
          .equals(localPart));
    }
  }
}

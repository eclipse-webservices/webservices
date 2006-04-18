/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060222   115834 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jem.util.emf.workbench.WorkbenchResourceHelperBase;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.webservice.internal.WebServiceConstants;
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.WSDLPort;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServices;
import org.eclipse.jst.j2ee.webservice.wsdd.WsddResource;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.VersionFormatException;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

/**
 *
 */
public class ValidationUtils
{

  /**
   * 
   */
  public ValidationUtils()
  {
  }
  
  public boolean doesServerSupportProject(String serverFactoryId, String projectName)
  {
    IProject project = ProjectUtilities.getProject(projectName);
    IFacetedProject fProject = null;
    if (project.exists())
    {
      try
      {
        fProject = ProjectFacetsManager.create(project);
      } catch (CoreException ce)
      {

      }
      
      if (fProject != null)
      {
        Set facets = fProject.getProjectFacets();
        return doesServerSupportFacets(serverFactoryId, facets);
      }
      else
      {
        //If it's not a faceted project, we have nothing to compare to - assume it's good.
        return true;
      }
    }
    else
    {
      //If the project doesn't exist, we have nothing to compare to - assume it's good.
      return true;      
    }
    
  }

  public boolean doesServerSupportFacets(String serverFactoryId, Set facets)
  {
    Set runtimes = FacetUtils.getRuntimes(new Set[]{facets});
    Iterator itr = runtimes.iterator();
    IServerType st = ServerCore.findServerType(serverFactoryId);        
    String runtimeTypeId = st.getRuntimeType().getId();
    while (itr.hasNext())
    {
      org.eclipse.wst.common.project.facet.core.runtime.IRuntime fRuntime = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime)itr.next();
      IRuntime sRuntime = FacetUtil.getRuntime(fRuntime);
      if (runtimeTypeId.equals(sRuntime.getRuntimeType().getId()))
      {
        //found a match
        return true;
      }
    }
    
    return false;    
  }
  
  public boolean doesServerSupportTemplate(String serverFactoryId, String templateId)
  {
    IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateId);
    Set templateFacets = template.getFixedProjectFacets();
    Iterator templateFacetsItr = templateFacets.iterator();
    while (templateFacetsItr.hasNext())
    {
      boolean serverSupportsThisOne = false;
      IProjectFacet fixedFacet = (IProjectFacet)templateFacetsItr.next();      
      List versions = null;
      try
      {
        versions = fixedFacet.getSortedVersions(true);
      } catch (VersionFormatException e)
      {
        Set versionSet = fixedFacet.getVersions();
        Iterator itr = versionSet.iterator();
        versions = new ArrayList();
        while (itr.hasNext())
        {
            versions.add(itr.next());
        }            
      } catch (CoreException e)
      {
        Set versionSet = fixedFacet.getVersions();
        Iterator itr = versionSet.iterator();
        versions = new ArrayList();
        while (itr.hasNext())
        {
            versions.add(itr.next());
        }            
      } 
      Iterator versionsItr = versions.iterator();
      while(versionsItr.hasNext())
      {
        IProjectFacetVersion pfv = (IProjectFacetVersion)versionsItr.next();
        Set pfvs = new HashSet();
        pfvs.add(pfv);
        if (doesServerSupportFacets(serverFactoryId, pfvs))
        {
          serverSupportsThisOne = true;
          break;
        }        
      }
      
      if (!serverSupportsThisOne)
      {
        return false;
      }
    }
    
    return true;
  }
  
  public IStatus validateProjectTargetAndJ2EE(String projectName, String compName, String earName, String earCompName, String serverFactoryId, String j2eeLevel)
  {
    IProject p = ProjectUtilities.getProject(projectName);
    IProject earP = null;
    if (earName!=null && !earName.equalsIgnoreCase("")) {
    	earP = ProjectUtilities.getProject(earName);
    }
    IStatus targetStatus = doesProjectTargetMatchServerType(p, serverFactoryId);
    if (earP!=null && targetStatus.getSeverity()==Status.OK)
    {
      //check the EAR      
      IStatus earTargetStatus = doesProjectTargetMatchServerType(earP, serverFactoryId);
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
    IStatus j2eeStatus = doesProjectMatchJ2EELevel(p, compName, j2eeLevel);
    if(earP!=null && j2eeStatus.getSeverity()==Status.OK)
    {
      IStatus earJ2EEStatus = doesProjectMatchJ2EELevel(earP, earCompName, j2eeLevel);
      if(earJ2EEStatus.getSeverity()==Status.ERROR)
      {
        return earJ2EEStatus;
      }
    }
    else
    {
      return j2eeStatus;
    }
    
    return Status.OK_STATUS;
  }
  
  private IStatus doesProjectTargetMatchServerType(IProject p, String serverFactoryId)
  {
    if (p!=null && p.exists())
    {
      IRuntime projectTarget = ServerSelectionUtils.getRuntimeTarget(p.getName());
      if (projectTarget!=null)
      {
        String projectTargetId = projectTarget.getRuntimeType().getId();
        String serverTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverFactoryId);
        if (serverTargetId!=null && serverTargetId.length()>0)
        {
          if(!projectTargetId.equals(serverTargetId))
          { 
            return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_SERVER_TARGET_MISMATCH,new String[]{p.getName()}) );
          }
        }
      }
    }
    return Status.OK_STATUS;        
  }

  private IStatus doesProjectMatchJ2EELevel(IProject p, String compName, String j2eeLevel)
  {

    try {
		if (p!=null && p.exists())
		{
  	  int projectJ2EELevel = J2EEUtils.getJ2EEVersion(p);
		  if (projectJ2EELevel!=-1)
		  {
		    String projectJ2EELevelString = String.valueOf(projectJ2EELevel);
		    if (j2eeLevel!=null && j2eeLevel.length()>0)
		    {
		      if (!projectJ2EELevelString.equals(j2eeLevel))
		      {
		        return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_J2EE_MISMATCH,new String[]{p.getName()}) );
		      }
		    }
		  }
		}
	} catch(Exception e){
    
  }
    
    return Status.OK_STATUS;        
  }
  
  public IStatus validateProjectType(String projectName, SelectionListChoices runtime2ClientTypes)
  {
    IStatus status = Status.OK_STATUS;
    IProject p = ProjectUtilities.getProject(projectName);
    if (p==null || !p.exists())
    {
      //Project does not exist which means a new project of the correct type will be created
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
    String message = NLS.bind(ConsumptionUIMessages.MSG_WRONG_CLIENT_PROJECT_TYPE,new String[]{projectName, clientTypeLabel});
    IStatus eStatus = StatusUtils.errorStatus( message );
    return eStatus;
    
  }
  
  
  private String getClientTypeLabel( String type )
  {	  
	  if (type.equals(IModuleConstants.JST_WEB_MODULE))
	  {
		  return ConsumptionUIMessages.LABEL_CLIENT_COMP_TYPE_WEB;
	  }
	  else if (type.equals(IModuleConstants.JST_EJB_MODULE))
	  {
		  return ConsumptionUIMessages.LABEL_CLIENT_COMP_TYPE_EJB;
	  }
	  else if (type.equals(IModuleConstants.JST_APPCLIENT_MODULE))
	  {
		  return ConsumptionUIMessages.LABEL_CLIENT_COMP_TYPE_APP_CLIENT;
	  }
	  else if (type.equals(IModuleConstants.JST_UTILITY_MODULE))
	  {
		  return ConsumptionUIMessages.LABEL_CLIENT_COMP_TYPE_CONTAINERLESS;
	  }
	  else
	  {
		  //No known label, return the typeId itself. 
		  return type;
	  }	  	  
  }  
  
  /**
   * 
   * @param p
   * @param wsdlURL
   * @param parser
   * @return
   */
  public boolean isProjectServiceProject(IProject p, String wsdlURL, WebServicesParser parser)
  {
    if (p==null || wsdlURL==null || wsdlURL.length()==0 || parser==null)
      return false;
    
    IResource wsXML = getWebServcesXML(p);
    if (wsXML==null)
      return false;
    
    
    //Make a list of all the wsdl-port's in webservices.xml
    if (!(wsXML instanceof IFile))
    {
      return false;
    }
     
    Resource res = WorkbenchResourceHelperBase.getResource((IFile)wsXML, true);
    WsddResource wsddRes = (WsddResource)res;    
    WebServices webServices = wsddRes.getWebServices();
    Iterator wsDescs = webServices.getWebServiceDescriptions().iterator();
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
  
  /**
   * 
   */
  private IResource getWebServcesXML(IProject p)
  {
    //Get the module root.    
    IResource moduleRoot = getModuleRoot(p);
    if (!(moduleRoot instanceof IContainer))
      return null;

    IResource webServicesXML = null;
    if (J2EEProjectUtilities.isDynamicWebProject(p))
    {
      StringBuffer wsPath = new StringBuffer();
      wsPath.append("WEB-INF/");
      wsPath.append(WebServiceConstants.WEBSERVICE_DD_SHORT_NAME);
      webServicesXML = ((IContainer) moduleRoot).findMember(wsPath.toString());
    }
    else
    {
      StringBuffer wsPath = new StringBuffer();
      wsPath.append("META-INF/");
      wsPath.append(WebServiceConstants.WEBSERVICE_DD_SHORT_NAME);
      webServicesXML = ((IContainer) moduleRoot).findMember(wsPath.toString());      
    }
    return webServicesXML;
  }
  
  private IResource getModuleRoot(IProject p)
  {
    IPath modulePath = null;
    try 
    {
      IVirtualComponent vc = ComponentCore.createComponent(p);
      if (vc != null) 
      {
        modulePath = vc.getRootFolder().getWorkspaceRelativePath();
      }
    }
    catch(Exception ex)
    {
      
    } 
    
    IResource res = ResourceUtils.findResource(modulePath);
    return res;
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
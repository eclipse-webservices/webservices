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
 * 20060204 124408   rsinha@ca.ibm.com - Rupam Kuehner      *     
 * 20060217   126757 rsinha@ca.ibm.com - Rupam Kuehner
 * 20070201   172244 makandre@ca.ibm.com - Andrew Mak, Remove usage of deprecated (and now removed) classes
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.VersionFormatException;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;

public class CreateModuleCommand extends AbstractDataModelOperation
{
	
	public final static int WEB = J2EEUtils.WEB;    
	public final static int EJB = J2EEUtils.EJB;    
	public final static int APPCLIENT = J2EEUtils.APPCLIENT;
	public final static int EAR = J2EEUtils.EAR;
    
    //Templates
    //TODO  Remove these template constants once J2EE tools defines constants (bug 117531)
    public final static String WEB_TEMPLATE = "template.jst.web";
    public final static String EJB_TEMPLATE = "template.jst.ejb";
    public final static String APPCLIENT_TEMPLATE = "template.jst.appclient";
    public final static String EAR_TEMPLATE = "template.jst.ear";
	
	private String   projectName;
	private String   moduleName;  // may be null for non-flexible project
	private int      moduleType;;
	private String   j2eeLevel;
	private String   serverFactoryId;
	private String   serverInstanceId_;
	
    private org.eclipse.wst.common.project.facet.core.runtime.IRuntime facetRuntime;
    
	public CreateModuleCommand(){
	}
	
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
		IStatus status = Status.OK_STATUS;
		
		// check if data ready
		status = checkDataReady();
		if (status.getSeverity()==Status.ERROR){
			return status;
		}	

        //ensure the project does not exist
        IProject project = ProjectUtilities.getProject(projectName);
        if (!project.exists())
        {
          // create the component according to the component type specified
          int type = getModuleType();
          switch (type) {
          case WEB:
            status = createWebComponent();
            break;
          case EJB:
             status = createEJBComponent();
            break;
          case APPCLIENT:
            status = createAppClientComponent();
            break;
          case EAR:
            status = createEARComponent();
            break;

          default:
            return StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_COMPONENT_CREATION, new String[]{moduleName}) );         
          }          
        }
		
        
        // check if flexible project exists
        /*
		if (project==null || !project.exists()){
			status = createFlexibleJavaProject();
			if (status.getSeverity()==Status.ERROR){
				return status;
			}			
		}
        */
        /*
		// check if project and/or component exists
        if (projectName!=null) {
          if (moduleName==null){
            if (project.exists())
              return status;
          }
          else {
			if (J2EEUtils.exists(projectName, moduleName))
				return status;
          }
        }
		else {
			return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_COMPONENT_CREATION, new String[]{projectName, moduleName}) );
		} 
        */       
        

		
	
		return status;
	}

	private IStatus checkDataReady(){
		
		if (projectName==null || serverFactoryId==null){
			return StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_COMPONENT_CREATION, new String[]{projectName, moduleName}) );
		}
		
		return Status.OK_STATUS;
	}
	
	
	/**
	 * Create a Web component
	 * @return
	 */
	public IStatus createWebComponent(){   
    
      IStatus status = Status.OK_STATUS;      
      
      try
      {        
        status = FacetUtils.createNewFacetedProject(projectName);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }        
        IProject createdProject = ProjectUtilities.getProject(projectName);
        IFacetedProject fproject = ProjectFacetsManager.create(createdProject);        
        
        //Decide which facets to install based on the templateId and the selected server. 
        Set facetsToAdd = getFacetsToAdd(WEB_TEMPLATE); 
        Set facetsToAddModified = facetsToAdd;
        //If the J2EE level has been set, modify the Web facet version to be consistent.
        if (j2eeLevel!=null && j2eeLevel.length()>0)
        {
          String webVersion = getWebVersionFromJ2EELevel(j2eeLevel);
          facetsToAddModified = new HashSet();
          Iterator itr = facetsToAdd.iterator();
          while (itr.hasNext())
          {
            //If this is the web facet, get the right version.
            IProjectFacetVersion pfv = (IProjectFacetVersion)itr.next();
            IProjectFacet pf = pfv.getProjectFacet(); 
            if (pf.getId().equals(IModuleConstants.JST_WEB_MODULE))
            {
              IProjectFacetVersion webfv = pf.getVersion(webVersion);
              facetsToAddModified.add(webfv);
            }
            else
            {
              facetsToAddModified.add(pfv); 
            }
          }
        }
        
        status = FacetUtils.addFacetsToProject(fproject, facetsToAddModified);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }                
        
        Set newFacetVersions = fproject.getProjectFacets();
        Set fixedFacets = new HashSet();
        for (Iterator iter = newFacetVersions.iterator(); iter.hasNext();) {
            IProjectFacetVersion facetVersion = (IProjectFacetVersion) iter.next();
            fixedFacets.add(facetVersion.getProjectFacet());
        }
        
        status = FacetUtils.setFixedFacetsOnProject(fproject, fixedFacets);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }                        
        
        //Set the runtime        
        if (facetRuntime != null)
        {
          status = FacetUtils.setFacetRuntimeOnProject(fproject, facetRuntime);
        }        
        
      } catch (CoreException ce)
      {
        return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), ce);
      }
      
      return status;
      		
	}
	
	/**
	 * Create an EAR component
	 * @return
	 */
	public IStatus createEARComponent(){
      IStatus status = Status.OK_STATUS;      
      
      try
      {
        status = FacetUtils.createNewFacetedProject(projectName);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }        
        IProject createdProject = ProjectUtilities.getProject(projectName);
        IFacetedProject fproject = ProjectFacetsManager.create(createdProject);        

        //Decide which facets to install based on the templateId and the selected server. 
        Set facetsToAdd = getFacetsToAdd(EAR_TEMPLATE); 
        Set facetsToAddModified = facetsToAdd;
        //If the J2EE level has been set, modify the Web facet version to be consistent.
        if (j2eeLevel!=null && j2eeLevel.length()>0)
        {
          String webVersion = getEARVersionFromJ2EELevel(j2eeLevel);
          facetsToAddModified = new HashSet();
          Iterator itr = facetsToAdd.iterator();
          while (itr.hasNext())
          {
            //If this is the ear facet, get the right version.
            IProjectFacetVersion pfv = (IProjectFacetVersion)itr.next();
            IProjectFacet pf = pfv.getProjectFacet(); 
            if (pf.getId().equals(IModuleConstants.JST_EAR_MODULE))
            {
              IProjectFacetVersion webfv = pf.getVersion(webVersion);
              facetsToAddModified.add(webfv);
            }
            else
            {
              facetsToAddModified.add(pfv); 
            }
          }
        }
        
        status = FacetUtils.addFacetsToProject(fproject, facetsToAddModified);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }                
        
        Set newFacetVersions = fproject.getProjectFacets();
        Set fixedFacets = new HashSet();
        for (Iterator iter = newFacetVersions.iterator(); iter.hasNext();) {
            IProjectFacetVersion facetVersion = (IProjectFacetVersion) iter.next();
            fixedFacets.add(facetVersion.getProjectFacet());
        }
        
        status = FacetUtils.setFixedFacetsOnProject(fproject, fixedFacets);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }                        
        
        //Set the runtime        
        if (facetRuntime != null)
        {
          status = FacetUtils.setFacetRuntimeOnProject(fproject, facetRuntime);
        }        
        
      } catch (CoreException ce)
      {        
        return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), ce);
      }
        
      return status;
	}
	
	/**
	 * Create an EJB Component
	 * @return
	 */
	public IStatus createEJBComponent(){
      IStatus status = Status.OK_STATUS;      
      
      try
      {
        status = FacetUtils.createNewFacetedProject(projectName);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }        
        IProject createdProject = ProjectUtilities.getProject(projectName);
        IFacetedProject fproject = ProjectFacetsManager.create(createdProject);        

        //Decide which facets to install based on the templateId and the selected server. 
        Set facetsToAdd = getFacetsToAdd(EJB_TEMPLATE); 
        Set facetsToAddModified = facetsToAdd;
        //If the J2EE level has been set, modify the Web facet version to be consistent.
        if (j2eeLevel!=null && j2eeLevel.length()>0)
        {
          String webVersion = getEJBVersionFromJ2EELevel(j2eeLevel);
          facetsToAddModified = new HashSet();
          Iterator itr = facetsToAdd.iterator();
          while (itr.hasNext())
          {
            //If this is the web facet, get the right version.
            IProjectFacetVersion pfv = (IProjectFacetVersion)itr.next();
            IProjectFacet pf = pfv.getProjectFacet(); 
            if (pf.getId().equals(IModuleConstants.JST_EJB_MODULE))
            {
              IProjectFacetVersion webfv = pf.getVersion(webVersion);
              facetsToAddModified.add(webfv);
            }
            else
            {
              facetsToAddModified.add(pfv); 
            }
          }
        }
        
        status = FacetUtils.addFacetsToProject(fproject, facetsToAddModified);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }                
        
        Set newFacetVersions = fproject.getProjectFacets();
        Set fixedFacets = new HashSet();
        for (Iterator iter = newFacetVersions.iterator(); iter.hasNext();) {
            IProjectFacetVersion facetVersion = (IProjectFacetVersion) iter.next();
            fixedFacets.add(facetVersion.getProjectFacet());
        }

        status = FacetUtils.setFixedFacetsOnProject(fproject, fixedFacets);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }                        
        
        //Set the runtime        
        if (facetRuntime != null)
        {
          status = FacetUtils.setFacetRuntimeOnProject(fproject, facetRuntime);
        }        
        
      } catch (CoreException ce)
      {
        return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_CREATE_EJB_COMPONENT, new String[] { projectName }), ce);
      }
      
      return status;      

	}
	
	/**
	 * Create an Application Client component
	 * @return
	 */
	public IStatus createAppClientComponent()
    {
      IStatus status = Status.OK_STATUS;      
      
      try
      {
        status = FacetUtils.createNewFacetedProject(projectName);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }        
        IProject createdProject = ProjectUtilities.getProject(projectName);
        IFacetedProject fproject = ProjectFacetsManager.create(createdProject);        

        //Decide which facets to install based on the templateId and the selected server. 
        Set facetsToAdd = getFacetsToAdd(APPCLIENT_TEMPLATE); 
        Set facetsToAddModified = facetsToAdd;
        //If the J2EE level has been set, modify the Web facet version to be consistent.
        if (j2eeLevel!=null && j2eeLevel.length()>0)
        {
          String webVersion = getAppClientVersionFromJ2EELevel(j2eeLevel);
          facetsToAddModified = new HashSet();
          Iterator itr = facetsToAdd.iterator();
          while (itr.hasNext())
          {
            //If this is the web facet, get the right version.
            IProjectFacetVersion pfv = (IProjectFacetVersion)itr.next();
            IProjectFacet pf = pfv.getProjectFacet(); 
            if (pf.getId().equals(IModuleConstants.JST_APPCLIENT_MODULE))
            {
              IProjectFacetVersion webfv = pf.getVersion(webVersion);
              facetsToAddModified.add(webfv);
            }
            else
            {
              facetsToAddModified.add(pfv); 
            }
          }
        }
        
        status = FacetUtils.addFacetsToProject(fproject, facetsToAddModified);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }                
        
        Set newFacetVersions = fproject.getProjectFacets();
        Set fixedFacets = new HashSet();
        for (Iterator iter = newFacetVersions.iterator(); iter.hasNext();) {
            IProjectFacetVersion facetVersion = (IProjectFacetVersion) iter.next();
            fixedFacets.add(facetVersion.getProjectFacet());
        }

        status = FacetUtils.setFixedFacetsOnProject(fproject, fixedFacets);
        if (status.getSeverity() == IStatus.ERROR)
        {
          return status;
        }                        
        
        //Set the runtime        
        if (facetRuntime != null)
        {
          status = FacetUtils.setFacetRuntimeOnProject(fproject, facetRuntime);
        }        
        
      } catch (CoreException ce)
      {
        return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_CREATE_APPCLIENT_COMPONENT, new String[] { projectName }), ce);
      }
        
      return status;      
	}
	
      /*
       * @return Set Returns the Set of facets to add to the new project, 
       * choosing the highest level of each facet that works on the selected server.
       */
      private Set getFacetsToAdd(String templateId)
      {
        Set facets = new HashSet();
        
        //Set the facet runtime.
        setFacetRuntime();
        if (facetRuntime != null)
        {
          IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateId);
          Set templateFacets = template.getFixedProjectFacets();
          Iterator templateFacetsItr = templateFacets.iterator();
          while (templateFacetsItr.hasNext())
          {
            IProjectFacet fixedFacet = (IProjectFacet)templateFacetsItr.next();
            List versions = null;
            try {
                versions = fixedFacet.getSortedVersions(false);
            } catch (VersionFormatException e) {
                Set versionSet = fixedFacet.getVersions();
                Iterator itr = versionSet.iterator();
                versions = new ArrayList();
                while (itr.hasNext())
                {
                    versions.add(itr.next());
                }            
            } catch (CoreException e) {
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
              
              //Check against Runtime
              if (FacetUtils.doesRuntimeSupportFacets(facetRuntime, pfvs))
              {
                  //We have a match. Add this one to the master set.
                  facets.add(pfv);
                  break;
              }                        
            }
          }
        }
        else
        {
          facets = FacetUtils.getInitialFacetVersionsFromTemplate(templateId);
        }
     
        
        return facets;
      }
      
      private void setFacetRuntime()
      {
        
        if (serverInstanceId_ != null && serverInstanceId_.length()>0)
        {
          IServer server = ServerCore.findServer(serverInstanceId_);
          IRuntime sRuntime = server.getRuntime();
          facetRuntime = FacetUtil.getRuntime(sRuntime);      
        }
        else
        {
          //Find a non Stub runtime that matches this server type
          IRuntime serverRuntime = ServerUtils.getNonStubRuntime(serverFactoryId);
          if (serverRuntime != null)
          {
            facetRuntime = FacetUtil.getRuntime(serverRuntime);
          }
          else
          {
            //Accept stub runtime.
            IServerType st = ServerCore.findServerType(serverFactoryId);
            String runtimeTypeId = st.getRuntimeType().getId();   
            //Find the facet runtime
            IRuntime[] runtimes = ServerCore.getRuntimes();
            for (int i=0; i<runtimes.length; i++)
            {
              IRuntime sRuntime = runtimes[i];
              if (sRuntime.getRuntimeType().getId().equals(runtimeTypeId))
              {
                facetRuntime = FacetUtil.getRuntime(sRuntime);
              }
            }                
          }
        }
      }      
    
   private String getWebVersionFromJ2EELevel(String j2eeVersion)
   {
        int aVersion = Integer.valueOf(j2eeLevel).intValue();
        switch (aVersion) {
        case J2EEVersionConstants.J2EE_1_2_ID:
            return "2.2";

        case J2EEVersionConstants.J2EE_1_3_ID:
            return "2.3";

        case J2EEVersionConstants.J2EE_1_4_ID:
            return "2.4";
            
        default:
            return "2.4";
        }                
   }
	
   private String getEJBVersionFromJ2EELevel(String j2eeVersion)
   {
        int aVersion = Integer.valueOf(j2eeLevel).intValue();
        switch (aVersion) {
        case J2EEVersionConstants.J2EE_1_2_ID:
            return "1.1";

        case J2EEVersionConstants.J2EE_1_3_ID:
            return "2.0";

        case J2EEVersionConstants.J2EE_1_4_ID:
            return "2.1";
            
        default:
            return "2.1";
        }                
   }
   
   private String getAppClientVersionFromJ2EELevel(String j2eeVersion)
   {
        int aVersion = Integer.valueOf(j2eeLevel).intValue();
        switch (aVersion) {
        case J2EEVersionConstants.J2EE_1_2_ID:
            return "1.2";

        case J2EEVersionConstants.J2EE_1_3_ID:
            return "1.3";

        case J2EEVersionConstants.J2EE_1_4_ID:
            return "1.4";
            
        default:
            return "1.4";
        }                
   }
   
   private String getEARVersionFromJ2EELevel(String j2eeVersion)
   {
        int aVersion = Integer.valueOf(j2eeLevel).intValue();
        switch (aVersion) {
        case J2EEVersionConstants.J2EE_1_2_ID:
            return "1.2";

        case J2EEVersionConstants.J2EE_1_3_ID:
            return "1.3";

        case J2EEVersionConstants.J2EE_1_4_ID:
            return "1.4";
            
        default:
            return "1.4";
        }                
   }
   
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}	

	public int getModuleType()
	{
		return moduleType;
	}

	public void setModuleType(int moduleType)
	{
		this.moduleType = moduleType;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	/**
	 * Expecting 12,13,14 etc.
	 * @param level
	 */
	public void setJ2eeLevel(String level)
	{
		if (level !=null && level.indexOf(".")!=-1)
			j2eeLevel = J2EEUtils.getJ2EEIntVersionAsString(level);
		else
			j2eeLevel = level;
	}

	public void setServerFactoryId(String serverFactoryId)
	{
		this.serverFactoryId = serverFactoryId;
	}
	
	public void setServerInstanceId( String serverInstanceId )
	{
	  serverInstanceId_ = serverInstanceId;
	}

}

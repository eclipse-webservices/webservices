/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

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
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.common.FacetMatcher;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;

public class CreateFacetedProjectCommand extends AbstractDataModelOperation
{

  private String   projectName;
  private String   templateId;
  private RequiredFacetVersion[]   requiredFacetVersions;
  //private FacetMatcher facetMatcher;
  private String   serverFactoryId;
  private String   serverInstanceId;
  
  private org.eclipse.wst.common.project.facet.core.runtime.IRuntime facetRuntime;
  
  private IProgressMonitor monitor_;
  
  
  public IStatus execute(IProgressMonitor monitor, IAdaptable info)
  {
    monitor_ = monitor;
    IStatus status = Status.OK_STATUS;
    
    // check if data ready
    status = checkDataReady();
    if (status.getSeverity()==Status.ERROR)
    {
      return status;
    }

    IProject project = ProjectUtilities.getProject(projectName);
    if (!project.exists())
    {
      try
      {
        IFacetedProject fproject = ProjectFacetsManager.create(projectName, null, monitor_);
        
        //Decide which facets to install based on the templateId and the selected server. 
        Set facetsToAdd = getFacetsToAdd();
        
        //Set up the install actions.
        Set actions = FacetUtils.getInstallActions(facetsToAdd);
        /*
        IProjectFacetVersion webFacet24 = ProjectFacetsManager.getProjectFacet("jst.web").getVersion("2.4");
        IProjectFacetVersion javaFacet14 = ProjectFacetsManager.getProjectFacet("jst.java").getVersion("1.4");
        Set facetsToAdd = new HashSet();
        facetsToAdd.add(webFacet24);
        facetsToAdd.add(javaFacet14);
        actions = FacetUtils.getInstallActions(facetsToAdd);
        */
        fproject.modify(actions, monitor_);
        
        Set newFacetVersions = fproject.getProjectFacets();
        Set fixedFacets = new HashSet();
        for (Iterator iter = newFacetVersions.iterator(); iter.hasNext();) {
            IProjectFacetVersion facetVersion = (IProjectFacetVersion) iter.next();
            fixedFacets.add(facetVersion.getProjectFacet());
        }
        fproject.setFixedProjectFacets(fixedFacets);
        
        
        //Set the runtime        
        if (facetRuntime != null)
        {
          fproject.setRuntime(facetRuntime, monitor);
        }

 
      } catch (CoreException ce)
      {
        System.out.println("Exception occurred when creating a faceted project.");
        return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }));
      }
    }
    else
    {
      //TODO Just add the facets you need.
    }
    return status;
  }
    
  private IStatus checkDataReady()
  {

    if (projectName == null || serverFactoryId == null)
    {
      return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] {projectName}));
    }

    return Status.OK_STATUS;
  }
  
  /*
   * @return Set Returns the Set of facets to add to the new project, 
   * choosing the highest level of each facet that works on the selected server.
   */
  private Set getFacetsToAdd()
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
        //IProjectFacetVersion[] versions = FacetUtils.getOrderedVersions(fixedFacet);
        List versions = fixedFacet.getSortedVersions(false); 
        Iterator versionsItr = versions.iterator();
        while(versionsItr.hasNext())
        //for (int i=0; i<versions.length; i++)
        {
          IProjectFacetVersion pfv = (IProjectFacetVersion)versionsItr.next();
          Set pfvs = new HashSet();
          pfvs.add(pfv);
          
          //Check against RequiredFacetVersions
          FacetMatcher fm = FacetUtils.match(requiredFacetVersions, pfvs);
          if (fm.isMatch())
          {
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
    }
    else
    {
      facets = FacetUtils.getInitialFacetVersionsFromTemplate(templateId);
    }
 
    
    return facets;
  }

  private void setFacetRuntime()
  {
    
    if (serverInstanceId != null && serverInstanceId.length()>0)
    {
      IServer server = ServerCore.findServer(serverInstanceId);
      IRuntime sRuntime = server.getRuntime();
      facetRuntime = FacetUtil.getRuntime(sRuntime);      
    }
    else
    {
      IServerType st = ServerCore.findServerType(serverFactoryId);
      String runtimeTypeId = st.getRuntimeType().getId();   
      //Find the facet runtime
      IRuntime[] runtimes = ServerCore.getRuntimes();
      for (int i=0; i<runtimes.length; i++)
      {
        IRuntime sRuntime = runtimes[i];
        if ( !sRuntime.isStub() && sRuntime.getRuntimeType().getId().equals(runtimeTypeId))
        {
          facetRuntime = FacetUtil.getRuntime(sRuntime);
        }
      }
    }
  }
  
  /*
  public void setFacetMatcher(FacetMatcher facetMatcher)
  {
    this.facetMatcher = facetMatcher;
  }
  */
  
  public void setProjectName(String projectName)
  {
    this.projectName = projectName;
  }  
  
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }    

  public void setRequiredFacetVersions(RequiredFacetVersion[] requiredFacetVersions)
  {
    this.requiredFacetVersions = requiredFacetVersions;
  }

  public void setServerFactoryId(String serverFactoryId)
  {
    this.serverFactoryId = serverFactoryId;
  }

  public void setServerInstanceId(String serverInstanceId)
  {
    this.serverInstanceId = serverInstanceId;
  }

  
  

}

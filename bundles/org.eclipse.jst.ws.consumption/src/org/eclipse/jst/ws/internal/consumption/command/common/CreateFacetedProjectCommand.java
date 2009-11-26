/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * 20060204 124408   rsinha@ca.ibm.com - Rupam Kuehner      
 * 20060217 126757   rsinha@ca.ibm.com - Rupam Kuehner
 * 20060221 119111   rsinha@ca.ibm.com - Rupam Kuehner
 * 20060427 126780   rsinha@ca.ibm.com - Rupam Kuehner
 * 20060517 126965   kathy@ca.ibm.com - Kathy Chan
 * 20060905   156230 kathy@ca.ibm.com - Kathy Chan, Handling projects with no target runtime
 * 20070505   184772 kathy@ca.ibm.com - Kathy Chan
 * 20080305   220371 kathy@ca.ibm.com - Kathy Chan
 * 20080305   220371 kathy@ca.ibm.com - Kathy Chan - reverting previous change to 220371
 * 20080326   220371 kathy@ca.ibm.com - Kathy Chan - re-applying 220371
 * 20081001   243869 ericdp@ca.ibm.com - Eric D. Peters, Web Service tools allowing mixed J2EE levels
 * 20091123   242615 mahutch@ca.ibm.com - Mark Hutchinson, Use project creation operations to create projects
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.EjbFacetProjectCreationDataModelProvider;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.j2ee.internal.project.facet.UtilityFacetInstallDataModelProvider;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebFacetProjectCreationDataModelProvider;
import org.eclipse.jst.j2ee.project.facet.AppClientFacetInstallDataModelProvider;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetProjectCreationDataModelProperties;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.common.FacetMatcher;
import org.eclipse.jst.ws.internal.consumption.common.FacetSetsByTemplateCache;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties.FacetDataModelMap;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
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

  //name of the project to be created
  private String   projectName; 

  //name of the EAR project associated with projectName
  private String   earProjectName; 

  //id of the IFacetedProjectTemplate to be used to create this project.
  private String   templateId;
  
  //required facet versions that must be satisfied when creating the project (this comes from
  //the selected serverRuntime or clientRuntime in the calling scenario)
  private RequiredFacetVersion[]   requiredFacetVersions;

  //server type id - used to determine the facet runtime the created project will be bound to
  //if serverInstanceId is null or empty
  private String   serverFactoryId;

  //server id - used to determine the facet runtime the created project will be bound to. May
  //be null or empty.
  private String   serverInstanceId;
  
  //facet runtime determined from the serverInstanceId or serverFactoryId
  private org.eclipse.wst.common.project.facet.core.runtime.IRuntime facetRuntime;
  
  
  private boolean containsWebFacet(Set facetVersions) {
	  for (Iterator iter = facetVersions.iterator(); iter.hasNext();) {
		  IProjectFacetVersion requiredFacetVersion = (IProjectFacetVersion) iter.next();
		  if (requiredFacetVersion.getProjectFacet().getId().equals(IModuleConstants.JST_WEB_MODULE)) {
			  return true;
		  }
	  }
	  return false;
  }
  
  //return the project creation data model for the templateId or null if no data model found
  private IDataModel getDataModelForTemplate(Set requiredFixedFacets) {
	  IDataModel dm = null;	  
	  if (templateId == null) 
		  return null;
	  else if (templateId.equals(IJ2EEModuleConstants.JST_EJB_TEMPLATE)) {
		  dm = DataModelFactory.createDataModel(new EjbFacetProjectCreationDataModelProvider());  
		  dm.setProperty(IJ2EEFacetProjectCreationDataModelProperties.FACET_PROJECT_NAME, projectName);
		  dm.setBooleanProperty(IJ2EEFacetProjectCreationDataModelProperties.ADD_TO_EAR, false);
		  dm.setProperty(IJ2EEFacetProjectCreationDataModelProperties.FACET_RUNTIME, facetRuntime);
	  }
	  else if (templateId.equals(IJ2EEModuleConstants.JST_APPCLIENT_TEMPLATE)) {
		  dm = DataModelFactory.createDataModel(new AppClientFacetInstallDataModelProvider());
		  dm.setProperty(IJ2EEFacetProjectCreationDataModelProperties.FACET_PROJECT_NAME, projectName);
		  dm.setProperty(IJ2EEFacetProjectCreationDataModelProperties.FACET_RUNTIME, facetRuntime);		  
	  }
	  else if (templateId.equals(IJ2EEModuleConstants.JST_UTILITY_TEMPLATE)) {
		  dm = DataModelFactory.createDataModel(new UtilityFacetInstallDataModelProvider());
		  dm.setProperty(IJ2EEFacetProjectCreationDataModelProperties.FACET_PROJECT_NAME, projectName);
		  dm.setProperty(IJ2EEFacetProjectCreationDataModelProperties.FACET_RUNTIME, facetRuntime);
	  }
	  else if (templateId.equals(IJ2EEModuleConstants.JST_WEB_TEMPLATE) || containsWebFacet(requiredFixedFacets)) {
		  //we want any project with the web template ID, or that contains a jst.web facet to be created with this data model
		  dm = DataModelFactory.createDataModel(new WebFacetProjectCreationDataModelProvider());
		  dm.setProperty(IJ2EEFacetProjectCreationDataModelProperties.FACET_PROJECT_NAME, projectName);
		  dm.setBooleanProperty(IJ2EEFacetProjectCreationDataModelProperties.ADD_TO_EAR, false);
		  dm.setProperty(IJ2EEFacetProjectCreationDataModelProperties.FACET_RUNTIME, facetRuntime);	  		  
	  }
	  return dm;
  }
  
  public IStatus execute(IProgressMonitor monitor, IAdaptable info)
  {
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
		  try{
			  IProject createdProject;
			  IFacetedProject fproject;
			  Set facetsToAdd = getFacetsToAdd();

			  IDataModel dm = getDataModelForTemplate(facetsToAdd);
			  if (dm != null) {
				  //we found a project creation data model for this template id.  use it to create the project

				  Set missingFacets = new HashSet();

				  //here we look at the facet versions in facetsToAdd and set the version on the data model to match
				  //if no facet data model is found we add it to the missingFacets collection to add later  
				  if (dm.isProperty(IFacetProjectCreationDataModelProperties.FACET_DM_MAP)) {
					  for (Iterator iter = facetsToAdd.iterator(); iter.hasNext();) {
						  IProjectFacetVersion requiredFacetVersion = (IProjectFacetVersion) iter.next();
						  final FacetDataModelMap map = (FacetDataModelMap) dm.getProperty(IFacetProjectCreationDataModelProperties.FACET_DM_MAP);
						  IDataModel facetDM = map.getFacetDataModel(requiredFacetVersion.getProjectFacet().getId());
						  if (facetDM != null) {
							  facetDM.setStringProperty(IFacetDataModelProperties.FACET_VERSION_STR,  requiredFacetVersion.getVersionString());
						  } else {
							  missingFacets.add(requiredFacetVersion);
						  }
					  }
				  }

				  dm.getDefaultOperation().execute(null, null);
				  createdProject = ProjectUtilities.getProject(projectName);
				  fproject = ProjectFacetsManager.create(createdProject);

				  //add any facets that were not added by the project creation data model
				  if (missingFacets.size() > 0) {
					  status = FacetUtils.addFacetsToProject(fproject, missingFacets);
					  if (status.getSeverity() == IStatus.ERROR) {
						  return status;
					  }
				  }
			  }
			  else {
				  //did not find a project creation data model.  create the project and install facets manually
				  status = FacetUtils.createNewFacetedProject(projectName);
				  if (status.getSeverity() == IStatus.ERROR) {
					  return status;
				  }

				  createdProject = ProjectUtilities.getProject(projectName);
				  fproject = ProjectFacetsManager.create(createdProject);

				  //Install the facet versions
				  status = FacetUtils.addFacetsToProject(fproject, facetsToAdd);
				  if (status.getSeverity() == IStatus.ERROR) {
					  return status;
				  }
				  //Set the installed facet versions as fixed.
				  Set newFacetVersions = fproject.getProjectFacets();
				  Set fixedFacets = new HashSet();
				  for (Iterator iter = newFacetVersions.iterator(); iter.hasNext();) {
					  IProjectFacetVersion facetVersion = (IProjectFacetVersion) iter.next();
					  fixedFacets.add(facetVersion.getProjectFacet());
				  }
				  status = FacetUtils.setFixedFacetsOnProject(fproject, fixedFacets);
				  if (status.getSeverity() == IStatus.ERROR) {
					  return status;
				  }
			  }

			  //Set the runtime        
			  if (facetRuntime != null) {
				  status = FacetUtils.setFacetRuntimeOnProject(fproject, facetRuntime);
			  }

			  // add facets required by Web service runtime
			  if (requiredFacetVersions.length != 0) {
				  status = FacetUtils.addRequiredFacetsToProject(project, requiredFacetVersions, monitor);
				  if (status.getSeverity() == Status.ERROR) {
					  return status;
				  }
			  }

			  if (facetRuntime != null) {
				  // add the default facets that's not in conflict with the existing facets
				  Set projectFacetVersionSet = fproject.getProjectFacets();
				  Set projectFacetSet = new HashSet();
				  // get the project facet from the project facet version we calculated
				  for (Iterator iter = projectFacetVersionSet.iterator(); iter.hasNext();) {
					  IProjectFacetVersion pfv = (IProjectFacetVersion) iter.next();
					  projectFacetSet.add(pfv.getProjectFacet());
				  }
				  try {
					  Set defaultProjectFacetVersionSet = facetRuntime.getDefaultFacets(projectFacetSet);
					  // The returned defaultFacetSet contains the original projectFacetSet passed into getDefaultFacets
					  // plus any default facets that are not in conflict with the original projectFacetSet.
					  // Add to facetsToAdd if the the default facet is not in the original set.
					  Set defaultFacetsToAdd = new HashSet();
					  for (Iterator iter = defaultProjectFacetVersionSet.iterator(); iter.hasNext();) {
						  IProjectFacetVersion defaultProjFacetVersion = (IProjectFacetVersion) iter.next();
						  if( ! projectFacetSet.contains( defaultProjFacetVersion.getProjectFacet() ) )
						  {
							  defaultFacetsToAdd.add(defaultProjFacetVersion);
						  }
					  }
					  if (!defaultFacetsToAdd.isEmpty()) {
						  status = FacetUtils.addFacetsToProject(fproject, defaultFacetsToAdd);
						  if (status.getSeverity() == IStatus.ERROR) {
							  return status;
						  }
					  }
				  } catch (CoreException e) {
					  // If there's any exception when trying to get the default facet, just ignore the default facet
					  // and return the original facetsToAdd.
				  }
			  }
		  }
		  catch (CoreException ce) {
			  return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }));
		  }
		  catch (ExecutionException e) {
			  return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }));
		  }

	  }

	  return status;
  }
    
  private IStatus checkDataReady()
  {

	if (projectName == null)
    {
      return StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] {projectName}));
    }

    return Status.OK_STATUS;
  }
  
  /**
   * Returns the set of facets to install on the new project. The set will consist
   * of the highest version of each of the template's fixed facets that satisfies
   * the required facet versions, the facet runtime, and the EAR project J2EE level.
   * @return Set a Set containing elements of type IProjectFacetVersion.
   */
  private Set getFacetsToAdd()
  {
    Set facets = null;
    
    //Set the facet runtime.
    setFacetRuntime();
    //Get all facet version combinations for the template in order of ascending version numbers.
    Set[] allCombinations = FacetSetsByTemplateCache.getInstance().getFacetVersionCombinationsFromTemplate(templateId);
    int n = allCombinations.length;
    if (facetRuntime != null)
    {
      //Walk the facet version combinations in order of descending version numbers.
      for (int i=n-1; i>=0; i--)
      {
        //Check this template combination to see if it is compatible with the 
        //required facet versions, the server runtime, and the EAR. If it is, choose it.
        Set combination = allCombinations[i];
        FacetMatcher fm = FacetUtils.match(requiredFacetVersions, combination);
        if (fm.isMatch())
        {
          //Check against Runtime & EAR
          if (FacetUtils.doesRuntimeSupportFacets(facetRuntime, combination)  && doesEARSupportFacets(combination) )
          {
            //We have a match. Use this combination of facet versions for project creation.
            facets = combination;
            break;
          }
        }                
      }
      
    }
    else
    {
      for (int i=n-1; i>=0; i--)
      {
        //Check this template combination to see if it is compatible with both the 
        //service/client runtime and the server runtime. If it is, choose it.
        Set combination = allCombinations[i];
        FacetMatcher fm = FacetUtils.match(requiredFacetVersions, combination);
        if (fm.isMatch())
        {
            //We have a match. Use this combination of facet versions for project creation.
            facets = combination;
            break;
        }                
      }      
    }
   
    //Unlikely to get to this point in the code, but if we do, choose the highest version
    //of each fixed facet in the template that is supported by the server runtime
    if (facets == null)
    {
      facets = getHighestFacetVersionsFromTemplateAndServer();
    }
     
    return facets;
  }

  /**
   * Sets the facetRuntime attribute based on the serverInstanceId or serverFactoryId
   * Preference is given to non-stub facet runtimes.
   */
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
		  if (serverFactoryId != null && serverFactoryId.length() > 0)
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
  }  
  
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
  public void setEarProjectName(String earProject) {
	  this.earProjectName = earProject;
  }
  
	private RequiredFacetVersion[] getRequiredWebFacets() {
		return new RequiredFacetVersion[] { getRequiredFacetVersion(
				IModuleConstants.JST_WEB_MODULE,
				J2EEUtils.getWebJ2EEVersionFromEARJ2EEVersion(ProjectUtilities.getProject(earProjectName))) };
	}

	private RequiredFacetVersion[] getRequiredAppClientFacets() {
		return new RequiredFacetVersion[] { getRequiredFacetVersion(
				IModuleConstants.JST_APPCLIENT_MODULE,
				J2EEUtils.getAppClientJ2EEVersionFromEARJ2EEVersion(ProjectUtilities.getProject(earProjectName))) };
	}

	private RequiredFacetVersion[] getRequiredEJBFacets() {
		return new RequiredFacetVersion[] { getRequiredFacetVersion(
				IModuleConstants.JST_EJB_MODULE,
				J2EEUtils.getEJBJ2EEVersionFromEARJ2EEVersion(ProjectUtilities.getProject(earProjectName))) };
	}

	private boolean doesEARSupportFacets(Set combination) {
		try {

			if (earProjectName == null || earProjectName.length() == 0
					|| !ProjectUtilities.getProject(earProjectName).exists())
				// the ear does not exist or the project type template is
				// undefined
				return true;
			else {
				if (combinationContainsFacet(combination, IModuleConstants.JST_WEB_MODULE))
					return FacetUtils
							.match(getRequiredWebFacets(), combination)
							.isMatch();
				else if (combinationContainsFacet(combination, IModuleConstants.JST_EJB_MODULE))
					return FacetUtils
							.match(getRequiredEJBFacets(), combination)
							.isMatch();
				else if (combinationContainsFacet(combination, IModuleConstants.JST_APPCLIENT_MODULE))
					return FacetUtils.match(getRequiredAppClientFacets(),
							combination).isMatch();
				else
					return true;
			}
		} catch (Exception e) {
			return true;
		}
	}

	private boolean combinationContainsFacet(Set combination, String facetName) {
		if (combination == null || facetName == null || facetName.length() == 0)
			return false;
        for (Iterator iter = combination.iterator(); iter.hasNext();) {
        	IProjectFacetVersion nextFacetVersion = (IProjectFacetVersion) iter.next();
        	if (facetName.equals(nextFacetVersion.getProjectFacet().getId()))
        			return true;
		}
        return false;
	}

	private RequiredFacetVersion getRequiredFacetVersion(String facetName,
			String facetVersion) {
		IProjectFacet projectFacet = ProjectFacetsManager
				.getProjectFacet(facetName);
		IProjectFacetVersion projFacetVersion = projectFacet
				.getVersion(facetVersion);
		RequiredFacetVersion reqFacetVersion = new RequiredFacetVersion();
		reqFacetVersion.setAllowNewer(false);
		reqFacetVersion.setProjectFacetVersion(projFacetVersion);
		return reqFacetVersion;
	}

	  private Set getHighestFacetVersionsFromTemplateAndServer()
	  {
	    IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateId);
	    Set fixedFacets = template.getFixedProjectFacets(); 
	    HashSet initial = new HashSet(); 
	    for (Iterator itr = fixedFacets.iterator(); itr.hasNext(); ) 
	    { 
	      IProjectFacet facet = (IProjectFacet) itr.next(); 
	      IProjectFacetVersion highestFacetVersion = null;
	      try {
	    	  highestFacetVersion = facet.getLatestSupportedVersion(facetRuntime);
	      } catch (CoreException e) {
	    	  try {
	    		  highestFacetVersion = facet.getLatestVersion();
	    	  } catch (CoreException e2) {
	    		  //not much we can do here
	    	  }
	    	  
	      }
	      initial.add(highestFacetVersion); 
	    }             
	    
	    return initial;
	  }
}

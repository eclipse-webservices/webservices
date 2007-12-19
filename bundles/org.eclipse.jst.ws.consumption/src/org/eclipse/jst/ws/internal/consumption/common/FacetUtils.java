/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070523   158230 kathy@ca.ibm.com - Kathy Chan
 * 20071219   213356 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.EjbFacetInstallDataModelProvider;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.IEjbFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.AppClientFacetInstallDataModelProvider;
import org.eclipse.jst.j2ee.project.facet.IAppClientFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.IUtilityFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.UtilityFacetInstallDataModelProvider;
import org.eclipse.jst.j2ee.web.project.facet.IWebFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDataModelProvider;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.VersionFormatException;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action.Type;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.common.project.facet.core.runtime.RuntimeManager;

import com.ibm.icu.util.StringTokenizer;

/**
 * FacetUtils contains utility methods related to facets and templates.
 * <br/><br/>
 * Terminology used in the Javadoc in this class
 * <ul>
 * <li><b>facet</b>: An {@link IProjectFacet}</li>
 * <li><b>facet version</b>: An {@link IProjectFacetVersion} </li>
 * <li><b>facet runtime</b>: An {@link IRuntime}</li>
 * <li><b>faceted project</b>: An {@link IFacetedProject}. A faceted project may be obtained
 * from an IProject (@see ProjectFacetsManager#create)</li>
 * <li><b>template</b>: An {@link IFacetedProjectTemplate}. Conceptually this is similar to a project type.</li>
 * <li><b>required facet version</b>: A {@link RequiredFacetVersion}. Used by serviceRuntimes and clientRuntimes
 * to identify what they require in terms of facet versions.</li>
 * </ul>
 */
public class FacetUtils
{

  /**
   * Returns an array of valid projects. Valid projects include projects with the facets nature or
   * projects with the Java nature.
   * @return IProject[] an array of valid projects
   */
  public static IProject[] getAllProjects()
  {
    //Return all projects in the workspace that have the project facet nature or that do not have the project
    //facet nature but have the Java nature.
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    ArrayList validProjects = new ArrayList();
    for (int i = 0; i < projects.length; i++)
    {
      try
      {
        IFacetedProject facProject = ProjectFacetsManager.create(projects[i]);
        if (facProject != null)
        {
          //Add it to the list
          validProjects.add(projects[i]);
        }
        else
        {
        	if (ResourceUtils.isJavaProject(projects[i]))
        	{
              validProjects.add(projects[i]);
        	}
        }
      }
      catch (CoreException ce)
      {        
      }      
    }
    return (IProject[])validProjects.toArray(new IProject[]{});
  }
  
  /**
   * Returns the facet versions on the given project.
   * @param projectName name of an existing project.
   * @returns Set containing elements of type {@link IProjectFacetVersion}. 
   * These are the facet versions currently installed on this project. If the project
   * is a Java project, facets are inferred from the Java project. Returns null 
   * <ul>
   * <li>if the project is not a faceted project or a Java project or</li>
   * <li>if the project does not exist or</li>
   * <li>if the project is null</li>
   * </ul>  
   */
  public static Set getFacetsForProject(String projectName)
  {
    Set facetVersions = null;
    IProject project = ProjectUtilities.getProject(projectName);
    if (project!=null && project.exists())
    {
      try
      {
        IFacetedProject fproject = ProjectFacetsManager.create(project);
        if (fproject != null)
        {
          facetVersions = fproject.getProjectFacets();
        } else
        {
          //If this is not a faceted project, it may still be okay if it is a Java project
          //and the client runtime supports a Java project.
          IJavaProject javaProject = null;              
          if (ResourceUtils.isJavaProject(project))
          {
        	javaProject = JavaCore.create(project);
            facetVersions = FacetUtils.getFacetsForJavaProject(javaProject);
          }
        }
      } catch (CoreException ce)
      {
    	  //Leaving the catch block empty. This method will return null if there
    	  //were any blow-ups in the facet API being called.
      }      
    }
    
    return facetVersions;
    
  }

  /**
   * Returns the facet runtime the given project is bound to.
   * @param projectName name of an existing project
   * @return {@link IRuntime} the project is bound to. Returns null
   * <ul>
   * <li>if the project is not bound to a facet runtime</li> 
   * <li>if the project does not exist</li>
   * <li>if the project is null</li>
   * </ul>
   */
  public static IRuntime getFacetRuntimeForProject(String projectName)
  {
    IProject project = ProjectUtilities.getProject(projectName);
    if (project!=null && project.exists())
    {
      try
      {
        IFacetedProject fproject = ProjectFacetsManager.create(project);
        if (fproject != null)
        {
          return fproject.getPrimaryRuntime();
        }
      } catch (CoreException ce)
      {
    	  //Leaving the catch block empty. This method will return null if there
    	  //were any blow-ups in the facet API being called.
      }
    }
    
    return null;    
  }
  
  
  /**
   * Returns a set of combinations of facet versions derived from the facet versions
   * in the provided arrayOfProjectFacetVersionArrays. For example, if 
   * arrayOfProjectFacetVersionArrays is a two dimenstional array of facet versions that has
   * a structure like this:<br/>
   * {{FacetA_version1, FacetA_version2},<br/>
   *  {FacetB_version1},<br/>
   *  {FacetC_version1, FacetC_version2}}<br/>
   * the following array of Sets will be returned:<br/>
   * {Set1, Set2, Set3, Set4}, where<br/>
   * Set1 = [FacetA_version1, FacetB_version1, FacetC_version1]<br/>
   * Set2 = [FacetA_version2, FacetB_version1, FacetC_version1]<br/>
   * Set3 = [FacetA_version1, FacetB_version1, FacetC_version2]<br/>
   * Set4 = [FacetA_version2, FacetB_version1, FacetC_version2]<br/>
   * <br/>
   * @param arrayOfProjectFacetVersionArrays a two dimensional array containing elements 
   * of type {@link IProjectFacetVersion}
   * @param returnValidOnly false if all combinations of facet versions are to be returned. 
   * true if only valid combinations of facet versions are to be returned.  
   * @return Set[] an array of Sets, where each Set contains elements of type {@link IProjectFacetVersion}. 
   */
  public static Set[] getFacetCombinations(IProjectFacetVersion[][] arrayOfProjectFacetVersionArrays, boolean returnValidOnly)  
  {
    ArrayList allCombinations = new ArrayList();
    //maxCount contains the number of versions in each array of IProjectFacetVersions.
    //initialize counter, which will be used to navigate arrayOfProjectFacetVersionArrays.
    int n = arrayOfProjectFacetVersionArrays.length;
    int[] maxCount = new int[n];
    int[] counter = new int[n];
    for (int i=0; i<n; i++)
    {
      maxCount[i] = arrayOfProjectFacetVersionArrays[i].length - 1;
      counter[i] = 0;
    }      
    
    //Navigate arrayOfProjectFacetVersionArrays to create all possible combinations.
    boolean done = false;
    while (!done)
    {
      //Create a combination of size n using current values in counter.
      //Add it to the list of all combinations, checking first for validity if returnValidOnly is true.
      Set combination = new HashSet();
      for (int j=0; j<n; j++)
      {
        IProjectFacetVersion pfv = arrayOfProjectFacetVersionArrays[j][counter[j]];
        combination.add(pfv);
      }
      
      //Check if valid.
      if (returnValidOnly)
      {
        Set actions = getInstallActions(combination);
		try {
			if( ProjectFacetsManager.check( new HashSet(), actions ).getSeverity() == IStatus.OK )        
	        {
	          allCombinations.add((combination));
	        }
		} catch (Throwable e) {
			// Do nothing if ProjectFacetsManager.check() throws exception.
			// TODO This try/catch will be unneccesary once WTP bug 137551 is fixed so that an OK Status 
			// 		would not be returned when an exception is thrown.
		}     
      }
      else
      {
        allCombinations.add((combination));
      }
      
      //Update the counters.
      for (int p=0; p<n; p++)
      {
        if ( (counter[p] + 1) <= maxCount[p])
        {
          (counter[p])++;
          break;
        }
        else
        {
          counter[p] = 0;
          if (p == n-1)
          {
            done = true;
          }
        }
      }
    }
    
    Set[] allCombinationsArray = (Set[])allCombinations.toArray(new Set[0]);    
    return allCombinationsArray;    
  }
  
  /**
   * Returns a set of facet versions given a template. The highest facet version of every fixed 
   * facet in the template is returned, with the exception of the jst.java facet, where the 1.4 
   * facet version is returned.
   * 
   * @param templateId id of a {@link IFacetedProjectTemplate}
   * @return Set containing elements of type {@link IProjectFacetVersion}
   */
  public static Set getInitialFacetVersionsFromTemplate(String templateId)
  {
    IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateId);
    Set fixedFacets = template.getFixedProjectFacets(); 
    HashSet initial = new HashSet(); 
    for (Iterator itr2 = fixedFacets.iterator(); itr2.hasNext(); ) 
    { 
      IProjectFacet facet = (IProjectFacet) itr2.next(); 
      IProjectFacetVersion highestFacetVersion = null;
      try {
    	  if (isJavaFacet(facet)) //special case the java facet because 1.4 is a better default than 5.0 for now.
    	  {
    		  highestFacetVersion = facet.getVersion("1.4");
    	  } else {
    		  highestFacetVersion = facet.getLatestVersion();
    	  }
      } catch (VersionFormatException e) {
      } catch (CoreException e) {
      }
      initial.add(highestFacetVersion); 
    }             
    
    return initial;
  }
  
  /**
   * Returns a set of facet versions given a template. The default facet version of every fixed 
   * facet in the template is returned.  If unable to get default versions, an empty set will be returned.
   * 
   * @param templateId id of a {@link IFacetedProjectTemplate}
   * @return Set containing elements of type {@link IProjectFacetVersion}
   */
  public static Set getDefaultFacetVersionsFromTemplate(String templateId)
  {
	  HashSet defaultSet = new HashSet(); 
	  try {
		  IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateId);
		  Set fixedFacets = template.getFixedProjectFacets(); 
		  for (Iterator itr2 = fixedFacets.iterator(); itr2.hasNext(); ) 
		  { 
			  IProjectFacet facet = (IProjectFacet) itr2.next(); 
			  IProjectFacetVersion defaultFacetVersion = null;
			  defaultFacetVersion = facet.getDefaultVersion();
			  defaultSet.add(defaultFacetVersion); 
		  }             
		  return defaultSet;
	  } catch (IllegalArgumentException e) {
		  return defaultSet;
	  } catch (VersionFormatException e) {
		  return defaultSet;
	  } 
	  
  }
  
  /**
   * Returns the template lables corresponding to the provided templateIds.
   * @param templateIds array of valid template ids. Each id must correspond to a {@link IFacetedProjectTemplate}.
   * @return String[] array of template labels.
   */
  public static String[] getTemplateLabels(String[] templateIds)
  {
    String[] labels = new String[templateIds.length];
    for (int i=0; i<templateIds.length; i++)
    {
      IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateIds[i]);      
      labels[i] = template.getLabel();
    }
    return labels;
    
  }
  
  /**
   * Returns the id of a template given its label. 
   * 
   * @param templateLabel label of a template
   * @return template id or empty String if no {@link IFacetedProjectTemplate} with the
   * given templateLabel could be found.
   */
  public static String getTemplateIdByLabel(String templateLabel)
  {
    for( Iterator itr = ProjectFacetsManager.getTemplates().iterator(); itr.hasNext(); )
    {
        final IFacetedProjectTemplate template = (IFacetedProjectTemplate) itr.next();
        if (template.getLabel().equals(templateLabel))
        {
          return template.getId();

        }
    }
    
    return "";
  }
  
  /**
   * Returns the label of a template given its id 
   * 
   * @param templateId id of a {@link IFacetedProjectTemplate}
   * @return template label
   */  
  public static String getTemplateLabelById(String templateId)
  {
    IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateId);
    return template.getLabel();
  }
  
  /**
   * Returns a set of install actions for the provided facet version
   * 
   * @param projectFacetVersions Set containing elements of type {@link IProjectFacetVersion}
   * @return Set containing elements of type {@link Action} with the type attribute set to Type.INSTALL
   * The set contains one Action for each {@link IProjectFacetVersion} in projectFacetVersions.
   */
  public static Set getInstallActions(Set projectFacetVersions)
  {
    HashSet actions = new HashSet();
    
    Iterator facets = projectFacetVersions.iterator();
    
    while(facets.hasNext())
    {
    	IProjectFacetVersion fv = (IProjectFacetVersion)facets.next();
    	if (fv != null) {
    		IProjectFacet pf = fv.getProjectFacet();
    		Action action = new Action(Type.INSTALL, fv, getConfigObject(pf));
    		actions.add(action);
    	}
    }
    
    return actions;
  }
  
  /**
   * Returns the data model config object for the given project facet
   * For J2EE facets, set Add_TO_EAR properties of the data model to false, 
   * for other facets, just return null
   * @param facetID
   * @return data model config object
   */
  public static Object getConfigObject(IProjectFacet projectFacet)
  {
	  
	  IDataModel dm = null;
	  if (projectFacet != null) {
		  String facetId = projectFacet.getId();
		  if (facetId != null) {
			  // set Add to EAR to false
			  if (facetId.equals(IModuleConstants.JST_WEB_MODULE)) { 
				  dm = DataModelFactory.createDataModel(new WebFacetInstallDataModelProvider());
				  dm.setBooleanProperty(IWebFacetInstallDataModelProperties.ADD_TO_EAR, false);
			  } else if (facetId.equals(IModuleConstants.JST_EJB_MODULE)) {
				  dm = DataModelFactory.createDataModel(new EjbFacetInstallDataModelProvider());
				  dm.setBooleanProperty(IEjbFacetInstallDataModelProperties.ADD_TO_EAR, false);
			  } else if (facetId.equals(IModuleConstants.JST_APPCLIENT_MODULE)) {
				  dm = DataModelFactory.createDataModel(new AppClientFacetInstallDataModelProvider());
				  dm.setBooleanProperty(IAppClientFacetInstallDataModelProperties.ADD_TO_EAR, false);
			  } else if (facetId.equals(IModuleConstants.JST_UTILITY_MODULE)) {
				  dm = DataModelFactory.createDataModel(new UtilityFacetInstallDataModelProvider());
				  dm.setBooleanProperty(IUtilityFacetInstallDataModelProperties.ADD_TO_EAR, false);
			  }
		  }
	  }
	  return dm;
  }
  /**
   * Returns the {@link FacetMatcher} calculated when checking the required facet versions 
   * against the facet versions.
   * 
   * @param requiredFacetVersions array of required facet versions
   * @param projectFacetVersions facet versions to check against. Set containing elements of type 
   * {@link IProjectFacetVersion}
   * @return FacetMatcher with the following characteristics:
   * <ul>
   * <li><b>isMatch</b>: returns true if the required facet versions already exist in the facet versions
   * or can be added to the facet versions. isMatch will return false otherwise.</li>
   * <li><b>getFacetsTested</b>: returns the same set of facet versions as the input parameter, projectFacetVersions</li>
   * <li><b>getFacetsThatMatched</b>: if isMatch returns false, getFacetsThatMatched returns null. If isMatch returns true, 
   * getFacetsThatMatched returns the subset of facet versions in the input parameter, projectFacetVersions, 
   * that matched one of the required facet versions. This may be an empty set.</li>
   * <li><b>getFacetsToAdd</b>: if isMatch returns false, getFacetsToAdd returns null. If isMatch returns true, 
   * getFacetsToAdd returns the subset of facet versions that would need to be added to the facet versions in
   * projectFacetVersions in order to satisfy the requirements of the required facet versions. This may be
   * an empty set.</li>
   * </ul>
   */
  public static FacetMatcher match(RequiredFacetVersion[] requiredFacetVersions, Set projectFacetVersions)
  {
    FacetMatcher fm = new FacetMatcher();
    fm.setFacetsTested(projectFacetVersions);
    HashSet facetsToAdd = new HashSet();
    HashSet requiredFacetVersionsToAdd = new HashSet();
    HashSet facetsThatMatched = new HashSet();
    for (int i=0; i<requiredFacetVersions.length; i++)
    {
      RequiredFacetVersion rfv = requiredFacetVersions[i];
      IProjectFacetVersion rpfv = rfv.getProjectFacetVersion();
      String rid = rpfv.getProjectFacet().getId();
      String rv = rpfv.getVersionString();
      boolean facetPresent = false;

      //Is the project facet present? or a later version of applicable.
      Iterator itr = projectFacetVersions.iterator();
      while(itr.hasNext())
      {
        IProjectFacetVersion pfv = (IProjectFacetVersion)itr.next();
        String id = pfv.getProjectFacet().getId();
        String version = pfv.getVersionString();
        if (rid.equals(id))
        {
          if (rv.equals(version))
          {
            //found an exact match
            facetPresent = true;
            facetsThatMatched.add(pfv);
          }
          else
          {
            if (rfv.getAllowNewer())
            {
              if (greaterThan(version, rv))
              {
                //found a match
                facetPresent = true;
                facetsThatMatched.add(pfv);
              }
            }
          }
          //No need to keep iterating since we hit a facet with the same id;
          break;
        }
      }
      
      //if not present, put it in the list to check if it can be added.
      if (!facetPresent)
      {
        facetsToAdd.add(rpfv);
        requiredFacetVersionsToAdd.add(rfv);
      }
      
    }
    
    //Check if the facetsToAdd can be added
    if (requiredFacetVersionsToAdd.size() > 0)
    {
      //Test all possible combinations of the facets that need to be added
      //Create an array of arrays. Each element of the array will contain the array
      //of the permitted IProjectFacetVersions for each required facet.
      boolean facetsCanBeAdded = false;
      Iterator itr = requiredFacetVersionsToAdd.iterator();
      ArrayList projectFacetVersionArrays = new ArrayList();      

      while (itr.hasNext())
      {
        RequiredFacetVersion reqFacetVersion = (RequiredFacetVersion) itr.next();
        IProjectFacetVersion[] versions = reqFacetVersion.getAllowedProjectFacetVersions();
        if (versions != null && versions.length > 0)
        {          
          //Add the array of versions to the list of arrays.
          projectFacetVersionArrays.add(versions);
        }
      }
      
      IProjectFacetVersion[][] arrayOfProjectFacetVersionArrays = (IProjectFacetVersion[][])projectFacetVersionArrays.toArray(new IProjectFacetVersion[0][0]);
      Set[] combinations = getFacetCombinations(arrayOfProjectFacetVersionArrays, false);
      for (int i=0; i<combinations.length; i++)
      {
        //Check if the set can be added. If so, this is a match. Update the facet matcher and break.
        Set actions = getInstallActions(combinations[i]);
        try {
			if( ProjectFacetsManager.check( projectFacetVersions, actions ).getSeverity() == IStatus.OK )
			{
			  //Facets can be added so there is a match
			  facetsCanBeAdded=true;
			  fm.setMatch(true);
			  fm.setFacetsThatMatched(facetsThatMatched);
			  fm.setFacetsToAdd(combinations[i]);
			  break;
			}
		} catch (Throwable e) {
			// Do nothing if ProjectFacetsManager.check() throws exception.
			// TODO This try/catch will be unneccesary once WTP bug 137551 is fixed so that an OK Status 
			// 		would not be returned when an exception is thrown.
		}                
      }
      
      
      if (!facetsCanBeAdded)
      {
        fm.setMatch(false);
      }      
    }
    else
    {
      //There are no facets to add.
      fm.setMatch(true);
      fm.setFacetsThatMatched(facetsThatMatched);
      fm.setFacetsToAdd(facetsToAdd);
    }
    
    return fm;
  }
    
  /**
   * Adds facet versions to the provided project if required based on checking provided 
   * required facet versions against the facet versions already installed on project.
   * 
   * @param project IProject which exists in the workspace
   * @param rfvs array of required facet versions.
   * @param monitor progress monitor
   * @return IStatus with severity IStatus.ERROR
   * <ul>
   * <li>if the project does not exist or</li>
   * <li>if the project is not open or</li>
   * <li>an attempt to add facet versions to the project resulted in an error.</li>
   * </ul> 
   * Otherwise, returns an IStatus with severity IStatus.OK    
   */
  public static IStatus addRequiredFacetsToProject(IProject project, RequiredFacetVersion[] rfvs, IProgressMonitor monitor)
  {
    IStatus status = Status.OK_STATUS;
    
    Set missingFacets = null;
    Set facetsToAdd = new HashSet();
    try
    {
      IFacetedProject fProject = ProjectFacetsManager.create(project);
      if (fProject != null)
      {
        Set projectFacetVersions = fProject.getProjectFacets();
        FacetMatcher projectFacetMatcher = FacetUtils.match(rfvs, projectFacetVersions);
        if (projectFacetMatcher.isMatch())
        {
          missingFacets = projectFacetMatcher.getFacetsToAdd();
          if (missingFacets.size() > 0)
          {
            IRuntime fRuntime = null;
            fRuntime = FacetUtils.getFacetRuntimeForProject(project.getName());
            if (fRuntime != null)
            {  
              //Add the highest version supported by the runtime the project is bound to.
              Iterator missingFacetsItr = missingFacets.iterator();
              while (missingFacetsItr.hasNext())
              {
                IProjectFacet facet = ((IProjectFacetVersion)missingFacetsItr.next()).getProjectFacet();
                //Get the highest level of this facet supported by the runtime.
                List versions = null;
                try {
                    versions = facet.getSortedVersions(false);
                } catch (VersionFormatException e) {
                    Set versionSet = facet.getVersions();
                    Iterator itr = versionSet.iterator();
                    versions = new ArrayList();
                    while (itr.hasNext())
                    {
                        versions.add(itr.next());
                    }            
                } catch (CoreException e) {
                    Set versionSet = facet.getVersions();
                    Iterator itr = versionSet.iterator();
                    versions = new ArrayList();
                    while (itr.hasNext())
                    {
                        versions.add(itr.next());
                    }            
                }
                
                //Iterate over the versions in descending order and pick the 
                //first one that fRuntime supports.
                Iterator versionsItr = versions.iterator();
                while(versionsItr.hasNext())
                {
                  boolean match = false;
                  IProjectFacetVersion pfv = (IProjectFacetVersion)versionsItr.next();
                  Set pfvs = new HashSet();
                  pfvs.add(pfv);
                  
                  //Check the required facets to see if this version of this facet is supported.
                  for (int j=0; j<rfvs.length; j++)
                  {
                    RequiredFacetVersion rfv = rfvs[j];
                    IProjectFacetVersion rpfv = rfv.getProjectFacetVersion();
                    if (rpfv.getProjectFacet().getId().equals(pfv.getProjectFacet().getId()))
                    {
                      if (rpfv.getVersionString().equals(pfv.getVersionString()))
                      {
                        match = true;
                      }
                      else
                      {
                        if (rfv.getAllowNewer())
                        {
                          if (greaterThan(pfv.getVersionString(), rpfv.getVersionString()))
                          {
                            match = true;
                          }
                        }
                      }
                    }
                  }
                  
                  if (match)
                  {
                    //Check against Runtime
                    if (FacetUtils.doesRuntimeSupportFacets(fRuntime, pfvs))
                    {
                      //We have a match. Add this one to the master set.
                      facetsToAdd.add(pfv);
                      break;
                    }
                  }                            
                }              
              }
            }
            else
            {
              facetsToAdd = missingFacets;
            }

            status = addFacetsToProject(fProject, facetsToAdd);
          }
        }            
      }
    } catch (CoreException ce)
    {
      //Display an appropriate error message to the user.
      //A CoreException could have been thrown for any of the following three reasons
      //1. The project does not exist
      //2. The project is not open
      //3. There was a problem adding the facets to the project.
      
      if (!project.exists())
      {
        status = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_DOES_NOT_EXIST, new String[] { project.getName()}));            
      }
      else if (!project.isOpen())
      {
        status = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_IS_NOT_OPEN, new String[] { project.getName()}));            
      }
      else
      { 
        status = getErrorStatusForAddingFacets(project.getName(), facetsToAdd, ce);
      }
    }
    
    return status;
  }
  

  /**
   * Adds the provided set of facet versions to the provided faceted project
   * 
   * @param fproject A faceted project which exists in the workspace
   * @param projectFacetVersions A set containing elements of type {@link IProjectFacetVersion}
   * @return An IStatus with a severity of IStatus.OK if the facet 
   * versions were added successfully. Otherwise, an IStatus with a severity of
   * IStatus.ERROR. 
   */
  public static IStatus addFacetsToProject(final IFacetedProject fproject, final Set projectFacetVersions)
  {
    final IStatus[] status = new IStatus[1];
    status[0] = Status.OK_STATUS;
    final Set actions = getInstallActions(projectFacetVersions);
    
    // Create a runnable that applies the install actions to the faceted project
    IRunnableWithProgress runnable = new IRunnableWithProgress()
    {
      public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException
      {
        try
        {
          fproject.modify(actions, shellMonitor);
        } catch (CoreException e)
        {
          status[0] = getErrorStatusForAddingFacets(fproject.getProject().getName(), projectFacetVersions, e);
        }
      }
    };    
        
    // Run the runnable in another thread unless there is no UI thread (Ant scenarios)
    if (Display.getCurrent() != null)
    {    	
	    try
	    {
	      PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
	    } catch (InvocationTargetException ite)
	    {
	      status[0] = getErrorStatusForAddingFacets(fproject.getProject().getName(), projectFacetVersions, ite);
	    } catch (InterruptedException ie)
	    {
	      status[0] = getErrorStatusForAddingFacets(fproject.getProject().getName(), projectFacetVersions, ie);
	    }
	}
	else
	{
		try
        {
          fproject.modify(actions, null);
        } catch (CoreException e)
        {
          status[0] = getErrorStatusForAddingFacets(fproject.getProject().getName(), projectFacetVersions, e);
        }
	}
    
    return status[0];
  }
  
  /**
   * Returns an error status indicating that the facet versions could not be
   * added to the faceted project
   * 
   * @param projectName a project name to insert in the error message in the IStatus
   * @param projectFacetVersions a set containing elements of type {@link IProjectFacetVersion}.
   * The facets in this set will be listed in the error message in the IStatus.
   * @param t a Throwable which will be inserted in the IStatus
   * @return an IStatus with severity IStatus.ERROR
   */
  private static IStatus getErrorStatusForAddingFacets(String projectName, Set projectFacetVersions, Throwable t)
  {
    IStatus status = Status.OK_STATUS;
    int size = projectFacetVersions.size();
    if (size > 0)
    {          
      Set facets = new HashSet();
      //Iterate over projectFacetVersions to form a set of IProjectFacets
      Iterator itr = projectFacetVersions.iterator();
      while (itr.hasNext())
      {
        IProjectFacetVersion projectFacet = (IProjectFacetVersion)itr.next();
        IProjectFacet facet = projectFacet.getProjectFacet();
        facets.add(facet);
      }
      String facetList = getFacetListMessageString(facets);
      status = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_ADDING_FACETS_TO_PROJECT, new String[] { projectName, facetList}), t);
    }
    
    return status;
  }
  
  /**
   * Creates a new faceted project with the provided name
   * 
   * @param projectName A String containing the name of the project to be created
   * @return An IStatus with a severity of IStatus.OK if the faceted project
   * was created successfully or if a project of the provided name already
   * exists in the workspace. Otherwise, an IStatus with severity of
   * IStatus.ERROR. 
   */
  public static IStatus createNewFacetedProject(final String projectName)
  {
    final IStatus[] status = new IStatus[1];
    status[0] = Status.OK_STATUS;
    IProject project = ProjectUtilities.getProject(projectName);
    if (!project.exists())
    {
      // Create a runnable that creates a new faceted project.
      IRunnableWithProgress runnable = new IRunnableWithProgress()
      {
        public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException
        {
          try
          {
            IFacetedProject fProject = ProjectFacetsManager.create(projectName, null, shellMonitor);
            if (fProject == null)
            {
              status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }));
            }
          } catch (CoreException e)
          {
            status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), e);
          }
        }
      };

    // Run the runnable in another thread unless there is no UI thread (Ant scenarios)    	  
      try
      {
    	  if (Display.getCurrent() != null)
    	  {
    		  PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);    		  
    	  }
    	  else
    	  {
    		  try
              {
                IFacetedProject fProject = ProjectFacetsManager.create(projectName, null, null);
                if (fProject == null)
                {
                  status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }));
                }
              } catch (CoreException e)
              {
                status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), e);
              }  
    	  }        
      } catch (InvocationTargetException ite)
      {
        status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), ite);
      } catch (InterruptedException ie)
      {
        status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), ie);
      }
    }

    return status[0];
  }
  
  /**
   * Sets the provided set of facets as fixed on the faceted project
   * 
   * @param fProject A faceted project which exists in the workspace
   * @param fixedFacets A set containing elements of type {@link IProjectFacet}
   * @return An IStatus with a severity of IStatus.OK if the facets 
   * were successfully set as fixed facets on the faceted project. 
   * Otherwise, an IStatus with a severity of IStatus.ERROR.
   * 
   * @see IFacetedProject#setFixedProjectFacets
   */
  public static IStatus setFixedFacetsOnProject(final IFacetedProject fProject, final Set fixedFacets)
  {
    final IStatus[] status = new IStatus[1];
    status[0] = Status.OK_STATUS;

    //Create a runnable that sets the fixed facets on the faceted project
    IRunnableWithProgress runnable = new IRunnableWithProgress()
    {
      public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException
      {
        try
        {
          fProject.setFixedProjectFacets(fixedFacets);
        } catch (CoreException e)
        {
          status[0] = getErrorStatusForSettingFixedFacets(fProject.getProject().getName(), fixedFacets, e);
        }
      }
    };

    // Run the runnable in another thread unless there is no UI thread (Ant scenarios)
    if (Display.getCurrent() != null)
    {
    	try
        {
          PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
        } catch (InvocationTargetException ite)
        {
          status[0] = getErrorStatusForSettingFixedFacets(fProject.getProject().getName(), fixedFacets, ite);
        } catch (InterruptedException ie)
        {
          status[0] = getErrorStatusForSettingFixedFacets(fProject.getProject().getName(), fixedFacets, ie);
        }	
    }
    else
    {
    	try
        {
          fProject.setFixedProjectFacets(fixedFacets);
        } catch (CoreException e)
        {
          status[0] = getErrorStatusForSettingFixedFacets(fProject.getProject().getName(), fixedFacets, e);
        }
    }       
    
    return status[0];
  }
  
  /**
   * Returns an error status indicating that the facets could not be
   * set as fixed facets on the faceted project
   * 
   * @param projectName a project name to insert in the error message in the IStatus
   * @param facets a set containing elements of type {@link IProjectFacet}.
   * The facets in this set will be listed in the error message in the IStatus.
   * @param t a Throwable which will be inserted in the IStatus
   * @return an IStatus with severity IStatus.ERROR
   */
  private static IStatus getErrorStatusForSettingFixedFacets(String projectName, Set facets, Throwable t)
  {
    IStatus status = Status.OK_STATUS;
    int size = facets.size();
    if (size > 0)
    {          
      String facetList = getFacetListMessageString(facets);      
      status = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_FIXED_FACETS, new String[] { projectName, facetList}), t);
    }
    
    return status;
  }  
  
  
  /**
   * Binds the faceted project to the facet runtime
   * 
   * @param fProject A faceted project which exists in the workspace
   * @param fRuntime A facet runtime
   * @return An IStatus with a severity of IStatus.OK if the faceted project
   * was bound to the facet runtime successfully. Otherwise, an IStatus with severity of
   * IStatus.ERROR. 
   */
  public static IStatus setFacetRuntimeOnProject(final IFacetedProject fProject, final IRuntime fRuntime)
  {
    final IStatus[] status = new IStatus[1];
    status[0] = Status.OK_STATUS;

    //Create a runnable that sets the facet runtime on the faceted project
    IRunnableWithProgress runnable = new IRunnableWithProgress()
    {
      public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException
      {
        try
        {
        	fProject.setTargetedRuntimes(Collections.singleton(fRuntime), shellMonitor);
        } catch (CoreException e)
        {
          status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_SETTING_RUNTIME, new String[] { fProject.getProject().getName(), fRuntime.getName() }), e);
        }
      }
    };

    // Run the runnable in another thread unless there is no UI thread (Ant scenarios)
    if (Display.getCurrent() != null)
    {
    	try
        {
          PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
        } catch (InvocationTargetException ite)
        {
          status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_SETTING_RUNTIME, new String[] { fProject.getProject().getName(), fRuntime.getName() }), ite);
        } catch (InterruptedException ie)
        {
          status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_SETTING_RUNTIME, new String[] { fProject.getProject().getName(), fRuntime.getName() }), ie);
        }	
    }
    else 
    {
    	try
        {
    		fProject.setTargetedRuntimes(Collections.singleton(fRuntime), null); //jvh - happens here...
        } catch (CoreException e)
        {
          status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_SETTING_RUNTIME, new String[] { fProject.getProject().getName(), fRuntime.getName() }), e);
        }
    }
        
    
    return status[0];
  }
  
  /**
   * Returns a translatable delimited list of facet labels derived from the provided
   * set of facets
   * 
   * @param facets a set containing elements of type {@link IProjectFacet}
   * @return String a delimited list of facet labels
   */
  private static String getFacetListMessageString(Set facets)
  {
    String facetListMessage = "";
    int size = facets.size();
    if (size > 0)
    {
      Iterator itr = facets.iterator();
      IProjectFacet firstProjectFacet = (IProjectFacet)itr.next();
      facetListMessage = firstProjectFacet.getLabel();
      
      //Continue appending to facetListMessage until all the facet labels
      //are in the list.
      while (itr.hasNext())
      {
        IProjectFacet projectFacet = (IProjectFacet)itr.next();
        String pfLabel = projectFacet.getLabel();
        facetListMessage = NLS.bind(ConsumptionMessages.MSG_FACETS, new String[] {facetListMessage, pfLabel});
      }            
    }    
    
    return facetListMessage;
  }
  
  
  /**
   * Returns the set of facet versions which can be inferred from the provided Java project
   * 
   * @param javaProject a Java project that exists in the workspace. Must not be null.
   * @return Set containing elements of type {@link IProjectFacetVersion}
   */
  public static Set getFacetsForJavaProject(IJavaProject javaProject)
  {
    Set facets = new HashSet();
    String jdkComplianceLevel = null;
    if (javaProject!=null)
    {
      jdkComplianceLevel = javaProject.getOption("org.eclipse.jdt.core.compiler.compliance", false);
      if (jdkComplianceLevel == null)
      {
        jdkComplianceLevel = JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE);
        if (jdkComplianceLevel == null)
        {
          jdkComplianceLevel = "1.4";
        }
      }
    }
    
    IProjectFacet javaFacet = ProjectFacetsManager.getProjectFacet(IModuleConstants.JST_JAVA);
    IProjectFacetVersion javaFacetVersion = null;
    if (jdkComplianceLevel.equals("1.3"))
    {
      javaFacetVersion = javaFacet.getVersion("1.3");
    }
    else if (jdkComplianceLevel.equals("1.4"))
    {
      javaFacetVersion = javaFacet.getVersion("1.4");
    }
    else if (jdkComplianceLevel.equals("1.5"))
    {
      javaFacetVersion = javaFacet.getVersion("5.0");
    }
    else if (jdkComplianceLevel.equals("1.6"))
    {
      javaFacetVersion = javaFacet.getVersion("6.0");
    }
    else
    {
      javaFacetVersion = javaFacet.getVersion("1.4");
    }
 
    facets.add(javaFacetVersion);
    IProjectFacet utilityFacet = ProjectFacetsManager.getProjectFacet(IModuleConstants.JST_UTILITY_MODULE);
    IProjectFacetVersion utilityFacetVersion = null;
    try
    {
      utilityFacetVersion = utilityFacet.getLatestVersion();
    }
    catch (CoreException ce)
    {
      
    }
    if (utilityFacetVersion != null)
    {
      facets.add(utilityFacetVersion);
    }
    return facets;
  }
  
  
  //Methods related to facet runtimes.
  
  /**
   * Returns a set of facet runtimes that support the given
   * required facet versions.
   * @param requiredFacetVersions an array containing elements of type {@link RequiredFacetVersion}
   * @return Set set of facet runtimes that support the given required facet versions.
   * (element type: {@link IRuntime}) 
   */
  public static Set getRuntimes(RequiredFacetVersion[] requiredFacetVersions)
  {
    //Form the sets of IProjectFacetVersions these RequiredFacetVersions represent.
    ArrayList listOfFacetSets = new ArrayList();
    
    HashSet facets = new HashSet();
    int javaFacetIndex = -1;
    for (int i=0; i<requiredFacetVersions.length; i++)
    {
      IProjectFacetVersion pfv = requiredFacetVersions[i].getProjectFacetVersion();
      if (FacetUtils.isJavaFacet(pfv.getProjectFacet()))
      {
        //Remember the index
        javaFacetIndex = i;
      }
      facets.add(requiredFacetVersions[i].getProjectFacetVersion());
    }
    
    listOfFacetSets.add(facets);
    
    //If the java facet was one of the facets in the set, and new versions of java are allowed,
    //create sets that contain the newer permitted versions of the java facets.
    if (javaFacetIndex > -1)
    {
      ArrayList permittedJavaVersions = new ArrayList();
      RequiredFacetVersion rfv = requiredFacetVersions[javaFacetIndex];
      if (rfv.getAllowNewer())
      {
        String version = rfv.getProjectFacetVersion().getVersionString();      
        Set allVersions = rfv.getProjectFacetVersion().getProjectFacet().getVersions();
        Iterator itr = allVersions.iterator();
        while (itr.hasNext())
        {
          IProjectFacetVersion thisPfv = (IProjectFacetVersion)itr.next();
          String thisVersion = thisPfv.getVersionString();
          if (greaterThan(thisVersion, version))
          {
            permittedJavaVersions.add(thisVersion);
          }          
        }
        
        String[] javaVersions = (String[])permittedJavaVersions.toArray(new String[0]);
        
        for (int j=0; j<javaVersions.length; j++)
        {
          HashSet thisFacetSet = new HashSet();
          
          for (int k=0; k<requiredFacetVersions.length; k++)
          {
             if (k==javaFacetIndex)
             {
               IProjectFacetVersion pfv = requiredFacetVersions[k].getProjectFacetVersion().getProjectFacet().getVersion(javaVersions[j]);
               thisFacetSet.add(pfv);
             }
             else
             {
               IProjectFacetVersion pfv = requiredFacetVersions[k].getProjectFacetVersion();
               thisFacetSet.add(pfv);
             }
          }
          
          listOfFacetSets.add(thisFacetSet);          
        }
      }
    }
    
    //Return the union of runtimes for all the facetSets.
    return getRuntimes((Set[])listOfFacetSets.toArray(new Set[0]));
    
  }  
  
  /**
   * Returns whether or not the provided facet runtime supports the provided
   * required facet versions.
   * 
   * @param requiredFacetVersions an array containing elements of type {@link RequiredFacetVersion}
   * @param fRuntimeName name of a {@link IRuntime}
   * @return boolean <code>true</code> if the facet runtime supports the required facet versions.
   * Returns <code>false</code> otherwise.
   */
  public static boolean isFacetRuntimeSupported(RequiredFacetVersion[] requiredFacetVersions, String fRuntimeName)
  {
    Set fRuntimes = getRuntimes(requiredFacetVersions);
    Iterator itr = fRuntimes.iterator();
    while (itr.hasNext())
    {
      IRuntime runtime = (IRuntime)itr.next();
      if (runtime.getName().equals(fRuntimeName))
      {
        return true;
      }      
    }
    
    return false;
  }
    
  /**
   * Returns the union of facet runtimes that support the provided sets of facet versions.
   * 
   * @param facetSets array of Sets, where each Set contains elements of type {@link IProjectFacetVersion}.
   * @return Set containing elements of type {@link IRuntime}
   */
  public static Set getRuntimes(Set[] facetSets)  
  {
    HashSet unionSet = new HashSet();
    for (int i=0; i<facetSets.length; i++)
    {
      Set facets = facetSets[i];
      Set runtimes = RuntimeManager.getRuntimes(facets);
      Iterator itr = runtimes.iterator();
      while (itr.hasNext())
      {
        IRuntime runtime = (IRuntime)itr.next();
        if (!unionSet.contains(runtime))
        {
          unionSet.add(runtime);
        }
      }
    }
    return unionSet;
  }
  
  /**
   * Returns whether or not the provided facet runtime supports the provided set of facet versions.
   * 
   * @param facetRuntime a facet runtime
   * @param projectFacetVersions set containing elements of type {@link IProjectFacetVersion}
   * @return boolean <code>true</code> if the facet runtime supports the provided set of facet versions.
   * Returns <code>false</code> otherwise.
   */
  public static boolean doesRuntimeSupportFacets(IRuntime facetRuntime, Set projectFacetVersions)
  {
    Set runtimes = RuntimeManager.getRuntimes(projectFacetVersions);
    Iterator itr = runtimes.iterator();
    while (itr.hasNext())
    {
      IRuntime runtime = (IRuntime)itr.next();
      if (runtime.getName().equals(facetRuntime.getName()))
      {
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * Returns whether versionA is greater than versionB
   * 
   * @param versionA version number of the form 1.2.3
   * @param versionA version number of the form 1.2.3
   * @return boolean <code>true</code> if versionA is greater than versionB, <code>false</code> otherwise.
   */
  public static boolean greaterThan(String versionA, String versionB)
  {
    StringTokenizer stA = new StringTokenizer(versionA, ".");
    StringTokenizer stB = new StringTokenizer(versionB, ".");
    
    int sizeA = stA.countTokens();
    int sizeB = stB.countTokens();
    
    int size;
    if (sizeA < sizeB)
    {
      size = sizeA;
    }
    else
      size = sizeB;
    
    for (int i=0; i<size; i++)
    {
      int a = Integer.parseInt(stA.nextToken());
      int b = Integer.parseInt(stB.nextToken());
      if (a!=b)
      {
        return a > b;
      }      
    }
    
    return sizeA > sizeB;
  }
  
  /**
   * Returns whether the provided facet has an id of "jst.java"
   * @param pf facet
   * @return <code>true</code> if facet has an id of "jst.java", <code>false</code> otherwise.
   */
  public static boolean isJavaFacet(IProjectFacet pf)
  {
    if (pf.getId().equals("jst.java"))
      return true;
    else
      return false;
  }
  
  /**
   * Returns whether or not the provided project is a faceted Java utility project or a non-faceted Java project.
   * 
   * @param project an IProject
   * @return boolean <code>true</code> if the provided project is a faceted Java utility project 
   * or a non-faceted Java project, <code>false</code> otherwise.
   */
  public static boolean isJavaProject(IProject project)
  {
    //Check if it's a faceted project
    try
    {
      IFacetedProject fProject = ProjectFacetsManager.create(project);
      if (fProject != null)
      {
        //Return true if it's a utility project
        if (J2EEUtils.isJavaComponent(project))
        {
          return true;
        }
        else
        {
          //See if the java facet is the only one it has.
          Set facets = fProject.getProjectFacets();
          if (facets.size()==1)
          {
            IProjectFacetVersion pfv = (IProjectFacetVersion)facets.iterator().next();
            if (isJavaFacet(pfv.getProjectFacet()))
            {
              return true;
            }
          }
        }
      }
      else
      {
        if (ResourceUtils.isJavaProject(project))
        {
          return true;
        }        
      }
    } catch (CoreException ce)
    {
      
    }

    return false;
  }
  
  /**
   * Returns whether or not the provided template id is equal to "template.jst.utility"
   * 
   * @param templateId template id
   * @return boolean <code>true</code> if the provided template id is equal to "template.jst.utility", 
   * <code>false</code> otherwise.
   */
  public static boolean isUtilityTemplate(String templateId)
  {
    if (ProjectFacetsManager.isTemplateDefined(templateId))
    {
      if (templateId.equals("template.jst.utility"))
      {
        return true;
      }
    }
    
    return false;
  }
      
}

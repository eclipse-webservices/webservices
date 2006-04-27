/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner (initial creation)
 * 20060426   138051 kathy@ca.ibm.com - Kathy Chan
 * 20060427   126780 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.consumption.common.FacetMatcher;
import org.eclipse.jst.ws.internal.consumption.common.FacetSetsByTemplateCache;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;

/**
 * FacetMatchCache caches results of calculations made respecting
 * the suitability of projects and project types for serviceRuntimes
 * and clientRuntimes.
 * 
 * <br/><br/>
 * Teminology used in the javadoc in this class:
 * <ul>
 * <li><b>serviceRuntime</b>: extension to org.eclipse.jst.ws.consumption.ui.serviceRuntimes.
 * The Java representation of this is org.eclipse.jst.ws.internal.consumption.ui.wsrt.ServiceRuntimeDescriptor</li>
 * <li><b>clientRuntime</b>: extension to org.eclipse.jst.ws.consumption.ui.clientRuntimes.
 * The Java representation of this is org.eclipse.jst.ws.internal.consumption.ui.wsrt.ClientRuntimeDescriptor</li>
 * </ul>
 *
 */
public class FacetMatchCache implements IResourceChangeListener
{
  //single instance per workbench
  private static FacetMatchCache instance_;
  
  
  //Tables related to existing projects in the workspace
  
  //serviceFacetMatchTable_: 
  //key: Object of type String.
  //     Forward slash separated concatenation of a serviceRuntimeId and a projectName 
  //     (e.g. org.eclipse.jst.ws.axis.creation.java/wp).
  //value: Object of type org.eclipse.jst.ws.internal.consumption.common.FacetMatcher.
  //       The FacetMatcher contains the matching characteristics calculated for the 
  //       serviceRuntime and project in the key.
  private Hashtable serviceFacetMatchTable_;
  
  //serviceTableKeysByProjectName_: 
  //key: Object of type String.
  //     project name (e.g. wp)
  //value: Set of elements of type String. The Strings in the Set are keys from the serviceFacetMatchTable_ 
  //       that contain the project name (e.g. org.eclipse.jst.ws.axis.creation.java/wp). Used to rapidly
  //       delete entries from serviceFacetMatchTable_ after a project is deleted from the workspace.
  private Hashtable serviceTableKeysByProjectName_;
  
  //clientFacetMatchTable_;
  //key: Object of type String.
  //     forward slash separated concatenation of a clientRuntimeId and a projectName 
  //     (e.g. org.eclipse.jst.ws.axis.consumption.java/wp)
  //value: Object of type org.eclipse.jst.ws.internal.consumption.common.FacetMatcher
  //       The FacetMatcher contains the matching characteristics calculated for the 
  //       clientRuntime and project in the key.
  private Hashtable clientFacetMatchTable_;
  
  //clientTableKeysByProjectName_
  //key: Object of type String.
  //     project name (e.g. wp)
  //value: Set of elements of type String. The Strings in the Set are keys from the clientFacetMatchTable_ 
  //       that contain the project name (e.g. org.eclipse.jst.ws.axis.consumption.java/wp). Used to rapidly
  //       delete entries from clientFacetMatchTable_ after a project is deleted from the workspace.
  private Hashtable clientTableKeysByProjectName_;
  
  //projectEntriesToDelete_: elements of type String containing the names of projects that have
  //been deleted from the workspace but their corresponding entries from serviceFacetMatchTable_ 
  //and clientFacetMatchTable_ have yet to be deleted.
  private List projectEntriesToDelete_;
    
  
  
  //Tables related to templates  
  
  //templatesByServiceRuntimeId_:
  //key: Object of type String.
  //     serviceRuntime id
  //value: Set of elements of type org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate   
  //       This the set of templates that support the serviceRuntime identified by the key.
  private Hashtable templatesByServiceRuntimeId_;
  
  //templatesByClientRuntimeId_
  //key: Object of type String.
  //     clientRuntime id
  //value: Set of elements of type org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate
  //       This the set of templates that support the clientRuntime identified by the key.
  private Hashtable templatesByClientRuntimeId_;
  
  
  /**
   * Returns a singleton instance of this class.
   * 
   * @return A singleton FacetMatchCache object.
   */
  public static synchronized FacetMatchCache getInstance()
  {
    if (instance_ == null)
    {
      instance_ = new FacetMatchCache();
      instance_.load();
    }
    return instance_;
  }  
  
  private void load()
  {
    serviceFacetMatchTable_ = new Hashtable();
    serviceTableKeysByProjectName_ = new Hashtable();
    clientFacetMatchTable_ = new Hashtable();    
    clientTableKeysByProjectName_ = new Hashtable();
    projectEntriesToDelete_ = new ArrayList();
    templatesByClientRuntimeId_ = new Hashtable();
    templatesByServiceRuntimeId_ = new Hashtable();
    
    //Listen for deletions of projects in order to delete all entries in
    //Hashtables for the deleted project.
    ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_DELETE);
  }
  

  /*
   * Returns the FacetMatcher resulting from matching a serviceRuntime or clientRuntime with an existing project.
   * @param isClient true if runtimeId is a clientRuntimeId. false if runtimeId is a serviceRuntimeId.
   * @param runtimeId the id of a clientRuntime if isClient is true or the id of a serviceRuntime if
   * isClient is false.
   * @param projectName the name of an existing project.
   * 
   * @returns FacetMatcher resulting from calling FacetUtils.match(..) using the serviceRuntime or clientRuntime id
   * and the project name. Returns null if the project doesn't exist. Non-faceted existing projects will always result
   * in a FacetMatcher with isMatch() equal to false (with the exception of Java projects which are a special case 
   * - see FacetUtils.getFacetsForProject()).
   */
  public synchronized FacetMatcher getMatchForProject(boolean isClient, String runtimeId, String projectName)
  {    
    //Delete entries from tables if there are deletions pending.
    if (projectEntriesToDelete_.size() > 0)
    {
      Iterator projectItr = projectEntriesToDelete_.iterator();
      while(projectItr.hasNext())
      {
        String projectNameToDelete = (String)projectItr.next();
       
        //Delete entries from client table
        removeTableEntriesForProject(true, projectNameToDelete);
        
        //Delete entries from service table
        removeTableEntriesForProject(false, projectNameToDelete);
      }
      
      //Clear the projectEntriesToDelete list
      projectEntriesToDelete_.clear();
    }
    
    IProject project = ProjectUtilities.getProject(projectName);
    FacetMatcher fm = null;
    if (project != null && project.exists())
    {
      String key = getKey(runtimeId, projectName);
      if (isClient)
      {
        fm = (FacetMatcher) clientFacetMatchTable_.get(key);
      }
      else
      {
        fm = (FacetMatcher) serviceFacetMatchTable_.get(key);
      }
      
      if (fm == null)
      {
        // This combination has not yet been calculated and cached so calculate it and cache it.        
        fm = calculateFacetMatcher(isClient, runtimeId, projectName);         
        if (isClient)
        {
          clientFacetMatchTable_.put(key,fm);     
        }
        else
        {
          serviceFacetMatchTable_.put(key, fm);
        }
        updateTableOfKeys(isClient, key, projectName);
        
      } else
      {
        // If the project's facets have changed since the last time the
        // facets were calculated, refresh the facetMatcher.        
        Set currentFacetVersions = FacetUtils.getFacetsForProject(projectName);
        Set previousFacetVersions = fm.getFacetsTested();
        if (!currentFacetVersions.equals(previousFacetVersions))
        {
          //recalculate and cache the FacetMatcher
          fm = calculateFacetMatcher(isClient, runtimeId, projectName);
          if (isClient)
          {
            clientFacetMatchTable_.put(key,fm);         
          }
          else
          {
            serviceFacetMatchTable_.put(key, fm);
          }                    
        }
      }
    }
    
    return fm;    
  }
  
  /**
   * Updates serviceTableKeysByProjectName_ or clientTableKeysByProjectName_ with
   * the provided project name and key. This is called whenever a new entry is 
   * added to serviceFacetMatchTable_ or clientFacetMatchTable_.
   * @param isClient true if runtimeId is a clientRuntimeId. false if runtimeId is a serviceRuntimeId.
   * @param key 
   * @param projectName
   */
  private void updateTableOfKeys(boolean isClient, String key, String projectName)
  {
    Set setOfKeysForProjectName = null;
    if (isClient)
    {
      setOfKeysForProjectName = (Set)clientTableKeysByProjectName_.get(projectName);      
    }
    else
    {
      setOfKeysForProjectName = (Set)serviceTableKeysByProjectName_.get(projectName);
    }
    
    if (setOfKeysForProjectName == null)
    {
      //Add an entry in the table for this project.
      Set keys = new HashSet();
      keys.add(key);
      if (isClient)
      {
        clientTableKeysByProjectName_.put(projectName, keys);
      }
      else
      {
        serviceTableKeysByProjectName_.put(projectName, keys);
      }
    }
    else
    {
      //Update the entry in the table for this project.
      setOfKeysForProjectName.add(key);      
    }
  }
  
  /**
   * Returns the {@link FacetMatcher} calculated for the given serviceRuntime or clientRuntime and project
   * @param isClient true if runtimeId is a clientRuntimeId. false if runtimeId is a serviceRuntimeId.
   * @param runtimeId the id of a clientRuntime if isClient is true or the id of a serviceRuntime if
   * isClient is false.
   * @param projectName the name of an existing project.
   * @return FacetMatcher resulting from calling FacetUtils.match(..) using the serviceRuntime or clientRuntime id
   * and the project name. Returns null if the project doesn't exist. Non-faceted existing projects will always result
   * in a FacetMatcher with isMatch() equal to false (with the exception of Java projects which are a special case 
   * - see FacetUtils.getFacetsForProject()).
   */
  private FacetMatcher calculateFacetMatcher(boolean isClient, String runtimeId, String projectName)
  {
    FacetMatcher fm = null;
    RequiredFacetVersion[] rfvs = null;
    if (isClient)
    {
      ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(runtimeId);
      rfvs = desc.getRequiredFacetVersions();
    }
    else
    {
      ServiceRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(runtimeId);
      rfvs = desc.getRequiredFacetVersions();
    }
    
    Set facetVersions = FacetUtils.getFacetsForProject(projectName);
    if (facetVersions == null)
    {
      fm = new FacetMatcher();
      fm.setMatch(false);
      return fm;
    }    
    
    fm = FacetUtils.match(rfvs, facetVersions);
    
    // Check if the facet runtime required by the required facet is supported
    // by the project without chaning it's current facet runtime
    
    IRuntime fProjectRuntime = FacetUtils.getFacetRuntimeForProject(projectName);
    if (fProjectRuntime != null) {
    	String fProjectRuntimeName = fProjectRuntime.getName();
    	boolean projectSupportRequiredFacetRuntime = false;
    	Set rts = FacetUtils.getRuntimes(rfvs);
    	for (Iterator iterator = rts.iterator(); iterator.hasNext() && !projectSupportRequiredFacetRuntime;) {
    		IRuntime fRequiredRuntime = (IRuntime) iterator.next();
    		if (fRequiredRuntime != null) {
    			if (fRequiredRuntime.getName().equals(fProjectRuntimeName)) {
    				projectSupportRequiredFacetRuntime = true;
    			}
    		}
    	}

    	// if project does not support the required facet runtime, set FacetMatch match to false
    	if (!projectSupportRequiredFacetRuntime) {
    		fm.setMatch(false);
    	}
    }
    return fm;
  }

  private String getKey(String a, String b)
  {
    StringBuffer keysb = new StringBuffer();
    keysb.append(a);
    keysb.append("/");
    keysb.append(b);    
    return keysb.toString();    
  }  
  
  /**
   * Removes all table entries in this cache for the given project
   * @param isClient true to remove entries from client side tables. false to remove entries from service side tables
   * @param projectName
   */
  private void removeTableEntriesForProject(boolean isClient, String projectName)
  {
    //First remove the entries from clientFacetMatchTable_ or serviceFacetMatchTable_
    //that have keys containing this project name.
    Set setOfKeysForProjectName = null;
    if (isClient)
    {
      setOfKeysForProjectName = (Set)clientTableKeysByProjectName_.get(projectName);      
    }
    else
    {
      setOfKeysForProjectName = (Set)serviceTableKeysByProjectName_.get(projectName);
    }
    
    if (setOfKeysForProjectName != null)
    { 
      Iterator keysItr = setOfKeysForProjectName.iterator();
      while (keysItr.hasNext())
      {
        String key = (String)keysItr.next();
        if (isClient)
        { 
          clientFacetMatchTable_.remove(key);          
        }
        else
        {
          serviceFacetMatchTable_.remove(key);
        }
      }
      
      //Second, remove the entry in clientTableKeysByProjectName_ or serviceTableKeysByProjectName_
      //with this projectName as the key.
      if (isClient)
      {
        clientTableKeysByProjectName_.remove(projectName);  
      }
      else
      {
        serviceTableKeysByProjectName_.remove(projectName);
      }
      
    }
    
  }

  /**
   * Returns a set of templates supported by the given clientRuntime
   * @param clientRuntimeId id of a clientRuntime
   * @returns Set (elementtype: IFacetedProjectTemplate)
   */
  public synchronized Set getTemplatesForClientRuntime(String clientRuntimeId)
  {
    Set templates = (Set)templatesByClientRuntimeId_.get(clientRuntimeId);
    if (templates != null)
    {
      //Return the cached set of templates.
      return templates;
    }
    else
    {
      //Calculate the templates, cache them for later use, and return them.
      ClientRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getClientRuntimeDescriptorById(clientRuntimeId);
      Set validTemplates = getTemplates(desc.getRequiredFacetVersions());
      templatesByClientRuntimeId_.put(clientRuntimeId, validTemplates);
      return validTemplates;
    }
  }
  
  /**
   * Returns a set of templates supported by the given serviceRuntime
   * @param serviceRuntimeId id of a serviceRuntime
   * @returns Set (elementtype: IFacetedProjectTemplate)
   */  
  public synchronized Set getTemplatesForServiceRuntime(String serviceRuntimeId)
  {
    Set templates = (Set)templatesByServiceRuntimeId_.get(serviceRuntimeId);
    if (templates != null)
    {
      //Return the cached set of templates.
      return templates;
    }
    else
    {
      //Calculate the templates, cache them for later use, and return them.
      ServiceRuntimeDescriptor desc = WebServiceRuntimeExtensionUtils2.getServiceRuntimeDescriptorById(serviceRuntimeId);
      Set validTemplates = getTemplates(desc.getRequiredFacetVersions());
      templatesByServiceRuntimeId_.put(serviceRuntimeId, validTemplates);
      return validTemplates;
    }
  }
 
  
  /**
   * Returns the set of templates that supported the given required facet versions.
   * @param requiredFacetVersions
   * @return Set containing elemets of type {@link IFacetedProjectTemplate}.
   */
  private Set getTemplates(RequiredFacetVersion[] requiredFacetVersions)
  {
    Set templates = new HashSet();
    
    for( Iterator itr = ProjectFacetsManager.getTemplates().iterator(); itr.hasNext(); )
    {
        final IFacetedProjectTemplate template = (IFacetedProjectTemplate) itr.next();
        String templateId = template.getId();
        if (templateId.indexOf("ear") == -1 && templateId.indexOf("wst.web") == -1) //Don't include the EARs!!
        {           
          Set[] combinations = FacetSetsByTemplateCache.getInstance().getFacetVersionCombinationsFromTemplate(templateId);
          for (int i=0; i<combinations.length; i++)
          {
            FacetMatcher fm = FacetUtils.match(requiredFacetVersions, combinations[i]);
            if (fm.isMatch())
            {
              //Found a combination that worked. Add the template to the list and move on.
              templates.add(template);
              break;
            }
          }
        }
    }    
    
    return templates;
    
  }
  
  public synchronized void resourceChanged(IResourceChangeEvent event)
  {
    if (event.getType() == IResourceChangeEvent.PRE_DELETE)
    {
      IResource projectResource = event.getResource();
      if (projectResource!=null)
      {
        String projectName = projectResource.getName();
        
        //Add this project name to the list of project entries
        //to delete. Next time getMatchForProject is called all
        //entries for this project from all tables in this cache 
        //will be deleted.
        projectEntriesToDelete_.add(projectName);
      }
    }    
  }  
}

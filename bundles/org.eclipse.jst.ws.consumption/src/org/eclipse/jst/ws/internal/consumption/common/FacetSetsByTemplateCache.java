/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner (creation)
 * 20060427   126780 rsinha@ca.ibm.com - Rupam Kuehner
 * 20070402 151943   sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.VersionFormatException;


/**
 * FacetSetsByTemplateCache caches the sets of facet version combinations for templates.
 */
public class FacetSetsByTemplateCache
{
  // single instance per workbench
  private static FacetSetsByTemplateCache instance_;
  
  //facetSetsByTemplateId_: 
  //key: Object of type String. String id of an IFacetedProjectTemplate.
  //value: Object of Set[]. An array of Sets where each set contains elements of type IProjectFacetVersion.
  private Hashtable facetSetsByTemplateId_;
  /**
   * Returns a singleton instance of this class.
   * 
   * @return A singleton FacetSetsByTemplateCache object.
   */
  public static synchronized FacetSetsByTemplateCache getInstance()
  {
    if (instance_ == null)
    {
      instance_ = new FacetSetsByTemplateCache();
      instance_.load();
    }
    return instance_;
  }
  
  private void load()
  {
    facetSetsByTemplateId_ = new Hashtable();
  }  
  
  /**
   * Returns all the valid facet version combinations that can be derived from the fixed facets
   * of the given template. For example, if the template has two fixed facets, A and B,
   * and A has versions 1.0 and 2.0, B has versions 1.1, 2.1,
   * the following array of Sets will be returned (assuming for the sake of the example
   * that all the combinations are valid):<br/>
   * {Set1, Set2, Set3, Set4}, where<br/>
   * Set1 = [A1.0, B1.1]<br/>
   * Set2 = [A2.0, B1.1]<br/>
   * Set3 = [A1.0, B2.1]<br/>
   * Set4 = [A2.0, B2.1]<br/>
   * <br/>
   * @param templateId id of an {@link IFacetedProjectTemplate}
   * @return Set[] An array of Sets where each set contains elements of type {@link IProjectFacetVersion}.
   */
  public synchronized Set[] getFacetVersionCombinationsFromTemplate(String templateId)
  {
	//Return the cached combinations if present.
    Set[] cachedCombinations = (Set[])facetSetsByTemplateId_.get(templateId);
    if (cachedCombinations != null)
    {
      return cachedCombinations;
    }
    else
    {

      //Combinations have not yet been cached for the given template. 
      //Determine the combinations and cache them.
      IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate(templateId);
      Set fixedFacets = template.getFixedProjectFacets();
      
      //Create an array of arrays. Each element of the array will contain the array
      //of IProjectFacetVersions for each IProjectFacet in the set of fixed facets.
      Iterator itr = fixedFacets.iterator();
      ArrayList projectFacetVersionArrays = new ArrayList();      

      while (itr.hasNext())
      {
        IProjectFacet facet = (IProjectFacet) itr.next();
        List versions = null;
        try
        {
          // Get the facet versions in ascending order.
          versions = facet.getSortedVersions(true);
        } catch (VersionFormatException e) {
            Set versionSet = facet.getVersions();
            Iterator versionSetItr = versionSet.iterator();
            versions = new ArrayList();
            while (versionSetItr.hasNext())
            {
                versions.add(itr.next());
            }            
        } catch (CoreException e) {
          Set versionSet = facet.getVersions();
          Iterator versionSetItr = versionSet.iterator();
          versions = new ArrayList();
          while (versionSetItr.hasNext())
          {
              versions.add(itr.next());
          }            
        }         
        
        if (versions.size() > 0)
        {
          //Create an array of IProjectFacetVersions from versions.
          Iterator versionItr = versions.iterator();
          ArrayList arrayOfVersionsList = new ArrayList();
          while (versionItr.hasNext())
          {
            IProjectFacetVersion pfv = (IProjectFacetVersion)versionItr.next();
            arrayOfVersionsList.add(pfv);
          }          
          
          //Add the array of versions to the list of arrays.
          projectFacetVersionArrays.add((IProjectFacetVersion[])arrayOfVersionsList.toArray(new IProjectFacetVersion[0]));
          

        }
      }
      Set[] allValidCombinationsArray = new HashSet[0];
      if (projectFacetVersionArrays.size()>0) {
    	  IProjectFacetVersion[][] arrayOfProjectFacetVersionArrays = (IProjectFacetVersion[][])projectFacetVersionArrays.toArray(new IProjectFacetVersion[0][0]);
    	  allValidCombinationsArray = FacetUtils.getFacetCombinations(arrayOfProjectFacetVersionArrays, true);
    	  facetSetsByTemplateId_.put(templateId, allValidCombinationsArray);
      }
     
      return allValidCombinationsArray;
    }    
  }  
}

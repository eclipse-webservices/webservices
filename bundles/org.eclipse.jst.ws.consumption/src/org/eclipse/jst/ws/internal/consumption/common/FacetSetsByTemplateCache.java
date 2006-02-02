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
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner (creation)
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.common;

import java.util.ArrayList;
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


public class FacetSetsByTemplateCache
{
  private static FacetSetsByTemplateCache instance_;
  
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
  
  public synchronized Set[] getFacetVersionCombinationsFromTemplate(String templateId)
  {
    Set[] cachedCombinations = (Set[])facetSetsByTemplateId_.get(templateId);
    if (cachedCombinations != null)
    {
      return cachedCombinations;
    }
    else
    {
      //ArrayList allValidCombinations = new ArrayList();
      
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
      
      IProjectFacetVersion[][] arrayOfProjectFacetVersionArrays = (IProjectFacetVersion[][])projectFacetVersionArrays.toArray(new IProjectFacetVersion[0][0]);
      Set[] allValidCombinationsArray = FacetUtils.getFacetCombinations(arrayOfProjectFacetVersionArrays, true);
      facetSetsByTemplateId_.put(templateId, allValidCombinationsArray);
      return allValidCombinationsArray;
    }    
  }  
}

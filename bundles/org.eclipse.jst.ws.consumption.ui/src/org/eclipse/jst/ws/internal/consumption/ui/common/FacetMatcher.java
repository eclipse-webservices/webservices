package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.Set;

import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class FacetMatcher
{
  private boolean match;
  private Set facetsThatMatched;
  private Set facetsToAdd;
  
  public Set getFacetsThatMatched()
  {
    return facetsThatMatched;
  }
  public void setFacetsThatMatched(Set facetsThatMatched)
  {
    this.facetsThatMatched = facetsThatMatched;
  }
  public Set getFacetsToAdd()
  {
    return facetsToAdd;
  }
  public void setFacetsToAdd(Set facetsToAdd)
  {
    this.facetsToAdd = facetsToAdd;
  }
  public boolean isMatch()
  {
    return match;
  }
  public void setMatch(boolean match)
  {
    this.match = match;
  }
  

  
  

}

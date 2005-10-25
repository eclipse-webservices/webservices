package org.eclipse.jst.ws.internal.consumption.ui.wsrt;


import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class RequiredFacetVersion
{
  private IProjectFacetVersion projectFacetVersion;
  private boolean allowNewer;
  
  public boolean getAllowNewer()
  {
    return allowNewer;
  }
  public void setAllowNewer(boolean allowNewer)
  {
    this.allowNewer = allowNewer;
  }
  public IProjectFacetVersion getProjectFacetVersion()
  {
    return projectFacetVersion;
  }
  public void setProjectFacetVersion(IProjectFacetVersion projectFacetVersion)
  {
    this.projectFacetVersion = projectFacetVersion;
  }  
  
}

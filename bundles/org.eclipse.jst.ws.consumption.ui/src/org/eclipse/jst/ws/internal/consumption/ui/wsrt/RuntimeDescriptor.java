package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;

public class RuntimeDescriptor
{

  private IConfigurationElement elem;
  private String id;
  private String label;
  

  public RuntimeDescriptor(IConfigurationElement elem)
  {
    this.elem = elem;
  }
  
  public String getId()
  {
    if (id == null)
    {
      id = elem.getAttribute("id");
    }
    return id;
  }
  
  public String getLabel()
  {
    if (label == null)
    {
      label = elem.getAttribute("label");
    }
    return label;
  }
  
  public String[] getJ2eeLevels()
  {
    return new String[]{"13", "14"};
  }
  
}

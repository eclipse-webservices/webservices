/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;

public class ClientProjectTypeRegistry
{
  private static ClientProjectTypeRegistry instance_;
  private IConfigurationElement[] configElements_;

  private ClientProjectTypeRegistry()
  {
    init();
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton ClientProjectTypeRegistry object.
  */
  public static ClientProjectTypeRegistry getInstance()
  {
    if (instance_ == null)
      instance_ = new ClientProjectTypeRegistry();
    return instance_;
  }

  public void init()
  {
    IPluginRegistry reg = Platform.getPluginRegistry();
    configElements_ = reg.getConfigurationElementsFor("org.eclipse.jst.ws.consumption.ui", "clientProjectType");
  }

  public IConfigurationElement getDefaultElement()
  {
    return getElementById("org.eclipse.jst.ws.consumption.ui.clientProjectType.Web");
  }

  public IConfigurationElement getElementById(String id)
  {
    return getElementByAttribute("id", id);
  }

  public IConfigurationElement getElementByLabel(String label)
  {
    return getElementByAttribute("label", label);
  }

  public IConfigurationElement getElementByProject(IProject project)
  {
    for (int i = 0; i < configElements_.length; i++)
      if (include(project, configElements_[i].getAttribute("include")) && exclude(project, configElements_[i].getAttribute("exclude")))
        return configElements_[i];
    return null;
  }

  private IConfigurationElement getElementByAttribute(String name, String value)
  {
    for (int i = 0; i < configElements_.length; i++)
      if (configElements_[i].getAttribute(name).equals(value))
        return configElements_[i];
    return null;
  }

  public IProject[] getProjects(String id)
  {
    Vector v = new Vector();
    IConfigurationElement element = getElementById(id);
    if (element != null)
    {
      IProject[] workspaceProjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();
      for (int i = 0; i < workspaceProjs.length; i++)
        if (include(workspaceProjs[i], element.getAttribute("include")) && exclude(workspaceProjs[i], element.getAttribute("exclude")))
          v.add(workspaceProjs[i]);
    }
    IProject[] projects = new IProject[v.size()];
    v.copyInto(projects);
    return projects;
  }

  private boolean include(IProject project, String include)
  {
    StringTokenizer st = new StringTokenizer(include);
    while(st.hasMoreTokens())
    {
      try
      {
        if (project.hasNature(st.nextToken()))
          return true;
      }
      catch (CoreException ce)
      {
      }
    }
    return false;
  }

  private boolean exclude(IProject project, String exclude)
  {
    StringTokenizer st = new StringTokenizer(exclude);
    while(st.hasMoreTokens())
    {
      try
      {
        if (project.hasNature(st.nextToken()))
          return false;
      }
      catch (CoreException ce)
      {
      }
    }
    return true;
  }
}

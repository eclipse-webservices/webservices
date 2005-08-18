/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.wsfinder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public abstract class WSFinderCommon implements IWSFinder
{
  private String id_;
  private String name_;
  private String desc_;

  public WSFinderCommon()
  {
    id_ = null;
    name_ = null;
    desc_ = null;
  }

  public String getID()
  {
    return id_;
  }

  public void setID(String id)
  {
    id_ = id;
  }

  public String getName()
  {
    return name_;
  }

  public void setName(String name)
  {
    name_ = name;
  }

  public String getDescription()
  {
    return desc_;
  }

  public void setDescription(String desc)
  {
    desc_ = desc;
  }

  protected IProject[] getWorkspaceProjects()
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    return root.getProjects();
  }
//TODO Remove old Nature refs
//  protected J2EEWebNatureRuntime getWebNature(IProject project)
//  {
//    try
//    {
//      IProjectNature nature = project.getNature(IWebNatureConstants.J2EE_NATURE_ID);
//      if (nature != null && nature instanceof J2EEWebNatureRuntime)
//        return (J2EEWebNatureRuntime)nature;
//    }
//    catch (CoreException ce)
//    {
//    }
//    return null;
//  }
//
//  protected EJBNatureRuntime getEJBNature(IProject project)
//  {
//    try
//    {
//      IProjectNature nature = project.getNature(IEJBNatureConstants.NATURE_ID);
//      if (nature != null && nature instanceof EJBNatureRuntime)
//        return (EJBNatureRuntime)nature;
//    }
//    catch (CoreException ce)
//    {
//    }
//    return null;
//  }
//
//  protected IFolder getFolderRootPublishable(J2EEWebNatureRuntime webNature)
//  {
//    return (IFolder)webNature.getRootPublishableFolder();
//  }
//
//  protected IFolder getFolderWEBINF(J2EEWebNatureRuntime webNature)
//  {
//    return webNature.getProject().getFolder(webNature.getWEBINFPath().toString());
//  }
//
//  protected IFolder getFolderMETAINF(EJBNatureRuntime ejbNature)
//  {
//    return ejbNature.getMetaFolder();
//  }
}

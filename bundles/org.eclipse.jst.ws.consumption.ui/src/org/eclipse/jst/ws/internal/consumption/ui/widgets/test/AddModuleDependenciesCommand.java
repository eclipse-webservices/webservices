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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.applicationclient.creation.IApplicationClientNatureConstants;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveManifest;
import org.eclipse.jst.j2ee.internal.earcreation.AddUtilityJARMapCommand;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.j2ee.internal.project.IEJBNatureConstants;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.j2ee.internal.project.J2EENature;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;

public class AddModuleDependenciesCommand extends SimpleCommand
{
  private String sampleProject;
  private String sampleEAR;
  private String clientProject;
  private String sampleServerTypeID;
  private IServer sampleExistingServer;
  private String clientJ2EEVersion;

  public AddModuleDependenciesCommand()
  {
    super("org.eclipse.jst.ws.internal.consumption.command.common.AddModuleDependenciesTask", "org.eclipse.jst.ws.internal.consumption.command.common.AddModuleDependenciesTask");
  }

  /**
   * Execute WebServerDefaultingTask Set the default server name and id given a
   * deployable.
   */
  public Status execute(Environment env)
  {
    try
    {
      IResource res = (IProject)ResourceUtils.findResource(sampleProject);
      IProject sampleIProject = null;
      if (res instanceof IProject)
        sampleIProject = (IProject)res;
      if (sampleIProject == null)
      {
        CreateSampleProjectCommand createSample = new CreateSampleProjectCommand();
        createSample.setSampleProject(sampleProject);
        createSample.setSampleProjectEAR(sampleEAR);
        if (sampleEAR!=null && sampleEAR.length()>0)
          createSample.setNeedEAR(true);    
        createSample.setExistingServer(sampleExistingServer);
        createSample.setServerFactoryId(sampleServerTypeID);
        createSample.setJ2eeVersion(clientJ2EEVersion);
        Status status = createSample.execute(env);
      }
      res = ResourceUtils.findResource(sampleProject);
      if (res instanceof IProject)
        sampleIProject = (IProject)res;
      res = ResourceUtils.findResource(sampleEAR);
      IProject sampleIEAR = null;
      if (res instanceof IProject)
        sampleIEAR = (IProject)res;
      if (sampleIProject == null)
        return new SimpleStatus("", "", Status.ERROR);
      res = ResourceUtils.findResource(clientProject);
      IProject clientIProject = null;
      if (res instanceof IProject)
        clientIProject = (IProject)res;
      if (clientIProject != null && !ResourceUtils.isWebProject(clientIProject))
      {
        String uri = clientIProject.getName() + ".jar";
        if (ResourceUtils.isTrueJavaProject(clientIProject))
          addJavaProjectAsUtilityJar(clientIProject, sampleIEAR, uri);
        addJAROrModuleDependency(sampleIProject, uri);
        addBuildPath(sampleIProject, clientIProject);
      }
    }
    catch (IOException ioe)
    {
    }
    catch (CoreException ce)
    {
    }
    return new SimpleStatus("");
  }

  private void addJavaProjectAsUtilityJar(IProject javaProject, IProject earProject, String uri)
  {
    AddUtilityJARMapCommand cmd = new AddUtilityJARMapCommand(earProject, uri, javaProject);
    cmd.execute();
  }

  private void addJAROrModuleDependency(IProject project, String uri) throws IOException, CoreException
  {
    J2EENature nature = null;
    if (ResourceUtils.isWebProject(project))
      nature = getWebNature(project);
    else if (ResourceUtils.isAppClientProject(project))
      nature = getAppClientNature(project);
    else if (ResourceUtils.isEJBProject(project))
      nature = getEJBNature(project);
    if (nature != null)
    {
      ArchiveManifest manifest = J2EEProjectUtilities.readManifest(project);
      manifest.mergeClassPath(new String[]{uri});
      J2EEProjectUtilities.writeManifest(project, manifest);
    }
  }

  private void addBuildPath(IProject referencingProject, IProject referencedProject) throws JavaModelException
  {
    IJavaProject javaProject = JavaCore.create(referencingProject);
    if (javaProject != null)
    {
      IClasspathEntry[] oldCp = javaProject.getRawClasspath();
      IClasspathEntry[] newCp = new IClasspathEntry[oldCp.length];
      for (int i = 0; i < oldCp.length; i++)
        newCp[i] = oldCp[i];
      newCp[newCp.length - 1] = JavaCore.newProjectEntry(referencedProject.getFullPath());
      javaProject.setRawClasspath(newCp, new NullProgressMonitor());
    }
  }

  private J2EENature getWebNature(IProject project)
  {
    try
    {
      return (J2EENature)project.getNature(IWebNatureConstants.J2EE_NATURE_ID);
    }
    catch (CoreException ce)
    {
    }
    return null;
  }

  private J2EENature getAppClientNature(IProject project)
  {
    for (int i = 0; i < IApplicationClientNatureConstants.APPCLIENT_NATURE_IDS.length; i++)
    {
      try
      {
        return (J2EENature)project.getNature(IApplicationClientNatureConstants.APPCLIENT_NATURE_IDS[i]);
      }
      catch (CoreException ce)
      {
      }
    }
    return null;
  }

  private J2EENature getEJBNature(IProject project)
  {
    try
    {
      // ksc return
      // (J2EENature)project.getNature(IEJBNatureConstants.EJB_20_NATURE_ID);
      return (J2EENature)project.getNature(IEJBNatureConstants.NATURE_ID);
    }
    catch (CoreException ce)
    {
    }
    try
    {
      return (J2EENature)project.getNature(IEJBNatureConstants.NATURE_ID);
    }
    catch (CoreException ce)
    {
    }
    return null;
  }

  private IProject getEARProject(IProject sampleProject)
  {
    EARNatureRuntime[] natures = J2EEUtils.getEARProjects(sampleProject);
    if (natures != null && natures.length > 0)
      return natures[0].getProject();
    else
      return null;
  }

  /**
   * @param proxyProject
   *          The proxyProject to set.
   */
  public void setSampleProject(String sampleProject)
  {
    this.sampleProject = sampleProject;
  }

  public String getSampleProject()
  {
    return sampleProject;
  }

  public void setSampleProjectEAR(String sampleEAR)
  {
    this.sampleEAR = sampleEAR;
  }

  public void setClientProject(String clientProject)
  {
    this.clientProject = clientProject;
  }

  public void setSampleServerTypeID(String sampleServerTypeID)
  {
    this.sampleServerTypeID = sampleServerTypeID;
  }

  public void setSampleExistingServer(IServer sampleExistingServer)
  {
    this.sampleExistingServer = sampleExistingServer;
  }
  
  public void setClientJ2EEVersion(String clientJ2EEVersion)
  {
    this.clientJ2EEVersion = clientJ2EEVersion;
  }
}

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
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IServer;

public class AddModuleDependenciesCommand extends SimpleCommand
{
  private String sampleProject;
  private String sampleEAR;
  private String sampleP;
  private String sampleC;
  private String clientC;
  private String clientP;
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
      
	  if(sampleProject != null){
	    int index = sampleProject.indexOf("/");
		sampleP = sampleProject.substring(0,index);
		sampleC = sampleProject.substring(index + 1);
	  }
	  IProject sampleIProject = (IProject)ProjectUtilities.getProject(sampleP);
              
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
      IResource res = ResourceUtils.findResource(sampleEAR);
      IProject sampleIEAR = null;
      if (res instanceof IProject)
        sampleIEAR = (IProject)res;
      if (sampleIProject == null)
        return new SimpleStatus("", "", Status.ERROR);
      
	  if(clientProject != null){
	    int index = clientProject.indexOf("/");
		clientP = clientProject.substring(0,index);
		clientC = clientProject.substring(index + 1);
	  }
	  IProject clientIProject = ProjectUtilities.getProject(clientP);
	       
      
      if (clientIProject != null && !J2EEUtils.isWebComponent(clientIProject, clientC))
      {
        String uri = clientIProject.getName() + ".jar";
        if (ResourceUtils.isTrueJavaProject(clientIProject))
          addJavaProjectAsUtilityJar(clientIProject, sampleIEAR, uri);
        addJAROrModuleDependency(sampleIProject, sampleC, uri);
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
	//  TODO  Remove refs to old command
//    AddUtilityJARMapCommand cmd = new AddUtilityJARMapCommand(earProject, uri, javaProject);
//    cmd.execute();
  }

  private void addJAROrModuleDependency(IProject project, String compName, String uri) throws IOException, CoreException
  {
	  //TODO use components API
//    J2EENature nature = null;
//    if (J2EEUtils.isWebComponent(project, compName))
//      nature = getWebNature(project);
//    else if (J2EEUtils.isAppClientComponent(project, compName))
//      nature = getAppClientNature(project);
//    else if (J2EEUtils.isEJBComponent(project, compName))
//      nature = getEJBNature(project);
//    if (nature != null)
//    {
//      ArchiveManifest manifest = J2EEProjectUtilities.readManifest(project);
//      manifest.mergeClassPath(new String[]{uri});
//      J2EEProjectUtilities.writeManifest(project, manifest);
//    }
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

  private IProject getEARProject(IProject sampleProject)
  {
    IVirtualComponent[] comps = J2EEUtils.getEARComponentsFromProject(sampleProject);
    if (comps != null && comps.length > 0)
      return comps[0].getProject();
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

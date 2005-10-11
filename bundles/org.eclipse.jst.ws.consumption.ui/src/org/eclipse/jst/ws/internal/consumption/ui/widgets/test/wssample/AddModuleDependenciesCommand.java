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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import java.io.IOException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveManifest;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.AddModuleToServerCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateModuleCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.StartServerCommand;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;

public class AddModuleDependenciesCommand extends AbstractDataModelOperation
{
  
  private TestInfo testInfo;	
	
  private IProject sampleIProject;
  private IProject clientIProject;
  private IProject sampleEARIProject;
  private String sampleEARProject;
  private String sampleEARModule;
  
  public AddModuleDependenciesCommand(TestInfo testInfo)
  {
    this.testInfo = testInfo;
  }

  /**
   * Execute WebServerDefaultingTask Set the default server name and id given a
   * deployable.
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    try
    {
      createSampleProjects(env, monitor );
	  clientIProject = ProjectUtilities.getProject(testInfo.getClientProject());
	        
      if (clientIProject != null && !J2EEUtils.isWebComponent(clientIProject, testInfo.getClientModule()))
      {
        String uri = clientIProject.getName() + ".jar";
		if (J2EEUtils.isJavaComponent(clientIProject,testInfo.getClientModule()))
          addJavaProjectAsUtilityJar(clientIProject, sampleEARIProject, uri);
        addJAROrModuleDependency(sampleIProject, testInfo.getGenerationModule(), uri);
        addBuildPath(sampleIProject, clientIProject);
      }
    }
    catch (IOException ioe)
    {
    }
    catch (CoreException ce)
    {
    }
    return Status.OK_STATUS;
  }

  private void addJavaProjectAsUtilityJar(IProject javaProject, IProject earProject, String uri)
  {
		//  TODO  Remove refs to old command
//    AddUtilityJARMapCommand cmd = new AddUtilityJARMapCommand(earProject, uri, javaProject);
//    cmd.execute();
  }

  private void addJAROrModuleDependency(IProject project, String compName, String uri) throws IOException, CoreException
  {
     ArchiveManifest manifest = J2EEProjectUtilities.readManifest(project);
     manifest.mergeClassPath(new String[]{uri});
     J2EEProjectUtilities.writeManifest(project, manifest);
    
  }

  public static final String DEFAULT_SAMPLE_EAR_PROJECT_EXT = "EAR";
  
  private void createSampleProjects(Environment env, IProgressMonitor monitor )
  {
	  
	  
	  
	  sampleIProject = ProjectUtilities.getProject(testInfo.getGenerationProject());
	  clientIProject = ProjectUtilities.getProject(testInfo.getClientProject());
	  
	if (testInfo.getClientNeedEAR()) {
	  if(testInfo.getClientEARProject() == null || testInfo.getClientEARProject().length() == 0){
		  sampleEARProject = testInfo.getGenerationProject() + DEFAULT_SAMPLE_EAR_PROJECT_EXT;
	      sampleEARModule = testInfo.getGenerationModule() + DEFAULT_SAMPLE_EAR_PROJECT_EXT;	
	  }
	  else{ 
	      sampleEARProject = testInfo.getClientEARProject();
		  sampleEARModule = testInfo.getClientEARModule();
	  }
	  
	  sampleEARIProject  = ProjectUtilities.getProject(sampleEARProject);
	  if(sampleEARIProject == null || !sampleEARIProject.isOpen()){
		  
	    CreateModuleCommand createEAR = new CreateModuleCommand();
	    createEAR.setProjectName(sampleEARProject);
	    createEAR.setModuleName(sampleEARModule);
	    createEAR.setServerInstanceId(testInfo.getClientExistingServer().getId());
	    createEAR.setServerFactoryId(testInfo.getClientServerTypeID());
	    createEAR.setModuleType(CreateModuleCommand.EAR);
	    createEAR.setJ2eeLevel(J2EEUtils.getJ2EEVersionAsString(clientIProject,testInfo.getClientModule()));
      createEAR.setEnvironment( env );
		  IStatus status = createEAR.execute( monitor, null );
	    if (status.getSeverity()==Status.ERROR)
        {
          env.getStatusHandler().reportError(status);     
        }
	  
		AddModuleToServerCommand modToServer = new AddModuleToServerCommand();
		modToServer.setModule(sampleEARModule);
		modToServer.setProject(sampleEARProject);
		modToServer.setServerInstanceId(testInfo.getClientExistingServer().getId());
    modToServer.setEnvironment( env );
		status = modToServer.execute( monitor, null );
		if (status.getSeverity()==Status.ERROR)
	    {
	      env.getStatusHandler().reportError(status);     
	    }     
	  
	  }
	}
	
	  if (!sampleIProject.isOpen())
      {
        CreateModuleCommand createSample = new CreateModuleCommand();
        createSample.setProjectName(testInfo.getGenerationProject());
        createSample.setModuleName(testInfo.getGenerationModule());
		createSample.setModuleType(CreateModuleCommand.WEB);
		createSample.setServerInstanceId(testInfo.getClientExistingServer().getId());
        createSample.setServerFactoryId(testInfo.getClientServerTypeID());
        createSample.setJ2eeLevel(J2EEUtils.getJ2EEVersionAsString(clientIProject,testInfo.getClientModule()));
        createSample.setEnvironment( env );
		IStatus status = createSample.execute( monitor, null );
      
	   if (testInfo.getClientNeedEAR()) {
//		Associate the client module and service EAR
	    AssociateModuleWithEARCommand associateCommand = new AssociateModuleWithEARCommand();
	    associateCommand.setProject(testInfo.getGenerationProject());
	    associateCommand.setModule(testInfo.getGenerationModule());
	    associateCommand.setEARProject(sampleEARProject);
	    associateCommand.setEar(sampleEARModule);
      associateCommand.setEnvironment( env );
	    status = associateCommand.execute( monitor, null );
	    if (status.getSeverity()==Status.ERROR)
	    {
	      env.getStatusHandler().reportError(status);     
	    }  
	   }
		
		StartServerCommand startServer = new StartServerCommand(false, true);
		startServer.setServerInstanceId(testInfo.getClientExistingServer().getId());
    startServer.setEnvironment( env );
		status = startServer.execute( monitor, null );
	    if (status.getSeverity()==Status.ERROR)
	    {
	      env.getStatusHandler().reportError(status);     
	    }     
		
	  }
	  
  }
  
  private void addBuildPath(IProject referencingProject, IProject referencedProject) throws JavaModelException
  {
    IJavaProject javaProject = JavaCore.create(referencingProject);
    if (javaProject != null)
    {
      IClasspathEntry[] oldCp = javaProject.getRawClasspath();
	  IClasspathEntry[] newCp = new IClasspathEntry[oldCp.length + 1];
	  for (int i = 0; i < oldCp.length; i++)
        newCp[i] = oldCp[i];
	  newCp[newCp.length - 1] = JavaCore.newProjectEntry(referencedProject.getFullPath());
	  javaProject.setRawClasspath(newCp, new NullProgressMonitor());
    }
  }

   

  

 

 
}

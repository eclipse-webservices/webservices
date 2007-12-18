/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060324   122799 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060503   138478 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060510   141115 rsinha@ca.ibm.com - Rupam Kuehner
 * 20071212	  200193 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import java.io.IOException;
import java.util.List;

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
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.project.facet.IJavaProjectMigrationDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.JavaProjectMigrationDataModelProvider;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.AddModuleToServerCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateFacetedProjectCommand;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.StartServerCommand;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.properties.ICreateReferenceComponentsDataModelProperties;
import org.eclipse.wst.common.componentcore.internal.operation.CreateReferenceComponentsDataModelProvider;
import org.eclipse.wst.common.componentcore.internal.util.ComponentUtilities;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.ws.internal.wsrt.TestInfo;

public class AddModuleDependenciesCommand extends AbstractDataModelOperation
{
  
  private TestInfo testInfo;	
	
  private IProject sampleIProject;
  private IProject clientIProject;
  private IProject sampleEARIProject;
  private String sampleEARProject;
  
  public AddModuleDependenciesCommand(TestInfo testInfo)
  {
    this.testInfo = testInfo;
  }

  // Defect 200193 - This default constructor is put as a temporary fix for allowing other
  // code to have access to methods in this command without calling execute().  
  public AddModuleDependenciesCommand()
  {   
  }
  
  /**
   * Execute WebServerDefaultingTask Set the default server name and id given a
   * deployable.
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  IEnvironment env = getEnvironment();
	  //1. Create a Web project for the sample if one does not already exist.
	  sampleIProject = ProjectUtilities.getProject(testInfo.getGenerationProject());
	  boolean createdSampleProject = false;
      if (!sampleIProject.exists())
      {    	
          CreateFacetedProjectCommand command = new CreateFacetedProjectCommand();
          command.setProjectName(testInfo.getGenerationProject());
          command.setTemplateId(IJ2EEModuleConstants.JST_WEB_TEMPLATE);          
          // RequiredFacetVersions is set to an empty array because we don't need to impose any additional constraints.
          // We just want to create the highest level of Web project that the selected server supports.
          command.setRequiredFacetVersions(new RequiredFacetVersion[0]);           
          command.setServerFactoryId(testInfo.getClientServerTypeID());
          command.setServerInstanceId(testInfo.getClientExistingServer().getId());
          IStatus status = command.execute( monitor, adaptable );
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError( status );
            return status;
          }                    	  
          createdSampleProject = true;
      }
	  
	  //2. If the selected server requires an EAR and no EAR name
	  //has been provided, choose an EAR name and create it if it doesn't exist.
      ValidationUtils vu = new ValidationUtils();
      boolean serverNeedsEAR = vu.serverNeedsEAR(testInfo.getClientServerTypeID());
      if (serverNeedsEAR) {
			if (testInfo.getClientEARProject() == null
					|| testInfo.getClientEARProject().length() == 0) {
				sampleEARProject = testInfo.getGenerationProject() + DEFAULT_SAMPLE_EAR_PROJECT_EXT;
			} else {
				sampleEARProject = testInfo.getClientEARProject();
			}
			sampleEARIProject  = ProjectUtilities.getProject(sampleEARProject);
			if (sampleEARIProject == null || !sampleEARIProject.exists())
			{
		          CreateFacetedProjectCommand command = new CreateFacetedProjectCommand();
		          command.setProjectName(sampleEARProject);
		          command.setTemplateId(IJ2EEModuleConstants.JST_EAR_TEMPLATE);          
		          // RequiredFacetVersions is set to an empty array because we don't need to impose any additional constraints.
		          // We just want to create the highest level of Web project that the selected server supports.
		          command.setRequiredFacetVersions(new RequiredFacetVersion[0]);           
		          command.setServerFactoryId(testInfo.getClientServerTypeID());
		          command.setServerInstanceId(testInfo.getClientExistingServer().getId());
		          IStatus status = command.execute( monitor, adaptable );
		          if (status.getSeverity() == Status.ERROR)
		          {
		            env.getStatusHandler().reportError( status );
		            return status;
		          }                    	  				
			}
			
	  }
 
	  // 3. If the selected server requires an EAR, and the sample project has
		// not already been added to the EAR, add it.
      if (serverNeedsEAR)
      {
  	    AssociateModuleWithEARCommand associateCommand = new AssociateModuleWithEARCommand();
	    associateCommand.setProject(testInfo.getGenerationProject());
	    associateCommand.setEARProject(sampleEARProject);
	    associateCommand.setEar(sampleEARProject);
		associateCommand.setEnvironment( env );
	    IStatus status = associateCommand.execute( monitor, null );
	    if (status.getSeverity()==Status.ERROR)
	    {
	      env.getStatusHandler().reportError(status);     
	    }      	  
      }
	  

      
	  
	  // 4. If server requires an EAR, and the sample EAR has not already been
		// added to the server, add it.
	  //   If no EAR is required, and sample project has not been added to the server add it.
      if (serverNeedsEAR)
      {
    	//Add sampleEARIProject to the server if needed.
  		AddModuleToServerCommand modToServer = new AddModuleToServerCommand();
		modToServer.setModule(sampleEARProject);
		modToServer.setProject(sampleEARProject);
		modToServer.setServerInstanceId(testInfo.getClientExistingServer().getId());
		modToServer.setEnvironment( env );
		IStatus status = modToServer.execute( monitor, null );
		if (status.getSeverity()==Status.ERROR)
	    {
	      env.getStatusHandler().reportError(status);     
	    }         	  
      }
      else
      {
    	  //add sampleIProject directly to the server if needed.
          AddModuleToServerCommand addToServer = new AddModuleToServerCommand();
          addToServer.setModule(testInfo.getGenerationProject());
          addToServer.setProject(testInfo.getGenerationProject());
          addToServer.setServerInstanceId(testInfo.getClientExistingServer().getId());
          addToServer.setEnvironment( env );
          IStatus status = addToServer.execute( monitor, null );
          if (status.getSeverity()==Status.ERROR)
          {
            env.getStatusHandler().reportError(status);     
          }                  	      	  
      }
	  
	  //5. Call StartServerCommand if this command had to create the sample project.
      if (createdSampleProject)
      {
		StartServerCommand startServer = new StartServerCommand(true);
		startServer.setServerInstanceId(testInfo.getClientExistingServer().getId());
		startServer.setEnvironment( env );
		IStatus status = startServer.execute( monitor, null );
	    if (status.getSeverity()==Status.ERROR)
	    {
	      env.getStatusHandler().reportError(status);     
	    }
      }
      
      
	  //6. Establish all necessary dependencies between client project, sample project, and EAR
      
	  clientIProject = ProjectUtilities.getProject(testInfo.getClientProject());

		if (clientIProject != null && !J2EEUtils.isWebComponent(clientIProject)) {
			if (J2EEUtils.isJavaComponent(clientIProject)) {				
				addJavaProjectAsUtilityJar(clientIProject, sampleEARIProject, monitor);
				addJavaProjectAsUtilityJar(clientIProject, sampleIProject,monitor);
			}

				try
				{
				  String uri = clientIProject.getName() + ".jar";
				  addJAROrModuleDependency(sampleIProject, uri);
				} catch (CoreException ce)
				{
					String errorMessage = NLS.bind(ConsumptionUIMessages.MSG_ERROR_MODULE_DEPENDENCY, new String[]{sampleIProject.getName(), clientIProject.getName()});
					IStatus errorStatus = StatusUtils.errorStatus(errorMessage);
					env.getStatusHandler().reportError(errorStatus);
				} catch (IOException ioe)
				{
					String errorMessage = NLS.bind(ConsumptionUIMessages.MSG_ERROR_MODULE_DEPENDENCY, new String[]{sampleIProject.getName(), clientIProject.getName()});
					IStatus errorStatus = StatusUtils.errorStatus(errorMessage);
					env.getStatusHandler().reportError(errorStatus);					
				}							
			
			try {		
				addBuildPath(sampleIProject, clientIProject);
			} catch (JavaModelException jme) {
				// Do nothing in this catch block. The worst that
				// will happen is that the sample Web project
				// will show some compile errors. The sample will
				// likely still launch successfully on the server
				// and the user will be able to use it.
			}
		}      	  
    
    return Status.OK_STATUS;
  }
  
  public void addJAROrModuleDependency(IProject project, String uri) throws IOException, CoreException
  {
    if (J2EEUtils.isWebComponent(project))
    {
      ArchiveManifest manifest = J2EEProjectUtilities.readManifest(project);
      manifest.mergeClassPath(new String[]{uri});
      J2EEProjectUtilities.writeManifest(project, manifest);
    }
  }
  
  public void addJavaProjectAsUtilityJar(IProject javaProject, IProject earProject,IProgressMonitor monitor)
  {
	  try {
		  IDataModel migrationdm = DataModelFactory.createDataModel(new JavaProjectMigrationDataModelProvider());
		  migrationdm.setProperty(IJavaProjectMigrationDataModelProperties.PROJECT_NAME, javaProject.getName());
		  migrationdm.setProperty(IJavaProjectMigrationDataModelProperties.ADD_TO_EAR, Boolean.FALSE);
		  migrationdm.getDefaultOperation().execute(monitor, null);
 
 
		  IDataModel refdm = DataModelFactory.createDataModel(new CreateReferenceComponentsDataModelProvider());
		  List targetCompList = (List) refdm.getProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST);
 
		  IVirtualComponent targetcomponent = ComponentCore.createComponent(javaProject);
		  IVirtualComponent sourcecomponent = ComponentUtilities.getComponent(earProject.getName());
		  targetCompList.add(targetcomponent);
 
		  refdm.setProperty(ICreateReferenceComponentsDataModelProperties.SOURCE_COMPONENT,sourcecomponent );
		  refdm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST, targetCompList);
		  refdm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENTS_DEPLOY_PATH,  "/WEB-INF/lib");
		  refdm.getDefaultOperation().execute(monitor, null);
	  }catch (Exception e) {
		  
	  }
  }

  
  public void addBuildPath(IProject referencingProject, IProject referencedProject) throws JavaModelException
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
 

  public static final String DEFAULT_SAMPLE_EAR_PROJECT_EXT = "EAR";
 
}

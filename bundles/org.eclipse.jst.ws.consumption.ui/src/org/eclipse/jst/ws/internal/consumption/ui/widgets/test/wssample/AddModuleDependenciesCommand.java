/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
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
 * 20080211   117924 trungha@ca.ibm.com - Trung Ha
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.classpathdep.UpdateClasspathAttributeUtil;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
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
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
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
				J2EEUtils.addJavaProjectAsUtilityJar(clientIProject, sampleEARIProject, monitor);
				J2EEUtils.addJavaProjectAsUtilityJar(clientIProject, sampleIProject,monitor);
			}

				try
				{
				  String uri = clientIProject.getName() + ".jar";
				  J2EEUtils.addJAROrModuleDependency(sampleIProject, uri);
				  
				  // Adding the attribute to the referenced project's classpath entries of 'lib' kind
				  // so that these libs will be bundled along with the project when exported
				  IClasspathEntry[] classPath = JavaCore.create(clientIProject).getRawClasspath();
				  for (int i = 0; i < classPath.length; i++) {
					IClasspathEntry classpathEntry = classPath[i];
					if ( classpathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY ){
						UpdateClasspathAttributeUtil.addDependencyAttribute(monitor, clientIProject.getName(), classpathEntry);
					}
				  }
				  
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
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}							
			
			
		}      	  
    
    return Status.OK_STATUS;
  }
  
 

  
  public static final String DEFAULT_SAMPLE_EAR_PROJECT_EXT = "EAR";
 
}

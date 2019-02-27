/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070502  185208 sengpl@ca.ibm.com - Seng Phung-Lu      
 * 20070509  180567 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20070705  195553 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20080313  126774 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20100507   312131 mahutch@ca.ibm.com - Mark Hutchinson, ws performance test JUnits can have inconsistent results due to background jobs running
 *******************************************************************************/
package org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.tests.axis.tomcat.v50.WSWizardTomcat50Test;
import org.eclipse.jst.ws.tests.performance.util.PerformanceJUnitUtils;
import org.eclipse.jst.ws.tests.performance.util.PerformanceJobUtil;
import org.eclipse.jst.ws.tests.unittest.WSJUnitConstants;
import org.eclipse.jst.ws.tests.util.JUnitUtils;
import org.eclipse.jst.ws.tests.util.ScenarioConstants;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;
import org.eclipse.wst.command.internal.env.eclipse.AccumulateStatusHandler;

/**
 * Client performance scenario with Axis and Tomcat v5.0
 */
public class PerfmsrClientAxisTC50 extends WSWizardTomcat50Test {

	private final String WS_RUNTIMEID_AXIS = WSJUnitConstants.WS_RUNTIMEID_AXIS;
  
	private final String CLIENT_PROJECT_NAME = WSJUnitConstants.CLIENT_PROJECT_NAME;
	
	private IFile sourceFile_;

    protected void createProjects() throws Exception{
        IProject webProject = ProjectUtilities.getProject(CLIENT_PROJECT_NAME);
        if (!webProject.exists()){
          JUnitUtils.createWebModule(CLIENT_PROJECT_NAME, CLIENT_PROJECT_NAME, server_.getId(),SERVERTYPEID_TC50, "14", env_, new NullProgressMonitor());
        }
      }
      
    
	/**
   * Sets up the input data;
   * - create project(s),
   * - copy resources to workspace 
	 */
	protected void installInputData() throws Exception {
		
	
		IProject webProject = ProjectUtilities.getProject(CLIENT_PROJECT_NAME);
        IFolder destFolder = (IFolder)J2EEUtils.getWebContentContainer(webProject);
        JUnitUtils.copyTestData("TDJava",destFolder,env_, null);
		sourceFile_ = destFolder.getFile(new Path("Echo.wsdl"));		

		JUnitUtils.disableValidation(webProject);
		JUnitUtils.syncBuildProject(webProject,env_, null);
		
	}

  /**
   * Set the persistent server runtime context preferences
   */
	protected void initJ2EEWSRuntimeServerDefaults() throws Exception {
        // Set default preferences for Axis and Tomcat 5.0    
		JUnitUtils.setWSRuntimeServer(WS_RUNTIMEID_AXIS, SERVERTYPEID_TC50);
		JUnitUtils.setClientScenarioDefault();
	}

  /**
   * Set the initial selection
   */
	protected void initInitialSelection() throws Exception {
		initialSelection_ = new StructuredSelection(sourceFile_);
	}

  /**
   * Launches the pop-up command to initiate the scenario
   * @throws Exception
   */
	public void testClientAxisTC50() throws Exception
	{	
		PerformanceJobUtil.waitForStartupJobs();
	  	IStatus[] status;
	  	
		JUnitUtils.enableProxyGeneration(true);
		JUnitUtils.enableOverwrite(true);
		JUnitUtils.setRuntimePreference("org.eclipse.jst.ws.axis.creation.axisWebServiceRT");
	
		Performance perf= Performance.getDefault();
		PerformanceMeter performanceMeter= perf.createPerformanceMeter(perf.getDefaultScenarioId(this));	    
	    try {
    
	      performanceMeter.start();
	      status = PerformanceJUnitUtils.launchCreationWizard(ScenarioConstants.WIZARDID_CLIENT,ScenarioConstants.OBJECT_CLASS_ID_IFILE,initialSelection_);
	      performanceMeter.stop();
	      performanceMeter.commit();
	      perf.assertPerformance(performanceMeter);
	    }
	    finally {
	    	if (performanceMeter==null)
	    		performanceMeter.dispose();
	 	}
	    
	    verifyOutput(status);


	}
	
  /**
   * Verify the scenario completed succesfully
   * @throws Exception
   */
	private final void verifyOutput(IStatus[] status) throws Exception {
        IProject webProject = ProjectUtilities.getProject(CLIENT_PROJECT_NAME);
    
        IPath destPath = ResourceUtils.getJavaSourceLocation(webProject);
        IFolder srcFolder = (IFolder)ResourceUtils.findResource(destPath);
    
		//IFolder srcFolder = JUnitUtils.getSourceFolderForWebProject(CLIENT_PROJECT_NAME);
		IFolder folder = srcFolder.getFolder("foo");
		assertTrue(folder.exists());
		assertTrue(folder.members().length > 0);
		
		AccumulateStatusHandler statusHandler = new AccumulateStatusHandler(status);
		IStatus[] s = statusHandler.getErrorReports();
		//
		if (s.length > 0){
			for (int i=0;i<s.length;i++){
				System.out.println("Error message for report #"+i+": "+s[i].getMessage());
			}
		}
		assertTrue(s.length == 0);       

	}
	
  /**
   * Remove workspace if necessary
   */
	protected void deleteInputData() throws Exception {

		// Delete the Web project.
		IProject webProject = ProjectUtilities.getProject(CLIENT_PROJECT_NAME);
		webProject.delete(true,true, null);
		
	}

}

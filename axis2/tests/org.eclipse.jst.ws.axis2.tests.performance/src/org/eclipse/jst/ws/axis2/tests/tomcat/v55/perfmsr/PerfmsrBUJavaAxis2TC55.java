/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070705  195553 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20080313  126774 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20090402  263873 mahutch@ca.ibm.com - Mark Hutchinson, Move Axis2 peformance tests to new plugin
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.tests.tomcat.v55.perfmsr;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.ws.axis2.tests.tomcat.v55.WSWizardTomcat55Test;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.tests.performance.util.PerformanceJUnitUtils;
import org.eclipse.jst.ws.tests.unittest.WSJUnitConstants;
import org.eclipse.jst.ws.tests.util.JUnitUtils;
import org.eclipse.jst.ws.tests.util.ScenarioConstants;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;
import org.eclipse.wst.command.internal.env.eclipse.AccumulateStatusHandler;

/**
 * Bottom up performance scenario with Axis2 and Tomcat v5.5
 */
public final class PerfmsrBUJavaAxis2TC55 extends WSWizardTomcat55Test {
	// Constants
    private final String WS_RUNTIMEID_AXIS =  WSJUnitConstants.WS_RUNTIMEID_AXIS2; 
	private final String PROJECT_NAME = "BUAxis2Web";
    
	private IFile sourceFile_;
	
  /**
   * Sets up the input data;
   * - create project(s),
   * - copy resources to workspace 
   */
	protected void installInputData() throws Exception
	{
    
		IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);
        IPath destPath = ResourceUtils.getJavaSourceLocation(webProject);
        IFolder folder = (IFolder)ResourceUtils.findResource(destPath);		
		JUnitUtils.copyTestData("BUJava/src",folder,env_, null);
		sourceFile_ = folder.getFile(new Path("foo/Echo.java"));
		// Ensure that Echo.class is built in:
		// <Web Project>/WebContent/WEB-INF/classes/foo/Echo.class
		JUnitUtils.disableValidation(webProject);
		JUnitUtils.syncBuildProject(webProject,env_, null);
		//assertTrue(JUnitUtils.getClassesFolderForWebProject(WEB_PROJECT_NAME).getFile(new Path("foo/Echo.class")).exists());
		
		
	}
	
    protected void createProjects() throws Exception{
        IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);
        if (webProject==null || !webProject.exists()){
          JUnitUtils.createWebModule(PROJECT_NAME, PROJECT_NAME, server_.getId(), SERVERTYPEID_TC55, "14", env_, new NullProgressMonitor());
        }
      }
	
  /**
   * Set the persistent server runtime context preferences
   */  
	protected void initJ2EEWSRuntimeServerDefaults() throws Exception
	{
		// Set default preferences for Axis2 and Tomcat 5.5
		JUnitUtils.setWSRuntimeServer(WS_RUNTIMEID_AXIS, SERVERTYPEID_TC55);		
		JUnitUtils.setServiceScenarioDefault();
	}
	
  /**
   * Set the initial selection
   */
	protected void initInitialSelection() throws Exception
	{
		initialSelection_ = new StructuredSelection(sourceFile_);
	}
	
  /**
   * Launches the pop-up command to initiate the scenario
   * @throws Exception
   */  
	public void testBUJavaAxis2TC55() throws Exception
	{
	  	IStatus[] status;
	    IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);
	    JUnitUtils.disableWSIDialog(webProject);
		JUnitUtils.enableOverwrite(true);
	    JUnitUtils.setRuntimePreference(WS_AXIS2_RUNTIME);

	    
	    Performance perf= Performance.getDefault();
	    PerformanceMeter performanceMeter= perf.createPerformanceMeter(perf.getDefaultScenarioId(this));	    
	    try {
    
	      performanceMeter.start();
	      status = PerformanceJUnitUtils.launchCreationWizard(ScenarioConstants.WIZARDID_BOTTOM_UP,ScenarioConstants.OBJECT_CLASS_ID_IFILE,initialSelection_);
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
   * Verify the scenario completed successfully
   * @throws Exception
   */
	private final void verifyOutput(IStatus[] status) throws Exception
	{
        IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);    
		IFolder webContentFolder = (IFolder)J2EEUtils.getWebContentContainer(webProject);
    
        IFolder webInfFolder = webContentFolder.getFolder("WEB-INF");
        IFolder servicesFolder = webInfFolder.getFolder("services");
		assertTrue(servicesFolder.exists());
		IFolder wsFolder = servicesFolder.getFolder("Echo");
		assertTrue(wsFolder.exists());
		assertTrue(wsFolder.members().length > 0);
    
		AccumulateStatusHandler statusHandler = new AccumulateStatusHandler(status);
		IStatus[] s = statusHandler.getErrorReports();
		//show errors
		if (s.length > 0){
			for (int i=0;i<s.length;i++){
				System.out.println("BUJava Error message for report #"+i+": "+s[i].getMessage());
			}
		}
		assertTrue(s.length == 0);
	}
	
  /**
   * Clear workspace if necessary
   */
	protected void deleteInputData() throws Exception
	{
		// Remove the EAR from the server.
		IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);
		//JUnitUtils.removeModuleFromServer(server_,webProject,env_);
		
		// Delete the Web project.
		webProject.delete(true,true, null);
        assertFalse(webProject.exists());
		
	}
}

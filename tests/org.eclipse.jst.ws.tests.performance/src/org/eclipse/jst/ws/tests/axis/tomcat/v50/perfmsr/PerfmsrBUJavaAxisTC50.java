package org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.tests.axis.tomcat.v50.WSWizardTomcat50Test;
import org.eclipse.jst.ws.tests.util.JUnitUtils;
import org.eclipse.jst.ws.tests.util.ScenarioConstants;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

/**
 * Bottom up performance scenario with Axis and Tomcat v5.0
 */
public final class PerfmsrBUJavaAxisTC50 extends WSWizardTomcat50Test {
	// Constants
    private final String WS_RUNTIMEID_AXIS = "org.eclipse.jst.ws.runtime.axis11";
	private final String PROJECT_NAME = "TestBUWeb";
    private final String WEB_MODULE_NAME = "TestBUWebModule";

	private IFile sourceFile_;
	
  /**
   * Sets up the input data;
   * - create project(s),
   * - copy resources to workspace 
   */
	protected void installInputData() throws Exception
	{
		// Create a Web project (TestWeb) targetted to Tomcat 5.0
		Status s = JUnitUtils.createWebModule(PROJECT_NAME, WEB_MODULE_NAME, SERVERTYPEID_TC50, String.valueOf(J2EEVersionConstants.J2EE_1_3_ID), env_);
		if (s.getSeverity() != Status.OK) {
		  System.out.println("Error: "+s.getMessage());
			throw new Exception(s.getThrowable());
		}
		IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);
		assertTrue(webProject.exists());

		
		// Copy the contents of data/<test name> to the Web project's source folder.
		// <Web Project>/JavaSource/foo/Echo.java
		//IFolder destFolder = JUnitUtils.getSourceFolderForWebProject(WEB_PROJECT_NAME);
        IPath destPath = ResourceUtils.getJavaSourceLocation(webProject, WEB_MODULE_NAME);
        IFolder folder = (IFolder)ResourceUtils.findResource(destPath);
		JUnitUtils.copyTestData("BUJava/src",folder,env_);
		sourceFile_ = folder.getFile(new Path("foo/Echo.java"));
		assertTrue(sourceFile_.exists());
		
		// Ensure that Echo.class is built in:
		// <Web Project>/WebContent/WEB-INF/classes/foo/Echo.class
		JUnitUtils.syncBuildProject(webProject,env_);
		//assertTrue(JUnitUtils.getClassesFolderForWebProject(WEB_PROJECT_NAME).getFile(new Path("foo/Echo.class")).exists());
		
		
	}
	
  /**
   * Set the persistent server runtime context preferences
   */  
	protected void initJ2EEWSRuntimeServerDefaults() throws Exception
	{
		// Set default preferences for J2EE 1.4 Axis and Tomcat 5.0
		JUnitUtils.setJ2EEWSRuntimeServer(String.valueOf(J2EEVersionConstants.J2EE_1_3_ID), WS_RUNTIMEID_AXIS, SERVERTYPEID_TC50);		
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
	public void testBUJavaAxisTC50() throws Exception
	{
	  	Status status = new SimpleStatus("");
	    IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);
	    JUnitUtils.disableWSIDialog(webProject);

	    Performance perf= Performance.getDefault();
	    PerformanceMeter performanceMeter= perf.createPerformanceMeter(perf.getDefaultScenarioId(this));	    
	    try {
    
	      performanceMeter.start();
	      status = JUnitUtils.launchCreationWizard(ScenarioConstants.WIZARDID_BOTTOM_UP,ScenarioConstants.OBJECT_CLASS_ID_IFILE,initialSelection_);
	      performanceMeter.stop();

	      performanceMeter.commit();
	      perf.assertPerformance(performanceMeter);
	    }
	    finally {
			performanceMeter.dispose();
	 	}
		
		if (status.getSeverity() == Status.OK)
		  verifyOutput();
		else
		  throw new Exception(status.getThrowable());
		
	}
	
  /**
   * Verify the scenario completed successfully
   * @throws Exception
   */
	private final void verifyOutput() throws Exception
	{
        IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);    
		IFolder webContentFolder = (IFolder)J2EEUtils.getWebContentContainer(webProject, WEB_MODULE_NAME);
    
        IPath webInfPath = J2EEUtils.getWebInfPath(webProject, WEB_MODULE_NAME);
		IFolder webInfFolder = (IFolder)ResourceUtils.findResource(webInfPath);

        IFolder wsdlFolder = webContentFolder.getFolder("wsdl");
		assertTrue(wsdlFolder.exists());
		assertTrue(wsdlFolder.members().length > 0);
		assertTrue(webContentFolder.getFolder("wsdl").members().length > 0);
    
        //TODO Check if wsdd contains new Web service
        //TODO Check if Web serivce can be invoked by a client
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
		webProject.delete(true,true,EnvironmentUtils.getIProgressMonitor(env_));
        assertFalse(webProject.exists());
		
	}
}
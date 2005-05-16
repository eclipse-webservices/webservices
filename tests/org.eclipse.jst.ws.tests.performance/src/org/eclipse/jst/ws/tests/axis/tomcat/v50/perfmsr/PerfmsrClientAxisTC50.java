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
 * Client performance scenario with Axis and Tomcat v5.0
 */
public class PerfmsrClientAxisTC50 extends WSWizardTomcat50Test {

  private final String WS_RUNTIMEID_AXIS = "org.eclipse.jst.ws.runtime.axis11";
  
	private final String CLIENT_PROJECT_NAME = "TestWebClient";
  private final String CLIENT_WEB_MODULE_NAME =  "TestWebClientModule";

	private IFile sourceFile_;

	/**
   * Sets up the input data;
   * - create project(s),
   * - copy resources to workspace 
	 */
	protected void installInputData() throws Exception {
		
		// Create an associated Web project (TestWeb) targetted to Tomcat v5.0 
		Status s = JUnitUtils.createWebProject(CLIENT_PROJECT_NAME, CLIENT_WEB_MODULE_NAME, SERVERTYPEID_TC50, String.valueOf(J2EEVersionConstants.J2EE_1_4_ID), env_);
		if (s.getSeverity() != Status.OK)
			throw new Exception(s.getThrowable());
    
		IProject webProject = ProjectUtilities.getProject(CLIENT_PROJECT_NAME);
		assertTrue(webProject.exists());

    // Copy Echo.wsdl file to WebContent folder
    IFolder destFolder = (IFolder)J2EEUtils.getWebContentContainer(webProject, CLIENT_WEB_MODULE_NAME);
		JUnitUtils.copyTestData("TDJava",destFolder,env_);
		sourceFile_ = destFolder.getFile(new Path("Echo.wsdl"));
		assertTrue(sourceFile_.exists());


	}

  /**
   * Set the persistent server runtime context preferences
   */
	protected void initJ2EEWSRuntimeServerDefaults() throws Exception {
    // Set default preferences for J2EE 1.4 Axis and Tomcat 5.0    
		JUnitUtils.setJ2EEWSRuntimeServer(String.valueOf(J2EEVersionConstants.J2EE_1_4_ID), WS_RUNTIMEID_AXIS, SERVERTYPEID_TC50);
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
	  	Status status = new SimpleStatus("");
	  	
		  JUnitUtils.enableProxyGeneration(true);
		  JUnitUtils.enableOverwrite(true);
		Performance perf= Performance.getDefault();
		PerformanceMeter performanceMeter= perf.createPerformanceMeter(perf.getDefaultScenarioId(this));	    
	    try {
    
	      performanceMeter.start();
	      status = JUnitUtils.launchCreationWizard(ScenarioConstants.WIZARDID_CLIENT,ScenarioConstants.OBJECT_CLASS_ID_IFILE,initialSelection_);
	      performanceMeter.stop();
	      performanceMeter.commit();
	      perf.assertPerformance(performanceMeter);
	    }
	    finally {
			performanceMeter.dispose();
	 	}
	    
		if (status.getSeverity() == Status.OK) {
		  verifyOutput();
		} else {
		  throw new Exception(status.getThrowable());
		}

	}
	
  /**
   * Verify the scenario completed succesfully
   * @throws Exception
   */
	private final void verifyOutput() throws Exception {
    IProject webProject = ProjectUtilities.getProject(CLIENT_PROJECT_NAME);
    
    IPath destPath = ResourceUtils.getJavaSourceLocation(webProject, CLIENT_WEB_MODULE_NAME);
    IFolder srcFolder = (IFolder)ResourceUtils.findResource(destPath);
    
		//IFolder srcFolder = JUnitUtils.getSourceFolderForWebProject(CLIENT_PROJECT_NAME);
		IFolder folder = srcFolder.getFolder("foo");
		assertTrue(folder.exists());
		assertTrue(folder.members().length > 0);
		
		//TODO Check that the client runs    

	}
	
  /**
   * Remove workspace if necessary
   */
	protected void deleteInputData() throws Exception {

		// Delete the Web project.
		IProject webProject = ProjectUtilities.getProject(CLIENT_PROJECT_NAME);
		webProject.delete(true,true,EnvironmentUtils.getIProgressMonitor(env_));
		
	}

}

package org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.tests.axis.tomcat.v50.WSWizardTomcat50Test;
import org.eclipse.jst.ws.tests.performance.util.PerformanceJUnitUtils;
import org.eclipse.jst.ws.tests.unittest.WSJUnitConstants;
import org.eclipse.jst.ws.tests.util.JUnitUtils;
import org.eclipse.jst.ws.tests.util.ScenarioConstants;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Bottom up performance scenario with Axis and Tomcat v5.0
 */
public final class PerfmsrBUJavaAxisTC50 extends WSWizardTomcat50Test {
	// Constants
    private final String WS_RUNTIMEID_AXIS =  WSJUnitConstants.WS_RUNTIMEID_AXIS; 
	private final String PROJECT_NAME = WSJUnitConstants.BU_PROJECT_NAME;
    
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
		sourceFile_ = folder.getFile(new Path("foo/Echo.java"));
		// Ensure that Echo.class is built in:
		// <Web Project>/WebContent/WEB-INF/classes/foo/Echo.class
		JUnitUtils.syncBuildProject(webProject,env_, null);
		//assertTrue(JUnitUtils.getClassesFolderForWebProject(WEB_PROJECT_NAME).getFile(new Path("foo/Echo.class")).exists());
		
		
	}
	
	
	
  /**
   * Set the persistent server runtime context preferences
   */  
	protected void initJ2EEWSRuntimeServerDefaults() throws Exception
	{
		// Set default preferences for Axis and Tomcat 5.0
		JUnitUtils.setWSRuntimeServer(WS_RUNTIMEID_AXIS, SERVERTYPEID_TC50);		
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
	  	IStatus status = Status.OK_STATUS;
	    IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);
	    JUnitUtils.disableWSIDialog(webProject);

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
			performanceMeter.dispose();
	 	}
		
		if (status.getSeverity() == Status.OK)
		  verifyOutput();
		else
		  throw new Exception(status.getException());
		
	}
	
  /**
   * Verify the scenario completed successfully
   * @throws Exception
   */
	private final void verifyOutput() throws Exception
	{
        IProject webProject = ProjectUtilities.getProject(PROJECT_NAME);    
		IFolder webContentFolder = (IFolder)J2EEUtils.getWebContentContainer(webProject);
    
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
		webProject.delete(true,true, null);
        assertFalse(webProject.exists());
		
	}
}
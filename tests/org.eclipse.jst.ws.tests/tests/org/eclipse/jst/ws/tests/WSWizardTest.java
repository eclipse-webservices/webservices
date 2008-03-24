/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 2007104   114835 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20071217  187280 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20080207  217346 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20080313  126774 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.tests;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.tests.plugin.TestsPlugin;
import org.eclipse.jst.ws.tests.unittest.WSJUnitConstants;
import org.eclipse.jst.ws.tests.util.JUnitUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseStatusHandler;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;

public abstract class WSWizardTest extends TestCase implements WSJUnitConstants
{
	protected IEnvironment env_;
	protected IRuntime serverRuntime_;
	protected IServer server_;
	protected IStructuredSelection initialSelection_;
  
  public String defaultURL_ = "http://localhost:8080/";
	
	/**
	 * Set up the workspace for the Web Service Wizard JUnit test. Setup consists of the following steps:
	 * 1) Obtain an instance of the environment.
	 * 2) Install the server runtime.
	 * 3) Install the input data. This may include projects, source files etc.
	 * 4) Install an instance of the server if required. This may also include configuration of the input data on the server.
	 * 5) Initialize the J2EE, Web Service Runtime and Server type defaults for the Web Service wizard to use.
	 * 6) Initialize the initial selection object to set the context for the Web Service wizard.
	 * @throws Exception
	 */
	protected void setUp() throws Exception
	{
		// Get an instance of the default environment with the AccumulateStatusHandler to minimize UI dialog blocks.
	  	PersistentResourceContext  resourceContext = PersistentResourceContext.getInstance();
		EclipseStatusHandler       handler         = new EclipseStatusHandler();
		EclipseEnvironment         environment     = new EclipseEnvironment( null, resourceContext, handler );
	  
		env_ = environment; 
		assertTrue(env_ != null);
		
		server_ = null;
		JUnitUtils.hideActionDialogs();
		installServerRuntime();
		installServer();
		
		// unzip pre-configured workspace projects
		//if (!ProjectUtilities.getProject(WSJUnitConstants.BU_PROJECT_NAME).exists())
			createProjects();		
		
		installInputData();
		initJ2EEWSRuntimeServerDefaults();
		initInitialSelection();
	}
	
	/**
	 * Install the server runtime.
	 * @throws Exception
	 */
	protected abstract void installServerRuntime() throws Exception;
	
	/**
	 * Installs the input data from an aggregator. This ensures that the environment, runtime
	 * and server are correctly set on the aggregate since its lifecycle methods are not necessarily
	 * called.
	 * @param env
	 * @param serverRuntime
	 * @param server
	 * @throws Exception
	 */
	public void installInputData(IEnvironment env,IRuntime serverRuntime,IServer server) throws Exception
	{
		env_ = env;
		serverRuntime_ = serverRuntime;
		server_ = server;
		//installInputData();
	}
	
	// Creates projects from the provided ZIP file.
//	public static boolean createProjects() {
//		IPath localZipPath = getLocalPath();
//		ProjectUnzipUtil util = new ProjectUnzipUtil(localZipPath, perf_projectNames);
//		return util.createProjects();
//	}
	
//	private static IPath getLocalPath() {
//		URL url = TestsPlugin.getDefault().find(perf_zipFilePath);
//		try {
//			url = Platform.asLocalURL(url);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return new Path(url.getPath());
//	}	
	
    /**
     * Create the necessary projects for the tests.  This could be Web, Java, or other J2EE projects.
     * @throws Exception
     */
    protected abstract void createProjects() throws Exception;
	
	/**
	 * Install the input data for the test. This may include projects, source files etc.
	 * @throws Exception
	 */
	protected abstract void installInputData() throws Exception;
	
	/**
	 * Install an instance of the server. This may also configure input data on the server.
	 * @throws Exception
	 */
	protected abstract void installServer() throws Exception;
	
	/**
	 * Initialize the J2EE level, Web Service runtime type and Server defaults for the test case.
	 * @throws Exception
	 */
	protected abstract void initJ2EEWSRuntimeServerDefaults() throws Exception;
	
	/**
	 * Initialize the initial selection which drives the Web Service wizard. This could include source Java files,
	 * WSDL URLs and even EJBs.
	 * @throws Exception
	 */
	protected abstract void initInitialSelection() throws Exception;
	
	/**
	 * Clean up the workspace. Cleanup consists of the following steps:
	 * 1) Delete the input data. This should include removal of configuration data on the server if necessary.
	 * 2) Stop the server.
	 * 3) Delete the server.
	 * 4) Delete the server runtime.
	 */
	protected void tearDown() throws Exception
	{
		stopServer();
//		deleteServer();
//      deleteInputData();
//		deleteServerRuntime();
	}
	
	/**
	 * Delete the input data. This should include removal of projects, src files etc. as well as removal of any server configured data.
	 * @throws Exception
	 */
	protected abstract void deleteInputData() throws Exception;
	
	/**
	 * Stop the server if it is running.
	 * @throws Exception
	 */
	protected void stopServer() throws Exception
	{
		if (server_ != null && server_.getServerState() == IServer.STATE_STARTED)
			server_.stop(true);
		assertTrue(server_.getServerState() == IServer.STATE_STOPPED);
	}
	
	/**
	 * Delete the server.
	 * @throws Exception
	 */
	protected void deleteServer() throws Exception
	{
		if (server_ != null)
			server_.delete();
	}
	
	/**
	 * Delete the server runtime.
	 * @throws Exception
	 */
	protected void deleteServerRuntime() throws Exception
	{
		if (serverRuntime_ != null)
			serverRuntime_.delete();
	}
	
	/**
	 * Get the default URL in the form of: http://localhost:9080/<contextRoot>/<path>
	 * @param path - a file or URL relative path.
	 * @param contextRoot - the context root of the Web module for this URL.
	 * @return
	 */
	protected String getDefaultURL(String path,String contextRoot)
	{
		StringBuffer url = new StringBuffer(defaultURL_);
		url.append(contextRoot);
		url.append('/');
		url.append(path);
		return url.toString();
	}	
	
	protected void logBadStatus(IStatus status)
	{
		TestsPlugin.getDefault().getLog().log(StatusUtils.infoStatus("*** JST.WS JUNIT ERROR (START) ***"));
		TestsPlugin.getDefault().getLog().log(status);
		TestsPlugin.getDefault().getLog().log(StatusUtils.infoStatus("*** JST.WS JUNIT ERROR (END) ***"));
	}
	
	/**
	 * Spin the event loop for awhile to give background activity a chance to
	 * die down. This decreases the chance that unrelated background work will
	 * affect results for the scenario being tested.
	 * 
	 * @param duration
	 *            The time in milliseconds to spin the event loop.
	 */
	protected void runEventLoop(final int duration) {
		final Display display = Display.getDefault();
		Runnable update = new Runnable() {
			public void run() {
				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start < duration) {
					if (!display.readAndDispatch())
						display.sleep();
				}
			}
		};
		if (Display.getCurrent() == null)
			display.syncExec(update);
		else
			update.run();
	}
}

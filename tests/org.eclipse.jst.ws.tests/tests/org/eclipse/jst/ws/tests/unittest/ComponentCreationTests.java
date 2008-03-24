/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
 * 20080313  126774 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.tests.unittest;

import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.etools.common.test.apitools.ProjectUnzipUtil;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.tests.plugin.TestsPlugin;
import org.eclipse.jst.ws.tests.util.JUnitUtils;

/**
 * Tests the various Component creation commands
 */
public class ComponentCreationTests extends TestCase implements WSJUnitConstants {

	public static Test suite(){
		return new TestSuite(ComponentCreationTests.class);
	}
	
	public void testWebComponentCreation(){

        createServerRuntime();
        createDynamicWebModule(projectNames);
        //createProjects();
        //createWebModule(projectName, projectName, J2EEVersionConstants.J2EE_1_4_ID);
        //createWebModule(project2Name, project2Name, J2EEVersionConstants.J2EE_1_3_ID);

	}
  
	// Creates projects from the provided ZIP file.
	public static boolean createProjects() {
		IPath localZipPath = getLocalPath();
		ProjectUnzipUtil util = new ProjectUnzipUtil(localZipPath, projectNames);
		return util.createProjects();
	}
	

	private static IPath getLocalPath() {
		URL url = TestsPlugin.getDefault().find(zipFilePath);
		try {
			url = Platform.asLocalURL(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Path(url.getPath());
	}	
  
    public void createServerRuntime(){
      
      try {
        JUnitUtils.createServerRuntime(RUNTIMETYPEID_TC50, SERVER_INSTALL_PATH);
      }
      catch(Exception e){
        e.printStackTrace();
      }      
      
    }
    
    public void createDynamicWebModule(String[] projectNames){
    	
      for (int i=0;i<projectNames.length;i++) {
          JUnitUtils.createWebModule(projectNames[i], projectNames[i], null, SERVERTYPEID_TC50, "14", null, new NullProgressMonitor());

	      IProject p = ResourceUtils.getWorkspaceRoot().getProject(projectNames[i]);
	      assertTrue(p.exists());
      }
    }
  
}

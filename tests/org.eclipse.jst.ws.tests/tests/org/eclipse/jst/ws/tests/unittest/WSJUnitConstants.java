/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070705  195553 sengpl@ca.ibm.com - Seng Phung-Lu      
 *******************************************************************************/
package org.eclipse.jst.ws.tests.unittest;

import org.eclipse.core.runtime.Path;


public interface WSJUnitConstants {
  
  public final String SERVER_INSTALL_PATH = System.getProperty("org.eclipse.jst.server.tomcat.50");
  public final String RUNTIMETYPEID_TC50 = "org.eclipse.jst.server.tomcat.runtime.50";

  public final String WS_RUNTIMEID_AXIS = "org.eclipse.jst.ws.runtime.axis11";
  public final String WS_RUNTIMEID_AXIS2 = "org.eclipse.jst.ws.runtime.axis2";
  
  public final String WS_AXIS2_RUNTIME = "org.eclipse.jst.ws.axis2.creation.axis2WebServiceRT";
  
  public final String SERVERTYPEID_TC50 = "org.eclipse.jst.server.tomcat.50";
  
  public final String webProjectName = "WebProject";
  
  // flexible project structure 1
  public final String projectName = "FlexProject";
  public final String webComponentName = "webComponent";
  public final String webComponent2Name = "webComp2";

  // flexible project structure 2
  public final String project2Name = "FlexProject2";
  public final String webComp3Name = "webComp3";
  public final String webComp4Name = "webComp4";
    
  public final String ejbProjectName = "FlexEJBProject";
  public final String ejbComponentName = "ejbComponent";

  public final String appClientProjectName = "FlexAppClientProject";
  public final String appClientCompName = "appClientComponent";  
  
  public final String earCompName = "EARComponent";
  
  public final Path zipFilePath = new Path("/data/WSTestProjects.zip");
  public final String[] projectNames = new String[]{projectName, project2Name};
  
  // performance constants
  public final String BU_PROJECT_NAME = "TestWeb";
  public final String TD_PROJECT_NAME = "TestTDProject";
  public final String CLIENT_PROJECT_NAME = "TestWebClient";
  
  public final Path perf_zipFilePath = new Path("/data/WSPerfProjects.zip");
  public final String[] perf_projectNames = new String[]{BU_PROJECT_NAME, TD_PROJECT_NAME, CLIENT_PROJECT_NAME};
 
}

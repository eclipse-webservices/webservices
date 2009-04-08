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
 * 20090402  263873 mahutch@ca.ibm.com - Mark Hutchinson, Move Axis2 peformance tests to new plugin
 *******************************************************************************/

package org.eclipse.jst.ws.axis2.tests.tomcat.v55;

import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.tests.WSWizardTest;
import org.eclipse.jst.ws.tests.util.JUnitUtils;

public abstract class WSWizardTomcat55Test extends WSWizardTest {
  
	protected final String SERVER_INSTALL_PATH = System.getProperty("org.eclipse.jst.server.tomcat.55");
	protected final String RUNTIME_INSTALL_PATH = System.getProperty("org.eclipse.jst.runtime.axis2");
	protected final String RUNTIMETYPEID_TC55 = "org.eclipse.jst.server.tomcat.runtime.55";
	protected final String SERVERTYPEID_TC55 = "org.eclipse.jst.server.tomcat.55";
	  

  /* (non-Javadoc)
   * @see com.ibm.etools.webservice.was.tests.WSWizardTest#installServerRuntime()
   */
  protected void installServerRuntime() throws Exception {

	// create server runtime
    serverRuntime_ = JUnitUtils.createServerRuntime(RUNTIMETYPEID_TC55, SERVER_INSTALL_PATH);
    
    // set install locations
    Axis2EmitterContext context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
    if (context.getAxis2RuntimeLocation()==null || context.getAxis2RuntimeLocation().equals("")){
    	String runtimePath = RUNTIME_INSTALL_PATH;
    	if (runtimePath!=null) {
    		 context.setAxis2RuntimeLocation(runtimePath);
    	}        
    }
  }


  /* (non-Javadoc)
   * @see com.ibm.etools.webservice.was.tests.WSWizardTest#installServer()
   */
  protected void installServer() throws Exception {

    server_ = JUnitUtils.createServer("Apache Tomcat v55", SERVERTYPEID_TC55, serverRuntime_, env_, null );
  }
  
}


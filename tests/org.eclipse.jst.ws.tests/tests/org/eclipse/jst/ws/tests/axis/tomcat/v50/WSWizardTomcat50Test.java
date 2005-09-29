/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.tests.axis.tomcat.v50;

import org.eclipse.jst.ws.tests.WSWizardTest;
import org.eclipse.jst.ws.tests.util.JUnitUtils;


public abstract class WSWizardTomcat50Test extends WSWizardTest {
  
	protected final String SERVER_INSTALL_PATH = System.getProperty("org.eclipse.jst.server.tomcat.50");
	protected final String RUNTIMETYPEID_TC50 = "org.eclipse.jst.server.tomcat.runtime.50";
	protected final String SERVERTYPEID_TC50 = "org.eclipse.jst.server.tomcat.50";
	  

  /* (non-Javadoc)
   * @see com.ibm.etools.webservice.was.tests.WSWizardTest#installServerRuntime()
   */
  protected void installServerRuntime() throws Exception {

    serverRuntime_ = JUnitUtils.createServerRuntime(RUNTIMETYPEID_TC50, SERVER_INSTALL_PATH);
  }


  /* (non-Javadoc)
   * @see com.ibm.etools.webservice.was.tests.WSWizardTest#installServer()
   */
  protected void installServer() throws Exception {

    server_ = JUnitUtils.createServer("Apache Tomcat v50", SERVERTYPEID_TC50, serverRuntime_, env_, null );
  }
  
}

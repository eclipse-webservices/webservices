/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.jst.ws.tests.unittest;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateServerCommand;
import org.eclipse.jst.ws.tests.util.JUnitUtils;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class ServerCreationTests extends TestCase implements WSJUnitConstants{


	public static Test suite(){
		return new TestSuite(ServerCreationTests.class);
	}
	
	public void init(){
	    try {
          assertNotNull(SERVER_INSTALL_PATH);          
	      JUnitUtils.createServerRuntime(RUNTIMETYPEID_TC50, SERVER_INSTALL_PATH);
	    }
	    catch(Exception e){
	      e.printStackTrace();
	    } 
	}
	
	public void testCreateTomcatv5Server(){
		init();
		
		System.out.println("Creating Tomcat v5 server.");
		CreateServerCommand csc = new CreateServerCommand();
		csc.setServerFactoryid(SERVERTYPEID_TC50);
		csc.execute(null, null );
		
		IServer server = null;
		String instId = csc.getServerInstanceId();
		System.out.println("Server instance Id = "+instId);
		if (instId!=null){
		  server = ServerCore.findServer(instId);
		}
		assertNotNull(server);
		
		try {
			server.delete();
		}
		catch (CoreException ce){
			new AssertionFailedError();
		}
		
		System.out.println("Done creating Tomcat v5 server.");
	}
	
	public void testReattemptCreateTomcatv5Server(){
		System.out.println("Attempting to create 2nd Tomcat v5 server.");
		CreateServerCommand csc = new CreateServerCommand();
		csc.setServerFactoryid(SERVERTYPEID_TC50);
		csc.execute(null, null );
		
		IServer server = null;
		String instId = csc.getServerInstanceId();
		if (instId!=null) {
		  server = ServerCore.findServer(instId);
		}
		assertNotNull(server);
		
		try {
			server.delete();
		}
		catch (CoreException ce){
			new AssertionFailedError();
		}
		
		System.out.println("Done attempting 2nd Tomcat v5 server creation.");		
	}
	
}

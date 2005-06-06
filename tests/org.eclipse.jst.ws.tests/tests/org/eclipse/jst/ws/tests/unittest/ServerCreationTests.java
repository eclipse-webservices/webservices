/**
 * 
 */
package org.eclipse.jst.ws.tests.unittest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.internal.consumption.command.common.CreateServerCommand;

/**
 * @author sengpl
 *
 */
public class ServerCreationTests extends TestCase implements WSJUnitConstants{


	public static Test suite(){
		return new TestSuite(ServerCreationTests.class);
	}
	
	public void testCreateTomcatv5Server(){
		System.out.println("Creating Tomcat v5 server.");
		CreateServerCommand csc = new CreateServerCommand();
		csc.setServerFactoryid(SERVERTYPEID_TC50);
		csc.execute(null);
		
		System.out.println("Done creating Tomcat v5 server.");
	}
	
	public void testReattemptCreateTomcatv5Server(){
		System.out.println("Attempting to create 2nd Tomcat v5 server.");
		CreateServerCommand csc = new CreateServerCommand();
		csc.setServerFactoryid(SERVERTYPEID_TC50);
		csc.execute(null);
		
		System.out.println("Done attempting 2nd Tomcat v5 server creation.");		
	}
}

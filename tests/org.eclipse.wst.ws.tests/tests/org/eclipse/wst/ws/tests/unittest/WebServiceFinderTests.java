package org.eclipse.wst.ws.tests.unittest;

import org.eclipse.wst.ws.internal.wsfinder.WebServiceFinder;
import org.eclipse.wst.ws.tests.data.TestWorkspace;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author joan
 *
 */

public class WebServiceFinderTests extends TestCase implements WSJUnitConstants {

	public static Test suite(){
		return new TestSuite(WebServiceFinderTests.class);
	}
	
	public void testWSFinder(){
		System.out.println("creating web service finder");
		WebServiceFinder wsf = WebServiceFinder.instance();
		
		TestWorkspace wspace = new TestWorkspace();
		try {
			wspace.installData();	
		}
		catch (Exception e){
			System.out.println("exception thrown " + e.toString());
		}
		
		System.out.println("attempting to locate all web services in workspace");
		wsf.getWebServices();	
		
		System.out.println("finished finding all webservices");
		 
	}

}

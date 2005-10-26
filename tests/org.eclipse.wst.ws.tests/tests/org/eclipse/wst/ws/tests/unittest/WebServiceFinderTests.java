package org.eclipse.wst.ws.tests.unittest;

import java.util.Iterator;

import org.eclipse.wst.ws.internal.wsfinder.WebServiceFinder;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

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
		
		System.out.println("attempting to locate all web services in workspace");
		Iterator wsIterator = wsf.getWebServices();
		while (wsIterator.hasNext()) {
			WebServiceInfo wsInfo = (WebServiceInfo) wsIterator.next();
			System.out.println("webService URL: " + wsInfo.getWsdlURL());
		}
		
		System.out.println("finished finding all webservices");
		 
	}

}

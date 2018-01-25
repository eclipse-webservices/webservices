/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.wst.ws.tests.unittest;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.ws.internal.wsfinder.WebServiceFinder;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

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
		Iterator wsIterator = wsf.getWebServices(null);
		while (wsIterator.hasNext()) {
			WebServiceInfo wsInfo = (WebServiceInfo) wsIterator.next();
			System.out.println("webService URL: " + wsInfo.getWsdlURL());
		}
		
		System.out.println("finished finding all webservices");
		 
	}

}

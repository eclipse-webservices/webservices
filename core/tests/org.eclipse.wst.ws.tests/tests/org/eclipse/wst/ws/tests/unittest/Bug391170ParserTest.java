/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20120531   391170 jenyoung@ca.ibm.com - Jennifer Young
 *******************************************************************************/

package org.eclipse.wst.ws.tests.unittest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.wst.ws.tests.plugin.TestsPlugin;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;

public class Bug391170ParserTest extends TestCase {
	
	private final String ACTUAL_TESTFILEPATH = "data/Bug391170/wsparsetest.wsdl";

	public static Test suite(){
		return new TestSuite(Bug391170ParserTest.class);
	}
	/*
	 * Test if WebServicesParser.getWSDLDefinition method
	 * returns a valid Definition object when the URI parameter
	 * passed in contains back slashes. In this case,
	 * the test wsdl file is referencing a binding that is
	 * in another wsdl file. When backslashes are used for the
	 * URI, it cannot find the referenced binding and port.getbinding
	 * returns null. In this test case, we are verifying that
	 * after the fix (replace backslashes with forward slashes
	 * prior to reading the wsdl file in WebServicesParser.getWSDLDefinition), 
	 * port.getBinding() no longer returns null.
	 */
	public void testGetWSDLDefinition(){
		
		String wsdlPath = getInstallURL() + ACTUAL_TESTFILEPATH;
		// On Unix/Linux systems, the backward slash is invalid anyway
		if (File.separator.equals("\\"))
		{
		  wsdlPath=wsdlPath.replace('/', '\\');
		}
		wsdlPath = "file:/" + wsdlPath.substring(1);
		WebServicesParser webServicesParser = new WebServicesParserExt();
        Definition definition = webServicesParser.getWSDLDefinition(wsdlPath);
	    if (definition.getServices()!=null) {
	    	Collection serviceValues = definition.getServices().values();

	    	Iterator serviceIter = serviceValues.iterator();
	    	while(serviceIter.hasNext()){
	        	Service service = (Service) serviceIter.next();
	            	
	            Collection servicePorts = service.getPorts().values();
	            Iterator portIter = servicePorts.iterator();
	            while(portIter.hasNext()){
	            	Port port = (Port)portIter.next();
	                assertNotNull(port.getBinding());
	            }
	        }
	    }		 
	}
	
	private String getInstallURL(){
			URL url;
			try {
				url = FileLocator.resolve(TestsPlugin.getDefault().getBundle().getEntry("/"));
			} catch (IOException e) {
				return null;
			}
			return url.getPath();
	}

}

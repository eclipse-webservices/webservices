/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance;

import java.net.URL;
import java.util.Hashtable;

import javax.wsdl.Definition;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.test.performance.PerformanceTestCase;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

public class ReadStockQuoteWSDLEMFTestCase extends PerformanceTestCase
{
  public static Test suite()
  {
    return new TestSuite(ReadStockQuoteWSDLEMFTestCase.class, "ReadStockQuoteWSDLEMFTestCase");
  }
    
  public void testReadStockQuoteWSDL_EMF() throws Exception
  {
    startMeasuring();
    URL wsdl = PerformancePlugin.getDefault().getBundle().getEntry("data/StockQuote/StockQuote.wsdl");
    
	ResourceSet resourceSet = new ResourceSetImpl();
    WSDLResourceImpl resource = (WSDLResourceImpl)resourceSet.createResource(URI.createURI("*.wsdl"));
	resource.setURI(URI.createURI(wsdl.toString()));
    java.util.Map map = new Hashtable();
    map.put(WSDLResourceImpl.CONTINUE_ON_LOAD_ERROR,Boolean.valueOf(true));
    map.put(WSDLResourceImpl.USE_EXTENSION_FACTORIES,Boolean.valueOf(true));
	resource.load(map); 
	Definition definition = resource.getDefinition();
	definition.getQName();
	
	stopMeasuring();
	commitMeasurements();
	assertPerformance();
  }
}

/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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

import javax.wsdl.Definition;
import javax.wsdl.xml.WSDLReader;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.test.performance.PerformanceTestCase;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;
import org.xml.sax.InputSource;

public class ReadStockQuoteWSDLTestCase extends PerformanceTestCase
{
  public static Test suite()
  {
    return new TestSuite(ReadStockQuoteWSDLTestCase.class, "ReadStockQuoteWSDLTestCase");
  }
  
  public void testReadStockQuoteWSDL() throws Exception
  {
    startMeasuring();
    URL wsdl = PerformancePlugin.getDefault().getBundle().getEntry("data/StockQuote/StockQuote.wsdl");
    WSDLFactoryImpl factory = new WSDLFactoryImpl();
    WSDLReader reader = factory.newWSDLReader();
    Definition definition = reader.readWSDL(wsdl.toString(), new InputSource(wsdl.openStream()));
    definition.getQName();
    stopMeasuring();
	commitMeasurements();
	assertPerformance();
  }
}

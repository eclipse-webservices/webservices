/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.xml;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.wsdl.validation.tests.internal.BaseTestCase;

/**
 * Tests for the WSDL validator's internal XML catalog.
 */
public class XMLCatalogTest extends BaseTestCase
{
  private String CATALOG_DIR = "testresources/samples/XSD/CatalogSchemas/";
  
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
	return new TestSuite(XMLCatalogTest.class);
  }
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
	super.setUp();
	XMLCatalog.reset();
  }



public void testSchemaDir()
  {
	XMLCatalog.addSchemaDir(PLUGIN_ABSOLUTE_PATH + CATALOG_DIR);
	String catalogLocation = PLUGIN_ABSOLUTE_PATH + CATALOG_DIR;
	catalogLocation = catalogLocation.replace('\\','/');
    while(catalogLocation.startsWith("/"))
    {
    	catalogLocation = catalogLocation.substring(1);
    }
    catalogLocation = FILE_PROTOCOL + catalogLocation;
	IXMLCatalog catalog = XMLCatalog.getInstance();
	String resolvedLocation = catalog.resolveEntityLocation("http://www.example.org/schema1", "");
	assertEquals("The resolved location is not equal to the expected location.", catalogLocation + "schema1.xsd", resolvedLocation);
	resolvedLocation = catalog.resolveEntityLocation("http://www.example.org/schema2", "");
	assertEquals("The second resolved location is not equal to the resolved location.", catalogLocation + "schema2.xsd", resolvedLocation);
  
  }
}

/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.resolver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * URIResolver tests.
 */
public class URIResolverTest extends TestCase
{
  private URIResolver uriResolver = null;
 /**
  * Create a tests suite from this test class.
  * 
  * @return A test suite containing this test class.
  */
  public static Test suite()
  {
    return new TestSuite(URIResolverTest.class);
  }
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception 
  {
	uriResolver = new URIResolver();	
  }
  
  public void testNormalizeAbsoluteFile()
  {
  	// System id is absolute and should not be modified.
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "file:/c:/somepath/myfile.txt";
  	assertEquals("systemId is absolute and should not be modified.", systemId, uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  	
  }
  
  public void testNormalizeRelativeFile()
  {
  	// System id is relative 
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("systemId is simple relative and is not modified correctly.", "file:/c:/somepath/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileStartsWithDotDot()
  {
    // System id is relative with ../
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "../myfile.txt";
  	assertEquals("systemId is relative with ../ and is not modified correctly.", "file:/c:/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileStartsWithDotDotTwice()
  {
    // System id is relative with ../../
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "../../myfile.txt";
  	assertEquals("systemId is relative with ../../ and is not modified correctly.", "file:/c:/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileStartsWithDot()
  {
    // System id is relative with ./
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "./myfile.txt";
  	assertEquals("systemId is relative with ./ and is not modified correctly.", "file:/c:/somepath/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileStartsWithDotTwice()
  {
    // System id is relative with ././
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "././myfile.txt";
  	assertEquals("systemId is relative with ././ and is not modified correctly.", "file:/c:/somepath/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileStartsWithSlash()
  {
    // System id is relative beginning with /
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "/myfile.txt";
  	assertEquals("systemId is relative beginning with / and is not modified correctly.", "file:/c:/somepath/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileDotDotInMiddle()
  {
    // System id contains ../ in the middle
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "somepath/../myfile.txt";
  	assertEquals("systemId is relative and contains ../ in the middle and is not modified correctly.", "file:/c:/somepath/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileFotInMiddle()
  {
    // System id contains ./ in the middle
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "somepath/./myfile.txt";
  	assertEquals("systemId is relative and contains ./ in the middle and is not modified correctly.", "file:/c:/somepath/somepath/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormlizeRelativeFileDotDotInMiddleOfBase()
  {
    // Base location contains ../ in the middle
  	String baseLocation = "file:/c:/somepath/../somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation contains ../ in the middle and is not modified correctly.", "file:/c:/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileDotInMiddleOfBase()
  {
    // Base location contains ./ in the middle
  	String baseLocation = "file:/c:/somepath/./somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation contains ./ in the middle and is not modified correctly.", "file:/c:/somepath/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileBaseStartsWithSlashDotDot()
  {
    // Base location starts with /../ 
  	String baseLocation = "file:/../somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation starts with ../ and is not modified correctly.", "file:/../somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileBaseStartsWithSlashDot()
  {
    // Base location starts with /./
  	String baseLocation = "file:/./somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation starts with ./ and is not modified correctly.", "file:/somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileBaseStartsWithDotDot()
  {
    // Base location starts with ../ 
  	String baseLocation = "file:../somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation starts with ../ and is not modified correctly.", "file:../somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileBaseStartsWithDot()
  {
    // Base location starts with ./ 
  	String baseLocation = "file:./somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation starts with ./ and is not modified correctly.", "file:somepath/myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileSomedirDotDot()
  {
    // System id contains somedir../ in the middle
  	String baseLocation = "file:/somepath/file.txt";
  	String systemId = "somedir../myfile.txt";
  	assertEquals("systemId has somedir../ and is not modified correctly.", "file:/somepath/somedir../myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeRelativeFileSomedirDot()
  {
    // System id contains somedir./ in the middle
  	String baseLocation = "file:/somepath/file.txt";
  	String systemId = "somedir./myfile.txt";
  	assertEquals("systemId has somedir./ and is not modified correctly.", "file:/somepath/somedir./myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeNullSystemId()
  {
    // System id is null
  	String baseLocation = "file:/somepath/file.txt";
  	String systemId = null;
  	assertEquals("systemId is null.", null, uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeNullBaseLocation()
  {
    // Base location is null
  	String baseLocation = null;
  	String systemId = "somedir./myfile.txt";
  	assertEquals("systemId is null.", null, uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }
  
  public void testNormalizeSystemIdAbsoluteNullBaseLocation()
  {
    // Base location is null
  	String baseLocation = "null";
  	String systemId = "file:/somedir./myfile.txt";
  	assertEquals("systemId is null.", "file:/somedir./myfile.txt", uriResolver.resolve(baseLocation, null, systemId).getLogicalLocation());
  }

	
}

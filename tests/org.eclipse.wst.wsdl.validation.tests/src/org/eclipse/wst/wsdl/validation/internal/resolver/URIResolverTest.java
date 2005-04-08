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
 * @author lmandel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
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
  	assertEquals("systemId is absolute and should not be modified.", uriResolver.resolve(baseLocation, null, systemId), systemId);
  	
  }
  
  public void testNormalizeRelativeFile()
  {
  	// System id is relative 
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("systemId is simple relative and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileStartsWithDotDot()
  {
    // System id is relative with ../
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "../myfile.txt";
  	assertEquals("systemId is relative with ../ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileStartsWithDotDotTwice()
  {
    // System id is relative with ../../
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "../../myfile.txt";
  	assertEquals("systemId is relative with ../../ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/myfile.txt");
  }
  
  public void testNormalizeRelativeFileStartsWithDot()
  {
    // System id is relative with ./
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "./myfile.txt";
  	assertEquals("systemId is relative with ./ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileStartsWithDotTwice()
  {
    // System id is relative with ././
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "././myfile.txt";
  	assertEquals("systemId is relative with ././ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileStartsWithSlash()
  {
    // System id is relative beginning with /
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "/myfile.txt";
  	assertEquals("systemId is relative beginning with / and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileDotDotInMiddle()
  {
    // System id contains ../ in the middle
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "somepath/../myfile.txt";
  	assertEquals("systemId is relative and contains ../ in the middle and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileFotInMiddle()
  {
    // System id contains ./ in the middle
  	String baseLocation = "file:/c:/somepath/somepath/file.txt";
  	String systemId = "somepath/./myfile.txt";
  	assertEquals("systemId is relative and contains ./ in the middle and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/somepath/somepath/myfile.txt");
  }
  
  public void testNormlizeRelativeFileDotDotInMiddleOfBase()
  {
    // Base location contains ../ in the middle
  	String baseLocation = "file:/c:/somepath/../somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation contains ../ in the middle and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileDotInMiddleOfBase()
  {
    // Base location contains ./ in the middle
  	String baseLocation = "file:/c:/somepath/./somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation contains ./ in the middle and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/c:/somepath/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileBaseStartsWithSlashDotDot()
  {
    // Base location starts with /../ 
  	String baseLocation = "file:/../somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation starts with ../ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/../somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileBaseStartsWithSlashDot()
  {
    // Base location starts with /./
  	String baseLocation = "file:/./somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation starts with ./ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileBaseStartsWithDotDot()
  {
    // Base location starts with ../ 
  	String baseLocation = "file:../somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation starts with ../ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:../somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileBaseStartsWithDot()
  {
    // Base location starts with ./ 
  	String baseLocation = "file:./somepath/file.txt";
  	String systemId = "myfile.txt";
  	assertEquals("baseLocation starts with ./ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:somepath/myfile.txt");
  }
  
  public void testNormalizeRelativeFileSomedirDotDot()
  {
    // System id contains somedir../ in the middle
  	String baseLocation = "file:/somepath/file.txt";
  	String systemId = "somedir../myfile.txt";
  	assertEquals("systemId has somedir../ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/somepath/somedir../myfile.txt");
  }
  
  public void testNormalizeRelativeFileSomedirDot()
  {
    // System id contains somedir./ in the middle
  	String baseLocation = "file:/somepath/file.txt";
  	String systemId = "somedir./myfile.txt";
  	assertEquals("systemId has somedir./ and is not modified correctly.", uriResolver.resolve(baseLocation, null, systemId), "file:/somepath/somedir./myfile.txt");
  }
  
  public void testNormalizeNullSystemId()
  {
    // System id is null
  	String baseLocation = "file:/somepath/file.txt";
  	String systemId = null;
  	assertEquals("systemId is null.", uriResolver.resolve(baseLocation, null, systemId), null);
  }
  
  public void testNormalizeNullBaseLocation()
  {
    // Base location is null
  	String baseLocation = null;
  	String systemId = "somedir./myfile.txt";
  	assertEquals("systemId is null.", uriResolver.resolve(baseLocation, null, systemId), null);
  }
  
  public void testNormalizeSystemIdAbsoluteNullBaseLocation()
  {
    // Base location is null
  	String baseLocation = "null";
  	String systemId = "file:/somedir./myfile.txt";
  	assertEquals("systemId is null.", uriResolver.resolve(baseLocation, null, systemId), "file:/somedir./myfile.txt");
  }

	
}

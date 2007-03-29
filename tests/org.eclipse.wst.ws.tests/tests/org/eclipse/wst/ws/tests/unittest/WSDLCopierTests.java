/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070126   171071 makandre@ca.ibm.com - Andrew Mak, Create public utility method for copying WSDL files
 *******************************************************************************/

package org.eclipse.wst.ws.tests.unittest;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.ws.internal.util.WSDLCopier;
import org.eclipse.wst.ws.tests.plugin.TestsPlugin;

/**
 * JUnit tests for WSDLCopier
 */
public class WSDLCopierTests extends TestCase {
	
	private static final String DATA_DIR     = "data/wsdlcopier/";
	
	private static final String WSDL_FOLDER1 = "wsdl1";
	private static final String WSDL_FOLDER2 = "wsdl2";
	private static final String WSDL_FOLDER3 = "wsdl3";
	
	private IProject project;

	/**
	 * Return suite of tests to run.
	 * 
	 * @return A TestSuite.
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("WSDLCopierTests");
		suite.addTest(new WSDLCopierTests("testCase1"));
		suite.addTest(new WSDLCopierTests("testCase2"));
		suite.addTest(new WSDLCopierTests("testCase3"));
		suite.addTest(new WSDLCopierTests("testCase4"));
		suite.addTest(new WSDLCopierTests("testCase5"));
		return suite;
	}
		
	/**
	 * Constructor.
	 * 
	 * @param name Test method name.
	 */
	public WSDLCopierTests(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
	    project = ResourcesPlugin.getWorkspace().getRoot().getProject("WSDLCopierTests");
	    
	    if (!project.exists())
	    	project.create(null);
	    	
	    if (!project.isOpen())
	    	project.open(null);
	}

	/**
	 * Create a folder.
	 * @param name Name of folder.
	 * @return The created folder.
	 * @throws CoreException
	 */
	protected IFolder createFolder(String name) throws CoreException {
		IFolder folder = project.getFolder(new Path(name));
        folder.create(false, true, null);
	    return folder;
	}
	
	/*
	 * Validate the directory structure of the target folder after copying.
	 */
	private void validate(IFolder folder, String filename) {		
		assertNotNull(folder.findMember("1/2/3/test.wsdl"));
		assertNotNull(folder.findMember("1/2a/" + filename));
		assertNotNull(folder.findMember("1/2a/test.xsd"));
		assertNotNull(folder.findMember("test.xsd"));		
	}
	
	/*
	 * Validate the timestamps of the files have not changed.
	 */
	private long[] validateTimestamps(IFolder folder, long[] oldTimestamps) {
		
		long[] currentTimestamps = new long[3];
		
		currentTimestamps[0] = folder.findMember("1/2/3/test.wsdl").getModificationStamp();
		if (oldTimestamps != null) assertEquals(currentTimestamps[0], oldTimestamps[0]);

		currentTimestamps[1] = folder.findMember("1/2a/test.xsd").getModificationStamp();
		if (oldTimestamps != null) assertEquals(currentTimestamps[1], oldTimestamps[1]);
		
		currentTimestamps[2] = folder.findMember("test.xsd").getModificationStamp();
		if (oldTimestamps != null) assertEquals(currentTimestamps[2], oldTimestamps[2]);
		
		return currentTimestamps;
	}
	
	/**
	 * Test case #1
	 * - copies wsdl from test data directory using file:/ URI 
	 */
	public void testCase1() {
		try {
			URL url = TestsPlugin.getDefault().getBundle().getEntry(DATA_DIR + "1/2a/start.wsdl");				
			url = FileLocator.toFileURL(url);
			
			IFolder folder = createFolder(WSDL_FOLDER1);
			
			System.out.println("Copying from: " + url);
			System.out.println("Copying to:   " + folder.getFullPath());
			
			WSDLCopier copier = new WSDLCopier();
			copier.setSourceURI(url.toString());
			copier.setTargetFolder(folder);
			
			ResourcesPlugin.getWorkspace().run(copier, null);
			
			validate(folder, "start.wsdl");
			assertEquals(copier.getRelativePath().toString(), "1/2a/start.wsdl");
			
			System.out.println("Success!");
		}
		catch (Exception e) {
			System.out.println(e);
			assertTrue(e.getMessage(), false);
		}
	}

	/**
	 * Test case #2
	 * - copies wsdl from one folder to another using platform:/resource URI
	 * - also changes the target filename
	 */
	public void testCase2() {
		try {
			IResource resource = project.getFolder(WSDL_FOLDER1).findMember("1/2a/start.wsdl");
			
			String path = "platform:/resource" + resource.getFullPath(); 						
			
			IFolder folder = createFolder(WSDL_FOLDER2);
			
			System.out.println("Copying from: " + path);
			System.out.println("Copying to:   " + folder.getFullPath());
			
			WSDLCopier copier = new WSDLCopier();
			copier.setSourceURI(path);			
			copier.setTargetFolder(folder);
			copier.setTargetFilename("newname.wsdl");
			
			ResourcesPlugin.getWorkspace().run(copier, null);
			
			validate(folder, "newname.wsdl");
			assertEquals(copier.getRelativePath().toString(), "1/2a/newname.wsdl");
			
			System.out.println("Success!");
		}
		catch (Exception e) {	
			System.out.println(e);
			assertTrue(e.getMessage(), false);
		}
	}

	/**
	 * Test case #3
	 * - runs the WSDLCopier without specifying a source URI (operation should fail)
	 */
	public void testCase3() {
		try {			
			IFolder folder = createFolder(WSDL_FOLDER3);
			
			System.out.println("Copying from: N/A");
			System.out.println("Copying to:   " + folder.getFullPath());
			
			WSDLCopier copier = new WSDLCopier();
			copier.setTargetFolder(folder);			
			
			try {
				ResourcesPlugin.getWorkspace().run(copier, null);
				assertTrue("Should not get here", false);
			}
			catch (CoreException ce) {
				System.out.println(ce);
			}
			
			System.out.println("Success!");
		}
		catch (Exception e) {
			System.out.println(e);
			assertTrue(e.getMessage(), false);
		}
	}	

	/**
	 * Test case #4
	 * - runs the WSDLCopier without specifying a target folder (operation should fail)
	 */
	public void testCase4() {
		try {
			IResource resource = project.getFolder(WSDL_FOLDER1).findMember("1/2a/start.wsdl");
			
			String path = "platform:/resource" + resource.getFullPath(); 															
			
			System.out.println("Copying from: " + path);
			System.out.println("Copying to:   N/A");
			
			WSDLCopier copier = new WSDLCopier();
			copier.setSourceURI(path);			
			
			try {
				ResourcesPlugin.getWorkspace().run(copier, null);
				assertTrue("Should not get here", false);
			}
			catch (CoreException ce) {
				System.out.println(ce);
			}
			
			System.out.println("Success!");
		}
		catch (Exception e) {
			System.out.println(e);
			assertTrue(e.getMessage(), false);			
		}
	}
	
	/**
	 * Test case #5
	 * - copies wsdl from one folder onto itself using platform:/resource URI
	 */
	public void testCase5() {
		try {
			IFolder folder = project.getFolder(WSDL_FOLDER1);
			IResource resource = folder.findMember("1/2a/start.wsdl");
			
			String path = "platform:/resource" + resource.getFullPath(); 						
									
			System.out.println("Copying from: " + path);
			System.out.println("Copying to:   " + folder.getFullPath());
			
			WSDLCopier copier = new WSDLCopier();
			copier.setSourceURI(path);			
			copier.setTargetFolder(folder);			

			long[] oldTimestamps = validateTimestamps(folder, null);
			
			ResourcesPlugin.getWorkspace().run(copier, null);
			
			validate(folder, "start.wsdl");
			validateTimestamps(folder, oldTimestamps);
			assertEquals(copier.getRelativePath().toString(), "1/2a/start.wsdl");
			
			System.out.println("Success!");
		}
		catch (Exception e) {	
			System.out.println(e);
			assertTrue(e.getMessage(), false);
		}
	}
}





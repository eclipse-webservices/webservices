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
package org.eclipse.wst.wsdl.validation.internal.ui.ant;

import java.io.File;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.eclipse.wst.wsdl.validation.tests.internal.BaseTestCase;

/**
 * WSDLValidate ant task tests.
 */
public class WSDLValidateTest extends BaseTestCase
{
  private String BASE_DIR;;
  private Project project;

 /**
  * Create a tests suite from this test class.
  * 
  * @return A test suite containing this test class.
  */
  public static Test suite()
  {
    return new TestSuite(WSDLValidateTest.class);
  }
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp()
  {
    super.setUp();
    try
    {
      BASE_DIR = PLUGIN_ABSOLUTE_PATH;
      BASE_DIR = BASE_DIR.replace('/', File.separatorChar);
      if(BASE_DIR.startsWith("\\"))
      {
        BASE_DIR = BASE_DIR.substring(1);  
      }
    }
    catch(Exception e)
    {
      
    }
    project = new Project();
    project.setBaseDir(new File(BASE_DIR));
  }
  
  /**
   * Test that a single file with a relative location is resolved properly.
   */
  public void testRelativeSingleFileLocation()
  {
    String fileLoc = "file.wsdl";
    String resolvedLocation = ("file:///" + BASE_DIR + fileLoc).replace('\\','/');
    WSDLValidateTask wsdlValidateTask = new WSDLValidateTask();
    wsdlValidateTask.setProject(project);
    wsdlValidateTask.setFile(fileLoc);
    List fileList = wsdlValidateTask.getFileList();
    assertEquals("The file list does not contain only one file.", 1, fileList.size());
    // The file locations are converted to lower case to avoid drive letter case differences on windows. ie. C vs c.
    assertEquals("The file location '" + fileList.get(0) + "' is not correctly resolved to the base location.", resolvedLocation.toLowerCase(), ((String)fileList.get(0)).toLowerCase());
    
  }
  
  /**
   * Test that a single file with an absolute location is resolved properly.
   */
  public void testAbsoluteSingleFileLocation()
  {
    String fileLoc = BASE_DIR + "file.wsdl";
    String resolvedLocation = (FILE_PROTOCOL + fileLoc).replace('\\','/');
    WSDLValidateTask wsdlValidateTask = new WSDLValidateTask();
    wsdlValidateTask.setProject(project);
    wsdlValidateTask.setFile(fileLoc);
    List fileList = wsdlValidateTask.getFileList();
    assertEquals("The file list does not contain only one file.", 1, fileList.size());
    assertEquals("The file location '" + fileLoc + "' is modified.", resolvedLocation.toLowerCase(), ((String)fileList.get(0)).toLowerCase());
    
  }
  
  /**
   * Test that a single file with an absolute, remote location is resolved properly.
   */
  public void testAbsoluteSingleFileRemoteLocation()
  {
    String resolvedLocation = "http://www.ws-i.org/SampleApplications/SupplyChainManagement/2003-07/Catalog.wsdl";
    String fileLoc = "http://www.ws-i.org/SampleApplications/SupplyChainManagement/2003-07/Catalog.wsdl";
    WSDLValidateTask wsdlValidateTask = new WSDLValidateTask();
    wsdlValidateTask.setProject(project);
    wsdlValidateTask.setFile(fileLoc);
    List fileList = wsdlValidateTask.getFileList();
    assertEquals("The file list does not contain only one file.", 1, fileList.size());
    assertEquals("The file location '" + fileLoc + "' is modified.", resolvedLocation.toLowerCase(), ((String)fileList.get(0)).toLowerCase());
    
  }
  
/**
   * Test that a single file in a fileset with a relative location is resolved properly
   * when no directory is specified.
   */
  public void testRelativeSingleFileInFilesetNoDirSpecified()
  {
    String fileLoc = "Empty.wsdl";
    String base_dir = BASE_DIR + SAMPLES_DIR + "WSDL" + File.separator + "SelfContained";
    String resolvedLocation = ("file:///" + base_dir + "/" + fileLoc).replace('\\','/');
    base_dir = base_dir.replace('/', File.separatorChar);
    
    WSDLValidateTask wsdlValidateTask = new WSDLValidateTask();
    wsdlValidateTask.setProject(project);
    
    FileSet fileset = wsdlValidateTask.createFileset();
    fileset.setProject(project);
    fileset.setDir(new File(base_dir));
   
    FilenameSelector filenameSelector = new FilenameSelector();
    filenameSelector.setName(fileLoc);
    fileset.addFilename(filenameSelector);
    //fileset.setFile(new File(fileLoc));
    
    List fileList = wsdlValidateTask.getFileList();
    assertEquals("The file list does not contain only one file.", 1, fileList.size());
    // The file locations are converted to lower case to avoid drive letter case differences on windows. ie. C vs c.
    assertEquals("The file location '" + fileList.get(0) + "' is not correctly resolved to the base location.", resolvedLocation.toLowerCase(), ((String)fileList.get(0)).toLowerCase());
    
  }
}

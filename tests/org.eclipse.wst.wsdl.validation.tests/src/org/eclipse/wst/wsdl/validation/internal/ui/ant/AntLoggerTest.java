/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.ui.ant;

import junit.framework.TestCase;

import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;

/**
 * Tests for the AntLogger.
 */
public class AntLoggerTest extends TestCase 
{
  AntLoggerTestTask task = null;
	
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception 
  {
	task = new AntLoggerTestTask();
  }

  /* (non-Javadoc)
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception 
  {
	task = null;
  }

  /**
   * Test that the log method:
   * 1. logs errors correctly.
   * 2. records the message correctly.
   * 3. records the throwable correctly.
   */
  public void testLogErrorWithThrowable()
  {
	AntLogger logger = new AntLogger(task);
	logger.log("MESSAGE", ILogger.SEV_ERROR, new Throwable("THROWABLE"));
	
	assertTrue("Warnings were reported when only errors should have been reported.", task.getWarnings().isEmpty());
	assertTrue("Infos were reported when only errors should have been reported.", task.getInfos().isEmpty());
	assertTrue("Verboses were reported when only errors should have been reported.", task.getVerboses().isEmpty());
	assertEquals("2 errors were not reported.", 2, task.getErrors().size());
	assertEquals("The message was not MESSAGE.", "MESSAGE", task.getErrors().get(0));
	assertEquals("The throwable was not THROWABLE.", "java.lang.Throwable: THROWABLE", task.getErrors().get(1));
  }
  
  /**
   * Test that the log method:
   * 1. logs warnings correctly.
   * 2. records the message correctly.
   * 3. records the throwable correctly.
   */
  public void testLogWarningWithThrowable()
  {
	AntLogger logger = new AntLogger(task);
	logger.log("MESSAGE", ILogger.SEV_WARNING, new Throwable("THROWABLE"));
	
	assertTrue("Errors were reported when only warnings should have been reported.", task.getErrors().isEmpty());
	assertTrue("Infos were reported when only warnings should have been reported.", task.getInfos().isEmpty());
	assertTrue("Verboses were reported when only errors should have been reported.", task.getVerboses().isEmpty());
	assertEquals("2 warnings were not reported.", 2, task.getWarnings().size());
	assertEquals("The message was not MESSAGE.", "MESSAGE", task.getWarnings().get(0));
	assertEquals("The throwable was not THROWABLE.", "java.lang.Throwable: THROWABLE", task.getWarnings().get(1));
  }
  
  /**
   * Test that the log method:
   * 1. logs infos correctly.
   * 2. records the message correctly.
   * 3. records the throwable correctly.
   */
  public void testLogInfoWithThrowable()
  {
	AntLogger logger = new AntLogger(task);
	logger.log("MESSAGE", ILogger.SEV_INFO, new Throwable("THROWABLE"));
	
	assertTrue("Warnings were reported when only infos should have been reported.", task.getWarnings().isEmpty());
	assertTrue("Errors were reported when only infos should have been reported.", task.getErrors().isEmpty());
	assertTrue("Verboses were reported when only errors should have been reported.", task.getVerboses().isEmpty());
	assertEquals("2 infos were not reported.", 2, task.getInfos().size());
	assertEquals("The message was not MESSAGE.", "MESSAGE", task.getInfos().get(0));
	assertEquals("The throwable was not THROWABLE.", "java.lang.Throwable: THROWABLE", task.getInfos().get(1));
  }
  
  /**
   * Test that the log method:
   * 1. logs verboses correctly.
   * 2. records the message correctly.
   * 3. records the throwable correctly.
   */
  public void testLogVerboseWithThrowable()
  {
	AntLogger logger = new AntLogger(task);
	logger.log("MESSAGE", ILogger.SEV_VERBOSE, new Throwable("THROWABLE"));
	
	assertTrue("Warnings were reported when only verboses should have been reported.", task.getWarnings().isEmpty());
	assertTrue("Errors were reported when only verboses should have been reported.", task.getErrors().isEmpty());
	assertTrue("Infos were reported when only verboses should have been reported.", task.getInfos().isEmpty());
	assertEquals("2 verboses were not reported.", 2, task.getVerboses().size());
	assertEquals("The message was not MESSAGE.", "MESSAGE", task.getVerboses().get(0));
	assertEquals("The throwable was not THROWABLE.", "java.lang.Throwable: THROWABLE", task.getVerboses().get(1));
  }
  
  /**
   * Test that the log method:
   * 1. logs errors correctly.
   * 2. records the message correctly.
   * 3. records the throwable correctly.
   */
  public void testLogErrorWithoutThrowable()
  {
	AntLogger logger = new AntLogger(task);
	logger.log("MESSAGE", ILogger.SEV_ERROR);
	
	assertTrue("Warnings were reported when only errors should have been reported.", task.getWarnings().isEmpty());
	assertTrue("Infos were reported when only errors should have been reported.", task.getInfos().isEmpty());
	assertTrue("Verboses were reported when only errors should have been reported.", task.getVerboses().isEmpty());
	assertEquals("1 error was not reported.", 1, task.getErrors().size());
	assertEquals("The message was not MESSAGE.", "MESSAGE", task.getErrors().get(0));
  }
  
  /**
   * Test that the log method:
   * 1. logs warnings correctly.
   * 2. records the message correctly.
   * 3. records the throwable correctly.
   */
  public void testLogWarningWithoutThrowable()
  {
	AntLogger logger = new AntLogger(task);
	logger.log("MESSAGE", ILogger.SEV_WARNING);
	
	assertTrue("Errors were reported when only warnings should have been reported.", task.getErrors().isEmpty());
	assertTrue("Infos were reported when only warnings should have been reported.", task.getInfos().isEmpty());
	assertTrue("Verboses were reported when only errors should have been reported.", task.getVerboses().isEmpty());
	assertEquals("1 error was not reported.", 1, task.getWarnings().size());
	assertEquals("The message was not MESSAGE.", "MESSAGE", task.getWarnings().get(0));
  }
  
  /**
   * Test that the log method:
   * 1. logs infos correctly.
   * 2. records the message correctly.
   * 3. records the throwable correctly.
   */
  public void testLogInfoWithoutThrowable()
  {
	AntLogger logger = new AntLogger(task);
	logger.log("MESSAGE", ILogger.SEV_INFO);
	
	assertTrue("Warnings were reported when only infos should have been reported.", task.getWarnings().isEmpty());
	assertTrue("Errors were reported when only infos should have been reported.", task.getErrors().isEmpty());
	assertTrue("Verboses were reported when only errors should have been reported.", task.getVerboses().isEmpty());
	assertEquals("1 info was not reported.", 1, task.getInfos().size());
	assertEquals("The message was not MESSAGE.", "MESSAGE", task.getInfos().get(0));
  }
  
  /**
   * Test that the log method:
   * 1. logs verboses correctly.
   * 2. records the message correctly.
   * 3. records the throwable correctly.
   */
  public void testLogVerboseWithoutThrowable()
  {
	AntLogger logger = new AntLogger(task);
	logger.log("MESSAGE", ILogger.SEV_VERBOSE);
	
	assertTrue("Warnings were reported when only verboses should have been reported.", task.getWarnings().isEmpty());
	assertTrue("Errors were reported when only verboses should have been reported.", task.getErrors().isEmpty());
	assertTrue("Infos were reported when only verboses should have been reported.", task.getInfos().isEmpty());
	assertEquals("1 verbose was not reported.", 1, task.getVerboses().size());
	assertEquals("The message was not MESSAGE.", "MESSAGE", task.getVerboses().get(0));
  }
}

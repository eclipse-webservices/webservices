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
package org.eclipse.wst.wsdl.validation.tests.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator;

/**
 * Base test case class which provides methods to 
 * - create logs
 * - read from logs
 * - run log comparison tests
 */
public class BaseTestCase extends TestCase
{
  protected String FILE_PROTOCOL = "file:///"; 
  protected String PLUGIN_ABSOLUTE_PATH;
  protected String SAMPLES_DIR = "testresources/samples/";
  protected String GENERATED_RESULTS_DIR = "testresources/generatedResults/";
  protected String IDEAL_RESULTS_DIR = "testresources/idealResults/";
  protected String LOG_FILE_LOCATION = "results.log";
  
  protected static final String PLUGIN_NAME = "org.eclipse.wst.wsdl.validation.tests";
  private WSDLValidator validator = WSDLValidator.getInstance();
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp()
  {
    PLUGIN_ABSOLUTE_PATH = WSDLValidatorTestsPlugin.getInstallURL();//getPluginLocation();
    if(PLUGIN_ABSOLUTE_PATH.startsWith("/"))
    {
      PLUGIN_ABSOLUTE_PATH = PLUGIN_ABSOLUTE_PATH.substring(1);
    }
    
    // Set the WS-I preference to ignore so only WSDL errors will be tested.
    WSPlugin wsui = WSPlugin.getInstance();
    PersistentWSIContext wsicontext = wsui.getWSISSBPContext();
    wsicontext.updateWSICompliances(PersistentWSIContext.IGNORE_NON_WSI);
    wsicontext = wsui.getWSIAPContext();
    wsicontext.updateWSICompliances(PersistentWSIContext.IGNORE_NON_WSI);
  }
  
  /**
   * Run a validator test. The test will run the validator, log the results and compare the results
   * with the ideal results. The test will only pass if the two log files are the same.
   * 
   * @param testfile The file to run the validator test on.
   * @param loglocation The location to create the log file.
   * @param idealloglocation The location of the ideal log file.
   */
  public void runTest(String testfile, String loglocation, String idealloglocation)
  {
    IValidationReport valreport = validator.validate(testfile);
    try
    {
      createLog(loglocation, valreport);
      String generatedLog = getStringFromFile(loglocation);
      String idealLog = getStringFromFile(idealloglocation);
      assertEquals(idealLog, generatedLog);
    } catch (Exception e)
    {
      fail("Could not compare log files");
    }
  }
  
  /**
   * Get a string representation of a file.
   * 
   * @param filename the file name of the file to get the string representation.
   * @return The string representation if successful.
   * @throws Exception Thrown if unable to create a string representaion of the file.
   */
  private String getStringFromFile(String filename) throws Exception
  {
    if(filename.startsWith("file:"))
  	{
  	  filename = filename.substring(6);
  	  while(filename.startsWith("\\") || filename.startsWith("/"))
  	  {
  	  	filename = filename.substring(1);
  	  }
  	}
    StringBuffer filestring = new StringBuffer();
    Reader reader = null;
    BufferedReader bufreader = null;
    try
    {
      File file = new File(filename);
      reader = new FileReader(file);
      bufreader = new BufferedReader(reader);
      while (bufreader.ready())
      {
        filestring.append(bufreader.readLine() + "\n");
      }
    } catch (FileNotFoundException e)
    {
      throw new Exception();
    } finally
    {
      bufreader.close();
      reader.close();
    }
    return filestring.toString();
  }
  
  /**
   * Create a log file for an XSD test.
   * 
   * @param filename The name of the log file to create.
   * @param valreport The validation report for this file.
   * @return The file contents as a string if successful or null if not successful.
   */
  private String createLog(String filename, IValidationReport valreport)
  {
    if(filename.startsWith("file:"))
  	{
  	  filename = filename.substring(6);
  	  while(filename.startsWith("\\") || filename.startsWith("/"))
  	  {
  	  	filename = filename.substring(1);
  	  }
  	}
     IValidationMessage[] valmessages = valreport.getValidationMessages();
     int nummessages = valmessages.length;//validator.getErrors().size() + validator.getWarnings().size();
     StringBuffer errorsString = new StringBuffer();
     StringBuffer warningsString = new StringBuffer();
     int numerrors = 0;
     int numwarnings = 0;
     for(int i = 0; i < nummessages; i++)
     {
       IValidationMessage valmes = valmessages[i];
       if(valmes.getSeverity() == IValidationMessage.SEV_WARNING)
       {
         numwarnings++;
         // If the message contains any file references make them relative.
         String message = valmes.getMessage();
         message = message.replaceAll("'[^']*" + PLUGIN_NAME + "[^'/]*/", "'");
         message = message.replaceAll("[(][^(]*" + PLUGIN_NAME + "[^'/]*/", "[(]");
         warningsString.append(message + " [" + valmes.getLine() +", " + valmes.getColumn() +"]\n");
         warningsString.append(createNestedMessageString(valmes.getNestedMessages()));
       }
       else
       {
         numerrors++;
         // If the message contains any file references make them relative.
         String message = valmes.getMessage();
         message = message.replaceAll("'[^']*" + PLUGIN_NAME + "[^'/]*/", "'");
         message = message.replaceAll("[(][^(]*" + PLUGIN_NAME + "[^'/]*/", "(");
         errorsString.append(message + " [" + valmes.getLine() +", " + valmes.getColumn() +"]\n");
         errorsString.append(createNestedMessageString(valmes.getNestedMessages()));
       }
     }
     StringBuffer log = new StringBuffer();
     log.append("number of errors      : " + numerrors + "\n");
     log.append("number of warnings    : " + numwarnings + "\n\n");
     log.append("------------error list-------------------------------------------\n");
     if(numerrors == 0)
     {
       log.append("(none)\n");
     }
     else
     {
       log.append(errorsString.toString());
     }
     log.append("------------warning list-----------------------------------------\n");
     if(numwarnings == 0)
     {
       log.append("(none)\n");
     }
     else
     {
       log.append(warningsString.toString());
     }
     log.append("-----------------------------------------------------------------\n");
   
     DataOutputStream outStream = null;
    try
    {
      File logfile = new File(filename);
      File parent = logfile.getParentFile();
  	if (!parent.exists()) 
  	{
  	  parent.mkdirs();
  	}
      if(logfile.exists())
      {
        logfile.delete();
      }
      logfile.createNewFile();
      
      outStream = new DataOutputStream(new FileOutputStream(filename, true));
      outStream.writeBytes(log.toString());
      outStream.close();

    } catch (IOException e)
    {
      // If we can't write the log then clear the log.
      log.delete(0, log.length());
    }
    return log.toString();
  }
  
  private String createNestedMessageString(List nestedMessages)
  {
    return createNestedMessageString(nestedMessages, 0);
  }
  
  private String createNestedMessageString(List nestedMessages, int depth)
  {
    if(nestedMessages != null && nestedMessages.size() > 0)
    {
      String messageString = "";
      Iterator nestedMesIter = nestedMessages.iterator();
      while(nestedMesIter.hasNext())
      {
        IValidationMessage nesvalmes = (IValidationMessage)nestedMesIter.next();
        for(int i = 0; i < depth; i++)
        {
          messageString += " ";
        }
        // If the message contains any file references make them relative.
        String message = nesvalmes.getMessage();
        message = message.replaceAll("'[^']*" + PLUGIN_NAME + "[^'/]*/", "'");
        message = message.replaceAll("[(][^(]*" + PLUGIN_NAME + "[^'/]*/", "[(]");
        messageString += ("-> " + message + " [" + nesvalmes.getLine() +", " + nesvalmes.getColumn() +"]\n");
        messageString += createNestedMessageString(nesvalmes.getNestedMessages(), depth + 1);
      }
      return messageString;
    }
    else
    {
      return "";
    }
  }
}
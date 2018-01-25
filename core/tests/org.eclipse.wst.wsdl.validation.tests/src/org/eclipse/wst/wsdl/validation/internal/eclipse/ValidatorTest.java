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
package org.eclipse.wst.wsdl.validation.internal.eclipse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.ValidationInfoImpl;
import org.eclipse.wst.wsdl.validation.internal.ValidationMessageImpl;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.tests.internal.WSDLValidatorTestsPlugin;
import org.eclipse.wst.xml.core.internal.validation.core.NestedValidatorContext;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationInfo;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationReport;

/**
 * Test the WSDL validation framework Validator.
 */
public class ValidatorTest extends TestCase 
{
  ValidatorWrapper validator = new ValidatorWrapper();
  
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
    return new TestSuite(ValidatorTest.class);
  }
  
  /**
   * Test the validate method. Tests to be performed:<br/>
   * 1. Test that validating a valid file from a URI or an input stream produces the same result.<br/>
   * 2. Test that validating an invalid file from a URI or an input stream produces the same result.
   */
  public void testValidate()
  {
	try
	{
	  // Test that validating a valid file from a URI and an input stream produces the same result.
	  NestedValidatorContext context = new NestedValidatorContext();
	  validator.setupValidation(context);
	  String PLUGIN_ABSOLUTE_PATH = WSDLValidatorTestsPlugin.getInstallURL();
	  String uri = "file:///" + PLUGIN_ABSOLUTE_PATH + "testresources/samples/Paths/Dash-InPath/DashInPathValid.wsdl";
	  ValidationReport report1 = validator.validate(uri, null, context);
	  validator.teardownValidation(context);
	  validator.setupValidation(context);
	  ValidationReport report2 = null;
	  InputStream is = null;
	  try
	  {
	    is = new URL(uri).openStream();
	    report2 = validator.validate(uri, is, context);
	  }
	  catch(Exception e)
	  {
		fail("A problem occurred while validating a valid file with an inputstream: " + e);
	  }
	  finally
	  {
		if(is != null)
		{
		  try
		  {
		    is.close();
		  }
		  catch(IOException e)
		  {
			// Do nothing.
		  }
		}
	  }
	  validator.teardownValidation(context);
	  assertTrue("Validation using a URI did not product a valid validation result.", report1.isValid());
	  assertEquals("Validation using URI and using inputstream of the same file produces different numbers of errors.", report1.getValidationMessages().length, report2.getValidationMessages().length);
	  
      // Test that validating an invalid file from a URI and an input stream produces the same result.
	  NestedValidatorContext context2 = new NestedValidatorContext();
	  validator.setupValidation(context2);
	  uri = "file:///" + PLUGIN_ABSOLUTE_PATH + "testresources/samples/Paths/Dash-InPath/DashInPathInvalid.wsdl";
	  report1 = validator.validate(uri, null, context2);
	  validator.teardownValidation(context2);
	  validator.setupValidation(context2);
	  report2 = null;
	  is = null;
	  try
	  {
	    is = new URL(uri).openStream();
	    report2 = validator.validate(uri, is, context2);
	  }
	  catch(Exception e)
	  {
		fail("A problem occurred while validating an invalid file with an inputstream: " + e);
	  }
	  finally
	  {
		if(is != null)
		{
		  try
		  {
		    is.close();
		  }
		  catch(IOException e)
		  {
			// Do nothing.
		  }
		}
	  }
	  validator.teardownValidation(context2);
	  assertFalse("Validation using a URI did not product an invalid validation result.", report1.isValid());
	  assertEquals("Validation using URI and using inputstream of the same file produces different numbers of errors.", report1.getValidationMessages().length, report2.getValidationMessages().length);
	}
	catch(Exception e)
	{
	  fail("Unable to locate plug-in location: " + e);
	}
  }
  
  /**
   * Test the setupValidation method.
   * 1. Test that after run for a context XML and XSD grammar pools exist for the context.
   */
  public void testSetupValidation()
  {
	validator.getXMLGrammarPoolsMap().clear();
	validator.getXSDGrammarPoolsMap().clear();
	
	NestedValidatorContext context = new NestedValidatorContext();
	assertNull("An XML grammar pool already exists for the context.", validator.getXMLGrammarPoolForContext(context));
	assertNull("An XSD grammar pool already exists for the context.", validator.getXSDGrammarPoolForContext(context));
	
	validator.setupValidation(context);
	
	assertNotNull("An XML grammar pool does not exist after the setupValidation method is run.", validator.getXMLGrammarPoolForContext(context));
	assertNotNull("An XSD grammar pool does not exist after the setupValidation method is run.", validator.getXSDGrammarPoolForContext(context));
	
	validator.getXMLGrammarPoolsMap().clear();
	validator.getXSDGrammarPoolsMap().clear();
  }
  
  /**
   * Test the teardownValidation method.
   * 1. Test that after run for a context XML and XSD grammar pools do not exist for the context.
   */
  public void testTeardownValidation()
  {
	validator.getXMLGrammarPoolsMap().clear();
	validator.getXSDGrammarPoolsMap().clear();
	
	NestedValidatorContext context = new NestedValidatorContext();
	validator.getXMLGrammarPoolsMap().put(context, new XMLGrammarPoolImpl());
	validator.getXSDGrammarPoolsMap().put(context, new InlineSchemaModelGrammarPoolImpl());
	
	validator.teardownValidation(context);
	
	assertNull("An XML grammar pool exists after the teardownValidation method is run.", validator.getXMLGrammarPoolForContext(context));
	assertNull("An XSD grammar pool exists after the teardownValidation method is run.", validator.getXSDGrammarPoolForContext(context));
	
	validator.getXMLGrammarPoolsMap().clear();
	validator.getXSDGrammarPoolsMap().clear();
  }
  
  /**
   * Test converting a message. The contents of the message should be included in the 
   * new report.
   * 1. Test that a regular message is added as is.
   * 2. Test that a nested message is added as a nested message.
   */
  public void testConvertMessage()
  {
	// 1. Test that a regular message is added as is.
	ValidationMessageImpl message = new ValidationMessageImpl("message", 1, 2, IValidationMessage.SEV_ERROR, "file:/someuri");
	ValidationInfo report = new ValidationInfo("file:/someuri");
	validator.convertMessage(message, report);
	assertEquals("The report does not have 1 message.", 1, report.getValidationMessages().length);
	assertEquals("The message string is incorrect.", "message", report.getValidationMessages()[0].getMessage());
	assertEquals("The message line number is incorrect.", 1, report.getValidationMessages()[0].getLineNumber());
	assertEquals("The message column number is incorrect.", 2, report.getValidationMessages()[0].getColumnNumber());
	assertEquals("The message severity is incorrect.", ValidationMessage.SEV_NORMAL, report.getValidationMessages()[0].getSeverity());
	assertEquals("The message URI is incorrect.", "file:/someuri", report.getValidationMessages()[0].getUri());
	assertEquals("The message has an incorrectly registred nested message.", 0, report.getNestedMessages().size());
	
	// 2. Test that a nested message is added as a nested message.
	ValidationMessageImpl message2 = new ValidationMessageImpl("message", 1, 2, IValidationMessage.SEV_ERROR, "file:/someuri2");
	ValidationInfo report2 = new ValidationInfo("file:/someuri");
	validator.convertMessage(message2, report2);
	assertEquals("The report with the nested message does not have 1 message.", 1, report2.getValidationMessages().length);
	assertEquals("The container message URI is incorrect.", "file:/someuri2", report2.getValidationMessages()[0].getUri());
	assertEquals("The container message does not have 1 nested message.", 1, report2.getValidationMessages()[0].getNestedMessages().size());
	assertEquals("The nested message URI is incorrect.", "file:/someuri2", ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getUri());
	assertEquals("The nested message string is incorrect.", "message", ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getMessage());
	assertEquals("The nested message line number is incorrect.", 1, ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getLineNumber());
	assertEquals("The nested message column number is incorrect.", 2, ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getColumnNumber());
	assertEquals("The nested message severity is incorrect.", ValidationMessage.SEV_NORMAL, ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getSeverity());
	assertEquals("The message does not have a nested message.", 1, report2.getNestedMessages().size());
  }
  
  /**
   * Test converting a WSDL validation report to a XML validation report.
   * The contents of the messages should be included in the new report.
   * 1. Test that a regular message is added as is.
   * 2. Test that a nested message is added as a nested message.
   */
  public void testConvertReportToXMLReport()
  {
	// 1. Test that a regular message is added as is.
	ValidationInfoImpl wsdlreport = new ValidationInfoImpl("file:/someuri", new MessageGenerator(ResourceBundle.getBundle("org.eclipse.wst.wsdl.validation.internal.eclipse.validatewsdl")));
	wsdlreport.addError("message", 1, 2, "file:/someuri");
	ValidationReport report = validator.convertReportToXMLReport(wsdlreport);
	assertEquals("The report does not have 1 message.", 1, report.getValidationMessages().length);
	assertEquals("The message string is incorrect.", "message", report.getValidationMessages()[0].getMessage());
	assertEquals("The message line number is incorrect.", 1, report.getValidationMessages()[0].getLineNumber());
	assertEquals("The message column number is incorrect.", 2, report.getValidationMessages()[0].getColumnNumber());
	assertEquals("The message severity is incorrect.", ValidationMessage.SEV_NORMAL, report.getValidationMessages()[0].getSeverity());
	assertEquals("The message URI is incorrect.", "file:/someuri", report.getValidationMessages()[0].getUri());
	assertEquals("The message has an incorrectly registred nested message.", 0, report.getNestedMessages().size());
	
	// 2. Test that a nested message is added as a nested message.
	ValidationInfoImpl wsdlreport2 = new ValidationInfoImpl("file:/someuri", new MessageGenerator(ResourceBundle.getBundle("org.eclipse.wst.wsdl.validation.internal.eclipse.validatewsdl")));
	wsdlreport2.addError("message", 1, 2, "file:/someuri2");
	ValidationReport report2 = validator.convertReportToXMLReport(wsdlreport2);
	assertEquals("The report does not have 1 message.", 1, report2.getValidationMessages().length);
	assertEquals("The message URI is incorrect.", "file:/someuri2", report2.getValidationMessages()[0].getUri());
	assertEquals("The message does not have 1 nested message.", 1, report2.getValidationMessages()[0].getNestedMessages().size());
	assertEquals("The message URI is incorrect.", "file:/someuri2", ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getUri());
	assertEquals("The message string is incorrect.", "message", ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getMessage());
	assertEquals("The message line number is incorrect.", 1, ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getLineNumber());
	assertEquals("The message column number is incorrect.", 2, ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getColumnNumber());
	assertEquals("The message severity is incorrect.", ValidationMessage.SEV_NORMAL, ((ValidationMessage)report2.getValidationMessages()[0].getNestedMessages().get(0)).getSeverity());
	assertEquals("The message does not have a nested message.", 1, report2.getNestedMessages().size());
  }

}

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
package org.eclipse.wst.wsdl.validation.internal.ui.text;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.ValidationMessageImpl;
import org.eclipse.wst.wsdl.validation.internal.ValidatorRegistry;
import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;
import org.eclipse.wst.wsdl.validation.internal.logging.LoggerFactory;
import org.eclipse.wst.wsdl.validation.internal.logging.StandardLogger;
import org.eclipse.wst.wsdl.validation.tests.internal.BaseTestCase;

public class WSDLValidateTest extends BaseTestCase
{	
	WSDLValidateWrapper validate = null;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception 
	{
		super.setUp();
		validate = new WSDLValidateWrapper();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception 
	{
		validate = null;
		super.tearDown();
	}
	
	/**
	 * Test the validateFile method.
	 * 1. A valid file should report back that it's valid.
	 * 2. An invalid file should report back that it's invalid.
	 * 3. A file that can't be found should succeed and report that it's invalid.
	 */
	public void testValidateFile()
	{
		String validFile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + "Paths/AngleHat^InPath/AngleHatInPathValid.wsdl";
		if(PLUGIN_ABSOLUTE_PATH.startsWith("/"))
		{
			validFile = "file://" + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + "Paths/AngleHat^InPath/AngleHatInPathValid.wsdl";
		}
		String invalidFile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + "Paths/AngleHat^InPath/AngleHatInPathInvalid.wsdl";
		if(PLUGIN_ABSOLUTE_PATH.startsWith("/"))
		{
			invalidFile = "file://" + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + "Paths/AngleHat^InPath/AngleHatInPathInvalid.wsdl";
		}
		String nonexistantFile = "nonexistantfile.wsdl";
		// 1. A valid file should report back that it's valid.
		IValidationReport report = validate.validateFile(validFile);
		assertFalse("Errors were reported for a valid file.", report.hasErrors());
		
		// 2. An invalid file should report back that it's invalid.
		IValidationReport report2 = validate.validateFile(invalidFile);
		assertTrue("Errors were not reported for an invalid file.", report2.hasErrors());
		
		// 3. A file that can't be found should succeed and report that it's invalid.
		IValidationReport report3 = validate.validateFile(nonexistantFile);
		assertTrue("Errors were not reported for a nonexistant file.", report3.hasErrors());
	}
	
	/**
	 * Test the validate method.
	 * 1. The method should succeed with one file specified.
	 * 2. The method should succeed and validate 2 files when 2 are specified.
	 * 3. The method should output verbose information for valid files when verbose is enabled.
	 */
	public void testValidate()
	{
		String validFile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + "Paths/AngleHat^InPath/AngleHatInPathValid.wsdl";
		if(PLUGIN_ABSOLUTE_PATH.startsWith("/"))
		{
			validFile = "file://" + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + "Paths/AngleHat^InPath/AngleHatInPathValid.wsdl";
		}
		String validFile2 = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + "Paths/AngleHatInFilename/AngleHat^InFilenameValid.wsdl";
		if(PLUGIN_ABSOLUTE_PATH.startsWith("/"))
		{
			validFile2 = "file://" + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + "Paths/AngleHatInFilename/AngleHat^InFilenameValid.wsdl";
		}
		ILogger currentLogger = LoggerFactory.getInstance().getLogger();
		WSDLValidateTestLogger logger = new WSDLValidateTestLogger();
		LoggerFactory.getInstance().setLogger(logger);
	  
		// 1. The method should succeed with one file specified.
		validate.getWSDLFiles().add(validFile);
		validate.validate();
		assertEquals("Validating a single valid file failed.", MessageFormat.format(WSDLValidateTextUIMessages._UI_VALIDATION_SUMMARY, new Object[]{new Integer(1), new Integer(0)}), logger.getInfos().get(0));
		validate.getWSDLFiles().clear();
		logger.getInfos().clear();
	  
	  	// 2. The method should succeed and validate 2 files when 2 are specified.
	  	validate.getWSDLFiles().add(validFile);
	  	validate.getWSDLFiles().add(validFile2);
	  	validate.validate();
	  	assertEquals("Validating two valid files failed.", MessageFormat.format(WSDLValidateTextUIMessages._UI_VALIDATION_SUMMARY, new Object[]{new Integer(2), new Integer(0)}), logger.getInfos().get(0));
	  	validate.getWSDLFiles().clear();
	  	logger.getInfos().clear();
	  
	  	// 3. The method should output verbose information for valid files when verbose is enabled.
	  	validate.setVerbose(true);
	  	validate.getWSDLFiles().add(validFile);
	  	validate.validate();
	  	assertEquals("Validating a single valid file failed.", MessageFormat.format(WSDLValidateTextUIMessages._UI_FILE_VALID, new Object[]{validFile}), logger.getVerboses().get(0));
	  	assertEquals("Validating a single valid file failed.", MessageFormat.format(WSDLValidateTextUIMessages._UI_VALIDATION_SUMMARY, new Object[]{new Integer(1), new Integer(0)}), logger.getInfos().get(0));
	  	validate.getWSDLFiles().clear();
	  	validate.setVerbose(false);
	  	logger.getInfos().clear();
	  	logger.getVerboses().clear();
	  
	  	LoggerFactory.getInstance().setLogger(currentLogger);
	}
	
	/**
	 * Test the getMessages method.
	 * 1. The method should return a properly formatted string for a single error.
	 * 2. The method should return a properly formatted string for a single warning.
	 * 3. The method should return a properly fomatted string for 2 messages.
	 */
	public void testGetMessages()
	{	
		IValidationMessage errorMessage = new ValidationMessageImpl("MESSAGE", 1, 2, IValidationMessage.SEV_ERROR, "URI");
		IValidationMessage warningMessage = new ValidationMessageImpl("MESSAGE", 1, 2, IValidationMessage.SEV_WARNING, "URI");
		// 1. The method should return a properly formatted string for a single error.
		IValidationMessage[] errorMessages = new IValidationMessage[]{errorMessage};
		String message = validate.getMessages(errorMessages);
		assertEquals("The error message was not correct.", WSDLValidateTextUIMessages._UI_ERROR_MARKER + " 1:2 MESSAGE", message);
		
		// 2. The method should return a properly formatted string for a single warning.
		IValidationMessage[] warningMessages = new IValidationMessage[]{warningMessage};
		String message2 = validate.getMessages(warningMessages);
		assertEquals("The warning message was not correct.", WSDLValidateTextUIMessages._UI_WARNING_MARKER + " 1:2 MESSAGE", message2);
		
		// 3. The method should return a properly fomatted string for 2 messages.
		IValidationMessage[] twoMessages = new IValidationMessage[]{errorMessage, warningMessage};
		String message3 = validate.getMessages(twoMessages);
		assertEquals("Two messages was not correct.", WSDLValidateTextUIMessages._UI_ERROR_MARKER + " 1:2 MESSAGE\n" + WSDLValidateTextUIMessages._UI_WARNING_MARKER + " 1:2 MESSAGE", message3);
	}
	
	/**
	 * Test the parseArguments method.
	 * 1. -wsdl11v parsing succeeds and registers the validator with the WSDL validator.
	 * 2. -extv parsing succeeds and registers the validator with the WSDL validator.
	 * 3. Extension validator parsing where a param is omitted succeeds.
	 * 4. -logger parsing succeeds and sets the correct logger.
	 * 5. -D (property) parsing succeeds sets the property on the configuration.
	 * 6. -v, -verbose parsing succeeds and sets verbose correctly.
	 * 7. The specified WSDL files are read properly.
	 * 
	 * Not currently tested:
	 * 		-schema
	 * 		-schemaDir
	 * 		-uriresolver
	 */
	public void testParseArguments()
	{
		// 1. -wsdl11v parsing succeeds and registers the validator with the WSDL validator.
		String[] args1 = new String[]{WSDLValidateWrapper.PARAM_WSDL11VAL, "http://wsdl11validator", "org.eclipse.wst.wsdl.validation.internal.wsdl11.http.HTTPValidator"};
		validate.parseArguments(args1);
		assertTrue("The WSDL 1.1 validator was not registered.", org.eclipse.wst.wsdl.validation.internal.wsdl11.ValidatorRegistry.getInstance().hasRegisteredValidator("http://wsdl11validator"));
		
		// 2. -extv parsing succeeds and registers the validator with the WSDL validator.
		String[] args2 = new String[]{WSDLValidateWrapper.PARAM_EXTVAL, "http://extvalidator", "validatorclass"};
		validate.parseArguments(args2);
		assertTrue("The WSDL extension validator was not registered.", ValidatorRegistry.getInstance().hasRegisteredValidator("http://extvalidator", ValidatorRegistry.EXT_VALIDATOR));
		
		// 3. Extension validator parsing where a param is omitted succeeds.
		String[] args3 = new String[]{WSDLValidateWrapper.PARAM_EXTVAL, "http://extvalidator2", "-dummyparam"};
		validate.parseArguments(args3);
		assertFalse("The WSDL extension validator was registered without enough information.", ValidatorRegistry.getInstance().hasRegisteredValidator("http://extvalidator2", ValidatorRegistry.EXT_VALIDATOR));
		
		// 4. -logger parsing succeeds and sets the correct logger.
		ILogger currentLogger = LoggerFactory.getInstance().getLogger();
		String[] args4 = new String[]{WSDLValidateWrapper.PARAM_LOGGER, "org.eclipse.wst.wsdl.validation.internal.logging.StandardLogger"};
		validate.parseArguments(args4);
		assertTrue("The registered logger is not a StandardLogger", LoggerFactory.getInstance().getLogger() instanceof StandardLogger);
		assertFalse("The registered logger is the same as originally registered.", currentLogger.equals(LoggerFactory.getInstance().getLogger()));
		LoggerFactory.getInstance().setLogger(currentLogger);
		
		// 5. -D (property) parsing succeeds sets the property on the configuration.
		String[] args5 = new String[]{WSDLValidateWrapper.PARAM_PROPERTY + "SAMPLENAME=SAMPLEVALUE"};
		validate.parseArguments(args5);
		assertEquals("The parameter was not set correctly.", "SAMPLEVALUE", validate.getConfiguration().getProperty("SAMPLENAME"));
		
		// 6. -v, -verbose parsing succeeds and sets verbose correctly.
		String[] args6 = new String[]{WSDLValidateWrapper.PARAM_VERBOSE};
		validate.setVerbose(false);
		validate.parseArguments(args6);
		assertTrue("Verbose is not set to true.", validate.isVerbose());
		
		String[] args6a = new String[]{WSDLValidateWrapper.PARAM_VERBOSE_SHORT};
		validate.setVerbose(false);
		validate.parseArguments(args6a);
		assertTrue("Verbose is not set to true.", validate.isVerbose());
		
		// 7. The specified WSDL files are read properly.
		String[] args7 = new String[]{"filename1.wsdl", "folder/filename2.wsdl", "folder\filename3.wsdl"};
		validate.parseArguments(args7);
		List wsdlFiles = validate.getWSDLFiles();
		assertEquals("There were not 3 WSDL files listed to validate.", 3, wsdlFiles.size());
		assertTrue("The WSDL file list did not include filename1.wsdl", wsdlFiles.contains("filename1.wsdl"));
		assertTrue("The WSDL file list did not include folder/filename2.wsdl", wsdlFiles.contains("folder/filename2.wsdl"));
		assertTrue("The WSDL file list did not include folder\filename3.wsdl", wsdlFiles.contains("folder\filename3.wsdl"));
	}
}

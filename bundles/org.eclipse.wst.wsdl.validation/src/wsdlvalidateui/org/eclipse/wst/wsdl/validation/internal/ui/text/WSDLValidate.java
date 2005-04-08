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

package org.eclipse.wst.wsdl.validation.internal.ui.text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidator;
import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolver;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolverDelegate;
import org.eclipse.wst.wsdl.validation.internal.ui.WSDLConfigurator;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalog;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalogEntityHolder;

import com.ibm.wsdl.util.StringUtils;

/**
 * A commande line tool to run WSDL Validation on a single or multiple files.
 * 
 * Options
 * -schemaDir directory       : a directory of schemas to load into the catalog
 * -schema namespace location : a schema to load into the registry
 * -wsdl11v classname namespace resourcebundle : register a WSDL 1.1 extension validator
 *                              to load for the given namespace with the given resourcebundle
 * -wsiv validatorClass namespace propertiesfile : register a WS-I validator
 * -uriresolver URIResolverClass : register an extension URI resolver
 */
public class WSDLValidate
{
  private final String FILE_PREFIX = "file:///";
  private static final String VALIDATOR_PROPERTIES =
    org.eclipse.wst.wsdl.validation.internal.Constants.WSDL_VALIDATOR_PROPERTIES_FILE;
  private static final String UI_PROPERTIES = "validatewsdlui";
  private static final String _ERROR_WRONG_ARGUMENTS = "_ERROR_WRONG_ARGUMENTS";
  private static final String _UI_INFORMATION_DELIMITER = "_UI_INFORMATION_DELIMITER";
  private static final String _UI_ACTION_VALIDATING_FILE = "_UI_ACTION_VALIDATING_FILE";
  private static final String _UI_VALID = "_UI_VALID";
  private static final String _UI_INVALID = "_UI_INVALID";
  private static final String _UI_ERROR_MARKER = "_UI_ERROR_MARKER";
  private static final String _UI_WARNING_MARKER = "_UI_WARNING_MARKER";
  private static final String _ERROR_UNABLE_TO_LOAD_EXT_VALIDATOR = "_ERROR_UNABLE_TO_LOAD_EXT_VALIDATOR";
  
  private static final String PARAM_WSDL11VAL = "-wsdl11v";
  private static final String PARAM_EXTVAL = "-extv";
  private static final String PARAM_SCHEMADIR = "-schemaDir";
  private static final String PARAM_SCHEMA = "-schema";
  private static final String PARAM_URIRESOLVER = "-uriresolver";
  
  private static final String STRING_EMPTY = "";
  private static final String STRING_SPACE = " ";
  private static final String STRING_DASH = "-";
  
  
  protected ResourceBundle resourceBundle;
  protected List errors = null;
  protected List warnings = null;
  protected WSDLValidator wsdlValidator;

  /**
   * Constuctor.
   */
  public WSDLValidate()
  {
  	wsdlValidator = new WSDLValidator();
  }
  
  public void addURIResolver(IURIResolver uriResolver)
  {
  	wsdlValidator.addURIResolver(uriResolver);
  }

  /**
   * Run WSDL validation on a given file.
   * 
   * @param directory - the current dir for resolving relative file names
   * @param filename - the name of the file to validate
   * @param validatorRB - the WSDL validator resource bundle
   * @throws Exception
   */
  protected IValidationReport validateFile(String directory, String filename, ResourceBundle validatorRB)
    throws Exception
  {
    //	resolve the location of the file
    String filelocation = null;
    try
    {
      URL test = StringUtils.getURL(new URL(FILE_PREFIX + directory + "/"), filename);
      filelocation = test.toExternalForm();
    }
    catch (MalformedURLException e)
    {
      throw new Exception("Unable to resolve WSDL file location");
    }
    // run validation on the file and record the errors and warnings
    IValidationReport valReport = wsdlValidator.validate(filelocation);
    return valReport;
  }

  /**
   * Returns a String with formatted output for a list of messages.
   * 
   * @param messages The messages to get.
   * @param errormarker The marker to use to label error messages.
   * @param warningmarker The marker to use to label warning messages.
   * @return A string with a formatted list of the messages.
   */
  protected String getMessages(IValidationMessage[] messages, String errormarker, String warningmarker)
  {
    StringBuffer outputBuffer = new StringBuffer();
    if (messages != null)
    {
      // create a list of messages that looks like
      // ERROR 1:1 Error message content
      int numMessages = messages.length;
      String marker = null;
      for (int i = 0; i < numMessages; i++)
      {
        IValidationMessage message = messages[i];
        int severity = message.getSeverity();
        if (severity == IValidationMessage.SEV_ERROR)
        {
          marker = errormarker;
        }
        else if (severity == IValidationMessage.SEV_WARNING)
        {
          marker = warningmarker;
        }
        else
        {
          marker = STRING_EMPTY;
        }
        outputBuffer
          .append(marker)
          .append(STRING_SPACE)
          .append(message.getLine())
          .append(":")
          .append(message.getColumn())
          .append(STRING_SPACE)
          .append(message.getMessage())
          .append("\n");
      }
    }
    return outputBuffer.toString();
  }

  /**
   * The main entry point into the command line tool. 
   * Checks the command line arguments, registers the default validators and runs validation on the
   * list of files.
   * 
   * @param args - the arguments to the validator
   */
  public static void main(String[] args)
  {
    List wsdlFiles = new Vector();
    MessageGenerator messGen = null;
    ResourceBundle validatorRB = null;
    try
    {
      ResourceBundle uiRB = ResourceBundle.getBundle(UI_PROPERTIES);
      messGen = new MessageGenerator(uiRB);
      validatorRB = ResourceBundle.getBundle(VALIDATOR_PROPERTIES);
    }
    catch (MissingResourceException e)
    {
      System.err.println("Validation failed: Unable to load the properties file.");
      return;
    }
    // no arguments specified. Print usage.
    if (args.length < 1)
    {
      System.out.println(messGen.getString(_ERROR_WRONG_ARGUMENTS));
      return;
    }

    int argslength = args.length;

    // register the default validators
    WSDLConfigurator.registerDefaultValidators(validatorRB);

    WSDLValidate wsdlValidator = new WSDLValidate();
    // go through the parameters
    for (int i = 0; i < argslength; i++)
    {
      String param = args[i];

      // registering a validator
      if (param.equalsIgnoreCase(WSDLValidate.PARAM_WSDL11VAL) || param.equalsIgnoreCase(WSDLValidate.PARAM_EXTVAL))
      {

        String validatorClass = args[++i];
        if (!validatorClass.startsWith(WSDLValidate.STRING_DASH))
        {
          String namespace = args[++i];

          if (!namespace.startsWith(WSDLValidate.STRING_DASH))
          {
            String propertiesFile = args[++i];
            if (propertiesFile.startsWith(WSDLValidate.STRING_DASH))
            {
              propertiesFile = null;
              i--;
            }
            if(param.equalsIgnoreCase(WSDLValidate.PARAM_WSDL11VAL))
            {  
              WSDLConfigurator.registerWSDL11Validator(namespace, validatorClass, propertiesFile, null);
            }
            else if(param.equalsIgnoreCase(WSDLValidate.PARAM_EXTVAL))
            {
              WSDLConfigurator.registerExtensionValidator(namespace, validatorClass, propertiesFile, null);
            }
          }
          else
          {
            namespace = null;
            i--;
          }
        }
        else
        {
          validatorClass = null;
          i--;
        }  
      }
      // registering a directory with schemas
      else if (param.equalsIgnoreCase(WSDLValidate.PARAM_SCHEMADIR))
      {
        String xsdDir = args[++i];
        XMLCatalog.addSchemaDir(xsdDir);
      }
      // registering a schema
      else if (param.equalsIgnoreCase(WSDLValidate.PARAM_SCHEMA))
      {
        String publicid = args[++i];
        String systemid = args[++i];
        XMLCatalog.addEntity(new XMLCatalogEntityHolder(publicid, systemid));
      }
      else if(param.equalsIgnoreCase(PARAM_URIRESOLVER))
      {
        String resolverClass = args[++i];
        wsdlValidator.addURIResolver(new URIResolverDelegate(resolverClass, null).getURIResolver());
      }
      // a file to validate
      else
      {
        if(!param.startsWith(WSDLValidate.STRING_DASH))
        {  
          wsdlFiles.add(param);
        }
      }

    }
    // validate the file
    StringBuffer outputBuffer = null;
    String infoDelim = messGen.getString(_UI_INFORMATION_DELIMITER);
    String valid = messGen.getString(_UI_VALID);
    String invalid = messGen.getString(_UI_INVALID);
    String errormarker = messGen.getString(_UI_ERROR_MARKER);
    String warningmarker = messGen.getString(_UI_WARNING_MARKER);

    Iterator filesIter = wsdlFiles.iterator();
    while (filesIter.hasNext())
    {
      outputBuffer = new StringBuffer();
      String wsdlFile = (String)filesIter.next();
      try
      {
        IValidationReport valReport = wsdlValidator.validateFile(System.getProperty("user.dir"), wsdlFile, validatorRB);

        outputBuffer.append(infoDelim).append("\n");
        outputBuffer.append(messGen.getString(_UI_ACTION_VALIDATING_FILE, wsdlFile)).append(" - ");
        if (!valReport.hasErrors())
        {
          outputBuffer.append(valid);
        }
        else
        {
          outputBuffer.append(invalid);
        }
        outputBuffer.append("\n").append(infoDelim).append("\n");
        outputBuffer.append(wsdlValidator.getMessages(valReport.getValidationMessages(), errormarker, warningmarker));
        System.out.println(outputBuffer.toString());
      }
      catch (Exception e)
      {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Load a validator into the wsdl validation framework registry.
   * 
   * @param namespace - the namespace the validator checks
   * @param validatorClass - the name of the class the implements IWSDLValidator
   * @param propertiesFile - the name of the properties file for the validator
   * @param type           - the type of validator - WSDL or WS-I
   * @param messGen        - a MessageGenerator for producing error messages
   */
//  protected static void loadExtensionValidator(
//    String namespace,
//    String validatorClass,
//    String propertiesFile,
//    Integer type,
//    MessageGenerator messGen)
//  {
//    try
//    {
//      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//      IWSDLValidator validator = (IWSDLValidator)classLoader.loadClass(validatorClass).newInstance();
//      // if no properties file is specified we can still load the validator
//      if (propertiesFile != null && !propertiesFile.equals(""))
//      {
//        ResourceBundle rb = ResourceBundle.getBundle(propertiesFile);
//        validator.setResourceBundle(rb);
//      }
//      else
//      {
//        propertiesFile = null;
//      }
//
//      ValidatorRegistry.getInstance().registerValidator(namespace, validator, type);
//    }
//    catch (Exception e)
//    {
//      System.out.println(messGen.getString(_ERROR_UNABLE_TO_LOAD_EXT_VALIDATOR, namespace, e));
//    }
//
//  }
  //	/**
  //	 * loadWSDL11ExtensionValidator
  //	 * Load an extension validator into the WSDL 1.1 validator
  //   * @param namespace
  //   * @param validatorClass
  //   * @param propertiesFile
  //   * @param type
  //   * @param classLoader
  //   */
  //  protected static void loadWSDL11ExtensionValidator(
  //			String namespace,
  //			String validatorClass,
  //			String resourceBundle,
  //			ClassLoader classLoader)
  //		{
  //			try
  // {
  // 	Class valclass = classLoader.loadClass(validatorClass);
  // 	IWSDL11Validator validator = (IWSDL11Validator)valclass.newInstance();
  // 	validator.setResourceBundle(ResourceBundle.getBundle(resourceBundle));
  // 	WSDLConfigurator.registerWSDL11Validator(namespace,validator);
  // }
  // catch(Exception e)
  // {
  //	 System.out.println("Unable to load the WSDL 1.1 validator for namespace "
  // + namespace + e);
  // }
  //
  //		}
}

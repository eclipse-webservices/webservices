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

package org.eclipse.wst.wsdl.validation.internal;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.StandardParserConfiguration;
import org.apache.xerces.xni.XNIException;
import org.eclipse.wst.wsdl.validation.internal.exception.ValidateWSDLException;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.xml.AbstractXMLConformanceFactory;
import org.eclipse.wst.wsdl.validation.internal.xml.IXMLValidator;
import org.eclipse.wst.wsdl.validation.internal.xml.LineNumberDOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This is the main entrypoint to the WSDL Validator. The controller is
 * responsible for calling the reader, the XML conformance check, the WSDL
 * validation and the WS-I validation if selected. The controller contains any
 * errors and warnings generated as well.
 */
public class ValidationController
{
  protected final String _ERROR_PROBLEM_WSDL_VALIDATOR = "_ERROR_PROBLEM_WSDL_VALIDATOR";
  protected final String _ERROR_NO_WSDL_VALIDATOR = "_ERROR_NO_WSDL_VALIDATOR";
  protected final String _ERROR_PROBLEM_EXT_VALIDATOR = "_ERROR_PROBLEM_EXT_VALIDATOR";

  protected ValidatorRegistry ver;
  protected ResourceBundle resourcebundle;
  protected MessageGenerator messagegenerator;
  protected URIResolver uriResolver;
  protected Hashtable attributes = new Hashtable();

  //protected String wsdlNamespace = null;

  /**
   * The ResourceBundle needs to be set so it can be passed to the reader.
   * 
   * @param rb
   *            The resource bundle for this validator.
   */
  public ValidationController(ResourceBundle rb, URIResolver uriResolver)
  {
    resourcebundle = rb;
    messagegenerator = new MessageGenerator(resourcebundle);
    this.uriResolver = uriResolver;

    ver = ValidatorRegistry.getInstance();
  }
  
  /**
   * Add the attributes specified to the validation.
   * 
   * @param attributes The attributes to add to the validation.
   */
  public void setAttributes(Hashtable attributes)
  {
  	this.attributes.putAll(attributes);
  }

  /**
   * Validate the WSDL file. Check the file for XML conformance. If it is XML
   * conformant, read the file and check it for WSDL conformance. If it is WSDL
   * conformant and WS-I conformance is set to suggest or require, check the
   * file for WS-I conformance.
   * 
   * @param uri
   *            The uri where the WSDL document is located.
   * @param wsiLevel
   *            The level of WS-I conformance to use for validation.
   * @return A validation report with the validation info for the file.
   */
  public ValidationReport validate(String uri)
  {
    ControllerValidationInfo valInfo = new ValidationInfoImpl(uri, messagegenerator);
    ((ValidationInfoImpl)valInfo).setURIResolver(uriResolver);
    ((ValidationInfoImpl)valInfo).setAttributes(attributes);

    if (validateXML(valInfo))
    {
      Document wsdldoc = getDocument(uri);
      String wsdlns = getWSDLNamespace(wsdldoc);
      if (validateWSDL(wsdldoc, valInfo, wsdlns))
      {
        validateExtensionValidators(wsdldoc, valInfo, wsdlns);
      }
    }
    return (ValidationReport)valInfo;
  }

  /**
   * Validate the file for XML conformance.
   * 
   * @param uri
   *            The uri where the WSDL document is located.
   * @return True if the file is conformant, false otherwise.
   */
  protected boolean validateXML(ControllerValidationInfo valInfo)
  {
    IXMLValidator xmlValidator = AbstractXMLConformanceFactory.getInstance().getXMLValidator();
    xmlValidator.setURIResolver(uriResolver);
    xmlValidator.setFile(valInfo.getFileURI());
    //xmlValidator.setValidationInfo(valInfo);
    xmlValidator.run();
    // if there are no xml conformance problems go on to check the wsdl stuff
    if (xmlValidator.hasErrors())
    {
      // temp handling of XML errors until validator is updated.
      List errors = xmlValidator.getErrors();
      Iterator errorsIter = errors.iterator();
      while (errorsIter.hasNext())
      {
        ValidationMessage valMes = (ValidationMessage)errorsIter.next();
        valInfo.addError(valMes.getMessage(), valMes.getLine(), valMes.getColumn(), valMes.getURI());
      }
      return false;
    }
    //wsdlNamespace = xmlValidator.getWSDLNamespace();
    return true;
  }

  /**
   * Validate the WSDL file. Set the errors and warning appropriately.
   * 
   * @param wsdldoc A W3C document representation of the WSDL document.
   * @param valInfo The current validation information.
   * @param wsdlNamespace The WSDL namespace to validate.
   * @return True if the file is valid, false otherwise.
   */
  protected boolean validateWSDL(Document wsdldoc, ControllerValidationInfo valInfo, String wsdlNamespace)
  {
    WSDLValidatorDelegate[] wsdlVals = ver.queryValidatorRegistry(wsdlNamespace, ValidatorRegistry.WSDL_VALIDATOR);
    if (wsdlVals != null)
    {
      for (int i = 0; i < wsdlVals.length; i++)
      {
        WSDLValidatorDelegate wsdlvaldel = wsdlVals[i];
        IWSDLValidator wsdlVal = wsdlvaldel.getValidator();

        // If the wsdl validator isn't null, validate.
        if (wsdlVal != null)
        {
          try
          {
            wsdlVal.validate(wsdldoc, valInfo);
          }
          catch (ValidateWSDLException e)
          {
            valInfo.addError(messagegenerator.getString(_ERROR_PROBLEM_WSDL_VALIDATOR, wsdlNamespace), 1, 1, valInfo.getFileURI());
          }
        }
        // If the validator is null and the namespace isn't create an error.
        // If the namespace is null the file is empty (and the XML validator
        // has let it go)
        // so it is valid but does not need to be validated.
        else
        {
          valInfo.addError(
            messagegenerator.getString(_ERROR_NO_WSDL_VALIDATOR, wsdlNamespace),
            1,
            1,
            valInfo.getFileURI());
        }
      }
    }
    // No validators registered.
    else
    {
      valInfo.addError(messagegenerator.getString(_ERROR_NO_WSDL_VALIDATOR, wsdlNamespace), 1, 1, valInfo.getFileURI());
    }
    valInfo.completeWSDLValidation();

    return valInfo.isWSDLValid();
  }

  /**
   * Validate the WSDL file with the extension validator. Set the errors and warning appropriately.
   * 
   * @param wsdldoc A W3C document representation of the WSDL document.
   * @param valInfo The current validation information.
   * @param wsdlNamespace The WSDL namespace to validate.
   * @return True if the file is valid, false otherwise.
   */
  protected void validateExtensionValidators(Document wsdldoc, ControllerValidationInfo valInfo, String wsdlNamespace)
  {
    WSDLValidatorDelegate[] extVals = ver.queryValidatorRegistry(wsdlNamespace, ValidatorRegistry.EXT_VALIDATOR);
    if(extVals != null)
    {
      int numvals = extVals.length;
      for(int i = 0; i < numvals; i++)
      {
        WSDLValidatorDelegate extvaldel = extVals[i];
        IWSDLValidator extval = extvaldel.getValidator();
        if(extval != null)
        {
          try
          {
          extval.validate(wsdldoc, valInfo);
          }
          catch(Throwable  t)
          {
            valInfo.addWarning(messagegenerator.getString(_ERROR_PROBLEM_EXT_VALIDATOR,  extvaldel.getValidatorClassName(), wsdlNamespace), 1, 1, valInfo.getFileURI());
            // This error should be logged and not displayed to the user.
          }
        }
      }
    }
  }

  /**
   * Set the ResourceBundle for this ValidatorManager.
   * 
   * @param rb
   *            The resource bundle to set.
   * @see #getResourceBundle
   */
  public void setResourceBundle(ResourceBundle rb)
  {
    resourcebundle = rb;
  }

  /**
   * Get the ResourceBundle for this ValidationController.
   * 
   * @return The resource bundle set for this ValidationController.
   * @see #setResourceBundle
   */
  public ResourceBundle getResourceBundle()
  {
    return resourcebundle;
  }
  
  /**
   * Get a DOM document representation of the WSDL document.
   * 
   * @param uri The uri of the file to read
   * @return The DOM model of the WSDL document or null if the document can't be read.
   */
  private Document getDocument(String uri)
  {
    try
    {
      // Catch a premature EOF error to allow empty WSDL files to be considered valid.
      StandardParserConfiguration configuration = new StandardParserConfiguration()
      {
        protected XMLErrorReporter createErrorReporter()
        {
          return new XMLErrorReporter()
          {
            public void reportError(String domain, String key, Object[] arguments, short severity) throws XNIException
            {
              boolean reportError = true;
              if (key.equals("PrematureEOF"))
              {         
                reportError = false;
              }

              if (reportError)
              {
                super.reportError(domain, key, arguments, severity);
              }
            }
          };
        }
      };

      DOMParser builder = new LineNumberDOMParser(configuration);
      builder.parse(uri);
      Document doc = builder.getDocument();

      return doc;
    }
    catch (Throwable t)
    {
      //System.out.println(t);
    }
    return null;
  }
  
  private String getWSDLNamespace(Document doc)
  {
    String wsdlns = null;
    if(doc != null)
    {
      Element definitions = doc.getDocumentElement();
      if(definitions.getLocalName().equals("definitions"))
      {
        wsdlns = definitions.getNamespaceURI();
      }
    }
    return wsdlns;
  }
}

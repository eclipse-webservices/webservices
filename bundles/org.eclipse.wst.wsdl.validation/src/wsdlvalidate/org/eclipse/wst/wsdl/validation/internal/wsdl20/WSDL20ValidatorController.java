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

package org.eclipse.wst.wsdl.validation.internal.wsdl20;

import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.wst.wsdl.validation.internal.IWSDLValidator;
import org.eclipse.wst.wsdl.validation.internal.ValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.exception.ValidateWSDLException;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.w3c.dom.Document;

/**
 * The validator controller is the head of validation. Has methods for getting 
 * extension validators.
 */
public class WSDL20ValidatorController implements IWSDLValidator
{
  protected final String _WARN_NO_VALDIATOR = "_WARN_NO_VALDIATOR";
  protected final int ERROR_MESSAGE = 0;
  protected final int WARNING_MESSAGE = 1;
  protected String fileURI;
  protected List schemas = new Vector();
//  protected Definitions wsdlDefinition;
  protected Hashtable elementLocations = null;
  protected ResourceBundle resourcebundle;
  protected MessageGenerator messagegenerator = null;
  //protected ValidationController validationController;
  protected ValidatorRegistry ver = ValidatorRegistry.getInstance();
  protected boolean errors = false;

  /**
   * Constructor.
   * 
   * @param rb The resource bundle to set for this controller.
   */
  public WSDL20ValidatorController(ResourceBundle rb)
  {
    setResourceBundle(rb);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.IWSDLValidator#validate(org.w3c.dom.Document, org.eclipse.wsdl.validate.ValidationInfo)
   */
  public void validate(Document domModel, ValidationInfo valInfo) throws ValidateWSDLException
  {
    // reset the variables
    //reset();
    fileURI = valInfo.getFileURI();
    //this.validationController = validationcontroller;

    
//    List readerErrors = readWSDLDocument(domModel, fileURI, getMessageGenerator());
    //		handle any reader errors
//    if (readerErrors != null)
//    {
//      Iterator readerErrorsI = readerErrors.iterator();
//      while (readerErrorsI.hasNext())
//      {
//        ReaderError re = (ReaderError)readerErrorsI.next();
//        valInfo.addError(re.getMessage(), re.getLine(), re.getColumn(), re.getFileURI());
//      }
//    }
//    validateWSDLElement(Constants.NS_URI_WSDL, wsdlDefinition, new Vector(), valInfo);
  }
  
  /**
   * Read in the WSDL document and set the model and imported schemas.
   * 
   * @param domModel A DOM document model of the file to be read.
   * @param file The file to read.
   * @param messagegenerator The messagegenerator the reader should use for any messages produced.
   * @throws ValidateWSDLException
   * @return A list of reader errors.
   */
//  protected List readWSDLDocument(Document domModel, String file, MessageGenerator messagegenerator) throws ValidateWSDLException
//  {
//    List readerErrors = null;
//    try
//    {
//
//      WSDLReaderImpl wsdlReader = new WSDLReaderImpl();
//      wsdlDefinition = wsdlReader.readWSDL(file, domModel);
//
//      readerErrors = wsdlReader.getReaderErrors();
//
//    }
//    catch (WSDLException e)
//    {
//      throw new ValidateWSDLException(e.getMessage() /*+ " " + e.getFaultCode()*/);
//    }
//
//    catch (Exception e)
//    {
//      throw new ValidateWSDLException("unable to read file" + e.getMessage() + " " + e.toString());
//    }
//    return readerErrors;
//  }

  /**
   * Given a WSDL element, call ValidateElement for it.
   * 
   * @param namespace The namespace of the element to validate.
   * @param element The element to validate.
   * @param parents The list of parents for this element.
   */
  public void validateWSDLElement(String namespace, Object element, List parents, ValidationInfo valInfo)
  {
    IWSDL20Validator val = ver.queryValidatorRegistry(namespace);
    if (val != null)
    {
      val.validate(element, parents, valInfo);
    }
    else
    {
      //valInfo.addWarning(messagegenerator.getString(_WARN_NO_VALDIATOR, namespace), element);
    }
  }

  /**
   * Add a schema to the list of schemas.
   * 
   * @param xsModel The schema to add.
   */
//  public void addSchema(XSModel xsModel)
//  {
//    if (xsModel != null)
//    {
//      schemas.add(xsModel);
//    }
//  }

  /**
   * Return the list containing the schemas.
   * 
   * @return The list of schemas.
   */
//  public List getSchemas()
//  {
//    return schemas;
//  }

  /**
  	* Get the ResourceBundle for this ValidatorManager.
  	* 
  	* @return The resource bundle registered for this controller.
  	* @see #setResourceBundle
  	*/
  public ResourceBundle getResourceBundle()
  {
    return resourcebundle;
  }

  /**
   * Set the ResourceBundle for this ValidatorManager.
   * 
   * @param rb The resource bundle to set.
   * @see #getResourceBundle
   */
  public void setResourceBundle(ResourceBundle rb)
  {
    if (rb != null)
    {
      resourcebundle = rb;
      messagegenerator = new MessageGenerator(rb);
    }

  }

  /**
   * Get the message generator registered for this controller.
   * 
   * @return The message generator registered for this controller.
   */
  public MessageGenerator getMessageGenerator()
  {
    return messagegenerator;
  }

  /**
   * Return the filename for the file currently being validated. Some validators require this.
   * 
   * @return The filename for the file being validated.
   */
//  public String getFilename()
//  {
//    return fileURI;
//  }

  /**
   * Convenience method for extensibly validators to add error messages.
   * 
   * @param object The object to add the error for.
   * @param error The error to add.
   */
//  public void addErrorMessage(Object object, String error)
//  {
//    addValidationMessage(ERROR_MESSAGE, object, error);
//    errors = true;
//  }

  /**
   * Method for extensibly validators to add error messages when they know
   * line and column numbers.
   * 
   * @param line The line where the error message is located.
   * @param column The column where the error message is located.
   * @param error The error message.
   */
//  public void addErrorMessage(int line, int column, String error)
//  {
//    addValidationMessage(ERROR_MESSAGE, line, column, error);
//    errors = true;
//  }

  /**
   * Convenience method for extensibly validators to add warning messages.
   * 
   * @param object The object to add the warning message.
   * @param warning The warning message.
   */
//  public void addWarningMessage(Object object, String warning)
//  {
//    addValidationMessage(WARNING_MESSAGE, object, warning);
//  }

  /**
   * Method for extensibly validators to add warning messages when they know
   * line and column numbers.
   * 
   * @param line The line where the error message is located.
   * @param column The column where the error message is located.
   * @param warning The warning message.
   */
//  public void addWarningMessage(int line, int column, String warning)
//  {
//    addValidationMessage(WARNING_MESSAGE, line, column, warning);
//  }

  /**
   * If you have an object read in by the reader for this
   * validatorcontroller the object can be passed in here and the line and column
   * information will be abstracted from it.
   * 
   * @param type The type of message to add.
   * @param o The object that has the error (used to get the location).
   * @param message The message to add.
   */
//  protected void addValidationMessage(int type, Object o, String message)
//  {
//    ValidatableElement ve = (ValidatableElement)o;
////    int[] location;
////    if (elementLocations.containsKey(o))
////    {
////      location = (int[])elementLocations.get(o);
////    }
////    // if we give it an element that hasn't been defined we'll set the location
////    // at (0,0) so the error shows up but no line marker in the editor
////    else
////    {
////      location = new int[] { 0, 0 };
////    }
//    addValidationMessage(type, ve.getLine(), ve.getColumn(), message);
//  }

  /**
    * Creates a validation message of the specified type.
    * 
  	* @param type The type of validation message to add.
  	* @param line The line where the error message is located.
  	* @param column The line where the column message is located.
  	* @param message The message to add.
  	*/
//  protected void addValidationMessage(int type, int line, int column, String message)
//  {
//    if (message != null)
//    {
//      if (type == ERROR_MESSAGE)
//      {
//        validationController.addErrorMessage(line, column, message);
//      }
//      else if (type == WARNING_MESSAGE)
//      {
//        validationController.addWarningMessage(line, column, message);
//      }
//    }
//  }

  /**
   * @see org.eclipse.wst.wsdl.validation.controller.IWSDLValidator#isValid()
   */
  public boolean isValid()
  {
    return !errors;
  }

  /**
   * Reset the validator controller.
   */
//  protected void reset()
//  {
//    schemas = new Vector();
//    fileURI = "";
//    wsdlDefinition = null;
//    elementLocations = null;
//    resourcebundle = null;
//    //validationController = null;
//    errors = false;
//  }
}

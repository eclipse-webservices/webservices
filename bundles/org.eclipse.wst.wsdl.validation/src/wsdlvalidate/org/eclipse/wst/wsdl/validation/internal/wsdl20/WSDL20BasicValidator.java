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

import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.wst.wsdl.validation.internal.IValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
/**
 * Validate the elements defined in a WSDL 1.1 Document.
 */
public class WSDL20BasicValidator implements IWSDL20Validator
{
  protected final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";
  protected final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";
  protected final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";
  protected final String SCHEMA_FULL_CHECKING_FEATURE_ID =
    "http://apache.org/xml/features/validation/schema-full-checking";
  protected final String CONTINUE_AFTER_FATAL_ERROR_ID = "http://apache.org/xml/features/continue-after-fatal-error";
  protected final String SOAP_ENCODING_URI = "http://schemas.xmlsoap.org/soap/encoding/";

  protected final int ELEMENT = 0;
  protected final int TYPE = 1;

  //protected WSDL20ValidatorController validatorcontroller;
  protected MessageGenerator messagegenerator;

  /**
   * Constructor.
   */
  public WSDL20BasicValidator()
  {
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.validator.IWSDL11Validator#validate(java.lang.Object, java.util.List, org.eclipse.wsdl.validate.IValidationInfo)
   */
  public void validate(Object element, List parents, IValidationInfo valInfo)
  {
    //this.validatorcontroller = validatorcontroller;
    //setDefaultResourceBundleIfNeeded(validatorcontroller);
  	
//    Definitions wsdlDefinitions = (Definitions)element;
//    validateTypes(wsdlDefinitions, valInfo);
//    validateServices(wsdlDefinitions, valInfo);
//    validateBindings(wsdlDefinitions, valInfo);
//    validateInterfaces(wsdlDefinitions, valInfo);

  }

  /**
   * Takes a list of ExtensibilityElements and checks if there's a validator 
   * associated with each element and if so calls the validator.
   * 
   * @param parents The list of parents of the elements.
   * @param extensibilityElements The list of elements to validate.
   * @param validatorcontroller The validator controller.
   * @param wsdlDefinition The defnintions element for this document.
   */
//  protected void validateExtensibilityElementList(
//    List parents,
//    ElementInfoItem[] extensibilityElements,
//    ValidationInfo valInfo,
//    Definitions wsdlDefinitions)
//  {
//    //TODO: get the validation controller statically to get the extensibility validator
////    ValidatorRegistry ver = ValidatorRegistry.getInstance();
////    Iterator extElems = extensibilityElements.iterator();
////    while (extElems.hasNext())
////    {
////      ElementInfoItem element = (ExtensibilityElement)extElems.next();
////      //      if (ver.hasRegisteredValidator(element.getElementType().getNamespaceURI()))
////      //      {
////      validatorcontroller.validateWSDLElement(element.getElementType().getNamespaceURI(), element, parents);
////      //ver.queryValidatorRegistry(element.getElementType().getNamespaceURI()).validate(element, parents, validatormanager);
////      //      }
////    }
//  }

  /**
   * If the resourcebundle hasn't been set, set it to the one registered with the ValidatorController.
   * 
   * @param validatorcontroller The validator controller to get the resource bundle from.
   */
//  protected void setDefaultResourceBundleIfNeeded(WSDL20ValidatorController validatorcontroller)
//  {
//    if (messagegenerator == null)
//    {
//      setResourceBundle(validatorcontroller.getResourceBundle());
//    }
//  }
  /**
   * Set the resourcebundle to the one specified.
   * 
   * @param rb The resource bundle to set.
   */
  public void setResourceBundle(ResourceBundle rb)
  {
    messagegenerator = new MessageGenerator(rb);
  }

  /**
   * Ensure that the Types element is correct. 
   * 
   * @param wsdlDefinition The definitions element from the current document.
   */

//  protected void validateTypes(Definitions wsdlDefinitions, ValidationInfo valInfo)
//  {
//    Types types = wsdlDefinitions.getTypes();
//    // ensure that types is defined
//    if (types != null)
//    {
//      List parents = new Vector();
//      parents.add(wsdlDefinitions);
//      ElementInfoItem elementInfoItems[] = types.getElementInfoItems();
//      parents.add(0, types);
//      validateExtensibilityElementList(parents, elementInfoItems, valInfo, wsdlDefinitions);
//      parents.remove(0);
//    }
//  }

  /**
   * Validates all of the declared services for the definition.
   * 
   * @param wsdlDefinitions The WSDL definitions element.
   * @param valInfo Validation info for the current validation.
   */
//  protected void validateServices(Definitions wsdlDefinitions, ValidationInfo valInfo)
//  {
//
//  }

  /**
   * Checks that the bindings refer to valid interfaces and all of the operations
   * in a given binding refer to a defined operation within the corresponding
   * interfaces.
   * 
   * @param wsdlDefinitions The WSDL definitions element.
   * @param valInfo Validation info for the current validation.
   */
//  protected void validateBindings(Definitions wsdlDefinitions, ValidationInfo valInfo)
//  {
//
//  }

  /**
   * Check that all of the Interfaces have valid messages associated with their
   * operation input, output, infault and outfault types.
   * 
   * @param wsdlDefinitions The WSDL definitions element.
   * @param valInfo Validation info for the current validation.
   */
//  private void validateInterfaces(Definitions wsdlDefinitions, ValidationInfo valInfo)
//  {
//    
//  }

  /**
   * Checks whether the given name is defined in the namespace for the part.  A part is an
   * ELEMENT in WSDL 2.0.
   * 
   * @param namespace The namespace to check.
   * @param name The name to check.
   * @param valInfo Validation info for the current validation.
   * @return True if the part element is defined, false otherwise.
   */
  protected boolean checkMessage(String namespace, String name, IValidationInfo valInfo)
  {

    boolean partvalid = false;
//    // first take care of the situation where it's schema for schema
//    if (namespace.equalsIgnoreCase(SchemaSymbols.URI_SCHEMAFORSCHEMA))
//    {
//      SchemaAttributeTable xsdTable = new SchemaAttributeTable();
//      if (xsdTable.containsSymbol(name))
//      {
//        partvalid = true;
//      }
//    }
//    // check inline and imported schema
//    else
//    {
//      List schemas = validatorcontroller.getSchemas();
//      Iterator schemasIter = schemas.iterator();
//      while (schemasIter.hasNext())
//      {
//        XSModel schema = (XSModel)schemasIter.next();
//        if (schema != null)
//        {
//          if (schema.getElementDeclaration(name, namespace) != null)
//          {
//            partvalid = true;
//            break;
//          }
//         
//        }
//      }
//    }
    // If the SOAP encoding namespace hasn't been explicitly imported do so now.
    // Allow the SOAP encoding namespace to be automatically imported but mark it as a warning.
//    if (!partvalid && namespace.equals(SOAP_ENCODING_URI))
//    {
//      try
//      {
//        XSDValidator xsdVal = new XSDValidator();
//        xsdVal.validate(XMLCatalog.getInstance().resolveEntityLocation(SOAP_ENCODING_URI, null), null);
//        // sanity check in case something goes wrong
//        if (xsdVal.isValid())
//        {
//          XSModel xsModel = xsdVal.getXSModel();
//
//          if (xsModel.getElementDeclaration(name, namespace) != null)
//          {
//            partvalid = true;
//          }
//
//          validatorcontroller.addWarningMessage(
//            partObject,
//            messagegenerator.getString("_WARN_SOAPENC_IMPORTED_PART", "'" + name + "'"));
//        }
//      }
//      catch (Exception e)
//      {
//        //TODO: log the error message
//        //System.out.println(e);
//      }
//    }
    return partvalid;
  }

}

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

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;

import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.SchemaAttributeTable;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.XSDValidator;

import com.ibm.wsdl.Constants;
/**
 * Validate the elements defined in a WSDL 1.1 Document.
 */
public class WSDL11BasicValidator implements IWSDL11Validator
{
  protected final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";
  protected final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";
  protected final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";
  protected final String SCHEMA_FULL_CHECKING_FEATURE_ID =
    "http://apache.org/xml/features/validation/schema-full-checking";
  protected final String CONTINUE_AFTER_FATAL_ERROR_ID = "http://apache.org/xml/features/continue-after-fatal-error";
  protected final String SOAP_ENCODING_URI = "http://schemas.xmlsoap.org/soap/encoding/";
  
  // Error and Warning Keys
  private final String _PORT_NAME_NOT_UNIQUE = "_PORT_NAME_NOT_UNIQUE";
  private final String _NO_BINDING_FOR_PORT = "_NO_BINDING_FOR_PORT";
  private final String _NO_ADDRESS_PORT = "_NO_ADDRESS_PORT";
  private final String _MORE_THEN_ONE_ADDRESS_PORT = "_MORE_THEN_ONE_ADDRESS_PORT";
  private final String _PORTTYPE_UNDEFINED_FOR_BINDING = "_PORTTYPE_UNDEFINED_FOR_BINDING";
  private final String _OPERATION_UNDEFINED_FOR_PORTTYPE = "_OPERATION_UNDEFINED_FOR_PORTTYPE";
  private final String _OPERATION_NO_INPUT_OR_OUTPUT = "_OPERATION_NO_INPUT_OR_OUTPUT";
  private final String _INPUT_NAME_NOT_UNIQUE = "_INPUT_NAME_NOT_UNIQUE";
  private final String _MESSAGE_UNDEFINED_FOR_INPUT = "_MESSAGE_UNDEFINED_FOR_INPUT";
  private final String _OUTPUT_NAME_NOT_UNIQUE = "_OUTPUT_NAME_NOT_UNIQUE";
  private final String _MESSAGE_UNDEFINED_FOR_OUTPUT = "_MESSAGE_UNDEFINED_FOR_OUTPUT";
  private final String _MESSAGE_UNDEFINED_FOR_FAULT = "_MESSAGE_UNDEFINED_FOR_FAULT";
  private final String _PART_NO_ELEMENT_OR_TYPE = "_PART_NO_ELEMENT_OR_TYPE";
  private final String _PART_BOTH_ELEMENT_AND_TYPE = "_PART_BOTH_ELEMENT_AND_TYPE";
  private final String _PART_INVALID_ELEMENT = "_PART_INVALID_ELEMENT";
  private final String _PART_INVALID_TYPE = "_PART_INVALID_TYPE";
  private final String _WARN_SOAPENC_IMPORTED_PART = "_WARN_SOAPENC_IMPORTED_PART";

  private final int ELEMENT = 0;
  private final int TYPE = 1;
  
  private final String REQUEST = "Request";
  private final String RESPONSE = "Response";
  private final String QUOTE = "'";
  private final String EMPTY_STRING = "";
  

  //protected WSDL11ValidatorController validatorcontroller;
  protected MessageGenerator messagegenerator;

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator#validate(java.lang.Object, java.util.List, org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo)
   */
  public void validate(Object element, List parents, WSDL11ValidationInfo valInfo)
  {
    //this.validatorcontroller = validatorcontroller;
    //setDefaultResourceBundleIfNeeded(validatorcontroller);
    Definition wsdlDefinition = (Definition)element;
    //validateTypes(wsdlDefinition, valInfo);
    validateServices(wsdlDefinition, valInfo);
    validateBindings(wsdlDefinition, valInfo);
    validatePortTypes(wsdlDefinition, valInfo);
    validateMessages(wsdlDefinition, valInfo);

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
  protected void validateExtensibilityElementList(
    List parents,
    List extensibilityElements,
    WSDL11ValidationInfo valInfo)
  {
    ValidatorRegistry ver = ValidatorRegistry.getInstance();
    Iterator extElems = extensibilityElements.iterator();
    while (extElems.hasNext())
    {
      ExtensibilityElement element = (ExtensibilityElement)extElems.next();
      String namespace = element.getElementType().getNamespaceURI();
      IWSDL11Validator val = ver.queryValidatorRegistry(namespace);
      if (val != null)
       {
        val.validate(element, parents, valInfo);
      }
//      else
//       {
//        valInfo.addNamespaceWithNoValidator(namespace);
//      }
    }
  }

  /**
   * If the resourcebundle hasn't been set, set it to the one registered with the ValidatorController.
   * 
   * @param validatorcontroller The validator controller to get the resource bundle from.
   */
  //  protected void setDefaultResourceBundleIfNeeded(WSDL11ValidatorController validatorcontroller)
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

  protected void validateTypes(Definition wsdlDefinition, WSDL11ValidationInfo valInfo)
  {
    Types types = wsdlDefinition.getTypes();
    // ensure that types is defined
    if (types != null)
    {
      List parents = new Vector();
      parents.add(wsdlDefinition);
      Object extensibleElements[] = types.getExtensibilityElements().toArray();
      parents.add(0, types);
      validateExtensibilityElementList(parents, types.getExtensibilityElements(), valInfo);
      parents.remove(0);
    }
  }

  /**
   * Validates all of the declared services for the definition.
   * 
   * @param wsdlDefinition The WSDL definitions element.
   */
  protected void validateServices(Definition wsdlDefinition, WSDL11ValidationInfo valInfo)
  {
    if (wsdlDefinition.getServices() == null)
      return;
    Object services[] = wsdlDefinition.getServices().values().toArray();
    List parents = new Vector();
    parents.add(wsdlDefinition);
    Hashtable allPorts = new Hashtable();

    //TODO: check that ports in other imported files don't conflict with ports in this one
    //    // register all of the imported ports
    //    Iterator imports = wsdlDefinition.getImports().values().iterator();
    //    while(imports.hasNext())
    //    {
    //      Iterator impservices = ((Import)imports.next()).getDefinition().getServices().values().iterator();
    //      while(impservices.hasNext())
    //      {
    //        Iterator impports = ((Service)impservices.next()).getPorts().values().iterator();
    //        while(impports.hasNext())
    //        {
    //          Port tempP = (Port)impports.next();
    //          allPorts.put(tempP.getName(),tempP);	
    //        }
    //      }
    //    }
    for (int i = 0; i < services.length; i++)
    {
      Service s = (Service)services[i];
      parents.add(0, s);
      Object ports[] = s.getPorts().values().toArray();
      HashSet portInputs = new HashSet();
      HashSet portOutputs = new HashSet();
      for (int j = 0; j < ports.length; j++)
      {
        Port p = (Port)ports[j];
        parents.add(0, p);
        // a Port name must be unique within the entire WDSL document
        if (allPorts.contains(p.getName()))
        {
          valInfo.addError(messagegenerator.getString(_PORT_NAME_NOT_UNIQUE, QUOTE + p.getName() + QUOTE), p);
        }
        else
        {
          allPorts.put(p.getName(), p);

          // get the binding for this port and see if the PortType for the binding
          // is defined
          if (p.getBinding() == null || p.getBinding().isUndefined())
          {
            String bindingName = EMPTY_STRING;
            if (p.getBinding() != null)
            {
              bindingName = p.getBinding().getQName().getLocalPart();
            }
            valInfo.addError(
              messagegenerator.getString(_NO_BINDING_FOR_PORT, QUOTE + p.getName() + QUOTE, QUOTE + bindingName + QUOTE),
              p);
          }
          else
          {
            // TODO: Check that the output of one port isn't the input of another and vice versa
            // extensibility elements the port
            // there can only be one and must be one extensibility element defined for a port
            List extelems = p.getExtensibilityElements();
            if (extelems.size() < 1)
            {
              valInfo.addError(messagegenerator.getString(_NO_ADDRESS_PORT, QUOTE + p.getName() + QUOTE), p);
            }
            else if (extelems.size() > 1)
            {
              for (int k = 1; k < extelems.size(); k++)
              {
                valInfo.addError(
                  messagegenerator.getString(_MORE_THEN_ONE_ADDRESS_PORT, QUOTE + p.getName() + QUOTE),
                  extelems.get(k));
              }
            }
            validateExtensibilityElementList(parents, p.getExtensibilityElements(), valInfo);
          }
        }

        parents.remove(0);
      }
      // extensibility elements for the service
      validateExtensibilityElementList(parents, s.getExtensibilityElements(), valInfo);
      parents.remove(0);
    }
  }

  /**
   * Checks that the bindings refer to valid PortTypes and all of the operations
  // in a given binding refer to a defined operation within the corresponding
  // PortType.
   * 
   * @param wsdlDefinition The WSDL definitions element.
   */
  protected void validateBindings(Definition wsdlDefinition, WSDL11ValidationInfo valInfo)
  {
    if (wsdlDefinition.getBindings() == null)
      return;
    Object bindings[] = wsdlDefinition.getBindings().values().toArray();
    List parents = new Vector();
    parents.add(wsdlDefinition);
    for (int i = 0; i < bindings.length; i++)
    {
      Binding b = (Binding)bindings[i];
      parents.add(0, b);
      PortType portType = b.getPortType();

      if (portType == null)
      {
        continue;
      }
      // the PortType is not defined so don't bother checking the operations
      if (portType.isUndefined())
      {
        valInfo.addError(
          messagegenerator.getString(
            _PORTTYPE_UNDEFINED_FOR_BINDING,
            QUOTE + portType.getQName().getLocalPart() + QUOTE,
            QUOTE + b.getQName().getLocalPart() + QUOTE),
          b);
      }
      else
      {
        // the PortType is defined so now we have to check that the operations are defined
        Object bindingOperations[] = b.getBindingOperations().toArray();

        // check if the operation is defined for each BindingOperation
        for (int k = 0; k < bindingOperations.length; k++)
        {
          BindingOperation bo = (BindingOperation)bindingOperations[k];
          parents.add(0, bo);
          if (bo.getOperation() == null || bo.getOperation().isUndefined())
          {
            valInfo.addError(
              messagegenerator.getString(
                _OPERATION_UNDEFINED_FOR_PORTTYPE,
                QUOTE + b.getQName().getLocalPart() + QUOTE,
                QUOTE + portType.getQName().getLocalPart() + QUOTE),
              bo);
            // nice idea to add suggestions to other elements to fix the error
            // but it doesn't work with multipe files like this
            //addValidationMessage(warningList,portType,portType.getQName().getLocalPart() + "Define an operation here to correspond with the operation in: " + bo.getName());
          }
          // take care of all the extensibility elements in the binding operation, binding inputs, outputs and faults
          else
          {
            BindingInput binput = bo.getBindingInput();
            if (binput != null)
            {
              parents.add(0, binput);
              // extensibility elements for binding operation input
              validateExtensibilityElementList(
                parents,
                bo.getBindingInput().getExtensibilityElements(),
                valInfo);
              parents.remove(0);
            }
            BindingOutput boutput = bo.getBindingOutput();
            if (boutput != null)
            {
              parents.add(0, boutput);
              // extensibility elements for binding operation output
              validateExtensibilityElementList(
                parents,
                bo.getBindingOutput().getExtensibilityElements(),
                valInfo);
              parents.remove(0);
            }
            // no input or output has been defined for the operation
            if (binput == null && boutput == null)
            {
              valInfo.addError(
                messagegenerator.getString(_OPERATION_NO_INPUT_OR_OUTPUT, QUOTE + bo.getName() + QUOTE),
                bo);
            }
            // extensibility elements for each binding operation fault
            Iterator faults = bo.getBindingFaults().values().iterator();
            while (faults.hasNext())
            {
              BindingFault bf = (BindingFault)faults.next();
              parents.add(0, bf);
              validateExtensibilityElementList(parents, bf.getExtensibilityElements(), valInfo);
              parents.remove(0);
            }
          }
          // extensibility elements for binding operation
          validateExtensibilityElementList(parents, bo.getExtensibilityElements(), valInfo);
          parents.remove(0);
        }
      }
      // extensibility elements for the binding
      validateExtensibilityElementList(parents, b.getExtensibilityElements(), valInfo);
      parents.remove(0);
    }

  }

  /**
   * Check that all of the PortTypes have valid messages associated with their
  // operation input, output and fault types.
   * 
   * @param wsdlDefinition The WSDL definitions element.
   */
  protected void validatePortTypes(Definition wsdlDefinition, WSDL11ValidationInfo valInfo)
  {
    if (wsdlDefinition.getPortTypes() == null)
      return;
    Object porttypes[] = wsdlDefinition.getPortTypes().values().toArray();

    for (int i = 0; i < porttypes.length; i++)
    {
      PortType p = (PortType)porttypes[i];
      Object operations[] = p.getOperations().toArray();
      List inAndOutNames = new Vector();
      for (int j = 0; j < operations.length; j++)
      {
        Operation o = (Operation)operations[j];
        if (o == null || o.isUndefined())
        {
          continue;
        }

        // check that the messages are defined for the input, output and faults
        Message m;
        Input input = o.getInput();
        if (input != null)
        {
          String inputName = input.getName();
          // if the name isn't defined it defaults to this
          if (inputName == null)
          {
            inputName = o.getName() + REQUEST;
          }
          if (inAndOutNames.contains(inputName))
          {
            valInfo.addError(
              messagegenerator.getString(
                _INPUT_NAME_NOT_UNIQUE,
                QUOTE + inputName + QUOTE,
                QUOTE + p.getQName().getLocalPart() + QUOTE),
              input);
          }
          else
          {
            inAndOutNames.add(inputName);
          }

          m = input.getMessage();
          if (m != null && m.isUndefined())
          {
            String messName = EMPTY_STRING;
            QName messQName = m.getQName();
            if (messQName != null)
            {
              messName = messQName.getLocalPart();
            }
            valInfo.addError(messagegenerator.getString(_MESSAGE_UNDEFINED_FOR_INPUT, QUOTE + messName + QUOTE), input);
          }
        }
        Output output = o.getOutput();
        if (output != null)
        {
          String outputName = output.getName();
          // if the name isn't defined it defaults to this
          if (outputName == null)
          {
            outputName = o.getName() + RESPONSE;
          }

          if (inAndOutNames.contains(outputName))
          {
            valInfo.addError(
              messagegenerator.getString(
                _OUTPUT_NAME_NOT_UNIQUE,
                QUOTE + outputName + QUOTE,
                QUOTE + p.getQName().getLocalPart() + QUOTE),
              output);
          }
          else
          {
            inAndOutNames.add(outputName);
          }

          m = output.getMessage();
          if (m != null && m.isUndefined())
          {
            String messName = EMPTY_STRING;
            QName messQName = m.getQName();
            if (messQName != null)
            {
              messName = messQName.getLocalPart();
            }
            valInfo.addError(messagegenerator.getString(_MESSAGE_UNDEFINED_FOR_OUTPUT, QUOTE + messName + QUOTE), output);
          }
        }

        Object faults[] = o.getFaults().values().toArray();

        //List faultNames = new Vector();
        for (int k = 0; k < faults.length; k++)
        {
          Fault f = (Fault)faults[k];
          m = f.getMessage();
          if (m != null && m.isUndefined())
          {
            String messName = EMPTY_STRING;
            QName messQName = m.getQName();
            if (messQName != null)
            {
              messName = messQName.getLocalPart();
            }
            valInfo.addError(messagegenerator.getString(_MESSAGE_UNDEFINED_FOR_FAULT, QUOTE + messName + QUOTE), f);
          }
        }
      }
    }
  }

  /**
   * Check that all the messages defined in the WSDL document are valid.
   * 
   * @param wsdlDefinition The WSDL definitions element.
   */
  protected void validateMessages(Definition wsdlDefinition, WSDL11ValidationInfo valInfo)
  {
    if (wsdlDefinition.getMessages() == null)
      return;
    Iterator messages = wsdlDefinition.getMessages().values().iterator();

    while (messages.hasNext())
    {
      Message m = (Message)messages.next();
      if (!m.isUndefined())
      {
        // if the message has a part (and it doesn't have to)
        // ensure each message part has either an element or a type
        if (!m.getParts().isEmpty())
        {
          Iterator parts = m.getParts().values().iterator();
          while (parts.hasNext())
          {
            Part p = (Part)parts.next();
            QName elementName = p.getElementName();
            QName typeName = p.getTypeName();
            Map extAtts = p.getExtensionAttributes();
            // TODO:This will have to be extended as parts can have extensibility elements
            //ensure the part has a type or an element defined
            if (elementName == null && typeName == null && (extAtts == null || extAtts.isEmpty()))
            {
              valInfo.addError(messagegenerator.getString(_PART_NO_ELEMENT_OR_TYPE, QUOTE + p.getName() + QUOTE), p);
            }
            //here the part has both the element and type defined and it can only have one defined	
            else if (elementName != null && typeName != null)
            {
              valInfo.addError(messagegenerator.getString(_PART_BOTH_ELEMENT_AND_TYPE, QUOTE + p.getName() + QUOTE), p);
            }
            else if (elementName != null)
            {
              if (!checkPartConstituent(elementName.getNamespaceURI(),
                elementName.getLocalPart(),
                ELEMENT,
                p,
                valInfo))
              {
                valInfo.addError(
                  messagegenerator.getString(
                    _PART_INVALID_ELEMENT,
                    QUOTE + p.getName() + QUOTE,
                    QUOTE + elementName.getLocalPart() + QUOTE),
                  p);
              }
            }
            else if (typeName != null)
            {
              // check that the type itself is defined properly
              if (!checkPartConstituent(typeName.getNamespaceURI(), typeName.getLocalPart(), TYPE, p, valInfo))
              {
                valInfo.addError(
                  messagegenerator.getString(
                    _PART_INVALID_TYPE,
                    QUOTE + p.getName() + QUOTE,
                    QUOTE + typeName.getLocalPart() + QUOTE),
                  p);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Checks whether the given name is defined in the namespace for the part.  A part is an
   * ELEMENT or a TYPE.
   * 
   * @param namespace The namespace to check.
   * @param name The name to check.
   * @param part The part to check, either ELEMENT or TYPE.
   * @param partObject The object representing the given part.
   * @return True if the part element of type is defined, false otherwise.
   */
  protected boolean checkPartConstituent(
    String namespace,
    String name,
    int part,
    Part partObject,
    WSDL11ValidationInfo valInfo)
  {

    boolean partvalid = false;
    // First take care of the situation where it's from the schema namespace.
    // The 1999, 2000 and 2001 schema namespaces are all accepted.
    if (namespace.equals(Constants.NS_URI_XSD_2001)
      || namespace.equals(Constants.NS_URI_XSD_1999)
      || namespace.equals(Constants.NS_URI_XSD_2000))
    {
      SchemaAttributeTable xsdTable = new SchemaAttributeTable();
      if (xsdTable.containsSymbol(name))
      {
        partvalid = true;
      }
    }
    // check inline and imported schema
    else
    {
      XSModel[] schemas = valInfo.getSchemas();
      int numSchemas = schemas.length;
      //Iterator schemasIter = schemas.iterator();
      for (int i = 0; i < numSchemas; i++)
      {
        XSModel schema = schemas[i];
        if (schema != null)
        {
          if (part == ELEMENT && schema.getElementDeclaration(name, namespace) != null)
          {
            partvalid = true;
            break;
          }
          else if (part == TYPE && schema.getTypeDefinition(name, namespace) != null)
          {
            partvalid = true;
            break;
          }
        }
      }
    }
    // If the SOAP encoding namespace hasn't been explicitly imported do so
	// now.
    // Allow the SOAP encoding namespace to be automatically imported but mark
	// it as a warning.
    if (!partvalid && namespace.equals(SOAP_ENCODING_URI))
    {
      try
      {
        XSDValidator xsdVal = new XSDValidator();
        String soapEnc = valInfo.getURIResolver().resolve(null, SOAP_ENCODING_URI, null);
        if(soapEnc != null)
        {
          xsdVal.validate(soapEnc, null);
          // sanity check in case something goes wrong
          if (xsdVal.isValid())
          {
            XSModel xsModel = xsdVal.getXSModel();

            if (part == ELEMENT && xsModel.getElementDeclaration(name, namespace) != null)
            {
              partvalid = true;
            }
            else if (part == TYPE && xsModel.getTypeDefinition(name, namespace) != null)
            {
              partvalid = true;
            }
            valInfo.addWarning(messagegenerator.getString(_WARN_SOAPENC_IMPORTED_PART, QUOTE + name + QUOTE), partObject);
          }
        }
      }
      catch (Exception e)
      {
        //TODO: log the error message
        //System.out.println(e);
      }
    }
    return partvalid;
  }

}

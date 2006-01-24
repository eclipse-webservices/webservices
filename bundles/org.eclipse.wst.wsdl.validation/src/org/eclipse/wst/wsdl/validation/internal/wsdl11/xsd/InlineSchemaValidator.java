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

package org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.UnknownExtensibilityElement;

import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.wsdl.validation.internal.util.ErrorMessage;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.ibm.wsdl.Constants;

/**
 * Plugin validator for the WSDL Validation framework. Validates inline schema found in a WSDL document.
 */
public class InlineSchemaValidator implements IWSDL11Validator
{
  /**
   * Property to allow the WSDL validator to use the target namespace
   * defined on the WSDL document for schemas with no target namespace.
   * This property will allow clients who depend on this functionality
   * to continue to use it and should be removed post 1.5.
   */
  private boolean USE_WSDL_TARGETNAMESPACE = false;
  
  private final String _WARN_OLD_SCHEMA_NAMESPACE = "_WARN_OLD_SCHEMA_NAMESPACE";
  private final String _WARN_SOAPENC_IMPORTED_SCHEMA = "_WARN_SOAPENC_IMPORTED_SCHEMA";
  private final String EMPTY_STRING = "";
  private final String QUOTE = "'";
  MessageGenerator messagegenerator = null;
  
  public InlineSchemaValidator()
  {
	String useNS = System.getProperty("use.wsdl.targetnamespace");
	if(useNS != null && useNS.equals("true"))
	{
	  USE_WSDL_TARGETNAMESPACE = true;
	}
  }
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator#validate(java.lang.Object, java.util.List, org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo)
   */
  public void validate(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
  	List elements = new ArrayList();
    UnknownExtensibilityElement elem = (UnknownExtensibilityElement) element;
    Definition wsdlDefinition = (Definition) parents.get(parents.size() - 1);
    String baseLocation = wsdlDefinition.getDocumentBaseURI();
    // Add in the namespaces defined in the doc already that aren't defined locally in this schema.
    // There is no need to check for namespaces other then in the defintions and types elements as
    // inline schema can not have any other parents and must have there two parents.
    // First take care of the definitions element
    
    // create the inline schema string
	Element w3celement = elem.getElement();
	Hashtable parentnamespaces = getNamespaceDeclarationsFromParents(wsdlDefinition,w3celement);
	String targetNamespace = w3celement.getAttribute(Constants.ATTR_TARGET_NAMESPACE);
	// if the targetNamespace hasn't been defined for the schema use the 
	// targetNamespace of the definitions element
	if(USE_WSDL_TARGETNAMESPACE && (targetNamespace == null || targetNamespace.equals(EMPTY_STRING)))
	 {
	  targetNamespace = wsdlDefinition.getTargetNamespace();
	  w3celement.setAttribute(Constants.ATTR_TARGET_NAMESPACE,targetNamespace);
	}
	
    // If the namespace given is one of the old schema namespaces produce a warning.
	String namespace = w3celement.getNamespaceURI();
	if(namespace.equals(Constants.NS_URI_XSD_1999) || namespace.equals(Constants.NS_URI_XSD_2000))
	 {
	  valInfo.addWarning(
	      messagegenerator.getString(_WARN_OLD_SCHEMA_NAMESPACE, QUOTE + Constants.NS_URI_XSD_2001 + QUOTE), element);
	}
	
    // now create and call the validator for the inline schema
    XSDValidator schemav = new XSDValidator();
    
    //String fileLocation = new URL(validatormanager.getFilename()).getPath();
    InlineXSDResolver inlineEntityResolver =
      getEntityResolver(wsdlDefinition, (Types) parents.get(0), baseLocation, targetNamespace);
    //	add in the external XSD Catalog to resolve schemas offline
    XMLEntityResolverChain entityResolverChain = new XMLEntityResolverChain();
    entityResolverChain.addEntityResolver(inlineEntityResolver);
    entityResolverChain.addEntityResolver((XMLEntityResolver)valInfo.getURIResolver());
    //entityResolverChain.addEntityResolver(XMLCatalogResolver.getInstance());
    entityResolverChain.addEntityResolver(new FileEntityResolver());
	
    //	 Create the string representation of the inline schema. 
    String xsd = InlineSchemaGenerator.createXSDString(w3celement, elements, baseLocation, parentnamespaces, inlineEntityResolver.getInlineSchemaNSs()); 

	
    schemav.validateInlineSchema(xsd, targetNamespace, baseLocation, entityResolverChain, inlineEntityResolver, valInfo.getSchemaCache());
    
//	check if the SOAP Encoding namespace is required but not imported
		 if (InlineSchemaGenerator.soapEncodingRequiredNotImported(elem.getElement(), baseLocation, parentnamespaces))
		 {
		   valInfo.addWarning(messagegenerator.getString(_WARN_SOAPENC_IMPORTED_SCHEMA), element);
		 }

    // If the schema isn't valid add the error messages produced to valinfo.
    // Don't add the errors if they are located on another inline schema. These
	// will be reported when the other schema is validated.
		 
    if (!schemav.isValid())
    {
      Iterator errors = schemav.getErrors().iterator();
      while (errors.hasNext())
      {
        ErrorMessage err = (ErrorMessage) errors.next();
        String uri = err.getURI();
        int line = err.getErrorLine();
        String errmess = err.getErrorMessage();
        errmess = replaceNamespace(errmess, namespace);
        if(line > 0)
        {
          if(uri == null || uri.equals(valInfo.getFileURI()))
          {
			valInfo.addError(errmess, getObjectAtLine(line - 1, elements));
          }
		  else if(!inlineEntityResolver.isInlineSchema(uri) && !uri.equals(valInfo.getFileURI() + InlineXSDResolver.INLINE_SCHEMA_ID))
          {
            valInfo.addError(errmess, line, err.getErrorColumn(), uri);
          }
        }
		else if(uri != null && !inlineEntityResolver.isInlineSchema(uri) && !uri.equals(valInfo.getFileURI() + InlineXSDResolver.INLINE_SCHEMA_ID))
        {
	      valInfo.addError(errmess, 0,0, uri);
        }
      }
    }
    // if the schema is valid, assign the model to the validatormanager
    else
    {
      XSModel xsModel = schemav.getXSModel();
      valInfo.addSchema(xsModel);
    }
  }

  /**
   * Get an entity resolver that will resolve inline schemas.  Every inline schema is preregistered with
   * the resolver.
   * 
   * @param wsdlDefinition The WSDL definitions element.
   * @param types The types element.
   * @param referenceLocation The location of the file that contains this schema.
   * @param targetNamespace The targetNamespace of the schema.
   * @return An entity resolver that can resolve inline schemas.
   */
  protected InlineXSDResolver getEntityResolver(Definition wsdlDefinition, Types types, String referenceLocation, String targetNamespace)
  {
    InlineXSDResolver entityResolver = new InlineXSDResolver();
//    entityResolver.setReferenceLocation(referenceLocation);
    List schemas = types.getExtensibilityElements();
    if (schemas != null)
    {
      Iterator iSchemas = schemas.iterator();
	  Set namespaces = new TreeSet(); 
      while (iSchemas.hasNext()) 
      { 
        UnknownExtensibilityElement extElem = (UnknownExtensibilityElement) iSchemas.next(); 
        String thisNamespace = extElem.getElement().getAttribute(Constants.ATTR_TARGET_NAMESPACE); 
        if(thisNamespace != null) 
        { 
                namespaces.add(thisNamespace); 
        } 
      } 
      iSchemas = schemas.iterator(); 

      while (iSchemas.hasNext())
      {
        UnknownExtensibilityElement extElem = (UnknownExtensibilityElement) iSchemas.next();
        String thisNamespace = extElem.getElement().getAttribute(Constants.ATTR_TARGET_NAMESPACE);
        if (thisNamespace != null && !thisNamespace.equalsIgnoreCase(targetNamespace))
        {
        	
        	Element element = extElem.getElement();
			
//			create the inline schema string
			 //Element w3celement = elem.getElement();
			 Hashtable parentnamespaces = getNamespaceDeclarationsFromParents(wsdlDefinition,element);
			 String xsd = InlineSchemaGenerator.createXSDString(element, new ArrayList(), referenceLocation, parentnamespaces, namespaces);
        	//addNamespaceDeclarationsFromParents(wsdlDefinition,element);
          entityResolver.add(thisNamespace, xsd);
        }

      }
    }
    return entityResolver;
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.validator.IWSDL11Validator#setResourceBundle(java.util.ResourceBundle)
   */
  public void setResourceBundle(ResourceBundle rb)
  {
  	messagegenerator = new MessageGenerator(rb);
  }
  
  public void setMessageGenerator(MessageGenerator messgen)
  {
    messagegenerator = messgen;
  }

  /**
   * Get the namespace declarations as in the form 
   * xmlns="somenamespace"
   * from the definitions and types elements and add them to the schema element so the schema 
   * validator will have access to them.
   * 
   * @param wsdlDefinition The WSDL definitions element.
   * @param element The types element.
   * @return A hashtable with the namespace elements from the elements provided.
   */
  protected Hashtable getNamespaceDeclarationsFromParents(Definition wsdlDefinition, Element element)
  {
  	Hashtable nss = new Hashtable();
    Iterator nameSpaces = wsdlDefinition.getNamespaces().keySet().iterator();

    String XMLNS = Constants.ATTR_XMLNS;
    
    while (nameSpaces.hasNext())
    {
      String nsprefix = XMLNS;
      String ns = (String) nameSpaces.next();
      if (!ns.equalsIgnoreCase(""))
      {
        nsprefix += ":";
      }
      if (!element.hasAttribute(nsprefix + ns))
      {
      	nss.put(nsprefix + ns, wsdlDefinition.getNamespace(ns));
//        element.setAttribute(nsprefix + ns, wsdlDefinition.getNamespace(ns));
      }

    }
    // Next handle the parent types element
    NamedNodeMap atts = element.getParentNode().getAttributes();
    int attslength = atts.getLength();
    for (int i = 0; i < attslength; i++)
    {
      Node tempnode = atts.item(i);
      String nodename = tempnode.getNodeName();
      // if this is a namespace attribute
      if (nodename.indexOf(XMLNS) != -1)
      {
		nss.put(nodename,  tempnode.getNodeValue());
        //element.setAttribute(nodename, tempnode.getNodeValue());
      }
    }
    return nss;
  }
  
  /**
   * Given a line number for the schema returns the element found on that line. 
   * Useful for obtaining elements from schema Strings.
   * 
   * @param line The line number for the schema.
   * @param elements The list of elements to check.
   * @return The object located at the line or at line 0 if the line is invalid.
   */
   protected Object getObjectAtLine(int line, List elements)
   {
   	if(line < 0 || line >= elements.size())
   	{
   	  line = 0;
   	}
	return elements.get(line);
   }
   
  /**
   * Replace any instance of the 2001 schema namespace in the given message with
   * the given namespace.
   * 
   * @param message The message to replace the namespace in.
   * @param namespace The namespace used for replacement.
   * @return The message with the 2001 schema namespace replaced by the given namespace.
   */
  private String replaceNamespace(String message, String namespace)
   {
     String xsd2001 = Constants.NS_URI_XSD_2001;
     int start = message.indexOf(xsd2001);
     int end = start + xsd2001.length();
     if(start < 0)
     {
       return message;
     }
     String startString = message.substring(0,start);
     String endString = message.substring(end,message.length());
     return startString + namespace + endString;
   }
}

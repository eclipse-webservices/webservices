/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.commands;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSchema;



public final class AddXSDSchemaCommand extends WSDLElementCommand
{
  private Definition definition;
  private XSDSchemaExtensibilityElement extensibilityElement;
  private String targetNamespace;
  
  public AddXSDSchemaCommand(Definition definition)
  {
    this.definition = definition;
    this.targetNamespace = definition.getTargetNamespace();
  }
  
  public AddXSDSchemaCommand(Definition definition, String targetNamespace)
  {
    this.definition = definition;
    this.targetNamespace = targetNamespace;
  }
  
  public void run()
  {    
    extensibilityElement = WSDLFactory.eINSTANCE.createXSDSchemaExtensibilityElement();
    extensibilityElement.setEnclosingDefinition(definition);
 
    XSDSchema xsdSchema = XSDFactory.eINSTANCE.createXSDSchema();
    xsdSchema.setSchemaForSchemaQNamePrefix("xsd"); //$NON-NLS-1$
    xsdSchema.setTargetNamespace(targetNamespace);
    // Choose the prefix Choose the prefix used for this schema's namespace and the schema for schema's namespace.
    //    
    java.util.Map qNamePrefixToNamespaceMap = xsdSchema.getQNamePrefixToNamespaceMap();
//    qNamePrefixToNamespaceMap.put("p", xsdSchema.getTargetNamespace());
    qNamePrefixToNamespaceMap.put
	  (xsdSchema.getSchemaForSchemaQNamePrefix(),org.eclipse.xsd.util.XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001);
    extensibilityElement.setSchema(xsdSchema);
       
    // TBD - Check if multiple schemas with the same targetNamespace can co-exist.
    Types types = getTypes();
    types.addExtensibilityElement(extensibilityElement);
    
    if (definition.getETypes() == null) {
    	definition.setETypes(types);
    }
  }

  private Types getTypes()
  {
    // Types is created if it does not exist.
    Types types = (Types)definition.getTypes();
    if (types != null)
      return types;
    else
    {
        types = WSDLFactory.eINSTANCE.createTypes();
        types.setEnclosingDefinition(definition);
        return types;
    }
  }
  
  public WSDLElement getWSDLElement()
  {
    return extensibilityElement;
  }
}

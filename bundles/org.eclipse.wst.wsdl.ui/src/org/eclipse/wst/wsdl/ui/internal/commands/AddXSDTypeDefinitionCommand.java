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
package org.eclipse.wst.wsdl.ui.internal.commands;

import java.util.Iterator;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSchema;

// This class is used to create a new XSDTypeDefinition in the targetNamespace.
public final class AddXSDTypeDefinitionCommand extends WSDLElementCommand
{
  private Definition definition;
  private XSDSchemaExtensibilityElement extensibilityElement;
  private String targetNamespace;
  private String typeName;
  
  public AddXSDTypeDefinitionCommand(Definition definition, String typeName)
  {
    this.definition = definition;
    this.targetNamespace = definition.getTargetNamespace();
    this.typeName = typeName;
  }
  
  public AddXSDTypeDefinitionCommand
    (Definition definition, 
     String targetNamespace,
     String typeName)
  {
    this.definition = definition;
    this.targetNamespace = targetNamespace;
    this.typeName = typeName;
  }
  
  public void run()
  {
    XSDSchema xsdSchema = getXSDSchema(targetNamespace);
    XSDComplexTypeDefinition typeDef = XSDFactory.eINSTANCE.createXSDComplexTypeDefinition();
    typeDef.setName(typeName);
    xsdSchema.getTypeDefinitions().add(typeDef);
    xsdSchema.getContents().add(typeDef);
  }

  private XSDSchema getXSDSchema(String targetNamespace)
  {
    XSDSchema xsdSchema;
    
    // Do we have a schema already?
    Iterator iterator = getTypes().getSchemas(targetNamespace).iterator();
    if (iterator.hasNext())
    {
      xsdSchema = (XSDSchema)iterator.next(); // Get the first one.
      return xsdSchema;
    }
    else
    {	    
      // Create a new schema because there's none.
      AddXSDSchemaCommand command = new AddXSDSchemaCommand(definition,targetNamespace);
      command.run();
      return ((XSDSchemaExtensibilityElement)command.getWSDLElement()).getSchema();
    }
  }
  
  private Types getTypes()
  {
    // Types is created if it does not exist yet.
    Types types = (Types)definition.getTypes();
    if (types != null)
      return types;
    else
    {
      AddTypesCommand command = new AddTypesCommand(definition);
      command.run();
      return (Types)command.getWSDLElement();
    }
  }
  
  public WSDLElement getWSDLElement()
  {
    return null;
  }
}
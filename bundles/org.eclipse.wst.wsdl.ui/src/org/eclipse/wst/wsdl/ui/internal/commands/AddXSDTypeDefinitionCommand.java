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
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;

// This class is used to create a new XSDTypeDefinition in the targetNamespace.
public final class AddXSDTypeDefinitionCommand extends WSDLElementCommand
{
  private Definition definition;
  private XSDSchemaExtensibilityElement extensibilityElement;
  private String targetNamespace;
  private String typeName;
  private XSDSchema schema;
  private boolean isComplexType = true;
  
  /**
   * TODO:
   * We have a potential problem here....  What if the definition targetnamespace is null....
   */
  public AddXSDTypeDefinitionCommand(Definition definition, String typeName)
  {
    this.definition = definition;
    this.targetNamespace = definition.getTargetNamespace();
    this.typeName = typeName;
  }
  
  public AddXSDTypeDefinitionCommand(Definition definition, String typeName, boolean isComplexType)
  {
  	this(definition, typeName);
  	this.isComplexType = isComplexType;
  }
  
  /**
   * @deprecated
   * Use AddXSDTypeDefinitionCommand(Definition definition, String typeName)
   */
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
    XSDSchema xsdSchema = getSchema();
    XSDNamedComponent typeDef;
    
    if (isComplexType) {
    	typeDef = XSDFactory.eINSTANCE.createXSDComplexTypeDefinition();
    }
    else {
    	typeDef = XSDFactory.eINSTANCE.createXSDSimpleTypeDefinition();
    }
    
    typeDef.setName(typeName);
    xsdSchema.getContents().add(typeDef);
  }
  
  public void run(String newTypeName) {
  	typeName = newTypeName;
  	run();
  }
  
  /*
   * Specifiy if we should create a complex or simple type.  This should should be
   * called before run().
   * Overrides the value set when the constructor
   * AddXSDTypeDefinitionCommand(Definition definition, String typeName, boolean isComplexType)
   * is used.
   */
  public void isComplexType(boolean isComplexType) {
  	this.isComplexType = isComplexType;
  }
  
  /*
   * Specify which Schema to use when creating the Type.
   * Call this method before calling run().  Otherwise it will use the first
   * Schema it finds.
   */
  public void setSchema(XSDSchema schema) {
  	this.schema = schema;
  }
  
  /*
   * Return the Schema used to create the Type
   */
  public XSDSchema getSchema() {
  	if (schema == null) {
  		return getXSDSchema(targetNamespace);
  	}
  	else {
  		return schema;
  	}
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
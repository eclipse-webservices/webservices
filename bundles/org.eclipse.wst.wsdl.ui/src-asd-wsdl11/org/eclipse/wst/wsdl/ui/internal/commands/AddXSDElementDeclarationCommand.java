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

import java.util.Iterator;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;


// This class is used to create a new XSDElementDeclaration in the targetNamespace.
public final class AddXSDElementDeclarationCommand extends WSDLElementCommand
{
  private Definition definition;
  private String targetNamespace;
  private String elementName;
  private XSDSchema schema;
  private XSDElementDeclaration xsdElement;
  
  /**
   * TODO:
   * We have a potential problem here....  What if the definition targetnamespace is null....
   */
  public AddXSDElementDeclarationCommand(Definition definition, String elementName)
  {
    this.definition = definition;
    this.targetNamespace = definition.getTargetNamespace();
    this.elementName = elementName;
  }
  
  /**
   * @deprecated
   * Use AddXSDElementDeclarationCommand(Definition definition, String typeName)
   */
  public AddXSDElementDeclarationCommand
    (Definition definition, 
     String targetNamespace, 
     String elementName)
  {
    this.definition = definition;
    this.targetNamespace = targetNamespace;
    this.elementName = elementName;
  }

  public void run()
  {
    XSDSchema xsdSchema = getSchema();
    XSDElementDeclaration elementDecl = 
      XSDFactory.eINSTANCE.createXSDElementDeclaration();
    elementDecl.setName(elementName);
    
    XSDSimpleTypeDefinition simpleTypeDefinition = getXSDStringType(xsdSchema);
    elementDecl.setTypeDefinition(simpleTypeDefinition);    
    xsdSchema.getContents().add(elementDecl);
    
    xsdElement = elementDecl;
  }
  
  public void run(String newElementName) {
  	elementName = newElementName;
  	run();
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
  
  private XSDSimpleTypeDefinition getXSDStringType(XSDSchema schema)
  {
    return schema.getSchemaForSchema().resolveSimpleTypeDefinition("string");
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
  
  public XSDElementDeclaration getXSDElement()
  {
	  return xsdElement;
  }
}
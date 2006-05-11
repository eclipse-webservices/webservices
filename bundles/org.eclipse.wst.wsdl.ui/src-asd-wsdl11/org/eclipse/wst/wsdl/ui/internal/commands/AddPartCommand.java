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
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.PartImpl;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;


public final class AddPartCommand extends WSDLElementCommand
{
  private Message message;
  private String name;
  private Part part;
  private Part originalPart;
  
  private QName typeOrElementName;
  private boolean useType = true;

  public AddPartCommand
		(Message message,
		 String name)
	{
	  this.message = message;
	  this.name = name;
	}
  
  public AddPartCommand
		(Message message,
		 String name,
		 String namespace,
		 String localName,
		 boolean useType)
	{
	  this.message = message;
	  this.name = name;
	  this.useType = useType;
	  typeOrElementName = new QName(namespace,localName);
	}
  
  /*
   * Constructor used to create a Part based on the given Part.  In essence, this will
   * create a copy of the given Part.
   */
  public AddPartCommand
	(Message message,
	 Part originalPart,
	 String name)
  {
  	this.message = message;
  	this.name = name;
  	this.originalPart = originalPart;
  }
  
  /**
   * @deprecated
   */
  public AddPartCommand
		(Message message,
		 String name,
		 String namespace,
		 String localName)
	{
	  this.message = message;
	  this.name = name;
	  typeOrElementName = new QName(namespace,localName);
	}
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public WSDLElement getWSDLElement()
  {
    return part;
  }
  
  public void run()
  {
    if (typeOrElementName == null)
      typeOrElementName = createDefaultTypeName();
    
    // Add namespace to the Definitions if it is not there yet.
    addNamespaceDeclaration();
    
    part = WSDLFactory.eINSTANCE.createPart();
    part.setName(name);
    
    if (originalPart == null) {
    	if (useType)
    		part.setTypeName(typeOrElementName);
    	else
    		part.setElementName(typeOrElementName);
    }
    else {
    	if (originalPart.getTypeName() != null)
    		part.setTypeName(originalPart.getTypeName());

    	String newElementName = null;
    	if (originalPart.getElementName() != null) {
    		
    		boolean createNewElement = true;
    		if (originalPart.getElementDeclaration() != null && originalPart.getElementDeclaration().getElement() != null) {
        		if (!(originalPart.getElementDeclaration().getElement() instanceof IDOMNode)) {
        			createNewElement = false;
        		}        		
    		}
    		
    		if (createNewElement) {
    			newElementName = getNewNameHelper(name, originalPart.getEnclosingDefinition(), false);
    			part.setElementName(new QName(originalPart.getElementName().getNamespaceURI(), newElementName));
	
    			if (originalPart.getEnclosingDefinition() != null) {
    				if (newElementName == null) {
    					newElementName = getNewNameHelper(name, originalPart.getEnclosingDefinition(), false);
    				}
    		
    				AddXSDElementDeclarationCommand elementAction = new AddXSDElementDeclarationCommand(originalPart.getEnclosingDefinition(), newElementName);
    				elementAction.run();
    			}
    		}
    		else {
    			part.setElementName(new QName(originalPart.getElementName().getNamespaceURI(), originalPart.getElementName().getLocalPart()));
    		}
    	}
    }
    	
    part.setEnclosingDefinition(message.getEnclosingDefinition());
    ((PartImpl)part).reconcileReferences(false); 
    message.addPart(part);
  }
  
  private void addNamespaceDeclaration()
  {
    Definition definition = message.getEnclosingDefinition();
    if (!definition.getNamespaces().containsValue(typeOrElementName.getNamespaceURI()))
    {
      definition.addNamespace("p",typeOrElementName.getNamespaceURI()); //$NON-NLS-1$
//      ((WSDLElementImpl)definition).updateElement(false);
    }
  }
  
  private QName createDefaultTypeName()
  {
    QName qname = new QName(WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001,"string"); //$NON-NLS-1$
    return qname;
  }
  
  private String getNewNameHelper(String base, Definition def, boolean isType)
  { 
    String name = base;    
    int count = 0;

    // Ugly....  Redo this...
    // Get a list of Elements...
    List elementList = null;
    if (def.getETypes() != null) {
    	List xsdsList = def.getETypes().getEExtensibilityElements();
    	if (xsdsList != null) {
    		Iterator xsdsIterator = xsdsList.iterator();
    		XSDSchemaExtensibilityElement xsdElement = (XSDSchemaExtensibilityElement) xsdsIterator.next();
    		XSDSchema schema = xsdElement.getSchema();
    		if (schema != null) {
    			elementList = schema.getElementDeclarations();
    		}
    	}
    }
    
    if (elementList != null) {
    	int index = 0;
    	while (index < elementList.size()) {
    		XSDElementDeclaration elementDeclaration = (XSDElementDeclaration) elementList.get(index);
    		
    		if (name.equals(elementDeclaration.getName())) {
    			count++;
    			name = name + count;
    			index = 0;
    		}
    		else {
    			index++;
    		}
    	}
    }

    return name;
  }
}

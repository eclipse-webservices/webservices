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
package org.eclipse.wst.wsdl.ui.internal.xsd.actions;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementAction;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AddSchemaAction extends AddElementAction
{
  protected String targetNamespace;
  protected boolean createTypesElement = false;

  public AddSchemaAction(Definition definition, Element definitionElement)
  {
    this(definition, definitionElement, false);
  }

  public AddSchemaAction(Definition definition, Element definitionElement, boolean createTypesElement)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_SCHEMA"), "icons/xsd_obj.gif", definitionElement, "xsd:schema");                       
    this.createTypesElement = createTypesElement;
    targetNamespace = definition.getTargetNamespace();    
  }

  public AddSchemaAction(Definition definition, Element definitionElement, Element typesElement)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_SCHEMA"), "icons/xsd_obj.gif", definitionElement, "xsd:schema");                       
    if (typesElement != null)
    {
      parentNode =  typesElement;
      this.createTypesElement = false;
    }
    else
    {
      this.createTypesElement = true;      
    }
    targetNamespace = definition.getTargetNamespace();    
  }

  public AddSchemaAction(Definition definition, Element definitionElement, Element typesElement, Document document)
  {
  	this(definition, definitionElement, typesElement);
  	this.definition = definition;
  	this.document = document;
  }
  
  protected boolean showDialog()
  {
    return true;
  }

  protected void addAttributes(Element newElement)
  {                                               
    newElement.setAttribute("xmlns:xsd", XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001);
    newElement.setAttribute("elementFormDefault", "qualified");
    newElement.setAttribute("targetNamespace", targetNamespace);
  }

  public void run()
  {
  	beginRecording();
  	if (parentNode == null || (document != null && document.getChildNodes().getLength() == 0)) {
  		createDefinitionStub();
  		prefix = null;
  		targetNamespace = definition.getTargetNamespace();
  	}
  	
    if (createTypesElement)
    {
      AddElementAction addTypesAction = new AddElementAction("", "icons/xsd_obj.gif", parentNode, parentNode.getPrefix(), "types");
      addTypesAction.setComputeTopLevelRefChild(true);
      addTypesAction.run();
      parentNode =  addTypesAction.getNewElement();
    } 
    super.run();
    endRecording();
  }     
}

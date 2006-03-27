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
package org.eclipse.wst.wsdl.ui.internal.actions; 

import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.InternalWSDLMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AddImportAction extends AddElementAction
{                              
  protected String namespace;
  protected String location;
  protected String elementDeclarationNamespacePrefix;

  public AddImportAction(IEditorPart part, Definition definition, Node parentNode, String prefix)
  {
	this(part, definition, parentNode, prefix, null, null);
  }
  
  public AddImportAction(IEditorPart part, Definition definition, Node parentNode, String prefix, String namespace, String location)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_IMPORT"), "icons/import_obj.gif", parentNode, prefix, "import");
	  setDefinition(definition);
	  setComputeTopLevelRefChild(true);
	  this.namespace = namespace;
	  this.location = location;
	  setEditorPart(part);
	  
	  if (part instanceof InternalWSDLMultiPageEditor) {
	  	document = ((IDOMModel) ((InternalWSDLMultiPageEditor) part).getModel()).getDocument();
	  }
  }
        
  protected Element createElement(String nodeName)
  {
  	if (elementDeclarationNamespacePrefix != null && namespace != null)
  	{ 
    	Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
    	if (definitionElement != null)
    	{    
	  	  definitionElement.setAttribute("xmlns:" + elementDeclarationNamespacePrefix, namespace);
    	}      	
  	}
  	return super.createElement(nodeName);  
  }
  
  public void setElementDeclarationNamespacePrefix(String nsPrefix)
  {
  	this.elementDeclarationNamespacePrefix = nsPrefix;
  }
                   
  protected void addAttributes(Element newElement)
  {                                              
    newElement.setAttribute("namespace", namespace != null ? namespace : "");
    newElement.setAttribute("location", location != null ? location : "");
  }                                     
  
  public void performAddElement() {
  	if (parentNode == null || (document != null && document.getChildNodes().getLength() == 0)) {
  		createDefinitionStub();
  	}
  	
  	super.performAddElement();
  }
}
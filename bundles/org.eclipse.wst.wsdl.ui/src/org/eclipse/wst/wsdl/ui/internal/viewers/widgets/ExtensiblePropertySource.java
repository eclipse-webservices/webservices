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
package org.eclipse.wst.wsdl.ui.internal.viewers.widgets;


import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.w3c.dom.Element;

import org.eclipse.wst.common.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.common.contentmodel.CMDocument;

import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.properties.section.IPropertyDescriptorProvider;
import org.eclipse.wst.xml.ui.views.properties.XMLPropertySourceAdapter;
import org.eclipse.wst.sse.core.INodeNotifier;

public class ExtensiblePropertySource extends XMLPropertySourceAdapter
{
	protected IEditorPart editorPart;
			
	public ExtensiblePropertySource(IEditorPart editorPart, INodeNotifier target) 
	{
		super(target);
		this.editorPart = editorPart;
  	}
  	
  	
  	    
	protected IPropertyDescriptor createPropertyDescriptor(CMAttributeDeclaration attrDecl) 
	{
		IPropertyDescriptor result = null;		
		CMDocument cmDocument = (CMDocument)attrDecl.getProperty("CMDocument");
		if (cmDocument != null)
		{
			String namespaceURI = (String)cmDocument.getProperty("http://org.eclipse.wst/cm/properties/targetNamespaceURI");   
			if (namespaceURI != null)
			{
				IPropertyDescriptorProvider provider = (IPropertyDescriptorProvider)WSDLEditorPlugin.getInstance().getPropertyDescriptorProviderRegistry().get(namespaceURI);
				if (provider != null)
				{
				  result = provider.getPropertyDescriptor(editorPart, (Element)fNode, namespaceURI, attrDecl.getNodeName());
				}
			}
		}
		if (result == null)
		{
		  result = super.createPropertyDescriptor(attrDecl);
		}
		return result;			
	}
	
	
	// TODO... I've asked Nitin to consider changing a couple of lines in
	// DOMPropertySource (to handle valueString == null).  With this change
	// the method below would not be needed.
// KC: Looks like he made the change in the sse version
//
//	public void setPropertyValue(Object nameObject, Object value) {
//		// Avoid cycling - can happen if a closing cell editor causes a refresh
//		// on the PropertySheet page and the setInput again asks the editor to
//		// close; besides, why apply the same value twice?
//		if (!settingObjects.isEmpty() && settingObjects.peek() == nameObject)
//			return;
//		settingObjects.push(nameObject);
//		String name = nameObject.toString();
//		String valueString = null;
//		if (value != null)
//			valueString = value.toString();
//		NamedNodeMap attrMap = fNode.getAttributes();
//		try {
//			if (attrMap != null) {
//				Attr attr = (Attr) attrMap.getNamedItem(name);
//				if (attr != null) {
//					// EXISTING VALUE
//					// potential out of control loop if updating the value triggers a viewer update, forcing the
//					// active cell editor to save its value and causing the loop to continue
//					
//					if (attr.getValue() == null || !attr.getValue().equals(valueString)) {
//						if (valueString == null)
//						{
//						  attr.getOwnerElement().removeAttributeNode(attr);
//						}
//						else
//						{
//						
//						if (attr instanceof XMLNode)
//							 ((XMLNode) attr).setValueSource(valueString);
//							 
//						else
//							attr.setValue(valueString);
//						}	
//					}
//					
//				}
//				else {
//					// NEW(?) value
//					if (value != null) { // never create an empty attribute
//						Attr newAttr = fNode.getOwnerDocument().createAttribute(name);
//						if (newAttr instanceof XMLNode)
//							 ((XMLNode) newAttr).setValueSource(valueString);
//						else
//							newAttr.setValue(valueString);
//						attrMap.setNamedItem(newAttr);
//					}
//				}
//			}
//			else {
//				if (fNode instanceof Element) {
//					((Element) fNode).setAttribute(name, valueString);
//				}
//			}
//		}
//		catch (DOMException e) {
//			Display.getCurrent().beep();
//		}
//		settingObjects.pop();
//	}	
}

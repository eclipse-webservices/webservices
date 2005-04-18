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
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.properties.section.IPropertyDescriptorProvider;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.ui.internal.properties.XMLPropertySourceAdapter;
import org.w3c.dom.Element;

public class ExtensiblePropertySource extends XMLPropertySourceAdapter
{
	protected IEditorPart editorPart;
	private INodeNotifier nodeNotifier;
			
	public ExtensiblePropertySource(IEditorPart editorPart, INodeNotifier target) 
	{
		super(target);
		this.editorPart = editorPart;
		this.nodeNotifier = target;
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
				  result = provider.getPropertyDescriptor(editorPart, (Element) nodeNotifier, namespaceURI, attrDecl.getNodeName());
				}
			}
		}
		if (result == null)
		{
		  result = super.createPropertyDescriptor(attrDecl);
		}
		return result;			
	}
}

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

import org.eclipse.jface.action.Action;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;

public class AddElementDeclarationAction extends Action
{
	protected Definition definition;
	protected String namespace;
	protected String prefix;
	
	public AddElementDeclarationAction(Definition definition, String namespace, String suggestedPrefix)
	{
		this.definition = definition;
		this.namespace = namespace;
		this.prefix = suggestedPrefix;
	}
	
	public void run()
	{
		String existingPrefix = definition.getPrefix(namespace);
		if (existingPrefix != null)
		{
			prefix = existingPrefix;
		}
		else
		{
			prefix = NameUtil.buildUniquePrefix(definition, prefix);
			Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
			if (definitionElement != null)
			{
				definitionElement.setAttribute("xmlns:" + prefix, namespace);
			}			
		}		
	}
	
	public String getPrefix()
	{
		return prefix;
	}
}

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
package org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

/**
 * Wrapper for InlineSchemaGenerator to allow testing protected methods.
 */
public class InlineSchemaGeneratorWrapper extends InlineSchemaGenerator 
{

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#checkSOAPEncodingRequired(java.util.List)
	 */
	protected boolean checkSOAPEncodingRequired(List reqns) 
	{
		return super.checkSOAPEncodingRequired(reqns);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#createXSDStringRecursively(org.w3c.dom.Element, java.util.List, java.util.List, java.util.Hashtable, java.lang.String)
	 */
	protected String createXSDStringRecursively(Element elem, List elements, List requiredNamespaces, Hashtable reqNSDecl, String filelocation) 
	{
		return super.createXSDStringRecursively(elem, elements, requiredNamespaces,
				reqNSDecl, filelocation);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#getImportNamespaces(org.w3c.dom.Element)
	 */
	protected List getImportNamespaces(Element elem) 
	{
		return super.getImportNamespaces(elem);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#getNSResolver(org.w3c.dom.Element)
	 */
	protected Hashtable getNSResolver(Element elem) 
	{
		return super.getNSResolver(elem);
	}

    /* (non-Javadoc)
     * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#getNamespacePrefixes(org.w3c.dom.Element)
     */
    protected List getNamespacePrefixes(Element elem) 
    {
	    return super.getNamespacePrefixes(elem);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#removeImports(java.util.List, java.util.List)
	 */
	protected List removeImports(List namespaces, List importedNamespaces) 
	{
		return super.removeImports(namespaces, importedNamespaces);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#removeLocalNamespaces(java.util.List, org.w3c.dom.Element)
	 */
	protected List removeLocalNamespaces(List namespaces, Element elem) 
	{
		return super.removeLocalNamespaces(namespaces, elem);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#resolveNamespaces(java.util.List, java.util.Hashtable, java.util.Hashtable)
	 */
	protected Hashtable resolveNamespaces(List namespaces, Hashtable nsResolver, Hashtable parentNSResolver) 
	{
		return super.resolveNamespaces(namespaces, nsResolver, parentNSResolver);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator#restrictImports(java.util.List, java.util.Set)
	 */
	protected List restrictImports(List namespaces, Set validImportNSs) 
	{
		return super.restrictImports(namespaces, validImportNSs);
	}
   
}

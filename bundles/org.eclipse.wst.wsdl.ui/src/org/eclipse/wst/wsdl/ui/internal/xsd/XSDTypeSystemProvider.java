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
package org.eclipse.wst.wsdl.ui.internal.xsd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.extension.ITypeSystemProvider;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.eclipse.wst.xsd.ui.internal.util.XSDDOMHelper;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.impl.XSDImportImpl;

//
//
public class XSDTypeSystemProvider implements ITypeSystemProvider
{
    public List getPrefixes(Definition definition, String namespace)
    {
        List list = new ArrayList();
        Map map = definition.getNamespaces();
        for (Iterator i = map.keySet().iterator(); i.hasNext();)
        {
            String prefix = (String) i.next();
            String theNamespace = (String) map.get(prefix);
            if (theNamespace != null && theNamespace.equals(namespace))
            {
                list.add(prefix);
            }
        }
        return list;
    }

    public List getPrefixedNames(Definition definition, String namespace, String localName)
    {
        List list = new ArrayList();
        for (Iterator i = getPrefixes(definition, namespace).iterator(); i.hasNext();)
        {
            String prefix = (String) i.next();
            String name = (prefix != null && prefix.length() > 0) ? prefix + ":" + localName : localName;
            list.add(name);
        }
        return list;
    }

    protected void addNamedComponents(Definition definition, List list, List namedComponents)
    {
        for (Iterator i = namedComponents.iterator(); i.hasNext();)
        {
            XSDNamedComponent component = (XSDNamedComponent) i.next();
            list.addAll(getPrefixedNames(definition, component.getTargetNamespace(), component.getName()));
        }
    }

    public void initWSIStyleImports(XSDSchema xsdSchema)
    {
        if (xsdSchema.getTargetNamespace() == null)
        {
            for (Iterator i = xsdSchema.getContents().iterator(); i.hasNext();)
            {
                XSDSchemaContent content = (XSDSchemaContent) i.next();
                if (content instanceof XSDImportImpl)
                {
                    XSDImportImpl xsdImport = (XSDImportImpl) content;
					XSDSchema schema = (XSDSchema)xsdImport.getResolvedSchema();					
					if (schema == null)
					{
						schema = xsdImport.importSchema();
					}             
                }
            }
        }
    }

    public List getAvailableTypeNames(Definition definition, int typeNameCategory)
    {
        List list = new ArrayList();
        
        if (typeNameCategory == ITypeSystemProvider.BUILT_IN_TYPE) {
        	list = getBuiltInTypeNamesList(definition);        	
        }
        else {
        	Types types = definition.getETypes();
        	if (types != null)
        	{
        		for (Iterator i = types.getEExtensibilityElements().iterator(); i.hasNext();)
        		{
        			Object o = i.next();
        			if (o instanceof XSDSchemaExtensibilityElement)
        			{
        				XSDSchema schema = ((XSDSchemaExtensibilityElement) o).getSchema();
        				if (schema != null)
        				{
        					initWSIStyleImports(schema);
        					addNamedComponents(definition, list, schema.getTypeDefinitions());
        				}
        			}
        		}
        	}
        
        	for (Iterator i = definition.getEImports().iterator(); i.hasNext();)
        	{
        		Import theImport = (Import) i.next();
        		XSDSchema schema = theImport.getESchema();
        		if (schema != null)
        		{
        			addNamedComponents(definition, list, schema.getTypeDefinitions());
        		}
        	}
        	list.addAll(getBuiltInTypeNamesList(definition));
        }
        
		return list;        
    }
    
    public List getAvailableTypes(Definition definition, XSDSchema schema, int typeNameCategory)
    {
        List list = new ArrayList();
        List keepTypes = new ArrayList();
        Iterator typeIterator = schema.getTypeDefinitions().iterator();
        // Filter out unwanted Types
        if (typeNameCategory == ITypeSystemProvider.USER_DEFINED_COMPLEX_TYPE) {
        	while (typeIterator.hasNext()) {
        		Object type = typeIterator.next();
        		if (type instanceof XSDComplexTypeDefinition) {
        			keepTypes.add(type);
        		}
        	}
        }
        else if (typeNameCategory == ITypeSystemProvider.USER_DEFINED_SIMPLE_TYPE) {
        	while (typeIterator.hasNext()) {
        		Object type = typeIterator.next();
        		if (type instanceof XSDSimpleTypeDefinition) {
        			keepTypes.add(type);
        		}
        	}
        }
        
        //addNamedComponents(definition, list, keepTypes);        
        //return list;
        return keepTypes;
    }

    public java.util.List getBuiltInTypeNamesList(Definition definition)
    {
        List items = new ArrayList();
        if (definition != null)
        {
            List prefixes = getPrefixes(definition, WSDLConstants.XSD_NAMESPACE_URI);
            for (Iterator i = prefixes.iterator(); i.hasNext();)
            {
                String prefix = (String) i.next();
                for (int j = 0; j < XSDDOMHelper.dataType.length; j++)
                {
                    String localName = XSDDOMHelper.dataType[j][0];
                    String name = (prefix != null && prefix.length() > 0) ? prefix + ":" + localName : localName;
                    items.add(name);
                }
            }
        }
        return items;
    }
    public List getAvailableElementNames(Definition definition)
    {
        List list = new ArrayList();
        Types types = definition.getETypes();
        if (types != null)
        {
            for (Iterator i = types.getEExtensibilityElements().iterator(); i.hasNext();)
            {
                Object o = i.next();
                if (o instanceof XSDSchemaExtensibilityElement)
                {
                    XSDSchema schema = ((XSDSchemaExtensibilityElement) o).getSchema();
                    if (schema != null)
                    {
						initWSIStyleImports(schema);
                        addNamedComponents(definition, list, schema.getElementDeclarations());
                    }
                }
            }
        }
        
        for (Iterator i = definition.getEImports().iterator(); i.hasNext();)
        {
            Import theImport = (Import) i.next();
            XSDSchema schema = theImport.getESchema();
            if (schema != null)
            {
                addNamedComponents(definition, list, schema.getElementDeclarations());
            }
        }
        return list;
    }
    public int getCategoryForTypeName(Definition definition, String typeName)
    {
        return 0;
    }
}
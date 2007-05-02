/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.common.uriresolver.internal.util.URIHelper;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.ImportImpl;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementDeclarationAction;
import org.eclipse.wst.wsdl.ui.internal.actions.AddImportAction;
import org.eclipse.wst.wsdl.ui.internal.actions.AddWSISchemaImportAction;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaContent;

public class WSDLSetComponentHelper {
    private Definition definition;
    private IFile currentIFile;
    
    public WSDLSetComponentHelper(IFile iFile, Definition definition) {
        currentIFile = iFile;
        this.definition = definition;
    }
    
    public void setWSDLComponent(WSDLElement inputElement, String property, ComponentSpecification spec) {
        addImportIfNecessary(spec);
        String componentObject = getPrefixedComponentName(spec);

        org.w3c.dom.Element wsdlElement = inputElement.getElement();
        
        wsdlElement.setAttribute(property, componentObject); //$NON-NLS-1$
    }
    
    public void setXSDTypeComponent(Part part, ComponentSpecification spec) {
        if (spec.getName() != null && spec.getQualifier() != null) {
            addImportIfNecessary(spec);
        }
        String componentObject = getPrefixedComponentName(spec);
        
        ComponentReferenceUtil.setComponentReference((Part) part, true, componentObject);
    }
    
    public void setXSDElementComponent(Part part, ComponentSpecification spec) {
        addImportIfNecessary(spec);
        String componentObject = getPrefixedComponentName(spec);
        
        ComponentReferenceUtil.setComponentReference((Part) part, false, componentObject);
    }
    
    /*
     * Return the prefixed component name described by the given
     * ComponentSpecification object.
     */
    public String getPrefixedComponentName(ComponentSpecification spec) {
        String name = (String) spec.getName();
        List prefixes = getPrefixes(definition, spec.getQualifier());
        if (prefixes.size() > 0) {
            name = prefixes.get(0) + ":" + name; //$NON-NLS-1$
        }
        
        return name;
    }         
          
    private List getPrefixes(Definition definition, String namespace) {
        List list = new ArrayList();
        Map map = definition.getNamespaces();
        for (Iterator i = map.keySet().iterator(); i.hasNext();) {
            String prefix = (String) i.next();
            String theNamespace = (String) map.get(prefix);
            if (theNamespace != null && theNamespace.equals(namespace)) {
                list.add(prefix);
            }
        }
        return list;
    }

    
    private void addImportIfNecessary(ComponentSpecification spec) {
        boolean foundMatch = false;
        
        // Check itself
        Path currentFileLocation = new Path(currentIFile.getLocation().toString());
        if (spec.getFile() == null || currentFileLocation.equals(spec.getFile().getLocation())) {
        	// if the ComponentSpecification's getFile() returns null, forget about adding necessary imports
            foundMatch = true;
        }
        
        // Check regular Imports
        if (!foundMatch) {
            Iterator importsIt = definition.getEImports().iterator();
            
            while (importsIt.hasNext()) {
                String importLocation = ""; //$NON-NLS-1$
                ImportImpl importItem = (ImportImpl) importsIt.next();
                importItem.importDefinitionOrSchema();
                
                if (importItem.getESchema() != null) {
                    XSDSchema schema = importItem.getESchema();
                    importLocation = getNormalizedLocation(schema.getSchemaLocation());
                }
                else {
                    Definition importDefinition = importItem.getEDefinition();
                    importLocation = getNormalizedLocation(importDefinition.getLocation()); 
                }            
    
                if (spec.getFile().getLocation().equals(new Path(importLocation))) {
                    foundMatch = true;
                    break;
                }
            }
        }
        
        // Check inline Schemas
        if (!foundMatch) {
            List imports = new ArrayList();
            
            if (definition.getETypes() != null) {
	            Iterator it = definition.getETypes().getEExtensibilityElements().iterator();
	            while (it.hasNext()) {
	                XSDSchemaExtensibilityElement eeElement = (XSDSchemaExtensibilityElement) it.next();
	                XSDSchema schema = eeElement.getSchema();
	                if (schema.getTargetNamespace() == null || schema.getTargetNamespace().equals("")) {                     //$NON-NLS-1$
	                    Iterator contents = schema.getContents().iterator();
	                    while (contents.hasNext()) {
	                        XSDSchemaContent content = (XSDSchemaContent) contents.next();
	                        if (content instanceof XSDImport) {
	                            imports.add(content);             
	                        }
	                    }
	                }
	            }
            }
            
            Iterator importIt = imports.iterator();
            while (importIt.hasNext()) {
                XSDImport importItem = (XSDImport) importIt.next();
                XSDSchema resolvedSchema = importItem.getResolvedSchema();
                String resolvedString = resolvedSchema.getSchemaLocation();
                String importLocation = getNormalizedLocation(resolvedString);
                
                if (spec.getFile().getLocation().equals(new Path(importLocation))) {
                    foundMatch = true;
                    break;
                }
            }            
        }
        
        if (!foundMatch) {
            boolean wsiStyleImport = isXSDSchemaFile(spec);
            if (wsiStyleImport) {
                AddElementDeclarationAction action = new AddElementDeclarationAction(definition, spec.getQualifier(), "xsd"); //$NON-NLS-1$
                action.run();
             
                String location = URIHelper.getRelativeURI(spec.getFile().getLocation(), currentIFile.getLocation());
                AddWSISchemaImportAction addImport = new AddWSISchemaImportAction(definition, spec.getQualifier(), location);
                addImport.run();
            }
            else {
                String newSelectedFileLoc = spec.getFile().getLocation().toOSString();
                String currentFileLoc = getNormalizedLocation(definition.getLocation());
                String relativeLoc = ComponentReferenceUtil.computeRelativeURI(newSelectedFileLoc, currentFileLoc, true);
                
                org.w3c.dom.Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
                String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
                String namespace = spec.getQualifier();
                
                AddImportAction addImportAction = new AddImportAction(null, definition, definitionElement, prefix, namespace, relativeLoc);
                addImportAction.run();            
                
                String uniquePrefix = getUniquePrefix(definition, prefix);            
                definitionElement.setAttribute("xmlns:" + uniquePrefix, namespace); //$NON-NLS-1$
            }
        }
    }
    /*
     * Try to determine if the passed in ComponentSpecification refers to
     * an XSD or WSDL file.  If it's an XSD, return true.
     */
    private boolean isXSDSchemaFile(ComponentSpecification spec) {
        String fileLocation = spec.getFile().getLocation().toOSString();
        int periodIndex = fileLocation.lastIndexOf("."); //$NON-NLS-1$
        
        if (periodIndex > 0) {
            String extension = fileLocation.substring(periodIndex + 1);
            if (extension.equalsIgnoreCase("xsd")) { //$NON-NLS-1$
                return true;
            }
        }

        return false;
    }
    
    private String getUniquePrefix(Definition definition, String initPrefix) {
      String uniquePrefix;
      Map map = definition.getNamespaces();

      if (definition.getNamespace(initPrefix) == null) {
        uniquePrefix = initPrefix;
      }
      else {// if used, then try to create a unique one
        String tempPrefix = initPrefix;
        int i = 1;
        while(map.containsKey(tempPrefix + i)) {
          i++;
        }
        uniquePrefix = tempPrefix + i;
      }
      return uniquePrefix;
    } 
    
    private String getNormalizedLocation(String location) {
        try {
            URL url = new URL(location);
            URL resolvedURL = FileLocator.resolve(url);
            location = resolvedURL.getPath();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return location; 
      }
}

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.dialogs.types;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementDeclarationAction;
import org.eclipse.wst.wsdl.ui.internal.actions.AddImportAction;
import org.eclipse.wst.wsdl.ui.internal.actions.AddWSISchemaImportAction;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.uriresolver.util.URIHelper;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentSpecification;
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
    
    public void setWSDLComponent(WSDLElement inputElement, String property, XMLComponentSpecification spec) {
        addImportIfNecessary(spec);
        String componentObject = getPrefixedComponentName(spec);

        org.w3c.dom.Element wsdlElement = inputElement.getElement();
        org.w3c.dom.Element definitionElement = definition.getElement();
        
        wsdlElement.setAttribute(property, componentObject); //$NON-NLS-1$
        
    }
    
    public void setXSDTypeComponent(Part part, XMLComponentSpecification spec) {
        if (!spec.getTagPath().equals(WSDLComponentSelectionProvider.BUILT_IN_TYPE)) {
            addImportIfNecessary(spec);
        }
        String componentObject = getPrefixedComponentName(spec);
        
        ComponentReferenceUtil.setComponentReference((Part) part, true, componentObject);
    }
    
    public void setXSDElementComponent(Part part, XMLComponentSpecification spec) {
        addImportIfNecessary(spec);
        String componentObject = getPrefixedComponentName(spec);
        
        ComponentReferenceUtil.setComponentReference((Part) part, false, componentObject);
    }
    
    /*
     * Return the prefixed component name described by the given
     * XMLComponentSpecification object.
     */
    public String getPrefixedComponentName(XMLComponentSpecification spec) {
        String name = (String) spec.getAttributeInfo("name");
        List prefixes = getPrefixes(definition, spec.getTargetNamespace());
        if (prefixes.size() > 0) {
            name = prefixes.get(0) + ":" + name;
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

    
    private void addImportIfNecessary(XMLComponentSpecification spec) {
        boolean foundMatch = false;
        
        // Check itself
        String currentFileLocation = currentIFile.getLocation().toString();
        if (currentFileLocation.equals(spec.getFileLocation())) {
            foundMatch = true;
        }
        
        // Check regular Imports
        if (!foundMatch) {
            Iterator importsIt = definition.getEImports().iterator();
            
            while (importsIt.hasNext()) {
                String importLocation = "";
                Import importItem = (Import) importsIt.next();
                if (importItem.getESchema() != null) {
                    XSDSchema schema = importItem.getESchema();
                    importLocation = getNormalizedLocation(schema.getSchemaLocation());
                }
                else {            
                    Definition importDefinition = importItem.getEDefinition();
                    importLocation = getNormalizedLocation(importDefinition.getLocation()); 
                }            
    
                if (importLocation.equals(spec.getFileLocation())) {
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
	                if (schema.getTargetNamespace() == null || schema.getTargetNamespace().equals("")) {                    
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
                
                if (importLocation.equals(spec.getFileLocation())) {
                    foundMatch = true;
                    break;
                }
            }            
        }
        
        if (!foundMatch) {
            boolean wsiStyleImport = isXSDSchemaFile(spec);
            if (wsiStyleImport) {
                AddElementDeclarationAction action = new AddElementDeclarationAction(definition, spec.getTargetNamespace(), "xsd");
                action.run();
             
                String location = URIHelper.getRelativeURI(new Path(spec.getFileLocation()), currentIFile.getLocation());
                AddWSISchemaImportAction addImport = new AddWSISchemaImportAction(definition, spec.getTargetNamespace(), location);
                addImport.run();
            }
            else {
                String newSelectedFileLoc = spec.getFileLocation();
                String currentFileLoc = getNormalizedLocation(definition.getLocation());
                String relativeLoc = ComponentReferenceUtil.computeRelativeURI(newSelectedFileLoc, currentFileLoc, true);
                
                org.w3c.dom.Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
                String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
                String namespace = spec.getTargetNamespace();
                
                AddImportAction addImportAction = new AddImportAction(null, definition, definitionElement, prefix, namespace, relativeLoc);
                addImportAction.run();            
                
                String uniquePrefix = getUniquePrefix(definition, prefix);            
                definitionElement.setAttribute("xmlns:" + uniquePrefix, namespace);
            }
        }
    }
    /*
     * Try to determine if the passed in XMLComponentSpecification refers to
     * an XSD or WSDL file.  If it's an XSD, return true.
     */
    private boolean isXSDSchemaFile(XMLComponentSpecification spec) {
        String fileLocation = spec.getFileLocation();
        int periodIndex = fileLocation.lastIndexOf(".");
        
        if (periodIndex > 0) {
            String extension = fileLocation.substring(periodIndex + 1);
            if (extension.equalsIgnoreCase("xsd")) {
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
            URL resolvedURL = Platform.resolve(url);
            location = resolvedURL.getPath();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return location; 
      }
    
    
}

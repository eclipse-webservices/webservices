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
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.eclipse.wst.wsdl.ui.internal.actions.AddImportAction;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentSpecification;


public class WSDLSetComponentHelper {
    private Definition definition;
    private IFile currentIFile;
    
    public WSDLSetComponentHelper(IFile iFile, Definition definition) {
        currentIFile = iFile;
        this.definition = definition;
    }
    
    public void setComponent(WSDLElement inputElement, String property, XMLComponentSpecification spec) {
        addImportIfNecessary(inputElement, spec);        
        String componentObject = getPrefixedComponentName(spec);

        org.w3c.dom.Element wsdlElement = inputElement.getElement();
        org.w3c.dom.Element definitionElement = definition.getElement();
        
        wsdlElement.setAttribute(property, componentObject); //$NON-NLS-1$
        
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

    
    private void addImportIfNecessary(WSDLElement inputElement, XMLComponentSpecification spec) {
        Iterator importsIt = definition.getEImports().iterator();
        boolean foundMatch = false;
        
        while (importsIt.hasNext()) {
            Import importItem = (Import) importsIt.next();
            Definition importDefinition = importItem.getEDefinition();
            String importLocation = getNormalizedLocation(importDefinition.getLocation());
            
            if (importLocation.equals(spec.getFileLocation())) {
                foundMatch = true;
                break;
            }
        }
        
        if (!foundMatch) {
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

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.ITypeSystemProvider;
import org.eclipse.wst.wsdl.ui.internal.typesystem.ExtensibleTypeSystemProvider;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xsd.ui.internal.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.common.IComponentList;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentFinder;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentSelectionProvider;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentSpecification;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xsd.XSDComponentSelectionDialog;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;

public class WSDLComponentSelectionProvider extends XMLComponentSelectionProvider {
    public static final String BUILT_IN_TYPE = "BUILT_IN_TYPE";
    
    private WSDLComponentFinder wsdlComponentFinder;
    private WSDLComponentSelectionDialog dialog;
    private WSDLComponentLabelProvider labelProvider;
    private Definition definition;
    private List lookupTagPaths;
    
    /*
     * Takes in the IFile we are currently editing.
     */
    public WSDLComponentSelectionProvider(IFile file, Definition definition, List lookupTagPaths) {
        List validExt = new ArrayList(1);
        validExt.add("wsdl");

        wsdlComponentFinder = new WSDLComponentFinder(lookupTagPaths);
        wsdlComponentFinder.setFile(file);
        wsdlComponentFinder.setValidExtensions(validExt);
        this.definition = definition;
        this.lookupTagPaths = lookupTagPaths;
        labelProvider = new WSDLComponentLabelProvider();
    }
    
    public WSDLComponentSelectionProvider(IFile file, Definition definition, List lookupTagPaths, List validExt) {
        this(file, definition, lookupTagPaths);
        wsdlComponentFinder.setValidExtensions(validExt);
    }
    
    public void setDialog(WSDLComponentSelectionDialog dialog) {
        this.dialog = dialog;
    }
    
    public String getType(Object element) {
        return null;
    }
    
    private void createWSDLComponentObjects(IComponentList list, List inputComponents, String tagPath) {
        Iterator it = inputComponents.iterator();
        while (it.hasNext()) {
            WSDLElement wsdlElement = (WSDLElement) it.next();
            XMLComponentSpecification spec = new XMLComponentSpecification(tagPath); 
            spec.addAttributeInfo("name", wsdlElement.getElement().getAttribute("name"));
            spec.setTargetNamespace(wsdlElement.getEnclosingDefinition().getTargetNamespace());
            
            String normalizedFile = getNormalizedLocation(wsdlElement.getEnclosingDefinition().getLocation());
            spec.setFileLocation(normalizedFile);
            
            addDataItemToTreeNode(list, spec);
        }
    }
    
    private void createXSDComponentObjects(IComponentList list, List inputComponents, String tagPath) {
        Iterator it = inputComponents.iterator();
        while (it.hasNext()) {
            XSDNamedComponent xsdElement = (XSDNamedComponent) it.next();
            XMLComponentSpecification spec = new XMLComponentSpecification(tagPath); 
            spec.addAttributeInfo("name", xsdElement.getName());
            spec.setTargetNamespace(xsdElement.getTargetNamespace());
            
            String normalizedFile = getNormalizedLocation(xsdElement.getSchema().getSchemaLocation());
            spec.setFileLocation(normalizedFile);
            
            addDataItemToTreeNode(list, spec);
        }
    }
        
    public void getComponents(IComponentList list, boolean quick) {
        if (quick) {
            Iterator tags = lookupTagPaths.iterator();
            while (tags.hasNext()) {
                getComponentsQuick(list, (String) tags.next());
            }
        }
        else {
            // Grab components from workspace locations
            String scope = "";
            if (dialog != null) {
                scope = dialog.getSearchScope();
            }
            
            List comps = new ArrayList();
            if (scope.equals(XSDComponentSelectionDialog.enclosingProjectString)) {
                comps = wsdlComponentFinder.getWorkbenchResourceComponents(XMLComponentFinder.ENCLOSING_PROJECT_SCOPE);
            }
            else if (scope.equals(XSDComponentSelectionDialog.entireWorkspaceString)) {
                comps = wsdlComponentFinder.getWorkbenchResourceComponents(XMLComponentFinder.ENTIRE_WORKSPACE_SCOPE);            
            }

            Iterator compsIt = comps.iterator();
            while (compsIt.hasNext()) {
                XMLComponentSpecification item = (XMLComponentSpecification) compsIt.next();
                addDataItemToTreeNode(list, item);
            }
        }
    }
    
    private void getComponentsQuick(IComponentList list, String tagPath) {
        // Grab components within the file
        if (tagPath.equals("/definitions/binding")) {
            // Grab explictly defined components
            createWSDLComponentObjects(list, definition.getEBindings(), tagPath);
            
            // Grab directly imported components
            Iterator importsIt = definition.getEImports().iterator();
            while (importsIt.hasNext()) {
                Import importItem = (Import) importsIt.next();
                Definition importDefinition = importItem.getEDefinition();
                List importedComponents = importDefinition.getEBindings();
                
                createWSDLComponentObjects(list, importedComponents, tagPath);
            }
        }
        else if (tagPath.equals("/definitions/portType")) {
            // Grab explictly defined components
            createWSDLComponentObjects(list, definition.getEPortTypes(), tagPath);
            
            // Grab directly imported components
            Iterator importsIt = definition.getEImports().iterator();
            while (importsIt.hasNext()) {
                Import importItem = (Import) importsIt.next();
                Definition importDefinition = importItem.getEDefinition();
                List importedComponents = importDefinition.getEPortTypes();
                
                createWSDLComponentObjects(list, importedComponents, tagPath);
            }
        }
        else if (tagPath.equals("/definitions/message")) {
            // Grab explictly defined components
            createWSDLComponentObjects(list, definition.getEMessages(), tagPath);
            
            // Grab directly imported components
            Iterator importsIt = definition.getEImports().iterator();
            while (importsIt.hasNext()) {
                Import importItem = (Import) importsIt.next();
                Definition importDefinition = importItem.getEDefinition();
                List importedComponents = importDefinition.getEMessages();
                
                createWSDLComponentObjects(list, importedComponents, tagPath);
            }

        }
        else if (tagPath.equals("/definitions/types/schema/complexType")) {
            createXSDComponentObjects(list, tagPath);
        }
        else if (tagPath.equals("/definitions/types/schema/simpleType")) {
            createXSDComponentObjects(list, tagPath);
            createXSDBuiltInTypeObjects(list);
            createRegularImportXSDTypeObjects(list);
        }

        else if (tagPath.equals("/definitions/types/schema/element")) {
            createXSDElementObjects(list, tagPath);
            createRegularImportXSDElementObjects(list);
        }
    }
    
    private void createXSDBuiltInTypeObjects(IComponentList list) {
        ITypeSystemProvider systemProvider = getTypeSystemProvider();
        Iterator it = systemProvider.getAvailableTypeNames(definition, ITypeSystemProvider.BUILT_IN_TYPE).iterator();
        while (it.hasNext()) {
            String builtIn = (String) it.next();
            //We need to remove the prefix
            int colonIndex = builtIn.indexOf(":");
            if (colonIndex >= 0) {
                builtIn = builtIn.substring(colonIndex + 1);
            }
                
            XMLComponentSpecification spec = new XMLComponentSpecification(BUILT_IN_TYPE); 
            spec.addAttributeInfo("name", builtIn);
            spec.setTargetNamespace(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001);

//            String normalizedFile = getNormalizedLocation(definition.getLocation());
//            spec.setFileLocation(normalizedFile);
            spec.setFileLocation("Built-in");    
            
            addDataItemToTreeNode(list, spec);
        }
    }
    
    private void createRegularImportXSDTypeObjects(IComponentList list) {
        // Handle regular imports(xsd and wsdl).
        Iterator imports = definition.getEImports().iterator();
        while (imports.hasNext()) {
            Import importItem = (Import) imports.next();
            List schemas = new ArrayList();
            String complexPath = "";
            String simplePath = "";
            
            if (importItem.getESchema() != null) {
                // Import is a xsd file
                schemas.add(importItem.getESchema());
                complexPath = "/schema/complexType";
                simplePath = "/schema/simpleType";
            }
            else if (importItem.getEDefinition() != null) {
                // Import is a wsdl file
                Definition definition = importItem.getEDefinition();
                Iterator schemaIt = definition.getETypes().getEExtensibilityElements().iterator();
                while (schemaIt.hasNext()) {
                    XSDSchemaExtensibilityElement eeElement = (XSDSchemaExtensibilityElement) schemaIt.next();
                    schemas.add(eeElement.getSchema());
                    complexPath = "/definitions/types/schema/complexType";
                    simplePath = "/definitions/types/schema/simpleType";
                }
            }
            
            if (schemas.size() > 0) {
                Iterator schemaIt = schemas.iterator();
                while (schemaIt.hasNext()) {
                    Iterator typesIt = ((XSDSchema) schemaIt.next()).getTypeDefinitions().iterator();
                    List complexList = new ArrayList();
                    List simpleList = new ArrayList();
                    while (typesIt.hasNext()) {
                        Object component = typesIt.next();
                        if (component instanceof XSDComplexTypeDefinition) {
                            complexList.add(component);
                        }
                        else if (component instanceof XSDSimpleTypeDefinition) {
                            simpleList.add(component);
                        }
                    }
                    
                    createXSDComponentObjects(list, complexList, complexPath);
                    createXSDComponentObjects(list, simpleList, simplePath);
                }
            }
        }
    }
    
    private void createRegularImportXSDElementObjects(IComponentList list) {
        // Handle regular imports(xsd and wsdl).
        Iterator imports = definition.getEImports().iterator();
        while (imports.hasNext()) {
            Import importItem = (Import) imports.next();
            List schemas = new ArrayList();
            String path = "";
            
            if (importItem.getESchema() != null) {
                // Import is a xsd file
                schemas.add(importItem.getESchema());
                path = "/schema/elements";
            }
            else if (importItem.getEDefinition() != null) {
                // Import is a wsdl file
                Definition definition = importItem.getEDefinition();
                Iterator schemaIt = definition.getETypes().getEExtensibilityElements().iterator();
                while (schemaIt.hasNext()) {
                    XSDSchemaExtensibilityElement eeElement = (XSDSchemaExtensibilityElement) schemaIt.next();
                    schemas.add(eeElement.getSchema());
                    path = "/definitions/types/schema/elements";
                }
            }
            
            if (schemas.size() > 0) {
                Iterator schemaIt = schemas.iterator();
                while (schemaIt.hasNext()) {
                    List elementList = ((XSDSchema) schemaIt.next()).getElementDeclarations();
                    createXSDComponentObjects(list, elementList, path);
                }
            }
        }
    }
    
    private void createXSDComponentObjects(IComponentList list, String tagPath) {
        // Handle inline schemas
        Iterator inlineSchemasIt = definition.getETypes().getSchemas().iterator();
        while (inlineSchemasIt.hasNext()) {
            XSDSchema schema = (XSDSchema) inlineSchemasIt.next();
            List types = new ArrayList();

            ITypeSystemProvider systemProvider = getTypeSystemProvider();
            if (systemProvider != null) {
                int kind = ITypeSystemProvider.USER_DEFINED_COMPLEX_TYPE;
                if (tagPath.equals("/definitions/types/schema/complexType")) {
                    kind = ITypeSystemProvider.USER_DEFINED_COMPLEX_TYPE;
                }
                else if (tagPath.equals("/definitions/types/schema/simpleType")) {
                    kind = ITypeSystemProvider.USER_DEFINED_SIMPLE_TYPE;
                }

                types.addAll(systemProvider.getAvailableTypes(definition, schema, kind));
                createXSDComponentObjects(list, types, tagPath);
            }
        }
        

    }

    private void createXSDElementObjects(IComponentList list, String tagPath) {
        Iterator inlineSchemasIt = definition.getETypes().getSchemas().iterator();
        while (inlineSchemasIt.hasNext()) {
            XSDSchema schema = (XSDSchema) inlineSchemasIt.next();
            createXSDComponentObjects(list, schema.getElementDeclarations(), tagPath);
        }
    }

      private ITypeSystemProvider getTypeSystemProvider() {
            ITypeSystemProvider provider = WSDLEditorUtil.getInstance().getTypeSystemProvider(definition);
            if (provider instanceof ExtensibleTypeSystemProvider) {
                provider = (ExtensibleTypeSystemProvider) provider; 
            }
            else {
                return null;
            }
            
            return provider;
      }

    
  
    public ILabelProvider getLabelProvider() {
        return labelProvider;
    }    
    
    public class WSDLComponentLabelProvider extends XMLComponentSelectionLabelProvider {
        public Image getImage(Object element) {
            XMLComponentTreeObject specification = (XMLComponentTreeObject) element;
            XMLComponentSpecification spec = (XMLComponentSpecification) specification.getXMLComponentSpecification().get(0);
            if (spec.getTagPath().equals("/definitions/binding")) {
                return WSDLEditorPlugin.getInstance().getImage("icons/binding_obj.gif");
            }
            else if (spec.getTagPath().equals("/definitions/portType")) {
                return WSDLEditorPlugin.getInstance().getImage("icons/port_obj.gif");
            }
            else if (spec.getTagPath().equals("/definitions/types/schema/complexType") ||
                     spec.getTagPath().equals("/schema/complexType")) {
                return XSDEditorPlugin.getXSDImage("icons/XSDComplexType.gif");
            }
            else if (spec.getTagPath().equals("/definitions/types/schema/simpleType") ||
                     spec.getTagPath().equals("/schema/simpleType") ||
                     spec.getTagPath().equals("BUILT_IN_TYPE")) {
                return XSDEditorPlugin.getXSDImage("icons/XSDSimpleType.gif");
            }
            else if (spec.getTagPath().equals("/definitions/types/schema/element") ||
                     spec.getTagPath().equals("/schema/element")) {
                return WSDLEditorPlugin.getInstance().getImage("icons/message_obj.gif");
            }
    
            return null;
        }
    }
}

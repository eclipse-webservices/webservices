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
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.ImportImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementDeclarationAction;
import org.eclipse.wst.wsdl.ui.internal.actions.AddImportAction;
import org.eclipse.wst.wsdl.ui.internal.actions.AddWSISchemaImportAction;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDElementDeclarationCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDTypeDefinitionCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.WSDLElementCommand;
import org.eclipse.wst.wsdl.ui.internal.extension.ITypeSystemProvider;
import org.eclipse.wst.wsdl.ui.internal.typesystem.ExtensibleTypeSystemProvider;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLResourceUtil;
import org.eclipse.wst.wsdl.ui.internal.widgets.NewComponentDialog;
import org.eclipse.wst.xsd.ui.internal.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.ImportTypesDialog;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.LoadAvaliableItems;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.NewTypeDialog;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.SetTypeDialog;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.TypesDialogTreeObject;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.impl.XSDImportImpl;
import org.w3c.dom.Element;


public class WSDLSetTypeDialog extends SetTypeDialog {
	private Definition definition;
	private IEditorPart editorPart;
	  
	public WSDLSetTypeDialog(Shell shell, Object input, IEditorPart editorPart, String dialogTitle, String kind) {
		super(shell, input, dialogTitle, kind);
	  	definition = ((Part) input).getEnclosingDefinition();
	  	this.editorPart = editorPart;
	}
	
	  
	  protected void updateCanFinish(Object object) {
	  	if (object instanceof StructuredSelection) {
	  		Object selectionObject= ((StructuredSelection) object).getFirstElement();
	  		if (selectionObject instanceof TypesDialogTreeObject) {
	  			TypesDialogTreeObject treeObject = (TypesDialogTreeObject) selectionObject;
	  			if (treeObject.getDataObject() instanceof XSDComplexTypeDefinition ||
	  				treeObject.getDataObject() instanceof XSDSimpleTypeDefinition ||
					(treeObject.getDataObject() instanceof String && treeObject.getType() == TypesDialogTreeObject.BUILT_IN_TYPE) ||
					treeObject.getDataObject() instanceof XSDElementDeclaration) {
	  				getButton(IDialogConstants.OK_ID).setEnabled(true);
	  			}
	  			else {
	  				getButton(IDialogConstants.OK_ID).setEnabled(false);
	  			}
	  		}
	  		else {
  				getButton(IDialogConstants.OK_ID).setEnabled(false);
  			}
	  	}
	  }
	
	protected void createButtonPressed() {
		// Determine where we create the new type/element
		XSDSchema parentSchema = null;
		TypesDialogTreeObject selection = treeView.getSelection();
		if (selection != null) {
			Object dataObject = selection.getDataObject();
			if (dataObject instanceof XSDSchema && selection.getType() == TypesDialogTreeObject.INLINE_SCHEMA) {
				parentSchema = (XSDSchema) dataObject;
			}
			else if (dataObject instanceof XSDComplexTypeDefinition || dataObject instanceof XSDSimpleTypeDefinition ||
					 dataObject instanceof XSDElementDeclaration) {
				dataObject = selection.getParent().getDataObject();
				if (dataObject instanceof XSDSchema && selection.getType() == TypesDialogTreeObject.INLINE_SCHEMA) {
					parentSchema = (XSDSchema) dataObject;
				}
			}
		}

	    WSDLElementCommand command;
		String newItemName;
		List usedNames = new ArrayList();     // Used for either Element or Complex Type names
		List usedSimpleTypeNames = new ArrayList();
		int returnCode = -1;
		if (kind.equalsIgnoreCase("type")) {
		      command = new AddXSDTypeDefinitionCommand(definition, "NewType");
		      ((AddXSDTypeDefinitionCommand) command).setSchema(parentSchema);
		      parentSchema = ((AddXSDTypeDefinitionCommand) command).getSchema();

			  newItemName = "NewComplexType";
			  
			  // Determine a list of names already used
			  usedNames = getUsedComplexTypeNames(parentSchema);
			  List usedSimpleNames = getUsedSimpleTypeNames(parentSchema);
			  newItemName = NameUtil.getUniqueNameHelper(newItemName, usedNames);
			  
			  NewTypeDialog dialog = new NewTypeDialog(XSDEditorPlugin.getShell(), newItemName, usedNames);
			  dialog.setUsedSimpleTypeNames(usedSimpleNames);
				
			  returnCode = dialog.createAndOpen();
			  if (returnCode == IDialogConstants.OK_ID) {
			  	newItemName = dialog.getName();
			  	((AddXSDTypeDefinitionCommand) command).isComplexType(dialog.isComplexType());
			  	((AddXSDTypeDefinitionCommand) command).run(newItemName);
			  }
		}
		else {
			 command = new AddXSDElementDeclarationCommand(definition, "NewElement");
			 ((AddXSDElementDeclarationCommand) command).setSchema(parentSchema);
			 parentSchema = ((AddXSDElementDeclarationCommand) command).getSchema();
			 
			 newItemName = "NewElement";
			 String newString = "Element";
			 
			 // Determine a list of names already used
			 usedNames = getUsedElementNames(parentSchema);
			 newItemName = NameUtil.getUniqueNameHelper(newItemName, usedNames);
			 
			 NewComponentDialog dialog = new NewComponentDialog(WSDLEditorPlugin.getShell(), "New" + " " + newString, newItemName, usedNames);
			 returnCode = dialog.createAndOpen();
			 if (returnCode == IDialogConstants.OK_ID) {
			 	newItemName = dialog.getName();
			 	((AddXSDElementDeclarationCommand) command).run(newItemName);
			 }
		}
		

		if (returnCode == IDialogConstants.OK_ID) {
	      // Select the newly created type/element
	      // We need to refresh our tree view
	      if (viewTypeCheckBox.getSelection()) {
	      	populateTreeViewer(viewTypeCheckBox.getSelection(), textFilter.getText(), false, false);
	      	
		    // We now want to expand the newly imported file and select the proper type/element
//	      	String prefix = (String) getPrefixes(definition, parentSchema.getTargetNamespace()).get(0);
//	      	treeView.selectTreeObject(TypesDialogTreeObject.getTreeObject(treeRootViewerInput, prefix + ":" + newItemName));
	      	treeView.selectTreeObject(TypesDialogTreeObject.getTreeObject(treeRootViewerInput, newItemName));
	      }
	      else {
	      	populateTreeViewer(viewTypeCheckBox.getSelection(), textFilter.getText(), true, true);

		    // We now want to expand the newly imported file and select the proper type/element
//	      	String prefix = (String) getPrefixes(definition, parentSchema.getTargetNamespace()).get(0);
			URI uri = URI.createPlatformResourceURI(parentSchema.getSchemaLocation());
	      	TypesDialogTreeObject parentTreeObject = TypesDialogTreeObject.getTreeObject(treeRootViewerInput, uri.lastSegment());
//	      	treeView.selectTreeObject(TypesDialogTreeObject.getTreeObject(parentTreeObject, prefix + ":" + newItemName));
	      	treeView.selectTreeObject(TypesDialogTreeObject.getTreeObject(parentTreeObject, newItemName));
	      }
		}

	}
	
	protected void importButtonPressed() {
    	ResourceSet resourceSet = null;
        resourceSet = ((org.eclipse.emf.ecore.EObject)input).eResource().getResourceSet();
       
		ImportTypesDialog dialog = new ImportTypesDialog(WSDLEditorPlugin.getShell(), null, true, kind);
	    String [] filters = { "xsd", "wsdl" };
	    IFile currentWSDLFile = ((IFileEditorInput)editorPart.getEditorInput()).getFile();
	    IFile [] excludedFiles = { currentWSDLFile };
	      
	    dialog.addFilterExtensions(filters, excludedFiles);
	    dialog.create();
	    dialog.getShell().setText(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT")); //$NON-NLS-1$
	    dialog.setTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT_FILE")); //$NON-NLS-1$
	    dialog.setMessage(WSDLEditorPlugin.getWSDLString("_UI_DESCRIPTION_SELECT_WSDL_OR_XSD")); //$NON-NLS-1$
	    dialog.addLoadAvaliableItemsClass(new WSDLLoadAvaliableItems(resourceSet));
	    
	    int rc = dialog.open();
	    if (rc == IDialogConstants.OK_ID) {
	  	  XSDNamedComponent selection = (XSDNamedComponent) dialog.getListSelection();
		  String namespaceURI = selection.getTargetNamespace();

	      if (dialog.isWSIStyleImport())
	      {
			AddElementDeclarationAction action = new AddElementDeclarationAction(definition, namespaceURI, "xsd");
			action.run();
			action.getPrefix();

			String location = getRelativeLocationOfSelectedFile(dialog.getFile(), false);
			AddWSISchemaImportAction addImport = new AddWSISchemaImportAction(definition, namespaceURI, location);
			addImport.run();
	      }
	      else
	      {      
		    addWSDLImport(definition, definition.getElement(), namespaceURI, "xsd", dialog.getFile());
	      }
	      
	      // We need to refresh our tree view
	      if (viewTypeCheckBox.getSelection()) {
	      	populateTreeViewer(viewTypeCheckBox.getSelection(), textFilter.getText(), false, false);
	      	treeView.selectTreeObject(TypesDialogTreeObject.getTreeObject(treeRootViewerInput, selection.getName()));
	      }
	      else {
	      	populateTreeViewer(viewTypeCheckBox.getSelection(), textFilter.getText(), true, true);

		    // We now want to expand the newly imported file and selected the proper type/element
	      	TypesDialogTreeObject parentTreeObject = TypesDialogTreeObject.getTreeObject(treeRootViewerInput, dialog.getFile().getName());
	      	treeView.selectTreeObject(TypesDialogTreeObject.getTreeObject(parentTreeObject, selection.getName()));
	      }
	    }
	}
	
	  protected void okPressed() {
	  	TypesDialogTreeObject selectionObject = (TypesDialogTreeObject) treeView.getSelection(); 
	  	String selectionString = selectionObject.getLabel();
	  	boolean isType = false;
	  	if (kind.equalsIgnoreCase("type")) {
	  		isType = true;
	  	}
	  	
	  	// We need to add the prefix to the selection
	  	if (selectionObject.getDataObject() instanceof XSDComplexTypeDefinition ||
	  			selectionObject.getDataObject() instanceof XSDSimpleTypeDefinition) {
		  	List selObjects = new ArrayList();
		  	selObjects.add(selectionObject.getDataObject());
		  	List prefixedName = getPrefixedNames(selObjects);
		  	if (prefixedName.size() > 0) {
		  		selectionString = (String) prefixedName.get(0);     // Grab the first prefixed name
		  	}	  	
	  	}
	  	else {
	  		// Some built-in type
	  		List prefixes = getPrefixes(definition, WSDLConstants.XSD_NAMESPACE_URI);
	  		if (prefixes.size() > 0) {
	  			selectionString = prefixes.get(0) + ":" + selectionString;       // Grab the first prefix
	  		}
	  	}
	  	
	  	ComponentReferenceUtil.setComponentReference((Part) input, isType, selectionString);
	  	super.okPressed();
	  }

	  protected void createFlatView(TypesDialogTreeObject root, String filterString) {
	  	if (kind.equalsIgnoreCase("type")) {
	  		// Type case
	  		// Handle Inline Schemas Imports
		  	Iterator schemasIt = getInlineSchemaImports().iterator();
		  	while (schemasIt.hasNext()) {
		  		XSDSchema schema = (XSDSchema) schemasIt.next();
		  		createComplexSimpleTypeTreeObjects(root, schema, filterString);
		  	}
		  	
		  	// Handle Regular Schema Imports
		  	schemasIt = getRegularSchemaImports().iterator();
		  	while (schemasIt.hasNext()) {
		  		XSDSchema schema = (XSDSchema) schemasIt.next();
		  		createComplexSimpleTypeTreeObjects(root, schema, filterString);
		  	}
		  	
		  	// Handle WSDL Imports
	        Iterator wsdlImportDefinitions = getWSDLImports().iterator();
	        while (wsdlImportDefinitions.hasNext()) {
	        	Definition definition = (Definition) wsdlImportDefinitions.next();
		  		createComplexSimpleTypeTreeObjects(root, definition, filterString);
	        }
		  	
		  	// Handle Built-in Types
		  	createBuiltInTypes(root, filterString);
	  	}
	  	else {
	  		// Element case
		  	Pattern regex = Pattern.compile(filterString);
	  		
		  	// Handle Inline and regular Schema Imports
	  		List schemaList = getInlineSchemaImports();
	  		schemaList.addAll(getRegularSchemaImports());
	  		Iterator schemasIt = schemaList.iterator();
	  		
	  		while (schemasIt.hasNext()) {
	  			XSDSchema schema = (XSDSchema) schemasIt.next();
	  			
	  			List elementList = schema.getElementDeclarations();
//	  			List elementNames = getPrefixedNames(elementList);

	  			createElementTreeObjects(root, elementList, filterString);
	  		}
	  		
	  		// Handle WSDL Imports
	        Iterator wsdlImportDefinitions = getWSDLImports().iterator();
	        while (wsdlImportDefinitions.hasNext()) {
	        	Definition definition = (Definition) wsdlImportDefinitions.next();
	  			createElementTreeObjects(root, definition, filterString);
	        }
	  	}
	  }
	  
	  protected void createTreeView(TypesDialogTreeObject root, String filterString) {
  		// Handle Type case
	  	// Create the BuiltInType Header TypesDialogTreeObject
	  	if (kind.equalsIgnoreCase("type")) {
		  	TypesDialogTreeObject builtInRoot = new TypesDialogTreeObject("Built-in simple type");
		  	root.addChild(builtInRoot);
		  	createBuiltInTypes(builtInRoot, filterString);
	  	}
	  	
	  	// Do inline schema imports
	    Iterator schemaIterator = getInlineSchemaImports().iterator();
	  	while (schemaIterator.hasNext()) {
	  		XSDSchema schema = (XSDSchema) schemaIterator.next();
	  		
	  		// Create Heading TypesDialogTreeObject for our current Schema
	  		if (schema.getTargetNamespace() != null) {
		  		TypesDialogTreeObject importSchemaRoot = new TypesDialogTreeObject(schema, TypesDialogTreeObject.INLINE_SCHEMA);
		  		importSchemaRoot.setAppendLabel("  (inline schema)");
		  		root.addChild(importSchemaRoot);
		  		if (kind.equalsIgnoreCase("type")) {
		  			createComplexSimpleTypeTreeObjects(importSchemaRoot, schema, filterString);
		  		}
		  		else {
		  			createElementTreeObjects(importSchemaRoot, schema.getElementDeclarations(), filterString);
		  		}
	  		}
	  		else {
	  			// WS-I Style
	  			Iterator wsiIterator= getWSISchemaImports(schema).iterator();
	  			
	  			while (wsiIterator.hasNext()) {
	  				XSDSchema wsiSchema = (XSDSchema) wsiIterator.next();
	  				TypesDialogTreeObject wsiRootObject = new TypesDialogTreeObject(wsiSchema);
	  		  		root.addChild(wsiRootObject);
	  		  		wsiRootObject.setAppendLabel("  (WS-I style)");
			  		if (kind.equalsIgnoreCase("type")) {
			  			createComplexSimpleTypeTreeObjects(wsiRootObject, wsiSchema, filterString);
			  		}
			  		else {
			  			createElementTreeObjects(wsiRootObject, wsiSchema.getElementDeclarations(), filterString);
			  		}
	  			}
	  		}
	  	}
	  	
	  	// Do regular Schema Imports
	  	schemaIterator = getRegularSchemaImports().iterator();
	  	while (schemaIterator.hasNext()) {
	  		XSDSchema schema = (XSDSchema) schemaIterator.next();
	  		
	  		// Create Heading TypesDialogTreeObject for our current Schema
	  		TypesDialogTreeObject importSchemaRoot = new TypesDialogTreeObject(schema);
	  		root.addChild(importSchemaRoot);
	  		if (kind.equalsIgnoreCase("type")) {
	  			createComplexSimpleTypeTreeObjects(importSchemaRoot, schema, filterString);
	  		}
	  		else {
	  			createElementTreeObjects(importSchemaRoot, schema.getElementDeclarations(), filterString);
	  		}
	  	}
	  	
	  	// Do WSDL Imports
        Iterator wsdlImportDefinitions = getWSDLImports().iterator();
        while (wsdlImportDefinitions.hasNext()) {
        	Definition definition = (Definition) wsdlImportDefinitions.next();
            
            // Create Heading TypesDialogTreeObject for our current WSDL
        	URI uri = URI.createPlatformResourceURI(definition.getLocation());
	  		TypesDialogTreeObject importWSDLRoot = new TypesDialogTreeObject(uri.lastSegment());
	  		root.addChild(importWSDLRoot);
	  		if (kind.equalsIgnoreCase("type")) {
	  			createComplexSimpleTypeTreeObjects(importWSDLRoot, definition, filterString);
	  		}
	  		else {
	  			createElementTreeObjects(importWSDLRoot, definition, filterString);
	  		}
        }
	  }
	  
	  
	  private void createElementTreeObjects(TypesDialogTreeObject rootObject, List elementList, String filterString) {
	  	Pattern regex = Pattern.compile(filterString);	  	
//	  	List elementList= schema.getElementDeclarations();
//		List elementNames = getPrefixedNames(elementList);

		for (int index = 0; index < elementList.size(); index++) {
			String name = ((XSDNamedComponent) elementList.get(index)).getName();
    		Matcher m = regex.matcher(name.toLowerCase());
			if (name.startsWith(filterString) || m.matches()) {
	  			TypesDialogTreeObject elementTreeObject = new TypesDialogTreeObject(elementList.get(index));
	  			rootObject.addChild(elementTreeObject);
	  			elementTreeObject.setLabel(name);
			}
		}
	  }
	  
	  private void createElementTreeObjects(TypesDialogTreeObject rootObject, Definition definition, String filterString) {
	   	ResourceSet resourceSet = null;
        resourceSet = ((org.eclipse.emf.ecore.EObject)input).eResource().getResourceSet();
        WSDLLoadAvaliableItems loadWSDLItems = new WSDLLoadAvaliableItems(resourceSet);
        List list = (List) loadWSDLItems.loadDefinitionObjects(definition);
        createElementTreeObjects(rootObject, list, filterString);
	  }
	  
	  private void createComplexSimpleTypeTreeObjects(TypesDialogTreeObject rootObject, Definition definition, String filterString) {
	   	ResourceSet resourceSet = null;
        resourceSet = ((org.eclipse.emf.ecore.EObject)input).eResource().getResourceSet();
        WSDLLoadAvaliableItems loadWSDLItems = new WSDLLoadAvaliableItems(resourceSet);
        List typesList = (List) loadWSDLItems.loadDefinitionObjects(definition);
        createComplexSimpleTypeTreeObjects(rootObject, typesList, filterString);
	  }
	  
	  private void createComplexSimpleTypeTreeObjects(TypesDialogTreeObject rootObject, XSDSchema schema, String filterString) {
	  	ITypeSystemProvider provider = WSDLEditorUtil.getInstance().getTypeSystemProvider(definition);
	    if (provider instanceof ExtensibleTypeSystemProvider) {
	    	provider = (ExtensibleTypeSystemProvider) provider; 
	    }
	    else {
	    	return;
	    }
	    
	    List typesList = new ArrayList();
	
	    // Collect all available Complex and Simple Types
	  	typesList.addAll(provider.getAvailableTypes(definition, schema, ITypeSystemProvider.USER_DEFINED_COMPLEX_TYPE));
	  	typesList.addAll(provider.getAvailableTypes(definition, schema, ITypeSystemProvider.USER_DEFINED_SIMPLE_TYPE));
	  	createComplexSimpleTypeTreeObjects(rootObject, typesList, filterString);
	  }
	  
	  /*
	   * This method will create appropriate Complex Type and Simple Type TreeObjects
	   * 
	   * List typeList ==> list of Complex and Simple types
	   */
	  private void createComplexSimpleTypeTreeObjects(TypesDialogTreeObject rootObject, List typesList, String filterString) {
	  	Pattern regex = Pattern.compile(filterString);
	  	
	  	for (int index = 0; index < typesList.size(); index++) {
    		Object typeObject = typesList.get(index);
    		String name = "";
    		
    		if (typeObject instanceof XSDNamedComponent) {
    			name = ((XSDNamedComponent) typeObject).getName();
    		
    			Matcher m = regex.matcher(name.toLowerCase());
    			if (name.startsWith(filterString) || m.matches()) {
    				TypesDialogTreeObject complexTreeObject = new TypesDialogTreeObject(typeObject);
    				rootObject.addChild(complexTreeObject);
    			}
    		}
	  	}
	  }
	  
	  /*
	   * return a list of WSDL from Imports
	   */
	  private List getWSDLImports() {
	  	return getImportsType(2);
	  }
	  
	  /*
	   * return a list of XML Schemas from Imports
	   */
	  private List getRegularSchemaImports() {
	  	return getImportsType(1);
	  }
	  
	  /*
	   * method returns XML Schemas from Imports or WSDLs from Imports (depending on the argument int importType).
	   * Users should use methods getRegularSchemaImports() or getWSDLImports() instead of calling
	   * this method directly.
	   * 
	   * importType = 1 ==> XML Schema Import
	   * importType = 2 ==> WSDL Import
	   */
	  private List getImportsType(int importType) {
	  	Definition definition = ((WSDLElement)input).getEnclosingDefinition();
		List importList = new ArrayList();
		
//		 Grab all Schemas under Imports
		for (Iterator i = definition.getEImports().iterator(); i.hasNext();) {
			Import theImport = (Import) i.next();
			((ImportImpl) theImport).importDefinitionOrSchema(); // Need to resolve imports because the model
									                			 // doesn't automatically do it for us
			Object importObject = null;
			if (importType == 1) {
				importObject = theImport.getESchema();
			}
			else if (importType == 2) {
				importObject = theImport.getEDefinition();
			}
			if (importObject != null)	{
				importList.add(importObject);
			}
		}

	  	return importList;
	  }
	  
	  private List getInlineSchemaImports() {
	  	List schemaList = new ArrayList();
	  	
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
    					schemaList.add(schema);
    				}
    			}
    		}
    	}
    	
    	// If there is a WS-I style schema wrapper, move it to the front of the list
    	for (int index = 0; index < schemaList.size(); index++) {
    		XSDSchema schema = (XSDSchema) schemaList.get(index);
    		if (schema.getTargetNamespace() == null) {
    			// Found the WS-I style schema wrapper
    			schemaList.remove(index);
    			schemaList.add(0, schema);
    		}
    	}
    	return schemaList;
	  }
	  
	    public List getWSISchemaImports(XSDSchema xsdSchema)
	    {
	    	List schemaImports = new ArrayList();
	        if (xsdSchema.getTargetNamespace() == null) {
	            for (Iterator i = xsdSchema.getContents().iterator(); i.hasNext();) {
	                XSDSchemaContent content = (XSDSchemaContent) i.next();
	                if (content instanceof XSDImportImpl) {
	                    XSDImportImpl xsdImport = (XSDImportImpl) content;
						XSDSchema schema = (XSDSchema)xsdImport.getResolvedSchema();					
						if (schema == null || schema.getSchemaLocation() == null) {
							schema = xsdImport.importSchema();
						}
						if (schema != null) {
							schemaImports.add(schema);
						}
	                }
	            }
	        }
	        
	        return schemaImports;
	    }
	  
	  private void createBuiltInTypes(TypesDialogTreeObject root, String filterString) {
	  	Iterator iterator = getBuiltInTypes().iterator();
	  	Pattern regex = Pattern.compile(filterString);
	  	if (iterator.hasNext()) {
	  		while (iterator.hasNext()) {
	  			Object item = iterator.next();
	  			String itemString = item.toString();
    			Matcher m = regex.matcher(itemString.toLowerCase());
				if (itemString.startsWith(filterString) || m.matches()) {
		  			TypesDialogTreeObject treeObject = new TypesDialogTreeObject(item, TypesDialogTreeObject.BUILT_IN_TYPE);
	  				root.addChild(treeObject);
	  			}
	  		}
	  	}
	  }

	  private List getBuiltInTypes() {
	  	ITypeSystemProvider provider = WSDLEditorUtil.getInstance().getTypeSystemProvider(definition);
	    if (provider instanceof ExtensibleTypeSystemProvider)
	    {
	      List items = ((ExtensibleTypeSystemProvider) provider).getAvailableTypeNames(definition, ITypeSystemProvider.BUILT_IN_TYPE);
	      List unPrefixedNames = new ArrayList(items.size());
	      
	      // We want to strip out the prefixes
	      Iterator iterator = items.iterator();
	      while (iterator.hasNext()) {
	      	String name = (String) iterator.next();
	      	int colonIndex = name.indexOf(':') + 1;
	      	unPrefixedNames.add(name.substring(colonIndex, name.length()));
	      }
	      
	      return unPrefixedNames;
	    }
	  	
	  	return (new ArrayList());
	  }


	  private List getPrefixedNames(List components) {
	  	List names = new ArrayList();
	  	ITypeSystemProvider provider = WSDLEditorUtil.getInstance().getTypeSystemProvider(definition);
	    if (provider instanceof ExtensibleTypeSystemProvider) {
	    	provider = (ExtensibleTypeSystemProvider) provider; 
	    }
	    else {
	    	return names;
	    }
	    
	    Iterator iterator = components.iterator();
	    while (iterator.hasNext()) {
	    	XSDNamedComponent component = (XSDNamedComponent) iterator.next();
	    	names.addAll(provider.getPrefixedNames(definition, component.getTargetNamespace(), component.getName()));
	    }
	  	
	    return names;
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
		 
	  
	  public String addWSDLImport(Definition definition, Element definitionElement, String namespaceURI, IFile selectedFile)
	  {
	    return addWSDLImport(definition, definitionElement, namespaceURI, "wsdl", selectedFile);
	  }

	  public String addWSDLImport(Definition definition, Element definitionElement, String namespaceURI, String basePrefix, IFile selectedFile)
	  {
	    AddElementDeclarationAction action = new AddElementDeclarationAction(definition, namespaceURI, basePrefix);
	    action.run();
	    String prefix = action.getPrefix();

	    String location = getRelativeLocationOfSelectedFile(selectedFile, true);
	    AddImportAction addImport = new AddImportAction(null, definition, definitionElement, definitionElement.getPrefix(), namespaceURI, location);
	    addImport.selectObjectForNewElement(false);
	    addImport.run();

	    return prefix;
	  }
	  
	  public String getRelativeLocationOfSelectedFile(IFile selectedFile, boolean enableIEStyleLocation)
	  {
	    IFile currentWSDLFile = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
	    return ComponentReferenceUtil.computeRelativeURI(selectedFile, currentWSDLFile, true);
	  }
	  
	  private class WSDLLoadAvaliableItems extends LoadAvaliableItems {
		ResourceSet modelResourceSet;

		public WSDLLoadAvaliableItems(ResourceSet rSet) {
			modelResourceSet = rSet;
		}

		  protected java.util.List loadFile(IFile wsdlFile) {
		    java.util.List modelObjectList = Collections.EMPTY_LIST;
		    try {
		      if (wsdlFile != null) {
		        URI uri = URI.createPlatformResourceURI(wsdlFile.getFullPath().toString());

		        Object rootModelObject = null;
		        if (uri.toString().endsWith("wsdl")) {
		          rootModelObject = WSDLResourceUtil.lookupAndLoadDefinition(modelResourceSet, uri.toString());
		        }

		        if (rootModelObject != null) {
		          modelObjectList = new ArrayList(loadDefinitionObjects(rootModelObject));
		        }
		      }
		    }
		    catch (Exception e) {
		    }
		    return modelObjectList;
		  }
		  
		  private Collection loadDefinitionObjects(Object rootModelObject) {
		    ArrayList objects = new ArrayList();
		    
		    if (rootModelObject instanceof Definition) {
		      Definition definition = (Definition)rootModelObject;		
		      Types types = (Types)definition.getTypes();
		      if (types != null) {
		        for (Iterator iter = types.getSchemas().iterator(); iter.hasNext();) {
		          XSDSchema schema = (XSDSchema) iter.next();
		          if (kind.equalsIgnoreCase("type")) {
		            objects.addAll(schema.getTypeDefinitions());
		          }
		          else if (kind.equalsIgnoreCase("element")) {
		            objects.addAll(schema.getElementDeclarations());
		          }
		        }
		      } 
		    }
		    return objects;
		  }
	  }
}
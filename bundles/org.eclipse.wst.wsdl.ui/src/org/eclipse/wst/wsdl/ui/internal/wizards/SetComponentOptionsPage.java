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
package org.eclipse.wst.wsdl.ui.internal.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;


import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.ui.dialogs.SelectSingleFileDialog;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;

public class SetComponentOptionsPage extends SetOptionsPage
{
  protected String kind;
  protected Button importStyleCheckBox;
  boolean isWSIStyleSchemaImport;  
// Support for choosing which schema to add element/type to    
// protected String schemaSelection;
// protected org.eclipse.swt.widgets.List schemaList;

  public SetComponentOptionsPage(
    IEditorPart editorPart,
    String pageName,
    String title,
    ImageDescriptor titleImage,
    String kind)
  {
    super(editorPart, pageName, title, titleImage, kind);
    this.kind = kind;
  }
  
  public boolean isWSIStyleSchemaImport()
  {
	return isWSIStyleSchemaImport;
  }  

  public void createControl(Composite parent)
  {
	super.createControl(parent);
	importStyleCheckBox = new Button(page3, SWT.CHECK);
	importStyleCheckBox.setText(WSDLEditorPlugin.getWSDLString("_UI_USE_WS-I_STYLE_SCHEMA_IMPORT"));
	importStyleCheckBox.setSelection(true);	
	isWSIStyleSchemaImport = true;
	importStyleCheckBox.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	
	SelectionListener selectionListener = new SelectionListener()
	{
		public void widgetSelected(SelectionEvent event)
		{
			isWSIStyleSchemaImport = importStyleCheckBox.getSelection();
		}
		
		public void widgetDefaultSelected(SelectionEvent event)
		{			
		}		
	};
	importStyleCheckBox.addSelectionListener(selectionListener);
  }

  protected void initNewNameTextField()
  {
    Definition definition = ((WSDLElement)input).getEnclosingDefinition();
    // TODO: generate new unique schema component name
    if (kind.equalsIgnoreCase("type"))
    {
      newNameText.setText(getNewNameHelper("NewComplexType", true));
    }
    else
    {
      newNameText.setText(getNewNameHelper("NewElement", false));
    }
  }     
                        
  protected Hashtable createNameTable(boolean isType)
  {     
    // TODO ... instead of using ComponentReferenceUtil, we should locate the specific schema
    // that will be used to create the component and ask it for its component names.
    // The code below is rather gross... we really need to fix this up!
    Hashtable table = new Hashtable();
    if (input != null)
    {                        
      Part part = (Part)input;
      for (Iterator iterator = ComponentReferenceUtil.getComponentNameList(part, isType).iterator(); iterator.hasNext();)
      {          
        String name = (String)iterator.next(); 
        int index = name.indexOf(":");
        if (index != -1 && name.length() > index)
        {
          name = name.substring(index + 1);
        }
        table.put(name, name);
        //System.out.println("Name -> " + name);
      }
    }                         
    return table;
  }

  protected String getNewNameHelper(String base, boolean isType)
  { 
    String name = base;    
    int count = 0;
    Hashtable table = createNameTable(isType);
    while (true)
    {
      if (table.get(name) == null)
      {
        break;
      }       
      else
      {   
        count++;
        name = base + count;
      }            
    }
    return name;
  }

  protected void initExistingNameList()
  {                    
    if (componentNameList.getItemCount() == 0)
    {
      componentNameList.removeAll(); 
      boolean isType = kind.equalsIgnoreCase("type") ? true : false;
      if (input != null)
      {
        Part part = (Part)input;
        for (Iterator iterator =  ComponentReferenceUtil.getComponentNameList(part, isType).iterator(); iterator.hasNext();)
        {
          componentNameList.add((String)iterator.next());
        }
      }
      if (componentNameList.getItemCount() > 0)
      {
        componentNameList.select(0);
        existingListSelection = (componentNameList.getSelection())[0];
      }
    }
  }       

// Support for choosing which schema to add element/type to
//  protected void initSchemaList()
//  {                    
//    if (schemaList.getItemCount() == 0)
//    {
//      schemaList.removeAll(); 
//      if (part != null)
//      {
//        javax.wsdl.Types types = definition.getTypes();
//        java.util.List schemas = types.getExtensibilityElements();
//
//        for (Iterator iterator =  schemas.iterator(); iterator.hasNext();)
//        {
//          XSDSchemaExtensibilityElement schema = (XSDSchemaExtensibilityElement)iterator.next();
//          Element schemaElement = schema.getSchemaElement();
//          String targetNamespace = schemaElement.getAttribute("targetNamespace");
//          String prefix = definition.getPrefix(targetNamespace);
//          if (prefix != null)
//          {
//            schemaList.add(prefix + ":" + targetNamespace);
//          }
//          else
//          {
//            schemaList.add(targetNamespace);            
//          }
//        }
//      }
//      if (componentNameList.getItemCount() > 0)
//      {
//        schemaList.select(0);
//        schemaSelection = (schemaList.getSelection())[0];
//      }
//    }
//  }       

//  public void widgetSelected(SelectionEvent e)
//  {
//    else if (e.widget == createNewRadio)
//    {
//      pageBook.showPage(page2); 
//      choice = 2;
// Support for choosing which schema to add element/type to
//      initSchemaList();
//      setDescription("Specify name of " + kind + " to create");
//      setPageComplete(isPageComplete());
//    }
// Support for choosing which schema to add element/type to
//    else if (e.widget == schemaList)
//    {
//      schemaSelection = (schemaList.getSelection())[0]; 
//    }
//  }

  protected void handleImport()
  {
    ResourceSet resourceSet = null;
    resourceSet = ((org.eclipse.emf.ecore.EObject)input).eResource().getResourceSet();

    Resource resource = ((org.eclipse.emf.ecore.EObject)input).eResource();
    URI uri = resource.getURI();
    
    Path path = new Path(uri.path());
    path = (Path) path.removeFirstSegments(1);
    int segments = path.segmentCount();

    Object container = null;
    if (segments > 2) {
    	container = ResourcesPlugin.getWorkspace().getRoot().getFolder(path.removeLastSegments(1));
    }
    else {
    	container = ResourcesPlugin.getWorkspace().getRoot().getProject(path.segment(path.segmentCount() - 2).toString());
    }
    
    SelectSingleFileDialog dialog = new SelectSingleFileDialog(getShell(), new StructuredSelection(container), true);
    String [] filters = { "xsd", "wsdl" };
    dialog.addFilterExtensions(filters);
    dialog.create();
    dialog.setTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT_FILE")); //$NON-NLS-1$
    dialog.getShell().setText(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT")); //$NON-NLS-1$
    dialog.setMessage(WSDLEditorPlugin.getWSDLString("_UI_DESCRIPTION_SELECT_WSDL_OR_XSD")); //$NON-NLS-1$
    int rc = dialog.open();
    if (rc == IDialogConstants.OK_ID)
    {
      selectedFile = dialog.getFile();
      importComponents = loadFile(selectedFile, resourceSet);
      importList.removeAll();
      for (Iterator i = importComponents.iterator(); i.hasNext(); )
      {
        XSDNamedComponent comp = (XSDNamedComponent)i.next();
        importList.add(comp.getName());
      }
      fileText.setText(dialog.getFile().getFullPath().toString());
    }
  }

  public Collection getModelObjects(Object rootModelObject)
  {
    ArrayList objects = new ArrayList();
    
    if (rootModelObject instanceof XSDSchema)
    {
      XSDSchema xsdSchema = (XSDSchema)rootModelObject;
      if (kind.equalsIgnoreCase("type"))
      {
        objects.addAll(((XSDSchema) xsdSchema).getTypeDefinitions());
      }
      else if (kind.equalsIgnoreCase("element"))
      {
        objects.addAll(((XSDSchema) xsdSchema).getElementDeclarations());
      }
    }
    else if (rootModelObject instanceof Definition)
    {
      Definition definition = (Definition)rootModelObject;		
      Types types = (Types)definition.getTypes();
      if (types != null)
      {
        for (Iterator iter = types.getSchemas().iterator(); iter.hasNext();)
        {
          XSDSchema schema = (XSDSchema) iter.next();
          if (kind.equalsIgnoreCase("type"))
          {
            objects.addAll(schema.getTypeDefinitions());
          }
          else if (kind.equalsIgnoreCase("element"))
          {
            objects.addAll(schema.getElementDeclarations());
          }
        }
      } 
    }
    return objects;
  }

// Support for choosing which schema to add element/type to  
//  public String getSchemaSelection()
//  {
//    return schemaSelection;
//  }
}

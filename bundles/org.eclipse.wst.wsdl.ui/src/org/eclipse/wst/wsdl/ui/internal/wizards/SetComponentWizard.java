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

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDElementDeclarationCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDTypeDefinitionCommand;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementDeclarationAction;
import org.eclipse.wst.wsdl.ui.internal.actions.AddWSISchemaImportAction;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.xsd.XSDNamedComponent;

public class SetComponentWizard extends Wizard
{
  protected Object input;
  private IEditorPart editorPart;
  private String kind;
  SetComponentOptionsPage newComponentOptionsPage;
  
  /**
   * Constructor for NewComponentWizard.
   */
  public SetComponentWizard(Object input, IEditorPart editorPart)
  {
    super();
    this.input = input;
    this.editorPart = editorPart;
// setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(WSDLEditor.class, "icons/wsdl_file_obj.gif"));
  }
  /**
   * @see org.eclipse.jface.wizard.IWizard#performFinish()
   */
  public boolean performFinish()
  {
    Part part = (Part)input;
    Definition definition = part.getEnclosingDefinition();
    org.w3c.dom.Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
    boolean isType = kind.equalsIgnoreCase("type");  //$NON-NLS-1$
    if (newComponentOptionsPage.getChoice() == 2)
    {
      String choice = newComponentOptionsPage.getExistingListSelection(); 
      if (choice != null)
      {
        ComponentReferenceUtil.setComponentReference(part, isType, choice);
      }
    }
    else if (newComponentOptionsPage.getChoice() == 1)
    {
/*
      String newName = newComponentOptionsPage.getNewName();
      javax.wsdl.Types types = definition.getTypes();
      org.w3c.dom.Node typesNode;
      org.w3c.dom.Element schemaElement;
      String xsdPrefix;
      String wsdlDocTargetNamespace = definition.getTargetNamespace();
      // for now the new prefix is the same as the targetNamespace's prefix
      String referencingPrefix = definition.getPrefix(wsdlDocTargetNamespace);
      
      if (!(definition.getNamespaces().containsValue("http://www.w3.org/2001/XMLSchema"))) //$NON-NLS-1$
      {
        // try to use xsd as the index
        if (definition.getNamespace("xsd") == null) //$NON-NLS-1$
        {
          xsdPrefix = "xsd"; //$NON-NLS-1$
        }
        else // if used, then try to create a unique one
        {
          String tempPrefix = "xsd"; //$NON-NLS-1$
          int i = 1;
          while(definition.getNamespace(tempPrefix + i) != null)
          {
            i++;
          }
          xsdPrefix = tempPrefix + i;
        }
        // Add the namespace to the definition element
        definition.addNamespace(xsdPrefix, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
        definitionElement.setAttribute("xmlns:" + xsdPrefix, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      else
      {
        xsdPrefix = definition.getPrefix("http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
      }   
      // if the types element is not present
      if (types == null)
      {
        AddElementAction addTypesAction = new AddElementAction("", "icons/xsd_obj.gif", definitionElement, definitionElement.getPrefix(), "types"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        addTypesAction.setComputeTopLevelRefChild(true);
        addTypesAction.run();
        typesNode = addTypesAction.getNewElement();

        AddElementAction addSchemaAction = new AddElementAction("", "icons/xsd_obj.gif", typesNode, xsdPrefix, "schema"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        addSchemaAction.run();
        schemaElement = addSchemaAction.getNewElement();
        
        schemaElement.setAttribute("xmlns:" + xsdPrefix, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$ //$NON-NLS-2$
        schemaElement.setAttribute("elementFormDefault", "qualified"); //$NON-NLS-1$ //$NON-NLS-2$
        schemaElement.setAttribute("targetNamespace", wsdlDocTargetNamespace); //$NON-NLS-1$
      }
      else // if the types element is present
      {
        typesNode = WSDLEditorUtil.getInstance().getElementForObject(types);
        java.util.List schemaList = types.getExtensibilityElements();
        if (schemaList.size() > 0) // if there is a schema
        {
          if (schemaList.get(0) instanceof XSDSchemaExtensibilityElement)
          {
            XSDSchemaExtensibilityElement schema = (XSDSchemaExtensibilityElement)schemaList.get(0);
            schemaElement = schema.getElement();
            String schemaNS = schemaElement.getAttribute("targetNamespace"); //$NON-NLS-1$
            referencingPrefix = definition.getPrefix(schemaNS);
          }
          else
          {
            return true;  // TODO: check for UnknownExtensibilityElement 
          }
        }
        else // if there is no schema then we'll create one
        {
          AddElementAction addSchemaAction = new AddElementAction("", "icons/xsd_obj.gif", typesNode, xsdPrefix, "schema"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          addSchemaAction.run();
          schemaElement = addSchemaAction.getNewElement();
          schemaElement.setAttribute("xmlns:" + xsdPrefix, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$ //$NON-NLS-2$
          schemaElement.setAttribute("elementFormDefault", "qualified");   //$NON-NLS-1$ //$NON-NLS-2$
          schemaElement.setAttribute("targetNamespace", wsdlDocTargetNamespace); //$NON-NLS-1$
        }
      }
      
      
      String xsdComp;
      if (isType)
      {
        xsdComp = "complexType"; //$NON-NLS-1$
      }
      else
      {
        xsdComp = "element"; //$NON-NLS-1$
      }
      AddElementAction addCompAction = new AddElementAction("", "icons/xsd_obj.gif", schemaElement, xsdPrefix, xsdComp); //$NON-NLS-1$ //$NON-NLS-2$
      addCompAction.run();
      Element newElement = addCompAction.getNewElement();
      newElement.setAttribute("name", newName); //$NON-NLS-1$
      if (!isType)
      {
        newElement.setAttribute("type", xsdPrefix + ":string"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      if (referencingPrefix == null)
      {
        referencingPrefix = ""; //$NON-NLS-1$
      }
      ComponentReferenceUtil.setComponentReference(part, isType, referencingPrefix.equals("") ? newName : referencingPrefix + ":" + newName); //$NON-NLS-1$ //$NON-NLS-2$
    */
    
      String newName = newComponentOptionsPage.getNewName();
      String targetNamespace = definition.getTargetNamespace();
      String referencingPrefix = definition.getPrefix(targetNamespace);
      if (isType)
      {
      	AddXSDTypeDefinitionCommand command = new AddXSDTypeDefinitionCommand(definition, targetNamespace, newName);
      	command.run();
      }
      else
      {
        AddXSDElementDeclarationCommand command = new AddXSDElementDeclarationCommand(definition, targetNamespace, newName);
        command.run();
      }      
      ComponentReferenceUtil.setComponentReference(part, isType, referencingPrefix.equals("") ? newName : referencingPrefix + ":" + newName); //$NON-NLS-1$ //$NON-NLS-2$      
    }    
    else if (newComponentOptionsPage.getChoice() == 3)
    { 
	  XSDNamedComponent selection = (XSDNamedComponent)newComponentOptionsPage.getSelection();
	  String namespaceURI = selection.getTargetNamespace();
      String prefix = "";
         	
      boolean useWSIImportPattern = true;	
      if (newComponentOptionsPage.isWSIStyleSchemaImport())
      {
		AddElementDeclarationAction action = new AddElementDeclarationAction(definition, namespaceURI, "xsd");
		action.run();
		prefix = action.getPrefix();
     
		String location = newComponentOptionsPage.getRelativeLocationOfSelectedFile(false);
		AddWSISchemaImportAction addImport = new AddWSISchemaImportAction(definition, namespaceURI, location);
		addImport.run();
      }
      else
      {      
	    prefix = newComponentOptionsPage.addWSDLImport(definition, definitionElement, namespaceURI, "xsd");
      }
	  ComponentReferenceUtil.setComponentReference(part, isType, prefix.length() == 0 ? selection.getName() : prefix + ":" + selection.getName());    	            	
    }
    return true;
  }

  public boolean canFinish()
  {
    if (newComponentOptionsPage.isPageComplete())
    {
      return true;
    }
    return false;
  }

  public void setReferenceKind(String kind)
  {
    this.kind = kind;
  }

  public Object getInput()
  {
    return input;
  }
 
  public void addPages()
  {
    String title = kind.equalsIgnoreCase("type") ? WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_TYPE") : WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_ELEMENT");
    newComponentOptionsPage = new SetComponentOptionsPage(editorPart, WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_TYPE"), title, null, kind);
    newComponentOptionsPage.setEditorPart(editorPart);
    newComponentOptionsPage.setInput(input);
    addPage(newComponentOptionsPage);
    setWindowTitle(title);
  } 
}

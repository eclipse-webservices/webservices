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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDElementDeclarationCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDTypeDefinitionCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.WSDLElementCommand;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.widgets.NewComponentDialog;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 * TODO: Need to clean up this class.... We probably don't use quite a few of these
 * methods/instance variables.
 */
public class SetNewComponentAction extends AddElementAction
{
  Object input;
  protected IEditorPart editorPart;
  protected String kind;
  private String typeKind = "complex";      // TODO: Refactor code to handle this better

  public SetNewComponentAction(
    String text,
    Node parentNode,
    String prefix,
    String localName,
    Object input)
  {
    super(text, parentNode, prefix, localName);
    this.input = input;
  }

  public void setEditor(IEditorPart editorPart)
  {
    this.editorPart = editorPart; 
  }

  public void setReferenceKind(String kind)
  {
    this.kind = kind;
  }
  
  public void setTypeKind(String typeKind)
  {
      this.typeKind = typeKind;
  }

  // TODO: Clean up next methods below!!!
  private WSDLElementCommand command;
  private String newDialogTitle = "New Component";
  
  protected void performAddElement()
  {
    Shell shell = Display.getCurrent().getActiveShell();
    String defaultName = getDefaultName();
    NewComponentDialog dialog = new NewComponentDialog(shell, newDialogTitle, defaultName, getUsedNames());

    int dialogCode = dialog.createAndOpen();
    
    if (dialogCode == Window.OK) {
        if (input instanceof Port) {
            Definition definition = ((Port) input).getEnclosingDefinition();
            String newName = dialog.getName();
            
            // TODO: We should be smarter in choosing some default options/settings for generating a new Binding
            BindingGenerator bindingGenerator = new BindingGenerator(definition);
            bindingGenerator.setName(newName);
            bindingGenerator.setProtocol("SOAP");
            bindingGenerator.generate();
            
          String itemPrefix = definition.getPrefix(definition.getTargetNamespace());
          if (itemPrefix == null)
          {
            itemPrefix = "";
          }
          org.w3c.dom.Element wsdlElement = WSDLEditorUtil.getInstance().getElementForObject((WSDLElement) input);
          wsdlElement.setAttribute("binding", itemPrefix.length() == 0 ? newName : itemPrefix + ":" + newName);     
          
        }
        else if (input instanceof Binding) {            
            Definition definition = ((Binding) input).getEnclosingDefinition();
            Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
            
            String newName = dialog.getName();
            String prefix = definitionElement.getPrefix();
            // Use AddMessageAction in WSDLMenuActionContributor
            AddElementAction addPortTypeAction = new AddElementAction("", "icons/message_obj.gif", definitionElement, prefix, "portType"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            addPortTypeAction.setComputeTopLevelRefChild(true);
            addPortTypeAction.run();
            org.w3c.dom.Element newMessage = addPortTypeAction.getNewElement();
            newMessage.setAttribute("name", newName); //$NON-NLS-1$
            // need to get the prefix for the item
            // could get list of updated messages and then select but currently we only have the local name
            String itemPrefix = definition.getPrefix(definition.getTargetNamespace());
            if (itemPrefix == null)
            {
              itemPrefix = ""; //$NON-NLS-1$
            }
            String portTypeValue = itemPrefix.length() == 0 ? newName : itemPrefix + ":" + newName; //$NON-NLS-1$ //$NON-NLS-2$

            // Set the component
            if (input != null && portTypeValue != null) {
                org.w3c.dom.Element wsdlElement = WSDLEditorUtil.getInstance().getElementForObject((WSDLElement) input);
                wsdlElement.setAttribute("type", portTypeValue);
            } 
        }
        else if (input instanceof Input || input instanceof Output || input instanceof Fault) {
            Definition definition = ((MessageReference) input).getEnclosingDefinition();
            String newName = dialog.getName();

            AddMessageCommand action = new AddMessageCommand(definition, newName);
            action.run();
            Message message = (Message) action.getWSDLElement();
            CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.getPartInfo(message); 
            CreateWSDLElementHelper.createPart(message);

          // need to get the prefix for the item
          // could get list of updated messages and then select but currently we only have the local name
          String itemPrefix = definition.getPrefix(definition.getTargetNamespace());
          if (itemPrefix == null)
          {
            itemPrefix = ""; //$NON-NLS-1$
          }
          org.w3c.dom.Element wsdlElement = WSDLEditorUtil.getInstance().getElementForObject((WSDLElement) input);
          wsdlElement.setAttribute("message", itemPrefix.length() == 0 ? newName : itemPrefix + ":" + newName); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if (input instanceof Part) {
            Definition definition = ((Part) input).getEnclosingDefinition();
            String referencingPrefix = definition.getPrefix(definition.getTargetNamespace());
            String newName = dialog.getName();
            
            if (kind.equalsIgnoreCase("type")) {
                if (typeKind.equalsIgnoreCase("complex")) {
                    ((AddXSDTypeDefinitionCommand) command).isComplexType(true);    
                }
                else {
                    ((AddXSDTypeDefinitionCommand) command).isComplexType(false);
                }                
                ((AddXSDTypeDefinitionCommand) command).run(newName);
                ComponentReferenceUtil.setComponentReference((Part) input, true, referencingPrefix.equals("") ? newName : referencingPrefix + ":" + newName);
            }
            else if (kind.equalsIgnoreCase("element")) {
                ((AddXSDElementDeclarationCommand) command).run(newName);
                ComponentReferenceUtil.setComponentReference((Part) input, false, referencingPrefix.equals("") ? newName : referencingPrefix + ":" + newName);
            }
        }
        
        // We shouldn't know about the editor in this class
      WSDLEditor editor = (WSDLEditor) editorPart;
      editor.getSelectionManager().setSelection(new StructuredSelection(input));
    }
  }
  
  private List getUsedNames() {
      if (input instanceof Port) {
          Definition definition = ((Port) input).getEnclosingDefinition();
          return NameUtil.getUsedBindingNames(definition);
      }
      else if (input instanceof Binding) {
          Definition definition = ((Binding) input).getEnclosingDefinition();
          return NameUtil.getUsedPortTypeNames(definition);
      }
      else if (input instanceof Input || input instanceof Output || input instanceof Fault) {
          Definition definition = ((MessageReference) input).getEnclosingDefinition();
          return NameUtil.getUsedMessageNames(definition);
      }
      else if (input instanceof Part) {
          Definition definition = ((Part) input).getEnclosingDefinition();
          if (kind.equalsIgnoreCase("element")) {
              if (command == null) {
                  command = new AddXSDElementDeclarationCommand(definition, "NewElement");
              }
              XSDSchema parentSchema = ((AddXSDElementDeclarationCommand) command).getSchema();
              
              return getUsedElementNames(parentSchema);
          }
          else if (kind.equalsIgnoreCase("type")) {
              if (command == null) {
                  command = new AddXSDTypeDefinitionCommand(definition, "NewType");
              }
              XSDSchema parentSchema = ((AddXSDTypeDefinitionCommand) command).getSchema();
              
              List usedNames;
              if (typeKind.equalsIgnoreCase("complex")) {
                  usedNames = getUsedComplexTypeNames(parentSchema);    
              }
              else {
                  usedNames = getUsedSimpleTypeNames(parentSchema);
              }
              
              return usedNames;
          }
      }
      
      return (new ArrayList());
  }
  
  private String getDefaultName() {
      if (input instanceof Port) {
          newDialogTitle = "Create New Binding";       // TODO: Ugly Hack... remove
          Definition def = ((Port) input).getEnclosingDefinition();
          return NameUtil.getUniqueNameHelper("NewBinding", NameUtil.getUsedBindingNames(def));
      }
      else if (input instanceof Binding) {
          newDialogTitle = "Create New Port Type";       // TODO: Ugly Hack... remove
          Definition def = ((Binding) input).getEnclosingDefinition();
          return NameUtil.getUniqueNameHelper("NewPortType", NameUtil.getUsedPortTypeNames(def));
      }
      else if (input instanceof Input || input instanceof Output || input instanceof Fault) {
          newDialogTitle = "Create New Message";       // TODO: Ugly Hack... remove
          Definition def = ((MessageReference) input).getEnclosingDefinition();
          return NameUtil.getUniqueNameHelper("NewMessage", NameUtil.getUsedMessageNames(def));
      }
      else if (input instanceof Part) {
          Definition def = ((Part) input).getEnclosingDefinition();
          
          if (kind.equalsIgnoreCase("element")) {
              newDialogTitle = "Create New Element";       // TODO: Ugly Hack... remove
              if (command == null) {
                  command = new AddXSDElementDeclarationCommand(def, "NewElement");
              }
              XSDSchema parentSchema = ((AddXSDElementDeclarationCommand) command).getSchema();
              
              List usedNames = getUsedElementNames(parentSchema);
              return NameUtil.getUniqueNameHelper("NewElement", usedNames);
          }
          else if (kind.equalsIgnoreCase("type")) {
              if (command == null) {
                  command = new AddXSDTypeDefinitionCommand(def, "NewType");
              }
              XSDSchema parentSchema = ((AddXSDTypeDefinitionCommand) command).getSchema();

              String newItemName = "NewComplexType";
              List usedNames = null;
              if (typeKind.equalsIgnoreCase("complex")) {
                  newDialogTitle = "Create New Complex Type";       // TODO: Ugly Hack... remove
                  usedNames = getUsedComplexTypeNames(parentSchema);    
              }
              else {
                  newDialogTitle = "Create New Simple Type";       // TODO: Ugly Hack... remove
                  usedNames = getUsedSimpleTypeNames(parentSchema);
                  newItemName = "NewSimpleType";
              }
              
              return newItemName = NameUtil.getUniqueNameHelper(newItemName, usedNames);
          }
      }
      
      return "NewComponent";
  }
  
  private List getUsedSimpleTypeNames(XSDSchema schema) {
      List namesList = new ArrayList();
      
      Iterator typesIt = schema.getTypeDefinitions().iterator();
      while (typesIt.hasNext()) {
          XSDTypeDefinition type = (XSDTypeDefinition) typesIt.next();
          if (type instanceof XSDSimpleTypeDefinition) {
              namesList.add(type.getName());
          }
      }
      
      return namesList;
  }
  
  private List getUsedComplexTypeNames(XSDSchema schema) {
      List namesList = new ArrayList();
      
      Iterator typesIt = schema.getTypeDefinitions().iterator();
      while (typesIt.hasNext()) {
          XSDTypeDefinition type = (XSDTypeDefinition) typesIt.next();
          if (type instanceof XSDComplexTypeDefinition) {
              namesList.add(type.getName());
          }
      }
      
      return namesList;
  }
  
  private List getUsedElementNames(XSDSchema schema) {
      List namesList = new ArrayList();
      
      Iterator elementsIt = schema.getElementDeclarations().iterator();
      while (elementsIt.hasNext()) {
          XSDElementDeclaration element = (XSDElementDeclaration) elementsIt.next();
          namesList.add(element.getName());
      }
      
      return namesList;
  }
}
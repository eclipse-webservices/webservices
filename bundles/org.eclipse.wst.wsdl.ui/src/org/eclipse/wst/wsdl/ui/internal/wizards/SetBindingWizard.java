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

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;

public class SetBindingWizard extends Wizard
{
  protected final static int STYLE_NEW_BINDING = 1;
  protected final static int STYLE_EXISTING_BINDING = 1<<1;
  protected final static int STYLE_DEFAULT =  STYLE_NEW_BINDING | STYLE_EXISTING_BINDING; 

  
  protected Object input;
  private IEditorPart editorPart;
  private String kind;
  SetBindingOptionsPage newBindingOptionsPage;

  /**
   * Constructor for NewBindingWizard.
   */
  public SetBindingWizard(Object input, IEditorPart editorPart)
  {
    super();
    this.input = input;
    this.editorPart = editorPart;
//    setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(WSDLEditor.class, "icons/wsdl_file_obj.gif"));
  }

  /**
   * @see org.eclipse.jface.wizard.IWizard#performFinish()
   */
  public boolean performFinish()
  {
    org.w3c.dom.Element wsdlElement = WSDLEditorUtil.getInstance().getElementForObject((WSDLElement)input);    
    Definition definition = ((WSDLElement)input).getEnclosingDefinition();
    org.w3c.dom.Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
    if (newBindingOptionsPage.getChoice() == 2)
    {
      String choice = newBindingOptionsPage.getExistingListSelection(); 
      if (choice != null)
      {
        wsdlElement.setAttribute("binding", choice);
      }
    }
    else if (newBindingOptionsPage.getChoice() == 1)    
    {
	  newBindingOptionsPage.bindingGenerator.generate();      	
      String newName = newBindingOptionsPage.bindingGenerator.getName();
      
      /*
      String prefix = definitionElement.getPrefix();
      // Use AddMessageAction in WSDLMenuActionContributor
      AddElementAction addBindingAction = new AddElementAction("", "icons/message_obj.gif", definitionElement, prefix, "binding");
	  addBindingAction.setComputeTopLevelRefChild(true);
      addBindingAction.run();      
      org.w3c.dom.Element newMessage = addBindingAction.getNewElement();
      newMessage.setAttribute("name", newName);
      // need to get the prefix for the item
      // could get list of updated messages and then select but currently we only have the local name
       * 
       */
      String itemPrefix = definition.getPrefix(definition.getTargetNamespace());
      if (itemPrefix == null)
      {
        itemPrefix = "";
      }
      wsdlElement.setAttribute("binding", itemPrefix.length() == 0 ? newName : itemPrefix + ":" + newName);     
      
    }
    else if (newBindingOptionsPage.getChoice() == 3)
    {
      javax.wsdl.Binding selection = (javax.wsdl.Binding)newBindingOptionsPage.getSelection();
      String namespaceURI = selection.getQName().getNamespaceURI();
	  String uniquePrefix = newBindingOptionsPage.addWSDLImport(definition, definitionElement, namespaceURI);	
      wsdlElement.setAttribute("binding", uniquePrefix + ":" + selection.getQName().getLocalPart());
    }

	WSDLEditor editor = (WSDLEditor)editorPart;
	editor.getSelectionManager().setSelection(new StructuredSelection(input));
	
    return true;
  }

  public boolean canFinish()
  {
    if (newBindingOptionsPage.isPageComplete())
    {
      return true;
    }
    return false;
  }

  public Object getInput()
  {
    return input;
  }
 
  public void addPages()
  {
    newBindingOptionsPage = new SetBindingOptionsPage(editorPart, WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_BINDING"), WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_BINDING"), null);
    newBindingOptionsPage.setEditorPart(editorPart);
    newBindingOptionsPage.setInput(input);
    addPage(newBindingOptionsPage);
    setWindowTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_BINDING"));
  }
}

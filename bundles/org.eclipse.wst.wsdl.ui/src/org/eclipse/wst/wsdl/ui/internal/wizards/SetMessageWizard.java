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
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;

public class SetMessageWizard extends Wizard
{
  protected Object input;
  private IEditorPart editorPart;
  private String kind;
  SetMessageOptionsPage newMessageOptionsPage;

  /**
   * Constructor for NewMessageWizard.
   */
  public SetMessageWizard(Object input, IEditorPart editorPart)
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
    org.w3c.dom.Element wsdlElement = WSDLEditorUtil.getInstance().getElementForObject((WSDLElement)input);    
    Definition definition = ((WSDLElement)input).getEnclosingDefinition();
    org.w3c.dom.Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
    if (newMessageOptionsPage.getChoice() == 2)
    {
      String choice = newMessageOptionsPage.getExistingListSelection(); 
      if (choice != null)
      {
        wsdlElement.setAttribute("message", choice); //$NON-NLS-1$
      }
    }
    else if (newMessageOptionsPage.getChoice() == 1)    
    {
       String newName = newMessageOptionsPage.getNewName();

    	AddMessageCommand action = new AddMessageCommand(definition, newName);
    	action.run();
    	Message message = (Message) action.getWSDLElement();
    	CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.getPartInfo(message); 
    	CreateWSDLElementHelper.createPart(message);
      
/*      
      String prefix = definitionElement.getPrefix();
      // Use AddMessageAction in WSDLMenuActionContributor
      AddElementAction addMessageAction = new AddElementAction("", "icons/message_obj.gif", definitionElement, prefix, "message"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	  addMessageAction.setComputeTopLevelRefChild(true);
      addMessageAction.run();      
      org.w3c.dom.Element newMessage = addMessageAction.getNewElement();
      newMessage.setAttribute("name", newName); //$NON-NLS-1$
*/
      
      // need to get the prefix for the item
      // could get list of updated messages and then select but currently we only have the local name
      String itemPrefix = definition.getPrefix(definition.getTargetNamespace());
      if (itemPrefix == null)
      {
        itemPrefix = ""; //$NON-NLS-1$
      }
      wsdlElement.setAttribute("message", itemPrefix.length() == 0 ? newName : itemPrefix + ":" + newName); //$NON-NLS-1$ //$NON-NLS-2$
    }
    else if (newMessageOptionsPage.getChoice() == 3)
    {
       javax.wsdl.Message selection = (javax.wsdl.Message)newMessageOptionsPage.getSelection();
	   String namespaceURI = selection.getQName().getNamespaceURI();
	   String uniquePrefix = newMessageOptionsPage.addWSDLImport(definition, definitionElement, namespaceURI);	
	   wsdlElement.setAttribute("message", uniquePrefix + ":" + selection.getQName().getLocalPart());
    }           

    WSDLEditor editor = (WSDLEditor)editorPart;
    editor.getSelectionManager().setSelection(new StructuredSelection(input));

    return true;
  }

  public boolean canFinish()
  {
    if (newMessageOptionsPage.isPageComplete())
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
    newMessageOptionsPage = new SetMessageOptionsPage(editorPart, WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_MESSAGE"), WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_MESSAGE"), null); //$NON-NLS-1$ //$NON-NLS-2$
    newMessageOptionsPage.setEditorPart(editorPart);
    newMessageOptionsPage.setInput(input);
    addPage(newMessageOptionsPage);
    setWindowTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_MESSAGE")); //$NON-NLS-1$
  }
}
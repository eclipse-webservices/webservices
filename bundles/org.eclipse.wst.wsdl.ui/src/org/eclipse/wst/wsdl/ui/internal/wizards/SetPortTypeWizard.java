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
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementAction;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;

public class SetPortTypeWizard extends Wizard
{
  protected Object input;
  private IEditorPart editorPart;
  private String kind;
  private Definition definition;
  SetPortTypeOptionsPage newPortTypeOptionsPage;

  /**
   * Constructor for NewPortTypeWizard.
   */
  public SetPortTypeWizard(Object input, IEditorPart editorPart)
  {
    this(((WSDLElement)input).getEnclosingDefinition(), editorPart, input);
  }

  public SetPortTypeWizard(Definition definition, IEditorPart editorPart)
  {
    this(definition, editorPart, null);
  }

  private SetPortTypeWizard(Definition definition, IEditorPart editorPart, Object input)
  {
    super();
    this.input = input;
    this.definition = definition;
    this.editorPart = editorPart;
    // setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(WSDLEditor.class, "icons/wsdl_file_obj.gif"));
  }

  
  String portTypeValue = null;
  /**
   * @see org.eclipse.jface.wizard.IWizard#performFinish()
   */
  public boolean performFinish()
  {  
    //Definition definition = ((WSDLElement) input).getEnclosingDefinition();
    org.w3c.dom.Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
    portTypeValue = null;
    if (newPortTypeOptionsPage.getChoice() == 2)
    {
	  portTypeValue = newPortTypeOptionsPage.getExistingListSelection();    
    }
    else if (newPortTypeOptionsPage.getChoice() == 1)
    {
      String newName = newPortTypeOptionsPage.getNewName();
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
	  portTypeValue = itemPrefix.length() == 0 ? newName : itemPrefix + ":" + newName; //$NON-NLS-1$ //$NON-NLS-2$
    }
    else if (newPortTypeOptionsPage.getChoice() == 3)
    {
      javax.wsdl.PortType selection = (javax.wsdl.PortType) newPortTypeOptionsPage.getSelection();
      String namespaceURI = selection.getQName().getNamespaceURI();
      String uniquePrefix = newPortTypeOptionsPage.addWSDLImport(definition, definitionElement, namespaceURI);
	  portTypeValue = uniquePrefix + ":" + selection.getQName().getLocalPart();
    }
    
    if (input != null && portTypeValue != null)
    {
  	  org.w3c.dom.Element wsdlElement = WSDLEditorUtil.getInstance().getElementForObject((WSDLElement) input);
  	  wsdlElement.setAttribute("type", portTypeValue);
    } 

    WSDLEditor editor = (WSDLEditor) editorPart;
    editor.getSelectionManager().setSelection(new StructuredSelection(input));

    return true;
  }
  
  public String getPortTypeValue()
  {
    return portTypeValue;
  }

  public boolean canFinish()
  {
    if (newPortTypeOptionsPage.isPageComplete())
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
    newPortTypeOptionsPage = new SetPortTypeOptionsPage(editorPart, WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_PORTTYPE"), WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_PORTTYPE"), null); //$NON-NLS-1$ //$NON-NLS-2$
    newPortTypeOptionsPage.setEditorPart(editorPart);
    newPortTypeOptionsPage.setInput(input);
    addPage(newPortTypeOptionsPage);
    setWindowTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_PORTTYPE")); //$NON-NLS-1$
  }
}

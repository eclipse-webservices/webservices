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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.ui.dialogs.SelectSingleFileDialog;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class SetMessageOptionsPage extends SetOptionsPage
{
  public static String BUILTIN_MESSAGES_LOCATION_URI = "platform:/plugin/org.eclipse.wst.ctc.services/wsdl/org/xmlsoap/schemas/wsdl/wsadie/messages/BuiltinMessages.wsdl"; //$NON-NLS-1$

  /**
   * Constructor for NewMessageOptionsPage.
   * @param pageName
   * @param title
   * @param titleImage
   */
  public SetMessageOptionsPage(
    IEditorPart editorPart,
    String pageName,
    String title,
    ImageDescriptor titleImage)
  {
    super(editorPart, pageName, title, titleImage, "message");
  }

  protected void initNewNameTextField()
  {   
    Definition definition = ((WSDLElement)input).getEnclosingDefinition();
  	String  name = NameUtil.buildUniqueMessageName(definition, (MessageReference) input);
    newNameText.setText(name);
  }

  protected void initExistingNameList()
  {                    
    if (componentNameList.getItemCount() == 0)
    {
      componentNameList.removeAll();
      Definition definition = ((WSDLElement)input).getEnclosingDefinition();

      ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
      java.util.List currentList = util.getMessageNames();
      if (currentList.size() > 0)
      {
        for (Iterator it = currentList.iterator(); it.hasNext(); )
        {
          componentNameList.add((String)it.next());
        }
        componentNameList.select(0);
        existingListSelection = (componentNameList.getSelection())[0];
      }
    }
  }       

  protected void handleImport()
  {
    ResourceSet resourceSet = null;
// TODO: port check
//      resourceSet = ((org.eclipse.emf.ecore.EObject)input).getResourceSet();
      resourceSet = ((org.eclipse.emf.ecore.EObject)input).eResource().getResourceSet();
    
    SelectSingleFileDialog dialog = new SelectSingleFileDialog(getShell(), null, true);
    String [] filters = { "wsdl" };
    dialog.addFilterExtensions(filters);
    dialog.create();
    dialog.setTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT_WSDL_FILE")); //$NON-NLS-1$
    dialog.getShell().setText(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT")); //$NON-NLS-1$
    dialog.setMessage(WSDLEditorPlugin.getWSDLString("_UI_DESCRIPTION_SELECT_WSDL_FILE_TO_IMPORT")); //$NON-NLS-1$
    int rc = dialog.open();
    if (rc == IDialogConstants.OK_ID)
    {
      selectedFile = dialog.getFile();
      importComponents = loadFile(selectedFile, resourceSet);
      importList.removeAll();
      for (Iterator i = importComponents.iterator(); i.hasNext(); )
      {
        Message msg = (Message)i.next();
        importList.add(msg.getQName().getLocalPart()); 
      }
      fileText.setText(dialog.getFile().getFullPath().toString());
    }
  }

  public Collection getModelObjects(Object rootModelObject)
  {
	if (rootModelObject instanceof Definition)
	{
	  Definition definition = (Definition)rootModelObject;
	  return ((Definition)definition).getMessages().values();
	} 
	return Collections.EMPTY_LIST;
  }
}

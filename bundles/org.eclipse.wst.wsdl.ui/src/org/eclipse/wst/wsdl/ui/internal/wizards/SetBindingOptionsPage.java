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

import javax.wsdl.Binding;
import javax.wsdl.Port;


import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.ui.dialogs.SelectSingleFileDialog;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.widgets.ProtocolComponentControl;

public class SetBindingOptionsPage
  extends SetOptionsPage
{
  public BindingGenerator bindingGenerator;
  /**
   * Constructor for NewBindingOptionsPage.
   * @param pageName
   * @param title
   * @param titleImage
   */
  public SetBindingOptionsPage(
    IEditorPart editorPart,
    String pageName,
    String title,
    ImageDescriptor titleImage)
  {
    super(editorPart, pageName, title, titleImage, "binding");
  }
  
  public void createPage1(Composite pageBook)
  {
	page1 = new Composite(pageBook, SWT.NONE);
	GridLayout layout = new GridLayout();
	layout.marginWidth = 0;
	page1.setLayout(layout);
	Definition definition = ((WSDLElement)input).getEnclosingDefinition();
	bindingGenerator = new BindingGenerator(definition);
	ProtocolComponentControl control = new BindingWizard.BindingProtocolComponentControl(page1, bindingGenerator);
	control.initFields();
	newNameText = control.getComponentNameField();
	initNewNameTextField();
  }

  protected void initNewNameTextField()
  {
    Definition definition = ((WSDLElement)input).getEnclosingDefinition();
    Port port = (Port)input;
    if (port.getName() == null || (port.getName() != null && port.getName().length() ==0))
    {
      newNameText.setText(NameUtil.buildUniqueBindingName(definition, null));
    }
    else
    {
      newNameText.setText(port.getName());
    }
  }

  protected void initExistingNameList()
  {                    
    if (componentNameList.getItemCount() == 0)
    {
      componentNameList.removeAll();
      Definition definition = ((WSDLElement)input).getEnclosingDefinition();

      ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
      java.util.List currentList = util.getBindingNames();
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
        Binding msg = (Binding)i.next();
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
      return ((Definition)definition).getBindings().values();      
    } 
    return Collections.EMPTY_LIST;
  }
}

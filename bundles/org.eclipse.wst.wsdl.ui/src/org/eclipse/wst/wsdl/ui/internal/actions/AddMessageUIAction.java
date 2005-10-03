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


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.widgets.NewComponentDialog;
import org.eclipse.wst.wsdl.util.WSDLConstants;



public final class AddMessageUIAction extends WSDLElementUIAction
{
  private Definition definition;
  private String name;
  
  public AddMessageUIAction
  	(Definition definition, 
     String name, 
     boolean createPart)
  {
    super
    	(new AddMessageCommand(definition,name,createPart), 
    	 WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD"), 
    	 WSDLConstants.MESSAGE_ELEMENT_TAG, 
    	 WSDLEditorPlugin.getImageDescriptor("icons/message_obj.gif"));
    
    this.definition = definition;
    this.name = name;
  }
 
  protected boolean showDialog()
  {
	  name = NameUtil.buildUniqueMessageName(definition, name);
	  name = showDialogHelper(WSDLEditorPlugin.getWSDLString("_UI_ACTION_NEW_MESSAGE"), name);
	  return name != null;
  }
  
  protected String showDialogHelper(String title, String defaultName)
  {   
    String result = defaultName;                                                                                             
    NewComponentDialog dialog = new NewComponentDialog(WSDLEditorPlugin.getShell(), title, defaultName);
    int rc = dialog.createAndOpen();
    if (rc == IDialogConstants.OK_ID)
    {
      result = dialog.getName();  
    }
    else
    {
      result = null;
    }               
    return result;
  } 
  
  protected void preRun()
  {
    ((AddMessageCommand)super.modelAction).setLocalName(name);
  }

  protected WSDLElement getOwner()
  {
    return definition;
  }

}

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
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.dialogs.types.WSDLComponentSelectionDialog;
import org.eclipse.wst.wsdl.ui.internal.dialogs.types.WSDLComponentSelectionProvider;
import org.eclipse.wst.wsdl.ui.internal.dialogs.types.WSDLSetComponentHelper;
import org.eclipse.wst.wsdl.util.WSDLConstants;

public class InvokeSetDialog
{
  String kind;
  String newValue = "";
  
  public void setReferenceKind(String kind)
  {
    this.kind = kind;
  }
  
  public String getValue()
  {
    return newValue; 
  }
  
  public void run(Object input, IEditorPart editorPart)
  {
      WSDLComponentSelectionDialog dialog = null;
      Shell shell = Display.getCurrent().getActiveShell();
      IFile iFile = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
      Definition definition = ((WSDLElement) input).getEnclosingDefinition();
      String property = "";
      List lookupPaths = new ArrayList(2);
      
    if (input instanceof Binding)
    {
        String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_PORTTYPE");
        
        WSDLComponentSelectionProvider provider = new WSDLComponentSelectionProvider(iFile, definition, WSDLConstants.PORT_TYPE);
        dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, provider);
        provider.setDialog(dialog);
        property = "type";
    }
    else if (input instanceof Port)
    {
        String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_BINDING");
        
        WSDLComponentSelectionProvider provider = new WSDLComponentSelectionProvider(iFile, definition, WSDLConstants.BINDING);
        dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, provider);
        provider.setDialog(dialog);
        property = "binding";
    }
    else if (input instanceof Part)
    {
        List validExtensions = new ArrayList(2);
        validExtensions.add("wsdl");
        validExtensions.add("xsd");
        lookupPaths = new ArrayList(4);
        WSDLComponentSelectionProvider provider = null;
        if (kind.equalsIgnoreCase("type")) {
            property = ""; 
            String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_TYPE");
            
            provider = new WSDLComponentSelectionProvider(iFile, definition, WSDLConstants.TYPE, validExtensions);
            dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, provider);
        }
        else {
            property = ""; 
            String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_ELEMENT");
            
            provider = new WSDLComponentSelectionProvider(iFile, definition, WSDLConstants.ELEMENT, validExtensions);
            dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, provider);
        }
        provider.setDialog(dialog);
    }
    else if (input instanceof Input || input instanceof Output || input instanceof Fault)
    {
        String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_MESSAGE");

        WSDLComponentSelectionProvider provider = new WSDLComponentSelectionProvider(iFile, definition, WSDLConstants.MESSAGE);
        dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, provider);   
        provider.setDialog(dialog);
        property = "message";
    }

    dialog.setBlockOnOpen(true);
    dialog.create();

    if (dialog.open() == Window.OK) {
        WSDLSetComponentHelper helper = new WSDLSetComponentHelper(iFile, definition);
        if (kind != null && kind.equalsIgnoreCase("type")) {
            helper.setXSDTypeComponent((Part) input, dialog.getSelection());
        }
        else if (kind != null && kind.equalsIgnoreCase("element")){
            helper.setXSDElementComponent((Part) input, dialog.getSelection());
        }
        else {
            helper.setWSDLComponent((WSDLElement) input, property, dialog.getSelection());
        }
    } 
  }
}

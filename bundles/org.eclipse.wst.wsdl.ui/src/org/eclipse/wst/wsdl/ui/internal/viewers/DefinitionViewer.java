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
package org.eclipse.wst.wsdl.ui.internal.viewers;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.EditNamespacesAction;

public class DefinitionViewer extends NamedComponentViewer 
{                     
  public DefinitionViewer(Composite parent, IEditorPart editorPart)
  {
    super(parent, editorPart);    
  } 

  protected String getHeadingText()
  { 
    return WSDLEditorPlugin.getWSDLString("_UI_LABEL_DEFINITION"); //$NON-NLS-1$
  }  
    
  protected Composite populatePrimaryDetailsSection(Composite parent)
  {
	  Composite composite = super.populatePrimaryDetailsSection(parent);
	  flatViewUtility.createLabel(composite, 0, WSDLEditorPlugin.getWSDLString("_UI_NAMESPACES")); //$NON-NLS-1$
	  Button button = flatViewUtility.createPushButton(composite, WSDLEditorPlugin.getWSDLString("_UI_EDIT_NAMESPACES"));
	  SelectionListener listener = new SelectionListener()
	  {
		  public void widgetDefaultSelected(SelectionEvent event)
		  {    		
		  }
		  
		  public void widgetSelected(SelectionEvent event)
		  {
			  EditNamespacesAction action = new EditNamespacesAction((Definition)getInput());					
			  action.run();	
		  }	
	  };
	  button.addSelectionListener(listener);
	  return composite;	
  }	    
}
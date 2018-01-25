/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSearchListDialog;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSearchListDialogConfiguration;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.common.ui.internal.search.dialogs.ScopedComponentSearchListDialog;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLBindingSearchListProvider;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLComponentDescriptionProvider;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLInterfaceSearchListProvider;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLMessageSearchListProvider;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;

public class W11BrowseComponentDialog implements IComponentDialog {
	private QualifiedName qualifiedName;
	private ComponentSpecification selection;
	private W11Description description;
	
	public W11BrowseComponentDialog(QualifiedName qualifiedName, IFile iFile, W11Description description) {
		this.qualifiedName = qualifiedName;
		this.description = description;
	}
	
	public void setInitialSelection(ComponentSpecification componentSpecification) {
	}

	public ComponentSpecification getSelectedComponent() {
		return selection;
	}

	public int createAndOpen() {
		Definition definition = (Definition) description.getTarget();
		
	    Shell shell = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getShell();
	    int returnValue = Window.CANCEL;
	    ComponentSearchListDialog dialog = null;
	    if (qualifiedName == IWSDLSearchConstants.BINDING_META_NAME)
	    {
	    	WSDLComponentDescriptionProvider descriptionProvider = new WSDLComponentDescriptionProvider();
	    	final WSDLBindingSearchListProvider searchListProvider = new WSDLBindingSearchListProvider(definition);
	    	ComponentSearchListDialogConfiguration configuration = new ComponentSearchListDialogConfiguration();
	    	configuration.setFilterLabelText(Messages._UI_LABEL_NAME_FILTER);
	        configuration.setDescriptionProvider(descriptionProvider);
	        configuration.setSearchListProvider(searchListProvider);
	        
	        String dialogTitle = Messages._UI_TITLE_SPECIFY_BINDING; //$NON-NLS-1$
	        dialog = new ScopedComponentSearchListDialog(shell, dialogTitle, configuration);
	        
	        IFile file = getFile();
	        if (file != null) {
		        ((ScopedComponentSearchListDialog) dialog).setCurrentResource(file);
	        }
	    }
	    else if (qualifiedName == IWSDLSearchConstants.PORT_TYPE_META_NAME)
	    {
	      WSDLComponentDescriptionProvider descriptionProvider = new WSDLComponentDescriptionProvider();
	      final WSDLInterfaceSearchListProvider searchListProvider = new WSDLInterfaceSearchListProvider(definition);
	     
	      ComponentSearchListDialogConfiguration configuration = new ComponentSearchListDialogConfiguration();
	      configuration.setFilterLabelText(Messages._UI_LABEL_NAME_FILTER);
	      configuration.setDescriptionProvider(descriptionProvider);
	      configuration.setSearchListProvider(searchListProvider);
	      //configuration.setNewComponentHandler(new NewTypeButtonHandler());
	      
	      String dialogTitle = Messages._UI_TITLE_SPECIFY_PORTTYPE; //$NON-NLS-1$
	      dialog = new ScopedComponentSearchListDialog(shell, dialogTitle, configuration);
	      
	      IFile file = getFile();
	      if (file != null) {
	    	  ((ScopedComponentSearchListDialog) dialog).setCurrentResource(file);
	      }
	    }
	    else if (qualifiedName == IWSDLSearchConstants.MESSAGE_META_NAME)
	    {
	      WSDLComponentDescriptionProvider descriptionProvider = new WSDLComponentDescriptionProvider();
	      final WSDLMessageSearchListProvider searchListProvider = new WSDLMessageSearchListProvider(definition);
	     
	      ComponentSearchListDialogConfiguration configuration = new ComponentSearchListDialogConfiguration();
	      configuration.setFilterLabelText(Messages._UI_LABEL_NAME_FILTER);
	      configuration.setDescriptionProvider(descriptionProvider);
	      configuration.setSearchListProvider(searchListProvider);
	      //configuration.setNewComponentHandler(new NewTypeButtonHandler());
	      
	      String dialogTitle = Messages._UI_TITLE_SPECIFY_MESSAGE; //$NON-NLS-1$
	      dialog = new ScopedComponentSearchListDialog(shell, dialogTitle, configuration);
	      
	      IFile file = getFile();
	      if (file != null) {
	    	  ((ScopedComponentSearchListDialog) dialog).setCurrentResource(file);
	      }
	    }
	    
	    if (dialog != null)
	    {
	      dialog.setBlockOnOpen(true);
	      dialog.create();
	      returnValue = dialog.open();
	      if (returnValue == Window.OK)
	      {
	        selection = dialog.getSelectedComponent();
	      }
	    }
	    return returnValue;
	}
	
	private IFile getFile() {
		IFile file = null;
		IEditorInput input = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
        if (input instanceof IFileEditorInput) {
        	file = ((IFileEditorInput) input).getFile();
        }
        
        return file;
	}

}

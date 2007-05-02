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
package org.eclipse.wst.wsdl.ui.internal.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.common.uriresolver.internal.util.URIHelper;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.util.IOpenExternalEditorHelper;
import org.eclipse.wst.xsd.ui.internal.editor.InternalXSDMultiPageEditor;
import org.eclipse.wst.xsd.ui.internal.editor.XSDFileEditorInput;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;

public class W11OpenExternalEditorHelper implements IOpenExternalEditorHelper {
	private Object object;
	private IFile wsdlFile;
	
	public W11OpenExternalEditorHelper(IFile wsdlFile)
	{
		this.wsdlFile = wsdlFile;
	}
	
	public void setModel(Object object) {
		this.object = object;
	}
	
	public void openExternalEditor() {
		if (object instanceof WSDLBaseAdapter) {
			Object notifier = ((WSDLBaseAdapter) object).getTarget();
			Object openOnModel = getModelToOpenOn(notifier);
			
			if (openOnModel instanceof XSDConcreteComponent) {
				openXSDEditor((XSDConcreteComponent) openOnModel);
			}
		}
	}
	
	protected XSDSchema getSchema(XSDConcreteComponent xsdComponent) {
		XSDSchema schema = xsdComponent.getSchema();
		if (schema == null) {
			Object notifier = ((WSDLBaseAdapter) object).getTarget();
			if (notifier instanceof XSDConcreteComponent) {
				schema = ((XSDConcreteComponent) notifier).getSchema();
			}
		}
		
		return schema;
	}
	
	protected void openXSDEditor(XSDConcreteComponent xsdComponent) {
		XSDSchema schema = getSchema(xsdComponent);
		if (schema != null) {
			String schemaLocation = URIHelper.removePlatformResourceProtocol(schema.getSchemaLocation());
			IPath schemaPath = new Path(schemaLocation);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(schemaPath);
			if (file != null && file.exists()) {
				IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (workbenchWindow != null) {
					IWorkbenchPage page = workbenchWindow.getActivePage();
					try {
            
            IEditorPart editorPart = null;
            if (isInlineSchema(file)) {
              XSDFileEditorInput editorInput = new XSDFileEditorInput(file, schema);
					
							editorInput.setEditorName(Messages._UI_LABEL_INLINE_SCHEMA_OF + file.getName()); //$NON-NLS-1$
							IEditorReference [] refs = page.getEditorReferences();
							int length = refs.length;
							for (int i = 0; i < length; i++)
							{
								IEditorInput input = refs[i].getEditorInput();
								if (input instanceof XSDFileEditorInput)
								{
									IFile aFile = ((XSDFileEditorInput)input).getFile();
									if (aFile.getFullPath().equals(file.getFullPath()))
									{
										if (((XSDFileEditorInput)input).getSchema() == schema)
										{
											editorPart = refs[i].getEditor(true);
											page.activate(refs[i].getPart(true));
											break;
										}
									}
								}
							}
							
							if (editorPart == null)
							{
								editorPart = page.openEditor(editorInput, "org.eclipse.wst.xsd.ui.internal.editor.InternalXSDMultiPageEditor", true, 0); //$NON-NLS-1$
							}
						}
						else {
              // Should open in default editor
              editorPart = IDE.openEditor(page, file, true);
              // editorPart = page.openEditor(new FileEditorInput(file), "org.eclipse.wst.xsd.ui.internal.editor.InternalXSDMultiPageEditor", true); //$NON-NLS-1$
						}
						
						if (editorPart instanceof InternalXSDMultiPageEditor)
						{
							InternalXSDMultiPageEditor xsdEditor = (InternalXSDMultiPageEditor)editorPart;
							xsdEditor.openOnGlobalReference(xsdComponent);
						}						
					}
					catch (PartInitException pie) {
//						Logger.log(Logger.WARNING_DEBUG, pie.getMessage(), pie);
					}
				}
			}
		}	        
	}
	
	public boolean linkApplicable() {
		boolean applicable = true;
		
		if (object instanceof IParameter) {
			IParameter param = (IParameter) object;
			String prefix = param.getComponentNameQualifier();
			if (prefix != null && prefix.equals(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001)) {
				applicable = false;
			}
		}
		
		return applicable;
	}
	
	protected Object getModelToOpenOn(Object object) {
		if (object instanceof XSDElementDeclaration) {
			XSDElementDeclaration xsdElement = ((XSDElementDeclaration) object).getResolvedElementDeclaration();
			return xsdElement.getTypeDefinition();
		}
		else if (object instanceof Part) {
			Object elementOrType = ((Part) object).getElementDeclaration();
			if (elementOrType == null) {
				elementOrType = ((Part) object).getTypeDefinition();
			}
			
			return elementOrType;
		}
		
		return object;
	}
	
	/**
	 * @param file
	 * @return
	 */
	protected boolean isInlineSchema(IFile file) {
		// Should there be a better test for this?  The IFiles are different so we can't use file == wsdlFile.
		return file.getFullPath().equals(wsdlFile.getFullPath());
	}
	
	public boolean isValid() {
		if (object instanceof WSDLBaseAdapter) {
			Object notifier = ((WSDLBaseAdapter) object).getTarget();
			Object openOnModel = getModelToOpenOn(notifier);

			// We check to ensure it's element != null.  If it does, then it's a sign of a
			// bad reference (is invalid).
			if (openOnModel instanceof XSDConcreteComponent && ((XSDConcreteComponent) openOnModel).getElement() != null) {
				XSDConcreteComponent xsdComponent = (XSDConcreteComponent) openOnModel;

				XSDSchema schema = getSchema(xsdComponent);
				if (schema != null) {
					String schemaLocation = URIHelper.removePlatformResourceProtocol(schema.getSchemaLocation());
					IPath schemaPath = new Path(schemaLocation);
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(schemaPath);
					if (file != null && file.exists()) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public void showPreview() {
		W11ParameterForPart param = (W11ParameterForPart) object;
		Object xsdModel = null;
		String title = null;
		String info = null;
		if (param.isType()) {
			XSDTypeDefinition type = ((Part)param.getTarget()).getTypeDefinition();
			xsdModel = type;
			title = type.getName();
			info = type.getTargetNamespace(); 
		}
		else {
			XSDElementDeclaration elem = ((Part)param.getTarget()).getElementDeclaration();
			xsdModel = elem;
			title = elem.getName();
			info = elem.getTargetNamespace(); 
		}
		XSDGraphViewerDialog dialog = new XSDGraphViewerDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, info, xsdModel);
		dialog.setOpenExternalEditor(this);
		dialog.create();
		dialog.open();
		dialog.getShell().setFocus();
	}
}

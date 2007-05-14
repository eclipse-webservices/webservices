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
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.uriresolver.internal.util.URIHelper;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.util.IOpenExternalEditorHelper;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.wst.xsd.ui.internal.dialogs.IOpenInNewEditor;
import org.eclipse.wst.xsd.ui.internal.dialogs.XSDGraphViewerDialog;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDConstants;

public class W11OpenExternalEditorHelper implements IOpenExternalEditorHelper, IOpenInNewEditor {
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
			  XSDConcreteComponent xsdComponent = (XSDConcreteComponent) openOnModel;
			  XSDSchema schema = getSchema(xsdComponent);
			  if (schema.eResource() instanceof WSDLResourceImpl)
			  {
			    String fileName = schema.eResource().getURI().lastSegment();
			    if (fileName == null) fileName = "WSDL"; //$NON-NLS-1$
			    String editorName = Messages._UI_LABEL_INLINE_SCHEMA_OF + fileName;
			    XSDGraphViewerDialog.openNonXSDResourceSchema(xsdComponent, schema, editorName);
			  }
			  else
			  {
				  XSDGraphViewerDialog.openXSDEditor(xsdComponent);
			  }
			}
		}
	}
	
  public void openXSDEditor()
  {
    openExternalEditor();  
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
    Object xsdModel = null;
    String title = "";
    String info = "";

    Object notifier = ((WSDLBaseAdapter) object).getTarget();
    xsdModel = getModelToOpenOn(notifier);
    if (xsdModel instanceof XSDNamedComponent)
    {
      XSDNamedComponent namedComponent = (XSDNamedComponent) xsdModel;
      title = namedComponent.getName();
      info = namedComponent.getTargetNamespace();
    }
    
    if (isValid() && xsdModel != null)
    {
  		XSDGraphViewerDialog dialog = new XSDGraphViewerDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, info, xsdModel, "org.eclipse.wst.wsdl.ui.preview");
  		dialog.setOpenExternalEditor(this);
      dialog.open();
    }
	}
}

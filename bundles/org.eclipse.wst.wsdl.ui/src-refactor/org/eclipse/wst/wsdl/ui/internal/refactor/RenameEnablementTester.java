/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.refactor;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.NodeAssociationManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;

public class RenameEnablementTester extends PropertyTester {
	public static final String RENAME_ENABLED = "renameEnabled"; //$NON-NLS-1$
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (!RENAME_ENABLED.equals(property)) return false;
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) return false; 

		IWorkbenchPage activePage = window.getActivePage(); 
		if (activePage == null) return false;

		IEditorPart editor = activePage.getActiveEditor();	
		if (editor == null) return false;

		Definition definition = (Definition) editor.getAdapter(Definition.class);
		if (definition == null) return false;

		return canEnable(definition, receiver);
	}

	private boolean canEnable(Definition definition, Object selectedObject) {
		if (selectedObject instanceof IStructuredSelection) {
			IStructuredSelection fStructuredSelection = (IStructuredSelection) selectedObject;
			selectedObject = fStructuredSelection.getFirstElement();

			if (selectedObject instanceof WSDLBaseAdapter) {
				WSDLBaseAdapter wsdlObject = (WSDLBaseAdapter) selectedObject;
				
				// do not enable for read-only objects
				if (wsdlObject.isReadOnly()) {
					return false;
				}
				
				selectedObject = wsdlObject.getTarget();
			}

			if (selectedObject instanceof XSDNamedComponent) {
				return doCanEnableXSDSelection((XSDNamedComponent) selectedObject);
			} else if(selectedObject instanceof WSDLElementImpl){
				return doCanEnableWSDLSelection((WSDLElementImpl) selectedObject);
			}
			if (selectedObject instanceof Element) {
				Element node = (Element) selectedObject;
				if (definition != null) {
					// issue (eb)  dependency on utility class to get component from the model based on element
					Object concreteComponent = 	(new NodeAssociationManager()).getModelObjectForNode(definition, node);
					return canEnable(definition, concreteComponent);
				}
			}
		}
		return false;
	}

	private boolean doCanEnableXSDSelection(XSDNamedComponent selectedObject) {
		if ( !(selectedObject.getElement() instanceof IDOMElement)) {
			return false;
		}

		// if it's element reference, then this action is not appropriate
		if (selectedObject instanceof XSDElementDeclaration) {
			XSDElementDeclaration element = (XSDElementDeclaration) selectedObject;
			if (element.isElementDeclarationReference()) {
				return false;
			}
		}

		if(selectedObject instanceof XSDTypeDefinition){
			XSDTypeDefinition type = (XSDTypeDefinition) selectedObject;
			XSDConcreteComponent parent = type.getContainer();
			if (parent instanceof XSDElementDeclaration) {
				XSDElementDeclaration element = (XSDElementDeclaration) parent;
				if(element.getAnonymousTypeDefinition().equals(type)){
					return false;
				}
			}
			else if(parent instanceof XSDAttributeDeclaration) {
				XSDAttributeDeclaration element = (XSDAttributeDeclaration) parent;
				if(element.getAnonymousTypeDefinition().equals(type)){
					return false;
				}
			}
		}

		return true;
	}

	private boolean doCanEnableWSDLSelection(WSDLElement selectedObject) {
		if (selectedObject instanceof Binding ||
				selectedObject instanceof PortType ||
				selectedObject instanceof Message) {
			return true;
		} else {
			return false;
		}
	}

}

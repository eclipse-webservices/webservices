/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.refactor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.GlobalBuildAction;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.wst.common.ui.internal.dialogs.SaveDirtyFilesDialog;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.NodeAssociationManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringMessages;
import org.eclipse.wst.xsd.ui.internal.refactor.XMLRefactoringComponent;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.RenameComponentProcessor;
import org.eclipse.wst.xsd.ui.internal.refactor.wizard.RenameRefactoringWizard;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;

public class RenameHandler extends AbstractHandler implements IHandler {	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (workbenchWindow == null) return null;

		IWorkbenchPage activePage = workbenchWindow.getActivePage();
		if (activePage == null) return null;

		IEditorPart editor = activePage.getActiveEditor();
		if (editor == null) return null;
		
		Definition definition = (Definition) editor.getAdapter(Definition.class);
		if (definition == null) return null;
		
		ISelection selection = activePage.getSelection();	
		return execute(definition, selection);
	}

	public Object execute(Definition definition, ISelection selection) {
		if ( ! (selection instanceof IStructuredSelection)) return null;
		
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object element = structuredSelection.getFirstElement();	
		if ( ! (element instanceof WSDLBaseAdapter || element instanceof WSDLElement)) return null;
		

		
		if (element instanceof WSDLBaseAdapter) {
			// convert the WSDLBaseAdapter (EMF) to WSDLElement
			element = ((WSDLBaseAdapter) element).getTarget();
		} 
		XMLRefactoringComponent selectedComponent = getRefactoringComponent(definition, element);
		if (selectedComponent != null) {
			run(definition, selectedComponent);
		}
				
		return null;
	}

	protected XMLRefactoringComponent getXSDRefactoringComponent(XSDNamedComponent selectedObject) {

		XMLRefactoringComponent selectedComponent = null;
		if ( selectedObject.getElement() instanceof IDOMElement) {
			selectedComponent = new XMLRefactoringComponent(
					selectedObject,
					(IDOMElement)selectedObject.getElement(), 
					selectedObject.getName(),
					selectedObject.getTargetNamespace());
	
			// if it's element reference, then this action is not appropriate
			if (selectedObject instanceof XSDElementDeclaration) {
				XSDElementDeclaration element = (XSDElementDeclaration) selectedObject;
				if (element.isElementDeclarationReference()) {
					selectedComponent = null;
				}
			}
			if(selectedObject instanceof XSDTypeDefinition){
				XSDTypeDefinition type = (XSDTypeDefinition) selectedObject;
				XSDConcreteComponent parent = type.getContainer();
				if (parent instanceof XSDElementDeclaration) {
					XSDElementDeclaration element = (XSDElementDeclaration) parent;
					if(element.getAnonymousTypeDefinition().equals(type)){
						selectedComponent = null;
					}
				}
				else if(parent instanceof XSDAttributeDeclaration) {
					XSDAttributeDeclaration element = (XSDAttributeDeclaration) parent;
					if(element.getAnonymousTypeDefinition().equals(type)){
						selectedComponent = null;
					}
				}
			}
		}

		return selectedComponent;
	}
	
	protected XMLRefactoringComponent getWSDLRefactoringComponent(WSDLElement selectedObject) {

		XMLRefactoringComponent selectedComponent = null;
		String localName = null;
		String namespace = null; 
		if (selectedObject instanceof Binding){
			localName = ((Binding)selectedObject).getQName().getLocalPart();
			namespace = ((Binding)selectedObject).getQName().getNamespaceURI();
		}
		else if (selectedObject instanceof PortType){
			localName = ((PortType)selectedObject).getQName().getLocalPart();
			namespace = ((PortType)selectedObject).getQName().getNamespaceURI();
		}
		else if (selectedObject instanceof Message){
			localName = ((Message)selectedObject).getQName().getLocalPart();
			namespace = ((Message)selectedObject).getQName().getNamespaceURI();
		}
		if(localName != null){
			selectedComponent = new XMLRefactoringComponent(
					selectedObject,
					(IDOMElement)selectedObject.getElement(), 
					localName,
					namespace);
		}
		
		return selectedComponent;
	}

	protected XMLRefactoringComponent getRefactoringComponent(Definition definition, Object selectedObject) {

		if (selectedObject instanceof XSDNamedComponent) {
			return getXSDRefactoringComponent((XSDNamedComponent) selectedObject);
		} else if(selectedObject instanceof WSDLElementImpl){
			return getWSDLRefactoringComponent((WSDLElementImpl) selectedObject);
		}
		if (selectedObject instanceof Element) {
			Element node = (Element) selectedObject;
			if (definition != null) {
				// issue (eb)  dependency on utility class to get component from the model based on element
				Object concreteComponent = 	(new NodeAssociationManager()).getModelObjectForNode(definition, node);
				return getRefactoringComponent(definition, concreteComponent);
			}
		}
		
		return null;
	}


	public void run(final Definition definition, XMLRefactoringComponent selectedComponent) {
	
        boolean rc = SaveDirtyFilesDialog.saveDirtyFiles();
        if (!rc)
        {
          return;
        }  
		RenameComponentProcessor processor = new RenameComponentProcessor(selectedComponent, selectedComponent.getName());
		RenameRefactoring refactoring = new RenameRefactoring(processor);
		try {
			RefactoringWizard wizard = new RenameRefactoringWizard(
					refactoring,
					RefactoringMessages
					.getString("RenameComponentWizard.defaultPageTitle"), //$NON-NLS-1$ TODO: provide correct strings
					RefactoringMessages
					.getString("RenameComponentWizard.inputPage.description"), //$NON-NLS-1$
					null)
                    {
                      public boolean performFinish()
                      {
                        boolean rc = super.performFinish();
                        //((DefinitionImpl)getDefinition()).reconcileReferences(true);
                        return rc;
                      }
              
                    };        
			RefactoringWizardOpenOperation operation = new RefactoringWizardOpenOperation(
					wizard);
			operation.run(WSDLEditorPlugin.getShell(), wizard
					.getDefaultPageTitle());
			triggerBuild();
            
			Display.getCurrent().asyncExec(new Runnable()
			{			  
			  public void run()
			  {
			    ((DefinitionImpl)definition).reconcileReferences(true);
			  }
			});  
            
		} catch (InterruptedException e) {
			// do nothing. User action got cancelled
		}
		
		if (definition instanceof DefinitionImpl) {
			((DefinitionImpl) definition).reconcileReferences(true);
		}
	}

	public static void triggerBuild() {
		if (ResourcesPlugin.getWorkspace().getDescription().isAutoBuilding()) {
			new GlobalBuildAction(WSDLEditorPlugin.getInstance().getWorkbench()
					.getActiveWorkbenchWindow(),
					IncrementalProjectBuilder.INCREMENTAL_BUILD).run();
		}
	}
}

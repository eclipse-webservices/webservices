/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.refactor.actions;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.GlobalBuildAction;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.NodeAssociationManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xsd.ui.internal.refactor.RefactoringComponent;
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

public class RenameComponentAction extends WSDLSelectionDispatchAction {

	private RefactoringComponent selectedComponent;

	public RenameComponentAction(ISelection selection,
			Definition model) {
		super(selection);
		setModel(model);
	}
 
	protected boolean doCanEnableXSDSelection(XSDNamedComponent selectedObject) {

		selectedComponent = null;
		if (selectedObject != null) {
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

		return canRun();
	}
	
	protected boolean doCanEnableWSDLSelection(WSDLElement selectedObject) {

		selectedComponent = null;
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
		
	
		return canRun();
	}

	protected boolean canEnable(Object selectedObject) {

		if (selectedObject instanceof XSDNamedComponent) {
			return doCanEnableXSDSelection((XSDNamedComponent) selectedObject);
		} else if(selectedObject instanceof WSDLElementImpl){
			return doCanEnableWSDLSelection((WSDLElementImpl) selectedObject);
		}
		if (selectedObject instanceof Element) {
			Element node = (Element) selectedObject;
			if (getDefinition() != null) {
				// issue (eb)  dependency on utility class to get component from the model based on element
				Object concreteComponent = 	(new NodeAssociationManager()).getModelObjectForNode(getDefinition(), node);
				return canEnable(concreteComponent);
			}
		}
		return false;

	}

	public boolean canRun() {

		return selectedComponent != null;
	}

	public void run(ISelection selection) {
	
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
                        // TODO Auto-generated method stub
                        boolean rc = super.performFinish();
                        //((DefinitionImpl)getDefinition()).reconcileReferences(true);
                        return rc;
                      }
              
                    };        
			RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(
					wizard);
			op.run(WSDLEditorPlugin.getShell(), wizard
					.getDefaultPageTitle());
			triggerBuild();
            
			Display.getCurrent().asyncExec(new Runnable()
			{			  
			  public void run()
			  {
			    ((DefinitionImpl)getDefinition()).reconcileReferences(true);
			  }
			});  
            
		} catch (InterruptedException e) {
			// do nothing. User action got cancelled
		}
		
		if (getModel() instanceof DefinitionImpl) {
			((DefinitionImpl) getModel()).reconcileReferences(true);
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

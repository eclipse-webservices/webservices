/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.outline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.actions.WSDLMenuListener;
import org.eclipse.wst.wsdl.ui.internal.text.WSDLModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WSDLContentOutlineConfiguration extends ContentOutlineConfiguration {
	private ExtensibleOutlineProvider fOutlineProvider = null;
	private KeyListener[] fKeyListeners = null;
	private IMenuListener fMenuListener = null;
	private WSDLEditor fEditor = null;

	private ExtensibleOutlineProvider getOutlineProvider() {
		if (fOutlineProvider == null) {
			// ISSUE: what happens if cannot get WSDL Editor? (See
			// getWSDLEditor comment)
			fOutlineProvider = new ExtensibleOutlineProvider(getWSDLEditor());
		}
		return fOutlineProvider;
	}

	public IContentProvider getContentProvider(TreeViewer viewer) {
		return getOutlineProvider();
	}

	public ILabelProvider getLabelProvider(TreeViewer viewer) {
		return getOutlineProvider();
	}

	public KeyListener[] getKeyListeners(TreeViewer viewer) {
		if (fKeyListeners == null) {
			final TreeViewer finalViewer = viewer;
			KeyAdapter keyListener = new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == SWT.F3 && getWSDLEditor() != null) {
						ISelection selection = getWSDLEditor().getSelectionManager().getSelection();
						if (selection instanceof IStructuredSelection) {
							Object object = ((IStructuredSelection) selection).getFirstElement();
							if (object instanceof EObject) {
								OpenOnSelectionHelper helper = new OpenOnSelectionHelper(getDefinition(finalViewer));
								helper.openEditor((EObject) object);
							}
						}
					}
				}
			};
			fKeyListeners = new KeyListener[]{keyListener};
		}

		return fKeyListeners;
	}

	public IMenuListener getMenuListener(TreeViewer viewer) {
		if (fMenuListener == null) {
			// ISSUE: what happens if cannot get WSDL Editor? (See
			// getWSDLEditor comment)
			if (getWSDLEditor() != null)
				fMenuListener = new WSDLMenuListener(getWSDLEditor(), getWSDLEditor().getSelectionManager());
		}
		return fMenuListener;
	}

	public ISelection getSelection(TreeViewer viewer, ISelection selection) {
		ISelection sel = selection;

		if (selection instanceof IStructuredSelection) {
			List wsdlSelections = new ArrayList();
			for (Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
				Object domNode = i.next();
				Object wsdlNode = getWSDLNode(domNode, viewer);
				if (wsdlNode != null) {
					wsdlSelections.add(wsdlNode);
				}
			}

			if (!wsdlSelections.isEmpty()) {
				sel = new StructuredSelection(wsdlSelections);
			}
		}
		return sel;
	}

	/**
	 * Gets the definition from treeviewer's input
	 * 
	 * @param model
	 *            (of type Object but really should be IStructuredModel)
	 * @return Definition
	 */
	private Definition getDefinition(TreeViewer viewer) {
		Definition definition = null;
		Object model = null;
		if (viewer != null)
			model = viewer.getInput();

		if (model instanceof IDOMModel) {
			IDOMDocument domDoc = ((IDOMModel) model).getDocument();
			if (domDoc != null) {
				WSDLModelAdapter modelAdapter = (WSDLModelAdapter) domDoc.getAdapterFor(WSDLModelAdapter.class);

				/*
				 * ISSUE: if adapter does not already exist for domDoc
				 * getAdapterFor will create one. So why is this null
				 * check/creation needed?
				 */
				if (modelAdapter == null) {
					modelAdapter = new WSDLModelAdapter();
					domDoc.addAdapter(modelAdapter);
					modelAdapter.createDefinition(domDoc.getDocumentElement(), domDoc);
				}

				definition = modelAdapter.getDefinition();
			}
		}
		return definition;
	}

	// ISSUE: There are some cases where outline comes up before editor
	private WSDLEditor getWSDLEditor() {
		if (fEditor == null) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			if (workbench != null) {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					IWorkbenchPage page = window.getActivePage();
					if (page != null) {
						IEditorPart editor = page.getActiveEditor();
						if (editor instanceof WSDLEditor)
							fEditor = (WSDLEditor) editor;
					}
				}
			}
		}
		return fEditor;
	}

	public void unconfigure(TreeViewer viewer) {
		super.unconfigure(viewer);
		fEditor = null;
	}

	/**
	 * Determines WSDL node based on object (DOM node)
	 * 
	 * @param object
	 * @return
	 */
	private Object getWSDLNode(Object object, TreeViewer viewer) {
		// get the element node
		Element element = null;
		if (object instanceof Node) {
			Node node = (Node) object;
			if (node != null) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element) node;
				}
				else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
					element = ((Attr) node).getOwnerElement();
				}
			}
		}
		Object o = element;
		if (element != null) {
			Object modelObject = WSDLEditorUtil.getInstance().findModelObjectForElement(getDefinition(viewer), element);
			if (modelObject != null) {
				o = modelObject;
			}
		}
		return o;
	}
}

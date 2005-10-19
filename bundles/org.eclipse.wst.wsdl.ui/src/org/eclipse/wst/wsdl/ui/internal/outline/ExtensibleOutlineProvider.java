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
package org.eclipse.wst.wsdl.ui.internal.outline;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.text.WSDLModelAdapter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;


public class ExtensibleOutlineProvider implements ITreeContentProvider, ILabelProvider {
	protected WSDLEditorExtension[] treeContentProviderExtensions;
	protected ITreeContentProvider[] treeContentProviders;

	protected WSDLEditorExtension[] labelProviderExtensions;
	protected ILabelProvider[] labelProviders;

	protected final static Object[] EMPTY_ARRAY = {};

	public ExtensibleOutlineProvider(WSDLEditor wsdlEditor) {
		WSDLEditorExtensionRegistry registry = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry();

		treeContentProviderExtensions = registry.getRegisteredExtensions(WSDLEditorExtension.OUTLINE_TREE_CONTENT_PROVIDER);
		treeContentProviders = new ITreeContentProvider[treeContentProviderExtensions.length];
		for (int i = 0; i < treeContentProviderExtensions.length; i++) {
			treeContentProviders[i] = (ITreeContentProvider) treeContentProviderExtensions[i].createExtensionObject(WSDLEditorExtension.OUTLINE_TREE_CONTENT_PROVIDER, wsdlEditor);
		}

		labelProviderExtensions = registry.getRegisteredExtensions(WSDLEditorExtension.OUTLINE_LABEL_PROVIDER);
		labelProviders = new ILabelProvider[labelProviderExtensions.length];
		for (int i = 0; i < labelProviderExtensions.length; i++) {
			labelProviders[i] = (ILabelProvider) labelProviderExtensions[i].createExtensionObject(WSDLEditorExtension.OUTLINE_LABEL_PROVIDER, wsdlEditor);
		}
	}


	protected ITreeContentProvider getApplicableTreeContentProvider(Object object) {
		ITreeContentProvider provider = null;
		for (int i = 0; i < treeContentProviderExtensions.length; i++) {
			if (treeContentProviderExtensions[i].isApplicable(object)) {
				provider = treeContentProviders[i];
				if (provider != null) {
					break;
				}
			}
		}
		return provider;
	}


	protected ILabelProvider getApplicableLabelProvider(Object object) {
		ILabelProvider provider = null;
		for (int i = 0; i < labelProviderExtensions.length; i++) {
			if (labelProviderExtensions[i].isApplicable(object)) {
				provider = labelProviders[i];
				if (provider != null) {
					break;
				}
			}
		}
		return provider;
	}

	// implements ITreeContentProvider
	//
	public Object[] getChildren(Object parentElement) {
		ITreeContentProvider provider = getApplicableTreeContentProvider(parentElement);
		// System.out.println("getElements " + provider);
		return provider != null ? provider.getChildren(parentElement) : EMPTY_ARRAY;
	}

	public Object getParent(Object element) {
		ITreeContentProvider provider = getApplicableTreeContentProvider(element);
		return provider != null ? provider.getParent(element) : null;
	}

	public boolean hasChildren(Object element) {
		ITreeContentProvider provider = getApplicableTreeContentProvider(element);
		return provider != null ? provider.hasChildren(element) : false;
	}

	public Object[] getElements(Object inputElement) {
		// inputElement is initially a structured model, so turn it into a
		// definition
		Definition definition = getDefinition(inputElement);
		ITreeContentProvider provider = getApplicableTreeContentProvider(definition);
		// System.out.println("getElements " + provider);
		return provider != null ? provider.getElements(definition) : EMPTY_ARRAY;
	}

	public void dispose() {
		// TODO... call dispose dispose the created label and content
		// providers
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		Definition definition = getDefinition(newInput);
		for (int i = 0; i < treeContentProviders.length; i++) {
			treeContentProviders[i].inputChanged(viewer, oldInput, definition);
		}
	}

	// implements ILabelProvider
	//
	public Image getImage(Object element) {
		ILabelProvider provider = getApplicableLabelProvider(element);
		return provider != null ? provider.getImage(element) : null;
	}

	public String getText(Object element) {
		String result = null;
		if (element != null) {
			ILabelProvider provider = getApplicableLabelProvider(element);
			result = provider != null ? provider.getText(element) : (element.toString() + "noProviderForClass=" + element.getClass().getName());
		}
		return result != null ? result : "";
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void removeListener(ILabelProviderListener listener) {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * Gets the definition from model
	 * 
	 * @param model
	 *            (of type Object but really should be IStructuredModel)
	 * @return Definition
	 */
	private Definition getDefinition(Object model) {
		Definition definition = null;

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
					modelAdapter.createDefinition(domDoc.getDocumentElement());
				}

				definition = modelAdapter.getDefinition();
			}
		}
		return definition;
	}
}

/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddBindingCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddImportCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddInterfaceCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddSchemaCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddServiceCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11EditNamespacesCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddServiceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceInfoManager;
import org.w3c.dom.Element;

public class W11Description extends WSDLBaseAdapter implements IDescription {
	protected List categories = new ArrayList();
	
	protected W11CategoryAdapter getCategory(int categoryId) {
		return getCategory(categoryId, getCategoryAdapters());
	}
	
	protected W11CategoryAdapter getCategory(int categoryId, List list) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			W11CategoryAdapter adapter = (W11CategoryAdapter) it.next();
			if (adapter.getGroupType() == categoryId) {
				return adapter;
			}
		}
		
		return null;
	}
	
	protected List getCategoryAdapters() {
	    // just set categoryadapters' children if category adapters are
	    // already created
	    if (categories.size() == 0) {
	    	categories = createCategoryAdapters();
	    }
	    else {
	    	W11CategoryAdapter category = getCategory(W11CategoryAdapter.IMPORTS, categories);
	    	category.setChildren(getImports());
	    	
	    	category = getCategory(W11CategoryAdapter.TYPES, categories);
	    	addListenerToTypes(category);

	    	category = getCategory(W11CategoryAdapter.SERVICES, categories);
	    	category.setChildren(getServices());

	    	category = getCategory(W11CategoryAdapter.BINDINGS, categories);
	    	category.setChildren(getBindings());

	    	category = getCategory(W11CategoryAdapter.INTERFACES, categories);
	    	category.setChildren(getInterfaces());

	    	category = getCategory(W11CategoryAdapter.MESSAGES, categories);
	    	category.setChildren(getMessages());
	    }
    	return categories;
	}
	
	// TODO: rmah: right now it looks like the definition needs to know too much about the categories
	// We should make the categories more self-sufficient.... so it knows how to compute it's children
	// based on it's kind
	protected List createCategoryAdapters() {
		List categories = new ArrayList();
		
		List importList = getImports();
//		List schemaList = getTypes();
		List serviceList = getServices();
		List bindingList = getBindings();
		List interfaceList = getInterfaces();
		List messageList = getMessages();

		String categoryTitle = W11CategoryAdapter.IMPORTS_HEADER_TEXT;
		Image categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/importheader_obj.gif"); //$NON-NLS-1$
		W11CategoryAdapter category = new W11CategoryAdapter(this, categoryTitle, categoryImage, importList, W11CategoryAdapter.IMPORTS);
		registerListener(category);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.TYPES_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/types_obj.gif"); //$NON-NLS-1$
		category = new W11TypesCategoryAdapter(this, categoryTitle, categoryImage, W11CategoryAdapter.TYPES);
		addListenerToTypes(category);
		registerListener(category);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.SERVICE_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/serviceheader_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, serviceList, W11CategoryAdapter.SERVICES);
		registerListener(category);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.BINDING_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/bindingheader_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, bindingList, W11CategoryAdapter.BINDINGS);
		registerListener(category);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.INTERFACE_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/porttypeheader_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, interfaceList, W11CategoryAdapter.INTERFACES);
		registerListener(category);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.MESSAGE_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/messageheader_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, messageList, W11CategoryAdapter.MESSAGES);
		registerListener(category);
		categories.add(category);
		
		return categories;
	  }
	
	// Special case:
	// We need to have the Types object inform our facade (W11TypesCategoryAdapter) of it's
	// changes.  This is different from the rest of the W11CategoryAdapters which listens
	// to the Definition object.
	private void addListenerToTypes(W11CategoryAdapter category) {
		Definition def = (Definition) this.getTarget();
		Types types = def.getETypes();
		if (types != null && !types.eAdapters().contains(category)) {
			types.eAdapters().add(category);
		}	
	}
	
	public String getTargetNamespace() {
		return ((Definition) target).getTargetNamespace();
	}
	
	public String getTargetNamespacePrefix() {
		String tns = getTargetNamespace();
		return ((Definition) target).getPrefix(tns);
	}
	
	public List getServices() {
		List adapterList = new ArrayList();
		ComponentReferenceUtil util = new ComponentReferenceUtil((Definition) getTarget());
		List services = util.getServices();
		populateAdapterList(services, adapterList);
		
		return adapterList;
	}

	public List getBindings() {
		List adapterList = new ArrayList();
		ComponentReferenceUtil util = new ComponentReferenceUtil((Definition) getTarget());
		List bindings = util.getBindings();
		populateAdapterList(bindings, adapterList);

		return adapterList;
	}

	public List getInterfaces() {
		List adapterList = new ArrayList();
		ComponentReferenceUtil util = new ComponentReferenceUtil((Definition) getTarget());
		List portTypes = util.getPortTypes();
		populateAdapterList(portTypes, adapterList);

		return adapterList;
	}
	
	public List getImports() {
		List adapterList = new ArrayList();
		populateAdapterList(((Definition) target).getEImports(), adapterList);

		return adapterList;
	}
	
	public List getTypes() {
		List adapterList = new ArrayList();
		Types types = ((Definition) target).getETypes();
        if (types != null)
        {  
		  populateAdapterList(types.getSchemas(), adapterList);
        }  
		return adapterList;
	}
	
	public List getMessages() {
		List adapterList = new ArrayList();
		ComponentReferenceUtil util = new ComponentReferenceUtil((Definition) getTarget());
		List messages = util.getMessages();
		populateAdapterList(messages, adapterList);

		return adapterList;
	}
	
	public String getName() {
		String name = ""; //$NON-NLS-1$
		
		Definition definition = ((Definition) target);
		if (definition.getQName() != null) {
			name = definition.getQName().getLocalPart();
		}
		return name;
	}
	
	public String[] getActions(Object object) {
		String[] actionIDs = new String[3];
		actionIDs[0] = ASDAddServiceAction.ID;
		actionIDs[1] = ASDAddBindingAction.ID;
		actionIDs[2] = ASDAddInterfaceAction.ID;
		
		return actionIDs;
	}
	
	public Command getAddImportCommand() {
		return new W11AddImportCommand((Definition) target);
	}
	
	public Command getAddSchemaCommand() {
		return new W11AddSchemaCommand((Definition) target);
	}
	
	public Command getAddServiceCommand() {
		return new W11AddServiceCommand((Definition) target);
	}
	
	public Command getAddBindingCommand() {
		return new W11AddBindingCommand((Definition) target);
	}
	
	public Command getAddInterfaceCommand() {
		return new W11AddInterfaceCommand((Definition) target);	
	}
	
	public Command getAddMessageCommand() {
		return new W11AddMessageCommand((Definition) target);	
	}
	
	public Command getEditNamespacesCommand() {
		return new W11EditNamespacesCommand((Definition) target);
	}
	
	public List getNamespacesInfo() {
		DOMNamespaceInfoManager namespaceInfoManager = new DOMNamespaceInfoManager();
		Element element = WSDLEditorUtil.getInstance().getElementForObject((Definition) target);
		return namespaceInfoManager.getNamespaceInfoList(element);
	}
	
	public Image getImage() {
		return null;
	}
	
	public String getText() {
		return "definition"; //$NON-NLS-1$
	}
	
	public ITreeElement[] getChildren() {
		List children = getCategoryAdapters();
		return (ITreeElement[]) children.toArray(new ITreeElement[0]);
	}
	
	public boolean hasChildren() {
		if (getChildren().length > 0) {
			return true;
		}
		
		return false;
	}
	
	public ITreeElement getParent() {
		return null;
	}
	
	public void notifyChanged(final Notification msg) {
		// TODO: rmah: This code should be moved into W11CategoryAdapter
		class CategoryNotification extends NotificationImpl {
			protected Object category;
			
			public CategoryNotification(Object category) {
				super(msg.getEventType(), msg.getOldValue(), msg.getNewValue(), msg.getPosition());
				this.category = category;
			}
			
			public Object getNotifier() {
				return category;
			}
			
			public Object getFeature() {
				return msg.getFeature();
			}
		}

		if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EImports()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.IMPORTS);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getImports());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		else if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_ETypes()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.TYPES);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getImports());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		else if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EServices()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.SERVICES);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getServices());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		else if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EBindings()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.BINDINGS);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getBindings());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		else if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EPortTypes()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.INTERFACES);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getInterfaces());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		else if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EMessages()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.MESSAGES);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getMessages());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		else {
			notifyListeners(null, null);
		}
	}
}

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
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddServiceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;

public class W11Description extends WSDLBaseAdapter implements IDescription {
	protected W11CategoryAdapter getCategory(int categoryId) {
		Iterator it = getCategoryAdapters().iterator();
		while (it.hasNext()) {
			W11CategoryAdapter adapter = (W11CategoryAdapter) it.next();
			if (adapter.getGroupType() == categoryId) {
				return adapter;
			}
		}
		
		return null;
	}
	
	private List getCategoryAdapters() {
		return createCategoryAdapters();
	}
	
	protected List createCategoryAdapters() {
		List categories = new ArrayList();
		
		List importList = getImports();
		List schemaList = getTypes();
		List serviceList = getServices();
		List bindingList = getBindings();
		List interfaceList = getInterfaces();
		List messageList = getMessages();

		String categoryTitle = W11CategoryAdapter.IMPORTS_HEADER_TEXT;
		Image categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/importheader_obj.gif"); //$NON-NLS-1$
		W11CategoryAdapter category = new W11CategoryAdapter(this, categoryTitle, categoryImage, importList, W11CategoryAdapter.IMPORTS);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.TYPES_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/types_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, schemaList, W11CategoryAdapter.TYPES);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.SERVICE_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/serviceheader_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, serviceList, W11CategoryAdapter.SERVICES);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.BINDING_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/bindingheader_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, bindingList, W11CategoryAdapter.BINDINGS);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.INTERFACE_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/porttypeheader_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, interfaceList, W11CategoryAdapter.INTERFACES);
		categories.add(category);
		
		categoryTitle = W11CategoryAdapter.MESSAGE_HEADER_TEXT;
		categoryImage = WSDLEditorPlugin.getInstance().getImage("icons/messageheader_obj.gif"); //$NON-NLS-1$
		category = new W11CategoryAdapter(this, categoryTitle, categoryImage, messageList, W11CategoryAdapter.MESSAGES);
		categories.add(category);
		
		return categories;
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
		populateAdapterList(types.getSchemas(), adapterList);

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
	
	public Image getImage() {
		return null;
	}
	
	public String getText() {
		return "description";
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
		if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_ETypes()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.IMPORTS);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getImports());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EServices()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.SERVICES);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getServices());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EBindings()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.BINDINGS);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getBindings());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EPortTypes()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.INTERFACES);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getInterfaces());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
		if (msg.getFeature() == WSDLPackage.eINSTANCE.getDefinition_EMessages()) {
			W11CategoryAdapter adapter = getCategory(W11CategoryAdapter.MESSAGES);
			Assert.isTrue(adapter != null);
			adapter.setChildren(getMessages());
			notifyListeners(new CategoryNotification(adapter), adapter.getText());
		}
	}
}

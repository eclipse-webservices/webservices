/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.ui;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.ui.internal.impl.DOMItemPropertyProvider;
import org.eclipse.jst.ws.jaxws.dom.ui.internal.plugin.DomUi;

/**
 * This is the item provider adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class IWebServiceItemProvider
	extends IJavaWebServiceElementItemProvider
	implements	
		IEditingDomainItemProvider,	
		IStructuredItemContentProvider,	
		ITreeItemContentProvider,	
		IItemLabelProvider,	
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IWebServiceItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		
		if (itemPropertyDescriptors != null) {
			return itemPropertyDescriptors;
		}

		super.getPropertyDescriptors(object);

		addServiceEndpointPropertyDescriptor(object);
		addTargetNamespacePropertyDescriptor(object);
		addPortNamePropertyDescriptor(object);
		addWsdlLocationPropertyDescriptor(object);
			
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Service Endpoint feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addServiceEndpointPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebService_serviceEndpoint_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebService_serviceEndpoint_feature", "_UI_IWebService_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_SERVICE__SERVICE_ENDPOINT,
				 false,
				 false,
				 true,
				 null,
				 getNameCategory(),
				 null));
	}

	/**
	 * This adds a property descriptor for the Target Namespace feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addTargetNamespacePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new DOMItemPropertyProvider
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebService_targetNamespace_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebService_targetNamespace_feature", "_UI_IWebService_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_SERVICE__TARGET_NAMESPACE,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.TEXT_VALUE_IMAGE,
				 getNameCategory(),
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addPortNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new DOMItemPropertyProvider
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebService_portName_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebService_portName_feature", "_UI_IWebService_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_SERVICE__PORT_NAME,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.TEXT_VALUE_IMAGE,
				 getNameCategory(),
				 null));
	}

	/**
	 * This adds a property descriptor for the Wsdl Location feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addWsdlLocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebService_wsdlLocation_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebService_wsdlLocation_feature", "_UI_IWebService_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_SERVICE__WSDL_LOCATION,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.TEXT_VALUE_IMAGE,
				 getNameCategory(),
				 null));
	}

	/**
	 * This returns IWebService.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, Images.INSTANCE.getImage(Images.IMG_WEB_SERVICE));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public String getText(Object object) {
		String label = ((IWebService)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_IWebService_type") : //$NON-NLS-1$
			label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);
		
		switch (notification.getFeatureID(IWebService.class)) {
		case DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
			return;
		}
		
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return DomUi.INSTANCE;
	}
	
	@Override
	public String getNameCategory()
	{
		return getString("_UI_WebServiceAnnotationCategory"); //$NON-NLS-1$
	}
}

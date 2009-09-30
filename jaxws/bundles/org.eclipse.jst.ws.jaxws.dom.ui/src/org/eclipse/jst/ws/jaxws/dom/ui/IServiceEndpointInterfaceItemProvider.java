/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.ui.internal.impl.DOMItemPropertyProvider;
import org.eclipse.jst.ws.jaxws.dom.ui.internal.plugin.DomUi;

/**
 * This is the item provider adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class IServiceEndpointInterfaceItemProvider
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
	public IServiceEndpointInterfaceItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) 
	{
		itemPropertyDescriptors = null;
		
		super.getPropertyDescriptors(object);

		addImplicitPropertyDescriptor(object);
		addImplementingWebServicesPropertyDescriptor(object);
		addTargetNamespacePropertyDescriptor(object);
		addSoapBindingStylePropertyDescriptor(object);
		addSoapBindingUsePropertyDescriptor(object);
		addSoapBindingParameterStylePropertyDescriptor(object);
		
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Implicit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addImplicitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IServiceEndpointInterface_implicit_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IServiceEndpointInterface_implicit_feature", "_UI_IServiceEndpointInterface_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Implementing Web Services feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addImplementingWebServicesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IServiceEndpointInterface_implementingWebServices_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IServiceEndpointInterface_implementingWebServices_feature", "_UI_IServiceEndpointInterface_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES,
				 false,
				 false,
				 true,
				 null,
				 null,
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
				 getString("_UI_IServiceEndpointInterface_targetNamespace_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IServiceEndpointInterface_targetNamespace_feature", "_UI_IServiceEndpointInterface_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.TEXT_VALUE_IMAGE,
				 getNameCategory(),
				 null));
	}

	/**
	 * This adds a property descriptor for the Soap Binding Style feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addSoapBindingStylePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IServiceEndpointInterface_soapBindingStyle_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IServiceEndpointInterface_soapBindingStyle_feature", "_UI_IServiceEndpointInterface_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 getString("_UI_IServiceEndpointInterface_soapBinding"), //$NON-NLS-1$
				 null));
	}

	/**
	 * This adds a property descriptor for the Soap Binding Use feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addSoapBindingUsePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IServiceEndpointInterface_soapBindingUse_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IServiceEndpointInterface_soapBindingUse_feature", "_UI_IServiceEndpointInterface_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 getString("_UI_IServiceEndpointInterface_soapBinding"), //$NON-NLS-1$
				 null));
	}

	/**
	 * This adds a property descriptor for the Soap Binding Parameter Style feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addSoapBindingParameterStylePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IServiceEndpointInterface_soapBindingParameterStyle_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IServiceEndpointInterface_soapBindingParameterStyle_feature", "_UI_IServiceEndpointInterface_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 getString("_UI_IServiceEndpointInterface_soapBinding"), //$NON-NLS-1$
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns IServiceEndpointInterface.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, Images.INSTANCE.getImage(Images.IMG_SEI));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public String getText(Object object) {
		String label = ((IServiceEndpointInterface)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_IServiceEndpointInterface_type") : //$NON-NLS-1$
			label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(IServiceEndpointInterface.class)) {
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT:
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE:
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE:
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE:
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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

		newChildDescriptors.add
			(createChildParameter
				(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS,
				 DomFactory.eINSTANCE.createIWebMethod()));
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

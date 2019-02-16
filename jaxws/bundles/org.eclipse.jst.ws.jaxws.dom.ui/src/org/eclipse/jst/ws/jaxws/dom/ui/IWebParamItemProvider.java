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
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.jdt.core.Signature;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.PropertyStateAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.ui.internal.impl.DOMItemPropertyProvider;
import org.eclipse.jst.ws.jaxws.dom.ui.internal.plugin.DomUi;

/**
 * This is the item provider adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class IWebParamItemProvider
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
	public IWebParamItemProvider(AdapterFactory adapterFactory) {
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

		addKindPropertyDescriptor(object);
		addTypeNamePropertyDescriptor(object);
		addPartNamePropertyDescriptor(object);
		addTargetNamespacePropertyDescriptor(object);
		addHeaderPropertyDescriptor(object);
		
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Kind feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addKindPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebParam_kind_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebParam_kind_feature", "_UI_IWebParam_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_PARAM__KIND,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 getNameCategory(),
				 null));
	}

	/**
	 * This adds a property descriptor for the Type Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addTypeNamePropertyDescriptorOld(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebParam_typeName_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebParam_typeName_feature", "_UI_IWebParam_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_PARAM__TYPE_NAME,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}
	
	protected void addTypeNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new ItemPropertyDescriptor(
					((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
					getResourceLocator(),
					getString("_UI_IWebParam_typeName_feature"), //$NON-NLS-1$
					getString("_UI_PropertyDescriptor_description", "_UI_IWebParam_typeName_feature", "_UI_IWebParam_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					DomPackage.Literals.IWEB_PARAM__TYPE_NAME,
					false,
					false,
					false,
					ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
					null,
					null) 
			{
				@Override
				protected Object getValue(EObject object, EStructuralFeature feature) 
				{
					String typeName = null;
					
					final Object typeSignature = super.getValue(object, feature);
					if (typeSignature != null)
					{						
						typeName = Signature.toString(typeSignature.toString());						
					}
					
					return typeName;
				}
			});
	}

	/**
	 * This adds a property descriptor for the Part Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addPartNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new DOMItemPropertyProvider
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebParam_partName_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebParam_partName_feature", "_UI_IWebParam_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_PARAM__PART_NAME,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 getNameCategory(),
				 null));
	}

	/**
	 * This adds a property descriptor for the Target Namespace feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addTargetNamespacePropertyDescriptor(Object object) {
		final IPropertyState state = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(object, IPropertyState.class);
		itemPropertyDescriptors.add
			(new DOMItemPropertyProvider
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebParam_targetNamespace_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebParam_targetNamespace_feature", "_UI_IWebParam_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE,
				 (state==null) ? false : state.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE),
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 getNameCategory(),
				 null));
	}

	/**
	 * This adds a property descriptor for the Header feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addHeaderPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWebParam_header_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_IWebParam_header_feature", "_UI_IWebParam_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DomPackage.Literals.IWEB_PARAM__HEADER,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 getNameCategory(),
				 null));
	}

	/**
	 * This returns Web_Param.gif.
	 */
	@Override
	public Object getImage(Object object) 
	{
		String imageName = Images.IMG_WEB_PARAM_IN;
		
		if ((object instanceof IWebParam) && ((IWebParam) object).getImplementation().equals("return")) //$NON-NLS-1$
		{ 
			imageName = Images.IMG_WEB_PARAM_OUT;
		}
		
		return overlayImage(object, Images.INSTANCE.getImage(imageName));
	}

	/**
	 * This returns the label text for the adapted class.
	 */
	@Override
	public String getText(Object object) 
	{	
		return ((IWebParam)object).getName();
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

		switch (notification.getFeatureID(IWebParam.class)) {
			case DomPackage.IWEB_PARAM__KIND:
			case DomPackage.IWEB_PARAM__TYPE_NAME:
			case DomPackage.IWEB_PARAM__PART_NAME:
			case DomPackage.IWEB_PARAM__TARGET_NAMESPACE:
			case DomPackage.IWEB_PARAM__HEADER:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
		return getString("_UI_WebParamAnnotationCategory"); //$NON-NLS-1$
	}	
}

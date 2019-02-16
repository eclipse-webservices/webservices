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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import org.eclipse.jst.ws.jaxws.dom.runtime.util.DomAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DomItemProviderAdapterFactory extends DomAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IDOMItemProvider idomItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIDOMAdapter() {
		if (idomItemProvider == null) {
			idomItemProvider = new IDOMItemProvider(this);
		}

		return idomItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IJavaWebServiceElementItemProvider iJavaWebServiceElementItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIJavaWebServiceElementAdapter() {
		if (iJavaWebServiceElementItemProvider == null) {
			iJavaWebServiceElementItemProvider = new IJavaWebServiceElementItemProvider(this);
		}

		return iJavaWebServiceElementItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IServiceEndpointInterfaceItemProvider iServiceEndpointInterfaceItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIServiceEndpointInterfaceAdapter() {
		if (iServiceEndpointInterfaceItemProvider == null) {
			iServiceEndpointInterfaceItemProvider = new IServiceEndpointInterfaceItemProvider(this);
		}

		return iServiceEndpointInterfaceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWebMethodItemProvider iWebMethodItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIWebMethodAdapter() {
		if (iWebMethodItemProvider == null) {
			iWebMethodItemProvider = new IWebMethodItemProvider(this);
		}

		return iWebMethodItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWebParamItemProvider iWebParamItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIWebParamAdapter() {
		if (iWebParamItemProvider == null) {
			iWebParamItemProvider = new IWebParamItemProvider(this);
		}

		return iWebParamItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWebServiceItemProvider iWebServiceItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIWebServiceAdapter() {
		if (iWebServiceItemProvider == null) {
			iWebServiceItemProvider = new IWebServiceItemProvider(this);
		}

		return iWebServiceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWebServiceProjectItemProvider iWebServiceProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIWebServiceProjectAdapter() {
		if (iWebServiceProjectItemProvider == null) {
			iWebServiceProjectItemProvider = new IWebServiceProjectItemProvider(this);
		}

		return iWebServiceProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebType} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWebTypeItemProvider iWebTypeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIWebTypeAdapter() {
		if (iWebTypeItemProvider == null) {
			iWebTypeItemProvider = new IWebTypeItemProvider(this);
		}

		return iWebTypeItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (idomItemProvider != null) idomItemProvider.dispose();
		if (iJavaWebServiceElementItemProvider != null) iJavaWebServiceElementItemProvider.dispose();
		if (iServiceEndpointInterfaceItemProvider != null) iServiceEndpointInterfaceItemProvider.dispose();
		if (iWebMethodItemProvider != null) iWebMethodItemProvider.dispose();
		if (iWebParamItemProvider != null) iWebParamItemProvider.dispose();
		if (iWebServiceItemProvider != null) iWebServiceItemProvider.dispose();
		if (iWebServiceProjectItemProvider != null) iWebServiceProjectItemProvider.dispose();
		if (iWebTypeItemProvider != null) iWebTypeItemProvider.dispose();
	}

}

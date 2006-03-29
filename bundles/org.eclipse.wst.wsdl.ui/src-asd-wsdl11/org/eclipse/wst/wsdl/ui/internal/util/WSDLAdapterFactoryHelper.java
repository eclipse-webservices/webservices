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
package org.eclipse.wst.wsdl.ui.internal.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLAdapterFactory;

public class WSDLAdapterFactoryHelper {
	private static WSDLAdapterFactoryHelper instance;
	
	private AdapterFactory adapterFactory;
	
	public static WSDLAdapterFactoryHelper getInstance() {
		if (instance == null) {
			instance = new WSDLAdapterFactoryHelper();
		}
		
		return instance;
	}
	
	public Adapter adapt(Notifier target) {
		AdapterFactory factory = getWSDLAdapterFactory();
		return factory.adapt(target, factory);
	}
	
	public AdapterFactory getWSDLAdapterFactory() {
		if (adapterFactory == null) {
			adapterFactory = new WSDLAdapterFactory();
		}
		return adapterFactory;
	}
	
	public void setWSDLAdapterFactory(AdapterFactory factory) {
		adapterFactory = factory;
	}
}

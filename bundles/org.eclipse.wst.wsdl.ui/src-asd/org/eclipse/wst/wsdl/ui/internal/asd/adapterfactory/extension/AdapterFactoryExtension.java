/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.adapterfactory.extension;

import org.eclipse.emf.common.notify.AdapterFactory;

public class AdapterFactoryExtension {
	private String namespace;
	private String adapterFactoryClassName;
	private ClassLoader classLoader;
	
	public AdapterFactoryExtension(String namespace) {
		this.namespace = namespace;
	}
	
	public void setAdapterFactoryClassName(String factoryClassName) {
		adapterFactoryClassName = factoryClassName;
	}
	
	public void setClassLoader(ClassLoader classLoader){
	    this.classLoader = classLoader;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public AdapterFactory getAdapterFactory() {
		AdapterFactory factory = null;
		if (adapterFactoryClassName != null) {
			try {
				Class theClass = classLoader != null ? classLoader.loadClass(adapterFactoryClassName) : Class.forName(adapterFactoryClassName);
				factory = (AdapterFactory) theClass.newInstance();
			}
			catch (Exception e) {
			}
		}

		return factory;
	}
}

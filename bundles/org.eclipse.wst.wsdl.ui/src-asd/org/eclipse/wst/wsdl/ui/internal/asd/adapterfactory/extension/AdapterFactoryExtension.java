/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.adapterfactory.extension;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.osgi.framework.Bundle;

public class AdapterFactoryExtension {
	private String namespace;
	private String adapterFactoryClassName;
	private Bundle bundleContributor;
	
	public AdapterFactoryExtension(String namespace) {
		this.namespace = namespace;
	}
	
	public void setAdapterFactoryClassName(String factoryClassName) {
		adapterFactoryClassName = factoryClassName;
	}
	
	public void setClassLoader(ClassLoader classLoader){
	    // unused
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setBundleContributor(Bundle bundleContributor) {
		this.bundleContributor = bundleContributor;
	}
	
	public AdapterFactory getAdapterFactory() {
		AdapterFactory factory = null;
		if (adapterFactoryClassName != null) {
			try {
				Class theClass = bundleContributor != null ? bundleContributor.loadClass(adapterFactoryClassName) : Class.forName(adapterFactoryClassName);
				factory = (AdapterFactory) theClass.newInstance();
			}
			catch (Exception e) {
			}
		}

		return factory;
	}
}

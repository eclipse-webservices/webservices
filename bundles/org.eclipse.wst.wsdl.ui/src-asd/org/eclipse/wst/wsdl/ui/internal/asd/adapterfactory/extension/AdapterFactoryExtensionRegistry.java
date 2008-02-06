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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AdapterFactoryExtensionRegistry {
	protected List registeredAdapterFactories = new ArrayList();

	public void add(AdapterFactoryExtension factory) {
		registeredAdapterFactories.add(factory);
	}
	
	public List getAdapterFactoryNamespaces() {
		List list = new ArrayList();
		for (Iterator i = registeredAdapterFactories.iterator(); i.hasNext();) {
			AdapterFactoryExtension extension = (AdapterFactoryExtension) i.next();
			list.add(extension.getNamespace());
		}
		return list;
	}
	
	public AdapterFactoryExtension getExtensionForNamespace(String namespace) {
		AdapterFactoryExtension result = null;
		if (namespace != null) {
			for (Iterator i = registeredAdapterFactories.iterator(); i.hasNext();) {
				AdapterFactoryExtension extension = (AdapterFactoryExtension) i.next();
				if (namespace.equals(extension.getNamespace())) {
					result = extension;
					break;
				}
			}
		}

		return result;
	}
}
/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContentGeneratorUIExtensionRegistry {
	protected List registeredContentGeneratorUIList = new ArrayList();

	public void add(ContentGeneratorUIExtension extension) {
		registeredContentGeneratorUIList.add(extension);
	}
	
	public List getBindingExtensionNames() {
		List list = new ArrayList();
		for (Iterator i = registeredContentGeneratorUIList.iterator(); i.hasNext();) {
			ContentGeneratorUIExtension extension = (ContentGeneratorUIExtension) i.next();
			list.add(extension.getName());
		}
		return list;
	}

	public ContentGeneratorUIExtension getExtensionForNamespace(String namespace) {
		ContentGeneratorUIExtension result = null;
		if (namespace != null) {
			for (Iterator i = registeredContentGeneratorUIList.iterator(); i.hasNext();) {
				ContentGeneratorUIExtension extension = (ContentGeneratorUIExtension) i.next();
				if (namespace.equals(extension.getNamespace())) {
					result = extension;
					break;
				}
			}
		}

		return result;
	}

	public ContentGeneratorUIExtension getExtensionForName(String name) {
		ContentGeneratorUIExtension result = null;
		if (name != null) {
			for (Iterator i = registeredContentGeneratorUIList.iterator(); i.hasNext();) {
				ContentGeneratorUIExtension extension = (ContentGeneratorUIExtension) i.next();
				if (name.equals(extension.getName())) {
					result = extension;
					break;
				}
			}
		}

		return result;
	}
}

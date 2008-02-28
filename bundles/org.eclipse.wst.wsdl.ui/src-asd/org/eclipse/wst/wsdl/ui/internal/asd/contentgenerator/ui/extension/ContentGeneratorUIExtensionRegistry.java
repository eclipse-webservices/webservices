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
package org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;

public class ContentGeneratorUIExtensionRegistry {
	protected List registeredContentGeneratorUIList = new ArrayList();

	public void add(ContentGeneratorUIExtension extension) {
		registeredContentGeneratorUIList.add(extension);
	}

	/**
	 * Get the default binding based on the project
	 * 
	 * @param project
	 * @return String binding id
	 */
	public String getDefaultBinding(IProject project) {
		// Default hard-code to SOAP
		String defaultBinding = "SOAP"; //$NON-NLS-1$
		/*
		 * TODO: Determine default binding to use based on project's wsi compliance
		 * preference.
		 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=220653
		 */
		return defaultBinding;
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

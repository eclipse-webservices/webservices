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

import org.eclipse.wst.wsdl.ui.internal.wizards.ContentGeneratorOptionsPage;

public class ContentGeneratorUIExtension {
	private String name;
	private String namespace;
	private String bindingOptionsPageClassName;
	private String portOptionsPageClassName;
	
	private ClassLoader classLoader;
	
	public ContentGeneratorUIExtension(String name, String namespace) {
		this.name = name;
		this.namespace = namespace;
	}

	public void setBindingOptionsPageClassName(String bindingClassName) {
		bindingOptionsPageClassName = bindingClassName;
	}

	public void setPortOptionsPageClassName(String portClassName) {
		portOptionsPageClassName = portClassName;
	}
	
	public void setClassLoader(ClassLoader classLoader){
	    this.classLoader = classLoader;
	}
	
	public String getName() {
		return name;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public ContentGeneratorOptionsPage getBindingContentGeneratorOptionsPage() {
		ContentGeneratorOptionsPage result = null;
		if (bindingOptionsPageClassName != null) {
			try {
				Class theClass = classLoader != null ? classLoader.loadClass(bindingOptionsPageClassName) : Class.forName(bindingOptionsPageClassName);
				result = (ContentGeneratorOptionsPage) theClass.newInstance();
			}
			catch (Exception e) {
			}
		}

		return result;
	}
	
	public ContentGeneratorOptionsPage getPortContentGeneratorOptionsPage() {
		ContentGeneratorOptionsPage result = null;
		if (portOptionsPageClassName != null) {
			try {
				Class theClass = classLoader != null ? classLoader.loadClass(portOptionsPageClassName) : Class.forName(portOptionsPageClassName);
				result = (ContentGeneratorOptionsPage) theClass.newInstance();
			}
			catch (Exception e) {
			}
		}

		return result;
	}
}
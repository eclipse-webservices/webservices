/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080715   240722 makandre@ca.ibm.com - Andrew Mak, Cannot setup TCP/IP Monitor for soap12 endpoints
 *******************************************************************************/

package org.eclipse.wst.ws.internal.monitor;

import java.util.ArrayList;
import java.util.List;

import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;

public class ExtensibilityElementTransformerRegistry {

	public static final ExtensibilityElementTransformerRegistry INSTANCE =
		new ExtensibilityElementTransformerRegistry();
	
	private List<IExtensibilityElementTransformer> transformers;
	
	private ExtensibilityElementTransformerRegistry() {
		
		transformers = new ArrayList<IExtensibilityElementTransformer>();
		
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement[] configs = 
			extensionRegistry.getConfigurationElementsFor("org.eclipse.wst.ws.ExtensibilityElementTransformer");
		
		for (IConfigurationElement config : configs) {
			
			try {
				IExtensibilityElementTransformer transformer = (IExtensibilityElementTransformer) 
					config.createExecutableExtension("class");			
			
				transformers.add(transformer);
			}
			catch (CoreException e) {
				WSPlugin.getInstance().getLog().log(e.getStatus());
			}
		}
	}
	
	public String transform(ExtensibilityElement element) {
		
		for (IExtensibilityElementTransformer transformer : transformers) {
			String str = transformer.transform(element);
			if (str != null)
				return str;			
		}

		return null;
	}
}

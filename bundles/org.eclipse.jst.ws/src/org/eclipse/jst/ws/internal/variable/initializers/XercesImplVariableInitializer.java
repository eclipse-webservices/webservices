/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.variable.initializers;

import java.net.URL;

import org.eclipse.core.runtime.IPluginDescriptor;


public class XercesImplVariableInitializer extends WebServiceClasspathVariableInitializer {
	public static final String XERCES_IMPL_VARIABLE = "XERCES_IMPL_JAR"; //$NON-NLS-1$
	public static final String XERCES_IMPL_JAR = "xercesImpl.jar"; //$NON-NLS-1$
	/**
	 * 
	 */
	public XercesImplVariableInitializer() {
		super();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.ClasspathVariableInitializer#initialize(java.lang.String)
	 */
	public void initialize(String variable) {
		IPluginDescriptor descr = getXercesPluginDescriptor();
		if (descr == null)
			return;
		URL installURL = descr.getInstallURL();
		//setup the xercesImpl.jar variable
		URL varURL = appendURL(installURL, XERCES_IMPL_JAR);
		if (varURL != null) 
			setClasspathVariable(XERCES_IMPL_VARIABLE, createPath(varURL));
	}
}

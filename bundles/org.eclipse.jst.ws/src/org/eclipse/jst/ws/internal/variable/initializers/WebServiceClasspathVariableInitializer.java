/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Feb 10, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.jst.ws.internal.variable.initializers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author kathy
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class WebServiceClasspathVariableInitializer
		extends
			ClasspathVariableInitializer {
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.ClasspathVariableInitializer#initialize(java.lang.String)
	 */
	public static final String WEBSERVICE_PLUGIN_ID = "org.eclipse.jst.ws"; //$NON-NLS-1$
	public static final String XERCES_PLUGIN_ID = "org.apache.xerces"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public WebServiceClasspathVariableInitializer() {
		super();
	}
	protected IPluginDescriptor getWebservicesPluginDescriptor() {
		return Platform.getPluginRegistry().getPluginDescriptor(WEBSERVICE_PLUGIN_ID);
	}
	protected IPluginDescriptor getXercesPluginDescriptor() {
		return Platform.getPluginRegistry().getPluginDescriptor(XERCES_PLUGIN_ID);
	}
	protected IPath createPath(URL aURL) {
		try {
			return new Path(Platform.asLocalURL(aURL).getFile());
		} catch (IOException e) {
			return null;
		}
	}
	protected URL appendURL(URL aURL, String jarName) {
		try {
			return new URL(aURL, jarName);
		} catch (MalformedURLException e) {
			return null;
		}
	}
	protected void setClasspathVariable(String variable, IPath varPath) {
		if (varPath != null) {
			try {
				JavaCore.setClasspathVariable(variable, varPath, null);
			} catch (JavaModelException e) {
				//Logger.getLogger().logError(e);
			}
		}
	}
	
	protected boolean jarExists(IPath jarPath) {
		if (jarPath != null) {
			File file = jarPath.toFile();
			return file != null && file.exists();
		}
		return false;
	}
}

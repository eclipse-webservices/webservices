/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.jst.ws.internal.consumption.ui.common;

import org.eclipse.core.resources.IProject;

/*
 *  Implementing consumers may consume the webServiceOSGISupport extension point, in order to allow OSGi bundles and applications to be supported by the Web Services Wizard.
 *   
 *  Implementing the extension point and this interface will alter the Web Service wizard language from EAR to OSGi when an OSGi supported project is detected. 
 *  Likewise, the value of the OSGi App field will be stored in ServiceOsgiAppProjectName, rather than in ServiceEarProjectName. 
 */

public interface IWebServiceOSGISupportExtension {

	/**
	 * Return the default name of a composite OSGi app, given a bundle
	 * 'projectName', for example, MyWebProj would return MyWebProj.app; this is
	 * similar to DefaultingUtils.getDefaultEARProjectName(...)
	 */
	public String getDefaultOSGIAppProjectName(String projectName);

	/**
	 * Whether or not a given project is an OSGi project that is supported by
	 * this consuming extension.
	 */
	public boolean isSupportedOSGIProject(IProject project);

}

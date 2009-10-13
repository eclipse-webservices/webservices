/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091013   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;

/**
 * 
 */
public abstract class JAXRSUtils {
	/**
	 * @param webProject
	 * @return IModelProvider
	 */
	public static IModelProvider getModelProvider(IProject webProject) {
		IModelProvider provider = ModelProviderManager
				.getModelProvider(webProject);
		Object webAppObj = provider.getModelObject();
		if (webAppObj == null) {
			return null;
		}
		return provider;
	}
}

/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.util.XpUtil;

/**
 * Singleton factory used to instantiate registered DOM validators. 
 * 
 * @author Georgi Vachkov
 */
public class DomValidatorFactory 
{
	/** The extension point ID */
	public static final String DOM_VALIDATION_XP = "org.eclipse.jst.ws.jaxws.dom.runtime.domValidator"; //$NON-NLS-1$
	
	/** Factory's singleton instance */
	public static final DomValidatorFactory INSTANCE = new DomValidatorFactory();
	
	private DomValidatorFactory() {
		// singleton
	}
	
	/**
	 * Extracts all registered DOM validators.
	 * @return collection of all registered DOM validators
	 */
	public Collection<IDomValidator> getRegisteredValidators()
	{
		final List<IDomValidator> validators = new ArrayList<IDomValidator>();
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(DOM_VALIDATION_XP);
		for (IExtension extension : extensionPoint.getExtensions()) {
			validators.add(((IDomValidator) XpUtil.instantiateImplementation(extension)));
		}
		
		return validators;
	}
}

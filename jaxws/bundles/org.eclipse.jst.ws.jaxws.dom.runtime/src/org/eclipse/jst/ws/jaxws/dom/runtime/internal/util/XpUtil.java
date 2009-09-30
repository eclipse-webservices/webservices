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
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;


/**
 * Utility class for working with extendion points.
 */
public final class XpUtil
{
	private static final String EXTENSION_NOT_PROPERLY_REGISTERED = "ConsumerType extension not properly registered (error in plugin.xml)"; //$NON-NLS-1$

	/**  */
	public static final String XP_IMPLEMENTATION_ELEMENT = "implementation"; //$NON-NLS-1$

	/**  */
	public static final String XP_IMPLEMENTATION_ELEMENT_CLASS_ATTR = "class"; //$NON-NLS-1$

	/**
	 * Creates and returns a new instance of the executable extension.
	 * 
	 * @param ext
	 * @return instance of extention implementation
	 */
	public static Object instantiateImplementation(final IExtension ext)
	{
		for (IConfigurationElement el : ext.getConfigurationElements())
		{
			if (el.getName().equals(XP_IMPLEMENTATION_ELEMENT))
			{
				try
				{
					return el.createExecutableExtension(XP_IMPLEMENTATION_ELEMENT_CLASS_ATTR);
				} catch (final CoreException e)
				{
					logger().logError("Unable to create extension", e); //$NON-NLS-1$
					throw new RuntimeException(e);
				}
			}
		}
		logger().logError(EXTENSION_NOT_PROPERLY_REGISTERED);
		throw new RuntimeException(EXTENSION_NOT_PROPERLY_REGISTERED);
	}

	private static ILogger logger()
	{
		return new Logger();
	}
}

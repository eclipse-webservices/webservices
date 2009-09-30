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
package org.eclipse.jst.ws.jaxws.dom.runtime.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimePlugin;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.util.NamedExtensionInfo;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * 
 * 
 * @author Georgi Hristov I036201
 *
 */
public class WsDOMRuntimeRegistry 
{
	private static final class RuntimeInfo extends NamedExtensionInfo implements IWsDOMRuntimeInfo
	{
		private Map<String, String> projectFacets;
		
		RuntimeInfo(final String pId, final String pLabel, final Map<String, String> projectFacets)
		{
			super(pId, pLabel);
			
			this.projectFacets = projectFacets;
		}
		
		public Map<String,String> getProjectFacets()
		{
			return this.projectFacets;
		}
 	}
	
	/** The ID of the extension point for a supported runtime */
	private static final String RUNTIME_XP = JaxWsDomRuntimePlugin.PLUGIN_ID + ".domruntimes"; //$NON-NLS-1$

	/**	 Error message displayed when extension point not properly defined */
	public static final String EXTENSION_NOT_PROPERLY_REGISTERED = "Registered runtime extension not properly registered (error in plugin.xml)"; //$NON-NLS-1$

	private static final String XP_IMPLEMENTATION_ELEMENT = "implementation"; //$NON-NLS-1$

	private static final String XP_PROJECT_FACET_ELEMENT = "project_facet"; //$NON-NLS-1$
	
	private static final String XP_PROJECT_FACET_ID = "id"; //$NON-NLS-1$
	
	private static final String XP_PROJECT_FACET_VERSION = "version"; //$NON-NLS-1$
	
	private static final String XP_IMPLEMENTATION_ELEMENT_CLASS_ATTR = "class"; //$NON-NLS-1$

	/**
	 * Creates and returns a new instance of the executable extension.
	 * 
	 * @param info the descriptor object built during extension point load
	 * @return instance of runtime extension implementation
	 */
	public static IWsDOMRuntimeExtension instantiateRuntime(final IWsDOMRuntimeInfo info)
	{
		final IExtension ext = findExtension(info);
		
		if(ext==null)
		{
			logger().logError(EXTENSION_NOT_PROPERLY_REGISTERED);
			return null;
		}
			
		for (IConfigurationElement el : ext.getConfigurationElements())
		{
			if (el.getName().equals(XP_IMPLEMENTATION_ELEMENT))
			{
				try
				{
					return (IWsDOMRuntimeExtension) el.createExecutableExtension(XP_IMPLEMENTATION_ELEMENT_CLASS_ATTR);
				} 
				catch (final CoreException e)
				{
					logger().logError("Unable to create extension", e); //$NON-NLS-1$
					return null;
					
				}
				catch (final ClassCastException e)
				{
					logger().logError("Implementation class does not implement proper interface", e); //$NON-NLS-1$
					return null;
				}
			}
		}
		
		logger().logError(EXTENSION_NOT_PROPERLY_REGISTERED);
		throw new RuntimeException(EXTENSION_NOT_PROPERLY_REGISTERED);
	}
	
	/**
	 * Locates the extension point contribution definition for this particular runtime.
	 * 
	 * @param info the descriptor object built during extension point load
	 * @return instance of contribution definition
	 */
	public static IExtension findExtension(final IWsDOMRuntimeInfo info)
	{
		if(info!=null)
		{
			final IExtension[] exts = Platform.getExtensionRegistry().getExtensionPoint(RUNTIME_XP).getExtensions();
		
			for (int i = 0; i < exts.length; i++)
			{
				if (exts[i].getSimpleIdentifier().equals(info.getId()))
				{
					return exts[i];
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the registered runtimes.
	 * 
	 * @return set of information records for the registered runtimes
	 */
	public static Collection<IWsDOMRuntimeInfo> getRegisteredRuntimesInfo()
	{
		final IExtension[] exts = Platform.getExtensionRegistry().getExtensionPoint(RUNTIME_XP).getExtensions();
		final Collection<IWsDOMRuntimeInfo> runtimeDescriptors = new ArrayList<IWsDOMRuntimeInfo>(exts.length);
		
		for (int i = 0; i < exts.length; i++)
		{
			HashMap<String,String> projectFacets = new HashMap<String,String>();
			
			for (IConfigurationElement el : exts[i].getConfigurationElements())
			{
				if (el.getName().equals(XP_PROJECT_FACET_ELEMENT))
				{
					projectFacets.put(el.getAttribute(XP_PROJECT_FACET_ID),el.getAttribute(XP_PROJECT_FACET_VERSION));
				}
			}
			
			
			runtimeDescriptors.add(new RuntimeInfo(exts[i].getSimpleIdentifier(), exts[i].getLabel(), projectFacets));
		}
		
		return runtimeDescriptors;
	}
	
	/**
	 * Returns the runtime descriptor object corresponding to this runtime id.
	 * 
	 * @param runtimeId the identifier of the runtime
	 * @return descriptor object for the needed runtime
	 */
	public static IWsDOMRuntimeInfo getRuntimeInfo(final String runtimeId)
	{
		Iterator<IWsDOMRuntimeInfo> registeredRuntime = getRegisteredRuntimesInfo().iterator();
		
		while(registeredRuntime.hasNext())
		{
			IWsDOMRuntimeInfo runtimeInfo = registeredRuntime.next();
			
			if(runtimeInfo.getId().equals(runtimeId))
			{
				return runtimeInfo;
			}
		}
		
		return null;
	}
	
	private static ILogger logger()
	{
		return new Logger();
	}
}

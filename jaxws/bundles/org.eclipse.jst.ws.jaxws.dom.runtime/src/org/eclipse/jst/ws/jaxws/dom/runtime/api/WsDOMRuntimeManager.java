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
package org.eclipse.jst.ws.jaxws.dom.runtime.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.ws.jaxws.dom.runtime.registry.IWsDOMRuntimeInfo;
import org.eclipse.jst.ws.jaxws.dom.runtime.registry.WsDOMRuntimeRegistry;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * Factory class for instantiating DOM Runtimes and managing those instances.
 * 
 * @author Georgi Hristov
 *
 */
public class WsDOMRuntimeManager implements IWSDOMRuntimeManager
{
	private static WsDOMRuntimeManager instance = new WsDOMRuntimeManager();

	private Map<String,IWsDOMRuntimeExtension> domRuntimes = new HashMap<String,IWsDOMRuntimeExtension>();

	protected WsDOMRuntimeManager()
	{
		// singleton
	}
	
	public static IWSDOMRuntimeManager instance()
	{
		return instance;
	}	
	
	/**
	 * Instantiates the DOM Runtimes if not already.
	 *
	 */
	public synchronized void createDOMRuntimes(final IProgressMonitor monitor)
	{
		for (IWsDOMRuntimeInfo runtimeInfo : WsDOMRuntimeRegistry.getRegisteredRuntimesInfo()) {
			createDOMRuntime(runtimeInfo, monitor);
		}
	}
	
	protected IWsDOMRuntimeExtension createDOMRuntime(final IWsDOMRuntimeInfo runtimeInfo, final IProgressMonitor monitor)
	{
		final IWsDOMRuntimeExtension domRuntime = getDOMRuntime(runtimeInfo);		
		if (domRuntime == null) {
			return null;
		}
		
		try {
			domRuntime.createDOM(monitor);
		} 
		catch (Exception unexpected) {
			logger().logError("Unexpected Exception! Dom from runtime " + runtimeInfo.getName() + " will not be processed!", unexpected); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return domRuntime;
	}
	
	/**
	 * This method should return the DOM runtime instance corresponding to the descriptor object
	 * of type IRuntimeInfo and instantiate such DOM runtime instance if non existent at the 
	 * moment of invocation of this method.
	 * 
	 * @param runtimeInfo - descriptor of the DOM runtime to be instantiated and returned 
	 * @return the DOM runtime instance
	 */
	public IWsDOMRuntimeExtension getDOMRuntime(final IWsDOMRuntimeInfo runtimeInfo)
	{
		if (runtimeInfo == null) {
			return null;
		}
		
		IWsDOMRuntimeExtension domRuntime = domRuntimes.get(runtimeInfo.getId());		
		if (domRuntime == null)
		{
			domRuntime = WsDOMRuntimeRegistry.instantiateRuntime(runtimeInfo);
			if (domRuntime != null) {
				synchronized(domRuntimes) {
					domRuntimes.put(runtimeInfo.getId(), domRuntime);
				}
			}
		}
		
		return domRuntime;
	}
	
	/**
	 * This method should return the DOM runtime instance corresponding to the runtime id
	 * and instantiate such DOM runtime instance if non existent at the 
	 * moment of invocation of this method.
	 * 
	 * @param runtimeId - string identifier of the runtime 
	 * @return the DOM runtime instance
	 */
	public IWsDOMRuntimeExtension getDOMRuntime(final String runtimeId)
	{
		return getDOMRuntime(WsDOMRuntimeRegistry.getRuntimeInfo(runtimeId));
	}
    
    /**
     * 
     * @return a list of all instantiated DOM Runtimes
     */
    public Collection<IWsDOMRuntimeExtension> getDOMRuntimes()
    {
    	if (domRuntimes.size() == 0) 
    	{
	    	final Collection<IWsDOMRuntimeInfo> rtInfos = WsDOMRuntimeRegistry.getRegisteredRuntimesInfo();
	    	for (IWsDOMRuntimeInfo runtimeInfo : rtInfos) {
				getDOMRuntime(runtimeInfo);
			}
    	}
    	
    	return domRuntimes.values();
    }
	
	/**
	 * This method should reload all instances of DOM Runtimes.
	 *
	 */
	public synchronized void reloadDOMRuntimes(final IProgressMonitor monitor)
	{
		synchronized(domRuntimes) {
			domRuntimes = new HashMap<String,IWsDOMRuntimeExtension>();
		}
		
		createDOMRuntimes(monitor);
	}

	private ILogger logger() {
		return new Logger();
	}
}

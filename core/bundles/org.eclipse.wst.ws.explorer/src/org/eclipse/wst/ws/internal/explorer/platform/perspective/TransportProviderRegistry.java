/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransportProvider;

/**
 * This class is responsible for loading the Web Services Explorer's transport provider
 * extensions and instantiating them when needed. 
 */
public class TransportProviderRegistry {

	private static final String PREF_TRANSPORT_PROVIDERS = "TRANSPORT_PROVIDERS";	
	private static final String EXTENSION_NAME           = "wseTransportProvider";
	private static final String SOAP_TRANSPORT_PROVIDER  = "soapTransportProvider";
	private static final String NAMESPACE_URI            = "namespaceURI";
	private static final String TRANSPORT_URI            = "transportURI";
	
	private static TransportProviderRegistry instance = null;
	
	private List preferredIDs        = null; 
	private Map soapProviderElements = null;
	private Map soapProviderCache    = null;
	
	/**
	 * Returns a singleton instance of this TransportProviderRegistry.
	 *  
	 * @return An instance of this class.
	 */
	public synchronized static TransportProviderRegistry getInstance() {
		if (instance == null)
			instance = new TransportProviderRegistry();
		return instance;
	}
	
	/*
	 * Constructor.
	 */
	private TransportProviderRegistry() {		
		
		// get the preference value
		Preferences preferences = ExplorerPlugin.getInstance().getPluginPreferences();
		String transportProviders = preferences.getString(PREF_TRANSPORT_PROVIDERS).replaceAll("\\s", "");
		
		// split it into a list of preferred transport provider IDs
		if (transportProviders.length() == 0)
			preferredIDs = new ArrayList();
		else			
			preferredIDs = Arrays.asList(transportProviders.split(","));
		
		// now find all extenders
		IExtensionRegistry registry = Platform.getExtensionRegistry();
	    IConfigurationElement[] configs = 
	    	registry.getConfigurationElementsFor(ExplorerPlugin.ID, EXTENSION_NAME); 

	    // currently we only have one type of providers: SOAP transport providers
	    soapProviderElements = new Hashtable();	    
	    for (int i = 0; i < configs.length; i++) {	    		    	
    		if (SOAP_TRANSPORT_PROVIDER.equals(configs[i].getName())) {	    			
    			String id = configs[i].getAttribute("id");
    			soapProviderElements.put(id, configs[i]);    			    				
    		}	    		    	
	    }
	    
	    soapProviderCache = new Hashtable();
	}		
	
	/*
	 * Determine if the URIs match.
	 */
	private boolean uriMatch(String required, String supported) {
		
		// not specified mean support everything
		if (supported == null)
			return true;
				
		if (!required.endsWith("/"))  required += "/";
		if (!supported.endsWith("/")) supported += "/";
		
		return required.equals(supported);
	}
	
	/*
	 * Create the extension for the given key, but only if it supports the binding namespace and transport
	 */
	private ISOAPTransportProvider createExtension(Object key, String namespaceURI, String transportURI) {
		
		IConfigurationElement element = (IConfigurationElement) soapProviderElements.get(key);						
		if (element == null)
			return null;
		
		boolean supportsNamespace = uriMatch(namespaceURI, element.getAttribute(NAMESPACE_URI));
		boolean supportsTransport = uriMatch(transportURI, element.getAttribute(TRANSPORT_URI));			
		
		if (supportsNamespace && supportsTransport) {			
			
			String providerClassID = element.getAttribute("class");
			Object obj = null;
			
			try {
				// check if we already have the transport provider cached,
				// if not, then create one
				synchronized (soapProviderCache) {
					obj = soapProviderCache.get(providerClassID); 					
					if (obj == null) {					
						obj = element.createExecutableExtension("class");
						soapProviderCache.put(providerClassID, obj);
					}					
				}
				return (ISOAPTransportProvider) obj;
			}
			catch (CoreException e) {}					
		}
		
		return null;
	}
	
	/**
	 * Get an ISOAPTransportProvider that supports the binding namespace and transport.  This method will
	 * first look for preferred IDs defined using the TRANSPORT_PROVIDERS preference key.  If a suitable
	 * provider is not found, it will then look at all other extenders in non-deterministic order.
	 * 
	 * @param namespaceURI The binding namespace URI
	 * @param transportURI The binding transport URI
	 * 
	 * @return Returns a suitable ISOAPTranportProvider for the given binding information, or null
	 * if there's no match.
	 */
	public ISOAPTransportProvider getSOAPTransportProvider(String namespaceURI, String transportURI) {
	
		ISOAPTransportProvider provider = null;
		
		Iterator iter = preferredIDs.iterator();
		
		// check preferred IDs first
		while (iter.hasNext()) {
			provider = createExtension(iter.next(), namespaceURI, transportURI);
			if (provider != null)
				return provider;
		}
		
		iter = soapProviderElements.keySet().iterator();
		
		// check rest of the providers
		while (iter.hasNext()) {
			Object key = iter.next();
		
			// already checked the preferred IDs
			if (preferredIDs.contains(key))
				continue;
			
			provider = createExtension(key, namespaceURI, transportURI);
			if (provider != null)
				return provider;			
		}
		
		return null;
	}
}

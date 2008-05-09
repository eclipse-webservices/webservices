/*******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080506   202945 pmoogk@ca.ibm.com - Peter Moogk, Allow WSE to be launched from a WSIL file.
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer;

/**
 * @author cbrealey@ca.ibm.com
 * 
 * This class defines the property names understood by the Web Services Explorer
 * and used to configure it's initial appearance and behaviour at the time it is
 * launched.
 * @see LaunchOption
 */
public class LaunchOptions {
	// General purpose preload constants
	
	public static final String STATE_LOCATION = "stateLocation";
	
	public static final String DEFAULT_FAVORITES_LOCATION = "defaultFavoritesLocation";
	
	// WSDL Page preload constants

	public static final String WSDL_URL = "wsdl";
	
	public static final String WSIL_URL = "wsilurl";

	public static final String WEB_SERVICE_ENDPOINT = "webServiceEndpoint";

	public static final String SERVICE_QNAME_STRING = "serviceQNameString";

	public static final String BINDING_NAME_STRING = "bindingNameString";

	// UDDI Page preload constants

	public static final String INQUIRY_URL = "inquiry";

	public static final String PUBLISH_URL = "publish";

	public static final String SERVICE_NAME = "serviceName";

	public static final String SERVICE_KEY = "serviceKey";

	public static final String CATEGORIES_DIRECTORY = "categoriesDirectory";
}

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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence;

final public class WSAnnotationFeatures
{
	private WSAnnotationFeatures()
	{
	}
	public static final String WS_ANNOTATION = "javax.jws.WebService";//$NON-NLS-1$
	public static final String NAME_ATTRIBUTE = "name";//$NON-NLS-1$
	public static final String ENDPOINT_INTERFACE_ATTRIBUTE = "endpointInterface";//$NON-NLS-1$
	public static final String SERVICE_NAME_ATTRIBUTE = "serviceName";//$NON-NLS-1$
	public static final String WSDL_LOCATION_ATTRIBUTE = "wsdlLocation";//$NON-NLS-1$
	public static final String TARGET_NAMESPACE_ATTRIBUTE = "targetNamespace";//$NON-NLS-1$
	public static final String PORT_NAME_ATTRIBUTE = "portName";	//$NON-NLS-1$
}

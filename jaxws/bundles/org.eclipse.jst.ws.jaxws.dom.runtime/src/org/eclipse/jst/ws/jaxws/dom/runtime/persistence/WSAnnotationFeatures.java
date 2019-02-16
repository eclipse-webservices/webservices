/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

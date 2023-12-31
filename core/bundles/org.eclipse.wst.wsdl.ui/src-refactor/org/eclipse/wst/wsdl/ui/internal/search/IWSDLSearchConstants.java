/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.search;

import org.eclipse.wst.common.core.search.pattern.QualifiedName;


public interface IWSDLSearchConstants {

	public static final String WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/"; //$NON-NLS-1$
	public static final String XMLSCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$
	
	public static String WSDL_CONTENT_TYPE_ID = "org.eclipse.wst.wsdl.wsdlsource"; //$NON-NLS-1$

	public static final QualifiedName MESSAGE_META_NAME = new QualifiedName(
			WSDL_NAMESPACE, "message"); //$NON-NLS-1$

	public static final QualifiedName PORT_TYPE_META_NAME = new QualifiedName(
			WSDL_NAMESPACE, "portType"); //$NON-NLS-1$

	public static final QualifiedName BINDING_META_NAME = new QualifiedName(
			WSDL_NAMESPACE, "binding"); //$NON-NLS-1$

	

}

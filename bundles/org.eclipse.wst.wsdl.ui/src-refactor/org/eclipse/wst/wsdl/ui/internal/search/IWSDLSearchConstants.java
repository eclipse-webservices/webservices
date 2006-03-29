/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.search;

import org.eclipse.wst.common.core.search.pattern.QualifiedName;


public interface IWSDLSearchConstants {

	public static final String WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";
	public static final String XMLSCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	
	public static String WSDL_CONTENT_TYPE_ID = "org.eclipse.wst.wsdl.wsdlsource";

	public static final QualifiedName MESSAGE_META_NAME = new QualifiedName(
			WSDL_NAMESPACE, "message");

	public static final QualifiedName PORT_TYPE_META_NAME = new QualifiedName(
			WSDL_NAMESPACE, "portType");

	public static final QualifiedName BINDING_META_NAME = new QualifiedName(
			WSDL_NAMESPACE, "binding");

	

}

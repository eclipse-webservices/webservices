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


/**
 * Class that holds information on javax.jws.WebParam annotation name and attribute names. 
 * 
 * @author Georgi Vachkov
 */
final public class WPAnnotationFeatures 
{
	public static final String WP_ANNOTATION = "javax.jws.WebParam";//$NON-NLS-1$
	public static final String NAME_ATTRIBUTE = "name";//$NON-NLS-1$
	public static final String PART_NAME_ATTRIBUTE = "partName";//$NON-NLS-1$
	public static final String TARGET_NAMESPACE_ATTRIBUTE = "targetNamespace";//$NON-NLS-1$
	public static final String MODE_ATTRIBUTE = "mode";//$NON-NLS-1$
	public static final String HEADER_ATTRIBUTE = "header";//$NON-NLS-1$
	
	public static final String WEB_PARAM_MODE = "javax.jws.WebParam.Mode";//$NON-NLS-1$
	public static final String WEB_PARAM_MODE_IN = WEB_PARAM_MODE + ".IN";//$NON-NLS-1$
	public static final String WEB_PARAM_MODE_OUT = WEB_PARAM_MODE + ".OUT";//$NON-NLS-1$
	public static final String WEB_PARAM_MODE_INOUT = WEB_PARAM_MODE + ".INOUT";//$NON-NLS-1$
}

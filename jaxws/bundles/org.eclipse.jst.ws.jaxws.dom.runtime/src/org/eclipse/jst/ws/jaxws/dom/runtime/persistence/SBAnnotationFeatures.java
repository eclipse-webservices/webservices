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
 * Contains SOAPBinding annotation name, attributes and possible values
 * 
 * @author Georgi Vachkov
 */
public class SBAnnotationFeatures 
{
	public static final String SB_ANNOTATION = "javax.jws.soap.SOAPBinding";//$NON-NLS-1$
	public static final String STYLE_ATTRIBUTE = "style";//$NON-NLS-1$
	public static final String USE_ATTRIBUTE = "use";//$NON-NLS-1$
	public static final String PARAMETER_STYLE_ATTRIBUTE = "parameterStyle";//$NON-NLS-1$
	
	public static final String SB_STYLE_DOCUMENT 	= SB_ANNOTATION + ".Style.DOCUMENT";//$NON-NLS-1$
	public static final String SB_STYLE_RPC 		= SB_ANNOTATION + ".SOAPBinding.Style.RPC";//$NON-NLS-1$
	
	public static final String SB_USE_LITERAL 	= SB_ANNOTATION + ".Use.LITERAL";//$NON-NLS-1$
	public static final String SB_USE_ENCODED 	= SB_ANNOTATION + ".Use.ENCODED";//$NON-NLS-1$
	
	public static final String SB_PARAMETER_STYLE_BARE 		= SB_ANNOTATION + ".ParameterStyle.BARE";//$NON-NLS-1$
	public static final String SB_PARAMETER_STYLE_WRAPPED	= SB_ANNOTATION + ".ParameterStyle.WRAPPED";//$NON-NLS-1$
	
	
	private SBAnnotationFeatures() {
		// no instance of this class allowed
	}
}
